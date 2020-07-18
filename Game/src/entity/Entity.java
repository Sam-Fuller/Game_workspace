package entity;

import colour.Colour;
import state.World;

public abstract class Entity {
	static final float yGravity = 0.03f;

	static final float xDeceleration = 0.001f;
	static final float yDeceleration = 0.001f;

	static final float maxSpeed = 100;


	float height;
	float width;


	int chunkNo;

	protected float posX = 0;
	protected float posY = 0;

	float velX = 0;
	float velY = 0;

	boolean gravityImmune;
	boolean decelerate;


	int maxHealth;
	int health;


	boolean hazard = true;
	boolean bounce;
	boolean destroy;

	float lightX;
	float lightY;
	float lightRadius = 0;

	protected boolean toBeDestroyed = false;




	public Entity(float height, float width, int chunkNo, float posX, float posY) {
		this.height = height;
		this.width = width;
		this.chunkNo = chunkNo;
		this.posX = posX;
		this.posY = posY;
	}

	public int getMaxHealth() {
		return maxHealth;
	}
	public void setMaxHealth(int maxHealth) {
		this.maxHealth = maxHealth;
	}
	public void incMaxHealth(int maxHealth) {
		this.maxHealth += maxHealth;
	}

	public int getHealth() {
		return health;
	}
	public void setHealth(int health) {
		this.health = health;
		if (this.health <= 0) {
			this.health = 0;
			toBeDestroyed = true;
		}
	}
	public void incHealth(int health) {
		this.health += health;
		if (this.health <= 0) {
			this.health = 0;
			toBeDestroyed = true;
		}
	}
	public void decHealth(int health) {
		this.health -= health;
		if (this.health <= 0) {
			this.health = 0;
			toBeDestroyed = true;
		}
	}


	public float getHeight() {
		return height;
	}
	protected void setHeight(float height) {
		this.height = height;
	}

	public float getWidth() {
		return width;
	}
	protected void setWidth(float width) {
		this.width = width;
	}


	public float getPosX() {
		return posX;
	}
	protected void setPosX(float posX) {
		this.posX = posX;
	}
	protected void incPosX(float velX) {
		this.posX += velX;
	}

	public float getPosY() {
		return posY;
	}
	protected void setPosY(float posY) {
		this.posY = posY;
	}
	protected void incPosY(float velY) {
		this.posY += velY;
	}

	protected float getPureVelocityX() {
		return velX;
	}
	protected float getVelocityX() {
		return velX;
	}
	public void setVelocityX(float velX) {
		this.velX = velX;
		if (this.velX > maxSpeed) this.velX = maxSpeed;
		if (this.velX < -maxSpeed) this.velX = -maxSpeed;
	}
	public void incVelocityX(float velX) {
		this.velX += velX;
		if (this.velX > maxSpeed) this.velX = maxSpeed;
		if (this.velX < -maxSpeed) this.velX = -maxSpeed;
	}

	protected float getPureVelocityY() {
		return velY;
	}
	protected float getVelocityY() {
		return velY;
	}
	public void setVelocityY(float velY) {
		this.velY = velY;
		if (this.velY > maxSpeed) this.velY = maxSpeed;
		if (this.velY < -maxSpeed) this.velY = -maxSpeed;
	}
	public void incVelocityY(float velY) {
		this.velY += velY;
		if (this.velY > maxSpeed) this.velY = maxSpeed;
		if (this.velY < -maxSpeed) this.velY = -maxSpeed;
	}

	public void setVelocities(float X, float Y){
		setVelocityX(X);
		setVelocityY(Y);
	}

	public int getChunkNo() {
		return chunkNo;
	}
	protected void incChunkNo(int inc) {
		chunkNo += inc;
	}

	protected boolean isHazard() {
		return hazard;
	}
	public void setHazard(boolean hazard) {
		this.hazard = hazard;
	}

	protected void setLightEffect(float x, float y, float radius) {
		lightX = x;
		lightY = y;
		lightRadius = radius;
	}
	protected float getLightX() {
		return lightX;
	}
	protected float getLightY() {
		return lightY;
	}
	public float getLightRadius() {
		return lightRadius;
	}

	protected void setGravityImmune(boolean gravityImmune) {
		this.gravityImmune = gravityImmune;
	}
	protected boolean isGravityImmune() {
		return gravityImmune;
	}

	protected void setDecelerate(Boolean decelerate) {
		this.decelerate = decelerate;
	}
	protected boolean getDecelerate() {
		return decelerate;
	}

