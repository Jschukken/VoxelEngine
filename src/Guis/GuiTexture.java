package Guis;

import org.lwjgl.util.vector.Vector2f;

import Textures.ModelTexture;

/**
 * Contains data for one GUI texture
 * 
 * @author Berk Aksakal
 * @edited Timo Aerts
 * @edited Chiel Ton
 */
public class GuiTexture {

	private ModelTexture texture;
	private Vector2f position;
	private Vector2f scale;
	private Vector2f maxScale;
	private Vector2f startPosition;
	
	public GuiTexture(ModelTexture texture, Vector2f position, Vector2f scale){
		this.texture = texture;
		this.position = position;
		this.startPosition = position;
		this.scale = scale;
		this.maxScale = scale;
	}
	public int getTexture() {
		return texture.getTextureID();
	}
	public Vector2f getPosition() {
		return position;
	}
	public Vector2f getStartPosition() {
		return startPosition;
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
	public void setPosition(Vector2f vec) {
		position = vec;
	}
	public void update() {
		
	}
	public void setTexture(ModelTexture tex) {
		this.texture = tex;
	}
}
