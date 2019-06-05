// 
// Decompiled by Procyon v0.5.34
// 

package menion.android.whereyougo.maps.mapsforge.filefilter;

import org.mapsforge.map.reader.header.FileOpenResult;
import java.io.FileFilter;

public interface ValidFileFilter extends FileFilter
{
    FileOpenResult getFileOpenResult();
}
