package base;

import java.util.ArrayList;

public class NoteBook {
	private ArrayList<Folder> folders = new ArrayList<Folder>();

	public NoteBook() {

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
		return this.folders;
	}

	public boolean insertNote(String folderName, Note note) {

		Folder f = null;
		if (!folders.isEmpty()) {
			for (Folder f1 : folders) {
				if (f1.getName().equals(folderName)) {
					f = f1;
				}
			}
		}

		if (f == null) {
			f = new Folder(folderName);
			folders.add(f);
		}

		for (Note n : f.getNotes()) {
			if (n.equals(note)) {
				System.out.println("Creating note" + note.getTitle() + "under folder" + f.getName() + "failed");
				return false;
			}
		}
		f.addNote(note);
		return true;

	}

}
