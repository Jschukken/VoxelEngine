package Shaders;

import java.util.List;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import Entities.Camera;
import Entities.Light;
import ToolBox.MatrixMath;

public class SkyboxShader extends ShaderProgram {
	/**
	 * Shader to process the skybox and prepare it for rendering
	 * @author Lars Gevers
	 */

	
	private static final String vertexFile = "/Shaders/skyBoxVertexShader.txt";
	private static final String fragmentFile = "/Shaders/skyBoxFragmentShader.txt";
	
	private int locationTransformationMatrix;
	private int locationProjectionMatrix;
	private int locationViewMatrix;
	private int locationSkyColour;
	
	public SkyboxShader(){
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
		locationSkyColour = super.getUniformLocation("skyColour");
		
	}
	
	/**
	 * loads the color of the sky into shader
	 * @param r the red component
	 * @param g the green component
	 * @param b the blue component
	 */
	public void loadSkyColour(float r, float g, float b){
		super.load3DVector(locationSkyColour, new Vector3f(r,g,b));
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
