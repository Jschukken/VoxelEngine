package Flashlight;

import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import Entities.Button;
import Entities.Camera;
import Entities.DestinationEntity;
import Entities.EnemyEntity;
import Entities.Entity;
import Entities.ParticleEntity;
import Entities.PlayerAttack;
import Entities.SpawnPointEntity;
import GameEngine.AudioHandler;
import Guis.GuiRenderer;
import Guis.GuiTexture;
import Models.RawModel;
import Models.TexturedModel;
import RenderEngine.DisplayManager;
import RenderEngine.Loader;
import RenderEngine.MasterGameRenderer;
import RenderEngine.MasterMenuRenderer;
import RenderEngine.TexturedModelRenderer;
import Shaders.StaticShader;
import Shaders.StaticShaderMenu;
import Textures.ModelTexture;
import ToolBox.TexturedModelMaker;

/**
 * The main game manager
 * 
 * @author Jelle Schukken
 *
 */
public class MainGameLoop {

	private static final int RENDER_DISTANCE = 30;
	private static final int MIN_RENDER_DISTANCE = 3;
	private static final int PARTICLE_COUNT = 100;
	public static StaticShaderMenu menush = null;
	public static Button button1 = null;
	public static Loader loader = null;
	public static StaticShader sh = null;
	public static AudioHandler audH = null;
	public static int[][][] map = new int[20][10][20];

	private static String state = "startup";

	private static IntBuffer[] songID = null;

	public static List<Entity> mapEntities = new ArrayList<Entity>();
	public static List<Entity> activeEntities = new ArrayList<Entity>();
	public static List<Entity> particleEntities = new ArrayList<Entity>();
	public static List<Entity> attackEntities = new ArrayList<Entity>();
	public static Entity destination;

	private static boolean pauseCheck = false;

	public static void main(String[] args) {
		DisplayManager.createDisplay();
		Camera camera = null;
		StaticShader shader = null;
		StaticShaderMenu menuShader = null;
		MasterGameRenderer gameRenderer = null;
		MasterMenuRenderer menuRenderer = null;
		AudioHandler ah = null;
		List<GuiTexture> guis = new ArrayList<GuiTexture>();
		GuiRenderer guiRenderer = null;
		while (!Display.isCloseRequested()) {

			switch (state) {

			case "startup":
				camera = null;
				mapEntities.clear();
				activeEntities.clear();

				try {
					loader.cleanUp();
					sh.cleanUp();
					audH.cleanUp();
					menush.cleanUp();
					gameRenderer.cleanUp();
					menuRenderer.cleanUp();
					guiRenderer.cleanUp();
				} catch (NullPointerException e) {
					System.out.println("shit");
				}

				ah = new AudioHandler();
				audH = ah;
				songID = ah.createSound("song");
				ah.startSong(songID);
				loader = new Loader();
				shader = new StaticShader();
				sh = shader;
				menuShader = new StaticShaderMenu();
				menush = menuShader;
				gameRenderer = new MasterGameRenderer(shader);
				menuRenderer = new MasterMenuRenderer();
				createButtons(loader);
				GuiTexture gui = new GuiTexture(loader.loadTexture("duck"), new Vector2f(0.5f, 0.5f), new Vector2f(0.25f, 0.25f));
				guis.add(gui);
				guiRenderer = new GuiRenderer(loader);
				state = "menu";
				GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
				break;

			case "menu":
				Mouse.setGrabbed(false);
				renderMenu(menuShader, menuRenderer);
				if (mouseInButton(button1)) {
					button1.setTexture(new ModelTexture(loader.loadTexture("Duck")));
					if (Mouse.isButtonDown(0)) {
						state = "loadmap";
						Mouse.setGrabbed(true);
					}
				} else {
					button1.setTexture(new ModelTexture(loader.loadTexture("Tile")));
				}
				
				break;

			case "loadmap":
				camera = new Camera(new Vector3f(0, map[0].length, 10), 0, 0, 0);
				loadMap(loader);
				state = "game";
				break;

			case "game":
				manageMusic();
				updateGame(camera);
				renderGame(gameRenderer, shader, camera);
				guiRenderer.render(guis);
				break;

			case "gameover":
				state = "startup";
				break;

			case "pause":
				manageMusic();
				pause();
				renderGame(gameRenderer, shader, camera);
				break;
			default:
				System.out.println("state error: " + state + " is an invalid state");
				System.exit(-1);
				break;

			}
		}

		DisplayManager.closeDisplay();
	}

