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

/**
 * Renders the KNearest visualization
 * 
 * @author Jorik Mols
 *
 */
public class KNearestRendering {
	Loader loader;
	
	// The knearest object used for computation
	KNearest kn;
	
	// Flags for registering mouse clicks/button presses correctly
	boolean wasDown;
	boolean alreadySaved;
	
	// List containing all models of the data points currently in the knearest object
	List<TexturedModel> dataPoints;
	
	// List of the decision boundary points, with a flag to check if this mode was already enabled
	List<TexturedModel> DBPoints;
	boolean DBEnabled;
	static int DBStepSize = 4;
	static boolean DBBORDERENABLED = false;
	
	// Value representing the editing state
	int editState;
	
	static float pointSize = 0.01f;
	static float dbpointSize = 0.004f;
	
	/**
	 * Constructor
	 */
	public KNearestRendering(KNearest knear) {
		this.kn = knear;
		this.loader = new Loader();
		this.wasDown = false;
		this.alreadySaved = false;
		this.editState = 1;
		this.dataPoints = new ArrayList<TexturedModel>();
		this.DBPoints = new ArrayList<TexturedModel>();
		this.DBEnabled = false;
		loadPointsAsModels();
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
		 * Create background
		 */
		TexturedModel background = createRectangle(-1f, -1f, 2f, 2f, 0f, "white");
		renderer.render(background, shader);
		
		for (TexturedModel dbp : DBPoints) {
			renderer.render(dbp, shader);
		}
		
		for (TexturedModel pnt : dataPoints) {
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
		
		/* Mouse clicks */
		if (Mouse.isButtonDown(0)) {
			if (!wasDown) {
				addPointFromMouseClick(Mouse.getX(), Mouse.getY());
				wasDown = true;
				loadPointsAsModels();
				DBPoints.clear();
				DBEnabled = false;
			}
		} else {
			wasDown = false;
		}
		
		/* Decision boundary mode */
		if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
			if (!DBEnabled) {
				loadDecisionBoundaryPointsHorizontal();
				loadDecisionBoundaryPointsVertical();
				DBEnabled = true;
			}
		}
		
		/* Button presses for edit state */
		if (Keyboard.isKeyDown(Keyboard.KEY_1)) { // class true
			this.editState = 1;
		} else if (Keyboard.isKeyDown(Keyboard.KEY_2)) { // class false
			this.editState = 2;
		} else if (Keyboard.isKeyDown(Keyboard.KEY_3)) { // class unknown
			this.editState = 3;
		} 
		
		/**
		 * Change value of k
		 */
		if (Keyboard.isKeyDown(Keyboard.KEY_NUMPAD1)) {
			setKAndClearBoundary(1);
		} else if (Keyboard.isKeyDown(Keyboard.KEY_NUMPAD2)) {
			setKAndClearBoundary(2);
		} else if (Keyboard.isKeyDown(Keyboard.KEY_NUMPAD3)) {
			setKAndClearBoundary(3);
		} else if (Keyboard.isKeyDown(Keyboard.KEY_NUMPAD4)) {
			setKAndClearBoundary(4);
		} else if (Keyboard.isKeyDown(Keyboard.KEY_NUMPAD5)) {
			setKAndClearBoundary(5);
		} else if (Keyboard.isKeyDown(Keyboard.KEY_NUMPAD6)) {
			setKAndClearBoundary(6);
		} else if (Keyboard.isKeyDown(Keyboard.KEY_NUMPAD7)) {
			setKAndClearBoundary(7);
		} else if (Keyboard.isKeyDown(Keyboard.KEY_NUMPAD8)) {
			setKAndClearBoundary(8);
		} else if (Keyboard.isKeyDown(Keyboard.KEY_NUMPAD9)) {
			setKAndClearBoundary(9);
		}
		
		/* Removal of points */
		if (Keyboard.isKeyDown(Keyboard.KEY_R)) {
			kn.clearPoints();
			loadPointsAsModels();
			DBPoints.clear();
			DBEnabled = false;
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
	
	/**
	 * Loads all stored and training points in the KNearest object as textured models into 
	 * the list this renderer keeps.
	 */
	public void loadPointsAsModels() {
		
		/* Clear current list of models */
		dataPoints.clear();
		
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
				System.out.println("Set to red");
			} else {
				color = "blue";
				System.out.println("Set to blue");
			}
			
			TexturedModel pnt = createRectangle(normX - 0.5f * pointSize, normY - 0.5f * pointSize, pointSize, (h / w) * pointSize, -1f, color);
			dataPoints.add(pnt);
			
		}
		
	}
	
