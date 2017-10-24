package Entities;

import org.lwjgl.util.vector.Vector3f;

import Flashlight.MainGameLoop;
import GameEngine.CollisionHandler;
import Models.TexturedModel;
import RenderEngine.DisplayManager;
/**
 * simulates a particle in a flamethrower
 * @author Jelle Schukken
 *
 */
public class PlayerAttack extends Entity {
	private float projectileSpeedX = 0.3f* 60.0f/(float)DisplayManager.FPS_CAP;
	private float projectileSpeedY = 0.3f* 60.0f/(float)DisplayManager.FPS_CAP;
	private float projectileSpeedZ = 0.3f* 60.0f/(float)DisplayManager.FPS_CAP;
	private float rise = 0.01f* 60.0f/(float)DisplayManager.FPS_CAP;
	private int fade = 50 - (int)(Math.random()*30);
	private static final float ANGLE = (float) 90 / 3;
	private Vector3f position;
	private Vector3f direction;
	private Vector3f normDir;
	private Vector3f scale;
	private float scaler = (float)(0.009- Math.random()*0.003);

	/*
	 * comment here would be nice
	 */
	public PlayerAttack(TexturedModel model, Vector3f position, Vector3f rot, Vector3f direction, Vector3f scale) {
		super(model, position, 0, (float) Math.toRadians(-direction.y), 0, scale);
		this.position = position;
		Vector3f newDir = new Vector3f((float) (direction.x + Math.random() * ANGLE - ANGLE / 2),
				(float) (direction.y + Math.random() * ANGLE - ANGLE / 2),
				(float) (direction.z + Math.random() * ANGLE - ANGLE / 2));
		int i = 0;
		//generates a random direction usually within a cone, but a small chance of being outside the cone
		while(Math.sqrt(Math.pow(newDir.x-direction.x,2)+Math.pow(newDir.y-direction.y,2)+Math.pow(newDir.z-direction.z, 2))>10 && i <10){
			newDir = new Vector3f((float) (direction.x + Math.random() * ANGLE - ANGLE / 2),
					(float) (direction.y + Math.random() * ANGLE - ANGLE / 2),
					(float) (direction.z + Math.random() * ANGLE - ANGLE / 2));
			i++;
		}

		this.direction = newDir;
		normDir = new Vector3f (newDir.x,newDir.y,newDir.z);
		normDir.normalise();
		this.scale = scale;
	}

	/**
	 * updates the flame particles position and scale, checks if it hits an enemy, and destroys itself after a certain time
	 */
	/*
	 * function could still use a bit more comments on the inside
	 */
	public void update() {
		super.update();
		fade -= 60.0f/(float)DisplayManager.FPS_CAP;
		Entity ent = CollisionHandler.hitDetectionSingleEnemy(
				new Vector3f(position.x, position.y, position.z));
		if (ent != null && fade > 10) {
			ent.getHit();
			destroy();
		} else if (fade < 0) {
			destroy();
		} else {
			position.x += (float) (projectileSpeedX * Math.sin(Math.toRadians(direction.y)));
			if (CollisionHandler.checkFlameCollision(this)) {
				position.x -= (float) (projectileSpeedX * Math.sin(Math.toRadians(direction.y)));
				projectileSpeedX /= 1.2f;
				projectileSpeedZ /= 1.02f;
				projectileSpeedY /= 1.02f;
			}
			position.z += (float) -(projectileSpeedZ * Math.cos(Math.toRadians(direction.y)));
			if (CollisionHandler.checkFlameCollision(this)) {
				position.z -= (float) -(projectileSpeedZ * Math.cos(Math.toRadians(direction.y)));
				projectileSpeedX /= 1.02f;
				projectileSpeedZ /= 1.2f;
				projectileSpeedY /= 1.02f;
			}
			position.y += (float) -(projectileSpeedY * Math.sin(Math.toRadians(direction.x))) + rise;
			if (CollisionHandler.checkFlameCollision(this)) {
				position.y -= (float) -(projectileSpeedY * Math.sin(Math.toRadians(direction.x))) + rise;
				direction.x = direction.x / 2;
			}
			projectileSpeedX = projectileSpeedX / 1.02f;
			projectileSpeedZ = projectileSpeedZ / 1.02f;
			projectileSpeedY = projectileSpeedY / 1.02f;
			scale.x = scale.x + scaler;
			position.x += scaler/2.0*normDir.x;
			scale.y = scale.y + scaler;
			position.y += scaler/2.0*normDir.y;
			scale.z = scale.z + scaler;
			position.z += scaler/2.0*normDir.z;
			rise += 0.001f;

		}

	}
	
	public Vector3f getDirection(){
		return normDir;
	}

	/**
	 * removes the particle from the game
	 */
	public void destroy() {
		MainGameLoop.mapManager.removeAttackEntity(this);
	}

}
