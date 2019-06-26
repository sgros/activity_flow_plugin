// 
// Decompiled by Procyon v0.5.34
// 

package net.sqlcipher.database;

import android.database.Cursor;
import android.os.MemoryFile;
import java.lang.reflect.Method;
import java.io.IOException;
import android.util.Log;
import android.os.ParcelFileDescriptor;
import java.io.FileNotFoundException;
import android.content.res.AssetFileDescriptor;

public class SQLiteContentHelper
{
    public static AssetFileDescriptor getBlobColumnAsAssetFile(final SQLiteDatabase sqLiteDatabase, String simpleQueryForBlobMemoryFile, final String[] array) throws FileNotFoundException {
        try {
            simpleQueryForBlobMemoryFile = (String)simpleQueryForBlobMemoryFile(sqLiteDatabase, simpleQueryForBlobMemoryFile, array);
            if (simpleQueryForBlobMemoryFile == null) {
                throw new FileNotFoundException("No results.");
            }
            final Class<? extends String> class1 = simpleQueryForBlobMemoryFile.getClass();
            ParcelFileDescriptor parcelFileDescriptor;
            try {
                final Method declaredMethod = class1.getDeclaredMethod("getParcelFileDescriptor", (Class<?>[])new Class[0]);
                declaredMethod.setAccessible(true);
                parcelFileDescriptor = (ParcelFileDescriptor)declaredMethod.invoke(simpleQueryForBlobMemoryFile, new Object[0]);
            }
            catch (Exception obj) {
                final StringBuilder sb = new StringBuilder();
                sb.append("SQLiteCursor.java: ");
                sb.append(obj);
                Log.i("SQLiteContentHelper", sb.toString());
                parcelFileDescriptor = null;
            }
            return new AssetFileDescriptor(parcelFileDescriptor, 0L, (long)((MemoryFile)simpleQueryForBlobMemoryFile).length());
        }
        catch (IOException ex) {
            throw new FileNotFoundException(ex.toString());
        }
    }
    
    private static MemoryFile simpleQueryForBlobMemoryFile(SQLiteDatabase rawQuery, final String s, final String[] array) throws IOException {
        rawQuery = (SQLiteDatabase)rawQuery.rawQuery(s, array);
        if (rawQuery == null) {
            return null;
        }
        try {
            if (!((Cursor)rawQuery).moveToFirst()) {
                return null;
            }
            final byte[] blob = ((Cursor)rawQuery).getBlob(0);
            if (blob == null) {
                return null;
            }
            final MemoryFile memoryFile = new MemoryFile((String)null, blob.length);
            memoryFile.writeBytes(blob, 0, 0, blob.length);
            return memoryFile;
        }
        finally {
            ((Cursor)rawQuery).close();
        }
    }
}