	/**
	 * Creates cube entities in locations according to a map and adds them to
	 * the entity list, TODO: take map parameter and load to map
	 * 
	 * @param loader
	 *            used to load models to the gpu
	 */
	private static void loadMap(Loader loader) {
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
					} else if (x == 0 || y == 0 || z == 0 || z == map[0][0].length - 1 || x == map.length - 1) {
						mapEntities.add(new Entity(tMod, new Vector3f(x, y, z), 0, 0, 0, new Vector3f(1, 1, 1)));
						map[x][y][z] = 1;
					}
				}
			}
		}
		// Entity entity = new Entity(tMod,new Vector3f(0,0,-10),0,0,0,new
		// Vector3f(1,1,1));

	}

	/**
	 * updates all necessary entities, currently only camera
	 * 
	 * @param camera
	 *            object to update TODO: will like make a global camera variable
	 */
	private static void updateGame(Camera camera) {
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

		if (Keyboard.isKeyDown(Keyboard.KEY_E) && !pauseCheck) {
			pauseCheck = true;
		} else if (!Keyboard.isKeyDown(Keyboard.KEY_E) && pauseCheck) {
			state = "pause";
			pauseCheck = false;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_R)) {
			state = "startup";
		}

	}

	/**
	 * TEMPORARY, creates a simple map while the map generator is in progress,
	 * can be used for debugging
	 */
	private static void tempMapCreator() {
		// map = Map.createGoodMap();
		//
		// int[][][] backup_map = map.clone();
		// map = new
		// int[backup_map.length][backup_map[0][0].length][backup_map[0].length];
		// for (int x = 0; x < backup_map.length; x++) {
		// for (int y = 0; y < backup_map[0].length; y++) {
		// for (int z = 0; z < backup_map[0][0].length; z++) {
		// map[x][z][y] = backup_map[x][y][z];
		// }
		// }
		// }

		// for (int x = 0; x < map.length; x++) {
		// for (int z = 0; z < map[0].length; z++) {
		// for (int y = 0; y < map[0][0].length; y++) {
		// if((int)(Math.random()*10) == 1)
		// map[x][z][y] = 1;
		// }
		// }
		// }

		// map[1][1][1] = 2; map[10][1][2] = 2; map[10][1][18] = 2;
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
	 * renders all entities within the render distance and visible to the camera
	 * to the screen
	 * 
	 * @param renderer
	 *            the renderer that will be used
	 * @param shader
	 *            shader used for rendering TODO: like change this as well
	 * @param camera
	 *            the camera it will render to
	 */
	private static void renderGame(MasterGameRenderer renderer, StaticShader shader, Camera camera) {

		renderer.prepare();

		shader.start();
		shader.loadViewMatrix(camera);
		try {
			renderer.render(destination, shader);
		} catch (NullPointerException e) {
			System.out.println("destination is required");
		}

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
			renderer.render(entity, shader);
		}
		for (Entity entity : attackEntities) {
			renderer.render(entity, shader);
		}
		shader.stop();

		DisplayManager.updateDisplay();
	}

	/**
	 * mutes the music
	 */
	private static void manageMusic() {
		if (Keyboard.isKeyDown(Keyboard.KEY_M)) {
			audH.endSong(songID);
		}
	}

	/**
	 * manages pause state
	 */
	private static void pause() {
		if (Keyboard.isKeyDown(Keyboard.KEY_E) && !pauseCheck) {
			pauseCheck = true;
		} else if (!Keyboard.isKeyDown(Keyboard.KEY_E) && pauseCheck) {
			state = "game";
			pauseCheck = false;
		}
	}

	/**
	 * sets the state of the game
	 * 
	 * @param newState
	 *            the new state
	 */
	public static void setState(String newState) {
		state = newState;
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
	public static void addActiveEntity(TexturedModel entity, Vector3f position, int[] path) {
		Entity enemy = new EnemyEntity(entity, position, 0, 0, 0, new Vector3f(1, 1, 1), path);
		activeEntities.add(enemy);

	}

	public static void addParticleEntity(TexturedModel entity, Vector3f position) {
		Entity particle = new ParticleEntity(entity, position,0,0,0, new Vector3f(.1f, .1f, .1f));
		particleEntities.add(particle);
		if (particleEntities.size() > PARTICLE_COUNT) {
			particleEntities.remove(0);
		}
	}
	
	public static void addAttackEntity(TexturedModel entity, Vector3f position, Vector3f rot, Vector3f direction) {
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
	public static void removeActiveEntity(Entity entity) {

		activeEntities.remove(entity);
	}

	/**
	 * Creates buttons for the main menu. Currently only the start button. TODO:
	 * Create new button object for genericity
	 * 
	 * @param loader
	 *            loader required to load the models.
	 */
	public static void createButtons(Loader loader) {
		float[] vertices = { -0.5f, 0.5f, -1f, -0.5f, -0.5f, -1f, 0.5f, -0.5f, 1f, 0.5f, 0.5f, -1f };

		int[] indices = { 0, 1, 3, 3, 2, 1 };

		float[] uv = { 0, 0, 0, 1, 1, 1, 1, 0 };

		RawModel model = loader.loadToVao(vertices, indices, uv);

		ModelTexture texture = new ModelTexture(loader.loadTexture("Tile"));
		Button tMod = new Button(model, texture, vertices);
		button1 = tMod;
	}

	/**
	 * Renders the menu
	 * 
	 * @param shader
	 *            the shader required to give textures to the model
	 */
	public static void renderMenu(StaticShaderMenu shader, MasterMenuRenderer renderer) {
		renderer.prepare();
		shader.start();
		TexturedModelRenderer.render(button1, shader);
		shader.stop();
		DisplayManager.updateDisplay();
	}

	/**
	 * Checks whether the mouse is inside the boundaries of the button TODO:
	 * Change to work with new Button object
	 * 
	 * @return whether the mouse is inside the button
	 */
	public static boolean mouseInButton(Button button) {
		int width = Display.getDisplayMode().getWidth();
		int height = Display.getDisplayMode().getHeight();
		float mouseX = (float) (-1.0 + 2.0 * Mouse.getX() / width);
		float mouseY = (float) (-1.0 + 2.0 * Mouse.getY() / height);
		
		if (mouseX > button.getLeftX() && mouseX < button.getRightX() && mouseY > button.getBotY() && mouseY < button.getTopY()) {
			return true;
		}
		return false;
	}

}
