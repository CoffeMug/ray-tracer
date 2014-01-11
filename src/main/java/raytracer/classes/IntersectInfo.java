package classes;
import interfaces.IShape;
/**
 * the intersect info class , this class holds information of an intersection
 * point.
 * @author amin
 *
 */

public class IntersectInfo {
	public transient Boolean isHit; // indicates if the shape was hit
	public int hitCount; // counts the number of shapes that were hit
	public IShape element; // the closest shape that was intersected
	public Vector3D position; // position of intersection
	public Vector3D normal; // normal vector on intersection point 
	public Color color; // color at intersection
	public double distance; // distance from point to screen

	/**
	 * the default constructor
	 */
	public IntersectInfo()
	{
		isHit = false;
	}
}
