package photosFx.model;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

import java.nio.file.attribute.FileTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;

public class Photo implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Date date;
    private File picture;
    private String caption;
    private String filePath;
    private String name;
    private List<Tag> tags;


    public Photo(File pic) throws IOException {
        if (pic != null) {
            this.filePath = pic.getAbsolutePath();
            this.name = pic.getName();
            this.picture = pic;
            this.caption = "";
            this.tags = new ArrayList<>();

            FileTime fileTime = Files.getLastModifiedTime(Paths.get(filePath));
            this.date = new Date(fileTime.toMillis());

        }
    }

    public String getFilePath() {
        return filePath;
    }

    public String getName() {
        return this.name;
    }

    public Date getDate() {
        return this.date;
    }

    public String getCaption() {
        return this.caption;
    }

    public File getFile() {
        return this.picture;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public void addTag(Tag tag) {
        tags.add(tag);
    }

    public void removeTag(Tag tag) {
        tags.remove(tag);
    }

    public List<Tag> getTags() {
        return tags;
    }

    public boolean hasTag(Tag tag) {
        for (Tag x : this.tags) {
            if (x.getTagName().toLowerCase().equals(tag.getTagName().toLowerCase())
                    && x.getTagValue().toLowerCase().equals(tag.getTagValue().toLowerCase())) {
                return true;
            }
        }
        return false;
    }
}

