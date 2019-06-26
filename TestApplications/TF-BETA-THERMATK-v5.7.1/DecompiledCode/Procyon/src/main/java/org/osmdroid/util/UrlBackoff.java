// 
// Decompiled by Procyon v0.5.34
// 

package org.osmdroid.util;

import java.util.HashMap;
import java.util.Map;

public class UrlBackoff
{
    private static final long[] mExponentialBackoffDurationInMillisDefault;
    private final Map<String, Delay> mDelays;
    private long[] mExponentialBackoffDurationInMillis;
    
    static {
        mExponentialBackoffDurationInMillisDefault = new long[] { 5000L, 15000L, 60000L, 120000L, 300000L };
    }
    
    public UrlBackoff() {
        this.mExponentialBackoffDurationInMillis = UrlBackoff.mExponentialBackoffDurationInMillisDefault;
        this.mDelays = new HashMap<String, Delay>();
    }
    
    public void next(final String s) {
        synchronized (this.mDelays) {
            final Delay delay = this.mDelays.get(s);
            // monitorexit(this.mDelays)
            if (delay == null) {
                final Delay delay2 = new Delay(this.mExponentialBackoffDurationInMillis);
                final Map<String, Delay> mDelays = this.mDelays;
                synchronized (this.mDelays) {
                    this.mDelays.put(s, delay2);
                    return;
                }
            }
            delay.next();
        }
    }
    
    public Delay remove(final String s) {
        synchronized (this.mDelays) {
            return this.mDelays.remove(s);
        }
    }
    
    public boolean shouldWait(final String s) {
        synchronized (this.mDelays) {
            final Delay delay = this.mDelays.get(s);
            // monitorexit(this.mDelays)
            return delay != null && delay.shouldWait();
        }
    }
}
