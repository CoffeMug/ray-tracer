package classes;
import java.util.Scanner;
import java.util.Observable;
import java.util.Observer;
import javax.swing.*;
import java.awt.event.*;

class TracerGUI extends JPanel implements ActionListener {
    static private final String newline = "\n";
    JTextArea log;
    JButton openButton; 
    JFileChooser fc;  
    JComboBox zoomList;
    JPanel tracerPanel;
    JScrollPane logScrollPane;
    Integer[] zoomValues = { 1, 2, 3, 4 };
 
    public TracerGUI() {
        super(new BorderLayout());
        fc = new JFileChooser();
        openButton = new JButton("Open a ppm File...", 
                                 createImageIcon("images/Open.gif"));
        openButton.addActionListener(this);

        zoomList = new JComboBox(zoomValues);
        zoomList.setSelectedIndex(1);
        zoomList.addActionListener(this);

        tracerPanel = new JPanel();
        tracerPanel.add(openButton);
        tracerPanel.add(zoomList);

        log = new JTextArea(5,20);
        log.setMargin(new Insets(5,5,5,5));
        log.setEditable(false);
        logScrollPane = new JScrollPane(log);
 
        add(tracerPanel, BorderLayout.PAGE_START);
        add(logScrollPane, BorderLayout.CENTER);

    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == openButton) {
            int returnVal = fc.showOpenDialog(TracerGUI.this);
 
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                log.append("Opening: " + file.getName() + "." + newline);
            } else {
                log.append("Open command cancelled by user." + newline);
            }
            log.setCaretPosition(log.getDocument().getLength());
 
        } else if (e.getSource() == zoomValues) {
            JComboBox cb = (JComboBox)e.getSource();
            String zoomValue = (String)cb.getSelectedItem();
            log.append("Zoom value: " + zoomValue + newline);

        }

    }
    
    protected static ImageIcon createImageIcon(String path) {
        java.net.URL imgURL = TracerGUI.class.getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }
 
    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event dispatch thread.
     */
    public static void createAndShowGUI() {

        JFrame frame = new JFrame("Raytracer");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.add(new TracerGUI());

        frame.pack();
        frame.setVisible(true);
    }

class GUIResponseHandler implements Observer {
    @Override
    public void update(Observable obj, Object arg) {
        if (arg instanceof String) {
            String resp = (String) arg;
            System.out.println("\nReceived response: " + resp);
        }
    }
}


public final class Tracer {
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

    public static void main (final String args[]){

        final TracerGUI gui = new TracerGUI();
        final GUIResponseHandler guiResponseHandler = new GUIResponseHandler();

        gui.TracerGUIObservable.addObserver(guiResponseHandler);

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
