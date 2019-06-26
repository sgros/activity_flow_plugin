// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.source;

import com.google.android.exoplayer2.decoder.DecoderInputBuffer;
import com.google.android.exoplayer2.FormatHolder;
import com.google.android.exoplayer2.util.Util;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.extractor.TrackOutput;

final class SampleMetadataQueue
{
    private int absoluteFirstIndex;
    private int capacity;
    private TrackOutput.CryptoData[] cryptoDatas;
    private int[] flags;
    private Format[] formats;
    private boolean isLastSampleQueued;
    private long largestDiscardedTimestampUs;
    private long largestQueuedTimestampUs;
    private int length;
    private long[] offsets;
    private int readPosition;
    private int relativeFirstIndex;
    private int[] sizes;
    private int[] sourceIds;
    private long[] timesUs;
    private Format upstreamFormat;
    private boolean upstreamFormatRequired;
    private boolean upstreamKeyframeRequired;
    private int upstreamSourceId;
    
    public SampleMetadataQueue() {
        this.capacity = 1000;
        final int capacity = this.capacity;
        this.sourceIds = new int[capacity];
        this.offsets = new long[capacity];
        this.timesUs = new long[capacity];
        this.flags = new int[capacity];
        this.sizes = new int[capacity];
        this.cryptoDatas = new TrackOutput.CryptoData[capacity];
        this.formats = new Format[capacity];
        this.largestDiscardedTimestampUs = Long.MIN_VALUE;
        this.largestQueuedTimestampUs = Long.MIN_VALUE;
        this.upstreamFormatRequired = true;
        this.upstreamKeyframeRequired = true;
    }
    
    private long discardSamples(int n) {
        this.largestDiscardedTimestampUs = Math.max(this.largestDiscardedTimestampUs, this.getLargestTimestamp(n));
        this.length -= n;
        this.absoluteFirstIndex += n;
        this.relativeFirstIndex += n;
        final int relativeFirstIndex = this.relativeFirstIndex;
        final int capacity = this.capacity;
        if (relativeFirstIndex >= capacity) {
            this.relativeFirstIndex = relativeFirstIndex - capacity;
        }
        this.readPosition -= n;
        if (this.readPosition < 0) {
            this.readPosition = 0;
        }
        if (this.length == 0) {
            if ((n = this.relativeFirstIndex) == 0) {
                n = this.capacity;
            }
            --n;
            return this.offsets[n] + this.sizes[n];
        }
        return this.offsets[this.relativeFirstIndex];
    }
    
    private int findSampleBefore(int n, final int n2, final long n3, final boolean b) {
        int n4 = n;
        n = 0;
        int n5 = -1;
        while (n < n2 && this.timesUs[n4] <= n3) {
            if (!b || (this.flags[n4] & 0x1) != 0x0) {
                n5 = n;
            }
            if (++n4 == this.capacity) {
                n4 = 0;
            }
            ++n;
        }
        return n5;
    }
    
    private long getLargestTimestamp(final int n) {
        long max = Long.MIN_VALUE;
        if (n == 0) {
            return Long.MIN_VALUE;
        }
        int relativeIndex = this.getRelativeIndex(n - 1);
        int n2 = 0;
        long n3;
        while (true) {
            n3 = max;
            if (n2 >= n) {
                break;
            }
            max = Math.max(max, this.timesUs[relativeIndex]);
            if ((this.flags[relativeIndex] & 0x1) != 0x0) {
                n3 = max;
                break;
            }
            if (--relativeIndex == -1) {
                relativeIndex = this.capacity - 1;
            }
            ++n2;
        }
        return n3;
    }
    
    private int getRelativeIndex(int n) {
        n += this.relativeFirstIndex;
        final int capacity = this.capacity;
        if (n >= capacity) {
            n -= capacity;
        }
        return n;
    }
    
    public int advanceTo(final long n, final boolean b, final boolean b2) {
        synchronized (this) {
            final int relativeIndex = this.getRelativeIndex(this.readPosition);
            if (!this.hasNextSample() || n < this.timesUs[relativeIndex] || (n > this.largestQueuedTimestampUs && !b2)) {
                return -1;
            }
            final int sampleBefore = this.findSampleBefore(relativeIndex, this.length - this.readPosition, n, b);
            if (sampleBefore == -1) {
                return -1;
            }
            this.readPosition += sampleBefore;
            return sampleBefore;
        }
    }
    
    public int advanceToEnd() {
        synchronized (this) {
            final int length = this.length;
            final int readPosition = this.readPosition;
            this.readPosition = this.length;
            return length - readPosition;
        }
    }
    
    public boolean attemptSplice(final long n) {
        synchronized (this) {
            final int length = this.length;
            boolean b = false;
            if (length == 0) {
                if (n > this.largestDiscardedTimestampUs) {
                    b = true;
                }
                return b;
            }
            if (Math.max(this.largestDiscardedTimestampUs, this.getLargestTimestamp(this.readPosition)) >= n) {
                return false;
            }
            int length2 = this.length;
            int n2;
            for (int relativeIndex = this.getRelativeIndex(this.length - 1); length2 > this.readPosition && this.timesUs[relativeIndex] >= n; relativeIndex = this.capacity - 1, length2 = n2) {
                n2 = length2 - 1;
                final int n3 = relativeIndex - 1;
                length2 = n2;
                if ((relativeIndex = n3) == -1) {}
            }
            this.discardUpstreamSamples(this.absoluteFirstIndex + length2);
            return true;
        }
    }
    
