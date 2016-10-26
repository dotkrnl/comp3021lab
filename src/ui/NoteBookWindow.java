package ui;

import base.Folder;
import base.Note;
import base.NoteBook;
import base.TextNote;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * NoteBook GUI with JAVAFX
 * COMP 3021
 * 
 * @author valerio
 */
public class NoteBookWindow extends Application {

    /**
     * TextArea containing the note
     */
    final TextArea textAreaNote = new TextArea("");

    /**
     * list view showing the titles of the current folder
     */
    final ListView<String> titleslistView = new ListView<String>();

    /**
     * Combo box for selecting the folder
     */
    final ComboBox<String> foldersComboBox = new ComboBox<String>();

    final Button newNoteButton = new Button("Add a Note");

    /**
     * Stage for the Application
     */
    Stage stage;

    /**
     * Main Notebook object
     */
    NoteBook noteBook = null;

    /**
     * Stupid code required by lab7.pdf
     * Name of current folder selected by the user
     */
    String currentFolder = "";
    /**
     * To be consistent.
     * Name of current note selected by the user
     */
    String currentNote = "";
    /**
     * Current search string
     */
    String currentSearch = "";


    public static void main(String[] args) {
        launch(NoteBookWindow.class, args);
    }

    @Override
    public void start(Stage stage) {
        loadNoteBook();
        
        this.stage = stage;
        
        // Use a border pane as the root for scene
        BorderPane border = new BorderPane();
        // add top, left and center
        border.setTop(addHBox());
        border.setLeft(addVBox());
        border.setCenter(addGridPane());

        Scene scene = new Scene(border);
        stage.setScene(scene);
        stage.setTitle("NoteBook COMP 3021");
        stage.show();
    }

