package tracer;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import bitmap.Bitmap;
import domain.*;
import exceptions.InvalidPixelException;
import materials.SolidMaterial;
import shapes.BaseShape;
import shapes.PlaneShape;
import shapes.SphereShape;
import shapes.TriangleShape;
import scene.Camera;
import utils.MD5Sum;


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
	SphereShape sphere1,sphere2,sphere3,sphere4;
	ArrayList<Light> testLights;
	Light light1,light2;
	Camera testCamera;
	PlaneShape testPlane;

	/* (non-Javadoc)
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();

		SolidMaterial sMat = new SolidMaterial(
				new Color(150, 2, 20), 0.800, 1.000);
		sphere1= new SphereShape(new Vector(-10, 7, 118), 8, sMat);

		sMat = new SolidMaterial(new Color(150, 50, 40), 0.800, 1.000);
		sphere2= new SphereShape(new Vector(10, 7, 118), 8, sMat);

		sMat = new SolidMaterial(new Color(100, 58, 58), 0.8, 1.000);
		sphere3 = new SphereShape(new Vector(0, 14, 107), 4, sMat);

		sMat = new SolidMaterial(new Color(200, 0, 0), 0, 1.000);
		sphere4 = new SphereShape(new Vector(0, 14, 124), 4, sMat);

		sMat = new SolidMaterial(new Color(210, 158, 20), 0, 1.000);
		testPlane = new PlaneShape(150 ,new Vector(-1, 0, 2), sMat);

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
	 * this method is written to bitmap limitation of reflection depth.
	 * in order to bitmap reflection depth we first create a scene with two
	 * parallel rectangles very close to each other. we shoot a ray to one
	 * of the rectangles that we know after three reflections will hit
	 * background. we hit this ray with reflection depth option first set
	 * to 5 and then set to 3. the resulting color should be the same.
	 * then we hit the same ray with depth 2 and the result should be
	 * different from 2 previous ones.
	 * then we hit another ray to a point that we know will not have
	 * any reflection. the resulting color is compared to all three
	 * previous colors and they should all be different.
	 */
	public void testReflectionDepth(){

		SolidMaterial sMat = new SolidMaterial(
				new Color(150, 2, 20), 0.800, 1.000);
		TriangleShape triangle1 = new TriangleShape(new Vector(-60, 0,0),
				new Vector(35,0,0), new Vector(0,50,0) ,sMat);

		sMat = new SolidMaterial(new Color(150, 50, 40), 0.800, 1.000);
		TriangleShape triangle2 = new TriangleShape(new Vector(-60, 0,3),
				new Vector(35,0,3), new Vector(0,50,3) ,sMat);

		light2 = new Light(new Vector(35, 10, 10));


		testShapes = new ArrayList<>();
		testShapes.add(triangle1);
		testShapes.add(triangle2);


		testLights = new ArrayList<>();
		//testLights.add(light1);
		testLights.add(light2);
		//testLights.add(light3);

		testCamera = new Camera(new Vector(40, 5, 6) ,
				new Vector(0, 0, 0) ,new Vector(0, 1, 0));

		testScene = new Scene(testCamera, new Color(128,128,128),
				null);


		Ray testRay = testCamera.getRay(84, 207);

		RayTracer rt = new RayTracer();

		Color color1;
		Color color2;
		Color color3;

		color1 = rt.calculateColor(testRay, testScene);

		rt.setDepth(3);

		color2 = rt.calculateColor(testRay, testScene);

		rt.setDepth(2);

		color3 = rt.calculateColor(testRay, testScene);

		assertTrue(color1.equals(color2));
		assertFalse(color1.equals(new Color(252, 104, 122)));
		assertFalse(color2.equals(new Color(252, 104, 122)));
		assertFalse(color3.equals(new Color(252, 104, 122)));
		assertFalse(color1.equals(color3));

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
	public void testWithDifferentLights() throws IOException, InvalidPixelException {

		RayTracer testTracer = new RayTracer();
		final Bitmap viewport = new Bitmap(400,400);
		final Bitmap viewport1 = new Bitmap(400,400);
		Color firstColor = new Color();
		Color secondColor = new Color();

		testTracer.rayTraceScene("testResult.ppm", testScene, viewport, 5, false, "1", 1);

		Light light3 = new Light(new Vector(-20, 70, 50));
		testScene.world.lights.add(light3);
		testTracer.rayTraceScene("testResult.ppm", testScene, viewport1, 5, false, "1", 1);

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
	public void testWithDifferentCameraPosition() throws IOException, InvalidPixelException {

		RayTracer testTracer = new RayTracer();
		final Bitmap viewport = new Bitmap(400,400);
		testTracer.rayTraceScene("testResult.ppm", testScene, viewport, 5, false,"1",1);

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
		testTracer.rayTraceScene("testResult.ppm", testScene, viewport, 5, false ,"1" ,1);
		bmpFile = new File("testResult.ppm");
		try {
			bmpFileHash2 = MD5Sum.getFileMD5Sum(bmpFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		assertFalse(bmpFileHash1.equals(bmpFileHash2));

		testScene.camera.setLocation(new Vector(-30, 50, 100));
		testTracer.rayTraceScene("testResult.ppm", testScene, viewport, 5, false,"1",1);
		bmpFile = new File("testResult.ppm");
		try {
			bmpFileHash2 = MD5Sum.getFileMD5Sum(bmpFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		assertTrue(bmpFileHash1.equals(bmpFileHash2));

	}

	/**
	 * in this method we pick one point in a non shadowed region and
	 * one point in a shadowed region. we shoot a ray from each point
	 * toward a light source. the point inside shadow region should hit
	 * another object in between and the point outside shadow region
	 * should not hit any object in between.
	 */
	public void testShadowing(){

		RayTracer testTracer = new RayTracer();
		IntersectInfo shadow = new IntersectInfo();
		IntersectInfo info = new IntersectInfo();

		// first we create an intersection point on a non shadowed space of
		// our plane shape.
		info.setDistance(55.172);
		info.setElement(testPlane);
		info.setHitCount(1);
		info.setIsHit(true);
		info.setNormal(new Vector(0.447, 0, -0.894));
		info.setPosition(new Vector(-8.830, 5.184, 163.289));
		Vector rayd = new Vector(-0.085, 0.494, -0.864);

		// calculate shadow, create ray from intersection point to light
		Ray shadowray = new Ray(info.getPosition(), rayd);

		// find any element in between intersection point and light
		shadow = testTracer.testIntersection(shadowray, testScene);
		// this point should not be in a shadow!
		assertFalse(shadow.getIsHit());


		// then we create an intersection point on a shadowed space of
		// our plane shape.
		info.setDistance(72.312);
		info.setElement(testPlane);
		info.setHitCount(1);
		info.setIsHit(true);
		info.setNormal(new Vector(0.447, 0, -0.894));
		info.setPosition(new Vector(11.772, -16.265, 173.591));
		rayd = new Vector(-0.206, 0.560, -0.802);

		// calculate shadow, create ray from intersection point to light
		shadowray = new Ray(info.getPosition(), rayd);

		// find any element in between intersection point and light
		shadow = testTracer.testIntersection(shadowray, testScene);
		// this point should not be in a shadow!
		assertTrue(shadow.getIsHit());
	}
}
