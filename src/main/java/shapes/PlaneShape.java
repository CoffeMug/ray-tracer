package shapes;
import interfaces.IMaterial;
import classes.*;

/**
 * the plane class which will produce the plane shape based its position and normal vector  
 * @author amin
 * 
 */
public class PlaneShape extends BaseShape {
    private double dist;
    private Vector3D pNorm;
        
    /**
     * the simple constructor which just produce a simple plane 
     * @param pos
     * @param nVector
     */
    public PlaneShape(final Vector3D pos, final Vector3D normal, final IMaterial material){
        super();// for suppressing PMD errors
        position = pos;
        this.pNorm = normal.normalize(); 
        this.dist = position.dotProduct(pNorm);
        this.material = material;
    }

    /**
     * the  constructor which constructs the plane with position vector
     * and distance.
     * @param pos
     * @param dist

     */
    public PlaneShape(final Vector3D pos, final double dist){
        super();// for suppressing PMD warnings
        position = pos;
        this.dist = dist;
    }
    /**
     * this constructor produces the plane based on POVrays format
     * @param dist
     * @param nVect
     * @param material
     */
        
    public PlaneShape(final double dist, final Vector3D normal, final IMaterial material){
        super();
        position = normal.vectorMultiply(dist);
        this.dist = dist;
        this.pNorm = normal.normalize();
        this.material = material;
    }

    /**
     * this method computes the intersection point between ray and the plane if there are any.
     * 
     */
    @Override  public IntersectInfo intersect(final Ray ray){
        final IntersectInfo info = new IntersectInfo();
        final double vD = pNorm.dotProduct(ray.getDirection());

        if (vD == 0){
            return info; // no intersection the ray is parallel to the plane
        }
                
        final double v0 = dist - pNorm.dotProduct(ray.getOrigin());
        final double tmp = (v0 / vD);
                
        if (tmp < 0) return info;// the ray does not hit the plane
                
        final Vector3D ri = ray.getOrigin().vectorAddition(ray.getDirection().vectorMultiply(tmp));
        final Vector3D rn = vD < 0 ? pNorm : pNorm.vectorMultiply(-1);
        //
        info.setElement(this);
        info.setIsHit(true);
        info.setPosition(ri);
        info.setNormal(rn);
        info.setDistance(tmp);
        info.setColor(material.getColor(0,0));

        return info;
    }

    @Override   
    public String toString(){
        return String.format("Plane {0}x+{1}y+{2}z+{3}=0)", position.toString(), dist);
    }
}
