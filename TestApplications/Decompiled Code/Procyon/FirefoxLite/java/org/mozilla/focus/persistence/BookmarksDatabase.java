// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.focus.persistence;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.arch.persistence.room.RoomDatabase;

public abstract class BookmarksDatabase extends RoomDatabase
{
    private static volatile BookmarksDatabase instance;
    
    public static BookmarksDatabase getInstance(final Context context) {
        if (BookmarksDatabase.instance == null) {
            synchronized (BookmarksDatabase.class) {
                if (BookmarksDatabase.instance == null) {
                    BookmarksDatabase.instance = Room.databaseBuilder(context.getApplicationContext(), BookmarksDatabase.class, "bookmarks.db").build();
                }
            }
        }
        return BookmarksDatabase.instance;
    }
    
    public abstract BookmarkDao bookmarkDao();
}
