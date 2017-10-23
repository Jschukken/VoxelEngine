package Entities;

import org.lwjgl.util.vector.Vector3f;

/**
 * simulates a light object
 * @author Jelle Schukken
 *
 */
public class Light {

	private Vector3f position;
	private Vector3f direction;
	private Vector3f colour;
	private Vector3f attenuation = new Vector3f(1, 0, 0);
	
	public Light(Vector3f position, Vector3f colour) {
		this.position = position;
		this.colour = colour;
		direction = new Vector3f((float)Math.random()*2f-2f,-1,(float)Math.random()*2f-2f);
	}
	
	public Light(Vector3f position, Vector3f colour, Vector3f attenuation) {
		this.position = position;
		this.colour = colour;
		this.attenuation = attenuation;
		direction = new Vector3f((float)Math.random()*2f-2f,-1,(float)Math.random()*2f-2f);
	}
	
	
	//_______BASIC GETTERS AND SETTERS_________
	public Vector3f getDirection(){
		return direction;
	}
	
	public Vector3f getAttenuation(){
		return attenuation;
	}

	public Vector3f getPosition() {
		return position;
	}
	
	public void setPosition(Vector3f position) {
		this.position = position;
	}
	
	public Vector3f getColour() {
		return colour;
	}
	
	public void setColour(Vector3f colour) {
		this.colour = colour;
	}
	
	
}
