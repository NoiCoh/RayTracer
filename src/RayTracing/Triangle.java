package RayTracing;

public class Triangle implements Primitive {
	vector vertex1;
	vector vertex2;
	vector vertex3;
	int materialIndex;
	vector normal;
	public Triangle(vector vector1, vector vector2, vector vector3, int materialIndex) {
		this.vertex1 = vector1;
		this.vertex2 = vector2;
		this.vertex3 = vector3;
		this.materialIndex=materialIndex;
		this.normal = findNormal(null);
		
	}

	@Override
	public double intersecte(Ray ray)  {
		double offset = (-1)*this.normal.dotProduct(this.vertex1);
		double t = findTIntersection(ray, this.normal,offset);
		if (t<=0) {
			return -1;
		}
		vector point = ray.basePoint.add(ray.directionVector.multByScalar(t));
		boolean first =checkInTriangle(vertex1,vertex2,point,this.normal);
		boolean second =checkInTriangle(vertex2,vertex3,point,this.normal);
		boolean third =checkInTriangle(vertex3,vertex1,point,this.normal);
		if(first && second && third) {
			return t;
		}
		return -1 ;
	}
	
	private double findTIntersection(Ray ray, vector normal,double offset) {
		double d = offset;
		double VN=ray.directionVector.dotProduct(normal);
		double P0N=ray.basePoint.dotProduct(normal);
		double t;
		if (VN == 0)
			return -1;
		t = -(P0N+d)/VN;
		
		return t;
	}

	private boolean checkInTriangle(vector vertex1, vector vertex2,vector point, vector normal) {
		vector V1 = vertex2.sub(vertex1);
		vector V2 = point.sub(vertex1);	
		vector V3 = V1.crossProduct(V2);
		if(normal.dotProduct(V3)<0 ) {
			return false;
		}
		return true;
	}

	@Override
	public vector findNormal(vector intersectionPoint) {
		vector V1 = vertex2.sub(vertex1);
		vector V2 = vertex3.sub(vertex1);
		vector triNormal = V1.crossProduct(V2);
		//triNormal.normalize();
		return triNormal;
	}
	
	@Override
	public int getMterialIndex() {
		return this.materialIndex;
	}
}
