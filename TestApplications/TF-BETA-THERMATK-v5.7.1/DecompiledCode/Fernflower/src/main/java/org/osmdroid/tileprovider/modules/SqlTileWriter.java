package org.osmdroid.tileprovider.modules;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteFullException;
import android.graphics.drawable.Drawable;
import android.util.Log;
import java.io.File;
import java.io.InputStream;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.ITileSource;
import org.osmdroid.util.GarbageCollector;
import org.osmdroid.util.MapTileIndex;
import org.osmdroid.util.SplashScreenable;

public class SqlTileWriter implements IFilesystemCache, SplashScreenable {
   private static boolean cleanOnStartup;
   protected static File db_file;
   private static final String[] expireQueryColumn = new String[]{"expires"};
   static boolean hasInited = false;
   protected static SQLiteDatabase mDb;
   private static final Object mLock = new Object();
   private static final String[] queryColumns = new String[]{"tile", "expires"};
   private final GarbageCollector garbageCollector = new GarbageCollector(new Runnable() {
      public void run() {
         SqlTileWriter.this.runCleanupOperation();
      }
   });
   protected long lastSizeCheck = 0L;

   public SqlTileWriter() {
      this.getDb();
      if (!hasInited) {
         hasInited = true;
         if (cleanOnStartup) {
            this.garbageCollector.gc();
         }
      }

   }

   private void createIndex(SQLiteDatabase var1) {
      var1.execSQL("CREATE INDEX IF NOT EXISTS expires_index ON tiles (expires);");
   }

   public static long getIndex(long var0) {
      return getIndex((long)MapTileIndex.getX(var0), (long)MapTileIndex.getY(var0), (long)MapTileIndex.getZoom(var0));
   }

   public static long getIndex(long var0, long var2, long var4) {
      int var6 = (int)var4;
      return ((var4 << var6) + var0 << var6) + var2;
   }

   public static String[] getPrimaryKeyParameters(long var0, String var2) {
      return new String[]{String.valueOf(var0), var2};
   }

   public static String[] getPrimaryKeyParameters(long var0, ITileSource var2) {
      return getPrimaryKeyParameters(var0, var2.name());
   }

   public static boolean isFunctionalException(SQLiteException var0) {
      byte var1;
      label63: {
         String var2 = var0.getClass().getSimpleName();
         switch(var2.hashCode()) {
         case -2070793707:
            if (var2.equals("SQLiteOutOfMemoryException")) {
               var1 = 14;
               break label63;
            }
            break;
         case -1764604492:
            if (var2.equals("SQLiteFullException")) {
               var1 = 4;
               break label63;
            }
            break;
         case -1458338457:
            if (var2.equals("SQLiteBindOrColumnIndexOutOfRangeException")) {
               var1 = 0;
               break label63;
            }
            break;
         case -1115484154:
            if (var2.equals("SQLiteReadOnlyDatabaseException")) {
               var1 = 15;
               break label63;
            }
            break;
         case -1113540439:
            if (var2.equals("SQLiteDatabaseCorruptException")) {
               var1 = 10;
               break label63;
            }
            break;
         case -672728977:
            if (var2.equals("SQLiteAccessPermException")) {
               var1 = 8;
               break label63;
            }
            break;
         case -669227773:
            if (var2.equals("SQLiteTableLockedException")) {
               var1 = 6;
               break label63;
            }
            break;
         case -119599910:
            if (var2.equals("SQLiteCantOpenDatabaseException")) {
               var1 = 9;
               break label63;
            }
            break;
         case 20404371:
            if (var2.equals("SQLiteMisuseException")) {
               var1 = 5;
               break label63;
            }
            break;
         case 124364321:
            if (var2.equals("SQLiteDoneException")) {
               var1 = 13;
               break label63;
            }
            break;
         case 325468747:
            if (var2.equals("SQLiteAbortException")) {
               var1 = 7;
               break label63;
            }
            break;
         case 532355648:
            if (var2.equals("SQLiteDiskIOException")) {
               var1 = 12;
               break label63;
            }
            break;
         case 666588538:
            if (var2.equals("SQLiteBlobTooBigException")) {
               var1 = 1;
               break label63;
            }
            break;
         case 1061155622:
            if (var2.equals("SQLiteConstraintException")) {
               var1 = 2;
               break label63;
            }
            break;
         case 1400520606:
            if (var2.equals("SQLiteDatabaseLockedException")) {
               var1 = 11;
               break label63;
            }
            break;
         case 1939376593:
            if (var2.equals("SQLiteDatatypeMismatchException")) {
               var1 = 3;
               break label63;
            }
         }

         var1 = -1;
      }

      switch(var1) {
      case 0:
      case 1:
      case 2:
      case 3:
      case 4:
      case 5:
      case 6:
         return true;
      case 7:
      case 8:
      case 9:
      case 10:
      case 11:
      case 12:
      case 13:
      case 14:
      case 15:
      default:
         return false;
      }
   }

