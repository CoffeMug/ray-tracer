package classes;

import shapes.*;

/**
 * abstract data type for scene. a scene contains camera, background,
 * shapes and lights
 * @author majid
 *
 */
public class Scene {
	 //Marking variables as transient is the safest and easiest modification
	 //for the variables which will not be serialized
	 public transient Color background;
     public transient Camera camera;
     public transient Shapes shapes;
     public transient Lights lights;
     public transient SpotLights spotlights;

     /**
      * scene class constructor with manual values.
      */
     public  Scene(){
         camera = new Camera(new Vector3D(0,-5,200), new Vector3D(0.6,0,0) ,
        		 				new Vector3D(0,5,0) );
         shapes = new Shapes();
         lights = new Lights();
         background = new Color(0, 0, 128);
     }
   
     public  Scene(final Camera camera, final Color background,
    		                   final Shapes shapes, final Lights lights,final SpotLights spotlights){
    	 
    	 this.camera = camera;
    	 this.shapes = shapes;
    	 this.lights = lights;
    	 this.spotlights = spotlights;
    	 this.background = background;
     }

}
