// 
// Decompiled by Procyon v0.5.34
// 

package android.arch.persistence.db.framework;

import android.database.sqlite.SQLiteDatabase$CursorFactory;
import android.database.sqlite.SQLiteDatabase;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteOpenHelper;
import android.arch.persistence.db.SupportSQLiteDatabase;
import android.content.Context;
import android.arch.persistence.db.SupportSQLiteOpenHelper;

class FrameworkSQLiteOpenHelper implements SupportSQLiteOpenHelper
{
    private final OpenHelper mDelegate;
    
    FrameworkSQLiteOpenHelper(final Context context, final String s, final Callback callback) {
        this.mDelegate = this.createDelegate(context, s, callback);
    }
    
    private OpenHelper createDelegate(final Context context, final String s, final Callback callback) {
        return new OpenHelper(context, s, new FrameworkSQLiteDatabase[1], callback);
    }
    
    @Override
    public SupportSQLiteDatabase getReadableDatabase() {
        return this.mDelegate.getReadableSupportDatabase();
    }
    
    @Override
    public SupportSQLiteDatabase getWritableDatabase() {
        return this.mDelegate.getWritableSupportDatabase();
    }
    
    @Override
    public void setWriteAheadLoggingEnabled(final boolean writeAheadLoggingEnabled) {
        this.mDelegate.setWriteAheadLoggingEnabled(writeAheadLoggingEnabled);
    }
    
    static class OpenHelper extends SQLiteOpenHelper
    {
        final Callback mCallback;
        final FrameworkSQLiteDatabase[] mDbRef;
        private boolean mMigrated;
        
        OpenHelper(final Context context, final String s, final FrameworkSQLiteDatabase[] mDbRef, final Callback mCallback) {
            super(context, s, (SQLiteDatabase$CursorFactory)null, mCallback.version, (DatabaseErrorHandler)new DatabaseErrorHandler() {
                public void onCorruption(final SQLiteDatabase sqLiteDatabase) {
                    final FrameworkSQLiteDatabase frameworkSQLiteDatabase = mDbRef[0];
                    if (frameworkSQLiteDatabase != null) {
                        mCallback.onCorruption(frameworkSQLiteDatabase);
                    }
                }
            });
            this.mCallback = mCallback;
            this.mDbRef = mDbRef;
        }
        
        public void close() {
            synchronized (this) {
                super.close();
                this.mDbRef[0] = null;
            }
        }
        
        SupportSQLiteDatabase getReadableSupportDatabase() {
            synchronized (this) {
                this.mMigrated = false;
                final SQLiteDatabase readableDatabase = super.getReadableDatabase();
                if (this.mMigrated) {
                    this.close();
                    return this.getReadableSupportDatabase();
                }
                return this.getWrappedDb(readableDatabase);
            }
        }
        
        FrameworkSQLiteDatabase getWrappedDb(final SQLiteDatabase sqLiteDatabase) {
            if (this.mDbRef[0] == null) {
                this.mDbRef[0] = new FrameworkSQLiteDatabase(sqLiteDatabase);
            }
            return this.mDbRef[0];
        }
        
        SupportSQLiteDatabase getWritableSupportDatabase() {
            synchronized (this) {
                this.mMigrated = false;
                final SQLiteDatabase writableDatabase = super.getWritableDatabase();
                if (this.mMigrated) {
                    this.close();
                    return this.getWritableSupportDatabase();
                }
                return this.getWrappedDb(writableDatabase);
            }
        }
        
        public void onConfigure(final SQLiteDatabase sqLiteDatabase) {
            this.mCallback.onConfigure(this.getWrappedDb(sqLiteDatabase));
        }
        
        public void onCreate(final SQLiteDatabase sqLiteDatabase) {
            this.mCallback.onCreate(this.getWrappedDb(sqLiteDatabase));
        }
        
        public void onDowngrade(final SQLiteDatabase sqLiteDatabase, final int n, final int n2) {
            this.mMigrated = true;
            this.mCallback.onDowngrade(this.getWrappedDb(sqLiteDatabase), n, n2);
        }
        
        public void onOpen(final SQLiteDatabase sqLiteDatabase) {
            if (!this.mMigrated) {
                this.mCallback.onOpen(this.getWrappedDb(sqLiteDatabase));
            }
        }
        
        public void onUpgrade(final SQLiteDatabase sqLiteDatabase, final int n, final int n2) {
            this.mMigrated = true;
            this.mCallback.onUpgrade(this.getWrappedDb(sqLiteDatabase), n, n2);
        }
    }
}
