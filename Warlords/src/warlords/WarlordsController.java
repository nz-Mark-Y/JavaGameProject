package warlords;


import java.io.IOException;
import java.util.ArrayList;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import warlords.model.Ball;
import warlords.model.Game;
import warlords.model.Paddle;
import warlords.model.Wall;
import warlords.model.Warlord;
import warlords.view.CampaignMenuViewController;
import warlords.view.GameViewController;
import warlords.view.LeftSideViewController;
import warlords.view.MainMenuViewController;
import warlords.view.RightSideViewController;

public class WarlordsController extends Application {
	private Stage primaryStage;
	private BorderPane rootLayout;
	private Game game;
	boolean goUp, goDown;
	Scene scene;

	public WarlordsController() {
		createNewGame();
	}
	
	// Set up the objects in a new game
	public void createNewGame() {
		Ball ball = new Ball(350, 350);
		Warlord player1 = new Warlord(new Paddle(200, 200), 100, 100);
		Warlord player2 = new Warlord(new Paddle(568, 568), 668, 668);
		Warlord player3 = new Warlord(new Paddle(200, 568), 100, 668);
		Warlord player4 = new Warlord(new Paddle(568, 200), 668, 100);
		ArrayList<Warlord> playerList = new ArrayList<Warlord>();
		playerList.add(player1);
		playerList.add(player2);
		playerList.add(player3);
		playerList.add(player4);
		Wall wall1 = new Wall(100, 200, 0);
		Wall wall2 = new Wall(668, 618, 1);
		Wall wall3 = new Wall(100, 618, 2);
		Wall wall4 = new Wall(668, 200, 3);
		ArrayList<Wall> wallList = new ArrayList<Wall>();
		wallList.add(wall1);
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
		showGameView();
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
			controller.setWarlordsController(this);

			RightSideViewController controller2 = loader2.getController();
			controller2.setWarlordsController(this);

			LeftSideViewController controller3 = loader3.getController();
			controller3.setWarlordsController(this);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	// Display the main menu, and two black side windows
	public void showMainMenu() {
		try {
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
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
	
	// Display the campaign menu
	public void showCampaignMenu() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(WarlordsController.class.getResource("view/CampaignMenuView.fxml"));
			AnchorPane campaignMenuView = (AnchorPane) loader.load();	
			
			rootLayout.setCenter(campaignMenuView);
			
			CampaignMenuViewController controller = loader.getController();
			controller.setWarlordsController(this, scene);
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
