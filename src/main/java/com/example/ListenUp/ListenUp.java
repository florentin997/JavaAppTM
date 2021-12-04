package com.example.ListenUp;


public class ListenUp {

    private int id;
    private String ArtistBand;
    private String Title;
    private String Duration; //string, flout or int?
    private String Type;
    private String URL;

    public ListenUp() {
    }

    public ListenUp(int id, String artistBand, String title, String duration, String type, String URL) {
        this.id = id;
        ArtistBand = artistBand;
        Title = title;
        Duration = duration;
        Type = type;
        this.URL = URL;
    }

    public ListenUp(int id) {
        super();
        this.id = id;
    }

    public ListenUp(String artistBand, String title, String duration, String type, String URL) {
        super();
        ArtistBand = artistBand;
        Title = title;
        Duration = duration;
        Type = type;
        this.URL = URL;
    }

    public ListenUp(String artistBand, String title, String duration, String type) {
        super();
        ArtistBand = artistBand;
        Title = title;
        Duration = duration;
        Type = type;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getArtistBand() {
        return ArtistBand;
    }

    public void setArtistBand(String artistBand) {
        ArtistBand = artistBand;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getDuration() {
        return Duration;
    }

    public void setDuration(String duration) {
        Duration = duration;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    @Override
    public String toString() {
        return "ListenUp [id=" + id + ", ArtistBand=" + ArtistBand + ", Title=" + Title + ", Duration=" + Duration
                + ", Type=" + Type + ", URL=" + URL + "]";
    }

}
