package Shaders;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

/**
 * abstract class, make a shader program, start it to use it, stop it to stop using it, and cleanup to delete it
 * @author Jelle Schukken
 *
 */
public abstract class ShaderProgram {
	
	private int programID;
	private int vertexShaderID;
	private int fragmentShaderID;
	
	private FloatBuffer matrixBuffer = BufferUtils.createFloatBuffer(16);
	
	/**
	 * take the vertex shader and the fragment shader and turn them into a shader program
	 * @param vertexFile the text of a vertex shader
	 * @param fragmentFile the text of a fragment shader
	 */
	public ShaderProgram(String vertexFile, String fragmentFile){
		programID = GL20.glCreateProgram();
		vertexShaderID = loadShader(vertexFile, GL20.GL_VERTEX_SHADER);
		fragmentShaderID = loadShader(fragmentFile, GL20.GL_FRAGMENT_SHADER);
				
		//attach shaders we created to the programs so starting the program starts using the shaders
		GL20.glAttachShader(programID, vertexShaderID);
		GL20.glAttachShader(programID, fragmentShaderID);
		bindAttributes();
		//im really not sure what this does, but google says i need it so ehhhh.
		GL20.glLinkProgram(programID);
		GL20.glValidateProgram(programID);
		
		getAllUniformLocations();
	}
	
	protected abstract void getAllUniformLocations();
	
	protected int getUniformLocation(String varName){
		return GL20.glGetUniformLocation(programID,varName);
		
	}
	
	//----------------- uniform loaders-------------------
	protected void loadFloat(int location, float value){
		
		GL20.glUniform1f(location, value);
		
	}
	
	protected void load2DVector(int location, Vector2f vec){
		
		GL20.glUniform2f(location, vec.x, vec.y);
		
	}
	
	protected void load3DVector(int location, Vector3f vec){
		
		GL20.glUniform3f(location, vec.x, vec.y, vec.z);
		
	}
	
	protected void loadMatrix(int location, Matrix4f matrix){
		
		matrix.store(matrixBuffer);
		matrixBuffer.flip();
		GL20.glUniformMatrix4(location,false,matrixBuffer);
		
	}
	
	protected void loadBool(int location, boolean bool){
		
		if(bool){
			loadFloat(location,1f);
		}else{
			loadFloat(location,0f);
		}
	}
	
	//----------------END uniform loaders------------------------
	
	protected abstract void bindAttributes();
	
	protected void bindAttribute(String variableName, int attribute){
		GL20.glBindAttribLocation(programID, attribute, variableName);
	}
	
	/**
	 * start using the program
	 */
	public void start(){
		
		GL20.glUseProgram(programID);
		
	}
	
	/**
	 * stop using the program
	 */
	public void stop(){
		
		GL20.glUseProgram(0);
		
	}
	
	/**
	 * delete all the stuff to prevent memory leaks brah
	 */
	public void cleanUp(){
		stop();
		GL20.glDetachShader(programID, vertexShaderID);
		GL20.glDetachShader(programID,  fragmentShaderID);
		GL20.glDeleteShader(vertexShaderID);
		GL20.glDeleteShader(fragmentShaderID);
		GL20.glDeleteProgram(programID);
	}

	/**
	 * loads shader from txt file into gpu
	 * @param file the file to load
	 * @param type the type of shader (vertex of fragment)
	 * @return the id of the shader in the gpu
	 */
	private int loadShader(String file, int type){
		
		//take text file and turn it into a line of text
		StringBuilder shaderSource = new StringBuilder();
		
		InputStream in = Class.class.getResourceAsStream(file);
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		
		String line;
		
		try{
			while((line = reader.readLine()) != null){
				shaderSource.append(line).append("//\n");
			}
			reader.close();
		}catch(IOException e){
			e.printStackTrace();
			System.out.println("error bitch");
			System.exit(-1);
		}
		
		//turn text into shader
		int shaderID = GL20.glCreateShader(type);
		GL20.glShaderSource(shaderID, shaderSource);
		GL20.glCompileShader(shaderID);
		
		//check compile status of shader
		if(GL20.glGetShaderi(shaderID,  GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE){
			System.out.println(GL20.glGetShaderInfoLog(shaderID,1000)); // prints compile log
			System.out.println("shit yo, shader didnt compile");
			System.exit(-1);
		}
		
		return shaderID;
	}
}
