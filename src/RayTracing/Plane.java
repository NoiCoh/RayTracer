package RayTracing;

public class Plane implements Primitive {
	vector normal;
	double offset;
	int materialIndex;
	
	public Plane() {};
	
	void setNormal(vector normal) {
		normal.normalize();
		this.normal=normal;
	}
	void setOffset(double offset) {
		this.offset=-1*offset;
	}
	void setMaterialIndex(int index) {
		this.materialIndex=index;
	}

	@Override
	public double intersecte(Ray ray)  {
		double t= (-1) * (ray.basePoint.dotProduct(this.normal)+ this.offset) / (ray.directionVector.dotProduct(this.normal));
		return t;
	}

	@Override
	public vector findNormal(vector intersectionPoint) {
		this.normal.normalize();
		return this.normal;
	}

	@Override
	public int getMterialIndex() {
		return this.materialIndex;
	}
	
	public double findOffset(vector point) {
		return (this.normal.dotProduct(point));
	}
	
	
	public vector findVecOnPlane(vector point) {
		double z = -1* (this.normal.data[0]*1+this.normal.data[1]*1+this.offset)/this.normal.data[2];
		double[] z_val = {1,1,z};
		vector z_vec = new vector(z_val);
		vector newVector = z_vec.add(point.multByScalar(-1));
		return newVector;
	}

}
