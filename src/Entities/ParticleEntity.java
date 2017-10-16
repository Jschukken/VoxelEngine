package Entities;

import org.lwjgl.util.vector.Vector3f;

import Models.TexturedModel;

public class ParticleEntity extends Entity {

	private long time = System.nanoTime();

	private Vector3f speed = new Vector3f(0, 0, 0);
	private Vector3f position;

	public ParticleEntity(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ, Vector3f scale) {
		super(model, position, rotX, rotY, rotZ, scale);
		// TODO Auto-generated constructor stub
		this.position = position;
		speed.x = (float) (Math.random() - .5) / 8f;
		speed.y = (float) Math.random() / 8f;
		speed.z = (float) (Math.random() - .5) / 8f;
	}

	public void update() {
		super.update();
		// if(System.nanoTime()-time>1000000){
		// destroy();
		// }else{
		position.x += speed.x;
		position.y += speed.y;
		position.z += speed.z;
		speed.y -= 0.05 / 8f;
		// }
	}

	public void destroy() {
		// MainGameLoop
	}

}
