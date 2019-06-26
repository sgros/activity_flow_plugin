package org.osmdroid.tileprovider.modules;

import java.io.File;
import java.io.InputStream;
import org.osmdroid.tileprovider.tilesource.ITileSource;

public interface IArchiveFile {
   void close();

   InputStream getInputStream(ITileSource var1, long var2);

   void init(File var1) throws Exception;

   void setIgnoreTileSource(boolean var1);
}
