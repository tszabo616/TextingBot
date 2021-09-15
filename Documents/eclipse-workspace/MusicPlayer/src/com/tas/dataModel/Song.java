package com.tas.dataModel;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.image.Image;

public class Song {
	private SimpleStringProperty songTitle = new SimpleStringProperty("");
	private SimpleStringProperty songTitleSort = new SimpleStringProperty("");
	private SimpleStringProperty songArtist = new SimpleStringProperty("");
	private SimpleStringProperty songArtistSort = new SimpleStringProperty("");
	private SimpleStringProperty genre = new SimpleStringProperty("");
	private SimpleStringProperty trackNumber = new SimpleStringProperty("");
	private SimpleStringProperty tracksTotal = new SimpleStringProperty("");
	private int trackLength;
	private SimpleStringProperty trackLengthFormatted = new SimpleStringProperty("");
	private SimpleIntegerProperty playCount  = new SimpleIntegerProperty();
	private LocalDateTime dateAdded;
	private LocalDateTime dateModified;
	private SimpleStringProperty dateAddedFormatted = new SimpleStringProperty("");
	private SimpleStringProperty dateModifiedFormatted = new SimpleStringProperty("");
	private Album album;
	private SimpleStringProperty albumTitle = new SimpleStringProperty("");
	private SimpleStringProperty albumTitleSort = new SimpleStringProperty("");
	private SimpleStringProperty albumArtist = new SimpleStringProperty("");
	private SimpleObjectProperty<byte[]> albumArtworkBytes = new SimpleObjectProperty<byte[]>();
	private SimpleStringProperty year = new SimpleStringProperty("");
	private SimpleStringProperty bpm = new SimpleStringProperty("");
	private SimpleStringProperty bitRate = new SimpleStringProperty();
	private boolean variableBitRate;
	private SimpleStringProperty variableBitRateFormatted = new SimpleStringProperty("");
	private SimpleStringProperty fileSize = new SimpleStringProperty("");
	private SimpleStringProperty location = new SimpleStringProperty("");
	private SimpleStringProperty warning = new SimpleStringProperty("");
	private HashMap<String, Integer> playlists;
	private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d/yyyy h:mma");
	private final static String warningSymbol = "!";
	
	public Song() {
		this.album = new Album();
		this.playCount.set(0);
		this.dateAdded = LocalDateTime.now();
		this.dateModified = LocalDateTime.now();
		this.dateAddedFormatted.set(this.dateAdded.format(formatter));
		this.dateModifiedFormatted.set(this.dateModified.format(formatter));
		this.playlists = new HashMap<String, Integer>();
	}
	
	public Song(String songTitle, String songArtist, String trackNumber, String albumTitle, String albumArtist, String genre, String year) {
		this();
		this.songTitle.set(songTitle);
		this.songArtist.set(songArtist);
		this.trackNumber.set(trackNumber);
		this.genre.set(genre);
		this.albumTitle.set(albumTitle);
		this.albumArtist.set(albumArtist);
		this.year.set(year);
		this.album = new Album(albumTitle, albumArtist, year);
	}

