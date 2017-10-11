package PathFinding;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.lwjgl.util.vector.Vector2f;

/**
 * Runs the Lee algorithm to generate a path from a spawn to end
 * 
 * @author Chiel Ton
 */
public class Lee {
	private static int DX;
	private static int DY;
	private static int[][] MAP;
	private static List<Integer> PATH;

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
		// return once destination has been reached
		if ((x == DX && y == DY) || finished) {
			finished = true;
			return;
		}

		// Check for edge, go down
		if (x + 1 < MAP.length)
			if (MAP[x + 1][y] > k + 1) {
				MAP[x + 1][y] = k + 1;
				lee(k + 1, x + 1, y, finished);
			}
		// Check for edge, go up
		if (x - 1 >= 0)
			if (MAP[x - 1][y] > k + 1) {
				MAP[x - 1][y] = k + 1;
				lee(k + 1, x - 1, y, finished);
			}
		// Check for edge, go right
		if (y + 1 < MAP[0].length)
			if (MAP[x][y + 1] > k + 1) {
				MAP[x][y + 1] = k + 1;
				lee(k + 1, x, y + 1, finished);
			}
		// Check for edge, go left
		if (y - 1 >= 0)
			if (MAP[x][y - 1] > k + 1) {
				MAP[x][y - 1] = k + 1;
				lee(k + 1, x, y - 1, finished);
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
		PATH.add(x);
		PATH.add(y);

		// Check for edge, go down
		if (x + 1 < MAP.length) {
			if (MAP[x + 1][y] == k - 1) {
				leeBack(k - 1, x + 1, y);
				return;
			}
		}
		// Check for edge, go up
		if (x - 1 > 0) {
			if (MAP[x - 1][y] == k - 1) {
				leeBack(k - 1, x - 1, y);
				return;
			}
		}
		// Check for edge, go right
		if (y + 1 < MAP[0].length) {
			if (MAP[x][y + 1] == k - 1) {
				leeBack(k - 1, x, y + 1);
				return;
			}
		}
		// Check for edge, go left
		if (y - 1 > 0) {
			if (MAP[x][y - 1] == k - 1) {
				leeBack(k - 1, x, y - 1);
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
	public static List<Integer> startLee(int[][] m, Vector2f d, Vector2f s) {
		// convert input variables to integers
		int sx = (int) s.getX();
		int sy = (int) s.getY();
		DX = (int) d.getX();
		DY = (int) d.getY();
		MAP = m;
		PATH = new ArrayList<>(); // return path

		// fill matrix using lee
		for (int i = 0; i < MAP.length; i++) {
			for (int j = 0; j < MAP[0].length; j++) {
				if (MAP[i][j] == 0)
					MAP[i][j] = -1;
				if (MAP[i][j] == 1)
					MAP[i][j] = 200;
				if (MAP[i][j] == 2 || MAP[i][j] == 3)
					MAP[i][j] = -MAP[i][j];
			}
		}
		lee(0, sx, sy, false);
		// find the starting point for the back track
		// which is the lowest point next to the destination
		int e = 200;
		int x = 0;
		int y = 0;
		if (DX + 1 < MAP.length)
			if (MAP[DX + 1][DY] < e && MAP[DX + 1][DY] != -1) {
				e = MAP[DX + 1][DY];
				x = DX + 1;
				y = DY;
			}
		if (DX - 1 >= 0)
			if (MAP[DX - 1][DY] < e && MAP[DX - 1][DY] != -1) {
				e = MAP[DX - 1][DY];
				x = DX - 1;
				y = DY;
			}
		if (DY + 1 < MAP[0].length)
			if (MAP[DX][DY + 1] < e && MAP[DX][DY + 1] != -1) {
				e = MAP[DX][DY + 1];
				x = DX;
				y = DY + 1;
			}
		if (DY - 1 >= 0)
			if (MAP[DX][DY - 1] < e && MAP[DX][DY - 1] != -1) {
				e = MAP[DX][DY - 1];
				x = DX;
				y = DY - 1;
			}

		// backtrack, fix order and return
		// does not include spawn
		leeBack(e, x, y);
		Collections.reverse(PATH);
		for(int i = 0; i<PATH.size();i+=2) {
			int b = PATH.get(i);
			PATH.set(i, PATH.get(i+1));
			PATH.set(i+1, b);
		}
		PATH.add(DX);
		PATH.add(DY);
		return PATH;
	}
}
