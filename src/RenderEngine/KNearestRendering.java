package RenderEngine;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

import KNearest.KNearest;
import KNearest.Point;
import Models.RawModel;
import Models.TexturedModel;
import Shaders.StaticShaderMenu;
import Textures.ModelTexture;
import ToolBox.MatrixMath;

public class KNearestRendering {
	
	// The knearest object used for computation
	KNearest kn;
	
	// A loader for... uhm... loading?
	Loader loader;
	
	// Flags for registering mouse clicks/button presses correctly
	boolean wasDown;
	boolean alreadySaved;
	
	// Value representing the editing state
	int editState;
	
	float pointSize = 0.01f;
	
	/**
	 * Constructor
	 */
	public KNearestRendering(KNearest knear) {
		this.kn = knear;
		this.loader = new Loader();
		this.wasDown = false;
		this.alreadySaved = false;
		this.editState = 1;
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
		/*TexturedModel xAxis = createRectangle(-0.95f, -0.95f, 0.02f, 1.9f, "black");
		TexturedModel yAxis = createRectangle(-0.95f, -0.95f, 1.9f, 0.02f, "black");
		renderer.render(xAxis, shader);
		renderer.render(yAxis, shader);*/
		
		/**
		 * Create background
		 */
		TexturedModel background = createRectangle(-1f, -1f, 2f, 2f, 0f, "Background");
		renderer.render(background, shader);
		
		/**
		 * Get all points from the KNearest object and render them in the graph.
		 */
		List<Point> points = kn.getAllPoints();
		float h = Display.getDisplayMode().getHeight();
		float w = Display.getDisplayMode().getWidth();
		
		for (Point p : points) {
			List<Double> coords = p.getCoordinates();
			float normX = (float) ((coords.get(0) / w) * 2 - 1);
			float normY = (float) ((coords.get(1) / h) * 2 - 1);
			String color;
			if (p.getClassification()) {
				color = "red";
			} else {
				color = "blue";
			}
			TexturedModel pnt = createRectangle(normX - 0.5f * pointSize, normY - 0.5f * pointSize, pointSize, (h / w) * pointSize, -1f, color);
			renderer.render(pnt, shader);
		}
		
		// Stop shader and update display
		shader.stop();
		DisplayManager.updateDisplay();
				
	}
	
	/**
	 * Handles mouse clicks and button presses to change the editing state
	 */
	public void registerClicksButtons() {
		
		// Mouse clicks
		if (Mouse.isButtonDown(0)) {
			if (!wasDown) {
				addPointFromMouseClick(Mouse.getX(), Mouse.getY());
				wasDown = true;
			}
		} else {
			wasDown = false;
		}
		
		/* Button presses for edit state */
		if (Keyboard.isKeyDown(Keyboard.KEY_1)) { // class true
			this.editState = 1;
		} else if (Keyboard.isKeyDown(Keyboard.KEY_2)) { // class false
			this.editState = 2;
		} else if (Keyboard.isKeyDown(Keyboard.KEY_3)) { // class unknown
			this.editState = 3;
		} 
		
		/* Removal of points */
		if (Keyboard.isKeyDown(Keyboard.KEY_R)) {
			kn.clearPoints();
		} 
		
		/* Saving the points */
		if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
			if (!alreadySaved) {
				try {
					kn.writeDataToFile();
				} catch (IOException e) {
					System.out.println("Could not store training data!");
				}
				alreadySaved = true;
			}
		} else {
			alreadySaved = false;
		}
		
	}
	
	/**
	 * Adds a point to the knearest, using the coordinates of the mouse click
	 * 
	 * @param mouseX  the x position of the mouse
	 * @param mouseY  the y position of the mouse
	 */
	public void addPointFromMouseClick(int mouseX, int mouseY) {
		
		List<Double> coords = new ArrayList<Double>();
		coords.add((double) mouseX);
		coords.add((double) mouseY);
		if (editState == 1) {
			kn.addDataPoint(new Point(coords, true));
			System.out.println("added red point");
		} else if (editState == 2) {
			kn.addDataPoint(new Point(coords, false));
			System.out.println("added blue point");
		} else if (editState == 3) {
			kn.classifyAndAdd(new Point(coords));
		}
		
		
	}
	
	/**
	 * Creates a rectangle and returns a texturedmodel object of it.
	 * 
	 * @param x  the x coordinate of the lower left corner
	 * @param y  the y coordinate of the lower left corner
	 * @param h  the height of the rectangle
	 * @param w  the width of the rectangle
	 * @param texture  a string representing the color
	 * @return  a texturedmodel of a rectangle at (x,y) with height h, width w and color
	 */
	public TexturedModel createRectangle(float x, float y, float h, float w, float z, String texture) {
		
		/**
		 * Set vertices, indices and uv coordinates
		 */
		float[] vertices = { x, y+h, z, x, y, z, x+w, y, z, x+w, y+h, z};
		int[] indices = { 0, 1, 3, 3, 2, 1};
		float[] uv = { 0, 0, 0, 1, 1, 1, 1, 0 };

		RawModel model = loader.loadToVao(vertices, indices, uv, MatrixMath.CreateNormals(vertices, indices));
		
		ModelTexture textureMod = new ModelTexture(this.loader.loadTexture(texture));
		return new TexturedModel(model, textureMod);
		
	}
	
}
