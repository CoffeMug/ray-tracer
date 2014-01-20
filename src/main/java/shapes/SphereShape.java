package shapes;
import materials.TextureMaterial;
import interfaces.IMaterial;
import classes.*;
/**
 * 
 * @author amin
 *the sphere class which make the sphere shape based on a position vector and the sphere radius
 */
public class SphereShape extends BaseShape {
	public transient double radius;

	/**
	 * default constructor to produce a single sphere
	 * @param pos
	 * @param rtmp
	 * @param material
	 */
	public SphereShape(final Vector3D pos, final double radius , final IMaterial material){
		super();
		this.radius = radius;
		position = pos;
		this.material = material;

	}

	/**
	 * the overided method to compute the intersection point 
	 */
	@Override public IntersectInfo intersect(final Ray ray){

		final IntersectInfo info = new IntersectInfo();
		info.element = this;

		final Vector3D oc =this.position.vectorReduction(ray.origin);
		final double l2oc = oc.dotProduct(oc);
		final double tca = oc.dotProduct(ray.direction);
		final double t2hc = (this.radius * this.radius) - l2oc + (tca * tca);
		//	final double t = Product(dst) - (this.rDbl * this.rDbl);
		final double sr2 = (this.radius * this.radius);

		if (sr2 > l2oc ){ // origin of the ray is inside the sphere.

			info.isHit = true;
			info.distance = tca + (double)Math.sqrt(t2hc);
			info.position = ray.origin.vectorAddition(
					ray.direction.vectorMultiply(info.distance));
			info.normal = info.position.vectorReduction(position).vectorMultiply(-1/this.radius);


		}
		else if (sr2 < l2oc && tca > 0 ){ // origin of the ray is outside the sphere and ray hits the sphere.

			info.isHit = true;
			info.distance = tca - (double)Math.sqrt(t2hc);
			info.position = ray.origin.vectorAddition(
					ray.direction.vectorMultiply(info.distance));
			info.normal = info.position.vectorReduction(position).vectorMultiply(1/this.radius);


		}
		else if (t2hc >= 0){
			info.isHit = true;
			info.distance = tca - (double)Math.sqrt(t2hc);
			info.position = ray.origin.vectorAddition(
					ray.direction.vectorMultiply(info.distance));
			info.normal = info.position.vectorReduction(position).vectorMultiply(1/this.radius);

		}

		else{ // no intersection!
			info.isHit = false;
		}

		if (info.isHit){

			if (material.hasTexture())
			{
				
				//TextureMaterial tmpMaterial = (TextureMaterial) this.material;
				
				final Vector3D sp = new Vector3D(0, 1, 0).normalize(); // north pole / up

				// equator / sphere orientation
				final Vector3D se = new Vector3D(0, 0, 1).normalize(); 

				//points from center of sphere to intersection 
				final Vector3D rn = info.normal;

				final double phi = Math.acos((rn.vectorMultiply(-1)).dotProduct(sp));
				final double vi = phi / Math.PI;
				final double ui;

				if (vi == 0 || vi == 1)
					ui = 0;
				else {
					final double theta = (Math.acos(se.dotProduct(rn) / Math.sin(phi)))/(2*Math.PI);

					ui = (se.crossProduct(sp)).dotProduct(rn) > 0 ? theta : 1 - theta; 

				}


				info.color = this.material.getColor(ui , vi);
			}
			else
			{
				// skip uv calculation, just get the color
				info.color = this.material.getColor(0, 0);
			}

		}

		return info;
	}


	@Override public  String toString(){
		return String.format("Sphere ({0},{1},{2}) Radius: {3}", position.toString(), radius);
	}
}
