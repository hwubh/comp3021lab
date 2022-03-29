package base;

import java.util.ArrayList;
import java.util.List;

public class NoteBook {
	private ArrayList<Folder> folders = new ArrayList<Folder>();

	public NoteBook() {

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

	public void sortFolders()
	{
		for(Folder f :folders)
		{
			f.sortNotes();
		}

		for(int i = 0; i < folders.size() - 1; i++){
			for(int j = 0; j < folders.size()-1-i; j++){
				if(folders.get(j).compareTo(folders.get(j + 1)) > 0)
				{
					Folder temp = folders.get(j + 1);
					folders.set(j + 1, folders.get(j));
					folders.set(j, temp);
				}
			}
		}
	}

	public List<Note> searchNotes(String keywords){
		List<Note> notesFound = new ArrayList<>();
		for(Folder f: folders){
			notesFound.addAll(f.searchNote(keywords));
		}
		return notesFound;
	}

}
