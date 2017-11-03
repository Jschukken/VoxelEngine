package RenderEngine;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;

import Entities.Entity;
import Models.RawModel;
import Models.TexturedModel;
import Shaders.SkyboxShader;
import ToolBox.MatrixMath;

/**
 * renders the skybox Lars please add comments
 * @author Lars Gevers
 *
 */
public class SkyboxRenderer {

	private SkyboxShader shader;
	
	
	public SkyboxRenderer(SkyboxShader shader, Matrix4f projectionMatrix){
		this.shader = shader;
		shader.start();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.stop();
	}
	
	public void render(Entity skyBox){
		prepareTexturedModel(skyBox.getModel());
		prepareInstance(skyBox);
		GL11.glDrawElements(GL11.GL_TRIANGLES, skyBox.getModel().getModel().getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
		unbindTexturedModel();
	}
	
	private void prepareTexturedModel(TexturedModel model){
		RawModel rawModel = model.getModel();
		GL30.glBindVertexArray(rawModel.getVaoID());
		GL20.glEnableVertexAttribArray(0);// enable position array
		GL20.glEnableVertexAttribArray(1);// enable texture array
		GL20.glEnableVertexAttribArray(2);// enable normal array
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, model.getTexture().getTextureID());
		
	}
	
	private void unbindTexturedModel(){
		GL20.glDisableVertexAttribArray(0);// disable position array
		GL20.glDisableVertexAttribArray(1);// disable texture array
		GL20.glDisableVertexAttribArray(2);// disable normal array
		GL30.glBindVertexArray(0);
	}
	
	private void prepareInstance(Entity entity){
		Matrix4f transformationMatrix = MatrixMath.createTransMatrix(entity.getPosition(),entity.getRotX(),entity.getRotY(),entity.getRotZ(),entity.getScale());
		shader.loadTransformationMatrix(transformationMatrix);
		
	}
}
