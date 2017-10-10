package Entities;

import org.lwjgl.util.vector.Vector3f;

import Flashlight.MainGameLoop;
import GameEngine.CollisionHandler;
import Models.TexturedModel;

public class DestinationEntity extends Entity{
	
	private int healthPoints;
	private Vector3f position;

	public DestinationEntity(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ,
			Vector3f scale) {
		super(model, position, rotX, rotY, rotZ, scale);
		this.position = position;
		healthPoints = 20;
	}
	
	public void update(){
		Entity hit = CollisionHandler.hitDetectionSingleEnemy(position);
		if(hit != null){
			hit.destroy();
			healthPoints--;
		}
		if(healthPoints<=0){
			MainGameLoop.setState("gameover");
		}
	}
	
	public void setHealthPoints(int newHP){
		healthPoints = newHP;
	}

}