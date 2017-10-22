package Entities;

import java.nio.IntBuffer;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;

import Flashlight.MainGameLoop;
import GameEngine.CollisionHandler;
import RenderEngine.DisplayManager;
import ToolBox.MatrixMath;
import ToolBox.TexturedModelMaker;

/**
 * The camera object TODO: turn into player
 * 
 * @author Jelle Schukken
 *
 */
public class Camera {

	private static float FRICTION = 4;
	public static int MAX_HP = 5;
	public static final boolean GRAVITY = true;
	private static final long ONE_SECOND = 1000000000;

	private Vector3f position;
	private float rotX, rotY, rotZ;
	private float dx = 0, dy = 0, dz = 0;

	private float acceleration = 0.01f * 60.0f/DisplayManager.FPS_CAP;
	private float maxSpeed = 0.12f * 60.0f/DisplayManager.FPS_CAP;
	private float jumpHeight = 0.15f * 60.0f/DisplayManager.FPS_CAP;
	private float turnSpeed = 0.1f * 60.0f/DisplayManager.FPS_CAP;
	private float currSpeed;
	private float strafe;
	private long lastHit;
	private long lastRegen;

	private int hp;

	private IntBuffer[] jumpSFX;
	private IntBuffer[] getHitSFX;

	public Camera(Vector3f position, float rotX, float rotY, float rotZ) {
		super();
		this.position = position;
		this.rotX = rotX;
		this.rotY = rotY;
		this.rotZ = rotZ;
		jumpSFX = MainGameLoop.audH.createSound("jump");
		getHitSFX = MainGameLoop.audH.createSound("hurt");
		lastHit = System.nanoTime();
		hp = MAX_HP;
	}

	public void update() {
		if (Mouse.isGrabbed()) {
			checkJump();
			calculateMove();
			applieMove();
			applieGravity();
			checkHit();
			checkRegen();
			attack();
		}
	}

	/**
	 * calculates the velocity and rotation of the camera from input
	 */
	private void calculateMove() {

		if (Keyboard.isKeyDown(Keyboard.KEY_W) && !Keyboard.isKeyDown(Keyboard.KEY_S)) {
			currSpeed = -acceleration;
		} else if (Keyboard.isKeyDown(Keyboard.KEY_S) && !Keyboard.isKeyDown(Keyboard.KEY_W)) {
			currSpeed = acceleration;
		} else {
			currSpeed = 0;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_A) && !Keyboard.isKeyDown(Keyboard.KEY_D)) {
			strafe = -acceleration;
		} else if (Keyboard.isKeyDown(Keyboard.KEY_D) && !Keyboard.isKeyDown(Keyboard.KEY_A)) {
			strafe = acceleration;
		} else {
			strafe = 0;
		}

		rotX += -Mouse.getDY() * turnSpeed;
		rotY += Mouse.getDX() * turnSpeed;

		if (rotX > 80) {
			rotX = 80;
		} else if (rotX < -80) {
			rotX = -80;
		}

		// dy = (float)(currSpeed*Math.sin(Math.toRadians(rotX)));
		dx += (float) -(currSpeed * Math.sin(Math.toRadians(rotY)));
		dz += (float) (currSpeed * Math.cos(Math.toRadians(rotY)));

		dx += (float) (strafe * Math.sin(Math.toRadians(rotY + 90)));
		dz += (float) -(strafe * Math.cos(Math.toRadians(rotY + 90)));

		// maths to make sure the vector of movement is at most max speed
		double dx2 = Math.pow(dx, 2);
		double dz2 = Math.pow(dz, 2);
		double o = dx2 + dz2 - Math.pow(maxSpeed, 2);

