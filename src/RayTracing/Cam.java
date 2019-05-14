package RayTracing;

public class Cam {
	vector camPosition;
	vector lookAtDirection;
	vector camUpVector;
	float camScreenDistance;
	float camScreenWidth;
	
	public Cam() {
		// TODO Auto-generated constructor stub
	}
	public void setcamPosition(vector camPosition) {
		
		this.camPosition=camPosition;
		
	}
	public void SetCamScreenDistance(float camScreenDistance) {
		this.camScreenDistance=camScreenDistance;
		
	}
	public void SetCamScreenWidth(float camScreenWidth) {
		this.camScreenWidth=camScreenWidth;
		
	}
	public void setCamLookAtDirection(vector camLookAtDirection) {
		camLookAtDirection.normalize();
		this.lookAtDirection=camLookAtDirection;
		
	}
	public void SetCamUpVector(vector camUpVector) {
		camUpVector.normalize();
		this.camUpVector=camUpVector;
		
	}

}
