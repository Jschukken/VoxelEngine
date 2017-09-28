package Map;

import java.util.Arrays;
public class MapEvaluation {

    
	
    public static int[] characteristics (int[][] map) {
    /*
    	int[][] map = 
    	    {  { 1,   -2,  -3,  4,  5,  6,  7,  8,  9 },
    	       { 10, 11, 12, 13, 14, 15, 16, 17, 18 },
    	       { 19, 20, 21, 22, 23, 24, 25, 26, 27 } };
    	       */
    
    int[] characteristics = new int [7];		
    int dx = 0;  //destination x coordinate
    int dy =0;  //destination y coordinate
    int sx = 0;  //current spawn x coordinate
    int sy=0;  //current spawn y coordinate
    int spawns=0; //amount of spawn points
    int distance; // distance from current spawn point to destination
    int maxdist=0; //max distance
    int mindist=50; //min distance
    int meandist=0; //average distance
    int pathtile = 0; //amount of pathtiles
    int sidepath = 0; //amount of side paths from current path tile 
    int deadend =0;
    int routeoption =0; // total amount of route routeoption
        
    /*
     * distance between spawn point and destination
     */
    for(int i = 0; i < map.length; i++)for(int j = 0; j < map[0].length; j++)if(map[i][j]==2){
        dx = i;
        dy = j;
    }
    for(int i = 0; i < map.length; i++)for(int j = 0; j < map[0].length; j++)if(map[i][j]==3){
        sx = i;
        sy = j;
        spawns++;
        distance = Math.abs(dx-sx)+Math.abs(dy-sy);
        if (distance>maxdist){
            maxdist=distance;}
        if (distance<mindist){
            mindist=distance;}
        meandist= meandist+distance;
    }
    meandist=meandist/spawns;
    characteristics[0]= meandist;
    characteristics[1]= maxdist;
    characteristics[2]= mindist;
    characteristics[3]= spawns;
		
    /*
    * amount of path tiles
    */
    for (int i = 0; i < map.length; i++) {
        for (int j = 0; j < map[0].length; j++) {
            if (map[i][j] == 1) {
                pathtile++;}
            }
        }
    characteristics[4]= pathtile;
		
    /*
     * amount of paths (by checking for junctions)
     */
        
    //define a map with an extra 0 border
    int[][] lmap = new int[map.length+2][map[0].length+2];
    for(int i = 0; i < map.length; i++)for(int j = 0; j < map[0].length; j++)lmap[i+1][j+1]=map[i][j];
        
    for(int i = 1; i < lmap.length-1; i++)for(int j = 1; j < lmap[0].length-1; j++)if(lmap[i][j]==1){
        if(lmap[i+1][j]==1){
            sidepath++;}			
        if(lmap[i][j+1]==1) {
            sidepath++;}			
        if(lmap[i-1][j]==1) {
            sidepath++;}				
        if(lmap[i][j-1]==1) {
            sidepath++;}			
    
        if (sidepath==1) deadend++;
        if(sidepath>2) routeoption = routeoption + (sidepath -2);
        sidepath=0;
    }
   
    characteristics[5]=routeoption;
    characteristics[6]=deadend; 
    
    
    
   System.out.println(Arrays.toString(characteristics));
    return characteristics;
		                        
   
    }
}
