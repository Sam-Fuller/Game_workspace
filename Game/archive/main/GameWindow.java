package main;

import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;

import blocks.Colour;
import entities.Player;
import event.CursorMoveEvent;
import event.KeyEventHandler;
import event.MouseButtonEventHandler;
import renderLayers.AlertLayer;
import renderLayers.EscapeMenuLayer;
import renderLayers.HUDLayer;
import renderLayers.MapLayer;
import renderLayers.TTF;
import renderLayers.textureLoader;
import settings.Settings;
import world.RunPhysics;
import world.World;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.*;

import org.lwjgl.opengl.GL;



public class GameWindow {
	//statics
	public static final int gameSizeMultiplier = 10;
	public static final int gameX = 16 * gameSizeMultiplier;
	public static final int gameY = 9 * gameSizeMultiplier;

	// The window handle
	public static long window;

	/**
	 * launches the game window
	 */
	public static void run() {
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
		if ( !glfwInit() )
			throw new IllegalStateException("Unable to initialize GLFW");

		// Configure GLFW
		glfwDefaultWindowHints(); // optional, the current window hints are already the default
		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the window will stay hidden after creation
		glfwWindowHint(GLFW_SAMPLES, Settings.multiSampling);


		// Create the window
		int width = Settings.resX;		
		int height = Settings.resY;

		long monitor = Settings.monitor;
		if (monitor == -1) monitor = glfwGetPrimaryMonitor();

		window = glfwCreateWindow(width, height, "Game", monitor, NULL);
		if ( window == NULL )
			throw new RuntimeException("Failed to create the GLFW window");

		// Setup a key callback. It will be called every time a key is pressed, repeated or released.
		glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
			KeyEventHandler.keyEvent(key, action);
		});

		glfwSetMouseButtonCallback(window, (long window, int button, int action, int mods) -> {
			MouseButtonEventHandler.keyEvent(button, action);
		});

		GLFW.glfwSetCursorPosCallback(window, (long window, double xpos, double ypos) -> {
			CursorMoveEvent.onMove(xpos, ypos);
		});

		// Make the OpenGL context current
		glfwMakeContextCurrent(window);
		// Enable v-sync
		glfwSwapInterval(Settings.vSync ? 1 : 0);

		// Make the window visible
		glfwShowWindow(window);
	}

	public static void loop() {	
		// This line is critical for LWJGL's interoperation with GLFW's
		// OpenGL context, or any context that is managed externally.
		// LWJGL detects the context that is current in the current thread,
		// creates the GLCapabilities instance and makes the OpenGL
		// bindings available for use.

		GL.createCapabilities();
		TTF.init();
		textureLoader.loadAllTextures();
		Main.WeaponFireThread.start();


		// Set the clear color
		glClearColor(0f, 0f, 0f, 0f);

		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(0, Settings.resX, Settings.resY, 0, 1, -1);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);

		long now = 0;
		long then = 0;
		float frameSpeed;

		// Run the rendering loop until the user has attempted to close
		// the window or has pressed the ESCAPE key.
		while ( !glfwWindowShouldClose(GameWindow.window) ) {
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
			
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer

			if (!RunPhysics.paused) World.player.calculatePosition(frameSpeed); //10000

			MapLayer.draw(); //2000000
			Player.draw(); //5000

			AlertLayer.draw(frameSpeed);
			EscapeMenuLayer.draw(); //10000
			HUDLayer.draw(); //500000

			glfwSwapBuffers(window); //500000 - 1000000

			// Poll for window events. The key callback above will only be
			// invoked during this call.
			glfwPollEvents();
		}
	}

	public static float actualX(float x) {
		return (x*Settings.resX)/gameX;
	}
	public static float actualY(float y) {
		return (y*Settings.resY)/gameY;
	}
	
	public static float gridX(float x) {
		return (x*gameX)/Settings.resX;
	}
	public static float gridY(float y) {
		return (y*gameY)/Settings.resY;
	}

	public static void drawSquare(float x, float y, Colour colour) {		
		// set the color of the quad
		glColor4f(colour.red, colour.green, colour.blue, colour.alpha);

		// draw quad
		glBegin(GL11.GL_QUADS);
		glVertex2f(actualX(x),		actualY(y));
		glVertex2f(actualX(x+1),	actualY(y));
		glVertex2f(actualX(x+1),	actualY(y+1));
		glVertex2f(actualX(x),		actualY(y+1));
		glEnd();
	}

	public static void drawSquare(float x, float y, Colour colour, Colour light) {		
		// set the color of the quad
		glColor4f(colour.red * light.red, colour.green * light.green, colour.blue * light.blue, colour.alpha);

		// draw quad
		glBegin(GL11.GL_QUADS);
		glVertex2f(actualX(x),		actualY(y));
		glVertex2f(actualX(x+1),	actualY(y));
		glVertex2f(actualX(x+1),	actualY(y+1));
		glVertex2f(actualX(x),		actualY(y+1));
		glEnd();
	}

	public static void drawSquare(float x, float y, int texture, boolean blend, Colour light) {
		glColor4f(light.red, light.green, light.blue, 1f);
		glEnable(GL_TEXTURE_2D);

		glBindTexture(GL_TEXTURE_2D, texture);

		if (blend) glEnable(GL_BLEND);

		// draw quad
		glBegin(GL11.GL_QUADS);
		glTexCoord2f(0, 0);
		glVertex2f(actualX(x),		actualY(y));

		glTexCoord2f(1, 0);
		glVertex2f(actualX(x+1),	actualY(y));

		glTexCoord2f(1, 1);
		glVertex2f(actualX(x+1),	actualY(y+1));

		glTexCoord2f(0, 1);
		glVertex2f(actualX(x),		actualY(y+1));
		glEnd();

		if (blend) glDisable(GL_BLEND);
		glBindTexture(GL_TEXTURE_2D, 0);

	}

	public static void drawPoly(float x1, float y1, float x2, float y2, int texture, boolean blend, Colour light) {
		glEnable(GL_TEXTURE_2D);

		if (blend) glEnable(GL_BLEND);

		glBindTexture(GL_TEXTURE_2D, texture);
		glColor4f(light.red, light.green, light.blue, 1f);


		// draw quad
		glBegin(GL11.GL_QUADS);
		glTexCoord2f(0, 0);
		glVertex2f(actualX(x1),		actualY(y1));

		glTexCoord2f(1, 0);
		glVertex2f(actualX(x2),	actualY(y1));

		glTexCoord2f(1, 1);
		glVertex2f(actualX(x2),	actualY(y2));

		glTexCoord2f(0, 1);
		glVertex2f(actualX(x1),		actualY(y2));
		glEnd();

		if (blend) glDisable(GL_BLEND);
		glBindTexture(GL_TEXTURE_2D, 0);

	}

	public static void drawCircle(float x, float y, float r, int slices, Colour colourIn, Colour colourOut) {
		slices *= Settings.segmentMultiplier;

		x = actualX(x);
		y = actualY(y);
		r = actualX(r);

		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);

		glColor4f(colourIn.red, colourIn.green, colourIn.blue, colourIn.alpha);

		GL11.glBegin(GL11.GL_TRIANGLE_FAN);
		GL11.glVertex2f(x, y);

		glColor4f(colourOut.red, colourOut.green, colourOut.blue, colourOut.alpha);

		for(int i = 0; i <= slices; i++){
			double angle = Math.PI * 2 * i / slices;
			GL11.glVertex2f(x + (float)Math.cos(angle)*r, y + (float)Math.sin(angle)*r);
		}
		GL11.glEnd();

		glDisable(GL_BLEND);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST_MIPMAP_LINEAR);
	}

	public static void drawSector(float x, float y, float r1, float r2, float th1, float th2, Colour colourIn, Colour colourOut) {
		x = actualX(x);
		y = actualY(y);
		r1 = actualX(r1);
		r2 = actualX(r2);

		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);

		GL11.glBegin(GL11.GL_QUADS);

		glColor4f(colourIn.red, colourIn.green, colourIn.blue, colourIn.alpha);
		GL11.glVertex2f(x + (float)Math.cos(th2)*r1, y + (float)Math.sin(th2)*r1);
		GL11.glVertex2f(x + (float)Math.cos(th1)*r1, y + (float)Math.sin(th1)*r1);

		glColor4f(colourOut.red, colourOut.green, colourOut.blue, colourOut.alpha);
		GL11.glVertex2f(x + (float)Math.cos(th1)*r2, y + (float)Math.sin(th1)*r2);
		GL11.glVertex2f(x + (float)Math.cos(th2)*r2, y + (float)Math.sin(th2)*r2);

		GL11.glEnd();

		glDisable(GL_BLEND);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST_MIPMAP_LINEAR);
	}

	public static void drawSectorArc(float x, float y, float r1, float r2, float th1, float th2, int slices, Colour colourIn, Colour colourOut) {
		slices *= Settings.segmentMultiplier;

		float maxAngle = th2 - th1;
		float anglePerSlice = maxAngle/slices;

		for(int i = 0; i < slices; th1 += anglePerSlice, i++){
			drawSector(x, y, r1, r2, th1, th1 + anglePerSlice, colourIn, colourOut);
		}
	}

	public static void drawSectorCircle(float x, float y, float r1, float r2, int slices, Colour colourIn, Colour colourOut) {
		slices *= Settings.segmentMultiplier;

		drawSectorArc(x, y, r1, r2, 0, (float)(2*Math.PI), slices, colourIn, colourOut);
	}

	public static void drawCircle(float x, float y, float r, int slices, Colour colour) {
		x = actualX(x);
		y = actualY(y);
		r = actualX(r);

		glColor4f(colour.red, colour.green, colour.blue, colour.alpha);

		GL11.glBegin(GL11.GL_TRIANGLE_FAN);
		GL11.glVertex2f(x, y);

		for(int i = 0; i <= slices; i++){
			double angle = Math.PI * 2 * i / slices;
			GL11.glVertex2f(x + (float)Math.cos(angle)*r, y + (float)Math.sin(angle)*r);
		}
		GL11.glEnd();
	}

	public static void drawSector(float x, float y, float r1, float r2, float th1, float th2, Colour colour) {
		x = actualX(x);
		y = actualY(y);
		r1 = actualX(r1);
		r2 = actualX(r2);

		glColor4f(colour.red, colour.green, colour.blue, colour.alpha);

		GL11.glBegin(GL11.GL_QUADS);

		GL11.glVertex2f(x + (float)Math.cos(th1)*r1, y + (float)Math.sin(th1)*r1);
		GL11.glVertex2f(x + (float)Math.cos(th1)*r2, y + (float)Math.sin(th1)*r2);
		GL11.glVertex2f(x + (float)Math.cos(th2)*r2, y + (float)Math.sin(th2)*r2);
		GL11.glVertex2f(x + (float)Math.cos(th2)*r1, y + (float)Math.sin(th2)*r1);

		GL11.glEnd();
	}

	public static void drawSectorArc(float x, float y, float r1, float r2, float th1, float th2, int slices, Colour colour) {
		slices *= Settings.segmentMultiplier;

		float maxAngle = th2 - th1;
		float anglePerSlice = maxAngle/slices;
				
		for(int i = 0; i < slices; th1 += anglePerSlice, i++){
			drawSector(x, y, r1, r2, th1, th1 + anglePerSlice, colour);
		}
	}

	public static void drawSectorCircle(float x, float y, float r1, float r2, int slices, Colour colour) {
		drawSectorArc(x, y, r1, r2, 0, (float)(Math.PI+Math.PI), slices, colour);
	}

	public static void quit() {
		GLFW.glfwSetWindowShouldClose(window, true);
	}

}
