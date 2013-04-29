import java.io.File;
import java.io.FilenameFilter;

public class FilenameFilterTXT implements FilenameFilter {

    @Override
    public boolean accept(File dir, String name) {
        String[] parts = name.split("\\.");
        boolean isNum;
        try {
            Integer.parseInt(parts[0]);
            isNum = true;
        } catch (Exception e) {
            isNum = false;
        }
        
        return isNum && name.toLowerCase().endsWith(".txt");
    }

}
