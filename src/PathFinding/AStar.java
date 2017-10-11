package PathFinding;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.vector.Vector3f;

import Map.Map;

/*
 * Manages enemies pathfinding
 */
public class AStar {
	private static int DX;
	private static int DY;
	private static int DZ;
	private static int SIZE;
	private static int[][][] MAP;
	
	/*
	 * Given two points, calculates path between these two points
	 * return path is given as an integer array: xyzxyzxyzxyzxyzxyz...
	 * Uses the AStar method
	 */
	public static int[] aStar(int sx, int sy, int sz) {
		List<Integer> path = new ArrayList<>();
		List<Integer> open = new ArrayList<>(); 	// open list
		List<Integer> closed = new ArrayList<>(); 	// closed list
		
		open.add(sx);
		open.add(sy);
		open.add(sz);
		
		while(open.size()!=0) {
			int[] q;
		}		
		/*
		init open;
		init closed;
		open.add(sx,sy);
		
		while(open.size!=0) {
			q = node with leastt f;
			open.remove q
			obtainq's succesor and set their parents to q;
			for(all succesors) {
				successor.g = q.g + distance between successor and q
				successor.h = distance from goal to succ/essor
				successor.f = successor.g + successor.h
				       
				if (node with same position as successor in open && node.f < successor.f)
				   	skip this successor;
				else if (node with same position as successor in closed && node.f < successor.f)
				   	skip this successor;
				else
				    add node to open;
			}
			closed.add q
		}
		*/
		//convert to int array and return
		int[] ret = new int[path.size()];
	    for (int i=0; i < ret.length; i++)
	    {
	        ret[i] = path.get(i).intValue();
	    }
		return ret;
	}
}
