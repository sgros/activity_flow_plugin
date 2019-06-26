// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.video;

import android.media.MediaCodec$OnFrameRenderedListener;
import java.util.List;
import com.google.android.exoplayer2.drm.DrmInitData;
import com.google.android.exoplayer2.BaseRenderer;
import com.google.android.exoplayer2.util.MimeTypes;
import java.nio.ByteBuffer;
import com.google.android.exoplayer2.decoder.DecoderInputBuffer;
import com.google.android.exoplayer2.decoder.DecoderCounters;
import android.annotation.SuppressLint;
import com.google.android.exoplayer2.mediacodec.MediaFormatUtil;
import com.google.android.exoplayer2.util.Log;
import com.google.android.exoplayer2.util.TraceUtil;
import com.google.android.exoplayer2.util.Assertions;
import android.media.MediaCrypto;
import com.google.android.exoplayer2.ExoPlaybackException;
import android.os.SystemClock;
import com.google.android.exoplayer2.mediacodec.MediaCodecUtil;
import android.graphics.Point;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.mediacodec.MediaCodecInfo;
import android.media.MediaFormat;
import android.media.MediaCodec;
import com.google.android.exoplayer2.util.Util;
import android.os.Handler;
import com.google.android.exoplayer2.drm.FrameworkMediaCrypto;
import com.google.android.exoplayer2.drm.DrmSessionManager;
import com.google.android.exoplayer2.mediacodec.MediaCodecSelector;
import android.view.Surface;
import android.content.Context;
import android.annotation.TargetApi;
import com.google.android.exoplayer2.mediacodec.MediaCodecRenderer;

@TargetApi(16)
public class MediaCodecVideoRenderer extends MediaCodecRenderer
{
    private static final int[] STANDARD_LONG_EDGE_VIDEO_PX;
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
    private final VideoRendererEventListener.EventDispatcher eventDispatcher;
    private VideoFrameMetadataListener frameMetadataListener;
    private final VideoFrameReleaseTimeHelper frameReleaseTimeHelper;
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
    
    static {
        STANDARD_LONG_EDGE_VIDEO_PX = new int[] { 1920, 1600, 1440, 1280, 960, 854, 640, 540, 480 };
    }
    
    public MediaCodecVideoRenderer(final Context context, final MediaCodecSelector mediaCodecSelector, final long allowedJoiningTimeMs, final DrmSessionManager<FrameworkMediaCrypto> drmSessionManager, final boolean b, final Handler handler, final VideoRendererEventListener videoRendererEventListener, final int maxDroppedFramesToNotify) {
        super(2, mediaCodecSelector, drmSessionManager, b, 30.0f);
        this.allowedJoiningTimeMs = allowedJoiningTimeMs;
        this.maxDroppedFramesToNotify = maxDroppedFramesToNotify;
        this.context = context.getApplicationContext();
        this.frameReleaseTimeHelper = new VideoFrameReleaseTimeHelper(this.context);
        this.eventDispatcher = new VideoRendererEventListener.EventDispatcher(handler, videoRendererEventListener);
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
        this.clearReportedVideoSize();
    }
    
    private void clearRenderedFirstFrame() {
        this.renderedFirstFrame = false;
        if (Util.SDK_INT >= 23 && this.tunneling) {
            final MediaCodec codec = this.getCodec();
            if (codec != null) {
                this.tunnelingOnFrameRenderedListener = new OnFrameRenderedListenerV23(codec);
            }
        }
    }
    
    private void clearReportedVideoSize() {
        this.reportedWidth = -1;
        this.reportedHeight = -1;
        this.reportedPixelWidthHeightRatio = -1.0f;
        this.reportedUnappliedRotationDegrees = -1;
    }
    
    @TargetApi(21)
    private static void configureTunnelingV21(final MediaFormat mediaFormat, final int n) {
        mediaFormat.setFeatureEnabled("tunneled-playback", true);
        mediaFormat.setInteger("audio-session-id", n);
    }
    
    private static boolean deviceNeedsNoPostProcessWorkaround() {
        return "NVIDIA".equals(Util.MANUFACTURER);
    }
    
    private static int getCodecMaxInputSize(final MediaCodecInfo mediaCodecInfo, final String s, int n, int n2) {
        if (n != -1 && n2 != -1) {
            final int hashCode = s.hashCode();
            final int n3 = 4;
            int n4 = 0;
            Label_0180: {
                switch (hashCode) {
                    case 1599127257: {
                        if (s.equals("video/x-vnd.on2.vp9")) {
                            n4 = 5;
                            break Label_0180;
                        }
                        break;
                    }
                    case 1599127256: {
                        if (s.equals("video/x-vnd.on2.vp8")) {
                            n4 = 3;
                            break Label_0180;
                        }
                        break;
                    }
                    case 1331836730: {
                        if (s.equals("video/avc")) {
                            n4 = 2;
                            break Label_0180;
                        }
                        break;
                    }
                    case 1187890754: {
                        if (s.equals("video/mp4v-es")) {
                            n4 = 1;
                            break Label_0180;
                        }
                        break;
                    }
                    case -1662541442: {
                        if (s.equals("video/hevc")) {
                            n4 = 4;
                            break Label_0180;
                        }
                        break;
                    }
                    case -1664118616: {
                        if (s.equals("video/3gpp")) {
                            n4 = 0;
                            break Label_0180;
                        }
                        break;
                    }
                }
                n4 = -1;
            }
            Label_0310: {
                if (n4 != 0 && n4 != 1) {
                    if (n4 == 2) {
                        if (!"BRAVIA 4K 2015".equals(Util.MODEL)) {
                            if ("Amazon".equals(Util.MANUFACTURER)) {
                                if ("KFSOWI".equals(Util.MODEL)) {
                                    return -1;
                                }
                                if ("AFTS".equals(Util.MODEL) && mediaCodecInfo.secure) {
                                    return -1;
                                }
                            }
                            n = Util.ceilDivide(n, 16) * Util.ceilDivide(n2, 16) * 16 * 16;
                            break Label_0310;
                        }
                        return -1;
                    }
                    if (n4 != 3) {
                        if (n4 != 4 && n4 != 5) {
                            return -1;
                        }
                        n *= n2;
                        n2 = n3;
                        return n * 3 / (n2 * 2);
                    }
                }
                n *= n2;
            }
            n2 = 2;
            return n * 3 / (n2 * 2);
        }
        return -1;
    }
    
    private static Point getCodecMaxSize(final MediaCodecInfo mediaCodecInfo, final Format format) throws MediaCodecUtil.DecoderQueryException {
        final int height = format.height;
        final int width = format.width;
        int i = 0;
        final boolean b = height > width;
        int n;
        if (b) {
            n = format.height;
        }
        else {
            n = format.width;
        }
        int n2;
        if (b) {
            n2 = format.width;
        }
        else {
            n2 = format.height;
        }
        final float n3 = n2 / (float)n;
        for (int[] standard_LONG_EDGE_VIDEO_PX = MediaCodecVideoRenderer.STANDARD_LONG_EDGE_VIDEO_PX; i < standard_LONG_EDGE_VIDEO_PX.length; ++i) {
            int n4 = standard_LONG_EDGE_VIDEO_PX[i];
            final int n5 = (int)(n4 * n3);
            if (n4 <= n) {
                break;
            }
            if (n5 <= n2) {
                break;
            }
            if (Util.SDK_INT >= 21) {
                int n6;
                if (b) {
                    n6 = n5;
                }
                else {
                    n6 = n4;
                }
                if (!b) {
                    n4 = n5;
                }
                final Point alignVideoSizeV21 = mediaCodecInfo.alignVideoSizeV21(n6, n4);
                if (mediaCodecInfo.isVideoSizeAndRateSupportedV21(alignVideoSizeV21.x, alignVideoSizeV21.y, format.frameRate)) {
                    return alignVideoSizeV21;
                }
            }
            else {
                final int n7 = Util.ceilDivide(n4, 16) * 16;
                int n8 = Util.ceilDivide(n5, 16) * 16;
                if (n7 * n8 <= MediaCodecUtil.maxH264DecodableFrameSize()) {
                    int n9;
                    if (b) {
                        n9 = n8;
                    }
                    else {
                        n9 = n7;
                    }
                    if (b) {
                        n8 = n7;
                    }
                    return new Point(n9, n8);
                }
            }
        }
        return null;
    }
    
