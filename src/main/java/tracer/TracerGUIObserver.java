package tracer;

import bitmap.Bitmap;
import bitmap.BitmapVariant;
import domain.Color;
import domain.Scene;
import exceptions.InvalidPixelException;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import utils.XmlParser;

import java.io.IOException;
import java.util.Observable;
import java.util.Observer;
import java.util.stream.IntStream;

public class TracerGUIObserver implements Observer {

    private final Logger logger = LoggerFactory.getLogger(TracerGUIObserver.class);

    TracerParam param;

    private final Bitmap bitmap = new Bitmap();

    @Override
    public void update(Observable obj, Object arg) {
        if (arg instanceof TracerParam) {

            param = (TracerParam) arg;

            logger.info("The scene file is {} ", param.getSceneFile());

            final XmlParser parser = new XmlParser();

            // Build the bitmap
            bitmap
                    .withWidth(param.getWidth())
                    .withHeight(param.getHeight())
                    .withPixels(new Color[param.getWidth()][param.getHeight()]);

            final Scene scene = parser.parseXmlFile(param.getSceneFile());

            // Build rest of the camera
            scene.camera
                    .withHeight(param.getHeight())
                    .withWidth(param.getWidth())
                    .withZoom(param.getZoom())
                    .withXpix(param.getXpix())
                    .withYpix(param.getYpix());

            final RayTracer rayTracer = new RayTracer(param.getRenderDiffuse(), param.getRenderShadows(), param.getRenderReflection());

            // Single thread tracer
            if (param.getNoOfThreads() == 1){
                try {
                    rayTracer.rayTraceScene(scene, bitmap, param.getDepth(), param.getEnableTimer(),
                            "1", param.getNoOfThreads());
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InvalidPixelException e) {
                    e.printStackTrace();
                }
            }

            // Multiple threads tracer
            else {
                IntStream.rangeClosed(1, param.getNoOfThreads()).forEach(threadNumber -> {
                    try {
                        new TracerThread(threadNumber, rayTracer, scene, bitmap,
                                param.getDepth(),
                                param.getEnableTimer(),
                                param.getNoOfThreads());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                });
            }
        }
        try {
            bitmap.writeBitmapToFile(BitmapVariant.PPM_ASCII, param.getOutputFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
        bitmap.convertPPMToJPG();
    }

}
