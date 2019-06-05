// 
// Decompiled by Procyon v0.5.34
// 

package androidx.work.impl;

import androidx.work.impl.model.WorkTagDao;
import androidx.work.impl.model.WorkSpecDao;
import androidx.work.impl.model.WorkNameDao;
import androidx.work.impl.model.SystemIdInfoDao;
import androidx.work.impl.model.DependencyDao;
import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.migration.Migration;
import android.arch.persistence.room.Room;
import android.content.Context;
import java.util.concurrent.TimeUnit;
import android.arch.persistence.room.RoomDatabase;

public abstract class WorkDatabase extends RoomDatabase
{
    private static final long PRUNE_THRESHOLD_MILLIS;
    
    static {
        PRUNE_THRESHOLD_MILLIS = TimeUnit.DAYS.toMillis(7L);
    }
    
    public static WorkDatabase create(final Context context, final boolean b) {
        Object o;
        if (b) {
            o = Room.inMemoryDatabaseBuilder(context, WorkDatabase.class).allowMainThreadQueries();
        }
        else {
            o = Room.databaseBuilder(context, WorkDatabase.class, "androidx.work.workdb");
        }
        return ((Builder<WorkDatabase>)o).addCallback(generateCleanupCallback()).addMigrations(WorkDatabaseMigrations.MIGRATION_1_2).addMigrations(new WorkDatabaseMigrations.WorkMigration(context, 2, 3)).addMigrations(WorkDatabaseMigrations.MIGRATION_3_4).addMigrations(WorkDatabaseMigrations.MIGRATION_4_5).fallbackToDestructiveMigration().build();
    }
    
    static Callback generateCleanupCallback() {
        return new Callback() {
            @Override
            public void onOpen(final SupportSQLiteDatabase supportSQLiteDatabase) {
                super.onOpen(supportSQLiteDatabase);
                supportSQLiteDatabase.beginTransaction();
                try {
                    supportSQLiteDatabase.execSQL("UPDATE workspec SET state=0, schedule_requested_at=-1 WHERE state=1");
                    supportSQLiteDatabase.execSQL(WorkDatabase.getPruneSQL());
                    supportSQLiteDatabase.setTransactionSuccessful();
                }
                finally {
                    supportSQLiteDatabase.endTransaction();
                }
            }
        };
    }
    
    static long getPruneDate() {
        return System.currentTimeMillis() - WorkDatabase.PRUNE_THRESHOLD_MILLIS;
    }
    
    static String getPruneSQL() {
        final StringBuilder sb = new StringBuilder();
        sb.append("DELETE FROM workspec WHERE state IN (2, 3, 5) AND (period_start_time + minimum_retention_duration) < ");
        sb.append(getPruneDate());
        sb.append(" AND (SELECT COUNT(*)=0 FROM dependency WHERE     prerequisite_id=id AND     work_spec_id NOT IN         (SELECT id FROM workspec WHERE state IN (2, 3, 5)))");
        return sb.toString();
    }
    
    public abstract DependencyDao dependencyDao();
    
    public abstract SystemIdInfoDao systemIdInfoDao();
    
    public abstract WorkNameDao workNameDao();
    
    public abstract WorkSpecDao workSpecDao();
    
    public abstract WorkTagDao workTagDao();
}
