package Guis;

import java.util.List;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;

import Models.RawModel;
import RenderEngine.Loader;
import ToolBox.MatrixMath;

public class GuiRenderer {

	private RawModel quad;
	private GuiShader guishader;
	
	public GuiRenderer(Loader loader){
	float[] pos = {-1, 1, -1, -1, 1, 1, 1, -1};
	quad = loader.loadToVao(pos);
	guishader = new GuiShader();
	}
	
	public void render(List<GuiTexture> guis){
		guishader.start();
		GL30.glBindVertexArray(quad.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		
		//alpha blending
		//GL11.glEnable(GL11.GL_BLEND);
		//GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		//
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		for(GuiTexture gui: guis){
			gui.update();
			GL13.glActiveTexture(GL13.GL_TEXTURE0);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, gui.getTexture());
			Matrix4f matrix = MatrixMath.createTransformationMatrix(gui.getPosition(), gui.getScale());
			guishader.loadTransformation(matrix);
			GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, quad.getVertexCount());
		}
		
		//disable alphablending
		//GL11.glDisable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
		guishader.stop();
	}
	
	
	
public void cleanUp(){
	guishader.cleanUp();
}
}
