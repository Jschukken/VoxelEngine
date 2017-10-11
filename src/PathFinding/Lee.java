package PathFinding;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.vector.Vector3f;

import Map.Map;

/**
 * Runs the Lee algorithm to generate a path from a spawn to end
 * 
 * @author Chiel Ton
 */
public class Lee {
	private static int DX;
	private static int DY;
	private static int DZ;
	private static int[][][] MAP;

	/**
	 * fill the map using lee's algorithm
	 * 
	 * @param k
	 *            current cell value
	 * @param x
	 *            current x coordinate
	 * @param y
	 *            current y coordinate
	 * @param z
	 *            current z coordinate
	 * @param finished
	 *            boolean to indicate the algorithm is finished
	 */
	private static void lee3D(int k, int x, int y, int z, boolean finished) {
		// return once destination or another path to it has been reached
		if ((DX == x && DY == y && DZ == z) || finished) {
			finished = true;
			return;
		}

		if (x + 1 < MAP.length) {
			if (y + 1 < MAP[0].length) {
				if (MAP[x + 1][y + 1][z] > k + 1) {
					MAP[x + 1][y + 1][z] = k + 1;
					lee3D(k + 1, x + 1, y + 1, z, finished);
				}
			}
			if (MAP[x + 1][y][z] > k + 1) {
				MAP[x + 1][y][z] = k + 1;
				lee3D(k + 1, x + 1, y, z, finished);
			}
			if (y - 1 > 0)
				if (MAP[x + 1][y - 1][z] > k + 1) {
					MAP[x + 1][y - 1][z] = k + 1;
					lee3D(k + 1, x + 1, y - 1, z, finished);
				}
		}
		if (x - 1 >= 0) {
			if (y + 1 < MAP[0].length) {
				if (MAP[x - 1][y + 1][z] > k + 1) {
					MAP[x - 1][y + 1][z] = k + 1;
					lee3D(k + 1, x - 1, y + 1, z, finished);
				}
			}
			if (MAP[x - 1][y][z] > k + 1) {
				MAP[x - 1][y][z] = k + 1;
				lee3D(k + 1, x - 1, y, z, finished);
			}
			if (y - 1 > 0)
				if (MAP[x - 1][y-1][z] > k + 1) {
					MAP[x - 1][y-1][z] = k + 1;
					lee3D(k + 1, x - 1, y-1, z, finished);
				}
		}
		if (z + 1 < MAP[0][0].length) {
			if (y + 1 < MAP[0].length) {
				if (MAP[x][y + 1][z + 1] > k + 1) {
					MAP[x][y + 1][z + 1] = k + 1;
					lee3D(k + 1, x, y + 1, z + 1, finished);
				}
			}
			if (MAP[x][y][z +1] > k + 1) {
				MAP[x][y][z +1] = k + 1;
				lee3D(k + 1, x, y, z+1, finished);
			}
			if (y - 1 > 0) {
				if (MAP[x][y - 1][z + 1] > k + 1) {
					MAP[x + 1][y - 1][z + 1] = k + 1;
					lee3D(k + 1, x, y - 1, z + 1, finished);
				}
			}
		}
		if (z - 1 >= 0) {
			if (y + 1 < MAP[0].length) {
				if (MAP[x][y + 1][z - 1] > k + 1) {
					MAP[x][y + 1][z - 1] = k + 1;
					lee3D(k + 1, x, y + 1, z + 1, finished);
				}
			}
			if (MAP[x][y][z-1] > k + 1) {
				MAP[x][y][z-1] = k + 1;
				lee3D(k + 1, x, y, z-1, finished);
			}
			if (y - 1 > 0) {
				if (MAP[x][y - 1][z - 1] > k + 1) {
					MAP[x][y - 1][z - 1] = k + 1;
					lee3D(k + 1, x, y - 1, z - 1, finished);
				}
			}
		}
		System.out.println("no end point found");
	}

