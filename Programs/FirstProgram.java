import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FirstProgram {

    public static void main (String [] filename) throws IOException {
      
      	FileReader fileReader = new FileReader(filename[0]);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        List<String> lines = new ArrayList<String>();
        String line = null;
        while ((line = bufferedReader.readLine()) != null) {
            lines.add(line);
        }
        bufferedReader.close();
        
        System.out.println(lines.size() + " : lines");
    }
}
