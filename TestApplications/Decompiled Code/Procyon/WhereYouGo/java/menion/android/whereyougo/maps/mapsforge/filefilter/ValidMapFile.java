// 
// Decompiled by Procyon v0.5.34
// 

package menion.android.whereyougo.maps.mapsforge.filefilter;

import org.mapsforge.map.reader.MapDatabase;
import java.io.File;
import org.mapsforge.map.reader.header.FileOpenResult;

public final class ValidMapFile implements ValidFileFilter
{
    private FileOpenResult fileOpenResult;
    
    @Override
    public boolean accept(final File file) {
        final MapDatabase mapDatabase = new MapDatabase();
        this.fileOpenResult = mapDatabase.openFile(file);
        mapDatabase.closeFile();
        return this.fileOpenResult.isSuccess();
    }
    
    @Override
    public FileOpenResult getFileOpenResult() {
        return this.fileOpenResult;
    }
}
