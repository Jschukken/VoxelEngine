package Entitys;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;

public class Camera {
	
	private Vector3f position;
	private float rotX, rotY, rotZ;
	
	private float speed = 0.1f;
	private float turnSpeed = 0.1f;
	private float currSpeed;
	private float straf;
	
	
	public Camera(Vector3f position, float rotX, float rotY, float rotZ) {
		super();
		this.position = position;
		this.rotX = rotX;
		this.rotY = rotY;
		this.rotZ = rotZ;
	}
	
	public void move(){
		
		if(Keyboard.isKeyDown(Keyboard.KEY_SPACE)){
			speed = 0.4f;
		}else{
			speed = 0.1f;
		}
		
		if(Mouse.isGrabbed()){
			
			if(Keyboard.isKeyDown(Keyboard.KEY_W)&&!Keyboard.isKeyDown(Keyboard.KEY_S)){
				currSpeed = -speed;
			}else if(Keyboard.isKeyDown(Keyboard.KEY_S)&&!Keyboard.isKeyDown(Keyboard.KEY_W)){
				currSpeed = speed;
			}else{
				currSpeed = 0;
			}
			if(Keyboard.isKeyDown(Keyboard.KEY_A)&&!Keyboard.isKeyDown(Keyboard.KEY_D)){
				straf = -speed;
			}else if(Keyboard.isKeyDown(Keyboard.KEY_D)&&!Keyboard.isKeyDown(Keyboard.KEY_A)){
				straf = speed;
			}else{
				straf = 0;
			}
			
			
			
			rotX += -Mouse.getDY() * turnSpeed;
			rotY += Mouse.getDX() * turnSpeed;
			
			float dy = (float)(currSpeed*Math.sin(Math.toRadians(rotX)));
			float dx = (float)-(currSpeed*Math.sin(Math.toRadians(rotY)))*(speed-Math.abs(dy))/speed;
			float dz = (float)(currSpeed*Math.cos(Math.toRadians(rotY)))*(speed-Math.abs(dy))/speed;
			
			dx += (float)(straf*Math.sin(Math.toRadians(rotY + 90)));
			dz += (float)-(straf*Math.cos(Math.toRadians(rotY + 90)));
			
			position.x += dx;
			position.y += dy;
			position.z += dz;
			
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
