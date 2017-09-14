package tracer;

import java.util.ArrayList;

import bitmap.Bitmap;
import domain.*;
import materials.SolidMaterial;
import shapes.BaseShape;
import shapes.Plane;
import shapes.Sphere;
import domain.Camera;


import junit.framework.TestCase;

/**
 * @author majid
 * this is a big bitmap class which contains important bitmap cases.
 * within this class we do some integrity tests to make sure
 * that our camera and light source class are working properly
 * during the ray tracing process. further more we bitmap some
 * other ray tracing concepts like reflection, projection and
 * shadowing
 */
public class RayTracerTest extends TestCase {

	Scene testScene;
	ArrayList<BaseShape> testShapes;
	Sphere sphere1,sphere2,sphere3,sphere4;
	ArrayList<Light> testLights;
	Light light1,light2;
	Camera testCamera;
	Plane testPlane;

	/* (non-Javadoc)
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();

		SolidMaterial sMat = new SolidMaterial(
				new Color(150, 2, 20), 0.800, 1.000);
		sphere1= new Sphere(new Vector(-10, 7, 118), 8, sMat);

		sMat = new SolidMaterial(new Color(150, 50, 40), 0.800, 1.000);
		sphere2= new Sphere(new Vector(10, 7, 118), 8, sMat);

		sMat = new SolidMaterial(new Color(100, 58, 58), 0.8, 1.000);
		sphere3 = new Sphere(new Vector(0, 14, 107), 4, sMat);

		sMat = new SolidMaterial(new Color(200, 0, 0), 0, 1.000);
		sphere4 = new Sphere(new Vector(0, 14, 124), 4, sMat);

		sMat = new SolidMaterial(new Color(210, 158, 20), 0, 1.000);
		testPlane = new Plane(150 ,new Vector(-1, 0, 2), sMat);

		light1 = new Light(new Vector(10, 0, 50));
		light2 = new Light(new Vector(-20, 70, 50));


		testShapes = new ArrayList<>();
		testShapes.add(sphere1);
		testShapes.add(sphere2);
		testShapes.add(sphere3);
		testShapes.add(sphere4);
		testShapes.add(testPlane);

		testLights = new ArrayList<>();
		//testLights.add(light1);
		testLights.add(light2);
		//testLights.add(light3);

		testCamera = new Camera(new Vector(-30, 50, 100) ,
				new Vector(-10, 10, 114) ,new Vector(1, 0, 0));

		testScene = new Scene(testCamera, new Color(128,128,128),
				null);
	}

	/**
	 * in this bitmap case first we render a scene with 2 light sources. Then we
	 * add a new light source at the same position of an already existed light
	 * source. this new light source should brighten a pixel which is in front
	 * of it. we get a pixel which stands in front of light sources and compare
	 * its color before and after adding the new light source. if it gets
	 * brighter we pass the bitmap. it shows that lights are correctly rendered
	 * in our project.
	 */
	public void testWithDifferentLights() throws Exception {

		RayTracer testTracer = new RayTracer(false, false);
		final Bitmap viewport = new Bitmap(400,400);
		final Bitmap viewport1 = new Bitmap(400,400);
		Color firstColor = new Color();
		Color secondColor = new Color();

		testTracer.rayTraceScene(testScene, viewport, 5, false, 0, 1);

		Light light3 = new Light(new Vector(-20, 70, 50));
		testScene.getWorld().getLights().add(light3);
		testTracer.rayTraceScene(testScene, viewport1, 5, false, 0, 1);

		try {

			firstColor = viewport.readPixel(214, 200);
			secondColor = viewport1.readPixel(214, 200);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		assertTrue(firstColor.getRed() < secondColor.getRed()
				|| firstColor.getGreen() < secondColor.getGreen()
				|| firstColor.getBlue() < secondColor.getBlue());

	}

	/**
	 * in this bitmap case we trace a known scene. and write the result to a
	 * bitmap file,after that we create a md5sum of the bitmap file as string.
	 * then we change the camera position in scene and re-render the scene with
	 * new camera position and save the result inside a new bitmap file.
	 * again we create the md5sum of new scene and then compare two hashed
	 * string, they should not be equal.
	 * again we set camera position to its initial value and create the bitmap,
	 * calculate md5sum and compare it to original one, these two should be
	 * equal mining that our camera works correctly.
	 */
/*	public void testWithDifferentCameraPosition() throws IOException, InvalidPixelException {

		RayTracer testTracer = new RayTracer();
		final Bitmap viewport = new Bitmap(400,400);
		testTracer.rayTraceScene(testScene, viewport, 5, false,"1",1);

		String bmpFileHash1 = "";
		String bmpFileHash2 = "";
		File bmpFile = new File("testResult.ppm");
		try {
			bmpFileHash1 = MD5Sum.getFileMD5Sum(bmpFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		testScene.camera.setLocation(new Vector(30, -50, -100));
		testTracer.rayTraceScene(testScene, viewport, 5, false ,"1" ,1);
		bmpFile = new File("testResult.ppm");
		try {
			bmpFileHash2 = MD5Sum.getFileMD5Sum(bmpFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		assertFalse(bmpFileHash1.equals(bmpFileHash2));

		testScene.camera.setLocation(new Vector(-30, 50, 100));
		testTracer.rayTraceScene(testScene, viewport, 5, false,"1",1);
		bmpFile = new File("testResult.ppm");
		try {
			bmpFileHash2 = MD5Sum.getFileMD5Sum(bmpFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		assertTrue(bmpFileHash1.equals(bmpFileHash2));

	}*/

}
