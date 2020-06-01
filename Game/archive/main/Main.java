package main;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.IntStream;

import audio.MusicPlayer;
import audio.RunBackground;
import entities.Projectile;
import event.KeyEventHandler;
import event.MouseButtonEventHandler;
import settings.Settings;
import weapons.RunWeaponFire;
import world.RunPhysics;
import world.RunWorldGenerator;
import world.World;

public class Main {
	public static RunWeaponFire WeaponFire = new RunWeaponFire();
	public static Thread WeaponFireThread = new Thread(WeaponFire);
	
	public static void main(String[] args) {
		Settings.loadSettings();
		
		World.init();
		KeyEventHandler.init();
		MouseButtonEventHandler.init();
		//MusicPlayer.init();
		
		RunWorldGenerator worldGen = new RunWorldGenerator();
		Thread worldGenThread = new Thread(worldGen);
		worldGenThread.start();
		
		//Thread musicThread = new Thread(new RunBackground());
		//musicThread.start();
		
		RunPhysics physics = new RunPhysics();
		Thread physicsThread = new Thread(physics);
		physicsThread.start();
				
		GameWindow.run();
		
		//MusicPlayer.destroy();
		//musicThread.interrupt();
		RunWorldGenerator.interrupted = true;
		RunPhysics.interrupted = true;
		WeaponFire.interrupted = true;
		
		try {
			worldGenThread.join();
			physicsThread.join();
			WeaponFireThread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		System.out.println("Safely destroyed all threads");
	}
}