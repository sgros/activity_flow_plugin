// 
// Decompiled by Procyon v0.5.34
// 

package org.osmdroid.tileprovider.tilesource;

import android.graphics.drawable.Drawable;
import java.io.InputStream;

public interface ITileSource
{
    Drawable getDrawable(final InputStream p0) throws BitmapTileSourceBase.LowMemoryException;
    
    Drawable getDrawable(final String p0) throws BitmapTileSourceBase.LowMemoryException;
    
    int getMaximumZoomLevel();
    
    int getMinimumZoomLevel();
    
    String getTileRelativeFilenameString(final long p0);
    
    int getTileSizePixels();
    
    String name();
}
