package shapes;

import domain.Color;
import domain.IntersectInfo;
import domain.Ray;
import domain.Vector;
import materials.BaseMaterial;

/**
 * The sphere class which makes the sphere shape based on a position vector and the sphere radius
 * @author amin
 *
 */
public class Sphere extends BaseShape {
    public transient double radius;

    public Sphere(final Vector pos, final double radius, final BaseMaterial material){
        super();
        this.radius = radius;
        this.position = pos;
        this.material = material;
    }

    @Override 
    public IntersectInfo intersect(final Ray ray) {
        final IntersectInfo info = new IntersectInfo();
        info.setElement(this);
        final Vector oc = this.position.vectorReduction(ray.getOrigin());
        final double l2oc = oc.dotProduct(oc);
        final double tca = oc.dotProduct(ray.getDirection());
        final double sr2 = this.radius * this.radius;
        final double t2hc = sr2 - l2oc + tca * tca;
        final double tOutside = tca - Math.sqrt(t2hc);
        final double tInside = tca + Math.sqrt(t2hc);

        // Origin of the ray is inside the sphere
        if (l2oc < sr2){
            info.setIsHit(true);
            info.setDistance(tInside);
            info.setPosition(ray.getOrigin().vectorAddition(ray.getDirection().vectorMultiply(tInside)));
            info.setNormal(info.getPosition().vectorReduction(this.position).vectorMultiply(-1/this.radius));
            info.setColor(calculateColor(info, this.material));
        }
        // Origin of ray is outside the sphere but ray hits the sphere
        else if (sr2 <= l2oc && (tca > 0 || t2hc >=0)) {
            info.setIsHit(true);
            info.setDistance(tOutside);
            info.setPosition(ray.getOrigin().vectorAddition(ray.getDirection().vectorMultiply(tOutside)));
            info.setNormal(info.getPosition().vectorReduction(this.position).vectorMultiply(1/this.radius));
            info.setColor(calculateColor(info, this.material));
        }
        // No intersection!
        else {
            info.setIsHit(false);
        }
        return info;
    }

    private Color calculateColor(final IntersectInfo info, final BaseMaterial material) {
        if (material.hasTexture())
            {
                //TextureMaterial tmpMaterial = (TextureMaterial) this.material;
                final Vector sp = new Vector(1, 0, 0).normalize(); // north pole / up
                // equator / sphere orientation
                final Vector se = new Vector(0, 0, 1).normalize();
                //points from center of sphere to intersection 
                final Vector rn = info.getNormal();
                final double phi = Math.acos(rn.vectorMultiply(-1).dotProduct(sp) > 1 ? 1 : rn.vectorMultiply(-1).dotProduct(sp));
                final double vi = phi / Math.PI;
                final double ui;

                if (vi == 0 || vi == 1)
                    ui = 0;
                else {
                    final double theta = (Math.acos(se.dotProduct(rn) / Math.sin(phi) > 1 ? 1 :
                            Math.acos(se.dotProduct(rn) / Math.sin(phi))))/(2*Math.PI);
                    ui = (se.crossProduct(sp)).dotProduct(rn) > 0 ? theta : 1 - theta; 
                }
                return material.getColor(ui, vi);
            }
        else {
                // skip uv calculation, just get the color
                return material.getColor(0, 0);
        }
    }

    @Override
    public  String toString(){
        return String.format("Sphere ({0},{1},{2}) Radius: {3}", position.toString(), radius);
    }
}
