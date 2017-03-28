package warlords.control;

import java.io.IOException;
import java.util.ArrayList;

import javafx.application.Application;
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
import warlords.view.GameViewController;

public class WarlordsController extends Application {
	private Stage primaryStage;
	private BorderPane rootLayout;
	private Game game;
	
	public WarlordsController() {
		createNewGame();
	}
	
	public void createNewGame() {
		Ball ball = new Ball(600, 600);
		Warlord player1 = new Warlord(new Paddle(200, 200), 100, 100);
		Warlord player2 = new Warlord(new Paddle(568, 568), 668, 668);
		ArrayList<Warlord> playerList = new ArrayList<Warlord>();
		playerList.add(player1);
		playerList.add(player2);
		Wall wall1 = new Wall(100, 200, 0);
		Wall wall2 = new Wall(668, 618, 1);
		ArrayList<Wall> wallList = new ArrayList<Wall>();
		wallList.add(wall1);
		wallList.add(wall2);
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
	
	public void initRootLayout() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(WarlordsController.class.getResource("../view/RootLayout.fxml"));
			rootLayout = (BorderPane) loader.load();
			
			Scene scene  = new Scene(rootLayout);
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
	
	public void showGameView() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(WarlordsController.class.getResource("../view/GameView.fxml"));
			AnchorPane gameView = (AnchorPane) loader.load();
			
			rootLayout.setCenter(gameView);
			
			GameViewController controller = loader.getController();
			controller.setWarlordsController(this);
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
}
