package org.osmdroid.tileprovider.modules;

import android.util.Log;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import org.osmdroid.tileprovider.tilesource.ITileSource;
import org.osmdroid.util.MapTileIndex;

public class ZipFileArchive implements IArchiveFile {
   private boolean mIgnoreTileSource = false;
   protected ZipFile mZipFile;

   private String getTileRelativeFilenameString(long var1, String var3) {
      StringBuilder var4 = new StringBuilder();
      var4.append(var3);
      var4.append('/');
      var4.append(MapTileIndex.getZoom(var1));
      var4.append('/');
      var4.append(MapTileIndex.getX(var1));
      var4.append('/');
      var4.append(MapTileIndex.getY(var1));
      var4.append(".png");
      return var4.toString();
   }

   public void close() {
      try {
         this.mZipFile.close();
      } catch (IOException var2) {
      }

   }

   public InputStream getInputStream(ITileSource var1, long var2) {
      IOException var10000;
      label59: {
         boolean var10001;
         label60: {
            ZipEntry var11;
            try {
               if (this.mIgnoreTileSource) {
                  break label60;
               }

               String var10 = var1.getTileRelativeFilenameString(var2);
               var11 = this.mZipFile.getEntry(var10);
            } catch (IOException var9) {
               var10000 = var9;
               var10001 = false;
               break label59;
            }

            if (var11 == null) {
               return null;
            }

            try {
               return this.mZipFile.getInputStream(var11);
            } catch (IOException var6) {
               var10000 = var6;
               var10001 = false;
               break label59;
            }
         }

         Enumeration var12;
         try {
            var12 = this.mZipFile.entries();
         } catch (IOException var8) {
            var10000 = var8;
            var10001 = false;
            break label59;
         }

         while(true) {
            ZipEntry var15;
            try {
               String var4;
               do {
                  if (!var12.hasMoreElements()) {
                     return null;
                  }

                  var4 = ((ZipEntry)var12.nextElement()).getName();
               } while(!var4.contains("/"));

               var4 = this.getTileRelativeFilenameString(var2, var4.split("/")[0]);
               var15 = this.mZipFile.getEntry(var4);
            } catch (IOException var7) {
               var10000 = var7;
               var10001 = false;
               break;
            }

            if (var15 != null) {
               try {
                  InputStream var14 = this.mZipFile.getInputStream(var15);
                  return var14;
               } catch (IOException var5) {
                  var10000 = var5;
                  var10001 = false;
                  break;
               }
            }
         }
      }

      IOException var13 = var10000;
      StringBuilder var16 = new StringBuilder();
      var16.append("Error getting zip stream: ");
      var16.append(MapTileIndex.toString(var2));
      Log.w("OsmDroid", var16.toString(), var13);
      return null;
   }

   public void init(File var1) throws Exception {
      this.mZipFile = new ZipFile(var1);
   }

   public void setIgnoreTileSource(boolean var1) {
      this.mIgnoreTileSource = var1;
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append("ZipFileArchive [mZipFile=");
      var1.append(this.mZipFile.getName());
      var1.append("]");
      return var1.toString();
   }
}
