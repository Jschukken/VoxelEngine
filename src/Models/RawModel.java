package Models;

/**
 * models a rawModel, the shape of an entity
 * @author Jelle Schukken
 *
 */
public class RawModel {

	int vaoID;
	int vertexCount;
	float[] normals;
	
	public RawModel(int vaoID, int vertexCount){
		
		this.vaoID= vaoID;
		this.vertexCount = vertexCount;
		
	}
	
	public int getVaoID(){
		return vaoID;
	}
	
	public int getVertexCount(){
		return vertexCount;
	}
	
	public float[] getNormals(){
		return normals;
	}
	
	public void setNormals(float[] normals){
		this.normals = normals;
	}
}
