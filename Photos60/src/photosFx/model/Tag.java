
package photosFx.model;

import java.io.Serializable;
import java.util.*;


public class Tag implements Serializable {

    private String tagName;

    private String tagValue;

    /**
     * Constructor for Tag.
     * @param tagName the name of the tag
     * @param tagValue the value of the tag
     */
    public Tag(String tagName, String tagValue) {
        this.tagName = tagName;
        this.tagValue = tagValue;
    }

    public String getTagName() {
        return this.tagName;
    }

    public String getTagValue() {
        return this.tagValue;
    }

    /**
     * Returns the tag in the format "tagName: tagValue".
     * @return the tag in the format "tagName: tagValue"
     */
    public boolean equals(Object t) {
        if (t instanceof Tag) {
            Tag tag = (Tag) t;
            return this.tagName.equals(tag.getTagName()) && this.tagValue.equals(tag.getTagValue());
        } else {
            return false;
        }
    }

    public String toString() {
        return this.tagName + ": " + this.tagValue;
    }
}