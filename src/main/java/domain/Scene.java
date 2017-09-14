package domain;

/**
 * abstract data type for scene. a scene contains camera, background,
 * shapes and lights
 * @author majid
 *
 */
public class Scene {
    private final transient Color background;
    private final transient Camera camera;
    private final transient World world;

    public Color getBackground() {
        return background;
    }

    public Camera getCamera() {
        return camera;
    }

    public World getWorld() {
        return world;
    }

    public Scene(final Camera camera, final Color background, final World world){
        this.camera = camera;
        this.background = background;
        this.world = world;
    }
}
