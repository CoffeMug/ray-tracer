package domain;

import bitmap.BitmapVariant;

/**
 * implementation of color type
 * @author Atefeh Maleki
 * @author Majid Khorsandi
 */
public class Color{
    private transient int red;
    private transient int blue;
    private transient int green;

    public Color(final int red, final int green, final int blue){
        this.blue = blue >= 0 ?  blue : 0;
        this.red = red >= 0 ?  red : 0;
        this.green = green >= 0 ?  green : 0;
    }

    public Color() {
        red=0;
        blue=0;
        green=0;
    }

    public void setColor(final Color newColor){
        this.red = newColor.red;
        this.green = newColor.green;
        this.blue = newColor.blue;
    }
    
    public int getRed(){
        return this.red;
    }
        
    public int getGreen(){
        return this.green;
    }
        
    public int getBlue(){
        return this.blue;
    }
    
    public String getColorByFormat(BitmapVariant variant) {
        switch (variant) {
            case PPM_ASCII: return String.format("%d %d %d ", this.red, this.green, this.blue);
            case PPM_BINARY: return String.format("%c%c%c", this.red & 0xFF, this.green & 0xFF, this.blue & 0xFF);
            default: return new String("");
        }
    }

    public Color addColor(final Color colorToBeAdded){
        final Color finalColor = new Color();
        finalColor.red = this.red + colorToBeAdded.red > 255 ? 255 : this.red + colorToBeAdded.red;
        finalColor.green = this.green + colorToBeAdded.green > 255 ? 255 : this.green + colorToBeAdded.green;
        finalColor.blue = this.blue + colorToBeAdded.blue > 255 ? 255: this.blue + colorToBeAdded.blue;
        return finalColor;
    }

    public Color multiplyColorByValue(final double intensity){
        final Color finalColor = new Color();
        finalColor.blue = (int)(this.blue * intensity > 255 ? 255 : this.blue*intensity);
        finalColor.green = (int)(this.green * intensity > 255 ? 255 : this.green*intensity);
        finalColor.red = (int)(this.red*intensity > 255 ? 255 : this.red*intensity);
        return finalColor;
    }

    @Override
    public boolean equals(final Object colorToBeCompared) {
        if (this == colorToBeCompared){
            return true;
        }
        if (!(colorToBeCompared instanceof Color)){
            return false;
        }
        final Color color = (Color) colorToBeCompared;
        return this.green == color.green && this.red == color.red && this.blue == color.blue;
    }
        
    @Override
    public int hashCode(){
        return red^green^blue;
    }
}


