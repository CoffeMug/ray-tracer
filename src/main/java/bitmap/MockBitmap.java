package bitmap;

import domain.Color;

/** this is a Mock bitmap class we use for integration and system tests.
 * all methods of this class are commented in Ibitmap interface.
 * @author majid
 *
 */
public class MockBitmap implements IBitmap {
    private transient int height = 4 ;
    private transient int width = 4 ;
    private transient Color[] colPixs = new Color[16] ;
        
    public MockBitmap(){
        for (int i=0; i<=15 ;i++){
            colPixs[i] = new Color(110,0,110);
        }
    }
        
    public int getWidth() {
                
        return width;
    }
        
    public int getHeight(){
                
        return height;
    }
        
    public Color getSinglePixel(final int xCord, final int yCord) throws Exception{
                
        if ((xCord>=0 && xCord<= width) && (yCord>=0 && yCord<= height)){
            Color pixel = new Color();
            pixel = colPixs[(yCord - 1) * this.width + xCord - 1];
            return pixel;
        }
        throw new RuntimeException("this is not a valid pixel");
                
    }
        
    public boolean writeBitmapToFile(final int variant, final String filePath) {
        boolean flag;
        if ((variant == 1 || variant == 2) && checkFilePath()) {
            flag = true;
        }
        else{
            flag = false;
        }
        return flag;
    }

    public void convertToJPG() {
        return;
    }

    private boolean checkFilePath() {
        // this should use a regular expression checker to check if our path is
        // valid path both in Windows and Linux os.
        return true;
    }
        
    public void setPixel(final int xCord, final int yCord, final Color colr) {
                
        this.colPixs[(xCord - 1) * this.width + yCord - 1] = colr;

    }
}
