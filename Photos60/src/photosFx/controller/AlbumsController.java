package photosFx.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.TextInputDialog;
import java.util.Optional;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import photosFx.model.Album;
import photosFx.model.ContentSerializer;
import photosFx.model.Content;

public class AlbumsController implements Initializable {

    public static String username = "";
    public Content content;

    @FXML
    private Button logoutButton;
    @FXML
    private Button createAlbumButton;
    @FXML
    private Button deleteAlbumButton;
    @FXML
    private Button renameAlbumButton;
    @FXML
    private Button openAlbumButton;
    @FXML
    private TableView<Album> tableView;

    public void initialize(URL url, ResourceBundle resourceBundle) {
        // This method will be called when the FXML file is loaded
        if (userExists(username)) {
            try {
                content = ContentSerializer.loadContent(username);
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
                content = new Content();
            }
        } else {
            content = new Content();
        }

        displayData(content.albums);

        try {
            ContentSerializer.saveContent(content, username);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void displayData(List<Album> albums) {

        tableView.getItems().clear();

        // Retrieve the existing TableColumn from the TableView
        TableColumn<Album, String> nameColumn = (TableColumn<Album, String>) tableView.getColumns().get(0);
        TableColumn<Album, String> numPhotosColumn = (TableColumn<Album, String>) tableView.getColumns().get(1);
        TableColumn<Album, String> earliestPhotoColumn = (TableColumn<Album, String>) tableView.getColumns().get(2);
        TableColumn<Album, String> latestPhotoColumn = (TableColumn<Album, String>) tableView.getColumns().get(3);

        // Set up cell value factory to extract the name from the Album object
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        numPhotosColumn.setCellValueFactory(new PropertyValueFactory<>("numPhotos"));
        // earliestPhotoColumn.setCellValueFactory(new
        // PropertyValueFactory<>("earliestPhoto"));
        // latestPhotoColumn.setCellValueFactory(new
        // PropertyValueFactory<>("latestPhoto"));

        // Add data to the TableView
        tableView.getItems().addAll(albums);
    }

    public boolean userExists(String name) {

        String directoryPath = "src/photosFx/model/users";
        String fileName = name.toLowerCase() + ".dat";
        File directory = new File(directoryPath);
        File file = new File(directory, fileName);

        // Check if the file exists
        if (file.exists()) {
            System.out.println("file exists.");
            return true;
        } else {
            return false;
        }

    }

    @FXML
    private void handleRenameAlbumButtonClicked() {
        Album selectedAlbum = tableView.getSelectionModel().getSelectedItem();
        if (selectedAlbum != null) {
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Enter new Album Name for " + selectedAlbum.getName());
            dialog.setHeaderText("Rename");
            dialog.setContentText("Please enter the new album name:");

            Optional<String> result = dialog.showAndWait();

            result.ifPresent(albumName -> {
                // Handle the entered album name here
                String enteredText = result.get().toUpperCase().strip();

                if (enteredText.equals("")) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText(null);
                    alert.setContentText("Empty album name not allowed!");
                    alert.showAndWait();
                    return;
                } else if (content.getAlbumNames().contains(enteredText)) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText(null);
                    alert.setContentText("Album already exists!");
                    alert.showAndWait();
                    return;
                }

                selectedAlbum.setName(enteredText.toUpperCase());
                displayData(content.albums);
                try {
                    ContentSerializer.saveContent(content, username);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        } else {
            // Show message popup indicating no album is selected
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("No album selected. Please select an album to rename.");
            alert.showAndWait();
        }
    }

    @FXML
    private void handleCreateAlbumButtonClicked() {

        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Enter Album Name");
        dialog.setHeaderText(null);
        dialog.setContentText("Please enter the album name:");

        Optional<String> result = dialog.showAndWait();

        result.ifPresent(albumName -> {
            // Handle the entered album name here
            String enteredText = result.get().toUpperCase().strip();

            if (enteredText.equals("")) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Empty album name not allowed!");
                alert.showAndWait();
                return;
            } else if (content.getAlbumNames().contains(enteredText)) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Album already exits!");
                alert.showAndWait();
                return;
            }
            content.albums.add(new Album(enteredText.toUpperCase()));
            displayData(content.albums);
            try {
                ContentSerializer.saveContent(content, username);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    @FXML
    private void handleDeleteAlbumButtonClicked() {
        Album selectedAlbum = tableView.getSelectionModel().getSelectedItem();
        if (selectedAlbum != null) {
            // Delete the selected album
            String deletedAlbumName = deleteAlbum(selectedAlbum);

            // Show message popup indicating album is deleted
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Confirmation");
            alert.setHeaderText(null);
            alert.setContentText(deletedAlbumName + " Album deleted successfully.");
            alert.showAndWait();

        } else {
            // Show message popup indicating no album is selected
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("No album selected. Please select an album to delete.");
            alert.showAndWait();
        }
    }

    private String deleteAlbum(Album selectedAlbum) {

        String name = selectedAlbum.getName();
        content.albums.remove(selectedAlbum);
        displayData(content.albums);

        try {
            ContentSerializer.saveContent(content, username);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return name;

    }

    @FXML
    private void openAlbum() {
        try {
            // Load the FXML file for the new scene
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/photogrid.fxml"));
            Parent secondSceneRoot = loader.load();

            // Create a new scene with the loaded FXML file
            Scene secondScene = new Scene(secondSceneRoot, 600, 400);

            // Get the stage from the button and set the new scene
            Stage stage = (Stage) openAlbumButton.getScene().getWindow();
            stage.setScene(secondScene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleLogoutButtonClicked() {
        try {
            // Load the FXML file for the new scene
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/login.fxml"));
            Parent secondSceneRoot = loader.load();

            // Create a new scene with the loaded FXML file
            Scene secondScene = new Scene(secondSceneRoot, 600, 400);

            // Get the stage from the button and set the new scene
            Stage stage = (Stage) logoutButton.getScene().getWindow();
            stage.setScene(secondScene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
