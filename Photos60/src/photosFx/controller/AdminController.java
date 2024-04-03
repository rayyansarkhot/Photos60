package photosFx.controller;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import photosFx.model.Album;
import photosFx.model.Content;
import photosFx.model.ContentSerializer;

public class AdminController implements Initializable {
    @FXML
    private Button logoutButton;
    @FXML
    private Button createUserButton;
    @FXML
    private Button deleteUserButton;

    @FXML
    private TableView<String> tableView;

    public void initialize(URL url, ResourceBundle resourceBundle) {

        displayData(getUserList());

    }

    private List<String> getUserList() {
        // Directory path where .dat files are located
        String directoryPath = "src/photosFx/model/users";

        // Create a File object representing the directory
        File directory = new File(directoryPath);

        // Check if the directory exists
        if (directory.exists() && directory.isDirectory()) {
            // Get list of files in the directory
            File[] files = directory.listFiles();

            // List to store filenames without extension
            List<String> fileNames = new ArrayList<>();

            // Iterate through the files
            for (File file : files) {
                // Check if it's a file and ends with .dat extension
                if (file.isFile() && file.getName().toLowerCase().endsWith(".dat")) {
                    // Extract filename without extension
                    String fileNameWithoutExtension = file.getName().replaceFirst("[.][^.]+$", "");
                    // Add to the list
                    fileNames.add(fileNameWithoutExtension);
                }
            }

            // // Print the filenames without extension
            // for (String fileName : fileNames) {
            // System.out.println(fileName);
            // }
            return fileNames;
        } else {
            System.out.println("Directory does not exist or is not a directory.");
            return null;
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

    private void displayData(List<String> usernames) {
        // Clear the existing items in the table view
        tableView.getItems().clear();

        // Create an observable list to hold the usernames
        ObservableList<String> data = FXCollections.observableArrayList();

        // Add each username to the observable list
        data.addAll(usernames);

        // Set the items of the table view to the observable list
        tableView.setItems(data);

        // Optionally, you can configure the columns if needed
        // For example, if you have a single column for usernames:
        TableColumn<String, String> usernameColumn = (TableColumn<String, String>) tableView.getColumns().get(0);
        usernameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()));
        tableView.getColumns().setAll(usernameColumn);
    }

    @FXML
    private void handleDeleteUserButtonClicked() {
        String selectedUser = tableView.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {

            // Specify the file path
            String filePath = "src/photosFx/model/users/" + selectedUser + ".dat";

            // Create a File object representing the file to be deleted
            File fileToDelete = new File(filePath);

            // Check if the file exists
            if (fileToDelete.exists()) {
                // Attempt to delete the file
                boolean isDeleted = fileToDelete.delete();

                // Check if deletion was successful
                if (isDeleted) {

                    // Show message popup indicating album is deleted
                    Alert alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Confirmation");
                    alert.setHeaderText(null);
                    alert.setContentText(selectedUser + " User deleted successfully.");
                    alert.showAndWait();

                    displayData(getUserList());
                } else {
                    System.out.println("Failed to delete the file.");
                }
            } else {
                System.out.println("File does not exist.");
            }

        } else {
            // Show message popup indicating no album is selected
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("No user selected. Please select a user to delete.");
            alert.showAndWait();
        }
    }

    @FXML
    private void handleCreateUserButtonClicked() {

        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Enter Username");
        dialog.setHeaderText(null);
        dialog.setContentText("Please enter the username:");

        Optional<String> result = dialog.showAndWait();

        result.ifPresent(username -> {
            // Handle the entered album name here
            String enteredText = result.get().toUpperCase().strip();

            if (enteredText.equals("")) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Empty username not allowed!");
                alert.showAndWait();
                return;
            }
            else if (getUserList().contains(enteredText)) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("User already exists!");
                alert.showAndWait();
                return;
            }

            Content content = new Content();
            try {
                ContentSerializer.saveContent(content, enteredText);
            } catch (IOException e) {
                e.printStackTrace();
            }

            displayData(getUserList());

        });
    }

}
