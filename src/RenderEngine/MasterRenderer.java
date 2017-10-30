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
import Shaders.ParticleShader;
import Shaders.SkyboxShader;
import Shaders.StaticShader;
import Shaders.TerrainShader;

/**
 * work in progress, the master renderer
 * @author Jelle Schukken
 *
 */
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
	
	private SkyboxShader skyBoxShader = new SkyboxShader();
	private SkyboxRenderer skyBoxRenderer = null;
	
	private StaticShader shader = new StaticShader();
	private EntityRenderer renderer = null;

	private TerrainShader terrainShader = new TerrainShader();
	private TerrainRenderer terrainRenderer = null;
	
	private ParticleShader particleShader = new ParticleShader();
	private ParticleRenderer particleRenderer = null;
	
	private ShadowMapMasterRenderer shadowMapRenderer = null;
	
	private Map<TexturedModel, List<Entity>> entities = new HashMap<TexturedModel, List<Entity>>();
	private Map<TexturedModel, List<Entity>> particleEntities = new HashMap<TexturedModel, List<Entity>>();
	private List<Entity> mapEntities = new ArrayList<Entity>();
	private Entity skyBox = null;
	
	private float ambient = 0.0f;
	
	public MasterRenderer(Camera cam){
		this.camera = cam;
		createProjectionMatrix();
		renderer = new EntityRenderer(shader, projectionMatrix);
		terrainRenderer = new TerrainRenderer(terrainShader, projectionMatrix);
		skyBoxRenderer = new SkyboxRenderer(skyBoxShader, projectionMatrix);
		particleRenderer = new ParticleRenderer(particleShader, projectionMatrix);
		//Day - Night random generator
		Random r = new Random();
		ambient = r.nextFloat() * (0.3f - 0.01f) + 0.02f;
		if (ambient < 0.15f){
			RED = 0;
			GREEN = 0;
			BLUE = 0;
		}else {

			RED = 0.5f;
			GREEN = 0.5f;
			BLUE = 0.5f;
		}
		shadowMapRenderer = new ShadowMapMasterRenderer(camera);
	}

	/**
	 * standard startup things, clear color, clear buffer, enable depth test
	 */
	public void prepare() {

		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glClearColor(MasterRenderer.RED, MasterRenderer.GREEN, MasterRenderer.BLUE, 1);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		GL13.glActiveTexture(GL13.GL_TEXTURE5);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, getShadowMapTexture());
	}

	/**
	 * creates projection matrix to convert models to camera view
	 */
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

	/**
	 * The render class will call all shaders to process the entities and lights, based on the camera position
	 * Things that are not visible to the camera will not be rendered
	 * There are 4 shaders for 4 kinds of entities
	 * Each shader renders an object differently so that the world looks more diverse
	 * @param entity the entity to render
	 * @param shader the shader that will be used
	 */
	public void render(List<Light> lights, Camera camera){
		prepare();
		skyBoxShader.start();
		skyBoxShader.loadSkyColour(RED, GREEN, BLUE);
		skyBoxShader.loadViewMatrix(camera);
		skyBoxRenderer.render(skyBox);
		shader.stop();
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
		particleShader.start();
		particleShader.loadViewMatrix(camera);
		particleRenderer.render(particleEntities);
		entities.clear();
		mapEntities.clear();
		particleEntities.clear();
	}
	
	/*
	 * All normal visible entities will be added to a batch to make them easier to render
	 */
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
	
	/*
	 * All visible map entities will be added to a list for rendering
	 */
	public void processMapEntity(Entity entity){
		mapEntities.add(entity);
	}
	
	/*
	 * All visible particles will be added to a batch to make them easier to render and remove from the scene
	 */
	public void processParticle(Entity entity){
		TexturedModel entityModel = entity.getModel();
		List<Entity> batch = particleEntities.get(entityModel);
		if (batch != null){
			batch.add(entity);
		}else{
			List<Entity> newBatch = new ArrayList<Entity>();
			newBatch.add(entity);
			particleEntities.put(entityModel, newBatch);
		}
	}
	
	/*
	 * Enables the skybox to be rendered
	 */
	public void processSkyBox(Entity skyBox){
		this.skyBox = skyBox;
	}
	
	/**
	 * A shadowmap overlay will be rendered on the screen
	 * @param entityList all entities
	 * @param lights the list of lights used to create the shadowmap
	 */
	public void renderShadowMap(List<Entity> entityList, List<Light> lights){
		for (Entity entity : entityList){
			processMapEntity(entity);
		}
		shadowMapRenderer.render(mapEntities, lights);
		mapEntities.clear();
	}
	
	/*
	 * Gets the 2D shadowmap as a texture, which contains the depth value of each visible entity to the light
	 */
	public int getShadowMapTexture(){
		return shadowMapRenderer.getShadowMap();
	}
	
	public void cleanUp(){
		shader.cleanUp();
		terrainShader.cleanUp();
		shadowMapRenderer.cleanUp();
	}
}
