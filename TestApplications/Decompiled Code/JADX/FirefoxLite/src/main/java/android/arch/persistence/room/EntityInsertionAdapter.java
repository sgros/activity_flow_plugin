package android.arch.persistence.room;

import android.arch.persistence.p000db.SupportSQLiteStatement;

public abstract class EntityInsertionAdapter<T> extends SharedSQLiteStatement {
    public abstract void bind(SupportSQLiteStatement supportSQLiteStatement, T t);

    public EntityInsertionAdapter(RoomDatabase roomDatabase) {
        super(roomDatabase);
    }

    public final void insert(T t) {
        SupportSQLiteStatement acquire = acquire();
        try {
            bind(acquire, t);
            acquire.executeInsert();
        } finally {
            release(acquire);
        }
    }

    public final void insert(T[] tArr) {
        SupportSQLiteStatement acquire = acquire();
        try {
            for (Object bind : tArr) {
                bind(acquire, bind);
                acquire.executeInsert();
            }
        } finally {
            release(acquire);
        }
    }
}
