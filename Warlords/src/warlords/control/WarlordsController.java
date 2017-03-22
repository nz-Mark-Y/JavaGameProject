package warlords.control;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class WarlordsController extends Application {
	private BorderPane rootLayout;
	
	@Override
	public void start(Stage primaryStage) throws IOException {
		FXMLLoader loader= new FXMLLoader();
		loader.setLocation(WarlordsController.class.getResource("view/GameView.fxml"));
		this.rootLayout = (BorderPane) loader.load();
	}

	public static void main(String[] args) {
		launch(args);
	}
}
