package android.arch.persistence.room;

import android.arch.persistence.db.SimpleSQLiteQuery;
import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.db.SupportSQLiteOpenHelper;
import android.arch.persistence.db.SupportSQLiteQuery;
import android.arch.persistence.room.migration.Migration;
import android.database.Cursor;
import java.util.Iterator;
import java.util.List;

public class RoomOpenHelper extends SupportSQLiteOpenHelper.Callback {
   private DatabaseConfiguration mConfiguration;
   private final RoomOpenHelper.Delegate mDelegate;
   private final String mIdentityHash;
   private final String mLegacyHash;

   public RoomOpenHelper(DatabaseConfiguration var1, RoomOpenHelper.Delegate var2, String var3, String var4) {
      super(var2.version);
      this.mConfiguration = var1;
      this.mDelegate = var2;
      this.mIdentityHash = var3;
      this.mLegacyHash = var4;
   }

   private void checkIdentity(SupportSQLiteDatabase var1) {
      boolean var2 = hasRoomMasterTable(var1);
      String var3 = null;
      Object var4 = null;
      if (var2) {
         Cursor var8 = var1.query((SupportSQLiteQuery)(new SimpleSQLiteQuery("SELECT identity_hash FROM room_master_table WHERE id = 42 LIMIT 1")));
         String var7 = (String)var4;

         try {
            if (var8.moveToFirst()) {
               var7 = var8.getString(0);
            }
         } finally {
            var8.close();
         }

         var3 = var7;
      }

      if (!this.mIdentityHash.equals(var3) && !this.mLegacyHash.equals(var3)) {
         throw new IllegalStateException("Room cannot verify the data integrity. Looks like you've changed schema but forgot to update the version number. You can simply fix this by increasing the version number.");
      }
   }

   private void createMasterTableIfNotExists(SupportSQLiteDatabase var1) {
      var1.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
   }

   private static boolean hasRoomMasterTable(SupportSQLiteDatabase var0) {
      Cursor var1 = var0.query("SELECT 1 FROM sqlite_master WHERE type = 'table' AND name='room_master_table'");

      boolean var4;
      label86: {
         boolean var3;
         int var5;
         label85: {
            Throwable var10000;
            label90: {
               boolean var10001;
               boolean var2;
               try {
                  var2 = var1.moveToFirst();
               } catch (Throwable var11) {
                  var10000 = var11;
                  var10001 = false;
                  break label90;
               }

               var3 = false;
               var4 = var3;
               if (!var2) {
                  break label86;
               }

               label80:
               try {
                  var5 = var1.getInt(0);
                  break label85;
               } catch (Throwable var10) {
                  var10000 = var10;
                  var10001 = false;
                  break label80;
               }
            }

            Throwable var12 = var10000;
            var1.close();
            throw var12;
         }

         var4 = var3;
         if (var5 != 0) {
            var4 = true;
         }
      }

      var1.close();
      return var4;
   }

   private void updateIdentity(SupportSQLiteDatabase var1) {
      this.createMasterTableIfNotExists(var1);
      var1.execSQL(RoomMasterTable.createInsertQuery(this.mIdentityHash));
   }

   public void onConfigure(SupportSQLiteDatabase var1) {
      super.onConfigure(var1);
   }

   public void onCreate(SupportSQLiteDatabase var1) {
      this.updateIdentity(var1);
      this.mDelegate.createAllTables(var1);
      this.mDelegate.onCreate(var1);
   }

   public void onDowngrade(SupportSQLiteDatabase var1, int var2, int var3) {
      this.onUpgrade(var1, var2, var3);
   }

   public void onOpen(SupportSQLiteDatabase var1) {
      super.onOpen(var1);
      this.checkIdentity(var1);
      this.mDelegate.onOpen(var1);
      this.mConfiguration = null;
   }

   public void onUpgrade(SupportSQLiteDatabase var1, int var2, int var3) {
      boolean var5;
      label35: {
         if (this.mConfiguration != null) {
            List var4 = this.mConfiguration.migrationContainer.findMigrationPath(var2, var3);
            if (var4 != null) {
               Iterator var7 = var4.iterator();

               while(var7.hasNext()) {
                  ((Migration)var7.next()).migrate(var1);
               }

               this.mDelegate.validateMigration(var1);
               this.updateIdentity(var1);
               var5 = true;
               break label35;
            }
         }

         var5 = false;
      }

      if (!var5) {
         if (this.mConfiguration == null || this.mConfiguration.isMigrationRequiredFrom(var2)) {
            StringBuilder var6 = new StringBuilder();
            var6.append("A migration from ");
            var6.append(var2);
            var6.append(" to ");
            var6.append(var3);
            var6.append(" was required but not found. Please provide the ");
            var6.append("necessary Migration path via ");
            var6.append("RoomDatabase.Builder.addMigration(Migration ...) or allow for ");
            var6.append("destructive migrations via one of the ");
            var6.append("RoomDatabase.Builder.fallbackToDestructiveMigration* methods.");
            throw new IllegalStateException(var6.toString());
         }

         this.mDelegate.dropAllTables(var1);
         this.mDelegate.createAllTables(var1);
      }

   }

   public abstract static class Delegate {
      public final int version;

      public Delegate(int var1) {
         this.version = var1;
      }

      protected abstract void createAllTables(SupportSQLiteDatabase var1);

      protected abstract void dropAllTables(SupportSQLiteDatabase var1);

      protected abstract void onCreate(SupportSQLiteDatabase var1);

      protected abstract void onOpen(SupportSQLiteDatabase var1);

      protected abstract void validateMigration(SupportSQLiteDatabase var1);
   }
}
