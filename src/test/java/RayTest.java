package testcases;


import classes.Ray;
import classes.Vector3D;
import junit.framework.TestCase;

public class RayTest extends TestCase {
	
	private transient Ray ray1;
	private transient Vector3D vectInterSect;
	

	protected void setUp()throws Exception{
		Vector3D vect1;
		Vector3D vect2;
		vect1 = new Vector3D(1,2,3);
		vect2 = new Vector3D(4,5,6);
		ray1 = new Ray(vect1,vect2);
	    vectInterSect = new Vector3D(9,12,15);
	 }


	/**
	 * tests intersect method of ray class.
	 */
	public void testIntersect(){
		Vector3D tmpVect;
	
		tmpVect = ray1.intersectionPoint(2);
		assertTrue(vectInterSect.equals(tmpVect));
		
	}

}
