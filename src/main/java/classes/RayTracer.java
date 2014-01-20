package classes;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import interfaces.IBitmap;
import interfaces.IShape;

/**
 * The RayTracer class which is the main class responsible for tracing 
 * the scene and produces the rendered picture
 * @author Amin Khorsandi
 */

public class RayTracer {

	public transient Boolean renderDiffuse;
	public transient Boolean renderHighlights;
	public transient Boolean renderShadow;
	public transient Boolean renderReflection;
	private  transient int traceDepth = 0;
	private transient int reflectionDepth = 5;
	final double eps = 0.0001;



	/**
	 * this is simple constructor for class RayTracer.
	 */
	public RayTracer() {

		this.renderDiffuse = true;
		this.renderHighlights = true;
		this.renderReflection = true;
		this.renderShadow = true;


	} 

	/**
	 * the more complicated constructor which produce some
	 * effects in the rendered scene.
	 * @param renderDiffuse
	 * @param renderShadow
	 * @param renderReflection
	 */
	public RayTracer(final Boolean renderDiffuse,final Boolean renderShadow,
			final Boolean renderReflection){

		this.renderDiffuse = renderDiffuse;
		this.renderShadow = renderShadow;
		this.renderReflection = renderReflection;

	}

	/**
	 * getter method for getting trace depth.
	 * @return trace depth as integer value.
	 */
	public int getDepth(){
		return this.reflectionDepth;
	}

	/**
	 * this is the setter method for setting trace depth.
	 * @param dep integer value as depth of tracing.
	 */
	public void setDepth(int dep){
		this.reflectionDepth = dep;
	}

	/**
	 * this is the main entry point for rendering a scene.
	 * this method is responsible for correctly rendering the
	 * graphics device (in this case a bitmap from file).
	 * @param bmpfile
	 * @param Scene
	 * @param viewport a 2 dimensional array for saving resulting pixels
	 * in.
	 * @param depth reflection depth that should be rendered.
	 * @param showTime a boolean value specifying that whether the program should
	 * show tracing time or not.
	 * @param thread number of threads that will process raytracing.
	 * @param noOfWindows number of partitions we divide the scene into before
	 * starting to raytrace it.
	 */

	public void rayTraceScene(final String bmpfile, final Scene scene ,
			final IBitmap viewport ,final int depth , final boolean showTime, 
			final String thread , final int noOfWindows) {

		Color[][] buffer = new Color[viewport.getWidth()][ viewport.getHeight()];
		final int width = viewport.getWidth();
		final int height = viewport.getHeight();
		final int tNumber =Integer.parseInt(thread);
		final int wHeight = (int) Math.floor(viewport.getHeight()/noOfWindows);
		double startTime = System.currentTimeMillis();
		BufferedWriter out = null;
		this.reflectionDepth = depth;



		try {
			FileWriter fstream = new FileWriter("out.txt",true);
			out = new BufferedWriter(fstream);
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage());
		}

		// if number of threads does not fit all image plane we ask final
		// thread to process its own window plus all remaining pixels.
		final int upperBoundY = (tNumber == noOfWindows && 
				tNumber*wHeight < height) ?
						height : tNumber*wHeight ;
		final int lowerBoundY = (tNumber-1)*wHeight + 1;

		Ray ray;

		for (int y = lowerBoundY; y <= upperBoundY; y++) {
			for (int x = 1; x <= width ; x++) {

				ray = scene.camera.getRay(x,y); // was xp , yp

				// this will trigger the ray tracing algorithm
				buffer[x-1][y-1] = calculateColor(ray, scene);

				//after getting color of each pixel on image plane we 
				//save it in viewport Bitmap object.
				viewport.setPixel(x, y, buffer[x-1][y-1]);
			}

		}

