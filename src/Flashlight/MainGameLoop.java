package Flashlight;

import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;

import Entities.Camera;
import Entities.EnemyEntity;
import Entities.Entity;
import Entities.SpawnPointEntity;
import GameEngine.AudioHandler;
import Models.RawModel;
import Models.TexturedModel;
import RenderEngine.DisplayManager;
import RenderEngine.Loader;
import RenderEngine.MasterRenderer;
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
	public static StaticShaderMenu menush = null;
	public static TexturedModel button1 = null;
	public static Loader loader1 = null;
	public static StaticShader sh = null;
	public static AudioHandler audH = null;
	public static int[][][] map = new int[50][15][50];

	private static String state = "startup";

	private static IntBuffer[] songID = null;

	public static List<Entity> mapEntities = new ArrayList<Entity>();
	public static List<Entity> activeEntities = new ArrayList<Entity>();

	private static boolean pauseCheck = false;

	public static void main(String[] args) {
		DisplayManager.createDisplay();
		Camera camera = null;
		Loader loader = null;
		StaticShader shader = null;
		StaticShaderMenu menuShader = null;
		MasterRenderer renderer = null;
		TexturedModelRenderer menuRenderer = null;
		AudioHandler ah = null;
		tempMapCreator();

		while (!Display.isCloseRequested()) {

			switch (state) {

			case "startup":
				GL11.glClearColor(0.4f, 0.7f, 1.0f, 1);
				camera = null;
				mapEntities.clear();
				activeEntities.clear();

				try {
					loader1.cleanUp();
					sh.cleanUp();
					audH.cleanUp();
					menush.cleanUp();
				} catch (NullPointerException e) {

				}
				ah = new AudioHandler();
				audH = ah;
				songID = ah.createSound("song");
				ah.startSong(songID);
				loader = new Loader();
				loader1 = loader;
				shader = new StaticShader();
				sh = shader;
				menuShader = new StaticShaderMenu();
				menush = menuShader;
				renderer = new MasterRenderer(shader);
				createButtons(loader);
				state = "menu";
				break;

			case "menu":
				Mouse.setGrabbed(false);
				renderMenu(menuShader);
				
				if (Mouse.isButtonDown(0) && mouseInButton()) {
					state = "loadmap";
					Mouse.setGrabbed(true);
				}
				break;

			case "loadmap":
				camera = new Camera(new Vector3f(0, 50, 10), 0, 0, 0);
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
						mapEntities.add(new Entity(tMod, new Vector3f(x, y, z), 0, 0, 0, new Vector3f(1, 1, 1)));
					} else if(map[x][y][z] == 2){
						
							int[] arr = {1,2,map[0][0].length-2,map.length-2,map[0].length,map[0][0].length-2,map.length-2,map[0].length,1,1,map[0].length,1};
							activeEntities.add(new SpawnPointEntity(tMod, new Vector3f(x, y, z), 0, 0, 0, new Vector3f(1, 1, 1), tMod, arr));
						
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
		for(int i = 0; i < activeEntities.size();i++){
			activeEntities.get(i).update();
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
		
		for (Entity entity : mapEntities) {
			
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
		for (Entity entity : activeEntities) {
			
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
	
	/**
	 * adds an active entity to the game 
	 * @param entity the entity to add
	 * @param position the position of the entity
	 * @param path the path the entity follows :TODO, make this better. it is silly
	 */
	public static void addActiveEntity(TexturedModel entity, Vector3f position, int[] path){
		Entity enemy = new EnemyEntity(entity, position, 0, 0, 0, new Vector3f(1, 1, 1),path);
		activeEntities.add(enemy);
		
	}
	
	/**
	 * removes the given entity from the game
	 * @param entity the entity to remove
	 */
	public static void removeActiveEntity(Entity entity){

		activeEntities.remove(entity);
	}
	
	/**
	 * Creates buttons for the main menu. Currently only the start button.
	 * TODO: Create new button object for genericity
	 * @param loader loader required to load the models.
	 */
	public static void createButtons(Loader loader) {
		float[] vertices = { -0.5f, 0.5f, -1f, -0.5f, -0.5f, -1f, 0.5f, -0.5f, 1f, 0.5f, 0.5f, -1f};

		int[] indices = { 0, 1, 3, 3, 2, 1};

		float[] uv = { 0, 0, 0, 1, 1, 1, 1, 0};

		RawModel model = loader.loadToVao(vertices, indices, uv);
		
		ModelTexture texture = new ModelTexture(loader.loadTexture("Tile"));
		TexturedModel tMod = new TexturedModel(model, texture);
		button1 = tMod;
	}
	
	/**
	 * Renders the menu
	 * @param shader the shader required to give textures to the model
	 */
	public static void renderMenu(StaticShaderMenu shader) {
		shader.start();
		TexturedModelRenderer.render(button1, shader);
		shader.stop();
		DisplayManager.updateDisplay();
	}
	
	/**
	 * Checks whether the mouse is inside the boundaries of the button
	 * TODO: Change to work with new Button object
	 * @return whether the mouse is inside the button
	 */
	public static boolean mouseInButton() {
		
		if (Mouse.getX() > 540 && Mouse.getX() < 1380 && Mouse.getY() > 304  && Mouse.getY() < 776) {
			return true;
		}
		return false;
	}

}
