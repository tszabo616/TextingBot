package com.tas.application;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.tas.dataModel.FileObject;
import com.tas.dataModel.FilesList;
import com.tas.dataModel.Labels;
import com.tas.dataModel.Renamer;

import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.util.Callback;

public class Controller {
	private FilesList filesList;
	
	@FXML private BorderPane mainBorderPane;
	@FXML private ListView<FileObject> filesListView;
	@FXML private TextField prefixIdentifier;
	@FXML private TextField suffixIdentifier;
	@FXML private ComboBox<String> newPrefixLabel;
	@FXML private TextField newPrefixIdentifier;
	@FXML private TextField newPrefixIndex;
	@FXML private ComboBox<String> newSuffixLabel;
	@FXML private TextField newSuffixIdentifier;
	@FXML private TextField newSuffixIndex;
	@FXML private Button upButton;
	@FXML private Button downButton;
	
	@FXML private TableView<FileObject> filesTableView;
	@FXML private TableColumn<FileObject, String> rowNumberCol;

	
	public void initialize() {
		filesList = new FilesList();
//		filesListView.setItems(filesList.getFilesList());
//		filesListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
//		
//		filesListView.setCellFactory(new Callback<ListView<FileObject>, ListCell<FileObject>>() {
//            @Override
//            public ListCell<FileObject> call(ListView<FileObject> param) {
//                ListCell<FileObject> cell = new ListCell<FileObject>() {
//
//                    @Override
//                    protected void updateItem(FileObject fileObject, boolean empty) {
//                        super.updateItem(fileObject, empty);
//                        if(empty) {
//                            setText(null);
//                        } else {
//                            setText(fileObject.getFileName());
//                        }
//                    }
//                };
//                return cell;
//            }
//        });
		
		filesTableView.setItems(filesList.getFilesList());
		filesTableView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		rowNumberCol.setCellValueFactory(new Callback<CellDataFeatures<FileObject, String>, ObservableValue<String>>(){
			@Override public ObservableValue<String> call(CellDataFeatures<FileObject, String> p) {
				return new ReadOnlyObjectWrapper<String>(filesTableView.getItems().indexOf(p.getValue()) + 1 + "");
			}
		});
		
		upButton.setGraphic(new ImageView(new Image("file:media\\UpArrowClear.png", 11, 15, true, true)));
		downButton.setGraphic(new ImageView(new Image("file:media\\DownArrowClear.png", 11, 15, true, true)));
		
		ObservableList<String> labels = FXCollections.observableArrayList("None", "1, 2, 3, 4...", "A, B, C, D...", "a, b, c, d...", "I, II, III, IV...", "i, ii, iii, iv...");
		newPrefixLabel.getItems().addAll(labels);
		newSuffixLabel.getItems().addAll(labels);
	}
	
