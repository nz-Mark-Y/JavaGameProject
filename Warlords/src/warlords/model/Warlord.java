package warlords.model;
import warlords.tests.IWarlord;

public class Warlord implements IWarlord {
	private Paddle paddle;
	
	public Warlord(Paddle paddle) {
		this.paddle = paddle;
	}
	
	@Override
	public void setXPos(int x) {
		// TODO Auto-generated method stub		
	}

	@Override
	public void setYpos(int y) {
		// TODO Auto-generated method stub	
	}

	@Override
	public boolean isDead() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean hasWon() {
		// TODO Auto-generated method stub
		return false;
	}
	
	public Paddle getPaddle() {
		return paddle;
	}

}
