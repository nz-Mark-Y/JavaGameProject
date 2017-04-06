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
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import warlords.WarlordsController;

public class PlayerMenuViewController {
	private WarlordsController warlordsController;
	private ArrayList<Text> textList = new ArrayList<Text>();
	private ArrayList<Text> titleList = new ArrayList<Text>();
	private ArrayList<Rectangle> player1List = new ArrayList<Rectangle>();
	private ArrayList<Rectangle> player2List = new ArrayList<Rectangle>();
	private ArrayList<Rectangle> player3List = new ArrayList<Rectangle>();
	private ArrayList<Rectangle> player4List = new ArrayList<Rectangle>();
	Scene scene;
	private EventHandler<KeyEvent> handler;
	private int oneSelects;
	private boolean oneIsAI;
	private int twoSelects;
	private boolean twoIsAI;
	private int threeSelects;
	private boolean threeIsAI;
	private int fourSelects;
	private boolean fourIsAI;
	
	public PlayerMenuViewController() {
		
	}
	
	// Sets the controller and scene
	public void setWarlordsController(WarlordsController warlordsController, Scene scene) {
		this.warlordsController = warlordsController;
		this.scene = scene;
		
		// Add items to controller
		addItems();
		
		// Set custom fonts 
		setFonts();
        
        // KeyHandlers
		addHandlers();
	}
	
	public void setFonts() {
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
	}
	
	public void addHandlers() {
		oneSelects = 0;
        oneIsAI = false;
    	twoSelects = 0;
    	twoIsAI = false;
    	threeSelects = 0;
    	threeIsAI = false;
    	fourSelects = 0;
    	fourIsAI = false;
    	
    	updateSelection();
        // Create a key handler for the scene
        handler = new EventHandler<KeyEvent>() {
			@Override
		    public void handle(KeyEvent t) {
				switch(t.getCode()) {
					case LEFT:
						if (threeSelects == 0) {
							threeSelects = 9;
						} else {
							threeSelects--;
						}
						break;
					case RIGHT:
						if (threeSelects == 9) {
							threeSelects = 0;
						} else {
							threeSelects++;
						}
						break;
					case UP:
						if (threeIsAI) {
							threeIsAI = false;
						} else {
							threeIsAI = true;
						}
						break;
					case A:
						if (oneSelects == 0) {
							oneSelects = 9;
						} else {
							oneSelects--;
						}
						break;
					case D:
						if (oneSelects == 9) {
							oneSelects = 0;
						} else {
							oneSelects++;
						}
						break;
					case W:
						if (oneIsAI) {
							oneIsAI = false;
						} else {
							oneIsAI = true;
						}
						break;
					case J:
						if (twoSelects == 0) {
							twoSelects = 9;
						} else {
							twoSelects--;
						}
						break;
					case L:
						if (twoSelects == 9) {
							twoSelects = 0;
						} else {
							twoSelects++;
						}
						break;
					case I:
						if (twoIsAI) {
							twoIsAI = false;
						} else {
							twoIsAI = true;
						}
						break;
					case NUMPAD3:
						if (fourSelects == 0) {
							fourSelects = 9;
						} else {
							fourSelects--;
						}
						break;
					case NUMPAD9:
						if (fourSelects == 9) {
							fourSelects = 0;
						} else {
							fourSelects++;
						}
						break;
					case NUMPAD5:
						if (fourIsAI) {
							fourIsAI = false;
						} else {
							fourIsAI = true;
						}
						break;
					case ENTER:
						enterPressed();
						break;
					default:
						break;
				}
				updateSelection();
			}
        };    
    	scene.addEventHandler(KeyEvent.KEY_PRESSED, handler);
	}
	
	public void updateSelection() {
		for (int i=0;i<player1List.size(); i++) {
			if (i == oneSelects) {
				player1List.get(i).setVisible(true);
			} else {
				player1List.get(i).setVisible(false);
			}
		}
		for (int i=0;i<player2List.size(); i++) {
			if (i == twoSelects) {
				player2List.get(i).setVisible(true);
			} else {
				player2List.get(i).setVisible(false);
			}
		}
	}
	
