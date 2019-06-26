// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.upstream;

import java.io.FileNotFoundException;
import com.google.android.exoplayer2.ParserException;
import java.io.IOException;

public class DefaultLoadErrorHandlingPolicy implements LoadErrorHandlingPolicy
{
    private final int minimumLoadableRetryCount;
    
    public DefaultLoadErrorHandlingPolicy() {
        this(-1);
    }
    
    public DefaultLoadErrorHandlingPolicy(final int minimumLoadableRetryCount) {
        this.minimumLoadableRetryCount = minimumLoadableRetryCount;
    }
    
    @Override
    public long getBlacklistDurationMsFor(int responseCode, long n, final IOException ex, final int n2) {
        final boolean b = ex instanceof HttpDataSource.InvalidResponseCodeException;
        n = -9223372036854775807L;
        if (b) {
            responseCode = ((HttpDataSource.InvalidResponseCodeException)ex).responseCode;
            if (responseCode != 404) {
                n = n;
                if (responseCode != 410) {
                    return n;
                }
            }
            n = 60000L;
        }
        return n;
    }
    
    @Override
    public int getMinimumLoadableRetryCount(int n) {
        final int minimumLoadableRetryCount = this.minimumLoadableRetryCount;
        if (minimumLoadableRetryCount == -1) {
            if (n == 7) {
                n = 6;
            }
            else {
                n = 3;
            }
            return n;
        }
        return minimumLoadableRetryCount;
    }
    
    @Override
    public long getRetryDelayMsFor(final int n, long n2, final IOException ex, final int n3) {
        if (!(ex instanceof ParserException) && !(ex instanceof FileNotFoundException)) {
            n2 = Math.min((n3 - 1) * 1000, 5000);
        }
        else {
            n2 = -9223372036854775807L;
        }
        return n2;
    }
}