    private static int getMaxInputSize(final MediaCodecInfo mediaCodecInfo, final Format format) {
        if (format.maxInputSize != -1) {
            final int size = format.initializationData.size();
            int i = 0;
            int n = 0;
            while (i < size) {
                n += format.initializationData.get(i).length;
                ++i;
            }
            return format.maxInputSize + n;
        }
        return getCodecMaxInputSize(mediaCodecInfo, format.sampleMimeType, format.width, format.height);
    }
    
    private static boolean isBufferLate(final long n) {
        return n < -30000L;
    }
    
    private static boolean isBufferVeryLate(final long n) {
        return n < -500000L;
    }
    
    private void maybeNotifyDroppedFrames() {
        if (this.droppedFrames > 0) {
            final long elapsedRealtime = SystemClock.elapsedRealtime();
            this.eventDispatcher.droppedFrames(this.droppedFrames, elapsedRealtime - this.droppedFrameAccumulationStartTimeMs);
            this.droppedFrames = 0;
            this.droppedFrameAccumulationStartTimeMs = elapsedRealtime;
        }
    }
    
    private void maybeNotifyVideoSizeChanged() {
        if ((this.currentWidth != -1 || this.currentHeight != -1) && (this.reportedWidth != this.currentWidth || this.reportedHeight != this.currentHeight || this.reportedUnappliedRotationDegrees != this.currentUnappliedRotationDegrees || this.reportedPixelWidthHeightRatio != this.currentPixelWidthHeightRatio)) {
            this.eventDispatcher.videoSizeChanged(this.currentWidth, this.currentHeight, this.currentUnappliedRotationDegrees, this.currentPixelWidthHeightRatio);
            this.reportedWidth = this.currentWidth;
            this.reportedHeight = this.currentHeight;
            this.reportedUnappliedRotationDegrees = this.currentUnappliedRotationDegrees;
            this.reportedPixelWidthHeightRatio = this.currentPixelWidthHeightRatio;
        }
    }
    
    private void maybeRenotifyRenderedFirstFrame() {
        if (this.renderedFirstFrame) {
            this.eventDispatcher.renderedFirstFrame(this.surface);
        }
    }
    
    private void maybeRenotifyVideoSizeChanged() {
        if (this.reportedWidth != -1 || this.reportedHeight != -1) {
            this.eventDispatcher.videoSizeChanged(this.reportedWidth, this.reportedHeight, this.reportedUnappliedRotationDegrees, this.reportedPixelWidthHeightRatio);
        }
    }
    
    private void notifyFrameMetadataListener(final long n, final long n2, final Format format) {
        final VideoFrameMetadataListener frameMetadataListener = this.frameMetadataListener;
        if (frameMetadataListener != null) {
            frameMetadataListener.onVideoFrameAboutToBeRendered(n, n2, format);
        }
    }
    
    private void processOutputFormat(final MediaCodec mediaCodec, int n, final int currentHeight) {
        this.currentWidth = n;
        this.currentHeight = currentHeight;
        this.currentPixelWidthHeightRatio = this.pendingPixelWidthHeightRatio;
        if (Util.SDK_INT >= 21) {
            n = this.pendingRotationDegrees;
            if (n == 90 || n == 270) {
                n = this.currentWidth;
                this.currentWidth = this.currentHeight;
                this.currentHeight = n;
                this.currentPixelWidthHeightRatio = 1.0f / this.currentPixelWidthHeightRatio;
            }
        }
        else {
            this.currentUnappliedRotationDegrees = this.pendingRotationDegrees;
        }
        mediaCodec.setVideoScalingMode(this.scalingMode);
    }
    
    private void setJoiningDeadlineMs() {
        long joiningDeadlineMs;
        if (this.allowedJoiningTimeMs > 0L) {
            joiningDeadlineMs = SystemClock.elapsedRealtime() + this.allowedJoiningTimeMs;
        }
        else {
            joiningDeadlineMs = -9223372036854775807L;
        }
        this.joiningDeadlineMs = joiningDeadlineMs;
    }
    
    @TargetApi(23)
    private static void setOutputSurfaceV23(final MediaCodec mediaCodec, final Surface outputSurface) {
        mediaCodec.setOutputSurface(outputSurface);
    }
    
    private void setSurface(final Surface surface) throws ExoPlaybackException {
        Surface surface2 = surface;
        if (surface == null) {
            surface2 = this.dummySurface;
            if (surface2 == null) {
                final MediaCodecInfo codecInfo = this.getCodecInfo();
                surface2 = surface;
                if (codecInfo != null) {
                    surface2 = surface;
                    if (this.shouldUseDummySurface(codecInfo)) {
                        this.dummySurface = DummySurface.newInstanceV17(this.context, codecInfo.secure);
                        surface2 = this.dummySurface;
                    }
                }
            }
        }
        if (this.surface != surface2) {
            this.surface = surface2;
            final int state = this.getState();
            final MediaCodec codec = this.getCodec();
            if (codec != null) {
                if (Util.SDK_INT >= 23 && surface2 != null && !this.codecNeedsSetOutputSurfaceWorkaround) {
                    setOutputSurfaceV23(codec, surface2);
                }
                else {
                    this.releaseCodec();
                    this.maybeInitCodec();
                }
            }
            if (surface2 != null && surface2 != this.dummySurface) {
                this.maybeRenotifyVideoSizeChanged();
                this.clearRenderedFirstFrame();
                if (state == 2) {
                    this.setJoiningDeadlineMs();
                }
            }
            else {
                this.clearReportedVideoSize();
                this.clearRenderedFirstFrame();
            }
        }
        else if (surface2 != null && surface2 != this.dummySurface) {
            this.maybeRenotifyVideoSizeChanged();
            this.maybeRenotifyRenderedFirstFrame();
        }
    }
    
    private boolean shouldUseDummySurface(final MediaCodecInfo mediaCodecInfo) {
        return Util.SDK_INT >= 23 && !this.tunneling && !this.codecNeedsSetOutputSurfaceWorkaround(mediaCodecInfo.name) && (!mediaCodecInfo.secure || DummySurface.isSecureSupported(this.context));
    }
    
    @Override
    protected int canKeepCodec(final MediaCodec mediaCodec, final MediaCodecInfo mediaCodecInfo, final Format format, final Format format2) {
        if (mediaCodecInfo.isSeamlessAdaptationSupported(format, format2, true)) {
            final int width = format2.width;
            final CodecMaxValues codecMaxValues = this.codecMaxValues;
            if (width <= codecMaxValues.width && format2.height <= codecMaxValues.height && getMaxInputSize(mediaCodecInfo, format2) <= this.codecMaxValues.inputSize) {
                int n;
                if (format.initializationDataEquals(format2)) {
                    n = 3;
                }
                else {
                    n = 2;
                }
                return n;
            }
        }
        return 0;
    }
    
