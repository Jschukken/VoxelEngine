package Entitys;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;

import GameEngine.CollisionHandler;

/**
 * The camera object TODO: turn into player
 * @author Jelle Schukken
 *
 */
public class Camera {
	
	private Vector3f position;
	private float rotX, rotY, rotZ;
	private float dx = 0,dy= 0,dz= 0;
	
	private float acceleration = 0.01f;
	private float maxSpeed = 0.1f;
	private float turnSpeed = 0.1f;
	private float currSpeed;
	private float strafe;
	
	
	public Camera(Vector3f position, float rotX, float rotY, float rotZ) {
		super();
		this.position = position;
		this.rotX = rotX;
		this.rotY = rotY;
		this.rotZ = rotZ;
	}
	
	/**
	 * takes user input to move the position and rotation of camera
	 */
	public void move(){
		
		if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)){
			maxSpeed = 0.4f;
		}else{
			maxSpeed = 0.1f;
		}
		
		if(Mouse.isGrabbed()){
			
			if(Keyboard.isKeyDown(Keyboard.KEY_W)&&!Keyboard.isKeyDown(Keyboard.KEY_S)){
				currSpeed = -acceleration;
			}else if(Keyboard.isKeyDown(Keyboard.KEY_S)&&!Keyboard.isKeyDown(Keyboard.KEY_W)){
				currSpeed = acceleration;
			}else{
				currSpeed = 0;
			}
			if(Keyboard.isKeyDown(Keyboard.KEY_A)&&!Keyboard.isKeyDown(Keyboard.KEY_D)){
				strafe = -acceleration;
			}else if(Keyboard.isKeyDown(Keyboard.KEY_D)&&!Keyboard.isKeyDown(Keyboard.KEY_A)){
				strafe = acceleration;
			}else{
				strafe = 0;
			}
			
			if(Keyboard.isKeyDown(Keyboard.KEY_SPACE)){
				dy = Math.min(maxSpeed, dy+0.1f);
			}
			
			
			
			rotX += -Mouse.getDY() * turnSpeed;
			rotY += Mouse.getDX() * turnSpeed;
			
			//dy = (float)(currSpeed*Math.sin(Math.toRadians(rotX)));
			dx += (float)-(currSpeed*Math.sin(Math.toRadians(rotY)));
			dz += (float)(currSpeed*Math.cos(Math.toRadians(rotY)));
			
			dx += (float)(strafe*Math.sin(Math.toRadians(rotY + 90)));
			dz += (float)-(strafe*Math.cos(Math.toRadians(rotY + 90)));
			
			double dx2 = Math.pow(dx,2);
			double dz2 = Math.pow(dz,2);
			double o = dx2+dz2-Math.pow(maxSpeed,2);

			if(o>0){
				dx = Math.signum(dx)*(float)Math.sqrt(dx2-(o*dx2/(dx2+dz2)));
				dz = Math.signum(dz)*(float)Math.sqrt(dz2-(o*dz2/(dx2+dz2)));
			}
			

			
			
			position.x += dx;
			if(CollisionHandler.checkCollision(position)){
				
				position.x -= dx;
				//dx = 0;

			}
			position.z += dz;
			if(CollisionHandler.checkCollision(position)){
				

				position.z -= dz;
				//dz = 0;

			}
			position.y += dy;
			if(CollisionHandler.checkCollision(position)){
				
				position.y -= dy;
				if(dy<0 && currSpeed == 0 && strafe == 0){
					dx -= dx/10;
					dz -= dz/10;
				}
				dy = 0;

			}
			
			dy -= 0.01f;
			
		}
		
	}


	public Vector3f getPosition() {
		return position;
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
