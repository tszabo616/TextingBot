package com.tas.application;

import java.io.File;

import com.tas.dataModel.Song;
import com.tas.dataModel.SongData;

import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleButton;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class AudioPlayer{
	Song song;
    Media media;
    MediaPlayer mediaPlayer;
    Slider timeSlider;
    Slider volSlider;
    Label currentTimeLabel;
    Label totalTimeLabel;
    Label currentSongTitle;
    Label currentSongArtist;
    Button playButton;
    ToggleButton repeatButton;
    TableView<Song> songsTableView;
    SongData data;
    
    public AudioPlayer() {
    }
    
    public MediaPlayer getMediaPlayer() {
    	return mediaPlayer;
    }
    public Song getSong() {
    	return song;
    }
    public void setSong(Song song) {
    	this.song = song;
    	File audioFile = null;
    	try {
    		audioFile = new File(song.getLocation());
    	} catch (Exception e){
    		Alert alert = new Alert(Alert.AlertType.INFORMATION);
			alert.setTitle("File Not Found");
			alert.setHeaderText(null);
			alert.setContentText("Please locate song file for: " + song.getSongTitle() + ".");
			alert.showAndWait();
			return;
    	}
    	media = new Media(audioFile.toURI().toString());
    	mediaPlayer = new MediaPlayer(media);
    	mediaPlayer.setOnPlaying(new Runnable() {
    		@Override
    		public void run() {
    			currentSongTitle.setText(song.getSongTitle());
    			currentSongArtist.setText(song.getSongArtist());
    		}
    	});
    	mediaPlayer.setOnEndOfMedia(new Runnable() {
    		@Override
    		public void run() {
    			AudioPlayer.this.song.addPlayToCount();
    			songsTableView.refresh();
    			mediaPlayer.stop();
    			if (AudioPlayer.this.song != null) {
    				if(repeatButton.isSelected()) {
    					setSong(AudioPlayer.this.song);
    					mediaPlayer.play();
    				} else {
    					int indexListIndex = data.getIndexListIndex();
    					if(indexListIndex < data.getSongsList().size()-1) {
	    					int nextIndex = data.getIndexList().get(indexListIndex+1);
	    					if(nextIndex < data.getSongsList().size()) {
	    						setSong(data.getSongsList().get(nextIndex));
	    						data.setIndexListIndex(indexListIndex+1);
	    						mediaPlayer.play();
	    					}
	    				}
    				}
    			}
    		}
    	});
    	mediaPlayer.currentTimeProperty().addListener(new InvalidationListener() { 
    		public void invalidated(Observable ov) {
    			Platform.runLater(new Runnable() { 
    	            public void run() { 
    					timeSlider.setValue(mediaPlayer.getCurrentTime().toMillis()/mediaPlayer.getTotalDuration().toMillis()*100.0);
    	            	currentTimeLabel.setText(secondsToFormattedTime(mediaPlayer.getCurrentTime().toMillis()/1000.0)); 
    	            	totalTimeLabel.setText(secondsToFormattedTime(mediaPlayer.getTotalDuration().toMillis()/1000.0));
    	            } 
    	        });
    	    }
    	});
    }
    public void setTimeSlider(Slider timeSlider) {
    	this.timeSlider = timeSlider;
    }
    public void setVolSlider(Slider volSlider) {
		this.volSlider = volSlider;
	}
	public void setCurrentTimeLabel(Label currentTimeLabel) {
    	this.currentTimeLabel = currentTimeLabel;
    }
    public void setTotalTimeLabel(Label totalTimeLabel) {
    	this.totalTimeLabel = totalTimeLabel;
    }
    public void setPlayButton(Button playButton) {
    	this.playButton = playButton;
    }
    public void setSongsTableView(TableView<Song> songsTableView) {
    	this.songsTableView = songsTableView;
    }
	public void setCurrentSongTitle(Label currentSongTitle) {
		this.currentSongTitle = currentSongTitle;
	}
	public void setCurrentSongArtist(Label currentSongArtist) {
		this.currentSongArtist = currentSongArtist;
	}
	public void setData(SongData data) {
		this.data = data;
	}
	public void setRepeatButton(ToggleButton repeatButton) {
		this.repeatButton = repeatButton;
	}

	public void play() {
    	mediaPlayer.play();
    }
    
    public void pause() {
    	mediaPlayer.pause();
    }
    
    public void stop() {
    	mediaPlayer.stop();
    }
    
    private String secondsToFormattedTime(double seconds) {
		int hours = (int)seconds/3600;
		int minutes = (int)(seconds - hours*3600)/60;
		int rem_seconds = (int)(seconds - hours*3600 - minutes*60);
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
