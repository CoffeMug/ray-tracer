package domain;

/**
 * Implementation of Ray
 * @author Behzad
 */
public class Ray {
    private final Vector origin;
    private final Vector direction;

    public Ray(final Vector origin, final Vector direction) {
        this.origin = origin;
        if (direction.isNormal()) {
                this.direction = direction;
            }
        else { 
            this.direction = direction.normalize();
        }
    }

    public Vector intersectionPoint(final double tmp) {
        if(tmp <= 0 || ! this.direction.isNormal()){
            System.out.print("v is not normalized, or t is not greater than 0");
        }
        return this.origin.vectorAddition(direction.vectorMultiply(tmp));
    }

    public Vector getDirection() {
        return this.direction;
    }

    public Vector getOrigin() {
        return this.origin;
    }

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

    @Override
    public int hashCode(){
        return Vector.class.hashCode();
    }

}
