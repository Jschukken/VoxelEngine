package Map;
import java.util.concurrent.ThreadLocalRandom;

/**
 * The map generation
 * 
 * @author Chiel Ton
 *
 */

public class Map {
	public static final int SIZE = 20;       //x and y size of the map
    public static final int HEIGHT = 5;      //z size of the map
    public static final int THRESHOLD = 80;  //threshold for randomly turning 0's into 1's after initial path generation
    
    public void lee (int[][] m, int k, int x, int y){
    	//return once destination has been reached
    	if(m[x][y] == -2) return;
    	
    	//Check for edge, go down
    	if(x+1 < SIZE)
        {
            if(m[x+1][y] > k+1)
            {
                m[x+1][y] = k+1;
                lee(m, k+1, x+1, y);
            }
        }
         
    	//Check for edge, go up
        if(x-1 > 0)
        {
            if(m[x-1][y] > k+1)
            {
                m[x-1][y] = k+1;
                lee(m, k+1, x-1, y);
            }
        }
     
        //Check for edge, go right
        if(y+1 < SIZE)
        {
            if(m[x][y+1] > k+1)
            {
                m[x][y+1] = k+1;
                lee(m, k+1, x, y+1);
            }
        }
     
        //Check for edge, go left
        if(y-1 > 0)
        {
            if(m[x][y-1] > k+1)
            {
                m[x][y-1] = k+1;
                lee(m, k+1, x, y-1);
            }
        }
    }
    
    public void leeBack (int[][] m, int k, int x, int y){
    	m[x][y] = -1;
    	if(x+1 < SIZE)
        {
            if(m[x+1][y] == k-1)
            {
                leeBack(m, k-1, x+1, y);
            }
        }
         
    	//Check for edge, go up
        if(x-1 > 0)
        {
            if(m[x-1][y] == k-1)
            {
                leeBack(m, k-1, x-1, y);
            }
        }
     
        //Check for edge, go right
        if(y+1 < SIZE)
        {
            if(m[x][y+1] == k-1)
            {
                leeBack(m, k-1, x, y+1);
            }
        }
     
        //Check for edge, go left
        if(y-1 > 0)
        {
            if(m[x][y-1] == k-1)
            {
                leeBack(m, k-1, x, y-1);
            }
        }
    }
    
    /*
     * Add in the initial paths
     */
    public int[][] initial (int[][] map) {
        int dx = 0;
        int dy = 0;
        int d = 0;
        int x = 0;
        int y = 0;
        int sx = 0;
        int sy = 0;
        
        for(int i = 0; i < map.length; i++)for(int j = 0; j < map[0].length; j++)if(map[i][j]==-2){
            dx = i;
            dy = j;
        }
        for(int i = 0; i < map.length; i++)for(int j = 0; j < map[0].length; j++)if(map[i][j]==-3){
        	sx = i;
        	sy = j;
            map = extras(map, THRESHOLD);
            for(int k = 0; k < map.length; k++)for(int l = 0; l < map[0].length; l++)if(map[k][l] == 0) map[k][l]=200;
            lee(map, 0, sx, sy);
            for(int k = 0; k < map.length; k++)for(int l = 0; l < map[0].length; l++)if(map[k][l] > d) {
            	d=map[k][l];
            	x = k;
            	y = l;
            }
            leeBack(map, d, x, y);
            for(int k = 0; k < map.length; k++)for(int l = 0; l < map[0].length; l++)if(map[k][l] > 0)
            	map[k][l] = 0;
            for(int k = 0; k < map.length; k++)for(int l = 0; l < map[0].length; l++)if(map[k][l] < 0)
            	map[k][l] = Math.abs(map[k][l]);
            print2D(map);
        }
        return map;
    }
    
