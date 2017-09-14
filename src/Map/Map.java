package Map;

import java.util.concurrent.ThreadLocalRandom;

public class Map {
    public static final int SIZE = 20;       //x and y size of the map
    public static final int HEIGHT = 5;      //z size of the map
    public static final int THRESHOLD = 30;  //z size of the map
    
    /*
     * Add in the initial paths
     */
    public int[][] initial (int[][] map) {
        int dx = 0;  	//destination x coordinate
        int dy = 0;  	//destination y coordinate
        int sx = 0;		//current spawn x coordinate
        int sy = 0;		//current spawn y coordinate
        
        //save destination
        for(int i = 0; i < map.length; i++)for(int j = 0; j < map[0].length; j++)if(map[i][j]==-2){
            dx = i;
            dy = j;
        }
        //save and build path for each spawn
        for(int i = 0; i < map.length; i++)for(int j = 0; j < map[0].length; j++)if(map[i][j]==-3){
            sx = i;
            sy = j;
            if (dx - sx > 0) for(int k = 1; k < dx - sx; k++) map[sx+k][sy] = -1;
            else for(int k = 1; k < sx - dx; k++) map[sx-k][sy] = -1;
            if (dy - sy > 0) for(int k = 0; k < dy - sy; k++) map[dx][sy+k] = -1;
            else for(int k = 0; k < sy - dy; k++) map[dx][sy-k] = -1;
        }
        return map;
    }
    
    /*
     * takes out the loose ends
     */
    public int[][] removal (int[][] map) {
        //define a map with an extra 0 border
        int[][] lmap = new int[SIZE+2][SIZE+2];
        for(int i = 0; i < map.length; i++)for(int j = 0; j < map[0].length; j++)lmap[i+1][j+1]=map[i][j];
        
        //remove everything that has at most one neighbour, if something is found, repeat
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
     * runs the generation
     */
    public int[][][] run() {
        //initialize map with 0's a destination and a random number of spawns
        int[][] map = new int[SIZE][SIZE];
        map[ThreadLocalRandom.current().nextInt(0, SIZE)][ThreadLocalRandom.current().nextInt(0, SIZE)] = -2;
        for(int i = 0; i < ThreadLocalRandom.current().nextInt(1, 6); i++){
            map[ThreadLocalRandom.current().nextInt(0, SIZE)][ThreadLocalRandom.current().nextInt(0, SIZE)] = -3;
        }
        
        //fill map
        map = removal(extras(initial(map), THRESHOLD));
        for(int i = 0; i < map.length; i++)for(int j = 0; j < map[0].length; j++)if(map[i][j] < 0) map[i][j] = Math.abs(map[i][j]);
        print2D(map);
        
        //turn to 3D (height map not applied
        int[][][] map3D = new int[SIZE][SIZE][HEIGHT];
        for(int i = 0; i < map.length; i++)for(int j = 0; j < map[0].length; j++) map3D[i][j][0] = map[i][j];
        return map3D;
    }
    
    public static void main(String[] args) {
    (new Map()).run();
  }
}

