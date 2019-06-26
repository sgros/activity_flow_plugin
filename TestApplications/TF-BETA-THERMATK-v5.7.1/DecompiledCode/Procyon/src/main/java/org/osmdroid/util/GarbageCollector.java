// 
// Decompiled by Procyon v0.5.34
// 

package org.osmdroid.util;

import java.util.concurrent.atomic.AtomicBoolean;

public class GarbageCollector
{
    private final Runnable mAction;
    private final AtomicBoolean mRunning;
    
    public GarbageCollector(final Runnable mAction) {
        this.mRunning = new AtomicBoolean(false);
        this.mAction = mAction;
    }
    
    public boolean gc() {
        if (this.mRunning.getAndSet(true)) {
            return false;
        }
        final Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    GarbageCollector.this.mAction.run();
                }
                finally {
                    GarbageCollector.this.mRunning.set(false);
                }
            }
        });
        thread.setPriority(1);
        thread.start();
        return true;
    }
    
    public boolean isRunning() {
        return this.mRunning.get();
    }
}
