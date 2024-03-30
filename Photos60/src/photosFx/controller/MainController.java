package photosFx.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import photosFx.model.Album;
import photosFx.model.Content;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class MainController {

    @FXML
    private Button loginButton;
    @FXML
    private TextField usernameField;

    @FXML
    private void handleLoginButtonClicked() {
        try {
            AlbumsController.username = usernameField.getText(); // Retrieve text from TextField

            // Load the FXML file for the new scene
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/albums.fxml"));
            Parent secondSceneRoot = loader.load();

            // Create a new scene with the loaded FXML file
            Scene secondScene = new Scene(secondSceneRoot, 800, 600);

            // Get the stage from the button and set the new scene
            Stage stage = (Stage) loginButton.getScene().getWindow();
            stage.setScene(secondScene);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
