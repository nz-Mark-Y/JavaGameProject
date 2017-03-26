package warlords.model;
import warlords.tests.IPaddle;

public class Paddle implements IPaddle {
	private int x;
	private int y;
	
	public Paddle(int init_x, int init_y) {
		x = init_x;
		y = init_y;
	}
	
	public Paddle() {
		x = 0;
		y = 0;
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

}
