package Entities;

import org.lwjgl.util.vector.Vector3f;

import Flashlight.MainGameLoop;
import GameEngine.CollisionHandler;
import Models.TexturedModel;

/**
 * simulates a goal point for the enemies. The player must defend this.
 * @author Jelle Schukken
 *
 */
public class DestinationEntity extends Entity{
	
	private int healthPoints;
	public static int MAX_HP = 20;
	private Vector3f position;

	public DestinationEntity(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ,
			Vector3f scale) {
		super(model, position, rotX, rotY, rotZ, scale);
		this.position = position;
		healthPoints = MAX_HP;
	}
	
	/**
	 * checks if an enemy has made it to this point. If it has it loses health and dies appropriately
	 */
	public void update(){
		Entity hit = CollisionHandler.hitDetectionSingleEnemy(position);
		if(hit != null){
			hit.destroy();
			healthPoints--;
		}
		if(healthPoints<=0){
			MainGameLoop.setState("gameover");//destination died, set game over.
		}
	}
	
	//__________BASIC SETTERS AND GETTERS___________
	public void setHealthPoints(int newHP){
		healthPoints = newHP;
	}
	
	public float getHP() {
		return healthPoints;
	}
	
	public float getMaxHP() {
		return MAX_HP;
	}

}
