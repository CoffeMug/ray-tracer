package bitmap;

import domain.Color;

import java.io.*;
import java.util.Scanner;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;
import javax.imageio.stream.*;
import javax.imageio.ImageWriter;
import java.util.Iterator;;
import javax.imageio.ImageWriteParam;
import javax.imageio.IIOImage;

/**
 * this is an abstract data type for holding a bitmap information. bitmap class
 * is constituted from  three properties; width which holds bitmaps width, 
 * height which holds its height and an array of pixel abstract data type.
 * @author majid
 * @author amin
 * @see Color
 */
public class Bitmap implements IBitmap {
    private transient int type;
    private static final int PPM_ASCII = 1;
    private static final int PPM_RAW = 2;
    private transient int maxval;
    private transient int width;
    private transient int height;
    private transient Color[][] pixels;
    private transient int bitshift = -1;
    private transient int bits;
   
    /**
     * this constructor gets width and height and sets bitmap object
     * width and height.
     * @param width width of bitmap object in pixels.
     * @param height height of bitmap object in pixels.
     */
    public Bitmap(final int width, final int height){
        this.width = width;
        this.height = height;
        if (width>0 && height>0){
        	this.pixels = new Color[width][height];
        }   
    }
    
    public static Bitmap createNewBitmap(final int width, final int height){
    	return new Bitmap(width, height);
    }

    /**
     * returns bitmap object width
     */
    public int getWidth(){
        return width;
    }

    /** 
     * returns bitmap object height
     */
    public int getHeight(){
        return height;
    }

    /**
     * this method accepts x and y coordinates of a pixel and returns that
     * pixel object.
     * @param xCord x-coordinate of pixel.
     * @param yCord y-coordinate of pixel.
     */
    public Color getSinglePixel(final int xCord, final int yCord) throws Exception {
        if ((xCord >= 0 && xCord < width) && (yCord >= 0 && yCord < height)){
            Color pixel = new Color();
            pixel = pixels[xCord][yCord];
            return pixel;
        }
        throw new Exception("this is not a valid pixel!");
    }

    /**
     * this method reads bitmap file header section.
     * @param inp inputstream reading from file.
     * @throws IOException
     */
    public void readBitmapPropertiesFromFileHeaser(final InputStream inp) throws IOException {
        char char1, char2;
        
        char1 = (char) readByte(inp);
        char2 = (char) readByte(inp);
        checkFormatFromHeader(char1);
        checkFileType(char2);
        width = readInt(inp);
        height = readInt(inp);
        maxval = readInt(inp);
    }
    
    private void checkFormatFromHeader(char firstChar) throws IOException {
    	if ( firstChar != 'P' ){
            throw new IOException( "not a PPM file" );
        }
    }
    
    private void checkFileType(char bitMapFileSecondChar) throws IOException {
    	switch (bitMapFileSecondChar)
        {
        case '3':
            this.type = PPM_ASCII;
            break;
        case '6':
            this.type = PPM_RAW;
            break;
        default:
            throw new IOException( "not a standard PBM/PGM/PPM file" );
        }
    }
        
    /**
     * read pixel data into the filePixels array.
     * @param inp file input stream.
     * @throws IOException
     */
    private void readPixels(final InputStream inp) throws IOException {
        int red, green, blue;
        Color filePixels[][] = new Color[this.width][this.height];        
        Color pixelColour= null;

        for (int i = 0; i < this.width; i++ ){
            for (int j = 0; j < this.height; j++) {
                switch (this.type) {
                case PPM_ASCII:
                    red = readInt(inp);
                    green = readInt(inp);
                    blue = readInt(inp);
                    pixelColour = new Color(red, green, blue);
                    break;
                case PPM_RAW:
                    red = readByte(inp);
                    green = readByte(inp);
                    blue = readByte(inp);
                    if (maxval != 255){
                        red = fixDepth(red);
                        green = fixDepth(green);
                        blue = fixDepth(blue);
                    }
                    pixelColour = new Color(red, green, blue);
                    break;
                default: break;
                }

                filePixels[i][j] = pixelColour;
            }
        }
        this.pixels = filePixels;
    }

