package Map;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import KNearest.KNearest;

/**
 * Used to do map generation
 * 
 * @author Chiel Ton
 * @edited Frouke Hekker
 */
public class Map {
	public static final int SIZE = 10; // x and y size of the map
	public static final int HEIGHT = 7; // z size of the map
	public static final int THRESHOLD = 80; // threshold for randomly turning 0's into 1's
	public static final int SCALE = 5; // size of 3D map compared to 2D map
	public static final int LIGHTS = 2; // number of lights on the map

	public static int[][] m; // 2D map currently being worked on

	/**
	 * fill the matrix using lee's algorithm
	 * 
	 * @param k
	 *            current depth
	 * @param x
	 *            x coordinate from the current point
	 * @param y
	 *            y coordinate from the current point
	 * @param finished
	 *            boolean to indicate the destination has been found
	 */
	public static void lee(int k, int x, int y, boolean finished) {
		// return once destination or another path to it has been reached
		if (m[x][y] == -2 || m[x][y] == -5 || finished) {
			System.out.println("goal found");
			finished = true;
			return;
		}

		// Check for edge, go down
		if (x + 1 < SIZE) {
			if (m[x + 1][y] > k + 1) {
				m[x + 1][y] = k + 1;
				lee(k + 1, x + 1, y, finished);
			}
		}
		// Check for edge, go up
		if (x - 1 >= 0) {
			if (m[x - 1][y] > k + 1) {
				m[x - 1][y] = k + 1;
				lee(k + 1, x - 1, y, finished);
			}
		}
		// Check for edge, go right
		if (y + 1 < SIZE) {
			if (m[x][y + 1] > k + 1) {
				m[x][y + 1] = k + 1;
				lee(k + 1, x, y + 1, finished);
			}
		}
		// Check for edge, go left
		if (y - 1 >= 0) {
			if (m[x][y - 1] > k + 1) {
				m[x][y - 1] = k + 1;
				lee(k + 1, x, y - 1, finished);
			}
		}
	}

	/**
	 * Backtrack through the filled matrix to find the shortest path
	 * 
	 * @param k
	 *            current step of the function
	 * @param x
	 *            x coordinate of the current point
	 * @param y
	 *            y coordinate of the current
	 */
	public static void leeBack(int k, int x, int y) {
		// set current point as part of the path
		m[x][y] = -5;

		// Check for edge, go down
		if (x + 1 < SIZE) {
			if (m[x + 1][y] == k - 1) {
				leeBack(k - 1, x + 1, y);
				return;
			}
		}
		// Check for edge, go up
		if (x - 1 > 0) {
			if (m[x - 1][y] == k - 1) {
				leeBack(k - 1, x - 1, y);
				return;
			}
		}
		// Check for edge, go right
		if (y + 1 < SIZE) {
			if (m[x][y + 1] == k - 1) {
				leeBack(k - 1, x, y + 1);
				return;
			}
		}
		// Check for edge, go left
		if (y - 1 > 0) {
			if (m[x][y - 1] == k - 1) {
				leeBack(k - 1, x, y - 1);
				return;
			}
		}
	}

	/**
	 * a variant of Lee's algorithm that removes the remaining pieces of path
	 * (anything not taken care of by the initial path)
	 * 
	 * @param x
	 *            x coordinate from the potential loose end
	 * @param y
	 *            y coordinate from the potential loose end
	 */
	public static void leeIsland(int x, int y) {
		// Check for edge, go down
		if (x + 1 < SIZE) {
			if (m[x + 1][y] == -1) {
				m[x + 1][y] = -10;
				leeIsland(x + 1, y);
			}
		}
		// Check for edge, go up
		if (x - 1 >= 0) {
			if (m[x - 1][y] == -1) {
				m[x - 1][y] = -10;
				leeIsland(x - 1, y);
			}
		}
		// Check for edge, go right
		if (y + 1 < SIZE) {
			if (m[x][y + 1] == -1) {
				m[x][y + 1] = -10;
				leeIsland(x, y + 1);
			}
		}
		// Check for edge, go left
		if (y - 1 >= 0) {
			if (m[x][y - 1] == -1) {
				m[x][y - 1] = -10;
				leeIsland(x, y - 1);
			}
		}

	}

