package be.ac.umons.project.Mains;

import be.ac.umons.project.Control.Controller;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage){
        Controller control = Controller.getInstance();
        BorderPane root = control.getScene().mainBorderpane;
        Scene scene = new Scene(root,1090, 650);
        primaryStage.setResizable(false);
        primaryStage.setTitle("BATTLESHIPS");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
		System.out.println('Ran');
    }
}