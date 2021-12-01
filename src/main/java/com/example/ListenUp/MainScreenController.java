package com.example.ListenUp;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class MainScreenController {

    //private ArrayList id,song_name, song_artist,yt_link;

    @FXML
    Button create_new_playlist_button;

    @FXML
    Button add_button;

    @FXML
    Button delete_button;

    @FXML
    Button see_all_button;

    @FXML
    TextField artistTextField, titleTextField, durationTextField, typeTextField, urlTextField;

    String artistBand,title,type,URL;
    int duration; //duration should be changed to a data type that allows easy conversion to TIME (in MySQL)

    public void AddButtonEvent(ActionEvent actionEvent) {
        ListenUp listenUpObj = new ListenUp( artistBand,  title,  duration,  type,  URL){

        };
    }
}