    public void commitSample(final long b, int capacity, final long n, int n2, final TrackOutput.CryptoData cryptoData) {
        synchronized (this) {
            if (this.upstreamKeyframeRequired) {
                if ((capacity & 0x1) == 0x0) {
                    return;
                }
                this.upstreamKeyframeRequired = false;
            }
            Assertions.checkState(!this.upstreamFormatRequired);
            this.isLastSampleQueued = ((0x20000000 & capacity) != 0x0);
            this.largestQueuedTimestampUs = Math.max(this.largestQueuedTimestampUs, b);
            final int relativeIndex = this.getRelativeIndex(this.length);
            this.timesUs[relativeIndex] = b;
            this.offsets[relativeIndex] = n;
            this.sizes[relativeIndex] = n2;
            this.flags[relativeIndex] = capacity;
            this.cryptoDatas[relativeIndex] = cryptoData;
            this.formats[relativeIndex] = this.upstreamFormat;
            this.sourceIds[relativeIndex] = this.upstreamSourceId;
            ++this.length;
            if (this.length == this.capacity) {
                capacity = this.capacity + 1000;
                final int[] sourceIds = new int[capacity];
                final long[] offsets = new long[capacity];
                final long[] timesUs = new long[capacity];
                final int[] flags = new int[capacity];
                final int[] sizes = new int[capacity];
                final TrackOutput.CryptoData[] cryptoDatas = new TrackOutput.CryptoData[capacity];
                final Format[] formats = new Format[capacity];
                n2 = this.capacity - this.relativeFirstIndex;
                System.arraycopy(this.offsets, this.relativeFirstIndex, offsets, 0, n2);
                System.arraycopy(this.timesUs, this.relativeFirstIndex, timesUs, 0, n2);
                System.arraycopy(this.flags, this.relativeFirstIndex, flags, 0, n2);
                System.arraycopy(this.sizes, this.relativeFirstIndex, sizes, 0, n2);
                System.arraycopy(this.cryptoDatas, this.relativeFirstIndex, cryptoDatas, 0, n2);
                System.arraycopy(this.formats, this.relativeFirstIndex, formats, 0, n2);
                System.arraycopy(this.sourceIds, this.relativeFirstIndex, sourceIds, 0, n2);
                final int relativeFirstIndex = this.relativeFirstIndex;
                System.arraycopy(this.offsets, 0, offsets, n2, relativeFirstIndex);
                System.arraycopy(this.timesUs, 0, timesUs, n2, relativeFirstIndex);
                System.arraycopy(this.flags, 0, flags, n2, relativeFirstIndex);
                System.arraycopy(this.sizes, 0, sizes, n2, relativeFirstIndex);
                System.arraycopy(this.cryptoDatas, 0, cryptoDatas, n2, relativeFirstIndex);
                System.arraycopy(this.formats, 0, formats, n2, relativeFirstIndex);
                System.arraycopy(this.sourceIds, 0, sourceIds, n2, relativeFirstIndex);
                this.offsets = offsets;
                this.timesUs = timesUs;
                this.flags = flags;
                this.sizes = sizes;
                this.cryptoDatas = cryptoDatas;
                this.formats = formats;
                this.sourceIds = sourceIds;
                this.relativeFirstIndex = 0;
                this.length = this.capacity;
                this.capacity = capacity;
            }
        }
    }
    
    public long discardTo(long discardSamples, final boolean b, final boolean b2) {
        synchronized (this) {
            if (this.length == 0 || discardSamples < this.timesUs[this.relativeFirstIndex]) {
                return -1L;
            }
            int length;
            if (b2 && this.readPosition != this.length) {
                length = this.readPosition + 1;
            }
            else {
                length = this.length;
            }
            final int sampleBefore = this.findSampleBefore(this.relativeFirstIndex, length, discardSamples, b);
            if (sampleBefore == -1) {
                return -1L;
            }
            discardSamples = this.discardSamples(sampleBefore);
            return discardSamples;
        }
    }
    
    public long discardToEnd() {
        synchronized (this) {
            if (this.length == 0) {
                return -1L;
            }
            return this.discardSamples(this.length);
        }
    }
    
    public long discardToRead() {
        synchronized (this) {
            if (this.readPosition == 0) {
                return -1L;
            }
            return this.discardSamples(this.readPosition);
        }
    }
    
