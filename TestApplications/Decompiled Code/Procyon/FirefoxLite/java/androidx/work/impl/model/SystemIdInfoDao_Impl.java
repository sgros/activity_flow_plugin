// 
// Decompiled by Procyon v0.5.34
// 

package androidx.work.impl.model;

import android.database.Cursor;
import android.arch.persistence.db.SupportSQLiteQuery;
import android.arch.persistence.room.RoomSQLiteQuery;
import android.arch.persistence.db.SupportSQLiteStatement;
import android.arch.persistence.room.SharedSQLiteStatement;
import android.arch.persistence.room.EntityInsertionAdapter;
import android.arch.persistence.room.RoomDatabase;

public class SystemIdInfoDao_Impl implements SystemIdInfoDao
{
    private final RoomDatabase __db;
    private final EntityInsertionAdapter __insertionAdapterOfSystemIdInfo;
    private final SharedSQLiteStatement __preparedStmtOfRemoveSystemIdInfo;
    
    public SystemIdInfoDao_Impl(final RoomDatabase _db) {
        this.__db = _db;
        this.__insertionAdapterOfSystemIdInfo = new EntityInsertionAdapter<SystemIdInfo>(_db) {
            public void bind(final SupportSQLiteStatement supportSQLiteStatement, final SystemIdInfo systemIdInfo) {
                if (systemIdInfo.workSpecId == null) {
                    supportSQLiteStatement.bindNull(1);
                }
                else {
                    supportSQLiteStatement.bindString(1, systemIdInfo.workSpecId);
                }
                supportSQLiteStatement.bindLong(2, systemIdInfo.systemId);
            }
            
            public String createQuery() {
                return "INSERT OR REPLACE INTO `SystemIdInfo`(`work_spec_id`,`system_id`) VALUES (?,?)";
            }
        };
        this.__preparedStmtOfRemoveSystemIdInfo = new SharedSQLiteStatement(_db) {
            public String createQuery() {
                return "DELETE FROM SystemIdInfo where work_spec_id=?";
            }
        };
    }
    
    @Override
    public SystemIdInfo getSystemIdInfo(final String s) {
        final RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT * FROM SystemIdInfo WHERE work_spec_id=?", 1);
        if (s == null) {
            acquire.bindNull(1);
        }
        else {
            acquire.bindString(1, s);
        }
        final Cursor query = this.__db.query(acquire);
        try {
            final int columnIndexOrThrow = query.getColumnIndexOrThrow("work_spec_id");
            final int columnIndexOrThrow2 = query.getColumnIndexOrThrow("system_id");
            SystemIdInfo systemIdInfo;
            if (query.moveToFirst()) {
                systemIdInfo = new SystemIdInfo(query.getString(columnIndexOrThrow), query.getInt(columnIndexOrThrow2));
            }
            else {
                systemIdInfo = null;
            }
            return systemIdInfo;
        }
        finally {
            query.close();
            acquire.release();
        }
    }
    
    @Override
    public void insertSystemIdInfo(final SystemIdInfo systemIdInfo) {
        this.__db.beginTransaction();
        try {
            this.__insertionAdapterOfSystemIdInfo.insert(systemIdInfo);
            this.__db.setTransactionSuccessful();
        }
        finally {
            this.__db.endTransaction();
        }
    }
    
    @Override
    public void removeSystemIdInfo(final String s) {
        final SupportSQLiteStatement acquire = this.__preparedStmtOfRemoveSystemIdInfo.acquire();
        this.__db.beginTransaction();
        Label_0041: {
            Label_0033: {
                if (s == null) {
                    Label_0071: {
                        try {
                            acquire.bindNull(1);
                            break Label_0041;
                        }
                        finally {
                            break Label_0071;
                        }
                        break Label_0033;
                    }
                    this.__db.endTransaction();
                    this.__preparedStmtOfRemoveSystemIdInfo.release(acquire);
                }
            }
            acquire.bindString(1, s);
        }
        acquire.executeUpdateDelete();
        this.__db.setTransactionSuccessful();
        this.__db.endTransaction();
        this.__preparedStmtOfRemoveSystemIdInfo.release(acquire);
    }
}
