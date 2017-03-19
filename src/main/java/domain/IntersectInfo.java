package domain;
import shapes.IShape;
/**
 * the intersect info class , this class holds information of an intersection
 * point.
 * @author amin
 *
 */
public class IntersectInfo {
    private Boolean isHit; // indicates if the shape was hit
    private int hitCount; // counts the number of shapes that were hit
    private IShape element; // the closest shape that was intersected
    private Vector position; // position of intersection
    private Vector normal; // normal vector on intersection point
    private Color color; // color at intersection
    private double distance; // distance from point to screen

    /**
     * The default constructor
     */
    public IntersectInfo() {
        this.isHit = false;
    }

    public IntersectInfo(final double distance) {
        this.isHit = false;
        this.distance = distance;
    }

    public IntersectInfo(final Boolean hit, 
                         final IShape elem,
                         final Vector pos,
                         final Vector norm,
                         final Color color,
                         final double dist) {
        this.isHit = hit;
        this.element = elem;
        this.position = pos;
        this.normal = norm;
        this.color = color;
        this.distance = dist;
    }

    /**
     * Getters and setters.
     */

    public Boolean getIsHit() {
        return this.isHit;
    }

    public void setIsHit(final Boolean hit) {
        this.isHit = hit;
    }

    public int getHitCount() {
        return this.hitCount;
    }

    public void setHitCount(final int count) {
        this.hitCount = count;
    }

    public IShape getElement() {
        return this.element;
    }

    public void setElement(final IShape elem) {
        this.element = elem;
    }

    public Vector getPosition() {
        return this.position;
    }

    public void setPosition(final Vector pos) {
        this.position = pos;
    }

    public Vector getNormal() {
        return this.normal;
    }

    public void setNormal(final Vector norm) {
        this.normal = norm;
    }

    public Color getColor() {
        return this.color;
    }

    public void setColor(final Color color) {
        this.color = color;
    }

    public double getDistance() {
        return this.distance;
    }

    public void setDistance(final double distance) {
        this.distance = distance;
    }

}
