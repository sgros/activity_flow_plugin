package org.osmdroid.util;

import org.osmdroid.config.Configuration;

public class NetworkLocationIgnorer {
    private long mLastGps = 0;

    public boolean shouldIgnore(String str, long j) {
        if ("gps".equals(str)) {
            this.mLastGps = j;
        } else if (j < this.mLastGps + Configuration.getInstance().getGpsWaitTime()) {
            return true;
        }
        return false;
    }
}
