package RayTracing;

public class Light {
	
	vector lightPosition;
	double[] lightColor;
	float specularIntensity;
	float shadowIntensity;
	float lightRadius;
	
	public Light(vector lightPosition, double[] lightColor, float specularIntensity, float shadowIntensity,
			float lightRadius) {
		this.lightPosition=lightPosition;
		this.lightColor=lightColor;
		this.specularIntensity=specularIntensity;
		this.shadowIntensity=shadowIntensity;
		this.lightRadius=lightRadius;
	}

}
