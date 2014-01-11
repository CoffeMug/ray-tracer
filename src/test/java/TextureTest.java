/**
 * 
 */
package testcases;


import junit.framework.TestCase;
import materials.Texture;

import classes.Color;
import classes.MockBitmap;

/**
 * @author majid
 *
 */
public class TextureTest extends TestCase {

	private transient MockBitmap bmp;
	 
	

	public void setUp() throws Exception {
		bmp = new MockBitmap();
	}

	/**
	 * @throws java.lang.Exception
	 */

	public void tearDown() throws Exception {
		bmp = null;
	}

	/**
	 * Test method for {@link materials.Texture#FromBitmap(classes.Bitmap)}.
	 * it gets pixel information of a mock bitmap class and as we know what 
	 * are pixels in mock bitmap class, we can check to see if this method 
	 * gets all pixels correctly or not.
	 * @throws Exception 
	 */

	public void testFromBitmap() throws Exception {
	
		assertEquals(4, Texture.fromBitmap(bmp).colorMap.length) ;
		assertEquals(4, Texture.fromBitmap(bmp).colorMap[0].length) ;
		final Color col = new Color(110, 0, 110);
		for (int i=0; i<4; i++){
			for (int j=0;j<4;j++){
				assertEquals(col, Texture.fromBitmap(bmp).colorMap[i][j]);
			}
		}
		
		
	}

}
