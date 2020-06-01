package state;

import java.util.ArrayList;
import java.util.List;

import colour.Colour;
import entity.Player;
import graphics.Mesh;
import graphics.MeshBuilder;
import runnable.Graphics;

public class Hud {
	//centre of the hud
	static final float xCentre = 15.5f;
	static final float yCentre = 14f;

	//ability hexs
	static final float xOffset = 8;
	static final float abilityHexSize = xCentre/4f;
	static final float yOffset = 7f*abilityHexSize/16f;
	static final float abilityHexWidth = 1;
	static final float abilitySpacer = 3;

	//health tris
	static final float healthStartx = 5;
	static final float healthStarty = 5;
	static final float healthWidth = 5;
	static final float healthHeight = healthWidth * (float)Math.sqrt(3) / 2f;
	static final float healthSpacer = -1;

	static final Colour cXP = new Colour(0, 0.612f, 0.612f);
	static final Colour cHP = new Colour(0.612f, 0, 0);
	
	static Mesh abilityMesh;
	static Mesh healthMesh;

	public static void render() {
		if (abilityMesh == null) rerenderBaseMesh();
		rerenderHealth();
		
		abilityMesh.render();
		healthMesh.render();
	}
	
//	public static void abilityRender() {
//		if (abilityMesh == null) rerenderBaseMesh();
//		
//		abilityMesh.render();
//	}

	public static void rerenderBaseMesh() {		
		MeshBuilder colourMeshBuilder = new MeshBuilder();

		//Ability 1 hex
		colourMeshBuilder.drawSectorCircle(xCentre-xOffset, yCentre+yOffset, abilityHexSize-abilityHexWidth, abilityHexSize, 0, 6,
				Graphics.getColourScheme().HUDAbility1Inside(),
				Graphics.getColourScheme().HUDAbility1Outside());

		//Ability 2 hex
		colourMeshBuilder.drawSectorCircle(xCentre-xOffset+abilityHexSize+abilitySpacer, yCentre-yOffset, abilityHexSize-abilityHexWidth, abilityHexSize, 0, 6,
				Graphics.getColourScheme().HUDAbility2Inside(),
				Graphics.getColourScheme().HUDAbility2Outside());

		//Ability 3 hex
		colourMeshBuilder.drawSectorCircle(xCentre-xOffset+2*(abilityHexSize+abilitySpacer), yCentre+yOffset, abilityHexSize-abilityHexWidth, abilityHexSize, 0, 6,
				Graphics.getColourScheme().HUDAbility3Inside(),
				Graphics.getColourScheme().HUDAbility3Outside());

		//Ability 4 hex
		colourMeshBuilder.drawSectorCircle(xCentre-xOffset+3*(abilityHexSize+abilitySpacer), yCentre-yOffset, abilityHexSize-abilityHexWidth, abilityHexSize, 0, 6,
				Graphics.getColourScheme().HUDAbility4Inside(),
				Graphics.getColourScheme().HUDAbility4Outside());

		abilityMesh = colourMeshBuilder.asColourMesh(true, true);
	}
	
