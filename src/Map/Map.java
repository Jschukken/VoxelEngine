package Map;
import java.util.concurrent.ThreadLocalRandom;

/**
 * The map generation
 * 
 * @author Chiel Ton
 *
 */

public class Map {
	public static final int SIZE = 20;       	//x and y size of the map
    public static final int HEIGHT = 5;      	//z size of the map
    public static final int THRESHOLD = 80;  	//threshold for randomly turning 0's into 1's after initial path generation
    private int[][] m;							//map currently being worked on
    
    public void lee (int k, int x, int y, boolean finished){
    	//return once destination has been reached
    	if(m[x][y] == -2 || m[x][y] == -5 || finished) {
    		finished = true;
    		return;
    	}
    	
    	//Check for edge, go down
    	if(x+1 < SIZE)
        {
            if(m[x+1][y] > k+1)
            {
                m[x+1][y] = k+1;
                lee(k+1, x+1, y, finished);
            }
        }
         
    	//Check for edge, go up
        if(x-1 > 0)
        {
            if(m[x-1][y] > k+1)
            {
                m[x-1][y] = k+1;
                lee(k+1, x-1, y, finished);
            }
        }
     
        //Check for edge, go right
        if(y+1 < SIZE)
        {
            if(m[x][y+1] > k+1)
            {
                m[x][y+1] = k+1;
                lee(k+1, x, y+1, finished);
            }
        }
     
        //Check for edge, go left
        if(y-1 > 0)
        {
            if(m[x][y-1] > k+1)
            {
                m[x][y-1] = k+1;
                lee(k+1, x, y-1, finished);
            }
        }
    }
    
    public void leeBack (int k, int x, int y){
    	//set current point as part of the path
    	m[x][y] = -5;
    	
    	//Check for edge, go down
    	if(x+1 < SIZE)
        {
            if(m[x+1][y] == k-1)
            {
                leeBack(k-1, x+1, y);
                return;
            }
        }
         
    	//Check for edge, go up
        if(x-1 > 0)
        {
            if(m[x-1][y] == k-1)
            {
                leeBack(k-1, x-1, y);
                return;
            }
        }
     
        //Check for edge, go right
        if(y+1 < SIZE)
        {
            if(m[x][y+1] == k-1)
            {
                leeBack(k-1, x, y+1);
                return;
            }
        }
     
        //Check for edge, go left
        if(y-1 > 0)
        {
            if(m[x][y-1] == k-1)
            {
                leeBack(k-1, x, y-1);
                return;
            }
        }
    }
    
    /*
     * Add in the initial paths
     */
    public void initial () {
        int d = 0;
        int x = 0;
        int y = 0;
        int sx = 0;
        int sy = 0;
        
        for(int i = 0; i < m.length; i++)for(int j = 0; j < m[0].length; j++)if(m[i][j]==-3){
        	sx = i;
        	sy = j;
            extras(THRESHOLD);
            for(int k = 0; k < m.length; k++)for(int l = 0; l < m[0].length; l++)if(m[k][l] == 0) m[k][l]=200;
            lee(0, sx, sy, false);
            for(int k = 0; k < m.length; k++)for(int l = 0; l < m[0].length; l++)if(m[k][l] > d && m[k][l] != 200) {
            	d = m[k][l];
            	x = k;
            	y = l;
            }
            leeBack(d, x, y);
            for(int k = 0; k < m.length; k++)for(int l = 0; l < m[0].length; l++)if(m[k][l] > 0)
            	m[k][l] = 0;
        }
        for(int k = 0; k < m.length; k++)for(int l = 0; l < m[0].length; l++)if(m[k][l] == -5)
        	m[k][l] = -1;
    }
    
    /*
     * takes out a part of the loose ends
     */
    public void removal () {
        //define a map with an extra 0 border
        int[][] lmap = new int[SIZE+2][SIZE+2];
        for(int i = 0; i < m.length; i++)for(int j = 0; j < m[0].length; j++)lmap[i+1][j+1]=m[i][j];
        
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
        for(int i = 1; i < lmap.length-1; i++)for(int j = 1; j < lmap[0].length-1; j++)m[i-1][j-1]=lmap[i][j];
    }
    
    /*
     * add in extra possible path
     */
    public void extras (int threshold) {
        //all empty points get set to a random number
        for(int i = 0; i < m.length; i++)for(int j = 0; j < m[0].length; j++)if(m[i][j] >= 0)
            m[i][j] = ThreadLocalRandom.current().nextInt(0, 100);
        //if number above threshold set to path else to non-path
        for(int i = 0; i < m.length; i++)for(int j = 0; j < m[0].length; j++){
            if(m[i][j] >= threshold) m[i][j] = -1;
            else if(m[i][j] >= 0) m[i][j] = 0;
        }
    }
    
    /*
     * print the 2D map
     */
    public void print2D() {
    	//set everything to positive for readability
        for(int k = 0; k < m.length; k++)for(int l = 0; l < m[0].length; l++)if(m[k][l] < 0)
        	m[k][l] = Math.abs(m[k][l]);
        
        //print
        for(int i = 0; i < m.length; i++){
            for(int j = 0; j < m.length; j++) {
            	System.out.print(m[i][j]);
            	System.out.print("\t");
            }
            System.out.println();
        }
    }
    
    /*
     * print the 3D map in layers
     */
    public void print3D(int[][][] map) {
        for(int k = 0; k < HEIGHT; k++){
            for(int i = 0; i < map.length; i++){
                for(int j = 0; j < map[0].length; j++) {
                	System.out.print(map[i][j][k]);
                	System.out.print("\t");
                }
                System.out.println();
            }
            System.out.println();
        }
    }
    
    /*
     * creates a Map
     */
    public int[][][] createMap(){
    	//initialize 2D map
    	m = new int[SIZE][SIZE];
        m[ThreadLocalRandom.current().nextInt(0, SIZE)][ThreadLocalRandom.current().nextInt(0, SIZE)] = -2;
        for(int i = 0; i < ThreadLocalRandom.current().nextInt(1, 6); i++){
            m[ThreadLocalRandom.current().nextInt(0, SIZE)][ThreadLocalRandom.current().nextInt(0, SIZE)] = -3;
        }
        
        //fill map
        initial();
        extras(THRESHOLD);
        removal();
        for(int i = 0; i < m.length; i++)for(int j = 0; j < m[0].length; j++)if(m[i][j] < 0)
        	m[i][j] = Math.abs(m[i][j]);
        
        //turn to 3D (no height map or walls)
        int[][][] map3D = new int[SIZE][SIZE][HEIGHT];
        for(int i = 0; i < m.length; i++)for(int j = 0; j < m[0].length; j++) map3D[i][j][0] = m[i][j];
        return map3D;
    }
    
    public int[][][] createGoodMap(){
    	boolean good = false;
    	int[][][] map = new int[SIZE][SIZE][HEIGHT];
    	
    	while(!good) {
    		map = createMap();
    		//good = kNearest(evaluation(m));
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
