package Entities;

import org.lwjgl.util.vector.Vector3f;

import Models.TexturedModel;
import RenderEngine.DisplayManager;

/**
 * simulates a particle
 * @author Jelle Schukken
 *
 */
public class ParticleEntity extends Entity {
	private Vector3f speed = new Vector3f(0, 0, 0);
	private Vector3f position;

	public ParticleEntity(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ, Vector3f scale) {
		super(model, position, rotX, rotY, rotZ, scale);
		this.position = position;
		speed.x = (float) ((Math.random() - .5) / 8f)* 60.0f/(float)DisplayManager.FPS_CAP;
		speed.y = (float) (Math.random() / 8f)* 60.0f/(float)DisplayManager.FPS_CAP;
		speed.z = (float) ((Math.random() - .5) / 8f)* 60.0f/(float)DisplayManager.FPS_CAP;
	}

	/**
	 * updates particles position
	 */
	public void update() {
		position.x += speed.x;
		position.y += speed.y;
		position.z += speed.z;
		speed.y -= (0.05 / 8f)* 60.0f/(float)DisplayManager.FPS_CAP;
	}
}
