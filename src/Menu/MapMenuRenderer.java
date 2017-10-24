package Menu;

import java.util.ArrayList;
import java.util.List;

import Models.RawModel;
import Models.TexturedModel;
import RenderEngine.Loader;
import Textures.ModelTexture;
import ToolBox.MatrixMath;

/**
 * This still needs filling in, ??S
 * @author s144459
 *
 */
public class MapMenuRenderer {

	// A loader for... uhm... loading?
	private static Loader loader = new Loader();
			
	/**
	 * Creates a list containing all tile textured objects that together form a 2d render of the given map,
	 * at position x,y and with a given width and height.
	 * 
	 * @param map  the map to display
	 * @param x  the x position of the top left corner
	 * @param y  the y position of the top left corner
	 * @param w  the width of the displayed map
	 * @param h  the height of the displayed map
	 */
	public static List<TexturedModel> get2DMapTilesAtPosition(int[][] map, float x, float y, float w, float h) {
		
		List<TexturedModel> result = new ArrayList<TexturedModel>();
		/* Compute height and width per tile */
		float hPerTile = h / map.length;
		float wPerTile = w / map[0].length;
		
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
				if (tileType == 0) { // Nothing
					//tileTexture = "black";
					continue;
				} else if (tileType == 1) { // Path
					tileTexture = "white";
				} else if (tileType == 2) { // Destination
					tileTexture = "Companion Cube";
				} else if (tileType == 3) { // Spawn
					tileTexture = "red";
				}
				
				/* Create the texturedModel for this tile and render the tile */
				TexturedModel tile = createRectangle(curX, curY, hPerTile, wPerTile, -1f, tileTexture);
				result.add(tile);
				
			}
		}
		
		return result;
		
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
	public static TexturedModel createRectangle(float x, float y, float h, float w, float z, String texture) {
		
		/**
		 * Set vertices, indices and uv coordinates
		 */
		float[] vertices = { x, y+h, z, x, y, z, x+w, y, z, x+w, y+h, z};
		int[] indices = { 0, 1, 3, 3, 2, 1};
		float[] uv = { 0, 0, 0, 1, 1, 1, 1, 0 };

		RawModel model = loader.loadToVao(vertices, indices, uv, MatrixMath.CreateNormals(vertices, indices));
		
		ModelTexture textureMod = new ModelTexture(loader.loadTexture(texture));
		return new TexturedModel(model, textureMod);
		
	}
	
}
