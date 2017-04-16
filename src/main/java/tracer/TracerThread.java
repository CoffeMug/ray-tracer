package tracer;

import bitmap.Bitmap;
import domain.Scene;
import exceptions.InvalidPixelException;

/**
 * this class is implementing runnable interface to enable multi-threading.
 * @author majid
 *
 */
public class TracerThread implements Runnable {
    String threadName;
    Thread thread;
    RayTracer rayTracer;
    Scene scene;
    Bitmap viewport;
    int depth;
    Boolean timer;
    int lowerBound;
    int upperBound;

    public TracerThread (int threadName, RayTracer tracer, Scene scene,
                         Bitmap bitmap, int depth, boolean timer, int lowerBound,
                         int upperBound) throws InterruptedException {
        rayTracer = tracer;
        this.scene = scene;
        this.viewport = bitmap;
        this.depth = depth;
        this.timer = timer;
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
        this.threadName = Integer.toString(threadName);
        thread = new Thread (this, "Thread " + threadName);
        thread.start();
        thread.join();
    }
        
    public void run() {
        try {
            rayTracer.rayTraceScene(scene, viewport, depth, timer, lowerBound, upperBound);
        } catch (InvalidPixelException e) {
            e.printStackTrace();
        }
    }
        
}
