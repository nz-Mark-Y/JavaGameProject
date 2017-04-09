package warlords.model;
import java.io.File;
import java.util.ArrayList;

import javafx.scene.media.AudioClip;
import javafx.scene.transform.Rotate;
import warlords.tests.IGame;
import warlords.view.GameViewController;

public class Game implements IGame {
	private ArrayList<Ball> ballList = new ArrayList<Ball>();
	private ArrayList<Warlord> playerList = new ArrayList<Warlord>();
	private ArrayList<Wall> wallList = new ArrayList<Wall>();
	private boolean[] keyList = {false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false };
	private static int xBound;
	private static int yBound;
	private boolean finished;
	private float timeRemaining;
	private int ticksSinceLastHit;
	private float paddleSpeed = (float) 0.7;
	private GameViewController gameViewController;
	private AudioClip paddleBounce = new AudioClip(new File("sounds/paddleBounce.wav").toURI().toString());
	private AudioClip wallExplosion = new AudioClip(new File("sounds/explosion.wav").toURI().toString());
	private AudioClip playerDeath = new AudioClip(new File("sounds/playerDeath.wav").toURI().toString());
	private AudioClip boundaryBounce = new AudioClip(new File("sounds/boundaryBounce.wav").toURI().toString());

	public Game(Ball ball, int xBound, int yBound, ArrayList<Warlord> playerList, ArrayList<Wall> wallList) {
		Game.xBound = xBound;
		Game.yBound = yBound;
		this.playerList = playerList;
		this.wallList = wallList;
		this.ballList.add(ball);
		finished = false;
		timeRemaining = (float) 120;
		ticksSinceLastHit = 6;
	}

	// Game Logic
	@Override
	public void tick() {	
		timeRemaining -= 0.01;
		
		movePaddles();
		computerMovePaddles(ballList.get(0));
		sheepMove();
		
		int hitNum = checkHits();
		if (hitNum > 0) {
			gameViewController.delBall(ballList.get(hitNum));
			ballList.remove(hitNum);
		}
		checkWin();
	}
	
	public int checkHits() {
		for (int i=0; i<ballList.size(); i++) {
			Ball ball = ballList.get(i);
			if (ticksSinceLastHit > 5) {
				// Check if the ball is going to hit a paddle
				if (checkPaddles(ball)) {
					paddleBounce.play();
					ticksSinceLastHit = 0;
					return i;
				}
	
				// Check if the ball is going to hit a block
				if (checkWalls(ball)) {
					wallExplosion.play();
					ticksSinceLastHit = 0;
					return i;
				}
	
				// Check if the ball is going to hit a warlord
				if (checkWarlords(ball)) {
					playerDeath.play();
					ticksSinceLastHit = 0;
					return i;
				}
				
				// Check if the ball is going to hit a warlord
				if (checkSheep(ball)) {
					paddleBounce.play();
					ticksSinceLastHit = 0;
					return i;
				}
			} else {
				ticksSinceLastHit++;
			}
			
			if (checkBoundaries(ball)) {
				boundaryBounce.play();
				return i;
			}			
		}
		return -1;
	}
	
	// Check for boundaries, if we hit one then rebound
	private boolean checkBoundaries(Ball ball) { 
		boolean yHit = false;
		boolean xHit = false;
		// X boundaries
		if (ball.getXPos() + ball.getXVelocity() >= xBound) {
			ball.setXPos(xBound - (ball.getXPos() + ball.getXVelocity() - xBound));
			reverseVelocity(ball, 0);
			xHit = true;
		} else if (ball.getXPos() + ball.getXVelocity() <= 0) {	
			ball.setXPos((ball.getXPos() + ball.getXVelocity()) * -1);
			reverseVelocity(ball, 0);
			xHit = true;
		} else {	
			ball.setXPos(ball.getXPos() + ball.getXVelocity());
		}
					
		// Y boundaries
		if (ball.getYPos() + ball.getYVelocity() >= yBound) {
			ball.setYPos(yBound - (ball.getYPos() + ball.getYVelocity() - yBound));
			reverseVelocity(ball, 1);
			yHit = true;
		} else if (ball.getYPos() + ball.getYVelocity() <= 0) {	
			ball.setYPos((ball.getYPos() + ball.getYVelocity()) * -1);
			reverseVelocity(ball, 1);
			yHit = true;
		} else {
			ball.setYPos(ball.getYPos() + ball.getYVelocity());		
		}
		return (xHit || yHit);
	}
	

