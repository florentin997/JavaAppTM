package com.example.ListenUp;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

import java.io.IOException;

public class WebPageController{
    @FXML
    public WebView webViewPage;

    public void DisplayVideo(String url){
        webViewPage.getEngine().load(url);
//        webViewPage.getEngine().getLoadWorker().stateProperty().addListener(
//                new ChangeListener<Worker.State>() {
//                    @Override
//                    public void changed(
//                            ObservableValue<? extends Worker.State> observable,
//                            Worker.State oldValue, Worker.State newValue) {
//                        switch (newValue) {
//                            case SUCCEEDED:
//                            case FAILED:
//                            case CANCELLED:
//                                webViewPage
//                                        .getEngine()
//                                        .getLoadWorker()
//                                        .stateProperty()
//                                        .removeListener(this);
//                        }
//
//                        if (newValue != Worker.State.SUCCEEDED) {
//                            return;
//                        }
//                        webViewPage.getEngine().load(url);
//                    }
//                } );
    }

}
