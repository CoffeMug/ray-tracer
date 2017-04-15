/**
 * 
 */
package bitmap;

import domain.Color;
import exceptions.InvalidPixelException;

import java.io.IOException;

/** This is an interface for class bitmap. We use this interface to implement
 * some Mock objects for unit testing and integrity testing.
 * @author majid
 *
 */
public interface IBitmap {

    int getWidth();

    int getHeight();

    /**
     * this method gets x and y coordinates of a single pixel in our bitmap
     * objects and returns the color of that pixel.
     * @param xCord X coordinate of the pixel in bitmap matrix.
     * @param yCord Y coordinate of the pixel in bitmap matrix.
     * @return color of the pixel in (x,y) coordinate.
     */
    Color readPixel(int xCord, int yCord) throws Exception;

    void writeBitmapToFile(BitmapVariant variant, String filePath) throws IOException;

    void convertPPMToJPG();

    void writePixel(int xCord, int yCord, Color color) throws InvalidPixelException;
}
