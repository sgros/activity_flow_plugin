package menion.android.whereyougo.maps.mapsforge.filefilter;

import java.io.File;
import org.mapsforge.map.reader.MapDatabase;
import org.mapsforge.map.reader.header.FileOpenResult;

public final class ValidMapFile implements ValidFileFilter {
    private FileOpenResult fileOpenResult;

    public boolean accept(File mapFile) {
        MapDatabase mapDatabase = new MapDatabase();
        this.fileOpenResult = mapDatabase.openFile(mapFile);
        mapDatabase.closeFile();
        return this.fileOpenResult.isSuccess();
    }

    public FileOpenResult getFileOpenResult() {
        return this.fileOpenResult;
    }
}
