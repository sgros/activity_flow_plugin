// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.util;

import android.os.SystemClock;

public final class ConditionVariable
{
    private boolean isOpen;
    
    public void block() throws InterruptedException {
        synchronized (this) {
            while (!this.isOpen) {
                this.wait();
            }
        }
    }
    
    public boolean block(long elapsedRealtime) throws InterruptedException {
        synchronized (this) {
            final long elapsedRealtime2 = SystemClock.elapsedRealtime();
            long n;
            for (n = elapsedRealtime + elapsedRealtime2, elapsedRealtime = elapsedRealtime2; !this.isOpen && elapsedRealtime < n; elapsedRealtime = SystemClock.elapsedRealtime()) {
                this.wait(n - elapsedRealtime);
            }
            return this.isOpen;
        }
    }
    
    public boolean close() {
        synchronized (this) {
            final boolean isOpen = this.isOpen;
            this.isOpen = false;
            return isOpen;
        }
    }
    
    public boolean open() {
        synchronized (this) {
            if (this.isOpen) {
                return false;
            }
            this.isOpen = true;
            this.notifyAll();
            return true;
        }
    }
}
