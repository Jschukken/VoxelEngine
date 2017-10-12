package Shaders;

import org.lwjgl.util.vector.Matrix4f;

import Entities.Camera;
import Entities.Light;
import ToolBox.MatrixMath;

public class StaticShader extends ShaderProgram {

	
	private static final String vertexFile = "/Shaders/vertexShader.txt";
	private static final String fragmentFile = "/Shaders/fragmentShader.txt";
	
	private int locationTransformationMatrix;
	private int locationProjectionMatrix;
	private int locationViewMatrix;
	private int locationLightPosition;
	private int locationLightColour;
	
	public StaticShader(){
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
		locationViewMatrix = super.getUniformLocation("viewMatrix");
		locationLightPosition = super.getUniformLocation("lightPosition");
		locationLightColour = super.getUniformLocation("lightColour");
		
	}
	
	public void loadLight(Light light){
		super.load3DVector(locationLightPosition, light.getPosition());
		super.load3DVector(locationLightColour, light.getColour());
	}
	
	public void loadTransformationMatrix(Matrix4f matrix){
		super.loadMatrix(locationTransformationMatrix, matrix);
	}
	
	public void loadProjectionMatrix(Matrix4f matrix){
		super.loadMatrix(locationProjectionMatrix, matrix);
	}
	
	public void loadViewMatrix(Camera camera){
		super.loadMatrix(locationViewMatrix,MatrixMath.createViewMatrix(camera));
	}
}
