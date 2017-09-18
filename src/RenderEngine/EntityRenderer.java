package RenderEngine;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;

import Entities.Entity;
import Shaders.StaticShader;
import ToolBox.MatrixMath;

/**
 * renders entities
 * @author Jelle Schukken
 *
 */
public class EntityRenderer {

	/**
	 * renders entities to the screen using a specific shader
	 * @param entity the entity to render
	 * @param shader the shader with which to render
	 */
	public static void render(Entity entity, StaticShader shader) {
		GL30.glBindVertexArray(entity.getModel().getModel().getVaoID());
		GL20.glEnableVertexAttribArray(0);// enable position array
		GL20.glEnableVertexAttribArray(1);// enable texture array
		
		//make transform matrix and apply
		Matrix4f transformationMatrix = MatrixMath.createTransMatrix(entity.getPosition(),entity.getRotX(),entity.getRotY(),entity.getRotZ(),entity.getScale());
		shader.loadTransformationMatrix(transformationMatrix);
		
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, entity.getModel().getTexture().getTextureID());
		GL11.glDrawElements(GL11.GL_TRIANGLES, entity.getModel().getModel().getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
		
		
		GL20.glDisableVertexAttribArray(0);// disable position array
		GL20.glDisableVertexAttribArray(1);// disable texture array
		GL30.glBindVertexArray(0);
	}
}
