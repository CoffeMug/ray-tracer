package shapes;

import domain.IntersectInfo;
import domain.Ray;
import domain.Vector;
import materials.BaseMaterial;

/**
 * the plane class which will produce the plane shape based its position and normal vector  
 * @author amin
 */
public class Plane extends BaseShape {
    private double distance;
    private Vector normal;

    public Plane(final Vector pos, final Vector normal, final BaseMaterial material){
        super();// for suppressing PMD errors
        position = pos;
        this.normal = normal.normalize();
        this.distance = position.dotProduct(this.normal);
        this.material = material;
    }

    public Plane(final double distance, final Vector normal, final BaseMaterial material){
        super();
        position = normal.vectorMultiply(distance);
        this.distance = distance;
        this.normal = normal.normalize();
        this.material = material;
    }

    @Override
    public IntersectInfo intersect(final Ray ray){
        final IntersectInfo info = new IntersectInfo();
        final double vD = normal.dotProduct(ray.getDirection());

        if (vD == 0){
            return info; // no intersection the ray is parallel to the plane
        }

        final double v0 = distance - normal.dotProduct(ray.getOrigin());
        final double t = (v0/vD);

        if (t < 0) return info; // the ray does not hit the plane

        final Vector ri = ray.getOrigin().vectorAddition(ray.getDirection().vectorMultiply(t));
        final Vector rn = vD < 0 ? normal : normal.vectorMultiply(-1);

        info.setElement(this);
        info.setIsHit(true);
        info.setPosition(ri);
        info.setNormal(rn);
        info.setDistance(t);
        info.setColor(material.getColor(0,0));

        return info;
    }

    @Override   
    public String toString(){
        return String.format("Plane {0}x+{1}y+{2}z+{3}=0)", position.toString(), distance);
    }
}
