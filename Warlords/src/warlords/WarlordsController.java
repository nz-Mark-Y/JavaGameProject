package warlords;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javafx.application.Application;
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
import warlords.view.MainMenuViewController;
import warlords.view.MultiplayerMenuViewController;
import warlords.view.MutatorsViewController;
import warlords.view.OptionsViewController;
import warlords.view.PlayerMenuViewController;

public class WarlordsController extends Application {
	private Stage primaryStage;
	private BorderPane rootLayout;
	private Game game = null;
	Scene scene;
	private AudioClip mainMenuTheme = new AudioClip(new File("sounds/mainMenuTheme.wav").toURI().toString());
	private AudioClip menuSelection = new AudioClip(new File("sounds/menuSelection.wav").toURI().toString());

	public WarlordsController() {
		
	}
	
	// Set up the objects in a new game. Create a game object with the correct parameters
	public void createNewGame(int oneSelects, boolean oneIsAI, int twoSelects, boolean twoIsAI, int threeSelects, boolean threeIsAI, int fourSelects, boolean fourIsAI) {
		Ball ball = new Ball(350, 350); // Ball
		
		Warlord player1 = new Warlord(new Paddle(181, 547), 40, 678, oneIsAI, oneSelects); // Players
		Warlord player2 = new Warlord(new Paddle(181, 181), 40, 40, twoIsAI, twoSelects);
		Warlord player3 = new Warlord(new Paddle(547, 181), 678, 40, threeIsAI, threeSelects); 
		Warlord player4 = new Warlord(new Paddle(547, 547), 678, 678, fourIsAI, fourSelects); 
		ArrayList<Warlord> playerList = new ArrayList<Warlord>();
		playerList.add(player1);
		playerList.add(player2);
		playerList.add(player3);
		playerList.add(player4);
		
		Wall wall11 = new Wall(0, 563, 0); // Walls
		Wall wall12 = new Wall(70, 563, 0);
		Wall wall13 = new Wall(140, 598, 0);
		Wall wall14 = new Wall(175, 668, 0);
		Wall wall15 = new Wall(175, 738, 0);
		Wall wall21 = new Wall(0, 175, 1);
		Wall wall22 = new Wall(70, 175, 1);
		Wall wall23 = new Wall(140, 140, 1);
		Wall wall24 = new Wall(175, 70, 1);
		Wall wall25 = new Wall(175, 0, 1);
		Wall wall31 = new Wall(738, 175, 2);
		Wall wall32 = new Wall(668, 175, 2);
		Wall wall33 = new Wall(598, 140, 2);
		Wall wall34 = new Wall(563, 70, 2);
		Wall wall35 = new Wall(563, 0, 2);
		Wall wall41 = new Wall(738, 563, 3);
		Wall wall42 = new Wall(668, 563, 3);
		Wall wall43 = new Wall(598, 598, 3);
		Wall wall44 = new Wall(563, 668, 3);
		Wall wall45 = new Wall(563, 738, 3);
		ArrayList<Wall> wallList = new ArrayList<Wall>();
		wallList.add(wall11);
		wallList.add(wall12);
		wallList.add(wall13);
		wallList.add(wall14);
		wallList.add(wall15);
		wallList.add(wall21);
		wallList.add(wall22);
		wallList.add(wall23);
		wallList.add(wall24);
		wallList.add(wall25);
		wallList.add(wall31);
		wallList.add(wall32);
		wallList.add(wall33);
		wallList.add(wall34);
		wallList.add(wall35);
		wallList.add(wall41);
		wallList.add(wall42);
		wallList.add(wall43);
		wallList.add(wall44);
		wallList.add(wall45);
		
		game = new Game(ball, 763, 763, playerList, wallList, -1);	// Create the game	
	}
	
	// On start, create the root layout then show the main menu
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
		if (game == null) { // Default game, useful when testing
			createNewGame(0, true, 0, true, 7, false, 0 ,true);
		}
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(WarlordsController.class.getResource("view/GameView.fxml")); // The game view
			AnchorPane gameView = (AnchorPane) loader.load();

