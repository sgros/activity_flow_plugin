package androidx.work.impl;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.migration.Migration;
import android.content.Context;
import android.os.Build.VERSION;
import androidx.work.impl.utils.Preferences;

public class WorkDatabaseMigrations {
   public static Migration MIGRATION_1_2 = new Migration(1, 2) {
      public void migrate(SupportSQLiteDatabase var1) {
         var1.execSQL("CREATE TABLE IF NOT EXISTS `SystemIdInfo` (`work_spec_id` TEXT NOT NULL, `system_id` INTEGER NOT NULL, PRIMARY KEY(`work_spec_id`), FOREIGN KEY(`work_spec_id`) REFERENCES `WorkSpec`(`id`) ON UPDATE CASCADE ON DELETE CASCADE )");
         var1.execSQL("INSERT INTO SystemIdInfo(work_spec_id, system_id) SELECT work_spec_id, alarm_id AS system_id FROM alarmInfo");
         var1.execSQL("DROP TABLE IF EXISTS alarmInfo");
         var1.execSQL("INSERT OR IGNORE INTO worktag(tag, work_spec_id) SELECT worker_class_name AS tag, id AS work_spec_id FROM workspec");
      }
   };
   public static Migration MIGRATION_3_4 = new Migration(3, 4) {
      public void migrate(SupportSQLiteDatabase var1) {
         if (VERSION.SDK_INT >= 23) {
            var1.execSQL("UPDATE workspec SET schedule_requested_at=0 WHERE state NOT IN (2, 3, 5) AND schedule_requested_at=-1 AND interval_duration<>0");
         }

      }
   };
   public static Migration MIGRATION_4_5 = new Migration(4, 5) {
      public void migrate(SupportSQLiteDatabase var1) {
         var1.execSQL("ALTER TABLE workspec ADD COLUMN `trigger_content_update_delay` INTEGER NOT NULL DEFAULT -1");
         var1.execSQL("ALTER TABLE workspec ADD COLUMN `trigger_max_content_delay` INTEGER NOT NULL DEFAULT -1");
      }
   };

   public static class WorkMigration extends Migration {
      final Context mContext;

      public WorkMigration(Context var1, int var2, int var3) {
         super(var2, var3);
         this.mContext = var1;
      }

      public void migrate(SupportSQLiteDatabase var1) {
         (new Preferences(this.mContext)).setNeedsReschedule(true);
      }
   }
}
