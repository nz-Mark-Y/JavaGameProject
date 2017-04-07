package warlords.view;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
	private WarlordsController warlordsController;
	private Game game;
	Scene scene;
	private EventHandler<KeyEvent> handler0;
	private EventHandler<KeyEvent> handler1;
	private AudioClip multiplayerTheme = new AudioClip(new File("sounds/multiplayerThemeReduced.wav").toURI().toString());
	private Timer animationTimer;
	private Timer timeLeftTimer;
	private boolean paused;
	private int delayer = 0;

	public GameViewController() {

	}

	@FXML
	private void initialise() {

	}

	public void setWarlordsController(WarlordsController warlordsController, final Scene scene) {
		this.warlordsController = warlordsController;
		this.game = warlordsController.getGame();
		this.game.setGameViewController(this);
		this.scene = scene;
		paused = true;

		// Initialize the graphics for the objects
		graphicsInit();
		addViewsToModels();

		// Initial ball velocity
		int ballXVel = 0;
		int ballYVel = 0;
		onTick();
		while ((ballXVel == 0) || (ballXVel == 1) || (ballXVel == -1) || (ballXVel == 2) || (ballXVel == -2) || (ballYVel == 0) || (ballYVel == 1) || (ballYVel == -1) || (ballYVel == 2) || (ballYVel == -2)) {
			ballXVel = 6 - (int) (Math.random() * (12));
			ballYVel = 6 - (int) (Math.random() * (12));
		}
		game.getBall().setXVelocity(ballXVel);
		game.getBall().setYVelocity(ballYVel);

		// Fonts
		setFonts();

		// Timers
		createTimers();

		// Key handers for keys being pressed and released
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

	public void createTimers() {
		// Count in the three seconds
		countIn.setText("3");
		Timer countInTimer = new Timer();
		countInTimer.scheduleAtFixedRate(new TimerTask () {
			int count = 2;
			@Override
			public void run() {
				if (count > 0) {
					countIn.setText(Integer.toString(count));
					count--;
				} else if (count == 0){
					countIn.setText("GO");
					count--;
				} else {	
					countIn.setVisible(false);
					paused = false;
					// Game timer for ball and paddles to move
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
										} else {
											multiplayerTheme.stop();
											System.out.println("Game finished");
											animationTimer.cancel();
										}
									}
								});
							} catch (Exception ex){
								System.out.println(ex.getMessage());
							}
						}
					}, 20, 20);		

					// Timer to display time remaining
					timeLeftTimer = new Timer();
					timeLeftTimer.scheduleAtFixedRate(new TimerTask () {
						@Override
						public void run() {
							timeLeft.setText(Integer.toString(game.getTimeRemaining()));
						}
					}, 500, 500);		
					countInTimer.cancel();
				}
			}
		}, 1000, 1000);
		
		
	}
	
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
		pauseMessage1.setFont(textFont);
		pauseMessage2.setFont(smallTextFont);
		pauseMessage1.setVisible(false);
		pauseMessage2.setVisible(false);
		pauseBox.setVisible(false);
	}

	// Do the logic, then rerender all the objects.
	private void onTick() {
		game.tick();
		for (int i=0; i<game.getBallList().size(); i++) {
			game.getBallList().get(i).getBallView().setCenterX(game.getBallList().get(i).getXPos() + Ball.length / 2);
			game.getBallList().get(i).getBallView().setCenterY(-(game.getBallList().get(i).getYPos() + Ball.height / 2));
		}	
		for (int i=0; i<game.getPlayerList().size(); i++) {
			game.getPlayerList().get(i).getWarlordView().setX(game.getPlayerList().get(i).getXPos());
			game.getPlayerList().get(i).getWarlordView().setY(-(game.getPlayerList().get(i).getYPos() + Warlord.height));
			game.getPlayerList().get(i).getPaddle().getPaddleView().setX(game.getPlayerList().get(i).getPaddle().getXPos());
			game.getPlayerList().get(i).getPaddle().getPaddleView().setY(-(game.getPlayerList().get(i).getPaddle().getYPos() + Paddle.height));
		}
		for (int i=0;i<game.getWallList().size(); i++) {
			game.getWallList().get(i).getWallView().setX(game.getWallList().get(i).getXPos());
			game.getWallList().get(i).getWallView().setY(-(game.getWallList().get(i).getYPos() + Wall.height));
		}

		//Play multiplayer theme
		if(!multiplayerTheme.isPlaying() && !paused){
			multiplayerTheme.play();
		}

		//Loop through the ball frames
		if(delayer == 5){
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

	public void pause() {
		if (!paused) {
			multiplayerTheme.stop();
			paused = true;
			animationTimer.cancel();
			timeLeftTimer.cancel();
			pauseMessage1.setVisible(true);
			pauseMessage2.setVisible(true);
			pauseBox.setVisible(true);
		} else {
			multiplayerTheme.stop();
			paused = false;
			createTimers();
			pauseMessage1.setVisible(false);
			pauseMessage2.setVisible(false);
			pauseBox.setVisible(false);
		}
	}

	public void exit() {
		multiplayerTheme.stop();
		scene.removeEventHandler(KeyEvent.KEY_PRESSED, handler0);
		scene.removeEventHandler(KeyEvent.KEY_RELEASED, handler1);
		game.finish();
		warlordsController.showMainMenu();
	}

	public void createBulletView(Ball bullet) {
		Circle circle = new Circle();
		circle.setFill(ball.getFill());
		circle.setRadius(5);
		circle.setCenterX(0);
		circle.setCenterY(0);
		circle.setLayoutX(0);
		circle.setLayoutY(768);
		pane.getChildren().add(circle);
		bullet.setBallView(circle);
	}

	public void delBall(Ball bullet) {
		bullet.getBallView().setVisible(false);
	}

	public void addViewsToModels() {
		game.getBall().setBallView(ball);
		game.getPlayerList().get(0).setWarlordView(player1Warlord);
		game.getPlayerList().get(1).setWarlordView(player2Warlord);
		game.getPlayerList().get(2).setWarlordView(player3Warlord);
		game.getPlayerList().get(3).setWarlordView(player4Warlord);
		game.getPlayerList().get(0).getPaddle().setPaddleView(player1Paddle, 0);
		game.getPlayerList().get(1).getPaddle().setPaddleView(player2Paddle, 1);
		game.getPlayerList().get(2).getPaddle().setPaddleView(player3Paddle, 2);
		game.getPlayerList().get(3).getPaddle().setPaddleView(player4Paddle, 3);
		game.getWallList().get(0).setWallView(player1Wall);
		game.getWallList().get(1).setWallView(player2Wall);
		game.getWallList().get(2).setWallView(player3Wall);
		game.getWallList().get(3).setWallView(player4Wall);
	}

	private void graphicsInit() {
		//Applying patterns to crates
		player1Wall.setFill(player1CratePattern);
		player2Wall.setFill(player2CratePattern);
		player3Wall.setFill(player3CratePattern);
		player4Wall.setFill(player4CratePattern);

		//Applying patterns to paddles
		player1Paddle.setStrokeWidth(0);
		player1Paddle.setFill(player1ShipPattern);

		player2Paddle.setStrokeWidth(0);
		player2Paddle.setFill(player2ShipPattern);

		player3Paddle.setStrokeWidth(0);
		player3Paddle.setFill(player3ShipPattern);

		player4Paddle.setStrokeWidth(0);
		player4Paddle.setFill(player4ShipPattern);

		//Applying patterns to warlords
		player1Warlord.setStrokeWidth(0);
		player1Warlord.setFill(player1MothershipPattern);

		player2Warlord.setStrokeWidth(0);
		player2Warlord.setFill(player2MothershipPattern);

		player3Warlord.setStrokeWidth(0);
		player3Warlord.setFill(player3MothershipPattern);

		player4Warlord.setStrokeWidth(0);
		player4Warlord.setFill(player4MothershipPattern);

		//Applying pattern to ball
		ball.setStrokeWidth(0);
		ball.setFill(gameBall1Pattern);
		
		//Apply flag pattern
		player1Flag.setFill(null);	//NEED TO ACCESS WHICH COUNTRY WAS SELECTED IN THE MENU SELECTION
		player2Flag.setFill(null);
		player3Flag.setFill(null);
		player4Flag.setFill(null);
	}

	@FXML
	private Circle ball;

	@FXML 
	private Rectangle player1Paddle;

	@FXML 
	private Rectangle player2Paddle;

	@FXML 
	private Rectangle player1Wall;

	@FXML 
	private Rectangle player2Wall;

	@FXML 
	private Rectangle player1Warlord;

	@FXML 
	private Rectangle player2Warlord;

	@FXML 
	private Rectangle player3Paddle;

	@FXML 
	private Rectangle player4Paddle;

	@FXML 
	private Rectangle player3Wall;

	@FXML 
	private Rectangle player4Wall;

	@FXML 
	private Rectangle player3Warlord;

	@FXML 
	private Rectangle player4Warlord;
	
	@FXML 
	private Rectangle player1Flag;

	@FXML 
	private Rectangle player2Flag;

	@FXML 
	private Rectangle player3Flag;

	@FXML 
	private Rectangle player4Flag;

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
	private Rectangle pauseBox;

	//Creating patterns for the walls
	private Image player1Crate = new Image("file:images/player1Crate.png");
	ImagePattern player1CratePattern = new ImagePattern(player1Crate);

	private Image player2Crate = new Image("file:images/player2Crate.png");
	ImagePattern player2CratePattern = new ImagePattern(player2Crate);

	private Image player3Crate = new Image("file:images/player3Crate.png");
	ImagePattern player3CratePattern = new ImagePattern(player3Crate);

	private Image player4Crate = new Image("file:images/player4Crate.png");
	ImagePattern player4CratePattern = new ImagePattern(player4Crate);

	//Creating patterns for the paddles
	private Image player1Ship = new Image("file:images/player1Ship.png");
	ImagePattern player1ShipPattern = new ImagePattern(player1Ship);

	private Image player2Ship = new Image("file:images/player2Ship.png");
	ImagePattern player2ShipPattern = new ImagePattern(player2Ship);

	private Image player3Ship = new Image("file:images/player3Ship.png");
	ImagePattern player3ShipPattern = new ImagePattern(player3Ship);

	private Image player4Ship = new Image("file:images/player4Ship.png");
	ImagePattern player4ShipPattern = new ImagePattern(player4Ship);

	//Creating patterns for the warlords
	private Image player1Mothership = new Image("file:images/player1Mothership.png");
	ImagePattern player1MothershipPattern = new ImagePattern(player1Mothership);

	private Image player2Mothership = new Image("file:images/player2Mothership.png");
	ImagePattern player2MothershipPattern = new ImagePattern(player2Mothership);

	private Image player3Mothership = new Image("file:images/player3Mothership.png");
	ImagePattern player3MothershipPattern = new ImagePattern(player3Mothership);

	private Image player4Mothership = new Image("file:images/player4Mothership.png");
	ImagePattern player4MothershipPattern = new ImagePattern(player4Mothership);

	//Creating patterns for the ball
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

}
