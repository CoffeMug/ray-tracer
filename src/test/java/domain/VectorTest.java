package domain;

import org.junit.Before;
import org.junit.Test;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class VectorTest {

	private Vector vect1;
	private Vector vect2;
	private Vector vect3;
	private Vector vect1CrossProdVect2;
	private Vector vectMagnitude;
	private Vector vect1PlusVect2;
	private Vector vect1MinusVect2;
	private Vector vect1MultiplyVect2;
	private Vector normVec;

	@Before
	public void setUp() {
		vect1 = new Vector(1, 2, 3);
		vect2 = new Vector(4, 5, 6);
		vect3 = new Vector(1, 2, 3);
		vect1PlusVect2 = new Vector(5,7,9);
		vect1MinusVect2 = new Vector(-3,-3,-3);
		vect1MultiplyVect2 =new Vector(2,4,6);
		vect1CrossProdVect2 = new Vector(-3,6,-3);
		vectMagnitude = new Vector(1,2,2);
		normVec = new Vector(1,0,0);
	}

	@Test
	public void magnitudeOfVector(){
		assertEquals(3, vectMagnitude.magnitude(),0);
	}

	@Test
	public void dotProduct(){
		assertEquals(32, vect1.dotProduct(vect2), 0);
	}

	@Test
	public void crossProduct(){
		Vector tmpVect;

		tmpVect = vect1.crossProduct(vect2);
		assertTrue(vect1CrossProdVect2.equals(tmpVect));
	}

	@Test
	public void vectorAddition(){
		Vector tmpVect ;

		tmpVect = vect1.vectorAddition(vect2);
		assertTrue(vect1PlusVect2.equals(tmpVect));
	}

	@Test
	public void vectorReduction(){
		Vector tmpVect ;
		tmpVect = vect1.vectorReduction(vect2);
		assertTrue(vect1MinusVect2.equals(tmpVect));
	}

	@Test
	public void vectorMultiply(){
		Vector tmpVect ;

		tmpVect = vect1.vectorMultiply(2);
		assertTrue(vect1MultiplyVect2.equals(tmpVect));
	}

	@Test
	public void normalizeCheck(){
		assertTrue(normVec.normalizeCheck());
	}

	@Test
	public void normalize(){
		Vector normVec2 = vect1.normalize();
		assertEquals(1, normVec2.magnitude(), 0.05);
	}

	@Test
	public void notEqul() {
		assertFalse(vect1.equals(vect2));
	}

	@Test
	public void equal() {
		assertTrue(vect1.equals(vect3));
	}
}
