/**
 * 
 */
package materials;

import bitmap.IBitmap;


import domain.Color;

/**
 * @author majid
 * texture class is a 2X2 array of colors. we fill this array with a ppm 
 * picture we want to project on our shapes. 
 */
public class Texture {
    private int width;
    private int height; 
    private static transient Color[][] colorMap;
     
    public Texture(final Color[][] colormap){
        this.colorMap = colormap;
    }

    public Texture(final int width, final int height, final Color[][] colormap) {
        this.width = width;
        this.height = height;
        this.colorMap = colormap;
    }
    public Color[][] getColorMap() {
        return this.colorMap;
    }

    public void setColorMatp(final Color[][] map) {
        this.colorMap = map;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    /**
     * with this method we can create a texture using a ppm file.
     * @param bmp1 physical path pointing to a ppm file on disk
     * @return an object of type texture which is a 2-dimensional array
     * of type color. later we will use this array in class textureMaterial
     * to project a ppm file as a texture on a shape.
     * @throws Exception
     */
    public static Texture fromBitmap(final IBitmap bmp) throws Exception{
        Color[][] colormap = new Color[bmp.getWidth()][bmp.getHeight()];
        final Texture texture = new Texture(bmp.getWidth(), bmp.getHeight(), colormap);
         
        for (int y = 0; y < bmp.getHeight(); y++){
            for (int x = 0; x < bmp.getWidth(); x++){
                colorMap[x][y] = bmp.getSinglePixel(x, y);
            }
        }
        return texture;
    }

}
