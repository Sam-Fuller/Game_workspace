package upgrade;

import colour.Colour;
import graphics.MeshBuilder;
import upgrade.Upgrade.State;
import upgrade.tree.UpgradeTree;

public class Ability extends Upgrade {
	public Ability(UpgradeTree parent, State state) {
		super(parent, state);
	}

	@Override
	public void buy(boolean flipped, int x, int y, UpgradeTree tree) {
		if (this.getState() == State.UNLOCKED) {
			this.state = State.BOUGHT;

			if (x > 0) tree.upgrades[x-1][y].unlock(x-1, y);
			if (x <= UpgradeTree.treeRows) tree.upgrades[x+1][y].unlock(x+1, y);
			if (x+1 <= UpgradeTree.treeRows) tree.upgrades[x+2][y].unlock(x+2, y);
			if (x+2 <= UpgradeTree.treeRows) tree.upgrades[x+3][y].unlock(x+3, y);

			if (y <= UpgradeTree.treeColumns) {
				if (x > 0) tree.upgrades[x-1][y+1].unlock(x-1, y+1);
				tree.upgrades[x][y+1].unlock(x, y+1);
				if (x <= UpgradeTree.treeRows) tree.upgrades[x+1][y+1].unlock(x+1, y+1);
				if (x+1 <= UpgradeTree.treeRows) tree.upgrades[x+2][y+1].unlock(x+2, y+1);
				if (x+2 <= UpgradeTree.treeRows) tree.upgrades[x+3][y+1].unlock(x+3, y+1);
			}

			if (y > 0) tree.upgrades[x][y-1].unlock(x, y-1);
			if (y+1 <= UpgradeTree.treeColumns) tree.upgrades[x+1][y+2].unlock(x+1, y+2);
		}
	}

	public boolean isInside(boolean flipped, int x, int y, float xpos, float ypos) {
		float midy = UpgradeTree.treeStarty + y*(Upgrade.upgradeHeight+UpgradeTree.treeUpgradeyOffset) + upgradeHeight/2 + UpgradeTree.treeUpgradeyOffset/2;
		float midx = UpgradeTree.treeStartx + x*(Upgrade.upgradeWidth+UpgradeTree.treeUpgradexOffset+UpgradeTree.treeUpgradexKeming) + upgradeWidth/2 + UpgradeTree.treeUpgradexOffset/2;

		//if too low continue
		if (ypos > midy + Upgrade.upgradeHeight) {
			return false;
		}
		//if too high continue
		if (ypos < midy - Upgrade.upgradeHeight) {
			return false;
		}

		float heightOfMouseRelativeToHexCentre = Math.abs((ypos - midy)/Upgrade.upgradeHeight);
		float widthAtHeight = (1-heightOfMouseRelativeToHexCentre)*Upgrade.upgradeWidth/2+Upgrade.upgradeWidth/2+UpgradeTree.treeUpgradexOffset;


		//if too right continue
		if (xpos > midx + widthAtHeight) {
			return false;
		}
		//if too left continue
		if (xpos < midx - widthAtHeight) {
			return false;
		}

		System.out.println("true");
		return true;
	}

	public void rerender(boolean flipped, float x, float y) {
		if (state == State.INVISIBLE) return;

		MeshBuilder colourMeshBuilder = new MeshBuilder();

		colourMeshBuilder.drawSectorCircle(x + upgradeWidth/2 + UpgradeTree.treeUpgradexOffset,
				y + upgradeHeight/2 + UpgradeTree.treeUpgradeyOffset/2,
				0, upgradeWidth + UpgradeTree.treeUpgradexOffset*1.33f, 0, 6, getColourTri());


		triMesh = colourMeshBuilder.asColourMesh(true, true);


		colourMeshBuilder = new MeshBuilder();

		colourMeshBuilder.drawSectorCircle(x + upgradeWidth/2 + UpgradeTree.treeUpgradexOffset,
				y + upgradeHeight/2 + UpgradeTree.treeUpgradeyOffset/2,
				0, (upgradeWidth + UpgradeTree.treeUpgradexOffset)*(1+(upgradeHoverScale-1)/2), 0, 6, getColourTri());

		selectedMesh = colourMeshBuilder.asColourMesh(true, true);

		rerenderIcon(flipped, x, y);
	}
}
