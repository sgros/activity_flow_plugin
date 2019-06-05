package androidx.work.impl;

import android.arch.persistence.p000db.SupportSQLiteDatabase;
import android.arch.persistence.p000db.SupportSQLiteOpenHelper;
import android.arch.persistence.p000db.SupportSQLiteOpenHelper.Configuration;
import android.arch.persistence.room.DatabaseConfiguration;
import android.arch.persistence.room.InvalidationTracker;
import android.arch.persistence.room.RoomDatabase.Callback;
import android.arch.persistence.room.RoomOpenHelper;
import android.arch.persistence.room.RoomOpenHelper.Delegate;
import android.arch.persistence.room.util.TableInfo;
import android.arch.persistence.room.util.TableInfo.Column;
import android.arch.persistence.room.util.TableInfo.ForeignKey;
import android.arch.persistence.room.util.TableInfo.Index;
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

    /* Access modifiers changed, original: protected */
    public SupportSQLiteOpenHelper createOpenHelper(DatabaseConfiguration databaseConfiguration) {
        return databaseConfiguration.sqliteOpenHelperFactory.create(Configuration.builder(databaseConfiguration.context).name(databaseConfiguration.name).callback(new RoomOpenHelper(databaseConfiguration, new Delegate(5) {
            public void createAllTables(SupportSQLiteDatabase supportSQLiteDatabase) {
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

            public void dropAllTables(SupportSQLiteDatabase supportSQLiteDatabase) {
                supportSQLiteDatabase.execSQL("DROP TABLE IF EXISTS `Dependency`");
                supportSQLiteDatabase.execSQL("DROP TABLE IF EXISTS `WorkSpec`");
                supportSQLiteDatabase.execSQL("DROP TABLE IF EXISTS `WorkTag`");
                supportSQLiteDatabase.execSQL("DROP TABLE IF EXISTS `SystemIdInfo`");
                supportSQLiteDatabase.execSQL("DROP TABLE IF EXISTS `WorkName`");
            }

            /* Access modifiers changed, original: protected */
            public void onCreate(SupportSQLiteDatabase supportSQLiteDatabase) {
                if (WorkDatabase_Impl.this.mCallbacks != null) {
                    int size = WorkDatabase_Impl.this.mCallbacks.size();
                    for (int i = 0; i < size; i++) {
                        ((Callback) WorkDatabase_Impl.this.mCallbacks.get(i)).onCreate(supportSQLiteDatabase);
                    }
                }
            }

            public void onOpen(SupportSQLiteDatabase supportSQLiteDatabase) {
                WorkDatabase_Impl.this.mDatabase = supportSQLiteDatabase;
                supportSQLiteDatabase.execSQL("PRAGMA foreign_keys = ON");
                WorkDatabase_Impl.this.internalInitInvalidationTracker(supportSQLiteDatabase);
                if (WorkDatabase_Impl.this.mCallbacks != null) {
                    int size = WorkDatabase_Impl.this.mCallbacks.size();
                    for (int i = 0; i < size; i++) {
                        ((Callback) WorkDatabase_Impl.this.mCallbacks.get(i)).onOpen(supportSQLiteDatabase);
                    }
                }
            }

            /* Access modifiers changed, original: protected */
            public void validateMigration(SupportSQLiteDatabase supportSQLiteDatabase) {
                SupportSQLiteDatabase supportSQLiteDatabase2 = supportSQLiteDatabase;
                HashMap hashMap = new HashMap(2);
                hashMap.put("work_spec_id", new Column("work_spec_id", "TEXT", true, 1));
                hashMap.put("prerequisite_id", new Column("prerequisite_id", "TEXT", true, 2));
                HashSet hashSet = new HashSet(2);
                hashSet.add(new ForeignKey("WorkSpec", "CASCADE", "CASCADE", Arrays.asList(new String[]{"work_spec_id"}), Arrays.asList(new String[]{"id"})));
                hashSet.add(new ForeignKey("WorkSpec", "CASCADE", "CASCADE", Arrays.asList(new String[]{"prerequisite_id"}), Arrays.asList(new String[]{"id"})));
                HashSet hashSet2 = new HashSet(2);
                hashSet2.add(new Index("index_Dependency_work_spec_id", false, Arrays.asList(new String[]{"work_spec_id"})));
                hashSet2.add(new Index("index_Dependency_prerequisite_id", false, Arrays.asList(new String[]{"prerequisite_id"})));
                TableInfo tableInfo = new TableInfo("Dependency", hashMap, hashSet, hashSet2);
                TableInfo read = TableInfo.read(supportSQLiteDatabase2, "Dependency");
                StringBuilder stringBuilder;
                if (tableInfo.equals(read)) {
                    hashMap = new HashMap(23);
                    hashMap.put("id", new Column("id", "TEXT", true, 1));
                    hashMap.put("state", new Column("state", "INTEGER", true, 0));
                    hashMap.put("worker_class_name", new Column("worker_class_name", "TEXT", true, 0));
                    hashMap.put("input_merger_class_name", new Column("input_merger_class_name", "TEXT", false, 0));
                    hashMap.put("input", new Column("input", "BLOB", true, 0));
                    hashMap.put("output", new Column("output", "BLOB", true, 0));
                    hashMap.put("initial_delay", new Column("initial_delay", "INTEGER", true, 0));
                    hashMap.put("interval_duration", new Column("interval_duration", "INTEGER", true, 0));
                    hashMap.put("flex_duration", new Column("flex_duration", "INTEGER", true, 0));
                    hashMap.put("run_attempt_count", new Column("run_attempt_count", "INTEGER", true, 0));
                    hashMap.put("backoff_policy", new Column("backoff_policy", "INTEGER", true, 0));
                    hashMap.put("backoff_delay_duration", new Column("backoff_delay_duration", "INTEGER", true, 0));
                    hashMap.put("period_start_time", new Column("period_start_time", "INTEGER", true, 0));
                    hashMap.put("minimum_retention_duration", new Column("minimum_retention_duration", "INTEGER", true, 0));
                    hashMap.put("schedule_requested_at", new Column("schedule_requested_at", "INTEGER", true, 0));
                    hashMap.put("required_network_type", new Column("required_network_type", "INTEGER", false, 0));
                    hashMap.put("requires_charging", new Column("requires_charging", "INTEGER", true, 0));
                    hashMap.put("requires_device_idle", new Column("requires_device_idle", "INTEGER", true, 0));
                    hashMap.put("requires_battery_not_low", new Column("requires_battery_not_low", "INTEGER", true, 0));
                    hashMap.put("requires_storage_not_low", new Column("requires_storage_not_low", "INTEGER", true, 0));
                    hashMap.put("trigger_content_update_delay", new Column("trigger_content_update_delay", "INTEGER", true, 0));
                    hashMap.put("trigger_max_content_delay", new Column("trigger_max_content_delay", "INTEGER", true, 0));
                    hashMap.put("content_uri_triggers", new Column("content_uri_triggers", "BLOB", false, 0));
                    hashSet = new HashSet(0);
                    hashSet2 = new HashSet(1);
                    hashSet2.add(new Index("index_WorkSpec_schedule_requested_at", false, Arrays.asList(new String[]{"schedule_requested_at"})));
                    tableInfo = new TableInfo("WorkSpec", hashMap, hashSet, hashSet2);
                    read = TableInfo.read(supportSQLiteDatabase2, "WorkSpec");
                    if (tableInfo.equals(read)) {
                        hashMap = new HashMap(2);
                        hashMap.put("tag", new Column("tag", "TEXT", true, 1));
                        hashMap.put("work_spec_id", new Column("work_spec_id", "TEXT", true, 2));
                        hashSet = new HashSet(1);
                        hashSet.add(new ForeignKey("WorkSpec", "CASCADE", "CASCADE", Arrays.asList(new String[]{"work_spec_id"}), Arrays.asList(new String[]{"id"})));
                        hashSet2 = new HashSet(1);
                        hashSet2.add(new Index("index_WorkTag_work_spec_id", false, Arrays.asList(new String[]{"work_spec_id"})));
                        tableInfo = new TableInfo("WorkTag", hashMap, hashSet, hashSet2);
                        read = TableInfo.read(supportSQLiteDatabase2, "WorkTag");
                        if (tableInfo.equals(read)) {
                            hashMap = new HashMap(2);
                            hashMap.put("work_spec_id", new Column("work_spec_id", "TEXT", true, 1));
                            hashMap.put("system_id", new Column("system_id", "INTEGER", true, 0));
                            hashSet = new HashSet(1);
                            hashSet.add(new ForeignKey("WorkSpec", "CASCADE", "CASCADE", Arrays.asList(new String[]{"work_spec_id"}), Arrays.asList(new String[]{"id"})));
                            tableInfo = new TableInfo("SystemIdInfo", hashMap, hashSet, new HashSet(0));
                            read = TableInfo.read(supportSQLiteDatabase2, "SystemIdInfo");
                            if (tableInfo.equals(read)) {
                                hashMap = new HashMap(2);
                                hashMap.put("name", new Column("name", "TEXT", true, 1));
                                hashMap.put("work_spec_id", new Column("work_spec_id", "TEXT", true, 2));
                                HashSet hashSet3 = new HashSet(1);
                                hashSet3.add(new ForeignKey("WorkSpec", "CASCADE", "CASCADE", Arrays.asList(new String[]{"work_spec_id"}), Arrays.asList(new String[]{"id"})));
                                hashSet = new HashSet(1);
                                hashSet.add(new Index("index_WorkName_work_spec_id", false, Arrays.asList(new String[]{"work_spec_id"})));
                                TableInfo tableInfo2 = new TableInfo("WorkName", hashMap, hashSet3, hashSet);
                                TableInfo read2 = TableInfo.read(supportSQLiteDatabase2, "WorkName");
                                if (!tableInfo2.equals(read2)) {
                                    stringBuilder = new StringBuilder();
                                    stringBuilder.append("Migration didn't properly handle WorkName(androidx.work.impl.model.WorkName).\n Expected:\n");
                                    stringBuilder.append(tableInfo2);
                                    stringBuilder.append("\n Found:\n");
                                    stringBuilder.append(read2);
                                    throw new IllegalStateException(stringBuilder.toString());
                                }
                                return;
                            }
                            stringBuilder = new StringBuilder();
                            stringBuilder.append("Migration didn't properly handle SystemIdInfo(androidx.work.impl.model.SystemIdInfo).\n Expected:\n");
                            stringBuilder.append(tableInfo);
                            stringBuilder.append("\n Found:\n");
                            stringBuilder.append(read);
                            throw new IllegalStateException(stringBuilder.toString());
                        }
                        stringBuilder = new StringBuilder();
                        stringBuilder.append("Migration didn't properly handle WorkTag(androidx.work.impl.model.WorkTag).\n Expected:\n");
                        stringBuilder.append(tableInfo);
                        stringBuilder.append("\n Found:\n");
                        stringBuilder.append(read);
                        throw new IllegalStateException(stringBuilder.toString());
                    }
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("Migration didn't properly handle WorkSpec(androidx.work.impl.model.WorkSpec).\n Expected:\n");
                    stringBuilder.append(tableInfo);
                    stringBuilder.append("\n Found:\n");
                    stringBuilder.append(read);
                    throw new IllegalStateException(stringBuilder.toString());
                }
                stringBuilder = new StringBuilder();
                stringBuilder.append("Migration didn't properly handle Dependency(androidx.work.impl.model.Dependency).\n Expected:\n");
                stringBuilder.append(tableInfo);
                stringBuilder.append("\n Found:\n");
                stringBuilder.append(read);
                throw new IllegalStateException(stringBuilder.toString());
            }
        }, "c84d23ade98552f1cec71088c1f0794c", "1db8206f0da6aa81bbdd2d99a82d9e14")).build());
    }

    /* Access modifiers changed, original: protected */
    public InvalidationTracker createInvalidationTracker() {
        return new InvalidationTracker(this, "Dependency", "WorkSpec", "WorkTag", "SystemIdInfo", "WorkName");
    }

    public WorkSpecDao workSpecDao() {
        if (this._workSpecDao != null) {
            return this._workSpecDao;
        }
        WorkSpecDao workSpecDao;
        synchronized (this) {
            if (this._workSpecDao == null) {
                this._workSpecDao = new WorkSpecDao_Impl(this);
            }
            workSpecDao = this._workSpecDao;
        }
        return workSpecDao;
    }

    public DependencyDao dependencyDao() {
        if (this._dependencyDao != null) {
            return this._dependencyDao;
        }
        DependencyDao dependencyDao;
        synchronized (this) {
            if (this._dependencyDao == null) {
                this._dependencyDao = new DependencyDao_Impl(this);
            }
            dependencyDao = this._dependencyDao;
        }
        return dependencyDao;
    }

    public WorkTagDao workTagDao() {
        if (this._workTagDao != null) {
            return this._workTagDao;
        }
        WorkTagDao workTagDao;
        synchronized (this) {
            if (this._workTagDao == null) {
                this._workTagDao = new WorkTagDao_Impl(this);
            }
            workTagDao = this._workTagDao;
        }
        return workTagDao;
    }

    public SystemIdInfoDao systemIdInfoDao() {
        if (this._systemIdInfoDao != null) {
            return this._systemIdInfoDao;
        }
        SystemIdInfoDao systemIdInfoDao;
        synchronized (this) {
            if (this._systemIdInfoDao == null) {
                this._systemIdInfoDao = new SystemIdInfoDao_Impl(this);
            }
            systemIdInfoDao = this._systemIdInfoDao;
        }
        return systemIdInfoDao;
    }

    public WorkNameDao workNameDao() {
        if (this._workNameDao != null) {
            return this._workNameDao;
        }
        WorkNameDao workNameDao;
        synchronized (this) {
            if (this._workNameDao == null) {
                this._workNameDao = new WorkNameDao_Impl(this);
            }
            workNameDao = this._workNameDao;
        }
        return workNameDao;
    }
}