    /**
     * Create the top section.
     * @return HBox item of the top section.
     */
    private HBox addHBox() {
        HBox hbox = new HBox();
        hbox.setPadding(new Insets(15, 12, 15, 12));
        hbox.setSpacing(10); // Gap between nodes

        Button buttonLoad = new Button("Load");
        buttonLoad.setPrefSize(100, 20);
        buttonLoad.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Choose File to Load Notebook");
                
                FileChooser.ExtensionFilter extFilter =
                        new FileChooser.ExtensionFilter(
                                "Serialized Object File (*.ser)", "*.ser");
                fileChooser.getExtensionFilters().add(extFilter);
                
                File file = fileChooser.showOpenDialog(stage);
                if (file != null) {
                    loadNoteBook(file);
                    textAreaNote.setText("");
                }
            }
        });
        
        Button buttonSave = new Button("Save");
        buttonSave.setPrefSize(100, 20);
        buttonSave.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Choose File to Save Notebook");

                FileChooser.ExtensionFilter extFilter =
                        new FileChooser.ExtensionFilter(
                                "Serialized Object File (*.ser)", "*.ser");
                fileChooser.getExtensionFilters().add(extFilter);
                
                File file = fileChooser.showSaveDialog(stage);
                if (file != null) {
                    saveNoteBook(file);
                }
            }
        });
        
        Label searchLabel = new Label("Search:");

        TextField searchField = new TextField();
        
        Button buttonSearch = new Button("Search");
        buttonSearch.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                currentSearch = searchField.getText();
                textAreaNote.setText("");
                updateListView();
            }
        });
        
        Button buttonClearSearch = new Button("Clear Search");
        buttonClearSearch.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                currentSearch = "";
                searchField.setText("");
                textAreaNote.setText("");
                updateListView();
            }
        });

        hbox.getChildren().addAll(buttonLoad, buttonSave,
                searchLabel, searchField, buttonSearch, buttonClearSearch);

        return hbox;
    }

    /**
     * Create the section on the left.
     * @return VBox object of the left section.
     */
    private VBox addVBox() {
        VBox vbox = new VBox();
        vbox.setPadding(new Insets(10)); // Set all sides to 10
        vbox.setSpacing(8); // Gap between nodes
        
        updateFolderView();

        HBox foldersHBox = new HBox();
        foldersHBox.setSpacing(8);
        
        foldersComboBox.getSelectionModel()
                .selectedItemProperty()
                .addListener(new ChangeListener<Object>() {
                    @Override
                    public void changed(ObservableValue<?extends Object> ov,
                            Object t, Object t1) {
                        if (t1 != null) {
                            // this contains the name of the folder selected
                            currentFolder = t1.toString();
                            // update list view
                            updateListView();
                        }
                    }
                });
        foldersComboBox.setValue("-----");
        
        Button newFolderBotton = new Button("Add a Folder");
        newFolderBotton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                TextInputDialog dialog = new TextInputDialog("");
                dialog.setTitle("Add a Folder");
                dialog.setHeaderText("Add a new folder for your notebook:");
                dialog.setContentText(
                        "Please enter the name you want to create:");
                
                // Traditional way to get the response value.
                Optional<String> result = dialog.showAndWait();
                if (result.isPresent() && !result.get().equals("")) {
                    if (noteBook.addFolder(result.get())) {
                        // Update view
                        updateFolderView();
                        currentFolder = result.get();
                        foldersComboBox.setValue(result.get());
                    } else {
                        // Already exist.
                        Alert alert = new Alert(AlertType.WARNING);
                        alert.setTitle("Warning");
                        alert.setContentText("You already have a folder"
                                + " with name " + result.get());
                        alert.show();
                    }
                } else {
                    // Invalid input.
                    Alert alert = new Alert(AlertType.WARNING);
                    alert.setTitle("Warning");
                    alert.setContentText("Please input a valid folder name.");
                    alert.show();
                }
            }
        });
        
        foldersHBox.getChildren().add(foldersComboBox);
        foldersHBox.getChildren().add(newFolderBotton);

        titleslistView.setPrefHeight(100);

        titleslistView.getSelectionModel().selectedItemProperty()
                .addListener(new ChangeListener<Object>() {
                    @Override
                    public void changed(ObservableValue<?extends Object> ov,
                            Object t, Object t1) {
                        if (t1 == null) return;
                        
                        // This is the selected title
                        currentNote = t1.toString();

                        // Load the content of the selected note in textArea
                        Note current = getCurrentNote();
                        if (current instanceof TextNote) {
                            // Only text note supported now.
                            textAreaNote.setText(((TextNote)current).getContent());
                        }
                    }
                });

        newNoteButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                TextInputDialog dialog = new TextInputDialog("");
                dialog.setTitle("Add a Note");
                dialog.setHeaderText("Add a new note to current folder:");
                dialog.setContentText(
                        "Please enter the name of your note:");
                
                // Traditional way to get the response value.
                Optional<String> result = dialog.showAndWait();
                if (result.isPresent() && !result.get().equals("")) {
                    if (noteBook.createTextNote(currentFolder, result.get())) {
                        Alert alert = new Alert(AlertType.INFORMATION);
                        alert.setTitle("Successful");
                        alert.setContentText("Insert note " + result.get() +
                                " to folder " + currentFolder
                                + " successfully!");
                        alert.show();
                        updateListView();
                    } else {
                        // Already exist.
                        Alert alert = new Alert(AlertType.WARNING);
                        alert.setTitle("Warning");
                        alert.setContentText("You already have a note"
                                + " with name " + result.get());
                        alert.show();
                    }
                } else {
                    // Invalid input.
                    Alert alert = new Alert(AlertType.WARNING);
                    alert.setTitle("Warning");
                    alert.setContentText("Please input a valid folder name.");
                    alert.show();
                }
            }
        });
        
        vbox.getChildren().add(new Label("Choose folder:"));
        vbox.getChildren().add(foldersHBox);
        vbox.getChildren().add(new Label("Choose note title:"));
        vbox.getChildren().add(titleslistView);
        vbox.getChildren().add(newNoteButton);

        return vbox;
    }

    /**
     * Update the list of folders.
     */
    private void updateFolderView() {
        foldersComboBox.getItems().clear();
        for (Folder f : noteBook.getFolders()) {
            foldersComboBox.getItems().add(f.getName());
        }
        updateListView();
    }
    
    /**
     * Update the list of notes based on selected folder and search string.
     */
    private void updateListView() {
        titleslistView.getItems().clear();

        Folder current = getCurrentFolder();
        List<Note> noteList = null;

        // If folder selected.
        if (current != null) {
            if (currentSearch.equals("")) {
                noteList = current.getNotes();
            } else {
                noteList = current.searchNotes(currentSearch);
            }
            newNoteButton.setDisable(false);
        } else {
            newNoteButton.setDisable(true);
        }

        ArrayList<String> list = new ArrayList<String>();
        // If notes available, add to list
        if (noteList != null) {
            // populate the list object with all the TextNote titles
            for (Note n : noteList) {
                list.add(n.getTitle());
            }
        }
        // Populate list of notes.
        ObservableList<String> notes = FXCollections
                .observableArrayList(list);
        titleslistView.setItems(notes);
        textAreaNote.setText("");
    }

    /**
     * Creates a grid for the center region with four columns and three rows.
     * @return The GridPane.
     */
    private GridPane addGridPane() {
        GridPane grid = new GridPane();
        
        HBox hbox = new HBox();
        hbox.setSpacing(8);
        
        try {
            ImageView saveView = new ImageView(
                    new Image(getClass().getResource("/resources/save.png")
                            .toURI().toString()));
            saveView.setFitHeight(18);
            saveView.setFitWidth(18);
            saveView.setPreserveRatio(true);
            hbox.getChildren().add(saveView);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        
        Button buttonSave = new Button("Save Note");
        buttonSave.setPrefSize(100, 20);
        buttonSave.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Note note = getCurrentNote();
                if (note != null && note instanceof TextNote) {
                    TextNote tnote = (TextNote) note;
                    tnote.setContent(textAreaNote.getText());
                } else {
                    // Invalid note.
                    Alert alert = new Alert(AlertType.WARNING);
                    alert.setTitle("Warning");
                    alert.setContentText("Please select a folder and a note.");
                    alert.show();
                }
            }
        });
        hbox.getChildren().add(buttonSave);

        try {
            ImageView deleteView = new ImageView(
                    new Image(getClass().getResource("/resources/delete.png")
                            .toURI().toString()));
            deleteView.setFitHeight(18);
            deleteView.setFitWidth(18);
            deleteView.setPreserveRatio(true);
            hbox.getChildren().add(deleteView);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        Button buttonDelete = new Button("Delete Note");
        buttonDelete.setPrefSize(100, 20);
        buttonDelete.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Folder folder = getCurrentFolder();
                Note note = getCurrentNote();
                if (note != null) {
                    // Implied folder exist.
                    folder.getNotes().remove(note);
                    updateListView();
                    Alert alert = new Alert(AlertType.CONFIRMATION);
                    alert.setTitle("Succeed!");
                    alert.setContentText(
                            "Your note has been successfully removed.");
                    alert.show();
                } else {
                    // Invalid note.
                    Alert alert = new Alert(AlertType.WARNING);
                    alert.setTitle("Warning");
                    alert.setContentText("Please select a folder and a note.");
                    alert.show();
                }
            }
        });
        hbox.getChildren().add(buttonDelete);
        
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(10, 10, 10, 10));
        textAreaNote.setEditable(true);
        textAreaNote.setMaxSize(450, 400);
        textAreaNote.setWrapText(true);
        textAreaNote.setPrefWidth(450);
        textAreaNote.setPrefHeight(400);
        // 0 0 is the position in the grid
        grid.add(hbox, 0, 0);
        grid.add(textAreaNote, 0, 1);

        return grid;
    }

    /**
     * Load the example note book to the instance.
     * It's hard-coding data.
     */
    private void loadNoteBook() {
        NoteBook nb = new NoteBook();
        
        noteBook = nb;
        updateFolderView();
    }

    /**
     * Load the note book file to the instance.
     * @param file A file consists of notebook object.
     */
    private void loadNoteBook(File file) {
        try {
            noteBook = new NoteBook(file.getAbsolutePath());
            updateFolderView();
        } catch (ClassNotFoundException e) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Failed to Load");
            alert.setContentText("Cannot find notebook object in the file.");
            alert.show();
        } catch (IOException e) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Failed to Load");
            alert.setContentText("Failed to read the file.");
            alert.show();
        }
    }

    /**
     * Save the note book to the file.
     * @param file A file to save the notebook object.
     */
    private void saveNoteBook(File file) {
        if (noteBook.save(file.getAbsolutePath())) {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Successfully saved");
            alert.setContentText("You file has been saved to file "
                    + file.getName());
            alert.showAndWait().ifPresent(rs -> {
                if (rs == ButtonType.OK) {
                    System.out.println("Pressed OK.");
                }
            });
        } else {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Failed to Save");
            alert.setContentText("Failed to save the file.");
            alert.show();
        }
    }
    

    /**
     * @return The Folder object of current selection. Null for not exist.
     */
    private Folder getCurrentFolder() {
        // look up the correct folder
        for (Folder f : noteBook.getFolders()) {
            if (f.getName().equals(currentFolder)) {
                return f;
            }
        }
        // or null
        return null;
    }
    
    /**
     * @return The Note object of current selection. Null for not exist.
     */
    private Note getCurrentNote() {
        Folder f = getCurrentFolder();
        if (f == null) return null;

        for (Note n : f.getNotes()) {
            if (n.getTitle().equals(currentNote)) {
                return n;
            }
        }

        return null;
    }

}
