package photosFx.controller;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import javafx.fxml.Initializable;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import photosFx.model.Album;

public class PhotoGridController implements Initializable {

    @FXML
    public Button logoutButton;

    @FXML
    public Button backButton;

    public void initialize(URL url, ResourceBundle resourceBundle) {
        // This method will be called when the FXML file is loaded

    }

    private void displayData(List<Album> albums) {

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

    @FXML
    private void backButtonClicked() {
        try {
            // Load the FXML file for the new scene
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/albums.fxml"));
            Parent secondSceneRoot = loader.load();

            // Create a new scene with the loaded FXML file
            Scene secondScene = new Scene(secondSceneRoot, 800, 600);

            // Get the stage from the button and set the new scene
            Stage stage = (Stage) backButton.getScene().getWindow();
            stage.setScene(secondScene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