		if (o > 0) {
			dx = Math.signum(dx) * (float) Math.sqrt(dx2 - (o * dx2 / (dx2 + dz2)));
			dz = Math.signum(dz) * (float) Math.sqrt(dz2 - (o * dz2 / (dx2 + dz2)));
		}

	}

	/**
	 * applies movement calculated to camera
	 */
	private void applieMove() {
		position.x += dx;
		for (int i = 0; i < 4 && (CollisionHandler.checkPlayerCollision(position)
				|| CollisionHandler.protectedZones(position)); i++) {
			position.x -= dx / 4;

		}
		position.z += dz;
		for (int i = 0; i < 4 && (CollisionHandler.checkPlayerCollision(position)
				|| CollisionHandler.protectedZones(position)); i++) {
			position.z -= dz / 4;

		}
		position.y += dy;
		if (CollisionHandler.checkPlayerCollision(position) || CollisionHandler.protectedZones(position)) {

			position.y -= dy;
			if (dy < 0 && currSpeed == 0 && strafe == 0) {
				dx -= dx / FRICTION;
				dz -= dz / FRICTION;
			}
			dy = 0;
		}
	}

	/**
	 * checks for and applies jump
	 */
	private void checkJump() {
		if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)
				&& (CollisionHandler.checkPlayerCollision(new Vector3f(position.x, position.y + dy, position.z))
						|| CollisionHandler.protectedZones(new Vector3f(position.x, position.y + dy, position.z)))) {
			dy = jumpHeight;
			MainGameLoop.audH.playAudio(jumpSFX, position, position);
		}
	}

	/**
	 * applies gravity to camera
	 */
	private void applieGravity() {
		if (GRAVITY) {
			dy = Math.max(dy - 0.01f * 60.0f/DisplayManager.FPS_CAP, -maxSpeed * 4);
		}

		fallCheck();
	}

	/**
	 * checks if the camera should take dmg
	 */
	private void checkHit() {
		Entity ent = CollisionHandler.hitDetectionSingleEnemy(position);
		if(ent!= null && System.nanoTime()-lastHit>ONE_SECOND/2){
			lastHit = System.nanoTime();
			getHit();
		}
	}

	/**
	 * attacks if button 0 is pressed
	 */
	private void attack() {
		if (Mouse.isButtonDown(0)) {
			System.out.println("thrower check");
			for (int i = 0; i < 2 * 60.0f/DisplayManager.FPS_CAP; i++) {
				MainGameLoop.mapManager.addAttackEntity(TexturedModelMaker.basicCube,
						new Vector3f(position.x, position.y + .7f, position.z), getLookAt(),
						new Vector3f(rotX + 0.0001f, rotY, rotZ), new Vector3f(.05f, .05f, .05f));
			}
		}
	}
	
	/**
	 * checks for and applied health regen
	 */
	private void checkRegen(){
		if(hp<MAX_HP && System.nanoTime()-lastHit > 5*ONE_SECOND && System.nanoTime()-lastRegen > ONE_SECOND){
			hp++;
			lastRegen = System.nanoTime();
			//maybe play a sound
		}
	}

	/**
	 * simulates getting hit
	 */
	public void getHit() {
		hp--;
		MainGameLoop.audH.playAudio(getHitSFX, position, position);
		for(int i = 0; i < 20; i++){
			MainGameLoop.mapManager.addParticleEntity(TexturedModelMaker.basicCube,new Vector3f(position.x,position.y,position.z));
		}
		if (hp <= 0) {
			MainGameLoop.setState("gameover");
		}
	}

	/**
	 * checks to see if the camera is below the map
	 */
	private void fallCheck() {
		// if you fall out of the map, game over

		if (position.y < -50) {
			MainGameLoop.setState("gameover");
		}
	}

	public Vector3f getLookAt() {
		Vector3f lookAt = MatrixMath.rotateVector(new Vector3f(rotX, rotY, rotZ));
		if (lookAt.length() > 0) {
			return lookAt;
		}
		return new Vector3f(1, 0, 0);

	}

	public Vector3f getPosition() {
		return new Vector3f(position.x, position.y + 1f, position.z);
	}

	public void setPosition(Vector3f newPosition) {
		position = newPosition;
	}

	public float getRotX() {
		return rotX;
	}

	public float getRotY() {
		return rotY;
	}

	public float getRotZ() {
		return rotZ;
	}
	
	public float getHP() {
		return hp;
	}
	
	public float getMaxHP() {
		return MAX_HP;
	}

}
