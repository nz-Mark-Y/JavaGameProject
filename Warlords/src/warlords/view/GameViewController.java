package warlords.view;

import java.util.Timer;
import java.util.TimerTask;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import warlords.WarlordsController;
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
	
	public GameViewController() {
		
	}
	
	@FXML
	private void initialise() {
		
	}
	
	public void setWarlordsController(WarlordsController warlordsController, final Scene scene) {
		this.warlordsController = warlordsController;
		this.game = warlordsController.getGame();
		this.scene = scene;
		
		// Initial ball velocity
		game.getBall().setXVelocity(-3);
		game.getBall().setYVelocity(-3);
		
		// Game timer for ball and paddles to move
		Timer timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask () {
			@Override
			public void run() {
				onTick();
			}
		}, 20, 20);	
		
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
							case P:
								game.setKeyUp(16);
								break;
							case ESCAPE:
								game.setKeyUp(17);
								break;
							default:
								break;					
						}
					}
		        };    
		    	scene.setOnKeyPressed(handler0);
		    	scene.setOnKeyReleased(handler1);
			}		
		});     
        thread.start();
	}
	
	// Do the logic, then rerender all the objects. Will add lists in soon.
	private void onTick() {
		game.tick();
		ball.setCenterX(game.getBall().getXPos() + game.getBall().length / 2);
		ball.setCenterY(-(game.getBall().getYPos() + game.getBall().height / 2));	
		player1Paddle.setX(game.getPlayerList().get(0).getPaddle().getXPos());
		player1Paddle.setY(-(game.getPlayerList().get(0).getPaddle().getYPos() + Paddle.height));
		player2Paddle.setX(game.getPlayerList().get(1).getPaddle().getXPos());
		player2Paddle.setY(-(game.getPlayerList().get(1).getPaddle().getYPos() + Paddle.height));
		player1Wall.setX(game.getWallList().get(0).getXPos());
		player1Wall.setY(-(game.getWallList().get(0).getYPos() + Wall.height));
		player2Wall.setX(game.getWallList().get(1).getXPos());
		player2Wall.setY(-(game.getWallList().get(1).getYPos() + Wall.height));
		player1Warlord.setX(game.getPlayerList().get(0).getXPos());
		player1Warlord.setY(-(game.getPlayerList().get(0).getYPos() + Warlord.height));
		player2Warlord.setX(game.getPlayerList().get(1).getXPos());
		player2Warlord.setY(-(game.getPlayerList().get(1).getYPos() + Warlord.height));	
		player3Paddle.setX(game.getPlayerList().get(2).getPaddle().getXPos());
		player3Paddle.setY(-(game.getPlayerList().get(2).getPaddle().getYPos() + Paddle.height));
		player4Paddle.setX(game.getPlayerList().get(3).getPaddle().getXPos());
		player4Paddle.setY(-(game.getPlayerList().get(3).getPaddle().getYPos() + Paddle.height));
		player3Wall.setX(game.getWallList().get(2).getXPos());
		player3Wall.setY(-(game.getWallList().get(2).getYPos() + Wall.height));
		player4Wall.setX(game.getWallList().get(3).getXPos());
		player4Wall.setY(-(game.getWallList().get(3).getYPos() + Wall.height));
		player3Warlord.setX(game.getPlayerList().get(2).getXPos());
		player3Warlord.setY(-(game.getPlayerList().get(2).getYPos() + Warlord.height));
		player4Warlord.setX(game.getPlayerList().get(3).getXPos());
		player4Warlord.setY(-(game.getPlayerList().get(3).getYPos() + Warlord.height));	
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
}
