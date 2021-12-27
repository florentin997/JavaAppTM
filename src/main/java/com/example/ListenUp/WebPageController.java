package com.example.ListenUp;

import javafx.fxml.FXML;
import javafx.scene.web.WebView;

public class WebPageController{
    @FXML
    public WebView webViewPage;

    public void DisplayVideo(String url){
        webViewPage.getEngine().load(url);
    }

}
