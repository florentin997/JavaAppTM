package com.example.ListenUp;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URISyntaxException;
import java.text.ParseException;

public class WebPageController{
    @FXML
    public WebView webViewPage;

    public void DisplayVideo(String url){
        //MainScreenController msc = new MainScreenController();
        //webViewPage.getEngine().load(msc.selectedItemURL());
        webViewPage.getEngine().load(url);
    }

//    public WebPageController(String url){
//
//    }
}
