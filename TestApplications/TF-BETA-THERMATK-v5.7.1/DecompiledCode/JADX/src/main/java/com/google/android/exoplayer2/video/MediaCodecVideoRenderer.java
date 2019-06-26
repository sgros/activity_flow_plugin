package com.google.android.exoplayer2.video;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Point;
import android.media.MediaCodec;
import android.media.MediaCodec.OnFrameRenderedListener;
import android.media.MediaCrypto;
import android.media.MediaFormat;
import android.os.Handler;
import android.os.SystemClock;
import android.view.Surface;
import com.google.android.exoplayer2.BaseRenderer;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.decoder.DecoderCounters;
import com.google.android.exoplayer2.decoder.DecoderInputBuffer;
import com.google.android.exoplayer2.drm.DrmInitData;
import com.google.android.exoplayer2.drm.DrmSessionManager;
import com.google.android.exoplayer2.drm.FrameworkMediaCrypto;
import com.google.android.exoplayer2.mediacodec.MediaCodecInfo;
import com.google.android.exoplayer2.mediacodec.MediaCodecRenderer;
import com.google.android.exoplayer2.mediacodec.MediaCodecSelector;
import com.google.android.exoplayer2.mediacodec.MediaCodecUtil;
import com.google.android.exoplayer2.mediacodec.MediaCodecUtil.DecoderQueryException;
import com.google.android.exoplayer2.mediacodec.MediaFormatUtil;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.util.Log;
import com.google.android.exoplayer2.util.MimeTypes;
import com.google.android.exoplayer2.util.TraceUtil;
import com.google.android.exoplayer2.util.Util;
import com.google.android.exoplayer2.video.VideoRendererEventListener.EventDispatcher;
import java.nio.ByteBuffer;
import java.util.List;

@TargetApi(16)
public class MediaCodecVideoRenderer extends MediaCodecRenderer {
    private static final int[] STANDARD_LONG_EDGE_VIDEO_PX = new int[]{1920, 1600, 1440, 1280, 960, 854, 640, 540, 480};
    private static boolean deviceNeedsSetOutputSurfaceWorkaround;
    private static boolean evaluatedDeviceNeedsSetOutputSurfaceWorkaround;
    private final long allowedJoiningTimeMs;
    private int buffersInCodecCount;
    private CodecMaxValues codecMaxValues;
    private boolean codecNeedsSetOutputSurfaceWorkaround;
    private int consecutiveDroppedFrameCount;
    private final Context context;
    private int currentHeight;
    private float currentPixelWidthHeightRatio;
    private int currentUnappliedRotationDegrees;
    private int currentWidth;
    private final boolean deviceNeedsNoPostProcessWorkaround;
    private long droppedFrameAccumulationStartTimeMs;
    private int droppedFrames;
    private Surface dummySurface;
    private final EventDispatcher eventDispatcher;
    private VideoFrameMetadataListener frameMetadataListener;
    private final VideoFrameReleaseTimeHelper frameReleaseTimeHelper = new VideoFrameReleaseTimeHelper(this.context);
    private long initialPositionUs;
    private long joiningDeadlineMs;
    private long lastInputTimeUs;
    private long lastRenderTimeUs;
    private final int maxDroppedFramesToNotify;
    private long outputStreamOffsetUs;
    private int pendingOutputStreamOffsetCount;
    private final long[] pendingOutputStreamOffsetsUs;
    private final long[] pendingOutputStreamSwitchTimesUs;
    private float pendingPixelWidthHeightRatio;
    private int pendingRotationDegrees;
    private boolean renderedFirstFrame;
    private int reportedHeight;
    private float reportedPixelWidthHeightRatio;
    private int reportedUnappliedRotationDegrees;
    private int reportedWidth;
    private int scalingMode;
    private Surface surface;
    private boolean tunneling;
    private int tunnelingAudioSessionId;
    OnFrameRenderedListenerV23 tunnelingOnFrameRenderedListener;

    protected static final class CodecMaxValues {
        public final int height;
        public final int inputSize;
        public final int width;

        public CodecMaxValues(int i, int i2, int i3) {
            this.width = i;
            this.height = i2;
            this.inputSize = i3;
        }
    }

    @TargetApi(23)
    private final class OnFrameRenderedListenerV23 implements OnFrameRenderedListener {
        private OnFrameRenderedListenerV23(MediaCodec mediaCodec) {
            mediaCodec.setOnFrameRenderedListener(this, new Handler());
        }

        public void onFrameRendered(MediaCodec mediaCodec, long j, long j2) {
            MediaCodecVideoRenderer mediaCodecVideoRenderer = MediaCodecVideoRenderer.this;
            if (this == mediaCodecVideoRenderer.tunnelingOnFrameRenderedListener) {
                mediaCodecVideoRenderer.onProcessedTunneledBuffer(j);
            }
        }
    }

    private static boolean isBufferLate(long j) {
        return j < -30000;
    }

    private static boolean isBufferVeryLate(long j) {
        return j < -500000;
    }

    public MediaCodecVideoRenderer(Context context, MediaCodecSelector mediaCodecSelector, long j, DrmSessionManager<FrameworkMediaCrypto> drmSessionManager, boolean z, Handler handler, VideoRendererEventListener videoRendererEventListener, int i) {
        super(2, mediaCodecSelector, drmSessionManager, z, 30.0f);
        this.allowedJoiningTimeMs = j;
        this.maxDroppedFramesToNotify = i;
        this.context = context.getApplicationContext();
        this.eventDispatcher = new EventDispatcher(handler, videoRendererEventListener);
        this.deviceNeedsNoPostProcessWorkaround = deviceNeedsNoPostProcessWorkaround();
        this.pendingOutputStreamOffsetsUs = new long[10];
        this.pendingOutputStreamSwitchTimesUs = new long[10];
        this.outputStreamOffsetUs = -9223372036854775807L;
        this.lastInputTimeUs = -9223372036854775807L;
        this.joiningDeadlineMs = -9223372036854775807L;
        this.currentWidth = -1;
        this.currentHeight = -1;
        this.currentPixelWidthHeightRatio = -1.0f;
        this.pendingPixelWidthHeightRatio = -1.0f;
        this.scalingMode = 1;
        clearReportedVideoSize();
    }

    /* Access modifiers changed, original: protected */
    public int supportsFormat(MediaCodecSelector mediaCodecSelector, DrmSessionManager<FrameworkMediaCrypto> drmSessionManager, Format format) throws DecoderQueryException {
        int i = 0;
        if (!MimeTypes.isVideo(format.sampleMimeType)) {
            return 0;
        }
        boolean z;
        DrmInitData drmInitData = format.drmInitData;
        if (drmInitData != null) {
            z = false;
            for (int i2 = 0; i2 < drmInitData.schemeDataCount; i2++) {
                z |= drmInitData.get(i2).requiresSecureDecryption;
            }
        } else {
            z = false;
        }
        List decoderInfos = mediaCodecSelector.getDecoderInfos(format.sampleMimeType, z);
        int i3 = 2;
        if (decoderInfos.isEmpty()) {
            if (!z || mediaCodecSelector.getDecoderInfos(format.sampleMimeType, false).isEmpty()) {
                i3 = 1;
            }
            return i3;
        } else if (!BaseRenderer.supportsFormatDrm(drmSessionManager, drmInitData)) {
            return 2;
        } else {
            MediaCodecInfo mediaCodecInfo = (MediaCodecInfo) decoderInfos.get(0);
            boolean isFormatSupported = mediaCodecInfo.isFormatSupported(format);
            int i4 = mediaCodecInfo.isSeamlessAdaptationSupported(format) ? 16 : 8;
            if (mediaCodecInfo.tunneling) {
                i = 32;
            }
            return (isFormatSupported ? 4 : 3) | (i4 | i);
        }
    }

    /* Access modifiers changed, original: protected */
    public void onEnabled(boolean z) throws ExoPlaybackException {
        super.onEnabled(z);
        int i = this.tunnelingAudioSessionId;
        this.tunnelingAudioSessionId = getConfiguration().tunnelingAudioSessionId;
        this.tunneling = this.tunnelingAudioSessionId != 0;
        if (this.tunnelingAudioSessionId != i) {
            releaseCodec();
        }
        this.eventDispatcher.enabled(this.decoderCounters);
        this.frameReleaseTimeHelper.enable();
    }

