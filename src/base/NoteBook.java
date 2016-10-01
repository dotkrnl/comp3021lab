package base;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NoteBook implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    private ArrayList<Folder> folders;

    public NoteBook() {
        folders = new ArrayList<Folder>();
    }

    /**
     * Constructor of an object NoteBook from an object serialization on disk
     *
     * @param file, the path of the file for loading the object serialization
     */
    public NoteBook(String file) throws IOException, ClassNotFoundException {
        FileInputStream fis = new FileInputStream(file);
        ObjectInputStream in = new ObjectInputStream(fis);
        NoteBook n = (NoteBook) in.readObject();
        this.folders = n.getFolders();
    }

    public boolean createTextNote(String folderName, String title) {
        TextNote note = new TextNote(title);
        return insertNote(folderName, note);
    }

    public boolean createTextNote(String folderName, String title, String content) {
        TextNote note = new TextNote(title, content);
        return insertNote(folderName, note);
    }

    public boolean createImageNote(String folderName, String title) {
        ImageNote note = new ImageNote(title);
        return insertNote(folderName, note);
    }

    public ArrayList<Folder> getFolders() {
        return folders;
    }

    private boolean insertNote(String folderName, Note toAddNote) {
        Folder destFolder = null;
        for (Folder folder : folders) {
            if (folder.getName().equals(folderName)) {
                destFolder = folder;
                break;
            }
        }

        if (destFolder == null) {
            destFolder = new Folder(folderName);
            folders.add(destFolder);
        }

        for (Note note : destFolder.getNotes()) {
            if (note.equals(toAddNote)) {
                System.out.println("Creating note " + note.getTitle() +
                        " under folder " + folderName + " failed");
                // Should be "Inserting" but specified in lab2.pdf
                return false;
            }
        }

        destFolder.addNote(toAddNote);

        return true;
    }

    public void sortFolders() {
        for (Folder folder : folders) {
            folder.sortNotes();
        }

        Collections.sort(folders);
    }

    public List<Note> searchNotes(String keywordsWithOr) {
        ArrayList<Note> matchedNotes = new ArrayList<Note>();

        for (Folder folder : folders) {
            matchedNotes.addAll(folder.searchNotes(keywordsWithOr));
        }

        return matchedNotes;
    }

    /**
     * method to save the NoteBook instance to file
     *
     * @param file, the path of the file where to save the object serialization
     * @return true if save on file is successful, false otherwise
     */
    public boolean save(String file) {
        try (
                FileOutputStream fos = new FileOutputStream(file);
                ObjectOutputStream out = new ObjectOutputStream(fos)
        ) {
            out.writeObject(this);
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}
