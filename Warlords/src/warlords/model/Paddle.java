package warlords.model;

import java.util.Timer;
import java.util.TimerTask;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import warlords.tests.IPaddle;

public class Paddle implements IPaddle {
	private int x;
	private int y;
	public static int height = 40;
	public static int length = 40;
	private float theta;
	private Rectangle paddleView;
	private boolean isSlowed;
	
	// Constructor for the paddle
	public Paddle(int init_x, int init_y) {
		x = init_x;
		y = init_y;
		theta = 45;
		isSlowed = false;
	}
	
	// Default constructor for the paddle, creates at (0, 0)
	public Paddle() {
		x = 0;
		y = 0;
		theta = 45;
		isSlowed = false;
	}

	// Set the X position
	@Override
	public void setXPos(int x) {
		this.x = x;
	}
	
	// Set the Y position
	@Override
	public void setYPos(int y) {
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

	// Get the angle
	public float getTheta() {
		return theta;
	}
	
	// Increase the angle by x degrees
	public void incrTheta(float x) {
		theta += x;
	}
	
	// Decrease the angle by x degrees
	public void decrTheta(float x) {
		theta -= x;
	}

	// Link the paddle to the paddle view in GameViewController
	public void setPaddleView(Rectangle input, int playerNum) {
		paddleView = input;
		rotatePaddle(playerNum);
	}
	
	// Set the rotation of the paddle based on its angle and owner
	public void rotatePaddle(int playerNum) {
		if (playerNum == 0) {
			paddleView.setRotate(-getTheta());
		} else if (playerNum == 1) {
			paddleView.setRotate(180 + getTheta());
		} else if (playerNum == 2) {
			paddleView.setRotate(180 - getTheta());
		} else {
			paddleView.setRotate(getTheta());
		}
	}
	
	// Get the view of the paddle
	public Rectangle getPaddleView() {	
		return paddleView;
	}
	
	// Get if the paddle is slowed or not
	public boolean getSlow() {
		return isSlowed;
	}
	
	// Set if the paddle is slowed or not
	public void setSlow(boolean input) {
		isSlowed = input;
		if (input == true) {
			paddleView.setStroke(Color.LAWNGREEN);
			paddleView.setStrokeWidth(3.0);
			Timer slowTime = new Timer();
			slowTime.schedule(new TimerTask() { // 5 seconds of slow
				@Override
				public void run() {
					isSlowed = false;	
					paddleView.setStrokeWidth(0);
				}			
			}, 5000);
		}
	}
}
