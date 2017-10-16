package Flashlight;

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
import Guis.GuiRenderer;
import Guis.GuiTexture;
import KNearest.KNearest;
import KNearest.KNearestRendering;
import Menu.MapMenuRenderer;
import Menu.MenuHandler;
import RenderEngine.DisplayManager;
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
		kn = new KNearest(5);
		KNearestRendering knr = new KNearestRendering(kn);
		MapMenuRenderer mapMenuRenderer = new MapMenuRenderer();
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
				state = "mainMenu";
				GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
				
				// Difficulty level HUD
				ModelTexture ducker = new ModelTexture(loader.loadTexture("Potato"));
				GuiTexture gui = new GuiTexture(ducker , new Vector2f(-0.6f, 0.9f), new Vector2f(0.4f, 0.1f));
				// Timer HUD
				ModelTexture ducker2 = new ModelTexture(loader.loadTexture("Potato"));
				GuiTexture gui2 = new GuiTexture(ducker2 , new Vector2f(0f, 0.9f), new Vector2f(0.15f, 0.1f));
				// End Point HP HUD
				ModelTexture ducker3 = new ModelTexture(loader.loadTexture("Potato"));
				GuiTexture gui3 = new GuiTexture(ducker3 , new Vector2f(0.6f, 0.9f), new Vector2f(0.4f, 0.1f));
				// Player HP HUD
				ModelTexture ducker4 = new ModelTexture(loader.loadTexture("red"));
				GuiTexture gui4 = new GuiTexture(ducker4 , new Vector2f(-0.6f, -0.9f), new Vector2f(0.4f, 0.1f));
				guis.add(gui);
				guis.add(gui2);
				guis.add(gui3);
				guis.add(gui4);
				guiRenderer = new GuiRenderer(loader);
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
				guiRenderer.render(guis);
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
			//apManager.cleanUp();
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
