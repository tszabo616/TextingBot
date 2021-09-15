package com.tas.application;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.AudioHeader;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagException;

import com.tas.dataModel.Playlist;
import com.tas.dataModel.PlaylistData;
import com.tas.dataModel.Settings;
import com.tas.dataModel.Song;
import com.tas.dataModel.SongData;

import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Slider;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.media.MediaPlayer;
import javafx.scene.shape.Circle;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.WindowEvent;
import javafx.util.Callback;
import javafx.util.StringConverter;

public class Controller {
	private SongData data;
	private PlaylistData playlists;
	private AudioPlayer audioPlayer;
	private HashMap<String, String> genresMap;
	private ListType listType;
	private SimpleStringProperty activeList = new SimpleStringProperty("");
	
	@FXML private BorderPane mainBorderPane;
	@FXML private Label musicLabel;
	@FXML private Label playlistsLabel;
	@FXML private TableView<Song> songsTableView;
	@FXML private TableColumn<Song, String> rowNumberCol;
	@FXML private TableColumn<Song, String> warningCol;
	@FXML private TableColumn<Song, String> songTitleCol;
	@FXML private TableColumn<Song, String> songArtistCol;
	@FXML private TableColumn<Song, String> albumTitleCol;
	@FXML private TableColumn<Song, String> dateModifiedCol;
	@FXML private TableColumn<Song, String> dateAddedCol;
	@FXML private TableColumn<Song, String> timeCol;
	@FXML private TableColumn<Song, String> genreCol;
	@FXML private TableColumn<Song, String> yearCol;
	@FXML private TableColumn<Song, String> playsCol;
	@FXML private Label songsCount;
	@FXML private Menu addSongToPlaylistContextMenu;
	@FXML private HBox songsCountBox;

	@FXML private ContextMenu songContextMenu;
	@FXML private ContextMenu songColContextMenu;
	@FXML CheckBox songTitleColVisibleCM;
	@FXML CheckBox songArtistColVisibleCM;
	@FXML CheckBox albumTitleColVisibleCM;
	@FXML CheckBox dateModifiedColVisibleCM;
	@FXML CheckBox dateAddedColVisibleCM;
	@FXML CheckBox timeColVisibleCM;
	@FXML CheckBox genreColVisibleCM;
	@FXML CheckBox yearColVisibleCM;
	@FXML CheckBox playsColVisibleCM;
	
	@FXML private Button playButton;
	@FXML private ToggleButton shuffleButton;
	@FXML private ToggleButton repeatButton;
	@FXML private Button prevButton;
	@FXML private Button nextButton;
	@FXML private Slider timeSlider;
	@FXML private Slider volSlider;
	@FXML private Label currentTime;
	@FXML private Label totalTime;
	@FXML private Label currentSongTitle;
	@FXML private Label currentSongArtist;
	@FXML private ListView<Playlist> playlistListView;
	
	@FXML private Label YoutubeLabel;
	@FXML private WebView webView;
	
	private Image playButtonImage;
	private Image pauseButtonImage;
	
	private static enum ListType {
		SONGSLIST,
		PLAYLIST
	}

	public void initialize() {
		// Load from files ==============================================
		data = new SongData();
		try {
			data.loadSongs();
			Settings.getInstance().loadSettings();
			genresMap = new HashMap<String, String>();
			setGenresMap();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		listType = Controller.ListType.SONGSLIST;
		activeList.set(data.getName());
		
		// Playlists ===============================================
		playlists = new PlaylistData();
		try {
			playlists.loadPlaylists();
		} catch (IOException e) {
			e.printStackTrace();
		}
		playlists = data.generatePlaylists(playlists);
		
		playlistListView.setItems(playlists.getList());
		playlistListView.setEditable(true);
		playlistListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		
		playlistListView.setCellFactory(new Callback<ListView<Playlist>, ListCell<Playlist>>() {
			@Override
			public TextFieldListCell<Playlist> call(ListView<Playlist> param) {
				TextFieldListCell<Playlist> cell = new TextFieldListCell<Playlist>() {
					@Override
					public void updateItem(Playlist playlist, boolean empty) {
						super.updateItem(playlist, empty);
						if (empty) {
							setText(null);
						} else {
							setText(playlist.getName());
						}
					}
				};
				StringConverter<Playlist> stringConverter = new StringConverter<Playlist>() {
					@Override
					public String toString(Playlist object) {
						return object.getName();
					}

					@Override
					public Playlist fromString(String string) {
						Playlist playlist = cell.getItem();
						if(playlist == null) {
							Playlist newPlaylist = new Playlist(string);
							return newPlaylist;
						} else {
							playlist.setName(string);
							return playlist;
						}
					}
				};
				cell.setEditable(true);
				cell.setConverter(stringConverter);
				return cell;
			}
		});
		
		playlistListView.setFixedCellSize(25.0);
		playlistListView.setPrefHeight(playlistListView.getFixedCellSize()*playlistListView.getItems().size() + 2.0);
		playlistListView.getItems().addListener(new ListChangeListener<Playlist>() {
			@Override
			public void onChanged(Change<? extends Playlist> arg0) {
				playlistListView.setPrefHeight(playlistListView.getFixedCellSize()*playlistListView.getItems().size() + 2.0);
			}
		});

		playlistListView.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			// Set tableview to view playlist
			@Override
			public void handle(MouseEvent t) {
				if (t.getButton() == MouseButton.PRIMARY && t.getClickCount() == 1) {
					Playlist playlist = playlistListView.getSelectionModel().getSelectedItem();
					songsTableView.setItems(playlist.getSongsList());
					songsTableView.refresh();
					listType = Controller.ListType.PLAYLIST;
					activeList.set(playlist.getName());
				}
			}
		});
		