	// Removes the key handler from the scene, moves to the next view
	public void enterPressed() {		
		scene.removeEventHandler(KeyEvent.KEY_PRESSED, handler);
		warlordsController.showMultiplayerMenuView();
	}
	
	public void addItems() {
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
		textList.add(backInstructions1);
		textList.add(backInstructions2);
		player1List.add(player1Select0);
		player1List.add(player1Select1);
		player1List.add(player1Select2);
		player1List.add(player1Select3);
		player1List.add(player1Select4);
		player1List.add(player1Select5);
		player1List.add(player1Select6);
		player1List.add(player1Select7);
		player1List.add(player1Select8);
		player1List.add(player1Select9);
		player2List.add(player2Select0);
		player2List.add(player2Select1);
		player2List.add(player2Select2);
		player2List.add(player2Select3);
		player2List.add(player2Select4);
		player2List.add(player2Select5);
		player2List.add(player2Select6);
		player2List.add(player2Select7);
		player2List.add(player2Select8);
		player2List.add(player2Select9);
		player3List.add(player3Select0);
		player3List.add(player3Select1);
		player3List.add(player3Select2);
		player3List.add(player3Select3);
		player3List.add(player3Select4);
		player3List.add(player3Select5);
		player3List.add(player3Select6);
		player3List.add(player3Select7);
		player3List.add(player3Select8);
		player3List.add(player3Select9);
		player4List.add(player4Select0);
		player4List.add(player4Select1);
		player4List.add(player4Select2);
		player4List.add(player4Select3);
		player4List.add(player4Select4);
		player4List.add(player4Select5);
		player4List.add(player4Select6);
		player4List.add(player4Select7);
		player4List.add(player4Select8);
		player4List.add(player4Select9);
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
	private Text backInstructions1;
	
	@FXML
	private Text backInstructions2;
	
	@FXML
	private Rectangle player1Select0;
	
	@FXML
	private Rectangle player1Select1;
	
	@FXML
	private Rectangle player1Select2;
	
	@FXML
	private Rectangle player1Select3;
	
	@FXML
	private Rectangle player1Select4;
	
	@FXML
	private Rectangle player1Select5;
	
	@FXML
	private Rectangle player1Select6;
	
	@FXML
	private Rectangle player1Select7;
	
	@FXML
	private Rectangle player1Select8;
	
	@FXML
	private Rectangle player1Select9;
	
	@FXML
	private Rectangle player2Select0;
	
	@FXML
	private Rectangle player2Select1;
	
	@FXML
	private Rectangle player2Select2;
	
	@FXML
	private Rectangle player2Select3;
	
	@FXML
	private Rectangle player2Select4;
	
	@FXML
	private Rectangle player2Select5;
	
	@FXML
	private Rectangle player2Select6;
	
	@FXML
	private Rectangle player2Select7;
	
	@FXML
	private Rectangle player2Select8;
	
	@FXML
	private Rectangle player2Select9;
	
	@FXML
	private Rectangle player3Select0;
	
	@FXML
	private Rectangle player3Select1;
	
	@FXML
	private Rectangle player3Select2;
	
	@FXML
	private Rectangle player3Select3;
	
	@FXML
	private Rectangle player3Select4;
	
	@FXML
	private Rectangle player3Select5;
	
	@FXML
	private Rectangle player3Select6;
	
	@FXML
	private Rectangle player3Select7;
	
	@FXML
	private Rectangle player3Select8;
	
	@FXML
	private Rectangle player3Select9;
	
	@FXML
	private Rectangle player4Select0;
	
	@FXML
	private Rectangle player4Select1;
	
	@FXML
	private Rectangle player4Select2;
	
	@FXML
	private Rectangle player4Select3;
	
	@FXML
	private Rectangle player4Select4;
	
	@FXML
	private Rectangle player4Select5;
	
	@FXML
	private Rectangle player4Select6;
	
	@FXML
	private Rectangle player4Select7;
	
	@FXML
	private Rectangle player4Select8;
	
	@FXML
	private Rectangle player4Select9;

}

