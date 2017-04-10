package warlords.model;

import javafx.scene.shape.Rectangle;
import warlords.tests.IWarlord;

public class Warlord implements IWarlord {
	private Paddle paddle;
	private Paddle sheep;
	private int x;
	private int y;
	public static int height = 60;
	public static int length = 60; 
	private boolean computerControlled;
	private boolean dead;
	private boolean won;
	private int wallsLeft;
	private int classNum;
	private Rectangle warlordView;
	private int lastAbility;
	private boolean sheepGoingLeft = true;
	private int isImmune;
	
	// Default constructor for the wall, creates at (0, 0)
	public Warlord(Paddle paddle, boolean control, int classNum) {
		this.paddle = paddle;
		x = 0;
		y = 0;
		dead = false;
		won = false;
		wallsLeft = 0;
		computerControlled = control;
		lastAbility = 0;
	}
	
	// Constructor for the warlord, specifies if the AI controls the paddle, and the class of the warlord
	public Warlord(Paddle paddle, int x, int y, boolean control, int classNum) {
		this.paddle = paddle;
		this.x = x;
		this.y = y;
		this.classNum = classNum;
		dead = false;
		won = false;
		wallsLeft = 0;
		computerControlled = control;
		lastAbility = 0;
		if (classNum == 3) { // If the player is New Zealand
			sheep = new Paddle(paddle.getXPos(), paddle.getYPos()); // Create a sheep
		} else {
			sheep = null;
		}
		isImmune = -1;
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

	// Get if the player is dead
	@Override
	public boolean isDead() {
		return dead;
	}

	// Get if the player has won
	@Override
	public boolean hasWon() {
		return won;
	}
	
	// Get the X position
	public int getXPos() {
		return x;
	}
	
	// Set the Y position
	public int getYPos() {
		return y;
	}
	
	
	// When the player dies
	public void dies() {
		paddle.getPaddleView().setVisible(false); // Hide the paddle
		if (sheep != null) {
			sheep.getPaddleView().setVisible(false); // Hide the sheep
		}
		warlordView.setVisible(false); // Hide the warlord
		dead = true; // Set the warlord as dead
	}
	
	// Returns if the player has won
	public void wins() {
		won = true;
	}
	
	// Increments wallsLeft
	public void addWall() {
		wallsLeft++;
	}
	
	// Returns how many walls left
	public int getWallsLeft() {
		return wallsLeft;
	}
	
	// Returns the paddle
	public Paddle getPaddle() {
		return paddle;
	}
	
	// Returns if the paddle is computer controlled or not
	public boolean isComputerControlled() {
		return computerControlled;
	}
	
	// Set the player as computer controlled
	public void setPlayerControlled(boolean input) {
		computerControlled = input;
	}

	// Get the class number
	public int getClassNum() {
		return classNum;
	}
	
	// Set the class number
	public void setClassNum(int input) {
		classNum = input;
	}
	
	// Link the warlord to its warlord view in GameViewController
	public void setWarlordView(Rectangle input) {
		warlordView = input;
	}
	
	// Get the warlord view
	public Rectangle getWarlordView() {
		return warlordView;
	}
	
	// Get when the player last used their ability
	public int getLastAbility() {
		return lastAbility;
	}
	
	// Set when the player used their last ability
	public void setLastAbility(int input) {
		lastAbility = input;
	}
	
	// Get the sheep
	public Paddle getSheep() {
		return sheep;
	}
	
	// Get if the sheep if currently moving left
	public boolean getSheepGoingLeft() {
		return sheepGoingLeft;
	}
	
	// Set if the sheep is currently moving left
	public void setSheepGoingLeft(boolean input) {
		sheepGoingLeft = input;
	}
	
	// Get if the player is currently immune
	public int getImmune() {
		return isImmune;
	}
	
	// Set the player as immune
	public void setImmune(int input) {
		isImmune = input;
	}
}

