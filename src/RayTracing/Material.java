package RayTracing;

public class Material {
	double[] diffuseColor;
	double[] specularColor;
	double[] reflectionColor;
	float phongSpecularityCoefficient;
	float transparency;
	float bonus;
	public Material () {};
	
	public void setDiffuseColor(double[] diffuseColor) {
		this.diffuseColor=diffuseColor;
	}
	public void setSpecularColor(double[] specularColor) {
		this.specularColor=specularColor;

	}
	public void setReflectionColor(double[] reflectionColor) {
		this.reflectionColor=reflectionColor;
	}
	public void setPhongSpecularityCoefficient(float phongSpecularityCoefficient) {
		this.phongSpecularityCoefficient=phongSpecularityCoefficient;
	}
	public void setTransparency(float transparency) {
		this.transparency=transparency;
	}
	public void setBonus(float bonus) {
		this.bonus=bonus;
	}
	
}
