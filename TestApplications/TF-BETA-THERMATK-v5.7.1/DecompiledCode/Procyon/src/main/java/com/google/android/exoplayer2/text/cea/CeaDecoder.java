// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.text.cea;

import com.google.android.exoplayer2.text.SubtitleDecoderException;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.text.SubtitleInputBuffer;
import com.google.android.exoplayer2.text.Subtitle;
import java.util.PriorityQueue;
import com.google.android.exoplayer2.text.SubtitleOutputBuffer;
import java.util.ArrayDeque;
import com.google.android.exoplayer2.text.SubtitleDecoder;

abstract class CeaDecoder implements SubtitleDecoder
{
    private final ArrayDeque<CeaInputBuffer> availableInputBuffers;
    private final ArrayDeque<SubtitleOutputBuffer> availableOutputBuffers;
    private CeaInputBuffer dequeuedInputBuffer;
    private long playbackPositionUs;
    private long queuedInputBufferCount;
    private final PriorityQueue<CeaInputBuffer> queuedInputBuffers;
    
    public CeaDecoder() {
        this.availableInputBuffers = new ArrayDeque<CeaInputBuffer>();
        final int n = 0;
        for (int i = 0; i < 10; ++i) {
            this.availableInputBuffers.add(new CeaInputBuffer());
        }
        this.availableOutputBuffers = new ArrayDeque<SubtitleOutputBuffer>();
        for (int j = n; j < 2; ++j) {
            this.availableOutputBuffers.add(new CeaOutputBuffer());
        }
        this.queuedInputBuffers = new PriorityQueue<CeaInputBuffer>();
    }
    
    private void releaseInputBuffer(final CeaInputBuffer e) {
        e.clear();
        this.availableInputBuffers.add(e);
    }
    
    protected abstract Subtitle createSubtitle();
    
    protected abstract void decode(final SubtitleInputBuffer p0);
    
    @Override
    public SubtitleInputBuffer dequeueInputBuffer() throws SubtitleDecoderException {
        Assertions.checkState(this.dequeuedInputBuffer == null);
        if (this.availableInputBuffers.isEmpty()) {
            return null;
        }
        return this.dequeuedInputBuffer = this.availableInputBuffers.pollFirst();
    }
    
    @Override
    public SubtitleOutputBuffer dequeueOutputBuffer() throws SubtitleDecoderException {
        if (this.availableOutputBuffers.isEmpty()) {
            return null;
        }
        while (!this.queuedInputBuffers.isEmpty() && this.queuedInputBuffers.peek().timeUs <= this.playbackPositionUs) {
            final CeaInputBuffer ceaInputBuffer = this.queuedInputBuffers.poll();
            if (ceaInputBuffer.isEndOfStream()) {
                final SubtitleOutputBuffer subtitleOutputBuffer = this.availableOutputBuffers.pollFirst();
                subtitleOutputBuffer.addFlag(4);
                this.releaseInputBuffer(ceaInputBuffer);
                return subtitleOutputBuffer;
            }
            this.decode(ceaInputBuffer);
            if (this.isNewSubtitleDataAvailable()) {
                final Subtitle subtitle = this.createSubtitle();
                if (!ceaInputBuffer.isDecodeOnly()) {
                    final SubtitleOutputBuffer subtitleOutputBuffer2 = this.availableOutputBuffers.pollFirst();
                    subtitleOutputBuffer2.setContent(ceaInputBuffer.timeUs, subtitle, Long.MAX_VALUE);
                    this.releaseInputBuffer(ceaInputBuffer);
                    return subtitleOutputBuffer2;
                }
            }
            this.releaseInputBuffer(ceaInputBuffer);
        }
        return null;
    }
    
    @Override
    public void flush() {
        this.queuedInputBufferCount = 0L;
        this.playbackPositionUs = 0L;
        while (!this.queuedInputBuffers.isEmpty()) {
            this.releaseInputBuffer(this.queuedInputBuffers.poll());
        }
        final CeaInputBuffer dequeuedInputBuffer = this.dequeuedInputBuffer;
        if (dequeuedInputBuffer != null) {
            this.releaseInputBuffer(dequeuedInputBuffer);
            this.dequeuedInputBuffer = null;
        }
    }
    
    protected abstract boolean isNewSubtitleDataAvailable();
    
    @Override
    public void queueInputBuffer(final SubtitleInputBuffer subtitleInputBuffer) throws SubtitleDecoderException {
        Assertions.checkArgument(subtitleInputBuffer == this.dequeuedInputBuffer);
        if (subtitleInputBuffer.isDecodeOnly()) {
            this.releaseInputBuffer(this.dequeuedInputBuffer);
        }
        else {
            final CeaInputBuffer dequeuedInputBuffer = this.dequeuedInputBuffer;
            final long queuedInputBufferCount = this.queuedInputBufferCount;
            this.queuedInputBufferCount = 1L + queuedInputBufferCount;
            dequeuedInputBuffer.queuedInputBufferCount = queuedInputBufferCount;
            this.queuedInputBuffers.add(this.dequeuedInputBuffer);
        }
        this.dequeuedInputBuffer = null;
    }
    
    @Override
    public void release() {
    }
    
    protected void releaseOutputBuffer(final SubtitleOutputBuffer e) {
        e.clear();
        this.availableOutputBuffers.add(e);
    }
    
    @Override
    public void setPositionUs(final long playbackPositionUs) {
        this.playbackPositionUs = playbackPositionUs;
    }
    
    private static final class CeaInputBuffer extends SubtitleInputBuffer implements Comparable<CeaInputBuffer>
    {
        private long queuedInputBufferCount;
        
        @Override
        public int compareTo(final CeaInputBuffer ceaInputBuffer) {
            final boolean endOfStream = this.isEndOfStream();
            final boolean endOfStream2 = ceaInputBuffer.isEndOfStream();
            final int n = 1;
            int n2 = 1;
            if (endOfStream != endOfStream2) {
                if (!this.isEndOfStream()) {
                    n2 = -1;
                }
                return n2;
            }
            long n3;
            if ((n3 = super.timeUs - ceaInputBuffer.timeUs) == 0L && (n3 = this.queuedInputBufferCount - ceaInputBuffer.queuedInputBufferCount) == 0L) {
                return 0;
            }
            int n4;
            if (n3 > 0L) {
                n4 = n;
            }
            else {
                n4 = -1;
            }
            return n4;
        }
    }
    
    private final class CeaOutputBuffer extends SubtitleOutputBuffer
    {
        @Override
        public final void release() {
            CeaDecoder.this.releaseOutputBuffer(this);
        }
    }
}
