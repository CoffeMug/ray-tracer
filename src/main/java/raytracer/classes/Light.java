package classes;

/**
 * 
 * @author amin
 * the Light class responsible for creating light sources
 */

public class Light {
    public transient Vector3D position;
    public Light(){
        this.position = null;
    }

    public Light(final Vector3D pos){
        position = pos;
    }

    /**
     * this method will compute the color of the Intersection 
     * point based on current light source,and it will return
     * the color value of it.
     */
    public Color getColor (IntersectInfo intersection,
                           IntersectInfo info,Double cosPhi){
        Color color=info.color;
        Color tmpColor = new Color();
                
        if (!intersection.isHit || (intersection.isHit   &&  
            (intersection.position.vectorReduction(info.position).vectorLength()>=
            (this.position.vectorReduction(info.position)).vectorLength()))) {
                        
            color = info.color.multiColor(cosPhi).multiColor(info.element
                                                             .getMaterial().getDiffuse());
            tmpColor = tmpColor.addColor(color);
                        
        }
        color = tmpColor;
        return color;
    }
        
    /**
     * returns light position vector as string.
     */
    @Override  public  String toString(){
        return String.format("Light ({1})" + position.toString()) ;
    }

}