	protected void setBounce(boolean bounce) {
		this.bounce = bounce;
	}
	protected boolean isBounce() {
		return bounce;
	}
	protected void setDestroy(boolean destroy) {
		this.destroy = destroy;
	}
	protected boolean isDestroy() {
		return destroy;
	}

	private int getOffsetX(float posX) {
		//correct for chunk
		//if left
		if (posX < 0) {
			if (World.getWorld().get(getChunkNo()).getExit() == 1) {
				//if exit left
				return World.getWorld().get(getChunkNo()+1).getLevelSize();
			} else if (World.getWorld().get(getChunkNo()).getEntrance() == 1) {
				//if entrance left
				return World.getWorld().get(getChunkNo()-1).getLevelSize();
			}
		}
		//if right
		else if (posX > World.getWorld().get(getChunkNo()).getLevelSize()-1) {
			if (World.getWorld().get(getChunkNo()).getExit() == 3) {
				//if exit right
				return -World.getWorld().get(getChunkNo()).getLevelSize();
			} else if (World.getWorld().get(getChunkNo()).getEntrance() == 3) {
				//if entrance right
				return -World.getWorld().get(getChunkNo()).getLevelSize();
			}
		}
		return 0;
	}
	private int getOffsetY(float posY) {
		//correct for chunk
		//if above
		if (posY < 0) {
			if (World.getWorld().get(getChunkNo()).getExit() == 0) {
				//if exit above
				return World.getWorld().get(getChunkNo()+1).getLevelSize();
			}else if (getChunkNo() > 0 && World.getWorld().get(getChunkNo()).getEntrance() == 0) {
				//if entrance left
				return World.getWorld().get(getChunkNo()-1).getLevelSize();
			}
		}
		//if below
		else if (posY > World.getWorld().get(getChunkNo()).getLevelSize()-1) {
			if (World.getWorld().get(getChunkNo()).getExit() == 2) {
				//if exit right
				return -World.getWorld().get(getChunkNo()).getLevelSize();
			} else if (World.getWorld().get(getChunkNo()).getEntrance() == 2) {
				//if entrance right
				return -World.getWorld().get(getChunkNo()).getLevelSize();
			}
		}
		return 0;
	}
	private int getOffsetChunk(float posX, float posY) {
		//correct for chunk
		//if above
		if (posY < 0) {
			if (World.getWorld().get(getChunkNo()).getExit() == 0) {
				//if exit above
				return 1;
			}else if (World.getWorld().get(getChunkNo()).getEntrance() == 0) {
				//if entrance left
				return -1;
			}
		}
		//if below
		else if (posY > World.getWorld().get(getChunkNo()).getLevelSize()-1) {
			if (World.getWorld().get(getChunkNo()).getExit() == 2) {
				//if exit right
				return 1;
			} else if (World.getWorld().get(getChunkNo()).getEntrance() == 2) {
				//if entrance right
				return -1;
			}
		}
		//if left
		if (posX < 0) {
			if (World.getWorld().get(getChunkNo()).getExit() == 1) {
				//if exit left
				return 1;
			} else if (World.getWorld().get(getChunkNo()).getEntrance() == 1) {
				//if entrance left
				return -1;
			}
		}
		//if right
		else if (posX > World.getWorld().get(getChunkNo()).getLevelSize()-1) {
			if (World.getWorld().get(getChunkNo()).getExit() == 3) {
				//if exit right
				return 1;
			} else if (World.getWorld().get(getChunkNo()).getEntrance() == 3) {
				//if entrance right
				return -1;
			}
		}
		return 0;
	}

