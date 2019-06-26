// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.extractor.mkv;

import java.io.IOException;
import com.google.android.exoplayer2.extractor.ExtractorInput;
import com.google.android.exoplayer2.util.ParsableByteArray;

final class Sniffer
{
    private int peekLength;
    private final ParsableByteArray scratch;
    
    public Sniffer() {
        this.scratch = new ParsableByteArray(8);
    }
    
    private long readUint(final ExtractorInput extractorInput) throws IOException, InterruptedException {
        final byte[] data = this.scratch.data;
        int i = 0;
        extractorInput.peekFully(data, 0, 1);
        final int n = this.scratch.data[0] & 0xFF;
        if (n == 0) {
            return Long.MIN_VALUE;
        }
        int n2;
        int n3;
        for (n2 = 128, n3 = 0; (n & n2) == 0x0; n2 >>= 1, ++n3) {}
        int n4 = n & ~n2;
        extractorInput.peekFully(this.scratch.data, 1, n3);
        while (i < n3) {
            final byte[] data2 = this.scratch.data;
            ++i;
            n4 = (data2[i] & 0xFF) + (n4 << 8);
        }
        this.peekLength += n3 + 1;
        return n4;
    }
    
    public boolean sniff(final ExtractorInput extractorInput) throws IOException, InterruptedException {
        final long length = extractorInput.getLength();
        long n = 1024L;
        if (length != -1L) {
            if (length > 1024L) {
                n = n;
            }
            else {
                n = length;
            }
        }
        final int n2 = (int)n;
        extractorInput.peekFully(this.scratch.data, 0, 4);
        long unsignedInt = this.scratch.readUnsignedInt();
        this.peekLength = 4;
        while (true) {
            boolean b = true;
            if (unsignedInt != 440786851L) {
                if (++this.peekLength == n2) {
                    return false;
                }
                extractorInput.peekFully(this.scratch.data, 0, 1);
                unsignedInt = ((unsignedInt << 8 & 0xFFFFFFFFFFFFFF00L) | (long)(this.scratch.data[0] & 0xFF));
            }
            else {
                final long uint = this.readUint(extractorInput);
                final long n3 = this.peekLength;
                if (uint == Long.MIN_VALUE || (length != -1L && n3 + uint >= length)) {
                    return false;
                }
                while (true) {
                    final int peekLength = this.peekLength;
                    final long n4 = peekLength;
                    final long n5 = n3 + uint;
                    if (n4 >= n5) {
                        if (peekLength != n5) {
                            b = false;
                        }
                        return b;
                    }
                    if (this.readUint(extractorInput) == Long.MIN_VALUE) {
                        return false;
                    }
                    final long uint2 = this.readUint(extractorInput);
                    if (uint2 < 0L || uint2 > 2147483647L) {
                        return false;
                    }
                    if (uint2 == 0L) {
                        continue;
                    }
                    final int n6 = (int)uint2;
                    extractorInput.advancePeekPosition(n6);
                    this.peekLength += n6;
                }
            }
        }
    }
}
