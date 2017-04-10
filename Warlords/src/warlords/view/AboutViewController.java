package warlords.view;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
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
	
	// Links the controller to the WarlordsController and scene
	public void setWarlordsController(WarlordsController warlordsController, Scene scene) {
		this.warlordsController = warlordsController;
		this.scene = scene;
		// Add menu items to controller
		menuList.add(aboutLine1);
		menuList.add(aboutLine2);
		menuList.add(aboutLine3);
		menuList.add(aboutLine4);
		menuList.add(aboutLine5);
		menuList.add(aboutLine6);
		
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
	
	// Text
	@FXML
	private Text title;
	@FXML
	private Text aboutLine1;
	@FXML
	private Text aboutLine2;
	@FXML
	private Text aboutLine3;
	@FXML
	private Text aboutLine4;
	@FXML
	private Text aboutLine5;
	@FXML
	private Text aboutLine6;
	@FXML
	private Text back;
}

