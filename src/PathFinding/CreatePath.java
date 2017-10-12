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
	public static List<Integer> createPath(int[][][] m, Vector3f d, Vector3f s) {
		// set map back to 2D form
		int[][] map = new int[m.length][m[0][0].length];
		for (int i = 0; i < m.length; i++)
			for (int j = 0; j < m[0].length; j++)
				for (int k = 0; k < m[0][0].length; k++)
					if (m[i][j][k] == 1) {
						if (j == 0)
							map[i][k] = 1;
						if (j - 1 > 0 && j + 1 < m[0][0].length)
							if (!(m[i][j - 1][k] == 1 || m[i][j + 1][k] == 1)) {
								map[i][k] = 1;
							}
					} else if (m[i][j][k] == 2) {
						map[i][k] = 2;
					} else if (m[i][j][k] == 3) {
						map[i][k] = 3;
					}
		// set vectors to 2D
		Vector2f spawn = new Vector2f(s.getX(), s.getY());
		Vector2f destination = new Vector2f(d.getX(), d.getY());
		// obtain and return path
		return AStar.startAStar(map, destination, spawn);
	}

	/**
	 * just for testing
	 */
	public static void main(String[] args) {
		List<Integer> path = new ArrayList<>();
		int[][][] map = new int[10][1][10];
		map[5][0][5] = 2;
		map[5][0][7] = 3;
		map[4][0][5] = 1;
		map[4][0][6] = 1;
		map[4][0][7] = 1;
		map[6][0][5] = 1;
		map[6][0][7] = 1;
		map[7][0][5] = 1;
		map[7][0][6] = 1;
		map[7][0][7] = 1;
		int dx = 0, dy = 0, dz = 0, sx = 0, sy = 0, sz = 0;

		// find starting and end point
		for (int i = 0; i < map.length; i++)
			for (int j = 0; j < map[0].length; j++)
				for (int k = 0; k < map[0][0].length; k++) {
					if (map[i][j][k] == 2) {
						dx = i;
						dy = k;
						dz = j;
					}
					if (map[i][j][k] == 3) {
						sx = i;
						sy = k;
						sz = j;
					}
				}
		path = createPath(map, new Vector3f(dx, dy, dz), new Vector3f(sx, sy, sz));

		// convert and print path
		int[] print = new int[path.size()];
		for (int i = 0; i < path.size(); i++)
			print[i] = path.get(i);
		System.out.println(Arrays.toString(print));
	}
}
