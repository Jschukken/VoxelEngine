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
import KNearest.KNearest;
import Map.Map;
import Map.MapEvaluation;
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
	private KNearest kNear;
	public int[][][] map;

	public List<Entity> mapEntities = new ArrayList<Entity>();
	public List<Entity> activeEntities = new ArrayList<Entity>();
	public List<Entity> particleEntities = new ArrayList<Entity>();
	public List<Entity> attackEntities = new ArrayList<Entity>();
	public Entity destination;
	public Camera camera;

	public MapManager() {
		kNear = new KNearest(5);
		map = createGoodMap();
		loader = new Loader();
		shader = new StaticShader();
		renderer = new MasterGameRenderer(shader);
	}
	
	/**
	 * creates a good map using the create map function and validating using
	 * k-nearest
	 * 
	 * @param return
	 *            the generated 'good' map
	 */
	private int[][][] createGoodMap() {
		boolean good = false;
		boolean valid = false;
		int errorCatch = 0;
		int[][][] map = new int[Map.SIZE][Map.SIZE][Map.HEIGHT];

		
		while (!good && !valid) {
			errorCatch++;
			if(errorCatch>500){
				System.out.println("cannot generate a good map");
				cleanUp();
				System.exit(-1);
			}
			
			valid = false;
			good = false;
			Map.createMap();
			Map.print2D();
			
			List<Double> characteristics = new ArrayList<>();
			characteristics = MapEvaluation.characteristics(Map.m);
			if(characteristics.get((int) characteristics.size()-1) == 1) {//check if map is valid
				characteristics.remove(characteristics.size()-1);
				valid = true;
				//good = kNear.classify(characteristics); // use k-nearest
			}
			System.out.println(characteristics);
			//disable this when k-nearest works
			//valid = true;
			good = true;
		}
		map = Map.mapTo3D();
		int[][][] backUp = map; // backup for editing
		map = new int[map.length][map[0][0].length][map[0].length]; // empty and start over;

		// fix coordinates
		for (int x = 0; x < backUp.length; x++) {
			for (int z = 0; z < backUp[0].length; z++) {
				for (int y = 0; y < backUp[0][0].length; y++) {
					map[x][y][z] = backUp[x][z][y];
				}
			}
		}
		return map;
	}

	/**
	 * turns a map array into its corrisponding entities 
	 */
	public void loadMap() {
		camera = new Camera(new Vector3f(map.length / 2, map[0].length, map[0][0].length / 2), 0, 0, 0);
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
						camera.setPosition(new Vector3f(x,y+4,z));
						// } else if (x == 0 || y == 0 || z == 0 || z ==
						// map[0][0].length - 1 || x == map.length - 1) {
						// mapEntities.add(new Entity(tMod, new Vector3f(x, y,
						// z), 0, 0, 0, new Vector3f(1, 1, 1)));
						// map[x][y][z] = 1;
					}
				}
			}
		}
	}

	public void render() {

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

			if ((Math.acos(Vector3f.dot(toCamera, lookAt)) < Math.toRadians(MasterGameRenderer.FOV  + 5) || dist < MIN_RENDER_DISTANCE)
					&& dist < RENDER_DISTANCE) {
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
			if (dist < RENDER_DISTANCE) {
				renderer.render(destination, shader);
			}
		} catch (NullPointerException e) {
			System.out.println("destination is required");
		}

		shader.stop();
	}

	public void update() {
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
		for (int i = 0; i < attackEntities.size();i++) {
			attackEntities.get(i).update();
		}

	}

	public void cleanUp() {
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
		Entity particle = new ParticleEntity(entity, position, 0, 0, 0, new Vector3f(.1f, .1f, .1f));
		particleEntities.add(particle);
		if (particleEntities.size() > PARTICLE_COUNT) {
			particleEntities.remove(0);
		}
	}

	public void addAttackEntity(TexturedModel entity, Vector3f position, Vector3f rot, Vector3f direction, Vector3f scale) {
		Entity attack = new PlayerAttack(entity, position, rot, direction, scale);
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
	public void removeAttackEntity(Entity entity) {

		attackEntities.remove(entity);
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
