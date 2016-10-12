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

    /**
     * Construct an empty NoteBook.
     */
    public NoteBook() {
        folders = new ArrayList<>();
    }

    /**
     * Constructor of an object NoteBook from an object serialization on disk.
     *
     * @param file the path of the file for loading the object serialization.
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public NoteBook(String file) throws IOException, ClassNotFoundException {
        FileInputStream fis = new FileInputStream(file);
        ObjectInputStream in = new ObjectInputStream(fis);
        NoteBook n = (NoteBook) in.readObject();
        this.folders = n.getFolders();
        in.close();
    }

    /**
     * Create a text note in the given folder of the NoteBook.
     * @param folderName The name of the folder to store the text note.
     * @param title The title of the note.
     * @return The created note.
     */
    public boolean createTextNote(String folderName, String title) {
        TextNote note = new TextNote(title);
        return insertNote(folderName, note);
    }

    /**
     * Create a text note in the given folder of the NoteBook, with given content provided.
     * @param folderName The name of the folder to store the text note.
     * @param title The title of the note.
     * @param content The content of the note.
     * @return The created note.
     */
    public boolean createTextNote(String folderName, String title, String content) {
        TextNote note = new TextNote(title, content);
        return insertNote(folderName, note);
    }

    /**
     * Create an image note in the given folder of the NoteBook.
     * @param folderName The name of the folder to store the image note.
     * @param title The title of the note.
     * @return The created note.
     */
    public boolean createImageNote(String folderName, String title) {
        ImageNote note = new ImageNote(title);
        return insertNote(folderName, note);
    }

    /**
     * @return An ArrayList of all folders.
     */
    public ArrayList<Folder> getFolders() {
        return folders;
    }

    /**
     * Insert an note into the given folder of the NoteBook.
     * @param folderName The name of the folder to insert the note.
     * @param toAddNote The note to insert.
     * @return True if inserted successfully.
     */
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

    /**
     * Sort the folders and the notes in all folders.
     */
    public void sortFolders() {
        for (Folder folder : folders) {
            folder.sortNotes();
        }

        Collections.sort(folders);
    }

    /**
     * Search and return the notes matching the given keywords.
     *
     * @param keywordsWithOr Keyword string specified in lab3.pdf.
     * @return Notes matching the keywords.
     */
    public List<Note> searchNotes(String keywordsWithOr) {
        ArrayList<Note> matchedNotes = new ArrayList<>();

        for (Folder folder : folders) {
            matchedNotes.addAll(folder.searchNotes(keywordsWithOr));
        }

        return matchedNotes;
    }

    /**
     * Method to save the NoteBook instance to file.
     *
     * @param file The path of the file where to save the object serialization.
     * @return True if save on file is successful, false otherwise.
     */
    public boolean save(String file) {
        // Should throw exception but specified by lab5.pdf
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