		// Labels ====================================================
		musicLabel.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			// Set tableview to view full song list
			@Override
			public void handle(MouseEvent t) {
				if (t.getButton() == MouseButton.PRIMARY) {
					songsTableView.setItems(data.getSongsList());
					songsTableView.refresh();
					playlistListView.getSelectionModel().clearSelection();
					listType = Controller.ListType.SONGSLIST;
					activeList.set(data.getName());
					
					songsTableView.setVisible(true);
					songsTableView.setPrefHeight(mainBorderPane.getCenter().getLayoutBounds().getMaxY() - songsCountBox.getLayoutBounds().getMaxY());
					webView.setVisible(false);
					webView.setPrefHeight(0.0);
					webView.setMinHeight(0.0);
				}
			}
		});
		playlistsLabel.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			// Clear playlist listview selection
			@Override
			public void handle(MouseEvent t) {
				if (t.getButton() == MouseButton.PRIMARY) {
					playlistListView.getSelectionModel().clearSelection();
					songsTableView.setVisible(true);
					songsTableView.setPrefHeight(mainBorderPane.getCenter().getLayoutBounds().getMaxY() - songsCountBox.getLayoutBounds().getMaxY());
					webView.setVisible(false);
					webView.setPrefHeight(0.0);
					webView.setMinHeight(0.0);
				}
			}
		});
		
		YoutubeLabel.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			// 
			@Override
			public void handle(MouseEvent t) {
				if (t.getButton() == MouseButton.PRIMARY) {
					webView.setVisible(true);
					webView.setPrefHeight(mainBorderPane.getCenter().getLayoutBounds().getMaxY() - songsCountBox.getLayoutBounds().getMaxY());
					songsTableView.setVisible(false);
					songsTableView.setPrefHeight(0.0);
					songsTableView.setMinHeight(0.0);
					WebEngine webEngine = webView.getEngine();
					webEngine.load("http://youtube.com");
					
				}
			}
		});
		
		// TableView ====================================================
		songsTableView.setItems(data.getSongsList());
		songsTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		songsTableView.getSortOrder().add(songArtistCol);
		
		mainBorderPane.centerProperty().get().layoutBoundsProperty().addListener(new ChangeListener<Bounds>() {
			@Override
			public void changed(ObservableValue<? extends Bounds> arg0, Bounds arg1, Bounds arg2) {
				// Update the height of songsTableView according changing height of Center panel
				if(songsTableView.isVisible()) {
					songsTableView.setPrefHeight(mainBorderPane.getCenter().getLayoutBounds().getMaxY() - songsCountBox.getLayoutBounds().getMaxY());
				}
			}
		});
		
		songsCount.setText(Integer.toString(songsTableView.getItems().size()));
		songsTableView.getItems().addListener(new ListChangeListener<Song>() {
			@Override
			public void onChanged(Change<? extends Song> arg0) {
				// Change to song count that is displayed by listening to number of items in songsTableView
				songsCount.setText(Integer.toString(songsTableView.getItems().size()));
			}
	 
		});
		
		rowNumberCol.setCellValueFactory(new Callback<CellDataFeatures<Song, String>, ObservableValue<String>>(){
			@Override public ObservableValue<String> call(CellDataFeatures<Song, String> p) {
				return new ReadOnlyObjectWrapper<String>(songsTableView.getItems().indexOf(p.getValue()) + 1 + "");
			}
		});
		songTitleCol.setVisible(Settings.getInstance().isSongTitleColVisible());
		songArtistCol.setVisible(Settings.getInstance().isSongArtistColVisible());
		albumTitleCol.setVisible(Settings.getInstance().isAlbumTitleColVisible());
		dateModifiedCol.setVisible(Settings.getInstance().isDateModifiedColVisible());
		dateAddedCol.setVisible(Settings.getInstance().isDateAddedColVisible());
		timeCol.setVisible(Settings.getInstance().isTimeColVisible());
		genreCol.setVisible(Settings.getInstance().isGenreColVisible());
		yearCol.setVisible(Settings.getInstance().isYearColVisible());
		playsCol.setVisible(Settings.getInstance().isPlaysColVisible());
		
		songsTableView.setOnContextMenuRequested(new EventHandler<ContextMenuEvent>() {
			// Prevent table view context menu from showing during right click on table view column header
			@Override
		        public void handle(ContextMenuEvent event) {
		    	   if (event.getSceneY() < 50.0) {
		    		   songContextMenu.hide();
		    	   }
		        }
		});
		
		songsTableView.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			// Show table view column header context menu
			@Override
			public void handle(MouseEvent t) {
				if (t.getButton() == MouseButton.SECONDARY && t.getSceneY() < 50.0) {
					songColContextMenu.show(songsTableView, t.getScreenX(), t.getScreenY());
				}
			}
		});
		
		songsTableView.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			// Play selected song on double left click
			@Override
			public void handle(MouseEvent t) {
				if (t.getButton() == MouseButton.PRIMARY && t.getClickCount() == 2) {
					handlePlaySong();
				}
			}
		});
		
		songColContextMenu.setOnShowing(new EventHandler<WindowEvent>() {
		    public void handle(WindowEvent e) {
		    	editColVisibility();
		    }
		});
		
		songContextMenu.setOnShown(new EventHandler<WindowEvent>() {
			@Override
	        public void handle(WindowEvent event) {
				addSongToPlaylistContextMenu.getItems().clear();
				if(playlists.getList().size() > 0) {
					Song selectedSong = songsTableView.getSelectionModel().getSelectedItem();
					List<String> songPlaylistList = selectedSong.getPlaylistsList();
					MenuItem item;
					ObservableList<MenuItem> list = FXCollections.observableArrayList();
					for(int i=0; i<playlists.getList().size(); i++) {
						Playlist playlist = playlists.getList().get(i);
						if(!songPlaylistList.contains(playlist.getName())) {
							item = new MenuItem();
							item.setText(playlist.getName());
							item.setOnAction(new EventHandler<ActionEvent>(){
								@Override
								public void handle(ActionEvent event) {
									selectedSong.addToPlaylist(playlist);
									playlist.addSong(selectedSong);
								}
								
							});
							list.add(item);
						}
					}
					addSongToPlaylistContextMenu.getItems().addAll(list);
				}
	        }
		});
		
		// AudioPlayer ===============================================
		audioPlayer = new AudioPlayer();
		playButton.setShape(new Circle(20.0));
		playButtonImage = new Image("file:media\\PlayButton20.png");
		pauseButtonImage = new Image("file:media\\PauseButton20.png");
		playButton.setGraphic(new ImageView(playButtonImage));
		shuffleButton.setGraphic(new ImageView(new Image("file:media\\ShuffleButton9.png")));
		prevButton.setGraphic(new ImageView(new Image("file:media\\PrevButton20.png")));
		nextButton.setGraphic(new ImageView(new Image("file:media\\NextButton20.png")));
		repeatButton.setGraphic(new ImageView(new Image("file:media\\RepeatButton9.png")));
		
		audioPlayer.setData(data);
		audioPlayer.setSongsTableView(songsTableView);
		audioPlayer.setCurrentSongTitle(currentSongTitle);
		audioPlayer.setCurrentSongArtist(currentSongArtist);
		audioPlayer.setTimeSlider(timeSlider);
		audioPlayer.setCurrentTimeLabel(currentTime);
		audioPlayer.setTotalTimeLabel(totalTime);
		audioPlayer.setPlayButton(playButton);
		audioPlayer.setRepeatButton(repeatButton);
		
		timeSlider.valueProperty().addListener(new InvalidationListener() { 
			public void invalidated(Observable ov) {
		        if (timeSlider.isPressed() && audioPlayer.getSong() != null) { // Sets the time as specified by user by pressing 
		        	audioPlayer.getMediaPlayer().seek(audioPlayer.getMediaPlayer().getMedia().getDuration().multiply(timeSlider.getValue() / 100)); 
		        }
		    }

    	});
		
		volSlider.valueProperty().addListener(new InvalidationListener() { 
			public void invalidated(Observable ov) {
				if (volSlider.isPressed() && audioPlayer.getSong() != null) { // Sets the volume as specified by user by pressing 
		        	audioPlayer.getMediaPlayer().setVolume(volSlider.getValue()); 
		        }
		    }
    	});
		
		// WebView ====================================================
		webView.setVisible(false);
		webView.setPrefHeight(0.0);
		mainBorderPane.centerProperty().get().layoutBoundsProperty().addListener(new ChangeListener<Bounds>() {
			@Override
			public void changed(ObservableValue<? extends Bounds> arg0, Bounds arg1, Bounds arg2) {
				// Update the height of webView according changing height of Center panel
				if(webView.isVisible()) {
					webView.setPrefHeight(mainBorderPane.getCenter().getLayoutBounds().getMaxY() - songsCountBox.getLayoutBounds().getMaxY());
				}
			}
		});
		
