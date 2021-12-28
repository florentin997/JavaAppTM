package com.example.ListenUp;

import javafx.animation.PauseTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.*;
import java.text.ParseException;
import java.time.LocalTime;
import java.util.*;

public class MainScreenController implements Initializable {


    public static final String CONNECTION_URL = "jdbc:mysql://localhost/listenUp_db";

    @FXML
    Button create_new_playlist_button;

    //The entire table
    @FXML
    private TableView<ListenUp> tableViewObject;
    @FXML
    private TreeView<String> treeViewObject = new TreeView<>();
    //Columns
    @FXML
    private TableColumn<ListenUp, String> tableViewId;
    @FXML
    private TableColumn<ListenUp, String> tableViewArtist;
    @FXML
    private TableColumn<ListenUp, String> tableViewTitle;
    @FXML
    private TableColumn<ListenUp, String> tableViewType;
    @FXML
    private TableColumn<ListenUp, String> tableViewDuration;
    //The text fields in which the user inputs data
    @FXML
    private TextField artistTextField,durationTextField,titleTextField, urlTextField;
    @FXML
    private ComboBox typeComboBox=new ComboBox();

    //used in WebPageController to get the SelectedItemURL and play it in the webView window
//    public String selectedItemURL(){
//        return tableViewObject.getSelectionModel().getSelectedItem().getURL();
//    }

    ObservableList<ListenUp> listenUpList = FXCollections.observableArrayList();

    @Override
    public void initialize(java.net.URL url, ResourceBundle resourceBundle) {

        //set up the columns in the table
        tableViewArtist.setCellValueFactory(new PropertyValueFactory<ListenUp, String>("ArtistBand"));
        tableViewTitle.setCellValueFactory(new PropertyValueFactory<ListenUp, String>("Title"));
        //tableViewType.setCellValueFactory(new PropertyValueFactory<ListenUp, String>("Type"));
        typeComboBox.getItems().addAll("Rock","Pop","Country","Folk","Jazz","Hip Hop", "Rap","Electronic","Indie","Classical");
        tableViewDuration.setCellValueFactory(new PropertyValueFactory<ListenUp, String>("Duration"));

        tableViewObject.setItems(getAllSongs());
        tableViewObject.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        TreeItem root = new TreeItem("Playlists");

        TreeItem webItem = new TreeItem("Rock");
        webItem.getChildren().add(new TreeItem("R1"));
        webItem.getChildren().add(new TreeItem("R2"));
        root.getChildren().add(webItem);

        TreeItem javaItem = new TreeItem("Pop");
        javaItem.getChildren().add(new TreeItem("P1"));
        javaItem.getChildren().add(new TreeItem("P2"));

        root.getChildren().add(javaItem);
        treeViewObject.setRoot(root);
    }


    //open youtube page
    @FXML
    void GetUrlOnMouseClicked(ActionEvent event) throws URISyntaxException, IOException, ParseException {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("WebViewPage.fxml"));
            Parent root = fxmlLoader.load();

            WebPageController webpg = fxmlLoader.getController();
            webpg.DisplayVideo(tableViewObject.getSelectionModel().getSelectedItem().getURL());

