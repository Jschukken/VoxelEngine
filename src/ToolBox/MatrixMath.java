package ToolBox;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import Entitys.Camera;

public class MatrixMath {

	public static Matrix4f createTransMatrix(Vector3f position, float rotX, float rotY, float rotZ, Vector3f scale){
		Matrix4f matrix = new Matrix4f();
		matrix.setIdentity();
		
		Matrix4f.translate(position,matrix,matrix);
		
		Matrix4f.rotate(rotX, new Vector3f(1,0,0), matrix, matrix);//amount, axis, source, dest
		Matrix4f.rotate(rotY, new Vector3f(0,1,0), matrix, matrix);
		Matrix4f.rotate(rotZ, new Vector3f(0,0,1), matrix, matrix);
		
		Matrix4f.scale(scale, matrix,matrix);
		
		return matrix;
		
	}
	
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
