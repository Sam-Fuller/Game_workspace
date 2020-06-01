package audio;

import org.lwjgl.system.*;

import java.nio.*;

import static org.lwjgl.openal.AL10.*;
import static org.lwjgl.stb.STBVorbis.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.libc.LibCStdlib.*;

public class RunBackground implements Runnable {
	private static final String fileName = "resources/sounds/example.ogg";

	@Override
	public void run() {
		ShortBuffer rawAudioBuffer;

		int channels;
		int sampleRate;

		try (MemoryStack stack = stackPush()) {
		    //Allocate space to store return information from the function
		    IntBuffer channelsBuffer   = stack.mallocInt(1);
		    IntBuffer sampleRateBuffer = stack.mallocInt(1);

		    rawAudioBuffer = stb_vorbis_decode_filename(fileName, channelsBuffer, sampleRateBuffer);

		    //Retreive the extra information that was stored in the buffers by the function
		    channels = channelsBuffer.get(0);
		    sampleRate = sampleRateBuffer.get(0);
		}
		
		//Find the correct OpenAL format
		int format = -1;
		if (channels == 1) {
		    format = AL_FORMAT_MONO16;
		} else if (channels == 2) {
		    format = AL_FORMAT_STEREO16;
		}
		
		//Request space for the buffer
		int bufferPointer = alGenBuffers();

		//Send the data to OpenAL
		alBufferData(bufferPointer, format, rawAudioBuffer, sampleRate);

		//Free the memory allocated by STB
		free(rawAudioBuffer);

		//Request a source
		int sourcePointer = alGenSources();

		//Assign the sound we just loaded to the source
		alSourcei(sourcePointer, AL_BUFFER, bufferPointer);

		//Play the sound
		alSourcePlay(sourcePointer);

		try {
		    Thread.sleep(150000);
		} catch (InterruptedException ignored) {
			
		}
		
		alDeleteSources(sourcePointer);
		alDeleteBuffers(bufferPointer);
	}
	
}