	// Check for paddles, if we hit one then rebound 
	private boolean checkPaddles(Ball ball) {
		for (int i=0; i<playerList.size(); i++) {			
			if (!playerList.get(i).isDead()) {
				Paddle paddle = playerList.get(i).getPaddle();
				if (checkPaddle(ball, paddle, Paddle.height, Paddle.length)) {
					return true;
				}
			}
		}
		return false;
	}

	// Check for paddles, if we hit one then rebound 
	private boolean checkSheep(Ball ball) {
		for (int i=0; i<playerList.size(); i++) {			
			if ((!playerList.get(i).isDead()) && (playerList.get(i).getClassNum() == 3)) {
				Paddle paddle = playerList.get(i).getSheep();
				if (checkPaddle(ball, paddle, 20, 20)) {
					return true;
				}
			}
		}
		return false;
	}
	
	// Helper function for checking paddles
	private boolean checkPaddle(Ball ball, Paddle paddle, int height, int length) {
		int paddleX, paddleY;
		for (int j=0; j<length; j++) { // column by column
			for (int k=0; k<height; k++) { // row by row
				if ((k == 0) || (k == height - 1) || (j == 0) || (j == length - 1)) {
					paddleX = paddle.getXPos() + j;
					paddleY = paddle.getYPos() + k;
					if (coordInBallPath(ball, paddleX, paddleY)) { 
						if ((k == 0) && (ball.getYVelocity() > 0)) { // bottom side, reverse y
							ball.setYPos(paddle.getYPos() - (ball.getYPos() + ball.getYVelocity() + Ball.height - paddle.getYPos()));
							reverseVelocity(ball, 1);
							ball.setXPos(ball.getXPos() + ball.getXVelocity()); 
						} else if ((k == height - 1) && (ball.getYVelocity() < 0)) { // top side, reverse y
							ball.setYPos(paddle.getYPos() + height - (ball.getYPos() + ball.getYVelocity() - height - paddle.getYPos()));
							reverseVelocity(ball, 1);
							ball.setXPos(ball.getXPos() + ball.getXVelocity());
						} else if ((j == length - 1) && (ball.getXVelocity() < 0)) { // right side, reverse x
							ball.setXPos(paddle.getXPos() + length - (ball.getXPos() + ball.getXVelocity() - length - paddle.getXPos()));
							reverseVelocity(ball, 0);
							ball.setYPos(ball.getYPos() + ball.getYVelocity());
						} else if ((j == 0) && (ball.getXVelocity() > 0)){ // left side, reverse x
							ball.setXPos(paddle.getXPos() - (ball.getXPos() + ball.getXVelocity() + Ball.length - paddle.getXPos()));
							reverseVelocity(ball, 0);
							ball.setYPos(ball.getYPos() + ball.getYVelocity());
						} else {
						
						}
						return true;
					} 
				}							
			}
		}
		return false;
	}
	
