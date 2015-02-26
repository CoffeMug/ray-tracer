package testcases;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite("Test for testcases");
		//$JUnit-BEGIN$
		suite.addTestSuite(RayTracerTest.class);
        //		suite.addTestSuite(BitmapTest.class);
		suite.addTestSuite(ColorTest.class);
		suite.addTestSuite(CameraTest.class);
		suite.addTestSuite(TriangleShapeTest.class);
		suite.addTestSuite(PlaneShapeTest.class);
		suite.addTestSuite(TextureTest.class);
		suite.addTestSuite(Vector3DTest.class);
        //		suite.addTestSuite(RayTest.class);
		//$JUnit-END$
		return suite;
	}

}
