package warlords.model;
import javafx.scene.shape.Circle;
import warlords.tests.IBall;

public class Ball implements IBall {
	private int x;
	private int y;
	private int dx;
	private int dy;
	public static int height = 10;
	public static int length = 10; 
	private Circle ballView;
	
	public Ball(int init_x, int init_y) {
		x = init_x;
		y = init_y;
		dx = 0;
		dy = 0;
	}
	
	public Ball() {
		x = 0;
		y = 0;
		dx = 0;
		dy = 0;
	}
	
	
	@Override
	public void setXPos(int x) {
		this.x = x;
		
	}

	@Override
	public void setYPos(int y) {
		this.y = y;
		
	}

	@Override
	public int getXPos() {
		return x;
	}

	@Override
	public int getYPos() {
		return y;
	}

	@Override
	public void setXVelocity(int dX) {
		dx = dX;
		
	}

	@Override
	public void setYVelocity(int dY) {
		dy = dY;
		
	}

	@Override
	public int getXVelocity() {
		return dx;
	}

	@Override
	public int getYVelocity() {
		return dy;
	}

	public void setBallView(Circle input) {
		ballView = input;
	}
	
	public Circle getBallView() {
		return ballView;
	}
}
