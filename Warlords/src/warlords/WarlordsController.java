package warlords;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.media.AudioClip;
import javafx.stage.Stage;
import warlords.model.Ball;
import warlords.model.Game;
import warlords.model.Paddle;
import warlords.model.Wall;
import warlords.model.Warlord;
import warlords.view.AboutViewController;
import warlords.view.CampaignMenuViewController;
import warlords.view.GameViewController;
import warlords.view.LeftSideViewController;
import warlords.view.MainMenuViewController;
import warlords.view.MultiplayerMenuViewController;
import warlords.view.OptionsViewController;
import warlords.view.PlayerMenuViewController;
import warlords.view.RightSideViewController;

public class WarlordsController extends Application {
	private Stage primaryStage;
	private BorderPane rootLayout;
	private Game game = null;
	Scene scene;
	private AudioClip mainMenuTheme = new AudioClip(new File("sounds/mainMenuTheme.wav").toURI().toString());
	private AudioClip menuSelection = new AudioClip(new File("sounds/menuSelection.wav").toURI().toString());

	public WarlordsController() {
		
	}
	
	// Set up the objects in a new game
	public void createNewGame(int oneSelects, boolean oneIsAI, int twoSelects, boolean twoIsAI, int threeSelects, boolean threeIsAI, int fourSelects, boolean fourIsAI) {
		Ball ball = new Ball(350, 350);
		Warlord player1 = new Warlord(new Paddle(181, 547), 40, 678, oneIsAI, oneSelects);	
		Warlord player2 = new Warlord(new Paddle(181, 181), 40, 40, twoIsAI, twoSelects);
		Warlord player3 = new Warlord(new Paddle(547, 181), 678, 40, threeIsAI, threeSelects);
		Warlord player4 = new Warlord(new Paddle(547, 547), 678, 678, fourIsAI, fourSelects);

		ArrayList<Warlord> playerList = new ArrayList<Warlord>();
		playerList.add(player1);
		playerList.add(player2);
		playerList.add(player3);
		playerList.add(player4);
		Wall wall11 = new Wall(0, 175, 0);
		Wall wall12 = new Wall(35, 175, 0);
		Wall wall13 = new Wall(70, 140, 0);
		Wall wall14 = new Wall(105, 140, 0);
		Wall wall15 = new Wall(140, 105, 0);
		Wall wall16 = new Wall(140, 70, 0);
		Wall wall17 = new Wall(175, 35, 0);
		Wall wall18 = new Wall(175, 0, 0);
		Wall wall19 = new Wall(35, 140, 0);
		Wall wall110 = new Wall(140, 35, 0);
		Wall wall111 = new Wall(140, 140, 0);
		Wall wall112 = new Wall(105, 105, 0);
		Wall wall113 = new Wall(0, 210, 0);
		Wall wall114 = new Wall(70, 175, 0);
		Wall wall115 = new Wall(175, 70, 0);
		Wall wall116 = new Wall(210, 0, 0);
		Wall wall2 = new Wall(668, 618, 1);
		Wall wall3 = new Wall(100, 618, 2);
		Wall wall4 = new Wall(668, 200, 3);
		ArrayList<Wall> wallList = new ArrayList<Wall>();
		wallList.add(wall11);
		wallList.add(wall12);
		wallList.add(wall13);
		wallList.add(wall14);
		wallList.add(wall15);
		wallList.add(wall16);
		wallList.add(wall17);
		wallList.add(wall18);
		wallList.add(wall19);
		wallList.add(wall110);
		wallList.add(wall111);
		wallList.add(wall112);
		wallList.add(wall113);
		wallList.add(wall114);
		wallList.add(wall115);
		wallList.add(wall116);
		wallList.add(wall2);
		wallList.add(wall3);
		wallList.add(wall4);
		game = new Game(ball, 763, 763, playerList, wallList);		
	}
	
	@Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("Warlords");	
		
