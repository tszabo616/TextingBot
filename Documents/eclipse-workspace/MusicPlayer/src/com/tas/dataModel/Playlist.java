package com.tas.dataModel;

import java.util.List;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Playlist {
	private ObservableList<Song> songsList;
	private SimpleStringProperty name = new SimpleStringProperty("");
	
	public Playlist() {
		this.songsList = FXCollections.observableArrayList();
    }
	public Playlist(String name) {
		this();
		this.name.set(name);
    }

	public void addSong(Song song) {
		songsList.add(song);
	}
	public void addSongAt(int index, Song song) {
		songsList.add(index, song);
	}
	public void addSongs(List<Song> songs) {
		songsList.addAll(songs);
	}
	public void setSongAt(int index, Song song) {
		songsList.set(index, song);
	}
	public void removeSong(Song song) {
		song.getPlaylists().remove(name.get());
		songsList.remove(song);
		updatePlaylist();
	}
	public void removeSongs(List<Song> songs) {
		for(Song song : songs) {
			song.getPlaylists().remove(name.get());
		}
		songsList.removeAll(songs);
		updatePlaylist();
	}
	public void setSongsList(ObservableList<Song> songsList) {
		this.songsList = songsList;
	}
	public ObservableList<Song> getSongsList() {
		return songsList;
	}
	public String getName() {
		return name.get();
	}
	public void setName(String name) {
		this.name.set(name);
	}
	public String toString() {
		return name.get();
	}
	public void expandPlaylistTo(int index) {
    	// Increases the length of the list to the specified index. New entries are null.
		if(index > songsList.size() - 1) {
    		for(int i=songsList.size(); i<=index; i++) {
    			songsList.add(null);
    		}
    	}
    }
	
	public void updatePlaylist() {
		// Updates the stored index for each song in the playlist
		Song song;
		for(int i=0; i<songsList.size(); i++) {
			song = songsList.get(i);
			song.getPlaylists().put(name.get(), i);
		}
	}
}
