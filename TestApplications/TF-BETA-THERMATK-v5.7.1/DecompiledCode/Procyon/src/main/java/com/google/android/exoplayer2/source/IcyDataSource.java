// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.source;

import com.google.android.exoplayer2.upstream.DataSpec;
import android.net.Uri;
import java.util.List;
import java.util.Map;
import com.google.android.exoplayer2.upstream.TransferListener;
import java.io.IOException;
import com.google.android.exoplayer2.util.ParsableByteArray;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.upstream.DataSource;

final class IcyDataSource implements DataSource
{
    private int bytesUntilMetadata;
    private final Listener listener;
    private final int metadataIntervalBytes;
    private final byte[] metadataLengthByteHolder;
    private final DataSource upstream;
    
    public IcyDataSource(final DataSource upstream, final int n, final Listener listener) {
        Assertions.checkArgument(n > 0);
        this.upstream = upstream;
        this.metadataIntervalBytes = n;
        this.listener = listener;
        this.metadataLengthByteHolder = new byte[1];
        this.bytesUntilMetadata = n;
    }
    
    private boolean readMetadata() throws IOException {
        if (this.upstream.read(this.metadataLengthByteHolder, 0, 1) == -1) {
            return false;
        }
        final int n = (this.metadataLengthByteHolder[0] & 0xFF) << 4;
        if (n == 0) {
            return true;
        }
        final byte[] array = new byte[n];
        int n2 = n;
        int n3 = 0;
        int n4;
        while (true) {
            n4 = n;
            if (n2 <= 0) {
                break;
            }
            final int read = this.upstream.read(array, n3, n2);
            if (read == -1) {
                return false;
            }
            n3 += read;
            n2 -= read;
        }
        while (n4 > 0 && array[n4 - 1] == 0) {
            --n4;
        }
        if (n4 > 0) {
            this.listener.onIcyMetadata(new ParsableByteArray(array, n4));
        }
        return true;
    }
    
    @Override
    public void addTransferListener(final TransferListener transferListener) {
        this.upstream.addTransferListener(transferListener);
    }
    
    @Override
    public void close() throws IOException {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public Map<String, List<String>> getResponseHeaders() {
        return this.upstream.getResponseHeaders();
    }
    
    @Override
    public Uri getUri() {
        return this.upstream.getUri();
    }
    
    @Override
    public long open(final DataSpec dataSpec) throws IOException {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public int read(final byte[] array, int read, final int b) throws IOException {
        if (this.bytesUntilMetadata == 0) {
            if (!this.readMetadata()) {
                return -1;
            }
            this.bytesUntilMetadata = this.metadataIntervalBytes;
        }
        read = this.upstream.read(array, read, Math.min(this.bytesUntilMetadata, b));
        if (read != -1) {
            this.bytesUntilMetadata -= read;
        }
        return read;
    }
    
    public interface Listener
    {
        void onIcyMetadata(final ParsableByteArray p0);
    }
}
