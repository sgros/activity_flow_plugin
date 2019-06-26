// 
// Decompiled by Procyon v0.5.34
// 

package net.sqlcipher;

import android.database.CursorWindow;
import android.database.CrossProcessCursor;

public class CrossProcessCursorWrapper extends CursorWrapper implements CrossProcessCursor
{
    public CrossProcessCursorWrapper(final Cursor cursor) {
        super(cursor);
    }
    
    public void fillWindow(final int n, final CursorWindow cursorWindow) {
        DatabaseUtils.cursorFillWindow(this, n, cursorWindow);
    }
    
    public CursorWindow getWindow() {
        return null;
    }
    
    public boolean onMove(final int n, final int n2) {
        return true;
    }
}
