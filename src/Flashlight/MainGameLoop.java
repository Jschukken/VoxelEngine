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
import RenderEngine.DisplayManager;
import RenderEngine.Loader;
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

	public static StaticShaderMenu menush = null;
	public static Button button1 = null;
	public static Loader loader = null;
	public static StaticShader sh = null;
	public static AudioHandler audH = null;
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
		map = Map.createGoodMap();

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
				createButtons(loader);
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
				state = "startup";
				break;

			case "pause":
				manageMusic();
				pause();
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
	 * Creates buttons for the main menu. Currently only the start button. TODO:
	 * Create new button object for genericity
	 * 
	 * @param loader
	 *            loader required to load the models.
	 */
	public static void createButtons(Loader loader) {
		button1 = TexturedModelMaker.createButton(loader);
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
