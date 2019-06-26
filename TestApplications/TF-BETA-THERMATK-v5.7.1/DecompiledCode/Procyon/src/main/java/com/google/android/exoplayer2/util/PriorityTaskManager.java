// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.util;

import java.io.IOException;
import java.util.Collections;
import java.util.PriorityQueue;

public final class PriorityTaskManager
{
    private int highestPriority;
    private final Object lock;
    private final PriorityQueue<Integer> queue;
    
    public PriorityTaskManager() {
        this.lock = new Object();
        this.queue = new PriorityQueue<Integer>(10, Collections.reverseOrder());
        this.highestPriority = Integer.MIN_VALUE;
    }
    
    public void add(final int n) {
        synchronized (this.lock) {
            this.queue.add(n);
            this.highestPriority = Math.max(this.highestPriority, n);
        }
    }
    
    public void proceed(final int n) throws InterruptedException {
        synchronized (this.lock) {
            while (this.highestPriority != n) {
                this.lock.wait();
            }
        }
    }
    
    public boolean proceedNonBlocking(final int n) {
        synchronized (this.lock) {
            return this.highestPriority == n;
        }
    }
    
    public void proceedOrThrow(final int n) throws PriorityTooLowException {
        synchronized (this.lock) {
            if (this.highestPriority == n) {
                return;
            }
            throw new PriorityTooLowException(n, this.highestPriority);
        }
    }
    
    public void remove(int intValue) {
        synchronized (this.lock) {
            this.queue.remove(intValue);
            if (this.queue.isEmpty()) {
                intValue = Integer.MIN_VALUE;
            }
            else {
                final Integer peek = this.queue.peek();
                Util.castNonNull(peek);
                intValue = peek;
            }
            this.highestPriority = intValue;
            this.lock.notifyAll();
        }
    }
    
    public static class PriorityTooLowException extends IOException
    {
        public PriorityTooLowException(final int i, final int j) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Priority too low [priority=");
            sb.append(i);
            sb.append(", highest=");
            sb.append(j);
            sb.append("]");
            super(sb.toString());
        }
    }
}
