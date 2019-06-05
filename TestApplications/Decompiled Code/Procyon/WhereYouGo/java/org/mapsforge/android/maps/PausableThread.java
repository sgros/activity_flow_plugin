// 
// Decompiled by Procyon v0.5.34
// 

package org.mapsforge.android.maps;

public abstract class PausableThread extends Thread
{
    private boolean pausing;
    private boolean shouldPause;
    
    protected void afterPause() {
    }
    
    protected void afterRun() {
    }
    
    public final void awaitPausing() {
        synchronized (this) {
            while (!this.isInterrupted() && !this.isPausing()) {
                try {
                    this.wait(100L);
                }
                catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }
    // monitorexit(this)
    
    protected abstract void doWork() throws InterruptedException;
    
    protected abstract String getThreadName();
    
    protected abstract ThreadPriority getThreadPriority();
    
    protected abstract boolean hasWork();
    
    @Override
    public void interrupt() {
        synchronized (this) {
            super.interrupt();
        }
    }
    
    public final boolean isPausing() {
        synchronized (this) {
            return this.pausing;
        }
    }
    
    public final void pause() {
        synchronized (this) {
            if (!this.shouldPause) {
                this.shouldPause = true;
                this.notify();
            }
        }
    }
    
    public final void proceed() {
        synchronized (this) {
            if (this.shouldPause) {
                this.shouldPause = false;
                this.pausing = false;
                this.afterPause();
                this.notify();
            }
        }
    }
    
    @Override
    public final void run() {
        this.setName(this.getThreadName());
        this.setPriority(this.getThreadPriority().priority);
        while (!this.isInterrupted()) {
            synchronized (this) {
                while (!this.isInterrupted()) {
                    if (!this.shouldPause) {
                        if (this.hasWork()) {
                            break;
                        }
                    }
                    try {
                        if (this.shouldPause) {
                            this.pausing = true;
                        }
                        this.wait();
                    }
                    catch (InterruptedException ex) {
                        this.interrupt();
                    }
                }
            }
            // monitorexit(this)
            if (this.isInterrupted()) {
                break;
            }
            try {
                this.doWork();
            }
            catch (InterruptedException ex2) {
                this.interrupt();
            }
        }
        this.afterRun();
    }
    
    protected enum ThreadPriority
    {
        ABOVE_NORMAL(7), 
        BELOW_NORMAL(3), 
        HIGHEST(10), 
        LOWEST(1), 
        NORMAL(5);
        
        final int priority;
        
        private ThreadPriority(final int n) {
            if (n < 1 || n > 10) {
                throw new IllegalArgumentException("invalid priority: " + n);
            }
            this.priority = n;
        }
    }
}
