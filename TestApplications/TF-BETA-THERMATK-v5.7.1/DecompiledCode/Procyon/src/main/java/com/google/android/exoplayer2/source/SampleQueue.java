// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.source;

import java.io.IOException;
import java.io.EOFException;
import com.google.android.exoplayer2.extractor.ExtractorInput;
import com.google.android.exoplayer2.FormatHolder;
import com.google.android.exoplayer2.decoder.CryptoInfo;
import com.google.android.exoplayer2.decoder.DecoderInputBuffer;
import java.nio.ByteBuffer;
import com.google.android.exoplayer2.upstream.Allocation;
import com.google.android.exoplayer2.util.ParsableByteArray;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.upstream.Allocator;
import com.google.android.exoplayer2.extractor.TrackOutput;

public class SampleQueue implements TrackOutput
{
    private final int allocationLength;
    private final Allocator allocator;
    private Format downstreamFormat;
    private final SampleMetadataQueue.SampleExtrasHolder extrasHolder;
    private AllocationNode firstAllocationNode;
    private Format lastUnadjustedFormat;
    private final SampleMetadataQueue metadataQueue;
    private boolean pendingFormatAdjustment;
    private boolean pendingSplice;
    private AllocationNode readAllocationNode;
    private long sampleOffsetUs;
    private final ParsableByteArray scratch;
    private long totalBytesWritten;
    private UpstreamFormatChangedListener upstreamFormatChangeListener;
    private AllocationNode writeAllocationNode;
    
    public SampleQueue(final Allocator allocator) {
        this.allocator = allocator;
        this.allocationLength = allocator.getIndividualAllocationLength();
        this.metadataQueue = new SampleMetadataQueue();
        this.extrasHolder = new SampleMetadataQueue.SampleExtrasHolder();
        this.scratch = new ParsableByteArray(32);
        this.firstAllocationNode = new AllocationNode(0L, this.allocationLength);
        final AllocationNode firstAllocationNode = this.firstAllocationNode;
        this.readAllocationNode = firstAllocationNode;
        this.writeAllocationNode = firstAllocationNode;
    }
    
    private void advanceReadTo(final long n) {
        while (true) {
            final AllocationNode readAllocationNode = this.readAllocationNode;
            if (n < readAllocationNode.endPosition) {
                break;
            }
            this.readAllocationNode = readAllocationNode.next;
        }
    }
    
    private void clearAllocationNodes(AllocationNode clear) {
        if (!clear.wasInitialized) {
            return;
        }
        final AllocationNode writeAllocationNode = this.writeAllocationNode;
        final Allocation[] array = new Allocation[(writeAllocationNode.wasInitialized ? 1 : 0) + (int)(writeAllocationNode.startPosition - clear.startPosition) / this.allocationLength];
        for (int i = 0; i < array.length; ++i) {
            array[i] = clear.allocation;
            clear = clear.clear();
        }
        this.allocator.release(array);
    }
    
    private void discardDownstreamTo(final long n) {
        if (n == -1L) {
            return;
        }
        AllocationNode firstAllocationNode;
        while (true) {
            firstAllocationNode = this.firstAllocationNode;
            if (n < firstAllocationNode.endPosition) {
                break;
            }
            this.allocator.release(firstAllocationNode.allocation);
            this.firstAllocationNode = this.firstAllocationNode.clear();
        }
        if (this.readAllocationNode.startPosition < firstAllocationNode.startPosition) {
            this.readAllocationNode = firstAllocationNode;
        }
    }
    
    private static Format getAdjustedSampleFormat(final Format format, final long n) {
        if (format == null) {
            return null;
        }
        Format copyWithSubsampleOffsetUs = format;
        if (n != 0L) {
            final long subsampleOffsetUs = format.subsampleOffsetUs;
            copyWithSubsampleOffsetUs = format;
            if (subsampleOffsetUs != Long.MAX_VALUE) {
                copyWithSubsampleOffsetUs = format.copyWithSubsampleOffsetUs(subsampleOffsetUs + n);
            }
        }
        return copyWithSubsampleOffsetUs;
    }
    
    private void postAppend(final int n) {
        this.totalBytesWritten += n;
        final long totalBytesWritten = this.totalBytesWritten;
        final AllocationNode writeAllocationNode = this.writeAllocationNode;
        if (totalBytesWritten == writeAllocationNode.endPosition) {
            this.writeAllocationNode = writeAllocationNode.next;
        }
    }
    
