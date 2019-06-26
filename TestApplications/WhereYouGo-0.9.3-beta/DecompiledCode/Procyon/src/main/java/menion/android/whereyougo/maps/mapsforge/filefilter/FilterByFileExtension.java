// 
// Decompiled by Procyon v0.5.34
// 

package menion.android.whereyougo.maps.mapsforge.filefilter;

import java.io.File;
import java.io.FileFilter;

public class FilterByFileExtension implements FileFilter
{
    private final String extension;
    
    public FilterByFileExtension(final String extension) {
        this.extension = extension;
    }
    
    @Override
    public boolean accept(final File file) {
        boolean b = true;
        if (!file.canRead() || (!file.isDirectory() && (!file.isFile() || !file.getName().endsWith(this.extension)))) {
            b = false;
        }
        return b;
    }
}