	public static void rerenderHealth() {
		MeshBuilder colourMeshBuilder = new MeshBuilder();

		//Health
		int totalFullHealthTriangles = Player.getPlayer().getHealth();
		int totalTriangles = Player.getPlayer().getMaxHealth();
		
		boolean flipped = false;
		boolean fullHealthTriangle = true;
		int i = 0;
		for (; ; i++) {
			Colour colourTipFullHealth = (!flipped? Graphics.getColourScheme().HUDHealthBottom(): Graphics.getColourScheme().HUDHealthTop());
			Colour colourBaseFullHealth = (!flipped? Graphics.getColourScheme().HUDHealthTop(): Graphics.getColourScheme().HUDHealthBottom());
			Colour colourMidFullHealth = (!flipped? Graphics.getColourScheme().HUDHealthMid(): Graphics.getColourScheme().HUDHealthMid());
			
			Colour colourTipNoHealth = (!flipped? Graphics.getColourScheme().HUDNoHealthBottom(): Graphics.getColourScheme().HUDNoHealthTop());
			Colour colourBaseNoHealth = (!flipped? Graphics.getColourScheme().HUDNoHealthTop(): Graphics.getColourScheme().HUDNoHealthBottom());
			Colour colourMidNoHealth = (!flipped? Graphics.getColourScheme().HUDNoHealthMid(): Graphics.getColourScheme().HUDNoHealthMid());
			
			float midx = healthStartx + i*(healthWidth+healthSpacer);
			float midy = healthStarty;
			
			float leftx = midx - healthWidth/2;
			float midleftx = midx - healthWidth/4;
			float midrightx = midx + healthWidth/4;
			float rightx = midx + healthWidth/2;
			
			float basey = !flipped? midy + healthHeight/2 : midy - healthHeight/2;
			float tipy = !flipped? midy - healthHeight/2 : midy + healthHeight/2;
			
			float offset =  0.1f;
			float offsetx = offset*(float)Math.sin(0.523599);
			float offsety = flipped? offset*(float)Math.cos(0.523599) : -offset*(float)Math.cos(0.523599);
			
			//colour left
			if (totalFullHealthTriangles - (i*4) > 0) fullHealthTriangle = true;
			else fullHealthTriangle = false;
			if (totalTriangles - (i*4) <= 0) break;
			colourMeshBuilder.drawTriangle(leftx+3*offsetx, basey+offsety, (fullHealthTriangle? colourBaseFullHealth : colourBaseNoHealth),
					midx-3*offsetx, basey+offsety, (fullHealthTriangle? colourBaseFullHealth : colourBaseNoHealth),
					midleftx, midy-2*offsety, (fullHealthTriangle? colourMidFullHealth : colourMidNoHealth));
			
			//colour mid
			if (totalFullHealthTriangles - (i*4+1) > 0) fullHealthTriangle = true;
			else fullHealthTriangle = false;
			if (totalTriangles - (i*4+1) <= 0) break;
			colourMeshBuilder.drawTriangle(midx, basey+2*offsety, (fullHealthTriangle? colourBaseFullHealth : colourBaseNoHealth),
					midrightx-3*offsetx, midy-offsety, (fullHealthTriangle? colourMidFullHealth : colourMidNoHealth),
					midleftx+3*offsetx, midy-offsety, (fullHealthTriangle? colourMidFullHealth : colourMidNoHealth));

			//colour right
			if (totalFullHealthTriangles - (i*4+2) > 0) fullHealthTriangle = true;
			else fullHealthTriangle = false;
			if (totalTriangles - (i*4+2) <= 0) break;
			colourMeshBuilder.drawTriangle(rightx-3*offsetx, basey+offsety, (fullHealthTriangle? colourBaseFullHealth : colourBaseNoHealth),
					midx+3*offsetx, basey+offsety, (fullHealthTriangle? colourBaseFullHealth : colourBaseNoHealth),
					midrightx, midy-2*offsety, (fullHealthTriangle? colourMidFullHealth : colourMidNoHealth));
			
			//colour point
			if (totalFullHealthTriangles - (i*4+3) > 0) fullHealthTriangle = true;
			else fullHealthTriangle = false;
			if (totalTriangles - (i*4+3) <= 0) break;
			colourMeshBuilder.drawTriangle(midx, tipy-offsety, (fullHealthTriangle? colourTipFullHealth : colourTipNoHealth),
					midrightx-3*offsetx, midy+offsety, (fullHealthTriangle? colourMidFullHealth : colourMidNoHealth),
					midleftx+3*offsetx, midy+offsety, (fullHealthTriangle? colourMidFullHealth : colourMidNoHealth));

			flipped = !flipped;
		}
		
		healthMesh = colourMeshBuilder.asColourMesh(true, true);
	}
}
