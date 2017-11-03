package RenderEngine;

import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

import Models.RawModel;

/**
 * Loads data from cpu to gpu for rendering
 * @author Jelle Schukken
 *
 */
public class Loader {
	
	static List<Integer> vaos = new ArrayList<Integer>();
	static List<Integer> vbos = new ArrayList<Integer>();
	static List<Integer> textures = new ArrayList<Integer>();

	/**
	 * loads the model itself to the vertex array object in the gpu
	 * @param vertices the vertices
	 * @param indices the indices of the vertices
	 * @return the raw model
	 */
		public RawModel loadToVao(float[] vertices, int[] indices, float[] uv, float[] normals){
			
			int vaoID = createVao();
			storeDataInAttributeList(vertices, 0, 3);
			storeDataInAttributeList(uv, 1, 2);
			storeDataInAttributeList(normals, 2, 3);
			bindIndicesBuffer(indices);
			GL30.glBindVertexArray(0);
			return new RawModel(vaoID, indices.length);
			
		}
		
		public RawModel loadToVao(float[] positions){
			int vaoID = createVao();
			this.storeDataInAttributeList(positions, 0, 2);
			GL30.glBindVertexArray(0);
			return new RawModel(vaoID, positions.length/2);
		}
		
		/**
		 * creates the vao on the gpu
		 * @return the vao ID
		 */
		public int createVao(){
			int vaoID = GL30.glGenVertexArrays();
			vaos.add(vaoID);
			GL30.glBindVertexArray(vaoID);
			
			return vaoID;
			
		}
		
		/**
		 * loads texture from file and returns the texture id
		 * @param file the texture file, must be square and a .PNG (in all caps)
		 * @return
		 */
		public int loadTexture(String file){
			Texture texture = null;
			try {
				texture = TextureLoader.getTexture("PNG", Class.class.getResource("/res/" + file + ".png").openStream());
				GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
				GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println("missing texture");
				System.exit(-1);
			}
			
			int textureID = texture.getTextureID();
			textures.add(textureID);
			return textureID;
			
		}
		
		private void storeDataInAttributeList(float[] data, int attributeNumber, int dimentions){
			
			int vboID = GL15.glGenBuffers();
			vbos.add(vboID);
			GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER,vboID);
			FloatBuffer buffer = storeDataInFloatBuffer(data);
			GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
			GL20.glVertexAttribPointer(attributeNumber,dimentions,GL11.GL_FLOAT,false,0,0);
			GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER,0);
			
			
		}
		
		private void bindIndicesBuffer(int[] indices){
			
			int vboID = GL15.glGenBuffers();
			vbos.add(vboID);
			GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER,vboID);
			IntBuffer buffer = storeDataInIntBuffer(indices);
			GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
		}
		
		private IntBuffer storeDataInIntBuffer(int[] data){
			IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
			buffer.put(data);
			buffer.flip();
			return buffer;
		}
		
		private FloatBuffer storeDataInFloatBuffer(float[] data){
			FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
			buffer.put(data);
			buffer.flip();
			return buffer;
		}
		
		/**
		 * deletes all the created objects to prevent memory leaks
		 */
		public void cleanUp(){
			
			for(int vao : vaos){
				GL30.glDeleteVertexArrays(vao);
			}
			for(int vbo: vbos){
				GL15.glDeleteBuffers(vbo);
			}
			for(int texture: textures){
				GL11.glDeleteTextures(texture);
			}
			
		}
}
