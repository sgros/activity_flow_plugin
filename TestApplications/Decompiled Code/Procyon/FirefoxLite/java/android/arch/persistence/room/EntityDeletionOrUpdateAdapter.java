// 
// Decompiled by Procyon v0.5.34
// 

package android.arch.persistence.room;

import android.arch.persistence.db.SupportSQLiteStatement;

public abstract class EntityDeletionOrUpdateAdapter<T> extends SharedSQLiteStatement
{
    public EntityDeletionOrUpdateAdapter(final RoomDatabase roomDatabase) {
        super(roomDatabase);
    }
    
    protected abstract void bind(final SupportSQLiteStatement p0, final T p1);
    
    @Override
    protected abstract String createQuery();
    
    public final int handle(final T t) {
        final SupportSQLiteStatement acquire = this.acquire();
        try {
            this.bind(acquire, t);
            return acquire.executeUpdateDelete();
        }
        finally {
            this.release(acquire);
        }
    }
}
