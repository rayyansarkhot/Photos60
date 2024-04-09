package photosFx.model;

import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;

public class Content implements Serializable {

    private static final long serialVersionUID = 1L;

    public List<Album> albums;

    /**
     * Sample test constructor for Content.
     */
    public Content() {
        albums = new ArrayList<Album>(); // Initialize the list
        albums.add(new Album("RAYYAN"));
        albums.add(new Album("SHAYAN"));

    }

    /**
     * Adds an album to the list of albums.
     * @param album the album to add
     */
    public List<String> getAlbumNames() {

        List<String> albumNames = new ArrayList<>();
        for (Album alb : albums) {
            albumNames.add(alb.name);
        }

        return albumNames;
    }

}
