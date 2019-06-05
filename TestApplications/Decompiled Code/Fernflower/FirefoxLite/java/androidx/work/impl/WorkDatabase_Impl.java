package androidx.work.impl;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.db.SupportSQLiteOpenHelper;
import android.arch.persistence.room.DatabaseConfiguration;
import android.arch.persistence.room.InvalidationTracker;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.RoomOpenHelper;
import android.arch.persistence.room.util.TableInfo;
import androidx.work.impl.model.DependencyDao;
import androidx.work.impl.model.DependencyDao_Impl;
import androidx.work.impl.model.SystemIdInfoDao;
import androidx.work.impl.model.SystemIdInfoDao_Impl;
import androidx.work.impl.model.WorkNameDao;
import androidx.work.impl.model.WorkNameDao_Impl;
import androidx.work.impl.model.WorkSpecDao;
import androidx.work.impl.model.WorkSpecDao_Impl;
import androidx.work.impl.model.WorkTagDao;
import androidx.work.impl.model.WorkTagDao_Impl;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

public class WorkDatabase_Impl extends WorkDatabase {
   private volatile DependencyDao _dependencyDao;
   private volatile SystemIdInfoDao _systemIdInfoDao;
   private volatile WorkNameDao _workNameDao;
   private volatile WorkSpecDao _workSpecDao;
   private volatile WorkTagDao _workTagDao;

   protected InvalidationTracker createInvalidationTracker() {
      return new InvalidationTracker(this, new String[]{"Dependency", "WorkSpec", "WorkTag", "SystemIdInfo", "WorkName"});
   }

