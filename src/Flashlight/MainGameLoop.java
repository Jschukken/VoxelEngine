package Flashlight;

import java.io.IOException;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;

import Entities.Button;
import GameEngine.AudioHandler;
import GameEngine.MapManager;
import Guis.GuiTexture;
import KNearest.KNearest;
import Menu.MapMenuRenderer;
import Menu.MenuHandler;
import RenderEngine.DisplayManager;
import RenderEngine.GuiRenderer;
import RenderEngine.KNearestRendering;
import RenderEngine.Loader;
import RenderEngine.MasterMenuRenderer;
import Shaders.StaticShader;
import Shaders.StaticShaderMenu;
import Textures.ModelTexture;

/**
 * The main game manager
 * 
 * @author Jelle Schukken
 *
 */
public class MainGameLoop {

	public static StaticShaderMenu menush = null;
	public static Button button1 = null;
	public static Loader loader = null;
	public static StaticShader sh = null;
	public static AudioHandler audH = null;
<<<<<<< HEAD
	public static MenuHandler menuh;
	public static MapMenuRenderer mmr;
	public static KNearest kn;
	public static int[][][] map;
	List<GuiTexture> guis = new ArrayList<GuiTexture>();
	GuiRenderer guiRenderer = null;
=======
	public static int[][][] map = new int[50][15][50];
>>>>>>> parent of c7ef24e... Defence map example

	private static String state = "startup";

	private static IntBuffer[] songID = null;
	
	public static MapManager mapManager;

	private static boolean pauseCheck = false;

	public static void main(String[] args) {
		DisplayManager.createDisplay();
		StaticShaderMenu menuShader = null;
		MasterMenuRenderer menuRenderer = null;
		kn = new KNearest(5, "resources\\res\\2DknTrainingData.txt", "resources\\res\\2DknStoredData.txt");
		KNearestRendering knr = new KNearestRendering(kn);
		MapMenuRenderer mmr = new MapMenuRenderer();
		AudioHandler ah = null;
		List<GuiTexture> guis = new ArrayList<GuiTexture>();
		GuiRenderer guiRenderer = null;
		mapManager = null;
		//map = Map.createGoodMap();

		while (!Display.isCloseRequested()) {

			switch (state) {

			case "startup":

				try {
					guiRenderer.cleanUp();
					loader.cleanUp();
					audH.cleanUp();
					menush.cleanUp();
					menuRenderer.cleanUp();
					mapManager.cleanUp();
				} catch (NullPointerException e) {
					System.out.println("CleanUp error: its cool yo");
				}
				
				ah = new AudioHandler();
				audH = ah;
				songID = ah.createSound("song");
				ah.startSong(songID);
				loader = new Loader();
				menuShader = new StaticShaderMenu();
				menush = menuShader;
				menuRenderer = new MasterMenuRenderer();
				
				menuh = new MenuHandler();
				menuh.createMenus(loader);
				
				/**
				 * Add like and dislike buttons to the game over screen
				 * with callable functions here
				 */
				Runnable likeMap = new Runnable() {
					public void run() {
						addCurrentMapToKNearest(true);
					}
				};
				Runnable dislikeMap = new Runnable() {
					public void run() {
						addCurrentMapToKNearest(false);
					}
				};
				menuh.addLikeButton(likeMap, loader);
				menuh.addDislikeButton(dislikeMap, loader);
				
				/**
				 * Add like and dislike buttons to the mapmenu
				 * with callable functions here.
				 * Used for quicker testing.
				 */
				Runnable likeMapMenu = new Runnable() {
					public void run() {
						addCurrentMapToKNearest(true);
						menuh.remove2DMapFromMapMenu();
						state = "loadMapMenu";
					}
				};
				Runnable dislikeMapMenu = new Runnable() {
					public void run() {
						addCurrentMapToKNearest(false);
						menuh.remove2DMapFromMapMenu();
						state = "loadMapMenu";
					}
				};
				menuh.addLikeButtonMapMenu(likeMapMenu, loader);
				menuh.addDislikeButtonMapMenu(dislikeMapMenu, loader);
				
				
				
				state = "mainMenu";
				guiRenderer = new GuiRenderer(loader);
				guiRenderer.createHUD();
				break;

			case "mainMenu":
				Mouse.setGrabbed(false);
				menuh.setState(state);
				menuh.updateButtons(loader);
				menuh.renderMenu(menuShader, menuRenderer);
				if (Keyboard.isKeyDown(Keyboard.KEY_K)) {
					state = "knearest";
				}
				break;
				
			case "loadMapMenu":
				mapManager = new MapManager();
				mapManager.loadMap();
				menuh.add2DMapToMapMenu(mapManager.twoDMap);
				state = "mapMenu";
				break;
				
			case "mapMenu":
				menuh.setState(state);
				menuh.updateButtons(loader);
				menuh.renderMenu(menuShader, menuRenderer);
				break;

			case "loadmap":
<<<<<<< HEAD
//				mapManager = new MapManager();
//				mapManager.loadMap();
				menuh.remove2DMapFromMapMenu();
=======
				camera = new Camera(new Vector3f(0, 50, 10), 0, 0, 0);
				loadMap(loader);
>>>>>>> parent of c7ef24e... Defence map example
				state = "game";
				break;
				
			case "knearest":
				knr.renderKNearestGraph(menuShader, menuRenderer);
				knr.registerClicksButtons();
				if (Keyboard.isKeyDown(Keyboard.KEY_M)) {
					state = "mainMenu";
				}
				break;

			case "game":
				manageMusic();
				mapManager.update();
				checkPause();
				mapManager.render();
				guiRenderer.render();
				DisplayManager.updateDisplay();
				break;

			case "gameover":
				Mouse.setGrabbed(false);
				menuh.setState(state);
				menuh.updateButtons(loader);
				menuh.renderMenu(menuShader, menuRenderer);
				break;

			case "pause":
				Mouse.setGrabbed(false);
				manageMusic();
				menuh.setState(state);
				menuh.updateButtons(loader);
				menuh.renderMenu(menuShader, menuRenderer);
				mapManager.update();
				break;
				
			default:
				System.out.println("state error: " + state + " is an invalid state");
				System.exit(-1);
				break;

			}
		}

		DisplayManager.closeDisplay();
	}


