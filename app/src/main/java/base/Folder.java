package base;

import java.util.ArrayList;

public class Folder {
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

}
