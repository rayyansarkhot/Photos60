package photosFx.model;

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



    public Date getEarliestPhoto() {
        return earliestPhoto;
    }

    public Date setEarliestPhoto(Date date) {
        earliestPhoto = date;
        return earliestPhoto;
    }

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