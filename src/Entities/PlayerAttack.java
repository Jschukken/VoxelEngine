package Entities;

import org.lwjgl.util.vector.Vector3f;

import GameEngine.CollisionHandler;
import Models.TexturedModel;

public class PlayerAttack extends Entity {
	private static final float PROJECTILE_SPEED = -0.4f;
	private boolean moving = true;
	private Vector3f position;
	//private Vector3f rotation;
	private Vector3f direction;

	public PlayerAttack(TexturedModel model, Vector3f position, Vector3f rot, Vector3f direction, Vector3f scale) {
		super(model, position, 0, (float)Math.signum(Math.sin(Math.toRadians(direction.y)))*rot.z, -(float)Math.signum(Math.sin(Math.toRadians(direction.y)))*rot.y, scale);
		this.position = position;
		//rotation = direction;
		this.direction = direction;
	}
	
	public void update(){
		if(moving){
			Entity ent = CollisionHandler.hitDetectionSingleEnemy(position);
			if(CollisionHandler.checkEnemyCollision(position)){//temporary
				moving = false;
			}else if(ent != null){
				ent.getHit();
				destroy();
			}else{
				position.x += (float)-(PROJECTILE_SPEED*Math.sin(Math.toRadians(direction.y)));
				position.z += (float)(PROJECTILE_SPEED*Math.cos(Math.toRadians(direction.y)));
				position.y += (float)(PROJECTILE_SPEED*Math.sin(Math.toRadians(direction.x)));
			}
		}
	}
	
	private void destroy(){
		//remove from game
	}

}