			FXMLLoader loader2 = new FXMLLoader();
			loader2.setLocation(WarlordsController.class.getResource("view/RightSideView.fxml")); // Grey side view
			AnchorPane rightSideView = (AnchorPane) loader2.load();

			FXMLLoader loader3 = new FXMLLoader();
			loader3.setLocation(WarlordsController.class.getResource("view/LeftSideView.fxml")); // Grey side view
			AnchorPane leftSideView = (AnchorPane) loader3.load();

			rootLayout.setCenter(gameView);
			rootLayout.setRight(rightSideView);
			rootLayout.setLeft(leftSideView);

			GameViewController controller = loader.getController(); // Link controllers
			controller.setWarlordsController(this, scene);
			
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
			loader.setLocation(WarlordsController.class.getResource("view/MainMenuView.fxml")); // The main menu
			AnchorPane menuView = (AnchorPane) loader.load();	
			
			FXMLLoader loader2 = new FXMLLoader();
			loader2.setLocation(WarlordsController.class.getResource("view/BlackSideView.fxml")); // Black side view
			AnchorPane leftSideView = (AnchorPane) loader2.load();
			
			FXMLLoader loader3 = new FXMLLoader();
			loader3.setLocation(WarlordsController.class.getResource("view/BlackSideView.fxml")); // Black side view
			AnchorPane rightSideView = (AnchorPane) loader3.load();
			
			rootLayout.setCenter(menuView);
			rootLayout.setLeft(leftSideView);
			rootLayout.setRight(rightSideView);
			
			MainMenuViewController controller = loader.getController(); // Link controllers
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
			loader.setLocation(WarlordsController.class.getResource("view/CampaignMenuView.fxml")); // The campaign meny
			AnchorPane campaignMenuView = (AnchorPane) loader.load();	
			
			rootLayout.setCenter(campaignMenuView);
			
			CampaignMenuViewController controller = loader.getController(); // Link controllers
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
			loader.setLocation(WarlordsController.class.getResource("view/MultiplayerMenuView.fxml")); // The multiplayer window
			AnchorPane multiplayerMenuView = (AnchorPane) loader.load();	
				
			rootLayout.setCenter(multiplayerMenuView);
				
			MultiplayerMenuViewController controller = loader.getController(); // Link controllers
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
			loader.setLocation(WarlordsController.class.getResource("view/OptionsView.fxml")); // The options page
			AnchorPane optionsView = (AnchorPane) loader.load();	
					
			rootLayout.setCenter(optionsView);
					
			OptionsViewController controller = loader.getController(); // Link controllers
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
			loader.setLocation(WarlordsController.class.getResource("view/AboutView.fxml")); // The about page
			AnchorPane aboutView = (AnchorPane) loader.load();	
						
			rootLayout.setCenter(aboutView);
						
			AboutViewController controller = loader.getController(); // Link controllers
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
			loader.setLocation(WarlordsController.class.getResource("view/PlayerMenuView.fxml")); // The multiplayer player select screen
			AnchorPane playerMenuView = (AnchorPane) loader.load();	
							
			rootLayout.setCenter(playerMenuView);
							
			PlayerMenuViewController controller = loader.getController(); // Link controllers
			controller.setWarlordsController(this, scene);
				
			menuSelection.play();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
	
	// Display the multiplayer mutators select screen
	public void showMutatorsView() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(WarlordsController.class.getResource("view/MutatorsView.fxml")); // The multiplayer mutators select screen
			AnchorPane mutatorsView = (AnchorPane) loader.load();	
							
			rootLayout.setCenter(mutatorsView);
							
			MutatorsViewController controller = loader.getController(); // Link controllers
			controller.setWarlordsController(this, scene);
				
			menuSelection.play();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	public Stage getPrimaryStage() {
		return primaryStage;
	}	

	// Return the current game
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
