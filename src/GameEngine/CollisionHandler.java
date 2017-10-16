package GameEngine;

import org.lwjgl.util.vector.Vector3f;

import Entities.Entity;
import Entities.SpawnPointEntity;
import Flashlight.MainGameLoop;

public class CollisionHandler {

	/**
	 * checks to see if the player collides with anything given the players
	 * position in the vector map
	 * 
	 * @param position
	 *            the position of the player
	 * @return return true if the player collides with anything, return false
	 *         otherwise
	 */
	public static boolean checkPlayerCollision(Vector3f position) {

		try {
			// lower square
			if (MainGameLoop.mapManager.map[(int) (position.x + .1)][(int) (position.y + .1)][(int) (position.z
					+ .1)] == 1) {
				return true;
			} else if (MainGameLoop.mapManager.map[(int) (position.x + .1)][(int) (position.y + .1)][(int) (position.z
					+ .9)] == 1) {
				return true;
			} else if (MainGameLoop.mapManager.map[(int) (position.x + .9)][(int) (position.y + .1)][(int) (position.z
					+ .9)] == 1) {
				return true;
			} else if (MainGameLoop.mapManager.map[(int) (position.x + .9)][(int) (position.y + .1)][(int) (position.z
					+ .1)] == 1) {
				return true;
			}
			// middle square
			else if (MainGameLoop.mapManager.map[(int) (position.x + .1)][(int) (position.y + .9)][(int) (position.z
					+ .1)] == 1) {
				return true;
			} else if (MainGameLoop.mapManager.map[(int) (position.x + .1)][(int) (position.y + .9)][(int) (position.z
					+ .9)] == 1) {
				return true;
			} else if (MainGameLoop.mapManager.map[(int) (position.x + .9)][(int) (position.y + .9)][(int) (position.z
					+ .9)] == 1) {
				return true;
			} else if (MainGameLoop.mapManager.map[(int) (position.x + .9)][(int) (position.y + .9)][(int) (position.z
					+ .1)] == 1) {
				return true;
			}
			// top square
			else if (MainGameLoop.mapManager.map[(int) (position.x + .1)][(int) (position.y + 1.8)][(int) (position.z
					+ .1)] == 1) {
				return true;
			} else if (MainGameLoop.mapManager.map[(int) (position.x + .1)][(int) (position.y + 1.8)][(int) (position.z
					+ .9)] == 1) {
				return true;
			} else if (MainGameLoop.mapManager.map[(int) (position.x + .9)][(int) (position.y + 1.8)][(int) (position.z
					+ .9)] == 1) {
				return true;
			} else if (MainGameLoop.mapManager.map[(int) (position.x + .9)][(int) (position.y + 1.8)][(int) (position.z
					+ .1)] == 1) {
				return true;
			} else {
				return false;
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			return false;
		}
	}

	public static boolean checkFlameCollision(Entity entity) {
		try {
			if (MainGameLoop.mapManager.map[(int) (entity.getPosition().x + .5)][(int) (entity
					.getPosition().y)][(int) (entity.getPosition().z + .5)] == 1) {
				return true;
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			return false;
		}
		return false;
	}

	/**
	 * checks to see if the enemy collides with anything given the enemy
	 * position in the map
	 * 
	 * @param position
	 *            the position of the enemy
	 * @return true if there is a collision, false otherwise
	 */
	public static boolean checkEnemyCollision(Entity entity) {

		try {
			// lower square
			if (MainGameLoop.mapManager.map[(int) (entity.getPosition().x
					+ .1 * entity.getDirection().x)][(int) (entity.getPosition().y
							+ .1 * entity.getDirection().y)][(int) (entity.getPosition().z + .1)] == 1) {
				return true;
			} else if (MainGameLoop.mapManager.map[(int) (entity.getPosition().x
					+ .1 * entity.getDirection().x)][(int) (entity.getPosition().y
							+ .1 * entity.getDirection().y)][(int) (entity.getPosition().z + .9)] == 1) {
				return true;
			} else if (MainGameLoop.mapManager.map[(int) (entity.getPosition().x
					+ .9 * entity.getDirection().x)][(int) (entity.getPosition().y
							+ .1 * entity.getDirection().y)][(int) (entity.getPosition().z + .9)] == 1) {
				return true;
			} else if (MainGameLoop.mapManager.map[(int) (entity.getPosition().x
					+ .9 * entity.getDirection().x)][(int) (entity.getPosition().y
							+ .1 * entity.getDirection().y)][(int) (entity.getPosition().z + .1)] == 1) {
				return true;
			}
			// middle square
			else if (MainGameLoop.mapManager.map[(int) (entity.getPosition().x
					+ .1 * entity.getDirection().x)][(int) (entity.getPosition().y
							+ .9 * entity.getDirection().y)][(int) (entity.getPosition().z + .1)] == 1) {
				return true;
			} else if (MainGameLoop.mapManager.map[(int) (entity.getPosition().x
					+ .1 * entity.getDirection().x)][(int) (entity.getPosition().y
							+ .9 * entity.getDirection().y)][(int) (entity.getPosition().z + .9)] == 1) {
				return true;
			} else if (MainGameLoop.mapManager.map[(int) (entity.getPosition().x
					+ .9 * entity.getDirection().x)][(int) (entity.getPosition().y
							+ .9 * entity.getDirection().y)][(int) (entity.getPosition().z + .9)] == 1) {
				return true;
			} else if (MainGameLoop.mapManager.map[(int) (entity.getPosition().x
					+ .9 * entity.getDirection().x)][(int) (entity.getPosition().y
							+ .9 * entity.getDirection().y)][(int) (entity.getPosition().z + .1)] == 1) {
				return true;
			} else {
				return false;
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			return false;
		}
	}

	/**
	 * check to see if the position collides with the enemy
	 * 
	 * @param position
	 * @return
	 */
	public static Entity hitDetectionSingleEnemy(Vector3f position) {
		for (Entity entity : MainGameLoop.mapManager.activeEntities) {
			double dist = Math.sqrt(
					Math.pow(position.x - entity.getPosition().x, 2) + Math.pow(position.y - entity.getPosition().y, 2)
							+ Math.pow(position.z - entity.getPosition().z, 2));
			if (dist<.8) {
				return entity;
			}
		}
		return null;

	}

	public static boolean protectedZones(Vector3f position) {
		try {
			if (Math.sqrt(Math.pow(position.x - MainGameLoop.mapManager.destination.getPosition().x, 2)
					+ Math.pow(position.z - MainGameLoop.mapManager.destination.getPosition().z, 2)) < 3
					&& position.y - MainGameLoop.mapManager.destination.getPosition().y < 3) {
				return true;
			}
		} catch (NullPointerException e) {

		}
		for (Entity entity : MainGameLoop.mapManager.activeEntities) {
			if (entity.getClass() == SpawnPointEntity.class) {
				if (Math.sqrt(Math.pow(position.x - entity.getPosition().x, 2)
						+ Math.pow(position.z - entity.getPosition().z, 2)) < 2) {
					return true;
				}
			}
		}
		return false;
	}
}
