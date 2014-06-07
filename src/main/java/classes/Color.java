package classes;

/**
 * implementation of color type
 * @author Atefeh Maleki
 * @author Majid Khorsandi
 */

public class Color {
    private transient int red;
    private transient int blue;
    private transient int green;

    /**
     * constructor to create color from 3 RGB values
     * @param red red component of the color being created.
     * @param blue blue component of the color being created.
     * @param green green component of the color being created.
     */
    public  Color ( final int Red,final int Green,final int Blue){
        this.blue = Blue >= 0 ?  Blue : 0;
        this.red = Red >= 0 ?  Red : 0;
        this.green = Green >= 0 ?  Green : 0;
    }

    /**this is default constructor and by default sets color to black. 
     */
    public Color (){
        red=0;
        blue=0;
        green=0;
    }

    /**
     * setter method for class color.
     * @param newColor the color we are going to assign to this color instance.
     */
    public void setColor(final Color newColor){
        this.red= newColor.red;
        this.green = newColor.green;
        this.blue = newColor.blue;
    }
    /**
     * returns red component of color
     * @return integer as red component of color
     */
    public int getRed(){
        return this.red;
    }
        
    /**
     * returns green component of color
     * @return integer as green component of color
     */
    public int getGreen(){
        return this.green;
    }
        
    /**
     * returns blue component of color
     * @return integer as blue component of color
     */
    public int getBlue(){
        return this.blue;
    }
    /**
     * this method returns color as a string value. for example if RGB
     * components of color are 70 90 120, this class will return a
     * string like "70 90 120". this method would be most useful when
     * we want to write contents of our bitmap color in a file.
     * @return white space separated components of color as a string.
     */
    public String getColorAsAsciiString(){
        return String.format("%d %d %d", this.red & 0xFF , this.green& 0xFF , this.blue& 0xFF) ; 
    }

    public String getColorAsBinaryString(){
        return String.format("%c %c %c", this.red & 0xFF , this.green& 0xFF , this.blue& 0xFF);
    }
        
    /**
     * class for blending the colors with each other,every RGB component will be added respectively and 
     * create new color.
     * @param clr is new RGB component which become as an parameter to this class and with some operation with the last one ,
     * new color be created.
     * @return the new color which will be made
     */
    public Color addColor(final Color clr){
        final Color colAdd = new Color();
        colAdd.red =((this.red)+(clr.red))>255?255:
            ((this.red)+(clr.red));
        colAdd.green=((this.green)+(clr.green))>255?255:
            ((this.green)+(clr.green));
        colAdd.blue=((this.blue)+(clr.blue))>255?255:
            ((this.blue)+(clr.blue));
        return colAdd;
    }

    /**
     * class for blending the colors with each other,every RGB component will be multiplied respectively and 
     * create new color.
     * @param clr is new RGB component which become as an parameter to this class and with some operation with the last one ,
     * new color be created.
     * @return the new color which will be made
     */
    public Color multiColor(final Color clr){
        final Color colMult = new Color();
        colMult.blue = ((this.blue*clr.blue))>255 ? 255:
            ((this.blue*clr.blue));
        colMult.green = ((this.green*clr.green)) > 255 ? 255:
            ((this.green*clr.green));
        colMult.red = ((this.red*clr.red))>255 ? 255:
            ((this.red*clr.red));
        return colMult;
    }

    /**
     * class for increasing or decreasing the colors intensity by multiplying
     * the color with a numeral value.
     * @param clr is double value which become as a parameter to this class
     * and with some operation with the last one ,new color will be created.
     * @return the new color which will be made
     */
    public Color multiColor(final double clr){
        final Color  colMult = new Color();
        colMult.blue = (int)((this.blue*clr)>255 ? 255 :
                             (this.blue*clr));
        colMult.green = (int)((this.green*clr)>255 ? 255 :
                              (this.green*clr));
        colMult.red = (int)((this.red*clr)>255 ? 255 :
                            (this.red*clr));
        return colMult;
    }

    /**
     * the limit module which will limit the amount of color between 0 and 1 instead of 0 and 255
     */
    public void limit()
    {
        red = (int)((red > 0.0) ? ( (red > 1.0) ? 1.0f : red ) : 0.0f);
        green = (int)((green > 0.0) ? ( (green > 1.0) ? 1.0f : green ) : 0.0f);
        blue = (int)((blue > 0.0) ? ( (blue > 1.0) ? 1.0f : blue ) : 0.0f);
    }
        
    /**
     * this class implements equals method so we can check to see if 2 different 
     * instances of Color class are equal or not. the method uses 
     * <code>getColorAsString()</code> method of each object to do evaluation.
     * @return true if <code>aColor</code> is equal to <code>this</code> and
     * false if <code>aColor</code> is not equal to <code>this</code>
     */
    @Override public boolean equals(final Object aColor) {
        boolean result;
        if ( this == aColor ){
            result = true;
        }

        if ( !(aColor instanceof Color) ) {
            result =  false;
        }

        final Color color = (Color) aColor;
        result = this.green == color.green && this.red == color.red && this.blue == color.blue;
        return result;
    }
        
    /**
     * this is the hashcode() method to support class equals() method
     */
    @Override public int hashCode(){
        return red ^ green ^ blue;
    }
}


