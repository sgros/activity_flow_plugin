// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.metadata.scte35;

import java.nio.ByteBuffer;
import com.google.android.exoplayer2.metadata.Metadata;
import com.google.android.exoplayer2.metadata.MetadataInputBuffer;
import com.google.android.exoplayer2.util.TimestampAdjuster;
import com.google.android.exoplayer2.util.ParsableBitArray;
import com.google.android.exoplayer2.util.ParsableByteArray;
import com.google.android.exoplayer2.metadata.MetadataDecoder;

public final class SpliceInfoDecoder implements MetadataDecoder
{
    private final ParsableByteArray sectionData;
    private final ParsableBitArray sectionHeader;
    private TimestampAdjuster timestampAdjuster;
    
    public SpliceInfoDecoder() {
        this.sectionData = new ParsableByteArray();
        this.sectionHeader = new ParsableBitArray();
    }
    
    @Override
    public Metadata decode(final MetadataInputBuffer metadataInputBuffer) {
        final TimestampAdjuster timestampAdjuster = this.timestampAdjuster;
        if (timestampAdjuster == null || metadataInputBuffer.subsampleOffsetUs != timestampAdjuster.getTimestampOffsetUs()) {
            (this.timestampAdjuster = new TimestampAdjuster(metadataInputBuffer.timeUs)).adjustSampleTimestamp(metadataInputBuffer.timeUs - metadataInputBuffer.subsampleOffsetUs);
        }
        final ByteBuffer data = metadataInputBuffer.data;
        final byte[] array = data.array();
        final int limit = data.limit();
        this.sectionData.reset(array, limit);
        this.sectionHeader.reset(array, limit);
        this.sectionHeader.skipBits(39);
        final long n = (long)this.sectionHeader.readBits(1) << 32 | (long)this.sectionHeader.readBits(32);
        this.sectionHeader.skipBits(20);
        final int bits = this.sectionHeader.readBits(12);
        final int bits2 = this.sectionHeader.readBits(8);
        Metadata.Entry entry = null;
        this.sectionData.skipBytes(14);
        if (bits2 != 0) {
            if (bits2 != 255) {
                if (bits2 != 4) {
                    if (bits2 != 5) {
                        if (bits2 == 6) {
                            entry = TimeSignalCommand.parseFromSection(this.sectionData, n, this.timestampAdjuster);
                        }
                    }
                    else {
                        entry = SpliceInsertCommand.parseFromSection(this.sectionData, n, this.timestampAdjuster);
                    }
                }
                else {
                    entry = SpliceScheduleCommand.parseFromSection(this.sectionData);
                }
            }
            else {
                entry = PrivateCommand.parseFromSection(this.sectionData, bits, n);
            }
        }
        else {
            entry = new SpliceNullCommand();
        }
        Metadata metadata;
        if (entry == null) {
            metadata = new Metadata(new Metadata.Entry[0]);
        }
        else {
            metadata = new Metadata(new Metadata.Entry[] { entry });
        }
        return metadata;
    }
}
