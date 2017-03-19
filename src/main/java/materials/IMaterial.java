/**
 * An interface every material we are going to produce should implement. 
 */
package materials;

import domain.Color;

/**
 * @author majid khorsandi
 *
 */
public interface IMaterial {
    /**
     * this method checks if current material is of type solid or texture.
     * @return true as texture or false as solid.
     */
    boolean hasTexture(); 
        
    /**
     * gets a coordinate on shape and returns color of that coordinate. 
     * @param uDbl
     * @param vDbl
     * @return color of the pixel located on that coordinate.
     */
    Color getColor(double uDbl, double vDbl);

}