    protected boolean codecNeedsSetOutputSurfaceWorkaround(String s) {
        final boolean startsWith = s.startsWith("OMX.google");
        final int n = 0;
        if (startsWith) {
            return false;
        }
        synchronized (MediaCodecVideoRenderer.class) {
            if (!MediaCodecVideoRenderer.evaluatedDeviceNeedsSetOutputSurfaceWorkaround) {
                if (Util.SDK_INT <= 27 && "dangal".equals(Util.DEVICE)) {
                    MediaCodecVideoRenderer.deviceNeedsSetOutputSurfaceWorkaround = true;
                }
                else if (Util.SDK_INT < 27) {
                    s = Util.DEVICE;
                    int n2 = 0;
                    Label_3180: {
                        switch (s.hashCode()) {
                            case 2048855701: {
                                if (s.equals("HWWAS-H")) {
                                    n2 = 54;
                                    break Label_3180;
                                }
                                break;
                            }
                            case 2048319463: {
                                if (s.equals("HWVNS-H")) {
                                    n2 = 53;
                                    break Label_3180;
                                }
                                break;
                            }
                            case 2047252157: {
                                if (s.equals("ELUGA_Prim")) {
                                    n2 = 27;
                                    break Label_3180;
                                }
                                break;
                            }
                            case 2047190025: {
                                if (s.equals("ELUGA_Note")) {
                                    n2 = 26;
                                    break Label_3180;
                                }
                                break;
                            }
                            case 2033393791: {
                                if (s.equals("ASUS_X00AD_2")) {
                                    n2 = 11;
                                    break Label_3180;
                                }
                                break;
                            }
                            case 2030379515: {
                                if (s.equals("HWCAM-H")) {
                                    n2 = 52;
                                    break Label_3180;
                                }
                                break;
                            }
                            case 2029784656: {
                                if (s.equals("HWBLN-H")) {
                                    n2 = 51;
                                    break Label_3180;
                                }
                                break;
                            }
                            case 2006372676: {
                                if (s.equals("BRAVIA_ATV3_4K")) {
                                    n2 = 15;
                                    break Label_3180;
                                }
                                break;
                            }
                            case 1977196784: {
                                if (s.equals("Infinix-X572")) {
                                    n2 = 57;
                                    break Label_3180;
                                }
                                break;
                            }
                            case 1906253259: {
                                if (s.equals("PB2-670M")) {
                                    n2 = 85;
                                    break Label_3180;
                                }
                                break;
                            }
                            case 1865889110: {
                                if (s.equals("santoni")) {
                                    n2 = 101;
                                    break Label_3180;
                                }
                                break;
                            }
                            case 1709443163: {
                                if (s.equals("iball8735_9806")) {
                                    n2 = 56;
                                    break Label_3180;
                                }
                                break;
                            }
                            case 1691543273: {
                                if (s.equals("CPH1609")) {
                                    n2 = 19;
                                    break Label_3180;
                                }
                                break;
                            }
                            case 1522194893: {
                                if (s.equals("woods_f")) {
                                    n2 = 117;
                                    break Label_3180;
                                }
                                break;
                            }
                            case 1349174697: {
                                if (s.equals("htc_e56ml_dtul")) {
                                    n2 = 49;
                                    break Label_3180;
                                }
                                break;
                            }
                            case 1306947716: {
                                if (s.equals("EverStar_S")) {
                                    n2 = 29;
                                    break Label_3180;
                                }
                                break;
                            }
                            case 1280332038: {
                                if (s.equals("hwALE-H")) {
                                    n2 = 50;
                                    break Label_3180;
                                }
                                break;
                            }
                            case 1176899427: {
                                if (s.equals("itel_S41")) {
                                    n2 = 59;
                                    break Label_3180;
                                }
                                break;
                            }
                            case 1150207623: {
                                if (s.equals("LS-5017")) {
                                    n2 = 65;
                                    break Label_3180;
                                }
                                break;
                            }
                            case 1060579533: {
                                if (s.equals("panell_d")) {
                                    n2 = 81;
                                    break Label_3180;
                                }
                                break;
                            }
                            case 958008161: {
                                if (s.equals("j2xlteins")) {
                                    n2 = 60;
                                    break Label_3180;
                                }
                                break;
                            }
                            case 917340916: {
                                if (s.equals("A7000plus")) {
                                    n2 = 7;
                                    break Label_3180;
                                }
                                break;
                            }
                            case 835649806: {
                                if (s.equals("manning")) {
                                    n2 = 67;
                                    break Label_3180;
                                }
                                break;
                            }
                            case 794040393: {
                                if (s.equals("GIONEE_WBL7519")) {
                                    n2 = 47;
                                    break Label_3180;
                                }
                                break;
                            }
                            case 794038622: {
                                if (s.equals("GIONEE_WBL7365")) {
                                    n2 = 46;
                                    break Label_3180;
                                }
                                break;
                            }
                            case 793982701: {
                                if (s.equals("GIONEE_WBL5708")) {
                                    n2 = 45;
                                    break Label_3180;
                                }
                                break;
                            }
                            case 507412548: {
                                if (s.equals("QM16XE_U")) {
                                    n2 = 99;
                                    break Label_3180;
                                }
                                break;
                            }
                            case 407160593: {
                                if (s.equals("Pixi5-10_4G")) {
                                    n2 = 91;
                                    break Label_3180;
                                }
                                break;
                            }
                            case 316246818: {
                                if (s.equals("TB3-850M")) {
                                    n2 = 109;
                                    break Label_3180;
                                }
                                break;
                            }
                            case 316246811: {
                                if (s.equals("TB3-850F")) {
                                    n2 = 108;
                                    break Label_3180;
                                }
                                break;
                            }
                            case 316215116: {
                                if (s.equals("TB3-730X")) {
                                    n2 = 107;
                                    break Label_3180;
                                }
                                break;
                            }
                            case 316215098: {
                                if (s.equals("TB3-730F")) {
                                    n2 = 106;
                                    break Label_3180;
                                }
                                break;
                            }
                            case 308517133: {
                                if (s.equals("A7020a48")) {
                                    n2 = 9;
                                    break Label_3180;
                                }
                                break;
                            }
                            case 307593612: {
                                if (s.equals("A7010a48")) {
                                    n2 = 8;
                                    break Label_3180;
                                }
                                break;
                            }
                            case 287431619: {
                                if (s.equals("griffin")) {
                                    n2 = 48;
                                    break Label_3180;
                                }
                                break;
                            }
                            case 245388979: {
                                if (s.equals("marino_f")) {
                                    n2 = 68;
                                    break Label_3180;
                                }
                                break;
                            }
                            case 182191441: {
                                if (s.equals("CPY83_I00")) {
                                    n2 = 20;
                                    break Label_3180;
                                }
                                break;
                            }
                            case 165221241: {
                                if (s.equals("A2016a40")) {
                                    n2 = 5;
                                    break Label_3180;
                                }
                                break;
                            }
                            case 102844228: {
                                if (s.equals("le_x6")) {
                                    n2 = 64;
                                    break Label_3180;
                                }
                                break;
                            }
                            case 98715550: {
                                if (s.equals("i9031")) {
                                    n2 = 55;
                                    break Label_3180;
                                }
                                break;
                            }
                            case 82882791: {
                                if (s.equals("X3_HK")) {
                                    n2 = 119;
                                    break Label_3180;
                                }
                                break;
                            }
                            case 80963634: {
                                if (s.equals("V23GB")) {
                                    n2 = 112;
                                    break Label_3180;
                                }
                                break;
                            }
                            case 76404911: {
                                if (s.equals("Q4310")) {
                                    n2 = 97;
                                    break Label_3180;
                                }
                                break;
                            }
                            case 76404105: {
                                if (s.equals("Q4260")) {
                                    n2 = 95;
                                    break Label_3180;
                                }
                                break;
                            }
                            case 76402249: {
                                if (s.equals("PRO7S")) {
                                    n2 = 93;
                                    break Label_3180;
                                }
                                break;
                            }
                            case 66216390: {
                                if (s.equals("F3311")) {
                                    n2 = 36;
                                    break Label_3180;
                                }
                                break;
                            }
                            case 66215433: {
                                if (s.equals("F3215")) {
                                    n2 = 35;
                                    break Label_3180;
                                }
                                break;
                            }
                            case 66215431: {
                                if (s.equals("F3213")) {
                                    n2 = 34;
                                    break Label_3180;
                                }
                                break;
                            }
                            case 66215429: {
                                if (s.equals("F3211")) {
                                    n2 = 33;
                                    break Label_3180;
                                }
                                break;
                            }
                            case 66214473: {
                                if (s.equals("F3116")) {
                                    n2 = 32;
                                    break Label_3180;
                                }
                                break;
                            }
                            case 66214470: {
                                if (s.equals("F3113")) {
                                    n2 = 31;
                                    break Label_3180;
                                }
                                break;
                            }
                            case 66214468: {
                                if (s.equals("F3111")) {
                                    n2 = 30;
                                    break Label_3180;
                                }
                                break;
                            }
                            case 65355429: {
                                if (s.equals("E5643")) {
                                    n2 = 24;
                                    break Label_3180;
                                }
                                break;
                            }
                            case 61542055: {
                                if (s.equals("A1601")) {
                                    n2 = 4;
                                    break Label_3180;
                                }
                                break;
                            }
                            case 55178625: {
                                if (s.equals("Aura_Note_2")) {
                                    n2 = 12;
                                    break Label_3180;
                                }
                                break;
                            }
                            case 41325051: {
                                if (s.equals("MEIZU_M5")) {
                                    n2 = 69;
                                    break Label_3180;
                                }
                                break;
                            }
                            case 3386211: {
                                if (s.equals("p212")) {
                                    n2 = 78;
                                    break Label_3180;
                                }
                                break;
                            }
                            case 3351335: {
                                if (s.equals("mido")) {
                                    n2 = 71;
                                    break Label_3180;
                                }
                                break;
                            }
                            case 3284551: {
                                if (s.equals("kate")) {
                                    n2 = 63;
                                    break Label_3180;
                                }
                                break;
                            }
                            case 3154429: {
                                if (s.equals("fugu")) {
                                    n2 = 38;
                                    break Label_3180;
                                }
                                break;
                            }
                            case 2689555: {
                                if (s.equals("XE2X")) {
                                    n2 = 120;
                                    break Label_3180;
                                }
                                break;
                            }
                            case 2464648: {
                                if (s.equals("Q427")) {
                                    n2 = 96;
                                    break Label_3180;
                                }
                                break;
                            }
                            case 2463773: {
                                if (s.equals("Q350")) {
                                    n2 = 94;
                                    break Label_3180;
                                }
                                break;
                            }
                            case 2436959: {
                                if (s.equals("P681")) {
                                    n2 = 79;
                                    break Label_3180;
                                }
                                break;
                            }
                            case 1514185: {
                                if (s.equals("1714")) {
                                    n2 = 2;
                                    break Label_3180;
                                }
                                break;
                            }
                            case 1514184: {
                                if (s.equals("1713")) {
                                    n2 = 1;
                                    break Label_3180;
                                }
                                break;
                            }
                            case 1513190: {
                                if (s.equals("1601")) {
                                    n2 = 0;
                                    break Label_3180;
                                }
                                break;
                            }
                            case 101481: {
                                if (s.equals("flo")) {
                                    n2 = 37;
                                    break Label_3180;
                                }
                                break;
                            }
                            case 99329: {
                                if (s.equals("deb")) {
                                    n2 = 23;
                                    break Label_3180;
                                }
                                break;
                            }
                            case 98848: {
                                if (s.equals("cv3")) {
                                    n2 = 22;
                                    break Label_3180;
                                }
                                break;
                            }
                            case 98846: {
                                if (s.equals("cv1")) {
                                    n2 = 21;
                                    break Label_3180;
                                }
                                break;
                            }
                            case 88274: {
                                if (s.equals("Z80")) {
                                    n2 = 123;
                                    break Label_3180;
                                }
                                break;
                            }
                            case 80618: {
                                if (s.equals("QX1")) {
                                    n2 = 100;
                                    break Label_3180;
                                }
                                break;
                            }
                            case 79305: {
                                if (s.equals("PLE")) {
                                    n2 = 92;
                                    break Label_3180;
                                }
                                break;
                            }
                            case 78669: {
                                if (s.equals("P85")) {
                                    n2 = 80;
                                    break Label_3180;
                                }
                                break;
                            }
                            case 76779: {
                                if (s.equals("MX6")) {
                                    n2 = 72;
                                    break Label_3180;
                                }
                                break;
                            }
                            case 75739: {
                                if (s.equals("M5c")) {
                                    n2 = 66;
                                    break Label_3180;
                                }
                                break;
                            }
                            case 73405: {
                                if (s.equals("JGZ")) {
                                    n2 = 61;
                                    break Label_3180;
                                }
                                break;
                            }
                            case 3483: {
                                if (s.equals("mh")) {
                                    n2 = 70;
                                    break Label_3180;
                                }
                                break;
                            }
                            case 2719: {
                                if (s.equals("V5")) {
                                    n2 = 113;
                                    break Label_3180;
                                }
                                break;
                            }
                            case 2715: {
                                if (s.equals("V1")) {
                                    n2 = 111;
                                    break Label_3180;
                                }
                                break;
                            }
                            case 2564: {
                                if (s.equals("Q5")) {
                                    n2 = 98;
                                    break Label_3180;
                                }
                                break;
                            }
                            case 2126: {
                                if (s.equals("C1")) {
                                    n2 = 16;
                                    break Label_3180;
                                }
                                break;
                            }
                            case -56598463: {
                                if (s.equals("woods_fn")) {
                                    n2 = 118;
                                    break Label_3180;
                                }
                                break;
                            }
                            case -173639913: {
                                if (s.equals("ELUGA_A3_Pro")) {
                                    n2 = 25;
                                    break Label_3180;
                                }
                                break;
                            }
                            case -277133239: {
                                if (s.equals("Z12_PRO")) {
                                    n2 = 122;
                                    break Label_3180;
                                }
                                break;
                            }
                            case -282781963: {
                                if (s.equals("BLACK-1X")) {
                                    n2 = 13;
                                    break Label_3180;
                                }
                                break;
                            }
                            case -290434366: {
                                if (s.equals("taido_row")) {
                                    n2 = 105;
                                    break Label_3180;
                                }
                                break;
                            }
                            case -430914369: {
                                if (s.equals("Pixi4-7_3G")) {
                                    n2 = 90;
                                    break Label_3180;
                                }
                                break;
                            }
                            case -521118391: {
                                if (s.equals("GIONEE_GBL7360")) {
                                    n2 = 41;
                                    break Label_3180;
                                }
                                break;
                            }
                            case -575125681: {
                                if (s.equals("GiONEE_CBL7513")) {
                                    n2 = 39;
                                    break Label_3180;
                                }
                                break;
                            }
                            case -782144577: {
                                if (s.equals("OnePlus5T")) {
                                    n2 = 77;
                                    break Label_3180;
                                }
                                break;
                            }
                            case -788334647: {
                                if (s.equals("whyred")) {
                                    n2 = 116;
                                    break Label_3180;
                                }
                                break;
                            }
                            case -794946968: {
                                if (s.equals("watson")) {
                                    n2 = 115;
                                    break Label_3180;
                                }
                                break;
                            }
                            case -797483286: {
                                if (s.equals("SVP-DTV15")) {
                                    n2 = 103;
                                    break Label_3180;
                                }
                                break;
                            }
                            case -821392978: {
                                if (s.equals("A7000-a")) {
                                    n2 = 6;
                                    break Label_3180;
                                }
                                break;
                            }
                            case -842500323: {
                                if (s.equals("nicklaus_f")) {
                                    n2 = 74;
                                    break Label_3180;
                                }
                                break;
                            }
                            case -879245230: {
                                if (s.equals("tcl_eu")) {
                                    n2 = 110;
                                    break Label_3180;
                                }
                                break;
                            }
                            case -958336948: {
                                if (s.equals("ELUGA_Ray_X")) {
                                    n2 = 28;
                                    break Label_3180;
                                }
                                break;
                            }
                            case -965403638: {
                                if (s.equals("s905x018")) {
                                    n2 = 104;
                                    break Label_3180;
                                }
                                break;
                            }
                            case -993250464: {
                                if (s.equals("A10-70F")) {
                                    n2 = 3;
                                    break Label_3180;
                                }
                                break;
                            }
                            case -1052835013: {
                                if (s.equals("namath")) {
                                    n2 = 73;
                                    break Label_3180;
                                }
                                break;
                            }
                            case -1139198265: {
                                if (s.equals("Slate_Pro")) {
                                    n2 = 102;
                                    break Label_3180;
                                }
                                break;
                            }
                            case -1180384755: {
                                if (s.equals("iris60")) {
                                    n2 = 58;
                                    break Label_3180;
                                }
                                break;
                            }
                            case -1217592143: {
                                if (s.equals("BRAVIA_ATV2")) {
                                    n2 = 14;
                                    break Label_3180;
                                }
                                break;
                            }
                            case -1320080169: {
                                if (s.equals("GiONEE_GBL7319")) {
                                    n2 = 40;
                                    break Label_3180;
                                }
                                break;
                            }
                            case -1481772729: {
                                if (s.equals("panell_dt")) {
                                    n2 = 84;
                                    break Label_3180;
                                }
                                break;
                            }
                            case -1481772730: {
                                if (s.equals("panell_ds")) {
                                    n2 = 83;
                                    break Label_3180;
                                }
                                break;
                            }
                            case -1481772737: {
                                if (s.equals("panell_dl")) {
                                    n2 = 82;
                                    break Label_3180;
                                }
                                break;
                            }
                            case -1554255044: {
                                if (s.equals("vernee_M5")) {
                                    n2 = 114;
                                    break Label_3180;
                                }
                                break;
                            }
                            case -1615810839: {
                                if (s.equals("Phantom6")) {
                                    n2 = 89;
                                    break Label_3180;
                                }
                                break;
                            }
                            case -1680025915: {
                                if (s.equals("ComioS1")) {
                                    n2 = 17;
                                    break Label_3180;
                                }
                                break;
                            }
                            case -1696512866: {
                                if (s.equals("XT1663")) {
                                    n2 = 121;
                                    break Label_3180;
                                }
                                break;
                            }
                            case -1931988508: {
                                if (s.equals("AquaPowerM")) {
                                    n2 = 10;
                                    break Label_3180;
                                }
                                break;
                            }
                            case -1936688065: {
                                if (s.equals("PGN611")) {
                                    n2 = 88;
                                    break Label_3180;
                                }
                                break;
                            }
                            case -1936688066: {
                                if (s.equals("PGN610")) {
                                    n2 = 87;
                                    break Label_3180;
                                }
                                break;
                            }
                            case -1936688988: {
                                if (s.equals("PGN528")) {
                                    n2 = 86;
                                    break Label_3180;
                                }
                                break;
                            }
                            case -1978990237: {
                                if (s.equals("NX573J")) {
                                    n2 = 76;
                                    break Label_3180;
                                }
                                break;
                            }
                            case -1978993182: {
                                if (s.equals("NX541J")) {
                                    n2 = 75;
                                    break Label_3180;
                                }
                                break;
                            }
                            case -2022874474: {
                                if (s.equals("CP8676_I02")) {
                                    n2 = 18;
                                    break Label_3180;
                                }
                                break;
                            }
                            case -2097309513: {
                                if (s.equals("K50a40")) {
                                    n2 = 62;
                                    break Label_3180;
                                }
                                break;
                            }
                            case -2144781160: {
                                if (s.equals("GIONEE_SWW1631")) {
                                    n2 = 44;
                                    break Label_3180;
                                }
                                break;
                            }
                            case -2144781185: {
                                if (s.equals("GIONEE_SWW1627")) {
                                    n2 = 43;
                                    break Label_3180;
                                }
                                break;
                            }
                            case -2144781245: {
                                if (s.equals("GIONEE_SWW1609")) {
                                    n2 = 42;
                                    break Label_3180;
                                }
                                break;
                            }
                        }
                        n2 = -1;
                    }
                    switch (n2) {
                        case 0:
                        case 1:
                        case 2:
                        case 3:
                        case 4:
                        case 5:
                        case 6:
                        case 7:
                        case 8:
                        case 9:
                        case 10:
                        case 11:
                        case 12:
                        case 13:
                        case 14:
                        case 15:
                        case 16:
                        case 17:
                        case 18:
                        case 19:
                        case 20:
                        case 21:
                        case 22:
                        case 23:
                        case 24:
                        case 25:
                        case 26:
                        case 27:
                        case 28:
                        case 29:
                        case 30:
                        case 31:
                        case 32:
                        case 33:
                        case 34:
                        case 35:
                        case 36:
                        case 37:
                        case 38:
                        case 39:
                        case 40:
                        case 41:
                        case 42:
                        case 43:
                        case 44:
                        case 45:
                        case 46:
                        case 47:
                        case 48:
                        case 49:
                        case 50:
                        case 51:
                        case 52:
                        case 53:
                        case 54:
                        case 55:
                        case 56:
                        case 57:
                        case 58:
                        case 59:
                        case 60:
                        case 61:
                        case 62:
                        case 63:
                        case 64:
                        case 65:
                        case 66:
                        case 67:
                        case 68:
                        case 69:
                        case 70:
                        case 71:
                        case 72:
                        case 73:
                        case 74:
                        case 75:
                        case 76:
                        case 77:
                        case 78:
                        case 79:
                        case 80:
                        case 81:
                        case 82:
                        case 83:
                        case 84:
                        case 85:
                        case 86:
                        case 87:
                        case 88:
                        case 89:
                        case 90:
                        case 91:
                        case 92:
                        case 93:
                        case 94:
                        case 95:
                        case 96:
                        case 97:
                        case 98:
                        case 99:
                        case 100:
                        case 101:
                        case 102:
                        case 103:
                        case 104:
                        case 105:
                        case 106:
                        case 107:
                        case 108:
                        case 109:
                        case 110:
                        case 111:
                        case 112:
                        case 113:
                        case 114:
                        case 115:
                        case 116:
                        case 117:
                        case 118:
                        case 119:
                        case 120:
                        case 121:
                        case 122:
                        case 123: {
                            MediaCodecVideoRenderer.deviceNeedsSetOutputSurfaceWorkaround = true;
                            break;
                        }
                    }
                    s = Util.MODEL;
                    final int hashCode = s.hashCode();
                    int n3 = 0;
                    Label_3763: {
                        if (hashCode != 2006354) {
                            if (hashCode == 2006367) {
                                if (s.equals("AFTN")) {
                                    n3 = 1;
                                    break Label_3763;
                                }
                            }
                        }
                        else if (s.equals("AFTA")) {
                            n3 = n;
                            break Label_3763;
                        }
                        n3 = -1;
                    }
                    if (n3 == 0 || n3 == 1) {
                        MediaCodecVideoRenderer.deviceNeedsSetOutputSurfaceWorkaround = true;
                    }
                }
                MediaCodecVideoRenderer.evaluatedDeviceNeedsSetOutputSurfaceWorkaround = true;
            }
            return MediaCodecVideoRenderer.deviceNeedsSetOutputSurfaceWorkaround;
        }
    }
    
