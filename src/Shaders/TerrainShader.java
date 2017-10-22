package Shaders;

import java.util.List;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import Entities.Camera;
import Entities.Light;
import ToolBox.MatrixMath;

public class TerrainShader extends ShaderProgram {

	
	private static final String vertexFile = "/Shaders/terrainVertexShader.txt";
	private static final String fragmentFile = "/Shaders/terrainFragmentShader.txt";
	
	private int locationTransformationMatrix;
	private int locationProjectionMatrix;
	private int locationViewMatrix;
	private int locationLightPosition[];
	private int locationLightColour[];
	private int locationAttenuation[];
	private int locationShineDamper;
	private int locationReflectivity;
	private int locationSkyColour;
	private int locationMAXLIGHTS;
	private int locationAmbient;
	private int locationToShadowMapSpace;
	private int locationShadowMap;
	
	public TerrainShader(){
		super(vertexFile, fragmentFile);
	}
	
	@Override
	protected void bindAttributes(){
		//variable, vao attribute
		super.bindAttribute("position", 0);
		super.bindAttribute("textureCoords",1);
		super.bindAttribute("normal", 2);
	}
	
	protected void getAllUniformLocations(){
		
		locationTransformationMatrix = super.getUniformLocation("transformationMatrix");
		locationProjectionMatrix = super.getUniformLocation("projectionMatrix");
		locationViewMatrix = super.getUniformLocation("viewMatrix");
		locationShineDamper = super.getUniformLocation("shineDamper");
		locationReflectivity = super.getUniformLocation("reflectivity");
		locationSkyColour = super.getUniformLocation("skyColour");
		locationToShadowMapSpace = super.getUniformLocation("toShadowMapSpace");
		locationShadowMap = super.getUniformLocation("shadowMap");
		
		locationAmbient = super.getUniformLocation("ambient");
		locationMAXLIGHTS = super.getUniformLocation("MAXLIGHTS");
		locationLightPosition = new int[10];
		locationLightColour = new int[10];
		locationAttenuation = new int[10];
		for (int i = 0; i < Map.Map.LIGHTS; i++){
			locationLightPosition[i] = super.getUniformLocation("lightPosition[" + i + "]");
			locationLightColour[i] = super.getUniformLocation("lightColour[" + i + "]");
			locationAttenuation[i] = super.getUniformLocation("attenuation[" + i + "]");
		}
	}
	
	public void loadSkyColour(float r, float g, float b){
		super.load3DVector(locationSkyColour, new Vector3f(r,g,b));
	}
	
	public void loadAmbient(float ambient){
		super.loadFloat(locationAmbient, ambient);
	}
	
	public void loadLights(List<Light> lights){
		super.loadInt(locationMAXLIGHTS, lights.size());
		for (int i = 0; i < lights.size(); i++){
			try {
				if (lights.size() > i){
					super.load3DVector(locationLightPosition[i], lights.get(i).getPosition());
					super.load3DVector(locationLightColour[i], lights.get(i).getColour());
					super.load3DVector(locationAttenuation[i], lights.get(i).getAttenuation());
				}else{
					super.load3DVector(locationLightPosition[i], new Vector3f(0, 0, 0));
					super.load3DVector(locationLightColour[i], new Vector3f(0, 0, 0));
					super.load3DVector(locationAttenuation[i], new Vector3f(1, 0, 0));
				}
			} catch (Exception ignored) {
			}
		}
	}
	
	public void loadShineVariables(float shineDamper, float reflectivity){
		super.loadFloat(locationShineDamper, shineDamper);
		super.loadFloat(locationReflectivity, reflectivity);
	}
	
	public void connectTextureUnits(){
		super.loadInt(locationShadowMap, 5);
	}
	
	public void loadToShadowSpaceMatrix(Matrix4f matrix){
		super.loadMatrix(locationToShadowMapSpace, matrix);
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
