package testcases;


import junit.framework.TestCase;


import classes.*;

/**
 * The test case for camera class which will test the Camera constructors and the get ray method.
 * by using different initialization arguments. 
 * @author amin
 *
 */

public class CameraTest extends TestCase  {
	
	public transient Vector3D testPosition;
	public transient Vector3D testLookat;
	public transient Vector3D testUpward;
	public transient Camera testCamera;
	


	
	/**
	 * common variables which will be used in all the test cases are defined here. 
	 */
	public void setUp() throws Exception {
		
		testPosition = new Vector3D (2,2,-5);
		testLookat = new Vector3D (2,2,0);
		testUpward = new Vector3D (0,1,0);
	}


	/**
	 * this method is testing the getRay method of the Camera class with camera located in selected arbitrary 
	 * locations. it will produce a known ray and will compare it with the ray actually produced by the 
	 * camera in an arbitrary position.
	 * the test will fail if these 2 rays are not equal objects.
	 * 
	 */
	
	public void testGetRay() {
		
		final Vector3D testPos = new Vector3D (2,2,-5);
		final Vector3D testDir = new Vector3D (-0.4818,0.4818,0.7319);
		
		final Ray testRay = new Ray(testPos,testDir);
		final Camera testCam = new Camera( testPosition ,testLookat ,testUpward);
		assertTrue(testRay.equals(testCam.getRay(2, 2)));
	}
	

	/**
	 * this method test boundary situation for a Camera class.
	 */
	public void testCameraProperties(){
		testCamera = new Camera(testPosition,testLookat,testUpward,600,
										400,400,400,400);
		assertFalse(testCamera.isValid);
		
		testCamera = new Camera(testPosition,testLookat,testUpward,50,
				3000,400,400,400);
		assertFalse(testCamera.isValid);
		
		testCamera = new Camera(testPosition,testLookat,testUpward,50,
				400,4000,400,400);
		assertFalse(testCamera.isValid);
		
		testCamera = new Camera(testPosition,testLookat,testUpward,50,
				400,400,4000,400);
		assertFalse(testCamera.isValid);
		
		testCamera = new Camera(testPosition,testLookat,testUpward,50,
				400,400,400,4000);
		assertFalse(testCamera.isValid);
	}
	

}