    @Override
    protected void configureCodec(final MediaCodecInfo mediaCodecInfo, final MediaCodec mediaCodec, final Format format, final MediaCrypto mediaCrypto, final float n) throws MediaCodecUtil.DecoderQueryException {
        this.codecMaxValues = this.getCodecMaxValues(mediaCodecInfo, format, this.getStreamFormats());
        final MediaFormat mediaFormat = this.getMediaFormat(format, this.codecMaxValues, n, this.deviceNeedsNoPostProcessWorkaround, this.tunnelingAudioSessionId);
        if (this.surface == null) {
            Assertions.checkState(this.shouldUseDummySurface(mediaCodecInfo));
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
    
    protected void dropOutputBuffer(final MediaCodec mediaCodec, final int n, final long n2) {
        TraceUtil.beginSection("dropVideoBuffer");
        mediaCodec.releaseOutputBuffer(n, false);
        TraceUtil.endSection();
        this.updateDroppedBufferCounters(1);
    }
    
    @Override
    protected boolean flushOrReleaseCodec() {
        try {
            return super.flushOrReleaseCodec();
        }
        finally {
            this.buffersInCodecCount = 0;
        }
    }
    
    protected CodecMaxValues getCodecMaxValues(final MediaCodecInfo mediaCodecInfo, final Format format, final Format[] array) throws MediaCodecUtil.DecoderQueryException {
        int width = format.width;
        final int height = format.height;
        int maxInputSize = getMaxInputSize(mediaCodecInfo, format);
        if (array.length == 1) {
            int min;
            if ((min = maxInputSize) != -1) {
                final int codecMaxInputSize = getCodecMaxInputSize(mediaCodecInfo, format.sampleMimeType, format.width, format.height);
                min = maxInputSize;
                if (codecMaxInputSize != -1) {
                    min = Math.min((int)(maxInputSize * 1.5f), codecMaxInputSize);
                }
            }
            return new CodecMaxValues(width, height, min);
        }
        final int length = array.length;
        int a = height;
        int n = 0;
        int n2;
        int max;
        int max2;
        int max3;
        for (int i = 0; i < length; ++i, n = n2, width = max, a = max2, maxInputSize = max3) {
            final Format format2 = array[i];
            n2 = n;
            max = width;
            max2 = a;
            max3 = maxInputSize;
            if (mediaCodecInfo.isSeamlessAdaptationSupported(format, format2, false)) {
                n2 = (n | ((format2.width == -1 || format2.height == -1) ? 1 : 0));
                max = Math.max(width, format2.width);
                max2 = Math.max(a, format2.height);
                max3 = Math.max(maxInputSize, getMaxInputSize(mediaCodecInfo, format2));
            }
        }
        int max4 = width;
        int max5 = a;
        int max6 = maxInputSize;
        if (n != 0) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Resolutions unknown. Codec max resolution: ");
            sb.append(width);
            sb.append("x");
            sb.append(a);
            Log.w("MediaCodecVideoRenderer", sb.toString());
            final Point codecMaxSize = getCodecMaxSize(mediaCodecInfo, format);
            max4 = width;
            max5 = a;
            max6 = maxInputSize;
            if (codecMaxSize != null) {
                max4 = Math.max(width, codecMaxSize.x);
                max5 = Math.max(a, codecMaxSize.y);
                max6 = Math.max(maxInputSize, getCodecMaxInputSize(mediaCodecInfo, format.sampleMimeType, max4, max5));
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("Codec max resolution adjusted to: ");
                sb2.append(max4);
                sb2.append("x");
                sb2.append(max5);
                Log.w("MediaCodecVideoRenderer", sb2.toString());
            }
        }
        return new CodecMaxValues(max4, max5, max6);
    }
    
    @Override
    protected boolean getCodecNeedsEosPropagation() {
        return this.tunneling;
    }
    
    @Override
    protected float getCodecOperatingRateV23(float n, final Format format, final Format[] array) {
        final int length = array.length;
        final float n2 = -1.0f;
        int i = 0;
        float a = -1.0f;
        while (i < length) {
            final float frameRate = array[i].frameRate;
            float max = a;
            if (frameRate != -1.0f) {
                max = Math.max(a, frameRate);
            }
            ++i;
            a = max;
        }
        if (a == -1.0f) {
            n = n2;
        }
        else {
            n *= a;
        }
        return n;
    }
    
    @SuppressLint({ "InlinedApi" })
    protected MediaFormat getMediaFormat(final Format format, final CodecMaxValues codecMaxValues, final float n, final boolean b, final int n2) {
        final MediaFormat mediaFormat = new MediaFormat();
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
            if (n != -1.0f) {
                mediaFormat.setFloat("operating-rate", n);
            }
        }
        if (b) {
            mediaFormat.setInteger("no-post-process", 1);
            mediaFormat.setInteger("auto-frc", 0);
        }
        if (n2 != 0) {
            configureTunnelingV21(mediaFormat, n2);
        }
        return mediaFormat;
    }
    