    private int preAppend(final int a) {
        final AllocationNode writeAllocationNode = this.writeAllocationNode;
        if (!writeAllocationNode.wasInitialized) {
            writeAllocationNode.initialize(this.allocator.allocate(), new AllocationNode(this.writeAllocationNode.endPosition, this.allocationLength));
        }
        return Math.min(a, (int)(this.writeAllocationNode.endPosition - this.totalBytesWritten));
    }
    
    private void readData(long n, final ByteBuffer byteBuffer, int i) {
        this.advanceReadTo(n);
        while (i > 0) {
            final int min = Math.min(i, (int)(this.readAllocationNode.endPosition - n));
            final AllocationNode readAllocationNode = this.readAllocationNode;
            byteBuffer.put(readAllocationNode.allocation.data, readAllocationNode.translateOffset(n), min);
            final int n2 = i - min;
            final long n3 = n + min;
            final AllocationNode readAllocationNode2 = this.readAllocationNode;
            n = n3;
            i = n2;
            if (n3 == readAllocationNode2.endPosition) {
                this.readAllocationNode = readAllocationNode2.next;
                n = n3;
                i = n2;
            }
        }
    }
    
    private void readData(long n, final byte[] array, final int n2) {
        this.advanceReadTo(n);
        int n3;
        for (int i = n2; i > 0; i = n3) {
            final int min = Math.min(i, (int)(this.readAllocationNode.endPosition - n));
            final AllocationNode readAllocationNode = this.readAllocationNode;
            System.arraycopy(readAllocationNode.allocation.data, readAllocationNode.translateOffset(n), array, n2 - i, min);
            n3 = i - min;
            final long n4 = n + min;
            final AllocationNode readAllocationNode2 = this.readAllocationNode;
            n = n4;
            i = n3;
            if (n4 == readAllocationNode2.endPosition) {
                this.readAllocationNode = readAllocationNode2.next;
                n = n4;
            }
        }
    }
    
    private void readEncryptionData(final DecoderInputBuffer decoderInputBuffer, final SampleMetadataQueue.SampleExtrasHolder sampleExtrasHolder) {
        final long offset = sampleExtrasHolder.offset;
        this.scratch.reset(1);
        this.readData(offset, this.scratch.data, 1);
        final long n = offset + 1L;
        final byte[] data = this.scratch.data;
        final int n2 = 0;
        final byte b = data[0];
        final boolean b2 = (b & 0x80) != 0x0;
        final int n3 = b & 0x7F;
        final CryptoInfo cryptoInfo = decoderInputBuffer.cryptoInfo;
        if (cryptoInfo.iv == null) {
            cryptoInfo.iv = new byte[16];
        }
        this.readData(n, decoderInputBuffer.cryptoInfo.iv, n3);
        long n4 = n + n3;
        int unsignedShort;
        if (b2) {
            this.scratch.reset(2);
            this.readData(n4, this.scratch.data, 2);
            n4 += 2L;
            unsignedShort = this.scratch.readUnsignedShort();
        }
        else {
            unsignedShort = 1;
        }
        final int[] numBytesOfClearData = decoderInputBuffer.cryptoInfo.numBytesOfClearData;
        int[] array = null;
        Label_0195: {
            if (numBytesOfClearData != null) {
                array = numBytesOfClearData;
                if (numBytesOfClearData.length >= unsignedShort) {
                    break Label_0195;
                }
            }
            array = new int[unsignedShort];
        }
        final int[] numBytesOfEncryptedData = decoderInputBuffer.cryptoInfo.numBytesOfEncryptedData;
        int[] array2 = null;
        Label_0227: {
            if (numBytesOfEncryptedData != null) {
                array2 = numBytesOfEncryptedData;
                if (numBytesOfEncryptedData.length >= unsignedShort) {
                    break Label_0227;
                }
            }
            array2 = new int[unsignedShort];
        }
        if (b2) {
            final int n5 = unsignedShort * 6;
            this.scratch.reset(n5);
            this.readData(n4, this.scratch.data, n5);
            final long n6 = n4 + n5;
            this.scratch.setPosition(0);
            int n7 = n2;
            while (true) {
                n4 = n6;
                if (n7 >= unsignedShort) {
                    break;
                }
                array[n7] = this.scratch.readUnsignedShort();
                array2[n7] = this.scratch.readUnsignedIntToInt();
                ++n7;
            }
        }
        else {
            array2[array[0] = 0] = sampleExtrasHolder.size - (int)(n4 - sampleExtrasHolder.offset);
        }
        final CryptoData cryptoData = sampleExtrasHolder.cryptoData;
        final CryptoInfo cryptoInfo2 = decoderInputBuffer.cryptoInfo;
        cryptoInfo2.set(unsignedShort, array, array2, cryptoData.encryptionKey, cryptoInfo2.iv, cryptoData.cryptoMode, cryptoData.encryptedBlocks, cryptoData.clearBlocks);
        final long offset2 = sampleExtrasHolder.offset;
        final int n8 = (int)(n4 - offset2);
        sampleExtrasHolder.offset = offset2 + n8;
        sampleExtrasHolder.size -= n8;
    }
    