   protected void catchException(Exception var1) {
      if (var1 instanceof SQLiteException && !isFunctionalException((SQLiteException)var1)) {
         this.refreshDb();
      }

   }

   protected SQLiteDatabase getDb() {
      // $FF: Couldn't be decompiled
   }

   public Cursor getTileCursor(String[] var1, String[] var2) {
      return this.getDb().query("tiles", var2, "key=? and provider=?", var1, (String)null, (String)null, (String)null);
   }

   public Drawable loadTile(ITileSource param1, long param2) throws Exception {
      // $FF: Couldn't be decompiled
   }

   public void onDetach() {
   }

   public void refreshDb() {
      Object var1 = mLock;
      synchronized(var1){}

      Throwable var10000;
      boolean var10001;
      label122: {
         try {
            if (mDb != null) {
               mDb.close();
               mDb = null;
            }
         } catch (Throwable var14) {
            var10000 = var14;
            var10001 = false;
            break label122;
         }

         label119:
         try {
            return;
         } catch (Throwable var13) {
            var10000 = var13;
            var10001 = false;
            break label119;
         }
      }

      while(true) {
         Throwable var2 = var10000;

         try {
            throw var2;
         } catch (Throwable var12) {
            var10000 = var12;
            var10001 = false;
            continue;
         }
      }
   }

   public void runCleanupOperation() {
      SQLiteDatabase var1 = this.getDb();
      if (var1 != null && var1.isOpen()) {
         this.createIndex(var1);
         long var2 = db_file.length();
         if (var2 > Configuration.getInstance().getTileFileSystemCacheMaxBytes()) {
            this.runCleanupOperation(var2 - Configuration.getInstance().getTileFileSystemCacheTrimBytes(), Configuration.getInstance().getTileGCBulkSize(), Configuration.getInstance().getTileGCBulkPauseInMillis(), true);
         }
      } else {
         if (Configuration.getInstance().isDebugMode()) {
            Log.d("OsmDroid", "Finished init thread, aborted due to null database reference");
         }

      }
   }

   public void runCleanupOperation(long var1, int var3, long var4, boolean var6) {
      StringBuilder var7 = new StringBuilder();
      SQLiteDatabase var8 = this.getDb();
      boolean var9 = true;

      while(var1 > 0L) {
         if (var9) {
            var9 = false;
         } else if (var4 > 0L) {
            try {
               Thread.sleep(var4);
            } catch (InterruptedException var19) {
            }
         }

         long var10 = System.currentTimeMillis();

         String var13;
         String var14;
         Cursor var23;
         label70: {
            Exception var10000;
            label78: {
               StringBuilder var12;
               boolean var10001;
               try {
                  var12 = new StringBuilder();
                  var12.append("SELECT key,LENGTH(HEX(tile))/2 FROM tiles WHERE expires IS NOT NULL ");
               } catch (Exception var22) {
                  var10000 = var22;
                  var10001 = false;
                  break label78;
               }

               var13 = "";
               if (var6) {
                  var14 = "";
               } else {
                  try {
                     StringBuilder var24 = new StringBuilder();
                     var24.append("AND expires < ");
                     var24.append(var10);
                     var24.append(" ");
                     var14 = var24.toString();
                  } catch (Exception var21) {
                     var10000 = var21;
                     var10001 = false;
                     break label78;
                  }
               }

               try {
                  var12.append(var14);
                  var12.append("ORDER BY ");
                  var12.append("expires");
                  var12.append(" ASC LIMIT ");
                  var12.append(var3);
                  var23 = var8.rawQuery(var12.toString(), (String[])null);
                  break label70;
               } catch (Exception var20) {
                  var10000 = var20;
                  var10001 = false;
               }
            }

            Exception var25 = var10000;
            this.catchException(var25);
            break;
         }

         var23.moveToFirst();
         var7.setLength(0);
         var7.append("key in (");
         var14 = "";

         while(!var23.isAfterLast()) {
            var10 = var23.getLong(0);
            long var15 = var23.getLong(1);
            var23.moveToNext();
            var7.append(var14);
            var7.append(var10);
            var14 = ",";
            var1 -= var15;
            if (var1 <= 0L) {
               break;
            }
         }

         var23.close();
         if (var13.equals(var14)) {
            return;
         }

         var7.append(')');

         try {
            var8.delete("tiles", var7.toString(), (String[])null);
         } catch (SQLiteFullException var17) {
            Log.e("OsmDroid", "SQLiteFullException while cleanup.", var17);
            this.catchException(var17);
         } catch (Exception var18) {
            this.catchException(var18);
            return;
         }
      }

   }

   public boolean saveFile(ITileSource param1, long param2, InputStream param4, Long param5) {
      // $FF: Couldn't be decompiled
   }
}
