package upgrade;

import colour.Colour;
import colour.ColourScheme;
import graphics.Mesh;
import graphics.MeshBuilder;
import loader.TextLoader;
import runnable.Graphics;
import upgrade.tree.UpgradeTree;

public class Upgrade {
	public static enum State {
		LOCKED, UNLOCKED, BOUGHT, EMPTY, INVISIBLE
	}

	//width and height of a single upgrade triangle
	public static final float upgradeWidth = 7;
	public static final float upgradeHeight = upgradeWidth * (float)Math.sqrt(3) / 2f;

	static final float upgradeHoverScale = 1.15f;

	State state;

	Mesh triMesh;
	Mesh iconMesh;
	Mesh textMesh;
	Mesh selectedMesh;

	UpgradeTree parent;

	public Upgrade(UpgradeTree parent, State state) {
		this.parent = parent;
		this.state = state;
	}

	public State getState() {
		return state;
	}

	public void unlock(int x, int y) {
		if (this.getState() == State.LOCKED) {
			this.state = State.UNLOCKED;
			this.rerender((x+y)%2!=0,
					UpgradeTree.treeStartx+x*(Upgrade.upgradeWidth+UpgradeTree.treeUpgradexOffset+UpgradeTree.treeUpgradexKeming),
					UpgradeTree.treeStarty+y*(Upgrade.upgradeHeight+UpgradeTree.treeUpgradeyOffset*1.5f)+((x+y)%2==0? UpgradeTree.treeUpgradeyOffset/4:-UpgradeTree.treeUpgradeyOffset/4));;
		}
	}

	public void buy(boolean flipped, int x, int y, UpgradeTree tree) {
		if (this.getState() == State.UNLOCKED) {
			this.state = State.BOUGHT;

			if (x > 0) tree.upgrades[x-1][y].unlock(x-1, y);
			if (x < UpgradeTree.treeRows) tree.upgrades[x+1][y].unlock(x+1, y);

			if (flipped) {
				if (y > 0) tree.upgrades[x][y-1].unlock(x, y-1);
			}
			else {
				if (y < UpgradeTree.treeColumns) tree.upgrades[x][y+1].unlock(x, y+1);
			}
		}
	}

	public boolean isInside(boolean flipped, int x, int y, float xpos, float ypos) {
		if (state == State.INVISIBLE) return false;
		if (state == State.EMPTY) return false;

		float midy = UpgradeTree.treeStarty + y*(Upgrade.upgradeHeight+UpgradeTree.treeUpgradeyOffset*1.5f)+(!flipped? UpgradeTree.treeUpgradeyOffset/4:-UpgradeTree.treeUpgradeyOffset/4);
		float midx = UpgradeTree.treeStartx + x*(Upgrade.upgradeWidth+UpgradeTree.treeUpgradexOffset+UpgradeTree.treeUpgradexKeming);

		//calculate triangle borders
		float heightOfMouseRelativeToTriangle = (ypos-(midy-Upgrade.upgradeHeight/2)) / ((midy+Upgrade.upgradeHeight/2)-(midy-Upgrade.upgradeHeight/2));
		float widthAtHeight = flipped? (1-heightOfMouseRelativeToTriangle)*Upgrade.upgradeWidth/2 : heightOfMouseRelativeToTriangle*Upgrade.upgradeWidth/2;

		//if too right continue
		if (ypos > midy + Upgrade.upgradeHeight/2) {
			return false;
		}
		//if too left continue
		if (ypos < midy - Upgrade.upgradeHeight/2) {
			return false;
		}

		//if too low continue
		if (xpos > midx + widthAtHeight) {
			return false;
		}
		//if too high continue
		if (xpos < midx - widthAtHeight) {
			return false;
		}

		return true;
	}

	protected Colour getColourTri() {
		if (state == State.INVISIBLE) return null;
		if (state == State.EMPTY) return parent.getTreeNoIconColour();
		if (state == State.LOCKED) return parent.getTreeLockedIconColour();
		if (state == State.UNLOCKED) return parent.getTreeUnlockedIconColour();
		if (state == State.BOUGHT) return parent.getTreeBoughtIconColour();

		return Graphics.getColourScheme().background();
	}
	protected Colour getColourIcon() {
		return parent.getTreeIconColour();
	}

	public void rerender(boolean flipped, float x, float y) {
		if (state == State.INVISIBLE) return;

		MeshBuilder colourMeshBuilder = new MeshBuilder();

		colourMeshBuilder.drawTriangle(x, y, upgradeWidth, upgradeHeight,
				(flipped? 0 : (float)Math.PI),
				getColourTri(), getColourTri());

		triMesh = colourMeshBuilder.asColourMesh(true, true);


		colourMeshBuilder = new MeshBuilder();

		Colour colourUpgradeTreeList = getColourTri();//Graphics.getColourScheme().UpgradeMenuHoverColour();

		float yHoverScaleOffset = upgradeHeight*(upgradeHoverScale-1f) * (1-((float)Math.sqrt(3)/2f));
		float midy = y + (flipped? yHoverScaleOffset : -yHoverScaleOffset);

		colourMeshBuilder.drawTriangle(x, midy, upgradeWidth*upgradeHoverScale, upgradeHeight*upgradeHoverScale,
				(flipped? 0 : (float)Math.PI),
				colourUpgradeTreeList, colourUpgradeTreeList);

		selectedMesh = colourMeshBuilder.asColourMesh(true, true);

		rerenderIcon(flipped, x, y);
	}

	protected void rerenderIcon(boolean flipped, float x, float y) {
		return;
	}

	protected void rerenderText() {
		if (state == State.INVISIBLE) return;
		if (state == State.EMPTY) return;

		MeshBuilder textureMesh = new MeshBuilder();

		textureMesh.drawText(getName(), -0.55f, 0.3f, 0.15f);
		textureMesh.drawText(getDescription(), -0.55f, 0.45f, 0.05f);

		textureMesh.drawText("stats", 0.5f, 0.35f, 0.1f);
		textureMesh.drawText(getStats(), 0.5f, 0.45f, 0.05f);

		textMesh = textureMesh.asTexturedMesh(TextLoader.getLargeTextImage(), true, true);
	}

	public void render() {
		if (state == State.INVISIBLE) return;

		triMesh.render();
	}

	public void renderIcon() {
		if (state == State.INVISIBLE) return;

		if (iconMesh != null) iconMesh.render();
	}

	public void renderText() {
		if (state == State.INVISIBLE) return;
		if (state == State.EMPTY) return;

		if (textMesh == null) rerenderText();
		textMesh.render();
	}

	public void renderSelected() {
		if (state == State.INVISIBLE) return;
		if (state == State.EMPTY) return;

		selectedMesh.render();
	}

	public String getName() {return "Empty";}
	public String getDescription() {return "";}
	public String getStats() {return "";}

	public void onBuy() {}

}
