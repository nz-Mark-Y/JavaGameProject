package warlords.model;

import javafx.scene.shape.Rectangle;
import warlords.tests.IWall;

public class Wall implements IWall {
	private int x;
	private int y;
	public static int height = 30;
	public static int length = 30; 
	private boolean destroyed;
	private int belongsTo;
	private Rectangle wallView;

	// Default constructor for the wall, creates at (0, 0)
	public Wall(int owner) {
		x = 0;
		y = 0;
		belongsTo = owner;
		destroyed = false;
	}

	// Constructor for the wall, also specifies the player that owns the wall
	public Wall(int x, int y, int owner) {
		this.x = x;
		this.y = y;
		belongsTo = owner;
		destroyed = false;
	}

	// Set the X position
	@Override
	public void setXPos(int x) {
		this.x = x;
	}
	
	// Set the Y position
	@Override
	public void setYpos(int y) {
		this.y = y;
	}

	// Get the X position
	public int getXPos() {
		return x;
	}
	
	// Get the Y position
	public int getYPos() {
		return y;
	}

	// Get the owner of the wall
	public int getOwner() {
		return belongsTo;
	}

	// Get if the wall is destroyed
	@Override
	public boolean isDestroyed() {
		return destroyed;
	}

	// Set the wall to destroyed
	public void destroy() {
		destroyed = true;
		wallView.setVisible(false); // Remove from the player's view
	}

	// Link the wall to the wall view in the GameViewController
	public void setWallView(Rectangle input) {
		wallView = input;
	}

	// Get the wall view
	public Rectangle getWallView() {
		return wallView;
	}
}
