package Entities;

import org.lwjgl.util.vector.Vector3f;

import Flashlight.MainGameLoop;
import GameEngine.CollisionHandler;
import Models.TexturedModel;

public class PlayerAttack extends Entity {
	private float projectileSpeed = 0.3f;
	private float rise = 0.01f;
	private int fade = 70 - (int)(Math.random()*70);
	private static final float ANGLE = (float) 90 / 3;
	private Vector3f position;
	// private Vector3f rotation;
	private Vector3f direction;
	private Vector3f scale;
	private float scaler = (float)(0.009- Math.random()*0.003);

	public PlayerAttack(TexturedModel model, Vector3f position, Vector3f rot, Vector3f direction, Vector3f scale) {
		super(model, position, 0, (float) Math.toRadians(-direction.y), 0, scale);
		this.position = position;
		// rotation = direction;
		Vector3f newDir = new Vector3f((float) (direction.x + Math.random() * ANGLE - ANGLE / 2),
				(float) (direction.y + Math.random() * ANGLE - ANGLE / 2),
				(float) (direction.z + Math.random() * ANGLE - ANGLE / 2));
		int i = 0;
		while(Math.sqrt(Math.pow(newDir.x-direction.x,2)+Math.pow(newDir.y-direction.y,2)+Math.pow(newDir.z-direction.z, 2))>10 && i <10){
			newDir = new Vector3f((float) (direction.x + Math.random() * ANGLE - ANGLE / 2),
					(float) (direction.y + Math.random() * ANGLE - ANGLE / 2),
					(float) (direction.z + Math.random() * ANGLE - ANGLE / 2));
			i++;
		}
		System.out.println(i);

		this.direction = newDir;
		this.scale = scale;
	}

	public void update() {
		fade--;
		Entity ent = CollisionHandler.hitDetectionSingleEnemy(
				new Vector3f(position.x + scale.x / 2.0f, position.y + scale.y / 2.0f, position.z + scale.z / 2.0f));
		if (ent != null && fade > 30) {
			ent.getHit();
			destroy();
		} else if (fade < 0) {
			destroy();
		} else {
			position.x += (float) (projectileSpeed * Math.sin(Math.toRadians(direction.y)));
			if (CollisionHandler.checkFlameCollision(position)) {
				position.x -= (float) (projectileSpeed * Math.sin(Math.toRadians(direction.y)));
				projectileSpeed = projectileSpeed / 1.02f;
			}
			position.z += (float) -(projectileSpeed * Math.cos(Math.toRadians(direction.y)));
			if (CollisionHandler.checkFlameCollision(position)) {
				position.z -= (float) -(projectileSpeed * Math.cos(Math.toRadians(direction.y)));
				projectileSpeed = projectileSpeed / 1.02f;
			}
			position.y += (float) -(projectileSpeed * Math.sin(Math.toRadians(direction.x))) + rise;
			if (CollisionHandler.checkFlameCollision(position)) {
				position.y -= (float) -(projectileSpeed * Math.sin(Math.toRadians(direction.x))) + rise;
				direction.x = direction.x / 2;
			}
			projectileSpeed = projectileSpeed / 1.02f;
			scale.x = scale.x + scaler;
			scale.y = scale.y + scaler;
			scale.z = scale.z + scaler;
			rise += 0.001f;

		}

	}

	public void destroy() {
		MainGameLoop.mapManager.removeAttackEntity(this);
	}

}
