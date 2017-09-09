package Shader;

public class StaticShader extends ShaderProgram {

	
	private static final String vertexFile = "";
	private static final String fragmentFile = "";
	
	public StaticShader(){
		super(vertexFile, fragmentFile);
	}
	
	@Override
	protected void bindAttributes(){
		
	}
}