	// Check for walls, if we hit one then rebound and destroy the wall
	private boolean checkWalls(Ball ball) {
		for (int i=0; i<wallList.size(); i++) {
			Wall wall = wallList.get(i);
			if (!wall.isDestroyed()) {
				int wallX, wallY;
				for (int j=0; j<Wall.length; j++) { // column by column
					for (int k=0; k<Wall.height; k++) { // row by row
						if ((k == 0) || (k == Wall.height - 1) || (j == 0) || (j == Wall.length - 1)) {
							wallX = wall.getXPos() + j;
							wallY = wall.getYPos() + k;
							if (coordInBallPath(ball, wallX, wallY)) { 
								if ((k == 0) && (ball.getYVelocity() > 0)) { // bottom side, reverse y
									ball.setYPos(wall.getYPos() - (ball.getYPos() + ball.getYVelocity() + Ball.height - wall.getYPos()));
									reverseVelocity(ball, 1);
									ball.setXPos(ball.getXPos() + ball.getXVelocity()); 
								} else if ((k == Wall.height - 1) && (ball.getYVelocity() < 0)) { // top side, reverse y
									ball.setYPos(wall.getYPos() + Wall.height - (ball.getYPos() + ball.getYVelocity() - Wall.height - wall.getYPos()));
									reverseVelocity(ball, 1);
									ball.setXPos(ball.getXPos() + ball.getXVelocity());
								} else if ((j == Wall.length - 1) && (ball.getXVelocity() < 0)) { // right side, reverse x
									ball.setXPos(wall.getXPos() + Wall.length - (ball.getXPos() + ball.getXVelocity() - Wall.length - wall.getXPos()));
									reverseVelocity(ball, 0);
									ball.setYPos(ball.getYPos() + ball.getYVelocity());
								} else if ((j == 0) && (ball.getXVelocity() > 0)){ // left side, reverse x
									ball.setXPos(wall.getXPos() - (ball.getXPos() + ball.getXVelocity() + Ball.length - wall.getXPos()));
									reverseVelocity(ball, 0);
									ball.setYPos(ball.getYPos() + ball.getYVelocity());
								} else {
									
								}
								wall.destroy();
								return true;
							} 
						}							
					}
				}
			}
		}
		return false;
	}

	// Check for warlords, if we hit one then rebound and destroy the warlord
	private boolean checkWarlords(Ball ball) {
		for (int i=0; i<playerList.size(); i++) {
			Warlord player = playerList.get(i);
			if (!player.isDead()) {
				int playerX, playerY;
				for (int j=0; j<Warlord.length; j++) { // column by column
					for (int k=0; k<Warlord.height; k++) { // row by row
						if ((k == 0) || (k == Warlord.height - 1) || (j == 0) || (j == Warlord.length - 1)) {
							playerX = player.getXPos() + j;
							playerY = player.getYPos() + k;
							if (coordInBallPath(ball, playerX, playerY)) { 
								if ((k == 0) && (ball.getYVelocity() > 0)) { // bottom side, reverse y
									ball.setYPos(player.getYPos() - (ball.getYPos() + ball.getYVelocity() + Ball.height - player.getYPos()));
									reverseVelocity(ball, 1);
									ball.setXPos(ball.getXPos() + ball.getXVelocity()); 
								} else if ((k == Warlord.height - 1) && (ball.getYVelocity() < 0)) { // top side, reverse y
									ball.setYPos(player.getYPos() + Warlord.height - (ball.getYPos() + ball.getYVelocity() - Warlord.height - player.getYPos()));
									reverseVelocity(ball, 1);
									ball.setXPos(ball.getXPos() + ball.getXVelocity());
								} else if ((j == Warlord.length - 1) && (ball.getXVelocity() < 0)) { // right side, reverse x
									ball.setXPos(player.getXPos() + Warlord.length - (ball.getXPos() + ball.getXVelocity() - Warlord.length - player.getXPos()));
									reverseVelocity(ball, 0);
									ball.setYPos(ball.getYPos() + ball.getYVelocity());
								} else if ((j == 0) && (ball.getXVelocity() > 0)){ // left side, reverse x
									ball.setXPos(player.getXPos() - (ball.getXPos() + ball.getXVelocity() + Ball.length - player.getXPos()));
									reverseVelocity(ball, 0);
									ball.setYPos(ball.getYPos() + ball.getYVelocity());
								} else {
		
								}
								player.dies();
								return true;
							} 
						}							
					}
				}
			}
		}
		return false;
	}
	
	// On bounce, randomly increase or decrease velocity by 1, or leave the same
	public void reverseVelocity(Ball ball, int dir) {
		int newValue;
		int randomX = 1 - (int) (Math.random() * (3));
		int randomY = 1 - (int) (Math.random() * (3));
		if (dir == 0) {
			newValue = (ball.getXVelocity() * -1) + randomX;
			if ((Math.abs(newValue) < 3) || (Math.abs(newValue) > 6)) {
				ball.setXVelocity(ball.getXVelocity() * -1);
			} else {
				ball.setXVelocity(newValue);
			}
		} else {
			newValue = (ball.getYVelocity() * -1) + randomY;
			if ((Math.abs(newValue) < 3) || (Math.abs(newValue) > 6)) {
				ball.setYVelocity(ball.getYVelocity() * -1);
			} else {
				ball.setYVelocity(newValue);
			}
		}
	}
	
