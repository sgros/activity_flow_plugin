// 
// Decompiled by Procyon v0.5.34
// 

package androidx.work.impl;

import androidx.work.impl.model.WorkTagDao_Impl;
import androidx.work.impl.model.WorkSpecDao_Impl;
import androidx.work.impl.model.WorkNameDao_Impl;
import androidx.work.impl.model.SystemIdInfoDao_Impl;
import androidx.work.impl.model.DependencyDao_Impl;
import java.util.Set;
import java.util.Map;
import java.util.Arrays;
import java.util.HashSet;
import android.arch.persistence.room.util.TableInfo;
import java.util.HashMap;
import android.arch.persistence.room.RoomOpenHelper;
import android.arch.persistence.db.SupportSQLiteOpenHelper;
import android.arch.persistence.room.DatabaseConfiguration;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.InvalidationTracker;
import android.arch.persistence.db.SupportSQLiteDatabase;
import java.util.List;
import androidx.work.impl.model.WorkTagDao;
import androidx.work.impl.model.WorkSpecDao;
import androidx.work.impl.model.WorkNameDao;
import androidx.work.impl.model.SystemIdInfoDao;
import androidx.work.impl.model.DependencyDao;

public class WorkDatabase_Impl extends WorkDatabase
{
    private volatile DependencyDao _dependencyDao;
    private volatile SystemIdInfoDao _systemIdInfoDao;
    private volatile WorkNameDao _workNameDao;
    private volatile WorkSpecDao _workSpecDao;
    private volatile WorkTagDao _workTagDao;
    
    @Override
    protected InvalidationTracker createInvalidationTracker() {
        return new InvalidationTracker(this, new String[] { "Dependency", "WorkSpec", "WorkTag", "SystemIdInfo", "WorkName" });
    }
    
