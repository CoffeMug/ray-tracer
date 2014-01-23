/**
 * 
 */
package materials;

import classes.Color;


/** this class extends base material class and is used when we want to 
 * project a picture on a shape as its material.
 * @author majid
 *
 */
public class TextureMaterial extends BaseMaterial {

    public transient Texture texture;
     
    /**
     * this constructor gets texture object a texture material should 
     * represent and the reflection coefficient of material
     * @param text texture object.
     * @param reflection amount of reflection the texture should implement.
     */
    public TextureMaterial(final Texture text, final double reflection)
    {
        super(); // used for suppressing PMD warning
        this.reflection = reflection;
        this.texture = text;
    }

    @Override public boolean hasTexture() {
        return true;
    }

    /**
     * using texture class, this method gets color of a specified color on 
     * the shape in vector coordinates, according to its corresponding color
     * on a texture object in X,Y coordinates.
     */
    @Override public Color getColor(final double ui, final double vi){
        double uu = (ui)  * texture.width;
        double vv = (1-vi) * texture.height;
        Color c1 = texture.colorMap[(int)uu][(int)vv];

        return c1;
    }
     
    @Override    public  double getDiffuse(){
        return 1.0;
    }
}

