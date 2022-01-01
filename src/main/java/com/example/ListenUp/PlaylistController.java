package com.example.ListenUp;

import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableListBase;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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
    @FXML
    private TextField playlistTitleTextField;
    @FXML
    private Button seePlaylistsButton;

    public void initialize() {

        tableViewArtist.setCellValueFactory(new PropertyValueFactory<ListenUp, String>("ArtistBand"));
        tableViewTitle.setCellValueFactory(new PropertyValueFactory<ListenUp, String>("Title"));
        tableViewType.setCellValueFactory(new PropertyValueFactory<ListenUp, String>("Type"));
        tableViewDuration.setCellValueFactory(new PropertyValueFactory<ListenUp, String>("Duration"));

        MainScreenController msc = new MainScreenController();
        tableViewObjectPlaylist.setItems(msc.getAllSongs());
        tableViewObjectPlaylist.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }

    public static ObservableList<ListenUp> GetAllSongsByArtist(ObservableList<ListenUp> list,String artistName) {
        ObservableList<ListenUp> songsByArtist = FXCollections.observableArrayList();
        for (ListenUp song : list) {
            if (song.getArtistBand().toLowerCase().equals(artistName.toLowerCase())) {
                songsByArtist.add(song);
            }
        }
        return songsByArtist;
    }

   //adds all selected songs to the playlist table from the DB
    public void AddSongsToPlaylistButton(MouseEvent mouseEvent) throws SQLException {

        boolean toggledButton = toggleButtonPlaylist.selectedProperty().get();

        ObservableList<ListenUp> songsList = tableViewObjectPlaylist.getSelectionModel().getSelectedItems();

        if(toggledButton){
            String playlistTitle = playlistTitleTextField.getText();

            //get all songs from the table
            ObservableList<ListenUp> allSongsFromDB = tableViewObjectPlaylist.getItems();

            //get the name of the selected artist/band
            String artistBandName = tableViewObjectPlaylist.getSelectionModel().getSelectedItem().getArtistBand();

            ObservableList<ListenUp> allSongsBySelectedArtist =   GetAllSongsByArtist(allSongsFromDB,artistBandName);
            System.out.println(allSongsBySelectedArtist);
            try
            {
                for(ListenUp l: allSongsBySelectedArtist){
                    Connection conn = getConnection();
                    PreparedStatement ps = conn.prepareStatement("insert into playlist(PlaylistTitle, idSong) values(?,?) ", Statement.RETURN_GENERATED_KEYS);
                    ps.setString(1, playlistTitle);
                    ps.setInt(2,l.getId());
                    int affectedRows = ps.executeUpdate();

                    ResultSet rs = ps.getGeneratedKeys();
                    if(rs.next())
                    {
                        rs.getInt(1);
                    }
                    closeConnection(conn);
                }
            }
            catch(SQLException e)
            {
                e.printStackTrace();
            };
        }

        for(ListenUp song : songsList ){
            int idSong = song.getId();
            String playlistTitle = playlistTitleTextField.getText();
            String songName = song.getTitle();

            try
            {
                Connection conn = getConnection();
                PreparedStatement ps = conn.prepareStatement("insert into playlist(PlaylistTitle, idSong) values(?,?)", Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, playlistTitle);
                ps.setInt(2, idSong);

                int affectedRows = ps.executeUpdate();

                ResultSet rs = ps.getGeneratedKeys();
                if(rs.next())
                {
                    song.setId(rs.getInt(1));
                }
                closeConnection(conn);
            }
            catch(SQLException e)
            {
                e.printStackTrace();
            }
        }

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
