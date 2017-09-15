package Flashlight;

import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import Entitys.Camera;
import Entitys.Entity;
import Entitys.TexturedModelMaker;
import GameEngine.AudioHandler;
import Models.RawModel;
import Models.TexturedModel;
import RenderEngine.DisplayManager;
import RenderEngine.Loader;
import RenderEngine.MasterRenderer;
import Shaders.StaticShader;
import Textures.ModelTexture;

/**
 * The main game manager
 * 
 * @author Jelle Schukken
 *
 */
public class MainGameLoop {

	private static final int RENDER_DISTANCE = 30;
	private static final int MIN_RENDER_DISTANCE = 3;
	public static Loader loader1 = null;
	public static StaticShader sh = null;
	public static AudioHandler audH = null;
	public static int[][][] map = new int[100][15][100];

	private static String state = "startup";

	private static IntBuffer[] songID = null;

	private static List<Entity> entities = new ArrayList<Entity>();

	private static boolean pauseCheck = false;

	public static void main(String[] args) {
		DisplayManager.createDisplay();
		Camera camera = null;
		Loader loader = null;
		StaticShader shader = null;
		MasterRenderer renderer = null;
		AudioHandler ah = null;

		while (!Display.isCloseRequested()) {

			switch (state) {

			case "startup":
				entities.clear();
				try {
					loader1.cleanUp();
					sh.cleanUp();
					audH.cleanUp();
				} catch (NullPointerException e) {

				}
				state = "menu";
				break;

			case "menu":
				state = "loadmap";
				break;

			case "loadmap":
				ah = new AudioHandler();
				audH = ah;
				songID = ah.createSound("song");
				ah.startSong(songID);
				camera = new Camera(new Vector3f(0, 20, 10), 0, 0, 0);
				loader = new Loader();
				loader1 = loader;
				shader = new StaticShader(); // temporary
				sh = shader;
				renderer = new MasterRenderer(shader);
				tempMapCreator();
				loadMap(loader);
				state = "game";
				break;

			case "game":
				manageMusic();
				updateGame(camera);
				renderGame(renderer, shader, camera);
				break;

			case "gameover":
				state = "startup";
				break;

			case "pause":
				manageMusic();
				pause();
				renderGame(renderer, shader, camera);
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
						entities.add(new Entity(tMod, new Vector3f(x, y, z), 0, 0, 0, new Vector3f(1, 1, 1)));
					} else if (x == 0 || y == 0 || z == 0 || z == map[0][0].length - 1 || x == map.length - 1) {
						entities.add(new Entity(tMod, new Vector3f(x, y, z), 0, 0, 0, new Vector3f(1, 1, 1)));
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

	private static void tempMapCreator(){
		for (int x = 0; x < map.length; x++) {
			for (int z = 0; z < map[0].length; z++) {
				for (int y = 0; y < map[0][0].length; y++) {
					if((int)(Math.random()*10) == 1)
						map[x][z][y] = 1;
				}
			}
		}
	}
	/**
	 * renders all entities within the render distance and visible to the camera to the screen
	 * 
	 * @param renderer
	 *            the renderer that will be used
	 * @param shader
	 *            shader used for rendering TODO: like change this as well
	 * @param camera
	 *            the camera it will render to
	 */
	private static void renderGame(MasterRenderer renderer, StaticShader shader, Camera camera) {

		renderer.prepare();

		shader.start();
		shader.loadViewMatrix(camera);
		
		//vector the camera is looking at
		Vector3f lookAt = new Vector3f(
				(float) (Math.cos(Math.toRadians(camera.getRotY()+90)) * Math.cos(Math.toRadians(camera.getRotX()))),
				(float) (Math.sin(Math.toRadians(camera.getRotX()))),
				(float) Math.sin(Math.toRadians(camera.getRotY()+90)));
		
		lookAt.normalise();
		
		Vector3f toCamera;
		
		for (Entity entity : entities) {
			
			//vector from the entity to the camera
			toCamera = new Vector3f(camera.getPosition().x-entity.getPosition().x,  camera.getPosition().y-entity.getPosition().y,
					camera.getPosition().z - entity.getPosition().z + 0.01f);
			
			toCamera.normalise();
			
			double dist = Math.sqrt(Math.pow(camera.getPosition().x - entity.getPosition().x, 2)
					+ Math.pow(camera.getPosition().y - entity.getPosition().y, 2)
					+ Math.pow(camera.getPosition().z - entity.getPosition().z, 2));
			
			
			if ((Math.acos(Vector3f.dot(toCamera, lookAt)) < Math.toRadians(MasterRenderer.FOV+5) || dist<MIN_RENDER_DISTANCE)
					&& dist < RENDER_DISTANCE) {
				renderer.render(entity, shader);
			}

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
	 * @param newState the new state
	 */
	public static void setState(String newState) {
		state = newState;
	}

}
