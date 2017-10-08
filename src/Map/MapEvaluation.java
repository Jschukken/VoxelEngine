package Map;

import java.util.Arrays;

public class MapEvaluation {

	public static int lee(int x, int y, int[][] map) {
		int reachend = 0;
		map[x][y] = 4;
		if (map[x][y] == 2) {
			reachend = 1;
		}
		

		// Check for edge, go down
		if (x + 1 < map.length) {
			if (map[x + 1][y] == 1) {				
				lee(x + 1, y, map);
			}
		}
		// Check for edge, go up
		if (x - 1 >= 0) {
			if (map[x - 1][y] == 1) {
				lee(x - 1, y, map);
			}
		}
		// Check for edge, go right
		if (y + 1 < map.length) {
			if (map[x][y + 1] == 1) {
				lee(x, y + 1, map);
			}
		}
		// Check for edge, go left
		if (y - 1 >= 0) {
			if (map[x][y - 1] == 1) {
				lee(x, y - 1, map);
			}
			
		}
		return reachend;
	}

	public static double[] characteristics(int[][] map) {
		/*
		 * int[][] map = { { 1, -2, -3, 4, 5, 6, 7, 8, 9 }, { 10, 11, 12, 13, 14, 15,
		 * 16, 17, 18 }, { 19, 20, 21, 22, 23, 24, 25, 26, 27 } };
		 */

		double[] characteristics = new double[10];
		int dx = 0; // destination x coordinate
		int dy = 0; // destination y coordinate
		int sx = 0; // current spawn x coordinate
		int sy = 0; // current spawn y coordinate
		int destsurround = 0;// amount of pathtiles that surround the destination
		int spawnsurround = 0;
		int minspawnsurround = 4;// minimum amount of pathtiles that surround the spawnpoints
		int spawns = 0; // amount of spawn points
		int distance; // distance from current spawn point to destination
		int maxdist = 0; // max distance
		int mindist = 50; // min distance
		double meandist = 0; // average distance
		int pathtile = 0; // amount of pathtiles
		int sidepath = 0; // amount of side paths from current path tile
		int deadend = 0;
		int routeoption = 0; // total amount of route routeoption
		int spawnacces =0; // amount of spawns that can reach the end point via paths

		// define a map with an extra 0 border
		int[][] lmap = new int[map.length + 2][map[0].length + 2];
		for (int i = 0; i < map.length; i++)
			for (int j = 0; j < map[0].length; j++)
				lmap[i + 1][j + 1] = map[i][j];

		/*
		 * distance between spawn point and destination
		 */
		for (int i = 1; i < lmap.length - 1; i++)
			for (int j = 1; j < lmap[0].length - 1; j++)
				if (lmap[i][j] == 2) {
					dx = i;
					dy = j;

					if (lmap[i + 1][j] == 1) {
						destsurround++;
					}
					if (lmap[i][j + 1] == 1) {
						destsurround++;
					}
					if (lmap[i - 1][j] == 1) {
						destsurround++;
					}
					if (lmap[i][j - 1] == 1) {
						destsurround++;
					}
				}
		for (int i = 1; i < lmap.length - 1; i++)
			for (int j = 1; j < lmap[0].length - 1; j++)
				if (lmap[i][j] == 3) {
					sx = i;
					sy = j;

					if (lmap[i + 1][j] == 1) {
						spawnsurround++;
					}
					if (lmap[i][j + 1] == 1) {
						spawnsurround++;
					}
					if (lmap[i - 1][j] == 1) {
						spawnsurround++;
					}
					if (lmap[i][j - 1] == 1) {
						spawnsurround++;
					}
					if (spawnsurround < minspawnsurround)
						minspawnsurround = spawnsurround;
					spawnsurround = 0;

					spawns++;
					distance = Math.abs(dx - sx) + Math.abs(dy - sy);
					if (distance > maxdist) {
						maxdist = distance;
					}
					if (distance < mindist) {
						mindist = distance;
					}
					meandist = meandist + distance;
				}
		meandist = meandist / spawns;
		characteristics[0] = destsurround;
		characteristics[1] = minspawnsurround;
		characteristics[2] = meandist;
		characteristics[3] = maxdist;
		characteristics[4] = mindist;
		characteristics[5] = spawns;

		/*
		 * amount of path tiles
		 */
		for (int i = 0; i < map.length; i++) {
			for (int j = 0; j < map[0].length; j++) {
				if (map[i][j] == 1) {
					pathtile++;
				}
			}
		}
		characteristics[6] = pathtile;

		/*
		 * amount of paths (by checking for junctions)
		 */
		for (int i = 1; i < lmap.length - 1; i++)
			for (int j = 1; j < lmap[0].length - 1; j++)
				if (lmap[i][j] == 1) {
					if (lmap[i + 1][j] == 1) {
						sidepath++;
					}
					if (lmap[i][j + 1] == 1) {
						sidepath++;
					}
					if (lmap[i - 1][j] == 1) {
						sidepath++;
					}
					if (lmap[i][j - 1] == 1) {
						sidepath++;
					}

					if (sidepath == 1)
						deadend++;
					if (sidepath > 2)
						routeoption = routeoption + (sidepath - 2);
					sidepath = 0;
				}

		characteristics[7] = routeoption;
		characteristics[8] = deadend;
		for (int i = 1; i < map.length ; i++)
			for (int j = 1; j < map[0].length ; j++)
				if (map[i][j] == 3) {
					System.out.println(lee(i, j, map));
					if (lee(i, j, map) == 1) {
						spawnacces++;
						
					}
				}
		if (spawnacces==spawns) {
			characteristics[9]=1;
		}else {
			characteristics[9]=0;
		}

		System.out.println(Arrays.toString(characteristics));
		return characteristics;

	}
}
