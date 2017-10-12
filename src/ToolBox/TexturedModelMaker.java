package ToolBox;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.vector.Vector3f;

import Entities.Button;
import Models.RawModel;
import Models.TexturedModel;
import RenderEngine.DisplayManager;
import RenderEngine.Loader;
import RenderEngine.MasterMenuRenderer;
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

	// note: may want to remove the texturing here and do that in the map
	// loading, so the cube becomes more general
	/**
	 * creates a basic 1 by 1 cube
	 * 
	 * @param loader
	 *            the loader with which to load the cube
	 * @return the cube textured model
	 */
	public static TexturedModel cubeTexturedModel(Loader loader, String textureName) {

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
		float[] normals = GetNormals(vertices, indices);
		RawModel model = loader.loadToVao(vertices, indices, uv);
		ModelTexture texture = new ModelTexture(loader.loadTexture(textureName));
		basicCube = new TexturedModel(model, texture);
		return basicCube;
	}
	
	public static float[] GetNormals(float[] vertices, int[] indices){
		List<Vector3f> v_vertices = new ArrayList<Vector3f>();
		for (int i = 0; i < vertices.length; i += 3)
			v_vertices.add(new Vector3f(vertices[i], vertices[i + 1], vertices[i + 2]));
		
		float[] normals = new float[vertices.length];
		List<Vector3f> v_normals = new ArrayList<Vector3f>();
		for (int i = 0; i < indices.length - 3; i+= 3){
			Vector3f a = v_vertices.get(indices[i]);
			Vector3f b = v_vertices.get(indices[i + 1]);
			Vector3f c = v_vertices.get(indices[i + 2]);

			Vector3f ab = new Vector3f(a.x - b.x, a.y - b.y, a.z - b.z);
			Vector3f ac = new Vector3f(a.x - c.x, a.y - c.y, a.z - c.z);
			Vector3f normal = Vector3f.cross(ab, ac, null);
			v_normals.add(normal);
			v_normals.add(normal);
			System.out.append("AB (" + ab.x + ", " + ab.y + ", " + ab.z + ") AC(" + ac.x + ", " + ac.y + ", " + ac.z + ") Normal (" + normal.x + ", " + normal.y + ", " + normal.z + ") \n");
		}
		
		
		return normals;
	}

}
