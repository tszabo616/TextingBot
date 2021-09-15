package com.tas.dataModel;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashMap;

public class SongSerializable implements Serializable {
	private String songTitle;
	private String songTitleSort;
	private String songArtist;
	private String songArtistSort;
	private String genre;
	private String trackNumber;
	private int trackLength;
	private int playCount;
	private LocalDateTime dateAdded;
	private LocalDateTime dateModified;
	private Album album;
	private String bpm;
	private String bitRate;
	private boolean variableBitRate;
	private String fileSize;
	private String location;
	private HashMap<String, Integer> playlists;
	private static final long serialVersionUID = 1L;  // for Serialization
	
	public SongSerializable() {
	}
	
	public String getSongTitle() {
		return songTitle;
	}
	public void setSongTitle(String songTitle) {
		this.songTitle = songTitle;
	}
	public String getSongTitleSort() {
		return songTitleSort;
	}
	public void setSongTitleSort(String songTitleSort) {
		this.songTitleSort = songTitleSort;
	}
	public String getSongArtist() {
		return songArtist;
	}
	public void setSongArtist(String songArtist) {
		this.songArtist = songArtist;
	}
	public String getSongArtistSort() {
		return songArtistSort;
	}
	public void setSongArtistSort(String songArtistSort) {
		this.songArtistSort = songArtistSort;
	}
	public String getGenre() {
		return genre;
	}
	public void setGenre(String genre) {
		this.genre = genre;
	}
	public String getTrackNumber() {
		return trackNumber;
	}
	public void setTrackNumber(String trackNumber) {
		this.trackNumber = trackNumber;
	}
	public int getTrackLength() {
		return trackLength;
	}
	public void setTrackLength(int trackLength) {
		this.trackLength = trackLength;
	}
	public int getPlayCount() {
		return playCount;
	}
	public void setPlayCount(int playCount) {
		this.playCount = playCount;
	}
	public LocalDateTime getDateAdded() {
		return dateAdded;
	}
	public void setDateAdded(LocalDateTime dateAdded) {
		this.dateAdded = dateAdded;
	}
	public LocalDateTime getDateModified() {
		return dateModified;
	}
	public void setDateModified(LocalDateTime dateModified) {
		this.dateModified = dateModified;
	}
	public Album getAlbum() {
		return album;
	}
	public void setAlbum(Album album) {
		this.album = album;
	}
	public String getBPM() {
		return bpm;
	}
	public void setBPM(String bpm) {
		this.bpm = bpm;
	}
	public String getBitRate() {
		return bitRate;
	}
	public void setBitRate(String bitRate) {
		this.bitRate = bitRate;
	}
	public boolean isVariableBitRate() {
		return variableBitRate;
	}
	public void setVariableBitRate(boolean variableBitRate) {
		this.variableBitRate = variableBitRate;
	}
	public String getFileSize() {
		return fileSize;
	}
	public void setFileSize(String fileSize) {
		this.fileSize = fileSize;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public HashMap<String, Integer> getPlaylists() {
		return playlists;
	}
	public void setPlaylists(HashMap<String, Integer> playlists) {
		this.playlists = playlists;
	}
}
