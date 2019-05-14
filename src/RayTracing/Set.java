package RayTracing;

public class Set {
	float [] backgroundColor ;
	int NumberOfShadowRays;
	int MaxNumberOfRecursion;
	int SuperSamplingLevel;
	
	public void setBackGroundColor(float[] backgroundColor) {
		this.backgroundColor=backgroundColor;
		
	}

	public void setNumberOfShadowRays(int NumberOfShadowRays) {
		this.NumberOfShadowRays=NumberOfShadowRays;		
	}

	public void setMaxNumberOfRecursion(int MaxNumberOfRecursion) {
		this.MaxNumberOfRecursion=MaxNumberOfRecursion;
		
	}

	public void setSuperSamplingLevel(int SuperSamplingLevel) {
		this.SuperSamplingLevel=SuperSamplingLevel;		
	}

}
