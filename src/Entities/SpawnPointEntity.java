package Entities;

import org.lwjgl.util.vector.Vector3f;

import Flashlight.MainGameLoop;
import Models.TexturedModel;
import PathFinding.CreatePath;
import PathFinding.AStar;
import Map.Map;
import java.util.Arrays;

public class SpawnPointEntity extends Entity {

<<<<<<< HEAD
	private int spawnTimer = 0;
	public static int spawnRate = 240;
	private int pathTimer = 0;
	public static int pathRate = 5 * spawnRate;
=======
	private static int spawnTimer = 0;
>>>>>>> parent of c7ef24e... Defence map example
	
	private TexturedModel enemy;
	private int[] path;
	
	public SpawnPointEntity(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ, Vector3f scale, TexturedModel enemy, int[] path) {
		super(model, position, rotX, rotY, rotZ, scale);
		this.enemy = enemy;
<<<<<<< HEAD
		this.path = CreatePath.createPath(position);
		this.position = position;
		spawnTimer = (int)(Math.random()*spawnRate);
=======
		this.path = path;
		// TODO Auto-generated constructor stub
>>>>>>> parent of c7ef24e... Defence map example
	}
	
	public void update() {
		spawnTimer++;
<<<<<<< HEAD
		pathTimer++;
		// spawn enemy if sufficient time has passed
		if (spawnTimer >= spawnRate) {
=======
		if(spawnTimer >= 60){
>>>>>>> parent of c7ef24e... Defence map example
			spawnTimer = 0;
			Vector3f p = new Vector3f(position.x, position.y, position.z);
			MainGameLoop.mapManager.addActiveEntity(enemy, p, path);
		}
		// update path if sufficient time has passed
		if (pathTimer >= pathRate) {
			pathTimer = 0;
			this.path = CreatePath.createPath(position);
		}
	}
<<<<<<< HEAD
}
=======

}
>>>>>>> parent of c7ef24e... Defence map example
