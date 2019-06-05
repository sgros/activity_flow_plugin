package android.arch.persistence.room;

import android.arch.persistence.p000db.SupportSQLiteStatement;

public abstract class EntityDeletionOrUpdateAdapter<T> extends SharedSQLiteStatement {
    public abstract void bind(SupportSQLiteStatement supportSQLiteStatement, T t);

    public abstract String createQuery();

    public EntityDeletionOrUpdateAdapter(RoomDatabase roomDatabase) {
        super(roomDatabase);
    }

    public final int handle(T t) {
        SupportSQLiteStatement acquire = acquire();
        try {
            bind(acquire, t);
            int executeUpdateDelete = acquire.executeUpdateDelete();
            return executeUpdateDelete;
        } finally {
            release(acquire);
        }
    }
}
