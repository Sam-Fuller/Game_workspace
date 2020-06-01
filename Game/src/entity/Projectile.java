package entity;

import colour.Colour;
import graphics.ColourMesh;
import graphics.MeshBuilder;
import runnable.Graphics;
import state.World;

public class Projectile extends Entity{
	boolean madeByPlayer;

	int damage;
	
	boolean destroyOnDamage = true;

	protected Colour colourIn;
	protected Colour colourOut;

	int knockback = 0;

	long lastDamage = 0;
	long damageCooldown = 100000000;

	int bounceLimit = 0;


	protected boolean timeLimited = false;
	protected long timeLimit = 0;

	protected ColourMesh colourMesh;


	public Projectile(boolean madeByPlayer, float height, float width, int chunkNo, float posX, float posY, Colour colourIn, Colour colourOut) {
		super(height, width, chunkNo, posX-1, posY-1);

		this.colourIn = colourIn;
		this.colourOut = colourOut;
		this.madeByPlayer = madeByPlayer;

		setDestroy(true);
		setGravityImmune(true);
	}
	public Projectile(boolean madeByPlayer, float height, float width, int chunkNo, float posX, float posY, Colour colour) {
		super(height, width, chunkNo, posX-1, posY-1);

		this.colourIn = colour;
		this.colourOut = colour;
		this.madeByPlayer = madeByPlayer;

		setDestroy(true);
		setGravityImmune(true);
	}

	public boolean isMadeByPlayer() {
		return madeByPlayer;
	}

	public int getKnockback() {
		return knockback;
	}
	public void setKnockback(int knockback) {
		this.knockback = knockback;
	}

	public int getDamage() {
		return damage;
	}
	public void setDamage(int damage) {
		this.damage = damage;
	}
	public long getDamageCooldown() {
		return damageCooldown;
	}
	public void setDamageCooldown(long damageCooldown) {
		this.damageCooldown = damageCooldown;
	}

	public boolean isDestroyOnDamage() {
		return destroyOnDamage;
	}
	public void setDestroyOnDamage(boolean destroyOnDamage) {
		this.destroyOnDamage = destroyOnDamage;
	}

	public void setTimeLimit(long timelimit) {
		this.timeLimit = timelimit + System.nanoTime();
		this.timeLimited = true;
	}

	@Override
	public void action(float frameSpeed) {		
		if (timeLimited) if (timeLimit < System.nanoTime()) toBeDestroyed = true;

		moveChunk();

		incPosX(getMovementX(frameSpeed));
		incPosY(getMovementY(frameSpeed));
		
		if (isMadeByPlayer()) checkEnemyHitbox();
		else checkContact();
	}

	protected void checkEnemyHitbox() {
		if (lastDamage + damageCooldown > System.nanoTime()) return;

		for (Entity enemy: World.getWorld().get(getChunkNo()).getEntities()) {
			if (enemy.getMaxHealth() == 0) return;
			
			if (enemy.touchingSquare(this)) {
				enemy.decHealth(damage);
				if (isDestroyOnDamage()) toBeDestroyed = true;
				else lastDamage = System.nanoTime();
			}
		}
	}

	private void checkContact() {
		if (this.touchingSquarePlayer()) {
			float x = getPosX() + World.getOffsetX(getChunkNo()) - getWidth() - Player.getPlayer().getPosX() + Player.getPlayer().getWidth();
			float y = getPosY() + World.getOffsetY(getChunkNo()) - getHeight() - Player.getPlayer().getPosY() + Player.getPlayer().getHeight();
			
			if (isDestroyOnDamage()) {
				Player.getPlayer().decHealth(damage);

				if (knockback != 0) {
					Player.getPlayer().setVelocityX(x > 0? knockback/(x+1) : knockback/(x-1));
					Player.getPlayer().setVelocityY(y > 0? knockback/(y+1) : knockback/(y-1));
				}
				toBeDestroyed = true;

			}else {
				long now = System.nanoTime();
				if (now - lastDamage > damageCooldown) {
					Player.getPlayer().decHealth(damage);
					lastDamage = now;

					if (knockback != 0) {
						Player.getPlayer().setVelocityX(x > 0? knockback/(x+1) : knockback/(x-1));
						Player.getPlayer().setVelocityY(y > 0? knockback/(y+1) : knockback/(y-1));
					}
				}
			}
		}
	}

	@Override
	public void render() {
		if (colourMesh == null) gameStateChange();
		
		

		colourMesh.render((getPosX()+World.getOffsetX(getChunkNo())), (getPosY()+World.getOffsetY(getChunkNo())));
	}

	public void gameStateChange() {
		float x = Graphics.gridToScreenX(0);//+0.5f;//+Graphics.getGameX()/2+1f;
		float y = Graphics.gridToScreenY(0)-1;//.5f;//+Graphics.getGameY()/2+1f;

		MeshBuilder meshBuilder = new MeshBuilder();

		meshBuilder.drawSectorCircle(x, y, 0, getWidth()/2, 0, 20, colourIn, colourOut);

		colourMesh = meshBuilder.asColourMesh(false);
	}

	@Override
	public void update(float frameSpeed) {
		if (toBeDestroyed) onDestruction();
	}

	public void setBounceLimit(int bounceLimit) {
		this.bounce = true;
		this.bounceLimit = bounceLimit;
	}
	@Override
	public boolean isBounce() {
		if (!bounce) return false;
		if (bounceLimit-- > 0) return true;
		return false;
	}

	@Override
	public boolean isDestroy() {
		if (!destroy) return false;
		if (bounce && bounceLimit > 0) return false;

		return toBeDestroyed = true;
	}
	@Override
	public boolean getDecelerate() {
		return false;
	}
	@Override
	public void onDestruction() {
		World.getWorld().get(chunkNo).getEntities().remove(this);

		colourMesh.cleanUp();
	}
	@Override
	protected void incChunkNo(int inc) {
		World.getWorld().get(chunkNo).getEntities().remove(this);
		chunkNo += inc;
		World.getWorld().get(chunkNo).getEntities().add(this);
	}
}
