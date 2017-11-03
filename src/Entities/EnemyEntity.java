package Entities;

import org.lwjgl.util.vector.Vector3f;

import Flashlight.MainGameLoop;
import GameEngine.CollisionHandler;
import GameEngine.MapManager;
import Models.TexturedModel;
import PathFinding.AStar;
import RenderEngine.DisplayManager;
import ToolBox.MatrixMath;
import ToolBox.TexturedModelMaker;

/**
 * simulates an enemy robot
 * 
 * @author Jelle Schukken, Berk Aksakal
 *
 */
public class EnemyEntity extends Entity {

	private int[] path;
	private int pathPosition = 0;
	private float ENEMY_SPEED = .0601f * 60.0f / (float) DisplayManager.FPS_CAP;
	private float TURN_SPEED = 0.07f;
	private Vector3f position;
	private Vector3f direction;
	private float rotX, rotY, rotZ;
	private int hp;
	private boolean turnState = true;
	private float turnSpeed;
	private long old = 0;
	private int time = 0;
	private int hpcheck;
	private TexturedModel shrapnel = TexturedModelMaker.cubeTexturedModel(MainGameLoop.loader, "black");

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
		// hp = 20;
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
		lookAt.x = -lookAt.x;
		lookAt.y = -lookAt.y;
		lookAt.z = -lookAt.z;
		direction.normalise();
		double angle = Vector3f.angle(lookAt, direction);
		if (angle > Math.toRadians(5 * 60.0f / DisplayManager.FPS_CAP)) {
			rotY += turnSpeed;
			return true;
		}
		return false;

	}

	/**
	 * returns the speed and direction the robot should turn
	 * @return the speed and direction the robot should turn
	 */
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
			return (float) TURN_SPEED * 60.0f / DisplayManager.FPS_CAP;
		} else {
			return (float) -TURN_SPEED * 60.0f / DisplayManager.FPS_CAP;
		}

	}

	/**
	 * moves the enemy
	 */
	private void move() {
		boolean check = false;
		timeUpdate();
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

		if (position.x - path[pathPosition] < -ENEMY_SPEED) {
			position.x += ENEMY_SPEED;
			if (CollisionHandler.checkEnemyCollision(this)) {
				position.x -= ENEMY_SPEED;
			} else {
				check = true;
			}
		} else if (position.x - path[pathPosition] > ENEMY_SPEED) {
			position.x -= ENEMY_SPEED;
			if (CollisionHandler.checkEnemyCollision(this)) {
				position.x += ENEMY_SPEED;
			} else {
				check = true;
			}
		}
		if (position.z - path[pathPosition + 1] < -ENEMY_SPEED) {
			position.z += ENEMY_SPEED;
			if (CollisionHandler.checkEnemyCollision(this)) {
				position.z -= ENEMY_SPEED;
			} else {
				check = true;
			}

		} else if (position.z - path[pathPosition + 1] > ENEMY_SPEED) {
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

		// System.out.println(position.x + " " + path[pathPosition] + " : " +
		// position.z+ " " + path[pathPosition+1]);
		position.y -= ENEMY_SPEED;
		if (CollisionHandler.checkEnemyCollision(this)) {
			position.y += ENEMY_SPEED;
		}

		//roundPosition();
	}

	/**
	 * simulates getting hit
	 */
	public void getHit() {
		for (int i = 0; i < 5; i++) {
			MainGameLoop.mapManager.addParticleEntity(shrapnel, new Vector3f(position.x, position.y, position.z));
		}
		hp--;
		setModel(MapManager.hitNormalModel);
		if (hp <= 0) {
			destroy();
		}
	}

	/**
	 * removes enemy from the game
	 */
	public void destroy() {
		// saves the position of the enemy for later use in A*
		AStar.upDeaths((int) position.x / 5 + 1, (int) position.z / 5 + 1);
		// destroys the enemy
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
		position.x = ((int) (position.x * 1000.0 + 0.5)) / (float) (1000.0);
		position.y = ((int) (position.y * 1000.0 + 0.5)) / (float) (1000.0);
		position.z = ((int) (position.z * 1000.0 + 0.5)) / (float) (1000.0);

	}

	/**
	 * returns the robots current direction
	 */
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
		return Math.abs(position.x - path[pathPosition]) < ENEMY_SPEED * 2.0
				&& Math.abs(position.z - path[pathPosition + 1]) < ENEMY_SPEED * 2.0;
	}

	// _____________BASIC SETTERS AND GETTERS_______________
	public float getRotX() {
		return rotX;
	}

	public float getRotY() {
		return rotY;
	}

	public Vector3f getScale() {
		return new Vector3f(1f, 1f, 1f);
	}

	public float getRotZ() {
		return rotZ;
	}

	public Vector3f getCollisionPosition() {
		return position;
	}

	public Vector3f getPosition() {
		return new Vector3f(position.x, (float) (position.y - .5), position.z);
	}

	// __________END GETTERS AND SETTERS_____________
	/**
	 * Berk please add comment
	 */
	public void timeUpdate() {
		long current = System.currentTimeMillis();
		if (current >= (old + 1000)) {
			old = current;
			time++;

			if (hp == hpcheck) {
				if (time % 2 == 0) {
					setModel(MapManager.normalModel);
				} else {
					setModel(MapManager.runModel);
				}
			} else {
				if (time % 5 == 0) {
					setModel(MapManager.hitNormalModel);
				} else {

					setModel(MapManager.hitRunModel);
				}
			}
			if (time > 10) {
				time = 0;
			}
		}
	}

	public void setHp(int a) {
		hp = a;
		hpcheck = a;
	}

	public void setSpeed(float a) {
		ENEMY_SPEED = ENEMY_SPEED + a;
	}
}
