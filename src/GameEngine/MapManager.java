package GameEngine;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.vector.Vector3f;

import Entities.Camera;
import Entities.DestinationEntity;
import Entities.EnemyEntity;
import Entities.Entity;
import Entities.Light;
import Entities.ParticleEntity;
import Entities.PlayerAttack;
import Entities.SpawnPointEntity;
import Flashlight.MainGameLoop;
import KNearest.KNearest;
import KNearest.Point;
import Map.Map;
import Map.MapEvaluation;
import Models.TexturedModel;
import RenderEngine.GuiRenderer;
import RenderEngine.Loader;
import RenderEngine.MasterRenderer;
import ToolBox.TexturedModelMaker;

/**
 * Maneges and loads the map
 * 
 * @author Jelle Schukken
 * @edited Chiel Ton
 * @edited Frouke Hekker
 *
 */
public class MapManager {

	private static final int RENDER_DISTANCE = 30;
	private static final int MIN_RENDER_DISTANCE = 3;
	private static final int PARTICLE_COUNT = 1000;
	private static final int GENERATE_LIMIT = 50;

	private Loader loader;
	private MasterRenderer renderer;
	//private StaticShader shader;
	private KNearest kNear;
	public int[][][] map;

	public int[][] twoDMap;
	public List<Double> currentAttributes;

	public List<Entity> mapEntities = new ArrayList<Entity>();
	public List<Entity> wallEntities = new ArrayList<Entity>();
	public List<Entity> activeEntities = new ArrayList<Entity>();
	public List<Entity> particleEntities = new ArrayList<Entity>();
	public List<Entity> attackEntities = new ArrayList<Entity>();
	public List<Light> Lights = new ArrayList<Light>();
	public Entity skyBox;
	public DestinationEntity destination;
	public Camera camera;
	public static TexturedModel hitRunModel;
	public static TexturedModel runModel;
	public static TexturedModel hitNormalModel;
	public static TexturedModel normalModel;

