// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.upstream;

import android.net.Uri;
import java.util.List;
import java.util.Map;
import java.io.IOException;

public interface DataSource
{
    void addTransferListener(final TransferListener p0);
    
    void close() throws IOException;
    
    Map<String, List<String>> getResponseHeaders();
    
    Uri getUri();
    
    long open(final DataSpec p0) throws IOException;
    
    int read(final byte[] p0, final int p1, final int p2) throws IOException;
    
    public interface Factory
    {
        DataSource createDataSource();
    }
}