	protected float getMovementX(float frameSpeed) {
		float fPosX = getPosX();
		float fPosY = getPosY();

		float entityWidth = getWidth();
		float entityHeight = getHeight();
		
		//if moving right
		if (getVelocityX() > 0) {
			int entityTop = (int) Math.floor(fPosY - entityHeight/2);
			int entityBottom = (int) Math.floor(fPosY + entityHeight/2);

			int entityRight = (int) Math.floor(fPosX + entityWidth/2);

			int offsetX = getOffsetX(entityRight);

			for (int currentPosY = entityTop; currentPosY < entityBottom; currentPosY++) {
				int offsetY = getOffsetY(currentPosY);
				int offsetC = getOffsetChunk(entityRight, currentPosY);

				if (World.getWorld().get(getChunkNo()+offsetC).getMap()[entityRight+offsetX][(currentPosY+offsetY)*2]) {
					if (isBounce()) setVelocityX(-getVelocityX());
					else if (!isDestroy()) setVelocityX(0);
					setVelocityY(0);
					
					return (entityRight - (fPosX + entityWidth/2f));
				}
			}
			
			
			entityRight = (int) Math.floor(fPosX + entityWidth/2 + 0.5f);
			offsetX = getOffsetX(entityRight);

			for (int currentPosY = entityTop; currentPosY < entityBottom; currentPosY++) {
				int offsetY = getOffsetY(currentPosY);
				int offsetC = getOffsetChunk(entityRight, currentPosY);

				if (World.getWorld().get(getChunkNo()+offsetC).getMap()[entityRight+offsetX][(currentPosY+offsetY)*2+1]) {
					if (isBounce()) setVelocityX(-getVelocityX());
					else if (!isDestroy()) setVelocityX(0);
					setVelocityY(0);
					
					return (entityRight - (fPosX + entityWidth/2f + 0.5f));
				}
			}
		}
		
		//if moving left
		else if (getVelocityX() < 0) {
			int entityTop = (int) Math.ceil(fPosY - entityHeight/2);
			int entityBottom = (int) Math.floor(fPosY + entityHeight/2);

			int entityLeft = (int) Math.floor(fPosX - entityWidth/2);

			int offsetX = getOffsetX(entityLeft);

			for (int currentPosY = entityTop; currentPosY < entityBottom; currentPosY++) {
				int offsetY = getOffsetY(currentPosY);
				int offsetC = getOffsetChunk(entityLeft, currentPosY);

				if (World.getWorld().get(getChunkNo()+offsetC).getMap()[entityLeft+offsetX][(currentPosY+offsetY)*2]) {
					if (isBounce()) setVelocityX(-getVelocityX());
					else if (!isDestroy()) setVelocityX(0);
					setVelocityY(0);
					
					return 1 - ((fPosX - entityWidth/2f) - entityLeft);
				}
			}
			
			
			entityLeft = (int) Math.floor(fPosX - entityWidth/2 + 0.5f);
			offsetX = getOffsetX(entityLeft);

			for (int currentPosY = entityTop; currentPosY < entityBottom; currentPosY++) {
				int offsetY = getOffsetY(currentPosY);
				int offsetC = getOffsetChunk(entityLeft, currentPosY);

				if (World.getWorld().get(getChunkNo()+offsetC).getMap()[entityLeft+offsetX][(currentPosY+offsetY)*2+1]) {
					if (isBounce()) setVelocityX(-getVelocityX());
					else if (!isDestroy()) setVelocityX(0);
					setVelocityY(0);
					
					return 1 - ((fPosX - entityWidth/2f + 0.5f) - entityLeft);
				}
			}
		}
		
		
		
		

		if(getDecelerate()) {
			float velX = Math.abs(getPureVelocityX());

			//new speed = (rate - sqrt(old speed)) ^ 2
			float deltaTime = (float) (xDeceleration*frameSpeed - Math.sqrt(velX));

			if (deltaTime > 0) deltaTime = 0;
			float newVelX = (float) Math.pow(deltaTime, 2);

			if (getPureVelocityX() < 0) newVelX = -newVelX;
			setVelocityX(newVelX);
		}

		float finalSpeed = getVelocityX()*frameSpeed;

		if (finalSpeed > maxSpeed) finalSpeed = maxSpeed;
		if (finalSpeed < -maxSpeed) finalSpeed = -maxSpeed;

		return finalSpeed;
	}
	
