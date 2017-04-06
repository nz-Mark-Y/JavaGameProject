package warlords.model;
import java.io.File;

import javafx.scene.media.AudioClip;
import javafx.scene.shape.Rectangle;
import warlords.tests.IWarlord;

public class Warlord implements IWarlord {
	private Paddle paddle;
	private int x;
	private int y;
	public static int height = 50;
	public static int length = 50; 
	private boolean computerControlled;
	private boolean dead;
	private boolean won;
	private int wallsLeft;
	private int classNum;
	private Rectangle warlordView;
	private int lastAbility;
	private AudioClip playerDeath = new AudioClip(new File("sounds/playerDeath.wav").toURI().toString());
	
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
	
	public Warlord(Paddle paddle, int x, int y, boolean control, int classNum) {
		this.paddle = paddle;
		this.x = x;
		this.y = y;
		dead = false;
		won = false;
		wallsLeft = 0;
		computerControlled = control;
		this.classNum = classNum;
		lastAbility = 0;
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
		paddle.getPaddleView().setVisible(false);
		warlordView.setVisible(false);
		dead = true;
		
		//Play sound
		playerDeath.play();
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
	
	public boolean isComputerControlled() {
		return computerControlled;
	}
	
	public void setPlayerControlled(boolean input) {
		computerControlled = input;
	}

	public int getClassNum() {
		return classNum;
	}
	
	public void setClassNum(int input) {
		classNum = input;
	}
	
	public void setWarlordView(Rectangle input) {
		warlordView = input;
	}
	
	public Rectangle getWarlordView() {
		return warlordView;
	}
	
	public int getLastAbility() {
		return lastAbility;
	}
	
	public void setLastAbility(int input) {
		lastAbility = input;
	}
}

