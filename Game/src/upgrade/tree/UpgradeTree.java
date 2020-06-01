package upgrade.tree;

import colour.Colour;
import graphics.Mesh;
import graphics.MeshBuilder;
import loader.TextLoader;
import main.Settings;
import runnable.Graphics;
import upgrade.Upgrade;

public abstract class UpgradeTree {
	//the top left coords of the tree display area
	public static final float treeStartx = 36;
	public static final float treeStarty = 27;

	//offsets so that the upgrades do some nice keming
	public static final float treeUpgradeyOffset = 0.4f;
	public static final float treeUpgradexOffset = treeUpgradeyOffset * ((float)Math.sqrt(3) / 2f);
	public static final float treeUpgradexKeming = -Upgrade.upgradeWidth/2;

	public static final int treeRows = 30;
	public static final int treeColumns = 5;

	//the coords of the upgrade currently being selected, -1 if no upgrade is being selected
	int selectedUpgradex = -1;
	int selectedUpgradey = -1;

	long lastSelected = 0;

	protected Mesh iconMesh;
	Mesh treeMesh;

	public Upgrade[][] upgrades = new Upgrade[treeRows][treeColumns];

	public UpgradeTree() {
		for (int x = 0; x < treeRows; x++) {
			for (int y = 0; y < treeColumns; y++) {
				upgrades[x][y] = new Upgrade(this, Upgrade.State.INVISIBLE);
			}
		}
	}

	public void renderIcon() {
		iconMesh.render();
	}

	public abstract void rerenderIcon(float x, float y);

	public abstract Colour getTreeNoIconColour();
	public abstract Colour getTreeLockedIconColour();
	public abstract Colour getTreeUnlockedIconColour();
	public abstract Colour getTreeBoughtIconColour();
	public abstract Colour getTreeIconColour();

	private void rerenderTree() {
		for (int x = 0; x < treeRows; x++) {
			for (int y = 0; y < treeColumns; y++) {
				upgrades[x][y].rerender((x+y)%2!=0,
						treeStartx+x*(Upgrade.upgradeWidth+treeUpgradexOffset+treeUpgradexKeming),
						treeStarty+y*(Upgrade.upgradeHeight+treeUpgradeyOffset*1.5f)+((x+y)%2==0? treeUpgradeyOffset/4:-treeUpgradeyOffset/4));
			}
		}

		MeshBuilder colourMeshBuilder = new MeshBuilder();

		treeMesh = colourMeshBuilder.asColourMesh(true, true);
	}

	public void renderTree() {
		if (treeMesh == null) rerenderTree();

		for (int x = 0; x < treeRows; x++) {
			for (int y = 0; y < treeColumns; y++) {
				if (selectedUpgradex == x && selectedUpgradey == y) {
					upgrades[selectedUpgradex][selectedUpgradey].renderSelected();
					upgrades[selectedUpgradex][selectedUpgradey].renderText();
				}
				
				upgrades[x][y].render();
				upgrades[x][y].renderIcon();
			}
		}

		treeMesh.render();
	}

	public void cursorClick(float xpos, float ypos) {
		long now = System.nanoTime();

		int oldx = selectedUpgradex;
		int oldy = selectedUpgradey;

		UpgradeTreeClick(xpos, ypos);

		if (selectedUpgradex == oldx && selectedUpgradey == oldy) {
			if (selectedUpgradex >= 0 && selectedUpgradey >= 0) {
				if (now - lastSelected < Settings.settings.doubleClick.getValue()) {
					buyUpgrade();
				}
			}
		}

		lastSelected = now;
	}
	
	private void buyUpgrade() {
		upgrades[selectedUpgradex][selectedUpgradey].buy((selectedUpgradex+selectedUpgradey)%2!=0, selectedUpgradex, selectedUpgradey, this);
		upgrades[selectedUpgradex][selectedUpgradey].rerender((selectedUpgradex+selectedUpgradey)%2!=0,
				treeStartx+selectedUpgradex*(Upgrade.upgradeWidth+treeUpgradexOffset+treeUpgradexKeming),
				treeStarty+selectedUpgradey*(Upgrade.upgradeHeight+treeUpgradeyOffset*1.5f)+((selectedUpgradex+selectedUpgradey)%2==0? treeUpgradeyOffset/4:-treeUpgradeyOffset/4));
	}

	private void UpgradeTreeClick(float xpos, float ypos) {
		int tempSelectedUpgradex = -1;
		int tempSelectedUpgradey = -1;

		boolean flipped = true;
		for (int y = 0; y <= treeColumns-1; y++) {
			flipped = y%2==0;

			for (int x = 0; x <= treeRows-1; x++) {
				flipped = !flipped;

				if (!upgrades[x][y].isInside(flipped, x, y, xpos, ypos)) continue;

				tempSelectedUpgradex = x;
				tempSelectedUpgradey = y;
			}
		}

		selectedUpgradex = tempSelectedUpgradex;
		selectedUpgradey = tempSelectedUpgradey;
	}
}
