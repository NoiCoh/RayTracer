package RayTracing;

import java.awt.Transparency;
import java.awt.color.*;
import java.awt.image.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.imageio.ImageIO;

/**
 * Main class for ray tracing exercise.
 */
public class RayTracer {

	public int imageWidth;
	public int imageHeight;
	List<Primitive> primitives = new ArrayList<Primitive>();
	List<Primitive> UpdatedPrimitives = new ArrayList<Primitive>();
	List<Material> materials = new ArrayList<Material>();
	List<Light> lights = new ArrayList<Light>();
	Cam cam = new Cam();
	Set set = new Set();
	Scene scene = new Scene();

	/**
	 * Runs the ray tracer. Takes scene file, output image file and image size as
	 * input.
	 */
	public static void main(String[] args) {

		try {

			RayTracer tracer = new RayTracer();

			// Default values:
			tracer.imageWidth = 500;
			tracer.imageHeight = 500;

			if (args.length < 2)
				throw new RayTracerException(
						"Not enough arguments provided. Please specify an input scene file and an output image file for rendering.");

			String sceneFileName = args[0];
			String outputFileName = args[1];

			if (args.length > 3) {
				tracer.imageWidth = Integer.parseInt(args[2]);
				tracer.imageHeight = Integer.parseInt(args[3]);
			}

			// Parse scene file:
			tracer.parseScene(sceneFileName);

			// Render scene:
			tracer.renderScene(outputFileName);

			// } catch (IOException e) {
			// System.out.println(e.getMessage());
		} catch (RayTracerException e) {
			System.out.println(e.getMessage());
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

	}

	/**
	 * Parses the scene file and creates the scene. Change this function so it
	 * generates the required objects.
	 */
	public void parseScene(String sceneFileName) throws IOException, RayTracerException {
		FileReader fr = new FileReader(sceneFileName);

		BufferedReader r = new BufferedReader(fr);
		String line = null;
		int lineNum = 0;
		System.out.println("Started parsing scene file " + sceneFileName);

		while ((line = r.readLine()) != null) {
			line = line.trim();
			++lineNum;

			if (line.isEmpty() || (line.charAt(0) == '#')) { // This line in the scene file is a comment
				continue;
			} else {
				String code = line.substring(0, 3).toLowerCase();
				// Split according to white space characters:
				String[] params = line.substring(3).trim().toLowerCase().split("\\s+");

				if (code.equals("cam")) {

					double[] temp1 = { Double.parseDouble(params[0]), Double.parseDouble(params[1]),
							Double.parseDouble(params[2]) };
					cam.setcamPosition(new vector(temp1));

					double[] temp2 = { Double.parseDouble(params[3]), Double.parseDouble(params[4]),
							Double.parseDouble(params[5]) };
					cam.setCamLookAtDirection((new vector(temp2)).add(cam.camPosition.multByScalar(-1)));

					double[] temp3 = { Double.parseDouble(params[6]), Double.parseDouble(params[7]),
							Double.parseDouble(params[8]) };
					cam.SetCamUpVector(new vector(temp3));

					cam.SetCamScreenDistance(Float.parseFloat(params[9]));

					cam.SetCamScreenWidth(Float.parseFloat(params[10]));

					System.out.println(String.format("Parsed camera parameters (line %d)", lineNum));
				} else if (code.equals("set")) {
					float[] backgroundColor = { Float.parseFloat(params[0]), Float.parseFloat(params[1]),
							Float.parseFloat(params[2]) };
					set.setBackGroundColor(backgroundColor);

					set.setNumberOfShadowRays(Integer.parseInt(params[3]));

					set.setMaxNumberOfRecursion(Integer.parseInt(params[4]));

					set.setSuperSamplingLevel(1);
					set.setSuperSamplingLevel(Integer.parseInt(params[5]));

					System.out.println(String.format("Parsed general settings (line %d)", lineNum));
				} else if (code.equals("mtl")) {
					Material material = new Material();

					double[] diffuseColor = { Double.parseDouble(params[0]), Double.parseDouble(params[1]),
							Double.parseDouble(params[2]) };
					material.setDiffuseColor(diffuseColor);

					double[] specularColor = { Double.parseDouble(params[3]), Double.parseDouble(params[4]),
							Double.parseDouble(params[5]) };
					material.setSpecularColor(specularColor);

					double[] reflectionColor = { Double.parseDouble(params[6]), Double.parseDouble(params[7]),
							Double.parseDouble(params[8]) };
					material.setReflectionColor(reflectionColor);

					material.setPhongSpecularityCoefficient(Float.parseFloat(params[9]));

					material.setTransparency(Float.parseFloat(params[10]));

					// material.setBonus(Float.parseFloat(params[11]));

					materials.add(material);

					System.out.println(String.format("Parsed material (line %d)", lineNum));
				} else if (code.equals("sph")) {

					Sphere sphere = new Sphere();
					sphere.setCenter(params[0], params[1], params[2]);
					sphere.setRadius(params[3]);
					sphere.setMaterial(params[4]);

					primitives.add(sphere);

					System.out.println(String.format("Parsed sphere (line %d)", lineNum));
				} else if (code.equals("pln")) {

					Plane plane = new Plane();
					double[] temp = { Double.parseDouble(params[0]), Double.parseDouble(params[1]),
							Double.parseDouble(params[2]) };
					vector normal = new vector(temp);
					plane.setNormal(normal);

					plane.setOffset(Float.parseFloat(params[3]));

					plane.setMaterialIndex(Integer.parseInt(params[4]));

					primitives.add(plane);

					System.out.println(String.format("Parsed plane (line %d)", lineNum));
				} else if (code.equals("trg")) {

					double[] temp1 = { Double.parseDouble(params[0]), Double.parseDouble(params[1]),
							Double.parseDouble(params[2]) };
					vector vector1 = new vector(temp1);
					double[] temp2 = { Double.parseDouble(params[3]), Double.parseDouble(params[4]),
							Double.parseDouble(params[5]) };
					vector vector2 = new vector(temp2);
					double[] temp3 = { Double.parseDouble(params[6]), Double.parseDouble(params[7]),
							Double.parseDouble(params[8]) };
					vector vector3 = new vector(temp3);

					int MaterialIndex = Integer.parseInt(params[9]);

					Triangle trinagle = new Triangle(vector1, vector2, vector3, MaterialIndex);

					primitives.add(trinagle);

					System.out.println(String.format("Parsed plane (line %d)", lineNum));
				} else if (code.equals("lgt")) {
					double[] temp1 = { Double.parseDouble(params[0]), Double.parseDouble(params[1]),
							Double.parseDouble(params[2]) };
					vector LightPosition = new vector(temp1);

					double[] LightColor = { Double.parseDouble(params[3]), Double.parseDouble(params[4]),
							Double.parseDouble(params[5]) };
					float specularIntensity = Float.parseFloat(params[6]);
					float shadowIntensity = Float.parseFloat(params[7]);
					float lightRadius = Float.parseFloat(params[8]);

					Light light = new Light(LightPosition, LightColor, specularIntensity, shadowIntensity, lightRadius);

					lights.add(light);

					System.out.println(String.format("Parsed light (line %d)", lineNum));
				} else {
					System.out.println(String.format("ERROR: Did not recognize object: %s (line %d)", code, lineNum));
				}

			}
		}
		scene.setCam(cam);
		scene.setSet(set);
		scene.setMaterials(materials);
		scene.setPrimitives(primitives);
		scene.setLights(lights);
		if (!scene.checkValid()) {
			System.out.println("Error in Scene file");
		}

		System.out.println("Finished parsing scene file " + sceneFileName);

	}

	/**
	 * Renders the loaded scene and saves it to the specified file location.
	 */
	public void renderScene(String outputFileName) {
		long startTime = System.currentTimeMillis();

		// Create a byte array to hold the pixel data:
		byte[] rgbData = new byte[this.imageWidth * this.imageHeight * 3];
		int ssl = set.SuperSamplingLevel;
		int ssHeight = imageHeight * ssl;
		int ssWidth = imageWidth * ssl;

		double[][] M = computeNewCoordinate();
		vector Vx = new vector(M[0]);
		vector Vy = new vector(M[1]);
		vector Vz = new vector(M[2]);
		Vx.normalize();
		Vy.normalize();
		Vz.normalize();
		vector P = (Vz.multByScalar(cam.camScreenDistance)).add(cam.camPosition);
		double aspectRatio = imageHeight / imageWidth;
		double camScreenHeight = aspectRatio * cam.camScreenWidth;

		vector P0 = ((Vy.multByScalar(-1 * camScreenHeight / 2)).add(Vx.multByScalar(-1 * cam.camScreenWidth / 2)))
				.add(P);

		double pixelWidth = (cam.camScreenWidth / ssWidth);
		double pixelHeight = (camScreenHeight / ssHeight);

		for (int y = 0; y < imageHeight; y++) {
			P = P0;
			for (int x = 0; x < imageWidth; x++) {
				Color finalcolor = new Color(0.0, 0.0, 0.0);
				vector ssP = P;
				for (int sRaw = 0; sRaw < ssl; sRaw++) {
					for (int sCol = 0; sCol < ssl; sCol++) {
						double heightOffset = ((ssl > 1) ? ((new Random().nextDouble() + sRaw) * (pixelWidth)) : 0);
						double widthOffset = ((ssl > 1) ? ((new Random().nextDouble() + sCol) * (pixelHeight)) : 0);
						ssP = (P.add(Vy.multByScalar(heightOffset))).add(Vx.multByScalar(widthOffset));
						Ray ray = new Ray(cam.camPosition, ssP.sub(cam.camPosition));
						ray.directionVector.normalize();
						Intersection hit = Intersection.FindIntersction(ray, primitives);
						if (hit.min_t == Double.MAX_VALUE) {
							finalcolor.r += set.backgroundColor[0];
							finalcolor.g += set.backgroundColor[1];
							finalcolor.b += set.backgroundColor[2];
						} else {
							UpdatedPrimitives = new ArrayList<Primitive>(primitives);
							UpdatedPrimitives.remove(hit.min_primitive);
							Color col = color(hit, ray, set.MaxNumberOfRecursion);
							finalcolor.r += col.r;
							finalcolor.g += col.g;
							finalcolor.b += col.b;
						}
					}
				}
				rgbData[(this.imageWidth * (this.imageHeight - y - 1) + imageWidth - x - 1)
						* 3] = (byte) (finalcolor.r * 255 / (ssl * ssl));
				rgbData[(this.imageWidth * (this.imageHeight - y - 1) + imageWidth - x - 1) * 3
						+ 1] = (byte) (finalcolor.g * 255 / (ssl * ssl));
				rgbData[(this.imageWidth * (this.imageHeight - y - 1) + imageWidth - x - 1) * 3
						+ 2] = (byte) (finalcolor.b * 255 / (ssl * ssl));

				P = P.add(Vx.multByScalar(cam.camScreenWidth / imageWidth));

			}
			P0 = P0.add(Vy.multByScalar(camScreenHeight / imageHeight));
		}
		// Write pixel color values in RGB format to rgbData:
		// Pixel [x, y] red component is in rgbData[(y * this.imageWidth + x) * 3]
		// green component is in rgbData[(y * this.imageWidth + x) * 3 + 1]
		// blue component is in rgbData[(y * this.imageWidth + x) * 3 + 2]
		//
		// Each of the red, green and blue components should be a byte, i.e. 0-255

		long endTime = System.currentTimeMillis();
		Long renderTime = endTime - startTime;

		// The time is measured for your own conveniece, rendering speed will not affect
		// your score
		// unless it is exceptionally slow (more than a couple of minutes)
		System.out.println("Finished rendering scene in " + renderTime.toString() + " milliseconds.");

		// This is already implemented, and should work without adding any code.
		saveImage(this.imageWidth, rgbData, outputFileName);

		System.out.println("Saved file " + outputFileName);

	}

	private Color color(Intersection hit, Ray ray, int recDepth) {
		if (recDepth == 0) {
			Color col = new Color(set.backgroundColor[0], set.backgroundColor[1], set.backgroundColor[2]);
			return col;
		}
		vector intersectionPoint = ray.basePoint.add(ray.directionVector.multByScalar(hit.min_t));
		vector N = hit.min_primitive.findNormal(intersectionPoint);
		if (N.dotProduct(ray.directionVector) > 0) {
			N = N.multByScalar(-1);
		}
		N.normalize();
		Color col = new Color();
		Material mat = materials.get(hit.min_primitive.getMterialIndex() - 1);

		for (Light light : scene.lights) {
			vector L = light.lightPosition.sub(intersectionPoint);
			double rTemp = 0;
			double gTemp = 0;
			double bTemp = 0;
			L.normalize();
			double cTeta = N.dotProduct(L);
			if (cTeta < 0) {
				continue;
			}
			rTemp += mat.diffuseColor[0] * light.lightColor[0] * cTeta;
			gTemp += mat.diffuseColor[1] * light.lightColor[1] * cTeta;
			bTemp += mat.diffuseColor[2] * light.lightColor[2] * cTeta;
			vector R = N.multByScalar((L.multByScalar(2).dotProduct(N))).sub(L);
			double sTeta = Math.pow(R.dotProduct(ray.directionVector.multByScalar(-1)),
					mat.phongSpecularityCoefficient);
			rTemp += mat.specularColor[0] * light.lightColor[0] * sTeta * (light.specularIntensity);
			gTemp += mat.specularColor[1] * light.lightColor[1] * sTeta * (light.specularIntensity);
			bTemp += mat.specularColor[2] * light.lightColor[2] * sTeta * (light.specularIntensity);

			double SoftshadowIntensity = softShadow(light, L.multByScalar(-1), intersectionPoint);
			col.r += rTemp * ((1 - light.shadowIntensity) + light.shadowIntensity * SoftshadowIntensity);
			col.g += gTemp * ((1 - light.shadowIntensity) + light.shadowIntensity * SoftshadowIntensity);
			col.b += bTemp * ((1 - light.shadowIntensity) + light.shadowIntensity * SoftshadowIntensity);
		}
		Color transfCol = new Color();
		if (mat.transparency > 0) {
			transfCol = culcTransColors(mat, N, ray, intersectionPoint, recDepth);
		}

		Color reflectionColor = new Color();
		if (mat.reflectionColor[0] > 0 || mat.reflectionColor[1] > 0 || mat.reflectionColor[2] > 0) {
			reflectionColor = culcRefColors(ray, N, intersectionPoint, mat, recDepth);
		}

		col.r = (col.r * (1 - mat.transparency) + transfCol.r * mat.transparency + reflectionColor.r);
		col.g = (col.g * (1 - mat.transparency) + transfCol.g * mat.transparency + reflectionColor.g);
		col.b = (col.b * (1 - mat.transparency) + transfCol.b * mat.transparency + reflectionColor.b);
		if (col.r > 1) {
			col.r = 1;
		}
		if (col.g > 1) {
			col.g = 1;
		}
		if (col.b > 1) {
			col.b = 1;
		}
		return col;
	}

	private Color culcRefColors(Ray ray, vector N, vector IntersectionPoint, Material mat, int recDepth) {
		Color col = new Color();

		vector R = ray.directionVector.add(N.multByScalar(-2 * ray.directionVector.dotProduct(N)));
		R.normalize();
		Ray refRay = new Ray(IntersectionPoint.add(R.multByScalar(0.001)), R);
		Intersection hit = Intersection.FindIntersction(refRay, primitives);

		if (hit.min_t == Double.MAX_VALUE) {

			col.r = (set.backgroundColor[0] * mat.reflectionColor[0]);
			col.g = (set.backgroundColor[1] * mat.reflectionColor[1]);
			col.b = (set.backgroundColor[2] * mat.reflectionColor[2]);
		} else {
			Color tempCol = color(hit, refRay, recDepth - 1);
			col.r = (tempCol.r * mat.reflectionColor[0]);
			col.g = (tempCol.g * mat.reflectionColor[1]);
			col.b = (tempCol.b * mat.reflectionColor[2]);
		}
		if (col.r > 1) {
			col.r = 1;
		}
		if (col.g > 1) {
			col.g = 1;
		}
		if (col.b > 1) {
			col.b = 1;
		}
		return col;
	}

	public Color culcTransColors(Material mat, vector N, Ray ray, vector intersectionPoint, int recDepth) {
		Color col = new Color();
		Ray transRay = new Ray(intersectionPoint.add(ray.directionVector.multByScalar(0.001)), ray.directionVector);
		Intersection transHit = Intersection.FindIntersction(transRay, UpdatedPrimitives);
		UpdatedPrimitives.remove(transHit.min_primitive);
		if (transHit.min_t != Double.MAX_VALUE) {
			Color tempCol = color(transHit, transRay, recDepth - 1);
			col.r = tempCol.r;
			col.g = tempCol.g;
			col.b = tempCol.b;
		} else {
			col.r = set.backgroundColor[0];
			col.g = set.backgroundColor[1];
			col.b = set.backgroundColor[2];
		}
		if (col.r > 1) {
			col.r = 1;
		}
		if (col.g > 1) {
			col.g = 1;
		}
		if (col.b > 1) {
			col.b = 1;
		}
		return col;
	}

	private double softShadow(Light light, vector planeNormal, vector intersectionPoint) {
		Plane lightArea = new Plane();
		lightArea.setNormal(planeNormal);
		lightArea.setOffset(lightArea.findOffset(light.lightPosition));
		vector v_vec = lightArea.findVecOnPlane(light.lightPosition);
		vector u_vec = planeNormal.crossProduct(v_vec);
		v_vec.normalize();
		u_vec.normalize();

		vector corner = (light.lightPosition.add(v_vec.multByScalar(-0.5 * light.lightRadius)))
				.add(u_vec.multByScalar(-0.5 * light.lightRadius));
		vector full_v = (corner.add(v_vec.multByScalar(light.lightRadius))).sub(corner);
		vector full_u = (corner.add(u_vec.multByScalar(light.lightRadius))).sub(corner);
		double scalar = 1.0 / set.NumberOfShadowRays;
		vector v = full_v.multByScalar(scalar);
		vector u = full_u.multByScalar(scalar);
		double sum = 0;
		for (int i = 0; i < set.NumberOfShadowRays; i++) {
			for (int j = 0; j < set.NumberOfShadowRays; j++) {
				sum += pointOnlight(light, corner, v, u, i, j, intersectionPoint);
			}
		}
		return sum / (set.NumberOfShadowRays * set.NumberOfShadowRays);
	}

	private int pointOnlight(Light light, vector corner, vector v, vector u, int i, int j, vector intersectionPoint) {
		Random random1 = new Random();
		double num1 = random1.nextDouble();
		double num2 = random1.nextDouble();
		vector point = corner.add(v.multByScalar(i + num1).add(u.multByScalar(j + num2)));
		vector pointDirction = point.sub(intersectionPoint);
		double Plength = Math.sqrt(pointDirction.dotProduct(pointDirction));
		if (checkDirectLight(pointDirction, intersectionPoint, Plength)) {
			return 0;
		}
		return 1;
	}

	private boolean checkDirectLight(vector L, vector intersectionPoint, double Llength) {

		L.normalize();
		Ray lightRay = new Ray(intersectionPoint.add(L.multByScalar(0.001)), L);
		return Intersection.isIntersect(lightRay, scene, Llength);

	}

	private double[][] computeNewCoordinate() {
		double[][] M = new double[3][3];
		vector Vx = cam.lookAtDirection.crossProduct(cam.camUpVector);
		Vx.normalize();
		cam.camUpVector = (Vx).crossProduct(cam.lookAtDirection);
		cam.camUpVector.normalize();
		M[0] = Vx.data;
		M[1] = cam.camUpVector.data;
		M[2] = cam.lookAtDirection.data;
		return M;
	}

	//////////////////////// FUNCTIONS TO SAVE IMAGES IN PNG FORMAT
	//////////////////////// //////////////////////////////////////////

	/*
	 * Saves RGB data as an image in png format to the specified location.
	 */
	public static void saveImage(int width, byte[] rgbData, String fileName) {
		try {

			BufferedImage image = bytes2RGB(width, rgbData);
			ImageIO.write(image, "png", new File(fileName));

		} catch (IOException e) {
			System.out.println("ERROR SAVING FILE: " + e.getMessage());
		}

	}

	/*
	 * Producing a BufferedImage that can be saved as png from a byte array of RGB
	 * values.
	 */
	public static BufferedImage bytes2RGB(int width, byte[] buffer) {
		int height = buffer.length / width / 3;
		ColorSpace cs = ColorSpace.getInstance(ColorSpace.CS_sRGB);
		ColorModel cm = new ComponentColorModel(cs, false, false, Transparency.OPAQUE, DataBuffer.TYPE_BYTE);
		SampleModel sm = cm.createCompatibleSampleModel(width, height);
		DataBufferByte db = new DataBufferByte(buffer, width * height);
		WritableRaster raster = Raster.createWritableRaster(sm, db, null);
		BufferedImage result = new BufferedImage(cm, raster, false, null);

		return result;
	}

	public static class RayTracerException extends Exception {
		public RayTracerException(String msg) {
			super(msg);
		}
	}

}
