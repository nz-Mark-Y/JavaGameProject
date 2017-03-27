package warlords.model;
import java.util.ArrayList;

import warlords.tests.IGame;

public class Game implements IGame {
	private Ball ball;
	private ArrayList<Warlord> playerList = new ArrayList<Warlord>();
	private ArrayList<Wall> wallList = new ArrayList<Wall>();
	private static int xBound;
	private static int yBound;
	private boolean finished;
	private int timeRemaining;
	
	public Game(Ball ball, int xBound, int yBound, ArrayList<Warlord> playerList, ArrayList<Wall> wallList) {
		this.ball = ball;
		Game.xBound = xBound;
		Game.yBound = yBound;
		this.playerList = playerList;
		this.wallList = wallList;
		finished = false;
		timeRemaining = 120;
	}
	
	@Override
	public void tick() {
		// Check if the ball is going to hit a warlord
		if (checkWarlords()) {
			checkWin();
			return;
		}
		
		// Check if the ball is going to hit a block
		if (checkWalls()) {
			checkWin();
			return;
		}
		
		// Check if the ball is going to hit a paddle
		if (checkPaddles()) {
			checkWin();
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
		
		checkWin();
		return;
	}
	
	// Check for paddles, if we hit one then rebound
	private boolean checkPaddles() {
		for (int i=0; i<playerList.size(); i++) {
			Paddle paddle = playerList.get(i).getPaddle();
			if (!playerList.get(i).isDead()) {
				int paddleX, paddleY;
				for (int j=0; j<Paddle.length; j++) { 
					for (int k=0; k<Paddle.height; k++) { 
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
		}
		return false;
	}
	
	// Check for walls, if we hit one then rebound and destroy the wall
	private boolean checkWalls() {
		for (int i=0; i<wallList.size(); i++) {
			Wall wall = wallList.get(i);
			if (!wall.isDestroyed()) {
				int wallX, wallY;
				for (int j=0; j<Wall.length; j++) {
					for (int k=0; k<Wall.height; k++) {
						wallX = wall.getXPos() + j;
						wallY = wall.getYPos() + k;
						if (coordInBallPath(wallX, wallY)) {
							if (((k == 0) && (ball.getYPos() < wall.getYPos())) || ((k == 4) && (ball.getYPos() > wall.getYPos()))) {
								ball.setYPos(wall.getYPos() - (ball.getYPos() + ball.getYVelocity() - wall.getYPos()));
								ball.setYVelocity(ball.getYVelocity() * -1);
								ball.setXPos(ball.getXPos() + ball.getXVelocity());
							} else {
								ball.setXPos(wall.getXPos() - (ball.getXPos() + ball.getXVelocity() - wall.getXPos()));
								ball.setXVelocity(ball.getXVelocity() * -1);
								ball.setYPos(ball.getYPos() + ball.getYVelocity());
							}	
							wall.destroy();
							return true;
						}
					}
				}
			}
		}
		return false;
	}
	
	// Check for warlords, if we hit one then rebound and destroy the warlord
	private boolean checkWarlords() {
		for (int i=0; i<playerList.size(); i++) {
			Warlord player = playerList.get(i);
			if (!player.isDead()) {
				int playerX, playerY;
				for (int j=0; j<Warlord.length; j++) {
					for (int k=0; k<Warlord.height; k++) {
						playerX = player.getXPos() + j;
						playerY = player.getYPos() + k;
						if (coordInBallPath(playerX, playerY)) {
							if (((k == 0) && (ball.getYPos() < player.getYPos())) || ((k == 4) && (ball.getYPos() > player.getYPos()))) {
								ball.setYPos(player.getYPos() - (ball.getYPos() + ball.getYVelocity() - player.getYPos()));
								ball.setYVelocity(ball.getYVelocity() * -1);
								ball.setXPos(ball.getXPos() + ball.getXVelocity());
							} else {
								ball.setXPos(player.getXPos() - (ball.getXPos() + ball.getXVelocity() - player.getXPos()));
								ball.setXVelocity(ball.getXVelocity() * -1);
								ball.setYPos(ball.getYPos() + ball.getYVelocity());
							}	
							player.dies();
							return true;
						}
					}
				}
			}
		}
		return false;
	}
	
	private void checkWin() {
		int winnerPos = 0;
		if (timeRemaining == 0) {
			for (int i=0; i<wallList.size(); i++) {
				playerList.get(wallList.get(i).getOwner()).addWall();
			}
			for (int i=0; i<playerList.size(); i++) {
				if (playerList.get(i).getWallsLeft() > playerList.get(winnerPos).getWallsLeft()) {
					winnerPos = i;
				}
			}
			playerList.get(winnerPos).wins();
			finished = true;
			return;
		}
		int playersAlive = playerList.size();
		for (int i=0; i<playerList.size(); i++) {
			if (playerList.get(i).isDead()) {
				playersAlive--;
			} else {
				winnerPos = i;
			}
		}
		if (playersAlive < 2) { 
			playerList.get(winnerPos).wins();
			finished = true;
		}
		return;
	}

	// Check if a coordinate on the ball will hit a coordinate
	private boolean coordInBallPath(int xCoord, int yCoord) {
		int x0, x1, y0, y1;
		for (int z=0; z<ball.length; z++) {
			for (int y=0; y<ball.height; y++) {
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
		return finished;
	}

	@Override
	public void setTimeRemaining(int seconds) {
		timeRemaining = seconds;
	}

}
