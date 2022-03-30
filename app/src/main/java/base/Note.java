package base;
import java.util.Date;
import java.util.Objects;
import java.io.Serializable;
import java.lang.String;

public class Note implements Comparable<Note>, Serializable{
    private Date date;
	private String title;
	
	public Note(String title){
		this.title = title;
		this.date = new Date();
    }
    
    String getTitle(){
        return this.title;
    }

    Date getDate(){
        return this.date;
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

    @Override
    public int compareTo(Note o)
    {
        // >0 when more recently
        return this.date.compareTo(o.getDate());
    }

    @Override
    public String toString() {
        return date.toString() + "\t" + title;
    }

}
