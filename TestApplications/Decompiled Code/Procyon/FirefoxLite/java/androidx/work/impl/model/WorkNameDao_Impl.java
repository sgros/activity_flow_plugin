// 
// Decompiled by Procyon v0.5.34
// 

package androidx.work.impl.model;

import android.arch.persistence.db.SupportSQLiteStatement;
import android.arch.persistence.room.EntityInsertionAdapter;
import android.arch.persistence.room.RoomDatabase;

public class WorkNameDao_Impl implements WorkNameDao
{
    private final RoomDatabase __db;
    private final EntityInsertionAdapter __insertionAdapterOfWorkName;
    
    public WorkNameDao_Impl(final RoomDatabase _db) {
        this.__db = _db;
        this.__insertionAdapterOfWorkName = new EntityInsertionAdapter<WorkName>(_db) {
            public void bind(final SupportSQLiteStatement supportSQLiteStatement, final WorkName workName) {
                if (workName.name == null) {
                    supportSQLiteStatement.bindNull(1);
                }
                else {
                    supportSQLiteStatement.bindString(1, workName.name);
                }
                if (workName.workSpecId == null) {
                    supportSQLiteStatement.bindNull(2);
                }
                else {
                    supportSQLiteStatement.bindString(2, workName.workSpecId);
                }
            }
            
            public String createQuery() {
                return "INSERT OR IGNORE INTO `WorkName`(`name`,`work_spec_id`) VALUES (?,?)";
            }
        };
    }
    
    @Override
    public void insert(final WorkName workName) {
        this.__db.beginTransaction();
        try {
            this.__insertionAdapterOfWorkName.insert(workName);
            this.__db.setTransactionSuccessful();
        }
        finally {
            this.__db.endTransaction();
        }
    }
}
