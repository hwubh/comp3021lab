package base;
import java.util.Date;
import java.util.Objects;
import java.lang.String;

public class Note {
    private Date date;
	private String title;
	
	public Note(String title){
		this.title = title;
		this.date = new Date();
    }
    
    String getTitle(){
        return this.title;
    }



    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Note)) {
            return false;
        }
        Note note = (Note) o;
        return Objects.equals(title, note.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(date, title);
    }


}
