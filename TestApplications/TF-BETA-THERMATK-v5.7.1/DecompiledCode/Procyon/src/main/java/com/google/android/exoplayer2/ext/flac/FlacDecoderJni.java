// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.ext.flac;

import java.io.IOException;
import com.google.android.exoplayer2.util.FlacStreamInfo;
import com.google.android.exoplayer2.extractor.ExtractorInput;
import java.nio.ByteBuffer;

final class FlacDecoderJni
{
    private static final int TEMP_BUFFER_SIZE = 8192;
    private ByteBuffer byteBufferData;
    private boolean endOfExtractorInput;
    private ExtractorInput extractorInput;
    private final long nativeDecoderContext;
    private byte[] tempBuffer;
    
    public FlacDecoderJni() throws FlacDecoderException {
        this.nativeDecoderContext = this.flacInit();
        if (this.nativeDecoderContext != 0L) {
            return;
        }
        throw new FlacDecoderException("Failed to initialize decoder");
    }
    
    private native FlacStreamInfo flacDecodeMetadata(final long p0) throws IOException, InterruptedException;
    
    private native int flacDecodeToArray(final long p0, final byte[] p1) throws IOException, InterruptedException;
    
    private native int flacDecodeToBuffer(final long p0, final ByteBuffer p1) throws IOException, InterruptedException;
    
    private native void flacFlush(final long p0);
    
    private native long flacGetDecodePosition(final long p0);
    
    private native long flacGetLastFrameFirstSampleIndex(final long p0);
    
    private native long flacGetLastFrameTimestamp(final long p0);
    
    private native long flacGetNextFrameFirstSampleIndex(final long p0);
    
    private native long flacGetSeekPosition(final long p0, final long p1);
    
    private native String flacGetStateString(final long p0);
    
    private native long flacInit();
    
    private native boolean flacIsDecoderAtEndOfStream(final long p0);
    
    private native void flacRelease(final long p0);
    
    private native void flacReset(final long p0, final long p1);
    
    private int readFromExtractorInput(int read, int n) throws IOException, InterruptedException {
        n = (read = this.extractorInput.read(this.tempBuffer, read, n));
        if (n == -1) {
            this.endOfExtractorInput = true;
            read = 0;
        }
        return read;
    }
    
    public FlacStreamInfo decodeMetadata() throws IOException, InterruptedException {
        return this.flacDecodeMetadata(this.nativeDecoderContext);
    }
    
    public void decodeSample(final ByteBuffer byteBuffer) throws IOException, InterruptedException, FlacFrameDecodeException {
        byteBuffer.clear();
        int n;
        if (byteBuffer.isDirect()) {
            n = this.flacDecodeToBuffer(this.nativeDecoderContext, byteBuffer);
        }
        else {
            n = this.flacDecodeToArray(this.nativeDecoderContext, byteBuffer.array());
        }
        if (n < 0) {
            if (!this.isDecoderAtEndOfInput()) {
                throw new FlacFrameDecodeException("Cannot decode FLAC frame", n);
            }
            byteBuffer.limit(0);
        }
        else {
            byteBuffer.limit(n);
        }
    }
    
    public void decodeSampleWithBacktrackPosition(final ByteBuffer byteBuffer, final long n) throws InterruptedException, IOException, FlacFrameDecodeException {
        try {
            this.decodeSample(byteBuffer);
        }
        catch (IOException ex) {
            if (n >= 0L) {
                this.reset(n);
                final ExtractorInput extractorInput = this.extractorInput;
                if (extractorInput != null) {
                    extractorInput.setRetryPosition(n, ex);
                    throw null;
                }
            }
            throw ex;
        }
    }
    
    public void flush() {
        this.flacFlush(this.nativeDecoderContext);
    }
    
    public long getDecodePosition() {
        return this.flacGetDecodePosition(this.nativeDecoderContext);
    }
    
    public long getLastFrameFirstSampleIndex() {
        return this.flacGetLastFrameFirstSampleIndex(this.nativeDecoderContext);
    }
    
    public long getLastFrameTimestamp() {
        return this.flacGetLastFrameTimestamp(this.nativeDecoderContext);
    }
    
    public long getNextFrameFirstSampleIndex() {
        return this.flacGetNextFrameFirstSampleIndex(this.nativeDecoderContext);
    }
    
    public long getSeekPosition(final long n) {
        return this.flacGetSeekPosition(this.nativeDecoderContext, n);
    }
    
    public String getStateString() {
        return this.flacGetStateString(this.nativeDecoderContext);
    }
    
    public boolean isDecoderAtEndOfInput() {
        return this.flacIsDecoderAtEndOfStream(this.nativeDecoderContext);
    }
    
    public boolean isEndOfData() {
        final ByteBuffer byteBufferData = this.byteBufferData;
        boolean b = true;
        if (byteBufferData != null) {
            if (byteBufferData.remaining() != 0) {
                b = false;
            }
            return b;
        }
        return this.extractorInput == null || this.endOfExtractorInput;
    }
    
    public int read(final ByteBuffer byteBuffer) throws IOException, InterruptedException {
        final int remaining = byteBuffer.remaining();
        final ByteBuffer byteBufferData = this.byteBufferData;
        int min;
        if (byteBufferData != null) {
            min = Math.min(remaining, byteBufferData.remaining());
            final int limit = this.byteBufferData.limit();
            final ByteBuffer byteBufferData2 = this.byteBufferData;
            byteBufferData2.limit(byteBufferData2.position() + min);
            byteBuffer.put(this.byteBufferData);
            this.byteBufferData.limit(limit);
        }
        else {
            if (this.extractorInput == null) {
                return -1;
            }
            final int min2 = Math.min(remaining, 8192);
            final int fromExtractorInput = this.readFromExtractorInput(0, min2);
            if ((min = fromExtractorInput) < 4) {
                min = fromExtractorInput + this.readFromExtractorInput(fromExtractorInput, min2 - fromExtractorInput);
            }
            byteBuffer.put(this.tempBuffer, 0, min);
        }
        return min;
    }
    
    public void release() {
        this.flacRelease(this.nativeDecoderContext);
    }
    
    public void reset(final long n) {
        this.flacReset(this.nativeDecoderContext, n);
    }
    
    public void setData(final ExtractorInput extractorInput) {
        this.byteBufferData = null;
        this.extractorInput = extractorInput;
        if (this.tempBuffer == null) {
            this.tempBuffer = new byte[8192];
        }
        this.endOfExtractorInput = false;
    }
    
    public void setData(final ByteBuffer byteBufferData) {
        this.byteBufferData = byteBufferData;
        this.extractorInput = null;
        this.tempBuffer = null;
    }
    
    public static final class FlacFrameDecodeException extends Exception
    {
        public final int errorCode;
        
        public FlacFrameDecodeException(final String message, final int errorCode) {
            super(message);
            this.errorCode = errorCode;
        }
    }
}
