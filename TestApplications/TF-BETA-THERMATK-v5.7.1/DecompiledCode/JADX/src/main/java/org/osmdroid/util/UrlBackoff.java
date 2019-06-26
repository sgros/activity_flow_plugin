package org.osmdroid.util;

import java.util.HashMap;
import java.util.Map;

public class UrlBackoff {
    private static final long[] mExponentialBackoffDurationInMillisDefault = new long[]{5000, 15000, 60000, 120000, 300000};
    private final Map<String, Delay> mDelays = new HashMap();
    private long[] mExponentialBackoffDurationInMillis = mExponentialBackoffDurationInMillisDefault;

    public void next(String str) {
        Delay delay;
        synchronized (this.mDelays) {
            delay = (Delay) this.mDelays.get(str);
        }
        if (delay == null) {
            Delay delay2 = new Delay(this.mExponentialBackoffDurationInMillis);
            synchronized (this.mDelays) {
                this.mDelays.put(str, delay2);
            }
            return;
        }
        delay.next();
    }

    public Delay remove(String str) {
        Delay delay;
        synchronized (this.mDelays) {
            delay = (Delay) this.mDelays.remove(str);
        }
        return delay;
    }

    public boolean shouldWait(String str) {
        Delay delay;
        synchronized (this.mDelays) {
            delay = (Delay) this.mDelays.get(str);
        }
        return delay != null && delay.shouldWait();
    }
}
