package Guis;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;

import Flashlight.MainGameLoop;
import Models.RawModel;
import RenderEngine.Loader;
import Textures.ModelTexture;
import ToolBox.MatrixMath;

public class GuiRenderer {

	private RawModel quad;
	private GuiShader guishader;
	private Loader loader;
	private List<GuiTexture> guis = new ArrayList<GuiTexture>();
	private List<GuiTexture> timer = new ArrayList<GuiTexture>();
	
	public GuiRenderer(Loader load){
	float[] pos = {-1, 1, -1, -1, 1, 1, 1, -1};
	quad = load.loadToVao(pos);
	guishader = new GuiShader();
	loader = load;
	}
	
	public void render(){
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
		
		updateTimer();
		for(GuiTexture gui : timer) {
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
	
	public GuiTexture drawZero(Loader loader, Vector2f pos, Vector2f scale) {
		ModelTexture mTex = new ModelTexture(loader.loadTexture("Potato"));
		GuiTexture number = new GuiTexture(mTex , pos, scale);
		return number;
	}
	
	public void createHUD() {
		
		// Difficulty level HUD
		ModelTexture ducker = new ModelTexture(loader.loadTexture("Potato"));
		GuiTexture gui = new GuiTexture(ducker , new Vector2f(-0.6f, 0.9f), new Vector2f(0.4f, 0.1f)) {
			@Override
			public void update() {
				
			}
		};
		
		// Timer HUD
		ModelTexture ducker2 = new ModelTexture(loader.loadTexture("Potato"));
		GuiTexture gui2 = new GuiTexture(ducker2 , new Vector2f(0f, 0.9f), new Vector2f(0.15f, 0.1f));
		
		// End Point HP HUD
		ModelTexture ducker3 = new ModelTexture(loader.loadTexture("Potato"));
		GuiTexture gui3 = new GuiTexture(ducker3 , new Vector2f(0.6f, 0.9f), new Vector2f(0.4f, 0.1f)) {
			@Override
			public void update() {
				this.setScale(new Vector2f(this.getMaxScale().x*((float) MainGameLoop.mapManager.destination.getHealthPoints() / (float) MainGameLoop.mapManager.destination.getMaxHealthPoints()), this.getScale().y));
			}
		};
		
		// Player HP HUD
		ModelTexture ducker4 = new ModelTexture(loader.loadTexture("Potato"));
		GuiTexture gui4 = new GuiTexture(ducker4 , new Vector2f(-0.6f, -0.9f), new Vector2f(0.4f, 0.1f)) {
			@Override
			public void update() {
				this.setScale(new Vector2f(this.getMaxScale().x*((float) MainGameLoop.mapManager.camera.getHp() / (float) MainGameLoop.mapManager.camera.getMaxhp()), this.getScale().y));
			}
		};
		
		guis.add(gui);
		guis.add(gui3);
		guis.add(gui4);
		
	}
	
	public void updateTimer() {
		String digits = timer.toString();
		Vector2f pos = new Vector2f(0f, 0.9f);
		while (digits.length() < 4) {
			digits = "0" + digits;
		}
		for (int i = i; i < digits.length(); i++) {
			switch (digits.charAt(i)) {
				case 0:
					timer.set(i, drawZero(loader, pos, new Vector2f(Vector2f.add(pos, new Vector2f(i,0), pos), 0.01f)));
					
				case 1:
					timer[i]
			}
		}
	}
	
	
public void cleanUp(){
	guishader.cleanUp();
}
}
