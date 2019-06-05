package android.arch.persistence.room;

import android.arch.persistence.db.SupportSQLiteOpenHelper;
import android.content.Context;
import java.util.List;
import java.util.Set;

public class DatabaseConfiguration {
   public final boolean allowMainThreadQueries;
   public final List callbacks;
   public final Context context;
   public final RoomDatabase.JournalMode journalMode;
   private final Set mMigrationNotRequiredFrom;
   public final RoomDatabase.MigrationContainer migrationContainer;
   public final String name;
   public final boolean requireMigration;
   public final SupportSQLiteOpenHelper.Factory sqliteOpenHelperFactory;

   public DatabaseConfiguration(Context var1, String var2, SupportSQLiteOpenHelper.Factory var3, RoomDatabase.MigrationContainer var4, List var5, boolean var6, RoomDatabase.JournalMode var7, boolean var8, Set var9) {
      this.sqliteOpenHelperFactory = var3;
      this.context = var1;
      this.name = var2;
      this.migrationContainer = var4;
      this.callbacks = var5;
      this.allowMainThreadQueries = var6;
      this.journalMode = var7;
      this.requireMigration = var8;
      this.mMigrationNotRequiredFrom = var9;
   }

   public boolean isMigrationRequiredFrom(int var1) {
      boolean var2;
      if (!this.requireMigration || this.mMigrationNotRequiredFrom != null && this.mMigrationNotRequiredFrom.contains(var1)) {
         var2 = false;
      } else {
         var2 = true;
      }

      return var2;
   }
}
