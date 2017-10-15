package Menu;

import Models.RawModel;
import Models.TexturedModel;
import RenderEngine.DisplayManager;
import RenderEngine.Loader;
import RenderEngine.MasterMenuRenderer;
import Shaders.StaticShaderMenu;
import Textures.ModelTexture;

public class MapMenuRenderer {

	// A loader for... uhm... loading?
	Loader loader;
		
	public MapMenuRenderer() {
		this.loader = new Loader();
	}
	
	/**
	 * Handles the placement of the rendered map in the mapmenu
	 */
	public void renderMapMenu(int[][] map, StaticShaderMenu shader, MasterMenuRenderer renderer) {
		renderMap(map, -0.9f, 0.9f, 1.8f, 1.3f, shader, renderer);
	}
	
	/**
	 * Renders a 2D representation of the map at position x,y with a total
	 * given width and height.
	 * 
	 * @param map  the map to display
	 * @param x  the x position of the top left corner
	 * @param y  the y position of the top left corner
	 * @param w  the width of the displayed map
	 * @param h  the height of the displayed map
	 */
	public void renderMap(int[][] map, float x, float y, float w, float h, StaticShaderMenu shader, MasterMenuRenderer renderer) {
		
		/* Compute height and width per tile */
		float hPerTile = map.length / h;
		float wPerTile = map[0].length / w;
		
		/* Start shader and renderer */
		shader.start();
		renderer.prepare();
		
		for (int i = 0; i < map.length; i++) {
			for (int j = 0; j < map[i].length; j++) {
				
				/**
				 * Get each tile info
				 * Compute its current x and y position
				 * Find the correct texture for the tile type
				 */
				int tileType = map[i][j];
				float curX = (x + (i * wPerTile));
				float curY = (y - (j * hPerTile));
				
				/* Get the correct texture */
				String tileTexture = "";
				if (tileType == 0) {
					tileTexture = "white";
				} else if (tileType == 1) {
					tileTexture = "red";
				} else if (tileType == 2) {
					tileTexture = "blue";
				} else if (tileType == 3) {
					tileTexture = "black";
				}
				
				/* Create the texturedModel for this tile and render the tile */
				TexturedModel tile = createRectangle(curX, curY, hPerTile, wPerTile, -1f, tileTexture);
				renderer.render(tile, shader);
				
			}
		}
		
		shader.stop();
		
	}
	
	
	/**
	 * Creates a rectangle and returns a texturedmodel object of it.
	 * 
	 * @param x  the x coordinate of the lower left corner
	 * @param y  the y coordinate of the lower left corner
	 * @param h  the height of the rectangle
	 * @param w  the width of the rectangle
	 * @param texture  a string representing the color
	 * @return  a texturedmodel of a rectangle at (x,y) with height h, width w and texture
	 */
	public TexturedModel createRectangle(float x, float y, float h, float w, float z, String texture) {
		
		/**
		 * Set vertices, indices and uv coordinates
		 */
		float[] vertices = { x, y+h, z, x, y, z, x+w, y, z, x+w, y+h, z};
		int[] indices = { 0, 1, 3, 3, 2, 1};
		float[] uv = { 0, 0, 0, 1, 1, 1, 1, 0 };

		RawModel model = loader.loadToVao(vertices, indices, uv);
		
		ModelTexture textureMod = new ModelTexture(this.loader.loadTexture(texture));
		return new TexturedModel(model, textureMod);
		
	}
	
}
