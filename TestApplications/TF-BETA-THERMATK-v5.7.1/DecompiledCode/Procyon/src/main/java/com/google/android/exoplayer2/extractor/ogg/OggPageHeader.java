// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.extractor.ogg;

import java.io.IOException;
import java.io.EOFException;
import com.google.android.exoplayer2.ParserException;
import com.google.android.exoplayer2.extractor.ExtractorInput;
import com.google.android.exoplayer2.util.Util;
import com.google.android.exoplayer2.util.ParsableByteArray;

final class OggPageHeader
{
    private static final int TYPE_OGGS;
    public int bodySize;
    public long granulePosition;
    public int headerSize;
    public final int[] laces;
    public long pageChecksum;
    public int pageSegmentCount;
    public long pageSequenceNumber;
    public int revision;
    private final ParsableByteArray scratch;
    public long streamSerialNumber;
    public int type;
    
    static {
        TYPE_OGGS = Util.getIntegerCodeForString("OggS");
    }
    
    OggPageHeader() {
        this.laces = new int[255];
        this.scratch = new ParsableByteArray(255);
    }
    
    public boolean populate(final ExtractorInput extractorInput, final boolean b) throws IOException, InterruptedException {
        this.scratch.reset();
        this.reset();
        final long length = extractorInput.getLength();
        final int n = 0;
        if ((length == -1L || extractorInput.getLength() - extractorInput.getPeekPosition() >= 27L) && extractorInput.peekFully(this.scratch.data, 0, 27, true)) {
            if (this.scratch.readUnsignedInt() != OggPageHeader.TYPE_OGGS) {
                if (b) {
                    return false;
                }
                throw new ParserException("expected OggS capture pattern at begin of page");
            }
            else {
                this.revision = this.scratch.readUnsignedByte();
                if (this.revision == 0) {
                    this.type = this.scratch.readUnsignedByte();
                    this.granulePosition = this.scratch.readLittleEndianLong();
                    this.streamSerialNumber = this.scratch.readLittleEndianUnsignedInt();
                    this.pageSequenceNumber = this.scratch.readLittleEndianUnsignedInt();
                    this.pageChecksum = this.scratch.readLittleEndianUnsignedInt();
                    this.pageSegmentCount = this.scratch.readUnsignedByte();
                    this.headerSize = this.pageSegmentCount + 27;
                    this.scratch.reset();
                    extractorInput.peekFully(this.scratch.data, 0, this.pageSegmentCount);
                    for (int i = n; i < this.pageSegmentCount; ++i) {
                        this.laces[i] = this.scratch.readUnsignedByte();
                        this.bodySize += this.laces[i];
                    }
                    return true;
                }
                if (b) {
                    return false;
                }
                throw new ParserException("unsupported bit stream revision");
            }
        }
        else {
            if (b) {
                return false;
            }
            throw new EOFException();
        }
    }
    
    public void reset() {
        this.revision = 0;
        this.type = 0;
        this.granulePosition = 0L;
        this.streamSerialNumber = 0L;
        this.pageSequenceNumber = 0L;
        this.pageChecksum = 0L;
        this.pageSegmentCount = 0;
        this.headerSize = 0;
        this.bodySize = 0;
    }
}
