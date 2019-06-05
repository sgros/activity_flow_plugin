package org.mozilla.logger;

import android.util.Log;

public class Logger {
    public static void throwOrWarn(boolean z, String str, String str2, RuntimeException runtimeException) {
        if (z) {
            Log.e(str, str2);
        } else if (runtimeException == null) {
            throw new RuntimeException(str2);
        } else {
            throw runtimeException;
        }
    }
}
