package tracer;

import bitmap.Bitmap;
import bitmap.BitmapVariant;
import domain.Color;
import domain.Scene;
import exceptions.InvalidPixelException;
import exceptions.PpmToJpgConversionException;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import utils.XmlParser;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
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

            final RayTracer rayTracer = new RayTracer(param.getRenderDiffuse(), param.getRenderShadows());

            // Single thread tracer
            if (param.getNoOfThreads() == 1){
                try {
                    rayTracer.rayTraceScene(scene, bitmap, param.getDepth(), param.getEnableTimer(),
                            0, bitmap.getHeight());
                } catch (InvalidPixelException e) {
                    e.printStackTrace();
                }
            }

            // Multiple threads tracer
            else {
                final int wHeight = (int) Math.floor(bitmap.getHeight()/param.getNoOfThreads());
                IntStream.rangeClosed(1, param.getNoOfThreads()).forEach(threadNumber -> {
                    try {
                        final int upperBound = threadNumber == param.getNoOfThreads() && threadNumber * wHeight < bitmap.getHeight() ?
                                bitmap.getHeight() : threadNumber * wHeight ;
                        final int lowerBound = (threadNumber - 1) * wHeight;
                        new TracerThread(threadNumber, rayTracer, scene, bitmap,
                                param.getDepth(),
                                param.getEnableTimer(),
                                lowerBound, upperBound);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                });
            }
        }
        bitmap.writeBitmapToFile(BitmapVariant.PPM_ASCII, param.getOutputFile());
        convertPPMToJPG();
    }

    private void convertPPMToJPG() {
        BufferedImage image;
        File outfile;
        try {
            image = new BufferedImage(bitmap.getWidth(), bitmap.getHeight(), BufferedImage.TYPE_INT_RGB);
            for (int i=0; i < bitmap.getWidth(); i++) {
                for (int j=0; j < bitmap.getHeight(); j++) {
                    Color color = bitmap.readPixel(i, j);
                    int rgb = makeRgb(color.getRed(), color.getGreen(), color.getBlue());
                    image.setRGB(i, j, rgb);
                }
            }
            outfile = new File("output.jpeg");

            ImageOutputStream imageOutputStream = ImageIO.createImageOutputStream(outfile);
            Iterator<ImageWriter> iterator = ImageIO.getImageWritersByFormatName("jpeg");
            ImageWriter writer = iterator.next();
            ImageWriteParam imageWriteParam = writer.getDefaultWriteParam();
            imageWriteParam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
            imageWriteParam.setCompressionQuality(0.95f);
            writer.setOutput(imageOutputStream);
            writer.write(null, new IIOImage(image,null,null), imageWriteParam);
            writer.dispose();
        } catch (IOException e) {
            logger.error("Error converting PPM to JPG!");
            throw new PpmToJpgConversionException(e.getMessage());
        }
    }
    private static int makeRgb( final int red, final int green, final int blue ) {
        return 0xff000000 | ( red << 16 ) | ( green << 8 ) | blue;
    }
}
