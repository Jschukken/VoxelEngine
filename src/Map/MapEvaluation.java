package Map;

import java.util.ArrayList;
import java.util.List;

/**
 * Evaluates the supplied map
 * 
 * @author Frouke Hekker
 * @author Chiel Ton
 */
public class MapEvaluation {
	private static int[][] map; // 2D map currently being evaluated
	private static int[][] lmap; // map but scaled slightly larger

	private static int dx; // end point x coordinate
	private static int dy; // end point y coordinate
	private static int spawns; // amount of spawn points
	private static boolean found; // ugly

	private static List<Double> characteristics = new ArrayList<>(); // return list

	/**
	 * Runs lee to check if all spawns can reach the end point
	 * 
	 * @param x
	 *            x coordinate from the current point
	 * @param y
	 *            y coordinate from the current point
	 * @param map
	 *            map being evaluated
	 * @return reached end 0 if not reached 1 if reached
	 */
	private static void lee(int x, int y) {
		if (found) {
			return;
		}
		map[x][y] = 4;

		// Check for edge, go down
		if (x + 1 < map.length) {
			if(map[x + 1][y] == 2) {
				found = true;
				return;
			}
			else if (map[x + 1][y] == 1) {
				lee(x + 1, y);
			}
		}
		// Check for edge, go up
		if (x - 1 >= 0) {
			if(map[x - 1][y] == 2) {
				found = true;
				return;
			}
			else if (map[x - 1][y] == 1) {
				lee(x - 1, y);
			}
		}
		// Check for edge, go right
		if (y + 1 < map.length) {
			if(map[x][y + 1] == 2) {
				found = true;
				return;
			}
			else if (map[x][y + 1] == 1) {
				lee(x, y + 1);
			}
			
		}
		// Check for edge, go left
		if (y - 1 >= 0) {
			if(map[x][y - 1] == 2) {
				found = true;
				return;
			}
			else if (map[x][y - 1] == 1) {
				lee(x, y - 1);
			}
			
		}
	}

	/**
	 * checks if the map is valid by using a variant of Lee
	 * 
	 * @return 1 if valid 0 if not
	 */
	private static void checkValid() {
		int spawnacces = 0; // amount of spawns that can reach the end point via paths
		found = false;

		// run lee to check if the map is valid
		for (int i =0; i < map.length; i++)
			for (int j = 0; j < map[0].length; j++)
				if (map[i][j] == 3) {
					lee(i, j);
					if (found) {
						spawnacces++;
					}
					// set values back to basic
					found = false;
					for (int k = 1; k < map.length; k++)
						for (int l = 1; l < map[0].length; l++)
							if (map[k][l] == 4)
								map[k][l] = 1;
				}

		// check if all spawns worked
		if (spawnacces == spawns) {
			characteristics.add((double) 1);
		} else {
			characteristics.add((double) 0);
		}
	}

	/**
	 * calculates a parameter closely related to the amount of possible path choices
	 * and thickness of the paths Dead ends should be zero as of recent updates
	 */
	private static void amountOfPaths() {
		int deadEnd = 0; // number of found dead ends
		int sidePath = 0; // number of found path diversions for one cell
		int routeOption = 0; // number of total found path diversions
		// loop over map to search
		for (int i = 1; i < lmap.length - 1; i++)
			for (int j = 1; j < lmap[0].length - 1; j++)
				if (lmap[i][j] == 1) {
					if (lmap[i + 1][j] == 1) {
						sidePath++;
					}
					if (lmap[i][j + 1] == 1) {
						sidePath++;
					}
					if (lmap[i - 1][j] == 1) {
						sidePath++;
					}
					if (lmap[i][j - 1] == 1) {
						sidePath++;
					}

					if (sidePath == 1)
						deadEnd++;
					if (sidePath > 2)
						routeOption = routeOption + (sidePath - 2);
					sidePath = 0;
				}

		characteristics.add((double) routeOption);
		characteristics.add((double) deadEnd);
	}

	/**
	 * calculate average, minimum and maximum distances
	 */
	private static void distances() {
		int sx = 0; // current spawn x coordinate
		int sy = 0; // current spawn y coordinate

		int distance; // distance from current spawn point to destination
		int maxDist = 0; // max distance
		int minDist = Integer.MAX_VALUE; // minimal distance
		double meanDist = 0; // average distance

		for (int i = 1; i < lmap.length - 1; i++)
			for (int j = 1; j < lmap[0].length - 1; j++)
				if (lmap[i][j] == 3) {
					// find and save spawn point
					sx = i;
					sy = j;
					// calculate distance
					distance = Math.abs(dx - sx) + Math.abs(dy - sy);
					// save potential maximum-, minimum- and for now total-distances
					if (distance > maxDist) {
						maxDist = distance;
					}
					if (distance < minDist) {
						minDist = distance;
					}
					meanDist = meanDist + distance;
				}
		meanDist = meanDist / spawns;

		// save all calculated characteristics
		characteristics.add((double) spawns);
		characteristics.add((double) meanDist);
		characteristics.add((double) maxDist);
		characteristics.add((double) minDist);
	}

	/**
	 * Count the number of pathTiles
	 */
	private static void pathTiles() {
		int pathtile = 0; // amount of path tiles

		// calculate the amount of path tiles
		for (int i = 0; i < map.length; i++) {
			for (int j = 0; j < map[0].length; j++) {
				if (map[i][j] == 1) {
					pathtile++;
				}
			}
		}
		characteristics.add((double) pathtile);
	}

	/**
	 * Fills in the characteristics list
	 * 
	 * @param map
	 *            the map passed to the class
	 * @return the parameter list the class returns
	 */
	public static List<Double> characteristics(int[][] m) {
		spawns = 0;
		// load map into the class
		map = m;

		// define a map with an extra 0 border for use in later function
		lmap = new int[map.length + 2][map[0].length + 2];
		for (int i = 0; i < map.length; i++)
			for (int j = 0; j < map[0].length; j++)
				lmap[i + 1][j + 1] = map[i][j];

		// find the end point and save the coordinates
		for (int i = 1; i < lmap.length - 1; i++)
			for (int j = 1; j < lmap[0].length - 1; j++)
				if (lmap[i][j] == 2) {
					dx = i;
					dy = j;
				}

		// count the number of spawns
		for (int i = 1; i < lmap.length - 1; i++)
			for (int j = 1; j < lmap[0].length - 1; j++)
				if (lmap[i][j] == 3) {
					spawns++;
				}

		// gather the different parameters
		characteristics.clear();
		pathTiles();
		distances();
		amountOfPaths();
		checkValid();

		return characteristics;
	}
}