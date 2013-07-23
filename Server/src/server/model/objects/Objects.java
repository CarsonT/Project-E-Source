package server.model.objects;


public class Objects {

	public int objectId, objectX, objectY, objectHeight, objectFace, objectType, objectTicks, xp, item, owner, target;
	public long delay, oDelay;
	public boolean bait;
    public String belongsTo;
	
	public Objects(int id, int x, int y, int height, int face, int type, int ticks) {
		this.objectId = id;
		this.objectX = x;
		this.objectY = y;
		this.objectHeight = height;
		this.objectFace = face;
		this.objectType = type;
		this.objectTicks = ticks;
	}
	

	public int getObjectId() {
		return this.objectId;
	}
	
	public int getObjectX() {
		return this.objectX;
	}
	
	public int getObjectY() {
		return this.objectY;
	}
	
	public int getObjectHeight() {
		return this.objectHeight;
	}
	
	public int getObjectFace() {
		return this.objectFace;
	}
	
	public int getObjectType() {
		return this.objectType;
	}
	
	
}