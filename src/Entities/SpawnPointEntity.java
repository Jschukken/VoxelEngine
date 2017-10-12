package Entities;

import org.lwjgl.util.vector.Vector3f;

import Flashlight.MainGameLoop;
import Models.TexturedModel;
import PathFinding.CreatePath;

public class SpawnPointEntity extends Entity {

	private int spawnTimer = 0;
	public static int spawnRate = 240;
	
	private Vector3f position;
	private TexturedModel enemy;
	private int[] path;
	
	public SpawnPointEntity(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ, Vector3f scale, TexturedModel enemy, int[] path) {
		super(model, position, rotX, rotY, rotZ, scale);
		this.enemy = enemy;
		//this.path = path;
		this.path = path;
		this.position = position;
		spawnTimer = (int)(Math.random()*spawnRate);
	}
	
	public void update(){
		spawnTimer++;
		if(spawnTimer >= spawnRate){
			spawnTimer = 0;
			Vector3f p = new Vector3f(position.x,position.y,position.z);
			MainGameLoop.mapManager.addActiveEntity(enemy, p, path);		
		}
	}
}