package com.google.android.exoplayer2.extractor.mkv;

import com.google.android.exoplayer2.extractor.ExtractorInput;
import com.google.android.exoplayer2.util.NalUnitUtil;
import com.google.android.exoplayer2.util.ParsableByteArray;
import java.io.IOException;

final class Sniffer {
    private int peekLength;
    private final ParsableByteArray scratch = new ParsableByteArray(8);

    public boolean sniff(ExtractorInput extractorInput) throws IOException, InterruptedException {
        ExtractorInput extractorInput2 = extractorInput;
        long length = extractorInput.getLength();
        long j = 1024;
        if (length != -1 && length <= 1024) {
            j = length;
        }
        int i = (int) j;
        extractorInput2.peekFully(this.scratch.data, 0, 4);
        long readUnsignedInt = this.scratch.readUnsignedInt();
        this.peekLength = 4;
        while (true) {
            boolean z = true;
            if (readUnsignedInt != 440786851) {
                int i2 = this.peekLength + 1;
                this.peekLength = i2;
                if (i2 == i) {
                    return false;
                }
                extractorInput2.peekFully(this.scratch.data, 0, 1);
                readUnsignedInt = ((readUnsignedInt << 8) & -256) | ((long) (this.scratch.data[0] & NalUnitUtil.EXTENDED_SAR));
            } else {
                readUnsignedInt = readUint(extractorInput);
                long j2 = (long) this.peekLength;
                if (readUnsignedInt == Long.MIN_VALUE || (length != -1 && j2 + readUnsignedInt >= length)) {
                    return false;
                }
                while (true) {
                    int i3 = this.peekLength;
                    long j3 = j2 + readUnsignedInt;
                    if (((long) i3) >= j3) {
                        if (((long) i3) != j3) {
                            z = false;
                        }
                        return z;
                    } else if (readUint(extractorInput) == Long.MIN_VALUE) {
                        return false;
                    } else {
                        length = readUint(extractorInput);
                        if (length < 0 || length > 2147483647L) {
                            return false;
                        }
                        if (length != 0) {
                            int i4 = (int) length;
                            extractorInput2.advancePeekPosition(i4);
                            this.peekLength += i4;
                        }
                    }
                }
                return false;
            }
        }
    }

    private long readUint(ExtractorInput extractorInput) throws IOException, InterruptedException {
        int i = 0;
        extractorInput.peekFully(this.scratch.data, 0, 1);
        int i2 = this.scratch.data[0] & NalUnitUtil.EXTENDED_SAR;
        if (i2 == 0) {
            return Long.MIN_VALUE;
        }
        int i3 = 128;
        int i4 = 0;
        while ((i2 & i3) == 0) {
            i3 >>= 1;
            i4++;
        }
        i2 &= i3 ^ -1;
        extractorInput.peekFully(this.scratch.data, 1, i4);
        while (i < i4) {
            i++;
            i2 = (this.scratch.data[i] & NalUnitUtil.EXTENDED_SAR) + (i2 << 8);
        }
        this.peekLength += i4 + 1;
        return (long) i2;
    }
}
