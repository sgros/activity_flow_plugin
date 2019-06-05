package androidx.work.impl.model;

import android.arch.persistence.p000db.SupportSQLiteStatement;
import android.arch.persistence.room.EntityInsertionAdapter;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.RoomSQLiteQuery;
import android.database.Cursor;
import java.util.ArrayList;
import java.util.List;

public class DependencyDao_Impl implements DependencyDao {
    private final RoomDatabase __db;
    private final EntityInsertionAdapter __insertionAdapterOfDependency;

    public DependencyDao_Impl(RoomDatabase roomDatabase) {
        this.__db = roomDatabase;
        this.__insertionAdapterOfDependency = new EntityInsertionAdapter<Dependency>(roomDatabase) {
            public String createQuery() {
                return "INSERT OR IGNORE INTO `Dependency`(`work_spec_id`,`prerequisite_id`) VALUES (?,?)";
            }

            public void bind(SupportSQLiteStatement supportSQLiteStatement, Dependency dependency) {
                if (dependency.workSpecId == null) {
                    supportSQLiteStatement.bindNull(1);
                } else {
                    supportSQLiteStatement.bindString(1, dependency.workSpecId);
                }
                if (dependency.prerequisiteId == null) {
                    supportSQLiteStatement.bindNull(2);
                } else {
                    supportSQLiteStatement.bindString(2, dependency.prerequisiteId);
                }
            }
        };
    }

    public void insertDependency(Dependency dependency) {
        this.__db.beginTransaction();
        try {
            this.__insertionAdapterOfDependency.insert((Object) dependency);
            this.__db.setTransactionSuccessful();
        } finally {
            this.__db.endTransaction();
        }
    }

    public boolean hasCompletedAllPrerequisites(String str) {
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT COUNT(*)=0 FROM dependency WHERE work_spec_id=? AND prerequisite_id IN (SELECT id FROM workspec WHERE state!=2)", 1);
        if (str == null) {
            acquire.bindNull(1);
        } else {
            acquire.bindString(1, str);
        }
        Cursor query = this.__db.query(acquire);
        try {
            boolean z = false;
            if (query.moveToFirst() && query.getInt(0) != 0) {
                z = true;
            }
            query.close();
            acquire.release();
            return z;
        } catch (Throwable th) {
            query.close();
            acquire.release();
        }
    }

    public List<String> getDependentWorkIds(String str) {
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT work_spec_id FROM dependency WHERE prerequisite_id=?", 1);
        if (str == null) {
            acquire.bindNull(1);
        } else {
            acquire.bindString(1, str);
        }
        Cursor query = this.__db.query(acquire);
        try {
            List<String> arrayList = new ArrayList(query.getCount());
            while (query.moveToNext()) {
                arrayList.add(query.getString(0));
            }
            return arrayList;
        } finally {
            query.close();
            acquire.release();
        }
    }

    public boolean hasDependents(String str) {
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT COUNT(*)>0 FROM dependency WHERE prerequisite_id=?", 1);
        if (str == null) {
            acquire.bindNull(1);
        } else {
            acquire.bindString(1, str);
        }
        Cursor query = this.__db.query(acquire);
        try {
            boolean z = false;
            if (query.moveToFirst() && query.getInt(0) != 0) {
                z = true;
            }
            query.close();
            acquire.release();
            return z;
        } catch (Throwable th) {
            query.close();
            acquire.release();
        }
    }
}
