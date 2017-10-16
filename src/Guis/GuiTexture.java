package Guis;

import org.lwjgl.util.vector.Vector2f;

import Textures.ModelTexture;

public class GuiTexture {

	private ModelTexture texture;
	private Vector2f position;
	private Vector2f scale;
	private Vector2f maxScale;
	
	public GuiTexture(ModelTexture texture, Vector2f position, Vector2f scale){
		this.texture = texture;
		this.position = position;
		this.scale = scale;
		this.maxScale = scale;
	}
	public int getTexture() {
		return texture.getTextureID();
	}
	public Vector2f getPosition() {
		return position;
	}
	public Vector2f getScale() {
		return scale;
	}
	public Vector2f getMaxScale() {
		return maxScale;
	}
	public void setScale(Vector2f vec) {
		scale = vec;
	}
	public void update() {
		
	}
}
