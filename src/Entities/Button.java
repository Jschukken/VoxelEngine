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
	
	public float getLeftX() {
		return vertices[0];
	}
	
	public float getRightX() {
		return vertices[6];
	}
	
	public float getTopY() {
		return vertices[1];
	}
	
	public float getBotY() {
		return vertices[4];
	}
}
