package GameEngine;

import org.lwjgl.util.vector.Vector3f;

import Entities.Entity;
import Flashlight.MainGameLoop;

public class CollisionHandler {

	/**
	 * checks to see if the player collides with anything given the players position in the vector map
	 * @param position the position of the player
	 * @return return true if the player collides with anything, return false otherwise
	 */
	public static boolean checkPlayerCollision(Vector3f position) {

		try {
			// lower square
			if (MainGameLoop.map[(int) (position.x + .1)][(int) (position.y + .1)][(int) (position.z + .1)] == 1) {
				return true;
			} else if (MainGameLoop.map[(int) (position.x + .1)][(int) (position.y + .1)][(int) (position.z
					+ .9)] == 1) {
				return true;
			} else if (MainGameLoop.map[(int) (position.x + .9)][(int) (position.y + .1)][(int) (position.z
					+ .9)] == 1) {
				return true;
			} else if (MainGameLoop.map[(int) (position.x + .9)][(int) (position.y + .1)][(int) (position.z
					+ .1)] == 1) {
				return true;
			}
			// middle square
			else if (MainGameLoop.map[(int) (position.x + .1)][(int) (position.y + .9)][(int) (position.z + .1)] == 1) {
				return true;
			} else if (MainGameLoop.map[(int) (position.x + .1)][(int) (position.y + .9)][(int) (position.z
					+ .9)] == 1) {
				return true;
			} else if (MainGameLoop.map[(int) (position.x + .9)][(int) (position.y + .9)][(int) (position.z
					+ .9)] == 1) {
				return true;
			} else if (MainGameLoop.map[(int) (position.x + .9)][(int) (position.y + .9)][(int) (position.z
					+ .1)] == 1) {
				return true;
			}
			// top square
			else if (MainGameLoop.map[(int) (position.x + .1)][(int) (position.y + 1.8)][(int) (position.z
					+ .1)] == 1) {
				return true;
			} else if (MainGameLoop.map[(int) (position.x + .1)][(int) (position.y + 1.8)][(int) (position.z
					+ .9)] == 1) {
				return true;
			} else if (MainGameLoop.map[(int) (position.x + .9)][(int) (position.y + 1.8)][(int) (position.z
					+ .9)] == 1) {
				return true;
			} else if (MainGameLoop.map[(int) (position.x + .9)][(int) (position.y + 1.8)][(int) (position.z
					+ .1)] == 1) {
				return true;
			} else {
				return false;
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			return false;
		}
	}

	/**
	 * checks to see if the enemy collides with anything given the enemy position in the map
	 * @param position the position of the enemy
	 * @return true if there is a collision, false otherwise
	 */
	public static boolean checkEnemyCollision(Vector3f position) {

		try {
			// lower square
			if (MainGameLoop.map[(int) (position.x + .1)][(int) (position.y + .1)][(int) (position.z + .1)] == 1) {
				return true;
			} else if (MainGameLoop.map[(int) (position.x + .1)][(int) (position.y + .1)][(int) (position.z
					+ .9)] == 1) {
				return true;
			} else if (MainGameLoop.map[(int) (position.x + .9)][(int) (position.y + .1)][(int) (position.z
					+ .9)] == 1) {
				return true;
			} else if (MainGameLoop.map[(int) (position.x + .9)][(int) (position.y + .1)][(int) (position.z
					+ .1)] == 1) {
				return true;
			}
			// middle square
			else if (MainGameLoop.map[(int) (position.x + .1)][(int) (position.y + .9)][(int) (position.z + .1)] == 1) {
				return true;
			} else if (MainGameLoop.map[(int) (position.x + .1)][(int) (position.y + .9)][(int) (position.z
					+ .9)] == 1) {
				return true;
			} else if (MainGameLoop.map[(int) (position.x + .9)][(int) (position.y + .9)][(int) (position.z
					+ .9)] == 1) {
				return true;
			} else if (MainGameLoop.map[(int) (position.x + .9)][(int) (position.y + .9)][(int) (position.z
					+ .1)] == 1) {
				return true;
			}else {
				return false;
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			return false;
		}
	}
	
	/**
	 * check to see if the position collides with the enemy
	 * @param position
	 * @return
	 */
	public static Entity hitDetectionSingleEnemy(Vector3f position){
		for(Entity entity : MainGameLoop.activeEntities){
			if((int)position.x == (int)entity.getPosition().x && (int)position.z == (int)entity.getPosition().z && (int)position.y == (int)entity.getPosition().y){
				return entity;
			}
		}
		return null;
		
	}
}