	protected float getMovementY(float frameSpeed) {
		float fPosX = getPosX();
		float fPosY = getPosY();

		float entityWidth = getWidth();
		float entityHeight = getHeight();

		//if moving down or stationary in the Y axis
		if (getVelocityY() >= 0) {
			int entityBottom = (int) Math.floor(fPosY + entityHeight/2);

			int entityLeft = (int) Math.floor(fPosX - entityWidth/2 +0.5f);
			int entityRight = (int) Math.ceil(fPosX + entityWidth/2 +0.5f);

			int offsetY = getOffsetY(entityBottom);

			for (int currentPosX = entityLeft; currentPosX < entityRight; currentPosX++) {
				int offsetX = getOffsetX(currentPosX);
				int offsetC = getOffsetChunk(currentPosX, entityBottom);

				if (World.getWorld().get(getChunkNo()+offsetC).getMap()[currentPosX+offsetX][(entityBottom+offsetY)*2]) {
					if (isBounce()) setVelocityY(-getVelocityY());
					else if (!isDestroy()) setVelocityY(0);

					return entityBottom - (fPosY + entityHeight/2f);
				}
			}
		}

		//if moving up
		else {
			int entityTop = (int) Math.floor(fPosY - entityHeight/2);

			int entityLeft = (int) Math.floor(fPosX - entityWidth/2 +0.5f);
			int entityRight = (int) Math.ceil(fPosX + entityWidth/2 +0.5f);

			int offsetY = getOffsetY(entityTop);

			for (int currentPosX = entityLeft; currentPosX < entityRight; currentPosX++) {
				int offsetX = getOffsetX(currentPosX);
				int offsetC = getOffsetChunk(currentPosX, entityTop);
				
				if (World.getWorld().get(getChunkNo()+offsetC).getMap()[currentPosX+offsetX][(entityTop+offsetY)*2]) {
					if (isBounce()) setVelocityY(-getVelocityY());
					else if (!isDestroy()) setVelocityY(0);

					return (fPosY - entityHeight/2f) - entityTop;
				}
			}
		}


		if(getDecelerate()) {
			float velY = Math.abs(getPureVelocityY());

			//new speed = (rate - sqrt(old speed)) ^ 2
			float deltaTime = (float) (yDeceleration*frameSpeed - Math.sqrt(velY));

			if (deltaTime > 0) deltaTime = 0;
			float newVelY = (float) Math.pow(deltaTime, 2);

			if (getPureVelocityY() < 0) newVelY = -newVelY;
			setVelocityY(newVelY);
		}

		if (!isGravityImmune()) incVelocityY(yGravity*frameSpeed);

		float finalSpeed = getVelocityY()*frameSpeed;

		if (finalSpeed > maxSpeed) finalSpeed = maxSpeed;
		if (finalSpeed < -maxSpeed) finalSpeed = -maxSpeed;

		return finalSpeed;
	}

	protected void moveChunk() {
		//calculate if entity has moved chunk		
		if (getPosY() < 0) {
			//its above
			if (getChunkNo() > 0 && World.getWorld().get(getChunkNo()).getEntrance() == 0) {
				if (getChunkNo() > 0) {
					incChunkNo(-1);
					incPosY(World.getWorld().get(getChunkNo()).getLevelSize());
				}
			}else {
				setPosX(World.getWorld().get(getChunkNo()).getLevelSize());
				setPosY(World.getWorld().get(getChunkNo()).getLevelSize());
			}

		} else if (getPosY() >= World.getWorld().get(getChunkNo()).getLevelSize()) {
			//its below
			if (World.getWorld().get(getChunkNo()).getExit() == 2) {
				incChunkNo(1);
				incPosY(-World.getWorld().get(getChunkNo()-1).getLevelSize());
			}else {
				setPosX(World.getWorld().get(getChunkNo()).getLevelSize());
				setPosY(World.getWorld().get(getChunkNo()).getLevelSize());
			}

		} else if (getPosX() < 0) {
			//its left
			if (World.getWorld().get(getChunkNo()).getEntrance() == 1) {
				if (getChunkNo() > 0) {
					incChunkNo(-1);
					incPosX(World.getWorld().get(getChunkNo()).getLevelSize());
				}

			}else if (World.getWorld().get(getChunkNo()).getExit() == 1) {
				incChunkNo(1);
				incPosX(World.getWorld().get(getChunkNo()).getLevelSize());
			}else {
				setPosX(World.getWorld().get(getChunkNo()).getLevelSize());
				setPosY(World.getWorld().get(getChunkNo()).getLevelSize());
			}

		} else if (getPosX() >= World.getWorld().get(getChunkNo()).getLevelSize()) {
			//its right
			if (World.getWorld().get(getChunkNo()).getEntrance() == 3) {
				if (getChunkNo() > 0) {
					incChunkNo(-1);
					incPosX(-World.getWorld().get(getChunkNo()+1).getLevelSize());
				}
			}else if (World.getWorld().get(getChunkNo()).getExit() == 3) {
				incChunkNo(1);
				incPosX(-World.getWorld().get(getChunkNo()-1).getLevelSize());
			}else {
				setPosX(World.getWorld().get(getChunkNo()).getLevelSize());
				setPosY(World.getWorld().get(getChunkNo()).getLevelSize());
			}
		}
	}

