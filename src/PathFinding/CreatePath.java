package PathFinding;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import Map.Map;

/**
 * Creates the paths for the enemies
 * 
 * @author Chiel Ton
 * @edited Frouke Hekker
 */
public class CreatePath {
	/**
	 * Calculates the optimal path between two points Uses Lee and A star depending
	 * on the selected code. Does not include spawn but does include destination in
	 * the path.
	 * 
	 * @param m
	 *            current map
	 * @param s
	 *            starting vector
	 * @param d
	 *            destination vector
	 * @return the path between spawn and end point given in format xyxyxyxy
	 */
	public static int[] createPath(Vector3f s) {
		List<Integer> path = new ArrayList<>(); // holds the final path
		int dx = 0; // x coordinate of the end point
		int dy = 0; // y coordinate of the end point

		// search for destination and add 0 border around 2D map
		int[][] map = Map.m;
		int[][] lmap = new int[map.length + 2][map[0].length + 2];
		for (int i = 0; i < map.length; i++)
			for (int j = 0; j < map[0].length; j++)
				lmap[i + 1][j + 1] = map[i][j];
		for (int i = 0; i < lmap.length; i++)
			for (int j = 0; j < lmap[0].length; j++)
				if (lmap[i][j] == 2) {
					dx = i;
					dy = j;
				}

		// set vectors to 2D
		Vector2f destination = new Vector2f(dx, dy);
		Vector2f spawn = new Vector2f(s.getX() / Map.SCALE + 1, s.getZ() / Map.SCALE + 1);

		// obtain path
		path = AStar.startAStar(lmap, destination, spawn);

		// scale and return path
		int[] ret = new int[path.size()];
		for (int i = 0; i < path.size(); i++) {
			ret[i] = 5 * (path.get(i) - 1) + 3;

		}
		return ret;
	}
}
