/**
 * 
 */
package materials;

import domain.Color;


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
    public TextureMaterial(final Texture text, final double reflection, final double diffuse)
    {
        super(diffuse, reflection); // used for suppressing PMD warning
        this.texture = text;
    }

    @Override 
    public boolean hasTexture() {
        return true;
    }

    /**
     * using texture class, this method gets color of a specified color on 
     * the shape in vector coordinates, according to its corresponding color
     * on a texture object in X,Y coordinates.
     */
    @Override 
    public Color getColor(final double ui, final double vi){
        double uu = (ui) * this.texture.getWidth();
        double vv = (vi) * this.texture.getHeight();
        Color c1 = texture.getColorMap()[(int)uu][(int)vv];
        return c1;
    }

}

