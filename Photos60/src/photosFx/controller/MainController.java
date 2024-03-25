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
            String username = usernameField.getText(); // Retrieve text from TextField

            // Load the FXML file for the new scene
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/albums.fxml"));
            Parent secondSceneRoot = loader.load();

            // Create a new scene with the loaded FXML file
            Scene secondScene = new Scene(secondSceneRoot, 800, 600);

            // Get the stage from the button and set the new scene
            Stage stage = (Stage) loginButton.getScene().getWindow();
            stage.setScene(secondScene);

            Content content;
            if (userExists(username)) {
                try {
                    FileInputStream fileIn = new FileInputStream("src/photosFx/model/users"+ username + ".dat");
                    ObjectInputStream in = new ObjectInputStream(fileIn);
                    content = (Content) in.readObject();
                    in.close();
                    fileIn.close();

                    // Now you can use the deserializedContent object
                    List<Album> albums = content.albums;
                    System.out.println(albums);

                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            } else {
                content = new Content();
            }

            /*
             *
             * Need to now display the data in content, i.e. the album info, on the table itself
             */

        } catch (IOException e) {
            e.printStackTrace();
        }
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
}
