package warlords.view;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import javafx.fxml.FXML;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import warlords.control.WarlordsController;
import warlords.model.Game;
import warlords.model.Paddle;
import warlords.model.Wall;
import warlords.model.Warlord;

public class GameViewController {
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
	
	private WarlordsController warlordsController;
	private Game game;
	
	public GameViewController() {
		
	}
	
	@FXML
	private void initialise() {
		
	}
	
	public void setWarlordsController(WarlordsController warlordsController) {
		this.warlordsController = warlordsController;
		this.game = warlordsController.getGame();
		
		game.getBall().setXVelocity(-3);
		game.getBall().setYVelocity(-3);
		
		Timer timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask () {
			@Override
			public void run() {
				onTick();
			}
		},40, 40);
	}
	
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
}
