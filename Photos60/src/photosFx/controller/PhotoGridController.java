package photosFx.controller;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import java.text.SimpleDateFormat;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;
import javafx.scene.text.FontPosture;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import photosFx.model.Album;
import photosFx.model.ContentSerializer;
import photosFx.model.Photo;
import static photosFx.controller.AlbumsController.content;
import static photosFx.controller.AlbumsController.username;
import photosFx.controller.BaseController.EditPhotoController;

public class PhotoGridController implements Initializable {

    @FXML
    public Button logoutButton;

    @FXML
    public Button backButton;
    @FXML
    public Button addPhotoButton;
    @FXML
    public Button refreshButton;




    @FXML
    ImageView photoCover;

    @FXML
    Text photoCaption;
    @FXML
    Text photoDate;
    @FXML
    Text photoName;

    @FXML
    Button openSlideshowButton;
    @FXML
    Button editPhotoButton;
    @FXML
    Button deletePhotoButton;
    @FXML
    Button copyPhotoButton;
    @FXML
    Button movePhotoButton;

    Photo populatePhoto;
    private static Photo currentImage;
    public static Album currentAlbum; // Add this line

    public static List<Photo> sortedPhotos;
    private static ImageView lastSelectedImage = null;

    FileChooser fileChooser = new FileChooser();

