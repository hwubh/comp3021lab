package base;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.BufferedWriter;
import java.io.FileWriter;

public class TextNote extends Note {
    private static final long serialVersionUID = 9020159955693642552L;
    String content;

    public TextNote(String title) {
        super(title);
    }

    /**
     * load a TextNote from File f
     * 
     * the tile of the TextNote is the name of the file
     * the content of the TextNote is the content of the file
     * 
     * @param File f
     */
    public TextNote(File f) {
        super(f.getName());
        this.content = getTextFromFile(f.getAbsolutePath());
    }

    public TextNote(String title, String content) {
        super(title);
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    /**
     * get the content of a file
     * 
     * @param absolutePath of the file
     * @return the content of the file
     */
    private String getTextFromFile(String absolutePath) {
        String result = "";

        try {
            result = new String(Files.readAllBytes(Paths.get(absolutePath)));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
 * export text note to file
 * 
 * 
 * @param pathFolder path of the folder where to export the note
 * the file has to be named as the title of the note with extension ".txt"
 * 
 * if the tile contains white spaces " " they has to be replaced with underscores "_"
     * @throws IOException
 * 
 * 
 */
	public void exportTextToFile(String pathFolder) throws IOException {       
        File file = new File( pathFolder + File.separator + "Users\\admin\\Desktop\\comp3021\\lab\\comp3021lab" + File.separator + this.getTitle().replaceAll(" ", "_")  + ".txt");
        //System.out.println(new File(this.getTitle().replaceAll(" ", "_")+".txt").getAbsolutePath());
        BufferedWriter writer = null;
        try{
            FileWriter fileWriter = new FileWriter(file);
            writer = new BufferedWriter(fileWriter);
            writer.write(this.content);
        }
        catch(IOException e){
            e.printStackTrace();
        }
        finally{
            writer.flush();
            writer.close();
        }

}

}
