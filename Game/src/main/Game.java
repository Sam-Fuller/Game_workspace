package main;

import entity.Player;
import graphics.ColourMesh;
import graphics.TexturedMesh;
import runnable.Graphics;
import runnable.Music;
import runnable.Physics;
import runnable.WeaponFire;
import runnable.WorldGen;
import state.GameState;
import state.World;

public class Game {
	public static final String gameName = "game";
	
	public static GameState gameState;
	
	public static Graphics graphics = new Graphics();
	public static Thread graphicsThread = new Thread(graphics);
	
	public static Music music = new Music();
	public static Thread musicThread = new Thread(music);
	
	public static Physics physics = new Physics();
	public static Thread physicsThread = new Thread(physics);
	
	public static WeaponFire weaponFire = new WeaponFire();
	public static Thread weaponFireThread = new Thread(weaponFire);
	
	public static WorldGen worldGen = new WorldGen();
	public static Thread worldGenThread = new Thread(worldGen);
	
	public static void main(String[] args) {
		Settings.load();
				
		Player.init();
		
		gameState = new GameState();
		
		graphicsThread.start();
		physicsThread.start();
		weaponFireThread.start();
	}

	public static void cleanUp() {
		WorldGen.interrupted = true;
		WeaponFire.interrupted = true;
		Physics.interrupted = true;
		
		ColourMesh.cleanUpShader();
		TexturedMesh.cleanUpShader();
	}
}
