package RenderEngine;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;

import Models.TexturedModel;
import Shaders.StaticShader;
import Shaders.StaticShaderMenu;
import ToolBox.MatrixMath;

/**
 * renders Textured Models
 * @author Timo Aerts
 *
 */

public class TexturedModelRenderer {

	

		public static void render(TexturedModel tex, StaticShaderMenu shader) {
			GL30.glBindVertexArray(tex.getModel().getVaoID());
			GL20.glEnableVertexAttribArray(0);// enable position array
			GL20.glEnableVertexAttribArray(1);// enable texture array
			
			//Creates transformationMatrix and applies
//			Matrix4f transformationMatrix = MatrixMath.createTransMatrix(button.getPosition(), 0f, 0f, 0f, button.getScale());
//			shader.loadTransformationMatrix(transformationMatrix);
			
			GL13.glActiveTexture(GL13.GL_TEXTURE0);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, tex.getTexture().getTextureID());
			GL11.glDrawElements(GL11.GL_TRIANGLES, tex.getModel().getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
			
			
			GL20.glDisableVertexAttribArray(0);// disable position array
			GL20.glDisableVertexAttribArray(1);// disable texture array
			GL30.glBindVertexArray(0);
		}
	}