	/**
	 * Add in the initial paths from spawns to the end point
	 */
	public static void initial() {
		int d = 0; // current farthest point found by lee
		int x = 0; // x coordinate of farthest point
		int y = 0; // y coordinate of farthest point

		// draw the path for each spawn points
		for (int i = 0; i < m.length; i++)
			for (int j = 0; j < m[0].length; j++)
				if (m[i][j] == -3) {
					// fill matrix with random obstacles
					extras(THRESHOLD);
					// set 0 to high number for lee to work
					for (int k = 0; k < m.length; k++)
						for (int l = 0; l < m[0].length; l++)
							if (m[k][l] == 0)
								m[k][l] = 200;
					lee(0, i, j, false);
					// find the starting point for the back track
					for (int k = 0; k < m.length; k++)
						for (int l = 0; l < m[0].length; l++)
							if (m[k][l] > d && m[k][l] != 200) {
								d = m[k][l];
								x = k;
								y = l;
							}
					leeBack(d, x, y);
					// set matrix back to normal
					for (int k = 0; k < m.length; k++)
						for (int l = 0; l < m[0].length; l++)
							if (m[k][l] > 0)
								m[k][l] = 0;
				}

		// finalize all paths
		for (int k = 0; k < m.length; k++)
			for (int l = 0; l < m[0].length; l++)
				if (m[k][l] == -5)
					m[k][l] = -1;
	}

	/**
	 * takes out the loose ends and stuff not connected to anything
	 */
	public static void removal() {
		// define a map with an extra 0 border
		int[][] lmap = new int[SIZE + 2][SIZE + 2];
		for (int i = 0; i < m.length; i++)
			for (int j = 0; j < m[0].length; j++)
				lmap[i + 1][j + 1] = m[i][j];

		// remove everything that has at most one neighbor, if something is found,
		// repeat
		boolean removed = true;
		while (removed) {
			removed = false;
			for (int i = 1; i < lmap.length - 1; i++)
				for (int j = 1; j < lmap[0].length - 1; j++)
					if (lmap[i][j] == -1) {
						if (lmap[i][j + 1] == 0 && lmap[i][j - 1] == 0 && lmap[i + 1][j] == 0) {
							lmap[i][j] = 0;
							removed = true;
						} else if (lmap[i][j - 1] == 0 && lmap[i + 1][j] == 0 && lmap[i - 1][j] == 0) {
							lmap[i][j] = 0;
							removed = true;
						} else if (lmap[i][j + 1] == 0 && lmap[i + 1][j] == 0 && lmap[i - 1][j] == 0) {
							lmap[i][j] = 0;
							removed = true;
						} else if (lmap[i][j + 1] == 0 && lmap[i][j - 1] == 0 && lmap[i - 1][j] == 0) {
							lmap[i][j] = 0;
							removed = true;
						}
					}
		}

		// set map back to original size and return
		for (int i = 1; i < lmap.length - 1; i++)
			for (int j = 1; j < lmap[0].length - 1; j++)
				m[i - 1][j - 1] = lmap[i][j];

		// lee to remove Islands
		for (int i = 0; i < m.length; i++)
			for (int j = 0; j < m[0].length; j++)
				if (m[i][j] == -3) {
					leeIsland(i, j);
				}

		// return map back to normal
		for (int i = 0; i < m.length; i++)
			for (int j = 0; j < m[0].length; j++)
				if (m[i][j] == -1) {
					m[i][j] = 0;
				} else if (m[i][j] == -10) {
					m[i][j] = -1;
				}

	}

	/**
	 * add in extra possible path used to both generate mazes for lee and to add in
	 * random extras later
	 * 
	 * @param threshold
	 *            change of a random block being turned into a piece of path in
	 *            percentages
	 */
	public static void extras(int threshold) {
		// all empty points get set to a random number
		for (int i = 0; i < m.length; i++)
			for (int j = 0; j < m[0].length; j++)
				if (m[i][j] >= 0)
					m[i][j] = ThreadLocalRandom.current().nextInt(0, 100);

		// if number above threshold set to path else to non-path
		for (int i = 0; i < m.length; i++)
			for (int j = 0; j < m[0].length; j++) {
				if (m[i][j] >= threshold)
					m[i][j] = -1;
				else if (m[i][j] >= 0)
					m[i][j] = 0;
			}
	}

	/**
	 * print the 2D map, prints the 2D map currently in the class variable
	 */
	public static void print2D() {
		// set everything to positive for readability
		for (int k = 0; k < m.length; k++)
			for (int l = 0; l < m[0].length; l++)
				if (m[k][l] < 0)
					m[k][l] = Math.abs(m[k][l]);

		// print
		for (int i = 0; i < m.length; i++) {
			for (int j = 0; j < m.length; j++) {
				System.out.print(m[i][j]);
				System.out.print("\t");
			}
			System.out.println();
		}
	}

	/**
	 * print the 3D map in layers
	 * 
	 * @param map
	 *            the 3D map to be printed
	 */
	public static void print3D(int[][][] map) {
		for (int k = 0; k < HEIGHT; k++) {
			for (int i = 0; i < map.length; i++) {
				for (int j = 0; j < map[0].length; j++) {
					System.out.print(map[i][j][k]);
					System.out.print("\t");
				}
				System.out.println();
			}
			System.out.println();
		}
	}

