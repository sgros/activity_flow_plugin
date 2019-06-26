package net.sqlcipher;

import android.util.Log;
import java.io.File;
import net.sqlcipher.database.SQLiteDatabase;

public final class DefaultDatabaseErrorHandler implements DatabaseErrorHandler {
    private final String TAG = getClass().getSimpleName();

    public void onCorruption(SQLiteDatabase sQLiteDatabase) {
        String str = this.TAG;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Corruption reported by sqlite on database, deleting: ");
        stringBuilder.append(sQLiteDatabase.getPath());
        Log.e(str, stringBuilder.toString());
        if (sQLiteDatabase.isOpen()) {
            Log.e(this.TAG, "Database object for corrupted database is already open, closing");
            try {
                sQLiteDatabase.close();
            } catch (Exception e) {
                Log.e(this.TAG, "Exception closing Database object for corrupted database, ignored", e);
            }
        }
        deleteDatabaseFile(sQLiteDatabase.getPath());
    }

    private void deleteDatabaseFile(String str) {
        if (!str.equalsIgnoreCase(SQLiteDatabase.MEMORY) && str.trim().length() != 0) {
            String str2 = this.TAG;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("deleting the database file: ");
            stringBuilder.append(str);
            Log.e(str2, stringBuilder.toString());
            try {
                new File(str).delete();
            } catch (Exception e) {
                str2 = this.TAG;
                stringBuilder = new StringBuilder();
                stringBuilder.append("delete failed: ");
                stringBuilder.append(e.getMessage());
                Log.w(str2, stringBuilder.toString());
            }
        }
    }
}
