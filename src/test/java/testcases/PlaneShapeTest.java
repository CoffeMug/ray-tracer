package testcases;

import classes.Color;

import junit.framework.TestCase;
import materials.SolidMaterial;

import shapes.PlaneShape;


import classes.IntersectInfo;
import classes.Ray;
import classes.Vector3D;

public class PlaneShapeTest extends TestCase {
	
	private transient Vector3D pVect;
	private transient Vector3D nVect;
	private transient Vector3D roVect;
	private transient Vector3D rdVect;
	private transient SolidMaterial  solid;
	
	
	


	public void setUp() throws Exception {
		pVect  = new Vector3D(4,4,0);
		nVect  = new Vector3D(0,0,2);
		roVect = new Vector3D(2,2,2);
		rdVect = new Vector3D(4,6,8);
		solid = new SolidMaterial(new Color(255,0,0), 0.800, 1.0);
	}

			

	/**
	 * this is the test method for testing if the ray is hitting the plane or not
	 * it will fail if the direction of the ray is not toward the plain or the ray is parallel to the plane
	 */
	public void testIntersect() {
		Ray testRay;
		PlaneShape testPlane;
		
		testRay = new Ray(roVect,rdVect);
		testPlane = new PlaneShape(pVect,nVect,solid);
		final IntersectInfo planeTestInfo = testPlane.intersect(testRay);
		
		assertTrue(planeTestInfo.isHit != null);
		

	}


}
