package RayTracing;

public class Sphere implements Primitive{
	vector center;
	float radius;
	int materialIndex;
	public Sphere() {};
	
	public void setCenter(String x, String y, String z) {
		double[] temp = {Double.parseDouble(x),Double.parseDouble(y),Double.parseDouble(z)};
		this.center = new vector(temp);
	}

	public void setRadius(String radius) {
		this.radius= Float.parseFloat(radius);		
	}

	public void setMaterial(String material) {
		this.materialIndex=Integer.parseInt(material);
	}

	@Override
	public double intersecte(Ray ray)  {
		vector L= this.center.add((ray.basePoint).multByScalar(-1));
		double Tca = L.dotProduct(ray.directionVector);
		if(Tca <0) {
			return 0;
		}
		double Dsqure = (L.dotProduct(L)) - (Tca * Tca);
		if(Dsqure > (this.radius * this.radius)) {
			return 0;
		}
		double Thc = Math.sqrt((this.radius *this.radius) - Dsqure);
		double t1 =Tca -Thc;
		double t2 =Tca + Thc;
		if(t1 >t2) {
			
			double temp = t1;
			t1 =t2;
			t2=temp;
		}
			
		if (t1<0) {
			t1=t2;
			if(t1<0) {
				return 0;
			}
		}
		return t1;
	}

	@Override
	public vector findNormal(vector intersectionPoint) {
		vector normal = intersectionPoint.add(this.center.multByScalar(-1));
		normal.normalize();
		return normal;
	}
	@Override
	public int getMterialIndex() {
		return this.materialIndex;
	}
}
