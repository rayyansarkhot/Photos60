package photosFx.model;

import java.io.*;

/**
 * Serializes object for use after application closes.
 * @author Rayyan Sarkhot
 */

public class ContentSerializer {
    static final long serialVersionUID = 1L;

    public static void saveContent(Content content, String username) throws IOException {
        String filepath = "src/photosFx/model/users/" + username + ".dat";
        try (ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(filepath))) {
            os.writeObject(content);
        }
    }

    public static Content loadContent(String username) throws IOException, ClassNotFoundException {
        String filepath = "src/photosFx/model/users/" + username + ".dat";
        try (ObjectInputStream os = new ObjectInputStream(new FileInputStream(filepath))) {
            return (Content) os.readObject();
        }
    }
}
