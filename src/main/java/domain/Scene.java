package domain;

import domain.Color;
import scene.Camera;
import scene.World;

/**
 * abstract data type for scene. a scene contains camera, background,
 * shapes and lights
 * @author majid
 *
 */
public class Scene {
    public transient Color background;
    public transient Camera camera;
    public transient World world;

    public Scene(final Camera camera, final Color background, final World world){
        this.camera = camera;
        this.background = background;
        this.world = world;
    }
}
