// 
// Decompiled by Procyon v0.5.34
// 

package android.arch.persistence.room;

import android.arch.persistence.db.SupportSQLiteStatement;

public abstract class EntityInsertionAdapter<T> extends SharedSQLiteStatement
{
    public EntityInsertionAdapter(final RoomDatabase roomDatabase) {
        super(roomDatabase);
    }
    
    protected abstract void bind(final SupportSQLiteStatement p0, final T p1);
    
    public final void insert(final T t) {
        final SupportSQLiteStatement acquire = this.acquire();
        try {
            this.bind(acquire, t);
            acquire.executeInsert();
        }
        finally {
            this.release(acquire);
        }
    }
    
    public final void insert(final T[] array) {
        final SupportSQLiteStatement acquire = this.acquire();
        try {
            for (int length = array.length, i = 0; i < length; ++i) {
                this.bind(acquire, array[i]);
                acquire.executeInsert();
            }
        }
        finally {
            this.release(acquire);
        }
    }
}