    public int advanceTo(final long n, final boolean b, final boolean b2) {
        return this.metadataQueue.advanceTo(n, b, b2);
    }
    
    public int advanceToEnd() {
        return this.metadataQueue.advanceToEnd();
    }
    
    public void discardTo(final long n, final boolean b, final boolean b2) {
        this.discardDownstreamTo(this.metadataQueue.discardTo(n, b, b2));
    }
    
    public void discardToEnd() {
        this.discardDownstreamTo(this.metadataQueue.discardToEnd());
    }
    
    public void discardToRead() {
        this.discardDownstreamTo(this.metadataQueue.discardToRead());
    }
    
    public void discardUpstreamSamples(final int n) {
        this.totalBytesWritten = this.metadataQueue.discardUpstreamSamples(n);
        final long totalBytesWritten = this.totalBytesWritten;
        AllocationNode allocationNode;
        if (totalBytesWritten != 0L && totalBytesWritten != (allocationNode = this.firstAllocationNode).startPosition) {
            while (this.totalBytesWritten > allocationNode.endPosition) {
                allocationNode = allocationNode.next;
            }
            final AllocationNode next = allocationNode.next;
            this.clearAllocationNodes(next);
            allocationNode.next = new AllocationNode(allocationNode.endPosition, this.allocationLength);
            AllocationNode next2;
            if (this.totalBytesWritten == allocationNode.endPosition) {
                next2 = allocationNode.next;
            }
            else {
                next2 = allocationNode;
            }
            this.writeAllocationNode = next2;
            if (this.readAllocationNode == next) {
                this.readAllocationNode = allocationNode.next;
            }
        }
        else {
            this.clearAllocationNodes(this.firstAllocationNode);
            this.firstAllocationNode = new AllocationNode(this.totalBytesWritten, this.allocationLength);
            final AllocationNode firstAllocationNode = this.firstAllocationNode;
            this.readAllocationNode = firstAllocationNode;
            this.writeAllocationNode = firstAllocationNode;
        }
    }
    
    @Override
    public void format(final Format lastUnadjustedFormat) {
        final Format adjustedSampleFormat = getAdjustedSampleFormat(lastUnadjustedFormat, this.sampleOffsetUs);
        final boolean format = this.metadataQueue.format(adjustedSampleFormat);
        this.lastUnadjustedFormat = lastUnadjustedFormat;
        this.pendingFormatAdjustment = false;
        final UpstreamFormatChangedListener upstreamFormatChangeListener = this.upstreamFormatChangeListener;
        if (upstreamFormatChangeListener != null && format) {
            upstreamFormatChangeListener.onUpstreamFormatChanged(adjustedSampleFormat);
        }
    }
    
    public int getFirstIndex() {
        return this.metadataQueue.getFirstIndex();
    }
    
    public long getFirstTimestampUs() {
        return this.metadataQueue.getFirstTimestampUs();
    }
    
    public long getLargestQueuedTimestampUs() {
        return this.metadataQueue.getLargestQueuedTimestampUs();
    }
    
    public int getReadIndex() {
        return this.metadataQueue.getReadIndex();
    }
    
    public Format getUpstreamFormat() {
        return this.metadataQueue.getUpstreamFormat();
    }
    
    public int getWriteIndex() {
        return this.metadataQueue.getWriteIndex();
    }
    
    public boolean hasNextSample() {
        return this.metadataQueue.hasNextSample();
    }
    
    public boolean isLastSampleQueued() {
        return this.metadataQueue.isLastSampleQueued();
    }
    
    public int peekSourceId() {
        return this.metadataQueue.peekSourceId();
    }
    
