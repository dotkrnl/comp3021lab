package base;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;

public class TextNote extends Note {

    private static final long serialVersionUID = 1L;

    private String content;

    /**
     * Construct a note with empty text content.
     * @param title The title of the note.
     */
    public TextNote(String title) {
        super(title);
    }

    /**
     * Construct a note with text content provided.
     * @param title The title of the note.
     * @param content The content of the note.
     */
    public TextNote(String title, String content) {
        this(title);
        this.content = content;
    }

    /**
     * Load a TextNote from File f.
     * The tile of the TextNote is the name of the file.
     * The content of the TextNote is the content of the file.
     *
     * @param f The file to read from.
     * @throws IOException
     */
    public TextNote(File f) throws IOException {
        super(f.getName());
        // Stupid code required by lab5.pdf
        this.content = getTextFromFile(f.getAbsolutePath());
    }

    /**
     * @return The content of note.
     */
    public String getContent() {
        return this.content;
    }
    
    /**
     * Get the content of a file.
     *
     * @param absolutePath of the file.
     * @return The content of the file.
     * @throws IOException
     */
    private String getTextFromFile(String absolutePath) throws IOException {
        File inputFile = new File(absolutePath);
        Scanner scanner = new Scanner(inputFile);
        String ret = scanner.useDelimiter("\\Z").next();
        scanner.close();
        return ret;
    }

    /**
     * Export text note to file.
     * The file has to be named as the title of the note with extension ".txt".
     * If the tile contains white spaces " " they has to be replaced with underscores "_".
     *
     * @param pathFolder Path of the folder where to export the note.
     * @throws IOException
     */
    public void exportTextToFile(String pathFolder) throws IOException {
        // Fix that on UNIX it will be cause writing to '/' if path is empty.
        String folderNameWithSeparator =
                pathFolder.equals("") ? "" : pathFolder + File.separator;

        String fileName = getTitle().replaceAll(" ", "_") + ".txt";

        File file = new File(folderNameWithSeparator + fileName);
        if (!file.exists()) {
            file.createNewFile();
        }

        try (FileWriter out = new FileWriter(file)) {
            out.write(content);
        }
    }

    @Override
    boolean matchKeyword(String keyword) {
        return getTitle().toLowerCase().contains(keyword.toLowerCase()) ||
                content.toLowerCase().contains(keyword.toLowerCase());
    }
    
    /**
     * Function required by lab6.pdf, bug fixed.
     * @return The most occurred letter.
     */
    public Character countLetters(){
        HashMap<Character,Integer> count = new HashMap<Character,Integer>();
        String a = this.getTitle() + this.getContent();
        int b = 0;
        Character r = ' ';
        for (int i = 0; i < a.length(); i++) {
            Character c = a.charAt(i);
            if (c <= 'Z' && c >= 'A' || c <= 'z' && c >= 'a') {
                if (!count.containsKey(c)) {
                    count.put(c, 1);
                } else {
                    count.put(c, count.get(c) + 1);
                }
                if (count.get(c) > b) {
                    b = count.get(c);
                    r = c;
                }
            }
        }
        return r;
    }
}
