package materials;

import bitmap.IBitmap;
import domain.Color;
import exceptions.InvalidPixelException;


/** this class extends base material class and is used when we want to 
 * project a picture on a shape as its material.
 * @author majid
 *
 */
public class TextureMaterial extends BaseMaterial {

    private IBitmap texture;
     
    /**
     * this constructor gets texture object a texture material should 
     * represent and the reflection coefficient of material
     * @param text texture object.
     * @param reflection amount of reflection the texture should implement.
     */
    public TextureMaterial(final IBitmap text, final double reflection, final double diffuse)
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
    public Color getColor(final double ui, final double vi) throws InvalidPixelException {
        double uu = (ui) * this.texture.getWidth();
        double vv = (vi) * this.texture.getHeight();
        return texture.readPixel((int)uu,(int)vv);
    }
}

