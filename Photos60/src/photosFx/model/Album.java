package photosFx.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Album implements Serializable {
    public String name;
    // public List<Photo> photos;

    public Album(String name) {
        this.name = name;
        // this.photos = new ArrayList<>();
    }

    // Add methods to manipulate photos within the album
    // public void addPhoto(Photo photo) {
    //     photos.add(photo);
    // }

    // public List<Photo> getPhotos() {
    //     return photos;
    // }


}
