package org.osmdroid.tileprovider.modules;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.util.Log;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import org.osmdroid.tileprovider.tilesource.ITileSource;
import org.osmdroid.util.MapTileIndex;

public class MBTilesFileArchive implements IArchiveFile {
   private SQLiteDatabase mDatabase;

   public void close() {
      this.mDatabase.close();
   }

   public InputStream getInputStream(ITileSource var1, long var2) {
      ByteArrayInputStream var15;
      label50: {
         Throwable var10000;
         label45: {
            String var4;
            double var5;
            int var7;
            boolean var10001;
            try {
               var4 = Integer.toString(MapTileIndex.getX(var2));
               var5 = Math.pow(2.0D, (double)MapTileIndex.getZoom(var2));
               var7 = MapTileIndex.getY(var2);
            } catch (Throwable var13) {
               var10000 = var13;
               var10001 = false;
               break label45;
            }

            double var8 = (double)var7;
            Double.isNaN(var8);

            Cursor var17;
            label34: {
               try {
                  String var14 = Double.toString(var5 - var8 - 1.0D);
                  String var10 = Integer.toString(MapTileIndex.getZoom(var2));
                  var17 = this.mDatabase.query("tiles", new String[]{"tile_data"}, "tile_column=? and tile_row=? and zoom_level=?", new String[]{var4, var14, var10}, (String)null, (String)null, (String)null);
                  if (var17.getCount() != 0) {
                     var17.moveToFirst();
                     var15 = new ByteArrayInputStream(var17.getBlob(0));
                     break label34;
                  }
               } catch (Throwable var12) {
                  var10000 = var12;
                  var10001 = false;
                  break label45;
               }

               var15 = null;
            }

            try {
               var17.close();
               break label50;
            } catch (Throwable var11) {
               var10000 = var11;
               var10001 = false;
            }
         }

         Throwable var16 = var10000;
         StringBuilder var18 = new StringBuilder();
         var18.append("Error getting db stream: ");
         var18.append(MapTileIndex.toString(var2));
         Log.w("OsmDroid", var18.toString(), var16);
         return null;
      }

      if (var15 != null) {
         return var15;
      } else {
         return null;
      }
   }

   public void init(File var1) throws Exception {
      this.mDatabase = SQLiteDatabase.openDatabase(var1.getAbsolutePath(), (CursorFactory)null, 17);
   }

   public void setIgnoreTileSource(boolean var1) {
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append("DatabaseFileArchive [mDatabase=");
      var1.append(this.mDatabase.getPath());
      var1.append("]");
      return var1.toString();
   }
}
