package com.google.android.exoplayer2.upstream;

import com.google.android.exoplayer2.ParserException;
import com.google.android.exoplayer2.upstream.HttpDataSource.InvalidResponseCodeException;
import java.io.FileNotFoundException;
import java.io.IOException;

public class DefaultLoadErrorHandlingPolicy implements LoadErrorHandlingPolicy {
    private final int minimumLoadableRetryCount;

    public DefaultLoadErrorHandlingPolicy() {
        this(-1);
    }

    public DefaultLoadErrorHandlingPolicy(int i) {
        this.minimumLoadableRetryCount = i;
    }

    public long getBlacklistDurationMsFor(int i, long j, IOException iOException, int i2) {
        if (!(iOException instanceof InvalidResponseCodeException)) {
            return -9223372036854775807L;
        }
        i = ((InvalidResponseCodeException) iOException).responseCode;
        if (i == 404 || i == 410) {
            return 60000;
        }
        return -9223372036854775807L;
    }

    public long getRetryDelayMsFor(int i, long j, IOException iOException, int i2) {
        return ((iOException instanceof ParserException) || (iOException instanceof FileNotFoundException)) ? -9223372036854775807L : (long) Math.min((i2 - 1) * 1000, 5000);
    }

    public int getMinimumLoadableRetryCount(int i) {
        int i2 = this.minimumLoadableRetryCount;
        if (i2 != -1) {
            return i2;
        }
        return i == 7 ? 6 : 3;
    }
}
