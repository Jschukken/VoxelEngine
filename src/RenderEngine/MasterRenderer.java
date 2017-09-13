package RenderEngine;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;

import Entities.Entity;
import Models.TexturedModel;
import Shaders.StaticShader;

/**
 * work in progress, the master renderer
 * @author Jelle Schukken
 *
 */
public class MasterRenderer {


	private Matrix4f projectionMatrix;

	public static final float FOV = 70;// 70 degrees
	private static final float NEAR_PLANE = -.25f;// closest rendered object
	private static final float FAR_PLANE = 100f;// farthest rendered object
	
	public MasterRenderer(StaticShader shader){
		createProjectionMatrix();
		shader.start();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.stop();
	}

	/**
	 * standard startup things, clear color, clear buffer, enable depth test
	 */
	public void prepare() {

		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glClearColor(0.4f, 0.7f, 1.0f, 1);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
	}

	/**
	 * creates projection matrix to convert models to camera view
	 */
	public void createProjectionMatrix() {

		projectionMatrix = new Matrix4f();

		float aspect = (float) Display.getWidth() / (float) Display.getHeight();
		float yScale = 1f / (float) Math.tan(Math.toRadians(FOV / 2f));
		float xScale = yScale / aspect;
		float zp = NEAR_PLANE + FAR_PLANE;
		float zm = NEAR_PLANE - FAR_PLANE;

		projectionMatrix.m00 = xScale;
		projectionMatrix.m11 = yScale;
		projectionMatrix.m22 = zp / zm;
		projectionMatrix.m23 = -1;
		projectionMatrix.m32 = (2 * FAR_PLANE * NEAR_PLANE) / zm;
	}

	/**
	 * render an entity with a shader
	 * @param entity the entity to render
	 * @param shader the shader that will be used
	 */
	public void render(Entity entity, StaticShader shader) {
		EntityRenderer.render(entity, shader);
	}
	
	public void render(TexturedModel tex, StaticShader shader) {
		TexturedModelRenderer.render(tex, shader);
	}
}