    @Override
    public void handleMessage(final int n, final Object o) throws ExoPlaybackException {
        if (n == 1) {
            this.setSurface((Surface)o);
        }
        else if (n == 4) {
            this.scalingMode = (int)o;
            final MediaCodec codec = this.getCodec();
            if (codec != null) {
                codec.setVideoScalingMode(this.scalingMode);
            }
        }
        else if (n == 6) {
            this.frameMetadataListener = (VideoFrameMetadataListener)o;
        }
        else {
            super.handleMessage(n, o);
        }
    }
    
    @Override
    public boolean isReady() {
        Label_0054: {
            if (super.isReady()) {
                if (!this.renderedFirstFrame) {
                    final Surface dummySurface = this.dummySurface;
                    if ((dummySurface == null || this.surface != dummySurface) && this.getCodec() != null && !this.tunneling) {
                        break Label_0054;
                    }
                }
                this.joiningDeadlineMs = -9223372036854775807L;
                return true;
            }
        }
        if (this.joiningDeadlineMs == -9223372036854775807L) {
            return false;
        }
        if (SystemClock.elapsedRealtime() < this.joiningDeadlineMs) {
            return true;
        }
        this.joiningDeadlineMs = -9223372036854775807L;
        return false;
    }
    
    protected boolean maybeDropBuffersToKeyframe(final MediaCodec mediaCodec, int skipSource, final long n, final long n2) throws ExoPlaybackException {
        skipSource = this.skipSource(n2);
        if (skipSource == 0) {
            return false;
        }
        final DecoderCounters decoderCounters = super.decoderCounters;
        ++decoderCounters.droppedToKeyframeCount;
        this.updateDroppedBufferCounters(this.buffersInCodecCount + skipSource);
        this.flushOrReinitCodec();
        return true;
    }
    
