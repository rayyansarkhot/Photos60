package photosFx.model;

import java.io.*;

public class AlbumSerializer {
    static final long serialVersionUID = 1L;

    public static void saveAlbum(Album album, String filename) throws IOException {
        try (ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(filename))) {
            os.writeObject(album);
        }
    }

    public static Album loadAlbum(String filename) throws IOException, ClassNotFoundException {
        try (ObjectInputStream os = new ObjectInputStream(new FileInputStream(filename))) {
            return (Album) os.readObject();
        }
    }
}
