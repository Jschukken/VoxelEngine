package Guis;

import java.util.ArrayList;
import java.util.Arrays;
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

/**
 * Renders the GUI
 * 
 * @author Berk Aksakal
 * @edited Timo Aerts
 * @edited Chiel Ton
 */
public class GuiRenderer {

	private RawModel quad;
	private GuiShader guishader;
	private Loader loader;
	private List<GuiTexture> guis = new ArrayList<GuiTexture>();
	private List<GuiTexture> timer = new ArrayList<GuiTexture>();
	private int countdown = 0;

	private long old = 0; // timestamp in ms for comparison with current time
	private int time = 0; // time that has passed ingame

	public GuiRenderer(Loader load) {
		float[] pos = { -1, 1, -1, -1, 1, 1, 1, -1 };
		quad = load.loadToVao(pos);
		guishader = new GuiShader();
		loader = load;
	}

	/**
	 * takes the time and outputs it in the desired digital format
	 * 
	 * @return string giving time left in difficulty level as a string
	 */
	public String timeConversion() {
		String sec = String.valueOf(time%60);	//number of seconds since game start
		String min = String.valueOf(time/60);	//number of minutes since game start
		
		if(sec.length() == 1)
			sec = "0"+sec;
		if(min.length() == 1)
			min = "0"+min;

		return min + ":" + sec;
	}

	/**
	 * Calculates the time that is to be displayed Needs to be called at least once
	 * every second
	 * 
	 * @return the time that needs to be on the display as 4 integers
	 */
	public void timeUpdate() {
		long current = System.currentTimeMillis();
		if (current >= (old + 1000)) {
			old = current;
			time++;
		}
	}

	public void render() {
		guishader.start();
		GL30.glBindVertexArray(quad.getVaoID());
		GL20.glEnableVertexAttribArray(0);

		// alpha blending
		// GL11.glEnable(GL11.GL_BLEND);
		// GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		//
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		for (GuiTexture gui : guis) {
			gui.update();
			GL13.glActiveTexture(GL13.GL_TEXTURE0);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, gui.getTexture());
			Matrix4f matrix = MatrixMath.createTransformationMatrix(gui.getPosition(), gui.getScale());
			guishader.loadTransformation(matrix);
			GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, quad.getVertexCount());
		}

		updateTimer();

		for (GuiTexture gui : timer) {
			GL13.glActiveTexture(GL13.GL_TEXTURE0);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, gui.getTexture());
			Matrix4f matrix = MatrixMath.createTransformationMatrix(gui.getPosition(), gui.getScale());
			guishader.loadTransformation(matrix);
			GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, quad.getVertexCount());
		}
		// disable alphablending
		// GL11.glDisable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
		guishader.stop();
	}

	public GuiTexture drawZero(Loader loader, Vector2f pos, Vector2f scale) {
		ModelTexture mTex = new ModelTexture(loader.loadTexture("Potato"));
		GuiTexture number = new GuiTexture(mTex, pos, scale);
		return number;
	}

	public void createHUD() {

		// Difficulty level HUD
		ModelTexture ducker = new ModelTexture(loader.loadTexture("Potato"));
		GuiTexture gui = new GuiTexture(ducker, new Vector2f(-0.6f, 0.9f), new Vector2f(0.4f, 0.1f)) {
			@Override
			public void update() {

			}
		};

		// Timer HUD
		ModelTexture ducker2 = new ModelTexture(loader.loadTexture("Back Button"));
		// GuiTexture gui2 = new GuiTexture(ducker2 , new Vector2f(0f, 0.9f), new
		// Vector2f(0.15f, 0.1f));
		GuiTexture gui2 = new GuiTexture(ducker2, new Vector2f(-0.075f, 0.9f), new Vector2f(0.03f, 0.1f));

		// End Point HP HUD
		ModelTexture ducker3 = new ModelTexture(loader.loadTexture("Potato"));
		GuiTexture gui3 = new GuiTexture(ducker3, new Vector2f(0.6f, 0.9f), new Vector2f(0.4f, 0.1f)) {
			@Override
			// adjusts scale and repositions to keep right edge in the same position
			public void update() {
				float scale = (float) MainGameLoop.mapManager.destination.getHealthPoints()
						/ (float) MainGameLoop.mapManager.destination.getMaxHealthPoints();
				float posX = (float) this.getStartPosition().x - this.getMaxScale().x * (1 - scale);
				float posY = (float) this.getPosition().y;
				this.setPosition(new Vector2f(posX, posY));
				this.setScale(new Vector2f(this.getMaxScale().x * scale, this.getScale().y));
			}
		};

		// Player HP HUD
		ModelTexture ducker4 = new ModelTexture(loader.loadTexture("Potato"));
		GuiTexture gui4 = new GuiTexture(ducker4, new Vector2f(-0.6f, -0.9f), new Vector2f(0.4f, 0.1f)) {
			@Override
			public void update() {
				// adjusts scale and repositions to keep right edge in the same position
				// copy from end point HD which is tested, this one is not though
				float scale = (float) MainGameLoop.mapManager.camera.getHp()
						/ (float) MainGameLoop.mapManager.camera.getMaxhp();
				float posX = (float) this.getStartPosition().x - this.getMaxScale().x * (1 - scale);
				float posY = (float) this.getPosition().y;
				this.setPosition(new Vector2f(posX, posY));
				this.setScale(new Vector2f(this.getMaxScale().x * scale, this.getScale().y));

			}
		};

		guis.add(gui);
		// guis.add(gui2);
		guis.add(gui3);
		guis.add(gui4);

		timer.add(gui);
		timer.add(gui);
		timer.add(gui);
		timer.add(gui);
		timer.add(gui);

	}

	public void updateTimer() {
		timeUpdate();
		String digits = timeConversion();
		ModelTexture ducker2;
		GuiTexture gui;

		for (int i = 0; i < digits.length(); i++) {
			// TODO: Update pos vector
			ducker2 = new ModelTexture(loader.loadTexture("Square nrEight"));
			// GuiTexture gui2 = new GuiTexture(ducker2 , new Vector2f(0f, 0.9f), new
			// Vector2f(0.15f, 0.1f));
			gui = new GuiTexture(ducker2, new Vector2f(-0.120f + (i * 0.06f), 0.9f), new Vector2f(0.03f, 0.1f));
			timer.set(i, gui);

			/*
			 * switch (digits.charAt(i)) { // TODO: Call relevant draw function (Will still
			 * make those) template = drawNumber(loader, pos, scale); case '0':
			 * 
			 * 
			 * case '1':
			 * 
			 * 
			 * case '2':
			 * 
			 * 
			 * case '3':
			 * 
			 * 
			 * case '4':
			 * 
			 * 
			 * case '5':
			 * 
			 * 
			 * case '6':
			 * 
			 * 
			 * case '7':
			 * 
			 * 
			 * case '8':
			 * 
			 * 
			 * case '9':
			 * 
			 * 
			 * case ':': }
			 */
		}
	}

	public void cleanUp() {
		guishader.cleanUp();
	}
}