    void maybeNotifyRenderedFirstFrame() {
        if (!this.renderedFirstFrame) {
            this.renderedFirstFrame = true;
            this.eventDispatcher.renderedFirstFrame(this.surface);
        }
    }
    
    @Override
    protected void onCodecInitialized(final String s, final long n, final long n2) {
        this.eventDispatcher.decoderInitialized(s, n, n2);
        this.codecNeedsSetOutputSurfaceWorkaround = this.codecNeedsSetOutputSurfaceWorkaround(s);
    }
    
    @Override
    protected void onDisabled() {
        this.lastInputTimeUs = -9223372036854775807L;
        this.outputStreamOffsetUs = -9223372036854775807L;
        this.pendingOutputStreamOffsetCount = 0;
        this.clearReportedVideoSize();
        this.clearRenderedFirstFrame();
        this.frameReleaseTimeHelper.disable();
        this.tunnelingOnFrameRenderedListener = null;
        try {
            super.onDisabled();
        }
        finally {
            this.eventDispatcher.disabled(super.decoderCounters);
        }
    }
    
    @Override
    protected void onEnabled(final boolean b) throws ExoPlaybackException {
        super.onEnabled(b);
        final int tunnelingAudioSessionId = this.tunnelingAudioSessionId;
        this.tunnelingAudioSessionId = this.getConfiguration().tunnelingAudioSessionId;
        this.tunneling = (this.tunnelingAudioSessionId != 0);
        if (this.tunnelingAudioSessionId != tunnelingAudioSessionId) {
            this.releaseCodec();
        }
        this.eventDispatcher.enabled(super.decoderCounters);
        this.frameReleaseTimeHelper.enable();
    }
    
