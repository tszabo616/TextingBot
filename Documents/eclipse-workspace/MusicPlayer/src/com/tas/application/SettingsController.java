package com.tas.application;

import java.io.File;

import com.tas.dataModel.Settings;
import com.tas.dataModel.Song;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.layout.BorderPane;
import javafx.stage.DirectoryChooser;

public class SettingsController {
	private BorderPane mainBorderPane;
	@FXML RadioButton lightThemeButton;
	@FXML RadioButton darkThemeButton;
	@FXML Label defaultFileDir;
	@FXML CheckBox songTitleColVisibleBox;
	@FXML CheckBox songArtistColVisibleBox;
	@FXML CheckBox albumTitleColVisibleBox;
	@FXML CheckBox dateModifiedColVisibleBox;
	@FXML CheckBox dateAddedColVisibleBox;
	@FXML CheckBox timeColVisibleBox;
	@FXML CheckBox genreColVisibleBox;
	@FXML CheckBox yearColVisibleBox;
	@FXML CheckBox playsColVisibleBox;
	
	public BorderPane getMainBorderPane() {
		return mainBorderPane;
	}
	public void setMainBorderPane(BorderPane mainBorderPane) {
		this.mainBorderPane = mainBorderPane;
	}

	public void editTheme(Settings.Theme theme) {
		switch (theme) {
		    case LIGHT : 
		    	lightThemeButton.setSelected(true);
		    	break;
		    case DARK : 
		    	darkThemeButton.setSelected(true);
		    	break;
		}
	}
	
	public void editDefaultFileDir(Settings settings) {
		defaultFileDir.setText(settings.getDefaultFileDir());
	}
	
	public void editColVisibility(Settings settings) {
		songTitleColVisibleBox.setSelected(settings.isSongTitleColVisible());
		songArtistColVisibleBox.setSelected(settings.isSongArtistColVisible());
		albumTitleColVisibleBox.setSelected(settings.isAlbumTitleColVisible());
		dateModifiedColVisibleBox.setSelected(settings.isDateModifiedColVisible());
		dateAddedColVisibleBox.setSelected(settings.isDateAddedColVisible());
		timeColVisibleBox.setSelected(settings.isTimeColVisible());
		genreColVisibleBox.setSelected(settings.isGenreColVisible());
		yearColVisibleBox.setSelected(settings.isYearColVisible());
		playsColVisibleBox.setSelected(settings.isPlaysColVisible());
	}
	
	public void updateColVisibility(Settings settings, TableColumn<Song, String> songTitleCol, TableColumn<Song, String> songArtistCol, TableColumn<Song, String> albumTitleCol,
			TableColumn<Song, String> dateModifiedCol, TableColumn<Song, String> dateAddedCol, TableColumn<Song, String> timeCol, TableColumn<Song, String> genreCol, 
			TableColumn<Song, String> yearCol, TableColumn<Song, String> playsCol) {
		songTitleCol.setVisible(songTitleColVisibleBox.isSelected());
		songArtistCol.setVisible(songArtistColVisibleBox.isSelected());
		albumTitleCol.setVisible(albumTitleColVisibleBox.isSelected());
		dateModifiedCol.setVisible(dateModifiedColVisibleBox.isSelected());
		dateAddedCol.setVisible(dateAddedColVisibleBox.isSelected());
		timeCol.setVisible(timeColVisibleBox.isSelected());
		genreCol.setVisible(genreColVisibleBox.isSelected());
		yearCol.setVisible(yearColVisibleBox.isSelected());
		playsCol.setVisible(playsColVisibleBox.isSelected());
		
		settings.setSongTitleColVisible(songTitleColVisibleBox.isSelected());
		settings.setSongArtistColVisible(songArtistColVisibleBox.isSelected());
		settings.setAlbumTitleColVisible(albumTitleColVisibleBox.isSelected());
		settings.setDateModifiedColVisible(dateModifiedColVisibleBox.isSelected());
		settings.setDateAddedColVisible(dateAddedColVisibleBox.isSelected());
		settings.setTimeColVisible(timeColVisibleBox.isSelected());
		settings.setGenreColVisible(genreColVisibleBox.isSelected());
		settings.setYearColVisible(yearColVisibleBox.isSelected());
		settings.setPlaysColVisible(playsColVisibleBox.isSelected());
	}
	
	public void setTheme() {
		if(lightThemeButton.isSelected()) {
			Settings.getInstance().setTheme(Settings.Theme.LIGHT);
		} else {
			Settings.getInstance().setTheme(Settings.Theme.DARK);
		}
	}
	
	public void setDefaultFileDir(Settings settings) {
		settings.setDefaultFileDir(defaultFileDir.getText());
	}
	
	@FXML
	public void handleSetDefaultFileDir() {
		DirectoryChooser dirChooser = new DirectoryChooser();
		dirChooser.setTitle("Select Default Directory");
		dirChooser.setInitialDirectory(new File(Settings.getInstance().getDefaultFileDir()).getParentFile());
		File dir = dirChooser.showDialog(mainBorderPane.getScene().getWindow());
		if(dir != null) {
			defaultFileDir.setText(dir.getAbsolutePath());
		}
	}
}
