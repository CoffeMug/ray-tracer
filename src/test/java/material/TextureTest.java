package material;

import bitmap.MockBitmap;
import junit.framework.TestCase;
import materials.Texture;

import domain.Color;
import org.junit.Test;

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


	@Test
	public void testFromBitmap() throws Exception {

		assertEquals(4, Texture.fromBitmap(bmp).getColorMap().length) ;
		assertEquals(4, Texture.fromBitmap(bmp).getColorMap()[0].length) ;
		final Color col = new Color(110, 0, 110);
		for (int i=0; i<4; i++){
			for (int j=0;j<4;j++){
				assertEquals(col, Texture.fromBitmap(bmp).getColorMap()[i][j]);
			}
		}


	}

}