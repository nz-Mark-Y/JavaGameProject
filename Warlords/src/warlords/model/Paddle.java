package warlords.model;
import javafx.scene.shape.Rectangle;
import warlords.tests.IPaddle;

public class Paddle implements IPaddle {
	private int x;
	private int y;
	public static int height = 40;
	public static int length = 40;
	private float theta;
	private Rectangle paddleView;
	
	public Paddle(int init_x, int init_y) {
		x = init_x;
		y = init_y;
		theta = 45;
	}
	
	public Paddle() {
		x = 0;
		y = 0;
		theta = 45;
	}

	@Override
	public void setXPos(int x) {
		this.x = x;
	}

	@Override
	public void setYPos(int y) {
		this.y = y;
	}
	
	public int getXPos() {
		return x;
	}
	
	public int getYPos() {
		return y;
	}
	
	public float getTheta() {
		return theta;
	}
	
	public void incrTheta(float x) {
		theta += x;
	}
	
	public void decrTheta(float x) {
		theta -= x;
	}

	public void setPaddleView(Rectangle input) {
		paddleView = input;
	}
	
	public Rectangle getPaddleView() {
		return paddleView;
	}
}
