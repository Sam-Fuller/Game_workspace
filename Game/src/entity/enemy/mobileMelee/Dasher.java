package entity.enemy.mobileMelee;

import entity.Player;

public class Dasher {//extends MobileMelee {
//	static final int dHeight = 3;
//	static final int dWidth = 3;
//
//	static final float damage = 10;
//	static final long damageCooldown = 1000000000;
//	
//	static final float maxDistance = 100;
//
//	long lastDamage = 0;
//
//	boolean dashing = false;
//	long lastDash = 0;
//	static final float dashSpeed = 0.25f;
//	static final long dashCooldown = 5000000000l;
//
//
//	public Dasher(float posX, float posY, int level) {
//		super(dHeight, dWidth, posX, posY, level);
//	}
//
//	@Override
//	public void action(float frameSpeed) {
//		if (!dashing) {
//			if (!Player.getPlayer().within(getChunkNo(), getPosX(), getPosY(), maxDistance)) return;
//			if (Player.getPlayer().within(getChunkNo(), getPosX(), getPosY(), 1));
//			
//			long now = System.nanoTime();
//			if (now - lastDash > dashCooldown) {
//				lastDash = now;
//				
//				float dX = Player.player.getPosX() - getPosX() - Player.getOffsetX(getChunkNo());
//				float dY = Player.player.getPosY() - getPosY() - Player.getOffsetY(getChunkNo());
//				
//				float distance = Player.distance(getChunkNo(), getPosX(), getPosY());
//				
//				dX *= 10f/distance;
//				dY *= 10f/distance;
//								
//				setVelocityX(dX*dashSpeed);
//				setVelocityY(dY*dashSpeed);
//				
//				dashing = true;
//			}
//		}
//	}
//
//	@Override
//	public void onContact(float x, float y) {
//		long now = System.nanoTime();
//		if (now - lastDamage > damageCooldown) {
//			Player.decHealth(damage);
//			lastDamage = now;
//			
//			World.player.setVelocityX(x > 0? 2/(x+1) : 2/(x-1));
//			World.player.setVelocityY(y > 0? 2/(y+1) : 2/(y-1));
//		}
//	}
//
//	@Override
//	public void draw(float offsetX, float offsetY) {
//		float x = getPosX()-offsetX-getWidth();
//		float y = getPosY()-offsetY-getHeight();
//		
//		Colour light = World.getLight(getChunkNo(), getPosX(), getPosY());
//		GameWindow.drawPoly(x, y, x+getWidth(), y+getHeight(), textureID, true, light);
//	}
//
//	@Override
//	public void update(float frameSpeed) {
//		moveChunk();
//		
//		if(dashing) {
//			incPosX(getMovementX(frameSpeed));
//			incPosY(getMovementY(frameSpeed));
//			
//			if(getVelocityX() == 0) dashing = false;
//			if(getVelocityY() == 0) dashing = false;
//		}
//	}
//
//	@Override
//	public int getLightRadius() {
//		return 5;
//	}
//
//	@Override
//	public Colour glowColour() {
//		return Colour.RED;
//	}
//
//	@Override
//	public float getLightX() {
//		return 2f;
//	}
//
//	@Override
//	public float getLightY() {
//		return 2f;
//	}
//
//	@Override
//	protected void onDestruction() {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public void render() {
//		// TODO Auto-generated method stub
//		
//	}

}
