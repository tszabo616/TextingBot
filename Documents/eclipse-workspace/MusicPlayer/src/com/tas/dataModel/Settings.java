package com.tas.dataModel;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;

import javafx.collections.ObservableList;

public class Settings implements Serializable {
	private static Settings instance = new Settings();
	private static String filename = "settings.dat";
	
	private Theme theme;
	private String defaultFileDir;
	
	private static String lightThemeFile = "/com/tas/application/LightTheme.css";
	private static String darkThemeFile = "/com/tas/application/DarkTheme.css";
	
	private boolean songTitleColVisible;
	private boolean songArtistColVisible;
	private boolean albumTitleColVisible;
	private boolean dateModifiedColVisible;
	private boolean dateAddedColVisible;
	private boolean timeColVisible;
	private boolean genreColVisible;
	private boolean yearColVisible;
	private boolean playsColVisible;
	private static final long serialVersionUID = 1L;  // for Serialization
	
	public static enum Theme {
	    LIGHT,
	    DARK
	}
	
	private Settings() {
		theme = Theme.LIGHT;
		defaultFileDir = "";
		songTitleColVisible = true;
		songArtistColVisible = true;
		albumTitleColVisible = true;
		dateModifiedColVisible = true;
		dateAddedColVisible = true;
		timeColVisible = true;
		genreColVisible = true;
		yearColVisible = true;
		playsColVisible = true;
	}
	
	public static Settings getInstance() {
		return instance;
	}
	
	public Theme getTheme() {
		return theme;
	}
	public void setTheme(Theme theme) {
		this.theme = theme;
	}
	public String getDefaultFileDir() {
		return defaultFileDir;
	}
	public void setDefaultFileDir(String defaultFileDir) {
		this.defaultFileDir = defaultFileDir;
	}
	public String getThemeFile() {
		if(theme == Theme.LIGHT) {
			return lightThemeFile;
		} else {
			return darkThemeFile;
		}
	}
	public boolean isSongTitleColVisible() {
		return songTitleColVisible;
	}
	public void setSongTitleColVisible(boolean songTitleColVisible) {
		this.songTitleColVisible = songTitleColVisible;
	}
	public boolean isSongArtistColVisible() {
		return songArtistColVisible;
	}
	public void setSongArtistColVisible(boolean songArtistColVisible) {
		this.songArtistColVisible = songArtistColVisible;
	}
	public boolean isAlbumTitleColVisible() {
		return albumTitleColVisible;
	}
	public void setAlbumTitleColVisible(boolean albumTitleColVisible) {
		this.albumTitleColVisible = albumTitleColVisible;
	}
	public boolean isDateModifiedColVisible() {
		return dateModifiedColVisible;
	}
	public void setDateModifiedColVisible(boolean dateModifiedColVisible) {
		this.dateModifiedColVisible = dateModifiedColVisible;
	}
	public boolean isDateAddedColVisible() {
		return dateAddedColVisible;
	}
	public void setDateAddedColVisible(boolean dateAddedColVisible) {
		this.dateAddedColVisible = dateAddedColVisible;
	}
	public boolean isTimeColVisible() {
		return timeColVisible;
	}
	public void setTimeColVisible(boolean timeColVisible) {
		this.timeColVisible = timeColVisible;
	}
	public boolean isGenreColVisible() {
		return genreColVisible;
	}
	public void setGenreColVisible(boolean genreColVisible) {
		this.genreColVisible = genreColVisible;
	}
	public boolean isYearColVisible() {
		return yearColVisible;
	}
	public void setYearColVisible(boolean yearColVisible) {
		this.yearColVisible = yearColVisible;
	}
	public boolean isPlaysColVisible() {
		return playsColVisible;
	}
	public void setPlaysColVisible(boolean playsColVisible) {
		this.playsColVisible = playsColVisible;
	}
	
	@Override
	public String toString() {
		return "Settings [theme=" + theme + "]";
	}

	public void saveSettings() throws IOException {
		System.out.println("Saving settings.");
		Path filePath = FileSystems.getDefault().getPath(filename);
        try (ObjectOutputStream dataFile = new ObjectOutputStream(new BufferedOutputStream(Files.newOutputStream(filePath)))) {
        	dataFile.writeObject(instance);
        } catch(IOException e) {
        	System.out.println(e.getMessage());
        }
	}
	
	public void loadSettings() throws IOException {
    	Path filePath = FileSystems.getDefault().getPath(filename);
        try (ObjectInputStream dataFile = new ObjectInputStream(new BufferedInputStream(Files.newInputStream(filePath)))) {
            boolean eof = false;
            while(!eof) {
                try {
                    instance = (Settings) dataFile.readObject();
                } catch(EOFException e) {
                    eof = true;
                }
            }
        } catch(InvalidClassException e) {
            System.out.println("InvalidClassException " + e.getMessage());
            e.printStackTrace();
        } catch(IOException e) {
            System.out.println("IOException " + e.getMessage());
            e.printStackTrace();
            saveSettings();
        } catch(ClassNotFoundException e) {
            System.out.println("ClassNotFoundException " + e.getMessage());
            e.printStackTrace();
        } 
    }
	
	public void applyTheme(ObservableList<String> styleSheets) {
		styleSheets.clear();
		styleSheets.add(getThemeFile());
	}
}
