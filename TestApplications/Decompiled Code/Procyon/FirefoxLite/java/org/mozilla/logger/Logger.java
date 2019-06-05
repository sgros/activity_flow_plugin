// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.logger;

import android.util.Log;

public class Logger
{
    public static void throwOrWarn(final boolean b, final String s, final String message, final RuntimeException ex) {
        if (b) {
            Log.e(s, message);
            return;
        }
        if (ex == null) {
            throw new RuntimeException(message);
        }
        throw ex;
    }
}
