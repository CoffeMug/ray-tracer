package shapes;
import domain.Color;
import domain.IntersectInfo;
import domain.Ray;
import domain.Vector;
import materials.BaseMaterial;
import materials.IMaterial;

/**
 * 
 * @author amin
 * the sphere class which make the sphere shape based on a position vector and the sphere radius
 */
public class SphereShape extends BaseShape {
    public transient double radius;

    /**
     * default constructor to produce a single sphere
     * @param pos
     * @param rtmp
     * @param material
     */
    public SphereShape(final Vector pos, final double radius, final BaseMaterial material){
        super();
        this.radius = radius;
        this.position = pos;
        this.material = material;
    }

    /**
     * the overided method to compute the intersection point 
     */
    @Override 
    public IntersectInfo intersect(final Ray ray) {
        final IntersectInfo info = new IntersectInfo();
        info.setElement(this);
        final Vector oc = this.position.vectorReduction(ray.getOrigin());
        final double l2oc = oc.dotProduct(oc);
        final double tca = oc.dotProduct(ray.getDirection());
        final double t2hc = (this.radius * this.radius) - l2oc + (tca * tca);
        //      final double t = Product(dst) - (this.rDbl * this.rDbl);
        final double sr2 = (this.radius * this.radius);

        if (sr2 > l2oc ){ // origin of the ray is inside the sphere.
            info.setIsHit(true);
            info.setDistance(tca + (double)Math.sqrt(t2hc));
            info.setPosition(ray.getOrigin().vectorAddition(
                                                            ray.getDirection().vectorMultiply(info.getDistance())));
            info.setNormal(info.getPosition().vectorReduction(position).vectorMultiply(-1/this.radius));
            info.setColor(calculateColor(info, this.material));
        }
        else if (sr2 < l2oc && tca > 0 ){ // origin of the ray is outside the sphere and ray hits the sphere.
            info.setIsHit(true);
            info.setDistance(tca - (double)Math.sqrt(t2hc));
            info.setPosition(ray.getOrigin().vectorAddition(
                                                            ray.getDirection().vectorMultiply(info.getDistance())));
            info.setNormal(info.getPosition().vectorReduction(position).vectorMultiply(1/this.radius));
            info.setColor(calculateColor(info, this.material));
        }
        else if (t2hc >= 0){
            info.setIsHit(true);
            info.setDistance(tca - (double)Math.sqrt(t2hc));
            info.setPosition(ray.getOrigin().vectorAddition(
                                                            ray.getDirection().vectorMultiply(info.getDistance())));
            info.setNormal(info.getPosition().vectorReduction(position).vectorMultiply(1/this.radius));
            info.setColor(calculateColor(info, this.material));
        }
        else{ // no intersection!
            info.setIsHit(false);
        }
        return info;
    }

    private Color calculateColor(final IntersectInfo info, final IMaterial material) {
        //                System.out.println("Salam mikham bebinam in dayerehe material dare ya na?!");

        if (material.hasTexture())
            {
                //                System.out.println("Salam material dare in dayerehe!");
                //TextureMaterial tmpMaterial = (TextureMaterial) this.material;
                final Vector sp = new Vector(0, 1, 0).normalize(); // north pole / up
                // equator / sphere orientation
                final Vector se = new Vector(0, 0, 1).normalize();
                //points from center of sphere to intersection 
                final Vector rn = info.getNormal();
                final double phi = Math.acos((rn.vectorMultiply(-1)).dotProduct(sp));
                final double vi = phi / Math.PI;
                final double ui;

                if (vi == 0 || vi == 1)
                    ui = 0;
                else {
                    final double theta = (Math.acos(se.dotProduct(rn) / Math.sin(phi)))/(2*Math.PI);
                    ui = (se.crossProduct(sp)).dotProduct(rn) > 0 ? theta : 1 - theta; 
                }
                return material.getColor(ui, vi);
            }
        else
            {
                // skip uv calculation, just get the color
                return material.getColor(0, 0);
            }
    }

    @Override public  String toString(){
        return String.format("Sphere ({0},{1},{2}) Radius: {3}", position.toString(), radius);
    }
}
