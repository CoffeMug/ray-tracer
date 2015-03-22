package classes;

import classes.Light;

public class SpotLight extends Light {
    private double focus;
    private Vector3D direction;
        
    /**
     * main constructor of SpotLight class
     * @param pos
     * @param deg
     * @param dir
     */ 
    public SpotLight(final Vector3D pos, final double deg, final Vector3D dir) {
        this.position = pos;
        focus = deg;
        direction = dir;
                
    }
    /**
     * this method checks that if the point is in the cone of the 
     * spotlight or not 
     * @param ray
     * @return
     */
    public Boolean InsideCone(final Ray ray) {
        final double cosPhi;

        cosPhi = ray.getDirection().dotProduct(direction.vectorMultiply(-1))
            /(ray.getDirection().vectorLength()*direction.vectorMultiply(-1).vectorLength());
        if (cosPhi >= focus)
            return true;
        else
            return false;
    }
}