    @Override
    protected void onInputFormatChanged(final Format format) throws ExoPlaybackException {
        super.onInputFormatChanged(format);
        this.eventDispatcher.inputFormatChanged(format);
        this.pendingPixelWidthHeightRatio = format.pixelWidthHeightRatio;
        this.pendingRotationDegrees = format.rotationDegrees;
    }
    
    @Override
    protected void onOutputFormatChanged(final MediaCodec mediaCodec, final MediaFormat mediaFormat) {
        final boolean b = mediaFormat.containsKey("crop-right") && mediaFormat.containsKey("crop-left") && mediaFormat.containsKey("crop-bottom") && mediaFormat.containsKey("crop-top");
        int integer;
        if (b) {
            integer = mediaFormat.getInteger("crop-right") - mediaFormat.getInteger("crop-left") + 1;
        }
        else {
            integer = mediaFormat.getInteger("width");
        }
        int integer2;
        if (b) {
            integer2 = mediaFormat.getInteger("crop-bottom") - mediaFormat.getInteger("crop-top") + 1;
        }
        else {
            integer2 = mediaFormat.getInteger("height");
        }
        this.processOutputFormat(mediaCodec, integer, integer2);
    }
    
    @Override
    protected void onPositionReset(final long n, final boolean b) throws ExoPlaybackException {
        super.onPositionReset(n, b);
        this.clearRenderedFirstFrame();
        this.initialPositionUs = -9223372036854775807L;
        this.consecutiveDroppedFrameCount = 0;
        this.lastInputTimeUs = -9223372036854775807L;
        final int pendingOutputStreamOffsetCount = this.pendingOutputStreamOffsetCount;
        if (pendingOutputStreamOffsetCount != 0) {
            this.outputStreamOffsetUs = this.pendingOutputStreamOffsetsUs[pendingOutputStreamOffsetCount - 1];
            this.pendingOutputStreamOffsetCount = 0;
        }
        if (b) {
            this.setJoiningDeadlineMs();
        }
        else {
            this.joiningDeadlineMs = -9223372036854775807L;
        }
    }
    
    @Override
    protected void onProcessedOutputBuffer(final long n) {
        --this.buffersInCodecCount;
        while (true) {
            final int pendingOutputStreamOffsetCount = this.pendingOutputStreamOffsetCount;
            if (pendingOutputStreamOffsetCount == 0 || n < this.pendingOutputStreamSwitchTimesUs[0]) {
                break;
            }
            final long[] pendingOutputStreamOffsetsUs = this.pendingOutputStreamOffsetsUs;
            this.outputStreamOffsetUs = pendingOutputStreamOffsetsUs[0];
            System.arraycopy(pendingOutputStreamOffsetsUs, 1, pendingOutputStreamOffsetsUs, 0, this.pendingOutputStreamOffsetCount = pendingOutputStreamOffsetCount - 1);
            final long[] pendingOutputStreamSwitchTimesUs = this.pendingOutputStreamSwitchTimesUs;
            System.arraycopy(pendingOutputStreamSwitchTimesUs, 1, pendingOutputStreamSwitchTimesUs, 0, this.pendingOutputStreamOffsetCount);
        }
    }
    
    protected void onProcessedTunneledBuffer(final long n) {
        final Format updateOutputFormatForTime = this.updateOutputFormatForTime(n);
        if (updateOutputFormatForTime != null) {
            this.processOutputFormat(this.getCodec(), updateOutputFormatForTime.width, updateOutputFormatForTime.height);
        }
        this.maybeNotifyVideoSizeChanged();
        this.maybeNotifyRenderedFirstFrame();
        this.onProcessedOutputBuffer(n);
    }
    
    @Override
    protected void onQueueInputBuffer(final DecoderInputBuffer decoderInputBuffer) {
        ++this.buffersInCodecCount;
        this.lastInputTimeUs = Math.max(decoderInputBuffer.timeUs, this.lastInputTimeUs);
        if (Util.SDK_INT < 23 && this.tunneling) {
            this.onProcessedTunneledBuffer(decoderInputBuffer.timeUs);
        }
    }
    
    @Override
    protected void onReset() {
        try {
            super.onReset();
        }
        finally {
            final Surface dummySurface = this.dummySurface;
            if (dummySurface != null) {
                if (this.surface == dummySurface) {
                    this.surface = null;
                }
                this.dummySurface.release();
                this.dummySurface = null;
            }
        }
    }
    
    @Override
    protected void onStarted() {
        super.onStarted();
        this.droppedFrames = 0;
        this.droppedFrameAccumulationStartTimeMs = SystemClock.elapsedRealtime();
        this.lastRenderTimeUs = SystemClock.elapsedRealtime() * 1000L;
    }
    
    @Override
    protected void onStopped() {
        this.joiningDeadlineMs = -9223372036854775807L;
        this.maybeNotifyDroppedFrames();
        super.onStopped();
    }
    
