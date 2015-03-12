package classes;

import java.io.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import materials.SolidMaterial;
import materials.Texture;
import materials.TextureMaterial;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import shapes.*;

public class XmlParser {
    private transient Document dom;

    /**
     * this methods gets path of a XML scene file and parses scene elements into
     * corresponding classes.
     * @param filePath
     * @return an object of type scene.
     */
    public Scene parseXmlFile(final String filePath){
        //get the factory
        final DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        Scene scc=null;

        try {

            //Using factory get an instance of document builder
            final DocumentBuilder dbb = dbf.newDocumentBuilder();

            //parse using builder to get DOM representation of the XML file
            dom = dbb.parse(filePath);

            try {
                scc = parseDocument();
            } catch (Exception e) {
                 throw new RuntimeException(e.getMessage());
            }

        }catch(ParserConfigurationException pce) {
            throw new RuntimeException(pce.getMessage());
        }catch(SAXException se) {
            throw new RuntimeException(se.getMessage());
        }catch(IOException ioe) {
            throw new RuntimeException(ioe.getMessage());
        }
        return scc;
    }

    /**
     * this method does actual parsing
     * @return a scene object.
     * @throws Exception if number of objects in a scene exceeds 20 parsing will stop.
     */
    private Scene parseDocument() throws Exception {
        int spheres_nbr = 0;
        int triangles_nbr = 0;
        int planes_nbr = 0;
        int lights_nbr = 0;
        int spotLights_nbr = 0;
        int  sum = 0;
        Shapes shapes;
        Lights lights;
        SpotLights spotlights;
        Camera camera;
        Color  backgroundColor;
        Scene scene;

        //get the root element
        final Element docElement = dom.getDocumentElement();

        Element nlo =(Element) docElement.getElementsByTagName("camera").item(0);
        camera =getCamera(nlo);
        nlo =(Element) docElement.getElementsByTagName("background").item(0);
        backgroundColor = getBackground(nlo);

        spheres_nbr = docElement.getElementsByTagName("sphere").getLength();
        triangles_nbr = docElement.getElementsByTagName("triangle").getLength();
        planes_nbr = docElement.getElementsByTagName("plane").getLength();
        lights_nbr = docElement.getElementsByTagName("light").getLength();
        spotLights_nbr = docElement.getElementsByTagName("spotlight").getLength();
        sum = spheres_nbr + triangles_nbr + planes_nbr + lights_nbr;

        final NodeList spheres = docElement.getElementsByTagName("sphere");
        final NodeList planes = docElement.getElementsByTagName("plane");
        final NodeList triangles = docElement.getElementsByTagName("triangle");
        final NodeList lamps = docElement.getElementsByTagName("light");
        final NodeList spotlamps = docElement.getElementsByTagName("spotlight");
                
        shapes = new Shapes();
        lights = new Lights();
        spotlights = new SpotLights();
        for (int i=0 ; i < planes_nbr; i++){
            shapes.add(getPlane((Element)planes.item(i)));
        }
        for (int i=0 ; i < spheres_nbr; i++){
            shapes.add(getSphere((Element)spheres.item(i)));
        }
        for (int i=0 ; i < triangles_nbr; i++){
            shapes.add(getTriangle((Element)triangles.item(i)));
        }
        for (int i=0 ; i < lights_nbr; i++){
            lights.add(getLight((Element)lamps.item(i)));
        }
        for (int i=0 ; i < spotLights_nbr; i++){
            spotlights.add(getSpotLight((Element)spotlamps.item(i)));
        }
        scene = new Scene(camera, backgroundColor, shapes, lights, spotlights);
        return scene;
    }

    /**
     * this methods gets a camera element, parses it and returns a corresponding
     * camera object.
     * @param camElement 
     * @return a camera object
     */
    private Camera getCamera(final Element camElement) {
        Vector3D location;
        Vector3D lookat;
        Vector3D upside;

        // here we extract location vector for camera.
        Element nlo =(Element) camElement.getElementsByTagName("location").item(0);
        Element vec =(Element) nlo.getElementsByTagName("vector").item(0);
        location = getVector(vec);

        //then extract sky vector.
        nlo =(Element) camElement.getElementsByTagName("sky").item(0);
        vec =(Element) nlo.getElementsByTagName("vector").item(0);
        upside = getVector(vec);

        //then extract lookAt vector.
        nlo =(Element) camElement.getElementsByTagName("look_at").item(0);
        vec =(Element) nlo.getElementsByTagName("vector").item(0);
        lookat = getVector(vec);

        //build camera object
        return new Camera(location, lookat, upside);
    }

