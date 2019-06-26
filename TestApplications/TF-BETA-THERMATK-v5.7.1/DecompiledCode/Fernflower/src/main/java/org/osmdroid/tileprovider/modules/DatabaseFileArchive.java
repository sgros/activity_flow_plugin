package org.osmdroid.tileprovider.modules;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.util.Log;
import java.io.File;
import java.io.InputStream;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.ITileSource;
import org.osmdroid.util.MapTileIndex;

public class DatabaseFileArchive implements IArchiveFile {
   static final String[] tile_column = new String[]{"tile"};
   private SQLiteDatabase mDatabase;
   private boolean mIgnoreTileSource = false;

   public void close() {
      this.mDatabase.close();
   }

   public byte[] getImage(ITileSource var1, long var2) {
      SQLiteDatabase var4 = this.mDatabase;
      if (var4 != null && var4.isOpen()) {
         byte[] var25;
         label90: {
            Throwable var10000;
            StringBuilder var23;
            label83: {
               boolean var10001;
               String[] var24;
               try {
                  var24 = new String[1];
               } catch (Throwable var21) {
                  var10000 = var21;
                  var10001 = false;
                  break label83;
               }

               var24[0] = "tile";

               long var5;
               long var7;
               long var9;
               try {
                  var5 = (long)MapTileIndex.getX(var2);
                  var7 = (long)MapTileIndex.getY(var2);
                  var9 = (long)MapTileIndex.getZoom(var2);
               } catch (Throwable var20) {
                  var10000 = var20;
                  var10001 = false;
                  break label83;
               }

               int var11 = (int)var9;
               var5 = ((var9 << var11) + var5 << var11) + var7;

               boolean var12;
               try {
                  var12 = this.mIgnoreTileSource;
               } catch (Throwable var19) {
                  var10000 = var19;
                  var10001 = false;
                  break label83;
               }

               Cursor var22;
               if (!var12) {
                  try {
                     SQLiteDatabase var13 = this.mDatabase;
                     StringBuilder var14 = new StringBuilder();
                     var14.append("key = ");
                     var14.append(var5);
                     var14.append(" and ");
                     var14.append("provider");
                     var14.append(" = ?");
                     var22 = var13.query("tiles", var24, var14.toString(), new String[]{var1.name()}, (String)null, (String)null, (String)null);
                  } catch (Throwable var18) {
                     var10000 = var18;
                     var10001 = false;
                     break label83;
                  }
               } else {
                  try {
                     SQLiteDatabase var27 = this.mDatabase;
                     var23 = new StringBuilder();
                     var23.append("key = ");
                     var23.append(var5);
                     var22 = var27.query("tiles", var24, var23.toString(), (String[])null, (String)null, (String)null, (String)null);
                  } catch (Throwable var17) {
                     var10000 = var17;
                     var10001 = false;
                     break label83;
                  }
               }

               label85: {
                  try {
                     if (var22.getCount() != 0) {
                        var22.moveToFirst();
                        var25 = var22.getBlob(0);
                        break label85;
                     }
                  } catch (Throwable var16) {
                     var10000 = var16;
                     var10001 = false;
                     break label83;
                  }

                  var25 = null;
               }

               try {
                  var22.close();
                  break label90;
               } catch (Throwable var15) {
                  var10000 = var15;
                  var10001 = false;
               }
            }

            Throwable var26 = var10000;
            var23 = new StringBuilder();
            var23.append("Error getting db stream: ");
            var23.append(MapTileIndex.toString(var2));
            Log.w("OsmDroid", var23.toString(), var26);
            return null;
         }

         if (var25 != null) {
            return var25;
         } else {
            return null;
         }
      } else {
         if (Configuration.getInstance().isDebugTileProviders()) {
            Log.d("OsmDroid", "Skipping DatabaseFileArchive lookup, database is closed");
         }

         return null;
      }
   }

   public InputStream getInputStream(ITileSource param1, long param2) {
      // $FF: Couldn't be decompiled
   }

   public void init(File var1) throws Exception {
      this.mDatabase = SQLiteDatabase.openDatabase(var1.getAbsolutePath(), (CursorFactory)null, 17);
   }

   public void setIgnoreTileSource(boolean var1) {
      this.mIgnoreTileSource = var1;
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append("DatabaseFileArchive [mDatabase=");
      var1.append(this.mDatabase.getPath());
      var1.append("]");
      return var1.toString();
   }
}
