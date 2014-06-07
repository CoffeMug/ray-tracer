/**
 * 
 */
package materials;

import classes.Color;
import interfaces.IMaterial;

/** material is what covers shape surface.
 * 
 * @author majid
 *
 */
public abstract class BaseMaterial implements IMaterial{
    public abstract Color getColor(double uDbl, double vDbl);
    public abstract boolean hasTexture();
    public double reflection;
                
    public void setReflection(final double ref){
        this.reflection = ref;
    }
    public double getReflection(){
        return this.reflection;
    }
        
    /**
     * some extra necessary calculations on material object.
     * @param tin
     * @return
     */
    protected double wrapUp(final double tin)
    {
        double tmp = tin;
        tmp = tmp % 2.0;
        if (tmp < -1){
            tmp = tmp + 2.0;
        }
         
        if (tmp >= 1){
            tmp -= 2.0;
        }
        return tmp;
    }
}
