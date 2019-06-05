// 
// Decompiled by Procyon v0.5.34
// 

package net.sqlcipher.database;

import net.sqlcipher.Cursor;
import android.content.ContentValues;
import android.util.Log;
import android.net.Uri;
import android.content.ContentResolver;
import android.widget.Toast;
import android.content.Context;

public final class SqliteWrapper
{
    private static final String SQLITE_EXCEPTION_DETAIL_MESSAGE = "unable to open database file";
    private static final String TAG = "SqliteWrapper";
    
    private SqliteWrapper() {
    }
    
    public static void checkSQLiteException(final Context context, final SQLiteException ex) {
        if (isLowMemory(ex)) {
            Toast.makeText(context, (CharSequence)ex.getMessage(), 0).show();
            return;
        }
        throw ex;
    }
    
    public static int delete(final Context context, final ContentResolver contentResolver, final Uri uri, final String s, final String[] array) {
        try {
            return contentResolver.delete(uri, s, array);
        }
        catch (SQLiteException ex) {
            Log.e("SqliteWrapper", "Catch a SQLiteException when delete: ", (Throwable)ex);
            checkSQLiteException(context, ex);
            return -1;
        }
    }
    
    public static Uri insert(final Context context, final ContentResolver contentResolver, final Uri uri, final ContentValues contentValues) {
        try {
            return contentResolver.insert(uri, contentValues);
        }
        catch (SQLiteException ex) {
            Log.e("SqliteWrapper", "Catch a SQLiteException when insert: ", (Throwable)ex);
            checkSQLiteException(context, ex);
            return null;
        }
    }
    
    private static boolean isLowMemory(final SQLiteException ex) {
        return ex.getMessage().equals("unable to open database file");
    }
    
    public static Cursor query(final Context context, final ContentResolver contentResolver, final Uri uri, final String[] array, final String s, final String[] array2, final String s2) {
        try {
            return (Cursor)contentResolver.query(uri, array, s, array2, s2);
        }
        catch (SQLiteException ex) {
            Log.e("SqliteWrapper", "Catch a SQLiteException when query: ", (Throwable)ex);
            checkSQLiteException(context, ex);
            return null;
        }
    }
    
    public static boolean requery(final Context context, final android.database.Cursor cursor) {
        try {
            return cursor.requery();
        }
        catch (SQLiteException ex) {
            Log.e("SqliteWrapper", "Catch a SQLiteException when requery: ", (Throwable)ex);
            checkSQLiteException(context, ex);
            return false;
        }
    }
    
    public static int update(final Context context, final ContentResolver contentResolver, final Uri uri, final ContentValues contentValues, final String s, final String[] array) {
        try {
            return contentResolver.update(uri, contentValues, s, array);
        }
        catch (SQLiteException ex) {
            Log.e("SqliteWrapper", "Catch a SQLiteException when update: ", (Throwable)ex);
            checkSQLiteException(context, ex);
            return -1;
        }
    }
}
