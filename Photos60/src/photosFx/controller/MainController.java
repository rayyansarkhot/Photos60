package photosFx.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import java.io.IOException;

public class MainController {

    @FXML
    private Button loginButton;

    @FXML
    private void handleLoginButtonClicked() {
        try {
            // Load the FXML file for the new scene
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/albums.fxml"));
            Parent secondSceneRoot = loader.load();

            // Create a new scene with the loaded FXML file
            Scene secondScene = new Scene(secondSceneRoot,800,600);

            // Get the stage from the button and set the new scene
            Stage stage = (Stage) loginButton.getScene().getWindow();
            stage.setScene(secondScene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
