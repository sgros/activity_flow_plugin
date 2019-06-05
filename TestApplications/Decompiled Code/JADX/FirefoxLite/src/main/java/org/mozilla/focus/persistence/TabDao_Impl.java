package org.mozilla.focus.persistence;

import android.arch.persistence.p000db.SupportSQLiteStatement;
import android.arch.persistence.room.EntityDeletionOrUpdateAdapter;
import android.arch.persistence.room.EntityInsertionAdapter;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.RoomSQLiteQuery;
import android.arch.persistence.room.SharedSQLiteStatement;
import android.database.Cursor;
import java.util.ArrayList;
import java.util.List;

public class TabDao_Impl extends TabDao {
    private final RoomDatabase __db;
    private final EntityDeletionOrUpdateAdapter __deletionAdapterOfTabEntity;
    private final EntityInsertionAdapter __insertionAdapterOfTabEntity;
    private final SharedSQLiteStatement __preparedStmtOfDeleteAllTabs;

    public TabDao_Impl(RoomDatabase roomDatabase) {
        this.__db = roomDatabase;
        this.__insertionAdapterOfTabEntity = new EntityInsertionAdapter<TabEntity>(roomDatabase) {
            public String createQuery() {
                return "INSERT OR REPLACE INTO `tabs`(`tab_id`,`tab_parent_id`,`tab_title`,`tab_url`) VALUES (?,?,?,?)";
            }

            public void bind(SupportSQLiteStatement supportSQLiteStatement, TabEntity tabEntity) {
                if (tabEntity.getId() == null) {
                    supportSQLiteStatement.bindNull(1);
                } else {
                    supportSQLiteStatement.bindString(1, tabEntity.getId());
                }
                if (tabEntity.getParentId() == null) {
                    supportSQLiteStatement.bindNull(2);
                } else {
                    supportSQLiteStatement.bindString(2, tabEntity.getParentId());
                }
                if (tabEntity.getTitle() == null) {
                    supportSQLiteStatement.bindNull(3);
                } else {
                    supportSQLiteStatement.bindString(3, tabEntity.getTitle());
                }
                if (tabEntity.getUrl() == null) {
                    supportSQLiteStatement.bindNull(4);
                } else {
                    supportSQLiteStatement.bindString(4, tabEntity.getUrl());
                }
            }
        };
        this.__deletionAdapterOfTabEntity = new EntityDeletionOrUpdateAdapter<TabEntity>(roomDatabase) {
            public String createQuery() {
                return "DELETE FROM `tabs` WHERE `tab_id` = ?";
            }

            public void bind(SupportSQLiteStatement supportSQLiteStatement, TabEntity tabEntity) {
                if (tabEntity.getId() == null) {
                    supportSQLiteStatement.bindNull(1);
                } else {
                    supportSQLiteStatement.bindString(1, tabEntity.getId());
                }
            }
        };
        this.__preparedStmtOfDeleteAllTabs = new SharedSQLiteStatement(roomDatabase) {
            public String createQuery() {
                return "DELETE FROM tabs";
            }
        };
    }

    public void insertTabs(TabEntity... tabEntityArr) {
        this.__db.beginTransaction();
        try {
            this.__insertionAdapterOfTabEntity.insert((Object[]) tabEntityArr);
            this.__db.setTransactionSuccessful();
        } finally {
            this.__db.endTransaction();
        }
    }

    public void deleteAllTabsAndInsertTabsInTransaction(TabEntity... tabEntityArr) {
        this.__db.beginTransaction();
        try {
            super.deleteAllTabsAndInsertTabsInTransaction(tabEntityArr);
            this.__db.setTransactionSuccessful();
        } finally {
            this.__db.endTransaction();
        }
    }

    public void deleteAllTabs() {
        SupportSQLiteStatement acquire = this.__preparedStmtOfDeleteAllTabs.acquire();
        this.__db.beginTransaction();
        try {
            acquire.executeUpdateDelete();
            this.__db.setTransactionSuccessful();
        } finally {
            this.__db.endTransaction();
            this.__preparedStmtOfDeleteAllTabs.release(acquire);
        }
    }

    public List<TabEntity> getTabs() {
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT * FROM tabs", 0);
        Cursor query = this.__db.query(acquire);
        try {
            int columnIndexOrThrow = query.getColumnIndexOrThrow("tab_id");
            int columnIndexOrThrow2 = query.getColumnIndexOrThrow("tab_parent_id");
            int columnIndexOrThrow3 = query.getColumnIndexOrThrow("tab_title");
            int columnIndexOrThrow4 = query.getColumnIndexOrThrow("tab_url");
            List<TabEntity> arrayList = new ArrayList(query.getCount());
            while (query.moveToNext()) {
                arrayList.add(new TabEntity(query.getString(columnIndexOrThrow), query.getString(columnIndexOrThrow2), query.getString(columnIndexOrThrow3), query.getString(columnIndexOrThrow4)));
            }
            return arrayList;
        } finally {
            query.close();
            acquire.release();
        }
    }
}