    public int read(final FormatHolder formatHolder, final DecoderInputBuffer decoderInputBuffer, final boolean b, final boolean b2, final long n) {
        final int read = this.metadataQueue.read(formatHolder, decoderInputBuffer, b, b2, this.downstreamFormat, this.extrasHolder);
        if (read == -5) {
            this.downstreamFormat = formatHolder.format;
            return -5;
        }
        if (read == -4) {
            if (!decoderInputBuffer.isEndOfStream()) {
                if (decoderInputBuffer.timeUs < n) {
                    decoderInputBuffer.addFlag(Integer.MIN_VALUE);
                }
                if (decoderInputBuffer.isEncrypted()) {
                    this.readEncryptionData(decoderInputBuffer, this.extrasHolder);
                }
                decoderInputBuffer.ensureSpaceForWrite(this.extrasHolder.size);
                final SampleMetadataQueue.SampleExtrasHolder extrasHolder = this.extrasHolder;
                this.readData(extrasHolder.offset, decoderInputBuffer.data, extrasHolder.size);
            }
            return -4;
        }
        if (read == -3) {
            return -3;
        }
        throw new IllegalStateException();
    }
    
    public void reset() {
        this.reset(false);
    }
    
    public void reset(final boolean b) {
        this.metadataQueue.reset(b);
        this.clearAllocationNodes(this.firstAllocationNode);
        this.firstAllocationNode = new AllocationNode(0L, this.allocationLength);
        final AllocationNode firstAllocationNode = this.firstAllocationNode;
        this.readAllocationNode = firstAllocationNode;
        this.writeAllocationNode = firstAllocationNode;
        this.totalBytesWritten = 0L;
        this.allocator.trim();
    }
    
    public void rewind() {
        this.metadataQueue.rewind();
        this.readAllocationNode = this.firstAllocationNode;
    }
    
    @Override
    public int sampleData(final ExtractorInput extractorInput, int n, final boolean b) throws IOException, InterruptedException {
        n = this.preAppend(n);
        final AllocationNode writeAllocationNode = this.writeAllocationNode;
        n = extractorInput.read(writeAllocationNode.allocation.data, writeAllocationNode.translateOffset(this.totalBytesWritten), n);
        if (n != -1) {
            this.postAppend(n);
            return n;
        }
        if (b) {
            return -1;
        }
        throw new EOFException();
    }
    
    @Override
    public void sampleData(final ParsableByteArray parsableByteArray, int i) {
        while (i > 0) {
            final int preAppend = this.preAppend(i);
            final AllocationNode writeAllocationNode = this.writeAllocationNode;
            parsableByteArray.readBytes(writeAllocationNode.allocation.data, writeAllocationNode.translateOffset(this.totalBytesWritten), preAppend);
            i -= preAppend;
            this.postAppend(preAppend);
        }
    }
    
    @Override
    public void sampleMetadata(long totalBytesWritten, final int n, final int n2, final int n3, final CryptoData cryptoData) {
        if (this.pendingFormatAdjustment) {
            this.format(this.lastUnadjustedFormat);
        }
        final long n4 = totalBytesWritten + this.sampleOffsetUs;
        if (this.pendingSplice) {
            if ((n & 0x1) == 0x0 || !this.metadataQueue.attemptSplice(n4)) {
                return;
            }
            this.pendingSplice = false;
        }
        totalBytesWritten = this.totalBytesWritten;
        this.metadataQueue.commitSample(n4, n, totalBytesWritten - n2 - n3, n2, cryptoData);
    }
    
    public boolean setReadPosition(final int readPosition) {
        return this.metadataQueue.setReadPosition(readPosition);
    }
    
    public void setSampleOffsetUs(final long sampleOffsetUs) {
        if (this.sampleOffsetUs != sampleOffsetUs) {
            this.sampleOffsetUs = sampleOffsetUs;
            this.pendingFormatAdjustment = true;
        }
    }
    
    public void setUpstreamFormatChangeListener(final UpstreamFormatChangedListener upstreamFormatChangeListener) {
        this.upstreamFormatChangeListener = upstreamFormatChangeListener;
    }
    
    public void sourceId(final int n) {
        this.metadataQueue.sourceId(n);
    }
    
    public void splice() {
        this.pendingSplice = true;
    }
    
    private static final class AllocationNode
    {
        public Allocation allocation;
        public final long endPosition;
        public AllocationNode next;
        public final long startPosition;
        public boolean wasInitialized;
        
        public AllocationNode(final long startPosition, final int n) {
            this.startPosition = startPosition;
            this.endPosition = startPosition + n;
        }
        
        public AllocationNode clear() {
            this.allocation = null;
            final AllocationNode next = this.next;
            this.next = null;
            return next;
        }
        
        public void initialize(final Allocation allocation, final AllocationNode next) {
            this.allocation = allocation;
            this.next = next;
            this.wasInitialized = true;
        }
        
        public int translateOffset(final long n) {
            return (int)(n - this.startPosition) + this.allocation.offset;
        }
    }
    
    public interface UpstreamFormatChangedListener
    {
        void onUpstreamFormatChanged(final Format p0);
    }
}
