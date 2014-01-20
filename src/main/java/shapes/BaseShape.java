package shapes;
import interfaces.IMaterial;
import interfaces.IShape;
import classes.*;

/**
 * 
 * @author amin
 *the basic shape class which is a base for all shapes in the scene
 */

public abstract class BaseShape implements IShape {

	        
	protected Vector3D position;
	protected IMaterial material;
	
	/**
	 * default constructor
	 */
	public BaseShape(){
		position = new Vector3D(0,0,0);

	}

	public abstract IntersectInfo intersect(Ray ray);

	/**
	 * @return the position
	 */
	public Vector3D getPosition() {
		return position;
	}

	/**
	 * @param position the position to set
	 */
	public void setPosition(final Vector3D position) {
		this.position = position;
	}
	
	public IMaterial getMaterial(){
		return material;
	}
	
	public void setMaterial(final IMaterial material){
		this.material = material;
	}



}
