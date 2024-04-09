package photosFx.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import photosFx.model.*;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.util.Pair;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


/**
 * Base controller for the application, common method for slideshow, opening the photo, and editing captions and tags
 */
public class BaseController implements Initializable {
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    @FXML
    public ImageView imageView;
    public Photo selectedPhoto;

    public void setCurrentPhoto(Photo currentImage) {
        this.selectedPhoto = currentImage;
    }

    /**
     * Open the selected photo in the photo editor
     */
    public static class EditPhotoController extends BaseController {


        @FXML
        public Button goBackButton;
        @FXML
        public Button createTagButton;
        @FXML
        public Button deleteTagButton;

        @FXML
        public TextArea photoCaption;
        @FXML
        public Text imagePath;

        @FXML
        public Text imageDate;

        @FXML
        private Text textField;

        @FXML
        private TableView<Tag> tagView;


        /**
         * Refresh the view with the selected photo's information
         */
        public void refresh() {
            Photo photo = selectedPhoto;
            textField.setText(photo.getName());

            imagePath.setText(selectedPhoto.getFilePath());
            imageView.setImage(new Image("file:" + selectedPhoto.getFilePath()));
            imageDate.setText(selectedPhoto.getDate().toString());

            List<Tag> tags = photo.getTags();
            updateCaptionField();
            displayTags(tags);
        }




        public void debug(ActionEvent actionEvent) {
            System.out.print("debug");
        }


        public void exitButtonClicked(ActionEvent actionEvent) throws IOException {
            System.out.println("exit button");
            Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
            // PhotoGridController.refresh();
            stage.close();
        }

        /**
         * Save changes to the photo's caption and tags (which is dynamically updated)
         * @param actionEvent
         */
        @FXML
        public void saveChanges(ActionEvent actionEvent) {
            AlbumsController.updateContent();
            if (!photoCaption.getText().equals(selectedPhoto.getCaption())) {
                System.out.println("caption has changed");
                changeCaption(photoCaption.getText());
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText(null);
                alert.setContentText("Caption has been updated to " + photoCaption.getText());
                alert.showAndWait();
            }
            Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
            stage.close();
        }

        /**
         * Create a new tag for the selected photo
         * @param actionEvent
         */
        @FXML
        public void createTag(ActionEvent actionEvent) {

            /* --- dialog box for creating a tag --- */

            Dialog<Pair<String, String>> dialog = new Dialog<>();
            ButtonType confirmButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(confirmButtonType, ButtonType.CANCEL);
            dialog.setTitle("Add Tag");
            dialog.setContentText("Enter key and value pair: ");

            TextField tagName = new TextField();
            tagName.setPromptText("Key (ex. location)");

            TextField tagValue = new TextField();
            tagValue.setPromptText("Value (ex. New York)");

            HBox hbox = new HBox();
            hbox.setAlignment(Pos.CENTER_LEFT);
            hbox.setSpacing(10);
            hbox.getChildren().addAll(new Label("Enter key value pair"), tagName, new Label(":"), tagValue);

            dialog.getDialogPane().setContent(hbox);

            dialog.setResultConverter(dialogButton -> {
                if (dialogButton == confirmButtonType) {
                    return new Pair<>(tagName.getText(), tagValue.getText());
                }
                return null;
            });

            Optional<Pair<String, String>> result = dialog.showAndWait();

            result.ifPresent(keyValuePair -> {
                System.out.println("Tag Name=" + keyValuePair.getKey() + ", Tag Value=" + keyValuePair.getValue());
                selectedPhoto.addTag(new Tag(keyValuePair.getKey(), keyValuePair.getValue()));
                AlbumsController.updateContent();
            });

            List<Tag> tags = selectedPhoto.getTags();
            displayTags(tags);


            /* --- end dialog box --- */


        }

        /**
         * Delete the selected tag from the photo
         * @param actionEvent
         */
        @FXML
        public void deleteTag(ActionEvent actionEvent) {
            Tag selectedTag = tagView.getSelectionModel().getSelectedItem();
            selectedPhoto.removeTag(selectedTag);
            displayTags();
            if (selectedTag == null) {
                System.out.println("no tag selected");
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("No tag selected. Please select a tag to delete.");
                alert.showAndWait();
                return;
            }
        }

