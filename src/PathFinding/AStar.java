package PathFinding;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import Map.Map;

/**
 * Finds a path for the enemies to follow using A*
 * 
 * @author Chiel Ton
 *
 */
public class AStar {
	private static int DX;// end point x coordinate
	private static int DY;// end point y coordinate
	private static int[][] MAP;// 2D version of current map

	/**
	 * Calculates the heuristic value for the given node (Manhattan distance)
	 * 
	 * @param a
	 *            Node for which the h value must be calculated
	 * @return h
	 */
	public static double calculateH(int x, int y) {
		// calculate manhattan distance
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
				temp.g = q.g + 1;
				temp.h = Math.abs(temp.x - DX) + Math.abs(temp.y - DY);
				temp.f = temp.g + temp.h;

				boolean add = true;
				for (Node comp : open)
					if (comp.x == temp.x && comp.y == temp.y && comp.f < temp.f)
						add = false;
				for (Node comp : closed)
					if (comp.x == temp.x && comp.y == temp.y && comp.f < temp.f)
						add = false;
				if (add) {
					open.add(temp);
				}
			}
			successor.clear();
			closed.add(q);
		}
		//finding the most effective point next to the end
		double minF = Double.MAX_VALUE;
		for (Node comp : closed) {
			if(comp.x+1 == DX && comp.y == DY && comp.f < minF) {
				minF = comp.f;
				q = comp;
			}
			if(comp.x-1 == DX && comp.y == DY && comp.f < minF) {
				minF = comp.f;
				q = comp;
			}
			if(comp.x == DX && comp.y+1 == DY && comp.f < minF) {
				minF = comp.f;
				q = comp;
			}
			if(comp.x == DX && comp.y-1 == DY && comp.f < minF) {
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
			Node current = pathNodes.get(i);
			path.add(current.x);
			path.add(current.y);
		}
		path.add(DX);
		path.add(DY);
		return path;
	}

	/**
	 * starts the execution of A*
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

		Map.print2D(MAP);

		// create and return path
		// does not include spawn, but includes destination
		return aStar(sx, sy);
	}
}
