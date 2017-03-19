package domain;

/**
 * 
 * @author amin
 * the Light class responsible for creating light sources
 */

public class Light {
    public transient Vector position;
    public Light(){
        this.position = null;
    }

    public Light(final Vector pos){
        position = pos;
    }

    /**
     * this method will compute the color of the Intersection 
     * point based on current light source,and it will return
     * the color value of it.
     */
    public Color getColor (IntersectInfo intersection,
                           IntersectInfo info, Double cosPhi){
        Color color = info.getColor();
        Color tmpColor = new Color();
                
        if (!intersection.getIsHit() || (intersection.getIsHit()   &&  
                                         (intersection.getPosition().vectorReduction(info.getPosition()).vectorLength()>=
                                          (this.position.vectorReduction(info.getPosition())).vectorLength()))) {
                        
            color = info.getColor().multiplyColorByAnotherValue(cosPhi).
                multiplyColorByAnotherValue(info.getElement().getMaterial().getDiffuse());
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