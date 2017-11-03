package Shaders;

import org.lwjgl.util.vector.Matrix4f;

/**
 * Shader for the GUI renderer
 * 
 * @author Berk Aksakal
 *
 */
public class GuiShader extends ShaderProgram{
	
	//Specify the location of the shader files
	private static final String VERTEX_FILE = "/Shaders/guiVertexShader.txt";
	private static final String FRAGMENT_FILE = "/Shaders/guiFragmentShader.txt";
	
	private int location_transformationMatrix;

	public GuiShader() {
		// Loading the shader files
		super(VERTEX_FILE, FRAGMENT_FILE);
	}
	// Loads up a transformation matrix
	public void loadTransformation(Matrix4f matrix){
		super.loadMatrix(location_transformationMatrix, matrix);
	}

	//Gets the location of the transformation matrix
	
	@Override
	protected void getAllUniformLocations() {
		location_transformationMatrix = super.getUniformLocation("transformationMatrix");
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute("pos", 0);
	}
	
	
	

}
