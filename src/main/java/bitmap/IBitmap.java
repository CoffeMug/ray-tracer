package bitmap;

import domain.Color;

/** This is an interface for class bitmap. We use this interface to implement
 * some Mock objects for unit testing and integrity testing.
 * @author majid
 *
 */
public interface IBitmap {

    int getWidth();

    int getHeight();

    Color readPixel(int xCord, int yCord);

    void writeBitmapToFile(BitmapVariant variant, String filePath);

    void writePixel(int xCord, int yCord, Color color);
}
