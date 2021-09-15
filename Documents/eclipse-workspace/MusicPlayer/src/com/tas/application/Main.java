package com.tas.application;

import com.tas.dataModel.Settings;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("MainWindow.fxml"));
		Parent root = (Parent)loader.load();
		root.getStylesheets().add(getClass().getResource(Settings.getInstance().getThemeFile()).toExternalForm());
		Controller controller = (Controller)loader.getController();
        primaryStage.setTitle("Music Player");
        primaryStage.setScene(new Scene(root, 1220, 500));
        primaryStage.show();
        
        primaryStage.setOnCloseRequest(e -> controller.handleExit());
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
