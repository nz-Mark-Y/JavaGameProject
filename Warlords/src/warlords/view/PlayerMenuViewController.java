package warlords.view;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import warlords.WarlordsController;

public class PlayerMenuViewController {
	private WarlordsController warlordsController;
	private ArrayList<Text> textList = new ArrayList<Text>();
	private ArrayList<Text> titleList = new ArrayList<Text>();
	private ArrayList<Label> labelList = new ArrayList<Label>();
	private ArrayList<Rectangle> player1List = new ArrayList<Rectangle>();
	private ArrayList<Rectangle> player2List = new ArrayList<Rectangle>();
	private ArrayList<Rectangle> player3List = new ArrayList<Rectangle>();
	private ArrayList<Rectangle> player4List = new ArrayList<Rectangle>();
	private ArrayList<String> descriptions = new ArrayList<String>();
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

		// Add Descriptions
		addDescriptions();
		
		// Add items to controller
		addItems();

		// Set custom fonts 
		setFonts();

		// Apply patterns
		setPatterns();

		// KeyHandlers
		addHandlers();
	}
	
	public ImagePattern getPlayerFlag(int playerNumber){
		//TO IMPLEMENT	
		return null;	
	}

	public void setPatterns(){
		//Applying flag patterns
		flag0.setFill(flag0Pattern);
		flag1.setFill(flag1Pattern);
		flag2.setFill(flag2Pattern);
		flag3.setFill(flag3Pattern);
		flag4.setFill(flag4Pattern);
		flag5.setFill(flag5Pattern);
		flag6.setFill(flag6Pattern);
		flag7.setFill(flag7Pattern);
		flag8.setFill(flag8Pattern);
		flag9.setFill(flag9Pattern);
		
		//Applying cursor patterns
		player1Select0.setStrokeWidth(0);
		player1Select0.setFill(player1ShipPattern);
		player1Select1.setStrokeWidth(0);
		player1Select1.setFill(player1ShipPattern);
		player1Select2.setStrokeWidth(0);
		player1Select2.setFill(player1ShipPattern);
		player1Select3.setStrokeWidth(0);
		player1Select3.setFill(player1ShipPattern);
		player1Select4.setStrokeWidth(0);
		player1Select4.setFill(player1ShipPattern);
		player1Select5.setStrokeWidth(0);
		player1Select5.setFill(player1ShipPattern);
		player1Select6.setStrokeWidth(0);
		player1Select6.setFill(player1ShipPattern);
		player1Select7.setStrokeWidth(0);
		player1Select7.setFill(player1ShipPattern);
		player1Select8.setStrokeWidth(0);
		player1Select8.setFill(player1ShipPattern);
		player1Select9.setStrokeWidth(0);
		player1Select9.setFill(player1ShipPattern);
		
		player2Select0.setStrokeWidth(0);
		player2Select0.setFill(player2ShipPattern);
		player2Select1.setStrokeWidth(0);
		player2Select1.setFill(player2ShipPattern);
		player2Select2.setStrokeWidth(0);
		player2Select2.setFill(player2ShipPattern);
		player2Select3.setStrokeWidth(0);
		player2Select3.setFill(player2ShipPattern);
		player2Select4.setStrokeWidth(0);
		player2Select4.setFill(player2ShipPattern);
		player2Select5.setStrokeWidth(0);
		player2Select5.setFill(player2ShipPattern);
		player2Select6.setStrokeWidth(0);
		player2Select6.setFill(player2ShipPattern);
		player2Select7.setStrokeWidth(0);
		player2Select7.setFill(player2ShipPattern);
		player2Select8.setStrokeWidth(0);
		player2Select8.setFill(player2ShipPattern);
		player2Select9.setStrokeWidth(0);
		player2Select9.setFill(player2ShipPattern);
		
		player3Select0.setStrokeWidth(0);
		player3Select0.setFill(player3ShipPattern);
		player3Select1.setStrokeWidth(0);
		player3Select1.setFill(player3ShipPattern);
		player3Select2.setStrokeWidth(0);
		player3Select2.setFill(player3ShipPattern);
		player3Select3.setStrokeWidth(0);
		player3Select3.setFill(player3ShipPattern);
		player3Select4.setStrokeWidth(0);
		player3Select4.setFill(player3ShipPattern);
		player3Select5.setStrokeWidth(0);
		player3Select5.setFill(player3ShipPattern);
		player3Select6.setStrokeWidth(0);
		player3Select6.setFill(player3ShipPattern);
		player3Select7.setStrokeWidth(0);
		player3Select7.setFill(player3ShipPattern);
		player3Select8.setStrokeWidth(0);
		player3Select8.setFill(player3ShipPattern);
		player3Select9.setStrokeWidth(0);
		player3Select9.setFill(player3ShipPattern);
		
		player4Select0.setStrokeWidth(0);
		player4Select0.setFill(player4ShipPattern);
		player4Select1.setStrokeWidth(0);
		player4Select1.setFill(player4ShipPattern);
		player4Select2.setStrokeWidth(0);
		player4Select2.setFill(player4ShipPattern);
		player4Select3.setStrokeWidth(0);
		player4Select3.setFill(player4ShipPattern);
		player4Select4.setStrokeWidth(0);
		player4Select4.setFill(player4ShipPattern);
		player4Select5.setStrokeWidth(0);
		player4Select5.setFill(player4ShipPattern);
		player4Select6.setStrokeWidth(0);
		player4Select6.setFill(player4ShipPattern);
		player4Select7.setStrokeWidth(0);
		player4Select7.setFill(player4ShipPattern);
		player4Select8.setStrokeWidth(0);
		player4Select8.setFill(player4ShipPattern);
		player4Select9.setStrokeWidth(0);
		player4Select9.setFill(player4ShipPattern);
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
		for(int i=0; i<labelList.size(); i++) {
			labelList.get(i).setFont(textFont);
		}
	}

	// Removes the key handler from the scene, moves to the next view
	public void enterPressed() {		
		scene.removeEventHandler(KeyEvent.KEY_PRESSED, handler);
		warlordsController.createNewGame(oneSelects, oneIsAI, twoSelects, twoIsAI, threeSelects, threeIsAI, fourSelects, fourIsAI);
		warlordsController.showMultiplayerMenuView();
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
		description1.setText(descriptions.get(oneSelects));
		description2.setText(descriptions.get(twoSelects));
		description3.setText(descriptions.get(threeSelects));
		description4.setText(descriptions.get(fourSelects));
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
		for (int i=0;i<player3List.size(); i++) {
			if (i == threeSelects) {
				player3List.get(i).setVisible(true);
			} else {
				player3List.get(i).setVisible(false);
			}
		}
		for (int i=0;i<player4List.size(); i++) {
			if (i == fourSelects) {
				player4List.get(i).setVisible(true);
			} else {
				player4List.get(i).setVisible(false);
			}
		}
		if (oneIsAI) {
			title5.setVisible(true);
		} else {
			title5.setVisible(false);
		}
		if (twoIsAI) {
			title6.setVisible(true);
		} else {
			title6.setVisible(false);
		}
		if (threeIsAI) {
			title7.setVisible(true);
		} else {
			title7.setVisible(false);
		}
		if (fourIsAI) {
			title8.setVisible(true);
		} else {
			title8.setVisible(false);
		}
	}

	public void addItems() {
		titleList.add(title1);
		titleList.add(title2);
		titleList.add(title3);
		titleList.add(title4);
		titleList.add(title5);
		titleList.add(title6);
		titleList.add(title7);
		titleList.add(title8);
		textList.add(line1);
		textList.add(line2);
		textList.add(line3);
		textList.add(line4);
		textList.add(line5);
		textList.add(line6);
		textList.add(line7);
		textList.add(line8);
		textList.add(backInstructions1);
		labelList.add(description1);
		labelList.add(description2);
		labelList.add(description3);
		labelList.add(description4);
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

	public void addDescriptions() {
		descriptions.add("france: \n To Be Implemented");
		descriptions.add("USA: \n Shoots a single use bullet using your UP key \n Bullet will destroy a wall or warlord if it hits one, but does not bounce");
		descriptions.add("Britain: \n To Be Implemented");
		descriptions.add("New Zealand: \n To Be Implemented");
		descriptions.add("China: \n To Be Implemented");
		descriptions.add("Australia: \n To Be Implemented");
		descriptions.add("India: \n To Be Implemented");
		descriptions.add("Russia: \n To Be Implemented");
		descriptions.add("Egypt: \n To Be Implemented");
		descriptions.add("Brazil: \n To Be Implemented");
	}
	
	@FXML
	private Label description1;
	
	@FXML
	private Label description2;
	
	@FXML
	private Label description3;
	
	@FXML
	private Label description4;
	
	@FXML
	private Text title1;

	@FXML
	private Text title2;

	@FXML
	private Text title3;

	@FXML
	private Text title4;

	@FXML
	private Text title5;

	@FXML
	private Text title6;

	@FXML
	private Text title7;

	@FXML
	private Text title8;

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

	@FXML
	private Rectangle flag0;

	@FXML
	private Rectangle flag1;

	@FXML
	private Rectangle flag2;

	@FXML
	private Rectangle flag3;

	@FXML
	private Rectangle flag4;

	@FXML
	private Rectangle flag5;

	@FXML
	private Rectangle flag6;

	@FXML
	private Rectangle flag7;

	@FXML
	private Rectangle flag8;

	@FXML
	private Rectangle flag9;

	//Creating patterns for the flags
	private Image flag0Image = new Image("file:images/fr.png");
	ImagePattern flag0Pattern = new ImagePattern(flag0Image);

	private Image flag1Image = new Image("file:images/us.png");
	ImagePattern flag1Pattern = new ImagePattern(flag1Image);

	private Image flag2Image = new Image("file:images/gb.png");
	ImagePattern flag2Pattern = new ImagePattern(flag2Image);

	private Image flag3Image = new Image("file:images/nz.png");
	ImagePattern flag3Pattern = new ImagePattern(flag3Image);

	private Image flag4Image = new Image("file:images/cn.png");
	ImagePattern flag4Pattern = new ImagePattern(flag4Image);

	private Image flag5Image = new Image("file:images/au.png");
	ImagePattern flag5Pattern = new ImagePattern(flag5Image);

	private Image flag6Image = new Image("file:images/in.png");
	ImagePattern flag6Pattern = new ImagePattern(flag6Image);

	private Image flag7Image = new Image("file:images/ru.png");
	ImagePattern flag7Pattern = new ImagePattern(flag7Image);

	private Image flag8Image = new Image("file:images/eg.png");
	ImagePattern flag8Pattern = new ImagePattern(flag8Image);

	private Image flag9Image = new Image("file:images/br.png");
	ImagePattern flag9Pattern = new ImagePattern(flag9Image);

	//Creating patterns for the paddles
	private Image player1Ship = new Image("file:images/player1Ship.png");
	ImagePattern player1ShipPattern = new ImagePattern(player1Ship);

	private Image player2Ship = new Image("file:images/player2Ship.png");
	ImagePattern player2ShipPattern = new ImagePattern(player2Ship);

	private Image player3Ship = new Image("file:images/player3Ship.png");
	ImagePattern player3ShipPattern = new ImagePattern(player3Ship);

	private Image player4Ship = new Image("file:images/player4Ship.png");
	ImagePattern player4ShipPattern = new ImagePattern(player4Ship);
}

