package androidx.work.impl;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import androidx.work.impl.model.DependencyDao;
import androidx.work.impl.model.SystemIdInfoDao;
import androidx.work.impl.model.WorkNameDao;
import androidx.work.impl.model.WorkSpecDao;
import androidx.work.impl.model.WorkTagDao;
import java.util.concurrent.TimeUnit;

public abstract class WorkDatabase extends RoomDatabase {
   private static final long PRUNE_THRESHOLD_MILLIS;

   static {
      PRUNE_THRESHOLD_MILLIS = TimeUnit.DAYS.toMillis(7L);
   }

   public static WorkDatabase create(Context var0, boolean var1) {
      RoomDatabase.Builder var2;
      if (var1) {
         var2 = Room.inMemoryDatabaseBuilder(var0, WorkDatabase.class).allowMainThreadQueries();
      } else {
         var2 = Room.databaseBuilder(var0, WorkDatabase.class, "androidx.work.workdb");
      }

      return (WorkDatabase)var2.addCallback(generateCleanupCallback()).addMigrations(WorkDatabaseMigrations.MIGRATION_1_2).addMigrations(new WorkDatabaseMigrations.WorkMigration(var0, 2, 3)).addMigrations(WorkDatabaseMigrations.MIGRATION_3_4).addMigrations(WorkDatabaseMigrations.MIGRATION_4_5).fallbackToDestructiveMigration().build();
   }

   static RoomDatabase.Callback generateCleanupCallback() {
      return new RoomDatabase.Callback() {
         public void onOpen(SupportSQLiteDatabase var1) {
            super.onOpen(var1);
            var1.beginTransaction();

            try {
               var1.execSQL("UPDATE workspec SET state=0, schedule_requested_at=-1 WHERE state=1");
               var1.execSQL(WorkDatabase.getPruneSQL());
               var1.setTransactionSuccessful();
            } finally {
               var1.endTransaction();
            }

         }
      };
   }

   static long getPruneDate() {
      return System.currentTimeMillis() - PRUNE_THRESHOLD_MILLIS;
   }

   static String getPruneSQL() {
      StringBuilder var0 = new StringBuilder();
      var0.append("DELETE FROM workspec WHERE state IN (2, 3, 5) AND (period_start_time + minimum_retention_duration) < ");
      var0.append(getPruneDate());
      var0.append(" AND (SELECT COUNT(*)=0 FROM dependency WHERE     prerequisite_id=id AND     work_spec_id NOT IN         (SELECT id FROM workspec WHERE state IN (2, 3, 5)))");
      return var0.toString();
   }

   public abstract DependencyDao dependencyDao();

   public abstract SystemIdInfoDao systemIdInfoDao();

   public abstract WorkNameDao workNameDao();

   public abstract WorkSpecDao workSpecDao();

   public abstract WorkTagDao workTagDao();
}
