package Models;

import Textures.ModelTexture;

/**
 * models a textured model, and entity without position or transformations
 * 
 * @author Jelle Schukken
 *
 */
public class TexturedModel {

	private RawModel model;
	private ModelTexture texture;

	public TexturedModel(RawModel model, ModelTexture texture) {
		this.model = model;
		this.texture = texture;

	}

	public RawModel getModel() {
		return model;
	}

	public ModelTexture getTexture() {
		return texture;
	}

	public void setTexture(ModelTexture texture) {
		this.texture = texture;
	}

}
