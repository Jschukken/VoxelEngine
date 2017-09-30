package Entities;

import Models.RawModel;
import Models.TexturedModel;
import Textures.ModelTexture;

public class Button extends TexturedModel {
	float[] vertices;

	public Button(RawModel model, ModelTexture texture, float[] vert) {
		super(model, texture);
		vertices = vert;
	}

}
