package menion.android.whereyougo.maps.mapsforge.filefilter;

import java.io.FileFilter;
import org.mapsforge.map.reader.header.FileOpenResult;

public interface ValidFileFilter extends FileFilter {
    FileOpenResult getFileOpenResult();
}
