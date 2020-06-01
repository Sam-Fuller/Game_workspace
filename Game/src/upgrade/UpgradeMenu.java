package upgrade;

import java.nio.DoubleBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;

import colour.Colour;
import graphics.Mesh;
import graphics.MeshBuilder;
import runnable.Graphics;
import state.Hud;
import upgrade.tree.ExampleTree;
import upgrade.tree.UpgradeTree;
import upgrade.tree.shotgun.ShotgunTree;

public class UpgradeMenu {
	static final float upgradeTreeListStartx = 36;
	static final float upgradeTreeListStarty = 15;
	static final float upgradeTreeListWidth = 10;
	static final float upgradeTreeListHeight = upgradeTreeListWidth * (float)Math.sqrt(3)/2f;
	static final float upgradeTreeListHoverScale = 1.3f;
	static final float upgradeTreeListSpacer = -2;

	static List<UpgradeTree> upgradeTreeList = new ArrayList<UpgradeTree>();

	static Mesh upgradeTreeListMesh;
	static Mesh upgradeTreeListSelectedMesh;

	static int selectedTree = -1;

	public static void init() {
		upgradeTreeList.add(new ShotgunTree());
		upgradeTreeList.add(new ShotgunTree());

		upgradeTreeList.add(new ExampleTree());
		upgradeTreeList.add(new ExampleTree());
		upgradeTreeList.add(new ExampleTree());
		upgradeTreeList.add(new ExampleTree());
	}

	public static void render() {
		rerenderUpgradeTreeHover();
		if (upgradeTreeListMesh == null) rerenderUpgradeTreeList();

		Hud.render();

		upgradeTreeListSelectedMesh.render();
		upgradeTreeListMesh.render();
		for (UpgradeTree upgradeTree: upgradeTreeList) upgradeTree.renderIcon();

		if (selectedTree >= 0) upgradeTreeList.get(selectedTree).renderTree();
	}

	private static void rerenderUpgradeTreeHover() {
		MeshBuilder colourMeshBuilder = new MeshBuilder();

		if (selectedTree != -1) {
			Colour colourUpgradeTreeList = Graphics.getColourScheme().UpgradeMenuHoverColour();

			float midx = upgradeTreeListStartx + selectedTree*(upgradeTreeListWidth+upgradeTreeListSpacer);
			float yHoverScaleOffset = upgradeTreeListHeight*(upgradeTreeListHoverScale-1f) * (1-((float)Math.sqrt(3)/2f));
			float midy = upgradeTreeListStarty + (selectedTree%2==0? -yHoverScaleOffset : yHoverScaleOffset);

			colourMeshBuilder.drawTriangle(midx, midy, upgradeTreeListWidth*upgradeTreeListHoverScale, upgradeTreeListHeight*upgradeTreeListHoverScale,
					(selectedTree%2==0? (float)Math.PI : 0),
					colourUpgradeTreeList, colourUpgradeTreeList);
		}

		upgradeTreeListSelectedMesh = colourMeshBuilder.asColourMesh(true, true);
	}

	private static void rerenderUpgradeTreeList() {
		MeshBuilder colourMeshBuilder = new MeshBuilder();

		boolean flipped = false;
		int i = 0;
		for (UpgradeTree upgradeTree: upgradeTreeList) {
			Colour colourUpgradeTreeList = Graphics.getColourScheme().UpgradeMenuTreeList();

			float midx = upgradeTreeListStartx + i*(upgradeTreeListWidth+upgradeTreeListSpacer);
			float midy = upgradeTreeListStarty;

			colourMeshBuilder.drawTriangle(midx, midy, upgradeTreeListWidth, upgradeTreeListHeight,
					(flipped? 0 : (float)Math.PI),
					colourUpgradeTreeList, colourUpgradeTreeList);

			float iconMidy = midy + (flipped? -upgradeTreeListHeight/6: upgradeTreeListHeight/10);

			upgradeTree.rerenderIcon(midx, iconMidy);

			flipped = !flipped;
			i++;
		}

		upgradeTreeListMesh = colourMeshBuilder.asColourMesh(true, true);
	}

	public static void cursorClick(float xpos, float ypos) {
		UpgradeTreeMenuClick(xpos, ypos);
		
		if (selectedTree >= 0) upgradeTreeList.get(selectedTree).cursorClick(xpos, ypos);
	}

	private static void UpgradeTreeMenuClick(float xpos, float ypos) {
		boolean flipped = true;

		float midy = upgradeTreeListStarty;

		//if too low return
		if (ypos > midy + upgradeTreeListHeight/2) {
			return;
		}
		//if too high return
		if (ypos < midy - upgradeTreeListHeight/2) {
			return;
		}

		int tempSelectedTree = -1;
		for (int i = 0; i < upgradeTreeList.size(); i++) {
			flipped = !flipped;

			float midx = upgradeTreeListStartx + i*(upgradeTreeListWidth+upgradeTreeListSpacer);

			//calculate triangle borders
			float heightOfMouseRelativeToTriangle = (ypos-(midy-upgradeTreeListHeight/2)) / upgradeTreeListHeight;
			float widthAtHeight = flipped? (1-heightOfMouseRelativeToTriangle)*upgradeTreeListWidth/2 : heightOfMouseRelativeToTriangle*upgradeTreeListWidth/2;

			//if too right return
			if (xpos > midx + widthAtHeight) {
				continue;
			}
			//if too left return
			if (xpos < midx - widthAtHeight) {
				continue;
			}

			tempSelectedTree = i;
		}

		if (tempSelectedTree == -1) return;
		selectedTree = tempSelectedTree;
	}
}
