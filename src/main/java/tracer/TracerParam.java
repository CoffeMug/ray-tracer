package tracer;

public class TracerParam {

    private static boolean renderShadows;
    private static boolean renderReflection;
    private static boolean renderDiffuse;
    private static boolean enableTimer;
    private static int traceDepth;
    private static int cameraZoom;
    private static int xpix;
    private static int ypix;
    private static int width;
    private static int height;
    private static int noOfThreads;
    private static String sceneFile;
    private static String outputFile;

    public TracerParam() {
        this.renderShadows = true;
        this.renderReflection = true;
        this.renderDiffuse = false;
        this.enableTimer = false;
        this.traceDepth = 1;
        this.cameraZoom = 100;
        this.xpix = 400;
        this.ypix = 400;
        this.width = 400;
        this.height = 400;
        this.noOfThreads = 1;
        this.outputFile = "output.ppm";
    }

    public void setRenderShadows(boolean renderShadows) {
        this.renderShadows = renderShadows;
    }

    public boolean getRenderShadows() {
        return this.renderShadows;
    }

    public void setRenderReflection(boolean renderReflection) {
        this.renderReflection = renderReflection;
    }

    public boolean getRenderReflection() {
        return this.renderReflection;
    }

    public void setRenderDiffuse(boolean renderDiffuse) {
        this.renderDiffuse = renderDiffuse;
    }

    public boolean getRenderDiffuse() {
        return this.renderDiffuse;
    }

    public void setEnableTimer(boolean enableTimer) {
        this.enableTimer = enableTimer;
    }

    public boolean getEnableTimer() {
        return this.enableTimer;
    }

    public void setDepth(int depth) {
        this.traceDepth = depth;
    }

    public int getDepth() {
        return this.traceDepth;
    }

    public void setZoom(int zoom) {
        this.cameraZoom = zoom;
    }

    public int getZoom() {
        return this.cameraZoom;
    }

    public void setXpix(int xpix) {
        this.xpix = xpix;
    }

    public int getXpix() {
        return this.xpix;
    }

    public void setYpix(int ypix) {
        this.ypix = ypix;
    }

    public int getYpix() {
        return this.ypix;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getWidth() {
        return this.width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getHeight() {
        return this.height;
    }

    public void setNoOfThreads(int noOfThreads) {
        this.noOfThreads = noOfThreads;  
    }

    public int getNoOfThreads() {
        return this.noOfThreads;
    }

    public void setSceneFile(String sceneFile) {
        this.sceneFile = sceneFile;  
    }

    public String getSceneFile() {
        return this.sceneFile;
    }

    public static String getOutputFile() {
        return outputFile;
    }

    public static void setOutputFile(String outputFile) {
        TracerParam.outputFile = outputFile;
    }
}
