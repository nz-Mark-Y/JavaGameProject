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

public class AboutViewController {
	private WarlordsController warlordsController;
	private ArrayList<Text> menuList = new ArrayList<Text>();
	Scene scene;
	private EventHandler<KeyEvent> handler;
	
	public AboutViewController() {
		
	}
	
	// Sets the controller and scene
	public void setWarlordsController(WarlordsController warlordsController, Scene scene) {
		this.warlordsController = warlordsController;
		this.scene = scene;
		// Add menu items to controller
		menuList.add(line1);
		menuList.add(line2);
		menuList.add(line3);
		menuList.add(line4);
		menuList.add(line5);
		menuList.add(line6);
		
		// Set custom fonts 
		Font myFont = null;
		Font titleFont = null;
		Font backFont = null;
        try {
            myFont = Font.loadFont(new FileInputStream(new File("Fonts/evanescent_p.ttf")), 36);
            titleFont = Font.loadFont(new FileInputStream(new File("Fonts/evanescent_p.ttf")), 96);
            backFont = Font.loadFont(new FileInputStream(new File("Fonts/evanescent_p.ttf")), 84);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        for (int i=0; i<menuList.size(); i++) {
        	menuList.get(i).setFont(myFont);
        }
        title.setFont(titleFont);
        back.setFont(backFont);
        
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
		warlordsController.showMainMenu();
	}
	
	@FXML
	private Text title;
	
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
	private Text back;
}

