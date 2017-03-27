package warlords.model;
import warlords.tests.IWall;

public class Wall implements IWall {
	private int x;
	private int y;
	public static int height = 5;
	public static int length = 5; 
	private boolean destroyed;
	private int belongsTo;
	
	public Wall(int owner) {
		x = 0;
		y = 0;
		belongsTo = owner;
		destroyed = false;
	}
	
	public Wall(int x, int y, int owner) {
		this.x = x;
		this.y = y;
		belongsTo = owner;
		destroyed = false;
	}
	
	@Override
	public void setXPos(int x) {
		this.x = x;
	}

	@Override
	public void setYpos(int y) {
		this.y = y;
	}
	
	public int getXPos() {
		return x;
	}
	
	public int getYPos() {
		return y;
	}
	
	public int getOwner() {
		return belongsTo;
	}

	@Override
	public boolean isDestroyed() {
		return destroyed;
	}
	
	public void destroy() {
		destroyed = true;
	}

}
