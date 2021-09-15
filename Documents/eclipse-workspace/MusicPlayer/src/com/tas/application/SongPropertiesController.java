package com.tas.application;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Base64;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.tas.dataModel.Song;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.web.HTMLEditor;

public class SongPropertiesController {
	@FXML ImageView	albumArtworkFieldHeader;
	@FXML Label songTitleFieldHeader;
	@FXML Label songArtistFieldHeader;
	@FXML Label albumTitleFieldHeader;
	
	@FXML TextField songTitleField;
	@FXML TextField songArtistField;
	@FXML TextField albumTitleField;
	@FXML TextField albumArtistField;
	@FXML TextField trackNumberField;
	@FXML TextField tracksTotalField;
	@FXML TextField bpmField;
	@FXML TextField yearField;
	@FXML TextField genreField;
	@FXML Label trackLengthField;
	@FXML Label playsField;
	
	@FXML HTMLEditor albumArtworkHTML;
	
	@FXML Label songTitleFieldSorting;
	@FXML TextField songTitleSortField;
	@FXML Label songArtistFieldSorting;
	@FXML TextField songArtistSortField;
	@FXML Label albumTitleFieldSorting;
	@FXML TextField albumTitleSortField;
	
	@FXML Label bitRateField;
	@FXML Label variableBitRateField;
	@FXML Label fileSizeField;
	@FXML Label dateAddedField;
	@FXML Label dateModifiedField;
	@FXML Label locationField;
	
	
    public void editSong(Song song) {
    	albumArtworkFieldHeader.setImage(song.getAlbumArtworkImage());
    	
    	songTitleFieldHeader.setText(song.getSongTitle());
    	songArtistFieldHeader.setText(song.getSongArtist());
    	albumTitleFieldHeader.setText(song.getAlbumTitle());
    	
    	songTitleField.setText(song.getSongTitle());
    	songArtistField.setText(song.getSongArtist());
    	albumTitleField.setText(song.getAlbumTitle());
    	albumArtistField.setText(song.getAlbumArtist());
    	trackNumberField.setText(song.getTrackNumber());
    	tracksTotalField.setText(song.getTracksTotal());
    	bpmField.setText(song.getBPM());
    	yearField.setText(song.getYear());
    	genreField.setText(song.getGenre());
    	trackLengthField.setText(song.getTrackLengthFormatted());
    	playsField.setText(Integer.toString(song.getPlayCount()));
    	
    	hideHTMLEditorToolbars();
    	setHTMLEditorImage(albumArtworkHTML, song.getAlbumArtworkBytes());
    	
    	songTitleFieldSorting.setText(song.getSongTitle());
    	songTitleSortField.setText(song.getSongTitleSort());
    	songArtistFieldSorting.setText(song.getSongArtist());
    	songArtistSortField.setText(song.getSongArtistSort());
    	albumTitleFieldSorting.setText(song.getAlbumTitle());
    	albumTitleSortField.setText(song.getAlbumTitleSort());
    	
    	bitRateField.setText(song.getBitRate());
    	variableBitRateField.setText(song.isVariableBitRateFormatted());
    	fileSizeField.setText(song.getFileSize());
    	dateAddedField.setText(song.getDateAddedFormatted());
    	dateModifiedField.setText(song.getDateModifiedFormatted());
    	locationField.setText(song.getLocation());
    }

    public boolean updateSong(Song song) {
    	String[] htmlTextSRC = getHTMLEditorImageSRC(albumArtworkHTML.getHtmlText());
    	byte[] imageBytes = null;
    	if(htmlTextSRC != null) {
    		imageBytes = Base64.getDecoder().decode(htmlTextSRC[1]);
    	} else {
    		
    		try {
    			File file = new File("media\\EmptyAlbumCover.png");
    			imageBytes = Files.readAllBytes(file.toPath());
			} catch (IOException e) {
				e.printStackTrace();
			}
    	}
    	
    	if(song.changed(songTitleField.getText(), 
    			songArtistField.getText(), 
    			albumTitleField.getText(), 
    			albumArtistField.getText(), 
    			imageBytes,
    			trackNumberField.getText(), 
    			tracksTotalField.getText(),
    			bpmField.getText(), 
				yearField.getText(), 
    			genreField.getText(), 
				songTitleSortField.getText(), 
				songArtistSortField.getText(), 
				albumTitleSortField.getText())) {
    		song.updateSong(songTitleField.getText(), 
        			songArtistField.getText(), 
        			albumTitleField.getText(), 
        			albumArtistField.getText(), 
        			imageBytes,
        			trackNumberField.getText(), 
        			tracksTotalField.getText(),
        			bpmField.getText(), 
    				yearField.getText(), 
        			genreField.getText(), 
    				songTitleSortField.getText(), 
    				songArtistSortField.getText(), 
    				albumTitleSortField.getText());
    		return true;
    	} else {
    		return false;
    	}
    }
    
    private void hideHTMLEditorToolbars() {
    	albumArtworkHTML.setVisible(false);
        Platform.runLater(() -> {
            Node[] nodes = albumArtworkHTML.lookupAll(".tool-bar").toArray(new Node[0]);
            for (Node node : nodes) {
                node.setVisible(false);
                node.setManaged(false);
            }
            albumArtworkHTML.setVisible(true);
        });
    }
    
    private String[] getHTMLEditorImageSRC(String htmlText){
    	String REGEX = "img src=\"[^\"]+\"";
		Pattern p = Pattern.compile(REGEX);
		Matcher m = p.matcher(htmlText);
		
		int count = 0;
		while(m.find()) {
	    	count++;
	    }
		
		if(count == 0) {
			return null;
		} else {
			m.reset();
			m.find();
			
		    Matcher m2;
		    String REGEX2 = "\".+\"";
		    Pattern p2 = Pattern.compile(REGEX2);
			String INPUT2 = htmlText.substring(m.start(), m.end());
			
			m2 = p2.matcher(INPUT2);
			m2.find();
			String[] string = INPUT2.substring(m2.start()+1, m2.end()-1).split(",");
	    	return string;
		}
    }
    
    private void setHTMLEditorImage(HTMLEditor htmlEditor, byte[] imageBytes){
//    	String startString = "<img src=\"data:image/jpeg;base64,";
    	String startString = "<img src=\"data:image/png;base64,";
    	String endString = "\">";
    	String srcString = Base64.getEncoder().encodeToString(imageBytes);
    	htmlEditor.setHtmlText(startString + srcString + endString);
    }
	
}
