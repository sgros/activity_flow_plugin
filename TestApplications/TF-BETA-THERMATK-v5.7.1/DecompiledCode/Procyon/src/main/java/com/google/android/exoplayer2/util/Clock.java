// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.util;

import android.os.Handler$Callback;
import android.os.Looper;

public interface Clock
{
    public static final Clock DEFAULT = new SystemClock();
    
    HandlerWrapper createHandler(final Looper p0, final Handler$Callback p1);
    
    long elapsedRealtime();
    
    void sleep(final long p0);
    
    long uptimeMillis();
}
