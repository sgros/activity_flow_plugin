package com.squareup.leakcanary;

import android.app.Application;
import android.content.Context;

public final class LeakCanary {
    public static boolean isInAnalyzerProcess(Context context) {
        return false;
    }

    public static RefWatcher install(Application application) {
        return RefWatcher.DISABLED;
    }

    public static RefWatcher installedRefWatcher() {
        return RefWatcher.DISABLED;
    }

    private LeakCanary() {
        throw new AssertionError();
    }
}
