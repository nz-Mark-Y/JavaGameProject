package warlords.view;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import warlords.WarlordsController;
import warlords.model.Ball;
import warlords.model.Game;
import warlords.model.Paddle;
import warlords.model.Wall;
import warlords.model.Warlord;

public class GameViewController {
	public static int gameSpeed = 12;
	private WarlordsController warlordsController;
	private Game game;
	Scene scene;
	private EventHandler<KeyEvent> handler0;
	private EventHandler<KeyEvent> handler1;
	private ArrayList<ImagePattern> flags = new ArrayList<ImagePattern>();
	private ArrayList<Rectangle> flagRects = new ArrayList<Rectangle>();
	private AudioClip multiplayerTheme = new AudioClip(new File("sounds/multiplayerTheme.wav").toURI().toString());
	private AudioClip countdownWait = new AudioClip(new File("sounds/countdownWait.wav").toURI().toString());
	private AudioClip countdownReady = new AudioClip(new File("sounds/countdownReady.wav").toURI().toString());
	private AudioClip postGameTheme = new AudioClip(new File("sounds/postGameTheme.wav").toURI().toString());
	private AudioClip powerupBuff = new AudioClip(new File("sounds/powerupBuff.wav").toURI().toString());
	private AudioClip powerupFire = new AudioClip(new File("sounds/powerupFire.wav").toURI().toString());
	private Timer animationTimer;
	private Timer timeLeftTimer;
	private boolean paused;
	private boolean countingIn;
	private int delayer = 0;

	public GameViewController() {

	}

	@FXML
	private void initialise() {

	}