    /**
     * routine to read a byte.  Instead of returning -1 on
     * EOF, it throws an exception.
     * @param inp file input stream.
     * @return read byte.
     * @throws IOException
     */
    private static int readByte(final InputStream inp) throws IOException
    {
        final int binInp;
        try {
            binInp = inp.read();
        }
        catch (Exception io) {
            throw new EOFException();
        }
        return binInp;
    }

   
    /**
     * Utility routine to read a bit, packed eight to a byte, big-endian.
     * @param inp file input stream.
     * @return read bits.
     * @throws IOException
     */
    @SuppressWarnings("unused")
        private boolean readBit( final InputStream inp ) throws IOException
    {
        if ( bitshift == -1 )
            {
                bits = readByte( inp );
                bitshift = 7;
            }
        final boolean bit = ( ( ( bits >> bitshift ) & 1 ) != 0 );
        --bitshift;
        return bit;
    }

    /**
     * Utility routine to read a character, ignoring comments.
     * @param inp file input stream.
     * @return read character.
     * @throws IOException
     */
    private static char readChar( final InputStream inp ) throws IOException
    {
        char char1;

        char1 = (char) readByte( inp );
        if ( char1 == '#' )
            {
                do
                    {
                        char1 = (char) readByte( inp );
                    }
                while ( char1 != '\n' && char1 != '\r' );
            }

        return char1;
    }

    /**
     * Utility routine to read the first non-whitespace character.
     * @param inp file input stream.
     * @return read nonwhite char.
     * @throws IOException
     */
    private static char readNonwhiteChar( final InputStream inp ) throws IOException
    {
        char char1;

        do
            {
                char1 = readChar( inp );
            }
        while ( char1 == ' ' || char1 == '\t' || char1 == '\n' || char1 == '\r' );

        return char1;
    }

    /**
     * Utility routine to read an ASCII integer, ignoring comments.
     * @param inp file input stream.
     * @return read ASCII integer.
     * @throws IOException
     */
    private static int readInt( final InputStream inp ) throws IOException
    {
        char char1;
        int icont;

        char1 = readNonwhiteChar( inp );
        if ( char1 < '0' || char1 > '9' ){
            throw new IOException( "junk in file where integer should be" );
        }

        icont = 0;
        do
            {
                icont = icont * 10 + char1 - '0';
                char1 = readChar( inp );
            }
        while ( char1 >= '0' && char1 <= '9' );

        return icont;
    }


    /**
     * Utility routine to rescale a pixel value from a non-eight-bit maxval
     * @param tmp integer pixel value we want to rescale.
     * @return
     */
    private int fixDepth( final int tmp )
    {
        return ( tmp * 255 + maxval / 2 ) / maxval;
    }

    /**
     * Utility routine make an RGBdefault pixel from three color values
     * @param red red value of color.
     * @param green green value of color.
     * @param blue blue value of color.
     * @return RGBdefault pixel.
     */
    @SuppressWarnings("unused")
        private static int makeRgb( final int red, final int green, final int blue )
    {
        return 0xff000000 | ( red << 16 ) | ( green << 8 ) | blue;
    }

