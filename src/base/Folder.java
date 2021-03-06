package base;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Folder implements Comparable<Folder>, java.io.Serializable {

    private static final long serialVersionUID = 1L;

    private ArrayList<Note> notes;
    private String name;

    /**
     * Construct a note folder with given name.
     *
     * @param name The name of the folder.
     */
    public Folder(String name) {
        this.name = name;
        notes = new ArrayList<>();
    }

    /**
     * Append a note after the end of the folder.
     *
     * @param aNote The note to add.
     */
    public void addNote(Note aNote) {
        notes.add(aNote);
    }

    /**
     * @return The name of the folder
     */
    public String getName() {
        return name;
    }

    /**
     * @return The array list of the notes in the folder.
     */
    public ArrayList<Note> getNotes() {
        return notes;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        // Remove per lab2.pdf requirement
        //if (getClass() != obj.getClass())
        //	return false;
        Folder other = (Folder) obj;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        return true;
    }

    @Override
    public String toString() {
        int nText = 0;
        int nImage = 0;

        for (Note note : notes) {
            if (note instanceof TextNote) {
                nText += 1;
            } else if (note instanceof ImageNote) {
                nImage += 1;
            }
        }

        return name + ':' + nText + ':' + nImage;
    }

    @Override
    public int compareTo(Folder o) {
        // For class Folder, we compare its name.
        // Folder with smaller name is considered as smaller.
        return getName().compareTo(o.getName());
    }

    /**
     * Sort the notes in the folder.
     */
    public void sortNotes() {
        Collections.sort(notes);
    }

    /**
     * Search and return the notes in the folder matching the given keywords.
     *
     * @param keywordsWithOr Keyword string specified in lab3.pdf.
     * @return Notes matching the keywords.
     */
    public List<Note> searchNotes(String keywordsWithOr) {
        ArrayList<Note> matchedNotes = new ArrayList<>();

        // Make it case-insensitive by toLowerCase all strings
        keywordsWithOr = keywordsWithOr.toLowerCase();
        String[] keywordsOrStringList = keywordsWithOr.split("(?<!or) (?!or)");
        String[][] keywordsList = new String[keywordsOrStringList.length][];

        for (int i = 0; i < keywordsOrStringList.length; i++) {
            keywordsList[i] = keywordsOrStringList[i].split(" or ");
        }

        for (Note note : notes) {
            boolean andMatched = true;
            for (String[] keywords : keywordsList) {
                boolean orMatched = false;
                for (String keyword : keywords) {
                    if (note.matchKeyword(keyword)) {
                        orMatched = true;
                        break;
                    }
                }
                if (!orMatched) {
                    andMatched = false;
                }
            }
            if (andMatched) {
                matchedNotes.add(note);
            }
        }

        return matchedNotes;
    }
}
