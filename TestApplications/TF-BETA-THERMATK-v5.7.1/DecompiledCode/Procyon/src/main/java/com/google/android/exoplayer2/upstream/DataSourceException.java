// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.upstream;

import java.io.IOException;

public final class DataSourceException extends IOException
{
    public final int reason;
    
    public DataSourceException(final int reason) {
        this.reason = reason;
    }
}
