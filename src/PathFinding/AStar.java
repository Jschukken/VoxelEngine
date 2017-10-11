package PathFinding;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.vector.Vector3f;

import Map.Map;

/**
 * Finds a path for the enemies to follow using A*
 * 
 * @author Chiel Ton
 *
 */
public class AStar {
	private static int DX;
	private static int DY;
	private static int DZ;
	private static int SIZE;
	private static int[][][] MAP;

	/**
	 * Returns the index of the minimal f entry
	 * @param list, the list from which the node with minimal F will be selected
	 * @return the 
	 */
	private static int minimalF(List<Node> list) {
		int min = Integer.MAX_VALUE;
		int index = -1;
		
		while(list.size() != 0) {
			Node current = list.get(0);
			if (current.f < min)
				min = current.f;
			list.remove(0);
		}
		
		return index;
	}

	/**
	 * =Given two points, calculates path between these two points return path is
	 * given as an integer array: xyzxyzxyzxyzxyzxyz... Uses the AStar method
	 * 
	 * @param sx
	 *            spawn x point
	 * @param sy
	 *            spawn y point
	 * @param sz
	 *            spawn z point
	 * @return
	 */
	private static List<Integer> aStar(int sx, int sy, int sz) {
		List<Integer> path = new ArrayList<>();
		List<Node> open = new ArrayList<>();
		List<Node> closed = new ArrayList<>();

		int g = 0;
		int h = Math.abs(sx - DX) + Math.abs(sy - DY) + Math.abs(sz - DZ);
		open.add(new Node(sx, sy, sz, g, h));

		while (open.size() != 0) {
			Node q = open.get(minimalF(open));
			open.remove(q);
		}
		/*
		obtainq's
		 * succesor and set their parents to q; for(all succesors) { successor.g = q.g +
		 * distance between successor and q successor.h = distance from goal to
		 * succ/essor successor.f = successor.g + successor.h
		 * 
		 * if (node with same position as successor in open && node.f < successor.f)
		 * skip this successor; else if (node with same position as successor in closed
		 * && node.f < successor.f) skip this successor; else add node to open; }
		 * closed.add q }
		 */
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
	public static List<Integer> startLee(int[][][] m, Vector3f s, Vector3f d) {
		// convert input variables to integers
		int sx = (int) s.getX();
		int sy = (int) s.getY();
		int sz = (int) s.getZ();
		DX = (int) s.getX();
		DY = (int) s.getY();
		DX = (int) s.getZ();
		MAP = m;

		List<Integer> path = aStar(sx, sy, sz);

		return path;
	}
}
