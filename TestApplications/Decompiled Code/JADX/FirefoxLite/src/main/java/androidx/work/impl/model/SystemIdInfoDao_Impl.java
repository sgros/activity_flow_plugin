package androidx.work.impl.model;

import android.arch.persistence.p000db.SupportSQLiteStatement;
import android.arch.persistence.room.EntityInsertionAdapter;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.RoomSQLiteQuery;
import android.arch.persistence.room.SharedSQLiteStatement;
import android.database.Cursor;

public class SystemIdInfoDao_Impl implements SystemIdInfoDao {
    private final RoomDatabase __db;
    private final EntityInsertionAdapter __insertionAdapterOfSystemIdInfo;
    private final SharedSQLiteStatement __preparedStmtOfRemoveSystemIdInfo;

    public SystemIdInfoDao_Impl(RoomDatabase roomDatabase) {
        this.__db = roomDatabase;
        this.__insertionAdapterOfSystemIdInfo = new EntityInsertionAdapter<SystemIdInfo>(roomDatabase) {
            public String createQuery() {
                return "INSERT OR REPLACE INTO `SystemIdInfo`(`work_spec_id`,`system_id`) VALUES (?,?)";
            }

            public void bind(SupportSQLiteStatement supportSQLiteStatement, SystemIdInfo systemIdInfo) {
                if (systemIdInfo.workSpecId == null) {
                    supportSQLiteStatement.bindNull(1);
                } else {
                    supportSQLiteStatement.bindString(1, systemIdInfo.workSpecId);
                }
                supportSQLiteStatement.bindLong(2, (long) systemIdInfo.systemId);
            }
        };
        this.__preparedStmtOfRemoveSystemIdInfo = new SharedSQLiteStatement(roomDatabase) {
            public String createQuery() {
                return "DELETE FROM SystemIdInfo where work_spec_id=?";
            }
        };
    }

    public void insertSystemIdInfo(SystemIdInfo systemIdInfo) {
        this.__db.beginTransaction();
        try {
            this.__insertionAdapterOfSystemIdInfo.insert((Object) systemIdInfo);
            this.__db.setTransactionSuccessful();
        } finally {
            this.__db.endTransaction();
        }
    }

    public void removeSystemIdInfo(String str) {
        SupportSQLiteStatement acquire = this.__preparedStmtOfRemoveSystemIdInfo.acquire();
        this.__db.beginTransaction();
        if (str == null) {
            try {
                acquire.bindNull(1);
            } catch (Throwable th) {
                this.__db.endTransaction();
                this.__preparedStmtOfRemoveSystemIdInfo.release(acquire);
            }
        } else {
            acquire.bindString(1, str);
        }
        acquire.executeUpdateDelete();
        this.__db.setTransactionSuccessful();
        this.__db.endTransaction();
        this.__preparedStmtOfRemoveSystemIdInfo.release(acquire);
    }

    public SystemIdInfo getSystemIdInfo(String str) {
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT * FROM SystemIdInfo WHERE work_spec_id=?", 1);
        if (str == null) {
            acquire.bindNull(1);
        } else {
            acquire.bindString(1, str);
        }
        Cursor query = this.__db.query(acquire);
        try {
            SystemIdInfo systemIdInfo = query.moveToFirst() ? new SystemIdInfo(query.getString(query.getColumnIndexOrThrow("work_spec_id")), query.getInt(query.getColumnIndexOrThrow("system_id"))) : null;
            query.close();
            acquire.release();
            return systemIdInfo;
        } catch (Throwable th) {
            query.close();
            acquire.release();
        }
    }
}