	/**
	 * Turn the map into it's 3D form includes: scaling, walls, lights and height
	 * map
	 * 
	 * @param return
	 *            returns the finished 3D map
	 */
	public static int[][][] mapTo3D() {
		// initialize the 3D map
		// +2's are for the walls surrounding the map
		int[][][] map = new int[SIZE * SCALE + 2][SIZE * SCALE + 2][HEIGHT];

		// scaling
		for (int i = 0; i < m.length; i++)
			for (int j = 0; j < m[0].length; j++) {
				// scale paths
				if (m[i][j] == 1) {
					for (int k = 0; k < SCALE; k++) {
						for (int l = 0; l < SCALE; l++) {
							map[i * SCALE + k + 1][j * SCALE + l + 1][0] = m[i][j];
						}
					}
				}
				// surround spawn points
				else if (m[i][j] == 2) {
					for (int k = 0; k < SCALE; k++) {
						for (int l = 0; l < SCALE; l++) {
							map[i * SCALE + k + 1][j * SCALE + l + 1][0] = 1;
						}
					}
					map[i * SCALE + SCALE / 2 + 1][j * SCALE + SCALE / 2 + 1][1] = 2;
				}
				// surround end points
				else if (m[i][j] == 3) {
					for (int k = 0; k < SCALE; k++) {
						for (int l = 0; l < SCALE; l++) {
							map[i * SCALE + k + 1][j * SCALE + l + 1][0] = 1;
						}
					}
					map[i * SCALE + SCALE / 2 + 1][j * SCALE + SCALE / 2 + 1][1] = 3;
				}
			}

		// walls
		for (int i = 0; i < map.length - 1; i++)
			for (int j = 0; j < map[0].length - 1; j++)
				if (map[i][j][0] == 1) {
					if (map[i + 1][j][0] == 0) {
						for (int k = 1; k < HEIGHT; k++) {
							map[i + 1][j][k] = 1;
						}
					}
					if (map[i - 1][j][0] == 0) {
						for (int k = 1; k < HEIGHT; k++) {
							map[i - 1][j][k] = 1;
						}
					}
					if (map[i][j + 1][0] == 0) {
						for (int k = 1; k < HEIGHT; k++) {
							map[i][j + 1][k] = 1;
						}
					}
					if (map[i][j - 1][0] == 0) {
						for (int k = 1; k < HEIGHT; k++) {
							map[i][j - 1][k] = 1;
						}
					}
				}
		// lights
		// distributed completely random over the map, slightly smaller change on being
		// in the exact corners
		int l = 0;
		while (l <= LIGHTS) {
			int x = ThreadLocalRandom.current().nextInt(1, SIZE * SCALE);
			int z = ThreadLocalRandom.current().nextInt(1, SIZE * SCALE);
			// check if random position is a wall
			if (map[x][z][4] == 1) {
				// search piece of path next to the wall and place light above it
				if (map[x + 1][z][0] == 1) {
					map[x + 1][z][HEIGHT - 2] = 4;
				} else if (map[x - 1][z][0] == 1) {
					map[x - 1][z][HEIGHT - 2] = 4;
				} else if (map[x][z + 1][0] == 1) {
					map[x][z + 1][HEIGHT - 2] = 4;
				} else if (map[x][z - 1][0] == 1) {
					map[x][z - 1][HEIGHT - 2] = 4;
				}
				// update number of lamps placed
				l++;
			}
		}

		// height
		// with currently only one possible height map
		for (int i = 0; i < map.length - 10; i += 10) {
			for (int j = 0; j < 4; j++) {
				for (int k = 0; k < map[0].length; k++) {
					if (map[i + j][k][1] == 2) {
						map[i + j][k][1] = 1;
						map[i + j][k][2] = 2;
					} else if (map[i + j][k][1] == 3) {
						map[i + j][k][1] = 1;
						map[i + j][k][2] = 3;
					}
					if (map[i + j][k][0] == 1) {
						map[i + j][k][0] = 0;
						map[i + j][k][1] = 1;
					}
				}
			}
		}

		// return
		return map;
	}

	/**
	 * creates a good map by adding in random spawn and end points and filling in
	 * sections of the rest of the map with path, the map itself is in a class
	 * variable
	 */
	public static void createMap() {
		// initialize 2D map
		m = new int[SIZE][SIZE];
		m[ThreadLocalRandom.current().nextInt(0, SIZE)][ThreadLocalRandom.current().nextInt(0, SIZE)] = -2;
		for (int i = 0; i < ThreadLocalRandom.current().nextInt(1, 6); i++) {
			m[ThreadLocalRandom.current().nextInt(0, SIZE)][ThreadLocalRandom.current().nextInt(0, SIZE)] = -3;
		}

		// fill map
		initial();
		extras(THRESHOLD);
		removal();
		for (int i = 0; i < m.length; i++)
			for (int j = 0; j < m[0].length; j++)
				if (m[i][j] < 0)
					m[i][j] = Math.abs(m[i][j]);

	}

}
