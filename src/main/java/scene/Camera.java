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
    private Vector up;
    private int zoom; 
    private int width; 
    private int height; 
    private int xpix; 
    private int ypix;

    public Camera(final Vector location, final Vector lookAt, final Vector up){
        this.location = location;
        this.lookAt = lookAt;
        this.up = up;
    }

    public Camera withZoom(final int zoom) {
        this.zoom = zoom;
        return this;
    }

    public Camera withWidth(final int width) {
        this.width = width;
        return this;
    }

    public Camera withHeight(final int height) {
        this.height = height;
        return this;
    }

    public Camera withXpix(final int xpix) {
        this.xpix = xpix;
        return this;
    }

    public Camera withYpix(final int ypix) {
        this.ypix = ypix;
        return this;
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
        final Vector crVect = (up.crossProduct(cVect)).normalize();
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
}

