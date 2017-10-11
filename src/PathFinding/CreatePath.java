package PathFinding;

import java.util.*;
import java.util.Arrays;
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
		List<Integer> path = new ArrayList<>();

		path = Lee.startLee(m, s, d);

		int[] ret = new int[path.size()];
		for (int i = 0; i < ret.length; i++) {
			ret[i] = path.get(i).intValue();
		}
		System.out.println(Arrays.toString(ret));
		return path;
	}

	/**
	 * just for testing
	 */
	public static void main(String[] args) {
		List<Integer> path = new ArrayList<>();
		int[][][] map = Map.createGoodMap();
		int dx = 0;
		int dy = 0;
		int dz = 0;
		int sx = 0;
		int sy = 0;
		int sz = 0;
		
		//fix coordinates
		for (int i = 0; i < map.length; i++)
			for (int j = 0; j < map[0].length; j++)
				for(int k = 0; k < map[0][0].length; k++) {
					if(map[i][j][k] == 2) {
						dx = i;
						dy = k;
						dz = j;
					}
					if(map[i][j][k] == 3) {
						sx = i;
						sy = k;
						sz = j;
					}
				}
	
		path = createPath(map, new Vector3f(dx, dy, dz), new Vector3f(sx, sy, sz));
	}
}
