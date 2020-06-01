package upgrade.tree.shotgun;

import java.util.ArrayList;

import colour.Colour;
import graphics.Mesh;
import graphics.MeshBuilder;
import runnable.Graphics;
import upgrade.Ability;
import upgrade.Upgrade;
import upgrade.tree.UpgradeTree;

public class ShotgunTree extends UpgradeTree{
		
	public ShotgunTree() {
		super();
		upgrades[0][0] = new UpgradeAttackSpeed(this, Upgrade.State.UNLOCKED);
		upgrades[1][0] = new UpgradeAttackSpeed(this, Upgrade.State.LOCKED);
		upgrades[2][0] = new UpgradeAttackSpeed(this, Upgrade.State.LOCKED);
		upgrades[2][1] = new UpgradeAttackSpeed(this, Upgrade.State.LOCKED);
		upgrades[3][1] = new UpgradeAttackSpeed(this, Upgrade.State.LOCKED);
		upgrades[4][1] = new UpgradeAttackSpeed(this, Upgrade.State.LOCKED);
		upgrades[5][1] = new UpgradeAttackSpeed(this, Upgrade.State.LOCKED);
		upgrades[5][2] = new UpgradeAttackSpeed(this, Upgrade.State.LOCKED);
		
		upgrades[6][2] = new Ability(this, Upgrade.State.LOCKED);
		upgrades[7][2] = new Upgrade(this, Upgrade.State.LOCKED);
		
		upgrades[4][0] = new Upgrade(this, Upgrade.State.LOCKED);
		upgrades[5][0] = new Upgrade(this, Upgrade.State.LOCKED);
		upgrades[6][0] = new Upgrade(this, Upgrade.State.LOCKED);
		upgrades[7][0] = new Upgrade(this, Upgrade.State.LOCKED);

		upgrades[8][0] = new Upgrade(this, Upgrade.State.EMPTY);
		upgrades[8][1] = new Upgrade(this, Upgrade.State.EMPTY);
		
		upgrades[3][0] = new Upgrade(this, Upgrade.State.EMPTY);
		
		upgrades[0][1] = new Upgrade(this, Upgrade.State.EMPTY);
		upgrades[1][1] = new Upgrade(this, Upgrade.State.EMPTY);
		
		upgrades[6][1] = new Upgrade(this, Upgrade.State.EMPTY);
		upgrades[7][1] = new Upgrade(this, Upgrade.State.EMPTY);
		
		upgrades[3][2] = new Upgrade(this, Upgrade.State.EMPTY);
		upgrades[4][2] = new Upgrade(this, Upgrade.State.EMPTY);
		
		upgrades[9][4] = new Upgrade(this, Upgrade.State.EMPTY);
		upgrades[8][4] = new Upgrade(this, Upgrade.State.EMPTY);
		
		upgrades[9][3] = new Upgrade(this, Upgrade.State.LOCKED);
		upgrades[7][4] = new Upgrade(this, Upgrade.State.LOCKED);

	}

	@Override
	public void rerenderIcon(float x, float y) {
		MeshBuilder colourMeshBuilder = new MeshBuilder();

		Colour iconColour = Graphics.getColourScheme().UpgradeTreeShotgunIcon();

		colourMeshBuilder.drawSectorCircle(x, y, 2, 1.8f, 0, 40, iconColour);
		colourMeshBuilder.drawSectorCircle(x, y+1, 1.5f, 1.35f, 0, 30, iconColour);
		colourMeshBuilder.drawSectorCircle(x, y+1.75f, 1, 0.9f, 0, 20, iconColour);
		colourMeshBuilder.drawSectorCircle(x, y+2.375f, 0.5f, 0.45f, 0, 20, iconColour);

		iconMesh = colourMeshBuilder.asColourMesh(true, true);
	}

	@Override
	public Colour getTreeNoIconColour() {
		return Graphics.getColourScheme().UpgradeTreeShotgunNoIconColour();
	}
	@Override
	public Colour getTreeLockedIconColour() {
		return Graphics.getColourScheme().UpgradeTreeShotgunLockedIconColour();
	}
	@Override
	public Colour getTreeUnlockedIconColour() {
		return Graphics.getColourScheme().UpgradeTreeShotgunUnlockedIconColour();
	}
	@Override
	public Colour getTreeBoughtIconColour() {
		return Graphics.getColourScheme().UpgradeTreeShotgunBoughtIconColour();
	}
	@Override
	public Colour getTreeIconColour() {
		return Graphics.getColourScheme().UpgradeTreeShotgunIconColour();
	}

	
}