    @Override
    protected SupportSQLiteOpenHelper createOpenHelper(final DatabaseConfiguration databaseConfiguration) {
        return databaseConfiguration.sqliteOpenHelperFactory.create(SupportSQLiteOpenHelper.Configuration.builder(databaseConfiguration.context).name(databaseConfiguration.name).callback(new RoomOpenHelper(databaseConfiguration, (RoomOpenHelper.Delegate)new RoomOpenHelper.Delegate(5) {
            public void createAllTables(final SupportSQLiteDatabase supportSQLiteDatabase) {
                supportSQLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS `Dependency` (`work_spec_id` TEXT NOT NULL, `prerequisite_id` TEXT NOT NULL, PRIMARY KEY(`work_spec_id`, `prerequisite_id`), FOREIGN KEY(`work_spec_id`) REFERENCES `WorkSpec`(`id`) ON UPDATE CASCADE ON DELETE CASCADE , FOREIGN KEY(`prerequisite_id`) REFERENCES `WorkSpec`(`id`) ON UPDATE CASCADE ON DELETE CASCADE )");
                supportSQLiteDatabase.execSQL("CREATE  INDEX `index_Dependency_work_spec_id` ON `Dependency` (`work_spec_id`)");
                supportSQLiteDatabase.execSQL("CREATE  INDEX `index_Dependency_prerequisite_id` ON `Dependency` (`prerequisite_id`)");
                supportSQLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS `WorkSpec` (`id` TEXT NOT NULL, `state` INTEGER NOT NULL, `worker_class_name` TEXT NOT NULL, `input_merger_class_name` TEXT, `input` BLOB NOT NULL, `output` BLOB NOT NULL, `initial_delay` INTEGER NOT NULL, `interval_duration` INTEGER NOT NULL, `flex_duration` INTEGER NOT NULL, `run_attempt_count` INTEGER NOT NULL, `backoff_policy` INTEGER NOT NULL, `backoff_delay_duration` INTEGER NOT NULL, `period_start_time` INTEGER NOT NULL, `minimum_retention_duration` INTEGER NOT NULL, `schedule_requested_at` INTEGER NOT NULL, `required_network_type` INTEGER, `requires_charging` INTEGER NOT NULL, `requires_device_idle` INTEGER NOT NULL, `requires_battery_not_low` INTEGER NOT NULL, `requires_storage_not_low` INTEGER NOT NULL, `trigger_content_update_delay` INTEGER NOT NULL, `trigger_max_content_delay` INTEGER NOT NULL, `content_uri_triggers` BLOB, PRIMARY KEY(`id`))");
                supportSQLiteDatabase.execSQL("CREATE  INDEX `index_WorkSpec_schedule_requested_at` ON `WorkSpec` (`schedule_requested_at`)");
                supportSQLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS `WorkTag` (`tag` TEXT NOT NULL, `work_spec_id` TEXT NOT NULL, PRIMARY KEY(`tag`, `work_spec_id`), FOREIGN KEY(`work_spec_id`) REFERENCES `WorkSpec`(`id`) ON UPDATE CASCADE ON DELETE CASCADE )");
                supportSQLiteDatabase.execSQL("CREATE  INDEX `index_WorkTag_work_spec_id` ON `WorkTag` (`work_spec_id`)");
                supportSQLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS `SystemIdInfo` (`work_spec_id` TEXT NOT NULL, `system_id` INTEGER NOT NULL, PRIMARY KEY(`work_spec_id`), FOREIGN KEY(`work_spec_id`) REFERENCES `WorkSpec`(`id`) ON UPDATE CASCADE ON DELETE CASCADE )");
                supportSQLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS `WorkName` (`name` TEXT NOT NULL, `work_spec_id` TEXT NOT NULL, PRIMARY KEY(`name`, `work_spec_id`), FOREIGN KEY(`work_spec_id`) REFERENCES `WorkSpec`(`id`) ON UPDATE CASCADE ON DELETE CASCADE )");
                supportSQLiteDatabase.execSQL("CREATE  INDEX `index_WorkName_work_spec_id` ON `WorkName` (`work_spec_id`)");
                supportSQLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
                supportSQLiteDatabase.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, \"c84d23ade98552f1cec71088c1f0794c\")");
            }
            
            public void dropAllTables(final SupportSQLiteDatabase supportSQLiteDatabase) {
                supportSQLiteDatabase.execSQL("DROP TABLE IF EXISTS `Dependency`");
                supportSQLiteDatabase.execSQL("DROP TABLE IF EXISTS `WorkSpec`");
                supportSQLiteDatabase.execSQL("DROP TABLE IF EXISTS `WorkTag`");
                supportSQLiteDatabase.execSQL("DROP TABLE IF EXISTS `SystemIdInfo`");
                supportSQLiteDatabase.execSQL("DROP TABLE IF EXISTS `WorkName`");
            }
            
            @Override
            protected void onCreate(final SupportSQLiteDatabase supportSQLiteDatabase) {
                if (WorkDatabase_Impl.this.mCallbacks != null) {
                    for (int i = 0; i < WorkDatabase_Impl.this.mCallbacks.size(); ++i) {
                        ((RoomDatabase.Callback)WorkDatabase_Impl.this.mCallbacks.get(i)).onCreate(supportSQLiteDatabase);
                    }
                }
            }
            
            public void onOpen(final SupportSQLiteDatabase supportSQLiteDatabase) {
                WorkDatabase_Impl.this.mDatabase = supportSQLiteDatabase;
                supportSQLiteDatabase.execSQL("PRAGMA foreign_keys = ON");
                RoomDatabase.this.internalInitInvalidationTracker(supportSQLiteDatabase);
                if (WorkDatabase_Impl.this.mCallbacks != null) {
                    for (int i = 0; i < WorkDatabase_Impl.this.mCallbacks.size(); ++i) {
                        ((RoomDatabase.Callback)WorkDatabase_Impl.this.mCallbacks.get(i)).onOpen(supportSQLiteDatabase);
                    }
                }
            }
            
            @Override
            protected void validateMigration(final SupportSQLiteDatabase supportSQLiteDatabase) {
                final HashMap<String, TableInfo.Column> hashMap = new HashMap<String, TableInfo.Column>(2);
                hashMap.put("work_spec_id", new TableInfo.Column("work_spec_id", "TEXT", true, 1));
                hashMap.put("prerequisite_id", new TableInfo.Column("prerequisite_id", "TEXT", true, 2));
                final HashSet<TableInfo.ForeignKey> set = new HashSet<TableInfo.ForeignKey>(2);
                set.add(new TableInfo.ForeignKey("WorkSpec", "CASCADE", "CASCADE", Arrays.asList("work_spec_id"), Arrays.asList("id")));
                set.add(new TableInfo.ForeignKey("WorkSpec", "CASCADE", "CASCADE", Arrays.asList("prerequisite_id"), Arrays.asList("id")));
                final HashSet<TableInfo.Index> set2 = new HashSet<TableInfo.Index>(2);
                set2.add(new TableInfo.Index("index_Dependency_work_spec_id", false, Arrays.asList("work_spec_id")));
                set2.add(new TableInfo.Index("index_Dependency_prerequisite_id", false, Arrays.asList("prerequisite_id")));
                final TableInfo obj = new TableInfo("Dependency", hashMap, set, set2);
                final TableInfo read = TableInfo.read(supportSQLiteDatabase, "Dependency");
                if (!obj.equals(read)) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("Migration didn't properly handle Dependency(androidx.work.impl.model.Dependency).\n Expected:\n");
                    sb.append(obj);
                    sb.append("\n Found:\n");
                    sb.append(read);
                    throw new IllegalStateException(sb.toString());
                }
                final HashMap<String, TableInfo.Column> hashMap2 = new HashMap<String, TableInfo.Column>(23);
                hashMap2.put("id", new TableInfo.Column("id", "TEXT", true, 1));
                hashMap2.put("state", new TableInfo.Column("state", "INTEGER", true, 0));
                hashMap2.put("worker_class_name", new TableInfo.Column("worker_class_name", "TEXT", true, 0));
                hashMap2.put("input_merger_class_name", new TableInfo.Column("input_merger_class_name", "TEXT", false, 0));
                hashMap2.put("input", new TableInfo.Column("input", "BLOB", true, 0));
                hashMap2.put("output", new TableInfo.Column("output", "BLOB", true, 0));
                hashMap2.put("initial_delay", new TableInfo.Column("initial_delay", "INTEGER", true, 0));
                hashMap2.put("interval_duration", new TableInfo.Column("interval_duration", "INTEGER", true, 0));
                hashMap2.put("flex_duration", new TableInfo.Column("flex_duration", "INTEGER", true, 0));
                hashMap2.put("run_attempt_count", new TableInfo.Column("run_attempt_count", "INTEGER", true, 0));
                hashMap2.put("backoff_policy", new TableInfo.Column("backoff_policy", "INTEGER", true, 0));
                hashMap2.put("backoff_delay_duration", new TableInfo.Column("backoff_delay_duration", "INTEGER", true, 0));
                hashMap2.put("period_start_time", new TableInfo.Column("period_start_time", "INTEGER", true, 0));
                hashMap2.put("minimum_retention_duration", new TableInfo.Column("minimum_retention_duration", "INTEGER", true, 0));
                hashMap2.put("schedule_requested_at", new TableInfo.Column("schedule_requested_at", "INTEGER", true, 0));
                hashMap2.put("required_network_type", new TableInfo.Column("required_network_type", "INTEGER", false, 0));
                hashMap2.put("requires_charging", new TableInfo.Column("requires_charging", "INTEGER", true, 0));
                hashMap2.put("requires_device_idle", new TableInfo.Column("requires_device_idle", "INTEGER", true, 0));
                hashMap2.put("requires_battery_not_low", new TableInfo.Column("requires_battery_not_low", "INTEGER", true, 0));
                hashMap2.put("requires_storage_not_low", new TableInfo.Column("requires_storage_not_low", "INTEGER", true, 0));
                hashMap2.put("trigger_content_update_delay", new TableInfo.Column("trigger_content_update_delay", "INTEGER", true, 0));
                hashMap2.put("trigger_max_content_delay", new TableInfo.Column("trigger_max_content_delay", "INTEGER", true, 0));
                hashMap2.put("content_uri_triggers", new TableInfo.Column("content_uri_triggers", "BLOB", false, 0));
                final HashSet<TableInfo.ForeignKey> set3 = new HashSet<TableInfo.ForeignKey>(0);
                final HashSet<TableInfo.Index> set4 = new HashSet<TableInfo.Index>(1);
                set4.add(new TableInfo.Index("index_WorkSpec_schedule_requested_at", false, Arrays.asList("schedule_requested_at")));
                final TableInfo obj2 = new TableInfo("WorkSpec", hashMap2, set3, set4);
                final TableInfo read2 = TableInfo.read(supportSQLiteDatabase, "WorkSpec");
                if (!obj2.equals(read2)) {
                    final StringBuilder sb2 = new StringBuilder();
                    sb2.append("Migration didn't properly handle WorkSpec(androidx.work.impl.model.WorkSpec).\n Expected:\n");
                    sb2.append(obj2);
                    sb2.append("\n Found:\n");
                    sb2.append(read2);
                    throw new IllegalStateException(sb2.toString());
                }
                final HashMap<String, TableInfo.Column> hashMap3 = new HashMap<String, TableInfo.Column>(2);
                hashMap3.put("tag", new TableInfo.Column("tag", "TEXT", true, 1));
                hashMap3.put("work_spec_id", new TableInfo.Column("work_spec_id", "TEXT", true, 2));
                final HashSet<TableInfo.ForeignKey> set5 = new HashSet<TableInfo.ForeignKey>(1);
                set5.add(new TableInfo.ForeignKey("WorkSpec", "CASCADE", "CASCADE", Arrays.asList("work_spec_id"), Arrays.asList("id")));
                final HashSet<TableInfo.Index> set6 = new HashSet<TableInfo.Index>(1);
                set6.add(new TableInfo.Index("index_WorkTag_work_spec_id", false, Arrays.asList("work_spec_id")));
                final TableInfo obj3 = new TableInfo("WorkTag", hashMap3, set5, set6);
                final TableInfo read3 = TableInfo.read(supportSQLiteDatabase, "WorkTag");
                if (!obj3.equals(read3)) {
                    final StringBuilder sb3 = new StringBuilder();
                    sb3.append("Migration didn't properly handle WorkTag(androidx.work.impl.model.WorkTag).\n Expected:\n");
                    sb3.append(obj3);
                    sb3.append("\n Found:\n");
                    sb3.append(read3);
                    throw new IllegalStateException(sb3.toString());
                }
                final HashMap<String, TableInfo.Column> hashMap4 = new HashMap<String, TableInfo.Column>(2);
                hashMap4.put("work_spec_id", new TableInfo.Column("work_spec_id", "TEXT", true, 1));
                hashMap4.put("system_id", new TableInfo.Column("system_id", "INTEGER", true, 0));
                final HashSet<TableInfo.ForeignKey> set7 = new HashSet<TableInfo.ForeignKey>(1);
                set7.add(new TableInfo.ForeignKey("WorkSpec", "CASCADE", "CASCADE", Arrays.asList("work_spec_id"), Arrays.asList("id")));
                final TableInfo obj4 = new TableInfo("SystemIdInfo", hashMap4, set7, new HashSet<TableInfo.Index>(0));
                final TableInfo read4 = TableInfo.read(supportSQLiteDatabase, "SystemIdInfo");
                if (!obj4.equals(read4)) {
                    final StringBuilder sb4 = new StringBuilder();
                    sb4.append("Migration didn't properly handle SystemIdInfo(androidx.work.impl.model.SystemIdInfo).\n Expected:\n");
                    sb4.append(obj4);
                    sb4.append("\n Found:\n");
                    sb4.append(read4);
                    throw new IllegalStateException(sb4.toString());
                }
                final HashMap<String, TableInfo.Column> hashMap5 = new HashMap<String, TableInfo.Column>(2);
                hashMap5.put("name", new TableInfo.Column("name", "TEXT", true, 1));
                hashMap5.put("work_spec_id", new TableInfo.Column("work_spec_id", "TEXT", true, 2));
                final HashSet<TableInfo.ForeignKey> set8 = new HashSet<TableInfo.ForeignKey>(1);
                set8.add(new TableInfo.ForeignKey("WorkSpec", "CASCADE", "CASCADE", Arrays.asList("work_spec_id"), Arrays.asList("id")));
                final HashSet<TableInfo.Index> set9 = new HashSet<TableInfo.Index>(1);
                set9.add(new TableInfo.Index("index_WorkName_work_spec_id", false, Arrays.asList("work_spec_id")));
                final TableInfo obj5 = new TableInfo("WorkName", hashMap5, set8, set9);
                final TableInfo read5 = TableInfo.read(supportSQLiteDatabase, "WorkName");
                if (obj5.equals(read5)) {
                    return;
                }
                final StringBuilder sb5 = new StringBuilder();
                sb5.append("Migration didn't properly handle WorkName(androidx.work.impl.model.WorkName).\n Expected:\n");
                sb5.append(obj5);
                sb5.append("\n Found:\n");
                sb5.append(read5);
                throw new IllegalStateException(sb5.toString());
            }
        }, "c84d23ade98552f1cec71088c1f0794c", "1db8206f0da6aa81bbdd2d99a82d9e14")).build());
    }
    
    @Override
    public DependencyDao dependencyDao() {
        if (this._dependencyDao != null) {
            return this._dependencyDao;
        }
        synchronized (this) {
            if (this._dependencyDao == null) {
                this._dependencyDao = new DependencyDao_Impl(this);
            }
            return this._dependencyDao;
        }
    }
    
    @Override
    public SystemIdInfoDao systemIdInfoDao() {
        if (this._systemIdInfoDao != null) {
            return this._systemIdInfoDao;
        }
        synchronized (this) {
            if (this._systemIdInfoDao == null) {
                this._systemIdInfoDao = new SystemIdInfoDao_Impl(this);
            }
            return this._systemIdInfoDao;
        }
    }
    
    @Override
    public WorkNameDao workNameDao() {
        if (this._workNameDao != null) {
            return this._workNameDao;
        }
        synchronized (this) {
            if (this._workNameDao == null) {
                this._workNameDao = new WorkNameDao_Impl(this);
            }
            return this._workNameDao;
        }
    }
    
    @Override
    public WorkSpecDao workSpecDao() {
        if (this._workSpecDao != null) {
            return this._workSpecDao;
        }
        synchronized (this) {
            if (this._workSpecDao == null) {
                this._workSpecDao = new WorkSpecDao_Impl(this);
            }
            return this._workSpecDao;
        }
    }
    
    @Override
    public WorkTagDao workTagDao() {
        if (this._workTagDao != null) {
            return this._workTagDao;
        }
        synchronized (this) {
            if (this._workTagDao == null) {
                this._workTagDao = new WorkTagDao_Impl(this);
            }
            return this._workTagDao;
        }
    }
}
