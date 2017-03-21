package code;
import warlordstest.IPaddle;

public class Paddle implements IPaddle {
	private int x;
	private int y;
	
	public Paddle(int init_x, int init_y) {
		this.x = init_x;
		this.y = init_y;
	}
	
	public Paddle() {
		this.x = 0;
		this.y = 0;
	}

	@Override
	public void setXPos(int x) {
		this.x = x;
	}

	@Override
	public void setYpos(int y) {
		this.y = y;
	}

}
