package upgrade.tree;

import colour.Colour;
import graphics.Mesh;
import graphics.MeshBuilder;
import runnable.Graphics;

public class ExampleTree extends UpgradeTree{
	Mesh iconMesh;

	@Override
	public void rerenderIcon(float x, float y) {
		MeshBuilder colourMeshBuilder = new MeshBuilder();

		Colour iconColour = Graphics.getColourScheme().UpgradeTreeExampleIcon();

		colourMeshBuilder.drawTriangle(x, y-1, (3*(float)Math.sqrt(3) / 2f), 3,
				(float)Math.PI,
				iconColour, iconColour);
		
		colourMeshBuilder.drawTriangle(x, y+1, (3*(float)Math.sqrt(3) / 2f), 3,
				(float)Math.PI,
				iconColour, iconColour);


		iconMesh = colourMeshBuilder.asColourMesh(true, true);
	}

	@Override
	public void renderIcon() {
		iconMesh.render();
	}

	@Override
	public void renderTree() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Colour getTreeNoIconColour() {
		return Graphics.getColourScheme().UpgradeTreeExampleNoIconColour();
	}
	@Override
	public Colour getTreeLockedIconColour() {
		return Graphics.getColourScheme().UpgradeTreeExampleLockedIconColour();
	}
	@Override
	public Colour getTreeUnlockedIconColour() {
		return Graphics.getColourScheme().UpgradeTreeExampleUnlockedIconColour();
	}
	@Override
	public Colour getTreeBoughtIconColour() {
		return Graphics.getColourScheme().UpgradeTreeExampleBoughtIconColour();
	}
	@Override
	public Colour getTreeIconColour() {
		return null;
	}

}
