package testcases;

import classes.Color;
import junit.framework.TestCase;

/**
 * test case for testing class Color
 * @author atefeh maleki
 */

public class ColorTest extends TestCase {

//	private Color color;
	private transient Color color2;
	private transient Color color3;

	
	/**
	 * in this setUp method a sample color object is created. we need this 
	 * object to do our tests. 
	 */
	protected void setUp() {
	//	color = new Color(100,-4,90);
		color2 = new Color(76,83,80);
		color3 = new Color(14,7,10);
	}

	/**
	 * test case to find the correctness of add method
	 * also it will test if the add of one component become grower than 255,it should return 255 instead.
	 */
	public void testAddColor(){
		Color testAdd;
		testAdd = color2.addColor(color3);
		String test;
		test = testAdd.getColorAsAsciiString();
		assertEquals("90 90 90",test);

	}
}



