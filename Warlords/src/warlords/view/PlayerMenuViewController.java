package warlords.view;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Slider;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import warlords.WarlordsController;

public class PlayerMenuViewController {
	private WarlordsController warlordsController;
	private ArrayList<Text> textList = new ArrayList<Text>();
	private ArrayList<Text> titleList = new ArrayList<Text>();
	Scene scene;
	private EventHandler<KeyEvent> handler;
	
	public PlayerMenuViewController() {
		
	}
	
	// Sets the controller and scene
	public void setWarlordsController(WarlordsController warlordsController, Scene scene) {
		this.warlordsController = warlordsController;
		this.scene = scene;
		// Add text items to controller
		titleList.add(title1);
		titleList.add(title2);
		titleList.add(title3);
		titleList.add(title4);
		textList.add(line1);
		textList.add(line2);
		textList.add(line3);
		textList.add(line4);
		textList.add(line5);
		textList.add(line6);
		textList.add(line7);
		textList.add(line8);
		textList.add(line9);
		textList.add(line10);
		textList.add(backInstructions1);
		textList.add(backInstructions2);
		
		// Set custom fonts 
		Font titleFont = null;
		Font textFont = null;
        try {
            titleFont = Font.loadFont(new FileInputStream(new File("Fonts/evanescent_p.ttf")), 48);
            textFont = Font.loadFont(new FileInputStream(new File("Fonts/evanescent_p.ttf")), 24);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        for (int i=0; i<titleList.size(); i++) {
        	titleList.get(i).setFont(titleFont);
        }
        for(int i=0; i<textList.size(); i++) {
        	textList.get(i).setFont(textFont);
        }
        
        // Create a key handler for the scene
        handler = new EventHandler<KeyEvent>() {
			@Override
		    public void handle(KeyEvent t) {
				switch(t.getCode()) {
					case ENTER:
						enterPressed();
					default:
						break;					
				}
			}
        };    
    	scene.addEventHandler(KeyEvent.KEY_PRESSED, handler);
	}
	
	// Removes the key handler from the scene, moves to the next view
	public void enterPressed() {		
		scene.removeEventHandler(KeyEvent.KEY_PRESSED, handler);
		warlordsController.showMultiplayerMenuView();
	}
	
	@FXML
	private Text title1;
	
	@FXML
	private Text title2;
	
	@FXML
	private Text title3;
	
	@FXML
	private Text title4;
	
	@FXML
	private Text line1;
	
	@FXML
	private Text line2;
	
	@FXML
	private Text line3;
	
	@FXML
	private Text line4;
	
	@FXML
	private Text line5;
	
	@FXML
	private Text line6;
	
	@FXML
	private Text line7;
	
	@FXML
	private Text line8;
	
	@FXML
	private Text line9;
	
	@FXML
	private Text line10;

	@FXML
	private Text backInstructions1;
	
	@FXML
	private Text backInstructions2;

}

