package entities;

import world.World;

public interface Collidable {
	public static final float yGravity = 0.03f;

	public static final float xDeceleration = 0.001f;
	public static final float yDeceleration = 0.001f;

	public boolean gravityImmune();

	public boolean getDecelerate();

	public float getWidth();
	public float getHeight();

	public float getPosX();
	public float getPosY();
	public void setPosX(float posX);
	public void setPosY(float posY);
	public void incPosX(float posX);
	public void incPosY(float posY);

	public float getPureVelocityX();
	public float getPureVelocityY();

	public float getVelocityX();
	public float getVelocityY();

	public void setVelocityX(float X);
	public void setVelocityY(float Y);
	public void setVelocities(float X, float Y);

	public void incVelocityX(float Y);
	public void incVelocityY(float Y);

	public int getChunkNo();
	public void incChunkNo(int inc);

	public boolean bounce();
	public boolean destroy();

	default int getOffsetX(float posX) {
		//correct for chunk
		//if left
		if (posX < 0) {
			if (World.world.get(getChunkNo()).getExit() == 1) {
				//if exit left
				return World.world.get(getChunkNo()+1).getLevelSize();
			} else if (World.world.get(getChunkNo()).getEntrance() == 1) {
				//if entrance left
				return World.world.get(getChunkNo()-1).getLevelSize();
			}
		}
		//if right
		else if (posX > World.world.get(getChunkNo()).getLevelSize()-1) {
			if (World.world.get(getChunkNo()).getExit() == 3) {
				//if exit right
				return -World.world.get(getChunkNo()).getLevelSize();
			} else if (World.world.get(getChunkNo()).getEntrance() == 3) {
				//if entrance right
				return -World.world.get(getChunkNo()).getLevelSize();
			}
		}
		return 0;
	}
	default int getOffsetY(float posY) {
		//correct for chunk
		//if above
		if (posY < 0) {
			if (World.world.get(getChunkNo()).getExit() == 0) {
				//if exit above
				return World.world.get(getChunkNo()+1).getLevelSize();
			}else if (getChunkNo() > 0 && World.world.get(getChunkNo()).getEntrance() == 0) {
				//if entrance left
				return World.world.get(getChunkNo()-1).getLevelSize();
			}
		}
		//if below
		else if (posY > World.world.get(getChunkNo()).getLevelSize()-1) {
			if (World.world.get(getChunkNo()).getExit() == 2) {
				//if exit right
				return -World.world.get(getChunkNo()).getLevelSize();
			} else if (World.world.get(getChunkNo()).getEntrance() == 2) {
				//if entrance right
				return -World.world.get(getChunkNo()).getLevelSize();
			}
		}
		return 0;
	}
	default int getOffsetChunk(float posX, float posY) {
		//correct for chunk
		//if above
		if (posY < 0) {
			if (World.world.get(getChunkNo()).getExit() == 0) {
				//if exit above
				return 1;
			}else if (World.world.get(getChunkNo()).getEntrance() == 0) {
				//if entrance left
				return -1;
			}
		}
		//if below
		else if (posY > World.world.get(getChunkNo()).getLevelSize()-1) {
			if (World.world.get(getChunkNo()).getExit() == 2) {
				//if exit right
				return 1;
			} else if (World.world.get(getChunkNo()).getEntrance() == 2) {
				//if entrance right
				return -1;
			}
		}
		//if left
		if (posX < 0) {
			if (World.world.get(getChunkNo()).getExit() == 1) {
				//if exit left
				return 1;
			} else if (World.world.get(getChunkNo()).getEntrance() == 1) {
				//if entrance left
				return -1;
			}
		}
		//if right
		else if (posX > World.world.get(getChunkNo()).getLevelSize()-1) {
			if (World.world.get(getChunkNo()).getExit() == 3) {
				//if exit right
				return 1;
			} else if (World.world.get(getChunkNo()).getEntrance() == 3) {
				//if entrance right
				return -1;
			}
		}
		return 0;
	}