	public MapManager() {
		currentAttributes = new ArrayList<Double>();
		kNear = new KNearest(5);
		map = createGoodMap();
		loader = new Loader();
		renderer = new MasterRenderer(camera);
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

		while (!good || !valid) {
			errorCatch++;
			if (errorCatch > GENERATE_LIMIT) {
				System.out.println("cannot generate a good map");
				cleanUp();
				MainGameLoop.setState("loadMapMenu");
				//System.exit(-1);
			}

			valid = false;
			good = false;
			Map.createMap();
			twoDMap = Map.m;

			List<Double> characteristics = new ArrayList<>();
			characteristics = MapEvaluation.characteristics(Map.m);
			currentAttributes.clear();
			if (characteristics.get(characteristics.size() - 1) == 1.0) {// check if map is valid
				characteristics.remove(characteristics.size() - 1);
				valid = true;
				good = kNear.classify(characteristics); // use k-nearest
			}
			currentAttributes.addAll(characteristics);
			characteristics.clear();
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
	 * turns a map array into its corresponding entities
	 */
	public void loadMap() {
		Lights = new ArrayList<Light>();
		Lights.add(new Light(new Vector3f((float)(map.length),(float)(map[0].length*5.0),(float)(map[0][0].length)), new Vector3f(1.0f, 1.0f, 1.2f), new Vector3f(.45f, 0, 0)));
		camera = new Camera(new Vector3f(map.length / 2, map[0].length, map[0][0].length / 2), 0, 0, 0);
		TexturedModel tMod = TexturedModelMaker.cubeTexturedModel(loader, "Tile");
		TexturedModel tWallMod = TexturedModelMaker.cubeTexturedModel(loader, "wallTile");
		TexturedModel tDestMod = TexturedModelMaker.cubeTexturedModel(loader, "Companion Cube");
		TexturedModel tSpawnMod = TexturedModelMaker.cubeTexturedModel(loader, "Antagonist Cube");
		hitRunModel = TexturedModelMaker.robotHitRunModel(loader);
		runModel = TexturedModelMaker.robotRunModel(loader);
		hitNormalModel = TexturedModelMaker.robotHitNormalModel(loader);
		normalModel = TexturedModelMaker.robotModel(loader);
		addSkyBoxEntity(TexturedModelMaker.skyBoxModel(loader), new Vector3f(camera.getPosition()));
		for (int x = 0; x < map.length; x++) {
			for (int y = 0; y < map[0].length; y++) {
				for (int z = 0; z < map[0][0].length; z++) {
					if (map[x][y][z] == 1) {
						if (y > 1)
							wallEntities.add(new Entity(tWallMod, new Vector3f(x, y, z), 0, 0, 0, new Vector3f(1, 1, 1)));
						else
							mapEntities.add(new Entity(tMod, new Vector3f(x, y, z), 0, 0, 0, new Vector3f(1, 1, 1)));
					} else if (map[x][y][z] == 2) {
						destination = new DestinationEntity(tDestMod, new Vector3f(x, y, z), 0, 0, 0,
								new Vector3f(1, 1, 1));
						camera.setPosition(new Vector3f(x, y + 6, z));
						// } else if (x == 0 || y == 0 || z == 0 || z ==
						// map[0][0].length - 1 || x == map.length - 1) {
						// mapEntities.add(new Entity(tMod, new Vector3f(x, y,
						// z), 0, 0, 0, new Vector3f(1, 1, 1)));
						// map[x][y][z] = 1;
					}
				}
			}
		}
		for (int x = 0; x < map.length; x++) {
			for (int y = 0; y < map[0].length; y++) {
				for (int z = 0; z < map[0][0].length; z++) {
					if (map[x][y][z] == 3) {
						activeEntities.add(new SpawnPointEntity(tSpawnMod, new Vector3f(x, y, z), 0, 0, 0,
								new Vector3f(1, 1, 1), normalModel));

					} else if (map[x][y][z] == 4){
						Vector3f position = new Vector3f(x, y, z);
						boolean toClose = false;
						for (Light light : Lights){
							double dist = Math.sqrt(Math.pow((position).x - light.getPosition().x, 2)
									+ Math.pow(position.y - light.getPosition().y, 2)
									+ Math.pow(position.z - light.getPosition().z, 2));
							if (dist < 10){
								toClose = true;
								break;
							}
						}
						if (toClose)
							continue;
						//Light light = new Light(position, new Vector3f(1.0f, 1.0f, 1.2f), new Vector3f(1f, 0.01f, 0.002f));
						//Lights.add(light);
						//mapEntities.add(new Entity(TexturedModelMaker.cubeTexturedModel(loader, "Tile"), light.getPosition(), 0, 0, 0, new Vector3f(1, 1, 1)));
					}
				}
			}
		}
	}

	/**
	 * renders all visible entities within range
	 */
	public void render() {
		//List<Light> sun = new ArrayList<Light>();
		//sun.add(new Light(new Vector3f((float)(map.length/2),(float)map[0].length,(float)(map[0][0].length/2)), new Vector3f(1.0f, 1.0f, 1.2f), new Vector3f(1f, 0.01f, 0.002f)));
		List<Entity> total = new ArrayList<Entity>(activeEntities);
		total.addAll(wallEntities);
		total.addAll(mapEntities);
		renderer.renderShadowMap(total, Lights);

		// vector the camera is looking at
		Vector3f lookAt = camera.getLookAt();

		lookAt.normalise();

		Vector3f toCamera;
		for (Entity entity : wallEntities) {

			// vector from the entity to the camera
			toCamera = new Vector3f(camera.getPosition().x - entity.getPosition().x,
					camera.getPosition().y - entity.getPosition().y,
					camera.getPosition().z - entity.getPosition().z + 0.01f);

			toCamera.normalise();

			double dist = Math.sqrt(Math.pow(camera.getPosition().x - entity.getPosition().x, 2)
					+ Math.pow(camera.getPosition().y - entity.getPosition().y, 2)
					+ Math.pow(camera.getPosition().z - entity.getPosition().z, 2));

			if ((Math.acos(Vector3f.dot(toCamera, lookAt)) < Math.toRadians(MasterRenderer.FOV  + 5) 
					|| dist < MIN_RENDER_DISTANCE) && dist < RENDER_DISTANCE) {
				renderer.processMapEntity(entity);
			}
		}
		
		for (Entity entity : mapEntities) {

			// vector from the entity to the camera
			toCamera = new Vector3f(camera.getPosition().x - entity.getPosition().x,
					camera.getPosition().y - entity.getPosition().y,
					camera.getPosition().z - entity.getPosition().z + 0.01f);

			toCamera.normalise();

			double dist = Math.sqrt(Math.pow(camera.getPosition().x - entity.getPosition().x, 2)
					+ Math.pow(camera.getPosition().y - entity.getPosition().y, 2)
					+ Math.pow(camera.getPosition().z - entity.getPosition().z, 2));

			if ((Math.acos(Vector3f.dot(toCamera, lookAt)) < Math.toRadians(MasterRenderer.FOV + 5)
					|| dist < MIN_RENDER_DISTANCE) && dist < RENDER_DISTANCE) {
				renderer.processMapEntity(entity);
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

			if ((Math.acos(Vector3f.dot(toCamera, lookAt)) < Math.toRadians(MasterRenderer.FOV + 5)
					|| dist < MIN_RENDER_DISTANCE) && dist < RENDER_DISTANCE) {
				renderer.processEntity(entity);
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

			if ((Math.acos(Vector3f.dot(toCamera, lookAt)) < Math.toRadians(MasterRenderer.FOV + 5)
					|| dist < MIN_RENDER_DISTANCE) && dist < RENDER_DISTANCE) {
				renderer.processParticle(entity);
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

			if ((Math.acos(Vector3f.dot(toCamera, lookAt)) < Math.toRadians(MasterRenderer.FOV + 5)
					|| dist < MIN_RENDER_DISTANCE) && dist < RENDER_DISTANCE) {
				renderer.processEntity(entity);
			}
		}

		try {
			double dist = Math.sqrt(Math.pow(camera.getPosition().x - destination.getPosition().x, 2)
					+ Math.pow(camera.getPosition().y - destination.getPosition().y, 2)
					+ Math.pow(camera.getPosition().z - destination.getPosition().z, 2));
			if (dist < RENDER_DISTANCE) {
				renderer.processEntity(destination);
			}
		} catch (NullPointerException e) {
			System.out.println("destination is required");
		}
		renderer.processSkyBox(skyBox);
		renderer.render(Lights, camera);
	}

	/**
	 * updates all entities
	 */
	public void update() {
		camera.update();
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
		for (int i = 0; i < attackEntities.size(); i++) {
			attackEntities.get(i).update();
		}

	}

	/**
	 * cleans up data
	 */
	public void cleanUp() {
		try {
			loader.cleanUp();
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
	 *            the path the entity follows :TODO, make this better. it is silly
	 */
	public void addActiveEntity(TexturedModel entity, Vector3f position, int[] path) {
		Entity enemy = new EnemyEntity(entity, position, 0, 0, 0, new Vector3f(1, 1, 1), path);
		int lvlhp = GuiRenderer.getLevel();
		((EnemyEntity) enemy).setHp(lvlhp*2 + 20);
		((EnemyEntity) enemy).setSpeed(lvlhp/100);
		activeEntities.add(enemy);

	}

	/**
	 * adds a particle entity to the game
	 * @param model the appearance of the particle
	 * @param position the position of the particle
	 */
	public void addParticleEntity(TexturedModel model, Vector3f position) {
		Entity particle = new ParticleEntity(model, position, 0, 0, 0, new Vector3f(.1f, .1f, .1f));
		particleEntities.add(particle);
		if (particleEntities.size() > PARTICLE_COUNT) {
			particleEntities.remove(0);
		}
	}

	/**
	 * adds an attack particle to the game
	 * @param model the model
	 * @param position the position
	 * @param rot the rotation
	 * @param direction the direction
	 * @param scale the scale
	 */
	public void addAttackEntity(TexturedModel model, Vector3f position, Vector3f rot, Vector3f direction,
			Vector3f scale) {
		Entity attack = new PlayerAttack(model, position, rot, direction, scale);
		attackEntities.add(attack);
		
	}

	/**
	 * adds skybox to game
	 */
	public void addSkyBoxEntity(TexturedModel entity, Vector3f position) {

		skyBox = new Entity(entity, position, 0, 0, 0, new Vector3f(250, 250, 250));
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
	 * creates a simple map, can
	 * be used for debugging
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
	
	/**
	 * Lets the kNearest this mapmanager has store its training data, used after a map
	 * is completed/failed so the new data point is stored
	 */
	public void kNearestSave() {
		try {
			kNear.writeDataToFile();
		} catch (IOException e) {};
	}
	
	/**
	 * Adds a given point to the knearest object and saves the data in the process
	 * 
	 * @param c  the class of the point
	 */
	public void addPointToKNearest(boolean c) {
		kNear.addDataPoint(new Point(currentAttributes, c));
		kNearestSave();
	}
	
	/**
	 * clears the data points for kNearest
	 */
	public void clearDataPoints() {
		kNear.clearPoints();
	}

}
