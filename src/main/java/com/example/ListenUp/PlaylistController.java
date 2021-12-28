package com.example.ListenUp;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

import java.sql.*;
import java.util.ResourceBundle;

public class PlaylistController {

    public static final String CONNECTION_URL = "jdbc:mysql://localhost/listenUp_db";

    public Connection getConnection() throws SQLException
    {
        return DriverManager.getConnection(CONNECTION_URL, "root", "strongpassword99!");
    }

    public void closeConnection(Connection conn) throws SQLException
    {
        conn.close();
    }

    @FXML
    private TableView<ListenUp> tableViewObjectPlaylist;
    @FXML
    private TableColumn<ListenUp, String> tableViewArtist, tableViewTitle, tableViewType, tableViewDuration;
    @FXML
    private ToggleButton toggleButtonPlaylist;


    public void initialize() {

        tableViewArtist.setCellValueFactory(new PropertyValueFactory<ListenUp, String>("ArtistBand"));
        tableViewTitle.setCellValueFactory(new PropertyValueFactory<ListenUp, String>("Title"));
        tableViewType.setCellValueFactory(new PropertyValueFactory<ListenUp, String>("Type"));
        tableViewDuration.setCellValueFactory(new PropertyValueFactory<ListenUp, String>("Duration"));

        MainScreenController msc = new MainScreenController();
        tableViewObjectPlaylist.setItems(msc.getAllSongs());
        tableViewObjectPlaylist.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }


   //adds all selected songs to the playlist table from the DB
    public void AddSongsToPlaylistButton(MouseEvent mouseEvent) {
    }

    //deletes a playlist and all its content from the playlist list (doesn't delete the songs from the songs table, only the playlist table)
    public void DeletePlaylistButton(MouseEvent mouseEvent) {
    }

    //It's disabled for the moment
    public void UpdatePlaylistButton(MouseEvent mouseEvent) {
    }

    public void FillTableOnStart(SortEvent<TableView<ListenUp>> tableViewSortEvent) {
    }


    //for the add button, when "Add songs by artist" button is active add all songs from the selected artist
    //if(AddSongsByArtist is active)
    //add all songs from that artist
    //else
    //add selected songs
}
