package Shaders;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import Entities.Camera;
import Entities.Light;
import ToolBox.MatrixMath;


public class StaticShaderMenu extends ShaderProgram {

	
	private static final String vertexFile = "/Shaders/vertexShaderMenu.txt";
	private static final String fragmentFile = "/Shaders/fragmentShaderMenu.txt";
	
	private int locationTransformationMatrix;
	private int locationProjectionMatrix;
	
	public StaticShaderMenu(){
		super(vertexFile, fragmentFile);
	}
	
	@Override
	protected void bindAttributes(){
		//variable, vao attribute
		super.bindAttribute("position", 0);
		super.bindAttribute("textureCoords",1);
	}
	
	protected void getAllUniformLocations(){
		
		locationTransformationMatrix = super.getUniformLocation("transformationMatrix");
		locationProjectionMatrix = super.getUniformLocation("projectionMatrix");
	}
	
	public void loadTransformationMatrix(Matrix4f matrix){
		super.loadMatrix(locationTransformationMatrix, matrix);
	}
	
	public void loadProjectionMatrix(Matrix4f matrix){
		super.loadMatrix(locationProjectionMatrix, matrix);
	}
}