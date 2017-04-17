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

    public TextureMaterial(final IBitmap text, final double reflection, final double diffuse)
    {
        super(diffuse, reflection);
        this.texture = text;
    }

    @Override 
    public boolean hasTexture() {
        return true;
    }

    @Override 
    public Color getColor(final double ui, final double vi) throws InvalidPixelException {
        double uu = Math.floor(ui * this.texture.getWidth());
        double vv = Math.floor(vi * this.texture.getHeight());
        return texture.readPixel((int)uu, (int)vv);
    }
}

