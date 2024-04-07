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
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import photosFx.model.*;

import java.io.IOException;
import java.net.URL;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


import javafx.scene.layout.GridPane;
import javafx.util.Pair;

import java.util.Optional;
import java.util.stream.Collectors;


import static photosFx.controller.AlbumsController.username;


public class EditPhotoController implements Initializable {

    public Photo selectedPhoto;
    @FXML
    public Button exitButton;
    @FXML
    public Button createTagButton;
    @FXML
    public Button deleteTagButton;
//    @FXML
//    public Text photoDate;
//
//    @FXML
//    ImageView photoView;
//
    @FXML
    public TextArea photoCaption;
    @FXML
    public Text imagePath;
    @FXML
    public ImageView imageView;
    @FXML
    public Text imageDate;

    @FXML
    private Text textField;

    @FXML
    private TableView <Tag> tagView;
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

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



    public void setCurrentPhoto(Photo currentImage) {
        this.selectedPhoto = currentImage;
//        textField = selectedPhoto.getName();
    }

    public void debug(ActionEvent actionEvent) {
        // updateTextField("hello");
        System.out.print("debug");
    }

    public void updateTextField(String text) {
        textField.setText("good morning");
    }

    public void exitButtonClicked(ActionEvent actionEvent) throws IOException {
        System.out.println("exit bu");
        Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        stage.close();
    }

    @FXML
    public void saveChanges(ActionEvent actionEvent) {
        AlbumsController.updateContent();
        if (photoCaption.getText() != selectedPhoto.getCaption()) {
            System.out.println("caption has changed");
            changeCaption(photoCaption.getText());
        }
        Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        stage.close();
    }

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

    public void changeCaption(String newCaption) {
        selectedPhoto.setCaption(newCaption);
        AlbumsController.updateContent();
        updateCaptionField();
    }

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


}