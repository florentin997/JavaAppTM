package com.example.ListenUp;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;

import javax.security.auth.callback.Callback;
import java.io.IOException;
import java.sql.*;
import java.util.ResourceBundle;
import java.util.logging.Logger;

public class MainScreenController implements Initializable {


    public static final String CONNECTION_URL = "jdbc:mysql://localhost/listenUp_db";

    //private ArrayList id,song_name, song_artist,yt_link;

    @FXML
    Button create_new_playlist_button;

    @FXML
    private Button add_button;

    @FXML
    private Button delete_button;

    @FXML
    private Button see_all_button;

    //The entire table
    @FXML
    private TableView<ListenUp> tableViewObject;
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

    //ObservableList<String> musicTypes = FXCollections.observableArrayList();
    @FXML
    private ComboBox typeComboBox=new ComboBox();


    String ArtistBand,title,type,URL;
    String duration; //duration should be changed to a data type that allows easy conversion to TIME (in MySQL)

    ObservableList<ListenUp> listenUpList = FXCollections.observableArrayList();

    @Override
    public void initialize(java.net.URL url, ResourceBundle resourceBundle) {

        //set up the columns in the table
        //tableViewId.setCellValueFactory(new PropertyValueFactory<ListenUp,String>("Id"));
        tableViewArtist.setCellValueFactory(new PropertyValueFactory<ListenUp, String>("ArtistBand"));
        tableViewTitle.setCellValueFactory(new PropertyValueFactory<ListenUp, String>("Title"));
        //tableViewType.setCellValueFactory(new PropertyValueFactory<ListenUp, String>("Type"));
        typeComboBox.getItems().addAll("Rock","Pop","Country","Folk","Jazz","Hip Hop", "Rap","Electronic","Indie","Classical");
        tableViewDuration.setCellValueFactory(new PropertyValueFactory<ListenUp, String>("Duration"));

        tableViewObject.setItems(getAllSongs());
    }

    public void SeeAllSongsOnMouseClick(MouseEvent mouseEvent) throws SQLException {

        tableViewObject.getItems().clear();

        try {
            Connection conn = getConnection();
            PreparedStatement ps = conn.prepareStatement("select * from songs");
            ResultSet resultSet = ps.executeQuery();

            while(resultSet.next()){
                int id = resultSet.getInt("id");
                //System.out.println(id);
                //String Id = resultSet.getString("Id");
                String ArtistBand = resultSet.getString("ArtistBand");
                String Title = resultSet.getString("Title");
                String Duration = resultSet.getString("Duration");
                String Type = resultSet.getString("Type");
                String URL = resultSet.getString("URL");
                //listenUpList.add(new ListenUp( ArtistBand, Title, Duration, Type));
                listenUpList.add(new ListenUp(id,ArtistBand, Title, Duration, Type,URL));
            }
            System.out.println(listenUpList);
            //closeConnection(conn);
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



//    public void DeleteSongOnMouseClicked(MouseEvent mouseEvent)
//    {
//        ListenUp selectedObject = tableViewObject.getSelectionModel().getSelectedItem();
//        int selectedItemID = selectedObject.getId();
//        try
//        {
//            Connection conn = getConnection();
//            PreparedStatement ps = conn.prepareStatement("delete from songs where id = ?");
//            ps.setInt(1, selectedItemID);  //1 stands for the index of the given parameters
//            closeConnection(conn);
//        }
//        catch(SQLException e)
//        {
//
//        }
//    }



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

    //THE ITEM IS SELECTED AND REMOVED ONLY THE FIRST TIME I EXECUTE THE CODE
    //IF I USE ANY OTHER BUTTON FIRST, DELETE DOESN'T WORK ANYMORE
    public void DeleteSongOnMouseClicked(MouseEvent mouseEvent) {
        ListenUp selectedObject = tableViewObject.getSelectionModel().getSelectedItem();
        System.out.println(selectedObject);
        if ( selectedObject != null )
        {
                delete(selectedObject.getId());
        }
    }

    public void DeleteSongOnKeyPressed(KeyEvent keyEvent)
    {
        final ListenUp selectedObject = tableViewObject.getSelectionModel().getSelectedItem();
        if ( selectedObject != null )
        {
            if ( keyEvent.getCode().equals( KeyCode.DELETE ) )
            {
                delete(selectedObject.getId());
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
