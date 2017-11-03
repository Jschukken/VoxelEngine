package RenderEngine;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import Models.RawModel;

/**
 * Takes in obj file and return it as a RawModel
 * @author Berk A.
 *
 */
public class OBJLoader {

	/**
	 * Takes in obj file get the data of the file and return it as a RawModel
	 * 
	 * @return RawModel of the obj file.
	 */
	public static RawModel loadObjModel(String fileName, Loader loader){
		/*
		 * Get file information, read the file
		 */
		FileReader fr = null;
		try{
		fr = new FileReader(new File("resources/res/" + fileName + ".obj"));
		
		}catch (FileNotFoundException e){
			System.err.println("Couldn't load file!");
			e.printStackTrace();
			System.exit(-1);
		}
		
		BufferedReader reader = new BufferedReader(fr);
		String line;
		List<Vector3f> vertices =new ArrayList<Vector3f>();
		List<Vector2f> textures = new ArrayList<Vector2f>();
		List<Vector3f> normals = new ArrayList<Vector3f>();
		List<Integer> indices =new ArrayList<Integer>();
		float[] verticesArray = null;
		float[] normalsArray = null;
		float[] textureArray = null;
		int[] indicesArray = null;
		try{
			
			
			//Infinate loop until we break out to get all of the information of the obj file
			while(true){
				line = reader.readLine();
				String[] currentLine = line.split(" ");
				//if the line is a vertex possition
				if(line.startsWith("v ")){
					Vector3f vertex = new Vector3f(Float.parseFloat(currentLine[1]), Float.parseFloat(currentLine[2]), Float.parseFloat(currentLine[3]));
					vertices.add(vertex);
					//if the line is a texture coordinate
				}else if(line.startsWith("vt ")){
					Vector2f texture = new Vector2f(Float.parseFloat(currentLine[1]), Float.parseFloat(currentLine[2]));
					textures.add(texture);
					// if the line is a normal
				}else if(line.startsWith("vn")){
					Vector3f normal = new Vector3f(Float.parseFloat(currentLine[1]), Float.parseFloat(currentLine[2]), Float.parseFloat(currentLine[3]));
					normals.add(normal);
					//if the line is a face
				}else if(line.startsWith("f")){
					//Set up arrays since we know the size of the needed arrays
					textureArray = new float[vertices.size()*2];
					normalsArray = new float[vertices.size()*3];
					break;
				}
			}
			
			while(line != null){
				if(!line.startsWith("f ")){
					line = reader.readLine();
					continue;
				}
				//Split the line to three, one for each vertex
				String[] currentLine = line.split(" ");
				String[] vertex1 = currentLine[1].split("/");
				String[] vertex2 = currentLine[2].split("/");
				String[] vertex3 = currentLine[3].split("/");
				
				
				processVertex(vertex1, indices, textures, normals, textureArray,normalsArray);
				processVertex(vertex2, indices, textures, normals, textureArray,normalsArray);
				processVertex(vertex3, indices, textures, normals, textureArray,normalsArray);
				line = reader.readLine();
			}
			// Finished reading the file
			reader.close();
			
		}catch(Exception e){
			e.printStackTrace();
		}
		// Convert the vertexlist to a vertexarray
		verticesArray = new float[vertices.size()*3];
		indicesArray = new int[indices.size()];
		int vertexPointer = 0;
		for(Vector3f vertex:vertices){
			verticesArray[vertexPointer++] = vertex.x/5;
			verticesArray[vertexPointer++] = vertex.y/5;
			verticesArray[vertexPointer++] = vertex.z/5;
		}
		
		for(int i =0; i< indices.size(); i++){
			indicesArray[i] = indices.get(i);
		}
		// Return the loaded model (RawModel)
		return loader.loadToVao(verticesArray, indicesArray, textureArray, normalsArray);
		
	}
	
	/*
	 * Method to process each vertex (to get rid of duplicate code since we have to perform the same task for each vertex)
	 */
	 
	private static void processVertex(String[] vertexData, List<Integer> indices, List<Vector2f> textures, List<Vector3f> normals, float[] textureArray, float[] normalsArray){
		//Index of the vertex position in the vertex positions list (We have to substract one since obj file start at one and our array start at 0)
		int currentVertexPointer = Integer.parseInt(vertexData[0]) - 1;
		indices.add(currentVertexPointer);
		//Get the texture that corresponds to this vertex
		Vector2f currentTex = textures.get(Integer.parseInt(vertexData[1]) -1);
		textureArray[currentVertexPointer*2] = currentTex.x;
		//1 - currentTex since opengl starts top left and blender start fromt he bottom left
		textureArray[currentVertexPointer*2] = 1 - currentTex.y;
		//Get the normal vector that corresponsds to this vertex
		Vector3f currentNorm = normals.get(Integer.parseInt(vertexData[2])-1);
		normalsArray[currentVertexPointer*3] =currentNorm.x;
		normalsArray[currentVertexPointer*3+1] =currentNorm.y;
		normalsArray[currentVertexPointer*3+2] =currentNorm.z;
		
		
	}
	
}
