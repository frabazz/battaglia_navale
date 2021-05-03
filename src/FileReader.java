import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class FileReader {
    String fileName;
    boolean fileError=false;
    public int[] getBoatsLength() {
        return boatsLength;
    }
    public boolean getFileError(){
        return  fileError;
    }
    int[] boatsLength;
    FileReader(String fileName){
        this.fileName = fileName;
        try {
            Path pathtoFile = Paths.get(fileName);
            File myFile = new File(pathtoFile.toAbsolutePath().toString());
            System.out.println(myFile.exists());
            Scanner s = new Scanner(myFile);
            int length = s.nextInt();
            boatsLength = new int[length];
            for(int i = 0;i < length;i++)boatsLength[i] = s.nextInt();
        } catch (FileNotFoundException e) {
            fileError = true;
        }
    }

}
