// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.extractor;

import com.google.android.exoplayer2.util.Assertions;
import java.io.IOException;
import java.io.EOFException;
import java.util.Arrays;
import com.google.android.exoplayer2.util.Util;
import com.google.android.exoplayer2.upstream.DataSource;

public final class DefaultExtractorInput implements ExtractorInput
{
    private final DataSource dataSource;
    private byte[] peekBuffer;
    private int peekBufferLength;
    private int peekBufferPosition;
    private long position;
    private final byte[] scratchSpace;
    private final long streamLength;
    
    public DefaultExtractorInput(final DataSource dataSource, final long position, final long streamLength) {
        this.dataSource = dataSource;
        this.position = position;
        this.streamLength = streamLength;
        this.peekBuffer = new byte[65536];
        this.scratchSpace = new byte[4096];
    }
    
    private void commitBytesRead(final int n) {
        if (n != -1) {
            this.position += n;
        }
    }
    
    private void ensureSpaceForPeek(int constrainValue) {
        constrainValue += this.peekBufferPosition;
        final byte[] peekBuffer = this.peekBuffer;
        if (constrainValue > peekBuffer.length) {
            constrainValue = Util.constrainValue(peekBuffer.length * 2, 65536 + constrainValue, constrainValue + 524288);
            this.peekBuffer = Arrays.copyOf(this.peekBuffer, constrainValue);
        }
    }
    
    private int readFromDataSource(final byte[] array, int read, final int n, final int n2, final boolean b) throws InterruptedException, IOException {
        if (Thread.interrupted()) {
            throw new InterruptedException();
        }
        read = this.dataSource.read(array, read + n2, n - n2);
        if (read != -1) {
            return n2 + read;
        }
        if (n2 == 0 && b) {
            return -1;
        }
        throw new EOFException();
    }
    
    private int readFromPeekBuffer(final byte[] array, final int n, int min) {
        final int peekBufferLength = this.peekBufferLength;
        if (peekBufferLength == 0) {
            return 0;
        }
        min = Math.min(peekBufferLength, min);
        System.arraycopy(this.peekBuffer, 0, array, n, min);
        this.updatePeekBuffer(min);
        return min;
    }
    
    private int skipFromPeekBuffer(int min) {
        min = Math.min(this.peekBufferLength, min);
        this.updatePeekBuffer(min);
        return min;
    }
    
    private void updatePeekBuffer(final int n) {
        this.peekBufferLength -= n;
        this.peekBufferPosition = 0;
        final byte[] peekBuffer = this.peekBuffer;
        final int peekBufferLength = this.peekBufferLength;
        byte[] peekBuffer2 = peekBuffer;
        if (peekBufferLength < peekBuffer.length - 524288) {
            peekBuffer2 = new byte[peekBufferLength + 65536];
        }
        System.arraycopy(this.peekBuffer, n, peekBuffer2, 0, this.peekBufferLength);
        this.peekBuffer = peekBuffer2;
    }
    
    @Override
    public void advancePeekPosition(final int n) throws IOException, InterruptedException {
        this.advancePeekPosition(n, false);
    }
    
    @Override
    public boolean advancePeekPosition(final int n, final boolean b) throws IOException, InterruptedException {
        this.ensureSpaceForPeek(n);
        int i = this.peekBufferLength - this.peekBufferPosition;
        while (i < n) {
            i = this.readFromDataSource(this.peekBuffer, this.peekBufferPosition, n, i, b);
            if (i == -1) {
                return false;
            }
            this.peekBufferLength = this.peekBufferPosition + i;
        }
        this.peekBufferPosition += n;
        return true;
    }
    
    @Override
    public long getLength() {
        return this.streamLength;
    }
    
    @Override
    public long getPeekPosition() {
        return this.position + this.peekBufferPosition;
    }
    
    @Override
    public long getPosition() {
        return this.position;
    }
    
    @Override
    public void peekFully(final byte[] array, final int n, final int n2) throws IOException, InterruptedException {
        this.peekFully(array, n, n2, false);
    }
    
    @Override
    public boolean peekFully(final byte[] array, final int n, final int n2, final boolean b) throws IOException, InterruptedException {
        if (!this.advancePeekPosition(n2, b)) {
            return false;
        }
        System.arraycopy(this.peekBuffer, this.peekBufferPosition - n2, array, n, n2);
        return true;
    }
    
    @Override
    public int read(final byte[] array, final int n, final int n2) throws IOException, InterruptedException {
        int n3;
        if ((n3 = this.readFromPeekBuffer(array, n, n2)) == 0) {
            n3 = this.readFromDataSource(array, n, n2, 0, true);
        }
        this.commitBytesRead(n3);
        return n3;
    }
    
    @Override
    public void readFully(final byte[] array, final int n, final int n2) throws IOException, InterruptedException {
        this.readFully(array, n, n2, false);
    }
    
    @Override
    public boolean readFully(final byte[] array, final int n, final int n2, final boolean b) throws IOException, InterruptedException {
        int n3;
        for (n3 = this.readFromPeekBuffer(array, n, n2); n3 < n2 && n3 != -1; n3 = this.readFromDataSource(array, n, n2, n3, b)) {}
        this.commitBytesRead(n3);
        return n3 != -1;
    }
    
    @Override
    public void resetPeekPosition() {
        this.peekBufferPosition = 0;
    }
    
    @Override
    public <E extends Throwable> void setRetryPosition(final long position, final E e) throws E, Throwable {
        Assertions.checkArgument(position >= 0L);
        this.position = position;
        throw e;
    }
    
    @Override
    public int skip(final int a) throws IOException, InterruptedException {
        int n;
        if ((n = this.skipFromPeekBuffer(a)) == 0) {
            final byte[] scratchSpace = this.scratchSpace;
            n = this.readFromDataSource(scratchSpace, 0, Math.min(a, scratchSpace.length), 0, true);
        }
        this.commitBytesRead(n);
        return n;
    }
    
    @Override
    public void skipFully(final int n) throws IOException, InterruptedException {
        this.skipFully(n, false);
    }
    
    public boolean skipFully(final int a, final boolean b) throws IOException, InterruptedException {
        int n;
        for (n = this.skipFromPeekBuffer(a); n < a && n != -1; n = this.readFromDataSource(this.scratchSpace, -n, Math.min(a, this.scratchSpace.length + n), n, b)) {}
        this.commitBytesRead(n);
        return n != -1;
    }
}
