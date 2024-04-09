package photosFx.model;

import javafx.scene.control.Alert;
import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Album implements Serializable {
    public String name;
    public int numPhotos;
    public Date earliestPhoto;
    public Date latestPhoto;
    public List<Photo> photos;
    static final long serialVersionUID = 1L;

    /** Constructor for Album. */
    public Album(String name) {
        this.name = name;
        this.numPhotos = 0;
        this.photos = new ArrayList<>();
    }

    /**
     * Constructor for Album.
     * @param newAlbumName the name of the album
     * @param photoSearchResultsArray the photos to add to the album
     * @author Shayan Rahmatullah
     */
    public Album(String newAlbumName, ArrayList<Photo> photoSearchResultsArray) {
        this.name = newAlbumName;
        this.numPhotos = photoSearchResultsArray.size();
        this.photos = photoSearchResultsArray;
        updateDateRange();
    }

    /**
     * Checks if the photo already exists in the album.
     * @param newPhoto the photo to check
     * @return true if the photo already exists in the album, false otherwise
     */
    public boolean doesPhotoExist(Photo newPhoto) {
        for (Photo photo : this.photos) {
            if (photo.getFilePath().equals(newPhoto.getFilePath())) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("The photo already exists in the album.");
                alert.showAndWait();
                return true;
            }
        }
        return false;
    }

    public String getName() {
        return name;
    }

    /**
     * Sets the name of the album.
     * @param newName the name to set
     */
    public void setName(String newName) {
        name = newName;
    }

    public int getNumPhotos() {
        return numPhotos;
    }

    /**
     * Adds a photo to the album.
     * @param photo the photo to add
     */
    public void addPhoto(Photo photo) {
        if (!doesPhotoExist(photo)) {
            photos.add(photo);
            numPhotos = photos.size();
            updateDateRange();
        }
    }

    /**
     * Removes a photo from the album.
     * @param photo the photo to remove
     */
    public void removePhoto(Photo photo) {
        photos.remove(photo);
        numPhotos = photos.size();
        updateDateRange();
    }

    public List<Photo> getPhotos() {
        return photos;
    }



    public Date getEarliestPhoto() {
        return earliestPhoto;
    }

    /**
     * Sets the earliest photo in the album.
     * @param date the date to set
     * @return the date set
     */
    public Date setEarliestPhoto(Date date) {
        earliestPhoto = date;
        return earliestPhoto;
    }

    /**
     * Sets the latest photo in the album.
     * @param date the date to set
     * @return the date set
     */
    public Date setLatestPhoto(Date date) {
        latestPhoto = date;
        return latestPhoto;
    }

    public Date getLatestPhoto() {
        return latestPhoto;
    }


    // consider using first and last photo instead if they are sorted by date
    private void updateDateRange() {
        if (photos.isEmpty()) {
            earliestPhoto = null;
            latestPhoto = null;
        } else {
            earliestPhoto = photos.get(0).getDate();
            latestPhoto = photos.get(0).getDate();
            for (Photo p : photos) {
                if (p.getDate().compareTo(earliestPhoto) < 0) {
                    earliestPhoto = p.getDate();
                }
                if (p.getDate().compareTo(latestPhoto) > 0) {
                    latestPhoto = p.getDate();
                }
            }
        }
    }
}