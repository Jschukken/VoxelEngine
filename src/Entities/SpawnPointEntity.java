package Entities;

import org.lwjgl.util.vector.Vector3f;

import Flashlight.MainGameLoop;
import Models.TexturedModel;
import PathFinding.CreatePath;
import RenderEngine.DisplayManager;

/**
 * represents the enemy spawn points
 * @author Jelle Schukken
 *
 */
public class SpawnPointEntity extends Entity {

	private int spawnTimer = 0;
	public static int spawnRate = (int)(240/(60/(float)DisplayManager.FPS_CAP));
	private int pathTimer = 0;
	public static int pathRate = 5 * spawnRate;
	
	private Vector3f position;
	private TexturedModel enemy;
	private int[] path;
	
	/*
	 * this function could use a comment
	 */
	public SpawnPointEntity(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ, Vector3f scale, TexturedModel enemy) {
		super(model, position, rotX, rotY, rotZ, scale);
		this.enemy = enemy;
		this.path = CreatePath.createPath(position);
		this.position = position;
		spawnTimer = (int)(Math.random()*spawnRate);
	}
	
	/**
	 * spawns enemy if a certain time has passed, recalculates path if a certain time has passed
	 */
	public void update() {
		spawnTimer++;
		pathTimer++;
		// spawn enemy if sufficient time has passed
		if (spawnTimer >= spawnRate) {
			spawnTimer = 0;
			Vector3f p = new Vector3f(position.x,position.y+0.1f,position.z);
			MainGameLoop.mapManager.addActiveEntity(enemy, p, path);
		}
		// update path if sufficient time has passed
		if (pathTimer >= pathRate) {
			pathTimer = 0;
			this.path = CreatePath.createPath(position);
		}
	}
}