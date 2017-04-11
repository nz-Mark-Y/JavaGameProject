package warlords.view;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import warlords.WarlordsController;
import warlords.model.Game;

public class Story0ViewController {
	private WarlordsController warlordsController;
	private int isSelected;
	private ArrayList<Text> menuList = new ArrayList<Text>();
	private ArrayList<Label> labelList = new ArrayList<Label>();
	Scene scene;
	private EventHandler<KeyEvent> handler;
	private AudioClip menuScroller = new AudioClip(new File("sounds/menuScroller.wav").toURI().toString());
	
	public Story0ViewController() {
		
	}
	
	// Links the controller to the WarlordsController and scene
	public void setWarlordsController(WarlordsController warlordsController, Scene scene) {
		this.warlordsController = warlordsController;
		this.scene = scene;
		// Add menu items to controller
		menuList.add(title);
		menuList.add(skip);
		labelList.add(story1);
		labelList.add(story2);
		labelList.add(story3);
		labelList.add(story4);
		labelList.add(story5);
		labelList.add(story6);
		labelList.add(story7);
		labelList.add(story8);
		labelList.add(story9);
		labelList.add(story10);
		labelList.add(story11);
		
		loadText();

		isSelected = 0;	
		
		// Set custom fonts 
		Font myFont = null;
		Font titleFont = null;
		Font textFont = null;
        try {
            myFont = Font.loadFont(new FileInputStream(new File("Fonts/evanescent_p.ttf")), 48);
            titleFont = Font.loadFont(new FileInputStream(new File("Fonts/evanescent_p.ttf")), 96);
            textFont = Font.loadFont(new FileInputStream(new File("Fonts/evanescent_p.ttf")), 24);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        for (int i=0; i<menuList.size(); i++) {
        	menuList.get(i).setFont(myFont);
        }
        for(int i=0; i<labelList.size(); i++) {
			labelList.get(i).setFont(textFont);
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
	
	private void loadText() {
		switch(MainMenuViewController.level){
		case 0:
			title.setText("Introduction");
			story1.setText("-Commander, are you okay?");
			story2.setText("*You throw a confused look*");
			story3.setText("-It's me, Savi, your best friend! ");
			story4.setText("Savi: \t Why are you, the commander prodigy, spacing out for? \n\t Come on, there is an enemy attack, go to your battle station!");
			story5.setText("*You look even more confused*");
			story6.setText("Savi: \t Are you still dreaming? Hello, britain is at war! \n\t We're the only country that survived that weird zombie apocalypse. \n\t Don't you remember?");
			story7.setText("*You shake your head*");
			story8.setText("Savi: \t You seriously don't remember anything?");
			story9.setText("*You shake your head again*");
			story10.setText("Savi: \t Hmm, maybe going to fight might wake you up. \n\n\t All you have to do is protect the mothership [Top right corner] \n\t from the virus probe [Ball] by using your \n\t ship [Use A and D for movement, W for powerup]. \n\n\t Come on, we have an enemy attack to take care of!");
			break;
		case 1:
			title.setText("Chapter 1");
			story1.setText("-I see you are a worthy opponent however this won't be \n enough to defeat me, the legendary emperor Rob");
			story2.setText("Rob: \t You may have defeated my fleet today \n\t but know that this is your last victory. \n\t Enjoy this moment while you still \n\t can for none shall oppose Rob!");
			story3.setText("Savi: \t I don't think so, Rob. \n\t You see we have a secret weapon you don't have: \n\n *Savi points at you* \n\nSavi: \t This legendary commander! \n\t Not even you can defeat him.");
			story4.setText("Rob: \t Are you that sure in yourself? \n\t You see, I also have a secret weapon: \n\t I have kidnapped his family!");
			story5.setText("Savi: \t How cruel!");
			story6.setText("*You stare blankly*");
			story7.setText("Rob: \t Even his pet sheep is part of my hostage collection!");
			story8.setText("*You suddenly glare at Rob*");
			story9.setText("Rob: \t A-anyways, if you interfere with my plans \n\t of conquering the whole galaxy with my zombie \n\t army again, don't think I will be as nice.");
			story10.setText("Savi: \t We'll see about that.");
			break;
		case 2:
			title.setText("Chapter 2");
			story1.setText("-Urgent news commander: the recon team has been \n ambushed by enemy forces. What are your orders?");
			story2.setText("Savi: \t Where are they positioned?");
			story3.setText("-In enemy territory, sir! \n Their chances of survival, if we do not intervene, is zero!");
			story4.setText("Savi: \t Dog Gamete! We must help them! \n\t Commander, let's dispatch a rescue unit.");
			story5.setText("Savi: \t No, wait, that will spread out forces, \n\t we can barely afford to dispatch one ship.");
			story6.setText("*You stare at the ceiling*");
			story7.setText("Savi: \t That's it! If only us two go together, we won't \n\t compromise our forces and succeed in bringing them back!");
			story8.setText("Savi: \t Do be careful: they were stationed near where the \n\t apocalypse started, radiation levels may be unusually high.");
			story9.setText("Savi: \t If only the scientific experiment which caused all \n\t this had been stopped earlier...");
			story10.setText("Savi: \t Anyways, let's go rescue the recon team!");
			break;
		case 3:
			title.setText("Chapter 3");
			story1.setText("Savi: \t Dog Gamete, we were too late, \n\t not even a single bone remains..");
			story2.setText("Savi: \t If only we had arrived sooner...");
			story3.setText("Rob: \t Hahahahaha, I did warn you, my power is absolute!");
			story4.setText("Savi: \t YOU! HOW DARE YOU!");
			story5.setText("Rob: \t Oh boohoo, you lost your little friends.");
			story6.setText("Rob: \t I did warn you though: stop interfering with \n\t my plans, this is my last warning!");
			story7.setText("Rob: \t Otherwise I might have more 'creative' \n\t ideas for my hostages...");
			story8.setText("Savi: \t WHAT DID YOU SAY? I SWEAR IF YOU -");
			story9.setText("Rob: \t In any case, don't worry too much about it, \n\t you will soon be joining all of them!");
			story10.setText("Savi: \t Oh no, an ambush! \n\t Quick commander, let's go!");
			break;
		case 4:
			title.setText("Chapter 4");
			story1.setText("Savi: \t Well we made it through the ambush, lucky us! \n\t Huh, that's strange I can't contact HQ, \n\t I hope nothing bad's happened while we were gone.");
			story2.setText("*You arrive back at HQ* \n\n Savi: What? How? Why?");
			story3.setText("Rob: \t Guess who warned you? That's right me! \n\t If only you had listened to me and obeyed...");
			story4.setText("Savi: \t We would be dead if we had done that!");
			story5.setText("Rob: \t Heh, details. \n\t Anyways, I kidnapped your precious princess. \n\t Well, it's more like she's the only \n\t survivor of my surprise attack.");
			story6.setText("Savi: \t WHAT??? \n\t Why are you involving her? \n\t She has nothing to do with all this!");
			story7.setText("Rob: \t You do know that to ascend as a overlord, \n\t you need the blood of a princess, right?");
			story8.setText("Savi: \tWhat are you on about? \n\t These are just myths!");
			story9.setText("Rob: \t We'll see about that.");
			story10.setText("Savi: \t Commander, please go rescue the princess. \n\t I'll look for survivors here in the meantime.");
			break;
		case 5:
			title.setText("Chapter 5");
			story1.setText("Rob: \t You sure are a persitent one, aren't you? \n\t Coming in my base while your little friend calls \n\t for backup, not bad for a desperate strategy.");
			story2.setText("*You ignore Rob*");
			story3.setText("Rob: \t However, this won't be enough to kill me. \n\t You see, I have cloned myself and although \n\t this clone right here will die, I still have a spare one.\n\t It is even someone you knew all along.");
			story4.setText("*You raise an eyebrow*");
			story5.setText("Rob: \t You see, Savi is an excellent actor: \n\t drawing you away from HQ so I can attack it, \n\t giving you a poison erasing your memories... \n\t You don't seem very impressed. \n\t Let me help you with that...\n\n *Rob shoots the princess* \n\n Rob: Now that I made sure you failed in your mission, \n\t I can die in peace...");
			story6.setText("*Rob perishes due to his fatal injuries*");
			story7.setText("*You go back to HQ but can't find Savi*");
			story8.setText("-Commander, you're alright! \n-I bring the reinforcements requested. \n\n *You throw a confused look*");
			story9.setText("-It's me Gary! \n-Dispatched with my unit as per your request!");
			story10.setText("Gary: From what I understand from the situation, \n\t something is clear: we must find and stop Savi. \n\t I just hope the ascension Rob was \n\t talking about was a hoax...");
			break;
		case 6:
			title.setText("Chapter 6");
			story1.setText("Savi: \t I am getting really tired of you interfering, \n\t you know that? I should put those hostages to use, \n\t I need to be left alone for a while.");
			story2.setText("Gary: I don't think so Savi, we're here to stop you!");
			story3.setText("Savi: \t So this is your backup? \n\t I should have guessed it: it's pathetic.");
			story4.setText("Gary: Why should we leave you alone? \n\t You're trying to take over the world!");
			story5.setText("Savi: \t Actually, I'm trying to take over the galaxy, \n\t get your facts right. Also, I never said I needed the \n\t blood of the princess while she was alive...");
			story6.setText("Gary: You don't mean...");
			story7.setText("Savi: \t That's right I will ascend as an overlord \n\t after the summoning ritual succeeds. \n\t In the meantime, my army is sure to keep you busy.");
			story8.setText("Gary: Darn, we must get past them before he finishes the ritual.");
			story9.setText("Gary: On a positive note, we did manage to rescue the hostages.");
			story10.setText("Gary: However, we can't rest here, we must stop Savi!");
			break;
		case 7:
			title.setText("Chapter 7");
			story1.setText("Savi: \t Domine deus meus in te speravi conlitebor \n\t tibi domine in toto corde meo quem ad modum desiderat \n\t cervus ad fantes aquarum. Domine meus Lucifer!");
			story2.setText("Gary: Not so fast, Savi!");
			story3.setText("Savi: \t You are getting on my nerves \n\t like no one has ever been. It's about time I payed \n\t you back for all the pain you've given me: \n\t Goodbye hostages!");
			story4.setText("*Savi shoots all your family members*");
			story5.setText("Savi: \t Hahahahaha, I warned you, get in my way, \n\t pay the price! I'll let you keep your \n\t pet sheep as a memento.");
			story6.setText("Gary: HOW COULD YOU, YOU EVIL BEING!");
			story7.setText("Savi: \t Me, evil? Am I hearing that correctly? \n\t Is the empire that eradicated over 300 races \n\t really fit of accusing me of being evil? \n\t Makes you wonder, what's worse, \n\t a few sacrifices or 300 genocides...");
			story8.setText("Gary: But that was different!");
			story9.setText("Savi: \t How so? By saying it was for 'safety'? \n\t You just can't face reality, you are too weak to do so! \n\t In any case, you are too late, I have fully ascended. \n\t All shall bow to Savi the Dark Overlord!");
			story10.setText("Gary: We won't fail here! \n\t Let's go commander, \n\t let's finish this once and for all!");
			break;
		case 8:
			title.setText("Epilogue");
			story1.setText("Gary: Finally, Savi has been defeated and all \n\t the zombies in the base have been eradicated.");
			story2.setText("Gary: However, there is still work to be done, \n\t so many zombies remain...");
			story3.setText("...");
			story4.setText("*You go to your Savi's corpse*");
			story5.setText("[Press F to pay respects]");
			story6.setText("...");
			story7.setText("Congratulation on completing the free version!");
			story8.setText("To access the full version, please buy the DLC.");
			story9.setText("...");
			story10.setText("Created by Mark Yep and Sylvain Bechet.");
			break;
			
		}
		story11.setText("Press ENTER to continue.");
	}

	// Refresh the selection menu
	public void updateSelection() {
		for (int i=0; i<labelList.size(); i++) {
			if (i == isSelected) {
				labelList.get(i).setVisible(true); // Selected item shown in white
				labelList.get(i).setTextFill(Color.valueOf("#FFFFFF"));
			} else {
				labelList.get(i).setVisible(false); // Unselected is hidden
			}			
		}	
	}
	
	// If the user presses the up arrow
	public void upArrowPressed() {
		if (isSelected == 0) {
			isSelected = labelList.size()-1;
		} else {
			isSelected--;
		}
	}
	
	// If the user presses the down arrow
	public void downArrowPressed() {
		if (isSelected == labelList.size()-1) {
			isSelected = 0;
		} else {
			isSelected++;
		}
	}
	
	// Removes the key handler from the scene, moves to the next view
	public void enterPressed() {
		//Load game
		switch(MainMenuViewController.level){
		case 0:
			scene.removeEventHandler(KeyEvent.KEY_PRESSED, handler);
			warlordsController.createNewGame(0, false, 0, true, 0, true, 0, true);
			Game.randomness = 0;
			Game.reverseControls = false;
			GameViewController.gameSpeed = 18;
			warlordsController.showGameView();//Creates introduction level with settings specified in this case
			break;
		case 1:
			scene.removeEventHandler(KeyEvent.KEY_PRESSED, handler);
			warlordsController.createNewGame(2, false, 0, true, 0, true, 0, true);
			Game.randomness = 0;
			Game.reverseControls = false;
			GameViewController.gameSpeed = 15;
			warlordsController.showGameView(); //Creates level 1 with settings specified in this case
			break;
		case 2:
			scene.removeEventHandler(KeyEvent.KEY_PRESSED, handler);
			warlordsController.createNewGame(3, false, 0, true, 0, true, 0, true);
			Game.randomness = 1;
			Game.reverseControls = false;
			GameViewController.gameSpeed = 15;
			warlordsController.showGameView(); //Creates level 2 with settings specified in this case
			break;
		case 3:
			scene.removeEventHandler(KeyEvent.KEY_PRESSED, handler);
			warlordsController.createNewGame(4, false, 0, true, 0, true, 0, true);
			Game.randomness = 1;
			Game.reverseControls = false;
			GameViewController.gameSpeed = 12;
			warlordsController.showGameView(); //Creates level 3 with settings specified in this case
			break;
		case 4:
			scene.removeEventHandler(KeyEvent.KEY_PRESSED, handler);
			warlordsController.createNewGame(5, false, 0, true, 0, true, 0, true);
			Game.randomness = 2;
			Game.reverseControls = false;
			GameViewController.gameSpeed = 12;
			warlordsController.showGameView(); //Creates level 4 with settings specified in this case
			break;
		case 5:
			scene.removeEventHandler(KeyEvent.KEY_PRESSED, handler);
			warlordsController.createNewGame(6, false, 0, true, 0, true, 0, true);
			Game.randomness = 2;
			Game.reverseControls = false;
			GameViewController.gameSpeed = 9;
			warlordsController.showGameView(); //Creates level 5 with settings specified in this case
			break;
		case 6:
			scene.removeEventHandler(KeyEvent.KEY_PRESSED, handler);
			warlordsController.createNewGame(7, false, 0, true, 0, true, 0, true);
			Game.randomness = 2;
			Game.reverseControls = false;
			GameViewController.gameSpeed = 6;
			warlordsController.showGameView(); ///Creates level 6 with settings specified in this case
			break;
		case 7:
			scene.removeEventHandler(KeyEvent.KEY_PRESSED, handler);
			warlordsController.createNewGame(8, false, 0, true, 0, true, 0, true);
			Game.randomness = 2;
			Game.reverseControls = true;
			GameViewController.gameSpeed = 6;
			warlordsController.showGameView(); //Creates level 7 with settings specified in this case
			break;
		default:
			scene.removeEventHandler(KeyEvent.KEY_PRESSED, handler);
			warlordsController.showMainMenu();
			break;
		}
	}
	
	// Text
	@FXML
	private Text title;
	@FXML
	private Text skip;
	@FXML
	private Label story1;
	@FXML
	private Label story2;
	@FXML
	private Label story3;
	@FXML
	private Label story4;
	@FXML
	private Label story5;
	@FXML
	private Label story6;
	@FXML
	private Label story7;
	@FXML
	private Label story8;
	@FXML
	private Label story9;
	@FXML
	private Label story10;
	@FXML
	private Label story11;
}
