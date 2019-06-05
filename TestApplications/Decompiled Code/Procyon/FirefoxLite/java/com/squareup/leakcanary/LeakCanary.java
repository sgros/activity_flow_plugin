// 
// Decompiled by Procyon v0.5.34
// 

package com.squareup.leakcanary;

import android.content.Context;
import android.app.Application;

public final class LeakCanary
{
    private LeakCanary() {
        throw new AssertionError();
    }
    
    public static RefWatcher install(final Application application) {
        return RefWatcher.DISABLED;
    }
    
    public static RefWatcher installedRefWatcher() {
        return RefWatcher.DISABLED;
    }
    
    public static boolean isInAnalyzerProcess(final Context context) {
        return false;
    }
}
