package warlords.model;
import warlords.tests.IWarlord;

public class Warlord implements IWarlord {
	private Paddle paddle;
	private int x;
	private int y;
	public static int height = 50;
	public static int length = 50; 
	private boolean playerControlled;
	private boolean dead;
	private boolean won;
	private int wallsLeft;
	
	public Warlord(Paddle paddle, boolean control) {
		this.paddle = paddle;
		x = 0;
		y = 0;
		dead = false;
		won = false;
		wallsLeft = 0;
		playerControlled = control;
	}
	
	public Warlord(Paddle paddle, int x, int y, boolean control) {
		this.paddle = paddle;
		this.x = x;
		this.y = y;
		dead = false;
		won = false;
		wallsLeft = 0;
		playerControlled = control;
	}
	
	@Override
	public void setXPos(int x) {
		this.x = x;
	}

	@Override
	public void setYpos(int y) {
		this.y = y;
	}

	@Override
	public boolean isDead() {
		return dead;
	}

	@Override
	public boolean hasWon() {
		return won;
	}
	
	public int getXPos() {
		return x;
	}
	
	public int getYPos() {
		return y;
	}
	
	public void dies() {
		dead = true;
	}
	
	public void wins() {
		won = true;
	}
	
	public void addWall() {
		wallsLeft++;
	}
	
	public int getWallsLeft() {
		return wallsLeft;
	}
	
	public Paddle getPaddle() {
		return paddle;
	}
	
	public boolean isPlayerControlled() {
		return playerControlled;
	}
	
	public void setPlayerControlled(boolean input) {
		playerControlled = input;
	}

}
