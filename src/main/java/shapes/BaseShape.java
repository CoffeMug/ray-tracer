package shapes;
import domain.IntersectInfo;
import domain.Ray;
import domain.Vector;
import materials.BaseMaterial;

/**
 * 
 * @author amin
 *the basic shape class which is a base for all shapes in the scene
 */

public abstract class BaseShape implements IShape {
    protected Vector position;
    protected BaseMaterial material;
        
    /**
     * default constructor
     */
    public BaseShape(){
        position = new Vector(0, 0, 0);
    }

    public abstract IntersectInfo intersect(Ray ray);

    /**
     * @return the position
     */
    public Vector getPosition() {
        return position;
    }

    /**
     * @param position the position to set
     */
    public void setPosition(final Vector position) {
        this.position = position;
    }
        
    public BaseMaterial getMaterial(){
        return material;
    }
        
    public void setMaterial(final BaseMaterial material){
        this.material = material;
    }
}
