package warlords.model;
import java.util.ArrayList;

import warlords.control.KeyHandlers;
import warlords.tests.IGame;

public class Game implements IGame {
	private Ball ball;
	private ArrayList<Warlord> playerList = new ArrayList<Warlord>();
	private ArrayList<Wall> wallList = new ArrayList<Wall>();
	private static int xBound;
	private static int yBound;
	private boolean finished;
	private int timeRemaining;
	private int ticksSinceLastHit;
	
	public Game(Ball ball, int xBound, int yBound, ArrayList<Warlord> playerList, ArrayList<Wall> wallList) {
		this.ball = ball;
		Game.xBound = xBound;
		Game.yBound = yBound;
		this.playerList = playerList;
		this.wallList = wallList;
		finished = false;
		timeRemaining = 120;
		ticksSinceLastHit = 11;
	}
	
	@Override
	public void tick() {	
		if (ticksSinceLastHit > 10) {
			// Check if the ball is going to hit a paddle
			if (checkPaddles()) {
				ticksSinceLastHit = 0;
				checkWin();
				return;
			}
				
			// Check if the ball is going to hit a block
			if (checkWalls()) {
				ticksSinceLastHit = 0;
				checkWin();
				return;
			}
				
			// Check if the ball is going to hit a warlord
			if (checkWarlords()) {
				ticksSinceLastHit = 0;
				checkWin();
				return;
			}
		} else {
			ticksSinceLastHit++;
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
			//if (!playerList.get(i).isDead()) {
				Paddle paddle = playerList.get(i).getPaddle();
				int paddleX, paddleY;
				for (int j=0; j<Paddle.length; j++) { // column by column
					for (int k=0; k<Paddle.height; k++) { // row by row
						if ((k == 0) || (k == Paddle.height - 1) || (j == 0) || (j == Paddle.length - 1)) {
							paddleX = paddle.getXPos() + j;
							paddleY = paddle.getYPos() + k;
							if (coordInBallPath(paddleX, paddleY)) { 
								if ((k == 0) && (ball.getYVelocity() > 0)) { // bottom side, reverse y
									ball.setYPos(paddle.getYPos() - (ball.getYPos() + ball.getYVelocity() + Ball.height - paddle.getYPos()));
									ball.setYVelocity(ball.getYVelocity() * -1);
									ball.setXPos(ball.getXPos() + ball.getXVelocity()); 
								} else if ((k == Paddle.height - 1) && (ball.getYVelocity() < 0)) { // top side, reverse y
									ball.setYPos(paddle.getYPos() + Paddle.height - (ball.getYPos() + ball.getYVelocity() - Paddle.height - paddle.getYPos()));
									ball.setYVelocity(ball.getYVelocity() * -1);
									ball.setXPos(ball.getXPos() + ball.getXVelocity());
								} else if ((j == Paddle.length - 1) && (ball.getXVelocity() < 0)) { // right side, reverse x
									ball.setXPos(paddle.getXPos() + Paddle.length - (ball.getXPos() + ball.getXVelocity() - Paddle.length - paddle.getXPos()));
									ball.setXVelocity(ball.getXVelocity() * -1);
									ball.setYPos(ball.getYPos() + ball.getYVelocity());
								} else if ((j == 0) && (ball.getXVelocity() > 0)){ // left side, reverse x
									ball.setXPos(paddle.getXPos() - (ball.getXPos() + ball.getXVelocity() + Ball.length - paddle.getXPos()));
									ball.setXVelocity(ball.getXVelocity() * -1);
									ball.setYPos(ball.getYPos() + ball.getYVelocity());
								} else {
									System.out.println("Unaccounted paddle hit at: " + j + ", " + k);
									System.out.println("\t Ball velocity: " + ball.getXVelocity() + ", " + ball.getYVelocity());
								}
								return true;
							} 
						}							
					}
				}
			//}
		}
		return false;
	}
	
