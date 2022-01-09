package com.example.ListenUp;

import javafx.animation.PauseTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.sql.*;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;


public class PlaylistController{

    public static final String CONNECTION_URL = "jdbc:mysql://localhost/listenUp_db";


    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(CONNECTION_URL, "root", "strongpassword99!");
    }

    public void closeConnection(Connection conn) throws SQLException {
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
    @FXML
    private ListView PlaylistListView;
    public void initialize() {

        tableViewArtist.setCellValueFactory(new PropertyValueFactory<ListenUp, String>("ArtistBand"));
        tableViewTitle.setCellValueFactory(new PropertyValueFactory<ListenUp, String>("Title"));
        tableViewType.setCellValueFactory(new PropertyValueFactory<ListenUp, String>("Type"));
        tableViewDuration.setCellValueFactory(new PropertyValueFactory<ListenUp, String>("Duration"));

        MainScreenController msc = new MainScreenController();
        tableViewObjectPlaylist.setItems(msc.getAllSongs());
        tableViewObjectPlaylist.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }

    public static ObservableList<ListenUp> GetAllSongsByArtist(ObservableList<ListenUp> list, String artistName) {
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

        if (toggledButton) {
            String playlistTitle = playlistTitleTextField.getText();

            //get all songs from the table
            ObservableList<ListenUp> allSongsFromDB = tableViewObjectPlaylist.getItems();

            //get the name of the selected artist/band
            String artistBandName = tableViewObjectPlaylist.getSelectionModel().getSelectedItem().getArtistBand();

            ObservableList<ListenUp> allSongsBySelectedArtist = GetAllSongsByArtist(allSongsFromDB, artistBandName);
            System.out.println(allSongsBySelectedArtist);
            try {
                for (ListenUp l : allSongsBySelectedArtist) {
                    Connection conn = getConnection();
                    PreparedStatement ps = conn.prepareStatement("insert into playlist(PlaylistTitle, idSong) values(?,?) ", Statement.RETURN_GENERATED_KEYS);
                    ps.setString(1, playlistTitle);
                    ps.setInt(2, l.getId());
                    int affectedRows = ps.executeUpdate();

                    ResultSet rs = ps.getGeneratedKeys();
                    if (rs.next()) {
                        rs.getInt(1);
                    }
                    closeConnection(conn);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            ;
        }

        for (ListenUp song : songsList) {
            int idSong = song.getId();
            String playlistTitle = playlistTitleTextField.getText();
            String songName = song.getTitle();

            try {
                Connection conn = getConnection();
                PreparedStatement ps = conn.prepareStatement("insert into playlist(PlaylistTitle, idSong) values(?,?)", Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, playlistTitle);
                ps.setInt(2, idSong);

                int affectedRows = ps.executeUpdate();

                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    song.setId(rs.getInt(1));
                }
                closeConnection(conn);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    //method that returns the name of the playlists
    public String[] GetAllPlaylistsNames() {
        try {
            Connection conn = getConnection();
            PreparedStatement ps = conn.prepareStatement("SELECT DISTINCT(PlaylistTitle) FROM playlist");
            ResultSet rs = ps.executeQuery();
            List<String> playlistNames = new ArrayList<>();
            while (rs.next()) {
                String title = rs.getString("PlaylistTitle");
                playlistNames.add(title);
            }
            closeConnection(conn);
            return playlistNames.toArray(new String[playlistNames.size()]);
        } catch (SQLException e) {
            return null;
        }
    }

    //returns the URL strings for all songs from a certain playlist
    public Playlist[] GetAllPlaylistSongs(String playlistTitle) {
        try {
            Connection conn = getConnection();
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM playlist WHERE PlaylistTitle=?");
            ps.setString(1, playlistTitle);
            ResultSet rs = ps.executeQuery();
            List<Playlist> playlistSongs = new ArrayList<>();
            while (rs.next()) {
                int idPlaylist = rs.getInt("IdPlaylist");
                String title = rs.getString("PlaylistTitle");
                int idSong = rs.getInt("idSong");
                playlistSongs.add(new Playlist(idPlaylist, title, idSong));
            }
            closeConnection(conn);
            return playlistSongs.toArray(new Playlist[playlistSongs.size()]);
        } catch (SQLException e) {
            return null;
        }
    }


    //gets all songs from the database where the playlist name is given and matches the playlist name from the DB
    public ListenUp GetURLFromPlaylistSongs(int idSong) {

        try {
            List<ListenUp> lista = new ArrayList<>();
            //ListenUp obj = new ListenUp(); // I created an empty constructor in the ListenUp class so I can build an empty obj
            Connection conn = getConnection();
            PreparedStatement ps = conn.prepareStatement("select * from songs WHERE id=?");
            ps.setInt(1, idSong);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int id = rs.getInt("id");
                String ArtistBand = rs.getString("ArtistBand");
                String Title = rs.getString("Title");
                String Duration = rs.getString("Duration");
                String Type = rs.getString("Type");
                String URL = rs.getString("URL");
                lista.add(new ListenUp(id, ArtistBand, Title, Duration, Type, URL));
            }
            closeConnection(conn);
            return lista.get(0);
        } catch (SQLException e) {
            return null;
        }
    }

//
//    public void run(){
//        try {
//            for (Playlist song : GetAllPlaylistSongs("ADDA playlist")) {
////            System.out.println(song.getIdSong());
////            System.out.println(GetURLFromPlaylistSongs(song.getIdSong()));
////            System.out.println(GetURLFromPlaylistSongs(song.getIdSong()).getURL());
//
//                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("WebViewPage.fxml"));
//                WebPageController webPgC = fxmlLoader.getController();
//                webPgC.DisplayVideo(GetURLFromPlaylistSongs(song.getIdSong()).getURL());
//
//                //webPgC.DisplayVideo("https://www.youtube.com/watch?v=H8Es4H7FycU");
//
//
//                Parent root2 = fxmlLoader.load();
//                Stage stage = new Stage();
//                stage.setTitle("ListenUp!");
//                stage.getIcons().add(new Image("C:/Users/Flavius/IdeaProjects/JavaAppTM/ListenUp.png"));
//                stage.setScene(new Scene(root2));
//                stage.show();
//            }
//        }
//            catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//

//
//    Task<Void> task = new Task<Void>() {
//        @Override
//        protected Void call() throws Exception {
//                Platform.runLater(new Runnable() {
//                    @Override
//                public void run() {
//
//                    //the next for gets all the Playlist objects from the "ADDA playlist" playlist so we can access te id of each song and then
//                    //use the id to get the song from the songs table and access its URL and play the songs
//
//                    //OPENS ALL SONGS AT THE SAME TIME
//
//                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("WebViewPage.fxml"));
//                    Parent root2 = null;
//                    try {
//                        root2 = fxmlLoader.load();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                    Stage stage = new Stage();
//                    stage.setTitle("ListenUp!");
//                    stage.getIcons().add(new Image("C:/Users/Flavius/IdeaProjects/JavaAppTM/ListenUp.png"));
//                    stage.setScene(new Scene(root2));
//                    stage.show();
////            //get the duration of the song (as string) and convert it to LocalTime
////            String duration = GetURLFromPlaylistSongs(77).getDuration();
////            LocalTime localTime = LocalTime.parse(duration);
////            //create a seconds variable that stores the duration of the song plus a 5s delay (to count for the time it takes for the new stage to open)
////            int seconds = localTime.toSecondOfDay() + 5;
////            //close the stage after the song is finished
////            PauseTransition delay = new PauseTransition(Duration.seconds(seconds));
////            delay.setOnFinished(event1 -> stage.close());
////            delay.play();
//                }
//                }
//                );
//            return null;
//        }
//    };



//    @Override
//    public void run() {
//
//        //the next for gets all the Playlist objects from the "ADDA playlist" playlist so we can access te id of each song and then
//        //use the id to get the song from the songs table and access its URL and play the songs
//
//        //OPENS ALL SONGS AT THE SAME TIME
//
//        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("WebViewPage.fxml"));
//        Parent root2 = null;
//        try {
//            root2 = fxmlLoader.load();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        Stage stage = new Stage();
//        stage.setTitle("ListenUp!");
//        stage.getIcons().add(new Image("C:/Users/Flavius/IdeaProjects/JavaAppTM/ListenUp.png"));
//        stage.setScene(new Scene(root2));
//        stage.show();
////            //get the duration of the song (as string) and convert it to LocalTime
////            String duration = GetURLFromPlaylistSongs(77).getDuration();
////            LocalTime localTime = LocalTime.parse(duration);
////            //create a seconds variable that stores the duration of the song plus a 5s delay (to count for the time it takes for the new stage to open)
////            int seconds = localTime.toSecondOfDay() + 5;
////            //close the stage after the song is finished
////            PauseTransition delay = new PauseTransition(Duration.seconds(seconds));
////            delay.setOnFinished(event1 -> stage.close());
////            delay.play();
//    }




//plays the song from the playlist (instead of playing and closing them one by one, it starts all the songs at the same time and closes them after each song's individual duration
    public void PlayPlaylistSongs(String playlistTitle){
        try {
            //Playlist[] listOfSongs = GetAllPlaylistSongs(playlistTitle);
            for (Playlist song : GetAllPlaylistSongs(playlistTitle)) {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("WebViewPage.fxml"));
                Parent root2 = fxmlLoader.load();
                WebPageController webpg = fxmlLoader.getController();
                webpg.DisplayVideo(GetURLFromPlaylistSongs(song.getIdSong()).getURL());
                Stage stage = new Stage();
                stage.setTitle("ListenUp!");
                stage.getIcons().add(new Image("C:/Users/Flavius/IdeaProjects/JavaAppTM/ListenUp.png"));
                stage.setScene(new Scene(root2));
                stage.show();
                //get the duration of the song (as string) and convert it to LocalTime
                String duration = GetURLFromPlaylistSongs(song.getIdSong()).getDuration();
                LocalTime localTime = LocalTime.parse(duration);
                //create a seconds variable that stores the duration of the song plus a 5s delay (to count for the time it takes for the new stage to open)
                int seconds = localTime.toSecondOfDay() + 5;
                //close the stage after the song is finished
                PauseTransition delay = new PauseTransition(Duration.seconds(seconds));
                delay.setOnFinished(event1 -> stage.close());
                delay.play();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    //task that opens a webpage view
//    Service service = new Service() {
//        @Override
//        protected Task createTask() {
//            return new Task() {
//                @Override
//                protected Object call() throws Exception {
//                    Platform.runLater(() -> {
//                        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("WebViewPage.fxml"));
////                        Parent root=null;
////                        try {
////                             root = fxmlLoader.load(getClass().getResource("WebViewPage.fxml").openStream());
////                        } catch (IOException e) {
////                            e.printStackTrace();
////                        }
//                        Parent root2 = null;
//                        try {
//                            root2 = fxmlLoader.load();
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                        Stage stage = new Stage();
//                        stage.setTitle("ListenUp!");
//                        stage.getIcons().add(new Image("C:/Users/Flavius/IdeaProjects/JavaAppTM/ListenUp.png"));
//                        stage.setScene(new Scene(root2));
//                        stage.show();
//                        WebPageController wpc = fxmlLoader.getController();
//                        wpc.DisplayVideo(url);
//
//                    });
//                    return null;
//                }
//            };
//        }
//    };


//    //task that returns a song after its duration is finished
//    Service service = new Service() {
//        @Override
//        protected Task createTask() {
//            return new Task() {
//                @Override
//                protected Object call() throws Exception {
//                    Platform.runLater(() -> {
//                        //aici sa pun o metoda care preia cate un cantec (nu cumva e pe acelasi thread?)
//
//                    });
//                    return null;
//                }
//            };
//        }
//    };

    public int Timer(int time) throws InterruptedException {
        Thread.sleep(time);
        int x=1;
        return x;
    }

    public void PlayPlaylistSongs(Playlist song) throws InterruptedException {

                MyRunnable playedSong = new MyRunnable(song);
                playedSong.run();

                String duration = GetURLFromPlaylistSongs(song.getIdSong()).getDuration();
                LocalTime localTime = LocalTime.parse(duration);
                //create a seconds variable that stores the duration of the song plus a 5s delay (to count for the time it takes for the new stage to open)
                int seconds = localTime.toSecondOfDay() + 5;
                //playedSong.wait(Timer(seconds));
                //close the stage after the song is finished
//                PauseTransition delay = new PauseTransition(Duration.seconds(seconds));
//                delay.setOnFinished(event1 -> stage.close());
//                delay.play();
                //Thread.sleep(seconds);


        }


    //deletes a playlist and all its content from the playlist list (doesn't delete the songs from the songs table, only the playlist table)
    public void DeletePlaylistButton(MouseEvent mouseEvent) {
    }

    //It's disabled for the moment
    public void UpdatePlaylistButton(MouseEvent mouseEvent) {
    }

    public void Play(MouseEvent mouseEvent) throws IOException, InterruptedException {

//        for(Playlist song: GetAllPlaylistSongs("test")){
//            //PlayPlaylistSongs(song);
//            MyRunnable playedSong = new MyRunnable(song);
//            playedSong.run();
//            playedSong.wait();
//        }
        PlayPlaylistSongs("test");

        //PlayPlaylistSongs("test");

        //MainScreen obj = new MainScreen();
//        Timer timer = new Timer();//create a new Timer
//        timer.schedule(task, 3000);//this line starts the timer at the same time its executed
        //PlayPlaylistSongs("bbb");
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

    public void SeeAllPlaylistsOnMouseClicked(MouseEvent mouseEvent) {
    }


    public class MyRunnable implements Runnable {

        public MyRunnable(Playlist song) throws InterruptedException {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("WebViewPage.fxml"));
            Parent root2 = null;
            try {
                root2 = fxmlLoader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Stage stage = new Stage();
            stage.setTitle("ListenUp!");
            stage.getIcons().add(new Image("C:/Users/Flavius/IdeaProjects/JavaAppTM/ListenUp.png"));
            stage.setScene(new Scene(root2));
            stage.show();
            WebPageController webPgC = fxmlLoader.getController();
            webPgC.DisplayVideo(GetURLFromPlaylistSongs(song.getIdSong()).getURL());
            String duration = GetURLFromPlaylistSongs(song.getIdSong()).getDuration();
            LocalTime localTime = LocalTime.parse(duration);
            //create a seconds variable that stores the duration of the song plus a 5s delay (to count for the time it takes for the new stage to open)
            int seconds = localTime.toSecondOfDay() + 5;
            //close the stage after the song is finished
            PauseTransition delay = new PauseTransition(Duration.seconds(seconds));
            delay.setOnFinished(event1 -> stage.close());
            delay.play();
            //Thread.sleep(seconds);
            //this.wait(seconds);
//            this.run();
        }

        public void run() {
        }
    }
}

//What i think the problem is:
//Whenever I use a for loop to iterate through each song of a playlist the thread it's the main one and
//it doesn't stop to wait for the other song (thread??) to stop
