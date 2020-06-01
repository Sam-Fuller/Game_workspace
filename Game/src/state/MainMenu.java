package state;

import java.util.ArrayList;
import java.util.List;

import colour.Colour;
import graphics.ColourMesh;
import graphics.Mesh;
import graphics.MeshBuilder;
import loader.TextLoader;
import runnable.Graphics;

public class MainMenu {
	static List<Mesh> meshList = new ArrayList<Mesh>();
	
	public static void gameStateChange() {
		meshList = new ArrayList<Mesh>();
		
		meshList.add(TextLoader.drawLargeText("Game", -0.8f, -0.8f, 0.5f, Graphics.getColourScheme().menuText(), true));
		
		MeshBuilder meshBuilder = new MeshBuilder();
		
		//meshBuilder.pushPoint(-0.5f, 0.5f, Colour.BLACK);
		//meshBuilder.pushPoint(0.5f, 0.5f, Colour.WHITE);
		//meshBuilder.pushPoint(-0, -0.9f, Colour.PURPLE);
		
		meshList.add(meshBuilder.asColourMesh(true, true));
	}
	
	public static void render() {
		if (meshList.size() == 0) gameStateChange();
		
		for (Mesh mesh: meshList)
			mesh.render();
	}

}
