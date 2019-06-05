// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.focus.utils;

import android.database.Cursor;

public class CursorUtils
{
    public static void closeCursorSafely(final Cursor cursor) {
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
    }
}
