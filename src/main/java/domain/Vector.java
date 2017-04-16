package domain;

/**
 * Implementation of Vector type
 * A vector is composed by 3 double numbers   
 * @author Behzad Oskooi
 */
public class Vector {
    private final double x;
    private final double y;
    private final double z;

    public Vector(){
        x = 0;
        y = 0;
        z = 0;
    }

    public Vector(final double x,
                  final double y,
                  final double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }


    public Vector(final Vector vector) {
        this.x = vector.x;
        this.y = vector.y;
        this.z = vector.z;

    }
        
    public double dotProduct(final Vector vector){
        return (this.x * vector.x +
                this.y * vector.y +
                this.z * vector.z);
    }    

    public Vector crossProduct(final Vector vector){
        return new Vector(this.y * vector.z - this.z * vector.y,
                            this.z * vector.x - this.x * vector.z,
                            this.x * vector.y - this.y * vector.x);
    }
        
    public Vector vectorAddition(final Vector vector){
        return new Vector(this.x + vector.x,
                            this.y + vector.y,
                            this.z + vector.z);
    }
        
    public Vector vectorReduction(final Vector vector){
        return new Vector(this.x - vector.x,
                            this.y - vector.y,
                            this.z - vector.z);
    }

    public Vector vectorMultiply(final double scale){
        return new Vector(this.x * scale,
                            this.y * scale,
                            this.z * scale);
    }
        
    public double vectorLength(){
        return Math.sqrt(this.x * this.x + this.y * this.y + this.z * this.z);
    }

    public Vector normalize(){
        return this.vectorMultiply(1/this.vectorLength());
    }
        
    public boolean isNormal(){
        return Double.compare(this.dotProduct(this), 1.0) == 0;
    }

    @Override 
    public boolean equals(final Object vector) {
        if(this == vector){return true;}
        if(!(vector instanceof Vector)) {return false;}
        final Vector newVector = (Vector)vector;
                
        if(newVector.x - this.x <= 0.00001  &&
           newVector.y - this.y <= 0.00001  &&
           newVector.z - this.z <= 0.00001){
            return true;
        }
        return false;           
    }
        
    @Override
    public int hashCode(){
        final long value = Double.doubleToLongBits(x + y + z);
        return (int) (value ^ (value >> 32));
    }
        
    @Override
    public String toString(){
        return String.format("{0},{1},{2}", x, y, z);
    }
}
