package base;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class TextNote extends Note {

    String content;

    public TextNote(String title) {
        super(title);
    }

    public TextNote(String title, String content) {
        this(title);
        this.content = content;
    }

    /**
     * Load a TextNote from File f
     * <p>
     * The tile of the TextNote is the name of the file
     * The content of the TextNote is the content of the file
     *
     * @param File f
     */
    public TextNote(File f) throws IOException {
        super(f.getName());
        // Stupid code required by lab5.pdf
        this.content = getTextFromFile(f.getAbsolutePath());
    }

    /**
     * Get the content of a file
     *
     * @param absolutePath of the file
     * @return the content of the file
     */
    private String getTextFromFile(String absolutePath) throws IOException {
        String result = "";
        File inputFile = new File(absolutePath);
        Scanner scanner = new Scanner(inputFile);
        result = scanner.useDelimiter("\\Z").next();
        return result;
    }

    /**
     * Export text note to file
     *
     * @param pathFolder path of the folder where to export the note
     *                   the file has to be named as the title of the note with extension ".txt"
     *                   <p>
     *                   if the tile contains white spaces " " they has to be replaced with underscores "_" *
     */
    public void exportTextToFile(String pathFolder) throws IOException {
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
}
