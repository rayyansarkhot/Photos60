package photosFx.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

import photosFx.model.Album;
import photosFx.model.Photo;
import photosFx.model.Tag;

import javafx.scene.Parent;
import javafx.scene.Scene;
import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;

import static photosFx.controller.AlbumsController.content;

public class SearchController implements Initializable {
    @FXML
    public ChoiceBox andOrSelect;

    @FXML
    TextField firstKey, firstValue;
    @FXML
    TextField secondKey, secondValue;
    @FXML
    Button makeAlbum, logoutButton, searchByTagButton, searchByDateButton, backButton;
    @FXML
    ListView<String> searchResults;
    @FXML
    DatePicker pickStartDate, pickToDate;

    ObservableList<String> observablePhotoSearchResultsArray = FXCollections
            .observableArrayList(new ArrayList<String>());

    ArrayList<Photo> photoSearchResultsArray = new ArrayList<Photo>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        andOrSelect.getItems().addAll(" ", "AND", "OR");
        andOrSelect.setValue(" ");

        secondKey.setDisable(true);
        secondValue.setDisable(true);

        andOrSelect.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !newValue.equals(" ")) {
                // If a value is selected, enable man2 fields
                secondKey.setDisable(false);
                secondValue.setDisable(false);
            } else {
                // If no value is selected, disable man2 fields
                secondKey.clear();
                secondValue.clear();
                secondKey.setDisable(true);
                secondValue.setDisable(true);
            }
        });

        refresh();
    }

    public void refresh() {
        observablePhotoSearchResultsArray.clear();
        ArrayList<String> photoDetails = new ArrayList<String>();
        for (Photo p : photoSearchResultsArray) {
            photoDetails.add(p.getName());
        }
        observablePhotoSearchResultsArray = FXCollections.observableArrayList(photoDetails);
        searchResults.setItems(observablePhotoSearchResultsArray);
        searchResults.refresh();
    }

    public void searchByTag(ActionEvent e) {
        String man1name = firstKey.getText().trim();
        String man1value = firstValue.getText().trim();
        String man2name = secondKey.getText().trim();
        String man2value = secondValue.getText().trim();

        if (man1name.isEmpty() || man1value.isEmpty()) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Mandatoradfy Tag Empty");
            alert.setHeaderText("There must be at least 1 mandatory tag to search for!");
            alert.showAndWait();
            return;
        }

        ArrayList<Tag> searchTags = new ArrayList<Tag>();
        Tag tag1 = new Tag(man1name, man1value);
        searchTags.add(tag1);
        Object selectedValue = andOrSelect.getValue();


        if (man2name.isEmpty() || man2value.isEmpty()) {
            if (!selectedValue.equals(" ")) {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Tag 2 field empty");
                alert.setHeaderText("One of the fields for tag 2 is empty, conducting search with only tag 1.");
                alert.showAndWait();
            }
            photoSearchResultsArray = orTagSearch(searchTags);
        } else {
            Tag tag2 = new Tag(man2name, man2value);
            searchTags.add(tag2);
            if (selectedValue.equals("AND")) {
                photoSearchResultsArray = andTagSearch(searchTags);
            } else if (selectedValue.equals("OR")) {
                photoSearchResultsArray = orTagSearch(searchTags);
            }
        }
        refresh();
    }

    public ArrayList<Photo> andTagSearch(ArrayList<Tag> searchTags) {
        ArrayList<Photo> results = new ArrayList<Photo>();
        HashSet<Photo> noDuplicates = new HashSet<Photo>();
        boolean found = false;
        for (Album a : content.albums) {
            for (Photo picture : a.getPhotos()) {
                for (Tag searchTag : searchTags) {
                    if (!picture.hasTag(searchTag)) {
                        found = false;
                        break;
                    } else {
                        found = true;
                    }
                }
                if (found) {
                    noDuplicates.add(picture);
                }
            }
        }
        results.addAll(noDuplicates);
        return results;
    }

    public ArrayList<Photo> orTagSearch(ArrayList<Tag> searchTags) {
        ArrayList<Photo> results = new ArrayList<Photo>();
        HashSet<Photo> noDuplicates = new HashSet<Photo>();
        for (Album a : content.albums) {
            for (Photo picture : a.getPhotos()) {
                for (Tag searchTag : searchTags) {
                    for (Tag photoTag : picture.getTags()) {
                        if (photoTag.getTagName().toLowerCase().equals(searchTag.getTagName().toLowerCase())
                                && photoTag.getTagValue().toLowerCase().equals(searchTag.getTagValue().toLowerCase())) {
                            noDuplicates.add(picture);
                        }
                    }
                }
            }
        }
        results.addAll(noDuplicates);
        return results;
    }


    public void searchByDate(ActionEvent e) throws ParseException {
        String dateBeginning = pickStartDate.getValue().toString();
        String dateEnd = pickToDate.getValue().toString();

        if (dateBeginning.isEmpty() || dateBeginning == null || dateEnd.isEmpty() || dateEnd == null) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Empty Input");
            alert.setHeaderText("Please enter dates using the date picker and try again.");
            alert.showAndWait();
            return;
        }

        pickStartDate.cancelEdit();
        pickToDate.cancelEdit();
        Date date1;
        Date date2;
        String pattern = "yyyy-MM-dd";
        try {
            date1 = new SimpleDateFormat(pattern).parse(dateBeginning);
            date2 = new SimpleDateFormat(pattern).parse(dateEnd);
        } catch (Exception exception) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Invalid input");
            alert.showAndWait();
            return;
        }
        if (date2.before(date1)) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Invalid Date Range");
            alert.setHeaderText(
                    "Please enter valid dates in mm/dd/yyyy format! The first date must be less than or equal to the second date.");
            alert.showAndWait();
            return;
        }

        photoSearchResultsArray = getPhotosInRange(date1, date2);
        refresh();

    }

    public ArrayList<Photo> getPhotosInRange(Date date1, Date date2) {
        ArrayList<Photo> results = new ArrayList<Photo>();
        for (Album album : content.albums) {
            for (Photo picture : album.getPhotos()) {
                Date testDate = picture.getDate();
                if (testDate.compareTo(date1) >= 0 && testDate.compareTo(date2) <= 0) {
                    if (results.contains(picture)) {
                        continue;
                    } else {
                        results.add(picture);
                    }
                }
            }
        }
        return results;
    }

    public void saveResultsAsAlbum(ActionEvent e) {
        if (photoSearchResultsArray.size() == 0) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("No Search Results");
            alert.setHeaderText("No results that match your search.");
            alert.setContentText("Please try different search parameters");
            alert.showAndWait();
            return;
        }

        Optional<String> newAlbumName = AlbumsController.openAlbumCreationDialog();
        if (newAlbumName.isEmpty()) {
            return;
        }

        // Album newAlbum = new Album(newAlbumName.get(), photoSearchResultsArray);
        content.albums.add(new Album(newAlbumName.get().toUpperCase(), photoSearchResultsArray));
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText(null);
        alert.setContentText(newAlbumName.get() + " album created successfully.");
        alert.showAndWait();
        AlbumsController.updateContent();
    }

    public void handleLogoutButtonClicked(ActionEvent actionEvent) {
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