		initRootLayout();
		showMainMenu();
	}

	public static void main(String[] args) {
		launch(args);
	}
	
	// Create the root window of the game
	public void initRootLayout() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(WarlordsController.class.getResource("view/RootLayout.fxml"));
			rootLayout = (BorderPane) loader.load();

			scene  = new Scene(rootLayout);

			primaryStage.setScene(scene);
			primaryStage.show();		
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
	
	// Display the game view, and the two side windows 
	public void showGameView() {
		if (game == null) {
			createNewGame(0, true, 0, true, 1, false, 0 ,true);
		}
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(WarlordsController.class.getResource("view/GameView.fxml"));
			AnchorPane gameView = (AnchorPane) loader.load();

			FXMLLoader loader2 = new FXMLLoader();
			loader2.setLocation(WarlordsController.class.getResource("view/RightSideView.fxml"));
			AnchorPane rightSideView = (AnchorPane) loader2.load();

			FXMLLoader loader3 = new FXMLLoader();
			loader3.setLocation(WarlordsController.class.getResource("view/LeftSideView.fxml"));
			AnchorPane leftSideView = (AnchorPane) loader3.load();

			rootLayout.setCenter(gameView);
			rootLayout.setRight(rightSideView);
			rootLayout.setLeft(leftSideView);

			GameViewController controller = loader.getController();
			controller.setWarlordsController(this, scene);

			RightSideViewController controller2 = loader2.getController();
			controller2.setWarlordsController(this);

			LeftSideViewController controller3 = loader3.getController();
			controller3.setWarlordsController(this);
			
			menuSelection.play();
			
			//Stop playing the main theme if playing
			if(mainMenuTheme.isPlaying()){
				mainMenuTheme.stop();
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	// Display the main menu, and two black side windows
	public void showMainMenu() {
		try {
			game = null;
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(WarlordsController.class.getResource("view/MainMenuView.fxml"));
			AnchorPane menuView = (AnchorPane) loader.load();	
			
			FXMLLoader loader2 = new FXMLLoader();
			loader2.setLocation(WarlordsController.class.getResource("view/BlackSideView.fxml"));
			AnchorPane leftSideView = (AnchorPane) loader2.load();
			
			FXMLLoader loader3 = new FXMLLoader();
			loader3.setLocation(WarlordsController.class.getResource("view/BlackSideView.fxml"));
			AnchorPane rightSideView = (AnchorPane) loader3.load();
			
			rootLayout.setCenter(menuView);
			rootLayout.setLeft(leftSideView);
			rootLayout.setRight(rightSideView);
			
			MainMenuViewController controller = loader.getController();
			controller.setWarlordsController(this, scene);
			
			//Play the main theme once
			if(!mainMenuTheme.isPlaying()){
				mainMenuTheme.play();
			} else {
				menuSelection.play();
			}
			
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
	
	// Display the campaign menu
	public void showCampaignMenuView() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(WarlordsController.class.getResource("view/CampaignMenuView.fxml"));
			AnchorPane campaignMenuView = (AnchorPane) loader.load();	
			
			rootLayout.setCenter(campaignMenuView);
			
			CampaignMenuViewController controller = loader.getController();
			controller.setWarlordsController(this, scene);
			
			menuSelection.play();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
	
	// Display the multiplayer menu
	public void showMultiplayerMenuView() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(WarlordsController.class.getResource("view/MultiplayerMenuView.fxml"));
			AnchorPane multiplayerMenuView = (AnchorPane) loader.load();	
				
			rootLayout.setCenter(multiplayerMenuView);
				
			MultiplayerMenuViewController controller = loader.getController();
			controller.setWarlordsController(this, scene);
			
			menuSelection.play();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
	
	// Display the options
	public void showOptionsView() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(WarlordsController.class.getResource("view/OptionsView.fxml"));
			AnchorPane optionsView = (AnchorPane) loader.load();	
					
			rootLayout.setCenter(optionsView);
					
			OptionsViewController controller = loader.getController();
			controller.setWarlordsController(this, scene);
			
			menuSelection.play();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
	
	// Display the about screen
	public void showAboutView() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(WarlordsController.class.getResource("view/AboutView.fxml"));
			AnchorPane aboutView = (AnchorPane) loader.load();	
						
			rootLayout.setCenter(aboutView);
						
			AboutViewController controller = loader.getController();
			controller.setWarlordsController(this, scene);
			
			menuSelection.play();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
	
	// Display the multiplayer player select screen
		public void showPlayerMenuView() {
			try {
				FXMLLoader loader = new FXMLLoader();
				loader.setLocation(WarlordsController.class.getResource("view/PlayerMenuView.fxml"));
				AnchorPane playerMenuView = (AnchorPane) loader.load();	
							
				rootLayout.setCenter(playerMenuView);
							
				PlayerMenuViewController controller = loader.getController();
				controller.setWarlordsController(this, scene);
				
				menuSelection.play();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}

	public Stage getPrimaryStage() {
		return primaryStage;
	}	

	public Game getGame() {
		return game;
	}

	// Terminate the application when window is closed
	@Override
	public void stop(){
		System.out.println("Application terminated successfully.");
		System.exit(0);
	}
}