    public long discardUpstreamSamples(int n) {
        n = this.getWriteIndex() - n;
        final boolean b = false;
        Assertions.checkArgument(n >= 0 && n <= this.length - this.readPosition);
        this.length -= n;
        this.largestQueuedTimestampUs = Math.max(this.largestDiscardedTimestampUs, this.getLargestTimestamp(this.length));
        boolean isLastSampleQueued = b;
        if (n == 0) {
            isLastSampleQueued = b;
            if (this.isLastSampleQueued) {
                isLastSampleQueued = true;
            }
        }
        this.isLastSampleQueued = isLastSampleQueued;
        n = this.length;
        if (n == 0) {
            return 0L;
        }
        n = this.getRelativeIndex(n - 1);
        return this.offsets[n] + this.sizes[n];
    }
    
    public boolean format(final Format upstreamFormat) {
        // monitorenter(this)
        Label_0015: {
            if (upstreamFormat != null) {
                break Label_0015;
            }
            try {
                this.upstreamFormatRequired = true;
                return false;
                this.upstreamFormatRequired = false;
                // iftrue(Label_0037:, !Util.areEqual((Object)upstreamFormat, (Object)this.upstreamFormat))
                return false;
                Label_0037: {
                    this.upstreamFormat = upstreamFormat;
                }
                return true;
            }
            finally {
            }
            // monitorexit(this)
        }
    }
    
    public int getFirstIndex() {
        return this.absoluteFirstIndex;
    }
    
    public long getFirstTimestampUs() {
        synchronized (this) {
            long n;
            if (this.length == 0) {
                n = Long.MIN_VALUE;
            }
            else {
                n = this.timesUs[this.relativeFirstIndex];
            }
            return n;
        }
    }
    
    public long getLargestQueuedTimestampUs() {
        synchronized (this) {
            return this.largestQueuedTimestampUs;
        }
    }
    
    public int getReadIndex() {
        return this.absoluteFirstIndex + this.readPosition;
    }
    
    public Format getUpstreamFormat() {
        synchronized (this) {
            Format upstreamFormat;
            if (this.upstreamFormatRequired) {
                upstreamFormat = null;
            }
            else {
                upstreamFormat = this.upstreamFormat;
            }
            return upstreamFormat;
        }
    }
    
    public int getWriteIndex() {
        return this.absoluteFirstIndex + this.length;
    }
    
    public boolean hasNextSample() {
        synchronized (this) {
            return this.readPosition != this.length;
        }
    }
    
    public boolean isLastSampleQueued() {
        synchronized (this) {
            return this.isLastSampleQueued;
        }
    }
    
    public int peekSourceId() {
        final int relativeIndex = this.getRelativeIndex(this.readPosition);
        int upstreamSourceId;
        if (this.hasNextSample()) {
            upstreamSourceId = this.sourceIds[relativeIndex];
        }
        else {
            upstreamSourceId = this.upstreamSourceId;
        }
        return upstreamSourceId;
    }
    
    public int read(final FormatHolder formatHolder, final DecoderInputBuffer decoderInputBuffer, final boolean b, final boolean b2, final Format format, final SampleExtrasHolder sampleExtrasHolder) {
        synchronized (this) {
            if (!this.hasNextSample()) {
                if (b2 || this.isLastSampleQueued) {
                    decoderInputBuffer.setFlags(4);
                    return -4;
                }
                if (this.upstreamFormat != null && (b || this.upstreamFormat != format)) {
                    formatHolder.format = this.upstreamFormat;
                    return -5;
                }
                return -3;
            }
            else {
                final int relativeIndex = this.getRelativeIndex(this.readPosition);
                if (b || this.formats[relativeIndex] != format) {
                    formatHolder.format = this.formats[relativeIndex];
                    return -5;
                }
                if (decoderInputBuffer.isFlagsOnly()) {
                    return -3;
                }
                decoderInputBuffer.timeUs = this.timesUs[relativeIndex];
                decoderInputBuffer.setFlags(this.flags[relativeIndex]);
                sampleExtrasHolder.size = this.sizes[relativeIndex];
                sampleExtrasHolder.offset = this.offsets[relativeIndex];
                sampleExtrasHolder.cryptoData = this.cryptoDatas[relativeIndex];
                ++this.readPosition;
                return -4;
            }
        }
    }
    
    public void reset(final boolean b) {
        this.length = 0;
        this.absoluteFirstIndex = 0;
        this.relativeFirstIndex = 0;
        this.readPosition = 0;
        this.upstreamKeyframeRequired = true;
        this.largestDiscardedTimestampUs = Long.MIN_VALUE;
        this.largestQueuedTimestampUs = Long.MIN_VALUE;
        this.isLastSampleQueued = false;
        if (b) {
            this.upstreamFormat = null;
            this.upstreamFormatRequired = true;
        }
    }
    
    public void rewind() {
        synchronized (this) {
            this.readPosition = 0;
        }
    }
    
    public boolean setReadPosition(final int n) {
        synchronized (this) {
            if (this.absoluteFirstIndex <= n && n <= this.absoluteFirstIndex + this.length) {
                this.readPosition = n - this.absoluteFirstIndex;
                return true;
            }
            return false;
        }
    }
    
    public void sourceId(final int upstreamSourceId) {
        this.upstreamSourceId = upstreamSourceId;
    }
    
    public static final class SampleExtrasHolder
    {
        public TrackOutput.CryptoData cryptoData;
        public long offset;
        public int size;
    }
}
