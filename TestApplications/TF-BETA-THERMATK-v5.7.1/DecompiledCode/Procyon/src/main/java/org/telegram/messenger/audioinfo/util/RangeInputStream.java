// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.messenger.audioinfo.util;

import java.io.IOException;
import java.io.InputStream;

public class RangeInputStream extends PositionInputStream
{
    private final long endPosition;
    
    public RangeInputStream(final InputStream inputStream, final long n, final long n2) throws IOException {
        super(inputStream, n);
        this.endPosition = n + n2;
    }
    
    public long getRemainingLength() {
        return this.endPosition - this.getPosition();
    }
    
    @Override
    public int read() throws IOException {
        if (this.getPosition() == this.endPosition) {
            return -1;
        }
        return super.read();
    }
    
    @Override
    public int read(final byte[] array, final int n, int n2) throws IOException {
        final long position = this.getPosition();
        final long n3 = n2;
        final long endPosition = this.endPosition;
        if (position + n3 > endPosition && (n2 = (int)(endPosition - this.getPosition())) == 0) {
            return -1;
        }
        return super.read(array, n, n2);
    }
    
    @Override
    public long skip(final long n) throws IOException {
        final long position = this.getPosition();
        final long endPosition = this.endPosition;
        long n2 = n;
        if (position + n > endPosition) {
            n2 = (int)(endPosition - this.getPosition());
        }
        return super.skip(n2);
    }
}
