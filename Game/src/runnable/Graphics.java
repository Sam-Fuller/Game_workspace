package runnable;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.GLFW_FALSE;
import static org.lwjgl.glfw.GLFW.GLFW_SAMPLES;
import static org.lwjgl.glfw.GLFW.GLFW_VISIBLE;
import static org.lwjgl.glfw.GLFW.glfwCreateWindow;
import static org.lwjgl.glfw.GLFW.glfwDefaultWindowHints;
import static org.lwjgl.glfw.GLFW.glfwDestroyWindow;
import static org.lwjgl.glfw.GLFW.glfwGetPrimaryMonitor;
import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwSetErrorCallback;
import static org.lwjgl.glfw.GLFW.glfwSetKeyCallback;
import static org.lwjgl.glfw.GLFW.glfwSetMouseButtonCallback;
import static org.lwjgl.glfw.GLFW.glfwShowWindow;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.glfw.GLFW.glfwSwapInterval;
import static org.lwjgl.glfw.GLFW.glfwTerminate;
import static org.lwjgl.glfw.GLFW.glfwWindowHint;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.system.MemoryUtil.NULL;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;

import colour.Colour;
import colour.ColourScheme;
import event.CursorMove;
import event.KeyPress;
import event.MouseButtonPress;
import graphics.ColourMesh;
import graphics.MeshBuilder;
import graphics.TexturedMesh;
import loader.TextLoader;
import main.Game;
import main.Settings;
import state.GameState;
import state.World;
import upgrade.UpgradeMenu;

/**
 * contains the graphics loop
 * updates output graphics
 * handles all opengl
 * 
 * @author Sam
 *
 */
public class Graphics implements Runnable {
	//game sizes, affects the number of blocks on the screen
	static final int gameSizeMultiplier = 10;
	static final int gameX = 16 * gameSizeMultiplier;
	static final int gameY = 9 * gameSizeMultiplier;
	
	static ColourScheme colourScheme = new ColourScheme();

	//the window to be rendered to
	static long window;
		
	/**
	 * @return the gameSizeMultiplier
	 */
	public static int getGameSizeMultiplier() {
		return gameSizeMultiplier;
	}
	
	/**
	 * @return the width of the game world
	 */
	public static int getGameX() {
		return gameX;
	}
	
	/**
	 * @return the height of the game world
	 */
	public static int getGameY() {
		return gameY;
	}
	
	/**
	 * @return the window to be rendered to
	 */
	public static long getWindow() {
		return window;
	}
	
	public static ColourScheme getColourScheme() {
		return colourScheme;
	}

	public static void setColourScheme(ColourScheme colourScheme) {
		Graphics.colourScheme = colourScheme;
	}

	/**
	 * initialise and run the graphics loop
	 * destroy all assets after use
	 */
	@Override
	public void run() {
		init();
		loop();

		// Free the window callbacks and destroy the window
		glfwFreeCallbacks(window);
		glfwDestroyWindow(window);

		// Terminate GLFW and free the error callback
		glfwTerminate();
		glfwSetErrorCallback(null).free();
	}

	/**
	 * initialise the game window
	 */
	private static void init() {
		// Setup an error callback. The default implementation
		// will print the error message in System.err.
		GLFWErrorCallback.createPrint(System.err).set();

		// Initialise GLFW. Most GLFW functions will not work before doing this.
		if (!glfwInit())
			throw new IllegalStateException("Unable to initialize GLFW");

		// Configure GLFW
		glfwDefaultWindowHints(); // optional, the current window hints are already the default
		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the window will stay hidden after creation
		glfwWindowHint(GLFW_SAMPLES, (int) Settings.settings.MSAA.getValue());


		// Create the window
		int width = (int) Settings.settings.xRes.getValue();		
		int height = (int) Settings.settings.yRes.getValue();

		long monitor = (long) Settings.settings.monitor.getValue();
		if (monitor == -1) monitor = glfwGetPrimaryMonitor();

		window = glfwCreateWindow(width, height, Game.gameName, monitor, NULL);
		if ( window == NULL )
			throw new RuntimeException("Failed to create the GLFW window");

		// Setup a key callback. It will be called every time a key is pressed, repeated or released.
		KeyPress.init();
		glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
			KeyPress.keyEvent(key, action);
		});

		MouseButtonPress.init();
		glfwSetMouseButtonCallback(window, (long window, int button, int action, int mods) -> {
			MouseButtonPress.keyEvent(button, action);
		});

		GLFW.glfwSetCursorPosCallback(window, (long window, double xpos, double ypos) -> {
			CursorMove.onMove(xpos, ypos);
		});

		// Make the OpenGL context current
		glfwMakeContextCurrent(window);
		// Enable v-sync
		glfwSwapInterval((int) Settings.settings.vSync.getValue());

		// Make the window visible
		glfwShowWindow(window);
		
		// This line is critical for LWJGL's interoperation with GLFW's
		// OpenGL context, or any context that is managed externally.
		// LWJGL detects the context that is current in the current thread,
		// creates the GLCapabilities instance and makes the OpenGL
		// bindings available for use.

		GL.createCapabilities();
		
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		
		GameState.loadTextures();
		TextLoader.init();
		
		ColourMesh.init();
		TexturedMesh.init();

		// Set the clear color
		Colour backgroundColour = colourScheme.background();
		glClearColor(backgroundColour.red, backgroundColour.green, backgroundColour.blue, 1f);

		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(0, Settings.settings.xRes.getValue(), Settings.settings.yRes.getValue(), 0, 1, -1);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
        		
		World.init();
		UpgradeMenu.init();
		
		GameState.changeState(GameState.State.MENU);
	}

	/**
	 * the main rendering game loop
	 */
	private static void loop() {
		long now = 0;
		long then = 0;
		float frameSpeed;
		
		// Run the rendering loop until the user has attempted to close
		while (!glfwWindowShouldClose(window)) {
//			try {
//				Thread.sleep(10);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}

			now = System.nanoTime();
			if (then == 0) then = now;
			frameSpeed = 60 * (now - then) / 1000000000.0f;

			if (frameSpeed == 0) continue;
			then = now;
			
			//if (frameSpeed > 10) frameSpeed = 10;
			
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer

			GameState.render(frameSpeed);

			glfwSwapBuffers(window);

			// Poll for window events. The key callback above will only be
			// invoked during this call.
			glfwPollEvents();
		}
		
		Game.cleanUp();
	}
	
	public static float resToScreenX(float x) {
		return (2*x/Settings.settings.xRes.getValue())-1;
	}
	public static float resToScreenY(float y) {
		return (2*y/Settings.settings.yRes.getValue())-1;
	}
	
	public static float screenToResX(float x) {
		return Settings.settings.xRes.getValue()*(x+1)/2;
	}
	public static float screenToResY(float y) {
		return Settings.settings.yRes.getValue()*(y+1)/2;
	}
	
	public static float gridToScreenX(float x) {
		return (2*x/gameX)-1;
	}
	public static float gridToScreenY(float y) {
		return (-2*y/gameY)+1;
	}
	
	public static float gridToScreenXScale(float x) {
		return (2*x/gameX);
	}
	public static float gridToScreenYScale(float y) {
		return (-2*y/gameY);
	}
	
	public static float resToGridx(float x) {
		return gameX*x/Settings.settings.xRes.getValue();
	}
	public static float resToGridy(float y) {
		return gameY*y/Settings.settings.yRes.getValue();
	}
}
