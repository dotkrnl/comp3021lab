package base;

import java.util.ArrayList;

public class NoteBook {

	private ArrayList<Folder> folders;
	
	public NoteBook() {
		folders = new ArrayList<Folder>();
	}
	
	public boolean createTextNote(String folderName, String title) {
		TextNote note = new TextNote(title);
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
}
