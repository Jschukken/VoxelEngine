package Menu;

import java.nio.IntBuffer;

import java.util.ArrayList;
import java.util.List;


import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

import Entities.Button;
import Flashlight.MainGameLoop;
import GameEngine.AudioHandler;
import GameEngine.MapManager;
import Map.Map;
import Models.RawModel;
import RenderEngine.DisplayManager;
import RenderEngine.Loader;
import RenderEngine.MasterMenuRenderer;
import RenderEngine.TexturedModelRenderer;
import Shaders.StaticShader;
import Shaders.StaticShaderMenu;
import Textures.ModelTexture;
import ToolBox.TexturedModelMaker;


public class MenuHandler {
	
	public List<Button> mainMenu = new ArrayList<Button>();
	public List<Button> mapMenu = new ArrayList<Button>();
	public List<Button> pauseMenu = new ArrayList<Button>();
	public List<Button> gameOverMenu = new ArrayList<Button>();
	public List<Button> activeMenu = new ArrayList<Button>();
	
	
	public MenuHandler() {		
	}
	
	public void createMainMenu(Loader loader) {

		System.out.println("Creating menu");
		float[] vertices = { -0.5f, 0.5f, -1f, -0.5f, 0.1f, -1f, 0.5f, 0.1f, 1f, 0.5f, 0.5f, -1f };

		int[] indices = { 0, 1, 3, 3, 2, 1 };

		float[] uv = { 0, 0, 0, 1, 1, 1, 1, 0 };

		RawModel model = loader.loadToVao(vertices, indices, uv);

		ModelTexture texture = new ModelTexture(loader.loadTexture("Tile"));
		Button tMod = new Button(model, texture, vertices) {
			@Override
			public void onClick() {
				MainGameLoop.setState("loadmap");
			}
		};
		
		mainMenu.add(tMod);
		
		vertices = new float[]{ -0.5f, -0.1f, -1f, -0.5f, -0.5f, -1f, 0.5f, -0.5f, 1f, 0.5f, -0.1f, -1f };
		
		model = loader.loadToVao(vertices, indices, uv);
		
		texture = new ModelTexture(loader.loadTexture("Tile"));
		Button tmod = new Button(model, texture, vertices) {
			@Override
			public void onClick() {
				DisplayManager.closeDisplay();
			}
		};
		
		mainMenu.add(tmod);

	}	
	
	/**
	 * updates all buttons relevant for the current state
	 */
	public void updateButtons(Loader loader) {
		
		int width = Display.getDisplayMode().getWidth();
		int height = Display.getDisplayMode().getHeight();
		float mouseX = (float) (-1.0 + 2.0 * Mouse.getX() / width);
		float mouseY = (float) (-1.0 + 2.0 * Mouse.getY() / height);
		
		for (Button button : activeMenu) {

			if (mouseX > button.getLeftX() && mouseX < button.getRightX() && mouseY > button.getBotY() && mouseY < button.getTopY()) {
				button.setTexture(new ModelTexture(loader.loadTexture("Duck")));
				if (Mouse.isButtonDown(0)) {
					button.onClick();
				}
			} else {
				button.setTexture(new ModelTexture(loader.loadTexture("Tile")));
			}
			
		}
	}
	
	public void renderMenu(StaticShaderMenu shader, MasterMenuRenderer renderer) {
		renderer.prepare();
		shader.start();
		for (Button button : activeMenu) {
			renderer.render(button, shader);
		}
		shader.stop();
		DisplayManager.updateDisplay();
	}
	
	/**
	 * sets the active menu based on the current state of MainGameLoop
	 * @param state
	 * 			the current state of MainGameLoop
	 */
	public void setState(String state) {
		switch (state) {
		
		case "mainMenu":
			activeMenu = mainMenu;
			break;
			
		case "mapMenu":
			activeMenu = mapMenu;
			break;
			
		case "pause":
			activeMenu = pauseMenu;
			break;
			
		case "gameover":
			activeMenu = gameOverMenu;
			break;
		}
	}

}