	// Links the WarlordsController to the GameViewController. Acts as a constructor for the GameViewController
	public void setWarlordsController(WarlordsController warlordsController, final Scene scene) {
		this.warlordsController = warlordsController;
		this.game = warlordsController.getGame();
		this.game.setGameViewController(this);
		this.scene = scene;
		paused = true;

		// Initialize the graphics for the objects
		graphicsInit();
		addViewsToModels();

		// Initial ball velocity. X and Y velocities between 3 and 6, and -3 and -6, inclusive
		int ballXVel = 0;
		int ballYVel = 0;
		onTick();
		while ((ballXVel == 0) || (ballXVel == 1) || (ballXVel == -1) || (ballXVel == 2) || (ballXVel == -2) || (ballYVel == 0) || (ballYVel == 1) || (ballYVel == -1) || (ballYVel == 2) || (ballYVel == -2)) {
			ballXVel = 6 - (int) (Math.random() * (12));
			ballYVel = 6 - (int) (Math.random() * (12));
		}
		game.getBall().setXVelocity(ballXVel);
		game.getBall().setYVelocity(ballYVel);

		// Fonts for text
		setFonts();

		// Count in the three seconds
		countInTime();

		// Key handers for keys being pressed and released. Sets booleans in the game model class to true or false depending on if the key is currently up or down
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				handler0 = new EventHandler<KeyEvent>() {
					@Override
					public void handle(KeyEvent t) {
						switch(t.getCode()) {
						case LEFT:
							game.setKeyDown(0);
							break;
						case RIGHT:
							game.setKeyDown(1);
							break;
						case UP:
							game.setKeyDown(2);
							break;
						case DOWN:
							game.setKeyDown(3);
							break;
						case A:
							game.setKeyDown(4);
							break;
						case D:
							game.setKeyDown(5);
							break;
						case W:
							game.setKeyDown(6);
							break;
						case S:
							game.setKeyDown(7);
							break;
						case J:
							game.setKeyDown(8);
							break;
						case L:
							game.setKeyDown(9);
							break;
						case I:
							game.setKeyDown(10);
							break;
						case K:
							game.setKeyDown(11);
							break;
						case NUMPAD3:
							game.setKeyDown(12);
							break;
						case NUMPAD9:
							game.setKeyDown(13);
							break;
						case NUMPAD5:
							game.setKeyDown(14);
							break;
						case NUMPAD6:
							game.setKeyDown(15);
							break;
						case P:
							pause();
							break;
						case ESCAPE:
							exit();
							break;
						case PAGE_DOWN:
							game.setTimeRemaining(10);
							break;
						case ENTER:
							if(MainMenuViewController.level <= 8 && MainMenuViewController.isCampaign){
								scene.removeEventHandler(KeyEvent.KEY_PRESSED, handler0);
								
								//Load next part of story
								warlordsController.showStory0View();
							} else {
								exit();
							}
						default:
							break;					
						}
					}
				};    
				handler1 = new EventHandler<KeyEvent>() {
					@Override
					public void handle(KeyEvent t) {
						switch(t.getCode()) {
						case LEFT:
							game.setKeyUp(0);
							break;
						case RIGHT:
							game.setKeyUp(1);
							break;
						case UP:
							game.setKeyUp(2);
							break;
						case DOWN:
							game.setKeyUp(3);
							break;
						case A:
							game.setKeyUp(4);
							break;
						case D:
							game.setKeyUp(5);
							break;
						case W:
							game.setKeyUp(6);
							break;
						case S:
							game.setKeyUp(7);
							break;
						case J:
							game.setKeyUp(8);
							break;
						case L:
							game.setKeyUp(9);
							break;
						case I:
							game.setKeyUp(10);
							break;
						case K:
							game.setKeyUp(11);
							break;
						case NUMPAD3:
							game.setKeyUp(12);
							break;
						case NUMPAD9:
							game.setKeyUp(13);
							break;
						case NUMPAD5:
							game.setKeyUp(14);
							break;
						case NUMPAD6:
							game.setKeyUp(15);
							break;
						default:
							break;					
						}
					}
				};    
				scene.addEventHandler(KeyEvent.KEY_PRESSED, handler0);
				scene.addEventHandler(KeyEvent.KEY_RELEASED, handler1);
			}		
		});     
		thread.start();
	}

	// Count in the three seconds. Set the display to show the count in numbers
	public void countInTime() {
		countingIn = true;
		countIn.setText("3");
		countdownWait.play();
		Timer countInTimer = new Timer();
		countInTimer.scheduleAtFixedRate(new TimerTask () {
			int count = 2;
			@Override
			public void run() {
				if (count > 0) {
					countdownWait.play();
					countIn.setText(Integer.toString(count));
					count--;
				} else if (count == 0){
					countdownReady.play();
					countIn.setText("GO");
					count--;
				} else {	
					countIn.setVisible(false);
					countingIn = false;
					paused = false;
					createTimers(); // Create the animation and timing timers
					countInTimer.cancel(); // End the count in timer
				}
			}
		}, 1000, 1000);
	}

	// Create the animation timer and the timing timer
	private void createTimers() {
		Platform.runLater(new Runnable() { // Setting Z order
			@Override
			public void run() {
				pauseBox.toFront();
				pauseMessage1.toFront();
				pauseMessage2.toFront();
				gameOver1.toFront();
				gameOver2.toFront();
				gameOver3.toFront();
			}
		});

		// Animation timer for ball and paddles to move
		animationTimer = new Timer();
		animationTimer.scheduleAtFixedRate(new TimerTask () {
			@Override
			public void run() {
				try {
					Platform.runLater(new Runnable() {
						@Override
						public void run() {
							if (!game.isFinished()) {
								onTick();
							} else { // If the game is over
								multiplayerTheme.stop();
								if (game.getWinner() == -1) {
									gameOver2.setText("Game is a Tie!");
								} else {
									gameOver2.setText("Player " + Integer.toString(game.getWinner()) + " Wins!");
								}								
								pauseBox.setVisible(true);
								gameOver1.setVisible(true);
								gameOver2.setVisible(true);
								gameOver3.setVisible(true);
								animationTimer.cancel(); // Stop the timer
							}
						}
					});
				} catch (Exception ex){
					System.out.println(ex.getMessage());
				}
			}
		}, gameSpeed, gameSpeed);	// Tick every 10ms

		// Timer to display time remaining
		timeLeftTimer = new Timer();
		timeLeftTimer.scheduleAtFixedRate(new TimerTask () {
			@Override
			public void run() {
				timeLeft.setText(Integer.toString(game.getTimeRemaining())); // Display the time remaining
			}
		}, 200, 200); // Tick every 200ms
	}

	// Do the logic, then move all the objects.
	private void onTick() {
		game.tick(); // Do logic
		for (int i=0; i<game.getBallList().size(); i++) { // Move balls
			game.getBallList().get(i).getBallView().setCenterX(game.getBallList().get(i).getXPos() + Ball.length / 2);
			game.getBallList().get(i).getBallView().setCenterY(-(game.getBallList().get(i).getYPos() + Ball.height / 2));
		}	
		for (int i=0; i<game.getPlayerList().size(); i++) { // Move paddles
			game.getPlayerList().get(i).getWarlordView().setX(game.getPlayerList().get(i).getXPos());
			game.getPlayerList().get(i).getWarlordView().setY(-(game.getPlayerList().get(i).getYPos() + Warlord.height));
			if (game.getPlayerList().get(i).getPaddle().getPyramid()) { // Pyramid paddles adjusted
				game.getPlayerList().get(i).getPaddle().getPaddleView().setY(-(game.getPlayerList().get(i).getPaddle().getYPos() + Paddle.height * 2));
			} else {
				game.getPlayerList().get(i).getPaddle().getPaddleView().setY(-(game.getPlayerList().get(i).getPaddle().getYPos() + Paddle.height));
			}
			game.getPlayerList().get(i).getPaddle().getPaddleView().setX(game.getPlayerList().get(i).getPaddle().getXPos());
			if (game.getPlayerList().get(i).getSheep() != null) { // Move New Zealand sheep
				game.getPlayerList().get(i).getSheep().getPaddleView().setX(game.getPlayerList().get(i).getSheep().getXPos());
				game.getPlayerList().get(i).getSheep().getPaddleView().setY(-(game.getPlayerList().get(i).getSheep().getYPos() + 20));
			}
			if (game.getPlayerList().get(i).getClassNum() == 2) { // Britain immunity
				if (game.getPlayerList().get(i).getImmune() == 1) {
					setImmune(i);
				} 
				if (game.getPlayerList().get(i).getImmune() == -2) {
					removeImmune(i);	
				}
			}
			if ((game.getPlayerList().get(i).getClassNum() == 0) || (game.getPlayerList().get(i).getClassNum() == 1) || (game.getPlayerList().get(i).getClassNum() == 5) || (game.getPlayerList().get(i).getClassNum() == 8)) { // If France, USA, Australia, or Egypt, cooldown visibility
				float diff = game.getPlayerList().get(i).getLastAbility() - game.getTimeRemaining();
				if (diff > 20) {
					diff = 20;
				}
				flagRects.get(i).setOpacity(diff / 20);
			}
		}
		for (int i=0;i<game.getWallList().size(); i++) { // Render walls in their correct places
			game.getWallList().get(i).getWallView().setX(game.getWallList().get(i).getXPos());
			game.getWallList().get(i).getWallView().setY(-(game.getWallList().get(i).getYPos() + Wall.height));
		}

		// Play multiplayer theme
		if(!multiplayerTheme.isPlaying() && !paused){
			multiplayerTheme.play();
		}

		// Play post game music if game is finished but stop it if exited
		if(game.isFinished() && !postGameTheme.isPlaying()){
			postGameTheme.play();
		}
		ballAnimation();
		playerExplosionAnimation();
		wallExplosionAnimation();
	}

	//Loads explosion animation when wall is hit
	private void wallExplosionAnimation() {
		for(int i=0;i<20; i++){
			if(game.getWallList().get(i).isDestroyed() && !game.getWallList().get(i).getWallView().isVisible()){
				game.getWallList().get(i).getWallView().setVisible(true);
				Image explosion = new Image("file:images/explosion.gif");
				ImagePattern explosionPattern = new ImagePattern(explosion);
				switch(i) {
				case 0: 
					player1Wall1.setStrokeWidth(0);
					player1Wall1.setFill(explosionPattern);
					break;
				case 1: 
					player1Wall2.setStrokeWidth(0);
					player1Wall2.setFill(explosionPattern);
					break;
				case 2: 
					player1Wall3.setStrokeWidth(0);
					player1Wall3.setFill(explosionPattern);
					break;
				case 3: 
					player1Wall4.setStrokeWidth(0);
					player1Wall4.setFill(explosionPattern);
					break;
				case 4: 
					player1Wall5.setStrokeWidth(0);
					player1Wall5.setFill(explosionPattern);
					break;
				case 5: 
					player2Wall1.setStrokeWidth(0);
					player2Wall1.setFill(explosionPattern);
					break;
				case 6: 
					player2Wall2.setStrokeWidth(0);
					player2Wall2.setFill(explosionPattern);
					break;
				case 7: 
					player2Wall3.setStrokeWidth(0);
					player2Wall3.setFill(explosionPattern);
					break;
				case 8: 
					player2Wall4.setStrokeWidth(0);
					player2Wall4.setFill(explosionPattern);
					break;
				case 9: 
					player2Wall5.setStrokeWidth(0);
					player2Wall5.setFill(explosionPattern);
					break;
				case 10: 
					player3Wall1.setStrokeWidth(0);
					player3Wall1.setFill(explosionPattern);
					break;
				case 11: 
					player3Wall2.setStrokeWidth(0);
					player3Wall2.setFill(explosionPattern);
					break;
				case 12: 
					player3Wall3.setStrokeWidth(0);
					player3Wall3.setFill(explosionPattern);
					break;
				case 13: 
					player3Wall4.setStrokeWidth(0);
					player3Wall4.setFill(explosionPattern);
					break;
				case 14: 
					player3Wall5.setStrokeWidth(0);
					player3Wall5.setFill(explosionPattern);
					break;
				case 15: 
					player4Wall1.setStrokeWidth(0);
					player4Wall1.setFill(explosionPattern);
					break;
				case 16: 
					player4Wall2.setStrokeWidth(0);
					player4Wall2.setFill(explosionPattern);
					break;
				case 17: 
					player4Wall3.setStrokeWidth(0);
					player4Wall3.setFill(explosionPattern);
					break;
				case 18: 
					player4Wall4.setStrokeWidth(0);
					player4Wall4.setFill(explosionPattern);
					break;
				case 19: 
					player4Wall5.setStrokeWidth(0);
					player4Wall5.setFill(explosionPattern);
					break;
				}
			}
		}
	}

	//Loads explosion animation when warlord is hit
	private void playerExplosionAnimation(){
		for(int i=0; i<game.getPlayerList().size(); i++){
			if(game.getPlayerList().get(i).isDead() && !game.getPlayerList().get(i).getWarlordView().isVisible()){
				game.getPlayerList().get(i).getWarlordView().setVisible(true);
				Image explosion = new Image("file:images/explosion.gif");
				ImagePattern explosionPattern = new ImagePattern(explosion);
				switch(i) {
				case 0: 
					player1Warlord.setStrokeWidth(0);
					player1Warlord.setFill(explosionPattern);
					break;
				case 1: 
					player1Warlord.setStrokeWidth(0);
					player2Warlord.setFill(explosionPattern);
					break;
				case 2: 
					player1Warlord.setStrokeWidth(0);
					player3Warlord.setFill(explosionPattern);
					break;
				case 3: 
					player1Warlord.setStrokeWidth(0);
					player4Warlord.setFill(explosionPattern);
					break;
				}
			}
		}
	}

	// Loop through the ball frames with delay (to be able to see)
	private void ballAnimation() {
		if (delayer == 5) {
			if (ball.getFill() == gameBall1Pattern) {
				ball.setFill(gameBall2Pattern);
				delayer = 0;
			} else if(ball.getFill() == gameBall2Pattern) {
				ball.setFill(gameBall3Pattern);
				delayer = 0;
			} else if(ball.getFill() == gameBall3Pattern) {
				ball.setFill(gameBall4Pattern);
				delayer = 0;
			} else if(ball.getFill() == gameBall4Pattern) {
				ball.setFill(gameBall5Pattern);
				delayer = 0;
			} else if(ball.getFill() == gameBall5Pattern) {
				ball.setFill(gameBall6Pattern);
				delayer = 0;
			} else if(ball.getFill() == gameBall6Pattern) {
				ball.setFill(gameBall1Pattern);
				delayer = 0;
			}
		} else {
			delayer++;
		}
	}

	// If the user hits the pause key
	public void pause() {
		if (countingIn == false) { // Check if we are not counting in to the game
			if (game.isFinished() == false) { // Check if the game is not finished
				if (!paused) { // If not already paused, stop the timers and music, show messages
					multiplayerTheme.stop();
					paused = true;
					animationTimer.cancel();
					timeLeftTimer.cancel();
					pauseMessage1.setVisible(true);
					pauseMessage2.setVisible(true);
					pauseBox.setVisible(true);
				} else { // Unpause, start the timers and music, hide messages
					multiplayerTheme.stop();
					paused = false;
					createTimers();
					pauseMessage1.setVisible(false);
					pauseMessage2.setVisible(false);
					pauseBox.setVisible(false);
				}
			}
		}
	}

	// If the user hits the exit key
	public void exit() {
		if (countingIn == false) {
			multiplayerTheme.stop(); // Stop the music
			postGameTheme.stop();
			animationTimer.cancel(); // Stop the timers
			timeLeftTimer.cancel();
			scene.removeEventHandler(KeyEvent.KEY_PRESSED, handler0); // Remove the key handlers
			scene.removeEventHandler(KeyEvent.KEY_RELEASED, handler1); // Remove the key handlers
			game.finish(); // End the game
			warlordsController.showMainMenu(); // Return to main menu
		}
	}

	// Creating bullets for USA ability
	public void createBulletView(Ball bullet) {
		Circle circle = new Circle();
		circle.setFill(ball.getFill());
		circle.setRadius(8);
		circle.setCenterX(0);
		circle.setCenterY(0);
		circle.setLayoutX(0);
		circle.setLayoutY(768);
		pane.getChildren().add(circle);
		bullet.setBallView(circle);
		powerupFire.play();
	}
	
	// Creating spiders for Australia ability
		public void createSpiderView(Ball spider) {
			Circle circle = new Circle();
			circle.setFill(spiderPattern);
			circle.setRadius(8);
			circle.setCenterX(0);
			circle.setCenterY(0);
			circle.setLayoutX(0);
			circle.setLayoutY(768);
			pane.getChildren().add(circle);
			spider.setBallView(circle);
			powerupFire.play();
		}

	// Removing bullets for USA and Australia ability
	public void delBall(Ball bullet) {
		bullet.getBallView().setVisible(false);
	}

	// Adding extra walls for China ability
	public void addWall(Wall wall, int i) {
		Rectangle rect = new Rectangle(); // Create a rectangle
		rect.setFill(redCratePattern);
		rect.setStroke(Color.BLACK);
		rect.setStrokeWidth(1);
		rect.setHeight(30);
		rect.setWidth(30);
		rect.setLayoutX(0);
		rect.setLayoutY(768);
		wall.setWallView(rect);
		pane.getChildren().add(rect);	
	}

	// Setting the warlord as immune, displaying a shield around it, for Britain ability
	private void setImmune(int i) {
		Rectangle rect = new Rectangle(); // Create a rectang;e
		rect.setFill(flag2Pattern);
		rect.setOpacity(0.5);
		rect.setHeight(60);
		rect.setWidth(60);
		rect.setX(game.getPlayerList().get(i).getWarlordView().getX());
		rect.setY(game.getPlayerList().get(i).getWarlordView().getY());
		rect.setLayoutX(0);
		rect.setLayoutY(768);
		if (i == 0) { // Manually set userData to identify rectangles
			rect.setUserData("0");
		}
		if (i == 1) {
			rect.setUserData("1");
		}
		if (i == 2) {
			rect.setUserData("2");
		}
		if (i == 3) {
			rect.setUserData("3");
		}
		pane.getChildren().add(rect); // Show it
		rect.setVisible(true);
		powerupBuff.play();
	}

	// Removing the shield and immunity from a warlord for Britain ability
	private void removeImmune(int i) {
		for (int j=0; j<pane.getChildren().size(); j++) {
			if (i == 0) {
				if (pane.getChildren().get(j).getUserData() == "0") { // Manually look for the right rectangle and hide it
					pane.getChildren().get(j).setVisible(false);
				}
			}
			if (i == 1) {
				if (pane.getChildren().get(j).getUserData() == "1") {
					pane.getChildren().get(j).setVisible(false);
				}
			}
			if (i == 2) {
				if (pane.getChildren().get(j).getUserData() == "2") {
					pane.getChildren().get(j).setVisible(false);
				}
			}
			if (i == 3) {
				if (pane.getChildren().get(j).getUserData() == "3") {
					pane.getChildren().get(j).setVisible(false);
				}
			}
		}
	}

	// Sets the fonts for all the text on the screen
	private void setFonts() {
		Font textFont = null;
		Font smallTextFont = null;
		try {
			textFont = Font.loadFont(new FileInputStream(new File("Fonts/evanescent_p.ttf")), 96);
			smallTextFont = Font.loadFont(new FileInputStream(new File("Fonts/evanescent_p.ttf")), 48);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		timeLeft.setFont(textFont);
		countIn.setFont(textFont);
		pauseMessage1.setFont(textFont);
		pauseMessage2.setFont(smallTextFont);
		gameOver1.setFont(textFont);
		gameOver2.setFont(textFont);
		gameOver3.setFont(smallTextFont);
		pauseMessage1.setVisible(false); // Hides the text so that its not shown until it needs to be
		pauseMessage2.setVisible(false);
		gameOver1.setVisible(false);
		gameOver2.setVisible(false);
		gameOver3.setVisible(false);
		pauseBox.setVisible(false);
	}

	// Links the views created here to the models which the logic is implemented on. Allows for rotation and dynamic showing/hiding
	public void addViewsToModels() {
		game.getBall().setBallView(ball);
		game.getPlayerList().get(0).setWarlordView(player1Warlord); // The warlords
		game.getPlayerList().get(1).setWarlordView(player2Warlord);
		game.getPlayerList().get(2).setWarlordView(player3Warlord);
		game.getPlayerList().get(3).setWarlordView(player4Warlord);

		game.getPlayerList().get(0).getPaddle().setPaddleView(player1Paddle, 0); // The paddles
		game.getPlayerList().get(1).getPaddle().setPaddleView(player2Paddle, 1);
		game.getPlayerList().get(2).getPaddle().setPaddleView(player3Paddle, 2);
		game.getPlayerList().get(3).getPaddle().setPaddleView(player4Paddle, 3);

		if (game.getPlayerList().get(0).getSheep() != null) { // The sheep for New Zealand ability
			game.getPlayerList().get(0).getSheep().setPaddleView(player1Sheep, 0);
		} else {
			player1Sheep.setVisible(false);
		}
		if (game.getPlayerList().get(1).getSheep() != null) {
			game.getPlayerList().get(1).getSheep().setPaddleView(player2Sheep, 1);
		} else {
			player2Sheep.setVisible(false);
		}
		if (game.getPlayerList().get(2).getSheep() != null) {
			game.getPlayerList().get(2).getSheep().setPaddleView(player3Sheep, 2);
		} else {
			player3Sheep.setVisible(false);
		}
		if (game.getPlayerList().get(3).getSheep() != null) {
			game.getPlayerList().get(3).getSheep().setPaddleView(player4Sheep, 3);
		} else {
			player4Sheep.setVisible(false);
		}

		game.getWallList().get(0).setWallView(player1Wall1); // The walls
		game.getWallList().get(1).setWallView(player1Wall2);
		game.getWallList().get(2).setWallView(player1Wall3);
		game.getWallList().get(3).setWallView(player1Wall4);
		game.getWallList().get(4).setWallView(player1Wall5);
		game.getWallList().get(5).setWallView(player2Wall1);
		game.getWallList().get(6).setWallView(player2Wall2);
		game.getWallList().get(7).setWallView(player2Wall3);
		game.getWallList().get(8).setWallView(player2Wall4);
		game.getWallList().get(9).setWallView(player2Wall5);
		game.getWallList().get(10).setWallView(player3Wall1);
		game.getWallList().get(11).setWallView(player3Wall2);
		game.getWallList().get(12).setWallView(player3Wall3);
		game.getWallList().get(13).setWallView(player3Wall4);
		game.getWallList().get(14).setWallView(player3Wall5);
		game.getWallList().get(15).setWallView(player4Wall1);
		game.getWallList().get(16).setWallView(player4Wall2);
		game.getWallList().get(17).setWallView(player4Wall3);
		game.getWallList().get(18).setWallView(player4Wall4);
		game.getWallList().get(19).setWallView(player4Wall5);
	}

	// Apply the png sprites to the game elements
	private void graphicsInit() {
		// Applying patterns to crates
		player1Wall1.setFill(player1CratePattern);
		player1Wall2.setFill(player1CratePattern);
		player1Wall3.setFill(player1CratePattern);
		player1Wall4.setFill(player1CratePattern);
		player1Wall5.setFill(player1CratePattern);

		player2Wall1.setFill(player2CratePattern);
		player2Wall2.setFill(player2CratePattern);
		player2Wall3.setFill(player2CratePattern);
		player2Wall4.setFill(player2CratePattern);
		player2Wall5.setFill(player2CratePattern);

		player3Wall1.setFill(player3CratePattern);
		player3Wall2.setFill(player3CratePattern);
		player3Wall3.setFill(player3CratePattern);
		player3Wall4.setFill(player3CratePattern);
		player3Wall5.setFill(player3CratePattern);

		player4Wall1.setFill(player4CratePattern);
		player4Wall2.setFill(player4CratePattern);
		player4Wall3.setFill(player4CratePattern);
		player4Wall4.setFill(player4CratePattern);
		player4Wall5.setFill(player4CratePattern);

		// Applying patterns to paddles
		player1Paddle.setStrokeWidth(0);
		player1Paddle.setFill(player1ShipPattern);

		player2Paddle.setStrokeWidth(0);
		player2Paddle.setFill(player2ShipPattern);

		player3Paddle.setStrokeWidth(0);
		player3Paddle.setFill(player3ShipPattern);

		player4Paddle.setStrokeWidth(0);
		player4Paddle.setFill(player4ShipPattern);

		// Applying patterns to sheep
		player1Sheep.setStrokeWidth(0);
		player1Sheep.setFill(sheepPattern);

		player2Sheep.setStrokeWidth(0);
		player2Sheep.setFill(sheepPattern);

		player3Sheep.setStrokeWidth(0);
		player3Sheep.setFill(sheepPattern);

		player4Sheep.setStrokeWidth(0);
		player4Sheep.setFill(sheepPattern);

		// Applying patterns to warlords
		player1Warlord.setStrokeWidth(0);
		player1Warlord.setFill(player1MothershipPattern);

		player2Warlord.setStrokeWidth(0);
		player2Warlord.setFill(player2MothershipPattern);

		player3Warlord.setStrokeWidth(0);
		player3Warlord.setFill(player3MothershipPattern);

		player4Warlord.setStrokeWidth(0);
		player4Warlord.setFill(player4MothershipPattern);

		// Applying pattern to ball
		ball.setStrokeWidth(0);
		ball.setFill(gameBall1Pattern);

		// Apply flag patterns
		flags.add(flag0Pattern);
		flags.add(flag1Pattern);
		flags.add(flag2Pattern);
		flags.add(flag3Pattern);
		flags.add(flag4Pattern);
		flags.add(flag5Pattern);
		flags.add(flag6Pattern);
		flags.add(flag7Pattern);
		flags.add(flag8Pattern);
		flags.add(flag9Pattern);
		player1Flag.setFill(flags.get(game.getPlayerList().get(0).getClassNum()));	
		player2Flag.setFill(flags.get(game.getPlayerList().get(1).getClassNum()));
		player3Flag.setFill(flags.get(game.getPlayerList().get(2).getClassNum()));
		player4Flag.setFill(flags.get(game.getPlayerList().get(3).getClassNum()));
		
		//Adapt depending on game type
		if(MainMenuViewController.isCampaign){
			gameOver3.setText("Press ENTER to Continue \n or ESC to exit.");
		} else {
			gameOver3.setText("Press ESC to exit.");
		}
		flagRects.add(player1Flag);
		flagRects.add(player2Flag);
		flagRects.add(player3Flag);
		flagRects.add(player4Flag);

		// Game Background
		ArrayList<ImagePattern> backgrounds = new ArrayList<ImagePattern>();
		backgrounds.add(gameBackground1Pattern);
		backgrounds.add(gameBackground2Pattern);
		backgrounds.add(gameBackground3Pattern);
		backgrounds.add(gameBackground4Pattern);

		background.setFill(backgrounds.get(game.getBackgroundNum()));
	}

	// Ball
	@FXML
	private Circle ball;

	// Paddles
	@FXML 
	private Rectangle player1Paddle;
	@FXML 
	private Rectangle player2Paddle;
	@FXML 
	private Rectangle player3Paddle;
	@FXML 
	private Rectangle player4Paddle;

	// Warlords
	@FXML 
	private Rectangle player1Warlord;
	@FXML 
	private Rectangle player2Warlord;
	@FXML 
	private Rectangle player3Warlord;
	@FXML 
	private Rectangle player4Warlord;

	// Player 1 Walls
	@FXML 
	private Rectangle player1Wall1;
	@FXML 
	private Rectangle player1Wall2;
	@FXML 
	private Rectangle player1Wall3;
	@FXML 
	private Rectangle player1Wall4;
	@FXML 
	private Rectangle player1Wall5;

	// Player 2 Walls
	@FXML 
	private Rectangle player2Wall1;
	@FXML 
	private Rectangle player2Wall2;
	@FXML 
	private Rectangle player2Wall3;
	@FXML 
	private Rectangle player2Wall4;
	@FXML 
	private Rectangle player2Wall5;

	// Player 3 Walls
	@FXML 
	private Rectangle player3Wall1;
	@FXML 
	private Rectangle player3Wall2;
	@FXML 
	private Rectangle player3Wall3;
	@FXML 
	private Rectangle player3Wall4;
	@FXML 
	private Rectangle player3Wall5;

	// Player 4 Walls
	@FXML 
	private Rectangle player4Wall1;
	@FXML 
	private Rectangle player4Wall2;
	@FXML 
	private Rectangle player4Wall3;
	@FXML 
	private Rectangle player4Wall4;
	@FXML 
	private Rectangle player4Wall5;

	// Flags
	@FXML 
	private Rectangle player1Flag;
	@FXML 
	private Rectangle player2Flag;
	@FXML 
	private Rectangle player3Flag;
	@FXML 
	private Rectangle player4Flag;

	// Sheep
	@FXML 
	private Rectangle player1Sheep;
	@FXML 
	private Rectangle player2Sheep;
	@FXML 
	private Rectangle player3Sheep;
	@FXML 
	private Rectangle player4Sheep;

	//Miscellaneous objects
	@FXML
	private Rectangle background;

	@FXML 
	private Pane pane;

	@FXML
	private Text timeLeft;

	@FXML
	private Text countIn;

	@FXML
	private Text pauseMessage1;

	@FXML
	private Text pauseMessage2;

	@FXML
	private Text gameOver1;

	@FXML
	private Text gameOver2;

	@FXML
	private Text gameOver3;

	@FXML 
	private Rectangle pauseBox;

	// Patterns for the walls
	private Image player1Crate = new Image("file:images/player1Crate.png");
	ImagePattern player1CratePattern = new ImagePattern(player1Crate);
	private Image player2Crate = new Image("file:images/player2Crate.png");
	ImagePattern player2CratePattern = new ImagePattern(player2Crate);
	private Image player3Crate = new Image("file:images/player3Crate.png");
	ImagePattern player3CratePattern = new ImagePattern(player3Crate);
	private Image player4Crate = new Image("file:images/player4Crate.png");
	ImagePattern player4CratePattern = new ImagePattern(player4Crate);
	private Image redCrate = new Image("file:images/redCrate.png");
	ImagePattern redCratePattern = new ImagePattern(redCrate);

	// Patterns for the paddles
	private Image player1Ship = new Image("file:images/player1Ship.png");
	ImagePattern player1ShipPattern = new ImagePattern(player1Ship);
	private Image player2Ship = new Image("file:images/player2Ship.png");
	ImagePattern player2ShipPattern = new ImagePattern(player2Ship);
	private Image player3Ship = new Image("file:images/player3Ship.png");
	ImagePattern player3ShipPattern = new ImagePattern(player3Ship);
	private Image player4Ship = new Image("file:images/player4Ship.png");
	ImagePattern player4ShipPattern = new ImagePattern(player4Ship);

	// Patterns for sheep
	private Image sheepImage = new Image("file:images/sheep.png");
	ImagePattern sheepPattern = new ImagePattern(sheepImage);
	
	// Patterns for spiders
	private Image spiderImage = new Image("file:images/spider.png");
	ImagePattern spiderPattern = new ImagePattern(spiderImage);
	
	// Patterns for the warlords
	private Image player1Mothership = new Image("file:images/player1Mothership.png");
	ImagePattern player1MothershipPattern = new ImagePattern(player1Mothership);
	private Image player2Mothership = new Image("file:images/player2Mothership.png");
	ImagePattern player2MothershipPattern = new ImagePattern(player2Mothership);
	private Image player3Mothership = new Image("file:images/player3Mothership.png");
	ImagePattern player3MothershipPattern = new ImagePattern(player3Mothership);
	private Image player4Mothership = new Image("file:images/player4Mothership.png");
	ImagePattern player4MothershipPattern = new ImagePattern(player4Mothership);

	// Patterns for the ball
	private Image gameBall1 = new Image("file:images/ball1.png");
	ImagePattern gameBall1Pattern = new ImagePattern(gameBall1);
	private Image gameBall2 = new Image("file:images/ball2.png");
	ImagePattern gameBall2Pattern = new ImagePattern(gameBall2);
	private Image gameBall3 = new Image("file:images/ball3.png");
	ImagePattern gameBall3Pattern = new ImagePattern(gameBall3);
	private Image gameBall4 = new Image("file:images/ball4.png");
	ImagePattern gameBall4Pattern = new ImagePattern(gameBall4);
	private Image gameBall5 = new Image("file:images/ball5.png");
	ImagePattern gameBall5Pattern = new ImagePattern(gameBall5);
	private Image gameBall6 = new Image("file:images/ball6.png");
	ImagePattern gameBall6Pattern = new ImagePattern(gameBall6);

	// Patterns for the flags
	private Image flag0Image = new Image("file:images/fr.png");
	ImagePattern flag0Pattern = new ImagePattern(flag0Image);
	private Image flag1Image = new Image("file:images/us.png");
	ImagePattern flag1Pattern = new ImagePattern(flag1Image);
	private Image flag2Image = new Image("file:images/gb.png");
	ImagePattern flag2Pattern = new ImagePattern(flag2Image);
	private Image flag3Image = new Image("file:images/nz.png");
	ImagePattern flag3Pattern = new ImagePattern(flag3Image);
	private Image flag4Image = new Image("file:images/cn.png");
	ImagePattern flag4Pattern = new ImagePattern(flag4Image);
	private Image flag5Image = new Image("file:images/au.png");
	ImagePattern flag5Pattern = new ImagePattern(flag5Image);
	private Image flag6Image = new Image("file:images/in.png");
	ImagePattern flag6Pattern = new ImagePattern(flag6Image);
	private Image flag7Image = new Image("file:images/ru.png");
	ImagePattern flag7Pattern = new ImagePattern(flag7Image);
	private Image flag8Image = new Image("file:images/eg.png");
	ImagePattern flag8Pattern = new ImagePattern(flag8Image);
	private Image flag9Image = new Image("file:images/br.png");
	ImagePattern flag9Pattern = new ImagePattern(flag9Image);

	// Patterns for the background
	private Image gameBackground1 = new Image("file:images/gameBackground.png");
	ImagePattern gameBackground1Pattern = new ImagePattern(gameBackground1);
	private Image gameBackground2 = new Image("file:images/gameBackground2.png");
	ImagePattern gameBackground2Pattern = new ImagePattern(gameBackground2);
	private Image gameBackground3 = new Image("file:images/gameBackground3.png");
	ImagePattern gameBackground3Pattern = new ImagePattern(gameBackground3);
	private Image gameBackground4 = new Image("file:images/gameBackground4.png");
	ImagePattern gameBackground4Pattern = new ImagePattern(gameBackground4);
}
