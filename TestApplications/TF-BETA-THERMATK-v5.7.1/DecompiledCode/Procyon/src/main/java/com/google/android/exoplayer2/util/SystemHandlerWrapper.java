// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.util;

import android.os.Message;
import android.os.Looper;
import android.os.Handler;

final class SystemHandlerWrapper implements HandlerWrapper
{
    private final Handler handler;
    
    public SystemHandlerWrapper(final Handler handler) {
        this.handler = handler;
    }
    
    @Override
    public Looper getLooper() {
        return this.handler.getLooper();
    }
    
    @Override
    public Message obtainMessage(final int n) {
        return this.handler.obtainMessage(n);
    }
    
    @Override
    public Message obtainMessage(final int n, final int n2, final int n3) {
        return this.handler.obtainMessage(n, n2, n3);
    }
    
    @Override
    public Message obtainMessage(final int n, final int n2, final int n3, final Object o) {
        return this.handler.obtainMessage(n, n2, n3, o);
    }
    
    @Override
    public Message obtainMessage(final int n, final Object o) {
        return this.handler.obtainMessage(n, o);
    }
    
    @Override
    public boolean post(final Runnable runnable) {
        return this.handler.post(runnable);
    }
    
    @Override
    public boolean postDelayed(final Runnable runnable, final long n) {
        return this.handler.postDelayed(runnable, n);
    }
    
    @Override
    public void removeCallbacksAndMessages(final Object o) {
        this.handler.removeCallbacksAndMessages(o);
    }
    
    @Override
    public void removeMessages(final int n) {
        this.handler.removeMessages(n);
    }
    
    @Override
    public boolean sendEmptyMessage(final int n) {
        return this.handler.sendEmptyMessage(n);
    }
    
    @Override
    public boolean sendEmptyMessageAtTime(final int n, final long n2) {
        return this.handler.sendEmptyMessageAtTime(n, n2);
    }
}
