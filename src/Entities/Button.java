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
		return Math.min(Math.min(vertices[0], vertices[3]), Math.min(vertices[6], vertices[9]));
	}
	
	/**
	 * Gets the X coordinate of the right edge of the button
	 * @return the X coordinate of the right edge as a float
	 */
	public float getRightX() {
		return Math.max(Math.max(vertices[0], vertices[3]), Math.max(vertices[6], vertices[9]));
	}
	
	/**
	 * Gets the Y coordinate of the top edge of the button
	 * @return the Y coordinate of the top edge as a float
	 */
	public float getTopY() {
		return Math.max(Math.max(vertices[1], vertices[4]), Math.max(vertices[7], vertices[10]));
	}
	
	/**
	 * Gets the Y coordinate of the bottom edge of the button
	 * @return the Y coordinate of the bottom edge as a float
	 */
	public float getBotY() {
		return Math.min(Math.min(vertices[1], vertices[4]), Math.min(vertices[7], vertices[10]));
	}
	
	public void onClick() {
		
	}
}
