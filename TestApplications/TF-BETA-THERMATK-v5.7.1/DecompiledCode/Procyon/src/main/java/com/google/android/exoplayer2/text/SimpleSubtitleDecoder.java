// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.text;

import java.nio.ByteBuffer;
import com.google.android.exoplayer2.decoder.OutputBuffer;
import com.google.android.exoplayer2.decoder.DecoderInputBuffer;
import com.google.android.exoplayer2.decoder.SimpleDecoder;

public abstract class SimpleSubtitleDecoder extends SimpleDecoder<SubtitleInputBuffer, SubtitleOutputBuffer, SubtitleDecoderException> implements SubtitleDecoder
{
    private final String name;
    
    protected SimpleSubtitleDecoder(final String name) {
        super(new SubtitleInputBuffer[2], new SubtitleOutputBuffer[2]);
        this.name = name;
        this.setInitialInputBufferSize(1024);
    }
    
    @Override
    protected final SubtitleInputBuffer createInputBuffer() {
        return new SubtitleInputBuffer();
    }
    
    @Override
    protected final SubtitleOutputBuffer createOutputBuffer() {
        return new SimpleSubtitleOutputBuffer(this);
    }
    
    @Override
    protected final SubtitleDecoderException createUnexpectedDecodeException(final Throwable t) {
        return new SubtitleDecoderException("Unexpected decode error", t);
    }
    
    protected abstract Subtitle decode(final byte[] p0, final int p1, final boolean p2) throws SubtitleDecoderException;
    
    @Override
    protected final SubtitleDecoderException decode(final SubtitleInputBuffer subtitleInputBuffer, final SubtitleOutputBuffer subtitleOutputBuffer, final boolean b) {
        try {
            final ByteBuffer data = subtitleInputBuffer.data;
            subtitleOutputBuffer.setContent(subtitleInputBuffer.timeUs, this.decode(data.array(), data.limit(), b), subtitleInputBuffer.subsampleOffsetUs);
            subtitleOutputBuffer.clearFlag(Integer.MIN_VALUE);
            return null;
        }
        catch (SubtitleDecoderException ex) {
            return ex;
        }
    }
    
    @Override
    public final String getName() {
        return this.name;
    }
    
    @Override
    protected final void releaseOutputBuffer(final SubtitleOutputBuffer subtitleOutputBuffer) {
        super.releaseOutputBuffer(subtitleOutputBuffer);
    }
    
    @Override
    public void setPositionUs(final long n) {
    }
}
