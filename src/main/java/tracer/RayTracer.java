package tracer;

import java.io.IOException;
import domain.*;
import bitmap.IBitmap;
import exceptions.InvalidPixelException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import shapes.IShape;

/**
 * The RayTracer class which is the main class responsible for tracing 
 * the scene and produces the rendered picture
 * @author Amin Khorsandi
 */
public class RayTracer {

    private final Logger logger = LoggerFactory.getLogger(RayTracer.class);

    private Boolean renderDiffuse;
    private Boolean renderShadow;
    private Boolean renderReflection;
    private int traceDepth = 0;
    private int reflectionDepth = 5;
    private final double eps = 0.0001;

    public RayTracer() {

    } 

    public RayTracer(final Boolean renderDiffuse,
                     final Boolean renderShadow,
                     final Boolean renderReflection){
        this.renderDiffuse = renderDiffuse;
        this.renderShadow = renderShadow;
        this.renderReflection = renderReflection;
    }

    public int getDepth(){
        return this.reflectionDepth;
    }

    public void setDepth(int dep){
        this.reflectionDepth = dep;
    }

    public void rayTraceScene(final Scene scene,
                              final IBitmap viewport,
                              final int depth,
                              final boolean showTime,
                              final String thread,
                              final int noOfWindows) throws IOException, InvalidPixelException {

        Color[][] buffer = new Color[viewport.getWidth()][viewport.getHeight()];
        final int width = viewport.getWidth();
        final int height = viewport.getHeight();
        final int tNumber =Integer.parseInt(thread);
        final int wHeight = (int) Math.floor(viewport.getHeight()/noOfWindows);
        this.reflectionDepth = depth;


        // if number of threads does not fit all image plane we ask final
        // thread to process its own window plus all remaining pixels.
        final int upperBoundY = (tNumber == noOfWindows && 
                                 tNumber*wHeight < height) ?
            height : tNumber*wHeight ;
        final int lowerBoundY = (tNumber-1)*wHeight;

        Ray ray;

        double startTime = System.currentTimeMillis();

        for (int y = lowerBoundY; y < upperBoundY; y++) {
            for (int x = 0; x < width; x++) {

                ray = scene.camera.getRay(x,y); // was xp , yp

                // this will trigger the ray tracing algorithm
                buffer[x][y] = calculateColor(ray, scene);

                //after getting color of each pixel on image plane we 
                //save it in viewport Bitmap object.
                viewport.writePixel(x, y, buffer[x][y]);
            }

        }

        // here we calculate trace time.
        double duration = System.currentTimeMillis() - startTime ;

        if (showTime){
            logger.info("Trace time: {} ms", duration);
        }

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
        Color color;
        if (info.getIsHit()){
            traceDepth = 0;
            color = rayTrace(info, ray, scene);
        }
        else{
            color = scene.background;
        }
        return color;
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
        final Vector ri = info.getPosition();
        final Vector rn = info.getNormal();
        final Vector so = ri.vectorAddition(rn.vectorMultiply(eps));

        // calculate reflection ray
        if (renderShadow){
            for (Light light : scene.world.lights) {
                final Vector sd = (light.position.vectorReduction(so)).normalize();
                final Double cosPhi = Math.abs(rn.dotProduct(sd));
                final Ray shadowRay = new Ray(so, sd);
                IntersectInfo shadow = testIntersection(shadowRay, scene);
                color = color.addColor(light.getColor(shadow, info, cosPhi));
            }
        }

        if (renderReflection){
            if (info.getElement().getMaterial().getReflection() > 0 && traceDepth < this.reflectionDepth){
                final Ray reflectionRay = getReflectionRay(info.getPosition(), info.getNormal(), ray.getDirection());
                final IntersectInfo intersectInfo = testIntersection(reflectionRay, scene);

                if (intersectInfo.getIsHit() && intersectInfo.getDistance() > 0){
                    traceDepth++ ;
                    intersectInfo.setColor(rayTrace(intersectInfo, reflectionRay, scene));
                }
                else {
                    intersectInfo.setColor(info.getColor());
                }
                color = color.addColor(intersectInfo.getColor()
                        .multiplyColorByValue(info.getElement().getMaterial().getReflection()));
            }
        }
        return color;
    }

    private Ray getReflectionRay(final Vector Ri,
                                 final Vector Rn,
                                 final Vector Rd) {
        final Vector To = Ri.vectorAddition(Rn.vectorMultiply(eps));
        final Vector Td = Rd.vectorReduction(Rn.vectorMultiply(Rn.dotProduct(Rd)*2));
        return new Ray(To, Td);
    }


    public IntersectInfo testIntersection(final Ray ray ,
                                          final Scene scene) {
        int hitCount = 0;
        IntersectInfo best = new IntersectInfo(Double.MAX_VALUE);

        for (IShape elt : scene.world.shapes) {
            final IntersectInfo info = elt.intersect(ray);
            if (info.getIsHit() && info.getDistance() < best.getDistance()
                && info.getDistance() > 0 ) {
                best = info;
                hitCount++;
            }
        }
        best.setHitCount(hitCount);
        return best;
    }

}

