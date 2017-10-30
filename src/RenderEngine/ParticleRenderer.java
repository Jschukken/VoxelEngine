package RenderEngine;

import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;

import Entities.Entity;
import Models.RawModel;
import Models.TexturedModel;
import Shaders.ParticleShader;
import ToolBox.MatrixMath;

/**
 * renders particles
 * @author Lars Gevers
 *
 */
public class ParticleRenderer {

	private ParticleShader shader;
	
	
	public ParticleRenderer(ParticleShader shader, Matrix4f projectionMatrix){
		this.shader = shader;
		shader.start();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.stop();
	}
	
	/**
	 * renders entities with proper shading
	 * @param entities the entities to render
	 * @param toShadowSpace the transformation matrix to shadow map
	 */
	public void render(Map<TexturedModel, List<Entity>> entities){
		for (TexturedModel model : entities.keySet()){
			prepareTexturedModel(model);
			List<Entity> batch = entities.get(model);
			for (Entity entity : batch){
				prepareInstance(entity);
				GL11.glDrawElements(GL11.GL_TRIANGLES, model.getModel().getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
			}			
			unbindTexturedModel();
		}
	}
	
	/**
	 * prepare model in gpu
	 * @param the model to load
	 */
	private void prepareTexturedModel(TexturedModel model){
		RawModel rawModel = model.getModel();
		GL30.glBindVertexArray(rawModel.getVaoID());
		GL20.glEnableVertexAttribArray(0);// enable position array
		GL20.glEnableVertexAttribArray(1);// enable texture array
		GL20.glEnableVertexAttribArray(2);// enable normal array
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, model.getTexture().getTextureID());
		
	}
	
	/**
	 * unloads a model from gpu
	 */
	private void unbindTexturedModel(){
		GL20.glDisableVertexAttribArray(0);// disable position array
		GL20.glDisableVertexAttribArray(1);// disable texture array
		GL20.glDisableVertexAttribArray(2);// disable normal array
		GL30.glBindVertexArray(0);
	}
	
	/**
	 * creates and loads transformation matrix to shader
	 * @param entity the entity for which to create the matrix
	 */
	private void prepareInstance(Entity entity){
		Matrix4f transformationMatrix = MatrixMath.createTransMatrix(entity.getPosition(),entity.getRotX(),entity.getRotY(),entity.getRotZ(),entity.getScale());
		shader.loadTransformationMatrix(transformationMatrix);
		
	}
}