    /* Access modifiers changed, original: protected */
    public void onStreamChanged(Format[] formatArr, long j) throws ExoPlaybackException {
        if (this.outputStreamOffsetUs == -9223372036854775807L) {
            this.outputStreamOffsetUs = j;
        } else {
            int i = this.pendingOutputStreamOffsetCount;
            if (i == this.pendingOutputStreamOffsetsUs.length) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Too many stream changes, so dropping offset: ");
                stringBuilder.append(this.pendingOutputStreamOffsetsUs[this.pendingOutputStreamOffsetCount - 1]);
                Log.m18w("MediaCodecVideoRenderer", stringBuilder.toString());
            } else {
                this.pendingOutputStreamOffsetCount = i + 1;
            }
            long[] jArr = this.pendingOutputStreamOffsetsUs;
            int i2 = this.pendingOutputStreamOffsetCount;
            jArr[i2 - 1] = j;
            this.pendingOutputStreamSwitchTimesUs[i2 - 1] = this.lastInputTimeUs;
        }
        super.onStreamChanged(formatArr, j);
    }

    /* Access modifiers changed, original: protected */
    public void onPositionReset(long j, boolean z) throws ExoPlaybackException {
        super.onPositionReset(j, z);
        clearRenderedFirstFrame();
        this.initialPositionUs = -9223372036854775807L;
        this.consecutiveDroppedFrameCount = 0;
        this.lastInputTimeUs = -9223372036854775807L;
        int i = this.pendingOutputStreamOffsetCount;
        if (i != 0) {
            this.outputStreamOffsetUs = this.pendingOutputStreamOffsetsUs[i - 1];
            this.pendingOutputStreamOffsetCount = 0;
        }
        if (z) {
            setJoiningDeadlineMs();
        } else {
            this.joiningDeadlineMs = -9223372036854775807L;
        }
    }

    /* JADX WARNING: Missing block: B:7:0x0016, code skipped:
            if (r9.surface == r0) goto L_0x0022;
     */
    /* JADX WARNING: Missing block: B:11:0x0020, code skipped:
            if (r9.tunneling != false) goto L_0x0022;
     */
    public boolean isReady() {
        /*
        r9 = this;
        r0 = super.isReady();
        r1 = 1;
        r2 = -9223372036854775807; // 0x8000000000000001 float:1.4E-45 double:-4.9E-324;
        if (r0 == 0) goto L_0x0025;
    L_0x000c:
        r0 = r9.renderedFirstFrame;
        if (r0 != 0) goto L_0x0022;
    L_0x0010:
        r0 = r9.dummySurface;
        if (r0 == 0) goto L_0x0018;
    L_0x0014:
        r4 = r9.surface;
        if (r4 == r0) goto L_0x0022;
    L_0x0018:
        r0 = r9.getCodec();
        if (r0 == 0) goto L_0x0022;
    L_0x001e:
        r0 = r9.tunneling;
        if (r0 == 0) goto L_0x0025;
    L_0x0022:
        r9.joiningDeadlineMs = r2;
        return r1;
    L_0x0025:
        r4 = r9.joiningDeadlineMs;
        r0 = 0;
        r6 = (r4 > r2 ? 1 : (r4 == r2 ? 0 : -1));
        if (r6 != 0) goto L_0x002d;
    L_0x002c:
        return r0;
    L_0x002d:
        r4 = android.os.SystemClock.elapsedRealtime();
        r6 = r9.joiningDeadlineMs;
        r8 = (r4 > r6 ? 1 : (r4 == r6 ? 0 : -1));
        if (r8 >= 0) goto L_0x0038;
    L_0x0037:
        return r1;
    L_0x0038:
        r9.joiningDeadlineMs = r2;
        return r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.exoplayer2.video.MediaCodecVideoRenderer.isReady():boolean");
    }

    /* Access modifiers changed, original: protected */
    public void onStarted() {
        super.onStarted();
        this.droppedFrames = 0;
        this.droppedFrameAccumulationStartTimeMs = SystemClock.elapsedRealtime();
        this.lastRenderTimeUs = SystemClock.elapsedRealtime() * 1000;
    }

    /* Access modifiers changed, original: protected */
    public void onStopped() {
        this.joiningDeadlineMs = -9223372036854775807L;
        maybeNotifyDroppedFrames();
        super.onStopped();
    }

    /* Access modifiers changed, original: protected */
    public void onDisabled() {
        this.lastInputTimeUs = -9223372036854775807L;
        this.outputStreamOffsetUs = -9223372036854775807L;
        this.pendingOutputStreamOffsetCount = 0;
        clearReportedVideoSize();
        clearRenderedFirstFrame();
        this.frameReleaseTimeHelper.disable();
        this.tunnelingOnFrameRenderedListener = null;
        try {
            super.onDisabled();
        } finally {
            this.eventDispatcher.disabled(this.decoderCounters);
        }
    }

    /* Access modifiers changed, original: protected */
    public void onReset() {
        try {
            super.onReset();
        } finally {
            Surface surface = this.dummySurface;
            if (surface != null) {
                if (this.surface == surface) {
                    this.surface = null;
                }
                this.dummySurface.release();
                this.dummySurface = null;
            }
        }
    }

    public void handleMessage(int i, Object obj) throws ExoPlaybackException {
        if (i == 1) {
            setSurface((Surface) obj);
        } else if (i == 4) {
            this.scalingMode = ((Integer) obj).intValue();
            MediaCodec codec = getCodec();
            if (codec != null) {
                codec.setVideoScalingMode(this.scalingMode);
            }
        } else if (i == 6) {
            this.frameMetadataListener = (VideoFrameMetadataListener) obj;
        } else {
            super.handleMessage(i, obj);
        }
    }

    private void setSurface(Surface surface) throws ExoPlaybackException {
        if (surface == null) {
            Surface surface2 = this.dummySurface;
            if (surface2 != null) {
                surface = surface2;
            } else {
                MediaCodecInfo codecInfo = getCodecInfo();
                if (codecInfo != null && shouldUseDummySurface(codecInfo)) {
                    this.dummySurface = DummySurface.newInstanceV17(this.context, codecInfo.secure);
                    surface = this.dummySurface;
                }
            }
        }
        if (this.surface != surface) {
            this.surface = surface;
            int state = getState();
            MediaCodec codec = getCodec();
            if (codec != null) {
                if (Util.SDK_INT < 23 || surface == null || this.codecNeedsSetOutputSurfaceWorkaround) {
                    releaseCodec();
                    maybeInitCodec();
                } else {
                    setOutputSurfaceV23(codec, surface);
                }
            }
            if (surface == null || surface == this.dummySurface) {
                clearReportedVideoSize();
                clearRenderedFirstFrame();
                return;
            }
            maybeRenotifyVideoSizeChanged();
            clearRenderedFirstFrame();
            if (state == 2) {
                setJoiningDeadlineMs();
            }
        } else if (surface != null && surface != this.dummySurface) {
            maybeRenotifyVideoSizeChanged();
            maybeRenotifyRenderedFirstFrame();
        }
    }

    /* Access modifiers changed, original: protected */
    public boolean shouldInitCodec(MediaCodecInfo mediaCodecInfo) {
        return this.surface != null || shouldUseDummySurface(mediaCodecInfo);
    }

    /* Access modifiers changed, original: protected */
    public boolean getCodecNeedsEosPropagation() {
        return this.tunneling;
    }

    /* Access modifiers changed, original: protected */
    public void configureCodec(MediaCodecInfo mediaCodecInfo, MediaCodec mediaCodec, Format format, MediaCrypto mediaCrypto, float f) throws DecoderQueryException {
        this.codecMaxValues = getCodecMaxValues(mediaCodecInfo, format, getStreamFormats());
        MediaFormat mediaFormat = getMediaFormat(format, this.codecMaxValues, f, this.deviceNeedsNoPostProcessWorkaround, this.tunnelingAudioSessionId);
        if (this.surface == null) {
            Assertions.checkState(shouldUseDummySurface(mediaCodecInfo));
            if (this.dummySurface == null) {
                this.dummySurface = DummySurface.newInstanceV17(this.context, mediaCodecInfo.secure);
            }
            this.surface = this.dummySurface;
        }
        mediaCodec.configure(mediaFormat, this.surface, mediaCrypto, 0);
        if (Util.SDK_INT >= 23 && this.tunneling) {
            this.tunnelingOnFrameRenderedListener = new OnFrameRenderedListenerV23(mediaCodec);
        }
    }

    /* Access modifiers changed, original: protected */
    public int canKeepCodec(MediaCodec mediaCodec, MediaCodecInfo mediaCodecInfo, Format format, Format format2) {
        if (mediaCodecInfo.isSeamlessAdaptationSupported(format, format2, true)) {
            int i = format2.width;
            CodecMaxValues codecMaxValues = this.codecMaxValues;
            if (i <= codecMaxValues.width && format2.height <= codecMaxValues.height && getMaxInputSize(mediaCodecInfo, format2) <= this.codecMaxValues.inputSize) {
                return format.initializationDataEquals(format2) ? 3 : 2;
            }
        }
        return 0;
    }

    /* Access modifiers changed, original: protected */
    public void releaseCodec() {
        try {
            super.releaseCodec();
        } finally {
            this.buffersInCodecCount = 0;
        }
    }

    /* Access modifiers changed, original: protected */
    public boolean flushOrReleaseCodec() {
        try {
            boolean flushOrReleaseCodec = super.flushOrReleaseCodec();
            return flushOrReleaseCodec;
        } finally {
            this.buffersInCodecCount = 0;
        }
    }

    /* Access modifiers changed, original: protected */
    public float getCodecOperatingRateV23(float f, Format format, Format[] formatArr) {
        float f2 = -1.0f;
        for (Format format2 : formatArr) {
            float f3 = format2.frameRate;
            if (f3 != -1.0f) {
                f2 = Math.max(f2, f3);
            }
        }
        if (f2 == -1.0f) {
            return -1.0f;
        }
        return f2 * f;
    }

    /* Access modifiers changed, original: protected */
    public void onCodecInitialized(String str, long j, long j2) {
        this.eventDispatcher.decoderInitialized(str, j, j2);
        this.codecNeedsSetOutputSurfaceWorkaround = codecNeedsSetOutputSurfaceWorkaround(str);
    }

    /* Access modifiers changed, original: protected */
    public void onInputFormatChanged(Format format) throws ExoPlaybackException {
        super.onInputFormatChanged(format);
        this.eventDispatcher.inputFormatChanged(format);
        this.pendingPixelWidthHeightRatio = format.pixelWidthHeightRatio;
        this.pendingRotationDegrees = format.rotationDegrees;
    }

    /* Access modifiers changed, original: protected */
    public void onQueueInputBuffer(DecoderInputBuffer decoderInputBuffer) {
        this.buffersInCodecCount++;
        this.lastInputTimeUs = Math.max(decoderInputBuffer.timeUs, this.lastInputTimeUs);
        if (Util.SDK_INT < 23 && this.tunneling) {
            onProcessedTunneledBuffer(decoderInputBuffer.timeUs);
        }
    }

    /* Access modifiers changed, original: protected */
    public void onOutputFormatChanged(MediaCodec mediaCodec, MediaFormat mediaFormat) {
        int integer;
        int integer2;
        String str = "crop-right";
        String str2 = "crop-top";
        String str3 = "crop-bottom";
        String str4 = "crop-left";
        Object obj = (mediaFormat.containsKey(str) && mediaFormat.containsKey(str4) && mediaFormat.containsKey(str3) && mediaFormat.containsKey(str2)) ? 1 : null;
        if (obj != null) {
            integer = (mediaFormat.getInteger(str) - mediaFormat.getInteger(str4)) + 1;
        } else {
            integer = mediaFormat.getInteger("width");
        }
        if (obj != null) {
            integer2 = (mediaFormat.getInteger(str3) - mediaFormat.getInteger(str2)) + 1;
        } else {
            integer2 = mediaFormat.getInteger("height");
        }
        processOutputFormat(mediaCodec, integer, integer2);
    }

    /* Access modifiers changed, original: protected */
    public boolean processOutputBuffer(long j, long j2, MediaCodec mediaCodec, ByteBuffer byteBuffer, int i, int i2, long j3, boolean z, Format format) throws ExoPlaybackException {
        long j4 = j;
        long j5 = j2;
        MediaCodec mediaCodec2 = mediaCodec;
        int i3 = i;
        long j6 = j3;
        if (this.initialPositionUs == -9223372036854775807L) {
            this.initialPositionUs = j4;
        }
        long j7 = j6 - this.outputStreamOffsetUs;
        if (z) {
            skipOutputBuffer(mediaCodec2, i3, j7);
            return true;
        }
        long j8 = j6 - j4;
        if (this.surface != this.dummySurface) {
            long elapsedRealtime = SystemClock.elapsedRealtime() * 1000;
            Object obj = getState() == 2 ? 1 : null;
            if (!this.renderedFirstFrame || (obj != null && shouldForceRenderOutputBuffer(j8, elapsedRealtime - this.lastRenderTimeUs))) {
                j5 = System.nanoTime();
                notifyFrameMetadataListener(j7, j5, format);
                if (Util.SDK_INT >= 21) {
                    renderOutputBufferV21(mediaCodec, i, j7, j5);
                } else {
                    renderOutputBuffer(mediaCodec2, i3, j7);
                }
                return true;
            }
            if (!(obj == null || j4 == this.initialPositionUs)) {
                j8 -= elapsedRealtime - j5;
                long nanoTime = System.nanoTime();
                elapsedRealtime = this.frameReleaseTimeHelper.adjustReleaseTime(j6, (j8 * 1000) + nanoTime);
                nanoTime = (elapsedRealtime - nanoTime) / 1000;
                if (shouldDropBuffersToKeyframe(nanoTime, j5) && maybeDropBuffersToKeyframe(mediaCodec, i, j7, j)) {
                    return false;
                }
                if (shouldDropOutputBuffer(nanoTime, j5)) {
                    dropOutputBuffer(mediaCodec2, i3, j7);
                } else if (Util.SDK_INT >= 21) {
                    if (nanoTime < 50000) {
                        notifyFrameMetadataListener(j7, elapsedRealtime, format);
                        renderOutputBufferV21(mediaCodec, i, j7, elapsedRealtime);
                    }
                } else if (nanoTime < 30000) {
                    if (nanoTime > 11000) {
                        try {
                            Thread.sleep((nanoTime - 10000) / 1000);
                        } catch (InterruptedException unused) {
                            Thread.currentThread().interrupt();
                            return false;
                        }
                    }
                    notifyFrameMetadataListener(j7, elapsedRealtime, format);
                    renderOutputBuffer(mediaCodec2, i3, j7);
                }
                return true;
            }
            return false;
        } else if (!isBufferLate(j8)) {
            return false;
        } else {
            skipOutputBuffer(mediaCodec2, i3, j7);
            return true;
        }
    }

    private void processOutputFormat(MediaCodec mediaCodec, int i, int i2) {
        this.currentWidth = i;
        this.currentHeight = i2;
        this.currentPixelWidthHeightRatio = this.pendingPixelWidthHeightRatio;
        if (Util.SDK_INT >= 21) {
            i = this.pendingRotationDegrees;
            if (i == 90 || i == 270) {
                i = this.currentWidth;
                this.currentWidth = this.currentHeight;
                this.currentHeight = i;
                this.currentPixelWidthHeightRatio = 1.0f / this.currentPixelWidthHeightRatio;
            }
        } else {
            this.currentUnappliedRotationDegrees = this.pendingRotationDegrees;
        }
        mediaCodec.setVideoScalingMode(this.scalingMode);
    }

    private void notifyFrameMetadataListener(long j, long j2, Format format) {
        VideoFrameMetadataListener videoFrameMetadataListener = this.frameMetadataListener;
        if (videoFrameMetadataListener != null) {
            videoFrameMetadataListener.onVideoFrameAboutToBeRendered(j, j2, format);
        }
    }

    /* Access modifiers changed, original: protected */
    public void onProcessedTunneledBuffer(long j) {
        Format updateOutputFormatForTime = updateOutputFormatForTime(j);
        if (updateOutputFormatForTime != null) {
            processOutputFormat(getCodec(), updateOutputFormatForTime.width, updateOutputFormatForTime.height);
        }
        maybeNotifyVideoSizeChanged();
        maybeNotifyRenderedFirstFrame();
        onProcessedOutputBuffer(j);
    }

    /* Access modifiers changed, original: protected */
    public void onProcessedOutputBuffer(long j) {
        this.buffersInCodecCount--;
        while (true) {
            int i = this.pendingOutputStreamOffsetCount;
            if (i != 0 && j >= this.pendingOutputStreamSwitchTimesUs[0]) {
                long[] jArr = this.pendingOutputStreamOffsetsUs;
                this.outputStreamOffsetUs = jArr[0];
                this.pendingOutputStreamOffsetCount = i - 1;
                System.arraycopy(jArr, 1, jArr, 0, this.pendingOutputStreamOffsetCount);
                long[] jArr2 = this.pendingOutputStreamSwitchTimesUs;
                System.arraycopy(jArr2, 1, jArr2, 0, this.pendingOutputStreamOffsetCount);
            } else {
                return;
            }
        }
    }

    /* Access modifiers changed, original: protected */
    public boolean shouldDropOutputBuffer(long j, long j2) {
        return isBufferLate(j);
    }

    /* Access modifiers changed, original: protected */
    public boolean shouldDropBuffersToKeyframe(long j, long j2) {
        return isBufferVeryLate(j);
    }

    /* Access modifiers changed, original: protected */
    public boolean shouldForceRenderOutputBuffer(long j, long j2) {
        return isBufferLate(j) && j2 > 100000;
    }

    /* Access modifiers changed, original: protected */
    public void skipOutputBuffer(MediaCodec mediaCodec, int i, long j) {
        TraceUtil.beginSection("skipVideoBuffer");
        mediaCodec.releaseOutputBuffer(i, false);
        TraceUtil.endSection();
        DecoderCounters decoderCounters = this.decoderCounters;
        decoderCounters.skippedOutputBufferCount++;
    }

    /* Access modifiers changed, original: protected */
    public void dropOutputBuffer(MediaCodec mediaCodec, int i, long j) {
        TraceUtil.beginSection("dropVideoBuffer");
        mediaCodec.releaseOutputBuffer(i, false);
        TraceUtil.endSection();
        updateDroppedBufferCounters(1);
    }

    /* Access modifiers changed, original: protected */
    public boolean maybeDropBuffersToKeyframe(MediaCodec mediaCodec, int i, long j, long j2) throws ExoPlaybackException {
        int skipSource = skipSource(j2);
        if (skipSource == 0) {
            return false;
        }
        DecoderCounters decoderCounters = this.decoderCounters;
        decoderCounters.droppedToKeyframeCount++;
        updateDroppedBufferCounters(this.buffersInCodecCount + skipSource);
        flushOrReinitCodec();
        return true;
    }

    /* Access modifiers changed, original: protected */
    public void updateDroppedBufferCounters(int i) {
        DecoderCounters decoderCounters = this.decoderCounters;
        decoderCounters.droppedBufferCount += i;
        this.droppedFrames += i;
        this.consecutiveDroppedFrameCount += i;
        decoderCounters.maxConsecutiveDroppedBufferCount = Math.max(this.consecutiveDroppedFrameCount, decoderCounters.maxConsecutiveDroppedBufferCount);
        i = this.maxDroppedFramesToNotify;
        if (i > 0 && this.droppedFrames >= i) {
            maybeNotifyDroppedFrames();
        }
    }

    /* Access modifiers changed, original: protected */
    public void renderOutputBuffer(MediaCodec mediaCodec, int i, long j) {
        maybeNotifyVideoSizeChanged();
        TraceUtil.beginSection("releaseOutputBuffer");
        mediaCodec.releaseOutputBuffer(i, true);
        TraceUtil.endSection();
        this.lastRenderTimeUs = SystemClock.elapsedRealtime() * 1000;
        DecoderCounters decoderCounters = this.decoderCounters;
        decoderCounters.renderedOutputBufferCount++;
        this.consecutiveDroppedFrameCount = 0;
        maybeNotifyRenderedFirstFrame();
    }

    /* Access modifiers changed, original: protected */
    @TargetApi(21)
    public void renderOutputBufferV21(MediaCodec mediaCodec, int i, long j, long j2) {
        maybeNotifyVideoSizeChanged();
        TraceUtil.beginSection("releaseOutputBuffer");
        mediaCodec.releaseOutputBuffer(i, j2);
        TraceUtil.endSection();
        this.lastRenderTimeUs = SystemClock.elapsedRealtime() * 1000;
        DecoderCounters decoderCounters = this.decoderCounters;
        decoderCounters.renderedOutputBufferCount++;
        this.consecutiveDroppedFrameCount = 0;
        maybeNotifyRenderedFirstFrame();
    }

    private boolean shouldUseDummySurface(MediaCodecInfo mediaCodecInfo) {
        return Util.SDK_INT >= 23 && !this.tunneling && !codecNeedsSetOutputSurfaceWorkaround(mediaCodecInfo.name) && (!mediaCodecInfo.secure || DummySurface.isSecureSupported(this.context));
    }

    private void setJoiningDeadlineMs() {
        this.joiningDeadlineMs = this.allowedJoiningTimeMs > 0 ? SystemClock.elapsedRealtime() + this.allowedJoiningTimeMs : -9223372036854775807L;
    }

    private void clearRenderedFirstFrame() {
        this.renderedFirstFrame = false;
        if (Util.SDK_INT >= 23 && this.tunneling) {
            MediaCodec codec = getCodec();
            if (codec != null) {
                this.tunnelingOnFrameRenderedListener = new OnFrameRenderedListenerV23(codec);
            }
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void maybeNotifyRenderedFirstFrame() {
        if (!this.renderedFirstFrame) {
            this.renderedFirstFrame = true;
            this.eventDispatcher.renderedFirstFrame(this.surface);
        }
    }

    private void maybeRenotifyRenderedFirstFrame() {
        if (this.renderedFirstFrame) {
            this.eventDispatcher.renderedFirstFrame(this.surface);
        }
    }

    private void clearReportedVideoSize() {
        this.reportedWidth = -1;
        this.reportedHeight = -1;
        this.reportedPixelWidthHeightRatio = -1.0f;
        this.reportedUnappliedRotationDegrees = -1;
    }

    private void maybeNotifyVideoSizeChanged() {
        if (this.currentWidth != -1 || this.currentHeight != -1) {
            if (this.reportedWidth != this.currentWidth || this.reportedHeight != this.currentHeight || this.reportedUnappliedRotationDegrees != this.currentUnappliedRotationDegrees || this.reportedPixelWidthHeightRatio != this.currentPixelWidthHeightRatio) {
                this.eventDispatcher.videoSizeChanged(this.currentWidth, this.currentHeight, this.currentUnappliedRotationDegrees, this.currentPixelWidthHeightRatio);
                this.reportedWidth = this.currentWidth;
                this.reportedHeight = this.currentHeight;
                this.reportedUnappliedRotationDegrees = this.currentUnappliedRotationDegrees;
                this.reportedPixelWidthHeightRatio = this.currentPixelWidthHeightRatio;
            }
        }
    }

    private void maybeRenotifyVideoSizeChanged() {
        if (this.reportedWidth != -1 || this.reportedHeight != -1) {
            this.eventDispatcher.videoSizeChanged(this.reportedWidth, this.reportedHeight, this.reportedUnappliedRotationDegrees, this.reportedPixelWidthHeightRatio);
        }
    }

    private void maybeNotifyDroppedFrames() {
        if (this.droppedFrames > 0) {
            long elapsedRealtime = SystemClock.elapsedRealtime();
            this.eventDispatcher.droppedFrames(this.droppedFrames, elapsedRealtime - this.droppedFrameAccumulationStartTimeMs);
            this.droppedFrames = 0;
            this.droppedFrameAccumulationStartTimeMs = elapsedRealtime;
        }
    }

    @TargetApi(23)
    private static void setOutputSurfaceV23(MediaCodec mediaCodec, Surface surface) {
        mediaCodec.setOutputSurface(surface);
    }

    @TargetApi(21)
    private static void configureTunnelingV21(MediaFormat mediaFormat, int i) {
        mediaFormat.setFeatureEnabled("tunneled-playback", true);
        mediaFormat.setInteger("audio-session-id", i);
    }

    /* Access modifiers changed, original: protected */
    @SuppressLint({"InlinedApi"})
    public MediaFormat getMediaFormat(Format format, CodecMaxValues codecMaxValues, float f, boolean z, int i) {
        MediaFormat mediaFormat = new MediaFormat();
        mediaFormat.setString("mime", format.sampleMimeType);
        mediaFormat.setInteger("width", format.width);
        mediaFormat.setInteger("height", format.height);
        MediaFormatUtil.setCsdBuffers(mediaFormat, format.initializationData);
        MediaFormatUtil.maybeSetFloat(mediaFormat, "frame-rate", format.frameRate);
        MediaFormatUtil.maybeSetInteger(mediaFormat, "rotation-degrees", format.rotationDegrees);
        MediaFormatUtil.maybeSetColorInfo(mediaFormat, format.colorInfo);
        mediaFormat.setInteger("max-width", codecMaxValues.width);
        mediaFormat.setInteger("max-height", codecMaxValues.height);
        MediaFormatUtil.maybeSetInteger(mediaFormat, "max-input-size", codecMaxValues.inputSize);
        if (Util.SDK_INT >= 23) {
            mediaFormat.setInteger("priority", 0);
            if (f != -1.0f) {
                mediaFormat.setFloat("operating-rate", f);
            }
        }
        if (z) {
            mediaFormat.setInteger("no-post-process", 1);
            mediaFormat.setInteger("auto-frc", 0);
        }
        if (i != 0) {
            configureTunnelingV21(mediaFormat, i);
        }
        return mediaFormat;
    }

    /* Access modifiers changed, original: protected */
    public CodecMaxValues getCodecMaxValues(MediaCodecInfo mediaCodecInfo, Format format, Format[] formatArr) throws DecoderQueryException {
        int i = format.width;
        int i2 = format.height;
        int maxInputSize = getMaxInputSize(mediaCodecInfo, format);
        if (formatArr.length == 1) {
            if (maxInputSize != -1) {
                int codecMaxInputSize = getCodecMaxInputSize(mediaCodecInfo, format.sampleMimeType, format.width, format.height);
                if (codecMaxInputSize != -1) {
                    maxInputSize = Math.min((int) (((float) maxInputSize) * 1.5f), codecMaxInputSize);
                }
            }
            return new CodecMaxValues(i, i2, maxInputSize);
        }
        int i3 = i2;
        int i4 = maxInputSize;
        i2 = 0;
        maxInputSize = i;
        for (Format format2 : formatArr) {
            if (mediaCodecInfo.isSeamlessAdaptationSupported(format, format2, false)) {
                int i5 = (format2.width == -1 || format2.height == -1) ? 1 : 0;
                i2 |= i5;
                maxInputSize = Math.max(maxInputSize, format2.width);
                i3 = Math.max(i3, format2.height);
                i4 = Math.max(i4, getMaxInputSize(mediaCodecInfo, format2));
            }
        }
        if (i2 != 0) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Resolutions unknown. Codec max resolution: ");
            stringBuilder.append(maxInputSize);
            String str = "x";
            stringBuilder.append(str);
            stringBuilder.append(i3);
            String str2 = "MediaCodecVideoRenderer";
            Log.m18w(str2, stringBuilder.toString());
            Point codecMaxSize = getCodecMaxSize(mediaCodecInfo, format);
            if (codecMaxSize != null) {
                maxInputSize = Math.max(maxInputSize, codecMaxSize.x);
                i3 = Math.max(i3, codecMaxSize.y);
                i4 = Math.max(i4, getCodecMaxInputSize(mediaCodecInfo, format.sampleMimeType, maxInputSize, i3));
                StringBuilder stringBuilder2 = new StringBuilder();
                stringBuilder2.append("Codec max resolution adjusted to: ");
                stringBuilder2.append(maxInputSize);
                stringBuilder2.append(str);
                stringBuilder2.append(i3);
                Log.m18w(str2, stringBuilder2.toString());
            }
        }
        return new CodecMaxValues(maxInputSize, i3, i4);
    }

    private static Point getCodecMaxSize(MediaCodecInfo mediaCodecInfo, Format format) throws DecoderQueryException {
        Object obj = format.height > format.width ? 1 : null;
        int i = obj != null ? format.height : format.width;
        int i2 = obj != null ? format.width : format.height;
        float f = ((float) i2) / ((float) i);
        for (int i3 : STANDARD_LONG_EDGE_VIDEO_PX) {
            int i32;
            int i4 = (int) (((float) i32) * f);
            if (i32 <= i || i4 <= i2) {
                break;
            }
            int i5;
            if (Util.SDK_INT >= 21) {
                i5 = obj != null ? i4 : i32;
                if (obj == null) {
                    i32 = i4;
                }
                Point alignVideoSizeV21 = mediaCodecInfo.alignVideoSizeV21(i5, i32);
                if (mediaCodecInfo.isVideoSizeAndRateSupportedV21(alignVideoSizeV21.x, alignVideoSizeV21.y, (double) format.frameRate)) {
                    return alignVideoSizeV21;
                }
            } else {
                i32 = Util.ceilDivide(i32, 16) * 16;
                i5 = Util.ceilDivide(i4, 16) * 16;
                if (i32 * i5 <= MediaCodecUtil.maxH264DecodableFrameSize()) {
                    int i6 = obj != null ? i5 : i32;
                    if (obj != null) {
                        i5 = i32;
                    }
                    return new Point(i6, i5);
                }
            }
        }
        return null;
    }

    private static int getMaxInputSize(MediaCodecInfo mediaCodecInfo, Format format) {
        if (format.maxInputSize == -1) {
            return getCodecMaxInputSize(mediaCodecInfo, format.sampleMimeType, format.width, format.height);
        }
        int i = 0;
        for (int i2 = 0; i2 < format.initializationData.size(); i2++) {
            i += ((byte[]) format.initializationData.get(i2)).length;
        }
        return format.maxInputSize + i;
    }

    /* JADX WARNING: Missing block: B:41:0x008b, code skipped:
            if (r7.secure != false) goto L_0x009f;
     */
    private static int getCodecMaxInputSize(com.google.android.exoplayer2.mediacodec.MediaCodecInfo r7, java.lang.String r8, int r9, int r10) {
        /*
        r0 = -1;
        if (r9 == r0) goto L_0x00a9;
    L_0x0003:
        if (r10 != r0) goto L_0x0007;
    L_0x0005:
        goto L_0x00a9;
    L_0x0007:
        r1 = r8.hashCode();
        r2 = 5;
        r3 = 1;
        r4 = 4;
        r5 = 3;
        r6 = 2;
        switch(r1) {
            case -1664118616: goto L_0x0046;
            case -1662541442: goto L_0x003c;
            case 1187890754: goto L_0x0032;
            case 1331836730: goto L_0x0028;
            case 1599127256: goto L_0x001e;
            case 1599127257: goto L_0x0014;
            default: goto L_0x0013;
        };
    L_0x0013:
        goto L_0x0050;
    L_0x0014:
        r1 = "video/x-vnd.on2.vp9";
        r8 = r8.equals(r1);
        if (r8 == 0) goto L_0x0050;
    L_0x001c:
        r8 = 5;
        goto L_0x0051;
    L_0x001e:
        r1 = "video/x-vnd.on2.vp8";
        r8 = r8.equals(r1);
        if (r8 == 0) goto L_0x0050;
    L_0x0026:
        r8 = 3;
        goto L_0x0051;
    L_0x0028:
        r1 = "video/avc";
        r8 = r8.equals(r1);
        if (r8 == 0) goto L_0x0050;
    L_0x0030:
        r8 = 2;
        goto L_0x0051;
    L_0x0032:
        r1 = "video/mp4v-es";
        r8 = r8.equals(r1);
        if (r8 == 0) goto L_0x0050;
    L_0x003a:
        r8 = 1;
        goto L_0x0051;
    L_0x003c:
        r1 = "video/hevc";
        r8 = r8.equals(r1);
        if (r8 == 0) goto L_0x0050;
    L_0x0044:
        r8 = 4;
        goto L_0x0051;
    L_0x0046:
        r1 = "video/3gpp";
        r8 = r8.equals(r1);
        if (r8 == 0) goto L_0x0050;
    L_0x004e:
        r8 = 0;
        goto L_0x0051;
    L_0x0050:
        r8 = -1;
    L_0x0051:
        if (r8 == 0) goto L_0x00a0;
    L_0x0053:
        if (r8 == r3) goto L_0x00a0;
    L_0x0055:
        if (r8 == r6) goto L_0x0061;
    L_0x0057:
        if (r8 == r5) goto L_0x00a0;
    L_0x0059:
        if (r8 == r4) goto L_0x005e;
    L_0x005b:
        if (r8 == r2) goto L_0x005e;
    L_0x005d:
        return r0;
    L_0x005e:
        r9 = r9 * r10;
        goto L_0x00a3;
    L_0x0061:
        r8 = com.google.android.exoplayer2.util.Util.MODEL;
        r1 = "BRAVIA 4K 2015";
        r8 = r1.equals(r8);
        if (r8 != 0) goto L_0x009f;
    L_0x006b:
        r8 = com.google.android.exoplayer2.util.Util.MANUFACTURER;
        r1 = "Amazon";
        r8 = r1.equals(r8);
        if (r8 == 0) goto L_0x008e;
    L_0x0075:
        r8 = com.google.android.exoplayer2.util.Util.MODEL;
        r1 = "KFSOWI";
        r8 = r1.equals(r8);
        if (r8 != 0) goto L_0x009f;
    L_0x007f:
        r8 = com.google.android.exoplayer2.util.Util.MODEL;
        r1 = "AFTS";
        r8 = r1.equals(r8);
        if (r8 == 0) goto L_0x008e;
    L_0x0089:
        r7 = r7.secure;
        if (r7 == 0) goto L_0x008e;
    L_0x008d:
        goto L_0x009f;
    L_0x008e:
        r7 = 16;
        r8 = com.google.android.exoplayer2.util.Util.ceilDivide(r9, r7);
        r9 = com.google.android.exoplayer2.util.Util.ceilDivide(r10, r7);
        r8 = r8 * r9;
        r8 = r8 * 16;
        r9 = r8 * 16;
        goto L_0x00a2;
    L_0x009f:
        return r0;
    L_0x00a0:
        r9 = r9 * r10;
    L_0x00a2:
        r4 = 2;
    L_0x00a3:
        r9 = r9 * 3;
        r4 = r4 * 2;
        r9 = r9 / r4;
        return r9;
    L_0x00a9:
        return r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.exoplayer2.video.MediaCodecVideoRenderer.getCodecMaxInputSize(com.google.android.exoplayer2.mediacodec.MediaCodecInfo, java.lang.String, int, int):int");
    }

    private static boolean deviceNeedsNoPostProcessWorkaround() {
        return "NVIDIA".equals(Util.MANUFACTURER);
    }

    /* Access modifiers changed, original: protected */
    /* JADX WARNING: Missing block: B:391:0x05f7, code skipped:
            r1 = -1;
     */
    /* JADX WARNING: Missing block: B:392:0x05f8, code skipped:
            switch(r1) {
                case 0: goto L_0x05fc;
                case 1: goto L_0x05fc;
                case 2: goto L_0x05fc;
                case 3: goto L_0x05fc;
                case 4: goto L_0x05fc;
                case 5: goto L_0x05fc;
                case 6: goto L_0x05fc;
                case 7: goto L_0x05fc;
                case 8: goto L_0x05fc;
                case 9: goto L_0x05fc;
                case 10: goto L_0x05fc;
                case 11: goto L_0x05fc;
                case 12: goto L_0x05fc;
                case 13: goto L_0x05fc;
                case 14: goto L_0x05fc;
                case 15: goto L_0x05fc;
                case 16: goto L_0x05fc;
                case 17: goto L_0x05fc;
                case 18: goto L_0x05fc;
                case 19: goto L_0x05fc;
                case 20: goto L_0x05fc;
                case 21: goto L_0x05fc;
                case 22: goto L_0x05fc;
                case 23: goto L_0x05fc;
                case 24: goto L_0x05fc;
                case 25: goto L_0x05fc;
                case 26: goto L_0x05fc;
                case 27: goto L_0x05fc;
                case 28: goto L_0x05fc;
                case 29: goto L_0x05fc;
                case 30: goto L_0x05fc;
                case 31: goto L_0x05fc;
                case 32: goto L_0x05fc;
                case 33: goto L_0x05fc;
                case 34: goto L_0x05fc;
                case 35: goto L_0x05fc;
                case 36: goto L_0x05fc;
                case 37: goto L_0x05fc;
                case 38: goto L_0x05fc;
                case 39: goto L_0x05fc;
                case 40: goto L_0x05fc;
                case 41: goto L_0x05fc;
                case 42: goto L_0x05fc;
                case 43: goto L_0x05fc;
                case 44: goto L_0x05fc;
                case 45: goto L_0x05fc;
                case 46: goto L_0x05fc;
                case 47: goto L_0x05fc;
                case 48: goto L_0x05fc;
                case 49: goto L_0x05fc;
                case 50: goto L_0x05fc;
                case 51: goto L_0x05fc;
                case 52: goto L_0x05fc;
                case 53: goto L_0x05fc;
                case 54: goto L_0x05fc;
                case 55: goto L_0x05fc;
                case 56: goto L_0x05fc;
                case 57: goto L_0x05fc;
                case 58: goto L_0x05fc;
                case 59: goto L_0x05fc;
                case 60: goto L_0x05fc;
                case 61: goto L_0x05fc;
                case 62: goto L_0x05fc;
                case 63: goto L_0x05fc;
                case 64: goto L_0x05fc;
                case 65: goto L_0x05fc;
                case 66: goto L_0x05fc;
                case 67: goto L_0x05fc;
                case 68: goto L_0x05fc;
                case 69: goto L_0x05fc;
                case 70: goto L_0x05fc;
                case 71: goto L_0x05fc;
                case 72: goto L_0x05fc;
                case 73: goto L_0x05fc;
                case 74: goto L_0x05fc;
                case 75: goto L_0x05fc;
                case 76: goto L_0x05fc;
                case 77: goto L_0x05fc;
                case 78: goto L_0x05fc;
                case 79: goto L_0x05fc;
                case 80: goto L_0x05fc;
                case 81: goto L_0x05fc;
                case 82: goto L_0x05fc;
                case 83: goto L_0x05fc;
                case 84: goto L_0x05fc;
                case 85: goto L_0x05fc;
                case 86: goto L_0x05fc;
                case 87: goto L_0x05fc;
                case 88: goto L_0x05fc;
                case 89: goto L_0x05fc;
                case 90: goto L_0x05fc;
                case 91: goto L_0x05fc;
                case 92: goto L_0x05fc;
                case 93: goto L_0x05fc;
                case 94: goto L_0x05fc;
                case 95: goto L_0x05fc;
                case 96: goto L_0x05fc;
                case 97: goto L_0x05fc;
                case 98: goto L_0x05fc;
                case 99: goto L_0x05fc;
                case 100: goto L_0x05fc;
                case 101: goto L_0x05fc;
                case 102: goto L_0x05fc;
                case 103: goto L_0x05fc;
                case 104: goto L_0x05fc;
                case 105: goto L_0x05fc;
                case 106: goto L_0x05fc;
                case 107: goto L_0x05fc;
                case 108: goto L_0x05fc;
                case 109: goto L_0x05fc;
                case 110: goto L_0x05fc;
                case 111: goto L_0x05fc;
                case 112: goto L_0x05fc;
                case 113: goto L_0x05fc;
                case 114: goto L_0x05fc;
                case 115: goto L_0x05fc;
                case 116: goto L_0x05fc;
                case 117: goto L_0x05fc;
                case 118: goto L_0x05fc;
                case 119: goto L_0x05fc;
                case 120: goto L_0x05fc;
                case 121: goto L_0x05fc;
                case 122: goto L_0x05fc;
                case 123: goto L_0x05fc;
                default: goto L_0x05fb;
            };
     */
    /* JADX WARNING: Missing block: B:394:0x05fc, code skipped:
            deviceNeedsSetOutputSurfaceWorkaround = true;
     */
    /* JADX WARNING: Missing block: B:395:0x05fe, code skipped:
            r1 = com.google.android.exoplayer2.util.Util.MODEL;
            r2 = r1.hashCode();
     */
    /* JADX WARNING: Missing block: B:396:0x0607, code skipped:
            if (r2 == 2006354) goto L_0x0619;
     */
    /* JADX WARNING: Missing block: B:398:0x060c, code skipped:
            if (r2 == 2006367) goto L_0x060f;
     */
    /* JADX WARNING: Missing block: B:401:0x0615, code skipped:
            if (r1.equals("AFTN") == false) goto L_0x0622;
     */
    /* JADX WARNING: Missing block: B:402:0x0617, code skipped:
            r0 = true;
     */
    /* JADX WARNING: Missing block: B:404:0x061f, code skipped:
            if (r1.equals("AFTA") == false) goto L_0x0622;
     */
    /* JADX WARNING: Missing block: B:406:0x0622, code skipped:
            r0 = true;
     */
    /* JADX WARNING: Missing block: B:407:0x0623, code skipped:
            if (r0 == false) goto L_0x0628;
     */
    /* JADX WARNING: Missing block: B:408:0x0625, code skipped:
            if (r0 == true) goto L_0x0628;
     */
    /* JADX WARNING: Missing block: B:410:0x0628, code skipped:
            deviceNeedsSetOutputSurfaceWorkaround = true;
     */
    public boolean codecNeedsSetOutputSurfaceWorkaround(java.lang.String r7) {
        /*
        r6 = this;
        r0 = "OMX.google";
        r7 = r7.startsWith(r0);
        r0 = 0;
        if (r7 == 0) goto L_0x000a;
    L_0x0009:
        return r0;
    L_0x000a:
        r7 = com.google.android.exoplayer2.video.MediaCodecVideoRenderer.class;
        monitor-enter(r7);
        r1 = evaluatedDeviceNeedsSetOutputSurfaceWorkaround;	 Catch:{ all -> 0x0630 }
        if (r1 != 0) goto L_0x062c;
    L_0x0011:
        r1 = com.google.android.exoplayer2.util.Util.SDK_INT;	 Catch:{ all -> 0x0630 }
        r2 = 27;
        r3 = 1;
        if (r1 > r2) goto L_0x0026;
    L_0x0018:
        r1 = "dangal";
        r4 = com.google.android.exoplayer2.util.Util.DEVICE;	 Catch:{ all -> 0x0630 }
        r1 = r1.equals(r4);	 Catch:{ all -> 0x0630 }
        if (r1 == 0) goto L_0x0026;
    L_0x0022:
        deviceNeedsSetOutputSurfaceWorkaround = r3;	 Catch:{ all -> 0x0630 }
        goto L_0x062a;
    L_0x0026:
        r1 = com.google.android.exoplayer2.util.Util.SDK_INT;	 Catch:{ all -> 0x0630 }
        if (r1 < r2) goto L_0x002c;
    L_0x002a:
        goto L_0x062a;
    L_0x002c:
        r1 = com.google.android.exoplayer2.util.Util.DEVICE;	 Catch:{ all -> 0x0630 }
        r4 = r1.hashCode();	 Catch:{ all -> 0x0630 }
        r5 = -1;
        switch(r4) {
            case -2144781245: goto L_0x05ec;
            case -2144781185: goto L_0x05e1;
            case -2144781160: goto L_0x05d6;
            case -2097309513: goto L_0x05cb;
            case -2022874474: goto L_0x05c0;
            case -1978993182: goto L_0x05b5;
            case -1978990237: goto L_0x05aa;
            case -1936688988: goto L_0x059f;
            case -1936688066: goto L_0x0594;
            case -1936688065: goto L_0x0588;
            case -1931988508: goto L_0x057c;
            case -1696512866: goto L_0x0570;
            case -1680025915: goto L_0x0564;
            case -1615810839: goto L_0x0558;
            case -1554255044: goto L_0x054c;
            case -1481772737: goto L_0x0540;
            case -1481772730: goto L_0x0534;
            case -1481772729: goto L_0x0528;
            case -1320080169: goto L_0x051c;
            case -1217592143: goto L_0x0510;
            case -1180384755: goto L_0x0504;
            case -1139198265: goto L_0x04f8;
            case -1052835013: goto L_0x04ec;
            case -993250464: goto L_0x04e1;
            case -965403638: goto L_0x04d5;
            case -958336948: goto L_0x04c9;
            case -879245230: goto L_0x04bd;
            case -842500323: goto L_0x04b1;
            case -821392978: goto L_0x04a6;
            case -797483286: goto L_0x049a;
            case -794946968: goto L_0x048e;
            case -788334647: goto L_0x0482;
            case -782144577: goto L_0x0476;
            case -575125681: goto L_0x046a;
            case -521118391: goto L_0x045e;
            case -430914369: goto L_0x0452;
            case -290434366: goto L_0x0446;
            case -282781963: goto L_0x043a;
            case -277133239: goto L_0x042e;
            case -173639913: goto L_0x0422;
            case -56598463: goto L_0x0416;
            case 2126: goto L_0x040a;
            case 2564: goto L_0x03fe;
            case 2715: goto L_0x03f2;
            case 2719: goto L_0x03e6;
            case 3483: goto L_0x03da;
            case 73405: goto L_0x03ce;
            case 75739: goto L_0x03c2;
            case 76779: goto L_0x03b6;
            case 78669: goto L_0x03aa;
            case 79305: goto L_0x039e;
            case 80618: goto L_0x0392;
            case 88274: goto L_0x0386;
            case 98846: goto L_0x037a;
            case 98848: goto L_0x036e;
            case 99329: goto L_0x0362;
            case 101481: goto L_0x0356;
            case 1513190: goto L_0x034b;
            case 1514184: goto L_0x0340;
            case 1514185: goto L_0x0335;
            case 2436959: goto L_0x0329;
            case 2463773: goto L_0x031d;
            case 2464648: goto L_0x0311;
            case 2689555: goto L_0x0305;
            case 3154429: goto L_0x02f9;
            case 3284551: goto L_0x02ed;
            case 3351335: goto L_0x02e1;
            case 3386211: goto L_0x02d5;
            case 41325051: goto L_0x02c9;
            case 55178625: goto L_0x02bd;
            case 61542055: goto L_0x02b2;
            case 65355429: goto L_0x02a6;
            case 66214468: goto L_0x029a;
            case 66214470: goto L_0x028e;
            case 66214473: goto L_0x0282;
            case 66215429: goto L_0x0276;
            case 66215431: goto L_0x026a;
            case 66215433: goto L_0x025e;
            case 66216390: goto L_0x0252;
            case 76402249: goto L_0x0246;
            case 76404105: goto L_0x023a;
            case 76404911: goto L_0x022e;
            case 80963634: goto L_0x0222;
            case 82882791: goto L_0x0216;
            case 98715550: goto L_0x020a;
            case 102844228: goto L_0x01fe;
            case 165221241: goto L_0x01f3;
            case 182191441: goto L_0x01e7;
            case 245388979: goto L_0x01db;
            case 287431619: goto L_0x01cf;
            case 307593612: goto L_0x01c3;
            case 308517133: goto L_0x01b7;
            case 316215098: goto L_0x01ab;
            case 316215116: goto L_0x019f;
            case 316246811: goto L_0x0193;
            case 316246818: goto L_0x0187;
            case 407160593: goto L_0x017b;
            case 507412548: goto L_0x016f;
            case 793982701: goto L_0x0163;
            case 794038622: goto L_0x0157;
            case 794040393: goto L_0x014b;
            case 835649806: goto L_0x013f;
            case 917340916: goto L_0x0134;
            case 958008161: goto L_0x0128;
            case 1060579533: goto L_0x011c;
            case 1150207623: goto L_0x0110;
            case 1176899427: goto L_0x0104;
            case 1280332038: goto L_0x00f8;
            case 1306947716: goto L_0x00ec;
            case 1349174697: goto L_0x00e0;
            case 1522194893: goto L_0x00d4;
            case 1691543273: goto L_0x00c8;
            case 1709443163: goto L_0x00bc;
            case 1865889110: goto L_0x00b0;
            case 1906253259: goto L_0x00a4;
            case 1977196784: goto L_0x0098;
            case 2006372676: goto L_0x008c;
            case 2029784656: goto L_0x0080;
            case 2030379515: goto L_0x0074;
            case 2033393791: goto L_0x0068;
            case 2047190025: goto L_0x005c;
            case 2047252157: goto L_0x0050;
            case 2048319463: goto L_0x0044;
            case 2048855701: goto L_0x0038;
            default: goto L_0x0036;
        };	 Catch:{ all -> 0x0630 }
    L_0x0036:
        goto L_0x05f7;
    L_0x0038:
        r2 = "HWWAS-H";
        r1 = r1.equals(r2);	 Catch:{ all -> 0x0630 }
        if (r1 == 0) goto L_0x05f7;
    L_0x0040:
        r1 = 54;
        goto L_0x05f8;
    L_0x0044:
        r2 = "HWVNS-H";
        r1 = r1.equals(r2);	 Catch:{ all -> 0x0630 }
        if (r1 == 0) goto L_0x05f7;
    L_0x004c:
        r1 = 53;
        goto L_0x05f8;
    L_0x0050:
        r4 = "ELUGA_Prim";
        r1 = r1.equals(r4);	 Catch:{ all -> 0x0630 }
        if (r1 == 0) goto L_0x05f7;
    L_0x0058:
        r1 = 27;
        goto L_0x05f8;
    L_0x005c:
        r2 = "ELUGA_Note";
        r1 = r1.equals(r2);	 Catch:{ all -> 0x0630 }
        if (r1 == 0) goto L_0x05f7;
    L_0x0064:
        r1 = 26;
        goto L_0x05f8;
    L_0x0068:
        r2 = "ASUS_X00AD_2";
        r1 = r1.equals(r2);	 Catch:{ all -> 0x0630 }
        if (r1 == 0) goto L_0x05f7;
    L_0x0070:
        r1 = 11;
        goto L_0x05f8;
    L_0x0074:
        r2 = "HWCAM-H";
        r1 = r1.equals(r2);	 Catch:{ all -> 0x0630 }
        if (r1 == 0) goto L_0x05f7;
    L_0x007c:
        r1 = 52;
        goto L_0x05f8;
    L_0x0080:
        r2 = "HWBLN-H";
        r1 = r1.equals(r2);	 Catch:{ all -> 0x0630 }
        if (r1 == 0) goto L_0x05f7;
    L_0x0088:
        r1 = 51;
        goto L_0x05f8;
    L_0x008c:
        r2 = "BRAVIA_ATV3_4K";
        r1 = r1.equals(r2);	 Catch:{ all -> 0x0630 }
        if (r1 == 0) goto L_0x05f7;
    L_0x0094:
        r1 = 15;
        goto L_0x05f8;
    L_0x0098:
        r2 = "Infinix-X572";
        r1 = r1.equals(r2);	 Catch:{ all -> 0x0630 }
        if (r1 == 0) goto L_0x05f7;
    L_0x00a0:
        r1 = 57;
        goto L_0x05f8;
    L_0x00a4:
        r2 = "PB2-670M";
        r1 = r1.equals(r2);	 Catch:{ all -> 0x0630 }
        if (r1 == 0) goto L_0x05f7;
    L_0x00ac:
        r1 = 85;
        goto L_0x05f8;
    L_0x00b0:
        r2 = "santoni";
        r1 = r1.equals(r2);	 Catch:{ all -> 0x0630 }
        if (r1 == 0) goto L_0x05f7;
    L_0x00b8:
        r1 = 101; // 0x65 float:1.42E-43 double:5.0E-322;
        goto L_0x05f8;
    L_0x00bc:
        r2 = "iball8735_9806";
        r1 = r1.equals(r2);	 Catch:{ all -> 0x0630 }
        if (r1 == 0) goto L_0x05f7;
    L_0x00c4:
        r1 = 56;
        goto L_0x05f8;
    L_0x00c8:
        r2 = "CPH1609";
        r1 = r1.equals(r2);	 Catch:{ all -> 0x0630 }
        if (r1 == 0) goto L_0x05f7;
    L_0x00d0:
        r1 = 19;
        goto L_0x05f8;
    L_0x00d4:
        r2 = "woods_f";
        r1 = r1.equals(r2);	 Catch:{ all -> 0x0630 }
        if (r1 == 0) goto L_0x05f7;
    L_0x00dc:
        r1 = 117; // 0x75 float:1.64E-43 double:5.8E-322;
        goto L_0x05f8;
    L_0x00e0:
        r2 = "htc_e56ml_dtul";
        r1 = r1.equals(r2);	 Catch:{ all -> 0x0630 }
        if (r1 == 0) goto L_0x05f7;
    L_0x00e8:
        r1 = 49;
        goto L_0x05f8;
    L_0x00ec:
        r2 = "EverStar_S";
        r1 = r1.equals(r2);	 Catch:{ all -> 0x0630 }
        if (r1 == 0) goto L_0x05f7;
    L_0x00f4:
        r1 = 29;
        goto L_0x05f8;
    L_0x00f8:
        r2 = "hwALE-H";
        r1 = r1.equals(r2);	 Catch:{ all -> 0x0630 }
        if (r1 == 0) goto L_0x05f7;
    L_0x0100:
        r1 = 50;
        goto L_0x05f8;
    L_0x0104:
        r2 = "itel_S41";
        r1 = r1.equals(r2);	 Catch:{ all -> 0x0630 }
        if (r1 == 0) goto L_0x05f7;
    L_0x010c:
        r1 = 59;
        goto L_0x05f8;
    L_0x0110:
        r2 = "LS-5017";
        r1 = r1.equals(r2);	 Catch:{ all -> 0x0630 }
        if (r1 == 0) goto L_0x05f7;
    L_0x0118:
        r1 = 65;
        goto L_0x05f8;
    L_0x011c:
        r2 = "panell_d";
        r1 = r1.equals(r2);	 Catch:{ all -> 0x0630 }
        if (r1 == 0) goto L_0x05f7;
    L_0x0124:
        r1 = 81;
        goto L_0x05f8;
    L_0x0128:
        r2 = "j2xlteins";
        r1 = r1.equals(r2);	 Catch:{ all -> 0x0630 }
        if (r1 == 0) goto L_0x05f7;
    L_0x0130:
        r1 = 60;
        goto L_0x05f8;
    L_0x0134:
        r2 = "A7000plus";
        r1 = r1.equals(r2);	 Catch:{ all -> 0x0630 }
        if (r1 == 0) goto L_0x05f7;
    L_0x013c:
        r1 = 7;
        goto L_0x05f8;
    L_0x013f:
        r2 = "manning";
        r1 = r1.equals(r2);	 Catch:{ all -> 0x0630 }
        if (r1 == 0) goto L_0x05f7;
    L_0x0147:
        r1 = 67;
        goto L_0x05f8;
    L_0x014b:
        r2 = "GIONEE_WBL7519";
        r1 = r1.equals(r2);	 Catch:{ all -> 0x0630 }
        if (r1 == 0) goto L_0x05f7;
    L_0x0153:
        r1 = 47;
        goto L_0x05f8;
    L_0x0157:
        r2 = "GIONEE_WBL7365";
        r1 = r1.equals(r2);	 Catch:{ all -> 0x0630 }
        if (r1 == 0) goto L_0x05f7;
    L_0x015f:
        r1 = 46;
        goto L_0x05f8;
    L_0x0163:
        r2 = "GIONEE_WBL5708";
        r1 = r1.equals(r2);	 Catch:{ all -> 0x0630 }
        if (r1 == 0) goto L_0x05f7;
    L_0x016b:
        r1 = 45;
        goto L_0x05f8;
    L_0x016f:
        r2 = "QM16XE_U";
        r1 = r1.equals(r2);	 Catch:{ all -> 0x0630 }
        if (r1 == 0) goto L_0x05f7;
    L_0x0177:
        r1 = 99;
        goto L_0x05f8;
    L_0x017b:
        r2 = "Pixi5-10_4G";
        r1 = r1.equals(r2);	 Catch:{ all -> 0x0630 }
        if (r1 == 0) goto L_0x05f7;
    L_0x0183:
        r1 = 91;
        goto L_0x05f8;
    L_0x0187:
        r2 = "TB3-850M";
        r1 = r1.equals(r2);	 Catch:{ all -> 0x0630 }
        if (r1 == 0) goto L_0x05f7;
    L_0x018f:
        r1 = 109; // 0x6d float:1.53E-43 double:5.4E-322;
        goto L_0x05f8;
    L_0x0193:
        r2 = "TB3-850F";
        r1 = r1.equals(r2);	 Catch:{ all -> 0x0630 }
        if (r1 == 0) goto L_0x05f7;
    L_0x019b:
        r1 = 108; // 0x6c float:1.51E-43 double:5.34E-322;
        goto L_0x05f8;
    L_0x019f:
        r2 = "TB3-730X";
        r1 = r1.equals(r2);	 Catch:{ all -> 0x0630 }
        if (r1 == 0) goto L_0x05f7;
    L_0x01a7:
        r1 = 107; // 0x6b float:1.5E-43 double:5.3E-322;
        goto L_0x05f8;
    L_0x01ab:
        r2 = "TB3-730F";
        r1 = r1.equals(r2);	 Catch:{ all -> 0x0630 }
        if (r1 == 0) goto L_0x05f7;
    L_0x01b3:
        r1 = 106; // 0x6a float:1.49E-43 double:5.24E-322;
        goto L_0x05f8;
    L_0x01b7:
        r2 = "A7020a48";
        r1 = r1.equals(r2);	 Catch:{ all -> 0x0630 }
        if (r1 == 0) goto L_0x05f7;
    L_0x01bf:
        r1 = 9;
        goto L_0x05f8;
    L_0x01c3:
        r2 = "A7010a48";
        r1 = r1.equals(r2);	 Catch:{ all -> 0x0630 }
        if (r1 == 0) goto L_0x05f7;
    L_0x01cb:
        r1 = 8;
        goto L_0x05f8;
    L_0x01cf:
        r2 = "griffin";
        r1 = r1.equals(r2);	 Catch:{ all -> 0x0630 }
        if (r1 == 0) goto L_0x05f7;
    L_0x01d7:
        r1 = 48;
        goto L_0x05f8;
    L_0x01db:
        r2 = "marino_f";
        r1 = r1.equals(r2);	 Catch:{ all -> 0x0630 }
        if (r1 == 0) goto L_0x05f7;
    L_0x01e3:
        r1 = 68;
        goto L_0x05f8;
    L_0x01e7:
        r2 = "CPY83_I00";
        r1 = r1.equals(r2);	 Catch:{ all -> 0x0630 }
        if (r1 == 0) goto L_0x05f7;
    L_0x01ef:
        r1 = 20;
        goto L_0x05f8;
    L_0x01f3:
        r2 = "A2016a40";
        r1 = r1.equals(r2);	 Catch:{ all -> 0x0630 }
        if (r1 == 0) goto L_0x05f7;
    L_0x01fb:
        r1 = 5;
        goto L_0x05f8;
    L_0x01fe:
        r2 = "le_x6";
        r1 = r1.equals(r2);	 Catch:{ all -> 0x0630 }
        if (r1 == 0) goto L_0x05f7;
    L_0x0206:
        r1 = 64;
        goto L_0x05f8;
    L_0x020a:
        r2 = "i9031";
        r1 = r1.equals(r2);	 Catch:{ all -> 0x0630 }
        if (r1 == 0) goto L_0x05f7;
    L_0x0212:
        r1 = 55;
        goto L_0x05f8;
    L_0x0216:
        r2 = "X3_HK";
        r1 = r1.equals(r2);	 Catch:{ all -> 0x0630 }
        if (r1 == 0) goto L_0x05f7;
    L_0x021e:
        r1 = 119; // 0x77 float:1.67E-43 double:5.9E-322;
        goto L_0x05f8;
    L_0x0222:
        r2 = "V23GB";
        r1 = r1.equals(r2);	 Catch:{ all -> 0x0630 }
        if (r1 == 0) goto L_0x05f7;
    L_0x022a:
        r1 = 112; // 0x70 float:1.57E-43 double:5.53E-322;
        goto L_0x05f8;
    L_0x022e:
        r2 = "Q4310";
        r1 = r1.equals(r2);	 Catch:{ all -> 0x0630 }
        if (r1 == 0) goto L_0x05f7;
    L_0x0236:
        r1 = 97;
        goto L_0x05f8;
    L_0x023a:
        r2 = "Q4260";
        r1 = r1.equals(r2);	 Catch:{ all -> 0x0630 }
        if (r1 == 0) goto L_0x05f7;
    L_0x0242:
        r1 = 95;
        goto L_0x05f8;
    L_0x0246:
        r2 = "PRO7S";
        r1 = r1.equals(r2);	 Catch:{ all -> 0x0630 }
        if (r1 == 0) goto L_0x05f7;
    L_0x024e:
        r1 = 93;
        goto L_0x05f8;
    L_0x0252:
        r2 = "F3311";
        r1 = r1.equals(r2);	 Catch:{ all -> 0x0630 }
        if (r1 == 0) goto L_0x05f7;
    L_0x025a:
        r1 = 36;
        goto L_0x05f8;
    L_0x025e:
        r2 = "F3215";
        r1 = r1.equals(r2);	 Catch:{ all -> 0x0630 }
        if (r1 == 0) goto L_0x05f7;
    L_0x0266:
        r1 = 35;
        goto L_0x05f8;
    L_0x026a:
        r2 = "F3213";
        r1 = r1.equals(r2);	 Catch:{ all -> 0x0630 }
        if (r1 == 0) goto L_0x05f7;
    L_0x0272:
        r1 = 34;
        goto L_0x05f8;
    L_0x0276:
        r2 = "F3211";
        r1 = r1.equals(r2);	 Catch:{ all -> 0x0630 }
        if (r1 == 0) goto L_0x05f7;
    L_0x027e:
        r1 = 33;
        goto L_0x05f8;
    L_0x0282:
        r2 = "F3116";
        r1 = r1.equals(r2);	 Catch:{ all -> 0x0630 }
        if (r1 == 0) goto L_0x05f7;
    L_0x028a:
        r1 = 32;
        goto L_0x05f8;
    L_0x028e:
        r2 = "F3113";
        r1 = r1.equals(r2);	 Catch:{ all -> 0x0630 }
        if (r1 == 0) goto L_0x05f7;
    L_0x0296:
        r1 = 31;
        goto L_0x05f8;
    L_0x029a:
        r2 = "F3111";
        r1 = r1.equals(r2);	 Catch:{ all -> 0x0630 }
        if (r1 == 0) goto L_0x05f7;
    L_0x02a2:
        r1 = 30;
        goto L_0x05f8;
    L_0x02a6:
        r2 = "E5643";
        r1 = r1.equals(r2);	 Catch:{ all -> 0x0630 }
        if (r1 == 0) goto L_0x05f7;
    L_0x02ae:
        r1 = 24;
        goto L_0x05f8;
    L_0x02b2:
        r2 = "A1601";
        r1 = r1.equals(r2);	 Catch:{ all -> 0x0630 }
        if (r1 == 0) goto L_0x05f7;
    L_0x02ba:
        r1 = 4;
        goto L_0x05f8;
    L_0x02bd:
        r2 = "Aura_Note_2";
        r1 = r1.equals(r2);	 Catch:{ all -> 0x0630 }
        if (r1 == 0) goto L_0x05f7;
    L_0x02c5:
        r1 = 12;
        goto L_0x05f8;
    L_0x02c9:
        r2 = "MEIZU_M5";
        r1 = r1.equals(r2);	 Catch:{ all -> 0x0630 }
        if (r1 == 0) goto L_0x05f7;
    L_0x02d1:
        r1 = 69;
        goto L_0x05f8;
    L_0x02d5:
        r2 = "p212";
        r1 = r1.equals(r2);	 Catch:{ all -> 0x0630 }
        if (r1 == 0) goto L_0x05f7;
    L_0x02dd:
        r1 = 78;
        goto L_0x05f8;
    L_0x02e1:
        r2 = "mido";
        r1 = r1.equals(r2);	 Catch:{ all -> 0x0630 }
        if (r1 == 0) goto L_0x05f7;
    L_0x02e9:
        r1 = 71;
        goto L_0x05f8;
    L_0x02ed:
        r2 = "kate";
        r1 = r1.equals(r2);	 Catch:{ all -> 0x0630 }
        if (r1 == 0) goto L_0x05f7;
    L_0x02f5:
        r1 = 63;
        goto L_0x05f8;
    L_0x02f9:
        r2 = "fugu";
        r1 = r1.equals(r2);	 Catch:{ all -> 0x0630 }
        if (r1 == 0) goto L_0x05f7;
    L_0x0301:
        r1 = 38;
        goto L_0x05f8;
    L_0x0305:
        r2 = "XE2X";
        r1 = r1.equals(r2);	 Catch:{ all -> 0x0630 }
        if (r1 == 0) goto L_0x05f7;
    L_0x030d:
        r1 = 120; // 0x78 float:1.68E-43 double:5.93E-322;
        goto L_0x05f8;
    L_0x0311:
        r2 = "Q427";
        r1 = r1.equals(r2);	 Catch:{ all -> 0x0630 }
        if (r1 == 0) goto L_0x05f7;
    L_0x0319:
        r1 = 96;
        goto L_0x05f8;
    L_0x031d:
        r2 = "Q350";
        r1 = r1.equals(r2);	 Catch:{ all -> 0x0630 }
        if (r1 == 0) goto L_0x05f7;
    L_0x0325:
        r1 = 94;
        goto L_0x05f8;
    L_0x0329:
        r2 = "P681";
        r1 = r1.equals(r2);	 Catch:{ all -> 0x0630 }
        if (r1 == 0) goto L_0x05f7;
    L_0x0331:
        r1 = 79;
        goto L_0x05f8;
    L_0x0335:
        r2 = "1714";
        r1 = r1.equals(r2);	 Catch:{ all -> 0x0630 }
        if (r1 == 0) goto L_0x05f7;
    L_0x033d:
        r1 = 2;
        goto L_0x05f8;
    L_0x0340:
        r2 = "1713";
        r1 = r1.equals(r2);	 Catch:{ all -> 0x0630 }
        if (r1 == 0) goto L_0x05f7;
    L_0x0348:
        r1 = 1;
        goto L_0x05f8;
    L_0x034b:
        r2 = "1601";
        r1 = r1.equals(r2);	 Catch:{ all -> 0x0630 }
        if (r1 == 0) goto L_0x05f7;
    L_0x0353:
        r1 = 0;
        goto L_0x05f8;
    L_0x0356:
        r2 = "flo";
        r1 = r1.equals(r2);	 Catch:{ all -> 0x0630 }
        if (r1 == 0) goto L_0x05f7;
    L_0x035e:
        r1 = 37;
        goto L_0x05f8;
    L_0x0362:
        r2 = "deb";
        r1 = r1.equals(r2);	 Catch:{ all -> 0x0630 }
        if (r1 == 0) goto L_0x05f7;
    L_0x036a:
        r1 = 23;
        goto L_0x05f8;
    L_0x036e:
        r2 = "cv3";
        r1 = r1.equals(r2);	 Catch:{ all -> 0x0630 }
        if (r1 == 0) goto L_0x05f7;
    L_0x0376:
        r1 = 22;
        goto L_0x05f8;
    L_0x037a:
        r2 = "cv1";
        r1 = r1.equals(r2);	 Catch:{ all -> 0x0630 }
        if (r1 == 0) goto L_0x05f7;
    L_0x0382:
        r1 = 21;
        goto L_0x05f8;
    L_0x0386:
        r2 = "Z80";
        r1 = r1.equals(r2);	 Catch:{ all -> 0x0630 }
        if (r1 == 0) goto L_0x05f7;
    L_0x038e:
        r1 = 123; // 0x7b float:1.72E-43 double:6.1E-322;
        goto L_0x05f8;
    L_0x0392:
        r2 = "QX1";
        r1 = r1.equals(r2);	 Catch:{ all -> 0x0630 }
        if (r1 == 0) goto L_0x05f7;
    L_0x039a:
        r1 = 100;
        goto L_0x05f8;
    L_0x039e:
        r2 = "PLE";
        r1 = r1.equals(r2);	 Catch:{ all -> 0x0630 }
        if (r1 == 0) goto L_0x05f7;
    L_0x03a6:
        r1 = 92;
        goto L_0x05f8;
    L_0x03aa:
        r2 = "P85";
        r1 = r1.equals(r2);	 Catch:{ all -> 0x0630 }
        if (r1 == 0) goto L_0x05f7;
    L_0x03b2:
        r1 = 80;
        goto L_0x05f8;
    L_0x03b6:
        r2 = "MX6";
        r1 = r1.equals(r2);	 Catch:{ all -> 0x0630 }
        if (r1 == 0) goto L_0x05f7;
    L_0x03be:
        r1 = 72;
        goto L_0x05f8;
    L_0x03c2:
        r2 = "M5c";
        r1 = r1.equals(r2);	 Catch:{ all -> 0x0630 }
        if (r1 == 0) goto L_0x05f7;
    L_0x03ca:
        r1 = 66;
        goto L_0x05f8;
    L_0x03ce:
        r2 = "JGZ";
        r1 = r1.equals(r2);	 Catch:{ all -> 0x0630 }
        if (r1 == 0) goto L_0x05f7;
    L_0x03d6:
        r1 = 61;
        goto L_0x05f8;
    L_0x03da:
        r2 = "mh";
        r1 = r1.equals(r2);	 Catch:{ all -> 0x0630 }
        if (r1 == 0) goto L_0x05f7;
    L_0x03e2:
        r1 = 70;
        goto L_0x05f8;
    L_0x03e6:
        r2 = "V5";
        r1 = r1.equals(r2);	 Catch:{ all -> 0x0630 }
        if (r1 == 0) goto L_0x05f7;
    L_0x03ee:
        r1 = 113; // 0x71 float:1.58E-43 double:5.6E-322;
        goto L_0x05f8;
    L_0x03f2:
        r2 = "V1";
        r1 = r1.equals(r2);	 Catch:{ all -> 0x0630 }
        if (r1 == 0) goto L_0x05f7;
    L_0x03fa:
        r1 = 111; // 0x6f float:1.56E-43 double:5.5E-322;
        goto L_0x05f8;
    L_0x03fe:
        r2 = "Q5";
        r1 = r1.equals(r2);	 Catch:{ all -> 0x0630 }
        if (r1 == 0) goto L_0x05f7;
    L_0x0406:
        r1 = 98;
        goto L_0x05f8;
    L_0x040a:
        r2 = "C1";
        r1 = r1.equals(r2);	 Catch:{ all -> 0x0630 }
        if (r1 == 0) goto L_0x05f7;
    L_0x0412:
        r1 = 16;
        goto L_0x05f8;
    L_0x0416:
        r2 = "woods_fn";
        r1 = r1.equals(r2);	 Catch:{ all -> 0x0630 }
        if (r1 == 0) goto L_0x05f7;
    L_0x041e:
        r1 = 118; // 0x76 float:1.65E-43 double:5.83E-322;
        goto L_0x05f8;
    L_0x0422:
        r2 = "ELUGA_A3_Pro";
        r1 = r1.equals(r2);	 Catch:{ all -> 0x0630 }
        if (r1 == 0) goto L_0x05f7;
    L_0x042a:
        r1 = 25;
        goto L_0x05f8;
    L_0x042e:
        r2 = "Z12_PRO";
        r1 = r1.equals(r2);	 Catch:{ all -> 0x0630 }
        if (r1 == 0) goto L_0x05f7;
    L_0x0436:
        r1 = 122; // 0x7a float:1.71E-43 double:6.03E-322;
        goto L_0x05f8;
    L_0x043a:
        r2 = "BLACK-1X";
        r1 = r1.equals(r2);	 Catch:{ all -> 0x0630 }
        if (r1 == 0) goto L_0x05f7;
    L_0x0442:
        r1 = 13;
        goto L_0x05f8;
    L_0x0446:
        r2 = "taido_row";
        r1 = r1.equals(r2);	 Catch:{ all -> 0x0630 }
        if (r1 == 0) goto L_0x05f7;
    L_0x044e:
        r1 = 105; // 0x69 float:1.47E-43 double:5.2E-322;
        goto L_0x05f8;
    L_0x0452:
        r2 = "Pixi4-7_3G";
        r1 = r1.equals(r2);	 Catch:{ all -> 0x0630 }
        if (r1 == 0) goto L_0x05f7;
    L_0x045a:
        r1 = 90;
        goto L_0x05f8;
    L_0x045e:
        r2 = "GIONEE_GBL7360";
        r1 = r1.equals(r2);	 Catch:{ all -> 0x0630 }
        if (r1 == 0) goto L_0x05f7;
    L_0x0466:
        r1 = 41;
        goto L_0x05f8;
    L_0x046a:
        r2 = "GiONEE_CBL7513";
        r1 = r1.equals(r2);	 Catch:{ all -> 0x0630 }
        if (r1 == 0) goto L_0x05f7;
    L_0x0472:
        r1 = 39;
        goto L_0x05f8;
    L_0x0476:
        r2 = "OnePlus5T";
        r1 = r1.equals(r2);	 Catch:{ all -> 0x0630 }
        if (r1 == 0) goto L_0x05f7;
    L_0x047e:
        r1 = 77;
        goto L_0x05f8;
    L_0x0482:
        r2 = "whyred";
        r1 = r1.equals(r2);	 Catch:{ all -> 0x0630 }
        if (r1 == 0) goto L_0x05f7;
    L_0x048a:
        r1 = 116; // 0x74 float:1.63E-43 double:5.73E-322;
        goto L_0x05f8;
    L_0x048e:
        r2 = "watson";
        r1 = r1.equals(r2);	 Catch:{ all -> 0x0630 }
        if (r1 == 0) goto L_0x05f7;
    L_0x0496:
        r1 = 115; // 0x73 float:1.61E-43 double:5.7E-322;
        goto L_0x05f8;
    L_0x049a:
        r2 = "SVP-DTV15";
        r1 = r1.equals(r2);	 Catch:{ all -> 0x0630 }
        if (r1 == 0) goto L_0x05f7;
    L_0x04a2:
        r1 = 103; // 0x67 float:1.44E-43 double:5.1E-322;
        goto L_0x05f8;
    L_0x04a6:
        r2 = "A7000-a";
        r1 = r1.equals(r2);	 Catch:{ all -> 0x0630 }
        if (r1 == 0) goto L_0x05f7;
    L_0x04ae:
        r1 = 6;
        goto L_0x05f8;
    L_0x04b1:
        r2 = "nicklaus_f";
        r1 = r1.equals(r2);	 Catch:{ all -> 0x0630 }
        if (r1 == 0) goto L_0x05f7;
    L_0x04b9:
        r1 = 74;
        goto L_0x05f8;
    L_0x04bd:
        r2 = "tcl_eu";
        r1 = r1.equals(r2);	 Catch:{ all -> 0x0630 }
        if (r1 == 0) goto L_0x05f7;
    L_0x04c5:
        r1 = 110; // 0x6e float:1.54E-43 double:5.43E-322;
        goto L_0x05f8;
    L_0x04c9:
        r2 = "ELUGA_Ray_X";
        r1 = r1.equals(r2);	 Catch:{ all -> 0x0630 }
        if (r1 == 0) goto L_0x05f7;
    L_0x04d1:
        r1 = 28;
        goto L_0x05f8;
    L_0x04d5:
        r2 = "s905x018";
        r1 = r1.equals(r2);	 Catch:{ all -> 0x0630 }
        if (r1 == 0) goto L_0x05f7;
    L_0x04dd:
        r1 = 104; // 0x68 float:1.46E-43 double:5.14E-322;
        goto L_0x05f8;
    L_0x04e1:
        r2 = "A10-70F";
        r1 = r1.equals(r2);	 Catch:{ all -> 0x0630 }
        if (r1 == 0) goto L_0x05f7;
    L_0x04e9:
        r1 = 3;
        goto L_0x05f8;
    L_0x04ec:
        r2 = "namath";
        r1 = r1.equals(r2);	 Catch:{ all -> 0x0630 }
        if (r1 == 0) goto L_0x05f7;
    L_0x04f4:
        r1 = 73;
        goto L_0x05f8;
    L_0x04f8:
        r2 = "Slate_Pro";
        r1 = r1.equals(r2);	 Catch:{ all -> 0x0630 }
        if (r1 == 0) goto L_0x05f7;
    L_0x0500:
        r1 = 102; // 0x66 float:1.43E-43 double:5.04E-322;
        goto L_0x05f8;
    L_0x0504:
        r2 = "iris60";
        r1 = r1.equals(r2);	 Catch:{ all -> 0x0630 }
        if (r1 == 0) goto L_0x05f7;
    L_0x050c:
        r1 = 58;
        goto L_0x05f8;
    L_0x0510:
        r2 = "BRAVIA_ATV2";
        r1 = r1.equals(r2);	 Catch:{ all -> 0x0630 }
        if (r1 == 0) goto L_0x05f7;
    L_0x0518:
        r1 = 14;
        goto L_0x05f8;
    L_0x051c:
        r2 = "GiONEE_GBL7319";
        r1 = r1.equals(r2);	 Catch:{ all -> 0x0630 }
        if (r1 == 0) goto L_0x05f7;
    L_0x0524:
        r1 = 40;
        goto L_0x05f8;
    L_0x0528:
        r2 = "panell_dt";
        r1 = r1.equals(r2);	 Catch:{ all -> 0x0630 }
        if (r1 == 0) goto L_0x05f7;
    L_0x0530:
        r1 = 84;
        goto L_0x05f8;
    L_0x0534:
        r2 = "panell_ds";
        r1 = r1.equals(r2);	 Catch:{ all -> 0x0630 }
        if (r1 == 0) goto L_0x05f7;
    L_0x053c:
        r1 = 83;
        goto L_0x05f8;
    L_0x0540:
        r2 = "panell_dl";
        r1 = r1.equals(r2);	 Catch:{ all -> 0x0630 }
        if (r1 == 0) goto L_0x05f7;
    L_0x0548:
        r1 = 82;
        goto L_0x05f8;
    L_0x054c:
        r2 = "vernee_M5";
        r1 = r1.equals(r2);	 Catch:{ all -> 0x0630 }
        if (r1 == 0) goto L_0x05f7;
    L_0x0554:
        r1 = 114; // 0x72 float:1.6E-43 double:5.63E-322;
        goto L_0x05f8;
    L_0x0558:
        r2 = "Phantom6";
        r1 = r1.equals(r2);	 Catch:{ all -> 0x0630 }
        if (r1 == 0) goto L_0x05f7;
    L_0x0560:
        r1 = 89;
        goto L_0x05f8;
    L_0x0564:
        r2 = "ComioS1";
        r1 = r1.equals(r2);	 Catch:{ all -> 0x0630 }
        if (r1 == 0) goto L_0x05f7;
    L_0x056c:
        r1 = 17;
        goto L_0x05f8;
    L_0x0570:
        r2 = "XT1663";
        r1 = r1.equals(r2);	 Catch:{ all -> 0x0630 }
        if (r1 == 0) goto L_0x05f7;
    L_0x0578:
        r1 = 121; // 0x79 float:1.7E-43 double:6.0E-322;
        goto L_0x05f8;
    L_0x057c:
        r2 = "AquaPowerM";
        r1 = r1.equals(r2);	 Catch:{ all -> 0x0630 }
        if (r1 == 0) goto L_0x05f7;
    L_0x0584:
        r1 = 10;
        goto L_0x05f8;
    L_0x0588:
        r2 = "PGN611";
        r1 = r1.equals(r2);	 Catch:{ all -> 0x0630 }
        if (r1 == 0) goto L_0x05f7;
    L_0x0590:
        r1 = 88;
        goto L_0x05f8;
    L_0x0594:
        r2 = "PGN610";
        r1 = r1.equals(r2);	 Catch:{ all -> 0x0630 }
        if (r1 == 0) goto L_0x05f7;
    L_0x059c:
        r1 = 87;
        goto L_0x05f8;
    L_0x059f:
        r2 = "PGN528";
        r1 = r1.equals(r2);	 Catch:{ all -> 0x0630 }
        if (r1 == 0) goto L_0x05f7;
    L_0x05a7:
        r1 = 86;
        goto L_0x05f8;
    L_0x05aa:
        r2 = "NX573J";
        r1 = r1.equals(r2);	 Catch:{ all -> 0x0630 }
        if (r1 == 0) goto L_0x05f7;
    L_0x05b2:
        r1 = 76;
        goto L_0x05f8;
    L_0x05b5:
        r2 = "NX541J";
        r1 = r1.equals(r2);	 Catch:{ all -> 0x0630 }
        if (r1 == 0) goto L_0x05f7;
    L_0x05bd:
        r1 = 75;
        goto L_0x05f8;
    L_0x05c0:
        r2 = "CP8676_I02";
        r1 = r1.equals(r2);	 Catch:{ all -> 0x0630 }
        if (r1 == 0) goto L_0x05f7;
    L_0x05c8:
        r1 = 18;
        goto L_0x05f8;
    L_0x05cb:
        r2 = "K50a40";
        r1 = r1.equals(r2);	 Catch:{ all -> 0x0630 }
        if (r1 == 0) goto L_0x05f7;
    L_0x05d3:
        r1 = 62;
        goto L_0x05f8;
    L_0x05d6:
        r2 = "GIONEE_SWW1631";
        r1 = r1.equals(r2);	 Catch:{ all -> 0x0630 }
        if (r1 == 0) goto L_0x05f7;
    L_0x05de:
        r1 = 44;
        goto L_0x05f8;
    L_0x05e1:
        r2 = "GIONEE_SWW1627";
        r1 = r1.equals(r2);	 Catch:{ all -> 0x0630 }
        if (r1 == 0) goto L_0x05f7;
    L_0x05e9:
        r1 = 43;
        goto L_0x05f8;
    L_0x05ec:
        r2 = "GIONEE_SWW1609";
        r1 = r1.equals(r2);	 Catch:{ all -> 0x0630 }
        if (r1 == 0) goto L_0x05f7;
    L_0x05f4:
        r1 = 42;
        goto L_0x05f8;
    L_0x05f7:
        r1 = -1;
    L_0x05f8:
        switch(r1) {
            case 0: goto L_0x05fc;
            case 1: goto L_0x05fc;
            case 2: goto L_0x05fc;
            case 3: goto L_0x05fc;
            case 4: goto L_0x05fc;
            case 5: goto L_0x05fc;
            case 6: goto L_0x05fc;
            case 7: goto L_0x05fc;
            case 8: goto L_0x05fc;
            case 9: goto L_0x05fc;
            case 10: goto L_0x05fc;
            case 11: goto L_0x05fc;
            case 12: goto L_0x05fc;
            case 13: goto L_0x05fc;
            case 14: goto L_0x05fc;
            case 15: goto L_0x05fc;
            case 16: goto L_0x05fc;
            case 17: goto L_0x05fc;
            case 18: goto L_0x05fc;
            case 19: goto L_0x05fc;
            case 20: goto L_0x05fc;
            case 21: goto L_0x05fc;
            case 22: goto L_0x05fc;
            case 23: goto L_0x05fc;
            case 24: goto L_0x05fc;
            case 25: goto L_0x05fc;
            case 26: goto L_0x05fc;
            case 27: goto L_0x05fc;
            case 28: goto L_0x05fc;
            case 29: goto L_0x05fc;
            case 30: goto L_0x05fc;
            case 31: goto L_0x05fc;
            case 32: goto L_0x05fc;
            case 33: goto L_0x05fc;
            case 34: goto L_0x05fc;
            case 35: goto L_0x05fc;
            case 36: goto L_0x05fc;
            case 37: goto L_0x05fc;
            case 38: goto L_0x05fc;
            case 39: goto L_0x05fc;
            case 40: goto L_0x05fc;
            case 41: goto L_0x05fc;
            case 42: goto L_0x05fc;
            case 43: goto L_0x05fc;
            case 44: goto L_0x05fc;
            case 45: goto L_0x05fc;
            case 46: goto L_0x05fc;
            case 47: goto L_0x05fc;
            case 48: goto L_0x05fc;
            case 49: goto L_0x05fc;
            case 50: goto L_0x05fc;
            case 51: goto L_0x05fc;
            case 52: goto L_0x05fc;
            case 53: goto L_0x05fc;
            case 54: goto L_0x05fc;
            case 55: goto L_0x05fc;
            case 56: goto L_0x05fc;
            case 57: goto L_0x05fc;
            case 58: goto L_0x05fc;
            case 59: goto L_0x05fc;
            case 60: goto L_0x05fc;
            case 61: goto L_0x05fc;
            case 62: goto L_0x05fc;
            case 63: goto L_0x05fc;
            case 64: goto L_0x05fc;
            case 65: goto L_0x05fc;
            case 66: goto L_0x05fc;
            case 67: goto L_0x05fc;
            case 68: goto L_0x05fc;
            case 69: goto L_0x05fc;
            case 70: goto L_0x05fc;
            case 71: goto L_0x05fc;
            case 72: goto L_0x05fc;
            case 73: goto L_0x05fc;
            case 74: goto L_0x05fc;
            case 75: goto L_0x05fc;
            case 76: goto L_0x05fc;
            case 77: goto L_0x05fc;
            case 78: goto L_0x05fc;
            case 79: goto L_0x05fc;
            case 80: goto L_0x05fc;
            case 81: goto L_0x05fc;
            case 82: goto L_0x05fc;
            case 83: goto L_0x05fc;
            case 84: goto L_0x05fc;
            case 85: goto L_0x05fc;
            case 86: goto L_0x05fc;
            case 87: goto L_0x05fc;
            case 88: goto L_0x05fc;
            case 89: goto L_0x05fc;
            case 90: goto L_0x05fc;
            case 91: goto L_0x05fc;
            case 92: goto L_0x05fc;
            case 93: goto L_0x05fc;
            case 94: goto L_0x05fc;
            case 95: goto L_0x05fc;
            case 96: goto L_0x05fc;
            case 97: goto L_0x05fc;
            case 98: goto L_0x05fc;
            case 99: goto L_0x05fc;
            case 100: goto L_0x05fc;
            case 101: goto L_0x05fc;
            case 102: goto L_0x05fc;
            case 103: goto L_0x05fc;
            case 104: goto L_0x05fc;
            case 105: goto L_0x05fc;
            case 106: goto L_0x05fc;
            case 107: goto L_0x05fc;
            case 108: goto L_0x05fc;
            case 109: goto L_0x05fc;
            case 110: goto L_0x05fc;
            case 111: goto L_0x05fc;
            case 112: goto L_0x05fc;
            case 113: goto L_0x05fc;
            case 114: goto L_0x05fc;
            case 115: goto L_0x05fc;
            case 116: goto L_0x05fc;
            case 117: goto L_0x05fc;
            case 118: goto L_0x05fc;
            case 119: goto L_0x05fc;
            case 120: goto L_0x05fc;
            case 121: goto L_0x05fc;
            case 122: goto L_0x05fc;
            case 123: goto L_0x05fc;
            default: goto L_0x05fb;
        };	 Catch:{ all -> 0x0630 }
    L_0x05fb:
        goto L_0x05fe;
    L_0x05fc:
        deviceNeedsSetOutputSurfaceWorkaround = r3;	 Catch:{ all -> 0x0630 }
    L_0x05fe:
        r1 = com.google.android.exoplayer2.util.Util.MODEL;	 Catch:{ all -> 0x0630 }
        r2 = r1.hashCode();	 Catch:{ all -> 0x0630 }
        r4 = 2006354; // 0x1e9d52 float:2.811501E-39 double:9.912706E-318;
        if (r2 == r4) goto L_0x0619;
    L_0x0609:
        r0 = 2006367; // 0x1e9d5f float:2.811519E-39 double:9.91277E-318;
        if (r2 == r0) goto L_0x060f;
    L_0x060e:
        goto L_0x0622;
    L_0x060f:
        r0 = "AFTN";
        r0 = r1.equals(r0);	 Catch:{ all -> 0x0630 }
        if (r0 == 0) goto L_0x0622;
    L_0x0617:
        r0 = 1;
        goto L_0x0623;
    L_0x0619:
        r2 = "AFTA";
        r1 = r1.equals(r2);	 Catch:{ all -> 0x0630 }
        if (r1 == 0) goto L_0x0622;
    L_0x0621:
        goto L_0x0623;
    L_0x0622:
        r0 = -1;
    L_0x0623:
        if (r0 == 0) goto L_0x0628;
    L_0x0625:
        if (r0 == r3) goto L_0x0628;
    L_0x0627:
        goto L_0x062a;
    L_0x0628:
        deviceNeedsSetOutputSurfaceWorkaround = r3;	 Catch:{ all -> 0x0630 }
    L_0x062a:
        evaluatedDeviceNeedsSetOutputSurfaceWorkaround = r3;	 Catch:{ all -> 0x0630 }
    L_0x062c:
        monitor-exit(r7);	 Catch:{ all -> 0x0630 }
        r7 = deviceNeedsSetOutputSurfaceWorkaround;
        return r7;
    L_0x0630:
        r0 = move-exception;
        monitor-exit(r7);	 Catch:{ all -> 0x0630 }
        throw r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.exoplayer2.video.MediaCodecVideoRenderer.codecNeedsSetOutputSurfaceWorkaround(java.lang.String):boolean");
    }
}
