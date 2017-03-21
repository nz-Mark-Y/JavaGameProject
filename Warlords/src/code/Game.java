package code;
import warlordstest.IGame;

public class Game implements IGame {
	private Ball ball;
	
	public Game(Ball ball) {
		this.ball = ball;
	}
	
	@Override
	public void tick() {
		ball.setXPos(ball.getXPos() + ball.getXVelocity());
		ball.setYPos(ball.getYPos() + ball.getYVelocity());
	}

	@Override
	public boolean isFinished() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setTimeRemaining(int seconds) {
		// TODO Auto-generated method stub
		
	}

}
