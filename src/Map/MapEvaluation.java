package Map;

import java.util.Arrays;
/**
 *
 * @author s141010
 */
public class MapEvaluation {

    int[][][] map = 
    {  { {1,   2,  3}, { 4,  5,  6}, { 7,  8,  9} },
       { {10, 11, 12}, {13, 14, 15}, {16, 17, 18} },
       { {19, 20, 21}, {22, 23, 24}, {25, 26, 27} } };
	
	public int[] characteristics () {
        int[] characteristics = new int [3];
		/*
	     * distance between beginning and end
	     */
        int dx = 0;  //destination x coordinate
        int dy =0;  //destination y coordinate
        int dz=0;  //destination y coordinate
        int sx = 0;  //current spawn x coordinate
        int sy=0;  //current spawn y coordinate
        int sz=0;  //current spawn z coordinate
        int distance;
        int pathtile = 0;
        int pathnr=0;
        int sidepath = 0;
        int options =0;
		for(int i = 0; i < map.length; i++)for(int j = 0; j < map[0].length; j++)for(int k = 0; k < map[0][0].length; k++)if(map[i][j][k]==-2){
	        dx = i;
	        dy = j;
	        dz = k;
	    }
		for(int i = 0; i < map.length; i++)for(int j = 0; j < map[0].length; j++)for(int k = 0; k < map[0][0].length; k++)if(map[i][j][k]==-3){
	        sx = i;
	        sy = j;
	        sz = k;
	    }
		distance = Math.abs(dx-sx)+Math.abs(dy-sy)+Math.abs(dz-sz);
		characteristics[0]= distance;
		
        /*
         * amount of path tiles
         */
        for (int[][] map1 : map) {
            for (int j = 0; j < map[0].length; j++) {
                for (int k = 0; k < map[0][0].length; k++) {
                    if (map1[j][k] == 1) {
                        pathtile++;
                    }
                }
            }
        }
		characteristics[1]= pathtile;
		
            /*
	     * amount of paths (by checking for junctions)
	     */
		for(int i = 0; i < map.length; i++)for(int j = 0; j < map[0].length; j++)for(int k = 0; k < map[0][0].length; k++){
                    if(i==map.length-1 && j==map[0].length-1 && k== map[0][0].length-1 ){
                        if(map[i-1][j][k]==1) {
				sidepath++;}
			if(map[i-1][j][k-1]==1) {
				sidepath++;}
			if(map[i][j-1][k]==1) {
				sidepath++;}
			if(map[i][j-1][k-1]==1) {
				sidepath++;}	
                        }else if(i==0 && j==0 && k==map[0][0].length-1){
                        if(map[i+1][j][k]==1){
				sidepath++;}
			if(map[i+1][j][k-1]==1) {
				sidepath++;}
			if(map[i][j+1][k]==1) {
				sidepath++;}
			if(map[i][j+1][k-1]==1) {
				sidepath++;}
                    }else if(i==0 && j==map[0].length-1 && k==0){
                        if(map[i+1][j][k]==1){
				sidepath++;}
			if(map[i+1][j][k+1]==1) {
				sidepath++;}
			if(map[i][j-1][k]==1) {
				sidepath++;}
			if(map[i][j-1][k+1]==1) {
				sidepath++;}	
                    }else if(i==map.length-1 && j==0 && k==0){
			if(map[i][j+1][k]==1) {
				sidepath++;}
			if(map[i][j+1][k+1]==1) {
				sidepath++;}
			if(map[i-1][j][k]==1) {
				sidepath++;}
			if(map[i-1][j][k+1]==1) {
				sidepath++;}
                    }else if(i== map.length-1 && j==map[0].length-1 && k==0){
                        if(map[i-1][j][k]==1) {
				sidepath++;}
			if(map[i-1][j][k+1]==1) {
				sidepath++;}
			if(map[i][j-1][k]==1) {
				sidepath++;}
			if(map[i][j-1][k+1]==1) {
				sidepath++;}	
                    }else if(i==map.length-1 && j==0 && k==map[0][0].length-1){
                        if(map[i][j+1][k]==1) {
				sidepath++;}
			if(map[i][j+1][k-1]==1) {
				sidepath++;}
			if(map[i-1][j][k]==1) {
				sidepath++;}
			if(map[i-1][j][k-1]==1) {
				sidepath++;}
                    }else if(i==0 && j==map[0].length-1 && k==map[0][0].length-1 ){
                        if(map[i+1][j][k]==1){
				sidepath++;}
			if(map[i+1][j][k-1]==1) {
				sidepath++;}
                        if(map[i][j-1][k]==1) {
				sidepath++;}
			if(map[i][j-1][k-1]==1) {
				sidepath++;}	
                    }else if(i==0 && j==0 && k==0){
                        if(map[i+1][j][k]==1){
                                    sidepath++;}
                        if(map[i+1][j][k+1]==1) {
                                    sidepath++;}
                        if(map[i][j+1][k]==1) {
                                    sidepath++;}
                        if(map[i][j+1][k+1]==1) {
                                    sidepath++;}
                    }else if(i==0 && j==0){
                            if(map[i+1][j][k-1]==1){
                                    sidepath++;}
                            if(map[i+1][j][k]==1){
                                    sidepath++;}
                            if(map[i+1][j][k+1]==1) {
                                    sidepath++;}
                            if(map[i][j+1][k]==1) {
                                    sidepath++;}
                            if(map[i][j+1][k-1]==1) {
                                    sidepath++;}
                            if(map[i][j+1][k+1]==1) {
                                    sidepath++;}
                    }else if(i==0 && k==0){
                            if(map[i+1][j][k]==1){
                                    sidepath++;}
                            if(map[i+1][j][k+1]==1) {
                                    sidepath++;}
                            if(map[i][j+1][k]==1) {
                                    sidepath++;}
                            if(map[i][j+1][k+1]==1) {
                                    sidepath++;}
                            if(map[i][j-1][k]==1) {
				sidepath++;}
                            if(map[i][j-1][k+1]==1) {
				sidepath++;}
                    }else if(j==0 && k==0){
                            if(map[i+1][j][k]==1){
                                    sidepath++;}
                            if(map[i+1][j][k+1]==1) {
                                    sidepath++;}
                            if(map[i][j+1][k]==1) {
                                    sidepath++;}
                            if(map[i][j+1][k+1]==1) {
                                    sidepath++;}
                            if(map[i-1][j][k]==1) {
				sidepath++;}
                            if(map[i-1][j][k+1]==1) {
				sidepath++;}
                    }else if(i==map.length-1 && j==map[0].length-1){
                        if(map[i-1][j][k]==1) {
				sidepath++;}
			if(map[i-1][j][k-1]==1) {
				sidepath++;}
			if(map[i-1][j][k+1]==1) {
				sidepath++;}
			if(map[i][j-1][k]==1) {
				sidepath++;}
			if(map[i][j-1][k-1]==1) {
				sidepath++;}
			if(map[i][j-1][k+1]==1) {
				sidepath++;}	
                    }else if(j==map[0].length-1 && k== map[0][0].length-1 ){
                        if(map[i+1][j][k]==1){
				sidepath++;}
			if(map[i+1][j][k-1]==1) {
				sidepath++;}
			if(map[i-1][j][k]==1) {
				sidepath++;}
			if(map[i-1][j][k-1]==1) {
				sidepath++;}
			if(map[i][j-1][k]==1) {
				sidepath++;}
			if(map[i][j-1][k-1]==1) {
				sidepath++;}
                    }else if(i==map.length-1 && k==map[0][0].length-1){
                        if(map[i][j+1][k]==1) {
				sidepath++;}
			if(map[i][j+1][k-1]==1) {
				sidepath++;}
			if(map[i-1][j][k]==1) {
				sidepath++;}
			if(map[i-1][j][k-1]==1) {
				sidepath++;}
			if(map[i][j-1][k]==1) {
				sidepath++;}
			if(map[i][j-1][k-1]==1) {
				sidepath++;}
                    }else if(i==0 && j== map[0].length-1){
                        if(map[i+1][j][k]==1){
				sidepath++;}
			if(map[i+1][j][k-1]==1) {
				sidepath++;}
			if(map[i+1][j][k+1]==1) {
				sidepath++;}
			if(map[i][j-1][k]==1) {
				sidepath++;}
			if(map[i][j-1][k-1]==1) {
				sidepath++;}
			if(map[i][j-1][k+1]==1) {
				sidepath++;}
                    }else if(i==0 && k== map[0].length-1){
                        if(map[i+1][j][k]==1){
				sidepath++;}
			if(map[i+1][j][k-1]==1) {
				sidepath++;}
			if(map[i][j+1][k]==1) {
				sidepath++;}
			if(map[i][j+1][k-1]==1) {
				sidepath++;}
			if(map[i][j-1][k]==1) {
				sidepath++;}
			if(map[i][j-1][k-1]==1) {
				sidepath++;}
                    }else if(j==0 && i==map.length-1){
                        if(map[i][j+1][k]==1) {
				sidepath++;}
			if(map[i][j+1][k-1]==1) {
				sidepath++;}
			if(map[i][j+1][k+1]==1) {
				sidepath++;}
			if(map[i-1][j][k]==1) {
				sidepath++;}
			if(map[i-1][j][k-1]==1) {
				sidepath++;}
			if(map[i-1][j][k+1]==1) {
				sidepath++;}
                    }else if(j==0 && k==map[0][0].length-1){
                        if(map[i+1][j][k]==1){
				sidepath++;}
			if(map[i+1][j][k-1]==1) {
				sidepath++;}
			if(map[i][j+1][k]==1) {
				sidepath++;}
			if(map[i][j+1][k-1]==1) {
				sidepath++;}
			if(map[i-1][j][k]==1) {
				sidepath++;}
			if(map[i-1][j][k-1]==1) {
				sidepath++;}	
                    }else if(k==0 && i==map.length-1){
                        if(map[i][j+1][k]==1) {
				sidepath++;}
			if(map[i][j+1][k+1]==1) {
				sidepath++;}
			if(map[i-1][j][k]==1) {
				sidepath++;}
			if(map[i-1][j][k+1]==1) {
				sidepath++;}
			if(map[i][j-1][k]==1) {
				sidepath++;}
			if(map[i][j-1][k+1]==1) {
				sidepath++;}
                    }else if(k==0 && j==map[0].length-1){
                        if(map[i+1][j][k]==1){
				sidepath++;}
			if(map[i+1][j][k+1]==1) {
				sidepath++;}
			if(map[i-1][j][k]==1) {
				sidepath++;}
			if(map[i-1][j][k+1]==1) {
				sidepath++;}
			if(map[i][j-1][k]==1) {
				sidepath++;}
			if(map[i][j-1][k+1]==1) {
				sidepath++;}	
                    }else if(i==map.length-1){
                        if(map[i][j+1][k]==1) {
				sidepath++;}
			if(map[i][j+1][k-1]==1) {
				sidepath++;}
			if(map[i][j+1][k+1]==1) {
				sidepath++;}
			if(map[i-1][j][k]==1) {
				sidepath++;}
			if(map[i-1][j][k-1]==1) {
				sidepath++;}
			if(map[i-1][j][k+1]==1) {
				sidepath++;}
			if(map[i][j-1][k]==1) {
				sidepath++;}
			if(map[i][j-1][k-1]==1) {
				sidepath++;}
			if(map[i][j-1][k+1]==1) {
				sidepath++;}
                    }else if(j==map[0].length-1){
                        if(map[i+1][j][k]==1){
				sidepath++;}
			if(map[i+1][j][k-1]==1) {
				sidepath++;}
			if(map[i+1][j][k+1]==1) {
				sidepath++;}
			if(map[i-1][j][k]==1) {
				sidepath++;}
			if(map[i-1][j][k-1]==1) {
				sidepath++;}
			if(map[i-1][j][k+1]==1) {
				sidepath++;}
			if(map[i][j-1][k]==1) {
				sidepath++;}
			if(map[i][j-1][k-1]==1) {
				sidepath++;}
			if(map[i][j-1][k+1]==1) {
				sidepath++;}	
                    }else if(k==map[0][0].length-1){
                        if(map[i+1][j][k]==1){
				sidepath++;}
			if(map[i+1][j][k-1]==1) {
				sidepath++;}
			if(map[i][j+1][k]==1) {
				sidepath++;}
			if(map[i][j+1][k-1]==1) {
				sidepath++;}
			if(map[i-1][j][k]==1) {
				sidepath++;}
			if(map[i-1][j][k-1]==1) {
				sidepath++;}
			if(map[i][j-1][k]==1) {
				sidepath++;}
			if(map[i][j-1][k-1]==1) {
				sidepath++;}
                    
                    }else if(i==0){
                            if(map[i+1][j][k]==1){
				sidepath++;}
                            if(map[i+1][j][k-1]==1) {
                                    sidepath++;}
                            if(map[i+1][j][k+1]==1) {
                                    sidepath++;}
                            if(map[i][j+1][k]==1) {
                                    sidepath++;}
                            if(map[i][j+1][k-1]==1) {
                                    sidepath++;}
                            if(map[i][j+1][k+1]==1) {
                                    sidepath++;}
                            if(map[i][j-1][k]==1) {
                                    sidepath++;}
                            if(map[i][j-1][k-1]==1) {
                                    sidepath++;}
                            if(map[i][j-1][k+1]==1) {
                                    sidepath++;}	
                    }else if(j==0){
                            if(map[i+1][j][k]==1){
				sidepath++;}
                            if(map[i+1][j][k-1]==1) {
                                    sidepath++;}
                            if(map[i+1][j][k+1]==1) {
                                    sidepath++;}
                            if(map[i][j+1][k]==1) {
                                    sidepath++;}
                            if(map[i][j+1][k-1]==1) {
                                    sidepath++;}
                            if(map[i][j+1][k+1]==1) {
                                    sidepath++;}
                            if(map[i-1][j][k]==1) {
                                    sidepath++;}
                            if(map[i-1][j][k-1]==1) {
                                    sidepath++;}
                            if(map[i-1][j][k+1]==1) {
                                    sidepath++;}	
                    }else if (k==0){
                            if(map[i+1][j][k]==1){
                                    sidepath++;}
                            if(map[i+1][j][k+1]==1) {
                                    sidepath++;}
                            if(map[i][j+1][k]==1) {
                                    sidepath++;}
                            if(map[i][j+1][k+1]==1) {
                                    sidepath++;}
                            if(map[i-1][j][k]==1) {
                                    sidepath++;}
                            if(map[i-1][j][k+1]==1) {
                                    sidepath++;}
                            if(map[i][j-1][k]==1) {
                                    sidepath++;}
                            if(map[i][j-1][k+1]==1) {
                                    sidepath++;}
                    } else{
                        if(map[i+1][j][k]==1){
				sidepath++;}
			if(map[i+1][j][k-1]==1) {
				sidepath++;}
			if(map[i+1][j][k+1]==1) {
				sidepath++;}
			if(map[i][j+1][k]==1) {
				sidepath++;}
			if(map[i][j+1][k-1]==1) {
				sidepath++;}
			if(map[i][j+1][k+1]==1) {
				sidepath++;}
			if(map[i-1][j][k]==1) {
				sidepath++;}
			if(map[i-1][j][k-1]==1) {
				sidepath++;}
			if(map[i-1][j][k+1]==1) {
				sidepath++;}
			if(map[i][j-1][k]==1) {
				sidepath++;}
			if(map[i][j-1][k-1]==1) {
				sidepath++;}
			if(map[i][j-1][k+1]==1) {
				sidepath++;}	
                        }
                        								
			if(sidepath>2) {
                            options = options + ((sidepath -2)/2);
			}
			sidepath=0;
		}
		characteristics[2] = options;
                
                System.out.println(Arrays.toString(characteristics));
		return characteristics;
		
		/*
		 * shortest path
		 */
                        
   
    }
         public static void main(String[] args) {
        new MapEvaluation().characteristics();}
    
}
