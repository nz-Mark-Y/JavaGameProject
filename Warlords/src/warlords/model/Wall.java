package warlords.model;

import java.util.Timer;
import java.util.TimerTask;

import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
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
	private boolean isTaken;
	private boolean isUnbreakable;

	// Default constructor for the wall, creates at (0, 0)
	public Wall(int owner) {
		x = 0;
		y = 0;
		belongsTo = owner;
		destroyed = false;
		isTaken = false;
		isUnbreakable = false;
	}

	// Constructor for the wall, also specifies the player that owns the wall
	public Wall(int x, int y, int owner) {
		this.x = x;
		this.y = y;
		belongsTo = owner;
		destroyed = false;
		isTaken = false;
		isUnbreakable = false;
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
	
	// Get if the wall is taken by the Russia ability
	public boolean getTaken() {
		return isTaken;
	}
	
	// Set if the wall is taken by the Russia ability
	public void setTaken(boolean taken) {
		isTaken = taken;
	}
	
	// Get if the wall is unbreakable or not
	public boolean getUnbreakable() {
		return isUnbreakable;
	}
		
	// Set if the wall is unbreakable or not
	public void setUnbreakable(boolean input, int playerNum) {
		isUnbreakable = input;
		final ImagePattern oldFill = (ImagePattern) wallView.getFill(); // Save the old fill
		wallView.setFill(wallPattern); // Add the new fill
		if (input == true) {	
			Timer unbreakTime = new Timer();
			unbreakTime.schedule(new TimerTask() { // 5 seconds of unbreakable
				@Override
				public void run() {
					isUnbreakable = false;
					wallView.setFill(oldFill); // Return to the old fill
				}			
			}, 5000);
		}
	}
	
	// Patterns for pyramids
	private Image wallImage = new Image("file:images/unbreakable.png");
	ImagePattern wallPattern = new ImagePattern(wallImage);
}
