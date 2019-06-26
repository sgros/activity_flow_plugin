// 
// Decompiled by Procyon v0.5.34
// 

package org.osmdroid.tileprovider.modules;

import java.io.InputStream;
import org.osmdroid.tileprovider.tilesource.ITileSource;

public interface IFilesystemCache
{
    void onDetach();
    
    boolean saveFile(final ITileSource p0, final long p1, final InputStream p2, final Long p3);
}