            Stage stage = new Stage();
            stage.setTitle("ListenUp!");
            stage.getIcons().add(new Image("C:/Users/Flavius/IdeaProjects/JavaAppTM/ListenUp.png"));
            stage.setScene(new Scene(root));
            stage.show();
            //get the duration of the song (as string) and convert it to LocalTime
            String duration = tableViewObject.getSelectionModel().getSelectedItem().getDuration();
            LocalTime localTime = LocalTime.parse(duration);
            //create a seconds variable that stores the duration of the song plus a 5s delay (to count for the time it takes for the new stage to open)
            int seconds = localTime.toSecondOfDay()+5;
            //System.out.println(duration);
            //close the stage after the song is finished
            PauseTransition delay = new PauseTransition(Duration.seconds(seconds));
            delay.setOnFinished( event1 -> stage.close() );
            delay.play();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void SeeAllSongsOnMouseClick(MouseEvent mouseEvent) throws SQLException {

        tableViewObject.getItems().clear();

        try {
            Connection conn = getConnection();
            PreparedStatement ps = conn.prepareStatement("select * from songs");
            ResultSet resultSet = ps.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String ArtistBand = resultSet.getString("ArtistBand");
                String Title = resultSet.getString("Title");
                String Duration = resultSet.getString("Duration");
                String Type = resultSet.getString("Type");
                String URL = resultSet.getString("URL");
                listenUpList.add(new ListenUp(id, ArtistBand, Title, Duration, Type, URL));
            }
        }
            catch(SQLException e) {}
        tableViewArtist.setCellValueFactory(new PropertyValueFactory<ListenUp, String>("ArtistBand"));
        tableViewTitle.setCellValueFactory(new PropertyValueFactory<ListenUp, String>("Title"));
        tableViewType.setCellValueFactory(new PropertyValueFactory<ListenUp, String>("Type"));
        tableViewDuration.setCellValueFactory(new PropertyValueFactory<ListenUp, String>("Duration"));
    }

    //gets all songs
    public ObservableList<ListenUp> getAllSongs()
    {
        try {
            Connection conn = getConnection();
            PreparedStatement ps = conn.prepareStatement("select * from songs");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String ArtistBand = rs.getString("ArtistBand");
                String Title = rs.getString("Title");
                String Duration = rs.getString("Duration");
                String Type = rs.getString("Type");
                String URL = rs.getString("URL");
                listenUpList.add(new ListenUp(id, ArtistBand, Title, Duration, Type, URL));
            }
            closeConnection(conn);
            return listenUpList;  //listenUpList.toArray(new ListenUp[listenUpList.size()])
        }
        catch(SQLException e)
        {
            return null;
        }
    }


    //NEED TO ADD CONDITIONS FOR ADDING SONGS (can't insert the song if it is already in the DB)
    public void addSongOnMouseClicked(MouseEvent mouseEvent) {
        int id=0;
        ListenUp song = new ListenUp(id,artistTextField.getText(),titleTextField.getText(),durationTextField.getText(), typeComboBox.getValue().toString(),urlTextField.getText());
        //ListenUp song = new ListenUp();
        try
        {
            Connection conn = getConnection();
            PreparedStatement ps = conn.prepareStatement("insert into songs(ArtistBand, Title, Duration, Type, URL) values(?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, song.getArtistBand());
            ps.setString(2, song.getTitle());
            ps.setString(3, song.getDuration());
            ps.setString(4, song.getType());
            ps.setString(5, song.getURL());
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


    public void delete(int id) {  //replaced bool with void so it doesn't have to return anything
        try {
            Connection conn = getConnection();
            PreparedStatement ps = conn.prepareStatement("delete from songs where id = ?");
            ps.setInt(1, id);
            ps.executeUpdate();
            //int affectedRows = ps.executeUpdate();
            closeConnection(conn);
            //return affectedRows == 1;
        }
        catch (SQLException e) {
            //return false;
            System.out.println("An exception occurred");
        }
    }


    public void DeleteSongOnMouseClicked(MouseEvent mouseEvent) {
        ListenUp selectedObject = tableViewObject.getSelectionModel().getSelectedItem();
        if ( selectedObject != null )
        {
                delete(selectedObject.getId());//selectedObject.getId()
        }
    }

    public void DeleteSongOnKeyPressed(KeyEvent keyEvent)
    {
        final ListenUp selectedObject = tableViewObject.getSelectionModel().getSelectedItem();
        if ( selectedObject != null )
        {
            if ( keyEvent.getCode().equals( KeyCode.DELETE ) )
            {
                delete(selectedObject.getId()); //selectedObject.getId()
            }
        }
    }

    public Connection getConnection() throws SQLException
    {
        return DriverManager.getConnection(CONNECTION_URL, "root", "strongpassword99!");
    }

    public void closeConnection(Connection conn) throws SQLException
    {
        conn.close();
    }

    public void CreatePlaylistButton(MouseEvent mouseEvent) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("NewPlaylistPage.fxml"));
            Parent root = fxmlLoader.load();

            Stage stage = new Stage();
            stage.setTitle("Playlist");
            stage.getIcons().add(new Image("C:/Users/Flavius/IdeaProjects/JavaAppTM/ListenUp.png"));
            stage.setScene(new Scene(root));
            stage.show();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
