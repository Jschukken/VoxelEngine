package Menu;

import java.util.ArrayList;
import java.util.List;


import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

import Entities.Button;
import Flashlight.MainGameLoop;
import Models.RawModel;
import Models.TexturedModel;
import RenderEngine.DisplayManager;
import RenderEngine.Loader;
import RenderEngine.MasterMenuRenderer;
import Shaders.StaticShaderMenu;
import Textures.ModelTexture;

/* CHEAT SHEET
 * Z =  0 -> Overlay
 * Z = -1 -> Buttons
 * Z=  -2 -> Background
 */

public class MenuHandler {
	
	public List<Button> mainMenu = new ArrayList<Button>();
	public List<TexturedModel> mainMenuFluff = new ArrayList<TexturedModel>();
	
	public List<Button> mapMenu = new ArrayList<Button>();
	public List<TexturedModel> mapMenuFluff = new ArrayList<TexturedModel>();
	
	public List<Button> pauseMenu = new ArrayList<Button>();
	public List<TexturedModel> pauseMenuFluff = new ArrayList<TexturedModel>();
	
	public List<Button> gameOverMenu = new ArrayList<Button>();
	public List<TexturedModel> gameOverFluff = new ArrayList<TexturedModel>();
	
	public List<Button> activeMenu = new ArrayList<Button>();
	public List<TexturedModel> activeFluff = new ArrayList<TexturedModel>();
	
	private boolean mouseHeld = false;
	
	public MenuHandler() {
	}
	
	
	public void createMenus(Loader loader) {
		createMainMenu(loader);
		createMapMenu(loader);
		createPauseMenu(loader);
		createGameOverMenu(loader);
	}
	
	/**
	 * creates all buttons 
	 * @param loader
	 *
	 */
	public void createMainMenu(Loader loader) {

		float[] vertices = { -0.3f, 0.3f, -1f, -0.3f, 0.0f, -1f, 0.3f, 0.0f, 1f, 0.3f, 0.3f, -1f };

		int[] indices = { 0, 1, 3, 3, 2, 1 };

		float[] uv = { 0, 0, 0, 1, 1, 1, 1, 0 };

		RawModel model = loader.loadToVao(vertices, indices, uv);

		ModelTexture tex1 = new ModelTexture(loader.loadTexture("Tile"));
		ModelTexture tex2 = new ModelTexture(loader.loadTexture("Duck"));
		
		Button tMod = new Button(model, tex1, tex2, vertices) {
			@Override
			public void onClick() {
				MainGameLoop.setState("mapMenu");
			}
		};
		
		mainMenu.add(tMod);
		
		vertices = new float[]{ -0.3f, -0.1f, -1f, -0.3f, -0.4f, -1f, 0.3f, -0.4f, 1f, 0.3f, -0.1f, -1f };
		
		model = loader.loadToVao(vertices, indices, uv);
		
		tex1 = new ModelTexture(loader.loadTexture("Tile"));
		tex2 = new ModelTexture(loader.loadTexture("Duck"));
		
		tMod = new Button(model, tex1, tex2, vertices) {
			@Override
			public void onClick() {
//				MainGameLoop.setState("settings");
			}
		};
		
		mainMenu.add(tMod);
		
		vertices = new float[] {-0.3f, -0.5f, -1f, -0.3f, -0.8f, -1f, 0.3f, -0.8f, -1f, 0.3f, -0.5f, -1f};
		
		model = loader.loadToVao(vertices, indices, uv);

		tex1 = new ModelTexture(loader.loadTexture("Tile"));
		tex2 = new ModelTexture(loader.loadTexture("Duck"));
		tMod = new Button(model, tex1, tex2, vertices) {
			@Override
			public void onClick() {
				DisplayManager.closeDisplay();
			}
		};
		
		mainMenu.add(tMod);
	}	

	public void createMapMenu(Loader loader) {

		float[] vertices = { -0.7f, -0.7f, -1f, -0.7f, -0.95f, -1f, -0.2f, -0.95f, 1f, -0.2f, -0.7f, -1f };

		int[] indices = { 0, 1, 3, 3, 2, 1 };

		float[] uv = { 0, 0, 0, 1, 1, 1, 1, 0 };

		RawModel model = loader.loadToVao(vertices, indices, uv);

		ModelTexture tex1 = new ModelTexture(loader.loadTexture("Tile"));
		ModelTexture tex2 = new ModelTexture(loader.loadTexture("Duck"));
		Button tMod = new Button(model, tex1, tex2, vertices) {
			@Override
			public void onClick() {
				MainGameLoop.setState("loadmap");
				Mouse.setGrabbed(true);
			}
		};
		
		mapMenu.add(tMod);
		
		vertices = new float[]{ 0.2f, -0.7f, -1f, 0.2f, -0.95f, -1f, 0.7f, -0.95f, 1f, 0.7f, -0.7f, -1f };
		
		model = loader.loadToVao(vertices, indices, uv);

		tex1 = new ModelTexture(loader.loadTexture("Tile"));
		tex2 = new ModelTexture(loader.loadTexture("Duck"));
		tMod = new Button(model, tex1, tex2, vertices) {
			@Override
			public void onClick() {
				MainGameLoop.setState("mainMenu");
			}
		};
		
		mapMenu.add(tMod);
	}	
	
