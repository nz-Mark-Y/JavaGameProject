package warlords.view;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import warlords.WarlordsController;

public class MainMenuViewController {
	private WarlordsController warlordsController;
	private int isSelected;
	private ArrayList<Text> menuList = new ArrayList<Text>();
	Scene scene;
	private EventHandler<KeyEvent> handler;
	
	public MainMenuViewController() {
		
	}
	
	// Sets the controller and scene
	public void setWarlordsController(WarlordsController warlordsController, Scene scene) {
		this.warlordsController = warlordsController;
		this.scene = scene;
		// Add menu items to controller
		menuList.add(campaign);
		menuList.add(multiplayer);
		menuList.add(options);
		menuList.add(about);
		menuList.add(exit);
		isSelected = 0;	
		
		// Set custom fonts 
		Font myFont = null;
        try {
            myFont = Font.loadFont(new FileInputStream(new File("Fonts/evanescent_p.ttf")), 96);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        for (int i=0; i<menuList.size(); i++) {
        	menuList.get(i).setFont(myFont);
        }
        updateSelection(); 
        
        // Create a key handler for the scene
        handler = new EventHandler<KeyEvent>() {
			@Override
		    public void handle(KeyEvent t) {
				switch(t.getCode()) {
					case UP:
						upArrowPressed();
						updateSelection();
						break;
					case DOWN:
						downArrowPressed();
						updateSelection();
						break;
					case ENTER:
						enterPressed();
					default:
						break;					
				}
			}
        };    
    	scene.addEventHandler(KeyEvent.KEY_PRESSED, handler);
	}
	
	// Refresh the selection menu
	public void updateSelection() {
		for (int i=0; i<menuList.size(); i++) {
			if (i == isSelected) {
				menuList.get(i).setFill(Color.valueOf("#FFFFFF"));
			} else {
				menuList.get(i).setFill(Color.valueOf("#686868"));
			}			
		}	
	}
	
	public void upArrowPressed() {
		if (isSelected == 0) {
			isSelected = menuList.size()-1;
		} else {
			isSelected--;
		}
	}
	
	public void downArrowPressed() {
		if (isSelected == menuList.size()-1) {
			isSelected = 0;
		} else {
			isSelected++;
		}
	}
	
	// Removes the key handler from the scene, moves to the next view
	public void enterPressed() {
		scene.removeEventHandler(KeyEvent.KEY_PRESSED, handler);
		if (isSelected == 0) {
			warlordsController.showCampaignMenu();
		} else if (isSelected == 1) {
			
		} else if (isSelected == 2) {
			
		} else if (isSelected == 3) {
			warlordsController.showGameView();
		} else if (isSelected == 4) {
			warlordsController.stop();
		} else {
			System.out.println("Unavailable Menu Option");
		}
	}
	
	@FXML
	private Text campaign;
	
	@FXML
	private Text multiplayer;
	
	@FXML
	private Text options;
	
	@FXML
	private Text about;
	
	@FXML
	private Text exit;
	
	@FXML
	private AnchorPane window;
}
