package com.example.ListenUp;

import javafx.animation.PauseTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.sql.*;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;


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
    private Button PlaySongs;
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

    //method that returns the name of the playlists
    public String[] GetAllPlaylistsNames(){
        try{
            Connection conn = getConnection();
            PreparedStatement ps = conn.prepareStatement("SELECT DISTINCT(PlaylistTitle) FROM playlist");
            ResultSet rs = ps.executeQuery();
            List<String> playlistNames= new ArrayList<>();
            while(rs.next()) {
                String title = rs.getString("PlaylistTitle");
                playlistNames.add(title);
            }
            closeConnection(conn);
            return playlistNames.toArray(new String[playlistNames.size()]);
        }
        catch(SQLException e)
        {
            return null;
        }
    }

    //returns the URL strings for all songs from a certain playlist
    public Playlist[] GetAllPlaylistSongs(String playlistTitle){
        try{
            Connection conn = getConnection();
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM playlist WHERE PlaylistTitle=?");
            ps.setString(1,playlistTitle);
            ResultSet rs = ps.executeQuery();
            List<Playlist> playlistSongs= new ArrayList<>();
            while(rs.next()) {
                int idPlaylist= rs.getInt("IdPlaylist");
                String title = rs.getString("PlaylistTitle");
                int idSong = rs.getInt("idSong");
                playlistSongs.add(new Playlist(idPlaylist,title,idSong));
            }
            closeConnection(conn);
            return playlistSongs.toArray(new Playlist[playlistSongs.size()]);
        }
        catch(SQLException e)
        {
            return null;
        }
    }


    //get one song from the playlist and wait for the duration of that song before getting the next song so we can play
    //the first song and close it before the next one starts
    public Playlist GetSongFromSongsList(int i) throws InterruptedException {
        Playlist [] list = GetAllPlaylistSongs("ccc");
        Playlist song = list[i];
        //i++;
        return song;
    }

    //gets all songs from the database where the playlist name is given and matches the playlist name from the DB
    public ListenUp GetURLFromPlaylistSongs(int idSong){

        try{
            List<ListenUp> lista = new ArrayList<>();
            Connection conn = getConnection();
            PreparedStatement ps = conn.prepareStatement("select * from songs WHERE id=?");
            ps.setInt(1,idSong);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                int id = rs.getInt("id");
                String ArtistBand= rs.getString("ArtistBand");
                String Title= rs.getString("Title");
                String Duration= rs.getString("Duration");
                String Type= rs.getString("Type");
                String URL= rs.getString("URL");
                lista.add(new ListenUp(id,ArtistBand,Title,Duration,Type,URL));
            }
            closeConnection(conn);
            return lista.get(0);
        }
        catch(SQLException e)
        {
            return null;
        }
    }


//    public boolean RunningTask(int time) throws InterruptedException {
//        Thread.sleep(time);
//        return false;
//    }

//    public boolean task(int idSong) throws IOException, InterruptedException {
//        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("WebViewPage.fxml"));
//        Parent root0 = fxmlLoader.load();
//        WebPageController webpg = fxmlLoader.getController();
//        webpg.DisplayVideo(GetURLFromPlaylistSongs(idSong).getURL());
//        Stage stage = new Stage();
//        stage.setTitle("ListenUp!");
//        stage.getIcons().add(new Image("C:/Users/Flavius/IdeaProjects/JavaAppTM/ListenUp.png"));
//        stage.setScene(new Scene(root0));
//        stage.show();
//        //get the duration of the song (as string) and convert it to LocalTime
//        String duration = GetURLFromPlaylistSongs(idSong).getDuration();
//        LocalTime localTime = LocalTime.parse(duration);
//        //create a seconds variable that stores the duration of the song plus a 5s delay (to count for the time it takes for the new stage to open)
//        int seconds = localTime.toSecondOfDay()+5;
//        //System.out.println(duration);
//        //close the stage after the song is finished
//        //Thread.sleep(seconds);
//        PauseTransition delay = new PauseTransition(Duration.seconds(seconds));
//        delay.setOnFinished( event1 -> stage.close() );
//        delay.play();
//        return true;
//    }

//    public void PlayPlaylistSongs(String playlistTitle){
//        try {
//            //the next for gets all the Playlist objects from the "ADDA playlist" playlist so we can access te id of each song and then
//            //use the id to get the song from the songs table and access its URL and play the songs
//
//            //OPENS ALL SONGS AT THE SAME TIME
//            for(Playlist song: GetAllPlaylistSongs(playlistTitle)){
//                Runnable r1 = new Runnable() {
//                public void run() {
//
//                }};
//                int idSong = song.getIdSong();
//
//                //Thread.sleep(seconds);
//                //method that stops the thread for as long as the song lasts and when it finishes it returns false
//                //when x is false break the loop
////                boolean timer=true;
////                while (timer){
////                    boolean x = RunningTask(seconds);
////                    if(x==false){
////                        break;
////                }
////            }
//            }
//        }
//        catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