    public void convertToJPG() {
        BufferedImage img = null;
        File outputfile = null;
        try {
            img = new BufferedImage(this.width, this.height, BufferedImage.TYPE_INT_RGB);
            for (int i=0; i < this.height; i++) {
                for (int j=0; j < this.width; j++) {
                    Color cl = this.getSinglePixel(i,j);
                    int rgb = this.makeRgb(cl.getRed(), cl.getGreen(), cl.getBlue());
                    img.setRGB(i, j, rgb);
                }
            }
            outputfile = new File("output.jpeg");

            ImageOutputStream ios = ImageIO.createImageOutputStream(outputfile);
            Iterator<ImageWriter> iter = ImageIO.getImageWritersByFormatName("jpeg");
            ImageWriter writer = iter.next();
            ImageWriteParam iwp = writer.getDefaultWriteParam();
            iwp.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
            iwp.setCompressionQuality(0.95f);
            writer.setOutput(ios);
            writer.write(null, new IIOImage(img,null,null), iwp);
            writer.dispose();
        }
         
        catch (Exception e) {
            System.out.print(e.getMessage());
        }
    }

    /**
     * this method writes information of our bitmap class to an output file.
     * <pVect>it first tries to create the header section of the file then in a 
     * loop, creates the body part of the file based on PPM format.</pVect>
     * @param variant an integer which holds output file type if its value is 1
     * then it shows that the method should write to file in ASCII format. if 
     * its 2 then the output shall be written in Bitmap format.
     */
    public boolean writeBitmapToFile(final int variant, final String filePath) {
        final String magic = variant == PPM_ASCII ? "P3" : "P6";
        PrintWriter outputStream = null;
        try
            {
                outputStream = new PrintWriter(new FileOutputStream(filePath));
            }
        catch(FileNotFoundException e)
            {
                System.out.println("Error opening the file bitmap");
                return false ;
            }
        outputStream.printf("%s\n", magic);
        outputStream.printf("#CREATOR: ray tracer\n");
        outputStream.printf("%d %d\n", this.width, this.height);
        outputStream.printf( "255\n");

        for (int i=0; i < this.height; i++) {
            for (int j=0; j < this.width; j++) {
                if (variant == PPM_ASCII){
                    outputStream.printf(this.pixels[i][j].getColorByFormat(BitmapVariant.PPM_ASCII) + " \n");
                }
                else{
                    outputStream.printf(this.pixels[i][j].getColorByFormat(BitmapVariant.PPM_RAW));
                }
            }
        }                
        outputStream.close();
        return true;
    }

    /**
     * this methods works in reverse to the writeBitmapToFile() method.It
     * gets the name of the file to read information from and starts reading
     * information from file and save it to an instance of our bitmap class.
     * @param variant an integer holds input file type(BINARY or ASCII)
     * @param fileName name of bitmap file to read data from
     * @throws exception of type IOException if can not find bitmap file.
     */

    public void createBitmapFromFile(final String fileName) throws IOException {
        //String word;
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(fileName);
            readBitmapPropertiesFromFileHeaser(inputStream);
            readPixels(inputStream);
        } 
        catch (FileNotFoundException e) {
            System.out.println("file not found" + fileName);
            throw new RuntimeException(e.getMessage());
        }
        finally {
            inputStream.close();
        }

    }


    public void setPixel(final int xCord, final int yCord, final Color colr) {
        this.pixels[xCord][yCord] = colr;
    }

    /**
     * this is a method we use to compare two bitmap files.
     * @param file1 path to first file.
     * @param file2 path to second file.
     * @return true if files are identical and false if they are not.
     */
    public boolean compareTwoFiles(final String file1, final String file2){
        boolean flag = true ; 
        try 
            {
                final Scanner file3 =  new Scanner(new FileInputStream(file1));
                final Scanner file4 =  new Scanner(new FileInputStream(file2));

                while( file3.hasNext() && file4.hasNext())
                    {
                        final String stringRead1 = file3.next();
                        final String stringRead2 = file4.next();
                        for (int i=0;i<=(stringRead1.length()>=stringRead2.length()
                                         ?stringRead2.length()-1:stringRead1.length()-1);i++){
                            if (stringRead1.charAt(i) != stringRead2.charAt(i)){
                                flag = false;
                                break;
                            }
                        }

                    }
            }
        catch(FileNotFoundException fnfe)
            {
                System.out.println( "Files were not found ");
            }
        return flag; 
    }
}
