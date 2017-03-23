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
 * this is an abstract data variant for holding a bitmap information. bitmap class
 * is constituted from  three properties; width which holds bitmaps width, 
 * height which holds its height and an array of pixel abstract data variant.
 * @author majid
 * @author amin
 * @see Color
 */
public class Bitmap implements IBitmap {
    private transient BitmapVariant variant;
    private transient int maximumColorComponentValue;
    private transient int width;
    private transient int height;
    private transient Color[][] pixels;

    public Bitmap(final int width, final int height){
        this.width = width;
        this.height = height;
      	this.pixels = new Color[width][height];
    }

    public int getWidth(){
        return width;
    }

    public int getHeight(){
        return height;
    }

    public Color readPixel(final int x, final int y) throws Exception {
        if ((x >= 0 && x < width) && (y >= 0 && y < height)){
            return pixels[x][y];
        }
        throw new Exception("This is not a valid pixel!");
    }

    public void writePixel(final int x, final int y, final Color color) {
        this.pixels[x][y] = color;
    }

    public void readBitmapPropertiesFromFile(final InputStream inputStream) throws IOException {
        checkFormatFromHeader(readChar(inputStream));
        checkFileType(readChar(inputStream));
        width = readInt(inputStream);
        height = readInt(inputStream);
        maximumColorComponentValue = readInt(inputStream);
    }

    public void convertPPMToJPG() {
        BufferedImage image;
        File outfile;
        try {
            image = new BufferedImage(this.width, this.height, BufferedImage.TYPE_INT_RGB);
            for (int i=0; i < this.width; i++) {
                for (int j=0; j < this.height; j++) {
                    Color cl = this.readPixel(i, j);
                    int rgb = this.makeRgb(cl.getRed(), cl.getGreen(), cl.getBlue());
                    image.setRGB(i, j, rgb);
                }
            }
            outfile = new File("output.jpeg");

            ImageOutputStream ios = ImageIO.createImageOutputStream(outfile);
            Iterator<ImageWriter> iter = ImageIO.getImageWritersByFormatName("jpeg");
            ImageWriter writer = iter.next();
            ImageWriteParam iwp = writer.getDefaultWriteParam();
            iwp.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
            iwp.setCompressionQuality(0.95f);
            writer.setOutput(ios);
            writer.write(null, new IIOImage(image,null,null), iwp);
            writer.dispose();
        }

        catch (Exception e) {
            System.out.print(e.getMessage());
        }
    }

    public void writeBitmapToFile(final BitmapVariant variant, final String filePath) throws IOException {
        final String magic = variant == BitmapVariant.PPM_ASCII ? "P3" : "P6";
        PrintWriter outputStream = null;
        try
        {
            outputStream = new PrintWriter(new FileOutputStream(filePath));
            outputStream.printf("%s\n", magic);
            outputStream.printf("# CREATOR: ray tracer\n");
            outputStream.printf("%d %d\n", this.width, this.height);
            outputStream.printf( "255\n");

            for (int i=0; i < this.height; i++) {
                for (int j=0; j < this.width; j++) {
                    if (variant == BitmapVariant.PPM_ASCII){
                        outputStream.printf(this.pixels[j][i].getColorByFormat(BitmapVariant.PPM_ASCII));
                    }
                    else {
                        outputStream.printf(this.pixels[j][i].getColorByFormat(BitmapVariant.PPM_BINARY));
                    }
                }
            }
        }
        catch(FileNotFoundException e)
        {
            System.out.println("Error opening the file to wirte the bitmap");
            throw new IOException();
        }
        finally {
            outputStream.close();
        }
    }

    /**
     * this methods works in reverse to the writeBitmapToFile() method.It
     * gets the name of the file to read information from and starts reading
     * information from file and save it to an instance of our bitmap class.
     * @param fileName name of bitmap file to read data from
     * @throws IOException of variant IOException if can not find bitmap file.
     */
   public void createBitmapFromFile(final String fileName) throws IOException {
        //String word;
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(fileName);
            readBitmapPropertiesFromFile(inputStream);
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

    private void checkFormatFromHeader(char firstChar) throws IOException {
    	if ( firstChar != 'P' ){
            throw new IOException( "not a PPM file" );
        }
    }
    
    private void checkFileType(char bitMapFileSecondChar) throws IOException {
    	switch (bitMapFileSecondChar) {
        case '3':
            this.variant = BitmapVariant.PPM_ASCII;
            break;
        case '6':
            this.variant = BitmapVariant.PPM_BINARY;
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
                switch (this.variant) {
                case PPM_ASCII:
                    red = readInt(inp);
                    green = readInt(inp);
                    blue = readInt(inp);
                    pixelColour = new Color(red, green, blue);
                    break;
                case PPM_BINARY:
                    red = readByte(inp);
                    green = readByte(inp);
                    blue = readByte(inp);
                    if (maximumColorComponentValue != 255){
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
     * Utility routine to read a character, ignoring comments.
     * @param inp file input stream.
     * @return read character.
     * @throws IOException
     */
    private static char readChar(final InputStream inp) throws IOException
    {
        char chr = (char)readByte(inp);
        if (chr == '#')
            {
                do
                    {
                        chr = (char) readByte(inp);
                    }
                while ( chr != '\n' && chr != '\r' );
            }

        return chr;
    }

    /**
     * Utility routine to read the first non-whitespace character.
     * @param inp file input stream.
     * @return read nonwhite char.
     * @throws IOException
     */
    private static char readNonwhiteSpaceChar(final InputStream inp ) throws IOException
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
     * @param inputStream file input stream.
     * @return read ASCII integer.
     * @throws IOException
     */
    private static int readInt(final InputStream inputStream) throws IOException
    {
        char chr;
        int icont;

        chr = readNonwhiteSpaceChar(inputStream);
        if ( chr < '0' || chr > '9' ){
            throw new IOException( "junk in file where integer should be" );
        }

        icont = 0;
        do
            {
                icont = icont * 10 + chr - '0';
                chr = readChar( inputStream );
            }
        while ( chr >= '0' && chr <= '9' );

        return icont;
    }


    /**
     * Utility routine to rescale a pixel value from a non-eight-bit maximumColorComponentValue
     * @param tmp integer pixel value we want to rescale.
     * @return
     */
    private int fixDepth( final int tmp )
    {
        return ( tmp * 255 + maximumColorComponentValue / 2 ) / maximumColorComponentValue;
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
}
