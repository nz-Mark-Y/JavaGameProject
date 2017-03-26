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
		playerList.add(playerOne);
	}
	
	@Override
	public void tick() {
		// Check if the ball is going to hit a block
		
		// Check if the ball is going to hit a paddle
		if (checkPaddles()) {
			return;
		}
		
		// Check for boundaries, if we hit one then rebound
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
	
	// Check for paddles, if we hit one then rebound
	private boolean checkPaddles() {
		for (int i=0; i<playerList.size(); i++) {
			Paddle paddle = playerList.get(i).getPaddle();
			int paddleX, paddleY;
			for (int j=0; j<5; j++) { // Paddle length of 5
				for (int k=0; k<5; k++) { // Paddle height of 5
					paddleX = paddle.getXPos() + j;
					paddleY = paddle.getYPos() + k;
					if (coordInBallPath(paddleX, paddleY)) {
						if (((k == 0) && (ball.getYPos() < paddle.getYPos())) || ((k == 4) && (ball.getYPos() > paddle.getYPos()))) {
							ball.setYPos(paddle.getYPos() - (ball.getYPos() + ball.getYVelocity() - paddle.getYPos()));
							ball.setYVelocity(ball.getYVelocity() * -1);
							ball.setXPos(ball.getXPos() + ball.getXVelocity());
						} else {
							ball.setXPos(paddle.getXPos() - (ball.getXPos() + ball.getXVelocity() - paddle.getXPos()));
							ball.setXVelocity(ball.getXVelocity() * -1);
							ball.setYPos(ball.getYPos() + ball.getYVelocity());
						}	
						return true;
					} 			
				}
			}
		}
		return false;
	}

	// Check if a coordinate on the ball will hit a coordinate
	private boolean coordInBallPath(int xCoord, int yCoord) {
		int x0, x1, y0, y1;
		for (int z=0; z<5; z++) { // Ball length of 5;
			for (int y=0; y<5; y++) { // Ball height of 5;
				x0 = ball.getXPos() + z;
				x1 = ball.getXPos() + ball.getXVelocity() + z;
				y0 = ball.getYPos() + y;
				y1 = ball.getYPos() + ball.getYVelocity() + y;		
				ArrayList<int []> coordsList = new ArrayList<int []>();
				
				// Bresenham's Algorithm
				int deltaX = x1 - x0;
				int deltaY = y1 - y0;
				if (deltaX == 0) {
					deltaX = 1;
				}
				double deltaError = Math.abs(deltaY / deltaX);
				double error = deltaError - 0.5;
				int yTemp = y0;
				for (int i=x0; i<x1; i++) {
					int[] tempArray = {i, yTemp};
					coordsList.add(tempArray);
					error = error + deltaError;
					if (error >= 0.5) {
						yTemp++;
						error = error - 1.0;
					}
				
				}
				// Check coords for hit	
				for (int i=0; i<coordsList.size();i++) {
					if ((xCoord == coordsList.get(i)[0]) && (yCoord == coordsList.get(i)[1])) {
						return true;
					}
				}
			}
		}
		return false;
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
