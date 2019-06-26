// 
// Decompiled by Procyon v0.5.34
// 

package org.osmdroid.tileprovider.modules;

import java.io.File;
import java.io.InputStream;
import org.osmdroid.tileprovider.tilesource.ITileSource;

public interface IArchiveFile
{
    void close();
    
    InputStream getInputStream(final ITileSource p0, final long p1);
    
    void init(final File p0) throws Exception;
    
    void setIgnoreTileSource(final boolean p0);
}
