package Shaders;

import org.lwjgl.util.vector.Matrix4f;

import Entities.Camera;
import ToolBox.MatrixMath;

public class ParticleShader extends ShaderProgram {
	/**
	 * Shader to process the particles and prepare them for rendering
	 * @author Lars Gevers
	 */
	
	private static final String vertexFile = "/Shaders/particleVertexShader.txt";
	private static final String fragmentFile = "/Shaders/particleFragmentShader.txt";
	
	private int locationTransformationMatrix;
	private int locationProjectionMatrix;
	private int locationViewMatrix;
	
	public ParticleShader(){
		super(vertexFile, fragmentFile);
	}
	
	/**
	 * Bind the attributes (position, textureCoords and normal) to the Vertex and Fragment shader
	 */
	@Override
	protected void bindAttributes(){
		//variable, vao attribute
		super.bindAttribute("position", 0);
		super.bindAttribute("textureCoords",1);
		super.bindAttribute("normal", 2);
	}
	
	/**
	 * Bind the variables from the Vertex and Fragment shader to Java variables
	 */
	protected void getAllUniformLocations(){
		
		locationTransformationMatrix = super.getUniformLocation("transformationMatrix");
		locationProjectionMatrix = super.getUniformLocation("projectionMatrix");
		locationViewMatrix = super.getUniformLocation("viewMatrix");
		
	}
	
	//_______LOAD LIGHT ATTRIBUTES INTO SHADER____________
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