	/**
	 * Finds the decision boundary points horizontally
	 */
	public void loadDecisionBoundaryPointsHorizontal() {
				
		float h = Display.getDisplayMode().getHeight();
		float w = Display.getDisplayMode().getWidth();
		
		/**
		 * For each row, find the points at which the classification changes and
		 * add decision boundary points there
		 */
		for (int i = 0; i <= h; i += DBStepSize) {
			
			/* Store the current height and the starting classification */
			float curY = i;
			boolean prevClass = kn.classify(createPoint(0, curY));
			
			/**
			 * For each pixel, determine if the class changed. If so, add a point between
			 * this pixel and the previous one
			 */
			for (int j = 1; j <= w; j += DBStepSize) {
				
				/* Get the current class */
				boolean curClass = kn.classify(createPoint(j, curY));
				
				/* If different, add point and change prev class */
				if (curClass != prevClass) {
					
					prevClass = curClass;
					
					/* Normalize coordinates */
					float curX = j - (0.5f * DBStepSize);
					float normX = (float) ((curX / w) * 2 - 1);
					float normY = (float) ((curY / h) * 2 - 1);
					
					TexturedModel pnt = createRectangle(normX - 0.5f * dbpointSize, normY + 0.5f * dbpointSize, dbpointSize, (h / w) * dbpointSize, -0.5f, "black");
					DBPoints.add(pnt);
					
					/* Check if we draw class (colored) border */
					if (DBBORDERENABLED) {
						
						/* Find x coordinates of the border points */
						float nextX = curX + 1f;
						float prevX = curX - 1f;
						float normNextX = (float) ((nextX / w) * 2 - 1);
						float normPrevX = (float) ((prevX / w) * 2 - 1);
						
						/* Find color of the border points */
						String nextColor;
						String prevColor;
						if (curClass) {
							nextColor = "lightred";
							prevColor = "lightblue";
						} else {
							nextColor = "lightblue";
							prevColor = "lightred";
						}
						
						/* Create models and add them to the list */
						TexturedModel nextPnt = createRectangle(normNextX - 0.5f * dbpointSize, normY + 0.5f * dbpointSize, dbpointSize, (h / w) * dbpointSize, -0.25f, nextColor);
						TexturedModel prevPnt = createRectangle(normPrevX - 0.5f * dbpointSize, normY + 0.5f * dbpointSize, dbpointSize, (h / w) * dbpointSize, -0.25f, prevColor);
						DBPoints.add(nextPnt);
						DBPoints.add(prevPnt);
						
					}
					
				}
				
			}
			
		}
		
	}
	
	/**
	 * Finds the decision boundary points vertically
	 */
	public void loadDecisionBoundaryPointsVertical() {
				
		float h = Display.getDisplayMode().getHeight();
		float w = Display.getDisplayMode().getWidth();
		
		/**
		 * For each row, find the points at which the classification changes and
		 * add decision boundary points there
		 */
		for (int i = 0; i <= w; i += DBStepSize) {
			
			/* Store the current height and the starting classification */
			float curX = i;
			boolean prevClass = kn.classify(createPoint(curX, 0));
			
			/**
			 * For each pixel, determine if the class changed. If so, add a point between
			 * this pixel and the previous one
			 */
			for (int j = 1; j <= h; j += DBStepSize) {
				
				/* Get the current class */
				boolean curClass = kn.classify(createPoint(curX, j));
				
				/* If different, add point and change prev class */
				if (curClass != prevClass) {
					
					prevClass = curClass;
					
					/* Normalize coordinates */
					float curY = j - (0.5f * DBStepSize);
					float normX = (float) ((curX / w) * 2 - 1);
					float normY = (float) ((curY / h) * 2 - 1);
					
					TexturedModel pnt = createRectangle(normX - 0.5f * dbpointSize, normY + 0.5f * dbpointSize, dbpointSize, (h / w) * dbpointSize, -0.5f, "black");
					DBPoints.add(pnt);
					
					/* Check if we draw class (colored) border */
					if (DBBORDERENABLED) {
					
						/* Find y coordinates of the border points */
						float nextY = curY + 1f;
						float prevY = curY - 1f;
						float normNextY = (float) ((nextY / h) * 2 - 1);
						float normPrevY = (float) ((prevY / h) * 2 - 1);
						
						/* Find color of the border points */
						String nextColor;
						String prevColor;
						if (curClass) {
							nextColor = "lightred";
							prevColor = "lightblue";
						} else {
							nextColor = "lightblue";
							prevColor = "lightred";
						}
						
						/* Create models and add them to the list */
						TexturedModel nextPnt = createRectangle(normX - 0.5f * dbpointSize, normNextY + 0.5f * dbpointSize, dbpointSize, (h / w) * dbpointSize, -0.25f, nextColor);
						TexturedModel prevPnt = createRectangle(normX - 0.5f * dbpointSize, normPrevY + 0.5f * dbpointSize, dbpointSize, (h / w) * dbpointSize, -0.25f, prevColor);
						DBPoints.add(nextPnt);
						DBPoints.add(prevPnt);
						
					}
					
				}
				
			}
			
		}
	}
	
	/**
	 * Creates a new point at (x,y) with no class
	 * 
	 * @param x  the x position
	 * @param y  the y position
	 * @return  a Point object with the given x and y coordinates
	 */
	public Point createPoint(double x, double y) {
		List<Double> coords = new ArrayList<Double>();
		coords.add(x);
		coords.add(y);
		return new Point(coords);
	}
	
	/**
	 * Sets k of knearest to given value and resets decision boundary
	 * 
	 * @param k  the new value for k
	 */
	private void setKAndClearBoundary(int k) {
		kn.setK(k);
		DBPoints.clear();
		DBEnabled = false;
	}
}
