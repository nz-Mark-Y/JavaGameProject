package warlords.model;
import warlords.tests.IBall;

public class Ball implements IBall {
	private int x;
	private int y;
	private int dx;
	private int dy;
	
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
		return this.x;
	}

	@Override
	public int getYPos() {
		return this.y;
	}

	@Override
	public void setXVelocity(int dX) {
		this.dx = dX;
		
	}

	@Override
	public void setYVelocity(int dY) {
		this.dy = dY;
		
	}

	@Override
	public int getXVelocity() {
		return this.dx;
	}

	@Override
	public int getYVelocity() {
		return this.dy;
	}

}
