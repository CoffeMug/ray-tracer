package materials;

import bitmap.IBitmap;
import domain.Color;


/** this class extends base material class and is used when we want to 
 * project a picture on a shape as its material.
 * @author majid
 *
 */
public class TextureMaterial extends BaseMaterial {

    public IBitmap bitmap;
     
    public TextureMaterial(final IBitmap bitmap, final double reflection, final double diffuse)
    {
        super(diffuse, reflection); // used for suppressing PMD warning
        this.bitmap = bitmap;
    }

    @Override 
    public boolean hasTexture() {
        return true;
    }

    @Override 
    public Color getColor(final double ui, final double vi) {
        double uu = (ui) * this.bitmap.getWidth();
        double vv = (vi) * this.bitmap.getHeight();
        return bitmap.readPixel((int)uu, (int)vv);
    }
}

