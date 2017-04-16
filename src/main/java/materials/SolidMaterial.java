package materials;


import domain.Color;

/**
 * @author majid
 *
 */
public class SolidMaterial extends BaseMaterial {

    private final Color color;

    public SolidMaterial(final Color color, final double reflection, final double diffuse) {
        super(diffuse, reflection); // for solving PMD error
        this.color = color;
    }

    @Override
    public boolean hasTexture() {
        return false;
    }

    @Override
    public Color getColor(final double uDbl, final double vDbl){
        return color;
    }

}
