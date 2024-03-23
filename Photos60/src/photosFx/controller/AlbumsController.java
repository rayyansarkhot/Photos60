package photosFx.controller;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class AlbumsController {

    @FXML
    private Button logoutButton;

    @FXML
    private void handleLogoutButtonClicked() {
        try {
            // Load the FXML file for the new scene
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/login.fxml"));
            Parent secondSceneRoot = loader.load();

            // Create a new scene with the loaded FXML file
            Scene secondScene = new Scene(secondSceneRoot,600,400);

            // Get the stage from the button and set the new scene
            Stage stage = (Stage) logoutButton.getScene().getWindow();
            stage.setScene(secondScene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
