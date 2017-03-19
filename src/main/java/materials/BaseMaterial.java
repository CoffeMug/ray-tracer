/**
 * 
 */
package materials;

import domain.Color;

/** material is what covers shape surface.
 * 
 * @author majid
 *
 */
public abstract class BaseMaterial implements IMaterial{
    private double reflection;
    private transient double diffuse;

    public BaseMaterial(final double diffuse, final double reflection) {
        this.diffuse = diffuse;
        this.reflection = reflection;
    }

    public double getReflection() {
        return reflection;
    }

    public void setReflection(double reflection) {
        this.reflection = reflection;
    }

    public double getDiffuse() {
        return diffuse;

    }

    public void setDiffuse(double diffuse) {
        this.diffuse = diffuse;
    }

    public abstract Color getColor(double uDbl, double vDbl);
    public abstract boolean hasTexture();

}