	public String getSongTitle() {
		return songTitle.get();
	}
	public void setSongTitle(String songTitle) {
		this.songTitle.set(songTitle);
	}
	public String getSongTitleSort() {
		return songTitleSort.get();
	}
	public void setSongTitleSort(String songTitleSort) {
		this.songTitleSort.set(songTitleSort);
	}
	public String getSongArtist() {
		return songArtist.get();
	}
	public void setSongArtist(String songArtist) {
		this.songArtist.set(songArtist);
	}
	public String getSongArtistSort() {
		return songArtistSort.get();
	}
	public void setSongArtistSort(String songArtistSort) {
		this.songArtistSort.set(songArtistSort);
	}
	public String getGenre() {
		return genre.get();
	}
	public void setGenre(String genre) {
		this.genre.set(genre);
	}
	public String getTrackNumber() {
		return trackNumber.get();
	}
	public void setTrackNumber(String trackNumber) {
		this.trackNumber.set(trackNumber);
	}
	public String getTracksTotal() {
		return tracksTotal.get();
	}
	public void setTracksTotal(String tracksTotal) {
		this.tracksTotal.set(tracksTotal);
		this.album.setTracksTotal(tracksTotal);
	}
	public String getTrackLengthFormatted() {
		return trackLengthFormatted.get();
	}
	public int getTrackLength() {
		return trackLength;
	}
	public void setTrackLength(int trackLength) {
		this.trackLength = trackLength;
		this.trackLengthFormatted.set(secondsToFormattedTime(this.trackLength));
	}
	public int getPlayCount() {
		return playCount.get();
	}
	public void setPlayCount(int playCount) {
		this.playCount.set(playCount);
	}
	public void addPlayToCount() {
		this.playCount.set(this.playCount.get() + 1);
	}
	public void resetPlayCount() {
		this.playCount.set(0);
	}
	public LocalDateTime getDateAdded() {
		return dateAdded;
	}
	public String getDateAddedFormatted() {
		return dateAddedFormatted.get();
	}
	public void setDateAdded(LocalDateTime dateAdded) {
		this.dateAdded = dateAdded;
		this.dateAddedFormatted.set(dateAdded.format(formatter));
	}
	public LocalDateTime getDateModified() {
		return dateModified;
	}
	public void setDateModified(LocalDateTime dateModified) {
		this.dateModified = dateModified;
		this.dateModifiedFormatted.set(dateModified.format(formatter));
	}
	public String getDateModifiedFormatted() {
		return dateModifiedFormatted.get();
	}
	public Album getAlbum() {
		return album;
	}
	public void setAlbum(Album album) {
		this.album = album;
		albumTitle.set(album.getAlbumTitle());
		albumTitleSort.set(album.getAlbumTitleSort());
		albumArtist.set(album.getAlbumArtist());
		albumArtworkBytes.set(album.getAlbumArtworkBytes());
		year.set(album.getYear());
		tracksTotal.set(album.getTracksTotal());
	}
	public String getAlbumTitle() {
		return albumTitle.get();
	}
	public void setAlbumTitle(String albumTitle) {
		album.setAlbumTitle(albumTitle);
		this.albumTitle.set(albumTitle);
	}
	public String getAlbumTitleSort() {
		return albumTitleSort.get();
	}
	public void setAlbumTitleSort(String albumTitleSort) {
		album.setAlbumTitleSort(albumTitleSort);
		this.albumTitleSort.set(albumTitleSort);
	}
	public String getAlbumArtist() {
		return albumArtist.get();
	}
	public void setAlbumArtist(String albumArtist) {
		album.setAlbumArtist(albumArtist);
		this.albumArtist.set(albumArtist);
	}
	public Image getAlbumArtworkImage() {
		return new Image(new ByteArrayInputStream(albumArtworkBytes.get()));
	}
	public byte[] getAlbumArtworkBytes() {
		return albumArtworkBytes.get();
	}
	public void setAlbumArtworkBytes(byte[] albumArtworkBytes) {
		this.albumArtworkBytes.set(albumArtworkBytes);
		album.setAlbumArtworkBytes(albumArtworkBytes);
	}
	public String getYear() {
		return year.get();
	}
	public void setYear(String year) {
		album.setYear(year);
		this.year.set(year);
	}
	public String getBPM() {
		return bpm.get();
	}
	public void setBPM(String bpm) {
		this.bpm.set(bpm);
	}
	public String getBitRate() {
		return bitRate.get();
	}
	public void setBitRate(String bitRate) {
		this.bitRate.set(bitRate);
	}
	public String isVariableBitRateFormatted() {
		return variableBitRateFormatted.get();
	}
	public boolean isVariableBitRate() {
		return variableBitRate;
	}
	public void setVariableBitRate(boolean variableBitRate) {
		this.variableBitRate = variableBitRate;
		if(this.variableBitRate){
			variableBitRateFormatted.set("Yes");
		} else{
			variableBitRateFormatted.set("No");
		}
	}
	public String getFileSize() {
		return fileSize.get();
	}
	public void setFileSize(String fileSize) {
		this.fileSize.set(fileSize);
	}
	public String getLocation() {
		return location.get();
	}
	public void setLocation(String location) {
		this.location.set(location);
	}
	public String getWarning() {
		return warning.get();
	}
	public void setWarning(boolean warningOn) {
		if(warningOn) {
			warning.set(warningSymbol);
		} else {
			warning.set("");
		}
	}
	public HashMap<String, Integer> getPlaylists() {
		return playlists;
	}
	public void setPlaylists(HashMap<String, Integer> playlists) {
		this.playlists = playlists;
	}
	public ObservableList<String> getPlaylistsList() {
		return FXCollections.observableArrayList(playlists.keySet());
	}
	public void addToPlaylist(Playlist playlist) {
		int index = playlist.getSongsList().size();
		this.playlists.put(playlist.getName(), index);
	}

