package testcases;
import junit.framework.TestCase;

import classes.Vector3D;



public class Vector3DTest extends TestCase {

	private transient Vector3D vect1;
	private transient Vector3D vect2;
	
	private transient Vector3D vectCross;
	private transient Vector3D vectMagnitude;
	private transient Vector3D vectAddition;
	private transient Vector3D vectReduction;
	private transient Vector3D vectMultiply;
	private transient Vector3D normVec;
	

	protected void setUp()throws Exception  {
		 vect1 = new Vector3D(1, 2, 3);
	     vect2 = new Vector3D(4, 5, 6); 
	     
	     vectAddition = new Vector3D(5,7,9);
	     vectReduction = new Vector3D(-3,-3,-3);
	     vectMultiply =new Vector3D(2,4,6);
	     vectCross = new Vector3D(-3,6,-3);
	     vectMagnitude = new Vector3D(1,2,2);
	     normVec = new Vector3D(1,0,0);
	     super.setUp();
	}
	

	/**
	 * tests length of Vector3D vectMagnitude should be 3 
	 */
	public void testMagnitude(){
		assertEquals(3,vectMagnitude.magnitude(),0);
	}
	

	/**
	 * this method tests dotProduct() method from class Vector3D.
	 */
	public void testDotProduct(){
		//setUp();
		assertEquals(32, vect1.dotProduct(vect2), 0);
	}
	
	

	/** 
	 * tests result for cross product of two Vector3Ds vect1 and vect2 should be tmpVect 
	 */
	public void testCrossProduct(){
		Vector3D tmpVect ;
		
		tmpVect = vect1.crossProduct(vect2);
		assertTrue(vectCross.equals(tmpVect));
		
	}
	

	/**
	 * Add two Vector3Ds and test if new Vector3D is equal to vectAddition
	 */
	
	public void testVectorAddition(){
        Vector3D tmpVect ;
		
		tmpVect = vect1.vectorAddition(vect2);
		assertTrue(vectAddition.equals(tmpVect));
	}
	

	/**
	 * Subtract two Vector3Ds and test if new Vector3D is equal to vectReduction
	 */
	public void testVectorReduction(){
        Vector3D tmpVect ;
		tmpVect = vect1.vectorReduction(vect2);
		assertTrue(vectReduction.equals(tmpVect));
	}
	

	/**
	 * multiplies Vector3D vect1 by two and test if new Vector3D is equal to VectMultiply 
	 */
	public void testVectorMultiply(){
        Vector3D tmpVect ;
		
		tmpVect = vect1.vectorMultiply(2);
		assertTrue(vectMultiply.equals(tmpVect));
	}
	

	/**
	 * to test normaliseCheck() method we give a normal Vector3D to this method
	 * and check whether it returns true or not.
	 */
	public void testNormalizeCheck(){
		assertTrue(normVec.normalizeCheck());
		
	}

	/**
	 * this test case tests Vector3D class normalize method by comparing
	 * magnitude of Vector3D resulted from Normalize() method with 1.
	 */
	public void testNormalize(){
		final Vector3D normVec2 = vect1.normalize();
		assertEquals(1, normVec2.magnitude(), 0.05);
	}
}