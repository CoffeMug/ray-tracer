package domain;

import org.junit.Before;
import org.junit.Test;
import scene.Camera;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertFalse;

/**
 * The bitmap case for camera class which will bitmap the Camera constructors and the get ray method.
 * by using different initialization arguments.
 * @author amin
 *
 */
public class CameraTest {

	public Vector position;
	public Vector lookat;
	public Vector upward;
	public Camera testCamera;


	@Before
	public void setUp() {
		position = new Vector(2, 2, -5);
		lookat = new Vector(2, 2, 0);
		upward = new Vector(0, 1, 0);
	}

	@Test
	public void getRayCameraInArbitraryLocation() {
		//@formatter:off
        final Ray ray = new Ray(new Vector(2, 2, -5),
                                new Vector(-0.4818, 0.4818, 0.7319));

        final Camera camera = new Camera(position, lookat, upward);
        assertTrue(ray.equals(camera.getRay(2, 2)));
        //@formatter:on
	}

}