package domain;

import org.junit.Before;
import org.junit.Test;

import static junit.framework.TestCase.assertTrue;

public class RayTest {

	private Ray ray;
	private Vector intersectionVector;

	@Before
	public void setUp()throws Exception{
		ray = new Ray(new Vector(1,2,3), new Vector(4,5,6));
		intersectionVector = new Vector(9,12,15);
	}

/*    @Test
    public void intersectionVector(){
        assertTrue(intersectionVector.equals(ray.intersectionPoint(2)));
    }*/
}