	default float getMovementX(float frameSpeed) {
		float fPosX = getPosX();
		float fPosY = getPosY();

		float entityWidth = getWidth();
		float entityHeight = getHeight();

		//if y coord is exact, yExact = -1 else yExact = 0
		int yExact = 1;
		if (fPosY == Math.ceil(fPosY)) {
			yExact = 0;
		}

		//if moving left
		if (this.getVelocityX() <= 0) {
			int posX = (int) Math.ceil(fPosX);
			int posY = (int) fPosY;
			int currentPosY;
			int oX = getOffsetX(posX-entityWidth-1);
			int oY;
			int oC;

			//check if inside a block
			for (int n = 0; n < entityHeight+yExact; n++) {
				currentPosY = posY - n - 1;

				oY = getOffsetY(currentPosY);
				oC = getOffsetChunk(posX-entityWidth-1, currentPosY);

				if (World.world.get(getChunkNo()+oC).getMap()[(int) (posX-entityWidth-1+oX)][currentPosY+oY].solid()) {
					if (bounce()) setVelocityX(-getVelocityX());
					else if (!destroy()) setVelocityX(0);

					return posX - fPosX;
				}
			}	
		} 

		//if moving right
		if (getVelocityX() >= 0) {
			int posX = (int) -Math.ceil(-fPosX);
			int posY = (int) fPosY;
			int currentPosY;
			int oX = getOffsetX(posX);
			int oY;
			int oC;

			//check if inside a block
			for (int n = 0; n < entityHeight+yExact; n++) {
				currentPosY = posY - n - 1;

				oY = getOffsetY(currentPosY);
				oC = getOffsetChunk(posX, currentPosY);

				if (World.world.get(getChunkNo()+oC).getMap()[posX+oX][currentPosY+oY].solid()) {
					if (bounce()) setVelocityX(-getVelocityX());
					else if (!destroy()) setVelocityX(0);

					return posX - fPosX;
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

		return getVelocityX()*frameSpeed;
	}
	default float getMovementY(float frameSpeed) {
		float fPosX = getPosX();
		float fPosY = getPosY();

		float entityWidth = getWidth();
		float entityHeight = getHeight();

		//if y coord is exact, yExact = -1 else xExact = 0
		int xExact = 1;
		if (fPosX == Math.ceil(fPosX)) {
			xExact = 0;
		}

		//if moving down
		if (getVelocityY() >= 0) {
			int posX = (int) fPosX;
			int posY = (int) -Math.ceil(-fPosY);
			int currentPosX;
			int oX;
			int oY = getOffsetY(posY);
			int oC;

			//check if inside a block
			for (int n = -xExact; n < entityWidth; n++) {
				currentPosX = posX - n - 1;

				oX = getOffsetX(currentPosX);
				oC = getOffsetChunk(currentPosX, posY);

				if (World.world.get(getChunkNo()+oC).getMap()[currentPosX+oX][posY+oY].solid()) {
					if (bounce()) setVelocityY(-getVelocityY());
					else if (!destroy()) setVelocityY(0);

					return posY - fPosY;
				}
			}
		}
		
		//if moving up
		if (getVelocityY() < 0) {
			int posX = (int) fPosX;
			int posY = (int) Math.ceil(fPosY);
			int currentPosX;
			int oX; 
			int oY = getOffsetY(posY-entityHeight-1); 
			int oC;

			//check if inside a block
			for (int n = -xExact; n < entityWidth; n++) {
				currentPosX = posX - n - 1;

				oX = getOffsetX(currentPosX);
				oC = getOffsetChunk(currentPosX, posY-entityHeight-1);

				if (World.world.get(getChunkNo()+oC).getMap()[currentPosX+oX][(int) (posY-entityHeight-1+oY)].solid()) {
					if (bounce()) setVelocityY(-getVelocityY());
					else if (!destroy()) setVelocityY(0);
					
					return posY - fPosY;
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

		if (!gravityImmune()) incVelocityY(yGravity*frameSpeed);

		return getVelocityY()*frameSpeed;
	}

	default void moveChunk() {
		//calculate if entity has moved chunk
		if (getPosY() < 0) {
			//its above
			if (getChunkNo() > 0 && World.world.get(getChunkNo()).getEntrance() == 0) {
				if (getChunkNo() > 0) {
					incChunkNo(-1);
					incPosY(World.world.get(getChunkNo()).getLevelSize());
				}
			}else {
				setPosX(World.world.get(getChunkNo()).getLevelSize()/2);
				setPosY(World.world.get(getChunkNo()).getLevelSize()/2);
			}

		} else if (getPosY() >= World.world.get(getChunkNo()).getLevelSize()) {
			//its below
			if (World.world.get(getChunkNo()).getExit() == 2) {
				incChunkNo(1);
				incPosY(-World.world.get(getChunkNo()-1).getLevelSize());
			}else {
				setPosX(World.world.get(getChunkNo()).getLevelSize()/2);
				setPosY(World.world.get(getChunkNo()).getLevelSize()/2);
			}

		} else if (getPosX() < 0) {
			//its left
			if (World.world.get(getChunkNo()).getEntrance() == 1) {
				if (getChunkNo() > 0) {
					incChunkNo(-1);
					incPosX(World.world.get(getChunkNo()).getLevelSize());
				}

			}else if (World.world.get(getChunkNo()).getExit() == 1) {
				incChunkNo(1);
				incPosX(World.world.get(getChunkNo()).getLevelSize());
			}else {
				setPosX(World.world.get(getChunkNo()).getLevelSize()/2);
				setPosY(World.world.get(getChunkNo()).getLevelSize()/2);
			}

		} else if (getPosX() >= World.world.get(getChunkNo()).getLevelSize()) {
			//its right
			if (World.world.get(getChunkNo()).getEntrance() == 3) {
				if (getChunkNo() > 0) {
					incChunkNo(-1);
					incPosX(-World.world.get(getChunkNo()+1).getLevelSize());
				}
			}else if (World.world.get(getChunkNo()).getExit() == 3) {
				incChunkNo(1);
				incPosX(-World.world.get(getChunkNo()-1).getLevelSize());
			}else {
				setPosX(World.world.get(getChunkNo()).getLevelSize()/2);
				setPosY(World.world.get(getChunkNo()).getLevelSize()/2);
			}
		}
	}
}
