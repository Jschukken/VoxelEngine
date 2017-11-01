package RenderEngine;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.PixelFormat;

import Flashlight.MainGameLoop;

/**
 * manages the display
 * 
 * @author Jelle Schukken
 *
 */
public class DisplayManager {

	public static final int WIDTH = 1920;
	public static final int HEIGHT = 1080;
	public static final int FPS_CAP = 60;
	private static boolean windowed = false;

	/**
	 * creates a display
	 */
	public static void createDisplay() {

		ContextAttribs attribs = new ContextAttribs(3, 2).withForwardCompatible(true).withProfileCore(true);

		try {
			if(windowed){
				Display.setDisplayMode(new DisplayMode(WIDTH,HEIGHT));
			}else{
				Display.setFullscreen(true);
			}
			Display.setVSyncEnabled(true);
			Display.create(new PixelFormat(), attribs);
			Display.setTitle("Flashlight");

			GL11.glViewport(0, 0, Display.getWidth(), Display.getHeight());
		} catch (LWJGLException e) {
			e.printStackTrace();
		}

		Mouse.setGrabbed(true);
	}

	/**
	 * updates the display
	 */
	public static void updateDisplay() {
		Display.sync(FPS_CAP);
		Display.update();

		while (Keyboard.next()) {
			if (Keyboard.getEventKeyState()) {

				if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE) && Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
					closeDisplay();
				}

				if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE) && Mouse.isGrabbed()) {
					Mouse.setGrabbed(false);
				} else if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE) && !Mouse.isGrabbed()) {
					Mouse.setGrabbed(true);
				}
			}
		}
	}

	/**
	 * closes the display and cleans up loose memory
	 */
	public static void closeDisplay() {
		try {
			MainGameLoop.mapManager.cleanUp();
			MainGameLoop.audH.cleanUp();
		} catch (NullPointerException e) {

		}
		Display.destroy();
		System.out.println("closed");
		System.exit(0);
	}
}
