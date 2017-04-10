package warlords.view;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import warlords.WarlordsController;
import warlords.model.Game;

public class MutatorsViewController {
	private WarlordsController warlordsController;
	private int isSelected;
	private int speedSelect;
	private int reverseSelect;
	private int randomSelect;
	private ArrayList<Text> menuList = new ArrayList<Text>();
	private ArrayList<Text> yesList = new ArrayList<Text>();
	Scene scene;
	private EventHandler<KeyEvent> handler;
	private AudioClip menuScroller = new AudioClip(new File("sounds/menuScroller.wav").toURI().toString());
	
	public MutatorsViewController() {
		
	}
	
	// Link the controller to the WarlordController and scene
	public void setWarlordsController(WarlordsController warlordsController, Scene scene) {
		this.warlordsController = warlordsController;
		this.scene = scene;
		// Add menu items to controller
		menuList.add(speed);
		menuList.add(reverse);
		menuList.add(random);
		yesList.add(speedYes);
		yesList.add(reverseYes);
		yesList.add(randomYes);
		menuList.add(back);
		isSelected = 0;	
		speedSelect = 1;	
		reverseSelect = 0;	
		randomSelect = 1;	
		
		// Set custom fonts 
		Font myFont = null;
		Font titleFont = null;
        try {
            myFont = Font.loadFont(new FileInputStream(new File("Fonts/evanescent_p.ttf")), 72);
            titleFont = Font.loadFont(new FileInputStream(new File("Fonts/evanescent_p.ttf")), 96);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        for (int i=0; i<menuList.size(); i++) {
        	menuList.get(i).setFont(myFont);
        }
        for (int i=0; i<yesList.size(); i++) {
        	yesList.get(i).setFont(myFont);
        }
        title.setFont(titleFont);
        updateSelection();
        
        // Create a key handler for the scene
        handler = new EventHandler<KeyEvent>() {
			@Override
		    public void handle(KeyEvent t) {
				menuScroller.play();
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
				menuList.get(i).setFill(Color.valueOf("#FFFFFF")); // Selected is white
				if (i < 3) {
					yesList.get(i).setFill(Color.valueOf("#FFFFFF")); // Selected is white
				}
			} else {
				menuList.get(i).setFill(Color.valueOf("#686868")); // Unselected is grey
				if (i < 3) {
					yesList.get(i).setFill(Color.valueOf("#686868")); // Unselected is grey
				}
			}			
		}	
		if (speedSelect == 0) { // Setting text based on which settings are selected
			GameViewController.gameSpeed = 18; // Slower game speed
			yesList.get(0).setText("Low");
		} else if (speedSelect == 1) {
			GameViewController.gameSpeed = 12; // Normal game speed
			yesList.get(0).setText("Med");
		} else {
			GameViewController.gameSpeed = 6; // Faster game speed
			yesList.get(0).setText("High");
		}
		if (reverseSelect == 0) {
			Game.reverseControls = false; // Non reversed controls
			yesList.get(1).setText("No");
		} else {
			Game.reverseControls = true; // Reversing controls
			yesList.get(1).setText("Yes");
		}
		if (randomSelect == 0) {
			Game.randomness = 0;
			yesList.get(2).setText("Low");
		} else if (randomSelect == 1) {
			Game.randomness = 1;
			yesList.get(2).setText("Med");
		} else {
			Game.randomness = 2;
			yesList.get(2).setText("High");
		}
	}
	
	// If the user presses the up arrow
	public void upArrowPressed() {
		if (isSelected == 0) {
			isSelected = menuList.size()-1;
		} else {
			isSelected--;
		}
	}
	
	// If the user presses the down arrow
	public void downArrowPressed() {
		if (isSelected == menuList.size()-1) {
			isSelected = 0;
		} else {
			isSelected++;
		}
	}
	
	// When enter is pressed, change mutators or exit
	public void enterPressed() {	
		if (isSelected == 0) { // Game speed mutator
			if (speedSelect == 2) {
				speedSelect = 0;
			} else {
				speedSelect++;
			}
		} else if (isSelected == 1) { // Reverse controls mutator		
			if (reverseSelect == 1) {
				reverseSelect = 0;
			} else {
				reverseSelect++;
			}
			
		} else if (isSelected == 2) { // Random bounces mutator			
			if (randomSelect == 2) {
				randomSelect = 0;
			} else {
				randomSelect++;
			}
		} else if (isSelected == 3) { // Removes the key handler from the scene, moves to the next view
			scene.removeEventHandler(KeyEvent.KEY_PRESSED, handler);
			warlordsController.showMultiplayerMenuView();
		} else {
			System.out.println("Unavailable Menu Option");
		}
		updateSelection();
	}
	
	// Text
	@FXML
	private Text title;
	@FXML
	private Text speed;
	@FXML
	private Text reverse;
	@FXML
	private Text random;
	@FXML
	private Text speedYes;
	@FXML
	private Text reverseYes;
	@FXML
	private Text randomYes;	
	@FXML
	private Text back;
}

