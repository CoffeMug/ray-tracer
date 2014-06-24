package testcases;


import junit.framework.TestCase;
import classes.*;
import shapes.*;


import materials.SolidMaterial;



public class TriangleShapeTest extends TestCase  {

	private transient Vector3D pVect;
	//private transient Vector3D nVect;
	private transient Vector3D rlVect;
	private transient Vector3D llVect;
	private transient Vector3D wllVect;
	private transient Vector3D wrlVect;
	private transient Vector3D roVect;
//	private transient Vector3D rdVect;
	
	

//	private transient TriangleShape wrongTriangle;
//	private transient PlaneShape testPlane;
	private transient SolidMaterial  solid;

	/**
	 * Initialization of variables needed in testing methods
	 * 
	 */
	public void setUp() throws Exception {
		
		pVect  = new Vector3D(4,4,0);
	//	nVect  = new Vector3D(0,0,2);
		rlVect = new Vector3D(4,0,4);
		llVect = new Vector3D(0,4,4);
		roVect = new Vector3D(0,0,0);
//		rdVect = new Vector3D(-2,2,-2);
		wllVect = new Vector3D(5,5,0);
		wrlVect = new Vector3D(3,3,0);
				
	}



	/**
	 * this method test if the intersection point is computes correctly or not
	 */
	public void testIntersect() {

		Ray testRay;
		TriangleShape testTriangle;
		testTriangle = new TriangleShape(pVect,llVect,rlVect,solid);
		testRay = new Ray(roVect,rlVect.crossProduct(llVect));

		//IntersectInfo planeTestInfo = testPlane.Intersect(testRay);
		final IntersectInfo triangleTestInfo = testTriangle.intersect(testRay);
		
		//assertTrue(planeTestInfo.position.equals(triangleTestInfo.position));
		assertTrue(triangleTestInfo.isHit != null);

	}
	
	/**
	 * this testCase checks to see if triangleTest() method of class 
	 * triangleSahpe works correctly.
	 */
	public void testTriangleTest(){
		
		
		assertFalse(TriangleShape.triangleTest(pVect,wllVect,wrlVect));
		
	}

}
