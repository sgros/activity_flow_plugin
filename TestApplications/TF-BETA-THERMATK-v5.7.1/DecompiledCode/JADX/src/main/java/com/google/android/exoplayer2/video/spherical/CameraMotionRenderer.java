package com.google.android.exoplayer2.video.spherical;

import com.google.android.exoplayer2.BaseRenderer;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.FormatHolder;
import com.google.android.exoplayer2.decoder.DecoderInputBuffer;
import com.google.android.exoplayer2.util.MimeTypes;
import com.google.android.exoplayer2.util.ParsableByteArray;
import com.google.android.exoplayer2.util.Util;
import java.nio.ByteBuffer;

public class CameraMotionRenderer extends BaseRenderer {
    private final DecoderInputBuffer buffer = new DecoderInputBuffer(1);
    private final FormatHolder formatHolder = new FormatHolder();
    private long lastTimestampUs;
    private CameraMotionListener listener;
    private long offsetUs;
    private final ParsableByteArray scratch = new ParsableByteArray();

    public boolean isReady() {
        return true;
    }

    public CameraMotionRenderer() {
        super(5);
    }

    public int supportsFormat(Format format) {
        return MimeTypes.APPLICATION_CAMERA_MOTION.equals(format.sampleMimeType) ? 4 : 0;
    }

    public void handleMessage(int i, Object obj) throws ExoPlaybackException {
        if (i == 7) {
            this.listener = (CameraMotionListener) obj;
        } else {
            super.handleMessage(i, obj);
        }
    }

    /* Access modifiers changed, original: protected */
    public void onStreamChanged(Format[] formatArr, long j) throws ExoPlaybackException {
        this.offsetUs = j;
    }

    /* Access modifiers changed, original: protected */
    public void onPositionReset(long j, boolean z) throws ExoPlaybackException {
        resetListener();
    }

    /* Access modifiers changed, original: protected */
    public void onDisabled() {
        resetListener();
    }

    public void render(long j, long j2) throws ExoPlaybackException {
        while (!hasReadStreamToEnd() && this.lastTimestampUs < 100000 + j) {
            this.buffer.clear();
            if (readSource(this.formatHolder, this.buffer, false) == -4 && !this.buffer.isEndOfStream()) {
                this.buffer.flip();
                DecoderInputBuffer decoderInputBuffer = this.buffer;
                this.lastTimestampUs = decoderInputBuffer.timeUs;
                if (this.listener != null) {
                    float[] parseMetadata = parseMetadata(decoderInputBuffer.data);
                    if (parseMetadata != null) {
                        CameraMotionListener cameraMotionListener = this.listener;
                        Util.castNonNull(cameraMotionListener);
                        cameraMotionListener.onCameraMotion(this.lastTimestampUs - this.offsetUs, parseMetadata);
                    }
                }
            } else {
                return;
            }
        }
    }

    public boolean isEnded() {
        return hasReadStreamToEnd();
    }

    private float[] parseMetadata(ByteBuffer byteBuffer) {
        if (byteBuffer.remaining() != 16) {
            return null;
        }
        this.scratch.reset(byteBuffer.array(), byteBuffer.limit());
        this.scratch.setPosition(byteBuffer.arrayOffset() + 4);
        float[] fArr = new float[3];
        for (int i = 0; i < 3; i++) {
            fArr[i] = Float.intBitsToFloat(this.scratch.readLittleEndianInt());
        }
        return fArr;
    }

    private void resetListener() {
        this.lastTimestampUs = 0;
        CameraMotionListener cameraMotionListener = this.listener;
        if (cameraMotionListener != null) {
            cameraMotionListener.onCameraMotionReset();
        }
    }
}