        /**
         * Change the caption of the selected photo
         * @param newCaption
         */
        public void changeCaption(String newCaption) {
            selectedPhoto.setCaption(newCaption);
            AlbumsController.updateContent();
            updateCaptionField();
        }

        /**
         * Update the caption field with the selected photo's caption
         */
        private void updateCaptionField() {
            photoCaption.setText(selectedPhoto.getCaption());
        }

        private void displayTags() {
            List<Tag> tags = selectedPhoto.getTags();
            displayTags(tags);
        }

        private void displayTags(List<Tag> tags) {


            tagView.getItems().clear();

            // Retrieve the existing TableColumn from the TableView
            TableColumn<Tag, String> nameColumn = (TableColumn<Tag, String>) tagView.getColumns().get(0);
            TableColumn<Tag, String> valueColumn = (TableColumn<Tag, String>) tagView.getColumns().get(1);

            // Set up cell value factory to extract the name from the Album object
            nameColumn.setCellValueFactory(new PropertyValueFactory<>("tagName"));
            valueColumn.setCellValueFactory(new PropertyValueFactory<>("tagValue"));


            // Add data to the TableView
            tagView.getItems().addAll(tags);
        }

        @FXML
        private void goBackButton(ActionEvent actionEvent) {
            try {
                // Load the FXML file for the new scene
                FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/albums.fxml"));
                Parent secondSceneRoot = loader.load();

                // Create a new scene with the loaded FXML file
                Scene secondScene = new Scene(secondSceneRoot, 800, 600);

                // Get the stage from the button and set the new scene
                Stage stage = (Stage) goBackButton.getScene().getWindow();
                stage.setScene(secondScene);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Controller for the slideshow view, only shows photo + name and allows for navigation
     */
    public static class SlideshowController extends BaseController {

        @FXML
        public Text imageName;
        @FXML
        public ImageView imageView;
        @FXML
        public Button goBackButton;

        @FXML
        public Button goLeftButton;
        @FXML
        public Button goRightButton;

        public List<Photo> album;

        public void refresh() {
            Photo photo = selectedPhoto;
            imageName.setText(photo.getName());
            imageView.setImage(new Image("file:" + photo.getFilePath()));
        }

        /**
         * Go to the previous photo in the album
         * @param actionEvent
         */
        @FXML
        public void goLeft(ActionEvent actionEvent) {
            System.out.println("go left selected");

            List<Photo> photos = album;
            int index = photos.indexOf(selectedPhoto);
            if (index == 0) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("You are at the first photo.");
                alert.showAndWait();
            } else {
                selectedPhoto = photos.get(index - 1);
            }
            refresh();
        }

        /**
         * Go to the next photo in the album
         * @param actionEvent
         */
        @FXML
        public void goRight(ActionEvent actionEvent) {
            List<Photo> photos = album;
            int index = photos.indexOf(selectedPhoto);
            if (index == photos.size() - 1) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("You are at the last photo.");
                alert.showAndWait();
            } else {
                selectedPhoto = photos.get(index + 1);
            }
            refresh();
        }

        /**
         * Go back to the photo grid view
         * @param actionEvent
         */
        @FXML
        public void goBack(ActionEvent actionEvent) {
            try {
                // Load the FXML file for the new scene
                FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/photogrid.fxml"));
                Parent secondSceneRoot = loader.load();
                PhotoGridController controller = loader.getController();
                controller.refresh();

                // Create a new scene with the loaded FXML file
                Scene secondScene = new Scene(secondSceneRoot, 800, 600);

                // Get the stage from the button and set the new scene
                Stage stage = (Stage) goBackButton.getScene().getWindow();
                stage.setScene(secondScene);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        /**
         * Set the current album to display
         * @param sortedPhotos
         */
        public void setCurrentAlbum(List<Photo> sortedPhotos) {
            this.album = sortedPhotos;
        }
    }
}