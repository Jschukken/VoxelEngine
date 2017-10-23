package Shaders;

import org.lwjgl.util.vector.Matrix4f;

/**
 * shader for shadows
 * @author Jelle Schukken
 *
 */
public class ShadowShader extends ShaderProgram {
	
	private static final String VERTEX_FILE = "/Shaders/shadowVertexShader.txt";
	private static final String FRAGMENT_FILE = "/Shaders/shadowFragmentShader.txt";
	
	private int location_mvpMatrix;

	public ShadowShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	@Override
	protected void getAllUniformLocations() {
		location_mvpMatrix = super.getUniformLocation("mvpMatrix");
		
	}
	
	/**
	 * loads transformation matrix into shader
	 * @param mvpMatrix the transformation matrix
	 */
	public void loadMvpMatrix(Matrix4f mvpMatrix){
		super.loadMatrix(location_mvpMatrix, mvpMatrix);
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute("in_position", 0);
		super.bindAttribute("in_textureCoords", 1);
	}

}
