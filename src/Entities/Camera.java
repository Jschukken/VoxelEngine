package Entities;

import java.nio.IntBuffer;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;

import Flashlight.MainGameLoop;
import GameEngine.CollisionHandler;
import ToolBox.TexturedModelMaker;

/**
 * The camera object TODO: turn into player
 * 
 * @author Jelle Schukken
 *
 */
public class Camera {

	private static float FRICTION = 4;
	public static final boolean GRAVITY = true;

	private Vector3f position;
	private float rotX, rotY, rotZ;
	private float dx = 0, dy = 0, dz = 0;

	private float acceleration = 0.01f;
	private float maxSpeed = 0.09f;
	private float jumpHeight = 0.15f;
	private float turnSpeed = 0.1f;
	private float currSpeed;
	private float strafe;

	private int hp;

	private IntBuffer[] jumpSFX;

	public Camera(Vector3f position, float rotX, float rotY, float rotZ) {
		super();
		this.position = position;
		this.rotX = rotX;
		this.rotY = rotY;
		this.rotZ = rotZ;
		jumpSFX = MainGameLoop.audH.createSound("jump");
		hp = 10;
	}

	/**
	 * takes user input to move the position and rotation of camera
	 */
	public void move() {

		if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
			maxSpeed = 0.4f;
		} else {
			maxSpeed = 0.09f;
		}

		if (Mouse.isGrabbed()) {

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

			if (Keyboard.isKeyDown(Keyboard.KEY_SPACE) && (CollisionHandler
					.checkPlayerCollision(new Vector3f(position.x, position.y + dy, position.z))
					|| CollisionHandler.protectedZones(new Vector3f(position.x, position.y + dy, position.z)))) {
				dy = jumpHeight;
				MainGameLoop.audH.playAudio(jumpSFX, position, position);
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

			// maths to make sure the hypotinus of movement is at most max speed
			// this was a massive pain in the ass btw
			double dx2 = Math.pow(dx, 2);
			double dz2 = Math.pow(dz, 2);
			double o = dx2 + dz2 - Math.pow(maxSpeed, 2);

			if (o > 0) {
				dx = Math.signum(dx) * (float) Math.sqrt(dx2 - (o * dx2 / (dx2 + dz2)));
				dz = Math.signum(dz) * (float) Math.sqrt(dz2 - (o * dz2 / (dx2 + dz2)));
			}

			// collision handeling
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

			if (GRAVITY) {
				dy = Math.max(dy - 0.01f, -maxSpeed * 4);
			}

			fallCheck();

			if (Mouse.isButtonDown(0)) {
				attack();
			}

			// System.out.println("\f (" + position.x + ", " + position.z + ",
			// "+ position.y + ") ");

		}

	}

	private void attack() {
		MainGameLoop.mapManager.addAttackEntity(TexturedModelMaker.basicCube,
				new Vector3f(position.x, position.y + .7f, position.z), getLookAt(), new Vector3f(rotX, rotY, rotZ));
	}

	public void getHit() {
		hp--;
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
		return new Vector3f((float) (Math.cos(Math.toRadians(rotY + 90)) * Math.cos(Math.toRadians(rotX))),
				(float) (Math.sin(Math.toRadians(rotX))),
				(float) (Math.cos(Math.toRadians(rotY)) * Math.cos(Math.toRadians(rotX))));

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

}
