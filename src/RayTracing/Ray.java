package RayTracing;

public class Ray {
	vector basePoint;
	vector directionVector;
	public Ray(vector camPosition, vector direction) {
		this.basePoint = camPosition;
		this.directionVector = direction;
	}

	

}
