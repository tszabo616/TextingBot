package com.tas.dataModel;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class PlaylistData {
	private ObservableList<Playlist> playlistList;
	
	public PlaylistData() {
		this.playlistList = FXCollections.observableArrayList();
    }

	public ObservableList<Playlist> getList() {
		return playlistList;
	}
	public void setList(ObservableList<Playlist> list) {
		this.playlistList = list;
	}
	public Playlist getPlaylist(String playlistName) {
		Playlist result = null;
		for(int i=0; i<playlistList.size(); i++) {
			Playlist playlist = playlistList.get(i);
			if(playlist.getName().equals(playlistName)) {
				result = playlist;
				break;
			}
		}
		return result;
	}
	public boolean playlistExists(String playlistName){
		if(getPlaylist(playlistName) == null) {
			return false;
		} else {
			return true;
		}
	}
	public void addPlaylist(Playlist playlist) {
		playlistList.add(playlist);
	}
	public void removePlaylist(Playlist playlist) {
		playlistList.remove(playlist);
	}
	public void removePlaylists(List<Playlist> playlists) {
		playlistList.removeAll(playlists);
	}
	
	public void savePlaylists() throws IOException {
		System.out.println("Saving playlists.");
		Path filePath = FileSystems.getDefault().getPath("playlists.dat");
        try (ObjectOutputStream dataFile = new ObjectOutputStream(new BufferedOutputStream(Files.newOutputStream(filePath)))) {
            for(Playlist playlist : playlistList) {
            	PlaylistSerializable savedPlaylist = new PlaylistSerializable(playlist.getName());
            	dataFile.writeObject(savedPlaylist);
            }
        } catch(IOException e) {
        	System.out.println(e.getMessage());
        }
	}
	
    public void loadPlaylists() throws IOException {
    	Path filePath = FileSystems.getDefault().getPath("playlists.dat");
        try (ObjectInputStream dataFile = new ObjectInputStream(new BufferedInputStream(Files.newInputStream(filePath)))) {
            boolean eof = false;
            while(!eof) {
                try {
                    PlaylistSerializable savedPlaylist = (PlaylistSerializable) dataFile.readObject();
                    Playlist playlist = new Playlist(savedPlaylist.getName());
                    playlistList.add(playlist);
                } catch(EOFException e) {
                    eof = true;
                }
            }
        } catch(InvalidClassException e) {
            System.out.println("InvalidClassException " + e.getMessage());
        } catch(IOException e) {
            System.out.println("IOException " + e.getMessage());
        } catch(ClassNotFoundException e) {
            System.out.println("ClassNotFoundException " + e.getMessage());
        }
    }
}