	private List<File> selectFiles() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Select Files");
		fileChooser.getExtensionFilters().addAll(new ExtensionFilter("All Files", "*.*"));
		return fileChooser.showOpenMultipleDialog(mainBorderPane.getScene().getWindow());
	}
	
	@FXML
	public void handleSelectFiles() {
		List<File> files = selectFiles();
		filesList.clear();
		if (files != null) {
			for (int i = 0; i < files.size(); i++) {
				filesList.add(new FileObject(files.get(i)));
			}
		}
	}
	
	@FXML
	public void handleAddFiles() {
		List<File> files = selectFiles();
		if (files != null) {
			for (int i = 0; i < files.size(); i++) {
				filesList.add(new FileObject(files.get(i)));
			}
		}
	}
	
	private List<File> selectFolders() {
		DirectoryChooser dirChooser = new DirectoryChooser();
		dirChooser.setTitle("Select Folders");
		List<File> files = new ArrayList<File>();
		File file;

		while(true) {
			file = dirChooser.showDialog(mainBorderPane.getScene().getWindow());
			if(file != null) {
				files.add(file);
				dirChooser.setInitialDirectory(file.getParentFile());
				
//				Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
//				alert.setTitle("Select More Folders");
//				alert.setHeaderText(null);
//				alert.setContentText("Do you want to select another folder?");
//
//				Optional<ButtonType> result = alert.showAndWait();
//				if (result.isPresent() && result.get() == ButtonType.CANCEL) {
//					break;
//				}
			} else {
				break;
			}
		}
		return files;
	}
	
	@FXML
	public void handleSelectFolders() {
		List<File> files = selectFolders();
		filesList.clear();
		if (files != null) {
			for (int i = 0; i < files.size(); i++) {
				filesList.add(new FileObject(files.get(i)));
			}
		}
	}
	
	@FXML
	public void handleAddFolders() {
		List<File> files = selectFolders();
		if (files != null) {
			for (int i = 0; i < files.size(); i++) {
				filesList.add(new FileObject(files.get(i)));
			}
		}
	}
	
	@FXML
	public void handleMoveUp() {
		FileObject selectedFile = filesTableView.getSelectionModel().getSelectedItem();
		if (selectedFile == null) {
			return;
		}
		int selectedIndex = filesTableView.getSelectionModel().getSelectedIndex();
		int fromIndex = selectedIndex;
		int toIndex = fromIndex - 1;
		if(toIndex >= 0) {
			filesList.move(fromIndex, toIndex);
			filesTableView.refresh();
			filesTableView.getSelectionModel().select(toIndex);
			filesTableView.scrollTo(toIndex);
		}
	}
	
	@FXML
	public void handleMoveDown() {
		FileObject selectedFile = filesTableView.getSelectionModel().getSelectedItem();
		if (selectedFile == null) {
			return;
		}
		int selectedIndex = filesTableView.getSelectionModel().getSelectedIndex();
		int fromIndex = selectedIndex;
		int toIndex = fromIndex + 1;
		if(toIndex < filesList.getFilesList().size()) {
			filesList.move(fromIndex, toIndex);
			filesTableView.refresh();
			filesTableView.getSelectionModel().select(toIndex);
			filesTableView.scrollTo(toIndex);
		}
	}

	@FXML
	public void handleRemoveFile() {
		List<FileObject> selectedFiles = filesTableView.getSelectionModel().getSelectedItems();
		if (selectedFiles == null) {
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			alert.setTitle("No File Selected");
			alert.setHeaderText(null);
			alert.setContentText("Please select a file.");
			alert.showAndWait();
			return;
		}
		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
		alert.setTitle("Remove File");
		alert.setHeaderText(null);
		alert.setContentText("Are you sure you want to remove the selected file?");

		Optional<ButtonType> result = alert.showAndWait();
		if (result.isPresent() && result.get() == ButtonType.OK) {
			for (int i = 0; i < selectedFiles.size(); i++) {
				filesList.remove(selectedFiles.get(i));;
			}
		}
	}
	
	@FXML
	public void handleDeleteKeyPressed(KeyEvent keyEvent) {
		List<FileObject> selectedFiles = filesTableView.getSelectionModel().getSelectedItems();
		if (selectedFiles != null) {
			if (keyEvent.getCode().equals(KeyCode.DELETE)) {
				Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
				alert.setTitle("Remove File");
				alert.setHeaderText(null);
				alert.setContentText("Are you sure you want to remove the selected file?");

				Optional<ButtonType> result = alert.showAndWait();
				if (result.isPresent() && result.get() == ButtonType.OK) {
					for (int i = 0; i < selectedFiles.size(); i++) {
						filesList.remove(selectedFiles.get(i));;
					}
				}
			}
		}
	}
	
	public void handleRenameFiles() {
		ObservableList<FileObject> files = filesList.getFilesList();
		if (files.size() > 1) {
			String prefixToAdd;
			String suffixToAdd;
			String prefixLabelType = newPrefixLabel.getSelectionModel().getSelectedItem();
			String suffixLabelType = newSuffixLabel.getSelectionModel().getSelectedItem();
			
			int prefixIndex;
			int suffixIndex;
			try {  // Check that prefix index can be parsed into an integer
				prefixIndex = (newPrefixIndex.getText().length() > 0) ? Integer.parseInt(newPrefixIndex.getText()) : 1;
			} catch (NumberFormatException e) {
				Alert alert = new Alert(Alert.AlertType.ERROR);
				alert.setTitle("Error");
				alert.setHeaderText(null);
				alert.setContentText("Please enter an integer for the prefix start index.");
				alert.showAndWait();
				return;
			}
			try {  // Check that suffix index can be parsed into an integer
				suffixIndex = (newSuffixIndex.getText().length() > 0) ? Integer.parseInt(newSuffixIndex.getText()) : 1;
			} catch (NumberFormatException e) {
				Alert alert = new Alert(Alert.AlertType.ERROR);
				alert.setTitle("Error");
				alert.setHeaderText(null);
				alert.setContentText("Please enter an integer for the suffix start index.");
				alert.showAndWait();
				return;
			}
			
			if(prefixIndex < 1) {
				Alert alert = new Alert(Alert.AlertType.ERROR);
				alert.setTitle("Error");
				alert.setHeaderText(null);
				alert.setContentText("Please enter an integer > 0 for the prefix start index.");
				alert.showAndWait();
				return;
			}
			if(suffixIndex < 1) {
				Alert alert = new Alert(Alert.AlertType.ERROR);
				alert.setTitle("Error");
				alert.setHeaderText(null);
				alert.setContentText("Please enter an integer > 0 for the suffix start index.");
				alert.showAndWait();
				return;
			}
			
			Labels prefixLabel = new Labels(prefixLabelType);
			Labels suffixLabel = new Labels(suffixLabelType);
			
			for (int i = 0; i < files.size(); i++) {
				prefixToAdd = prefixLabel.getLabel(prefixIndex) + newPrefixIdentifier.getText();
				suffixToAdd = newSuffixIdentifier.getText() + suffixLabel.getLabel(suffixIndex);
				Renamer renamer = new Renamer(files.get(i).getFileName(), prefixIdentifier.getText(), suffixIdentifier.getText(), prefixToAdd, suffixToAdd);
//				Path source = Path.of(files.get(i).getFile().getAbsolutePath());
				Path source = Paths.get(files.get(i).getFile().getAbsolutePath());
				
				try {
					Files.move(source, source.resolveSibling(renamer.newFileName()));
					files.get(i).setFile(new File(source.resolveSibling(renamer.newFileName()).toString()));
				} catch(IOException e) {
					System.out.println(files.get(i).getFileName() + " could not be moved.");
					e.printStackTrace();
				} catch(InvalidPathException e) {
					System.out.println(files.get(i).getFileName() + " could not be converted to a path.");
					e.printStackTrace();
				}
				prefixIndex++;
				suffixIndex++;
			}
			filesTableView.refresh();
			filesTableView.getSelectionModel().selectRange(0, filesTableView.getItems().size());
			filesTableView.getSelectionModel().clearSelection();
		}
	}
	
