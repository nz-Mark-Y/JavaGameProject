package warlords.model;
import java.util.ArrayList;

import warlords.tests.IGame;

public class Game implements IGame {
	private Ball ball;
	private ArrayList<Warlord> playerList = new ArrayList<Warlord>();
	private ArrayList<Wall> wallList = new ArrayList<Wall>();
	private boolean[] keyList = {false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false };
	private static int xBound;
	private static int yBound;
	private boolean finished;
	private float timeRemaining;
	private int ticksSinceLastHit;
	private float paddleSpeed = (float) 0.7;

	public Game(Ball ball, int xBound, int yBound, ArrayList<Warlord> playerList, ArrayList<Wall> wallList) {
		this.ball = ball;
		Game.xBound = xBound;
		Game.yBound = yBound;
		this.playerList = playerList;
		this.wallList = wallList;
		finished = false;
		timeRemaining = (float) 120;
		ticksSinceLastHit = 11;
	}

	@Override
	public void tick() {	
		timeRemaining -= 0.02;
		
		movePaddles();
		
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

	public void movePaddles() {
		if (keyList[0] == true) {
			curveLeft(playerList.get(2).getPaddle(), 2);
		} 
		if (keyList[1] == true) {
			curveRight(playerList.get(2).getPaddle(), 2);
		}
		if (keyList[4] == true) {
			curveLeft(playerList.get(0).getPaddle(), 0);
		} 
		if (keyList[5] == true) {
			curveRight(playerList.get(0).getPaddle(), 0);
		}
		if (keyList[8] == true) {
			curveLeft(playerList.get(1).getPaddle(), 1);
		} 
		if (keyList[9] == true) {
			curveRight(playerList.get(1).getPaddle(), 1);
		}
		if (keyList[12] == true) {
			curveLeft(playerList.get(3).getPaddle(), 3);
		} 
		if (keyList[13] == true) {
			curveRight(playerList.get(3).getPaddle(), 3); 
		}
		if (keyList[16] == true) {
			pause();
		} 
		if (keyList[17] == true) {
			pause();
		}
	}
	
	public void curveLeft(Paddle paddle, int centre) {
		if (centre == 1) {
			if (paddle.getXPos() > 0) {
				paddle.decrTheta((float) paddleSpeed);
				paddle.setXPos((int) (256 * Math.sin(paddle.getTheta() * Math.PI / 180))); 
				paddle.setYPos((int) (256 * Math.cos(paddle.getTheta() * Math.PI / 180)));
			}
		} else if (centre == 2) {
			if (paddle.getYPos() > 0) {
				paddle.incrTheta((float) paddleSpeed);
				paddle.setXPos(768 - Paddle.length - (int) (256 * Math.sin(paddle.getTheta() * Math.PI / 180))); 
				paddle.setYPos((int) (256 * Math.cos(paddle.getTheta() * Math.PI / 180)));
			}
		} else if (centre == 0) {
			if (paddle.getXPos() > 0) {
				paddle.decrTheta((float) paddleSpeed);
				paddle.setXPos((int) (256 * Math.sin(paddle.getTheta() * Math.PI / 180))); 
				paddle.setYPos(768 - Paddle.height - (int) ( 256 * Math.cos(paddle.getTheta() * Math.PI / 180)));
			}
		} else if (centre == 3) {	
			if (paddle.getYPos() < 768 - Paddle.length) {
				paddle.incrTheta((float) paddleSpeed);
				paddle.setXPos(768 - Paddle.length - (int) (256 * Math.sin(paddle.getTheta() * Math.PI / 180))); 
				paddle.setYPos(768 - Paddle.height - (int) (256 * Math.cos(paddle.getTheta() * Math.PI / 180)));
			} 
		}
	}
	
	public void curveRight(Paddle paddle, int centre) {
		if (centre == 1) {
			if (paddle.getYPos() > 2) {
				paddle.incrTheta((float) paddleSpeed);
				paddle.setXPos((int) (256 * Math.sin(paddle.getTheta() * Math.PI / 180))); 
				paddle.setYPos((int) (256 * Math.cos(paddle.getTheta() * Math.PI / 180)));
			}
		} else if (centre == 2) {
			if (paddle.getXPos() < 768 - Paddle.length) {
				paddle.decrTheta((float) paddleSpeed);
				paddle.setXPos(768 - Paddle.length - (int) (256 * Math.sin(paddle.getTheta() * Math.PI / 180))); 
				paddle.setYPos((int) (256 * Math.cos(paddle.getTheta() * Math.PI / 180)));
			}
		} else if (centre == 0) {
			if (paddle.getYPos() < 768 - Paddle.length) {
				paddle.incrTheta((float) paddleSpeed);
				paddle.setXPos((int) (256 * Math.sin(paddle.getTheta() * Math.PI / 180))); 
				paddle.setYPos(768 - Paddle.height - (int) (256 * Math.cos(paddle.getTheta() * Math.PI / 180)));
			}
		} else if (centre == 3) {
			if (paddle.getXPos() < 768 - Paddle.length) {
				paddle.decrTheta((float) paddleSpeed);
				paddle.setXPos(768 - Paddle.length - (int) (256 * Math.sin(paddle.getTheta() * Math.PI / 180))); 
				paddle.setYPos(768 - Paddle.height - (int) (256 * Math.cos(paddle.getTheta() * Math.PI / 180)));
			}
		}
	}
	
	public void pause() {
		System.out.println("Pause pressed");
	}
	
	@Override
	public boolean isFinished() {
		return finished;
	}

	@Override
	public void setTimeRemaining(int seconds) {
		timeRemaining = (float) seconds;
	}

	public int getTimeRemaining() {
		return (int) timeRemaining;
	}

	public void setKeyDown(int pos) {
		keyList[pos] = true;
	}
	
	public void setKeyUp(int pos) {
		keyList[pos] = false;
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
