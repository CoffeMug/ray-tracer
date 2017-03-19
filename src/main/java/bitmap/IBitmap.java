/**
 * 
 */
package bitmap;

import domain.Color;

/** this is an interface for class bitmap. we use this interface to implement
 * some Mock objects for unit testing and integrity testing.
 * @author majid
 *
 */
public interface IBitmap {
    /**
     * returns bitmap width.
     * @return an integer representing bitmap width.
     */
    int getWidth();
        
    /**
     * returns bitmap height.
     * @return an integer representing bitmap height.
     */
    int getHeight();
        
    /**
     * this method gets x and y coordinates of a single pixel in our bitmap
     * objects and returns the color of that pixel.
     * @param xCord X coordinate of the pixel in bitmap matrix.
     * @param yCord Y coordinate of the pixel in bitmap matrix.
     * @return color of the pixel in (x,y) coordinate.
     */
    Color getSinglePixel (int xCord, int yCord) throws Exception;
        
    /**
     * bitmap class uses this method locally to write its 2-D pixel array
     * to a corresponding PPM file.
     * @param variant type of PPM file we are going to write to. it can be
     * either ASCII or BINARY.
     * @param filePath path to the file we are going to write to.
     * @return true if pixels are written to file successfully and false if
     * not.
     */
    boolean writeBitmapToFile(int variant, String filePath);
        

    void convertToJPG();


    /**
     * this method searches for a pixel in our bitmap ADT having pixel's
     * X and Y coordinates. then given a color c, it will change pixel's
     * color to c. 
     * @param xCord x coordinate of pixel we are going to change its color.
     * @param yCord y coordinate of pixel we are going to change its color.
     * @param colr instance of class Color
     */
    void setPixel(int xCord, int yCord, Color colr);
}