	private static void checkPause(){
		if (Keyboard.isKeyDown(Keyboard.KEY_E) && !pauseCheck) {
			pauseCheck = true;
		} else if (!Keyboard.isKeyDown(Keyboard.KEY_E) && pauseCheck) {
			state = "pause";
			pauseCheck = false;
			Mouse.setGrabbed(false);
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_R)) {
			state = "startup";
			//apManager.cleanUp();
		}
<<<<<<< HEAD
		if (Keyboard.isKeyDown(Keyboard.KEY_G)) {
			state = "gameover";
=======

	}

	/**
	 * TEMPORARY, creates a simple map while the map generator is in progress, can be used for debugging
	 */
	private static void tempMapCreator(){
		for (int x = 0; x < map.length; x++) {
			for (int z = 0; z < map[0].length; z++) {
				for (int y = 0; y < map[0][0].length; y++) {
					if((int)(Math.random()*10) == 1)
						map[x][z][y] = 1;
				}
			}
		}
		map[1][1][1] = 2;
		map[1][1][48] = 1;
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
	private static void renderGame(MasterGameRenderer renderer, StaticShader shader, Camera camera) {

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
		for (Entity entity : mapEntities) {
			
			//vector from the entity to the camera
			toCamera = new Vector3f(camera.getPosition().x-entity.getPosition().x,  camera.getPosition().y-entity.getPosition().y,
					camera.getPosition().z - entity.getPosition().z + 0.01f);
			
			toCamera.normalise();
			
			double dist = Math.sqrt(Math.pow(camera.getPosition().x - entity.getPosition().x, 2)
					+ Math.pow(camera.getPosition().y - entity.getPosition().y, 2)
					+ Math.pow(camera.getPosition().z - entity.getPosition().z, 2));
			
			
			if ((Math.acos(Vector3f.dot(toCamera, lookAt)) < Math.toRadians(MasterGameRenderer.FOV+5) || dist<MIN_RENDER_DISTANCE)
					&& dist < RENDER_DISTANCE) {
				renderer.render(entity, shader);
			}

		}
		for (Entity entity : activeEntities) {
			
			//vector from the entity to the camera
			toCamera = new Vector3f(camera.getPosition().x-entity.getPosition().x,  camera.getPosition().y-entity.getPosition().y,
					camera.getPosition().z - entity.getPosition().z + 0.01f);
			
			toCamera.normalise();
			
			double dist = Math.sqrt(Math.pow(camera.getPosition().x - entity.getPosition().x, 2)
					+ Math.pow(camera.getPosition().y - entity.getPosition().y, 2)
					+ Math.pow(camera.getPosition().z - entity.getPosition().z, 2));
			
			
			if ((Math.acos(Vector3f.dot(toCamera, lookAt)) < Math.toRadians(MasterGameRenderer.FOV+5) || dist<MIN_RENDER_DISTANCE)
					&& dist < RENDER_DISTANCE) {
				renderer.render(entity, shader);
			}

>>>>>>> parent of c7ef24e... Defence map example
		}

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
	 * sets the state of the game
	 * 
	 * @param newState
	 *            the new state
	 */
	public static void setState(String newState) {
		state = newState;
	}
	
	/**
	 * Adds the current map to the knearest object with the given class
	 * 
	 * @param c  the class of this map, chosen by the player
	 */
	public static void addCurrentMapToKNearest(boolean c) {
		mapManager.addPointToKNearest(c);
	}


}
