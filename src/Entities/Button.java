package Entities;

import Models.RawModel;
import Models.TexturedModel;
import Textures.ModelTexture;

public class Button extends TexturedModel {
	private float[] vertices;

	public Button(RawModel model, ModelTexture texture, float[] vert) {
		super(model, texture);
		vertices = vert;
	}
	
	/**
	 * Gets the X coordinate of the left edge of the button
	 * @return the X coordinate of the left edge as a float
	 */
	public float getLeftX() {
		return vertices[0];
	}
	
	/**
	 * Gets the X coordinate of the right edge of the button
	 * @return the X coordinate of the right edge as a float
	 */
	public float getRightX() {
		return vertices[6];
	}
	
	/**
	 * Gets the Y coordinate of the top edge of the button
	 * @return the Y coordinate of the top edge as a float
	 */
	public float getTopY() {
		return vertices[1];
	}
	
	/**
	 * Gets the Y coordinate of the bottom edge of the button
	 * @return the Y coordinate of the bottom edge as a float
	 */
	public float getBotY() {
		return vertices[4];
	}
}