	public boolean changed(String songTitle, String songArtist, String albumTitle, String albumArtist, byte[] albumArtworkBytes, String trackNumber, String tracksTotal,
				String bpm, String year, String genre, String songTitleSort, String songArtistSort, String albumTitleSort) {
		boolean result = false;
		
		if(!this.songTitle.get().equals(songTitle) || !this.songArtist.get().equals(songArtist) || !this.albumTitle.get().equals(albumTitle)) {
			result = true;
		} else if(!this.albumArtist.get().equals(albumArtist) || !Arrays.equals(this.albumArtworkBytes.get(), albumArtworkBytes) || !this.trackNumber.get().equals(trackNumber)){
			result = true;
		} else if(!this.tracksTotal.get().equals(tracksTotal) || !this.bpm.get().equals(bpm) || !this.year.get().equals(year) || !this.genre.get().equals(genre)){
			result = true;
		} else if(!this.songTitleSort.get().equals(songTitleSort) || !this.songArtistSort.get().equals(songArtistSort) || !this.albumTitleSort.get().equals(albumTitleSort)){
			result = true;
		}
		return result;
	}
	
	public void updateSong(String songTitle, String songArtist, String albumTitle, String albumArtist, byte[] albumArtworkBytes, String trackNumber, String tracksTotal,
			String bpm, String year, String genre, String songTitleSort, String songArtistSort, String albumTitleSort) {
		this.songTitle.set(songTitle);
		this.songArtist.set(songArtist);
		this.albumTitle.set(albumTitle);
		this.albumArtist.set(albumArtist);
		this.albumArtworkBytes.set(albumArtworkBytes);
		this.trackNumber.set(trackNumber);
		this.tracksTotal.set(tracksTotal);
		this.bpm.set(bpm);
		this.year.set(year);
		this.genre.set(genre);
		this.songTitleSort.set(songTitleSort);
		this.songArtistSort.set(songArtistSort);
		this.albumTitleSort.set(albumTitleSort);
		
		this.album.updateAlbum(albumTitle, albumArtist, year);
		this.album.setTracksTotal(tracksTotal);
		this.album.setAlbumTitleSort(albumTitleSort);
		this.album.setAlbumArtworkBytes(albumArtworkBytes);
		this.dateModified = LocalDateTime.now();
		this.dateModifiedFormatted.set(this.dateModified.format(formatter));
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((songArtist.get() == null) ? 0 : songArtist.get().hashCode());
		result = prime * result + ((songTitle.get() == null) ? 0 : songTitle.get().hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Song other = (Song) obj;
		if (songArtist.get() == null) {
			if (other.songArtist.get() != null)
				return false;
		} else if (!songArtist.get().equals(other.songArtist.get()))
			return false;
		if (songTitle.get() == null) {
			if (other.songTitle.get() != null)
				return false;
		} else if (!songTitle.get().equals(other.songTitle.get()))
			return false;
		return true;
	}
	
//	@Override
//	public int compareTo(Song s) {
//		if(this.album.equals(s.album)) {  // Same Artist & Same Album --> Compare Track Number
//			return this.trackNumber - s.trackNumber;
//		} else {
//			if(this.songArtist == s.songArtist) {  // Same Artist --> Compare Song Title
//				return this.songTitle.toLowerCase().compareTo(s.songTitle.toLowerCase());
//			} else {  // Artists not the same --> Compare Artist
//				return this.songArtist.toLowerCase().compareTo(s.songArtist.toLowerCase());
//			}
//		}
//		return this.songTitle.toLowerCase().compareTo(s.songTitle.toLowerCase());
//	}

	@Override
	public String toString() {
		return "Song Title: " + this.songTitle.get() + "\n" +
				"Song Artist: " + this.songArtist.get() + "\n" +
				"Album Title: " + this.albumTitle.get() + "\n" +
				"Track Number: " + this.trackNumber.get() + "\n" +
				"Year: " + this.year.get() + "\n" +
				"Play Count: " + playCount.get();
	}
	
	public boolean fileExists() {
		return (new File(location.get())).exists();
	}
	
	private String secondsToFormattedTime(int seconds) {
		int hours = seconds/3600;
		int minutes = (seconds - hours*3600)/60;
		int rem_seconds = seconds - hours*3600 - minutes*60;
		String minutes_string;
		String seconds_string;
		if(minutes < 10) {
			minutes_string = "0" + Integer.toString(minutes);
		} else {
			minutes_string = Integer.toString(minutes);
		}
		if(rem_seconds < 10) {
			seconds_string = "0" + Integer.toString(rem_seconds);
		} else {
			seconds_string = Integer.toString(rem_seconds);
		}
		if(hours == 0) {
			return minutes_string + ":" + seconds_string; 
		} else {
			return Integer.toString(hours) + ":" + minutes_string + ":" + seconds_string; 
		}
	}

}
