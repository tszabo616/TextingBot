package com.tas.dataModel;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class FilesList {
	private ObservableList<FileObject> filesList;
	
	public FilesList() {
		filesList = FXCollections.observableArrayList();
	}

	public ObservableList<FileObject> getFilesList() {
		return filesList;
	}
	public void setFilesList(ObservableList<FileObject> filesList) {
		this.filesList = filesList;
	}
	public void add(FileObject file) {
		filesList.add(file);
	}
	public void clear() {
		filesList.clear();
	}
	public void remove(FileObject fileObject) {
		filesList.remove(fileObject);
	}
	
	public void move(int fromIndex, int toIndex) {
		ObservableList<FileObject> tempList= FXCollections.observableArrayList();
		FileObject fromObject = filesList.get(fromIndex);
		FileObject toObject = filesList.get(toIndex);
		
		for (int i = 0; i < filesList.size(); i++) {
			if(i == fromIndex) {
				tempList.add(toObject);
			} else if(i == toIndex) {
				tempList.add(fromObject);
			} else {
				tempList.add(filesList.get(i));
			}
		}
		filesList.clear();
		filesList.setAll(tempList);
	}
	
}
