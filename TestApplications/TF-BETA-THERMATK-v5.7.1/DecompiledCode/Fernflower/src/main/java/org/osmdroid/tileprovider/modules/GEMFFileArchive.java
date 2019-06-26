package org.osmdroid.tileprovider.modules;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import org.osmdroid.tileprovider.tilesource.ITileSource;
import org.osmdroid.util.GEMFFile;
import org.osmdroid.util.MapTileIndex;

public class GEMFFileArchive implements IArchiveFile {
   private GEMFFile mFile;

   public void close() {
      try {
         this.mFile.close();
      } catch (IOException var2) {
      }

   }

   public InputStream getInputStream(ITileSource var1, long var2) {
      return this.mFile.getInputStream(MapTileIndex.getX(var2), MapTileIndex.getY(var2), MapTileIndex.getZoom(var2));
   }

   public void init(File var1) throws Exception {
      this.mFile = new GEMFFile(var1);
   }

   public void setIgnoreTileSource(boolean var1) {
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append("GEMFFileArchive [mGEMFFile=");
      var1.append(this.mFile.getName());
      var1.append("]");
      return var1.toString();
   }
}
