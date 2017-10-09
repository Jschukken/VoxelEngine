package KNearest;

import java.util.List;

import org.lwjgl.opengl.Display;

import Models.RawModel;
import Models.TexturedModel;
import RenderEngine.DisplayManager;
import RenderEngine.Loader;
import RenderEngine.MasterMenuRenderer;
import RenderEngine.TexturedModelRenderer;
import Shaders.StaticShaderMenu;
import Textures.ModelTexture;

public class KNearestRendering {
	
	// The knearest object used for computation
	KNearest kn;
	
	// A loader for... uhm... loading?
	Loader loader;
	
	/**
	 * Constructor
	 */
	public KNearestRendering() {
		this.kn = new KNearest(5);
		this.loader = new Loader();
	}
	
	/**
	 * Renders the graph with all points that are in the KNearest object
	 * 
	 * @param shader the shader required to give textures to the model
	 */
	public void renderKNearestGraph(StaticShaderMenu shader, MasterMenuRenderer renderer) {
		
		// Prepare renderer and start shader
		renderer.prepare();
		shader.start();
		
		/**
		 * Render the axes of the graph
		 */
		float height = Display.getDisplayMode().getHeight();
		float width = Display.getDisplayMode().getWidth();
		float normalizedX = (width / 10) + 1f / (2);
		TexturedModel xAxis = createRectangle(-0.8f, -0.8f, 0.1f, 1.6f, "red");
		TexturedModel yAxis = createRectangle(-0.8f, -0.8f, 1.6f, 0.1f, "blue");
		renderer.render(xAxis, shader);
		renderer.render(yAxis, shader);
		
		
		/**
		 * Get all points from the KNearest object and render them in the graph.
		 */
		List<Point> points = kn.getAllPoints();
		
		// Stop shader and update display
		shader.stop();
		DisplayManager.updateDisplay();
		
	}
	
	public static void updateGraph() {
		
	}
	
	/**
	 * Creates a rectangle and returns a texturedmodel object of it.
	 * 
	 * @param x  the x coordinate of the lower left corner
	 * @param y  the y coordinate of the lower left corner
	 * @param h  the height of the rectangle
	 * @param w  the width of the rectangle
	 * @param color  a string representing the color
	 * @return  a texturedmodel of a rectangle at (x,y) with height h, width w and color
	 */
	public TexturedModel createRectangle(float x, float y, float h, float w, String color) {
		
		/**
		 * Set vertices, indices and uv coordinates
		 */
		float[] vertices = { x, y, -1f, x+w, y, -1f, x+w, y+h, -1f, x, y+h, -1f};
		int[] indices = { 0, 1, 3, 3, 2, 1};
		float[] uv = { 0, 0, 0, 0, 1, 1, 1, 1};

		RawModel model = loader.loadToVao(vertices, indices, uv);
		
		ModelTexture texture = new ModelTexture(this.loader.loadTexture(color));
		return new TexturedModel(model, texture);
		
	}
	
}
