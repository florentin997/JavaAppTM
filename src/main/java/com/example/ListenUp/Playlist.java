package com.example.ListenUp;

public class Playlist {
    private int idPlaylist;
    private String playlistTitle;
    private int idSong;

    public Playlist(int idPlaylist, String playlistTitle, int idSong) {
        this.idPlaylist = idPlaylist;
        this.playlistTitle = playlistTitle;
        this.idSong = idSong;
    }

    public int getIdPlaylist() {
        return idPlaylist;
    }

    public void setIdPlaylist(int idPlaylist) {
        this.idPlaylist = idPlaylist;
    }

    public String getPlaylistTitle() {
        return playlistTitle;
    }

    public void setPlaylistTitle(String playlistTitle) {
        this.playlistTitle = playlistTitle;
    }

    public int getIdSong() {
        return idSong;
    }

    public void setIdSong(int idSong) {
        this.idSong = idSong;
    }

    @Override
    public String toString() {
        return "Playlist{" +
                "idPlaylist=" + idPlaylist +
                ", playlistTitle='" + playlistTitle + '\'' +
                ", idSong=" + idSong +
                '}';
    }
}
