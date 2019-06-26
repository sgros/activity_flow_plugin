// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.util;

import android.os.Handler;
import android.os.Handler$Callback;
import android.os.Looper;

final class SystemClock implements Clock
{
    @Override
    public HandlerWrapper createHandler(final Looper looper, final Handler$Callback handler$Callback) {
        return new SystemHandlerWrapper(new Handler(looper, handler$Callback));
    }
    
    @Override
    public long elapsedRealtime() {
        return android.os.SystemClock.elapsedRealtime();
    }
    
    @Override
    public void sleep(final long n) {
        android.os.SystemClock.sleep(n);
    }
    
    @Override
    public long uptimeMillis() {
        return android.os.SystemClock.uptimeMillis();
    }
}