//		Iterator<Song> value = data.iterator();
//		while (value.hasNext()) {
//			System.out.println(value.next());
//			System.out.println("=========================");
//		}

//		Comparator<Song> sortAlbumsDescending = new Comparator<Song>(){
//			@Override
//			public int compare(Song s1, Song s2) {
//				if(s1.getAlbum().equals(s2.getAlbum())) {  // Same Album --> Compare Track Number
//					if(s1.getTrackNumber() == s2.getTrackNumber()) {  // Same Track Number --> Compare Song Title
//						return s1.getSongTitle().toLowerCase().compareTo(s2.getSongTitle().toLowerCase());
//					} else {
//						return s1.getTrackNumber() - s2.getTrackNumber();
//					}
//				} else {  // Same Album Title --> Compare Album Artist
//					if(s1.getAlbumTitle().equals(s2.getAlbumTitle())) {  
//						return s2.getAlbumArtist().toLowerCase().compareTo(s1.getAlbumArtist().toLowerCase());
//					} else { // Compare Album Title
//						return s1.getAlbumTitle().toLowerCase().compareTo(s2.getAlbumTitle().toLowerCase());
//					}
//				}
//			} 
//		};
//		
//		Comparator<Song> sortSongTitlesDescending = new Comparator<Song>(){
//			@Override
//			public int compare(Song s1, Song s2) {  // Same Song
//				if(s1.equals(s2)) {
//					return 0;
//				} else if(s1.getSongTitle().equals(s2.getSongTitle())) {  // Same Song Title --> Compare Song Artist
//					return s1.getSongArtist().toLowerCase().compareTo(s2.getSongArtist().toLowerCase());
//				} else {  // Compare Song Title
//					return s1.getSongTitle().toLowerCase().compareTo(s2.getSongTitle().toLowerCase());
//				}		
//			}
//		};
		
