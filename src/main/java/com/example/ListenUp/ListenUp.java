package com.example.ListenUp;




public class ListenUp {

    private int id;
    private String artistBand;
    private String title;
    private String duration; //string, flout or int?
    private String type;
    private String URL;


    public ListenUp(int id, String artistBand, String title, String duration, String type, String URL) {
        this.id = id;
        this.artistBand = artistBand;
        this.title = title;
        this.duration = duration;
        this.type = type;
        this.URL = URL;
    }


    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getArtistBand() {
        return artistBand;
    }

    public void setArtistBand(String artistBand) {
        this.artistBand = artistBand;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    @Override
    public String toString() {
        return "ListenUp [id=" + id + ", ArtistBand=" + artistBand + ", Title=" + title + ", Duration=" + duration
                + ", Type=" + type + ", URL=" + URL + "]";
    }

}
