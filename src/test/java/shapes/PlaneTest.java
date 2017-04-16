package shapes;

import domain.Color;

import domain.IntersectInfo;
import domain.Vector;
import exceptions.InvalidPixelException;
import materials.SolidMaterial;

import org.junit.Before;
import org.junit.Test;

import domain.Ray;

import static junit.framework.TestCase.assertTrue;

public class PlaneTest {

	private transient Vector pVect;
	private transient Vector nVect;
	private transient Vector roVect;
	private transient Vector rdVect;
	private transient SolidMaterial  solid;

	@Before
	public void setUp() {
		pVect  = new Vector(4,4,0);
		nVect  = new Vector(0,0,2);
		roVect = new Vector(2,2,2);
		rdVect = new Vector(4,6,8);
		solid = new SolidMaterial(new Color(255,0,0), 0.800, 1.0);
	}

	@Test
	public void rayHitPlane() throws InvalidPixelException {
		Ray ray = new Ray(roVect, rdVect);
		Plane plane = new Plane(pVect, nVect, solid);
		final IntersectInfo planeTestInfo = plane.intersect(ray);

		assertTrue(planeTestInfo.getIsHit());
	}

	@Test
	public void rayDoesNotHitPlane() throws InvalidPixelException {
		Plane plane = new Plane(pVect, nVect, solid);
		assertTrue(plane.intersect(new Ray(rdVect, roVect)).getIsHit());
	}
}