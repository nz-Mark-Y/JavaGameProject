package warlords.view;

import java.util.Timer;
import java.util.TimerTask;

import javafx.fxml.FXML;
import javafx.scene.text.Text;

import warlords.WarlordsController;
import warlords.model.Game;

public class LeftSideViewController {
	private WarlordsController warlordsController;
	private Game game;
	
	public LeftSideViewController() {
		
	}
	
	@FXML
	private void initialise() {
		
	}
	
	public void setWarlordsController(WarlordsController warlordsController) {
		this.warlordsController = warlordsController;
		this.game = warlordsController.getGame();
		
		Timer timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask () {
			@Override
			public void run() {
				showTime();
			}
		}, 500, 500);
	}
	
	private void showTime() {
		timeLeft.setText("Time Left: " + game.getTimeRemaining());
	}
	
	@FXML
	private Text timeLeft;
}