   protected SupportSQLiteOpenHelper createOpenHelper(DatabaseConfiguration var1) {
      RoomOpenHelper var2 = new RoomOpenHelper(var1, new RoomOpenHelper.Delegate(5) {
         public void createAllTables(SupportSQLiteDatabase var1) {
            var1.execSQL("CREATE TABLE IF NOT EXISTS `Dependency` (`work_spec_id` TEXT NOT NULL, `prerequisite_id` TEXT NOT NULL, PRIMARY KEY(`work_spec_id`, `prerequisite_id`), FOREIGN KEY(`work_spec_id`) REFERENCES `WorkSpec`(`id`) ON UPDATE CASCADE ON DELETE CASCADE , FOREIGN KEY(`prerequisite_id`) REFERENCES `WorkSpec`(`id`) ON UPDATE CASCADE ON DELETE CASCADE )");
            var1.execSQL("CREATE  INDEX `index_Dependency_work_spec_id` ON `Dependency` (`work_spec_id`)");
            var1.execSQL("CREATE  INDEX `index_Dependency_prerequisite_id` ON `Dependency` (`prerequisite_id`)");
            var1.execSQL("CREATE TABLE IF NOT EXISTS `WorkSpec` (`id` TEXT NOT NULL, `state` INTEGER NOT NULL, `worker_class_name` TEXT NOT NULL, `input_merger_class_name` TEXT, `input` BLOB NOT NULL, `output` BLOB NOT NULL, `initial_delay` INTEGER NOT NULL, `interval_duration` INTEGER NOT NULL, `flex_duration` INTEGER NOT NULL, `run_attempt_count` INTEGER NOT NULL, `backoff_policy` INTEGER NOT NULL, `backoff_delay_duration` INTEGER NOT NULL, `period_start_time` INTEGER NOT NULL, `minimum_retention_duration` INTEGER NOT NULL, `schedule_requested_at` INTEGER NOT NULL, `required_network_type` INTEGER, `requires_charging` INTEGER NOT NULL, `requires_device_idle` INTEGER NOT NULL, `requires_battery_not_low` INTEGER NOT NULL, `requires_storage_not_low` INTEGER NOT NULL, `trigger_content_update_delay` INTEGER NOT NULL, `trigger_max_content_delay` INTEGER NOT NULL, `content_uri_triggers` BLOB, PRIMARY KEY(`id`))");
            var1.execSQL("CREATE  INDEX `index_WorkSpec_schedule_requested_at` ON `WorkSpec` (`schedule_requested_at`)");
            var1.execSQL("CREATE TABLE IF NOT EXISTS `WorkTag` (`tag` TEXT NOT NULL, `work_spec_id` TEXT NOT NULL, PRIMARY KEY(`tag`, `work_spec_id`), FOREIGN KEY(`work_spec_id`) REFERENCES `WorkSpec`(`id`) ON UPDATE CASCADE ON DELETE CASCADE )");
            var1.execSQL("CREATE  INDEX `index_WorkTag_work_spec_id` ON `WorkTag` (`work_spec_id`)");
            var1.execSQL("CREATE TABLE IF NOT EXISTS `SystemIdInfo` (`work_spec_id` TEXT NOT NULL, `system_id` INTEGER NOT NULL, PRIMARY KEY(`work_spec_id`), FOREIGN KEY(`work_spec_id`) REFERENCES `WorkSpec`(`id`) ON UPDATE CASCADE ON DELETE CASCADE )");
            var1.execSQL("CREATE TABLE IF NOT EXISTS `WorkName` (`name` TEXT NOT NULL, `work_spec_id` TEXT NOT NULL, PRIMARY KEY(`name`, `work_spec_id`), FOREIGN KEY(`work_spec_id`) REFERENCES `WorkSpec`(`id`) ON UPDATE CASCADE ON DELETE CASCADE )");
            var1.execSQL("CREATE  INDEX `index_WorkName_work_spec_id` ON `WorkName` (`work_spec_id`)");
            var1.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
            var1.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, \"c84d23ade98552f1cec71088c1f0794c\")");
         }

         public void dropAllTables(SupportSQLiteDatabase var1) {
            var1.execSQL("DROP TABLE IF EXISTS `Dependency`");
            var1.execSQL("DROP TABLE IF EXISTS `WorkSpec`");
            var1.execSQL("DROP TABLE IF EXISTS `WorkTag`");
            var1.execSQL("DROP TABLE IF EXISTS `SystemIdInfo`");
            var1.execSQL("DROP TABLE IF EXISTS `WorkName`");
         }

         protected void onCreate(SupportSQLiteDatabase var1) {
            if (WorkDatabase_Impl.this.mCallbacks != null) {
               int var2 = 0;

               for(int var3 = WorkDatabase_Impl.this.mCallbacks.size(); var2 < var3; ++var2) {
                  ((RoomDatabase.Callback)WorkDatabase_Impl.this.mCallbacks.get(var2)).onCreate(var1);
               }
            }

         }

         public void onOpen(SupportSQLiteDatabase var1) {
            WorkDatabase_Impl.this.mDatabase = var1;
            var1.execSQL("PRAGMA foreign_keys = ON");
            WorkDatabase_Impl.this.internalInitInvalidationTracker(var1);
            if (WorkDatabase_Impl.this.mCallbacks != null) {
               int var2 = 0;

               for(int var3 = WorkDatabase_Impl.this.mCallbacks.size(); var2 < var3; ++var2) {
                  ((RoomDatabase.Callback)WorkDatabase_Impl.this.mCallbacks.get(var2)).onOpen(var1);
               }
            }

         }

         protected void validateMigration(SupportSQLiteDatabase var1) {
            HashMap var2 = new HashMap(2);
            var2.put("work_spec_id", new TableInfo.Column("work_spec_id", "TEXT", true, 1));
            var2.put("prerequisite_id", new TableInfo.Column("prerequisite_id", "TEXT", true, 2));
            HashSet var3 = new HashSet(2);
            var3.add(new TableInfo.ForeignKey("WorkSpec", "CASCADE", "CASCADE", Arrays.asList("work_spec_id"), Arrays.asList("id")));
            var3.add(new TableInfo.ForeignKey("WorkSpec", "CASCADE", "CASCADE", Arrays.asList("prerequisite_id"), Arrays.asList("id")));
            HashSet var4 = new HashSet(2);
            var4.add(new TableInfo.Index("index_Dependency_work_spec_id", false, Arrays.asList("work_spec_id")));
            var4.add(new TableInfo.Index("index_Dependency_prerequisite_id", false, Arrays.asList("prerequisite_id")));
            TableInfo var7 = new TableInfo("Dependency", var2, var3, var4);
            TableInfo var9 = TableInfo.read(var1, "Dependency");
            StringBuilder var5;
            if (var7.equals(var9)) {
               HashMap var8 = new HashMap(23);
               var8.put("id", new TableInfo.Column("id", "TEXT", true, 1));
               var8.put("state", new TableInfo.Column("state", "INTEGER", true, 0));
               var8.put("worker_class_name", new TableInfo.Column("worker_class_name", "TEXT", true, 0));
               var8.put("input_merger_class_name", new TableInfo.Column("input_merger_class_name", "TEXT", false, 0));
               var8.put("input", new TableInfo.Column("input", "BLOB", true, 0));
               var8.put("output", new TableInfo.Column("output", "BLOB", true, 0));
               var8.put("initial_delay", new TableInfo.Column("initial_delay", "INTEGER", true, 0));
               var8.put("interval_duration", new TableInfo.Column("interval_duration", "INTEGER", true, 0));
               var8.put("flex_duration", new TableInfo.Column("flex_duration", "INTEGER", true, 0));
               var8.put("run_attempt_count", new TableInfo.Column("run_attempt_count", "INTEGER", true, 0));
               var8.put("backoff_policy", new TableInfo.Column("backoff_policy", "INTEGER", true, 0));
               var8.put("backoff_delay_duration", new TableInfo.Column("backoff_delay_duration", "INTEGER", true, 0));
               var8.put("period_start_time", new TableInfo.Column("period_start_time", "INTEGER", true, 0));
               var8.put("minimum_retention_duration", new TableInfo.Column("minimum_retention_duration", "INTEGER", true, 0));
               var8.put("schedule_requested_at", new TableInfo.Column("schedule_requested_at", "INTEGER", true, 0));
               var8.put("required_network_type", new TableInfo.Column("required_network_type", "INTEGER", false, 0));
               var8.put("requires_charging", new TableInfo.Column("requires_charging", "INTEGER", true, 0));
               var8.put("requires_device_idle", new TableInfo.Column("requires_device_idle", "INTEGER", true, 0));
               var8.put("requires_battery_not_low", new TableInfo.Column("requires_battery_not_low", "INTEGER", true, 0));
               var8.put("requires_storage_not_low", new TableInfo.Column("requires_storage_not_low", "INTEGER", true, 0));
               var8.put("trigger_content_update_delay", new TableInfo.Column("trigger_content_update_delay", "INTEGER", true, 0));
               var8.put("trigger_max_content_delay", new TableInfo.Column("trigger_max_content_delay", "INTEGER", true, 0));
               var8.put("content_uri_triggers", new TableInfo.Column("content_uri_triggers", "BLOB", false, 0));
               var4 = new HashSet(0);
               HashSet var6 = new HashSet(1);
               var6.add(new TableInfo.Index("index_WorkSpec_schedule_requested_at", false, Arrays.asList("schedule_requested_at")));
               var9 = new TableInfo("WorkSpec", var8, var4, var6);
               var7 = TableInfo.read(var1, "WorkSpec");
               if (var9.equals(var7)) {
                  var8 = new HashMap(2);
                  var8.put("tag", new TableInfo.Column("tag", "TEXT", true, 1));
                  var8.put("work_spec_id", new TableInfo.Column("work_spec_id", "TEXT", true, 2));
                  var6 = new HashSet(1);
                  var6.add(new TableInfo.ForeignKey("WorkSpec", "CASCADE", "CASCADE", Arrays.asList("work_spec_id"), Arrays.asList("id")));
                  var4 = new HashSet(1);
                  var4.add(new TableInfo.Index("index_WorkTag_work_spec_id", false, Arrays.asList("work_spec_id")));
                  var7 = new TableInfo("WorkTag", var8, var6, var4);
                  var9 = TableInfo.read(var1, "WorkTag");
                  if (var7.equals(var9)) {
                     HashMap var10 = new HashMap(2);
                     var10.put("work_spec_id", new TableInfo.Column("work_spec_id", "TEXT", true, 1));
                     var10.put("system_id", new TableInfo.Column("system_id", "INTEGER", true, 0));
                     var3 = new HashSet(1);
                     var3.add(new TableInfo.ForeignKey("WorkSpec", "CASCADE", "CASCADE", Arrays.asList("work_spec_id"), Arrays.asList("id")));
                     var9 = new TableInfo("SystemIdInfo", var10, var3, new HashSet(0));
                     var7 = TableInfo.read(var1, "SystemIdInfo");
                     if (var9.equals(var7)) {
                        var2 = new HashMap(2);
                        var2.put("name", new TableInfo.Column("name", "TEXT", true, 1));
                        var2.put("work_spec_id", new TableInfo.Column("work_spec_id", "TEXT", true, 2));
                        var4 = new HashSet(1);
                        var4.add(new TableInfo.ForeignKey("WorkSpec", "CASCADE", "CASCADE", Arrays.asList("work_spec_id"), Arrays.asList("id")));
                        var3 = new HashSet(1);
                        var3.add(new TableInfo.Index("index_WorkName_work_spec_id", false, Arrays.asList("work_spec_id")));
                        var9 = new TableInfo("WorkName", var2, var4, var3);
                        var7 = TableInfo.read(var1, "WorkName");
                        if (!var9.equals(var7)) {
                           var5 = new StringBuilder();
                           var5.append("Migration didn't properly handle WorkName(androidx.work.impl.model.WorkName).\n Expected:\n");
                           var5.append(var9);
                           var5.append("\n Found:\n");
                           var5.append(var7);
                           throw new IllegalStateException(var5.toString());
                        }
                     } else {
                        var5 = new StringBuilder();
                        var5.append("Migration didn't properly handle SystemIdInfo(androidx.work.impl.model.SystemIdInfo).\n Expected:\n");
                        var5.append(var9);
                        var5.append("\n Found:\n");
                        var5.append(var7);
                        throw new IllegalStateException(var5.toString());
                     }
                  } else {
                     var5 = new StringBuilder();
                     var5.append("Migration didn't properly handle WorkTag(androidx.work.impl.model.WorkTag).\n Expected:\n");
                     var5.append(var7);
                     var5.append("\n Found:\n");
                     var5.append(var9);
                     throw new IllegalStateException(var5.toString());
                  }
               } else {
                  var5 = new StringBuilder();
                  var5.append("Migration didn't properly handle WorkSpec(androidx.work.impl.model.WorkSpec).\n Expected:\n");
                  var5.append(var9);
                  var5.append("\n Found:\n");
                  var5.append(var7);
                  throw new IllegalStateException(var5.toString());
               }
            } else {
               var5 = new StringBuilder();
               var5.append("Migration didn't properly handle Dependency(androidx.work.impl.model.Dependency).\n Expected:\n");
               var5.append(var7);
               var5.append("\n Found:\n");
               var5.append(var9);
               throw new IllegalStateException(var5.toString());
            }
         }
      }, "c84d23ade98552f1cec71088c1f0794c", "1db8206f0da6aa81bbdd2d99a82d9e14");
      SupportSQLiteOpenHelper.Configuration var3 = SupportSQLiteOpenHelper.Configuration.builder(var1.context).name(var1.name).callback(var2).build();
      return var1.sqliteOpenHelperFactory.create(var3);
   }

   public DependencyDao dependencyDao() {
      if (this._dependencyDao != null) {
         return this._dependencyDao;
      } else {
         synchronized(this){}

         Throwable var10000;
         boolean var10001;
         label136: {
            try {
               if (this._dependencyDao == null) {
                  DependencyDao_Impl var1 = new DependencyDao_Impl(this);
                  this._dependencyDao = var1;
               }
            } catch (Throwable var13) {
               var10000 = var13;
               var10001 = false;
               break label136;
            }

            label133:
            try {
               DependencyDao var15 = this._dependencyDao;
               return var15;
            } catch (Throwable var12) {
               var10000 = var12;
               var10001 = false;
               break label133;
            }
         }

         while(true) {
            Throwable var14 = var10000;

            try {
               throw var14;
            } catch (Throwable var11) {
               var10000 = var11;
               var10001 = false;
               continue;
            }
         }
      }
   }

   public SystemIdInfoDao systemIdInfoDao() {
      if (this._systemIdInfoDao != null) {
         return this._systemIdInfoDao;
      } else {
         synchronized(this){}

         Throwable var10000;
         boolean var10001;
         label136: {
            try {
               if (this._systemIdInfoDao == null) {
                  SystemIdInfoDao_Impl var1 = new SystemIdInfoDao_Impl(this);
                  this._systemIdInfoDao = var1;
               }
            } catch (Throwable var13) {
               var10000 = var13;
               var10001 = false;
               break label136;
            }

            label133:
            try {
               SystemIdInfoDao var15 = this._systemIdInfoDao;
               return var15;
            } catch (Throwable var12) {
               var10000 = var12;
               var10001 = false;
               break label133;
            }
         }

         while(true) {
            Throwable var14 = var10000;

            try {
               throw var14;
            } catch (Throwable var11) {
               var10000 = var11;
               var10001 = false;
               continue;
            }
         }
      }
   }

   public WorkNameDao workNameDao() {
      if (this._workNameDao != null) {
         return this._workNameDao;
      } else {
         synchronized(this){}

         Throwable var10000;
         boolean var10001;
         label136: {
            try {
               if (this._workNameDao == null) {
                  WorkNameDao_Impl var1 = new WorkNameDao_Impl(this);
                  this._workNameDao = var1;
               }
            } catch (Throwable var13) {
               var10000 = var13;
               var10001 = false;
               break label136;
            }

            label133:
            try {
               WorkNameDao var15 = this._workNameDao;
               return var15;
            } catch (Throwable var12) {
               var10000 = var12;
               var10001 = false;
               break label133;
            }
         }

         while(true) {
            Throwable var14 = var10000;

            try {
               throw var14;
            } catch (Throwable var11) {
               var10000 = var11;
               var10001 = false;
               continue;
            }
         }
      }
   }

   public WorkSpecDao workSpecDao() {
      if (this._workSpecDao != null) {
         return this._workSpecDao;
      } else {
         synchronized(this){}

         Throwable var10000;
         boolean var10001;
         label136: {
            try {
               if (this._workSpecDao == null) {
                  WorkSpecDao_Impl var1 = new WorkSpecDao_Impl(this);
                  this._workSpecDao = var1;
               }
            } catch (Throwable var13) {
               var10000 = var13;
               var10001 = false;
               break label136;
            }

            label133:
            try {
               WorkSpecDao var15 = this._workSpecDao;
               return var15;
            } catch (Throwable var12) {
               var10000 = var12;
               var10001 = false;
               break label133;
            }
         }

         while(true) {
            Throwable var14 = var10000;

            try {
               throw var14;
            } catch (Throwable var11) {
               var10000 = var11;
               var10001 = false;
               continue;
            }
         }
      }
   }

   public WorkTagDao workTagDao() {
      if (this._workTagDao != null) {
         return this._workTagDao;
      } else {
         synchronized(this){}

         Throwable var10000;
         boolean var10001;
         label136: {
            try {
               if (this._workTagDao == null) {
                  WorkTagDao_Impl var1 = new WorkTagDao_Impl(this);
                  this._workTagDao = var1;
               }
            } catch (Throwable var13) {
               var10000 = var13;
               var10001 = false;
               break label136;
            }

            label133:
            try {
               WorkTagDao var15 = this._workTagDao;
               return var15;
            } catch (Throwable var12) {
               var10000 = var12;
               var10001 = false;
               break label133;
            }
         }

         while(true) {
            Throwable var14 = var10000;

            try {
               throw var14;
            } catch (Throwable var11) {
               var10000 = var11;
               var10001 = false;
               continue;
            }
         }
      }
   }
}
