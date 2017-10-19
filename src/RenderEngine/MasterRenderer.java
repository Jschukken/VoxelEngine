package RenderEngine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.util.vector.Matrix4f;

import Entities.Camera;
import Entities.Entity;
import Entities.Light;
import Models.TexturedModel;
import Shaders.StaticShader;
import Shaders.TerrainShader;

public class MasterRenderer {

	public static final float FOV = 70;// 70 degrees
	public static final float NEAR_PLANE = -.25f;// closest rendered object
	public static final float FAR_PLANE = 100f;// farthest rendered object
	public static float RED = 0.4f;
	public static float GREEN = 0.7f;
	public static float BLUE = 1.0f;
	
	public Camera camera;
	
	public static final int MAX_LIGHTS = 4;
	
	private Matrix4f projectionMatrix;
	
	private StaticShader shader = new StaticShader();
	private EntityRenderer renderer = null;

	private TerrainShader terrainShader = new TerrainShader();
	private TerrainRenderer terrainRenderer = null;
	
	private ShadowMapMasterRenderer shadowMapRenderer = null;
	
	private Map<TexturedModel, List<Entity>> entities = new HashMap<TexturedModel, List<Entity>>();
	private List<Entity> mapEntities = new ArrayList<Entity>();
	
	private float ambient = 0.0f;
	
	public MasterRenderer(Camera cam){
		this.camera = cam;
		createProjectionMatrix();
		renderer = new EntityRenderer(shader, projectionMatrix);
		terrainRenderer = new TerrainRenderer(terrainShader, projectionMatrix);
		//Day - Night random generator
		Random r = new Random();
		ambient = r.nextFloat() * (0.3f - 0.01f) + 0.01f;
		if (ambient < 0.15f){
			RED = 0;
			GREEN = 0;
			BLUE = 0;
		}else {

			RED = 0.4f;
			GREEN = 0.7f;
			BLUE = 1.0f;
		}
		shadowMapRenderer = new ShadowMapMasterRenderer(camera);
	}

	public void prepare() {
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glClearColor(MasterRenderer.RED, MasterRenderer.GREEN, MasterRenderer.BLUE, 1);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		GL13.glActiveTexture(GL13.GL_TEXTURE5);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, getShadowMapTexture());
	}
	
	public void render(List<Light> lights, Camera camera){
		prepare();
		shader.start();
		shader.loadSkyColour(RED, GREEN, BLUE);
		shader.loadLights(lights);
		shader.loadAmbient(ambient);
		shader.loadViewMatrix(camera);
		renderer.render(entities, shadowMapRenderer.getToShadowMapSpaceMatrix());
		shader.stop();
		terrainShader.start();
		terrainShader.loadSkyColour(RED, GREEN, BLUE);
		terrainShader.loadLights(lights);
		terrainShader.loadAmbient(ambient);
		terrainShader.loadViewMatrix(camera);
		terrainRenderer.render(mapEntities, shadowMapRenderer.getToShadowMapSpaceMatrix());
		terrainShader.stop();
		entities.clear();
		mapEntities.clear();
	}
	
	public void processEntity(Entity entity){
		TexturedModel entityModel = entity.getModel();
		List<Entity> batch = entities.get(entityModel);
		if (batch != null){
			batch.add(entity);
		}else{
			List<Entity> newBatch = new ArrayList<Entity>();
			newBatch.add(entity);
			entities.put(entityModel, newBatch);
		}
	}
	
	public void processMapEntity(Entity entity){
		mapEntities.add(entity);
	}
	
	public void createProjectionMatrix() {

		projectionMatrix = new Matrix4f();

		float aspect = (float) Display.getWidth() / (float) Display.getHeight();
		float yScale = 1f / (float) Math.tan(Math.toRadians(FOV / 2f));
		float xScale = yScale / aspect;
		float zp = NEAR_PLANE + FAR_PLANE;
		float zm = NEAR_PLANE - FAR_PLANE;

		projectionMatrix.m00 = xScale;
		projectionMatrix.m11 = yScale;
		projectionMatrix.m22 = zp / zm;
		projectionMatrix.m23 = -1;
		projectionMatrix.m32 = (2 * FAR_PLANE * NEAR_PLANE) / zm;
	}
	
	public void renderShadowMap(List<Entity> entityList, Light sun){
		for (Entity entity : entityList){
			processMapEntity(entity);
		}
		shadowMapRenderer.render(mapEntities, sun);
		mapEntities.clear();
	}
	
	public int getShadowMapTexture(){
		return shadowMapRenderer.getShadowMap();
	}
	
	public void cleanUp(){
		shader.cleanUp();
		terrainShader.cleanUp();
		shadowMapRenderer.cleanUp();
	}
}
