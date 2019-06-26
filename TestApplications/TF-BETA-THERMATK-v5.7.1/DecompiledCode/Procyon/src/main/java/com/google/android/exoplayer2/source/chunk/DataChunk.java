// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.source.chunk;

import com.google.android.exoplayer2.util.Util;
import java.io.IOException;
import java.util.Arrays;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.upstream.DataSpec;
import com.google.android.exoplayer2.upstream.DataSource;

public abstract class DataChunk extends Chunk
{
    private byte[] data;
    private volatile boolean loadCanceled;
    
    public DataChunk(final DataSource dataSource, final DataSpec dataSpec, final int n, final Format format, final int n2, final Object o, final byte[] data) {
        super(dataSource, dataSpec, n, format, n2, o, -9223372036854775807L, -9223372036854775807L);
        this.data = data;
    }
    
    private void maybeExpandData(final int n) {
        final byte[] data = this.data;
        if (data == null) {
            this.data = new byte[16384];
        }
        else if (data.length < n + 16384) {
            this.data = Arrays.copyOf(data, data.length + 16384);
        }
    }
    
    @Override
    public final void cancelLoad() {
        this.loadCanceled = true;
    }
    
    protected abstract void consume(final byte[] p0, final int p1) throws IOException;
    
    public byte[] getDataHolder() {
        return this.data;
    }
    
    @Override
    public final void load() throws IOException, InterruptedException {
        try {
            super.dataSource.open(super.dataSpec);
            int n = 0;
            int n2 = 0;
            while (n != -1 && !this.loadCanceled) {
                this.maybeExpandData(n2);
                final int read = super.dataSource.read(this.data, n2, 16384);
                if ((n = read) != -1) {
                    n2 += read;
                    n = read;
                }
            }
            if (!this.loadCanceled) {
                this.consume(this.data, n2);
            }
        }
        finally {
            Util.closeQuietly(super.dataSource);
        }
    }
}