//		Comparator<Song> sortSongTitlesDescending = new Comparator<Song>(){
//		@Override
//		public int compare(Song s1, Song s2) {  // Same Song
//			if(s1.equals(s2)) {
//				return 0;
//			} else if(s1.getSongTitle().equals(s2.getSongTitle())) {  // Same Song Title --> Compare Song Artist
//				return s1.getSongArtistSort().toLowerCase().compareTo(s2.getSongArtistSort().toLowerCase());
//			} else {  // Compare Song Title
//				return s1.getSongTitleSort().toLowerCase().compareTo(s2.getSongTitleSort().toLowerCase());
//			}		
//		}
//	};
//	
//	songsTableView.comparatorProperty().set
	}
	
	private boolean songFileExists(Song song) {
		// Checks if the file can be located at the location stored in Song object
		if(!song.fileExists()) {
			Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
			alert.setTitle("File Error");
			alert.setHeaderText(null);
			alert.setContentText("Song file could not be found. Please locate file.");
			Optional<ButtonType> result = alert.showAndWait();
			if (result.isPresent() && result.get() == ButtonType.OK) {
				FileChooser fileChooser = new FileChooser();
				fileChooser.setTitle("Select Music Files");
				fileChooser.getExtensionFilters().addAll(
						new ExtensionFilter("Audio Files", "*.mp3", "*.mp4", "*.m4a", "*.m4v", "*.wav", "*.aac"),
						new ExtensionFilter("All Files", "*.*"));
				File file = fileChooser.showOpenDialog(mainBorderPane.getScene().getWindow());
				song.setLocation(file.getAbsolutePath());
				
				if(file.getAbsoluteFile() != null) {
					song.setWarning(false);
					songsTableView.refresh();
					return true;
				} else {
					song.setWarning(true);
					songsTableView.refresh();
					return false;
				}
			} else {
				song.setWarning(true);
				songsTableView.refresh();
				return false;
			}
		} else {
			song.setWarning(false);
			songsTableView.refresh();
			return true;
		}
	}
	
	@FXML
	public void handleResumePauseSong() {
		if(audioPlayer.getSong() != null) {
			if(audioPlayer.getMediaPlayer().getStatus() == MediaPlayer.Status.PLAYING) {
				audioPlayer.pause();
				playButton.setGraphic(new ImageView(playButtonImage));
			} else {
				audioPlayer.play();
				playButton.setGraphic(new ImageView(pauseButtonImage));
			}
		} else {
			handlePlaySong();
		}
	}
	
	@FXML
	public void handlePlaySong() {
		Song selectedSong = songsTableView.getSelectionModel().getSelectedItem();
		if (selectedSong == null) {
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			alert.setTitle("No Song Selected");
			alert.setHeaderText(null);
			alert.setContentText("Please select a song.");
			alert.showAndWait();
			return;
		}
		if(audioPlayer.getSong() != null) {
			audioPlayer.stop();
		}
		if(songFileExists(selectedSong)) {
			audioPlayer.setSong(selectedSong);
			int currentIndex = data.getSongsList().indexOf(selectedSong);
			data.createIndexList(currentIndex, shuffleButton.isSelected());
			audioPlayer.play();
			playButton.setGraphic(new ImageView(pauseButtonImage));
		}
	}
	
	@FXML
	public void handleNextSong() {
		if (audioPlayer.getSong() != null) {
			int indexListIndex = data.getIndexListIndex();
			if(indexListIndex < data.getSongsList().size()-1) {
				int nextIndex = data.getIndexList().get(indexListIndex+1);
				audioPlayer.stop();
				if(songFileExists(data.getSongsList().get(nextIndex))) {
					audioPlayer.setSong(data.getSongsList().get(nextIndex));
					data.setIndexListIndex(indexListIndex+1);
					audioPlayer.play();
					playButton.setGraphic(new ImageView(pauseButtonImage));
				}
			}
		}
	}
	
	@FXML
	public void handlePreviousSong() {
		if (audioPlayer.getSong() != null) {
			int indexListIndex = data.getIndexListIndex();
			if(indexListIndex > 0) {
				int prevIndex = data.getIndexList().get(indexListIndex-1);
				audioPlayer.stop();
				if(songFileExists(data.getSongsList().get(prevIndex))) {
					audioPlayer.setSong(data.getSongsList().get(prevIndex));
					data.setIndexListIndex(indexListIndex-1);
					audioPlayer.play();
					playButton.setGraphic(new ImageView(pauseButtonImage));
				}
			}
		}
	}
	
	@FXML
	public void handleShuffle() {
		if (audioPlayer.getSong() != null) {
			Song currentSong = audioPlayer.getSong();
			int currentIndex = data.getSongsList().indexOf(currentSong);
			data.createIndexList(currentIndex, shuffleButton.isSelected());
		}else {
			data.createIndexList(0, shuffleButton.isSelected());
		}
		data.setIndexListIndex(0);
	}
	
	@FXML
	public void handleResetSongPlayCount() {
		// Resets the play count for the selected song
		Song selectedSong = songsTableView.getSelectionModel().getSelectedItem();
		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
		alert.setTitle("Reset Song Play Count");
		alert.setHeaderText(null);
		alert.setContentText("Are you sure you want to reset the play count for the selected song, " + selectedSong.getSongTitle() + "?");

		Optional<ButtonType> result = alert.showAndWait();
		if (result.isPresent() && result.get() == ButtonType.OK) {
			selectedSong.resetPlayCount();
		}
	}
	
	@FXML
	public void handleRepeat() {
		if (audioPlayer.getSong() != null) {
			audioPlayer.getMediaPlayer().setOnEndOfMedia(new Runnable() {
	    		@Override
	    		public void run() {
	    			audioPlayer.getSong().addPlayToCount();
	    			songsTableView.refresh();
	    			audioPlayer.getMediaPlayer().stop();
	    			if(repeatButton.isSelected()) {
	    				audioPlayer.setSong(audioPlayer.getSong());
		    			audioPlayer.play();
	    			} else {
	    				int indexListIndex = data.getIndexListIndex();
	    				if(indexListIndex < data.getSongsList().size()-1) {
	    					int nextIndex = data.getIndexList().get(indexListIndex+1);
	    					if(nextIndex < data.getSongsList().size()) {
	    						audioPlayer.setSong(data.getSongsList().get(nextIndex));
	    						data.setIndexListIndex(indexListIndex+1);
	    						audioPlayer.play();
	    					}
	    				}
	    			}
	    		}
	    	});
		}
	}

	private void deleteSongs(List<Song> songs) {
		if(listType == Controller.ListType.SONGSLIST) {
			Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
			alert.setTitle("Delete Song");
			alert.setHeaderText(null);
			alert.setContentText("Are you sure you want to delete the selected songs?");

			Optional<ButtonType> result = alert.showAndWait();
			if (result.isPresent() && result.get() == ButtonType.OK) {
				for(Song song : songs) {
					ObservableList<String> playlistNames = song.getPlaylistsList();
					for(String playlistName : playlistNames) {
						playlists.getPlaylist(playlistName).removeSong(song);
					}
				}
				data.removeSongs(songs);
				data.createIndexList(0, shuffleButton.isSelected());
	            try {
					data.saveSongs();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} else if(listType == Controller.ListType.PLAYLIST) {
			Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
			alert.setTitle("Remove Song");
			alert.setHeaderText(null);
			alert.setContentText("Are you sure you want to remove the selected songs from the playlist?");
			
			Optional<ButtonType> result = alert.showAndWait();
			if (result.isPresent() && result.get() == ButtonType.OK) {
				Playlist playlist = playlists.getPlaylist(activeList.get());
				playlist.removeSongs(songs);
	            try {
					data.saveSongs();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		songsTableView.refresh();
	}

	@FXML
	public void handleDeleteSong() {
		List<Song> selectedSongs = songsTableView.getSelectionModel().getSelectedItems();
		if (selectedSongs == null) {
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			alert.setTitle("No Song Selected");
			alert.setHeaderText(null);
			alert.setContentText("Please select a song.");
			alert.showAndWait();
			return;
		}
		deleteSongs(selectedSongs);
	}

	@FXML
	public void handleDeleteKeyPressed(KeyEvent keyEvent) {
		List<Song> selectedSongs = songsTableView.getSelectionModel().getSelectedItems();
		if (selectedSongs != null) {
			if (keyEvent.getCode().equals(KeyCode.DELETE)) {
				deleteSongs(selectedSongs);
			}
		}
	}
	
	private void sortPlaylistListView() {
		playlistListView.getItems().sort((p1, p2) ->{
			if(p1.getName().equals(p2.getName())) {
				return 0;
			} else {
				return p1.getName().compareTo(p2.getName());
			}		
		});
	}
	
	@FXML
	public void handleAddPlaylist() {
		Playlist playlist = new Playlist();
		String playlistName = "New Playlist";
		if(playlists.getList().size() > 0) {
			int index = 0;
			while(playlists.playlistExists(playlistName)) {
				index++;
				playlistName = "New Playlist" + " - " + Integer.toString(index);
			}
		}
		playlist.setName(playlistName);
		playlists.addPlaylist(playlist);
		sortPlaylistListView();
		playlistListView.refresh();
	}
	
	@FXML
	public void handleDeletePlaylist() {
		Playlist selectedPlaylist = playlistListView.getSelectionModel().getSelectedItem();
		if (selectedPlaylist == null) {
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			alert.setTitle("No Playlist Selected");
			alert.setHeaderText(null);
			alert.setContentText("Please select a playlist.");
			alert.showAndWait();
			return;
		}
		
		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
		alert.setTitle("Delete Playlist");
		alert.setHeaderText(null);
		alert.setContentText("Are you sure you want to delete the selected playlist?");

		Optional<ButtonType> result = alert.showAndWait();
		if (result.isPresent() && result.get() == ButtonType.OK) {
			ObservableList<Song> songs = selectedPlaylist.getSongsList();
			for(Song song : songs) {
				song.getPlaylists().remove(selectedPlaylist.getName());
			}
			playlists.removePlaylist(selectedPlaylist);;
			sortPlaylistListView();
			playlistListView.refresh();
		}
	}
	
	public void setGenresMap() {
		Path filePath = FileSystems.getDefault().getPath("genresList.txt");
		List<String> lines;
		try {
			lines = Files.readAllLines(filePath);
			String[] strings;
			for(String line : lines) {
				strings = line.split("\\.");
				genresMap.put("(" + strings[0] + ")", strings[1]);
				
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@FXML
	public void handleImport() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Select Music Files");
		fileChooser.getExtensionFilters().addAll(
				new ExtensionFilter("Audio Files", "*.mp3", "*.mp4", "*.m4a", "*.m4v", "*.wav", "*.aac"),
				new ExtensionFilter("All Files", "*.*"));
		fileChooser.setInitialDirectory(new File(Settings.getInstance().getDefaultFileDir()));
		List<File> files = fileChooser.showOpenMultipleDialog(mainBorderPane.getScene().getWindow());
		
		if (files != null) {
			for (int i = 0; i < files.size(); i++) {
				try {
					AudioFile audioFile = AudioFileIO.read(files.get(i));
					Tag tag = audioFile.getTag();
					AudioHeader audioHeader = audioFile.getAudioHeader();
					
					String tagFieldString;
					Song song = new Song();
					song.setAlbumTitle(tag.getFirst(FieldKey.ALBUM)); // Album Title
					song.setAlbumArtist(tag.getFirst(FieldKey.ALBUM_ARTIST)); // Album Artist
					
					tagFieldString = tag.getFirst(FieldKey.ALBUM_SORT);  // Album Title for Sorting
					if(tagFieldString == null || tagFieldString == "") {
						song.setAlbumTitleSort(tag.getFirst(FieldKey.ALBUM));
					} else {
						song.setAlbumTitleSort(tag.getFirst(FieldKey.ALBUM_SORT));
					}
					
					song.setSongArtist(tag.getFirst(FieldKey.ARTIST)); // Song Artist
					
					tagFieldString = tag.getFirst(FieldKey.ARTIST_SORT);  // Song Artist for Sorting
					if(tagFieldString == null || tagFieldString == "") {
						song.setSongArtistSort(tag.getFirst(FieldKey.ARTIST));
					} else {
						song.setSongArtistSort(tag.getFirst(FieldKey.ARTIST_SORT));
					}
					
					song.setBPM(tag.getFirst(FieldKey.BPM)); // Beats Per Minute
					try {
						song.setAlbumArtworkBytes(tag.getFirstArtwork().getBinaryData());  // Album Artwork
					} catch(Exception e){
					}
					
					tagFieldString = tag.getFirst(FieldKey.GENRE); // Genre
					if(genresMap.containsKey("(" + tagFieldString + ")")) {
						song.setGenre(genresMap.get(tagFieldString));
						System.out.println("Contains Genre Key");
					} else {
						song.setGenre(tagFieldString);
					}
					
					song.setSongTitle(tag.getFirst(FieldKey.TITLE)); // Song Title
					
					tagFieldString = tag.getFirst(FieldKey.TITLE_SORT);  // Song Title for Sorting
					if(tagFieldString == null || tagFieldString == "") {
						song.setSongTitleSort(tag.getFirst(FieldKey.TITLE));
					} else {
						song.setSongTitleSort(tag.getFirst(FieldKey.TITLE_SORT));
					}
					
					song.setTrackNumber(tag.getFirst(FieldKey.TRACK)); // Track Number
					song.setTracksTotal(tag.getFirst(FieldKey.TRACK_TOTAL)); // Total Number of Tracks
					song.setTrackLength(audioHeader.getTrackLength()); // Total Number of Tracks
					tagFieldString = tag.getFirst(FieldKey.YEAR);
					song.setYear(tagFieldString.substring(0, Math.min(4, tagFieldString.length()))); // Year
					song.setBitRate(audioHeader.getBitRate() + " kbps");
					song.setVariableBitRate(audioHeader.isVariableBitRate());
					song.setFileSize(Long.toString(files.get(i).length()/1000) + " KB");
					song.setLocation(files.get(i).getPath());  // File Path
					data.addSong(song);
					
					data.getIndexList().add(data.getIndexList().size());
					
				} catch (IOException | CannotReadException | TagException | ReadOnlyFileException | InvalidAudioFrameException  e) {
					e.printStackTrace();
				}
			}
		}
		songsTableView.sort();
		try {
			data.saveSongs();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@FXML
	public void showSongPropertiesDialog() {
		Song selectedSong = songsTableView.getSelectionModel().getSelectedItem();
		if (selectedSong == null) {
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			alert.setTitle("No Song Selected");
			alert.setHeaderText(null);
			alert.setContentText("Please select a song.");
			alert.showAndWait();
			return;
		}
		Dialog<ButtonType> dialog = new Dialog<ButtonType>();
		dialog.initOwner(mainBorderPane.getScene().getWindow());
		dialog.setTitle("Song Properties");
		FXMLLoader fxmlLoader = new FXMLLoader();
		fxmlLoader.setLocation(getClass().getResource("SongPropertiesDialogue.fxml"));
		Settings.getInstance().applyTheme(dialog.getDialogPane().getStylesheets());
		try {
			dialog.getDialogPane().setContent(fxmlLoader.load());

		} catch (IOException e) {
			System.out.println("Could not load the dialog");
			e.printStackTrace();
			return;
		}
		dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
		dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

		SongPropertiesController songPropertiesController = fxmlLoader.getController();
		songPropertiesController.editSong(selectedSong);

		Optional<ButtonType> result = dialog.showAndWait();
		if (result.isPresent() && result.get() == ButtonType.OK) {
			boolean changed = songPropertiesController.updateSong(selectedSong);
			if(changed) {
				songsTableView.refresh();
				try {
					data.saveSongs();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	@FXML
	public void showDownloadSongsDialog() {
		Dialog<ButtonType> dialog = new Dialog<ButtonType>();
		dialog.initOwner(mainBorderPane.getScene().getWindow());
		dialog.setTitle("Download Songs");
		dialog.setResizable(true);
		FXMLLoader fxmlLoader = new FXMLLoader();
		fxmlLoader.setLocation(getClass().getResource("DownloadSongsDialogue.fxml"));
		Settings.getInstance().applyTheme(dialog.getDialogPane().getStylesheets());
		try {
			dialog.getDialogPane().setContent(fxmlLoader.load());
			
		} catch (IOException e) {
			System.out.println("Could not load the dialog");
			e.printStackTrace();
			return;
		}
		dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
		DownloadSongsController downloadController = fxmlLoader.getController();
		downloadController.setMainBorderPane(mainBorderPane);
		downloadController.getDownloadGridPane().heightProperty().addListener((obs, oldVal, newVal) -> {
			dialog.setHeight(dialog.getHeight() + newVal.doubleValue() - oldVal.doubleValue());
		});
		dialog.show();
	}

	@FXML
	public void showSettingsDialog() {
		Dialog<ButtonType> dialog = new Dialog<ButtonType>();
		dialog.initOwner(mainBorderPane.getScene().getWindow());
		dialog.setTitle("Settings");
		FXMLLoader fxmlLoader = new FXMLLoader();
		fxmlLoader.setLocation(getClass().getResource("SettingsDialogue.fxml"));
		Settings.getInstance().applyTheme(dialog.getDialogPane().getStylesheets());
		try {
			dialog.getDialogPane().setContent(fxmlLoader.load());
			
		} catch (IOException e) {
			System.out.println("Could not load the dialog");
			e.printStackTrace();
			return;
		}
		dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
		dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

		SettingsController settingsController = fxmlLoader.getController();
		settingsController.setMainBorderPane(mainBorderPane);
		settingsController.editTheme(Settings.getInstance().getTheme());
		settingsController.editDefaultFileDir(Settings.getInstance());
		settingsController.editColVisibility(Settings.getInstance());
		
		Optional<ButtonType> result = dialog.showAndWait();
		if (result.isPresent() && result.get() == ButtonType.OK) {
			settingsController.updateColVisibility(Settings.getInstance(), songTitleCol, songArtistCol, albumTitleCol,
					dateModifiedCol, dateAddedCol, timeCol, genreCol, yearCol, playsCol);
			settingsController.setTheme();
			settingsController.setDefaultFileDir(Settings.getInstance());
			Settings.getInstance().applyTheme(mainBorderPane.getStylesheets());
		}
	}
	
	public void editColVisibility() {
		songTitleColVisibleCM.setSelected(songTitleCol.isVisible());
		songArtistColVisibleCM.setSelected(songArtistCol.isVisible());
		albumTitleColVisibleCM.setSelected(albumTitleCol.isVisible());
		dateModifiedColVisibleCM.setSelected(dateModifiedCol.isVisible());
		dateAddedColVisibleCM.setSelected(dateAddedCol.isVisible());
		timeColVisibleCM.setSelected(timeCol.isVisible());
		genreColVisibleCM.setSelected(genreCol.isVisible());
		yearColVisibleCM.setSelected(yearCol.isVisible());
		playsColVisibleCM.setSelected(playsCol.isVisible());
	}
	
	@FXML
	public void handleSongTitleColVisibility() {
		songTitleCol.setVisible(songTitleColVisibleCM.isSelected());
	}
	@FXML
	public void handleSongArtistColVisibility() {
		songArtistCol.setVisible(songArtistColVisibleCM.isSelected());
	}
	@FXML
	public void handleAlbumTitleColVisibility() {
		albumTitleCol.setVisible(albumTitleColVisibleCM.isSelected());
	}
	@FXML
	public void handleDateModifiedColVisibility() {
		dateModifiedCol.setVisible(dateModifiedColVisibleCM.isSelected());
	}
	@FXML
	public void handleDateAddedColVisibility() {
		dateAddedCol.setVisible(dateAddedColVisibleCM.isSelected());
	}
	@FXML
	public void handleTimeColVisibility() {
		timeCol.setVisible(timeColVisibleCM.isSelected());
	}
	@FXML
	public void handleGenreColVisibility() {
		genreCol.setVisible(genreColVisibleCM.isSelected());
	}
	@FXML
	public void handleYearColVisibility() {
		yearCol.setVisible(yearColVisibleCM.isSelected());
	}
	@FXML
	public void handlePlaysColVisibility() {
		playsCol.setVisible(playsColVisibleCM.isSelected());
	}
	
	@FXML
	public void handleExit() {
		try {
			data.saveSongs();
			playlists.savePlaylists();
			Settings.getInstance().saveSettings();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Platform.exit();
		System.exit(0);
	}
}