	public void createPauseMenu(Loader loader) {

		float[] vertices = { -0.3f, 0.3f, -1f, -0.3f, 0.0f, -1f, 0.3f, 0.0f, 1f, 0.3f, 0.3f, -1f };

		int[] indices = { 0, 1, 3, 3, 2, 1 };

		float[] uv = { 0, 0, 0, 1, 1, 1, 1, 0 };

		RawModel model = loader.loadToVao(vertices, indices, uv);

		ModelTexture tex1 = new ModelTexture(loader.loadTexture("Tile"));
		ModelTexture tex2 = new ModelTexture(loader.loadTexture("Duck"));
		Button tMod = new Button(model, tex1, tex2, vertices) {
			@Override
			public void onClick() {
				MainGameLoop.setState("game");
				Mouse.setGrabbed(true);
			}
		};
		
		pauseMenu.add(tMod);
		
		vertices = new float[]{ -0.3f, -0.1f, -1f, -0.3f, -0.4f, -1f, 0.3f, -0.4f, 1f, 0.3f, -0.1f, -1f };
		
		model = loader.loadToVao(vertices, indices, uv);

		tex1 = new ModelTexture(loader.loadTexture("Tile"));
		tex2 = new ModelTexture(loader.loadTexture("Duck"));
		tMod = new Button(model, tex1, tex2, vertices) {
			@Override
			public void onClick() {
				MainGameLoop.setState("gameover");
			}
		};
		
		pauseMenu.add(tMod);
	}	
	
	public void createGameOverMenu(Loader loader) {

		System.out.println("Creating menu");
		float[] vertices = { -0.3f, 0.3f, -1f, -0.3f, 0.0f, -1f, 0.3f, 0.0f, 1f, 0.3f, 0.3f, -1f };

		int[] indices = { 0, 1, 3, 3, 2, 1 };

		float[] uv = { 0, 0, 0, 1, 1, 1, 1, 0 };

		RawModel model = loader.loadToVao(vertices, indices, uv);

		ModelTexture tex1 = new ModelTexture(loader.loadTexture("Tile"));
		ModelTexture tex2 = new ModelTexture(loader.loadTexture("Duck"));
		Button tMod = new Button(model, tex1, tex2, vertices) {
			@Override
			public void onClick() {
				MainGameLoop.setState("mainMenu");
			}
		};
		
		gameOverMenu.add(tMod);
		
		vertices = new float[]{ -0.3f, -0.1f, -1f, -0.3f, -0.4f, -1f, 0.3f, -0.4f, 1f, 0.3f, -0.1f, -1f };
		
		model = loader.loadToVao(vertices, indices, uv);

		tex1 = new ModelTexture(loader.loadTexture("Tile"));
		tex2 = new ModelTexture(loader.loadTexture("Duck"));
		tMod = new Button(model, tex1, tex2, vertices) {
			@Override
			public void onClick() {
				DisplayManager.closeDisplay();
			}
		};
		
		gameOverMenu.add(tMod);
		
		vertices = new float[] { -0.95f, -0.85f, -1f, -0.95f, -0.95f, -1f, -0.85f, -0.95f, -1f, -0.85f, -0.85f, -1f};

		model = loader.loadToVao(vertices, indices, uv);

		tex1 = new ModelTexture(loader.loadTexture("Tile"));
		tex2 = new ModelTexture(loader.loadTexture("Duck"));
		tMod = new Button(model, tex1, tex2, vertices) {
			@Override
			public void onClick() {
//				DisplayManager.closeDisplay();
			}
		};
		
		gameOverMenu.add(tMod);
		vertices = new float[] { -0.8f, -0.85f, -1f, -0.8f, -0.95f, -1f, -0.7f, -0.95f, -1f, -0.7f, -0.85f, -1f};

		model = loader.loadToVao(vertices, indices, uv);

		tex1 = new ModelTexture(loader.loadTexture("Tile"));
		tex2 = new ModelTexture(loader.loadTexture("Duck"));
		tMod = new Button(model, tex1, tex2, vertices) {
			@Override
			public void onClick() {
//				DisplayManager.closeDisplay();
			}
		};
		
		gameOverMenu.add(tMod);
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
				button.activeTexture();
				if (Mouse.isButtonDown(0) && !mouseHeld) {
					button.onClick();
					mouseHeld = true;
				} else if (!Mouse.isButtonDown(0)){
					mouseHeld = false;
				}
			} else {
				button.inactiveTexture();
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
