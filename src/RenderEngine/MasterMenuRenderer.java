package RenderEngine;

import org.lwjgl.opengl.GL11;

import Models.TexturedModel;
import Shaders.StaticShaderMenu;

/**
 * work in progress, the master renderer
 * @author Jelle Schukken
 *
 */
public class MasterMenuRenderer {



	public static final float FOV = 70;// 70 degrees
	
	public MasterMenuRenderer(){

	}

	/**
	 * standard startup things, clear color, clear buffer, enable depth test
	 */
	public void prepare() {

		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glClearColor(0f, 0f, 0f, 0f);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
	}

	/**
	 * render an entity with a shader
	 * @param entity the entity to render
	 * @param shader the shader that will be used
	 */
	public void render(TexturedModel entity, StaticShaderMenu shader) {
		TexturedModelRenderer.render(entity, shader);
	}
	
	public void cleanUp(){
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glClearColor(0.4f, 0.7f, 1.0f, 1);
	}
}
