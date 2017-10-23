package RenderEngine;

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
import Guis.GuiTexture;
import Models.RawModel;
import Shaders.GuiShader;
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
	private List<GuiTexture> levels = new ArrayList<GuiTexture>();

	private long old = 0; // timestamp in ms for comparison with current time
	private int time = 0; // time that has passed in the current level
	private int lvl = 0; // current difficulty level
	private final int DURATION = 5;

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
		int countdown = DURATION - time;
		String sec = String.valueOf(countdown % 60); // number of seconds since game start
		String min = String.valueOf(countdown / 60); // number of minutes since game start

		if (sec.length() == 1)
			sec = "0" + sec;
		if (min.length() == 1)
			min = "0" + min;

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

	/**
	 * Berk Chiel Timo please do comments for the rest of this file
	 */
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

	public void createHUD() {

		ModelTexture modelTex = new ModelTexture(loader.loadTexture("blue"));
		GuiTexture gui2 = new GuiTexture(modelTex, new Vector2f(0.6f, 0.950f), new Vector2f (0.4f, 0.050f));
		guis.add(gui2);
		gui2 = new GuiTexture(modelTex, new Vector2f(-0.6f, -0.950f), new Vector2f(0.4f, 0.050f));
		guis.add(gui2);
		// End Point HP HUD
		ModelTexture ducker3 = new ModelTexture(loader.loadTexture("red"));
		GuiTexture gui3 = new GuiTexture(ducker3, new Vector2f(0.6f, 0.950f), new Vector2f(0.399f, 0.049f)) {
			@Override
			// adjusts scale and repositions to keep right edge in the same position
			public void update() {
				float scale = (float) MainGameLoop.mapManager.destination.getHP()
						/ (float) MainGameLoop.mapManager.destination.getMaxHP();
				float posX = (float) this.getStartPosition().x - this.getMaxScale().x * (1 - scale);
				float posY = (float) this.getPosition().y;
				this.setPosition(new Vector2f(posX, posY));
				this.setScale(new Vector2f(this.getMaxScale().x * scale, this.getScale().y));
			}
		};

		// Player HP HUD
		ModelTexture ducker4 = new ModelTexture(loader.loadTexture("red"));
		GuiTexture gui4 = new GuiTexture(ducker4, new Vector2f(-0.6f, -0.950f), new Vector2f(0.399f, 0.049f)) {
			@Override
			public void update() {
				// adjusts scale and repositions to keep right edge in the same position
				// copy from end point HD which is tested, this one is not though
				float scale = (float) MainGameLoop.mapManager.camera.getHP()
						/ (float) MainGameLoop.mapManager.camera.getMaxHP();
				float posX = (float) this.getStartPosition().x - this.getMaxScale().x * (1 - scale);
				float posY = (float) this.getPosition().y;
				this.setPosition(new Vector2f(posX, posY));
				this.setScale(new Vector2f(this.getMaxScale().x * scale, this.getScale().y));

			}
		};

		ModelTexture ducker = new ModelTexture(loader.loadTexture("diffLevelOne"));
		GuiTexture gui = new GuiTexture(ducker, new Vector2f(-0.6f, 0.95f), new Vector2f(0.4f, 0.05f));

		guis.add(gui);
		guis.add(gui3);
		guis.add(gui4);
		
		levels.add(gui);

		ducker = new ModelTexture(loader.loadTexture("diffLevelTwo"));
		gui = new GuiTexture(ducker, new Vector2f(-0.6f, 0.95f), new Vector2f(0.4f, 0.05f));
		levels.add(gui);
		
		ducker = new ModelTexture(loader.loadTexture("diffLevelThree"));
		gui = new GuiTexture(ducker, new Vector2f(-0.6f, 0.95f), new Vector2f(0.4f, 0.05f));
		levels.add(gui);

		ducker = new ModelTexture(loader.loadTexture("diffLevelFour"));
		gui = new GuiTexture(ducker, new Vector2f(-0.6f, 0.95f), new Vector2f(0.4f, 0.05f));
		levels.add(gui);

		ducker = new ModelTexture(loader.loadTexture("diffLevelFive"));
		gui = new GuiTexture(ducker, new Vector2f(-0.6f, 0.95f), new Vector2f(0.4f, 0.05f));
		levels.add(gui);

		ducker = new ModelTexture(loader.loadTexture("diffLevelSix"));
		gui = new GuiTexture(ducker, new Vector2f(-0.6f, 0.95f), new Vector2f(0.4f, 0.05f));
		levels.add(gui);

		ducker = new ModelTexture(loader.loadTexture("diffLevelSeven"));
		gui = new GuiTexture(ducker, new Vector2f(-0.6f, 0.95f), new Vector2f(0.4f, 0.05f));
		levels.add(gui);

		ducker = new ModelTexture(loader.loadTexture("diffLevelEight"));
		gui = new GuiTexture(ducker, new Vector2f(-0.6f, 0.95f), new Vector2f(0.4f, 0.05f));
		levels.add(gui);

		ducker = new ModelTexture(loader.loadTexture("diffLevelNine"));
		gui = new GuiTexture(ducker, new Vector2f(-0.6f, 0.95f), new Vector2f(0.4f, 0.05f));
		levels.add(gui);

		ducker = new ModelTexture(loader.loadTexture("diffLevelTen"));
		gui = new GuiTexture(ducker, new Vector2f(-0.6f, 0.95f), new Vector2f(0.4f, 0.05f));
		levels.add(gui);

		ducker = new ModelTexture(loader.loadTexture("diffLevelEleven"));
		gui = new GuiTexture(ducker, new Vector2f(-0.6f, 0.95f), new Vector2f(0.4f, 0.05f));
		levels.add(gui);

		ducker = new ModelTexture(loader.loadTexture("diffLevelTwelve"));
		gui = new GuiTexture(ducker, new Vector2f(-0.6f, 0.95f), new Vector2f(0.4f, 0.05f));
		levels.add(gui);

		ducker = new ModelTexture(loader.loadTexture("diffLevelThirteen"));
		gui = new GuiTexture(ducker, new Vector2f(-0.6f, 0.95f), new Vector2f(0.4f, 0.05f));
		levels.add(gui);

		ducker = new ModelTexture(loader.loadTexture("diffLevelFourteen"));
		gui = new GuiTexture(ducker, new Vector2f(-0.6f, 0.95f), new Vector2f(0.4f, 0.05f));
		levels.add(gui);

		ducker = new ModelTexture(loader.loadTexture("diffLevelFifteen"));
		gui = new GuiTexture(ducker, new Vector2f(-0.6f, 0.95f), new Vector2f(0.4f, 0.05f));
		levels.add(gui);
/*
		ducker = new ModelTexture(loader.loadTexture("diffLevelSixteen"));
		gui = new GuiTexture(ducker, new Vector2f(-0.6f, 0.9f), new Vector2f(0.4f, 0.1f));
		levels.add(gui);

		ducker = new ModelTexture(loader.loadTexture("diffLevelSeventeen"));
		gui = new GuiTexture(ducker, new Vector2f(-0.6f, 0.9f), new Vector2f(0.4f, 0.1f));
		levels.add(gui);

		ducker = new ModelTexture(loader.loadTexture("diffLevelEighteen"));
		gui = new GuiTexture(ducker, new Vector2f(-0.6f, 0.9f), new Vector2f(0.4f, 0.1f));
		levels.add(gui);*/

		
		// create 5 slots in the timer list to be overwritten by the proper textures.
		timer.add(gui);
		timer.add(gui);
		timer.add(gui);
		timer.add(gui);
		timer.add(gui);
	}

	public void updateTimer() {
		//set all variables related to time and difficulty
		timeUpdate();
		String digits = timeConversion();
		if (digits.equals("00:00")) {
			time = 0;
			lvl++;
			if (lvl < levels.size()-1) {
				//System.out.println(lvl);
				guis.set(2, levels.get(lvl));
			}
		}
		
		ModelTexture mTex;
		GuiTexture gui;

		for (int i = 0; i < digits.length(); i++) {
			// TODO: Update pos vector
			// GuiTexture gui2 = new GuiTexture(ducker2 , new Vector2f(0f, 0.9f), new
			// Vector2f(0.15f, 0.1f));

			
			  switch (digits.charAt(i)) {
			  case '0':
				  mTex = new ModelTexture(loader.loadTexture("nrZero"));
				  break;
			  case '1':
				  mTex = new ModelTexture(loader.loadTexture("nrOne"));
				  break;
			  case '2':
				  mTex = new ModelTexture(loader.loadTexture("nrTwo"));
				  break;
			  case '3':
				  mTex = new ModelTexture(loader.loadTexture("nrThree"));
				  break;
			  case '4':
				  mTex = new ModelTexture(loader.loadTexture("nrFour"));
				  break;
			  case '5':
				  mTex = new ModelTexture(loader.loadTexture("nrFive"));
				  break;
			  case '6':
				  mTex = new ModelTexture(loader.loadTexture("nrSix"));
				  break;
			  case '7':
				  mTex = new ModelTexture(loader.loadTexture("nrSeven"));
				  break;
			  case '8':
				  mTex = new ModelTexture(loader.loadTexture("nrEight"));
				  break;
			  case '9':
				  mTex = new ModelTexture(loader.loadTexture("nrNine"));
				  break;
			  case ':': 
				  mTex = new ModelTexture(loader.loadTexture("symColon"));
				  break;
			  default:
				  mTex = new ModelTexture(loader.loadTexture("duck"));
				  break;
			}
			//mTex = new ModelTexture(loader.loadTexture("Quit Button"));
			gui = new GuiTexture(mTex, new Vector2f(-0.120f + (i * 0.06f), 0.95f), new Vector2f(0.03f, 0.05f));
			timer.set(i, gui);
			  
		}
	}

	public void cleanUp() {
		guishader.cleanUp();
	}
}
