// 
// Decompiled by Procyon v0.5.34
// 

package org.mapsforge.android;

import android.os.Looper;
import android.os.Build;

public final class AndroidUtils
{
    private static final String[] EMULATOR_NAMES;
    
    static {
        EMULATOR_NAMES = new String[] { "google_sdk", "sdk" };
    }
    
    private AndroidUtils() {
        throw new IllegalStateException();
    }
    
    public static boolean applicationRunsOnAndroidEmulator() {
        for (int i = 0; i < AndroidUtils.EMULATOR_NAMES.length; ++i) {
            if (Build.PRODUCT.equals(AndroidUtils.EMULATOR_NAMES[i])) {
                return true;
            }
        }
        return false;
    }
    
    public static boolean currentThreadIsUiThread() {
        return Looper.getMainLooper().getThread() == Thread.currentThread();
    }
}
