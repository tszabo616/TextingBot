package com.tas.application;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.function.Predicate;

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

import com.tas.dataModel.Settings;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

public class DownloadSongsController {
	private BorderPane mainBorderPane;
	private File destinationFolder;
	private File csvFile;
	private List<List<String>> csvList;
	private Task<Void> youtubeTask;
	@FXML
	private DialogPane downloadDialog;
	@FXML
	private GridPane downloadGridPane;
	@FXML
	private Label destinationFolderLabel;
	@FXML
	private Label csvFileLabel;
	@FXML
	private ProgressBar progressBar;

	public void intialize() {
	}
	
	public DialogPane getDownloadDialog() {
		return downloadDialog;
	}
	public GridPane getDownloadGridPane() {
		return downloadGridPane;
	}
	public void setMainBorderPane(BorderPane mainBorderPane) {
		this.mainBorderPane = mainBorderPane;
	}
	
	@FXML
	public void handleDownloadTemplate() {
		DirectoryChooser dirChooser = new DirectoryChooser();
		dirChooser.setTitle("Select Destination Folder");
		File folder = dirChooser.showDialog(mainBorderPane.getScene().getWindow());
		Path sourcePath = FileSystems.getDefault().getPath("DownloadMusicTemplate.csv");
		if (folder != null) {
			String targetFileLocation = folder.getAbsolutePath() + "\\" + sourcePath.getFileName();
			File targetFile = new File(targetFileLocation);
			try {
				Files.copy(sourcePath, targetFile.toPath(), StandardCopyOption.COPY_ATTRIBUTES);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	@FXML
	public void handleSelectFolder() {
		DirectoryChooser dirChooser = new DirectoryChooser();
		dirChooser.setTitle("Select Folder");
		destinationFolder = dirChooser.showDialog(mainBorderPane.getScene().getWindow());
		if (destinationFolder != null) destinationFolderLabel.setText(destinationFolder.getAbsolutePath());
	}
	
	@FXML
	private void handleSelectFile() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Select CSV File");
		fileChooser.getExtensionFilters().addAll(new ExtensionFilter("CSV", "*.csv"));
		fileChooser.setInitialDirectory(new File(Settings.getInstance().getDefaultFileDir()));
		csvFile = fileChooser.showOpenDialog(mainBorderPane.getScene().getWindow());
		if (csvFile != null) csvFileLabel.setText(csvFile.getAbsolutePath());
	}
	
	@FXML
	public void handleDownloadSongs() {
		if (destinationFolder == null) {
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			alert.setTitle("No Destination Folder Selected");
			alert.setHeaderText(null);
			alert.setContentText("Please select a folder.");
			alert.showAndWait();
			return;
		}
		if (csvFile == null) {
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			alert.setTitle("No CSV File Selected");
			alert.setHeaderText(null);
			alert.setContentText("Please select a file.");
			alert.showAndWait();
			return;
		}
		
		csvList = YoutubeToMP3.convertCSV(csvFile.getAbsolutePath());
		Predicate<List<String>> blankRow = row -> (row.size() < 1); 
		csvList.removeIf(blankRow);
		
		youtubeTask = new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				List<String> csvRow;
				String filePath, songTitle, songArtist, albumArtist, albumTitle;
				String year, trackNumber, genre, fileName, youtubeURL;
				File songFile;
				long count = 0;
				updateProgress(0.0, 1.0);

				for (int i = 1; i < csvList.size(); i++) {
					csvRow = csvList.get(i);
					if (csvRow.size() > 0) {
						songTitle = csvRow.get(0);
						songArtist = csvRow.get(1);
						albumArtist = csvRow.get(2);
						albumTitle = csvRow.get(3);
						year = csvRow.get(4);
						trackNumber = csvRow.get(5);
						genre = csvRow.get(6);
						fileName = csvRow.get(7) + ".mp3";
						youtubeURL = csvRow.get(8);
						filePath = destinationFolder.getAbsolutePath() + "\\" + fileName;
						
						try {
							YoutubeToMP3.generateFile(youtubeURL, filePath);
							songFile = new File(filePath);
//							long sleepCount = 0;
//							while (!songFile.exists()) {
//								try {
//									Thread.sleep(100);
//									sleepCount += 100;
//									if(sleepCount>5000) break;
//								} catch (InterruptedException e) {
//									e.printStackTrace();
//								}
//							}
							System.out.println("Downloaded Song: " + fileName);
							updateSongFile(songFile, songTitle, songArtist, albumArtist, albumTitle, year, trackNumber, genre);
						} catch(Exception e) {
							System.out.println("Could Not Download: " + fileName);
						}
						finally {
							count++;
							updateProgress((double)count/(csvList.size() - 1), 1.0);
						}
					}
				}
				updateProgress(1.0, 1.0);
				return null;
			}
		};
		progressBar.progressProperty().bind(youtubeTask.progressProperty());
		progressBar.progressProperty().addListener((obs, oldVal, newVal) -> {
			if(newVal.doubleValue() == 0.0) {
				progressBar.setMinHeight(10.0);
				progressBar.setPrefHeight(10.0);
				progressBar.setMaxHeight(10.0);
				progressBar.setVisible(true);
			} else if(newVal.doubleValue() >= 1.0) {
				progressBar.setMinHeight(0.0);
				progressBar.setPrefHeight(0.0);
				progressBar.setMaxHeight(0.0);
				progressBar.setVisible(false);
			}
		});
		new Thread(youtubeTask).start();
	}
	
	private void updateSongFile(File songFile, String songTitle, String songArtist, String albumArtist, String albumTitle, String year, String trackNumber, String genre) {
		AudioFile audioFile;
		Tag tag;
		try {
			audioFile = AudioFileIO.read(songFile);
			tag = audioFile.getTag();
			tag.setField(FieldKey.ALBUM, albumTitle);  // Album Title
			tag.setField(FieldKey.ALBUM_ARTIST, albumArtist);  // Album Artist
			tag.setField(FieldKey.ALBUM_SORT, albumTitle);  // Album Title for Sorting
			tag.setField(FieldKey.ARTIST, songArtist);  // Song Artist
			tag.setField(FieldKey.ARTIST_SORT, songArtist);  // Song Artist for Sorting
			tag.setField(FieldKey.GENRE, genre);  // Genre
			tag.setField(FieldKey.TITLE, songTitle);  // Song Title
			tag.setField(FieldKey.TITLE_SORT, songTitle);  // Song Title for Sorting
			tag.setField(FieldKey.TRACK, trackNumber);  // Track Number
			tag.setField(FieldKey.YEAR, year);  // Year
			AudioFileIO.write(audioFile);
		} catch (CannotReadException | IOException | TagException | ReadOnlyFileException| InvalidAudioFrameException | KeyNotFoundException | CannotWriteException e) {
			e.printStackTrace();
		}
	}
}
