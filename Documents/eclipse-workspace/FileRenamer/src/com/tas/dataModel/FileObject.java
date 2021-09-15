package com.tas.dataModel;

import java.io.File;

import javafx.beans.property.SimpleStringProperty;

public class FileObject {
	private File file;
	private SimpleStringProperty fileName = new SimpleStringProperty("");
	
	public FileObject(File file) {
		this.file = file;
		this.fileName.set(file.getName());
	}

	public File getFile() {
		return file;
	}
	public void setFile(File file) {
		this.file = file;
		this.fileName.set(file.getName());
	}
	public String getFileName() {
		return fileName.get();
	}
	public void setFileName(String fileName) {
		this.fileName.set(fileName);
	}
	
	public String toString() {
		String result = "File Path: " + file.getAbsolutePath() + "\n";
		result = result + "File Name: " + fileName.get();
		return result;
	}
}
