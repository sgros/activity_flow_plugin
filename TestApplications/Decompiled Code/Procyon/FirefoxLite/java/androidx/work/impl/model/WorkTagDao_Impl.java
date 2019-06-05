// 
// Decompiled by Procyon v0.5.34
// 

package androidx.work.impl.model;

import android.database.Cursor;
import java.util.ArrayList;
import android.arch.persistence.db.SupportSQLiteQuery;
import android.arch.persistence.room.RoomSQLiteQuery;
import java.util.List;
import android.arch.persistence.db.SupportSQLiteStatement;
import android.arch.persistence.room.EntityInsertionAdapter;
import android.arch.persistence.room.RoomDatabase;

public class WorkTagDao_Impl implements WorkTagDao
{
    private final RoomDatabase __db;
    private final EntityInsertionAdapter __insertionAdapterOfWorkTag;
    
    public WorkTagDao_Impl(final RoomDatabase _db) {
        this.__db = _db;
        this.__insertionAdapterOfWorkTag = new EntityInsertionAdapter<WorkTag>(_db) {
            public void bind(final SupportSQLiteStatement supportSQLiteStatement, final WorkTag workTag) {
                if (workTag.tag == null) {
                    supportSQLiteStatement.bindNull(1);
                }
                else {
                    supportSQLiteStatement.bindString(1, workTag.tag);
                }
                if (workTag.workSpecId == null) {
                    supportSQLiteStatement.bindNull(2);
                }
                else {
                    supportSQLiteStatement.bindString(2, workTag.workSpecId);
                }
            }
            
            public String createQuery() {
                return "INSERT OR IGNORE INTO `WorkTag`(`tag`,`work_spec_id`) VALUES (?,?)";
            }
        };
    }
    
    @Override
    public List<String> getTagsForWorkSpecId(String query) {
        final RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT DISTINCT tag FROM worktag WHERE work_spec_id=?", 1);
        if (query == null) {
            acquire.bindNull(1);
        }
        else {
            acquire.bindString(1, query);
        }
        query = (String)this.__db.query(acquire);
        try {
            final ArrayList<String> list = new ArrayList<String>(((Cursor)query).getCount());
            while (((Cursor)query).moveToNext()) {
                list.add(((Cursor)query).getString(0));
            }
            return list;
        }
        finally {
            ((Cursor)query).close();
            acquire.release();
        }
    }
    
    @Override
    public void insert(final WorkTag workTag) {
        this.__db.beginTransaction();
        try {
            this.__insertionAdapterOfWorkTag.insert(workTag);
            this.__db.setTransactionSuccessful();
        }
        finally {
            this.__db.endTransaction();
        }
    }
}
