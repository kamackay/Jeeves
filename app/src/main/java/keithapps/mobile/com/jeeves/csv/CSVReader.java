package keithapps.mobile.com.jeeves.csv;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * Created by Keith on 2/16/2016.
 * Comma Separated Value File Reader
 */
public class CSVReader extends FileInputStream {
    public CSVReader(File file) throws FileNotFoundException {
        super(file);
    }

    public CSVReader(FileDescriptor fd) {
        super(fd);
    }

    public CSVReader(String path) throws FileNotFoundException {
        super(path);
    }

    public String readLine(){
        StringBuilder sb = new StringBuilder();
        try {
            int i;
            while ((i = read()) != -1 && i != '\n') sb.append((char)i);
        } catch (Exception e){
            //Something happened
        }
        return sb.toString();
    }
}
