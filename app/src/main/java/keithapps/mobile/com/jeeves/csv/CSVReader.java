package keithapps.mobile.com.jeeves.csv;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

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

    public String readLine() {
        StringBuilder sb = new StringBuilder();
        try {
            int i;
            while ((i = read()) != -1 && i != '\n') sb.append((char) i);
        } catch (Exception e) {
            //Something happened
        }
        if (sb.length() == 0) return null;
        return sb.toString();
    }

    public List<String[]> getAll() {
        List<String[]> lines = new ArrayList<>();
        String line;
        while ((line = readLine()) != null) lines.add(line.split(","));
        try {
            close();
        } catch (Exception e) {
            //IDK, man
        }
        return lines;
    }
}
