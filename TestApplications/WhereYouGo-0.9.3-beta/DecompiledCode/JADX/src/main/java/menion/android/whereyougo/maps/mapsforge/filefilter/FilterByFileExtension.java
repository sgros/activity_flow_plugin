package menion.android.whereyougo.maps.mapsforge.filefilter;

import java.io.File;
import java.io.FileFilter;

public class FilterByFileExtension implements FileFilter {
    private final String extension;

    public FilterByFileExtension(String extension) {
        this.extension = extension;
    }

    public boolean accept(File file) {
        if (file.canRead()) {
            if (file.isDirectory()) {
                return true;
            }
            if (file.isFile() && file.getName().endsWith(this.extension)) {
                return true;
            }
        }
        return false;
    }
}
