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
	private static double spawns; // amount of spawn points
	private static boolean found; // ugly
	private static int weihtspawn = 5; // k-nearest weight for the amount of spawn points
	private static int weightmean = 5; // k-nearest weight for the mean distance to the spawn points
	private static int weightmax = 5; // k-nearest weight for the maximum distance to the spawn points
	private static int weightmin = 5; // k-nearest weight for the minimum distance to the spawn points
	private static int weightRoute = 5; // k-nearest weight for the amount of possible route/open space
	private static int weightpathtile = 5; // k-nearest weight for the amount of path tiles there are in the map
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
		if (map[x][y] == 1) {
			map[x][y] = 4;
		}

		// Check for edge, go down
		if (x + 1 < map.length) {
			if (map[x + 1][y] == 2) {
				found = true;
				return;
			} else if (map[x + 1][y] == 1) {
				lee(x + 1, y);
			}
		}
		// Check for edge, go up
		if (x - 1 >= 0) {
			if (map[x - 1][y] == 2) {
				found = true;
				return;
			} else if (map[x - 1][y] == 1) {
				lee(x - 1, y);
			}
		}
		// Check for edge, go right
		if (y + 1 < map.length) {
			if (map[x][y + 1] == 2) {
				found = true;
				return;
			} else if (map[x][y + 1] == 1) {
				lee(x, y + 1);
			}

		}
		// Check for edge, go left
		if (y - 1 >= 0) {
			if (map[x][y - 1] == 2) {
				found = true;
				return;
			} else if (map[x][y - 1] == 1) {
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
		characteristics.add((double) 0);
	}

	/**
	 * calculates a parameter closely related to the amount of possible path choices
	 * and thickness of the paths
	 */
	private static void amountOfPaths() {
		int sidePath = 0; // number of found path diversions for one cell
		double routeOption = 0; // number of total found path diversions
		double normRouteOption = 0;

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

					if (sidePath > 2)
						routeOption = routeOption + (sidePath - 2);
					sidePath = 0;
				}
		normRouteOption = (routeOption / (map.length * map[0].length * 4)) * weightRoute;
		System.out.println(normRouteOption);
		characteristics.add(normRouteOption);
	}

	/**
	 * calculate average, minimum and maximum distances
	 */
	private static void distances() {
		int sx = 0; // current spawn x coordinate
		int sy = 0; // current spawn y coordinate

		int distance; // distance from current spawn point to destination
		double maxDist = 0; // max distance
		double minDist = Integer.MAX_VALUE; // minimal distance
		double meanDist = 0; // average distance
		double normSpawns = 0;
		double normMeanDist = 0;
		double normmaxDist = 0;
		double normminDist = 0;

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
		normSpawns = (spawns / 5) * weihtspawn;
		normMeanDist = (meanDist / (map.length + map[0].length)) * weightmean;
		normmaxDist = (maxDist / (map.length + map[0].length)) * weightmax;
		normminDist = (normmaxDist / (map.length + map[0].length)) * weightmin;
		characteristics.add(normSpawns);
		characteristics.add(normMeanDist);
		characteristics.add(normmaxDist);
		characteristics.add(normminDist);
	}

	/**
	 * Count the number of pathTiles
	 */
	private static void pathTiles() {
		double pathtile = 0; // amount of path tiles
		double normpathtile = 0;
		// calculate the amount of path tiles
		for (int i = 0; i < map.length; i++) {
			for (int j = 0; j < map[0].length; j++) {
				if (map[i][j] == 1) {
					pathtile++;
				}
			}
		}
		normpathtile = (pathtile / (map.length * map[0].length)) * weightpathtile;
	}

	/**
	 * Fills in the characteristics list
	 * 
	 * @param map
	 *            the map passed to the class
	 * @return an array of normalised values, in order: the amount of path tiles the
	 *         map contains the amount of spawns there are the average manhattan
	 *         distance between the spawn and and point the maximum manhattan
	 *         distance between the spawn and and point the minimum manhattan
	 *         distance between the spawn and and point a parameter closely related
	 *         to the amount of possible path choices and thickness of the paths an
	 *         parameter indicating if there are paths connecting all the spawns to
	 *         the destination. outputs 1 if valid and 0 if not
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