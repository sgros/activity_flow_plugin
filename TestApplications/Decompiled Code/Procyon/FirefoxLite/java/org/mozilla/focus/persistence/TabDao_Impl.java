// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.focus.persistence;

import android.database.Cursor;
import java.util.ArrayList;
import android.arch.persistence.db.SupportSQLiteQuery;
import android.arch.persistence.room.RoomSQLiteQuery;
import java.util.List;
import android.arch.persistence.db.SupportSQLiteStatement;
import android.arch.persistence.room.SharedSQLiteStatement;
import android.arch.persistence.room.EntityInsertionAdapter;
import android.arch.persistence.room.EntityDeletionOrUpdateAdapter;
import android.arch.persistence.room.RoomDatabase;

public class TabDao_Impl extends TabDao
{
    private final RoomDatabase __db;
    private final EntityDeletionOrUpdateAdapter __deletionAdapterOfTabEntity;
    private final EntityInsertionAdapter __insertionAdapterOfTabEntity;
    private final SharedSQLiteStatement __preparedStmtOfDeleteAllTabs;
    
    public TabDao_Impl(final RoomDatabase _db) {
        this.__db = _db;
        this.__insertionAdapterOfTabEntity = new EntityInsertionAdapter<TabEntity>(_db) {
            public void bind(final SupportSQLiteStatement supportSQLiteStatement, final TabEntity tabEntity) {
                if (tabEntity.getId() == null) {
                    supportSQLiteStatement.bindNull(1);
                }
                else {
                    supportSQLiteStatement.bindString(1, tabEntity.getId());
                }
                if (tabEntity.getParentId() == null) {
                    supportSQLiteStatement.bindNull(2);
                }
                else {
                    supportSQLiteStatement.bindString(2, tabEntity.getParentId());
                }
                if (tabEntity.getTitle() == null) {
                    supportSQLiteStatement.bindNull(3);
                }
                else {
                    supportSQLiteStatement.bindString(3, tabEntity.getTitle());
                }
                if (tabEntity.getUrl() == null) {
                    supportSQLiteStatement.bindNull(4);
                }
                else {
                    supportSQLiteStatement.bindString(4, tabEntity.getUrl());
                }
            }
            
            public String createQuery() {
                return "INSERT OR REPLACE INTO `tabs`(`tab_id`,`tab_parent_id`,`tab_title`,`tab_url`) VALUES (?,?,?,?)";
            }
        };
        this.__deletionAdapterOfTabEntity = new EntityDeletionOrUpdateAdapter<TabEntity>(_db) {
            public void bind(final SupportSQLiteStatement supportSQLiteStatement, final TabEntity tabEntity) {
                if (tabEntity.getId() == null) {
                    supportSQLiteStatement.bindNull(1);
                }
                else {
                    supportSQLiteStatement.bindString(1, tabEntity.getId());
                }
            }
            
            public String createQuery() {
                return "DELETE FROM `tabs` WHERE `tab_id` = ?";
            }
        };
        this.__preparedStmtOfDeleteAllTabs = new SharedSQLiteStatement(_db) {
            public String createQuery() {
                return "DELETE FROM tabs";
            }
        };
    }
    
    @Override
    public void deleteAllTabs() {
        final SupportSQLiteStatement acquire = this.__preparedStmtOfDeleteAllTabs.acquire();
        this.__db.beginTransaction();
        try {
            acquire.executeUpdateDelete();
            this.__db.setTransactionSuccessful();
        }
        finally {
            this.__db.endTransaction();
            this.__preparedStmtOfDeleteAllTabs.release(acquire);
        }
    }
    
    @Override
    public void deleteAllTabsAndInsertTabsInTransaction(final TabEntity... array) {
        this.__db.beginTransaction();
        try {
            super.deleteAllTabsAndInsertTabsInTransaction(array);
            this.__db.setTransactionSuccessful();
        }
        finally {
            this.__db.endTransaction();
        }
    }
    
    @Override
    public List<TabEntity> getTabs() {
        final RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT * FROM tabs", 0);
        final Cursor query = this.__db.query(acquire);
        try {
            final int columnIndexOrThrow = query.getColumnIndexOrThrow("tab_id");
            final int columnIndexOrThrow2 = query.getColumnIndexOrThrow("tab_parent_id");
            final int columnIndexOrThrow3 = query.getColumnIndexOrThrow("tab_title");
            final int columnIndexOrThrow4 = query.getColumnIndexOrThrow("tab_url");
            final ArrayList list = new ArrayList<TabEntity>(query.getCount());
            while (query.moveToNext()) {
                list.add(new TabEntity(query.getString(columnIndexOrThrow), query.getString(columnIndexOrThrow2), query.getString(columnIndexOrThrow3), query.getString(columnIndexOrThrow4)));
            }
            return (List<TabEntity>)list;
        }
        finally {
            query.close();
            acquire.release();
        }
    }
    
    @Override
    public void insertTabs(final TabEntity... array) {
        this.__db.beginTransaction();
        try {
            this.__insertionAdapterOfTabEntity.insert(array);
            this.__db.setTransactionSuccessful();
        }
        finally {
            this.__db.endTransaction();
        }
    }
}
