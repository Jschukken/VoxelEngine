package Flashlight;

import org.lwjgl.opengl.Display;

import Models.RawModel;
import RenderEngine.DisplayManager;
import RenderEngine.Loader;
import RenderEngine.MasterRenderer;

public class MainGameLoop {
	
	public static Loader loader1 = null;

	public static void main(String[] args) {
		DisplayManager.createDisplay();
		
		MasterRenderer renderer = new MasterRenderer();
		
		Loader loader = new Loader();
		loader1 = loader;
		
		float[] vertices = {
				-0.5f, 0.5f, 0,
				-0.5f, -0.5f, 0, 
				0.5f, -0.5f,0,
				0.5f, 0.5f, 0,
		};
		
		int[] indices = {
				
				0,1,2,
				2,3,0
		};
		
		RawModel model = loader.loadToVao(vertices,indices);
		
		while (!Display.isCloseRequested()){
			renderer.prepare();
			
			renderer.render(model);
			
			DisplayManager.updateDisplay();
		}
		
		DisplayManager.closeDisplay();
	}

}
