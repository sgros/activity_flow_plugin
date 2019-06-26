// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.SQLite;

public class SQLiteException extends Exception
{
    private static final long serialVersionUID = -2398298479089615621L;
    public final int errorCode;
    
    public SQLiteException() {
        this.errorCode = 0;
    }
    
    public SQLiteException(final int errorCode, final String message) {
        super(message);
        this.errorCode = errorCode;
    }
    
    public SQLiteException(final String s) {
        this(0, s);
    }
}
