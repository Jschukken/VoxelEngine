package Entities;

import org.lwjgl.util.vector.Vector3f;

import Models.TexturedModel;
import ToolBox.MatrixMath;

/**
 * Models a complete entity (shape texture location)
 * @author Jelle Schukken
 *
 */
public class Entity {

	private TexturedModel model;

	private Vector3f position;
	protected Vector3f direction;
	private float rotX;
	private float rotY;
	private float rotZ;
	private Vector3f scale;

	public Entity(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ, Vector3f scale) {
		super();
		this.model = model;
		this.position = position;
		this.rotX = rotX;
		this.rotY = rotY;
		this.rotZ = rotZ;
		direction = MatrixMath.rotateVector(new Vector3f((float) Math.toDegrees(rotX), (float) Math.toDegrees(-rotY), (float) Math.toDegrees(-rotZ))); 
		this.scale = scale;
	}

	// ----------------GETTERS AND SETTERS------------------------
	public void setModel(TexturedModel model) {
		this.model = model;
	}

	public void setPosition(Vector3f position) {
		this.position = position;
	}

	public void setRotX(float rotX) {
		this.rotX = rotX;
	}

	public void setRotY(float rotY) {
		this.rotY = rotY;
	}

	public void setRotZ(float rotZ) {
		this.rotZ = rotZ;
	}

	public void setScale(Vector3f scale) {
		this.scale = scale;
	}

	public TexturedModel getModel() {
		return model;
	}

	public Vector3f getPosition() {
		return position;
	}
	
	public Vector3f getCollisionPosition(){
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

	public Vector3f getScale() {
		return scale;
	}
	
	public void update(){
		direction = MatrixMath.rotateVector(new Vector3f((float) Math.toDegrees(rotX), (float) Math.toDegrees(-rotY), 0)); 
		direction.normalise();
		
	}
	
	public Vector3f getDirection(){
		return direction;
	}
	
	public void getHit(){
		
	}
	
	public void destroy(){
		
		
	}

}
