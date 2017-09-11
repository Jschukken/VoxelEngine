package ToolBox;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import Entitys.Camera;

/**
 * helper class for math functions
 * @author Jelle Schukken
 *
 */
public class MatrixMath {

	/**
	 * creates a transformation matrix for input transformation
	 * @param translation vec3 representing translation
	 * @param rotX amount rotated over x axis in radians
	 * @param rotY amount rotated over y axis in radians
	 * @param rotZ amount rotated over z axis in radians
	 * @param scale amount to scale
	 * @return the total transformation matrix
	 */
	public static Matrix4f createTransMatrix(Vector3f translation, float rotX, float rotY, float rotZ, Vector3f scale){
		Matrix4f matrix = new Matrix4f();
		matrix.setIdentity();
		
		Matrix4f.translate(translation,matrix,matrix);
		
		Matrix4f.rotate(rotX, new Vector3f(1,0,0), matrix, matrix);//amount, axis, source, dest
		Matrix4f.rotate(rotY, new Vector3f(0,1,0), matrix, matrix);
		Matrix4f.rotate(rotZ, new Vector3f(0,0,1), matrix, matrix);
		
		Matrix4f.scale(scale, matrix,matrix);
		
		return matrix;
		
	}
	
	
	/**
	 * creates a view transformation matrix given camera info
	 * @param camera the camera for which to create a view matrix
	 * @return the view matrix
	 */
	public static Matrix4f createViewMatrix(Camera camera){
		Matrix4f matrix = new Matrix4f();
		matrix.setIdentity();
		
		Matrix4f.rotate((float)Math.toRadians(camera.getRotX()), new Vector3f(1,0,0), matrix, matrix);//amount, axis, source, dest
		Matrix4f.rotate((float)Math.toRadians(camera.getRotY()), new Vector3f(0,1,0), matrix, matrix);// may need to turn angles into radians, we'll see
		Matrix4f.rotate((float)Math.toRadians(camera.getRotZ()), new Vector3f(0,0,1), matrix, matrix);
		
		Matrix4f.translate(new Vector3f(-camera.getPosition().x,-camera.getPosition().y,-camera.getPosition().z),matrix,matrix);
		
		return matrix;
		
	}
}
