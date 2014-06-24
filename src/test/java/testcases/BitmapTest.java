package testcases;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import org.testng.TestNG;
import classes.Color;
import classes.Bitmap;

public class BitmapTest extends TestNG {

	private transient Bitmap bmp;
	private transient String ppmFile;

	protected void setUp() {		
		bmp = Bitmap.createNewBitmap(0,0);
		final Scanner keyboard = new Scanner(System.in);
		System.out.println("enter path to a ppm file:\n");
		ppmFile = keyboard.next();
		try {
			bmp.createBitmapFromFile(ppmFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		int i =1;
	}
	
	/**
	 * in setup we create a bitmap object from 
	 */
	public void testWriteToFile(){
		bmp.writeBitmapToFile(1, "Tempbitmap.PPM");
		assert(bmp.compareTwoFiles(ppmFile , "Tempbitmap.PPM"));
		final File temp = new File("Tempbitmap.PPM");
		temp.delete();
	}
	

	/**
	 * this method tests setPixel() method. first it creates a bitmap using
	 * class bitmap default constructor then it paints a single pixel using
	 * setPixel() method. finally it compares new pixel color with color we
	 * assigned to it using class color equals() method.
	 * @throws Exception 
	 */
	public void testSetPixel() throws Exception{

		final Color tempColor = new Color((byte)25,(byte)40,(byte)90);
		bmp.setPixel(3, 2,tempColor );
		assert(tempColor.equals(bmp.getSinglePixel(3, 2)));
	}
		
}
