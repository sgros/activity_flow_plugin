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

public class DependencyDao_Impl implements DependencyDao
{
    private final RoomDatabase __db;
    private final EntityInsertionAdapter __insertionAdapterOfDependency;
    
    public DependencyDao_Impl(final RoomDatabase _db) {
        this.__db = _db;
        this.__insertionAdapterOfDependency = new EntityInsertionAdapter<Dependency>(_db) {
            public void bind(final SupportSQLiteStatement supportSQLiteStatement, final Dependency dependency) {
                if (dependency.workSpecId == null) {
                    supportSQLiteStatement.bindNull(1);
                }
                else {
                    supportSQLiteStatement.bindString(1, dependency.workSpecId);
                }
                if (dependency.prerequisiteId == null) {
                    supportSQLiteStatement.bindNull(2);
                }
                else {
                    supportSQLiteStatement.bindString(2, dependency.prerequisiteId);
                }
            }
            
            public String createQuery() {
                return "INSERT OR IGNORE INTO `Dependency`(`work_spec_id`,`prerequisite_id`) VALUES (?,?)";
            }
        };
    }
    
    @Override
    public List<String> getDependentWorkIds(String query) {
        final RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT work_spec_id FROM dependency WHERE prerequisite_id=?", 1);
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
    public boolean hasCompletedAllPrerequisites(final String s) {
        final RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT COUNT(*)=0 FROM dependency WHERE work_spec_id=? AND prerequisite_id IN (SELECT id FROM workspec WHERE state!=2)", 1);
        if (s == null) {
            acquire.bindNull(1);
        }
        else {
            acquire.bindString(1, s);
        }
        final Cursor query = this.__db.query(acquire);
        try {
            final boolean moveToFirst = query.moveToFirst();
            boolean b = false;
            if (moveToFirst) {
                final int int1 = query.getInt(0);
                b = b;
                if (int1 != 0) {
                    b = true;
                }
            }
            return b;
        }
        finally {
            query.close();
            acquire.release();
        }
    }
    
    @Override
    public boolean hasDependents(String query) {
        final RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT COUNT(*)>0 FROM dependency WHERE prerequisite_id=?", 1);
        if (query == null) {
            acquire.bindNull(1);
        }
        else {
            acquire.bindString(1, query);
        }
        query = (String)this.__db.query(acquire);
        try {
            final boolean moveToFirst = ((Cursor)query).moveToFirst();
            boolean b = false;
            if (moveToFirst) {
                final int int1 = ((Cursor)query).getInt(0);
                b = b;
                if (int1 != 0) {
                    b = true;
                }
            }
            return b;
        }
        finally {
            ((Cursor)query).close();
            acquire.release();
        }
    }
    
    @Override
    public void insertDependency(final Dependency dependency) {
        this.__db.beginTransaction();
        try {
            this.__insertionAdapterOfDependency.insert(dependency);
            this.__db.setTransactionSuccessful();
        }
        finally {
            this.__db.endTransaction();
        }
    }
}
