package com.example.ListenUp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.IOException;


public class MainScreen extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        //FXMLLoader fxmlLoader = new FXMLLoader(MainScreen.class.getResource("ListenUpGUI.fxml"));

        Parent root = FXMLLoader.load(getClass().getResource("ListenUpGUI.fxml")); //newly added

        Scene scene = new Scene(root);
        //stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("ListenUp!");
        stage.getIcons().add(new Image("C:/Users/Flavius/IdeaProjects/JavaAppTM/ListenUp.png"));
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args)
    {
        launch(args);
    }


}