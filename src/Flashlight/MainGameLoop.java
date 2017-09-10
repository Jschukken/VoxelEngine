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

public class MainGameLoop {
	
	public static Loader loader1 = null;
	public static StaticShader  sh = null;
	
	private static List<Entity> entities = new ArrayList<Entity>();

	public static void main(String[] args) {
		DisplayManager.createDisplay();
		
		
		Loader loader = new Loader();
		loader1 = loader;
		
		StaticShader shader = new StaticShader(); // temporary
		sh = shader;
		MasterRenderer renderer = new MasterRenderer(shader);
		
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
		
		for(int x = -10; x<11; x++){
			for(int y = -10; y<11; y++){
				entities.add(new Entity(tMod,new Vector3f(x,0,y),0,0,0,new Vector3f(1,1,1)));
			}
		}
		//Entity entity = new Entity(tMod,new Vector3f(0,0,-10),0,0,0,new Vector3f(1,1,1));
		
		Camera camera = new Camera(new Vector3f(0,0,-10),0,0,0);
		
		
		while (!Display.isCloseRequested()){
			
			update(camera);
			render(renderer,shader,camera);
		}
		
		DisplayManager.closeDisplay();
	}
	
	private static void update(Camera camera){
		camera.move();
		
	}
	
	private static void render(MasterRenderer renderer, StaticShader shader, Camera camera){
		
		
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
