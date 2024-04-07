package photosFx.model;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Album implements Serializable {
    public String name;
    public int numPhotos;
    // public List<Photo> photos;
    public List<Photo> photos;
    static final long serialVersionUID = 1L;

    public Album(String name) {
        this.name = name;
        this.numPhotos = 0;
        this.photos = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String newName) {
        name = newName;
    }

    public int getNumPhotos() {
        return numPhotos;
    }

    public void addPhoto(Photo photo) {
        photos.add(photo);
        numPhotos = photos.size();
        updateDateRange();
    }

    public void removePhoto(Photo photo) {
        photos.remove(photo);
        numPhotos = photos.size();
        updateDateRange();
    }

    public List<Photo> getPhotos() {
        return photos;
    }


}