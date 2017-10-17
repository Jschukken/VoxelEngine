package Entities;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import Flashlight.MainGameLoop;
import GameEngine.CollisionHandler;
import Models.TexturedModel;
import RenderEngine.MasterGameRenderer;
import ToolBox.MatrixMath;
import ToolBox.TexturedModelMaker;

public class EnemyEntity extends Entity {

	private int[] path;
	private int pathPosition = 0;
	private static final float ENEMY_SPEED = .0601f;
	private Vector3f position;
	private Vector3f direction;
	private float rotX, rotY, rotZ;
	private int hp;
	private boolean turnState = true;
	private float turnSpeed;

	public EnemyEntity(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ, Vector3f scale,
			int[] path) {
		super(model, position, rotX, rotY, rotZ, scale);
		this.path = path;
		this.position = position;
		this.rotX = rotX;
		this.rotZ = rotZ;
		this.rotY = rotY;
		direction = super.getDirection();
		direction.normalise();
		turnSpeed = getTurnDir();
		hp = 20;
	}

	/**
	 * updates the enemies position and moves it to the next point on its path
	 */
	public void update() {
		
		super.update();
		if (turnState) {
			turnState = needTurn();
		} else {
			move();
		}
	}

	/**
	 * checks if the enemy needs to turn and turns if it does
	 * 
	 * @return true if it does, false if it doesn't
	 */
	private boolean needTurn() {
		Vector3f direction;
		if (pathPosition == 0) {
			direction = new Vector3f(position.x - path[pathPosition], 0, position.z - path[pathPosition + 1]);
		} else if (pathPosition < path.length - 1) {
			direction = new Vector3f(path[pathPosition - 2] - path[pathPosition], 0,
					path[pathPosition - 1] - path[pathPosition + 1]);
		} else {
			return false;
		}
		Vector3f lookAt = MatrixMath
				.rotateVector(new Vector3f((float) Math.toDegrees(rotX), (float) Math.toDegrees(-rotY), 0));
		lookAt.normalise();
		direction.normalise();
		double angle = Vector3f.angle(lookAt, direction);
		if (angle > Math.toRadians(2)) {
			rotY += turnSpeed;
			return true;
		}
		return false;

	}

	private float getTurnDir() {
		Vector3f direction;
		if (pathPosition == 0) {
			direction = new Vector3f(position.x - path[pathPosition], 0, position.z - path[pathPosition + 1]);
		} else if (pathPosition < path.length - 1) {
			direction = new Vector3f(path[pathPosition - 2] - path[pathPosition], 0,
					path[pathPosition - 1] - path[pathPosition + 1]);
		} else {
			return 0;
		}
		Vector3f lookAt = MatrixMath
				.rotateVector(new Vector3f((float) Math.toDegrees(rotX), (float) Math.toDegrees(-rotY), 0));
		lookAt.normalise();
		direction.normalise();
		if (lookAt.x * direction.z - lookAt.z * direction.x > 0) {
			return (float) -0.05;
		} else {
			return (float) 0.05;
		}

	}

	/**
	 * moves the enemy
	 */
	private void move() {
		boolean check = false;

		if (checkPath()) {
			pathPosition += 2;
			turnSpeed = getTurnDir();
			turnState = needTurn();
			if (pathPosition > path.length - 1) {
				destroy();
				return;
			}
			return;
		}

		if (position.x < path[pathPosition]) {
			position.x += ENEMY_SPEED;
			if (CollisionHandler.checkEnemyCollision(this)) {
				position.x -= ENEMY_SPEED;
			} else {
				check = true;
			}
		} else if (position.x > path[pathPosition]) {
			position.x -= ENEMY_SPEED;
			if (CollisionHandler.checkEnemyCollision(this)) {
				position.x += ENEMY_SPEED;
			} else {
				check = true;
			}
		}
		if (position.z < path[pathPosition + 1]) {
			position.z += ENEMY_SPEED;
			if (CollisionHandler.checkEnemyCollision(this)) {
				position.z -= ENEMY_SPEED;
			} else {
				check = true;
			}

		} else if (position.z > path[pathPosition + 1]) {
			position.z -= ENEMY_SPEED;
			if (CollisionHandler.checkEnemyCollision(this)) {
				position.z += ENEMY_SPEED;
			} else {
				check = true;
			}
		}
		if (!check) {
			if (!checkPath()) {
				position.y += ENEMY_SPEED * 2;
				// System.out.println(position.y +" "+path[pathPosition+1] +
				// checkPath());

			}
		}

		position.y -= ENEMY_SPEED;
		if (CollisionHandler.checkEnemyCollision(this)) {
			position.y += ENEMY_SPEED;
		}

		roundPosition();
	}

	/**
	 * simulates getting hit
	 */
	public void getHit() {
		for (int i = 0; i < 5; i++) {
			MainGameLoop.mapManager.addParticleEntity(TexturedModelMaker.basicCube,
					new Vector3f(position.x, position.y, position.z));
		}
		hp--;
		if (hp <= 0) {
			destroy();
		}
	}

	/**
	 * removes enemy from the game
	 */
	public void destroy() {
		for (int i = 0; i < 100; i++) {
			MainGameLoop.mapManager.addParticleEntity(TexturedModelMaker.basicCube,
					new Vector3f(position.x, position.y, position.z));
		}
		MainGameLoop.mapManager.removeActiveEntity(this);

	}

	/**
	 * rounds the position to the 1nd decimal place
	 */
	private void roundPosition() {
		position.x = ((int) (position.x * 100 + 0.5)) / 100.0f;
		position.y = ((int) (position.y * 100 + 0.5)) / 100.0f;
		position.z = ((int) (position.z * 100 + 0.5)) / 100.0f;

	}

	public Vector3f getDirection() {
		direction = MatrixMath
				.rotateVector(new Vector3f((float) Math.toDegrees(rotX), (float) Math.toDegrees(-rotY), 0));
		direction.normalise();
		return direction;
	}

	/**
	 * checks if the enemy has arrived at the next spot on their path
	 * 
	 * @return
	 */
	private boolean checkPath() {
		roundPosition();
		return position.x == path[pathPosition] && position.z == path[pathPosition + 1];
	}

	public float getRotX() {
		return rotX;
	}

	public float getRotY() {
		return rotY;
	}
	
	public Vector3f getScale(){
		return new Vector3f(1f,1f,1f);
	}

	public float getRotZ() {
		return rotZ;
	}

}