		// here we calculate trace time.
		double duration = System.currentTimeMillis() - startTime ;
		String str = Double.toString(duration) + "\r\n" ;
		try {
			out.write(str);
			out.close();
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage());
		}

		if (showTime){
			System.out.print("Tracetime: " + duration + " ms");
		}
		//write result of raytracing into a bitmap file.
		viewport.writeBitmapToFile(1,bmpfile);
	}

	/** this method returns color of intersection point if intersection
	 * happens.
	 * @param ray an object of type Ray we want to test its intersection
	 * with any shape or background in scene.
	 * @param scene current scene we test ray intersection with.
	 * @return color of intersection point.
	 */

	public Color calculateColor(final Ray ray,final Scene scene){
		final IntersectInfo info = testIntersection(ray, scene);
		Color colr;
		if (info.isHit){
			traceDepth = 0;
			colr = rayTrace(info,ray,scene);

		}
		else{
			colr = scene.background;
		}
		return colr;
	}

	/** This is the main RayTrace controller algorithm, the core of 
	 * the RayTracer recursive method setup this does the actual tracing
	 * of the ray and determines the color of each pixel
	 * @param info
	 * @param ray
	 * @param scene
	 * @return Color in intersection point.
	 */
	private Color rayTrace(final IntersectInfo info,final Ray ray,
			final Scene scene) {


		Color color = new Color();
		Color tmpColor = new Color();


		final Vector3D ri = info.position;
		final Vector3D rn = info.normal;
		final Vector3D so = ri.vectorAddition(rn.vectorMultiply(eps));

		/* this is the max depth of raytracing.
			 increasing depth will calculate more accurate color, 
			 however it will
			 also take longer (exponentially) */


		// calculate reflection ray


		if (renderShadow){

			for (Light light : scene.lights) {	

				// calculate shadow, create ray from intersection point to light
				final Vector3D sd = (light.position.vectorReduction(so)).normalize();	
				final Double cosPhi = Math.abs(rn.dotProduct(sd));


				final Ray shadowray = new Ray(so, sd);
				IntersectInfo shadow = new IntersectInfo();

				// find any element in between intersection point and light
				shadow = testIntersection(shadowray, scene);

				// here we compute the color of hit point
				tmpColor = light.getColor(shadow, info, cosPhi); 


				color = color.addColor(tmpColor);
			}


			for (SpotLight spotlight : scene.spotlights) {	
				// calculate shadow, create ray from intersection point to light
				final Vector3D sd = (spotlight.position.vectorReduction(so)).normalize();	
				final Double cosPhi = Math.abs(rn.dotProduct(sd));


				final Ray shadowray = new Ray(so, sd);
				IntersectInfo shadow = new IntersectInfo();

				// find any element in between intersection point and light
				shadow = testIntersection(shadowray, scene);
				if (spotlight.InsideCone(shadowray)){
					tmpColor = spotlight.getColor(shadow, info, cosPhi);

				color = color.addColor(tmpColor);
				}
			}

		}

		if (renderReflection){

			if (info.element.getMaterial().getReflection() > 0 && 
					traceDepth < this.reflectionDepth){
				final Ray reflectionray = getReflectionRay(info.position, info.normal, ray.direction);
				final IntersectInfo refl = testIntersection(reflectionray, scene);

				if (refl.isHit && refl.distance > 0){
					// recursive call, this makes reflections expensive
					traceDepth++ ;

					refl.color = rayTrace(refl, reflectionray, scene);

				}
				else  // does not reflect an object, then reflect background color

					refl.color = info.color;

				color = color.addColor(refl.color.multiColor(
						info.element.getMaterial().getReflection()));
			}

		}

		return color;
	}

	/***********************************************************************/

	/**
	 * when we have reflection enabled and our object inside the scene
	 * has a reflective surface, this method gets reflected ray from
	 * that object after intersection.
	 * @param Ri vector.
	 * @param Rn normal vector.
	 * @param Rd vector
	 * @return a ray reflected from object.
	 */
	private Ray getReflectionRay(final Vector3D Ri, final Vector3D Rn,
			final Vector3D Rd) {


		final Vector3D To = Ri.vectorAddition(Rn.vectorMultiply(eps));
		final Vector3D Td = Rd.vectorReduction(Rn.vectorMultiply(Rn.dotProduct(Rd)*2)); 
		return new Ray(To, Td);
	}


	/** This method tests for an intersection. It will try to find the
	 * closest object that intersects with the ray.it will inspect every
	 * object in the scene.
	 * @param ray the ray that may hit intersection point
	 * @param scene scene we are testing intersections in it.
	 * @return intersection point info if any
	 */

	public IntersectInfo testIntersection(final Ray ray ,
			final Scene scene) {
		int hitcount = 0;
		IntersectInfo best = new IntersectInfo();
		best.distance = Double.MAX_VALUE;

		for (IShape elt : scene.shapes) {

			final IntersectInfo info = elt.intersect(ray);
			if (info.isHit && info.distance < best.distance
					&& info.distance > 0 ) {
				best = info;
				hitcount++;
			}
		}
		best.hitCount = hitcount;
		return best;
	}

}

