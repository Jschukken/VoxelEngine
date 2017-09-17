package Entities;

import org.lwjgl.util.vector.Vector3f;

import Flashlight.MainGameLoop;
import Models.TexturedModel;

public class SpawnPointEntity extends Entity {

	private static int spawnTimer = 0;
	
	private TexturedModel enemy;
	private int[] path;
	
	public SpawnPointEntity(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ, Vector3f scale, TexturedModel enemy, int[] path) {
		super(model, position, rotX, rotY, rotZ, scale);
		this.enemy = enemy;
		this.path = path;
		// TODO Auto-generated constructor stub
	}
	
	public void update(){
		spawnTimer++;
		if(spawnTimer >= 60){
			spawnTimer = 0;
			Vector3f p = new Vector3f(position.x,position.y,position.z);
			MainGameLoop.addActiveEntity(enemy, p, path);
			
		}
	}

}
