package Flashlight;

import java.nio.IntBuffer;


import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

import Entities.Button;
import GameEngine.AudioHandler;
import GameEngine.MapManager;
import Map.Map;
import Menu.MenuHandler;
import RenderEngine.DisplayManager;
import RenderEngine.Loader;
import RenderEngine.MasterMenuRenderer;
import Shaders.StaticShader;
import Shaders.StaticShaderMenu;

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
	public static int[][][] map;

	private static String state = "startup";

	private static IntBuffer[] songID = null;
	
	public static MapManager mapManager;

	private static boolean pauseCheck = false;

	public static void main(String[] args) {
		DisplayManager.createDisplay();
		StaticShaderMenu menuShader = null;
		MasterMenuRenderer menuRenderer = null;
		AudioHandler ah = null;
		mapManager = null;
		//map = Map.createGoodMap();

		while (!Display.isCloseRequested()) {

			switch (state) {

			case "startup":

				try {
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
				state = "mainMenu";
				GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
				break;

			case "mainMenu":
				Mouse.setGrabbed(false);
				menuh.setState(state);
				menuh.updateButtons(loader);
				menuh.renderMenu(menuShader, menuRenderer);
				break;
				
			case "mapMenu":
				menuh.setState(state);
				menuh.updateButtons(loader);
				menuh.renderMenu(menuShader, menuRenderer);
				break;

			case "loadmap":
				mapManager = new MapManager();
				mapManager.loadMap();
				state = "game";
				break;

			case "game":
				manageMusic();
				mapManager.update();
				checkPause();
				mapManager.render();
				DisplayManager.updateDisplay();
				break;

			case "gameover":
				menuh.setState(state);
				menuh.updateButtons(loader);
				menuh.renderMenu(menuShader, menuRenderer);
				break;

			case "pause":
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


}
