package domain;

/**
 * 
 * @author amin
 * the Light class responsible for creating light sources
 */
public class Light {
    public transient Vector position;

    public Light(final Vector pos){
        position = pos;
    }

    /**
     * this method will compute the color of the Intersection
     * point based on current light source,and it will return
     * the color value of it.
     */
    public Color getColor (IntersectInfo intersection,
                           IntersectInfo info,
                           Double cosPhi){
        Color color = new Color();

        if (!intersection.getIsHit() || intersection.getIsHit()  &&
                intersection.getPosition().vectorReduction(info.getPosition()).vectorLength() >=
                        this.position.vectorReduction(info.getPosition()).vectorLength()) {
                        
            color = color.addColor(info.getColor().multiplyColorByValue(cosPhi).
                    multiplyColorByValue(info.getElement().getMaterial().getDiffuse()));
        }
        return color;
    }

    @Override
    public String toString() {
        return String.format("Light ({1})" + position.toString()) ;
    }
}
