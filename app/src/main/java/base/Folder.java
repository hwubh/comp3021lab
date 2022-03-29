package base;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Folder implements Comparable<Folder>{
    private ArrayList<Note> notes;
	private String name;

	public Folder(String name) {
	
		notes = new ArrayList<Note>();
        this.name = name;
	}

	public void addNote(Note note){
		notes.add(note);
	}

	public String getName(){
		return this.name;
	}

	public ArrayList<Note> getNotes() {
		return notes;
    }

	@Override
	public String toString() {
		int nText = 0;
		int nImage = 0;

        for(int i = 0; i < this.notes.size(); i++)
        {
            if(this.notes.get(i) instanceof TextNote)
            {
                nText++;
            }
            else if(this.notes.get(i) instanceof ImageNote)
            {
                nImage++;
            }
        } 
		return name + ":" + nText + ":" + nImage;
	}

    public boolean equals(Folder O) {
        if(this.name.equals(O.getName())){
            return true;
        }
        else{return false;}
    }

    @Override
    public int compareTo(Folder o)
    {
        return this.name.compareTo(o.getName());
    }

    public void sortNotes()
    {
        Collections.sort(notes); 
    }

    public List<Note> searchNote(String keywords){
        List<Note> notesFound = new ArrayList<>();
        String[] keyword = keywords.trim().split("\\s+");
        for(Note note: notes){
            boolean check = true;
            for(int i = 0; i < keyword.length; i++){
                if(note.getTitle().toLowerCase().contains(keyword[i].toLowerCase()))
                {
                    if((i + 1 < keyword.length) && (keyword[i+1].compareTo("or") == 0 || keyword[i+1].compareTo("OR") == 0)){
                        i += 2;
                    }
                    continue;
                }
                else if (note instanceof TextNote){
                    TextNote n = (TextNote) note;
                    String test = n.getContent();
                    if(n.getContent().toLowerCase().contains(keyword[i].toLowerCase())){
                        if((i + 1 < keyword.length) && (keyword[i+1].compareTo("or") == 0 || keyword[i+1].compareTo("OR") == 0)){
                            i += 2;
                        }
                        continue;
                    }
                }
                if((i + 1 < keyword.length) && (keyword[i+1].compareTo("or") == 0 || keyword[i+1].compareTo("OR") == 0)){
                    i++;
                    continue;
                }
                check = false;
                break;
            }
            if(check){
                notesFound.add(note);
            }
        }
        return notesFound;
    }

}