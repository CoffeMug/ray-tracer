package shapes;

import domain.Color;
import domain.IntersectInfo;
import domain.Ray;
import domain.Vector;
import exceptions.InvalidPixelException;
import materials.BaseMaterial;

/**
 * the Triangle class which is based on three Vectors
 * @author amin
 *
 */

public class Triangle extends BaseShape {
    private transient Vector corner0;
    private transient Vector corner1;
    private transient Vector corner2;

    public Triangle(final Vector c0, final Vector c1,
                    final Vector c2, final BaseMaterial material){
        super();
        this.corner0 = c0;
        this.corner1 = c1;
        this.corner2 = c2;
        this.material = material;
    }

    public IntersectInfo intersect(final Ray ray) throws InvalidPixelException {
        final IntersectInfo info = new IntersectInfo();
        final Vector aVec = corner1.vectorReduction(corner0);
        final Vector bVec = corner2.vectorReduction(corner0);
        final Vector Pn = (aVec.crossProduct(bVec)).normalize();//normal vector of triangle plane
        final double dist = corner0.dotProduct(Pn);//distance from origin of of coordinate system to the plane
        final double vd = Pn.dotProduct(ray.getDirection());
        final double v0 = dist - (Pn.dotProduct(ray.getOrigin()));
        if (vd == 0){
            return info; // no intersection
        }
                
        final double tmp = v0 / vd;
                
        if (tmp >= 0){
            final Vector ri = ray.getOrigin().vectorAddition(ray.getDirection().vectorMultiply(tmp));
            final Vector rn = vd < 0 ? Pn : Pn.vectorMultiply(-1);
            //q is the vector between the origin of the triangle and the intersection point
            final Vector qVec = ri.vectorReduction(corner0);
            double uTmp ; 
            double vTmp ;
            final double bb = bVec.dotProduct(bVec);
            final double qa = qVec.dotProduct(aVec);
            final double ab = aVec.dotProduct(bVec);
            final double qb = qVec.dotProduct(bVec);
            final double aa = aVec.dotProduct(aVec);
                        
            uTmp = ((bb * qa) - (ab * qb)) / ((aa * bb)- (ab * ab));
                        
            if (uTmp > 0 && uTmp < 1) {
                vTmp = (qb - uTmp*ab)/bb;
                                
                if (vTmp > 0 && vTmp < 1 && uTmp + vTmp <= 1) {
                    Color color;
                    if (material.hasTexture())
                        {
                            color = this.material.getColor(vTmp, uTmp);
                        }       
                    else{
                        color = getMaterial().getColor(0,0);
                    }
                    return new IntersectInfo(true, this, ri, rn, color, tmp); 
                }
            }
        }
        return info;
    }
}
