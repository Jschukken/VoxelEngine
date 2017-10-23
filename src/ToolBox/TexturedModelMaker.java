package ToolBox;

import Entities.Button;
import Models.RawModel;
import Models.TexturedModel;
import RenderEngine.DisplayManager;
import RenderEngine.Loader;
import RenderEngine.MasterMenuRenderer;
import RenderEngine.OBJLoader;
import RenderEngine.TexturedModelRenderer;
import Shaders.StaticShaderMenu;
import Textures.ModelTexture;

/**
 * helper class designed for static function that create models
 * 
 * @author Jelle Schukken
 *
 */
public class TexturedModelMaker {

	public static TexturedModel basicCube;
	public static TexturedModel skyBox;

	// note: may want to remove the texturing here and do that in the map
	// loading, so the cube becomes more general
	/**
	 * creates a basic 1 by 1 cube
	 * 
	 * @param loader
	 *            the loader with which to load the cube
	 * @return the cube textured model
	 */
	public static TexturedModel cubeTexturedModel(Loader loader, String textureFile) {

		float[] vertices = { -0.5f, 0.5f, -0.5f, -0.5f, -0.5f, -0.5f, 0.5f, -0.5f, -0.5f, 0.5f, 0.5f, -0.5f,

				-0.5f, 0.5f, 0.5f, -0.5f, -0.5f, 0.5f, 0.5f, -0.5f, 0.5f, 0.5f, 0.5f, 0.5f,

				0.5f, 0.5f, -0.5f, 0.5f, -0.5f, -0.5f, 0.5f, -0.5f, 0.5f, 0.5f, 0.5f, 0.5f,

				-0.5f, 0.5f, -0.5f, -0.5f, -0.5f, -0.5f, -0.5f, -0.5f, 0.5f, -0.5f, 0.5f, 0.5f,

				-0.5f, 0.5f, 0.5f, -0.5f, 0.5f, -0.5f, 0.5f, 0.5f, -0.5f, 0.5f, 0.5f, 0.5f,

				-0.5f, -0.5f, 0.5f, -0.5f, -0.5f, -0.5f, 0.5f, -0.5f, -0.5f, 0.5f, -0.5f, 0.5f

		};

		int[] indices = { 0, 1, 3, 3, 1, 2, 4, 5, 7, 7, 5, 6, 8, 9, 11, 11, 9, 10, 12, 13, 15, 15, 13, 14, 16, 17, 19,
				19, 17, 18, 20, 21, 23, 23, 21, 22 };

		float[] uv = { 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0, 1, 1, 1, 1, 0,
				0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0, 1, 1, 1, 1, 0

		};

		float[] normals = MatrixMath.CreateNormals(vertices, indices);
		RawModel model = loader.loadToVao(vertices, indices, uv, MatrixMath.CreateNormals(vertices, indices));
		model.setNormals(normals);
		ModelTexture texture = new ModelTexture(loader.loadTexture(textureFile));
		basicCube = new TexturedModel(model, texture);
		return basicCube;
	}
	
	public static TexturedModel robotModel(Loader loader) {

		RawModel model =  OBJLoader.loadObjModel("person", loader);
		ModelTexture texture =  new ModelTexture(loader.loadTexture("Tile"));
		return new TexturedModel(model, texture);
	}
	public static TexturedModel robotRunModel(Loader loader) {

		RawModel model =  OBJLoader.loadObjModel("torso", loader);
		ModelTexture texture =  new ModelTexture(loader.loadTexture("Tile"));
		return new TexturedModel(model, texture);
	}
	
	public static TexturedModel robotHitRunModel(Loader loader) {
		RawModel model =  OBJLoader.loadObjModel("torso", loader);
		ModelTexture texture = new ModelTexture(loader.loadTexture("red"));
		return new TexturedModel(model, texture);
	}
	
	public static TexturedModel robotHitNormalModel(Loader loader) {
		RawModel model =  OBJLoader.loadObjModel("person", loader);
		ModelTexture texture = new ModelTexture(loader.loadTexture("red"));
		return new TexturedModel(model, texture);
	}

	public static TexturedModel skyBoxModel(Loader loader) {

		float[] vertices = { -0.5f, 0.5f, -0.5f, -0.5f, -0.5f, -0.5f, 0.5f, -0.5f, -0.5f, 0.5f, 0.5f, -0.5f,

				-0.5f, 0.5f, 0.5f, -0.5f, -0.5f, 0.5f, 0.5f, -0.5f, 0.5f, 0.5f, 0.5f, 0.5f,

				0.5f, 0.5f, -0.5f, 0.5f, -0.5f, -0.5f, 0.5f, -0.5f, 0.5f, 0.5f, 0.5f, 0.5f,

				-0.5f, 0.5f, -0.5f, -0.5f, -0.5f, -0.5f, -0.5f, -0.5f, 0.5f, -0.5f, 0.5f, 0.5f,

				-0.5f, 0.5f, 0.5f, -0.5f, 0.5f, -0.5f, 0.5f, 0.5f, -0.5f, 0.5f, 0.5f, 0.5f,

				-0.5f, -0.5f, 0.5f, -0.5f, -0.5f, -0.5f, 0.5f, -0.5f, -0.5f, 0.5f, -0.5f, 0.5f

		};

		int[] indices = { 0, 1, 3, 3, 1, 2, 4, 5, 7, 7, 5, 6, 8, 9, 11, 11, 9, 10, 12, 13, 15, 15, 13, 14, 16, 17, 19,
				19, 17, 18, 20, 21, 23, 23, 21, 22 };

		float[] uv = { 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0, 1, 1, 1, 1, 0,
				0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0, 1, 1, 1, 1, 0

		};

		RawModel model = loader.loadToVao(vertices, indices, uv, MatrixMath.CreateNormals(vertices, indices));
		ModelTexture texture = new ModelTexture(loader.loadTexture("blue"));
		skyBox = new TexturedModel(model, texture);
		return skyBox;
	}


}
