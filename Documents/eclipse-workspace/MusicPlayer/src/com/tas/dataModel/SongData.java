package com.tas.dataModel;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.CannotWriteException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.KeyNotFoundException;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagException;
import org.jaudiotagger.tag.images.Artwork;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class SongData {
	private ObservableList<Song> songsList;
	private ArrayList<Integer> indexList;
	private int indexListIndex;
	private final String name = "Music";
	
	public SongData() {
		songsList = FXCollections.observableArrayList();
		indexList = new ArrayList<Integer>();
		indexListIndex = 0;
    }
	
	
	public String getName() {
		return name;
	}
	public void addSong(Song song) {
		songsList.add(song);
	}
	public void removeSong(Song song) {
		songsList.remove(song);
	}
	public void removeSongs(List<Song> songs) {
		songsList.removeAll(songs);
	}
	public ObservableList<Song> getSongsList() {
		return songsList;
	}
	public Iterator<Song> iterator() {
		return songsList.iterator();
	}
	public Song get(int index) {
		return songsList.get(index);
	}
	public ArrayList<Integer> getIndexList() {
		return indexList;
	}
	public void createIndexList(int currentIndex, boolean shuffled) {
		if(shuffled) {
			indexList = generateShuffledIndexList(currentIndex, songsList.size()-1);
		}else {
			indexList = generateIndexList(currentIndex, songsList.size()-1);
		}
	}
	public int getIndexListIndex() {
		return indexListIndex;
	}
	public void setIndexListIndex(int indexListIndex) {
		this.indexListIndex = indexListIndex;
	}
	
	public void saveSongs() throws IOException {
		System.out.println("Saving songs.");
		Path filePath = FileSystems.getDefault().getPath("data.dat");
        try (ObjectOutputStream dataFile = new ObjectOutputStream(new BufferedOutputStream(Files.newOutputStream(filePath)))) {
            for(Song song : songsList) {
            	overwriteSongFile(song);
            	SongSerializable savedSong = new SongSerializable();
            	savedSong.setSongTitle(song.getSongTitle());
            	savedSong.setSongTitleSort(song.getSongTitleSort());
            	savedSong.setSongArtist(song.getSongArtist());
            	savedSong.setSongArtistSort(song.getSongArtistSort());
            	savedSong.setGenre(song.getGenre());
            	savedSong.setTrackNumber(song.getTrackNumber());
            	savedSong.setTrackLength(song.getTrackLength());
            	savedSong.setPlayCount(song.getPlayCount());
            	savedSong.setDateAdded(song.getDateAdded());
            	savedSong.setDateModified(song.getDateModified());
            	savedSong.setAlbum(song.getAlbum());
            	savedSong.setBPM(song.getBPM());
            	savedSong.setBitRate(song.getBitRate());
            	savedSong.setVariableBitRate(song.isVariableBitRate());
            	savedSong.setFileSize(song.getFileSize());
            	savedSong.setLocation(song.getLocation());
            	savedSong.setPlaylists(song.getPlaylists());
            	dataFile.writeObject(savedSong);
            }
        } catch(IOException e) {
        	System.out.println(e.getMessage());
        }
	}
	
	public void overwriteSongFile(Song song) {
		File file = new File(song.getLocation());
		AudioFile audioFile;
		try {
			audioFile = AudioFileIO.read(file);
			Tag tag = audioFile.getTag();
			tag.setField(FieldKey.ALBUM, song.getAlbumTitle());  // Album Title
			tag.setField(FieldKey.ALBUM_ARTIST, song.getAlbumArtist());  // Album Artist
			tag.setField(FieldKey.ALBUM_SORT, song.getAlbumTitleSort());  // Album Title for Sorting
			tag.setField(FieldKey.ARTIST, song.getSongArtist());  // Song Artist
			tag.setField(FieldKey.ARTIST_SORT, song.getSongArtistSort());  // Song Artist for Sorting
			tag.setField(FieldKey.BPM, song.getBPM());  // Beats Per Minute
			tag.setField(FieldKey.GENRE, song.getGenre());  // Genre
			tag.setField(FieldKey.TITLE, song.getSongTitle());  // Song Title
			tag.setField(FieldKey.TITLE_SORT, song.getSongTitleSort());  // Song Title for Sorting
			tag.setField(FieldKey.TRACK, song.getTrackNumber());  // Track Number
			tag.setField(FieldKey.TRACK_TOTAL, song.getTracksTotal());  // Total Number of Tracks
			tag.setField(FieldKey.YEAR, song.getYear());  // Year

			Artwork artwork = tag.getFirstArtwork();
			try {
				artwork.setBinaryData(song.getAlbumArtworkBytes());  // Album Artwork
				tag.deleteArtworkField();
				tag.setField(artwork);
			}	catch(Exception e){
//				System.out.println("No artwork for:" + song.getSongTitle());
			} finally {
				audioFile.setTag(tag);
				AudioFileIO.write(audioFile);
			}
			
		} catch (CannotReadException | IOException | TagException | ReadOnlyFileException | InvalidAudioFrameException | KeyNotFoundException | CannotWriteException e) {
			e.printStackTrace();
		}
	}
	
    public void loadSongs() throws IOException {
    	Path filePath = FileSystems.getDefault().getPath("data.dat");
        try (ObjectInputStream dataFile = new ObjectInputStream(new BufferedInputStream(Files.newInputStream(filePath)))) {
            boolean eof = false;
            while(!eof) {
                try {
                    SongSerializable savedSong = (SongSerializable) dataFile.readObject();
                    Song song = new Song();
                	song.setSongTitle(savedSong.getSongTitle());
                	song.setSongTitleSort(savedSong.getSongTitleSort());
                	song.setSongArtist(savedSong.getSongArtist());
                	song.setSongArtistSort(savedSong.getSongArtistSort());
                	song.setGenre(savedSong.getGenre());
                	song.setTrackNumber(savedSong.getTrackNumber());
                	song.setTrackLength(savedSong.getTrackLength());
                	song.setPlayCount(savedSong.getPlayCount());
                	song.setDateAdded(savedSong.getDateAdded());
                	song.setDateModified(savedSong.getDateModified());
                	song.setAlbum(savedSong.getAlbum());
                	song.setBPM(savedSong.getBPM());
                	song.setBitRate(savedSong.getBitRate());
                	song.setVariableBitRate(savedSong.isVariableBitRate());
                	song.setFileSize(savedSong.getFileSize());
                	song.setLocation(savedSong.getLocation());
                	song.setPlaylists(savedSong.getPlaylists());
                	if(!song.fileExists()) {
                		song.setWarning(true);
                	}
                	songsList.add(song);
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
    
    private ArrayList<Integer> generateIndexList(int startIndex, int maxIndex) {
    	ArrayList<Integer> result = new ArrayList<Integer>();
    	
    	for(int i = startIndex; i<=maxIndex; i++) {
    		result.add(startIndex + (i - startIndex));
    	}
    	if(startIndex > 0) {
    		for(int i = 0; i<startIndex; i++) {
        		result.add(i);
        	}
    	}
    	return result;
    }
    
    private ArrayList<Integer> generateShuffledIndexList(int startIndex, int maxIndex) {
    	ArrayList<Integer> result = new ArrayList<Integer>();
    	
    	for(int i = startIndex+1; i<=maxIndex; i++) {
    		result.add(startIndex + (i - startIndex));
    	}
    	if(startIndex > 0) {
    		for(int i = 0; i<startIndex; i++) {
        		result.add(i);
        	}
    	}
    	Collections.shuffle(result);
    	result.add(0, startIndex);
    	return result;
    }
    
    public PlaylistData generatePlaylists(PlaylistData playlistData) {
    	for(Song song : songsList) {
    		HashMap<String, Integer> playlistsMap = song.getPlaylists();
    		Playlist playlist;
    		int index;
    		for(String playlistName : song.getPlaylistsList()) {
    			if(playlistData.playlistExists(playlistName)) {
    				playlist = playlistData.getPlaylist(playlistName);
    			} else {
    				playlist = new Playlist(playlistName);
    				playlistData.addPlaylist(playlist);
    			}
    			index = playlistsMap.get(playlistName);
    			playlist.expandPlaylistTo(index);
				playlist.setSongAt(index, song);
    		}
    	}
    	return playlistData;
    }
}