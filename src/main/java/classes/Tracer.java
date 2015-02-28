package classes;
import java.util.Scanner;
import java.util.Observable;
import java.util.Observer;
import javax.swing.*;

public final class Tracer implements Observer {
    static boolean renderShadows = true;
    static boolean renderReflection = true;
    static boolean renderDiffuse = true;
    static boolean enableTimer = false;
    static int depth = 5;
    static int zoom = 200;
    static int xpix = 400;
    static int ypix = 400;
    static int width = 400;
    static int height = 400;
    static int noOfThreads = 1;
    static String sceneFile = "";

    private Tracer(){

    }    

    @Override
    public void update(Observable obj, Object arg) {
        if (arg instanceof String) {
            String resp = (String) arg;
            System.out.println("\nReceived response: " + resp);
        }
    }

    public static void main (final String args[]){

        final TracerGUI gui = new TracerGUI();
        SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    //Turn off metal's use of bold fonts
                    UIManager.put("swing.boldMetal", Boolean.FALSE);
                    TracerGUI.createAndShowGUI();
                }
            });

        final XmlParser parser = new XmlParser();

        parseInputArguments(args);

        computeImage(xpix,ypix,width,height);

        final Scene scene = parser.parseXmlFile(sceneFile);
        final RayTracer rayt = new RayTracer(renderDiffuse, renderShadows, renderReflection);

        //400X400 is the size of result image we want to create.
        //later we will get this size as an argument from the user.
        final Bitmap viewport = Bitmap.createNewBitmap(width,height);
        scene.camera.height = height;
        scene.camera.width = width;
        scene.camera.zoom = zoom;
        scene.camera.xpix = xpix;
        scene.camera.ypix = ypix;

        // if no parallelization is intended.
        if (noOfThreads == 1){
            rayt.rayTraceScene("output.ppm", scene, viewport, depth, enableTimer,"1", noOfThreads);
        }
        // if we want to have parallelization we create as many threads as user
        // entered as argument.
        else if(noOfThreads >1){ //here we should bound number of threads && noOfThreads <)
            for (int i=1; i<= noOfThreads; i++){
                new TracerThread(i, rayt, scene, viewport, depth, enableTimer, noOfThreads);
            }
        }
    }
    /**
     * this method computes the image properties based on its arguments
     * parse from input
     */

    private static void computeImage(final int xx,final int yy,final int ww,final int hh){
        if(xx == 0)
            xpix =ww*yy/hh;
        if(yy == 0)
            ypix =hh*xx/ww;
        if(ww == 0)
            width = xx*hh/yy;
        if(hh == 0)
            height = ww*yy / xx;
    }

    /**
     * this method parses program arguments string. 
     * @param args
     */
    private static void parseInputArguments(String[] args){
        if (args.length > 0){
            for (int i = 0; i<args.length; i++){
                if (args[i].equals("-q")){
                    renderShadows = false;
                    renderReflection = false;
                    renderDiffuse = false;
                }
                if (args[i].equals("-r")){
                    if (args[i+1] != null){
                        depth = Integer.parseInt(args[i+1]);
                    }
                }
                if (args[i].equals("-t")){
                    enableTimer = true;
                }
                if (args[i].equals("-z")){
                    if (args[i+1] != null){
                        zoom = Integer.parseInt(args[i+1]);
                    }
                }
                if (args[i].equals("-x")){
                    if (args[i+1] != null){
                        xpix = Integer.parseInt(args[i+1]);
                    }
                }
                if (args[i].equals("-y")){
                    if (args[i+1] != null){
                        ypix = Integer.parseInt(args[i+1]);
                    }
                }
                if (args[i].equals("-w")){
                    if (args[i+1] != null){
                        width = Integer.parseInt(args[i+1]);
                    }
                }
                if (args[i].equals("-h")){
                    if (args[i+1] != null){
                        height = Integer.parseInt(args[i+1]);
                    }
                }
                if (args[i].equals("-p")){
                    if (args[i+1] != null){
                        noOfThreads = Integer.parseInt(args[i+1]);
                    }
                }
                if (args[i].contains(".xml") || args[i].contains(".XML") ){
                    sceneFile = args[i];
                }
            }
        }
    }
}
