package audio;

import org.lwjgl.openal.*;
import static org.lwjgl.openal.AL.*;
import static org.lwjgl.openal.ALC.*;
import static org.lwjgl.openal.ALC10.*;

public class MusicPlayer {
	private static long device;
	private static long context;

	private static ALCCapabilities alcCapabilities;
	private static ALCapabilities alCapabilities;
	
	public static void init() {
		//acquire the default device
		String defaultDeviceName = alcGetString(0, ALC_DEFAULT_DEVICE_SPECIFIER);
		device = alcOpenDevice(defaultDeviceName);
		
		//create context using output device
		int[] attributes = {0};
		context = alcCreateContext(device, attributes);
		//make context default
		alcMakeContextCurrent(context);
		
		alcCapabilities = createCapabilities(device);
		alCapabilities = createCapabilities(alcCapabilities);
	}
	
	public static void destroy() {
		alcDestroyContext(context);
		alcCloseDevice(device);
	}
}