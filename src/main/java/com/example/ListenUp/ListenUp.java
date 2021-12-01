package com.example.ListenUp;


public class ListenUp {

    private int id;
    private String ArtistBand;
    private String Title;
    private int Duration;
    private String Type;
    private String URL;

    public ListenUp() {
    }

    public ListenUp(int id, String artistBand, String title, int duration, String type, String URL) {
        super();
        this.id = id;
        ArtistBand = artistBand;
        Title = title;
        Duration = duration;
        Type = type;
        URL = URL;
    }

    public ListenUp(int id) {
        super();
        this.id = id;
    }

    public ListenUp(String artistBand, String title, int duration, String type, String URL) {
        super();
        ArtistBand = artistBand;
        Title = title;
        Duration = duration;
        Type = type;
        URL = URL;
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
    public int getDuration() {
        return Duration;
    }
    public void setDuration(int duration) {
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
        URL = URL;
    }
    @Override
    public String toString() {
        return "ListenUp [id=" + id + ", ArtistBand=" + ArtistBand + ", Title=" + Title + ", Duration=" + Duration
                + ", Type=" + Type + ", URL=" + URL + "]";
    }

}
