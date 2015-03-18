package classes;


/**
 * Implementation of Ray
 * @author Behzad
 */
public class Ray {
    private Vector3D origin;
    private Vector3D direction;
    //public Vector position;
    /**
     * constructor to create a ray from a point of origin and a 
     * direction vector
     * @param origin
     * @param direction
     */
    public Ray(final Vector3D origin, final Vector3D direction) {
        this.origin = origin;
        if (direction.normalizeCheck() == true) {
                this.direction = direction;
            }
        else { 
            this.direction = direction.normalize();
        }
    }

    /**
     * computes the point is on the line or not
     * @param tmp distance from ray origin to intersection point.
     * @return a Vector returned by method 
     * {@link #vectorAddition(Vector3D, Vector3D) vectorAddition
     * @throws Exception 
     */
    public Vector3D intersectionPoint(final double tmp) {
        if(tmp <= 0 || ! this.direction.normalizeCheck()){
            System.out.print("v is not normalized, or t is not greater than 0");
        }
        return this.origin.vectorAddition(direction.vectorMultiply(tmp));
    }

    public Vector3D getDirection() {
        return this.direction;
    }

    public void setDirection(final Vector3D direction) {
        this.direction = direction;
    }

    public Vector3D getOrigin() {
        return this.origin;
    }

    public void setOrigin(final Vector3D origin) {
        this.origin = origin;
    }


    /**
     * we override method equals() in class ray to be able to compare two rays.
     * here it is not necessary to implement method hashCode() because, ray
     * uses class vector3D equals method which has hashCode() method.
     */
    @Override  
    public boolean equals(final Object ray) {

        if(this == ray){return true;}
        if(!(ray instanceof Ray)) {return false;}
        final Ray newRay = (Ray)ray;

        if(this.direction.equals(newRay.direction) && this.origin.equals(newRay.origin)) {
            return true;
        }
        return false;           
    }
}
