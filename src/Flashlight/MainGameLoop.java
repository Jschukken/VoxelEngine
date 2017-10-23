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
	public static MenuHandler menuh;
	public static MapMenuRenderer mmr;
	public static KNearest kn;
	public static int[][][] map;
	List<GuiTexture> guis = new ArrayList<GuiTexture>();
	GuiRenderer guiRenderer = null;

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
				mapManager = new MapManager();
				
				/**
				 * Add clear saved maps button
				 */
				Runnable clearMaps = new Runnable() {
					public void run() {
						clearDataPoints();
					}
				};
				menuh.addClearMapsButton(clearMaps, loader);				
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
//				mapManager = new MapManager();
//				mapManager.loadMap();
				menuh.remove2DMapFromMapMenu();
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


	/**
	 * checks if the game should be paused
	 */
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
		if (Keyboard.isKeyDown(Keyboard.KEY_G)) {
			state = "gameover";
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
	
	/**
	 * clears the data points in map manager
	 */
	public static void clearDataPoints() {
		mapManager.clearDataPoints();
	}


}