    /**
     * this methods gets a Background element, parses it and returns a corresponding
     * background object.
     * @param backElement
     * @return background object
     * @throws Exception
     */
    private Color getBackground(final Element backElement)throws Exception {

        Color background;
        if (backElement == null){
            background = new Color();
            return background;
        }
        final Element elm = (Element) backElement.getElementsByTagName("color").item(0);
        try {
            background = getColor(elm);
        } catch (Exception e) {
            throw new Exception(e.toString());
        }
        return background;

    }

    /**
     * this methods gets a planeElement element, parses it and returns a corresponding
     * plane object.
     * @param planeElement
     * @return plane object
     * @throws Exception
     */
    private PlaneShape getPlane(final Element planeElement) throws Exception {
        Vector3D normal;
        Vector3D point = null;
        double distance = -1;
        SolidMaterial material;
        TextureMaterial txMaterial;
                
        PlaneShape plane = null;
        if (!planeElement.getAttribute("distance").isEmpty()) {
            distance = Double.parseDouble(
                       planeElement.getAttribute("distance").toString());
        }
        final Element normalElement =(Element)planeElement.
            getElementsByTagName("normal").item(0);
        Element elm = (Element)normalElement.getElementsByTagName("vector").item(0);
        normal = getVector(elm);

        final Element pointElement = planeElement.
            getElementsByTagName("point").getLength() > 0 ?
            (Element)planeElement.
            getElementsByTagName("point").item(0) : null;
        if (pointElement != null){
            elm = (Element)pointElement.getElementsByTagName("vector").item(0);
            point = getVector(elm);
        }

        final Element materialElement =(Element)planeElement.
            getElementsByTagName("surface").item(0);
        if (materialElement.getElementsByTagName("color").getLength()>0){
            try {
                material = getSolidMaterial(materialElement);
            } catch (Exception e) {
                throw new Exception(e.toString());

            }
            if (point != null) {
                plane = new PlaneShape(point, normal, material);
            }
            else if(distance != -1) {
                plane = new PlaneShape(distance, normal, material);
                                
            }
            else {
                throw new RuntimeException("The plain definition in XML file" +
                                           " is not correct");
            }
        }
                
        if (materialElement.getElementsByTagName("ppm").getLength()>0){
            try {
                txMaterial = getTextureMaterial(materialElement);
            } catch (Exception e) {
                throw new Exception(e.toString());
            }
            if (point != null) {
                plane = new PlaneShape(point, normal, txMaterial);
            }
            else if(distance != -1) {
                plane = new PlaneShape(distance, normal, txMaterial);
                                
            }
            else {
                throw new Exception("The plain definition in XML file" +
                                    " is not correct");
            }
        }
        return plane;
    }

    /**
     * this methods gets a SphereElement element, parse it and returns a corresponding
     * Sphere object.
     * @param SphereElement
     * @return Sphere object
     * @throws Exception
     */
    private SphereShape getSphere(final Element SphereElement)throws Exception{
        double rDbl;
        Vector3D position;
        SolidMaterial material;
        TextureMaterial txMaterial;
        SphereShape sphere = null;
        rDbl = Double.parseDouble(SphereElement.getAttribute("radius"));

        final Element locElement =(Element)SphereElement.
            getElementsByTagName("location").item(0);
        final Element elm = (Element)locElement.getElementsByTagName("vector").item(0);
        position = getVector(elm);

        final Element materialElement =(Element)SphereElement.
            getElementsByTagName("surface").item(0);
                
        if (materialElement.getElementsByTagName("color").getLength()>0){
            try {
                material = getSolidMaterial(materialElement);
            } catch (Exception e) {
                throw new RuntimeException(e.toString());

            }
            sphere = new SphereShape(position, rDbl, material);
        }
                
        if (materialElement.getElementsByTagName("ppm").getLength()>0){
            try {
                txMaterial = getTextureMaterial(materialElement);
            } catch (Exception e) {
                throw new Exception(e.toString());

            }
            sphere = new SphereShape(position, rDbl, txMaterial);
        }

        return sphere;

    }

