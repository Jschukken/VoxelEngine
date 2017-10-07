package GameEngine;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.vector.Vector3f;

import Entities.Camera;
import Entities.DestinationEntity;
import Entities.EnemyEntity;
import Entities.Entity;
import Entities.ParticleEntity;
import Entities.PlayerAttack;
import Entities.SpawnPointEntity;
import Map.Map;
import Models.TexturedModel;
import RenderEngine.Loader;
import RenderEngine.MasterGameRenderer;
import Shaders.StaticShader;
import ToolBox.TexturedModelMaker;

public class MapManager {
	
	private static final int RENDER_DISTANCE = 30;
	private static final int MIN_RENDER_DISTANCE = 3;
	private static final int PARTICLE_COUNT = 300;
	
	private Loader loader;
	private MasterGameRenderer renderer;
	private StaticShader shader;
	public  int[][][] map;
	
	public List<Entity> mapEntities = new ArrayList<Entity>();
	public List<Entity> activeEntities = new ArrayList<Entity>();
	public List<Entity> particleEntities = new ArrayList<Entity>();
	public List<Entity> attackEntities = new ArrayList<Entity>();
	public Entity destination;
	public Camera camera;
	
	public MapManager(){
		map = Map.createGoodMap();
		loader = new Loader();
		shader = new StaticShader();
		renderer = new MasterGameRenderer(shader);
	}
	
	public void loadMap(){
		camera = new Camera(new Vector3f(map.length/2, map[0].length, map[0][0].length/2), 0, 0, 0);
		TexturedModel tMod = TexturedModelMaker.cubeTexturedModel(loader);
		for (int x = 0; x < map.length; x++) {
			for (int y = 0; y < map[0].length; y++) {
				for (int z = 0; z < map[0][0].length; z++) {
					if (map[x][y][z] == 1) {
						mapEntities.add(new Entity(tMod, new Vector3f(x, y, z), 0, 0, 0, new Vector3f(1, 1, 1)));
					} else if (map[x][y][z] == 2) {

						int[] arr = { 1, 2, map[0][0].length - 2, map.length - 2, map[0].length, map[0][0].length - 2,
								map.length - 2, map[0].length, 1, 1, map[0].length, 1 };
						activeEntities.add(new SpawnPointEntity(tMod, new Vector3f(x, y, z), 0, 0, 0,
								new Vector3f(1, 1, 1), tMod, arr));

					} else if (map[x][y][z] == 3) {
						destination = new DestinationEntity(tMod, new Vector3f(x, y, z), 0, 0, 0,
								new Vector3f(1, 1, 1));
//					} else if (x == 0 || y == 0 || z == 0 || z == map[0][0].length - 1 || x == map.length - 1) {
//						mapEntities.add(new Entity(tMod, new Vector3f(x, y, z), 0, 0, 0, new Vector3f(1, 1, 1)));
//						map[x][y][z] = 1;
					}
				}
			}
		}
	}
	
