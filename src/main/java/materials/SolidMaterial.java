/**
 * 
 */
package materials;


import classes.Color;

/**
 * @author majid
 *
 */
public class SolidMaterial extends BaseMaterial {

    private transient final Color color;
    public transient double diffuse = 1;
        
    public SolidMaterial(final Color color, final double reflection, final double diffuse)
    {
        super(); // for solving PMD error
        this.color = color;
        this.reflection = reflection;
        this.diffuse = diffuse;
        //         this.Transparency = transparency;
        //         this.Gloss = gloss;

    }

    @Override public boolean hasTexture() { 
        return false;
    }

    @Override public Color getColor(final double uDbl, final double vDbl){
        return color;
    }
        
    @Override public double getDiffuse(){
        return this.diffuse;
    }
        
}
