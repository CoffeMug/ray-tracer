package interfaces;
import classes.Vector3D;
import classes.Ray;
import classes.IntersectInfo;

public interface IShape {
    /**
     * setter method for setting position property of current shape.
     * @param pos postion vector.
     */
    public void setPosition(Vector3D pos);
    
    /**
     * getter method which returns position property of current shape.
     * @return
     */
    public Vector3D getPosition();
    
    /**
     * setter method for setting material property of shape.
     * @param material material of type Imaterial which both can be a solid
     * or a texture material.
     */
    public void setMaterial(IMaterial material);
    
    /**
     * getter method which returns material of current shape.
     * @return material of current shape
     */
    public IMaterial getMaterial();
    
    /**
     * this method test intersection of a given ray with current shape and
     * if intersection happens, returns an object of type intersectInfo
     * which contains information about intersection point.
     * @param ray a ray object we want to test its intersection with current 
     * shape.
     * @return intersection info.
     */
    IntersectInfo intersect(Ray ray);
}

