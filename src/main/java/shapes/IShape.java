package shapes;
import domain.Vector;
import domain.Ray;
import domain.IntersectInfo;
import materials.BaseMaterial;

public interface IShape {
    /**
     * setter method for setting position property of current shape.
     * @param pos postion vector.
     */
    public void setPosition(Vector pos);
    
    /**
     * getter method which returns position property of current shape.
     * @return
     */
    public Vector getPosition();
    
    /**
     * getter method which returns material of current shape.
     * @return material of current shape
     */
    public BaseMaterial getMaterial();
    
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

