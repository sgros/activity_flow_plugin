// 
// Decompiled by Procyon v0.5.34
// 

package org.greenrobot.greendao.database;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase$CursorFactory;
import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;

public abstract class DatabaseOpenHelper extends SQLiteOpenHelper
{
    private final Context context;
    private EncryptedHelper encryptedHelper;
    private boolean loadSQLCipherNativeLibs;
    private final String name;
    private final int version;
    
    public DatabaseOpenHelper(final Context context, final String s, final int n) {
        this(context, s, null, n);
    }
    
    public DatabaseOpenHelper(final Context context, final String name, final SQLiteDatabase$CursorFactory sqLiteDatabase$CursorFactory, final int version) {
        super(context, name, sqLiteDatabase$CursorFactory, version);
        this.loadSQLCipherNativeLibs = true;
        this.context = context;
        this.name = name;
        this.version = version;
    }
    
    private EncryptedHelper checkEncryptedHelper() {
        if (this.encryptedHelper == null) {
            this.encryptedHelper = new EncryptedHelper(this.context, this.name, this.version, this.loadSQLCipherNativeLibs);
        }
        return this.encryptedHelper;
    }
    
    public Database getEncryptedReadableDb(final String s) {
        final EncryptedHelper checkEncryptedHelper = this.checkEncryptedHelper();
        return checkEncryptedHelper.wrap(checkEncryptedHelper.getReadableDatabase(s));
    }
    
    public Database getEncryptedReadableDb(final char[] array) {
        final EncryptedHelper checkEncryptedHelper = this.checkEncryptedHelper();
        return checkEncryptedHelper.wrap(checkEncryptedHelper.getReadableDatabase(array));
    }
    
    public Database getEncryptedWritableDb(final String s) {
        final EncryptedHelper checkEncryptedHelper = this.checkEncryptedHelper();
        return checkEncryptedHelper.wrap(checkEncryptedHelper.getWritableDatabase(s));
    }
    
    public Database getEncryptedWritableDb(final char[] array) {
        final EncryptedHelper checkEncryptedHelper = this.checkEncryptedHelper();
        return checkEncryptedHelper.wrap(checkEncryptedHelper.getWritableDatabase(array));
    }
    
    public Database getReadableDb() {
        return this.wrap(this.getReadableDatabase());
    }
    
    public Database getWritableDb() {
        return this.wrap(this.getWritableDatabase());
    }
    
    public void onCreate(final SQLiteDatabase sqLiteDatabase) {
        this.onCreate(this.wrap(sqLiteDatabase));
    }
    
    public void onCreate(final Database database) {
    }
    
    public void onOpen(final SQLiteDatabase sqLiteDatabase) {
        this.onOpen(this.wrap(sqLiteDatabase));
    }
    
    public void onOpen(final Database database) {
    }
    
    public void onUpgrade(final SQLiteDatabase sqLiteDatabase, final int n, final int n2) {
        this.onUpgrade(this.wrap(sqLiteDatabase), n, n2);
    }
    
    public void onUpgrade(final Database database, final int n, final int n2) {
    }
    
    public void setLoadSQLCipherNativeLibs(final boolean loadSQLCipherNativeLibs) {
        this.loadSQLCipherNativeLibs = loadSQLCipherNativeLibs;
    }
    
    protected Database wrap(final SQLiteDatabase sqLiteDatabase) {
        return new StandardDatabase(sqLiteDatabase);
    }
    
    private class EncryptedHelper extends SQLiteOpenHelper
    {
        public EncryptedHelper(final Context context, final String s, final int n, final boolean b) {
            super(context, s, null, n);
            if (b) {
                net.sqlcipher.database.SQLiteDatabase.loadLibs(context);
            }
        }
        
        @Override
        public void onCreate(final net.sqlcipher.database.SQLiteDatabase sqLiteDatabase) {
            DatabaseOpenHelper.this.onCreate(this.wrap(sqLiteDatabase));
        }
        
        @Override
        public void onOpen(final net.sqlcipher.database.SQLiteDatabase sqLiteDatabase) {
            DatabaseOpenHelper.this.onOpen(this.wrap(sqLiteDatabase));
        }
        
        @Override
        public void onUpgrade(final net.sqlcipher.database.SQLiteDatabase sqLiteDatabase, final int n, final int n2) {
            DatabaseOpenHelper.this.onUpgrade(this.wrap(sqLiteDatabase), n, n2);
        }
        
        protected Database wrap(final net.sqlcipher.database.SQLiteDatabase sqLiteDatabase) {
            return new EncryptedDatabase(sqLiteDatabase);
        }
    }
}
