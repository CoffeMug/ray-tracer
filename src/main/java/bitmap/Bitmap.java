package bitmap;

import domain.Color;
import exceptions.BitmapNotFoundException;
import exceptions.BitmapReadErrorException;
import exceptions.InvalidPixelException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.commons.io.IOUtils;

import java.io.*;

/**
 * this is an abstract data variant for holding a bitmap information. bitmap class
 * is constituted from  three properties; width which holds bitmaps width, 
 * height which holds its height and an array of pixel abstract data variant.
 * @author majid
 * @author amin
 * @see Color
 */
public class Bitmap implements IBitmap {

    private static final Logger logger = LoggerFactory.getLogger(Bitmap.class);

    private transient BitmapVariant variant;
    private transient int maximumColorComponentValue;
    private transient int width;
    private transient int height;
    private transient Color[][] pixels;


    public Bitmap() {

    }

    public Bitmap withWidth(int width) {
        this.width = width;
        return this;
    }

    public Bitmap withHeight(int height) {
        this.height = height;
        return this;
    }

    public Bitmap withPixels(Color[][] color) {
        this.pixels = color;
        return this;
    }

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

    public Color readPixel(final int x, final int y) throws InvalidPixelException {
        if (x >= 0 && x < width && y >= 0 && y < height){
            return pixels[x][y];
        } else {
            logger.error("Invalid pixel with the coordinates {} {}", x, y);
            throw new InvalidPixelException("This is not a valid pixel!");
        }
    }

    public void writePixel(final int x, final int y, final Color color) throws InvalidPixelException {
        if (x >= 0 && x < width && y >= 0 && y < height) {
            this.pixels[x][y] = color;
        } else {
            logger.error("Invalid pixel with the coordinates {} {}", x, y);
            throw new InvalidPixelException("This is not a valid pixel!");
        }
    }

    public void readBitmapPropertiesFromFile(final InputStream inputStream) throws IOException {
        checkFormatInHeader(readChar(inputStream));
        checkFileType(readChar(inputStream));
        width = readInt(inputStream);
        height = readInt(inputStream);
        maximumColorComponentValue = readInt(inputStream);
    }

    public void writeBitmapToFile(final BitmapVariant variant, final String filePath) {
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
            logger.error("Error opening bitmap file {} for write.", filePath);
            throw new BitmapNotFoundException(e.getMessage());
        }
        finally {
            outputStream.close();
        }
    }

   public IBitmap createBitmapFromFile(final String fileName) throws BitmapNotFoundException {
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(fileName);
            readBitmapPropertiesFromFile(inputStream);
            readPixels(inputStream);
        } catch (FileNotFoundException e) {
            logger.error("Bitmap file {} not found!", fileName);
            throw new BitmapNotFoundException(e.getMessage());
        } catch (IOException e) {
            logger.error("Error reading from the bitmap file {}", fileName);
            throw new BitmapReadErrorException(e.getMessage());
        } finally {
            IOUtils.closeQuietly(inputStream);
        }
        return this;
    }

    private void checkFormatInHeader(char firstChar) throws IOException {
    	if (firstChar != 'P'){
    	    logger.error("Not a standard PPM file!");
            throw new IOException();
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
            logger.error("Not a standard PBM/PGM/PPM file!");
            throw new IOException( "Not a standard PBM/PGM/PPM file" );
        }
    }

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

    private static int readByte(final InputStream inp) throws EOFException {
        final int binInp;
        try {
            binInp = inp.read();
        }
        catch (Exception io) {
            throw new EOFException();
        }
        return binInp;
    }

    private static char readChar(final InputStream inp) throws EOFException {
        char chr = (char)readByte(inp);
        if (chr == '#') {
            do {
                chr = (char) readByte(inp);
            }
            while (chr != '\n' && chr != '\r');
        }
        return chr;
    }

    private static char readNonwhiteSpaceChar(final InputStream inp ) throws EOFException {
        char char1;

        do {
            char1 = readChar(inp);
        }
        while (char1 == ' ' || char1 == '\t' || char1 == '\n' || char1 == '\r');

        return char1;
    }

    private static int readInt(final InputStream inputStream) throws IOException {
        char chr;
        int number;

        chr = readNonwhiteSpaceChar(inputStream);
        if (chr < '0' || chr > '9'){
            logger.error("Bad character in file; expecting integer.");
            throw new IOException( "junk in file where integer should be" );
        }

        number = 0;
        do {
            number = number * 10 + chr - '0';
            chr = readChar( inputStream );
        }
        while ( chr >= '0' && chr <= '9' );

        return number;
    }

    private int fixDepth( final int rgb ) {
        return (rgb * 255 + maximumColorComponentValue/2)/maximumColorComponentValue;
    }
}
