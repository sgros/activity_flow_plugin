// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.source.dash;

import com.google.android.exoplayer2.util.Util;
import com.google.android.exoplayer2.decoder.DecoderInputBuffer;
import com.google.android.exoplayer2.FormatHolder;
import java.io.IOException;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.source.dash.manifest.EventStream;
import com.google.android.exoplayer2.metadata.emsg.EventMessageEncoder;
import com.google.android.exoplayer2.source.SampleStream;

final class EventSampleStream implements SampleStream
{
    private int currentIndex;
    private final EventMessageEncoder eventMessageEncoder;
    private EventStream eventStream;
    private boolean eventStreamAppendable;
    private long[] eventTimesUs;
    private boolean isFormatSentDownstream;
    private long pendingSeekPositionUs;
    private final Format upstreamFormat;
    
    public EventSampleStream(final EventStream eventStream, final Format upstreamFormat, final boolean b) {
        this.upstreamFormat = upstreamFormat;
        this.eventStream = eventStream;
        this.eventMessageEncoder = new EventMessageEncoder();
        this.pendingSeekPositionUs = -9223372036854775807L;
        this.eventTimesUs = eventStream.presentationTimesUs;
        this.updateEventStream(eventStream, b);
    }
    
    public String eventStreamId() {
        return this.eventStream.id();
    }
    
    @Override
    public boolean isReady() {
        return true;
    }
    
    @Override
    public void maybeThrowError() throws IOException {
    }
    
    @Override
    public int readData(final FormatHolder formatHolder, final DecoderInputBuffer decoderInputBuffer, final boolean b) {
        if (b || !this.isFormatSentDownstream) {
            formatHolder.format = this.upstreamFormat;
            this.isFormatSentDownstream = true;
            return -5;
        }
        final int currentIndex = this.currentIndex;
        if (currentIndex == this.eventTimesUs.length) {
            if (!this.eventStreamAppendable) {
                decoderInputBuffer.setFlags(4);
                return -4;
            }
            return -3;
        }
        else {
            this.currentIndex = currentIndex + 1;
            final EventMessageEncoder eventMessageEncoder = this.eventMessageEncoder;
            final EventStream eventStream = this.eventStream;
            final byte[] encode = eventMessageEncoder.encode(eventStream.events[currentIndex], eventStream.timescale);
            if (encode != null) {
                decoderInputBuffer.ensureSpaceForWrite(encode.length);
                decoderInputBuffer.setFlags(1);
                decoderInputBuffer.data.put(encode);
                decoderInputBuffer.timeUs = this.eventTimesUs[currentIndex];
                return -4;
            }
            return -3;
        }
    }
    
    public void seekToUs(long pendingSeekPositionUs) {
        final long[] eventTimesUs = this.eventTimesUs;
        final boolean b = false;
        this.currentIndex = Util.binarySearchCeil(eventTimesUs, pendingSeekPositionUs, true, false);
        int n = b ? 1 : 0;
        if (this.eventStreamAppendable) {
            n = (b ? 1 : 0);
            if (this.currentIndex == this.eventTimesUs.length) {
                n = 1;
            }
        }
        if (n == 0) {
            pendingSeekPositionUs = -9223372036854775807L;
        }
        this.pendingSeekPositionUs = pendingSeekPositionUs;
    }
    
    @Override
    public int skipData(final long n) {
        return (this.currentIndex = Math.max(this.currentIndex, Util.binarySearchCeil(this.eventTimesUs, n, true, false))) - this.currentIndex;
    }
    
    public void updateEventStream(final EventStream eventStream, final boolean eventStreamAppendable) {
        final int currentIndex = this.currentIndex;
        long n;
        if (currentIndex == 0) {
            n = -9223372036854775807L;
        }
        else {
            n = this.eventTimesUs[currentIndex - 1];
        }
        this.eventStreamAppendable = eventStreamAppendable;
        this.eventStream = eventStream;
        this.eventTimesUs = eventStream.presentationTimesUs;
        final long pendingSeekPositionUs = this.pendingSeekPositionUs;
        if (pendingSeekPositionUs != -9223372036854775807L) {
            this.seekToUs(pendingSeekPositionUs);
        }
        else if (n != -9223372036854775807L) {
            this.currentIndex = Util.binarySearchCeil(this.eventTimesUs, n, false, false);
        }
    }
}
