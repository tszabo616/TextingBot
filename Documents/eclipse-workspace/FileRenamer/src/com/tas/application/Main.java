package com.tas.application;
	
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {
	@Override
	public void start(Stage primaryStage) throws Exception {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("MainWindow.fxml"));
		Parent root = (Parent)loader.load();
		root.getStylesheets().add(getClass().getResource("LightTheme.css").toExternalForm());
		Controller controller = (Controller)loader.getController();
        primaryStage.setTitle("File Renamer");
        primaryStage.getIcons().add(new Image("file:media\\FileRenamerLogoClear.png", 1057, 687, true, true));
        primaryStage.setScene(new Scene(root, 465, 710));
        primaryStage.show();
        primaryStage.setOnCloseRequest(e -> controller.handleExit());
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
