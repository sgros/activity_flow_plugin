// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.ext.flac;

import com.google.android.exoplayer2.decoder.OutputBuffer;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.List;
import com.google.android.exoplayer2.decoder.SimpleOutputBuffer;
import com.google.android.exoplayer2.decoder.DecoderInputBuffer;
import com.google.android.exoplayer2.decoder.SimpleDecoder;

final class FlacDecoder extends SimpleDecoder<DecoderInputBuffer, SimpleOutputBuffer, FlacDecoderException>
{
    private final FlacDecoderJni decoderJni;
    private final int maxOutputBufferSize;
    
    public FlacDecoder(final int n, final int n2, int maxFrameSize, List<byte[]> decodeMetadata) throws FlacDecoderException {
        super(new DecoderInputBuffer[n], new SimpleOutputBuffer[n2]);
        if (((List)decodeMetadata).size() == 1) {
            (this.decoderJni = new FlacDecoderJni()).setData(ByteBuffer.wrap(((List<byte[]>)decodeMetadata).get(0)));
            try {
                decodeMetadata = this.decoderJni.decodeMetadata();
                if (decodeMetadata != null) {
                    if (maxFrameSize == -1) {
                        maxFrameSize = decodeMetadata.maxFrameSize;
                    }
                    this.setInitialInputBufferSize(maxFrameSize);
                    this.maxOutputBufferSize = decodeMetadata.maxDecodedFrameSize();
                    return;
                }
                throw new FlacDecoderException("Metadata decoding failed");
            }
            catch (InterruptedException decodeMetadata) {}
            catch (IOException ex) {}
            throw new IllegalStateException((Throwable)decodeMetadata);
        }
        throw new FlacDecoderException("Initialization data must be of length 1");
    }
    
    @Override
    protected DecoderInputBuffer createInputBuffer() {
        return new DecoderInputBuffer(1);
    }
    
    @Override
    protected SimpleOutputBuffer createOutputBuffer() {
        return new SimpleOutputBuffer(this);
    }
    
    @Override
    protected FlacDecoderException createUnexpectedDecodeException(final Throwable t) {
        return new FlacDecoderException("Unexpected decode error", t);
    }
    
    @Override
    protected FlacDecoderException decode(DecoderInputBuffer init, final SimpleOutputBuffer simpleOutputBuffer, final boolean b) {
        if (b) {
            this.decoderJni.flush();
        }
        this.decoderJni.setData(((DecoderInputBuffer)init).data);
        init = (InterruptedException)simpleOutputBuffer.init(((DecoderInputBuffer)init).timeUs, this.maxOutputBufferSize);
        try {
            this.decoderJni.decodeSample((ByteBuffer)init);
            return null;
        }
        catch (InterruptedException init) {
            goto Label_0050;
        }
        catch (IOException ex2) {}
        catch (FlacDecoderJni.FlacFrameDecodeException ex) {
            return new FlacDecoderException("Frame decoding failed", ex);
        }
    }
    
    @Override
    public String getName() {
        return "libflac";
    }
    
    @Override
    public void release() {
        super.release();
        this.decoderJni.release();
    }
}
