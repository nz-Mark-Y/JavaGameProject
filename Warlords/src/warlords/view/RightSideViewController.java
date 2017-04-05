package warlords.view;

import javafx.fxml.FXML;

import warlords.WarlordsController;
import warlords.model.Game;

public class RightSideViewController {
	private WarlordsController warlordsController;
	private Game game;
	
	public RightSideViewController() {
		
	}
	
	@FXML
	private void initialise() {
		
	}
	
	public void setWarlordsController(WarlordsController warlordsController) {
		this.warlordsController = warlordsController;
		this.game = warlordsController.getGame();
	}
}
