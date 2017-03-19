package scene;

import domain.Light;
import shapes.Shapes;

import java.util.ArrayList;

/**
 * Created by amin on 2017-03-19.
 */
public class World {

    public transient Shapes shapes;
    public transient ArrayList<Light> lights;

    public World(final Shapes shapes, final ArrayList<Light> lights) {
        this.shapes = shapes;
        this.lights = lights;
    }

}
