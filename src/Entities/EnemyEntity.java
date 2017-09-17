package Entities;

import org.lwjgl.util.vector.Vector3f;

import Flashlight.MainGameLoop;
import GameEngine.CollisionHandler;
import Models.TexturedModel;

public class EnemyEntity extends Entity {

	private int[] path;
	private int pathPosition = 0;
	private static final float ENEMY_SPEED = .1001f;

	public EnemyEntity(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ, Vector3f scale,
			int[] path) {
		super(model, position, rotX, rotY, rotZ, scale);
		this.path = path;
		// TODO Auto-generated constructor stub
	}

	/**
	 * updates the enemies position and moves it to the next point on its path
	 */
	public void update() {
		boolean check = false;

		if (checkPath()) {
			pathPosition += 3;
			if (pathPosition > path.length - 1) {
				destroy();
				return;
			}
		}
		
		if (position.x < path[pathPosition]) {
			position.x += ENEMY_SPEED;
			if (CollisionHandler.checkEnemyCollision(position)) {
				position.x -= ENEMY_SPEED;
			}else{
				check = true;
			}
		} else if (position.x> path[pathPosition]) {
			position.x -= ENEMY_SPEED;
			if (CollisionHandler.checkEnemyCollision(position)) {
				position.x += ENEMY_SPEED;
			}else{
				check = true;
			}
		}
		if (position.z < path[pathPosition + 2]) {
			position.z += ENEMY_SPEED;
			if (CollisionHandler.checkEnemyCollision(position)) {
				position.z -= ENEMY_SPEED;
			}else{
				check = true;
			}

		} else if (position.z > path[pathPosition + 2]) {
			position.z -= ENEMY_SPEED;
			if (CollisionHandler.checkEnemyCollision(position)) {
				position.z += ENEMY_SPEED;
			}else{
				check = true;
			}
		}
		if (!check) {
			if (position.y+ENEMY_SPEED< path[pathPosition + 1] || !checkPath()) {
				position.y += ENEMY_SPEED*2;
				//System.out.println(position.y +" "+path[pathPosition+1] + checkPath());

			}
		}

		position.y -= ENEMY_SPEED;
		if(CollisionHandler.checkEnemyCollision(position)){
			position.y+=ENEMY_SPEED;
		}

		
		roundPosition();
	}

	/**
	 * simulates getting hit
	 */
	public void getHit(){
		destroy();
	}
	
	/**
	 * removes enemy from the game
	 */
	private void destroy() {
		MainGameLoop.removeActiveEntity(this);

	}
	
	/**
	 * rounds the position to the 1nd decimal place
	 */
	private void roundPosition(){
		position.x =  ((int)(position.x*10+0.5))/10.0f;
		position.y =  ((int)(position.y*10+0.5))/10.0f;
		position.z =  ((int)(position.z*10+0.5))/10.0f;
		
	}
	
	/**
	 * checks if the enemy has arrived at the next spot on their path
	 * @return
	 */
	private boolean checkPath(){
		roundPosition();
		return position.x == path[pathPosition] && position.y == path[pathPosition+1] && position.z == path[pathPosition+2]; 
	}

}
