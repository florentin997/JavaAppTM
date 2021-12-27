package com.example.ListenUp;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.web.WebView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;
import java.util.Date;

public class MainScreenController implements Initializable {


    public static final String CONNECTION_URL = "jdbc:mysql://localhost/listenUp_db";

    //private ArrayList id,song_name, song_artist,yt_link;

    Calendar time = new GregorianCalendar();

    @FXML
    Button create_new_playlist_button;

//    @FXML
//    private Button add_button;
//
//    @FXML
//    private Button delete_button;
//
//    @FXML
//    private Button see_all_button;

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

//    @FXML
//    private WebView webView;
    //ObservableList<String> musicTypes = FXCollections.observableArrayList();
    @FXML
    private ComboBox typeComboBox=new ComboBox();

    Runtime runtime = Runtime.getRuntime();

    Timer timer = new Timer();
    TimerTask exitApp = new TimerTask() {
        @Override
        public void run() {
            try {
                runtime.exec("killall -9  firefox");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };

    //used in WebPageController to get the SelectedItemURL and play it in the webView window
    public String selectedItemURL(){
        return tableViewObject.getSelectionModel().getSelectedItem().getURL();
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
            stage.setScene(new Scene(root));
            stage.show();

        }
        catch (IOException e) {
            e.printStackTrace();
        }

//        DateFormat df = new SimpleDateFormat("HH:mm:ss");
//        Date selectedItemTime = df.parse(tableViewObject.getSelectionModel().getSelectedItem().getDuration());
//        time.setTime(selectedItemTime);
//        //Desktop.getDesktop().browse(new URI((tableViewObject.getSelectionModel().getSelectedItem().getURL())));
//        //webView.getEngine().load(tableViewObject.getSelectionModel().getSelectedItem().getURL());
//        //takes the current clock time
//        LocalDateTime now = LocalDateTime.now();
//        //adds the time to the current time
//        timer.schedule(exitApp, time.getTime().getTime());
//
//        System.out.println(timer);
//        //closes the window

    }


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

    public void SeeAllSongsOnMouseClick(MouseEvent mouseEvent) throws SQLException {

        tableViewObject.getItems().clear();

        try {
            Connection conn = getConnection();
            PreparedStatement ps = conn.prepareStatement("select * from songs");
            ResultSet resultSet = ps.executeQuery();

            while(resultSet.next()){
                int id = resultSet.getInt("id");
                String ArtistBand = resultSet.getString("ArtistBand");
                String Title = resultSet.getString("Title");
                String Duration = resultSet.getString("Duration");
                String Type = resultSet.getString("Type");
                String URL = resultSet.getString("URL");
                listenUpList.add(new ListenUp(id,ArtistBand, Title, Duration, Type,URL));
            }
        }

            catch(SQLException e)
            {
            }

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

//    public void delete() {  //replaced bool with void so it doesn't have to return anything
//            ObservableList<ListenUp> selectedSongs;
//            //selectedSongs = tableViewObject.getItems();
//            selectedSongs = tableViewObject.getSelectionModel().getSelectedItems();
//            for(int i = selectedSongs.size(); i==5; i++){
//                    selectedSongs.add(new ListenUp(0,null,null,null,null,null));
//                    System.out.println(selectedSongs.get(i).getId());
//            }
//
//        try {
//            Connection conn = getConnection();
//            PreparedStatement ps = conn.prepareStatement("delete from songs where id in(?,?,?,?,?)");
//            ps.setInt(1, selectedSongs.get(0).getId());
//            ps.setInt(2, selectedSongs.get(1).getId());
//            ps.setInt(3, selectedSongs.get(2).getId());
//            ps.setInt(4, selectedSongs.get(3).getId());
//            ps.setInt(5, selectedSongs.get(4).getId());
//            ps.executeUpdate();
//            //int affectedRows = ps.executeUpdate();
//            closeConnection(conn);
//            //return affectedRows == 1;
//        }
//        catch (SQLException e) {
//            //return false;
//            System.out.println("An exception occurred");
//        }
//    }

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
}
