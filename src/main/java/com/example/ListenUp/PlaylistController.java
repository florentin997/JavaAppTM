package com.example.ListenUp;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.MouseEvent;

public class PlaylistController {

    @FXML
    private TableView<ListenUp> tableViewObjectPlaylist;
    @FXML
    private TableColumn tableViewArtist,tableViewTitle,tableViewType,tableViewDuration;
    @FXML
    private ToggleButton toggleButtonPlaylist;

    //adds all selected songs to the playlist table from the DB
    public void AddSongsToPlaylistButton(MouseEvent mouseEvent) {

    }

    //deletes a playlist and all its content from the playlist list (doesn't delete the songs from the songs table, only the playlist table)
    public void DeletePlaylistButton(MouseEvent mouseEvent) {

    }

    //It's disabled for the moment
    public void UpdatePlaylistButton(MouseEvent mouseEvent) {
    }

    //for the add button, when "Add songs by artist" button is active add all sons from the selected artist
    //if(AddSongsByArtist is active)
    //add all songs from that artist
    //else
    //add selected songs
}