//final ExecutorService executor = Executors.newFixedThreadPool(5); // it's just an arbitrary number
//    final List<Future<?>> futures = new ArrayList<>();
//    for (Playlist song : GetAllPlaylistSongs("bbb")) {
//        for (final String sellerName : sellerNames) {
//            Future<?> future = executor.submit(() -> {
//                getSellerAddress(sellerName);
//            });
//            futures.add(future);
//        }
//    }
//    try {
//        for (Future<?> future : futures) {
//            future.get(); // do anything you need, e.g. isDone(), ...
//        }
//    } catch (InterruptedException | ExecutionException e) {
//        e.printStackTrace();
//    }




//plays the song from the playlist (instead of playing and closing them one by one, it starts all the songs at the same time and closes them after each song's individual duration
    public void PlayPlaylistSongs(int songNumber,String playlistTitle){
        try {
            //the next for gets all the Playlist objects from the "ADDA playlist" playlist so we can access te id of each song and then
            //use the id to get the song from the songs table and access its URL and play the songs
            Playlist[] listOfSongs = GetAllPlaylistSongs(playlistTitle);
            //OPENS ALL SONGS AT THE SAME TIME

                Playlist idSong = listOfSongs[songNumber];
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("WebViewPage.fxml"));
                Parent root2 = fxmlLoader.load();
                WebPageController webpg = fxmlLoader.getController();
                webpg.DisplayVideo(GetURLFromPlaylistSongs(idSong.getIdSong()).getURL());
                Stage stage = new Stage();
                stage.setTitle("ListenUp!");
                stage.getIcons().add(new Image("C:/Users/Flavius/IdeaProjects/JavaAppTM/ListenUp.png"));
                stage.setScene(new Scene(root2));
//                Rectangle2D screen = Screen.getPrimary().getVisualBounds();
//                stage.setX(screen.getMaxX()-30-stage.getWidth());
//                stage.setY(30);
                stage.show();
                //get the duration of the song (as string) and convert it to LocalTime
                String duration = GetURLFromPlaylistSongs(idSong.getIdSong()).getDuration();
                LocalTime localTime = LocalTime.parse(duration);
                //create a seconds variable that stores the duration of the song plus a 5s delay (to count for the time it takes for the new stage to open)
                int seconds = localTime.toSecondOfDay()+5;
                //System.out.println(duration);
                //close the stage after the song is finished
                PauseTransition delay = new PauseTransition(Duration.seconds(seconds));
                delay.setOnFinished( event1 -> stage.close() );
                delay.play();
//                Timer timer = new Timer();
//                timer.schedule(GetSongFromSongsList(),3000);
//                try{
//                    Thread.sleep(3000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                timer.cancel();
            } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }


    public void PlayPlaylistSongs(String playlistTitle){
        try {
            //the next for gets all the Playlist objects from the "ADDA playlist" playlist so we can access te id of each song and then
            //use the id to get the song from the songs table and access its URL and play the songs

            //OPENS ALL SONGS AT THE SAME TIME
            for(Playlist song: GetAllPlaylistSongs(playlistTitle)){
                int idSong = song.getIdSong();
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("WebViewPage.fxml"));
                //Parent root = FXMLLoader.load(getClass().getResource("WebViewPage.fxml"));
                Parent root2 = fxmlLoader.load();
                WebPageController webpg = fxmlLoader.getController();
                //WebPageController webpg1 = fxmlLoader.getController();
                webpg.DisplayVideo(GetURLFromPlaylistSongs(idSong).getURL());
                Stage stage = new Stage();
                stage.setTitle("ListenUp!");
                stage.getIcons().add(new Image("C:/Users/Flavius/IdeaProjects/JavaAppTM/ListenUp.png"));
                stage.setScene(new Scene(root2));
                stage.show();
                //get the duration of the song (as string) and convert it to LocalTime
                String duration = GetURLFromPlaylistSongs(idSong).getDuration();
                LocalTime localTime = LocalTime.parse(duration);
                //create a seconds variable that stores the duration of the song plus a 5s delay (to count for the time it takes for the new stage to open)
                int seconds = localTime.toSecondOfDay()+5;
                //close the stage after the song is finished
                PauseTransition delay = new PauseTransition(Duration.seconds(seconds));
                delay.setOnFinished( event1 -> stage.close() );
                delay.play();
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }


    //deletes a playlist and all its content from the playlist list (doesn't delete the songs from the songs table, only the playlist table)
    public void DeletePlaylistButton(MouseEvent mouseEvent) {
    }

    //It's disabled for the moment
    public void UpdatePlaylistButton(MouseEvent mouseEvent) {
    }

    public void Play(MouseEvent mouseEvent) {
        PlayPlaylistSongs("bbb");
//        PlaySongs task2 = new PlaySongs();
//        Timer timer2 = new Timer();
//        timer.schedule(task2,3000); //param cu timpul melodiei
//        try{
//            Thread.sleep(3000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        timer.cancel();
//        //System.out.println(GetURLFromPlaylistSongs(74));
    }
}