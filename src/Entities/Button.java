package Entities;

import Models.RawModel;
import Models.TexturedModel;
import Textures.ModelTexture;

public class Button extends TexturedModel {
	
	// the list of vertices indicating the button's location and size.
	private float[] vertices;
	
	// the two textures of a button. Inactive for when the mouse is not hovering over it. Active for when it is.
	private ModelTexture inactiveTex;
	private ModelTexture activeTex;

	public Button(RawModel model, ModelTexture tex1, ModelTexture tex2, float[] vert) {
		super(model, tex1);
		inactiveTex = tex1;
		activeTex = tex2;
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
	
	/**
	 * In the current state, acts as a dummy to override and to avoid errors.
	 * Should be overridden per instance of Button to signify what that specific button should do.
	 */
	public void onClick() {
		System.out.println("Ye forgot to override this ya dunce!");
	}
	
	/**
	 * Sets the current texture to the active texture
	 */
	public void activeTexture() {
		super.setTexture(activeTex);
	}
	
	/**
	 * Sets the current texture to the inactive texture
	 */
	public void inactiveTexture() {
		super.setTexture(inactiveTex);
	}
}
