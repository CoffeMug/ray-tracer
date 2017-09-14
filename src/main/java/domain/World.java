package domain;

import shapes.BaseShape;

import java.util.ArrayList;

/**
 * Created by amin on 2017-03-19.
 */
public class World {

    private final transient ArrayList<BaseShape> shapes;
    private final transient ArrayList<Light> lights;

    public ArrayList<BaseShape> getShapes() {
        return shapes;
    }

    public ArrayList<Light> getLights() {
        return lights;
    }

    public World(final ArrayList<BaseShape> shapes, final ArrayList<Light> lights) {
        this.shapes = shapes;
        this.lights = lights;
    }

}