	public void render(){

		renderer.prepare();

		shader.start();
		shader.loadViewMatrix(camera);

		// vector the camera is looking at
		Vector3f lookAt = camera.getLookAt();

		lookAt.normalise();

		Vector3f toCamera;
		for (Entity entity : mapEntities) {

			// vector from the entity to the camera
			toCamera = new Vector3f(camera.getPosition().x - entity.getPosition().x,
					camera.getPosition().y - entity.getPosition().y,
					camera.getPosition().z - entity.getPosition().z + 0.01f);

			toCamera.normalise();

			double dist = Math.sqrt(Math.pow(camera.getPosition().x - entity.getPosition().x, 2)
					+ Math.pow(camera.getPosition().y - entity.getPosition().y, 2)
					+ Math.pow(camera.getPosition().z - entity.getPosition().z, 2));

			if ((Math.acos(Vector3f.dot(toCamera, lookAt)) < Math.toRadians(MasterGameRenderer.FOV + 5)
					|| dist < MIN_RENDER_DISTANCE) && dist < RENDER_DISTANCE) {
				renderer.render(entity, shader);
			}

		}
		
		for (Entity entity : activeEntities) {

			// vector from the entity to the camera
			toCamera = new Vector3f(camera.getPosition().x - entity.getPosition().x,
					camera.getPosition().y - entity.getPosition().y,
					camera.getPosition().z - entity.getPosition().z + 0.01f);

			toCamera.normalise();

			double dist = Math.sqrt(Math.pow(camera.getPosition().x - entity.getPosition().x, 2)
					+ Math.pow(camera.getPosition().y - entity.getPosition().y, 2)
					+ Math.pow(camera.getPosition().z - entity.getPosition().z, 2));

			if ((Math.acos(Vector3f.dot(toCamera, lookAt)) < Math.toRadians(MasterGameRenderer.FOV + 5)
					|| dist < MIN_RENDER_DISTANCE) && dist < RENDER_DISTANCE) {
				renderer.render(entity, shader);
			}

		}
		
		for (Entity entity : particleEntities) {
			// vector from the entity to the camera
			toCamera = new Vector3f(camera.getPosition().x - entity.getPosition().x,
					camera.getPosition().y - entity.getPosition().y,
					camera.getPosition().z - entity.getPosition().z + 0.01f);

			toCamera.normalise();

			double dist = Math.sqrt(Math.pow(camera.getPosition().x - entity.getPosition().x, 2)
					+ Math.pow(camera.getPosition().y - entity.getPosition().y, 2)
					+ Math.pow(camera.getPosition().z - entity.getPosition().z, 2));

			if ((Math.acos(Vector3f.dot(toCamera, lookAt)) < Math.toRadians(MasterGameRenderer.FOV + 5)
					|| dist < MIN_RENDER_DISTANCE) && dist < RENDER_DISTANCE) {
				renderer.render(entity, shader);
			}
		}
		
		for (Entity entity : attackEntities) {
			// vector from the entity to the camera
			toCamera = new Vector3f(camera.getPosition().x - entity.getPosition().x,
					camera.getPosition().y - entity.getPosition().y,
					camera.getPosition().z - entity.getPosition().z + 0.01f);

			toCamera.normalise();

			double dist = Math.sqrt(Math.pow(camera.getPosition().x - entity.getPosition().x, 2)
					+ Math.pow(camera.getPosition().y - entity.getPosition().y, 2)
					+ Math.pow(camera.getPosition().z - entity.getPosition().z, 2));

			if ((Math.acos(Vector3f.dot(toCamera, lookAt)) < Math.toRadians(MasterGameRenderer.FOV + 5)
					|| dist < MIN_RENDER_DISTANCE) && dist < RENDER_DISTANCE) {
				renderer.render(entity, shader);
			}
		}

		try {
			double dist = Math.sqrt(Math.pow(camera.getPosition().x - destination.getPosition().x, 2)
					+ Math.pow(camera.getPosition().y - destination.getPosition().y, 2)
					+ Math.pow(camera.getPosition().z - destination.getPosition().z, 2));
			if(dist<RENDER_DISTANCE){
				renderer.render(destination, shader);
			}
		} catch (NullPointerException e) {
			System.out.println("destination is required");
		}
		
		shader.stop();
	}
	
	public void update(){
		camera.move();
		try {
			destination.update();
		} catch (NullPointerException e) {
			System.out.println("destination is required");
		}
		
		for (int i = 0; i < activeEntities.size(); i++) {
			activeEntities.get(i).update();
		}
		for (Entity particle : particleEntities) {
			particle.update();
		}
		for (Entity entity : attackEntities) {
			entity.update();
		}

	}
	
	public void cleanUp(){
		try {
			loader.cleanUp();
			shader.cleanUp();
			renderer.cleanUp();
		} catch (NullPointerException e) {
			System.out.println("CleanUp error: its cool yo");
		}
	}
	
	/**
	 * adds an active entity to the game
	 * 
	 * @param entity
	 *            the entity to add
	 * @param position
	 *            the position of the entity
	 * @param path
	 *            the path the entity follows :TODO, make this better. it is
	 *            silly
	 */
	public void addActiveEntity(TexturedModel entity, Vector3f position, int[] path) {
		Entity enemy = new EnemyEntity(entity, position, 0, 0, 0, new Vector3f(1, 1, 1), path);
		activeEntities.add(enemy);

	}
	
	public void addParticleEntity(TexturedModel entity, Vector3f position) {
		Entity particle = new ParticleEntity(entity, position,0,0,0, new Vector3f(.1f, .1f, .1f));
		particleEntities.add(particle);
		if (particleEntities.size() > PARTICLE_COUNT) {
			particleEntities.remove(0);
		}
	}
	
	public void addAttackEntity(TexturedModel entity, Vector3f position, Vector3f rot, Vector3f direction) {
		Entity attack = new PlayerAttack(entity, position, rot, direction, new Vector3f(.5f, .5f, .5f));
		attackEntities.add(attack);
		if (attackEntities.size() > PARTICLE_COUNT) {
			attackEntities.remove(0);
		}
	}
	
	/**
	 * removes the given entity from the game
	 * 
	 * @param entity
	 *            the entity to remove
	 */
	public void removeActiveEntity(Entity entity) {

		activeEntities.remove(entity);
	}
	
	/**
	 * TEMPORARY, creates a simple map while the map generator is in progress,
	 * can be used for debugging
	 */
	private void tempMapCreator() {
		map[18][1][10] = 2;
		map[2][1][10] = 2;
		map[10][3][10] = 3;
		map[10][1][10] = 1;
		map[9][1][10] = 1;
		map[9][1][9] = 1;
		map[10][1][9] = 1;
		map[11][1][10] = 1;
		map[11][1][11] = 1;
		map[10][1][11] = 1;
		map[9][1][11] = 1;
		map[11][1][9] = 1;
		map[10][2][10] = 1;

	}

	

}
