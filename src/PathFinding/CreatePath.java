package PathFinding;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import Map.Map;

/**
 * Creates the paths for the enemies
 * 
 * @author Chiel Ton
 */
public class CreatePath {
	/**
	 * Calculates the optimal path between two points Uses Lee and A star depending
	 * on the selected code
	 * 
	 * @param m
	 *            current map
	 * @param s
	 *            starting vector
	 * @param d
	 *            destination vector
	 * @return the path between destination and end
	 */
	public static int[] createPathOld(int[][][] m, Vector3f d, Vector3f s) {
		List<Integer> path = new ArrayList<>(); // holds the final path

		// set map back to 2D form
		int[][] map = new int[m.length][m[0][0].length];
		for (int i = 0; i < m.length; i++)
			for (int j = 0; j < m[0].length; j++)
				for (int k = 0; k < m[0][0].length; k++)
					if (m[i][j][k] == 1) {
						if (j == 0)
							map[i][k] = 1;
						if (j + 1 < m[0][0].length)
							if (j == 1 && !(m[i][j + 1][k] == 1)) {
								map[i][k] = 1;
							}
					} else if (m[i][j][k] == 2) {
						map[i][k] = 2;
					} else if (m[i][j][k] == 3) {
						map[i][k] = 3;
					}
		// set vectors to 2D
		Vector2f destination = new Vector2f(d.getX(), d.getZ());
		Vector2f spawn = new Vector2f(s.getX(), s.getZ());
		path = AStar.startAStar(map, destination, spawn);
		// obtain and return path
		int[] ret = new int[path.size()];
		for (int i = 0; i < path.size(); i++) {
			ret[i] = path.get(i);
		}
		return ret;
	}

	/**
	 * Calculates the optimal path between two points Uses Lee and A star depending
	 * on the selected code
	 * 
	 * @param m
	 *            current map
	 * @param s
	 *            starting vector
	 * @param d
	 *            destination vector
	 * @return the path between destination and end
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
		path = AStar.startAStar(lmap, destination, spawn);
		// obtain, scale and return path
		int[] ret = new int[path.size()];
		for (int i = 0; i < path.size(); i++) {
			ret[i] = path.get(i);
		}
		System.out.println(Arrays.toString(ret));
		return ret;
	}

	/**
	 * just for testing, not working anymore
	 */
	//public static void main(String[] args) {
		/*
		 * int[][][] map = new int[10][1][10]; map[5][0][5] = 2; map[5][0][7] = 3;
		 * map[4][0][5] = 1; map[4][0][6] = 1; map[4][0][7] = 1; map[6][0][5] = 1;
		 * map[6][0][7] = 1; map[7][0][5] = 1; map[7][0][6] = 1; map[7][0][7] = 1; int
		 * dx = 0, dy = 0, dz = 0, sx = 0, sy = 0, sz = 0;
		 * 
		 * // find starting and end point for (int i = 0; i < map.length; i++) for (int
		 * j = 0; j < map[0].length; j++) for (int k = 0; k < map[0][0].length; k++) {
		 * if (map[i][j][k] == 2) { dx = i; dy = k; dz = j; } if (map[i][j][k] == 3) {
		 * sx = i; sy = k; sz = j; } } System.out.println(Arrays.toString(createPath(new
		 * Vector3f(sx, sy, sz))));
		 */
	//}
}
