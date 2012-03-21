import java.io.BufferedReader;
import java.io.FileReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FirstProgram {

    public static void main (String [] filename) throws IOException {
      
      //Creates a new FileReader,given the name of the file to read from.
      //filename is a parameters
      	FileReader fileReader = new FileReader(filename[0]);
       
    
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        ArrayList<String> lines = new ArrayList<String>();
        String line = null;
     // fileReader.mark();
        while ((line = bufferedReader.readLine()) != null) {
            lines.add(line);
        }
        bufferedReader.close();
        

        String s = lines.size() + " : lines";
        System.out.println(s);
        
        FileWriter filewriter = new FileWriter("fwrite.txt");
        BufferedWriter out = new BufferedWriter(filewriter);
   	    out.write(s);
        out.close();
    }
}