    // method for individual photo hover effect
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (photoCover != null) {
            photoCover.hoverProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue) {
                    if (photoCover.getEffect() != null) {
                        return;
                    };
                    DropShadow glow = new DropShadow();
                    glow.setColor(Color.web("#039ED3"));
                    glow.setRadius(7);
                    glow.setSpread(.8);
                    photoCover.setEffect(glow);
                } else {
                    // ensures photo stays red
                    if (photoMap.get(photoCover) != currentImage) {
                        photoCover.setEffect(null);
                        photoCover.setOpacity(1);
                    }
                }
            });
        }
    }

    private void displayData(List<Album> albums) {

    }


    @FXML
    FlowPane photoScroll;
    @FXML
    AnchorPane anchorPane;
    public void refresh() {
        Album album = currentAlbum;
        List<Photo> photos = album.getPhotos();
        photoScroll.getChildren().clear();
        sortedPhotos = photos.stream().sorted(Comparator.comparing(Photo::getName))
                .toList();
        // might not be the best way, check Album model updateDateRange() instead
        if (sortedPhotos.isEmpty()) {
            currentAlbum.setEarliestPhoto(null);
            currentAlbum.setLatestPhoto(null);
            return;
        }
        currentAlbum.setEarliestPhoto(sortedPhotos.get(0).getDate());
        currentAlbum.setLatestPhoto(sortedPhotos.get(sortedPhotos.size() - 1).getDate());

        for (Photo photo : sortedPhotos) {
            try {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("../view/singlephoto.fxml"));
                AnchorPane photoCover = loader.load();
                PhotoGridController controller = loader.getController();
                controller.setPhoto(photo);
                photoScroll.getChildren().add(photoCover);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            ContentSerializer.saveContent(AlbumsController.content, username);
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

    public void setCurrentAlbum(Album album) {
        this.currentAlbum = album;
    }

    @FXML
    public void addPhoto(ActionEvent actionEvent) throws IOException {
        fileChooser.setTitle("Select Picture");
        fileChooser.setInitialDirectory(
                new File(System.getProperty("user.home")));
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All images", "*.png", "*.jpg", "*.jpeg", "*.gif"));
        Stage primaryStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        File file = fileChooser.showOpenDialog(primaryStage);
        if (file == null) {
            return;
        }
        Photo newPhoto = new Photo(file);
        if (currentAlbum != null) {
            if (!currentAlbum.doesPhotoExist(newPhoto)) {
                currentAlbum.addPhoto(newPhoto);
                refresh();
            } else {
                return;
            }
        }
        refresh();
    }

    SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");

    private Map<ImageView, Photo> photoMap = new HashMap<>();
    public void setPhoto(Photo photo) {
        photoName.setText(photo.getName().substring(0, Math.min(photo.getName().length(), 17))
                + (photo.getName().length() > 17 ? "..." : ""));
        String caption = photo.getCaption();
        if (caption == null || caption.isEmpty()) {
            photoCaption.setText("No caption");
            photoCaption.setFill(Color.GRAY);
        } else {
            photoCaption.setText(caption);
        }
        photoDate.setText(formatter.format(photo.getDate()));
        photoCover.setImage(new Image(photo.getFile().toURI().toString()));
        photoMap.put(photoCover, photo);
        populatePhoto = photo;
        return;
    }

    /* ------ methods for photo options ------ */

    public void editPhoto() throws IOException {
        System.out.println("edit photo clicked");
        if (currentImage != null) {
            try {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("../view/editphoto.fxml"));
                Parent secondSceneRoot = loader.load();

                EditPhotoController editPhotoController = loader.getController();
                editPhotoController.setCurrentPhoto(currentImage);

                // controller.setCurrentPhoto(currentImage);
                Scene secondScene = new Scene(secondSceneRoot, 800, 600);
                Stage stage = new Stage();
                editPhotoController.refresh();
                stage.setScene(secondScene);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {noPhotoAlert();}
    }


    public void openSlideshow() throws IOException {
        System.out.println("open slideshow clicked");
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("../view/slideshow.fxml"));
            Parent secondSceneRoot = loader.load();

            BaseController.SlideshowController slideshowController = loader.getController();
            if (currentImage != null) {
                slideshowController.setCurrentPhoto(currentImage);
            } else {
                slideshowController.setCurrentPhoto(sortedPhotos.get(0));
            }
            slideshowController.setCurrentAlbum(sortedPhotos);

            Scene secondScene = new Scene(secondSceneRoot, 800, 600);
            Stage stage = (Stage) openSlideshowButton.getScene().getWindow();
            stage.setTitle("Opening slideshow ...");
            slideshowController.refresh();
            stage.setScene(secondScene);
            stage.show();
            stage.setTitle("Slideshow");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void deletePhoto() throws IOException {
        System.out.println("delete photo clicked");
        if (currentImage != null) {
            currentAlbum.removePhoto(currentImage);
            refresh();
        } else {noPhotoAlert();}
    }
    public void noPhotoAlert() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText("No photo selected.");
        alert.showAndWait();
    }

    private void transferPhoto(boolean isMoveOperation) throws IOException {
        System.out.println((isMoveOperation ? "move" : "copy") + " photo clicked");
        if (currentImage != null) {
            // Get the names of all albums
            List<String> choices = new ArrayList<>();
            for (Album album : content.albums) {
                choices.add(album.getName());
            }

            // Create a dialog to ask for the album name
            ChoiceDialog<String> dialog = new ChoiceDialog<>(null, choices);
            dialog.setTitle((isMoveOperation ? "Move" : "Copy") + " Photo");
            dialog.setHeaderText("Choose the album you want to " + (isMoveOperation ? "move" : "copy") + " the photo to:");
            Optional<String> result = dialog.showAndWait();

            // If the user made a selection, try to find the album
            if (result.isPresent()) {
                String albumName = result.get();
                Album targetAlbum = null;
                for (Album album : content.albums) {
                    if (album.getName().equals(albumName)) {
                        targetAlbum = album;
                        break;
                    }
                }

                // If the album was found, copy or move the photo
                if (targetAlbum != null) {
                    // note: consider removing isMoveOperatioon here
                    if (isMoveOperation && targetAlbum.doesPhotoExist(currentImage)) {
                        return;
                    }
                    targetAlbum.addPhoto(currentImage);
                    if (isMoveOperation) {
                        currentAlbum.removePhoto(currentImage);
                    }
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Success");
                    alert.setHeaderText(null);
                    alert.setContentText("The photo was successfully " + (isMoveOperation ? "moved" : "copied") + " to the album " + albumName + ".");
                    alert.showAndWait();
                    refresh();
                } else {
                    // If the album was not found, show an error message
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText(null);
                    alert.setContentText("The album " + albumName + " does not exist.");
                    alert.showAndWait();
                }
            }
        } else {noPhotoAlert();}
    }

    @FXML
    public void copyPhoto() throws IOException {
        transferPhoto(false);
    }

    @FXML
    public void movePhoto() throws IOException {
        transferPhoto(true);
    }


    public void selectedPhoto(MouseEvent mouseEvent) {
        ImageView imageView = (ImageView) mouseEvent.getSource();
        if (lastSelectedImage == imageView) {
            return;
        }
        // ensure valid image
        if (photoMap.containsKey(imageView)) {
            choose(imageView);
        }
    }

    public void choose(ImageView selectedImage) {
        // Remove effect and reset opacity for previously selected image
        if (lastSelectedImage != null) {
            lastSelectedImage.setEffect(null);
            lastSelectedImage.setOpacity(1);
        }
        // Highlight the selected image or apply some effect to indicate selection
        selectedImage.setOpacity(0.5);
        // Update the reference to the last selected image
        lastSelectedImage = selectedImage;
        currentImage = photoMap.get(selectedImage);
        System.out.println("Selected " + selectedImage.getImage().getUrl());
    }
}
