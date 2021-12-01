package com.example.ListenUp;


public interface ListenUpDAO {
	
	public void addSongs(ListenUp l);
	public boolean update(ListenUp l);
	public boolean delete(int id);
	public ListenUp findById(int id);
	public ListenUp[] getAllSongs();

}
