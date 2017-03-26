package tracer;

import bitmap.Bitmap;
import domain.Scene;
import exceptions.InvalidPixelException;

import java.io.IOException;

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
    int noOfThreads;
        
    /**
     * this is default constructor for TracerThread class.
     * @param threadName we use this argument to 
     * @param tracer instance of class we want to call its rayTrace() method. 
     * @param scene instance of class scene we want to raytrace.
     * @param viewport bitmap plane to show tracing result on it.
     * @param depth an integer that specified reflection depth
     * @param timer a boolean specifies whether to show render time or not.
     * @param noOfThreads number of threads specified by user.
     */
    public TracerThread (int threadName, RayTracer tracer, Scene scene,
                         Bitmap viewport, int depth, boolean timer, int noOfThreads) throws InterruptedException {
        rayTracer = tracer;
        this.scene = scene;
        this.viewport = viewport;
        this.depth = depth;
        this.timer = timer;
        this.noOfThreads = noOfThreads;
        this.threadName = Integer.toString(threadName);
        thread = new Thread (this, "Thread " + threadName);
        thread.start();
        thread.join();
    }
        
    public void run() {
        try {
            rayTracer.rayTraceScene(scene, viewport, depth, timer, threadName, noOfThreads);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidPixelException e) {
            e.printStackTrace();
        }
    }
        
}
