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
 
public class TracerGUIObserver implements Observer {

    private final Logger logger = LoggerFactory.getLogger(TracerGUIObserver.class);

    TracerParam param;

    private final Bitmap viewport = new Bitmap();

    @Override
    public void update(Observable obj, Object arg) {
        if (arg instanceof TracerParam) {

            param = (TracerParam) arg;

            logger.info("The scene file is {} ", param.getSceneFile());

            final XmlParser parser = new XmlParser();

            computeImage(param.getXpix(), param.getYpix(), param.getWidth(), param.getHeight(), param);

            // Build the view port
            viewport.withWidth(param.getWidth())
                    .withHeight(param.getHeight())
                    .withPixels(new Color[param.getWidth()][param.getHeight()]);

            final Scene scene = parser.parseXmlFile(param.getSceneFile());

            final RayTracer rayTracer = new RayTracer(
                    param.getRenderDiffuse(),
                    param.getRenderShadows(),
                    param.getRenderReflection());

            scene.camera.setHeight(param.getHeight());
            scene.camera.setWidth(param.getWidth());
            scene.camera.setZoom(param.getZoom());
            scene.camera.setXpix(param.getXpix());
            scene.camera.setYpix(param.getYpix());

            // Single thread tracer
            if (param.getNoOfThreads() == 1){
                try {
                    rayTracer.rayTraceScene(scene, viewport, param.getDepth(), param.getEnableTimer(),
                            "1", param.getNoOfThreads());
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InvalidPixelException e) {
                    e.printStackTrace();
                }
            }

            // Multiple threads tracer
            else {
                for (int threadNumber=1; threadNumber <= param.getNoOfThreads(); threadNumber++){
                    new TracerThread(threadNumber, rayTracer, scene, viewport,
                            param.getDepth(),
                            param.getEnableTimer(),
                            param.getNoOfThreads());
                }
            }
        }
        try {
            viewport.writeBitmapToFile(BitmapVariant.PPM_ASCII, param.getOutputFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
        viewport.convertPPMToJPG();

    }

    /**
     * this method computes the image properties based on its arguments
     * parse from input
     */
    private static void computeImage(final int xx, final int yy, final int ww, final int hh, TracerParam param) {
        if(xx == 0)
            param.setXpix(ww*yy/hh);
        if(yy == 0)
            param.setYpix(hh*xx/ww);
        if(ww == 0)
            param.setWidth(xx*hh/yy);
        if(hh == 0)
            param.setHeight(ww*yy/xx);
    }
}
