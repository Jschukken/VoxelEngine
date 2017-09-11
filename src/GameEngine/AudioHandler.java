package GameEngine;

import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.openal.AL;
import org.lwjgl.openal.AL10;
import org.lwjgl.util.WaveData;
import org.lwjgl.util.vector.Vector3f;
/**
 * handles audio
 * @author Jelle Schukken
 *
 */
public class AudioHandler {

	private List<IntBuffer> sources = new ArrayList<IntBuffer>();
	private List<IntBuffer> buffers = new ArrayList<IntBuffer>();
	
	public AudioHandler(){
	    try{
	        AL.create();
	      } catch (LWJGLException le) {
	        le.printStackTrace();
	        return;
	      }
	}
	
	public IntBuffer[] createSound( String file){
			IntBuffer buffer = BufferUtils.createIntBuffer(1);
		 
		  IntBuffer source = BufferUtils.createIntBuffer(1);
		 
		  AL10.alGenBuffers(buffer);
		  buffers.add(buffer);
		  java.io.FileInputStream fin = null;
		 //try {
			  WaveData waveFile = WaveData.create(Class.class.getResourceAsStream("/res/" + file + ".wav"));
//		    } catch (IOException e) {
//		      e.printStackTrace();
//		      System.exit(-1);
//		    }
		   // WaveData waveFile = WaveData.create(Class.class.getResourceAsStream("/res/" + file + ".wav"));
			  if(waveFile == null){
				  System.exit(-1);
			  }
		    AL10.alBufferData(buffer.get(0), waveFile.format, waveFile.data, waveFile.samplerate);
		    waveFile.dispose();
		 
		    // Bind the buffer with the source.
		    AL10.alGenSources(source);
		    sources.add(source);
			AL10.alSourcei(source.get(0), AL10.AL_BUFFER,   buffer.get(0) );
		    AL10.alSourcef(source.get(0), AL10.AL_PITCH,    1.0f          );
		    AL10.alSourcef(source.get(0), AL10.AL_GAIN,     2.0f          );
		 
		    IntBuffer[] temp = {source,buffer};
		    return temp;
	}
	
	public void playAudio(IntBuffer[] audioID, Vector3f sourceP, Vector3f listenerP){
		FloatBuffer sourcePos = (FloatBuffer)BufferUtils.createFloatBuffer(3).put(new float[] { sourceP.x, sourceP.y, sourceP.z }).rewind();

	    AL10.alSource (audioID[0].get(0), AL10.AL_POSITION, sourcePos     );
	    

		FloatBuffer listenerPos = (FloatBuffer)BufferUtils.createFloatBuffer(3).put(new float[] { listenerP.x, listenerP.y, listenerP.z }).rewind();
	    
	    AL10.alListener(AL10.AL_POSITION,    listenerPos);
	    
	    AL10.alSourcePlay(audioID[0].get(0));
	}
	
	public void startSong(IntBuffer[] audioID){
		AL10.alSourcei(audioID[0].get(0), AL10.AL_LOOPING,     AL10.AL_TRUE          );
		AL10.alSourcePlay(audioID[0].get(0));
	}
	
	public void endSong(IntBuffer[] audioID){
		
		AL10.alSourceStop(audioID[0].get(0));
	}
	
	
	public void cleanUp(){
		for(IntBuffer source: sources){
			AL10.alDeleteSources(source);
		}
		for(IntBuffer buffer: buffers){
			AL10.alDeleteBuffers(buffer);
		}
		AL.destroy();
		
	}
}
