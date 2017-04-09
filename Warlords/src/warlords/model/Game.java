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
	private boolean[] keyList = {false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false }; // Key up or down list
	private static int xBound; // x Boundary
	private static int yBound; // y Boundary
	private boolean finished;
	private float timeRemaining;
	private int ticksSinceLastHit;
	private float paddleSpeed = (float) 0.7;
	private GameViewController gameViewController;
	// Sounds
	private AudioClip paddleBounce = new AudioClip(new File("sounds/paddleBounce.wav").toURI().toString());
	private AudioClip wallExplosion = new AudioClip(new File("sounds/explosion.wav").toURI().toString());
	private AudioClip playerDeath = new AudioClip(new File("sounds/playerDeath.wav").toURI().toString());
	private AudioClip boundaryBounce = new AudioClip(new File("sounds/boundaryBounce.wav").toURI().toString());
	private AudioClip sheepBounce = new AudioClip(new File("sounds/sheep.wav").toURI().toString());

	// Constructor
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
		timeRemaining -= 0.01; // Game ticks every 10ms
		
		movePaddles(); // Set new paddle coordinates based on which keys are currently pressed
		computerMovePaddles(ballList.get(0)); // AI set their new paddle coordinates
		sheepMove(); // Sheep randomly move
		
		int hitNum = checkHits(); // Check for paddle, warlord, boundary, and wall collisions
		if (hitNum > 0) { // If the ball is not the main ball (i.e a USA bullet)
			gameViewController.delBall(ballList.get(hitNum)); 
			ballList.remove(hitNum); // Remove the ball on collision
		}
		
		countImmune(); // Count the 5s for the Britain ability
		checkWin(); // Check if a player has won
	}
	
	// Checking for collisions
	public int checkHits() {
		for (int i=0; i<ballList.size(); i++) {
			Ball ball = ballList.get(i);
			if (ticksSinceLastHit > 5) { // Wait at least 5 ticks after a collision before another collision can occur
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
					ticksSinceLastHit = 0; // Sound moved to checkWarlords function
					return i;
				}
				
				// Check if the ball is going to hit a sheep
				if (checkSheep(ball)) {
					sheepBounce.play();
					ticksSinceLastHit = 0;
					return i;
				}
			} else {
				ticksSinceLastHit++;
			}
			
			// Check if the ball is going to hit a boundary
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
		if (ball.getXPos() + ball.getXVelocity() >= xBound) { // If we are going to go past the right side boundary
			ball.setXPos(xBound - (ball.getXPos() + ball.getXVelocity() - xBound));
			reverseVelocity(ball, 0);
			xHit = true;
		} else if (ball.getXPos() + ball.getXVelocity() <= 0) {	 // If we are going to go past the left side boundary
			ball.setXPos((ball.getXPos() + ball.getXVelocity()) * -1);
			reverseVelocity(ball, 0);
			xHit = true;
		} else {	
			ball.setXPos(ball.getXPos() + ball.getXVelocity()); // Continue normally
		}
					
		// Y boundaries
		if (ball.getYPos() + ball.getYVelocity() >= yBound) { // If we are going to go past the top boundary
			ball.setYPos(yBound - (ball.getYPos() + ball.getYVelocity() - yBound));
			reverseVelocity(ball, 1);
			yHit = true;
		} else if (ball.getYPos() + ball.getYVelocity() <= 0) {	// If we are going to go past the bottom boundary
			ball.setYPos((ball.getYPos() + ball.getYVelocity()) * -1);
			reverseVelocity(ball, 1);
			yHit = true;
		} else { 
			ball.setYPos(ball.getYPos() + ball.getYVelocity());	// Continue normally
		}
		return (xHit || yHit);
	}
	

	// Check for paddles, if we hit one then rebound 
	private boolean checkPaddles(Ball ball) {
		for (int i=0; i<playerList.size(); i++) {			
			if (!playerList.get(i).isDead()) { // If the player isn't dead
				Paddle paddle = playerList.get(i).getPaddle();
				if (checkPaddle(ball, paddle, Paddle.height, Paddle.length)) {
					return true;
				}
			}
		}
		return false;
	}

	// Check for sheep, if we hit one then rebound 
	private boolean checkSheep(Ball ball) {
		for (int i=0; i<playerList.size(); i++) {			
			if ((!playerList.get(i).isDead()) && (playerList.get(i).getClassNum() == 3)) { // If the player isn't dead and is New Zealand
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
		for (int j=0; j<length; j++) { // Column by column of the paddle
			for (int k=0; k<height; k++) { // Row by row of the paddle
				if ((k == 0) || (k == height - 1) || (j == 0) || (j == length - 1)) { // If we are on the edge of the paddle (one of the side pixels)
					paddleX = paddle.getXPos() + j;
					paddleY = paddle.getYPos() + k;
					if (coordInBallPath(ball, paddleX, paddleY)) { // If the ball is going to hit one of the edge coordinates
						if ((k == 0) && (ball.getYVelocity() > 0)) { // Ball is going to hit bottom side, reverse y
							ball.setYPos(paddle.getYPos() - (ball.getYPos() + ball.getYVelocity() + Ball.height - paddle.getYPos()));
							reverseVelocity(ball, 1);
							ball.setXPos(ball.getXPos() + ball.getXVelocity()); 
						} else if ((k == height - 1) && (ball.getYVelocity() < 0)) { // Ball is going to hit top side, reverse y
							ball.setYPos(paddle.getYPos() + height - (ball.getYPos() + ball.getYVelocity() - height - paddle.getYPos()));
							reverseVelocity(ball, 1);
							ball.setXPos(ball.getXPos() + ball.getXVelocity());
						} else if ((j == length - 1) && (ball.getXVelocity() < 0)) { // Ball is going to hit right side, reverse x
							ball.setXPos(paddle.getXPos() + length - (ball.getXPos() + ball.getXVelocity() - length - paddle.getXPos()));
							reverseVelocity(ball, 0);
							ball.setYPos(ball.getYPos() + ball.getYVelocity());
						} else if ((j == 0) && (ball.getXVelocity() > 0)){ // Ball is going to hit left side, reverse x
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
			if (!wall.isDestroyed()) { // If the wall is not already destroyed
				int wallX, wallY;
				for (int j=0; j<Wall.length; j++) { // Column by column of the wall
					for (int k=0; k<Wall.height; k++) { // Row by row of the wall
						if ((k == 0) || (k == Wall.height - 1) || (j == 0) || (j == Wall.length - 1)) { // If we are on the edge of the wall (one of the side pixels)
							wallX = wall.getXPos() + j;
							wallY = wall.getYPos() + k;
							if (coordInBallPath(ball, wallX, wallY)) { 
								if ((k == 0) && (ball.getYVelocity() > 0)) { // Ball is going to hit bottom side, reverse y
									ball.setYPos(wall.getYPos() - (ball.getYPos() + ball.getYVelocity() + Ball.height - wall.getYPos()));
									reverseVelocity(ball, 1);
									ball.setXPos(ball.getXPos() + ball.getXVelocity()); 
								} else if ((k == Wall.height - 1) && (ball.getYVelocity() < 0)) { // Ball is going to hit top side, reverse y
									ball.setYPos(wall.getYPos() + Wall.height - (ball.getYPos() + ball.getYVelocity() - Wall.height - wall.getYPos()));
									reverseVelocity(ball, 1);
									ball.setXPos(ball.getXPos() + ball.getXVelocity());
								} else if ((j == Wall.length - 1) && (ball.getXVelocity() < 0)) { // Ball is going to hit right side, reverse x
									ball.setXPos(wall.getXPos() + Wall.length - (ball.getXPos() + ball.getXVelocity() - Wall.length - wall.getXPos()));
									reverseVelocity(ball, 0);
									ball.setYPos(ball.getYPos() + ball.getYVelocity());
								} else if ((j == 0) && (ball.getXVelocity() > 0)){ // Ball is going to hit left side, reverse x
									ball.setXPos(wall.getXPos() - (ball.getXPos() + ball.getXVelocity() + Ball.length - wall.getXPos()));
									reverseVelocity(ball, 0);
									ball.setYPos(ball.getYPos() + ball.getYVelocity());
								} else {
									
								}
								wall.destroy(); // Destroy the wall on hit
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
			if (!player.isDead()) { // If the player is not dead
				int playerX, playerY;
				for (int j=0; j<Warlord.length; j++) { // Column by column of the warlord
					for (int k=0; k<Warlord.height; k++) { // Row by row of the warlord
						if ((k == 0) || (k == Warlord.height - 1) || (j == 0) || (j == Warlord.length - 1)) {  // If we are on the edge of the warlord (one of the side pixels)
							playerX = player.getXPos() + j;
							playerY = player.getYPos() + k;
							if (coordInBallPath(ball, playerX, playerY)) { 
								if ((k == 0) && (ball.getYVelocity() > 0)) { // Ball is going to hit bottom side, reverse y
									ball.setYPos(player.getYPos() - (ball.getYPos() + ball.getYVelocity() + Ball.height - player.getYPos()));
									reverseVelocity(ball, 1);
									ball.setXPos(ball.getXPos() + ball.getXVelocity()); 
								} else if ((k == Warlord.height - 1) && (ball.getYVelocity() < 0)) { // Ball is going to hit top side, reverse y
									ball.setYPos(player.getYPos() + Warlord.height - (ball.getYPos() + ball.getYVelocity() - Warlord.height - player.getYPos()));
									reverseVelocity(ball, 1);
									ball.setXPos(ball.getXPos() + ball.getXVelocity());
								} else if ((j == Warlord.length - 1) && (ball.getXVelocity() < 0)) { // Ball is going to hit right side, reverse x
									ball.setXPos(player.getXPos() + Warlord.length - (ball.getXPos() + ball.getXVelocity() - Warlord.length - player.getXPos()));
									reverseVelocity(ball, 0);
									ball.setYPos(ball.getYPos() + ball.getYVelocity());
								} else if ((j == 0) && (ball.getXVelocity() > 0)){ // Ball is going to hit left side, reverse x
									ball.setXPos(player.getXPos() - (ball.getXPos() + ball.getXVelocity() + Ball.length - player.getXPos()));
									reverseVelocity(ball, 0);
									ball.setYPos(ball.getYPos() + ball.getYVelocity());
								} else {
		
								}
								if (player.getImmune() >= 0) { // If the player is Britain and immune, dont die
									
								} else { 
									player.dies(); // Player dies
									playerDeath.play();
								}
								return true;
							} 
						}							
					}
				}
			}
		}
		return false;
	}
	
	// On bounce, randomly increase or decrease velocity by 1, or leave the same. Helps to reduce AI getting stuck in loops, adds to game
	public void reverseVelocity(Ball ball, int dir) {
		int newValue;
		int randomX = 1 - (int) (Math.random() * (3)); // Random number between -1 and 1
		int randomY = 1 - (int) (Math.random() * (3));
		if (dir == 0) { // Reverse on X
			newValue = (ball.getXVelocity() * -1) + randomX;
			if ((Math.abs(newValue) < 3) || (Math.abs(newValue) > 6)) { // Ensure velocity will still be between 3 and 6 and -3 and -6
				ball.setXVelocity(ball.getXVelocity() * -1);
			} else {
				ball.setXVelocity(newValue);
			}
		} else { // Reverse on Y
			newValue = (ball.getYVelocity() * -1) + randomY;
			if ((Math.abs(newValue) < 3) || (Math.abs(newValue) > 6)) { // Ensure velocity will still be between 3 and 6 and -3 and -6
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
				playerList.get(wallList.get(i).getOwner()).addWall(); // Count up walls remaining
			}
			for (int i=0; i<playerList.size(); i++) {
				if (playerList.get(i).getWallsLeft() > playerList.get(winnerPos).getWallsLeft()) { // See who has the most walls remaining
					winnerPos = i;
				}
			}
			for (int i=0; i<playerList.size(); i++) {
				if (i != winnerPos) {
					if (playerList.get(i).getWallsLeft() == playerList.get(winnerPos).getWallsLeft()) { // If its a tie
						return;
					}
				}
			}
			playerList.get(winnerPos).wins(); // If no tie then a player wins	
			return;
		}
		// Check if there is only one player left alive
		int playersAlive = playerList.size();
		for (int i=0; i<playerList.size(); i++) {
			if (playerList.get(i).isDead()) { // Count players still alive
				playersAlive--;
			} else {
				winnerPos = i;
			}
		}
		if (playersAlive < 2) { // If there is only one left then they win
			playerList.get(winnerPos).wins();
			finished = true;
		}
		return;
	}

	// Check if a coordinate on the ball will hit a coordinate on another object. Used for collision detection
	private boolean coordInBallPath(Ball ball, int xCoord, int yCoord) {
		int x0, x1, y0, y1;
		for (int z=0; z<Ball.length; z=z+4) {
			for (int y=0; y<Ball.height; y=y+4) { // Check every 4th coordinate on the ball (stops lag)
				x0 = ball.getXPos() + z;
				x1 = ball.getXPos() + ball.getXVelocity() + z;
				y0 = ball.getYPos() + y;
				y1 = ball.getYPos() + ball.getYVelocity() + y;		
				ArrayList<int []> coordsList = new ArrayList<int []>();
				// Bresenham's Algorithm, creates an ArrayList of coordinates on the balls path
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
				// If the coordinates in question are in the balls path
				for (int i=0; i<coordsList.size();i++) {				
					if ((xCoord == coordsList.get(i)[0]) && (yCoord == coordsList.get(i)[1])) {
						return true;
					}
				}
			}
		}
		return false;
	}
	
	// Move the sheeps randomly for New Zealand ability
	// If the sheep is already moving in a certain direction, it will be more likely to continue in that direction than swap directions. Helps it move around more.
	public void sheepMove() {
		for (int i=0; i<playerList.size(); i++) {
			if (playerList.get(i).getSheep() != null) {
				double randNum = Math.random(); // Random number between 0 and 1
				if (playerList.get(i).getSheepGoingLeft()) { // If its already going left
					if (randNum > 0.05) { // Favour continuing left
						curveLeft(playerList.get(i).getSheep(), i, (float) paddleSpeed / 3); // Move the sheep left at a speed of 1/3 of the regular speed
						playerList.get(i).setSheepGoingLeft(true);
					} else {
						curveRight(playerList.get(i).getSheep(), i, (float) paddleSpeed / 3); // Move the sheep right at a speed of 1/3 of the regular speed
						playerList.get(i).setSheepGoingLeft(false);
					}
				} else { // If its already going right
					if (randNum > 0.95) { 
						curveLeft(playerList.get(i).getSheep(), i, (float) paddleSpeed / 3); // Move the sheep left at a speed of 1/3 of the regular speed
						playerList.get(i).setSheepGoingLeft(true);
					} else { // Favour continuing right
						curveRight(playerList.get(i).getSheep(), i, (float) paddleSpeed / 3); // Move the sheep right at a speed of 1/3 of the regular speed
						playerList.get(i).setSheepGoingLeft(false);
					}
				}			
			}	
		}
	}
	
	// If its corresponding key is down, then call the curve function on that paddle
	public void movePaddles() {
		if ((keyList[0] == true) && (!playerList.get(2).isComputerControlled())) { // Player 1 left
			curveLeft(playerList.get(2).getPaddle(), 2, (float) paddleSpeed);
		} 
		if ((keyList[1] == true) && (!playerList.get(2).isComputerControlled())) { // Player 1 right
			curveRight(playerList.get(2).getPaddle(), 2, (float) paddleSpeed);
		}
		if ((keyList[2] == true) && (!playerList.get(2).isComputerControlled())) { // Player 1 ability
			useAbility(2); 
		}
		if ((keyList[4] == true) && (!playerList.get(0).isComputerControlled())) { // Player 2 left
			curveLeft(playerList.get(0).getPaddle(), 0, (float) paddleSpeed);
		} 
		if ((keyList[5] == true) && (!playerList.get(0).isComputerControlled())) { // Player 2 right
			curveRight(playerList.get(0).getPaddle(), 0, (float) paddleSpeed);
		}
		if ((keyList[6] == true) && (!playerList.get(0).isComputerControlled())) { // Player 2 ability
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
			} else if (playerList.get(playerNum).getClassNum() == 2) { // Britain
				if (playerList.get(playerNum).getImmune() == -1) {
					playerList.get(playerNum).setImmune(0);
				}
			}
		}
	}
	
	public void countImmune() {
		for (int i=0;i<playerList.size(); i++) {
			if (playerList.get(i).getClassNum() == 2) {
				if (playerList.get(i).getImmune() > 500) {
					playerList.get(i).setImmune(-2);
				}
				if (playerList.get(i).getImmune() >= 0) {
					playerList.get(i).setImmune(playerList.get(i).getImmune() + 1);
				}
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
