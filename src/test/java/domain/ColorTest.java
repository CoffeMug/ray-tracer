package domain;

import bitmap.BitmapVariant;
import org.junit.Before;

import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Test case for testing class Color
 * Color range is [0..255]
 * @author atefeh maleki
 */
public class ColorTest {

	private Color colorOne;
	private Color colorTwo;
	private Color colorThree;
	private Color colorFour;
	private Color colorFive;


	@Before
	public void setup() {
		colorOne = new Color(76,83,80);
		colorTwo = new Color(14,7,10);
		colorThree = new Color(200, 250, 175);
		colorFour = new Color(260, 270, 299);
		colorFive = new Color(-100, -35, -1);
	}

	@Test
	public void addColorWhenResultInRange() {
		Color tmpColor = colorOne.addColor(colorTwo);
		assertEquals(90, tmpColor.getRed());
		assertEquals(90, tmpColor.getBlue());
		assertEquals(90, tmpColor.getGreen());
	}

	@Test
	public void addColorWhenResultOutOfRange() {
		Color tmpColor = colorOne.addColor(colorThree);
		assertEquals(255, tmpColor.getRed());
		assertEquals(255, tmpColor.getGreen());
		assertEquals(255, tmpColor.getBlue());
	}

	@Test
	public void getColorAsAsciiColorsInRange() {
		assertEquals("76 80 83", colorOne.getColorByFormat(BitmapVariant.PPM_ASCII));
	}

	@Test
	public void getColorAsAsciiColorsOutOfRangePositive() {
		assertEquals("255 255 255", colorFour.getColorByFormat(BitmapVariant.PPM_ASCII));
	}

	@Test
	public void getColorAsAsciiColorsOutOfRangeNegative() {
		assertEquals("0 0 0", colorFive.getColorByFormat(BitmapVariant.PPM_ASCII));
	}

	@Test
	public void getColorAsBinaryColorsInRange() {
		assertEquals("LPS", colorOne.getColorByFormat(BitmapVariant.PPM_BINARY));
	}

	@Test
	public void getColorAsBinaryColorsOutOfRangeRangePositive() {
		assertEquals("ÿÿÿ", colorFour.getColorByFormat(BitmapVariant.PPM_BINARY));
	}

	@Ignore
	@Test
	public void getColorAsBinaryColorsOutOfRangeRangeNegative() {
		String tmpColors = String.format("%c%c%c", (-100&0xFF), (-35&0xFF), (-1&0xFF));
		assertEquals(tmpColors, colorFive.getColorByFormat(BitmapVariant.PPM_BINARY));
	}

}


