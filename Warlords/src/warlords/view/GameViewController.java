package warlords.view;

import java.util.Timer;
import java.util.TimerTask;

import javafx.fxml.FXML;
import javafx.scene.shape.Circle;
import warlords.control.WarlordsController;
import warlords.model.Game;

public class GameViewController {
	@FXML
	private Circle ball;
	
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
		game.getBall().setXVelocity(2);
		game.getBall().setYVelocity(2);
		
		Timer timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask () {
			@Override
			public void run() {
				onTick();
			}
		},20, 20);
	}
	
	private void onTick() {
		ball.setCenterX(game.getBall().getXPos() + game.getBall().length / 2);
		ball.setCenterY(-(game.getBall().getYPos() + game.getBall().height / 2));
		game.tick();
	}
}
