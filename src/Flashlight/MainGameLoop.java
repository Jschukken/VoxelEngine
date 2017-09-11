package Flashlight;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import Entitys.Camera;
import Entitys.Entity;
import Models.RawModel;
import Models.TexturedModel;
import RenderEngine.DisplayManager;
import RenderEngine.Loader;
import RenderEngine.MasterRenderer;
import Shaders.ShaderProgram;
import Shaders.StaticShader;
import Textures.ModelTexture;

/**
 * The main game manager
 * @author Jelle Schukken
 *
 */
public class MainGameLoop {
	
	public static Loader loader1 = null;
	public static StaticShader  sh = null;
	public static int[][][] map = new int[50][50][10];
	
	private static List<Entity> entities = new ArrayList<Entity>();

	public static void main(String[] args) {
		DisplayManager.createDisplay();
		Camera camera = null;;
		Loader loader = null;;
		StaticShader shader = null;;
		MasterRenderer renderer = null;;
		
	
		String state = "startup";
		
		
		while (!Display.isCloseRequested()){
			
			switch (state){
				case "startup":

					state = "menu";
					break;
				case "menu":
					state = "loadmap";
					break;
				case "loadmap":
					camera = new Camera(new Vector3f(0,20,10),0,0,0);
					loader = new Loader();
					loader1 = loader;
					shader = new StaticShader(); // temporary
					sh = shader;
					renderer = new MasterRenderer(shader);
					loadMap(loader);
					state = "game";
					break;
				case "game":
					updateGame(camera);
					renderGame(renderer,shader,camera);
					break;
				case "gameover":
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
	 * Creates cube entities in locations according to a map and adds them to the entity list, TODO: take map parameter and load to map
	 * @param loader used to load models to the gpu
	 */
	private static void loadMap(Loader loader){

		
		float[] vertices = {			
				-0.5f,0.5f,-0.5f,	
				-0.5f,-0.5f,-0.5f,	
				0.5f,-0.5f,-0.5f,	
				0.5f,0.5f,-0.5f,		
				
				-0.5f,0.5f,0.5f,	
				-0.5f,-0.5f,0.5f,	
				0.5f,-0.5f,0.5f,	
				0.5f,0.5f,0.5f,
				
				0.5f,0.5f,-0.5f,	
				0.5f,-0.5f,-0.5f,	
				0.5f,-0.5f,0.5f,	
				0.5f,0.5f,0.5f,
				
				-0.5f,0.5f,-0.5f,	
				-0.5f,-0.5f,-0.5f,	
				-0.5f,-0.5f,0.5f,	
				-0.5f,0.5f,0.5f,
				
				-0.5f,0.5f,0.5f,
				-0.5f,0.5f,-0.5f,
				0.5f,0.5f,-0.5f,
				0.5f,0.5f,0.5f,
				
				-0.5f,-0.5f,0.5f,
				-0.5f,-0.5f,-0.5f,
				0.5f,-0.5f,-0.5f,
				0.5f,-0.5f,0.5f
				
		};
		
		int[] indices = {
				0,1,3,	
				3,1,2,	
				4,5,7,
				7,5,6,
				8,9,11,
				11,9,10,
				12,13,15,
				15,13,14,	
				16,17,19,
				19,17,18,
				20,21,23,
				23,21,22
		};
		
		float[] uv = {
			0,0,
			0,1,
			1,1,
			1,0,
			0,0,
			0,1,
			1,1,
			1,0,
			0,0,
			0,1,
			1,1,
			1,0,
			0,0,
			0,1,
			1,1,
			1,0,
			0,0,
			0,1,
			1,1,
			1,0,
			0,0,
			0,1,
			1,1,
			1,0
				
		};
		
		RawModel model = loader.loadToVao(vertices,indices,uv);
		ModelTexture texture = new ModelTexture(loader.loadTexture("duck"));
		TexturedModel tMod = new TexturedModel(model, texture);
		
		for(int x = 0; x<map.length; x++){
			for(int z = 0; z< map[0].length; z++){
				for(int y = 0; y < map[0][0].length; y++){
					if(map[x][z][y] == 0){
						entities.add(new Entity(tMod,new Vector3f(x,y,z),0,0,0,new Vector3f(1,1,1)));
					}else if(map[x][y][z] == 1){
						//do other stuff
					}
				}
			}
		}
		//Entity entity = new Entity(tMod,new Vector3f(0,0,-10),0,0,0,new Vector3f(1,1,1));
	
	}
	
	/**
	 * updates all necessary entities, currently only camera
	 * @param camera object to update TODO: will like make a global camera variable
	 */
	private static void updateGame(Camera camera){
		camera.move();
		
	}
	
	/**
	 * renders all entities to the screen
	 * @param renderer the renderer that will be used
	 * @param shader shader used for rendering TODO: like change this as well
	 * @param camera the camera it will render to
	 */
	private static void renderGame(MasterRenderer renderer, StaticShader shader, Camera camera){
		
		
		renderer.prepare();
		
		
		
		shader.start();
		shader.loadViewMatrix(camera);
		
		for(Entity entity: entities){
			renderer.render(entity,shader);
		}
		
		shader.stop();
		
		DisplayManager.updateDisplay();
	}

}
