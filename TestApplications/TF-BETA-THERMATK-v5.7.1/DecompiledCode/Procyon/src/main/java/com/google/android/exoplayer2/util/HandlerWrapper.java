// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.util;

import android.os.Message;
import android.os.Looper;

public interface HandlerWrapper
{
    Looper getLooper();
    
    Message obtainMessage(final int p0);
    
    Message obtainMessage(final int p0, final int p1, final int p2);
    
    Message obtainMessage(final int p0, final int p1, final int p2, final Object p3);
    
    Message obtainMessage(final int p0, final Object p1);
    
    boolean post(final Runnable p0);
    
    boolean postDelayed(final Runnable p0, final long p1);
    
    void removeCallbacksAndMessages(final Object p0);
    
    void removeMessages(final int p0);
    
    boolean sendEmptyMessage(final int p0);
    
    boolean sendEmptyMessageAtTime(final int p0, final long p1);
}
