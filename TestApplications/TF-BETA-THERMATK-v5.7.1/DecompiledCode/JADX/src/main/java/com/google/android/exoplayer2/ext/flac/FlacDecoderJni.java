package com.google.android.exoplayer2.ext.flac;

import com.google.android.exoplayer2.extractor.ExtractorInput;
import com.google.android.exoplayer2.util.FlacStreamInfo;
import java.io.IOException;
import java.nio.ByteBuffer;

final class FlacDecoderJni {
    private static final int TEMP_BUFFER_SIZE = 8192;
    private ByteBuffer byteBufferData;
    private boolean endOfExtractorInput;
    private ExtractorInput extractorInput;
    private final long nativeDecoderContext = flacInit();
    private byte[] tempBuffer;

    public static final class FlacFrameDecodeException extends Exception {
        public final int errorCode;

        public FlacFrameDecodeException(String str, int i) {
            super(str);
            this.errorCode = i;
        }
    }

    private native FlacStreamInfo flacDecodeMetadata(long j) throws IOException, InterruptedException;

    private native int flacDecodeToArray(long j, byte[] bArr) throws IOException, InterruptedException;

    private native int flacDecodeToBuffer(long j, ByteBuffer byteBuffer) throws IOException, InterruptedException;

    private native void flacFlush(long j);

    private native long flacGetDecodePosition(long j);

    private native long flacGetLastFrameFirstSampleIndex(long j);

    private native long flacGetLastFrameTimestamp(long j);

    private native long flacGetNextFrameFirstSampleIndex(long j);

    private native long flacGetSeekPosition(long j, long j2);

    private native String flacGetStateString(long j);

    private native long flacInit();

    private native boolean flacIsDecoderAtEndOfStream(long j);

    private native void flacRelease(long j);

    private native void flacReset(long j, long j2);

    public FlacDecoderJni() throws FlacDecoderException {
        if (this.nativeDecoderContext == 0) {
            throw new FlacDecoderException("Failed to initialize decoder");
        }
    }

    public void setData(ByteBuffer byteBuffer) {
        this.byteBufferData = byteBuffer;
        this.extractorInput = null;
        this.tempBuffer = null;
    }

    public void setData(ExtractorInput extractorInput) {
        this.byteBufferData = null;
        this.extractorInput = extractorInput;
        if (this.tempBuffer == null) {
            this.tempBuffer = new byte[8192];
        }
        this.endOfExtractorInput = false;
    }

    public boolean isEndOfData() {
        ByteBuffer byteBuffer = this.byteBufferData;
        boolean z = true;
        if (byteBuffer != null) {
            if (byteBuffer.remaining() != 0) {
                z = false;
            }
            return z;
        } else if (this.extractorInput != null) {
            return this.endOfExtractorInput;
        } else {
            return true;
        }
    }

    public int read(ByteBuffer byteBuffer) throws IOException, InterruptedException {
        int remaining = byteBuffer.remaining();
        ByteBuffer byteBuffer2 = this.byteBufferData;
        if (byteBuffer2 != null) {
            remaining = Math.min(remaining, byteBuffer2.remaining());
            int limit = this.byteBufferData.limit();
            ByteBuffer byteBuffer3 = this.byteBufferData;
            byteBuffer3.limit(byteBuffer3.position() + remaining);
            byteBuffer.put(this.byteBufferData);
            this.byteBufferData.limit(limit);
        } else if (this.extractorInput == null) {
            return -1;
        } else {
            remaining = Math.min(remaining, 8192);
            int readFromExtractorInput = readFromExtractorInput(0, remaining);
            if (readFromExtractorInput < 4) {
                readFromExtractorInput += readFromExtractorInput(readFromExtractorInput, remaining - readFromExtractorInput);
            }
            remaining = readFromExtractorInput;
            byteBuffer.put(this.tempBuffer, 0, remaining);
        }
        return remaining;
    }

    public FlacStreamInfo decodeMetadata() throws IOException, InterruptedException {
        return flacDecodeMetadata(this.nativeDecoderContext);
    }

    public void decodeSampleWithBacktrackPosition(ByteBuffer byteBuffer, long j) throws InterruptedException, IOException, FlacFrameDecodeException {
        try {
            decodeSample(byteBuffer);
        } catch (IOException e) {
            if (j >= 0) {
                reset(j);
                ExtractorInput extractorInput = this.extractorInput;
                if (extractorInput != null) {
                    extractorInput.setRetryPosition(j, e);
                    throw null;
                }
            }
            throw e;
        }
    }

    public void decodeSample(ByteBuffer byteBuffer) throws IOException, InterruptedException, FlacFrameDecodeException {
        int flacDecodeToBuffer;
        byteBuffer.clear();
        if (byteBuffer.isDirect()) {
            flacDecodeToBuffer = flacDecodeToBuffer(this.nativeDecoderContext, byteBuffer);
        } else {
            flacDecodeToBuffer = flacDecodeToArray(this.nativeDecoderContext, byteBuffer.array());
        }
        if (flacDecodeToBuffer >= 0) {
            byteBuffer.limit(flacDecodeToBuffer);
        } else if (isDecoderAtEndOfInput()) {
            byteBuffer.limit(0);
        } else {
            throw new FlacFrameDecodeException("Cannot decode FLAC frame", flacDecodeToBuffer);
        }
    }

    public long getDecodePosition() {
        return flacGetDecodePosition(this.nativeDecoderContext);
    }

    public long getLastFrameTimestamp() {
        return flacGetLastFrameTimestamp(this.nativeDecoderContext);
    }

    public long getLastFrameFirstSampleIndex() {
        return flacGetLastFrameFirstSampleIndex(this.nativeDecoderContext);
    }

    public long getNextFrameFirstSampleIndex() {
        return flacGetNextFrameFirstSampleIndex(this.nativeDecoderContext);
    }

    public long getSeekPosition(long j) {
        return flacGetSeekPosition(this.nativeDecoderContext, j);
    }

    public String getStateString() {
        return flacGetStateString(this.nativeDecoderContext);
    }

    public boolean isDecoderAtEndOfInput() {
        return flacIsDecoderAtEndOfStream(this.nativeDecoderContext);
    }

    public void flush() {
        flacFlush(this.nativeDecoderContext);
    }

    public void reset(long j) {
        flacReset(this.nativeDecoderContext, j);
    }

    public void release() {
        flacRelease(this.nativeDecoderContext);
    }

    private int readFromExtractorInput(int i, int i2) throws IOException, InterruptedException {
        i = this.extractorInput.read(this.tempBuffer, i, i2);
        if (i != -1) {
            return i;
        }
        this.endOfExtractorInput = true;
        return 0;
    }
}
