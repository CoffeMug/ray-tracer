package domain;

import shapes.BaseShape;

import java.util.ArrayList;

/**
 * Created by amin on 2017-03-19.
 */
public class World {

    public transient ArrayList<BaseShape> shapes;
    public transient ArrayList<Light> lights;

    public World(final ArrayList<BaseShape> shapes, final ArrayList<Light> lights) {
        this.shapes = shapes;
        this.lights = lights;
    }

}