    /**
     * this methods gets a triangleElement element, parse it and returns a corresponding
     * triangle object.
     * @param triangleElement
     * @return triangle object
     * @throws Exception
     */
    private TriangleShape getTriangle(final Element triangleElement)throws Exception{
        TriangleShape triangle = null;
        Vector3D c0Vect;
        Vector3D c1Vect;
        Vector3D c2Vect;
        SolidMaterial material;
        TextureMaterial txMaterial;
        boolean isCorner = true;

        final Element c0Element =(Element)triangleElement.
            getElementsByTagName("c0").item(0);
        Element elm = (Element)c0Element.getElementsByTagName("vector").item(0);
        c0Vect = getVector(elm);

        final Element c1Element =(Element)triangleElement.
            getElementsByTagName("c1").item(0);
        elm = (Element)c1Element.getElementsByTagName("vector").item(0);
        c1Vect = getVector(elm);

        final Element c2Element =(Element)triangleElement.
            getElementsByTagName("c2").item(0);
        elm = (Element)c2Element.getElementsByTagName("vector").item(0);
        c2Vect = getVector(elm);

        final Element v1Element =(Element)triangleElement.
            getElementsByTagName("v1").item(0);
        if (v1Element != null){
            elm = (Element)v1Element.getElementsByTagName("vector").item(0);
            isCorner = false;
            c1Vect = getVector(elm);
        }

        final Element v2Element =(Element)triangleElement.
            getElementsByTagName("v2").item(0);
        if (v2Element != null){
            elm = (Element)v2Element.getElementsByTagName("vector").item(0);
            c2Vect = getVector(elm);
        }

        final Element materialElement =(Element)triangleElement.
            getElementsByTagName("surface").item(0);
                
        if (isCorner == false) {
            c1Vect = TriangleShape.triangleComputeCorner(c0Vect, c1Vect);
            c2Vect = TriangleShape.triangleComputeCorner(c0Vect, c2Vect);
        }
                
        if (materialElement.getElementsByTagName("color").getLength()>0){
            try {
                material = getSolidMaterial(materialElement);
            } catch (Exception e) {
                throw new RuntimeException(e.toString());

            }
            triangle = new TriangleShape(c0Vect, c1Vect, c2Vect, material);
        }

        if (materialElement.getElementsByTagName("ppm").getLength()>0){
            try {
                txMaterial = getTextureMaterial(materialElement);
            } catch (Exception e) {
                throw new RuntimeException(e.toString());

            }
            triangle = new TriangleShape(c0Vect, c1Vect, c2Vect, txMaterial);
        }
        return triangle;
    }
        
    /**
     * this methods gets a matElement element, parse it and returns a corresponding
     * material object.
     * @param matElement
     * @return material object
     * @throws Exception
     */
    private SolidMaterial getSolidMaterial(final Element matElement) throws Exception {
        SolidMaterial mat=null;
        double diff=1;
        double ref=0;
        if (matElement.getElementsByTagName("finish").getLength()> 0){
            final Element elm= (Element) matElement.getElementsByTagName("finish").item(0);
            final String diffuse = elm.getAttribute("diffuse").toString();
            final String reflect = elm.getAttribute("reflect").toString();
            diff = (diffuse !="" ? Double.parseDouble(diffuse) : 0); 
            ref = (reflect !="" ? Double.parseDouble(reflect) : 0); 
        }
                
        if (matElement.getElementsByTagName("color").getLength()> 0){
            final Element elm= (Element) matElement.getElementsByTagName("color").item(0) ;
            try {
                mat = new SolidMaterial(getColor(elm),ref,diff);
            } catch (Exception e) {
                throw new Exception(e.toString());
            }
        }
        return mat;
    }

