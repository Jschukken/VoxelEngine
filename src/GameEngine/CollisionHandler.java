package GameEngine;

import org.lwjgl.util.vector.Vector3f;

import Flashlight.MainGameLoop;

public class CollisionHandler {

	
	public static boolean checkCollision(Vector3f position){
		
		try{
			if(MainGameLoop.map[(int)position.x][(int)position.z][(int)position.y] == 0){
				return true;
			}else{
				return false;
			}
		}catch(ArrayIndexOutOfBoundsException e){
			return false;
		}
	}
}
