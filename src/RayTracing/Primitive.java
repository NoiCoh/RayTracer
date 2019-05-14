package RayTracing;

public interface Primitive {
	
	public int getMterialIndex();
	double intersecte(Ray ray) ;
	public vector findNormal(vector intersectionPoint);



}
