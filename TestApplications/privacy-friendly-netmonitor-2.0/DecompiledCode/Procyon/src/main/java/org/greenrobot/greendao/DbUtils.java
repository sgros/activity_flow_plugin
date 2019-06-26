// 
// Decompiled by Procyon v0.5.34
// 

package org.greenrobot.greendao;

import java.io.ByteArrayOutputStream;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import org.greenrobot.greendao.database.Database;
import android.content.Context;
import java.io.IOException;
import java.io.OutputStream;
import java.io.InputStream;

public class DbUtils
{
    public static int copyAllBytes(final InputStream inputStream, final OutputStream outputStream) throws IOException {
        final byte[] array = new byte[4096];
        int n = 0;
        while (true) {
            final int read = inputStream.read(array);
            if (read == -1) {
                break;
            }
            outputStream.write(array, 0, read);
            n += read;
        }
        return n;
    }
    
    public static int executeSqlScript(final Context context, final Database database, final String s) throws IOException {
        return executeSqlScript(context, database, s, true);
    }
    
    public static int executeSqlScript(final Context context, final Database database, final String str, final boolean b) throws IOException {
        final String[] split = new String(readAsset(context, str), "UTF-8").split(";(\\s)*[\n\r]");
        int i;
        if (b) {
            i = executeSqlStatementsInTx(database, split);
        }
        else {
            i = executeSqlStatements(database, split);
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Executed ");
        sb.append(i);
        sb.append(" statements from SQL script '");
        sb.append(str);
        sb.append("'");
        DaoLog.i(sb.toString());
        return i;
    }
    
    public static int executeSqlStatements(final Database database, final String[] array) {
        int i = 0;
        final int length = array.length;
        int n = 0;
        while (i < length) {
            final String trim = array[i].trim();
            int n2 = n;
            if (trim.length() > 0) {
                database.execSQL(trim);
                n2 = n + 1;
            }
            ++i;
            n = n2;
        }
        return n;
    }
    
    public static int executeSqlStatementsInTx(final Database database, final String[] array) {
        database.beginTransaction();
        try {
            final int executeSqlStatements = executeSqlStatements(database, array);
            database.setTransactionSuccessful();
            return executeSqlStatements;
        }
        finally {
            database.endTransaction();
        }
    }
    
    public static void logTableDump(SQLiteDatabase query, final String s) {
        query = (SQLiteDatabase)query.query(s, (String[])null, (String)null, (String[])null, (String)null, (String)null, (String)null);
        try {
            DaoLog.d(DatabaseUtils.dumpCursorToString((Cursor)query));
        }
        finally {
            ((Cursor)query).close();
        }
    }
    
    public static byte[] readAllBytes(final InputStream inputStream) throws IOException {
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        copyAllBytes(inputStream, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }
    
    public static byte[] readAsset(Context open, final String s) throws IOException {
        open = (Context)open.getResources().getAssets().open(s);
        try {
            return readAllBytes((InputStream)open);
        }
        finally {
            ((InputStream)open).close();
        }
    }
    
    public static void vacuum(final Database database) {
        database.execSQL("VACUUM");
    }
}
