package org.osmdroid.tileprovider.modules;

import java.io.InputStream;
import org.osmdroid.tileprovider.tilesource.ITileSource;

public interface IFilesystemCache {
   void onDetach();

   boolean saveFile(ITileSource var1, long var2, InputStream var4, Long var5);
}
