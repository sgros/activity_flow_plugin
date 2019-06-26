// 
// Decompiled by Procyon v0.5.34
// 

package net.sqlcipher;

public class CursorWrapper extends android.database.CursorWrapper implements Cursor
{
    private final Cursor mCursor;
    
    public CursorWrapper(final Cursor mCursor) {
        super((android.database.Cursor)mCursor);
        this.mCursor = mCursor;
    }
    
    public int getType(final int n) {
        return this.mCursor.getType(n);
    }
    
    public Cursor getWrappedCursor() {
        return this.mCursor;
    }
}
