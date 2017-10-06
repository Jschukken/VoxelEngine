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
	
	/*
     * fill the map using lee's algorithm
     */
    public static void lee3D (int k, int x, int y, int z, boolean finished){
    	//return once destination or another path to it has been reached
    	if((DX == x && DY == y && DZ == z) || finished) {
    		finished = true;
    		return;
    	}
    	
    	if(x+1 < MAP.length) {
            if(MAP[x+1][y][z+1] > k+1) {
                MAP[x+1][y][z+1] = k+1;
                lee3D(k+1, x+1, y, z+1, finished);
            }
            else if(MAP[x+1][y][z] > k+1) {
                MAP[x+1][y][z] = k+1;
                lee3D(k+1, x+1, y, z, finished);
            }
            else if(z-1 > 0) if(MAP[x+1][y][z-1] > k+1){
                MAP[x+1][y][z-1] = k+1;
                lee3D(k+1, x+1, y, z-1, finished);            	
            }
        }
        if(x-1 >= 0) {
        	if(MAP[x-1][y][z+1] > k+1) {
                MAP[x-1][y][z+1] = k+1;
                lee3D(k+1, x-1, y, z+1, finished);
            }
            else if(MAP[x-1][y][z] > k+1) {
                MAP[x-1][y][z] = k+1;
                lee3D(k+1, x-1, y, z, finished);
            }
            else if(z-1 > 0) if(MAP[x-1][y][z-1] > k+1){
                MAP[x-1][y][z-1] = k+1;
                lee3D(k+1, x-1, y, z-1, finished);            	
            }
        }
        if(y+1 < SIZE) {
        	if(MAP[x][y+1][z+1] > k+1) {
                MAP[x][y+1][z+1] = k+1;
                lee3D(k+1, x, y+1, z+1, finished);
            }
            else if(MAP[x][y+1][z] > k+1) {
                MAP[x][y+1][z] = k+1;
                lee3D(k+1, x, y+1, z, finished);
            }
            else if(z-1 > 0) if(MAP[x][y+1][z-1] > k+1){
                MAP[x+1][y+1][z-1] = k+1;
                lee3D(k+1, x, y+1, z-1, finished);            	
            }
        }
        if(y-1 >= 0) {
        	if(MAP[x][y-1][z+1] > k+1) {
                MAP[x][y-1][z+1] = k+1;
                lee3D(k+1, x, y-1, z+1, finished);
            }
            else if(MAP[x][y-1][z] > k+1) {
                MAP[x][y-1][z] = k+1;
                lee3D(k+1, x, y-1, z, finished);
            }
            else if(z-1 > 0) if(MAP[x][y-1][z-1] > k+1){
                MAP[x][y-1][z-1] = k+1;
                lee3D(k+1, x, y-1, z-1, finished);            	
            }
        }
        System.out.println("no end point found");
    }
    
    /*
     * Backtrack through the filled matrix to find the shortest path
     */
    public static void leeBack3D (List<Integer> path, int k, int x, int y, int z){
    	//set current point as part of the path
    	path.add(x);
    	path.add(y);
    	path.add(z);
    	
    	//try all possible options to find a step down in the matrix
    	if(x+1 < SIZE) {
    		if(MAP[x+1][y][z+1] == k-1) {
                leeBack3D(path, k-1, x+1, y, z+1);
                return;
            }
            else if(MAP[x+1][y][z] == k-1) {
            	leeBack3D(path, k-1, x+1, y, z);
            	return;
            }
            else if(z-1 > 0) if(MAP[x+1][y][z-1] == k-1){
            	leeBack3D(path, k-1, x+1, y, z-1);
            	return;    	
            }
        }
        if(x-1 > 0) {
        	if(MAP[x-1][y][z+1] == k-1) {
                leeBack3D(path, k-1, x-1, y, z+1);
                return;
            }
            else if(MAP[x-1][y][z] == k-1) {
            	leeBack3D(path, k-1, x-1, y, z);
            	return;
            }
            else if(z-1 > 0) if(MAP[x-1][y][z-1] == k-1){
            	leeBack3D(path, k-1, x-1, y, z-1);
            	return;    	
            }
        }
        if(y+1 < SIZE) {
        	if(MAP[x][y+1][z+1] == k-1) {
                leeBack3D(path, k-1, x, y+1, z+1);
                return;
            }
            else if(MAP[x][y+1][z] == k-1) {
            	leeBack3D(path, k-1, x, y+1, z);
            	return;
            }
            else if(z-1 > 0) if(MAP[x][y+1][z-1] == k-1){
            	leeBack3D(path, k-1, x, y+1, z-1);
            	return;    	
            }
        }
        if(y-1 > 0) {
        	if(MAP[x][y-1][z+1] == k-1) {
                leeBack3D(path, k-1, x, y-1, z+1);
                return;
            }
            else if(MAP[x][y-1][z] == k-1) {
            	leeBack3D(path, k-1, x, y-1, z);
            	return;
            }
            else if(z-1 > 0) if(MAP[x][y-1][z-1] == k-1){
            	leeBack3D(path, k-1, x, y-1, z-1);
            	return;    	
            }
        }
        System.out.println("no path found");
    }
    
	/*
	 * Given two points, calculates path between these two points
	 * return path is given as an integer array with repeating x y z
	 * Uses Lee's algorithm method
	 */
	public static int[] createPath(int[][][] m, Vector3f s, Vector3f d) {
		// convert input variables to integers
		int sx = (int) s.getX();
		int sy = (int) s.getY();
		int sz = (int) s.getZ();
		DX = (int) s.getX();
		DY = (int) s.getY();
		DX = (int) s.getZ();
		MAP = m;
		SIZE = MAP.length;

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
		List<Integer> path = new ArrayList<>();
		leeBack3D(path, e, x, y, z);
		// set matrix back to normal
		for (int i = 0; i < MAP.length; i++)
			for (int j = 0; j < MAP[0].length; j++)
				for(int k = 0; k < MAP[0][0].length; k++)
				if (MAP[i][j][k] > 0 || MAP[i][j][k] == -5)
					m[i][j][k] = 0;
		
		//converts arrayList into array and returns
		int[] ret = new int[path.size()];
	    for (int i=0; i < ret.length; i++)
	    {
	        ret[i] = path.get(i).intValue();
	    }
		return ret;
	}
}