	// Check for walls, if we hit one then rebound and destroy the wall
	private boolean checkWalls() {
		for (int i=0; i<wallList.size(); i++) {
			Wall wall = wallList.get(i);
			//if (!wall.isDestroyed()) {
				int wallX, wallY;
				for (int j=0; j<Wall.length; j++) { // column by column
					for (int k=0; k<Wall.height; k++) { // row by row
						if ((k == 0) || (k == Wall.height - 1) || (j == 0) || (j == Wall.length - 1)) {
							wallX = wall.getXPos() + j;
							wallY = wall.getYPos() + k;
							if (coordInBallPath(wallX, wallY)) { 
								if ((k == 0) && (ball.getYVelocity() > 0)) { // bottom side, reverse y
									ball.setYPos(wall.getYPos() - (ball.getYPos() + ball.getYVelocity() + Ball.height - wall.getYPos()));
									ball.setYVelocity(ball.getYVelocity() * -1);
									ball.setXPos(ball.getXPos() + ball.getXVelocity()); 
								} else if ((k == Wall.height - 1) && (ball.getYVelocity() < 0)) { // top side, reverse y
									ball.setYPos(wall.getYPos() + Wall.height - (ball.getYPos() + ball.getYVelocity() - Wall.height - wall.getYPos()));
									ball.setYVelocity(ball.getYVelocity() * -1);
									ball.setXPos(ball.getXPos() + ball.getXVelocity());
								} else if ((j == Wall.length - 1) && (ball.getXVelocity() < 0)) { // right side, reverse x
									ball.setXPos(wall.getXPos() + Wall.length - (ball.getXPos() + ball.getXVelocity() - Wall.length - wall.getXPos()));
									ball.setXVelocity(ball.getXVelocity() * -1);
									ball.setYPos(ball.getYPos() + ball.getYVelocity());
								} else if ((j == 0) && (ball.getXVelocity() > 0)){ // left side, reverse x
									ball.setXPos(wall.getXPos() - (ball.getXPos() + ball.getXVelocity() + Ball.length - wall.getXPos()));
									ball.setXVelocity(ball.getXVelocity() * -1);
									ball.setYPos(ball.getYPos() + ball.getYVelocity());
								} else {
									System.out.println("Unaccounted wall hit at: " + j + ", " + k);
									System.out.println("\t Ball velocity: " + ball.getXVelocity() + ", " + ball.getYVelocity());
								}
								wall.destroy();
								return true;
							} 
						}							
					}
				}
			//}
		}
		return false;
	}
	
	// Check for warlords, if we hit one then rebound and destroy the warlord
	private boolean checkWarlords() {
		for (int i=0; i<playerList.size(); i++) {
			Warlord player = playerList.get(i);
			//if (!player.isDead()) {
				int playerX, playerY;
				for (int j=0; j<Warlord.length; j++) { // column by column
					for (int k=0; k<Warlord.height; k++) { // row by row
						if ((k == 0) || (k == Warlord.height - 1) || (j == 0) || (j == Warlord.length - 1)) {
							playerX = player.getXPos() + j;
							playerY = player.getYPos() + k;
							if (coordInBallPath(playerX, playerY)) { 
								if ((k == 0) && (ball.getYVelocity() > 0)) { // bottom side, reverse y
									ball.setYPos(player.getYPos() - (ball.getYPos() + ball.getYVelocity() + Ball.height - player.getYPos()));
									ball.setYVelocity(ball.getYVelocity() * -1);
									ball.setXPos(ball.getXPos() + ball.getXVelocity()); 
								} else if ((k == Warlord.height - 1) && (ball.getYVelocity() < 0)) { // top side, reverse y
									ball.setYPos(player.getYPos() + Warlord.height - (ball.getYPos() + ball.getYVelocity() - Warlord.height - player.getYPos()));
									ball.setYVelocity(ball.getYVelocity() * -1);
									ball.setXPos(ball.getXPos() + ball.getXVelocity());
								} else if ((j == Warlord.length - 1) && (ball.getXVelocity() < 0)) { // right side, reverse x
									ball.setXPos(player.getXPos() + Warlord.length - (ball.getXPos() + ball.getXVelocity() - Warlord.length - player.getXPos()));
									ball.setXVelocity(ball.getXVelocity() * -1);
									ball.setYPos(ball.getYPos() + ball.getYVelocity());
								} else if ((j == 0) && (ball.getXVelocity() > 0)){ // left side, reverse x
									ball.setXPos(player.getXPos() - (ball.getXPos() + ball.getXVelocity() + Ball.length - player.getXPos()));
									ball.setXVelocity(ball.getXVelocity() * -1);
									ball.setYPos(ball.getYPos() + ball.getYVelocity());
								} else {
									System.out.println("Unaccounted warlord hit at: " + j + ", " + k);
									System.out.println("\t Ball velocity: " + ball.getXVelocity() + ", " + ball.getYVelocity());
								}
								player.dies();
								return true;
							} 
						}							
					}
				}
			//}
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
					x1++;
				} 
				double deltaError = Math.abs(deltaY / deltaX);
				double error = deltaError - 0.5;
				int yTemp = y0;
				if (deltaX > 0) {
					for (int i=x0; i<x1; i++) {
						int[] tempArray = {i, yTemp};
						coordsList.add(tempArray);
						error = error + deltaError;
						if (error >= 0.5) {
							yTemp++;
							error = error - 1.0;
						}	
					}
				} else {
					for (int i=x1; i<x0; i++) {
						int[] tempArray = {i, yTemp};
						coordsList.add(tempArray);
						error = error + deltaError;
						if (error >= 0.5) {
							yTemp++;
							error = error - 1.0;
						}	
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
	
	public Ball getBall() {
		return ball;
	}
	
	public ArrayList<Warlord> getPlayerList() {
		return playerList;
	}
	
	public ArrayList<Wall> getWallList() {
		return wallList;
	}

}
