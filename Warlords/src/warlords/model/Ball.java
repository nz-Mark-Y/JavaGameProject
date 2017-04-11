package warlords.model;

import javafx.scene.shape.Circle;
import warlords.tests.IBall;

public class Ball implements IBall {
	private int x;
	private int y;
	private int dx;
	private int dy;
	public static int height = 16;
	public static int length = 16; 
	private Circle ballView;
	private boolean bulletBall;
	private boolean spiderBall;
	
	// Ball constructor for regular ball
	public Ball(int init_x, int init_y) {
		x = init_x;
		y = init_y;
		dx = 0;
		dy = 0;
		bulletBall = false;
		spiderBall = false;
	}
	
	
	// Ball constructor for if special
	public Ball(int init_x, int init_y, boolean isBullet, boolean isSpider) {
		x = init_x;
		y = init_y;
		dx = 0;
		dy = 0;
		bulletBall = isBullet;
		spiderBall = isSpider;
	}
	
	// Ball constructor for default ball at (0, 0)
	public Ball() {
		x = 0;
		y = 0;
		dx = 0;
		dy = 0;
		bulletBall = false;
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
	@Override
	public int getXPos() {
		return x;
	}

	// Get the Y position
	@Override
	public int getYPos() {
		return y;
	}

	// Set the X velocity
	@Override
	public void setXVelocity(int dX) {
		dx = dX;
		
	}

	// Set the Y velocity
	@Override
	public void setYVelocity(int dY) {
		dy = dY;
		
	}

	// Get the X velocity
	@Override
	public int getXVelocity() {
		return dx;
	}

	// Get the Y velocity
	@Override
	public int getYVelocity() {
		return dy;
	}

	// Link the ball to its view in the GameViewController
	public void setBallView(Circle input) {
		ballView = input;
	}
	
	// Get the ball's view
	public Circle getBallView() {
		return ballView;
	}
	
	// Get if the ball is a bullet or not
	public boolean isBullet() {
		return bulletBall;
	}
	
	// Get if the ball is a spider or not
		public boolean isSpider() {
			return spiderBall;
		}
}
