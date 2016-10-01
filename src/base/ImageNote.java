package base;

import java.io.File;

public class ImageNote extends Note {

    File image;

    /**
     * Construct a note with an image.
     * @param title The title of the note.
     */
    public ImageNote(String title) {
        super(title);
    }

    @Override
    boolean matchKeyword(String keyword) {
        return getTitle().toLowerCase().contains(keyword.toLowerCase());
    }
}
