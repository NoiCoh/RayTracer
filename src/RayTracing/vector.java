package RayTracing;

/**
 * 
 * vector in three-dimensional space.
 *
 */
public class vector {
	double[] data;

	public vector(double[] data) {
		this.data = data;
	}

	public double dotProduct(vector b) {
		double res = 0;
		for (int i = 0; i < 3; i++) {
			res += this.data[i] * b.data[i];
		}
		return res;
	}

	public vector crossProduct(vector b) {
		double x = (this.data[1] * b.data[2]) - (this.data[2] * b.data[1]);
		double y = (this.data[2] * b.data[0]) - (this.data[0] * b.data[2]);
		double z = (this.data[0] * b.data[1]) - (this.data[1] * b.data[0]);
		double[] xyz = { x, y, z };
		vector res = new vector(xyz);
		return res;
	}

	public vector add(vector b) {
		double[] res = new double[3];

		for (int i = 0; i < 3; i++) {
			res[i] = this.data[i] + b.data[i];
		}
		vector add = new vector(res);
		return add;
	}
	
	public vector sub(vector b) {
		double[] res = new double[3];

		for (int i = 0; i < 3; i++) {
			res[i] = this.data[i] - b.data[i];
		}
		vector sub = new vector(res);
		return sub;
	}

	public vector multByScalar(double scalar) {
		double[] temp = new double[3];
		for (int i = 0; i < 3; i++) {
			temp[i] = scalar * this.data[i];
		}
		vector res = new vector(temp);
		return res;
	}

	public void normalize() {
		double temp = Math.sqrt(this.dotProduct(this));
		this.data[0] /= temp;
		this.data[1] /= temp;
		this.data[2] /= temp;
	}

}
