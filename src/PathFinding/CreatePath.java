package PathFinding;

import java.util.ArrayList;
import java.util.List;

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
	public static List<Integer> createPath(int[][][] m, Vector3f s, Vector3f d) {
		List<Integer> path = new ArrayList<>(); //return path
		
		//set map back to its 2D form
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

		//obtain and return path
		path = AStar.aStarStart(map, s, d);
		return path;
	}

	/**
	 * just for testing
	 */
	public static void main(String[] args) {
		List<Integer> path = new ArrayList<>();
		int[][][] map = Map.createGoodMap();
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
		
		//convert and print path
		int[] print = new int[path.size()] ;
		for(int i = 0; i < path.size()-1; i++) {
			print[i] = path.get(i);
		}
	}
}
