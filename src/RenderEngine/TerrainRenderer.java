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
import Shaders.TerrainShader;
import Textures.ModelTexture;
import ToolBox.MatrixMath;

/**
 * renders entities
 * @author Jelle Schukken
 *
 */
public class TerrainRenderer {

	private TerrainShader shader;
	
	
	public TerrainRenderer(TerrainShader shader, Matrix4f projectionMatrix){
		this.shader = shader;
		shader.start();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.connectTextureUnits();
		shader.stop();
	}
	
	public void render(List<Entity> mapEntities, Matrix4f toShadowSpace){
		shader.loadToShadowSpaceMatrix(toShadowSpace);
		for (Entity mapEntity: mapEntities){
			prepareMap(mapEntity);
			loadModelMatrix(mapEntity);
			GL11.glDrawElements(GL11.GL_TRIANGLES, mapEntity.getModel().getModel().getVertexCount(), GL11.GL_UNSIGNED_INT, 0);

			unbindTexturedModel();
		}
	}
	
	private void prepareMap(Entity model){
		RawModel rawModel = model.getModel().getModel();
		GL30.glBindVertexArray(rawModel.getVaoID());
		GL20.glEnableVertexAttribArray(0);// enable position array
		GL20.glEnableVertexAttribArray(1);// enable texture array
		GL20.glEnableVertexAttribArray(2);// enable normal array
		ModelTexture texture = model.getModel().getTexture();
		texture.setShineDamper(100f);
		texture.setReflectivity(0.1f);
		shader.loadShineVariables(texture.getShineDamper(), texture.getReflectivity());
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, model.getModel().getTexture().getTextureID());
		
	}
	
	private void unbindTexturedModel(){
		GL20.glDisableVertexAttribArray(0);// disable position array
		GL20.glDisableVertexAttribArray(1);// disable texture array
		GL20.glDisableVertexAttribArray(2);// disable normal array
		GL30.glBindVertexArray(0);
	}
	
	private void loadModelMatrix(Entity entity){
		Matrix4f transformationMatrix = MatrixMath.createTransMatrix(entity.getPosition(),entity.getRotX(),entity.getRotY(),entity.getRotZ(),entity.getScale());
		shader.loadTransformationMatrix(transformationMatrix);
		
	}
}