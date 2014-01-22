package classes;

/**
 * The camera class which has three Vectors and and five integer variables 
 * as camera properties and one method to handle the rays in the scene 
 * from the camera perspective
 * @author amin
 */

public class Camera {
    public transient Vector3D location;// location of camera in the scene
    public transient Vector3D lookAt; 
    public transient Vector3D upside; // defines the up side of the scene
    public transient int zoom; // the zoom of camera in the scene
    public transient int width; // the width of the image of the world
    public transient int height; // the height of the image of the world
    public transient int xpix; // the number of pixels in x Axis of the image
    public transient int ypix; // the number of pixels in the y Axis of the image
    public transient boolean isValid;

    /**
     * the default constructor camera used in XmlParser class 
     * @param position
     * @param lookat
     */
    public Camera(final Vector3D location, final Vector3D lookat,final Vector3D upward){

        this(location, lookat,upward ,300,400,400,400,400);
    }

    /**
     * the more detailed constructor 
     * @param position
     * @param lookat
     * @param upward
     */
    public Camera(final Vector3D location, final Vector3D lookat, final Vector3D upward
                  ,final int zoom,final int width,final int height,final int xcord,
                  final int ycord) {

        if (validateCamera(zoom,width,height,xcord,ycord)){
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
        final Vector3D cVect = (lookAt.vectorReduction(location)).normalize();
        final Vector3D crVect = (upside.crossProduct(cVect)).normalize();
        final Vector3D cdVect = (crVect.crossProduct(cVect)).normalize();

        final Vector3D ccVect = location.vectorAddition(cVect.vectorMultiply(zoom)).
                                vectorReduction(crVect.vectorMultiply(width/2)).
                                vectorReduction(cdVect.vectorMultiply(height/2));

        final Vector3D Pxy = ccVect.vectorAddition(crVect.vectorMultiply((width/xpix)*
                             (vectx+0.5))).vectorAddition(cdVect.vectorMultiply
                             ((height/ypix)*(vecty+0.5)));

        final Vector3D dir = Pxy.vectorReduction(location);
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
}