    @Override
    protected void onStreamChanged(final Format[] array, final long outputStreamOffsetUs) throws ExoPlaybackException {
        if (this.outputStreamOffsetUs == -9223372036854775807L) {
            this.outputStreamOffsetUs = outputStreamOffsetUs;
        }
        else {
            final int pendingOutputStreamOffsetCount = this.pendingOutputStreamOffsetCount;
            if (pendingOutputStreamOffsetCount == this.pendingOutputStreamOffsetsUs.length) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Too many stream changes, so dropping offset: ");
                sb.append(this.pendingOutputStreamOffsetsUs[this.pendingOutputStreamOffsetCount - 1]);
                Log.w("MediaCodecVideoRenderer", sb.toString());
            }
            else {
                this.pendingOutputStreamOffsetCount = pendingOutputStreamOffsetCount + 1;
            }
            final long[] pendingOutputStreamOffsetsUs = this.pendingOutputStreamOffsetsUs;
            final int pendingOutputStreamOffsetCount2 = this.pendingOutputStreamOffsetCount;
            pendingOutputStreamOffsetsUs[pendingOutputStreamOffsetCount2 - 1] = outputStreamOffsetUs;
            this.pendingOutputStreamSwitchTimesUs[pendingOutputStreamOffsetCount2 - 1] = this.lastInputTimeUs;
        }
        super.onStreamChanged(array, outputStreamOffsetUs);
    }
    
    @Override
    protected boolean processOutputBuffer(long nanoTime, final long n, final MediaCodec mediaCodec, final ByteBuffer byteBuffer, final int n2, int n3, long adjustReleaseTime, final boolean b, final Format format) throws ExoPlaybackException {
        if (this.initialPositionUs == -9223372036854775807L) {
            this.initialPositionUs = nanoTime;
        }
        final long n4 = adjustReleaseTime - this.outputStreamOffsetUs;
        if (b) {
            this.skipOutputBuffer(mediaCodec, n2, n4);
            return true;
        }
        final long n5 = adjustReleaseTime - nanoTime;
        if (this.surface == this.dummySurface) {
            if (isBufferLate(n5)) {
                this.skipOutputBuffer(mediaCodec, n2, n4);
                return true;
            }
            return false;
        }
        else {
            final long n6 = SystemClock.elapsedRealtime() * 1000L;
            if (this.getState() == 2) {
                n3 = 1;
            }
            else {
                n3 = 0;
            }
            if (this.renderedFirstFrame && (n3 == 0 || !this.shouldForceRenderOutputBuffer(n5, n6 - this.lastRenderTimeUs))) {
                if (n3 != 0) {
                    if (nanoTime != this.initialPositionUs) {
                        final long nanoTime2 = System.nanoTime();
                        adjustReleaseTime = this.frameReleaseTimeHelper.adjustReleaseTime(adjustReleaseTime, (n5 - (n6 - n)) * 1000L + nanoTime2);
                        final long n7 = (adjustReleaseTime - nanoTime2) / 1000L;
                        if (this.shouldDropBuffersToKeyframe(n7, n) && this.maybeDropBuffersToKeyframe(mediaCodec, n2, n4, nanoTime)) {
                            return false;
                        }
                        if (this.shouldDropOutputBuffer(n7, n)) {
                            this.dropOutputBuffer(mediaCodec, n2, n4);
                        }
                        else if (Util.SDK_INT >= 21) {
                            if (n7 >= 50000L) {
                                return false;
                            }
                            this.notifyFrameMetadataListener(n4, adjustReleaseTime, format);
                            this.renderOutputBufferV21(mediaCodec, n2, n4, adjustReleaseTime);
                        }
                        else {
                            if (n7 >= 30000L) {
                                return false;
                            }
                            if (n7 > 11000L) {
                                try {
                                    Thread.sleep((n7 - 10000L) / 1000L);
                                }
                                catch (InterruptedException ex) {
                                    Thread.currentThread().interrupt();
                                    return false;
                                }
                            }
                            this.notifyFrameMetadataListener(n4, adjustReleaseTime, format);
                            this.renderOutputBuffer(mediaCodec, n2, n4);
                        }
                        return true;
                    }
                }
                return false;
            }
            nanoTime = System.nanoTime();
            this.notifyFrameMetadataListener(n4, nanoTime, format);
            if (Util.SDK_INT >= 21) {
                this.renderOutputBufferV21(mediaCodec, n2, n4, nanoTime);
            }
            else {
                this.renderOutputBuffer(mediaCodec, n2, n4);
            }
            return true;
        }
    }
    
    @Override
    protected void releaseCodec() {
        try {
            super.releaseCodec();
        }
        finally {
            this.buffersInCodecCount = 0;
        }
    }
    
    protected void renderOutputBuffer(final MediaCodec mediaCodec, final int n, final long n2) {
        this.maybeNotifyVideoSizeChanged();
        TraceUtil.beginSection("releaseOutputBuffer");
        mediaCodec.releaseOutputBuffer(n, true);
        TraceUtil.endSection();
        this.lastRenderTimeUs = SystemClock.elapsedRealtime() * 1000L;
        final DecoderCounters decoderCounters = super.decoderCounters;
        ++decoderCounters.renderedOutputBufferCount;
        this.consecutiveDroppedFrameCount = 0;
        this.maybeNotifyRenderedFirstFrame();
    }
    
    @TargetApi(21)
    protected void renderOutputBufferV21(final MediaCodec mediaCodec, final int n, final long n2, final long n3) {
        this.maybeNotifyVideoSizeChanged();
        TraceUtil.beginSection("releaseOutputBuffer");
        mediaCodec.releaseOutputBuffer(n, n3);
        TraceUtil.endSection();
        this.lastRenderTimeUs = SystemClock.elapsedRealtime() * 1000L;
        final DecoderCounters decoderCounters = super.decoderCounters;
        ++decoderCounters.renderedOutputBufferCount;
        this.consecutiveDroppedFrameCount = 0;
        this.maybeNotifyRenderedFirstFrame();
    }
    
    protected boolean shouldDropBuffersToKeyframe(final long n, final long n2) {
        return isBufferVeryLate(n);
    }
    
    protected boolean shouldDropOutputBuffer(final long n, final long n2) {
        return isBufferLate(n);
    }
    
    protected boolean shouldForceRenderOutputBuffer(final long n, final long n2) {
        return isBufferLate(n) && n2 > 100000L;
    }
    
    @Override
    protected boolean shouldInitCodec(final MediaCodecInfo mediaCodecInfo) {
        return this.surface != null || this.shouldUseDummySurface(mediaCodecInfo);
    }
    
    protected void skipOutputBuffer(final MediaCodec mediaCodec, final int n, final long n2) {
        TraceUtil.beginSection("skipVideoBuffer");
        mediaCodec.releaseOutputBuffer(n, false);
        TraceUtil.endSection();
        final DecoderCounters decoderCounters = super.decoderCounters;
        ++decoderCounters.skippedOutputBufferCount;
    }
    
    @Override
    protected int supportsFormat(final MediaCodecSelector mediaCodecSelector, final DrmSessionManager<FrameworkMediaCrypto> drmSessionManager, final Format format) throws MediaCodecUtil.DecoderQueryException {
        final boolean video = MimeTypes.isVideo(format.sampleMimeType);
        int n = 0;
        if (!video) {
            return 0;
        }
        final DrmInitData drmInitData = format.drmInitData;
        int n4;
        if (drmInitData != null) {
            int n2 = 0;
            int n3 = 0;
            while (true) {
                n4 = n3;
                if (n2 >= drmInitData.schemeDataCount) {
                    break;
                }
                n3 |= (drmInitData.get(n2).requiresSecureDecryption ? 1 : 0);
                ++n2;
            }
        }
        else {
            n4 = 0;
        }
        final List<MediaCodecInfo> decoderInfos = mediaCodecSelector.getDecoderInfos(format.sampleMimeType, (boolean)(n4 != 0));
        final boolean empty = decoderInfos.isEmpty();
        int n5 = 2;
        if (empty) {
            if (n4 == 0 || mediaCodecSelector.getDecoderInfos(format.sampleMimeType, false).isEmpty()) {
                n5 = 1;
            }
            return n5;
        }
        if (!BaseRenderer.supportsFormatDrm(drmSessionManager, drmInitData)) {
            return 2;
        }
        final MediaCodecInfo mediaCodecInfo = decoderInfos.get(0);
        final boolean formatSupported = mediaCodecInfo.isFormatSupported(format);
        int n6;
        if (mediaCodecInfo.isSeamlessAdaptationSupported(format)) {
            n6 = 16;
        }
        else {
            n6 = 8;
        }
        if (mediaCodecInfo.tunneling) {
            n = 32;
        }
        int n7;
        if (formatSupported) {
            n7 = 4;
        }
        else {
            n7 = 3;
        }
        return n7 | (n6 | n);
    }
    
    protected void updateDroppedBufferCounters(int maxDroppedFramesToNotify) {
        final DecoderCounters decoderCounters = super.decoderCounters;
        decoderCounters.droppedBufferCount += maxDroppedFramesToNotify;
        this.droppedFrames += maxDroppedFramesToNotify;
        this.consecutiveDroppedFrameCount += maxDroppedFramesToNotify;
        decoderCounters.maxConsecutiveDroppedBufferCount = Math.max(this.consecutiveDroppedFrameCount, decoderCounters.maxConsecutiveDroppedBufferCount);
        maxDroppedFramesToNotify = this.maxDroppedFramesToNotify;
        if (maxDroppedFramesToNotify > 0 && this.droppedFrames >= maxDroppedFramesToNotify) {
            this.maybeNotifyDroppedFrames();
        }
    }
    
    protected static final class CodecMaxValues
    {
        public final int height;
        public final int inputSize;
        public final int width;
        
        public CodecMaxValues(final int width, final int height, final int inputSize) {
            this.width = width;
            this.height = height;
            this.inputSize = inputSize;
        }
    }
    
    @TargetApi(23)
    private final class OnFrameRenderedListenerV23 implements MediaCodec$OnFrameRenderedListener
    {
        private OnFrameRenderedListenerV23(final MediaCodec mediaCodec) {
            mediaCodec.setOnFrameRenderedListener((MediaCodec$OnFrameRenderedListener)this, new Handler());
        }
        
        public void onFrameRendered(final MediaCodec mediaCodec, final long n, final long n2) {
            final MediaCodecVideoRenderer this$0 = MediaCodecVideoRenderer.this;
            if (this != this$0.tunnelingOnFrameRenderedListener) {
                return;
            }
            this$0.onProcessedTunneledBuffer(n);
        }
    }
}