//	public void handleSelectFiles() {
//		FileChooser fileChooser = new FileChooser();
//		fileChooser.setTitle("Select Files");
//		fileChooser.getExtensionFilters().addAll(new ExtensionFilter("All Files", "*.*"));
//		List<File> files = fileChooser.showOpenMultipleDialog(mainBorderPane.getScene().getWindow());
//		filesList.clear();
//		if (files != null) {
//			for (int i = 0; i < files.size(); i++) {
//				filesList.add(new FileObject(files.get(i)));
//			}
//		}
//	}
//	
//	public void handleAddFiles() {
//		FileChooser fileChooser = new FileChooser();
//		fileChooser.setTitle("Select Files");
//		fileChooser.getExtensionFilters().addAll(new ExtensionFilter("All Files", "*.*"));
//		List<File> files = fileChooser.showOpenMultipleDialog(mainBorderPane.getScene().getWindow());
//		if (files != null) {
//			for (int i = 0; i < files.size(); i++) {
//				filesList.add(new FileObject(files.get(i)));
//			}
//		}
//	}
//	
//	@FXML
//	public void handleMoveUp() {
//		FileObject selectedFile = filesListView.getSelectionModel().getSelectedItem();
//		if (selectedFile == null) {
//			return;
//		}
//		int selectedIndex = filesListView.getSelectionModel().getSelectedIndex();
//		int fromIndex = selectedIndex;
//		int toIndex = fromIndex - 1;
//		if(toIndex >= 0) {
//			filesList.move(fromIndex, toIndex);
//			filesListView.refresh();
//			filesListView.getSelectionModel().select(toIndex);
//		}
//	}
//	
//	@FXML
//	public void handleMoveDown() {
//		FileObject selectedFile = filesListView.getSelectionModel().getSelectedItem();
//		if (selectedFile == null) {
//			return;
//		}
//		int selectedIndex = filesListView.getSelectionModel().getSelectedIndex();
//		int fromIndex = selectedIndex;
//		int toIndex = fromIndex + 1;
//		if(toIndex < filesList.getFilesList().size()) {
//			filesList.move(fromIndex, toIndex);
//			filesListView.refresh();
//			filesListView.getSelectionModel().select(toIndex);
//		}
//	}
//
//	@FXML
//	public void handleRemoveFile() {
//		List<FileObject> selectedFiles = filesListView.getSelectionModel().getSelectedItems();
//		if (selectedFiles == null) {
//			Alert alert = new Alert(Alert.AlertType.INFORMATION);
//			alert.setTitle("No File Selected");
//			alert.setHeaderText(null);
//			alert.setContentText("Please select a file.");
//			alert.showAndWait();
//			return;
//		}
//		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
//		alert.setTitle("Remove File");
//		alert.setHeaderText(null);
//		alert.setContentText("Are you sure you want to remove the selected file?");
//
//		Optional<ButtonType> result = alert.showAndWait();
//		if (result.isPresent() && result.get() == ButtonType.OK) {
//			for (int i = 0; i < selectedFiles.size(); i++) {
//				filesList.remove(selectedFiles.get(i));;
//			}
//		}
//	}
//	
//	@FXML
//	public void handleDeleteKeyPressed(KeyEvent keyEvent) {
//		List<FileObject> selectedFiles = filesListView.getSelectionModel().getSelectedItems();
//		if (selectedFiles != null) {
//			if (keyEvent.getCode().equals(KeyCode.DELETE)) {
//				Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
//				alert.setTitle("Remove File");
//				alert.setHeaderText(null);
//				alert.setContentText("Are you sure you want to remove the selected file?");
//
//				Optional<ButtonType> result = alert.showAndWait();
//				if (result.isPresent() && result.get() == ButtonType.OK) {
//					for (int i = 0; i < selectedFiles.size(); i++) {
//						filesList.remove(selectedFiles.get(i));;
//					}
//				}
//			}
//		}
//	}
//	
//	public void handleRenameFiles() {
//		ObservableList<FileObject> files = filesList.getFilesList();
//		if (files.size() > 1) {
//			String prefixToAdd;
//			String suffixToAdd;
//			String prefixLabelType = newPrefixLabel.getSelectionModel().getSelectedItem();
//			String suffixLabelType = newSuffixLabel.getSelectionModel().getSelectedItem();
//			
//			int prefixIndex;
//			int suffixIndex;
//			try {  // Check that prefix index can be parsed into an integer
//				prefixIndex = (newPrefixIndex.getText().length() > 0) ? Integer.parseInt(newPrefixIndex.getText()) : 1;
//			} catch (NumberFormatException e) {
//				Alert alert = new Alert(Alert.AlertType.ERROR);
//				alert.setTitle("Error");
//				alert.setHeaderText(null);
//				alert.setContentText("Please enter an integer for the prefix start index.");
//				alert.showAndWait();
//				return;
//			}
//			try {  // Check that suffix index can be parsed into an integer
//				suffixIndex = (newSuffixIndex.getText().length() > 0) ? Integer.parseInt(newSuffixIndex.getText()) : 1;
//			} catch (NumberFormatException e) {
//				Alert alert = new Alert(Alert.AlertType.ERROR);
//				alert.setTitle("Error");
//				alert.setHeaderText(null);
//				alert.setContentText("Please enter an integer for the suffix start index.");
//				alert.showAndWait();
//				return;
//			}
//			
//			if(prefixIndex < 1) {
//				Alert alert = new Alert(Alert.AlertType.ERROR);
//				alert.setTitle("Error");
//				alert.setHeaderText(null);
//				alert.setContentText("Please enter an integer > 0 for the prefix start index.");
//				alert.showAndWait();
//				return;
//			}
//			if(suffixIndex < 1) {
//				Alert alert = new Alert(Alert.AlertType.ERROR);
//				alert.setTitle("Error");
//				alert.setHeaderText(null);
//				alert.setContentText("Please enter an integer > 0 for the suffix start index.");
//				alert.showAndWait();
//				return;
//			}
//			
//			Labels prefixLabel = new Labels(prefixLabelType);
//			Labels suffixLabel = new Labels(suffixLabelType);
//			
//			for (int i = 0; i < files.size(); i++) {
//				prefixToAdd = prefixLabel.getLabel(prefixIndex) + newPrefixIdentifier.getText();
//				suffixToAdd = newSuffixIdentifier.getText() + suffixLabel.getLabel(suffixIndex);
//				Renamer renamer = new Renamer(files.get(i).getFileName(), prefixIdentifier.getText(), suffixIdentifier.getText(), prefixToAdd, suffixToAdd);
//				Path source = Path.of(files.get(i).getFile().getAbsolutePath());
//				
//				try {
//					Files.move(source, source.resolveSibling(renamer.newFileName()));
//					files.get(i).setFile(new File(source.resolveSibling(renamer.newFileName()).toString()));
//				} catch(IOException e) {
//					System.out.println(files.get(i).getFileName() + " could not be moved.");
//					e.printStackTrace();
//				} catch(InvalidPathException e) {
//					System.out.println(files.get(i).getFileName() + " could not be converted to a path.");
//					e.printStackTrace();
//				}
//				prefixIndex++;
//				suffixIndex++;
//			}
//			filesListView.refresh();
//			filesListView.getSelectionModel().selectRange(0, filesListView.getItems().size());
//			filesListView.getSelectionModel().clearSelection();
//		}
//	}
	
	@FXML
	public void handleExit() {
		Platform.exit();
		System.exit(0);
	}
}