    private TextureMaterial getTextureMaterial(final Element matElement) throws Exception {
        TextureMaterial mat=null;
        Texture texture;
        final Bitmap bmp= Bitmap.createNewBitmap(0,0);

        if (matElement.getElementsByTagName("ppm").getLength()> 0){
            final Element elm= (Element) matElement.getElementsByTagName("ppm").item(0) ;
            final String bitmapFile = elm.getAttribute("file").toString();
            bmp.createBitmapFromFile(bitmapFile);
            texture = Texture.fromBitmap(bmp);
            try {
                mat = new TextureMaterial(texture,0);
            } catch (Exception e) {
                throw new RuntimeException(e.toString());

            }
        }
        return mat;
    }

    /** this methods gets a docElement element, parse it and returns a 
     * material's color object.
     * @param docElement
     * @return color object
     * @throws Exception
     */
    private Color getColor(final Element docElement) throws Exception {
        String strRed;
        String strGreen;
        String strBlue;
        int red=0;
        int green=0;
        int blue=0;
        Color color;
        // here we extract components of color.
        strRed = docElement.getAttribute("red");
        strGreen = docElement.getAttribute("green");
        strBlue = docElement.getAttribute("blue");
        /*if (isColorConvertableToInt(strRed) && 
          isColorConvertableToInt(strGreen) &&
          isColorConvertableToInt(strBlue)) {
        */                       
        red =(int) (Double.parseDouble(strRed)>= 0  
                    && Double.parseDouble(strRed)<= 1 ? 
                    Double.parseDouble(strRed)*255 :
                    Double.parseDouble(strRed))  ;
        green =(int) (Double.parseDouble(strGreen)>= 0  
                      && Double.parseDouble(strGreen)<= 1 ? 
                      Double.parseDouble(strGreen)*255 :
                      Double.parseDouble(strGreen))  ;
        blue = (int) (Double.parseDouble(strBlue)>= 0  
                      && Double.parseDouble(strBlue)<= 1 ? 
                      Double.parseDouble(strBlue)*255 :
                      Double.parseDouble(strBlue))  ;
        /* } else {

           throw new Exception("Input XML: Color: incorrect color parameter");
           }*/

        color = new Color(red,green,blue);
        return color;
    }

    private Double getReflection(final Element docElement){
        double ref;
        ref = Double.parseDouble(docElement.getAttribute("reflect").toString());
        return ref;
    }

    /**
     * this methods gets a lightElement element, parse it and returns a corresponding
     * light object.
     * @param lightElement
     * @return light object
     */
    private Light getLight(final Element lightElement) {
        Light light;
        final Element posElement =(Element) lightElement.getElementsByTagName("position").item(0);
        final Element elm = (Element)posElement.getElementsByTagName("vector").item(0);
        light = new Light(getVector(elm));
        return light;
    }

        
    /**
     * this method gets a spotLightElement element ,parse it and returns corresponding 
     * spotlight object
     * @param lightElement
     * @return
     */
    private SpotLight getSpotLight(final Element spotLightElement) {
        SpotLight spotlight;
        final double focus;
                
        focus = Double.parseDouble(spotLightElement.getAttribute("focus"));
        final Element posElement =(Element) spotLightElement.getElementsByTagName("position").item(0);
        final Element poselm = (Element)posElement.getElementsByTagName("vector").item(0);
                
        final Element dirElement =(Element) spotLightElement.getElementsByTagName("direction").item(0);
        final Element direlm = (Element)dirElement.getElementsByTagName("vector").item(0);
                
        spotlight = new SpotLight(getVector(poselm),focus,getVector(direlm));
        return spotlight;
    }

    /**
     * this methods gets a docElement element, parse it and returns a 
     * vector object.
     * @param docElement
     * @return vector object.
     */
    private Vector3D getVector(final Element docElement) {
        double xPos;
        double yPos;
        double zPos;
        Vector3D vector;

        xPos = Double.parseDouble(docElement.getAttribute("x"));
        yPos = Double.parseDouble(docElement.getAttribute("y"));
        zPos = Double.parseDouble(docElement.getAttribute("z"));
        vector = new Vector3D(xPos,yPos,zPos);
        return vector;
    }
}
