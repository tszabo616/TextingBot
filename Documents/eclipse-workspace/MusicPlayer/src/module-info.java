module MusicPlayer {
	requires javafx.base;
	requires javafx.fxml;
    requires javafx.controls;
    
	requires jaudiotagger;
	requires java.desktop;
	requires javafx.media;
	requires javafx.web;
	requires javafx.graphics;
    
    opens com.tas.application;
    opens com.tas.dataModel;
}