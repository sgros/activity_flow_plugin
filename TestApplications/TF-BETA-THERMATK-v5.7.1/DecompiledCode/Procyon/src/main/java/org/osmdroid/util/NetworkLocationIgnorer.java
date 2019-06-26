// 
// Decompiled by Procyon v0.5.34
// 

package org.osmdroid.util;

import org.osmdroid.config.Configuration;

public class NetworkLocationIgnorer
{
    private long mLastGps;
    
    public NetworkLocationIgnorer() {
        this.mLastGps = 0L;
    }
    
    public boolean shouldIgnore(final String anObject, final long mLastGps) {
        if ("gps".equals(anObject)) {
            this.mLastGps = mLastGps;
        }
        else if (mLastGps < this.mLastGps + Configuration.getInstance().getGpsWaitTime()) {
            return true;
        }
        return false;
    }
}
