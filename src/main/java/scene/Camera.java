package scene;

import domain.Ray;
import domain.Vector;

/**
 * The camera class which has three Vectors and five integer variables 
 * as camera properties and one method to handle the rays in the scene 
 * from the camera perspective
 * @author amin
 */

public class Camera {
    private Vector location;
    private Vector lookAt;
    private Vector upside;
    private int zoom; 
    private int width; 
    private int height; 
    private int xpix; 
    private int ypix; 
    private boolean isValid;

    public boolean isValid() {
        return isValid;
    }

    /**
     * the default constructor camera used in XmlParser class 
     * @param position
     * @param lookat
     */
    public Camera(final Vector location, final Vector lookAt, final Vector upward){
        this(location, lookAt, upward, 300, 400, 400, 400, 400);
    }

    /**
     * the more detailed constructor 
     * @param position
     * @param lookat
     * @param upward
     */
    public Camera(final Vector location, final Vector lookat, final Vector upward,
                  final int zoom, final int width, final int height, final int xcord,
                  final int ycord) {

        if (validateCamera(zoom, width, height, xcord, ycord)) {
            this.upside = upward.normalize();
            this.location = location;
            this.lookAt = lookat;
            this.upside = upward;
            this.width = width;
            this.height = height;
            this.xpix = xcord;
            this.ypix = ycord;
            this.zoom = zoom;
            this.isValid = true;
        }
        else{

            this.isValid = false ;
        }

    }

    /**
     * this method returns the ray as it passes through the view-port 
     * form the camera perspective 
     * @param vectx
     * @param vecty
     * @return
     */

    public Ray getRay(final float vectx, final float vecty){
        final Vector cVect = (lookAt.vectorReduction(location)).normalize();
        final Vector crVect = (upside.crossProduct(cVect)).normalize();
        final Vector cdVect = (crVect.crossProduct(cVect)).normalize();

        final Vector ccVect = location.vectorAddition(cVect.vectorMultiply(zoom)).
            vectorReduction(crVect.vectorMultiply(width/2)).
            vectorReduction(cdVect.vectorMultiply(height/2));

        final Vector Pxy = ccVect.vectorAddition(crVect.vectorMultiply((width/xpix)*
                                                   (vectx+0.5))).vectorAddition(cdVect.vectorMultiply
                                                   ((height/ypix)*(vecty+0.5)));

        final Vector dir = Pxy.vectorReduction(location);
        //Ray newRay = new Ray(location, dir.normalize());
        return new Ray(location, dir.normalize());
    }

    /**
     * this method is to check if the camera is constructed properly
     * @param zoom
     * @param width
     * @param height
     * @param xcord
     * @param ycord
     * @return
     */
    public static boolean validateCamera(final int zoom,final int width,
                                         final int height, final int xcord,
                                         final int ycord){
        boolean flag;
        if( zoom < 1 || zoom > 300 || height < 1 ||
            height > 2000 || width < 1 || width > 2000 ||
            xcord < 1 || xcord > 2000 || ycord < 1 || ycord > 2000 ){
            flag = false;
        }
        else{
            flag = true;
        }
        return flag;
    }

    /**
     * Getters and setters.
     *
     */

    public int getZoom() {
        return this.zoom;
    }

    public void setZoom(final int zoom) {
        this.zoom = zoom;
    }

    public int getWidth() {
        return this.width;
    }

    public void setWidth(final int width) {
        this.width = width;
    }

    public int getHeight() {
        return this.height;
    }

    public void setHeight(final int height) {
        this.height = height;
    }

    public int getXpix() {
        return this.xpix;
    }

    public void setXpix(final int xpix) {
        this.xpix = xpix;
    }

    public int getYpix() {
        return this.ypix;
    }

    public void setYpix(final int ypix) {
        this.ypix = ypix;
    }

    public Vector getLocation() {
        return this.location;
    }

    public void setLocation(final Vector location) {
        this.location = location;
    }

    public Vector getLookAt() {
        return this.lookAt;
    }

    public void setLookAt(final Vector lookAt) {
        this.lookAt = lookAt;
    }

    public Vector getUpside() {
        return this.upside;
    }
 
    public void setUpside(final Vector upside) {
        this.upside = upside;
    }
}

