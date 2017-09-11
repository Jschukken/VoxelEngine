package GameEngine;

import org.lwjgl.util.vector.Vector3f;

import Flashlight.MainGameLoop;

public class CollisionHandler {

	
	public static boolean checkPlayerCollision(Vector3f position){
		
		try{
			if(MainGameLoop.map[(int)position.x][(int)position.z][(int)position.y] == 1){
				return true;
			}else if(MainGameLoop.map[(int)position.x+1][(int)position.z][(int)position.y] == 1){
				return true;
			}else if(MainGameLoop.map[(int)position.x+1][(int)position.z+1][(int)position.y] == 1){
				return true;
			}else if(MainGameLoop.map[(int)position.x][(int)position.z+1][(int)position.y] == 1){
				return true;
			}
			else{
				return false;
			}
		}catch(ArrayIndexOutOfBoundsException e){
			return false;
		}
	}
}
