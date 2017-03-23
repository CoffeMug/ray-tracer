package tracer;

import bitmap.Bitmap;
import domain.Scene;
import utils.XmlParser;

import java.io.IOException;
import java.util.Observable;
import java.util.Observer; 
 
public class TracerGUIObserver implements Observer {

    TracerParam param; 

    @Override
    public void update(Observable obj, Object arg) {
        if (arg instanceof TracerParam) {
            param = (TracerParam) arg;

            System.out.println("The scene file is:" + param.getSceneFile());

            final XmlParser parser = new XmlParser();

            computeImage(param.getXpix(), param.getYpix(), param.getWidth(), param.getHeight(), param);

            final Scene scene = parser.parseXmlFile(param.getSceneFile());
            final RayTracer rayt = new RayTracer(param.getRenderDiffuse(), param.getRenderShadows(), 
                                                 param.getRenderReflection());
            final Bitmap viewport = new Bitmap(param.getWidth(), param.getHeight());

            scene.camera.setHeight(param.getHeight());
            scene.camera.setWidth(param.getWidth());
            scene.camera.setZoom(param.getZoom());
            scene.camera.setXpix(param.getXpix());
            scene.camera.setYpix(param.getYpix());

            // if no parallelization is intended.
            if (param.getNoOfThreads() == 1){
                try {
                    rayt.rayTraceScene("output.ppm", scene, viewport, param.getDepth(),
                                       param.getEnableTimer(),"1", param.getNoOfThreads());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            // if we want to have parallelization we create as many threads as user
            // entered as argument.
            else if(param.getNoOfThreads() > 1){ //here we should bound number of threads && noOfThreads <)
                for (int i=1; i<= param.getNoOfThreads(); i++){
                    new TracerThread(i, rayt, scene, viewport, param.getDepth(), 
                                     param.getEnableTimer(), param.getNoOfThreads());
                }
            }
        }
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