    /*
     * takes out the loose ends, at least a part
     */
    public int[][] removal (int[][] map) {
        //define a map with an extra 0 border
        int[][] lmap = new int[SIZE+2][SIZE+2];
        for(int i = 0; i < map.length; i++)for(int j = 0; j < map[0].length; j++)lmap[i+1][j+1]=map[i][j];
        
        //remove everything that has at most one neighbor, if something is found, repeat
        boolean removed = true;
        while(removed){
            removed = false;
            for(int i = 1; i < lmap.length-1; i++)for(int j = 1; j < lmap[0].length-1; j++)if(lmap[i][j]==-1){
                if(lmap[i][j+1]==0 && lmap[i][j-1]==0 && lmap[i+1][j]==0){
                    lmap[i][j] = 0;
                    removed = true;
                }
                else if(lmap[i][j-1]==0 && lmap[i+1][j]==0 && lmap[i-1][j]==0){
                    lmap[i][j] = 0;
                    removed = true;
                }
                else if(lmap[i][j+1]==0 && lmap[i+1][j]==0 && lmap[i-1][j]==0){
                    lmap[i][j] = 0;
                    removed = true;
                }
                else if(lmap[i][j+1]==0 && lmap[i][j-1]==0 && lmap[i-1][j]==0){
                    lmap[i][j] = 0;
                    removed = true;
                }
            }
        }
        
        //set map back to original size and return
        for(int i = 1; i < lmap.length-1; i++)for(int j = 1; j < lmap[0].length-1; j++)map[i-1][j-1]=lmap[i][j];
        return map;
    }
    
    /*
     * add in extra possible path
     */
    public int[][] extras (int[][] map, int threshold) {
        //all non-path get set to a random number
        for(int i = 0; i < map.length; i++)for(int j = 0; j < map[0].length; j++)if(map[i][j] >= 0)
            map[i][j] = ThreadLocalRandom.current().nextInt(0, 100);
        //if number above threshold set to path else to non-path
        for(int i = 0; i < map.length; i++)for(int j = 0; j < map[0].length; j++){
            if(map[i][j] >= threshold) map[i][j] = -1;
            else if(map[i][j] >= 0) map[i][j] = 0;
        }
        return map;
    }
    
    /*
     * print the 2D map
     */
    public void print2D(int[][] map) {
        for(int i = 0; i < map.length; i++){
            for(int j = 0; j < map.length; j++)System.out.print(map[i][j]);
            System.out.println();
        }
    }
    
    /*
     * print the 3D map in layers
     */
    public void print3D(int[][][] map) {
        for(int k = 0; k < HEIGHT; k++){
            for(int i = 0; i < map.length; i++){
                for(int j = 0; j < map[0].length; j++)System.out.print(map[i][j][k]);
                System.out.println();
            }
            System.out.println();
        }
    }
    
    /*
     * creates a Map
     */
    public int[][][] createMap(){
    	int[][] map = new int[SIZE][SIZE];
        map[ThreadLocalRandom.current().nextInt(0, SIZE)][ThreadLocalRandom.current().nextInt(0, SIZE)] = -2;
        for(int i = 0; i < ThreadLocalRandom.current().nextInt(1, 6); i++){
            map[ThreadLocalRandom.current().nextInt(0, SIZE)][ThreadLocalRandom.current().nextInt(0, SIZE)] = -3;
        }
        
        //fill map
        map = removal(extras(initial(map), THRESHOLD));
        for(int i = 0; i < map.length; i++)for(int j = 0; j < map[0].length; j++)if(map[i][j] < 0)
        	map[i][j] = Math.abs(map[i][j]);
        
        //turn to 3D (height map not applied
        int[][][] map3D = new int[SIZE][SIZE][HEIGHT];
        for(int i = 0; i < map.length; i++)for(int j = 0; j < map[0].length; j++) map3D[i][j][0] = map[i][j];
        return map3D;
    }
    
    public int[][][] createGoodMap(){
    	boolean good = false;
    	int[][][] map = new int[SIZE][SIZE][HEIGHT];
    	
    	while(!good) {
    		map = createMap();
    		//good = kNearest(evaluation(map));
    		good = true;
    	}
    	return map;
    }
    
    /*
     * run option to test
     */
    public static void main(String[] args) {
    (new Map()).createGoodMap();
  }
}
