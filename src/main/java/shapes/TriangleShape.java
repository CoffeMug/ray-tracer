package shapes;

import materials.TextureMaterial;
import interfaces.IMaterial;
import  classes.*;
/**
 * the Triangle class which is based on three Vectors
 * @author amin
 *
 */

public class TriangleShape extends BaseShape {
    private transient Vector3D vecT0;
    private transient Vector3D vecT1;
    private transient Vector3D vecT2;

    /**
     * the constructor which construct the triangle based on three Vectors(as its three corners)
     * @param t0vec 
     * @param t1vec
     * @param t2vec
     */
    public TriangleShape (final Vector3D t0vec, final Vector3D t1vec,
                          final Vector3D t2vec, final IMaterial material){
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
    public static Vector3D triangleComputeSide(final Vector3D t0vec, final Vector3D tnvec){
        return tnvec.vectorReduction(t0vec);
    }
        
    /**
     * this method computes the other corners of triangle based on one corner and one side
     * @param t0vec
     * @param avec
     * @return the corner next to t0 and along side a
     */
        
    public static Vector3D triangleComputeCorner(final Vector3D t0vec, final Vector3D avec){
        return t0vec.vectorAddition(avec);
    }
    /**
     * this method checks if the ray stroke the triangle or not.
     * if so,it will return the intersection point info
     */
    public IntersectInfo intersect(final Ray ray){
        final IntersectInfo info = new IntersectInfo();
        final Vector3D aVec = vecT1.vectorReduction(vecT0);
        final Vector3D bVec = vecT2.vectorReduction(vecT0);
        final Vector3D Pn = (aVec.crossProduct(bVec)).normalize();//normal vector of triangle plane
        final double dist = vecT0.dotProduct(Pn);//distance from origin of of coordinate system to the plane
        final double vd = Pn.dotProduct(ray.getDirection());
        final double v0 = dist - (Pn.dotProduct(ray.getOrigin()));
        if (vd == 0){
            return info; // no intersection
        }
                
        final double tmp = v0 / vd;
                
        if (tmp >= 0){
            final Vector3D ri = ray.getOrigin().vectorAddition(ray.getDirection().vectorMultiply(tmp));
            final Vector3D rn = vd < 0 ? Pn : Pn.vectorMultiply(-1);
            //q is the vector between the origin of the triangle and the intersection point
            final Vector3D qVec = ri.vectorReduction(vecT0);
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
                    info.element = this;
                    info.isHit = true;
                    info.position = ri;
                    info.normal = rn;
                    info.distance = tmp;
                                        
                    if (material.hasTexture())
                        {
                            info.color = this.material.getColor(vTmp , uTmp);
                        }       
                    else{
                        info.color = getMaterial().getColor(0,0);
                    }
                    return info;
                }
            }
        }
        return info;
    }

    public static Boolean triangleTest(final Vector3D vecT0, final Vector3D vecT1, final Vector3D vecT2){
        final Vector3D sidea = triangleComputeSide(vecT0, vecT1);
        final Vector3D sideb = triangleComputeSide(vecT0, vecT2);
        final Vector3D sidec = sideb.vectorReduction(sidea);
                
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
