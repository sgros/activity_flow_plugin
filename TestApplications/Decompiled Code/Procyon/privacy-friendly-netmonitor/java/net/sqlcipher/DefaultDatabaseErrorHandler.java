// 
// Decompiled by Procyon v0.5.34
// 

package net.sqlcipher;

import net.sqlcipher.database.SQLiteDatabase;
import java.io.File;
import android.util.Log;

public final class DefaultDatabaseErrorHandler implements DatabaseErrorHandler
{
    private final String TAG;
    
    public DefaultDatabaseErrorHandler() {
        this.TAG = this.getClass().getSimpleName();
    }
    
    private void deleteDatabaseFile(String tag) {
        if (!tag.equalsIgnoreCase(":memory:") && tag.trim().length() != 0) {
            final String tag2 = this.TAG;
            final StringBuilder sb = new StringBuilder();
            sb.append("deleting the database file: ");
            sb.append(tag);
            Log.e(tag2, sb.toString());
            try {
                new File(tag).delete();
            }
            catch (Exception ex) {
                tag = this.TAG;
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("delete failed: ");
                sb2.append(ex.getMessage());
                Log.w(tag, sb2.toString());
            }
        }
    }
    
    @Override
    public void onCorruption(final SQLiteDatabase sqLiteDatabase) {
        final String tag = this.TAG;
        final StringBuilder sb = new StringBuilder();
        sb.append("Corruption reported by sqlite on database, deleting: ");
        sb.append(sqLiteDatabase.getPath());
        Log.e(tag, sb.toString());
        if (sqLiteDatabase.isOpen()) {
            Log.e(this.TAG, "Database object for corrupted database is already open, closing");
            try {
                sqLiteDatabase.close();
            }
            catch (Exception ex) {
                Log.e(this.TAG, "Exception closing Database object for corrupted database, ignored", (Throwable)ex);
            }
        }
        this.deleteDatabaseFile(sqLiteDatabase.getPath());
    }
}
