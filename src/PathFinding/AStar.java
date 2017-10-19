package PathFinding;

import java.util.ArrayList;
import java.util.List;
import Map.Map;

import org.lwjgl.util.vector.Vector2f;

/**
 * Finds a path for the enemies to follow using A*
 * 
 * @author Chiel Ton
 * @edited Frouke Hekker
 */
public class AStar {
	private static int DX; // end point x coordinate
	private static int DY; // end point y coordinate
	private static int[][] MAP; // 2D version of current map
	public static int[][] DEATHS = new int[Map.m.length + 2][Map.m[0].length + 2]; // number of deaths per tile

	/**
	 * Calculates the heuristic value for the given node (Manhattan distance)
	 * 
	 * @param x
	 *            x coordinate from the node
	 * @param y
	 *            y coordinate from the node
	 * @return h estimated 'distance' to the end point
	 */
	public static double calculateH(int x, int y) {
		// calculate Manhattan distance
		return Math.abs(x - DX) + Math.abs(y - DY);
	}

	/**
	 * Returns the index of the minimal f entry
	 * 
	 * @param list,
	 *            the list from which the node with minimal F will be selected
	 * @return the index
	 */
	private static int minimalF(List<Node> list) {
		double min = Double.MAX_VALUE;
		int index = -1;

		// loop over saving minimal value
		for (int i = 0; i < list.size(); i++) {
			Node current = list.get(i);
			if (current.f < min) {
				min = current.f;
				index = i;
			}
		}
		// return index of the minimal value
		return index;
	}

	/**
	 * given two points, calculates path between these two points return path is
	 * given as an integer list: xyxyxyxyxyxy... Uses the AStar method
	 * 
	 * @param sx
	 *            spawn x point
	 * @param sy
	 *            spawn y point
	 * @return the list of integers that form the path
	 */
	private static List<Integer> aStar(int sx, int sy) {
		List<Integer> path = new ArrayList<>();// return path
		List<Node> pathNodes = new ArrayList<>();// nodes to be in the return path
		List<Node> open = new ArrayList<>();// open list for A*
		List<Node> closed = new ArrayList<>();// closed list for A*
		List<Node> successor = new ArrayList<>();// list of successors for A*

		// initialize first node
		open.add(new Node(sx, sy));
		Node q = null;

		// run the actual algorithm
		while (open.size() != 0) {
			// next working node
			q = open.get(minimalF(open));
			open.remove(q);

			// adding four possible parents
			if (MAP[q.x + 1][q.y] == 1)
				successor.add(new Node(q.x + 1, q.y, q));
			if (MAP[q.x - 1][q.y] == 1)
				successor.add(new Node(q.x - 1, q.y, q));
			if (MAP[q.x][q.y + 1] == 1)
				successor.add(new Node(q.x, q.y + 1, q));
			if (MAP[q.x][q.y - 1] == 1)
				successor.add(new Node(q.x, q.y - 1, q));
			// check if parent can go to open
			for (Node temp : successor) {
				boolean add = true;
				for (Node comp : open)
					if (comp.x == temp.x && comp.y == temp.y && comp.f < temp.f) {
						add = false;
						continue;
					}
				for (Node comp : closed)
					if (comp.x == temp.x && comp.y == temp.y && comp.f < temp.f) {
						add = false;
						continue;
					}
				if (add) {
					open.add(temp);
				}
			}
			successor.clear();
			closed.add(q);
		}

		// finding the most effective point next to the end
		double minF = Double.MAX_VALUE;
		for (Node comp : closed) {
			if (((comp.x + 1 == DX && comp.y == DY) || (comp.x - 1 == DX && comp.y == DY)
					|| (comp.x == DX && comp.y + 1 == DY) || (comp.x == DX && comp.y - 1 == DY)) && comp.f < minF) {
				minF = comp.f;
				q = comp;
			}
		}

		// from destination node track via parents back to spawn
		while (q.p != null) {
			pathNodes.add(q);
			q = q.p;
		}
		// turn node list to desired integer list
		for (int i = pathNodes.size() - 1; i >= 0; i--) {
			path.add(pathNodes.get(i).x);
			path.add(pathNodes.get(i).y);
		}
		path.add(DX);
		path.add(DY);
		return path;
	}

	/**
	 * starts the execution of A* does not include spawn, but includes destination
	 * 
	 * @param m
	 *            the current 3D map
	 * @param s
	 *            the vector pointing to the spawn
	 * @param d
	 *            the vector pointing to the destination
	 * @return the path found by the algorithm
	 */
	public static List<Integer> startAStar(int[][] m, Vector2f d, Vector2f s) {
		// convert input variables to integers
		int sx = (int) s.getX();
		int sy = (int) s.getY();
		DX = (int) d.getX();
		DY = (int) d.getY();
		MAP = m;

		// create and return path
		return aStar(sx, sy);
	}
	
	/**
	 * Setters and getters for deaths at a certain position
	 * 
	 * @param x
	 *            coordinate of the place where you want to set/get
	 * @param y
	 *            coordinate of the place where you want to set/get
	 */
	public static void upDeaths(int x, int y) {
		DEATHS[x][y]++;
	}

	public static int getDeaths(int x, int y) {
		return DEATHS[x][y];
	}
}
