// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.video.spherical;

import com.google.android.exoplayer2.util.Util;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.ExoPlaybackException;
import java.nio.ByteBuffer;
import com.google.android.exoplayer2.util.ParsableByteArray;
import com.google.android.exoplayer2.FormatHolder;
import com.google.android.exoplayer2.decoder.DecoderInputBuffer;
import com.google.android.exoplayer2.BaseRenderer;

public class CameraMotionRenderer extends BaseRenderer
{
    private final DecoderInputBuffer buffer;
    private final FormatHolder formatHolder;
    private long lastTimestampUs;
    private CameraMotionListener listener;
    private long offsetUs;
    private final ParsableByteArray scratch;
    
    public CameraMotionRenderer() {
        super(5);
        this.formatHolder = new FormatHolder();
        this.buffer = new DecoderInputBuffer(1);
        this.scratch = new ParsableByteArray();
    }
    
    private float[] parseMetadata(final ByteBuffer byteBuffer) {
        if (byteBuffer.remaining() != 16) {
            return null;
        }
        this.scratch.reset(byteBuffer.array(), byteBuffer.limit());
        this.scratch.setPosition(byteBuffer.arrayOffset() + 4);
        final float[] array = new float[3];
        for (int i = 0; i < 3; ++i) {
            array[i] = Float.intBitsToFloat(this.scratch.readLittleEndianInt());
        }
        return array;
    }
    
    private void resetListener() {
        this.lastTimestampUs = 0L;
        final CameraMotionListener listener = this.listener;
        if (listener != null) {
            listener.onCameraMotionReset();
        }
    }
    
    @Override
    public void handleMessage(final int n, final Object o) throws ExoPlaybackException {
        if (n == 7) {
            this.listener = (CameraMotionListener)o;
        }
        else {
            super.handleMessage(n, o);
        }
    }
    
    @Override
    public boolean isEnded() {
        return this.hasReadStreamToEnd();
    }
    
    @Override
    public boolean isReady() {
        return true;
    }
    
    @Override
    protected void onDisabled() {
        this.resetListener();
    }
    
    @Override
    protected void onPositionReset(final long n, final boolean b) throws ExoPlaybackException {
        this.resetListener();
    }
    
    @Override
    protected void onStreamChanged(final Format[] array, final long offsetUs) throws ExoPlaybackException {
        this.offsetUs = offsetUs;
    }
    
    @Override
    public void render(final long n, final long n2) throws ExoPlaybackException {
        while (!this.hasReadStreamToEnd() && this.lastTimestampUs < 100000L + n) {
            this.buffer.clear();
            if (this.readSource(this.formatHolder, this.buffer, false) != -4) {
                break;
            }
            if (this.buffer.isEndOfStream()) {
                break;
            }
            this.buffer.flip();
            final DecoderInputBuffer buffer = this.buffer;
            this.lastTimestampUs = buffer.timeUs;
            if (this.listener == null) {
                continue;
            }
            final float[] metadata = this.parseMetadata(buffer.data);
            if (metadata == null) {
                continue;
            }
            final CameraMotionListener listener = this.listener;
            Util.castNonNull(listener);
            listener.onCameraMotion(this.lastTimestampUs - this.offsetUs, metadata);
        }
    }
    
    @Override
    public int supportsFormat(final Format format) {
        int n;
        if ("application/x-camera-motion".equals(format.sampleMimeType)) {
            n = 4;
        }
        else {
            n = 0;
        }
        return n;
    }
}
