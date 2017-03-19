package shapes;

import domain.Color;
import domain.IntersectInfo;
import domain.Ray;
import domain.Vector;
import materials.BaseMaterial;

/**
 * the Triangle class which is based on three Vectors
 * @author amin
 *
 */

public class TriangleShape extends BaseShape {
    private transient Vector vecT0;
    private transient Vector vecT1;
    private transient Vector vecT2;

    /**
     * the constructor which construct the triangle based on three Vectors(as its three corners)
     * @param t0vec 
     * @param t1vec
     * @param t2vec
     */
    public TriangleShape (final Vector t0vec, final Vector t1vec,
                          final Vector t2vec, final BaseMaterial material){
        super();
        if (triangleTest(t0vec, t1vec, t2vec)){
            vecT0 = t0vec;
            vecT1 = t1vec;
            vecT2 = t2vec;
            this.material = material;
        }
        else{
            System.out.printf("This is not a triangle actually ");
        }
    }
        
    /**
     * this method computes the side of triangle based on its corners
     * @param t0vec
     * @param tnvec
     * @return the side between t0 and tn corners
     */
    public static Vector triangleComputeSide(final Vector t0vec, final Vector tnvec){
        return tnvec.vectorReduction(t0vec);
    }
        
    /**
     * this method computes the other corners of triangle based on one corner and one side
     * @param t0vec
     * @param avec
     * @return the corner next to t0 and along side a
     */
        
    public static Vector triangleComputeCorner(final Vector t0vec, final Vector avec){
        return t0vec.vectorAddition(avec);
    }
    /**
     * this method checks if the ray stroke the triangle or not.
     * if so,it will return the intersection point info
     */
    public IntersectInfo intersect(final Ray ray) {
        final IntersectInfo info = new IntersectInfo();
        final Vector aVec = vecT1.vectorReduction(vecT0);
        final Vector bVec = vecT2.vectorReduction(vecT0);
        final Vector Pn = (aVec.crossProduct(bVec)).normalize();//normal vector of triangle plane
        final double dist = vecT0.dotProduct(Pn);//distance from origin of of coordinate system to the plane
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
            final Vector qVec = ri.vectorReduction(vecT0);
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

    public static Boolean triangleTest(final Vector vecT0, final Vector vecT1, final Vector vecT2){
        final Vector sidea = triangleComputeSide(vecT0, vecT1);
        final Vector sideb = triangleComputeSide(vecT0, vecT2);
        final Vector sidec = sideb.vectorReduction(sidea);
                
        // this is to control that if the triangle is constructed correctly
        if(sidea.vectorLength()+sideb.vectorLength() <= sidec.vectorLength()){
            return false;
        }
        else if(sidea.vectorLength()+sidec.vectorLength() <= sideb.vectorLength()){
            return false;
        }
        else if(sideb.vectorLength()+sidec.vectorLength() <= sidea.vectorLength()){
            return false;
        }
        else {
            return true;
        }
    }
}