	// Checks win conditions
	private void checkWin() {
		int winnerPos = 0;
		// Check timeout
		if (timeRemaining <= 0) {
			finished = true;
			for (int i=0; i<wallList.size(); i++) {
				playerList.get(wallList.get(i).getOwner()).addWall();
			}
			for (int i=0; i<playerList.size(); i++) {
				if (playerList.get(i).getWallsLeft() > playerList.get(winnerPos).getWallsLeft()) {
					winnerPos = i;
				}
			}
			for (int i=0; i<playerList.size(); i++) {
				if (i != winnerPos) {
					if (playerList.get(i).getWallsLeft() == playerList.get(winnerPos).getWallsLeft()) {
						return;
					}
				}
			}
			playerList.get(winnerPos).wins();			
			return;
		}
		// Check if there is only one player left alive
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
	private boolean coordInBallPath(Ball ball, int xCoord, int yCoord) {
		int x0, x1, y0, y1;
		for (int z=0; z<Ball.length; z=z+4) {
			for (int y=0; y<Ball.height; y=y+4) {
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
	
	// Move the sheeps randomly
	public void sheepMove() {
		for (int i=0; i<playerList.size(); i++) {
			if (playerList.get(i).getSheep() != null) {
				double randNum = Math.random();
				if (playerList.get(i).getSheepGoingLeft()) {
					if (randNum > 0.05) {
						curveLeft(playerList.get(i).getSheep(), i, (float) paddleSpeed / 3);
						playerList.get(i).setSheepGoingLeft(true);
					} else {
						curveRight(playerList.get(i).getSheep(), i, (float) paddleSpeed / 3);
						playerList.get(i).setSheepGoingLeft(false);
					}
				} else {
					if (randNum > 0.95) {
						curveLeft(playerList.get(i).getSheep(), i, (float) paddleSpeed / 3);
						playerList.get(i).setSheepGoingLeft(true);
					} else {
						curveRight(playerList.get(i).getSheep(), i, (float) paddleSpeed / 3);
						playerList.get(i).setSheepGoingLeft(false);
					}
				}			
			}	
		}
	}
	
	// If the corresponding key is down, then call the curve function on that paddle
	public void movePaddles() {
		if ((keyList[0] == true) && (!playerList.get(2).isComputerControlled())) {
			curveLeft(playerList.get(2).getPaddle(), 2, (float) paddleSpeed);
		} 
		if ((keyList[1] == true) && (!playerList.get(2).isComputerControlled())) {
			curveRight(playerList.get(2).getPaddle(), 2, (float) paddleSpeed);
		}
		if ((keyList[2] == true) && (!playerList.get(2).isComputerControlled())) {
			useAbility(2); 
		}
		if ((keyList[4] == true) && (!playerList.get(0).isComputerControlled())) {
			curveLeft(playerList.get(0).getPaddle(), 0, (float) paddleSpeed);
		} 
		if ((keyList[5] == true) && (!playerList.get(0).isComputerControlled())) {
			curveRight(playerList.get(0).getPaddle(), 0, (float) paddleSpeed);
		}
		if ((keyList[6] == true) && (!playerList.get(0).isComputerControlled())) {
			useAbility(0);
		}
		if ((keyList[8] == true) && (!playerList.get(1).isComputerControlled())) {
			curveLeft(playerList.get(1).getPaddle(), 1, (float) paddleSpeed);
		} 
		if ((keyList[9] == true) && (!playerList.get(1).isComputerControlled())) {
			curveRight(playerList.get(1).getPaddle(), 1, (float) paddleSpeed);
		}
		if ((keyList[10] == true) && (!playerList.get(1).isComputerControlled())) {
			useAbility(1);
		}
		if ((keyList[12] == true) && (!playerList.get(3).isComputerControlled())) {
			curveLeft(playerList.get(3).getPaddle(), 3, (float) paddleSpeed);
		} 
		if ((keyList[13] == true) && (!playerList.get(3).isComputerControlled())) {
			curveRight(playerList.get(3).getPaddle(), 3, (float) paddleSpeed); 
		}
		if ((keyList[14] == true) && (!playerList.get(3).isComputerControlled())) {
			useAbility(3);
		}
	}
	
	// Move the paddle along its path to the left
	public void curveLeft(Paddle paddle, int playerNum, float speed) {
		if (playerNum == 1) {
			if (paddle.getXPos() > 0) {
				paddle.decrTheta(speed);
				paddle.setXPos((int) (256 * Math.sin(paddle.getTheta() * Math.PI / 180))); 
				paddle.setYPos((int) (256 * Math.cos(paddle.getTheta() * Math.PI / 180)));
			}
		} else if (playerNum == 2) {
			if (paddle.getYPos() > 0) {
				paddle.incrTheta(speed);
				paddle.setXPos(768 - Paddle.length - (int) (256 * Math.sin(paddle.getTheta() * Math.PI / 180))); 
				paddle.setYPos((int) (256 * Math.cos(paddle.getTheta() * Math.PI / 180)));
			}
		} else if (playerNum == 0) {
			if (paddle.getXPos() > 0) {
				paddle.decrTheta(speed);
				paddle.setXPos((int) (256 * Math.sin(paddle.getTheta() * Math.PI / 180))); 
				paddle.setYPos(768 - Paddle.height - (int) ( 256 * Math.cos(paddle.getTheta() * Math.PI / 180)));
			}
		} else if (playerNum == 3) {	
			if (paddle.getYPos() < 768 - Paddle.length) {
				paddle.incrTheta(speed);
				paddle.setXPos(768 - Paddle.length - (int) (256 * Math.sin(paddle.getTheta() * Math.PI / 180))); 
				paddle.setYPos(768 - Paddle.height - (int) (256 * Math.cos(paddle.getTheta() * Math.PI / 180)));
			} 
		}
		if (!finished) {
			paddle.rotatePaddle(playerNum);
		}
	}
	
	// Move the paddle along its path to the right
	public void curveRight(Paddle paddle, int playerNum, float speed) {
		if (playerNum == 1) {
			if (paddle.getYPos() > 2) {
				paddle.incrTheta(speed);
				paddle.setXPos((int) (256 * Math.sin(paddle.getTheta() * Math.PI / 180))); 
				paddle.setYPos((int) (256 * Math.cos(paddle.getTheta() * Math.PI / 180)));
			}
		} else if (playerNum == 2) {
			if (paddle.getXPos() < 768 - Paddle.length) {
				paddle.decrTheta(speed);
				paddle.setXPos(768 - Paddle.length - (int) (256 * Math.sin(paddle.getTheta() * Math.PI / 180))); 
				paddle.setYPos((int) (256 * Math.cos(paddle.getTheta() * Math.PI / 180)));
			}
		} else if (playerNum == 0) {
			if (paddle.getYPos() < 768 - Paddle.length) {
				paddle.incrTheta(speed);
				paddle.setXPos((int) (256 * Math.sin(paddle.getTheta() * Math.PI / 180))); 
				paddle.setYPos(768 - Paddle.height - (int) (256 * Math.cos(paddle.getTheta() * Math.PI / 180)));
			}
		} else if (playerNum == 3) {
			if (paddle.getXPos() < 768 - Paddle.length) {
				paddle.decrTheta(speed);
				paddle.setXPos(768 - Paddle.length - (int) (256 * Math.sin(paddle.getTheta() * Math.PI / 180))); 
				paddle.setYPos(768 - Paddle.height - (int) (256 * Math.cos(paddle.getTheta() * Math.PI / 180)));
			}
		}
		if (!finished) {
			paddle.rotatePaddle(playerNum);
		}
	}
	
	public void useAbility(int playerNum) {
		if (!playerList.get(playerNum).isDead()) {
			if (playerList.get(playerNum).getClassNum() == 0) { // France
				
			} else if (playerList.get(playerNum).getClassNum() == 1) { // USA
				if (playerList.get(playerNum).getLastAbility() == 0) {
					Ball bullet;
					if (playerNum == 0) {
						bullet = new Ball(playerList.get(0).getPaddle().getXPos() + Paddle.length + Ball.length + Ball.length / 2, playerList.get(0).getPaddle().getYPos() - Ball.height - Ball.height / 2, true);
						bullet.setXVelocity((int) (playerList.get(0).getPaddle().getXPos() / 50));
						bullet.setYVelocity((int) -((yBound - playerList.get(1).getPaddle().getYPos()) / 50));
					} else if (playerNum == 1) {
						bullet = new Ball(playerList.get(1).getPaddle().getXPos() + Paddle.length + Ball.length + Ball.length / 2, playerList.get(1).getPaddle().getYPos() + Paddle.height + Ball.height + Ball.height / 2, true);
						bullet.setXVelocity((int) (playerList.get(1).getPaddle().getXPos() / 50));
						bullet.setYVelocity((int) (playerList.get(1).getPaddle().getYPos() / 50));
					} else if (playerNum == 2) {
						bullet = new Ball(playerList.get(2).getPaddle(). getXPos() - Ball.length - Ball.length / 2, playerList.get(2).getPaddle().getYPos() + Paddle.height + Ball.height + Ball.height / 2, true);
						bullet.setXVelocity((int) -((xBound - playerList.get(2).getPaddle().getXPos()) / 50));
						bullet.setYVelocity((int) (playerList.get(2).getPaddle().getYPos() / 50));
					} else {
						bullet = new Ball(playerList.get(3).getPaddle().getXPos() - Ball.length - Ball.length / 2, playerList.get(3).getPaddle().getYPos() - Ball.height - Ball.height / 2, true);
						bullet.setXVelocity((int) -((xBound - playerList.get(2).getPaddle().getXPos()) / 50));
						bullet.setYVelocity((int) -((yBound - playerList.get(1).getPaddle().getYPos()) / 50));
					}
					ballList.add(bullet);
					gameViewController.createBulletView(bullet);
					playerList.get(playerNum).setLastAbility((int) timeRemaining);
				}
			} else {
				
			}
		}
	}
	
	public void computerMovePaddles(Ball ball) {
		for (int i=0; i<playerList.size(); i++) {
			if (playerList.get(i).isComputerControlled()) {
				if (Math.abs(ball.getXPos() - playerList.get(i).getPaddle().getXPos()) > Math.abs(ball.getYPos() - playerList.get(i).getPaddle().getYPos())) {
					if ((ball.getXPos() + ball.getXVelocity()) > (playerList.get(i).getPaddle().getXPos() + Paddle.length / 2)) {
						curveRight(playerList.get(i).getPaddle(), i, (float) paddleSpeed);
					} else {
						curveLeft(playerList.get(i).getPaddle(), i, (float) paddleSpeed);
					}
				} else {
					if ((ball.getYPos() + ball.getYVelocity()) > (playerList.get(i).getPaddle().getYPos() + Paddle.height / 2)) {
						if ((i==0) || (i==2)) {
							curveRight(playerList.get(i).getPaddle(), i, (float) paddleSpeed);
						} else {
							curveLeft(playerList.get(i).getPaddle(), i, (float) paddleSpeed);
						}		
					} else {
						if ((i==0) || (i==2)) {
							curveLeft(playerList.get(i).getPaddle(), i, (float) paddleSpeed);				
						} else {
							curveRight(playerList.get(i).getPaddle(), i, (float) paddleSpeed);
						}
					}
				}
			}
		}
	}
	
	public void setGameViewController(GameViewController gameViewController) {
		this.gameViewController = gameViewController;
	}
	
	@Override
	public boolean isFinished() {
		return finished;
	}
	
	public void finish() {
		finished = true;
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
		return ballList.get(0);
	}
	
	public ArrayList<Ball> getBallList() {
		return ballList;
	}

	public ArrayList<Warlord> getPlayerList() {
		return playerList;
	}

	public ArrayList<Wall> getWallList() {
		return wallList;
	}
	
	public int getWinner() {
		for (int i=0; i<playerList.size(); i++) {
			if (playerList.get(i).hasWon())
				return (i+1);
		}
		return -1;
	}
}
