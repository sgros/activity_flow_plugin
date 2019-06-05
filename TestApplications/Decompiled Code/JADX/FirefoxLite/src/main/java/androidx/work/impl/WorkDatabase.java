package androidx.work.impl;

import android.arch.persistence.p000db.SupportSQLiteDatabase;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.RoomDatabase.Builder;
import android.arch.persistence.room.RoomDatabase.Callback;
import android.content.Context;
import androidx.work.impl.WorkDatabaseMigrations.WorkMigration;
import androidx.work.impl.model.DependencyDao;
import androidx.work.impl.model.SystemIdInfoDao;
import androidx.work.impl.model.WorkNameDao;
import androidx.work.impl.model.WorkSpecDao;
import androidx.work.impl.model.WorkTagDao;
import java.util.concurrent.TimeUnit;

public abstract class WorkDatabase extends RoomDatabase {
    private static final long PRUNE_THRESHOLD_MILLIS = TimeUnit.DAYS.toMillis(7);

    /* renamed from: androidx.work.impl.WorkDatabase$1 */
    static class C06521 extends Callback {
        C06521() {
        }

        public void onOpen(SupportSQLiteDatabase supportSQLiteDatabase) {
            super.onOpen(supportSQLiteDatabase);
            supportSQLiteDatabase.beginTransaction();
            try {
                supportSQLiteDatabase.execSQL("UPDATE workspec SET state=0, schedule_requested_at=-1 WHERE state=1");
                supportSQLiteDatabase.execSQL(WorkDatabase.getPruneSQL());
                supportSQLiteDatabase.setTransactionSuccessful();
            } finally {
                supportSQLiteDatabase.endTransaction();
            }
        }
    }

    public abstract DependencyDao dependencyDao();

    public abstract SystemIdInfoDao systemIdInfoDao();

    public abstract WorkNameDao workNameDao();

    public abstract WorkSpecDao workSpecDao();

    public abstract WorkTagDao workTagDao();

    public static WorkDatabase create(Context context, boolean z) {
        Builder allowMainThreadQueries;
        if (z) {
            allowMainThreadQueries = Room.inMemoryDatabaseBuilder(context, WorkDatabase.class).allowMainThreadQueries();
        } else {
            allowMainThreadQueries = Room.databaseBuilder(context, WorkDatabase.class, "androidx.work.workdb");
        }
        return (WorkDatabase) allowMainThreadQueries.addCallback(generateCleanupCallback()).addMigrations(WorkDatabaseMigrations.MIGRATION_1_2).addMigrations(new WorkMigration(context, 2, 3)).addMigrations(WorkDatabaseMigrations.MIGRATION_3_4).addMigrations(WorkDatabaseMigrations.MIGRATION_4_5).fallbackToDestructiveMigration().build();
    }

    static Callback generateCleanupCallback() {
        return new C06521();
    }

    static String getPruneSQL() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("DELETE FROM workspec WHERE state IN (2, 3, 5) AND (period_start_time + minimum_retention_duration) < ");
        stringBuilder.append(getPruneDate());
        stringBuilder.append(" AND (SELECT COUNT(*)=0 FROM dependency WHERE     prerequisite_id=id AND     work_spec_id NOT IN         (SELECT id FROM workspec WHERE state IN (2, 3, 5)))");
        return stringBuilder.toString();
    }

    static long getPruneDate() {
        return System.currentTimeMillis() - PRUNE_THRESHOLD_MILLIS;
    }
}