	protected void insideWall() {
		//		if (isDestroy()) return;
		//
		//		float fPosX = getPosX();
		//		float fPosY = getPosY();
		//
		//		float entityWidth = getWidth();
		//
		//		//if moving down
		//		if (getVelocityY() >= 0) {
		//			int posX = (int) fPosX;
		//			int posY = (int) -Math.ceil(-fPosY+1); 
		//			int oY = getOffsetY(posY);
		//
		//			//check if inside a block
		//			int currentPosX = (int) (posX);
		//
		//			int oX = getOffsetX(currentPosX);
		//			int oC = getOffsetChunk(currentPosX, posY);
		//
		//			if (World.getWorld().get(getChunkNo()+oC).getMap()[currentPosX+oX][(posY+oY)*2]) {
		//				if (isBounce()) setVelocityY(-getVelocityY());
		//				else if (!isDestroy()) incPosY(-1);
		//			}
		//		}
	}

	protected void move(float frameSpeed) {
		moveChunk();
		insideWall();

		incPosX(getMovementX(frameSpeed));
		incPosY(getMovementY(frameSpeed));
	}

	//	public float distance(int chunk, float x, float y) {			
	//		float distX = getPosX() + World.getOffsetX(getChunkNo()) - x - World.getOffsetX(chunk);
	//		float distY = getPosY() + World.getOffsetY(getChunkNo()) - y - World.getOffsetY(chunk);
	//
	//		return (float) Math.sqrt(Math.pow(distX, 2) + Math.pow(distY, 2));
	//	}
	//	
	//	public boolean withinCircle(int chunk, float x, float y, float distance) {
	//		if (distance(chunk, x, y) > distance) return false;
	//		return true;
	//	}
	//	public boolean touchingCircle(Entity entity) {
	//		return withinCircle(entity.getChunkNo(), entity.getPosX(), entity.getPosY(), (entity.getWidth()+getWidth())/2);
	//	}

	public boolean touchingSquare(Entity entity) {
		//check X
		if (getPosX()+World.getOffsetX(getChunkNo())+getWidth()/2 < entity.getPosX()-entity.getWidth()/2) return false;
		if (getPosX()+World.getOffsetX(getChunkNo())-getWidth()/2 > entity.getPosX()+entity.getWidth()/2) return false;

		//check Y
		if (getPosY()+World.getOffsetY(getChunkNo())+getHeight()/2 < entity.getPosY()-entity.getHeight()/2) return false;
		if (getPosY()+World.getOffsetY(getChunkNo())-getHeight()/2 > entity.getPosY()+entity.getHeight()/2) return false;

		System.out.println(getPosX() +",	"+ getPosY());
		System.out.println(entity.getPosX() +",	"+ entity.getPosY());
		System.out.println();
		System.out.println((getPosX()+World.getOffsetX(getChunkNo())+getWidth()/2) +",	"+ (entity.getPosX()-entity.getWidth()/2));
		System.out.println((getPosX()+World.getOffsetX(getChunkNo())-getWidth()/2) +",	"+ (entity.getPosX()+entity.getWidth()/2));
		System.out.println((getPosY()+World.getOffsetY(getChunkNo())+getHeight()/2) +",	"+ (entity.getPosY()-entity.getHeight()/2));
		System.out.println((getPosY()+World.getOffsetY(getChunkNo())-getHeight()/2) +",	"+ (entity.getPosY()+entity.getHeight()/2));
		System.out.println();
		System.out.println((getPosX())+",	"+(getWidth()/2) +",	"+ (entity.getPosX())+",	"+(-entity.getWidth()/2));
		System.out.println((getPosX())+",	"+(-getWidth()/2) +",	"+ (entity.getPosX())+",	"+(+entity.getWidth()/2));
		System.out.println((getPosY())+",	"+(getHeight()/2) +",	"+ (entity.getPosY())+",	"+(-entity.getHeight()/2));
		System.out.println((getPosY())+",	"+(-getHeight()/2) +",	"+ (entity.getPosY())+",	"+(+entity.getHeight()/2));

		return true;
	}

	public boolean touchingSquarePlayer() {
		return touchingSquare(Player.getPlayer());

		//		//check X
		//		if (getPosX()+World.getOffsetX(getChunkNo())+getWidth() < Player.getPlayer().posX-3.5f) return false;
		//		if (getPosX()+World.getOffsetX(getChunkNo()) > Player.getPlayer().posX-3.5f+Player.getPlayer().getWidth()) return false;
		//
		//		//check Y
		//		if (getPosY()+World.getOffsetY(getChunkNo())+getHeight() < Player.getPlayer().posY-7) return false;
		//		if (getPosY()+World.getOffsetY(getChunkNo()) > Player.getPlayer().posY-7+Player.getPlayer().getHeight()) return false;
		//
		//		return true;
	}

	protected abstract void onDestruction();

	public abstract void action(float frameSpeed);
	public abstract void update(float frameSpeed);
	public abstract void render();

}
