package base;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class NoteBook implements Serializable {
	private ArrayList<Folder> folders = new ArrayList<Folder>();

	public NoteBook() {

	}

	/**
	 * 
	 * Constructor of an object NoteBook from an object serialization on disk
	 * 
	 * @param file, the path of the file for loading the object serialization
	 */
	public NoteBook(String file) {

		FileInputStream fis = null;
		ObjectInputStream in = null;
		try {
			fis = new FileInputStream(file);
			in = new ObjectInputStream(fis);
			NoteBook n = (NoteBook) in.readObject();
			this.folders = n.getFolders();
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
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

	public void sortFolders() {
		for (Folder f : folders) {
			f.sortNotes();
		}

		for (int i = 0; i < folders.size() - 1; i++) {
			for (int j = 0; j < folders.size() - 1 - i; j++) {
				if (folders.get(j).compareTo(folders.get(j + 1)) > 0) {
					Folder temp = folders.get(j + 1);
					folders.set(j + 1, folders.get(j));
					folders.set(j, temp);
				}
			}
		}
	}

	public List<Note> searchNotes(String keywords) {
		List<Note> notesFound = new ArrayList<>();
		for (Folder f : folders) {
			notesFound.addAll(f.searchNote(keywords));
		}
		return notesFound;
	}

	/**
	 * method to save the NoteBook instance to file
	 * 
	 * @param file, the path of the file where to save the object serialization
	 * @return true if save on file is successful, false otherwise
	 */
	public boolean save(String file) {
		FileOutputStream fos = null;
		ObjectOutputStream out = null;
		try {

			fos = new FileOutputStream(file);
			out = new ObjectOutputStream(fos);
			ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(file));
			output.writeObject(this);
			output.close();
		} catch (Exception e) {
			e.getStackTrace();
			return false;
		}
		return true;
	}

}
