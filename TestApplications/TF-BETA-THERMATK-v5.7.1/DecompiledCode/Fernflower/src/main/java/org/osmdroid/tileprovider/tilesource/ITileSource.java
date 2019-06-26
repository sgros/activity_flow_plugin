package org.osmdroid.tileprovider.tilesource;

import android.graphics.drawable.Drawable;
import java.io.InputStream;

public interface ITileSource {
   Drawable getDrawable(InputStream var1) throws BitmapTileSourceBase.LowMemoryException;

   Drawable getDrawable(String var1) throws BitmapTileSourceBase.LowMemoryException;

   int getMaximumZoomLevel();

   int getMinimumZoomLevel();

   String getTileRelativeFilenameString(long var1);

   int getTileSizePixels();

   String name();
}