	/**
	 * Backtrack through the filled matrix to find the shortest path
	 * 
	 * @param path
	 *            path found so far
	 * @param k
	 *            current cell value
	 * @param x
	 *            current x coordinate
	 * @param y
	 *            current y coordinate
	 * @param z
	 *            current z coordinate
	 */
	private static void leeBack3D(List<Integer> path, int k, int x, int y, int z) {
		// set current point as part of the path
		path.add(x);
		path.add(y);
		path.add(z);

		// try all possible options to find a step down in the matrix
		if (x + 1 < MAP[0].length) {
			if (MAP[x + 1][y+1][z] == k - 1) {
				leeBack3D(path, k - 1, x + 1, y, z);
				return;
			} else if (MAP[x + 1][y][z] == k - 1) {
				leeBack3D(path, k - 1, x + 1, y, z);
				return;
			} else if (y - 1 > 0)
				if (MAP[x + 1][y-1][z ] == k - 1) {
					leeBack3D(path, k - 1, x + 1, y-1, z);
					return;
				}
		}
		if (x - 1 > 0) {
			if (MAP[x - 1][y+1][z] == k - 1) {
				leeBack3D(path, k - 1, x - 1, y+1, z);
				return;
			} else if (MAP[x - 1][y][z] == k - 1) {
				leeBack3D(path, k - 1, x - 1, y, z);
				return;
			} else if (y - 1 > 0)
				if (MAP[x - 1][y-1][z] == k - 1) {
					leeBack3D(path, k - 1, x - 1, y-1, z);
					return;
				}
		}
		if (z + 1 < MAP[0][0].length) {
			if (MAP[x][y + 1][z + 1] == k - 1) {
				leeBack3D(path, k - 1, x, y + 1, z + 1);
				return;
			} else if (MAP[x][y][z+1] == k - 1) {
				leeBack3D(path, k - 1, x, y , z+ 1);
				return;
			} else if (y - 1 > 0)
				if (MAP[x][y - 1][z + 1] == k - 1) {
					leeBack3D(path, k - 1, x, y - 1, z + 1);
					return;
				}
		}
		if (z - 1 > 0) {
			if (MAP[x][y + 1][z - 1] == k - 1) {
				leeBack3D(path, k - 1, x, y + 1, z - 1);
				return;
			} else if (MAP[x][y - 1][z] == k - 1) {
				leeBack3D(path, k - 1, x, y , z- 1);
				return;
			} else if (y - 1 > 0)
				if (MAP[x][y - 1][z - 1] == k - 1) {
					leeBack3D(path, k - 1, x, y - 1, z - 1);
					return;
				}
		}
	}

	/**
	 * starts the execution of Lee's algorithm
	 * 
	 * @param m
	 *            the current 3D map
	 * @param s
	 *            the vector pointing to the spawn
	 * @param d
	 *            the vector pointing to the destination
	 * @return
	 */
	public static List<Integer> startLee(int[][][] m, Vector3f s, Vector3f d) {
		// convert input variables to integers
		int sx = (int) s.getX();
		int sy = (int) s.getY();
		int sz = (int) s.getZ();
		DX = (int) s.getX();
		DY = (int) s.getY();
		DX = (int) s.getZ();
		MAP = m;

		// create return path using lee's algorithm
		for (int i = 0; i < MAP.length; i++) {
			for (int j = 0; j < MAP[0].length; j++) {
				for (int k = 0; k < MAP[0][0].length; k++) {
					if (MAP[i][j][k] > 0)
						MAP[i][j][k] = -MAP[i][j][k];
					if (MAP[i][j][k] == 0)
						MAP[i][j][k] = 200;
				}
			}
		}
		lee3D(0, sx, sy, sz, false);
		int e = 0;
		int x = 0;
		int y = 0;
		int z = 0;
		// find the starting point for the back track
		for (int i = 0; i < MAP.length; i++) {
			for (int j = 0; j < MAP[0].length; j++) {
				for (int k = 0; k < MAP[0][0].length; k++) {
					if (MAP[i][j][k] > e && MAP[i][j][k] != 200) {
						e = MAP[i][j][k];
						x = i;
						y = j;
						z = k;
					}
				}
			}
		}
		// backtrack and save path
		List<Integer> path = new ArrayList<>();
		leeBack3D(path, e, x, y, z);
		// set matrix back to normal
		for (int i = 0; i < MAP.length; i++)
			for (int j = 0; j < MAP[0].length; j++)
				for (int k = 0; k < MAP[0][0].length; k++)
					if (MAP[i][j][k] > 0 || MAP[i][j][k] == -5)
						m[i][j][k] = 0;
		return path;
	}
}
