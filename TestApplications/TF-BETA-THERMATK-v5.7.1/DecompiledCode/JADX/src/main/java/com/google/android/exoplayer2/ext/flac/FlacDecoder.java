package com.google.android.exoplayer2.ext.flac;

import com.google.android.exoplayer2.decoder.DecoderInputBuffer;
import com.google.android.exoplayer2.decoder.SimpleDecoder;
import com.google.android.exoplayer2.decoder.SimpleOutputBuffer;
import com.google.android.exoplayer2.ext.flac.FlacDecoderJni.FlacFrameDecodeException;
import com.google.android.exoplayer2.util.FlacStreamInfo;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.List;

final class FlacDecoder extends SimpleDecoder<DecoderInputBuffer, SimpleOutputBuffer, FlacDecoderException> {
    private final FlacDecoderJni decoderJni;
    private final int maxOutputBufferSize;

    public String getName() {
        return "libflac";
    }

    public FlacDecoder(int i, int i2, int i3, List<byte[]> list) throws FlacDecoderException {
        super(new DecoderInputBuffer[i], new SimpleOutputBuffer[i2]);
        if (list.size() == 1) {
            this.decoderJni = new FlacDecoderJni();
            this.decoderJni.setData(ByteBuffer.wrap((byte[]) list.get(0)));
            try {
                FlacStreamInfo decodeMetadata = this.decoderJni.decodeMetadata();
                if (decodeMetadata != null) {
                    if (i3 == -1) {
                        i3 = decodeMetadata.maxFrameSize;
                    }
                    setInitialInputBufferSize(i3);
                    this.maxOutputBufferSize = decodeMetadata.maxDecodedFrameSize();
                    return;
                }
                throw new FlacDecoderException("Metadata decoding failed");
            } catch (IOException | InterruptedException e) {
                throw new IllegalStateException(e);
            }
        }
        throw new FlacDecoderException("Initialization data must be of length 1");
    }

    /* Access modifiers changed, original: protected */
    public DecoderInputBuffer createInputBuffer() {
        return new DecoderInputBuffer(1);
    }

    /* Access modifiers changed, original: protected */
    public SimpleOutputBuffer createOutputBuffer() {
        return new SimpleOutputBuffer(this);
    }

    /* Access modifiers changed, original: protected */
    public FlacDecoderException createUnexpectedDecodeException(Throwable th) {
        return new FlacDecoderException("Unexpected decode error", th);
    }

    /* Access modifiers changed, original: protected */
    public FlacDecoderException decode(DecoderInputBuffer decoderInputBuffer, SimpleOutputBuffer simpleOutputBuffer, boolean z) {
        if (z) {
            this.decoderJni.flush();
        }
        this.decoderJni.setData(decoderInputBuffer.data);
        try {
            this.decoderJni.decodeSample(simpleOutputBuffer.init(decoderInputBuffer.timeUs, this.maxOutputBufferSize));
            return null;
        } catch (FlacFrameDecodeException e) {
            return new FlacDecoderException("Frame decoding failed", e);
        } catch (IOException | InterruptedException e2) {
            throw new IllegalStateException(e2);
        }
    }

    public void release() {
        super.release();
        this.decoderJni.release();
    }
}
