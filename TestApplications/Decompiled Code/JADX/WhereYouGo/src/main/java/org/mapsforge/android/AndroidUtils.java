package org.mapsforge.android;

import android.os.Build;
import android.os.Looper;

public final class AndroidUtils {
    private static final String[] EMULATOR_NAMES = new String[]{"google_sdk", "sdk"};

    public static boolean applicationRunsOnAndroidEmulator() {
        for (Object equals : EMULATOR_NAMES) {
            if (Build.PRODUCT.equals(equals)) {
                return true;
            }
        }
        return false;
    }

    public static boolean currentThreadIsUiThread() {
        return Looper.getMainLooper().getThread() == Thread.currentThread();
    }

    private AndroidUtils() {
        throw new IllegalStateException();
    }
}
