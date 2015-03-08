package classes;

import java.util.Observable;
import java.util.Observer; 
import org.im4java.core.ConvertCmd;
import org.im4java.core.IMOperation;
 
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
            final RayTracer rayt = new RayTracer(param.getRenderDiffuse(), param.getRenderShadows(), param.getRenderReflection());
            final Bitmap viewport = Bitmap.createNewBitmap(param.getWidth(),param.getHeight());

            scene.camera.height = param.getHeight();
            scene.camera.width = param.getWidth();
            scene.camera.zoom = param.getZoom();
            scene.camera.xpix = param.getXpix();
            scene.camera.ypix = param.getYpix();

            // if no parallelization is intended.
            if (param.getNoOfThreads() == 1){
                rayt.rayTraceScene("output.ppm", scene, viewport, param.getDepth(), param.getEnableTimer(),"1", param.getNoOfThreads());
                convertToJpeg ("output.ppm");

            }
            // if we want to have parallelization we create as many threads as user
            // entered as argument.
            else if(param.getNoOfThreads() > 1){ //here we should bound number of threads && noOfThreads <)
                for (int i=1; i<= param.getNoOfThreads(); i++){
                    new TracerThread(i, rayt, scene, viewport, param.getDepth(), param.getEnableTimer(), param.getNoOfThreads());
                    convertToJpeg ("output.ppm");

                }
            }
        }
    }

    private static void convertToJpeg(final String ppm) {
        try {
            IMOperation op = new IMOperation();
            op.addImage(ppm);
            op.addImage("output.jpeg");

            ConvertCmd cmd = new ConvertCmd();
            cmd.run(op);
        }
        catch (Exception exception) {
            System.out.println(exception.getMessage());
        } 

    }

    /**
     * this method computes the image properties based on its arguments
     * parse from input
     */
    private static void computeImage(final int xx, final int yy, final int ww, final int hh, TracerParam param){
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
