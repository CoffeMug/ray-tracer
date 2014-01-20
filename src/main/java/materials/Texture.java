/**
 * 
 */
package materials;

import interfaces.IBitmap;


import classes.Color;

/**
 * @author majid
 * texture class is a 2X2 array of colors. we fill this array with a ppm 
 * picture we want to project on our shapes. 
 */
public class Texture {
	
	 public transient int width;
     public transient int height;
     public transient Color[][] colorMap;

     
     public Texture(final Color[][] colormap){
         width = colormap.length;
         height = colormap[0].length;
         colorMap = colormap;
     }

/**
 * with this method we can create a texture using a ppm file.
 * @param bmp1 physical path pointing to a ppm file on disk
 * @return an object of type texture which is a 2-dimensional array
 * of type color. later we will use this array in class textureMaterial
 * to project a ppm file as a texture on a shape.
 * @throws Exception
 */
     static public Texture fromBitmap(final IBitmap bmp1) throws Exception{
         Color[][] colormap = new Color[bmp1.getWidth()][bmp1.getHeight()];
         final Texture texture = new Texture(colormap);
         
         for (int y = 1; y <= texture.height; y++){
        	 for (int x = 1; x <= texture.width; x++){
        		 colormap[x-1][y-1] = bmp1.getSinglePixel(x, y);
        	 }
         }
         return texture;
     }

   }
