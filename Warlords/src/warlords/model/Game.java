package warlords.model;
import java.util.ArrayList;

import warlords.tests.IGame;

public class Game implements IGame {
	private Ball ball;
	private ArrayList<Warlord> playerList = new ArrayList<Warlord>();
	private static int xBound;
	private static int yBound;
	
	public Game(Ball ball, int xBound, int yBound, Warlord playerOne) {
		this.ball = ball;
		this.xBound = xBound;
		this.yBound = yBound;
		this.playerList.add(playerOne);
	}
	
	@Override
	public void tick() {
		if (ball.getXPos() + ball.getXVelocity() >= xBound) {
			ball.setXPos(xBound - (ball.getXPos() + ball.getXVelocity() - xBound));
			ball.setXVelocity(ball.getXVelocity() * -1);
		} else if (ball.getXPos() + ball.getXVelocity() <= 0) {	
			ball.setXPos((ball.getXPos() + ball.getXVelocity()) * -1);
			ball.setXVelocity(ball.getXVelocity() * -1);
		} else {
			
			ball.setXPos(ball.getXPos() + ball.getXVelocity());
		}
		
		if (ball.getYPos() + ball.getYVelocity() >= yBound) {
			ball.setYPos(yBound - (ball.getYPos() + ball.getYVelocity() - yBound));
			ball.setYVelocity(ball.getYVelocity() * -1);
		} else if (ball.getYPos() + ball.getYVelocity() <= 0) {	
			ball.setYPos((ball.getYPos() + ball.getYVelocity()) * -1);
			ball.setYVelocity(ball.getYVelocity() * -1);
		} else {
			
			ball.setYPos(ball.getYPos() + ball.getYVelocity());
		}		
	}
	
	private boolean checkPaddles() {
		for (int i = 0; i < this.playerList.size(); i++) {
			
		}
		return true;
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
