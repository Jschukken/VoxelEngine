package PathFinding;

import Map.Map;

/*
 * Manages enemies pathfinding
 */
public class AStar {
	/*
	 * Given requested spawn point and map, calculates path
	 * returns path: 1 indicates up 2 indicates down 3 indicates lef 4 indicates right from current position
	 * in order to progress to next position
	 */
	public int[] createPath(int sx, int sy, int[][][] map) {
		int[] path = new int[map.length*10];		//holds the path
		
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
				successor.h = distance from goal to successor
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
		
		return path;
	}
}
