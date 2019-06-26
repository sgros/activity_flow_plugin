package com.google.android.exoplayer2.mediacodec;

import android.annotation.TargetApi;
import android.media.MediaCodec;
import android.media.MediaCodec.BufferInfo;
import android.media.MediaCodec.CodecException;
import android.media.MediaCodec.CryptoException;
import android.media.MediaCodec.CryptoInfo;
import android.media.MediaCrypto;
import android.media.MediaCryptoException;
import android.media.MediaFormat;
import android.os.Bundle;
import android.os.SystemClock;
import com.google.android.exoplayer2.BaseRenderer;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.FormatHolder;
import com.google.android.exoplayer2.decoder.DecoderCounters;
import com.google.android.exoplayer2.decoder.DecoderInputBuffer;
import com.google.android.exoplayer2.drm.DrmSession;
import com.google.android.exoplayer2.drm.DrmSessionManager;
import com.google.android.exoplayer2.drm.FrameworkMediaCrypto;
import com.google.android.exoplayer2.mediacodec.MediaCodecUtil.DecoderQueryException;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.util.Log;
import com.google.android.exoplayer2.util.NalUnitUtil;
import com.google.android.exoplayer2.util.TimedValueQueue;
import com.google.android.exoplayer2.util.TraceUtil;
import com.google.android.exoplayer2.util.Util;
import java.nio.ByteBuffer;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;

@TargetApi(16)
public abstract class MediaCodecRenderer extends BaseRenderer {
    private static final byte[] ADAPTATION_WORKAROUND_BUFFER = Util.getBytesFromHexString("0000016742C00BDA259000000168CE0F13200000016588840DCE7118A0002FBF1C31C3275D78");
    private final float assumedMinimumCodecOperatingRate;
    private ArrayDeque<MediaCodecInfo> availableCodecInfos;
    private final DecoderInputBuffer buffer;
    private MediaCodec codec;
    private int codecAdaptationWorkaroundMode;
    private int codecDrainAction;
    private int codecDrainState;
    private DrmSession<FrameworkMediaCrypto> codecDrmSession;
    private Format codecFormat;
    private long codecHotswapDeadlineMs;
    private MediaCodecInfo codecInfo;
    private boolean codecNeedsAdaptationWorkaroundBuffer;
    private boolean codecNeedsDiscardToSpsWorkaround;
    private boolean codecNeedsEosFlushWorkaround;
    private boolean codecNeedsEosOutputExceptionWorkaround;
    private boolean codecNeedsEosPropagation;
    private boolean codecNeedsFlushWorkaround;
    private boolean codecNeedsMonoChannelCountWorkaround;
    private boolean codecNeedsReconfigureWorkaround;
    private float codecOperatingRate;
    private boolean codecReceivedBuffers;
    private boolean codecReceivedEos;
    private int codecReconfigurationState;
    private boolean codecReconfigured;
    private final ArrayList<Long> decodeOnlyPresentationTimestamps;
    protected DecoderCounters decoderCounters;
    private final DrmSessionManager<FrameworkMediaCrypto> drmSessionManager;
    private final DecoderInputBuffer flagsOnlyBuffer;
    private final FormatHolder formatHolder;
    private final TimedValueQueue<Format> formatQueue;
    private ByteBuffer[] inputBuffers;
    private Format inputFormat;
    private int inputIndex;
    private boolean inputStreamEnded;
    private final MediaCodecSelector mediaCodecSelector;
    private MediaCrypto mediaCrypto;
    private boolean mediaCryptoRequiresSecureDecoder;
    private ByteBuffer outputBuffer;
    private final BufferInfo outputBufferInfo;
    private ByteBuffer[] outputBuffers;
    private Format outputFormat;
    private int outputIndex;
    private boolean outputStreamEnded;
    private final boolean playClearSamplesWithoutKeys;
    private DecoderInitializationException preferredDecoderInitializationException;
    private long renderTimeLimitMs;
    private float rendererOperatingRate;
    private boolean shouldSkipAdaptationWorkaroundOutputBuffer;
    private boolean shouldSkipOutputBuffer;
    private DrmSession<FrameworkMediaCrypto> sourceDrmSession;
    private boolean waitingForFirstSampleInFormat;
    private boolean waitingForFirstSyncSample;
    private boolean waitingForKeys;

    public static class DecoderInitializationException extends Exception {
        public final String decoderName;
        public final String diagnosticInfo;
        public final DecoderInitializationException fallbackDecoderInitializationException;
        public final String mimeType;
        public final boolean secureDecoderRequired;

        public DecoderInitializationException(Format format, Throwable th, boolean z, int i) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Decoder init failed: [");
            stringBuilder.append(i);
            stringBuilder.append("], ");
            stringBuilder.append(format);
            this(stringBuilder.toString(), th, format.sampleMimeType, z, null, buildCustomDiagnosticInfo(i), null);
        }

        public DecoderInitializationException(Format format, Throwable th, boolean z, String str) {
            String diagnosticInfoV21;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Decoder init failed: ");
            stringBuilder.append(str);
            stringBuilder.append(", ");
            stringBuilder.append(format);
            String stringBuilder2 = stringBuilder.toString();
            String str2 = format.sampleMimeType;
            if (Util.SDK_INT >= 21) {
                diagnosticInfoV21 = getDiagnosticInfoV21(th);
            } else {
                diagnosticInfoV21 = null;
            }
            this(stringBuilder2, th, str2, z, str, diagnosticInfoV21, null);
        }

        private DecoderInitializationException(String str, Throwable th, String str2, boolean z, String str3, String str4, DecoderInitializationException decoderInitializationException) {
            super(str, th);
            this.mimeType = str2;
            this.secureDecoderRequired = z;
            this.decoderName = str3;
            this.diagnosticInfo = str4;
            this.fallbackDecoderInitializationException = decoderInitializationException;
        }

        private DecoderInitializationException copyWithFallbackException(DecoderInitializationException decoderInitializationException) {
            return new DecoderInitializationException(getMessage(), getCause(), this.mimeType, this.secureDecoderRequired, this.decoderName, this.diagnosticInfo, decoderInitializationException);
        }

        @TargetApi(21)
        private static String getDiagnosticInfoV21(Throwable th) {
            return th instanceof CodecException ? ((CodecException) th).getDiagnosticInfo() : null;
        }

        private static String buildCustomDiagnosticInfo(int i) {
            String str = i < 0 ? "neg_" : "";
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("com.google.android.exoplayer.MediaCodecTrackRenderer_");
            stringBuilder.append(str);
            stringBuilder.append(Math.abs(i));
            return stringBuilder.toString();
        }
    }

    /*  JADX ERROR: JadxRuntimeException in pass: BlockProcessor
        jadx.core.utils.exceptions.JadxRuntimeException: Can't find immediate dominator for block B:32:0x008a in {4, 7, 14, 17, 21, 22, 25, 27, 29, 31} preds:[]
        	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.computeDominators(BlockProcessor.java:242)
        	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.processBlocksTree(BlockProcessor.java:52)
        	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.visit(BlockProcessor.java:42)
        	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:27)
        	at jadx.core.dex.visitors.DepthTraversal.lambda$visit$1(DepthTraversal.java:14)
        	at java.base/java.util.ArrayList.forEach(ArrayList.java:1378)
        	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
        	at jadx.core.ProcessClass.process(ProcessClass.java:32)
        	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:292)
        	at jadx.api.JavaClass.decompile(JavaClass.java:62)
        	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
        */
    private void maybeInitCodecWithFallback(android.media.MediaCrypto r6, boolean r7) throws com.google.android.exoplayer2.mediacodec.MediaCodecRenderer.DecoderInitializationException {
        /*
        r5 = this;
        r0 = r5.availableCodecInfos;
        r1 = 0;
        if (r0 != 0) goto L_0x001f;
        r0 = new java.util.ArrayDeque;	 Catch:{ DecoderQueryException -> 0x0013 }
        r2 = r5.getAvailableCodecInfos(r7);	 Catch:{ DecoderQueryException -> 0x0013 }
        r0.<init>(r2);	 Catch:{ DecoderQueryException -> 0x0013 }
        r5.availableCodecInfos = r0;	 Catch:{ DecoderQueryException -> 0x0013 }
        r5.preferredDecoderInitializationException = r1;	 Catch:{ DecoderQueryException -> 0x0013 }
        goto L_0x001f;
        r6 = move-exception;
        r0 = new com.google.android.exoplayer2.mediacodec.MediaCodecRenderer$DecoderInitializationException;
        r1 = r5.inputFormat;
        r2 = -49998; // 0xffffffffffff3cb2 float:NaN double:NaN;
        r0.<init>(r1, r6, r7, r2);
        throw r0;
        r0 = r5.availableCodecInfos;
        r0 = r0.isEmpty();
        if (r0 != 0) goto L_0x007f;
        r0 = r5.codec;
        if (r0 != 0) goto L_0x007c;
        r0 = r5.availableCodecInfos;
        r0 = r0.peekFirst();
        r0 = (com.google.android.exoplayer2.mediacodec.MediaCodecInfo) r0;
        r2 = r5.shouldInitCodec(r0);
        if (r2 != 0) goto L_0x003a;
        return;
        r5.initCodec(r0, r6);	 Catch:{ Exception -> 0x003e }
        goto L_0x0027;
        r2 = move-exception;
        r3 = new java.lang.StringBuilder;
        r3.<init>();
        r4 = "Failed to initialize decoder: ";
        r3.append(r4);
        r3.append(r0);
        r3 = r3.toString();
        r4 = "MediaCodecRenderer";
        com.google.android.exoplayer2.util.Log.m19w(r4, r3, r2);
        r3 = r5.availableCodecInfos;
        r3.removeFirst();
        r3 = new com.google.android.exoplayer2.mediacodec.MediaCodecRenderer$DecoderInitializationException;
        r4 = r5.inputFormat;
        r0 = r0.name;
        r3.<init>(r4, r2, r7, r0);
        r0 = r5.preferredDecoderInitializationException;
        if (r0 != 0) goto L_0x006a;
        r5.preferredDecoderInitializationException = r3;
        goto L_0x0070;
        r0 = r0.copyWithFallbackException(r3);
        r5.preferredDecoderInitializationException = r0;
        r0 = r5.availableCodecInfos;
        r0 = r0.isEmpty();
        if (r0 != 0) goto L_0x0079;
        goto L_0x0027;
        r6 = r5.preferredDecoderInitializationException;
        throw r6;
        r5.availableCodecInfos = r1;
        return;
        r6 = new com.google.android.exoplayer2.mediacodec.MediaCodecRenderer$DecoderInitializationException;
        r0 = r5.inputFormat;
        r2 = -49999; // 0xffffffffffff3cb1 float:NaN double:NaN;
        r6.<init>(r0, r1, r7, r2);
        throw r6;
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.exoplayer2.mediacodec.MediaCodecRenderer.maybeInitCodecWithFallback(android.media.MediaCrypto, boolean):void");
    }

    public abstract int canKeepCodec(MediaCodec mediaCodec, MediaCodecInfo mediaCodecInfo, Format format, Format format2);

    public abstract void configureCodec(MediaCodecInfo mediaCodecInfo, MediaCodec mediaCodec, Format format, MediaCrypto mediaCrypto, float f) throws DecoderQueryException;

    /* Access modifiers changed, original: protected */
    public boolean getCodecNeedsEosPropagation() {
        return false;
    }

    public abstract float getCodecOperatingRateV23(float f, Format format, Format[] formatArr);

    /* Access modifiers changed, original: protected */
    public long getDequeueOutputBufferTimeoutUs() {
        return 0;
    }

    public abstract void onCodecInitialized(String str, long j, long j2);

    public abstract void onOutputFormatChanged(MediaCodec mediaCodec, MediaFormat mediaFormat) throws ExoPlaybackException;

    public abstract void onProcessedOutputBuffer(long j);

    public abstract void onQueueInputBuffer(DecoderInputBuffer decoderInputBuffer);

    /* Access modifiers changed, original: protected */
    public void onStarted() {
    }

    /* Access modifiers changed, original: protected */
    public void onStopped() {
    }

    public abstract boolean processOutputBuffer(long j, long j2, MediaCodec mediaCodec, ByteBuffer byteBuffer, int i, int i2, long j3, boolean z, Format format) throws ExoPlaybackException;

    /* Access modifiers changed, original: protected */
    public void renderToEndOfStream() throws ExoPlaybackException {
    }

    /* Access modifiers changed, original: protected */
    public boolean shouldInitCodec(MediaCodecInfo mediaCodecInfo) {
        return true;
    }

    public abstract int supportsFormat(MediaCodecSelector mediaCodecSelector, DrmSessionManager<FrameworkMediaCrypto> drmSessionManager, Format format) throws DecoderQueryException;

    public final int supportsMixedMimeTypeAdaptation() {
        return 8;
    }

    public MediaCodecRenderer(int i, MediaCodecSelector mediaCodecSelector, DrmSessionManager<FrameworkMediaCrypto> drmSessionManager, boolean z, float f) {
        super(i);
        Assertions.checkState(Util.SDK_INT >= 16);
        Assertions.checkNotNull(mediaCodecSelector);
        this.mediaCodecSelector = mediaCodecSelector;
        this.drmSessionManager = drmSessionManager;
        this.playClearSamplesWithoutKeys = z;
        this.assumedMinimumCodecOperatingRate = f;
        this.buffer = new DecoderInputBuffer(0);
        this.flagsOnlyBuffer = DecoderInputBuffer.newFlagsOnlyInstance();
        this.formatHolder = new FormatHolder();
        this.formatQueue = new TimedValueQueue();
        this.decodeOnlyPresentationTimestamps = new ArrayList();
        this.outputBufferInfo = new BufferInfo();
        this.codecReconfigurationState = 0;
        this.codecDrainState = 0;
        this.codecDrainAction = 0;
        this.codecOperatingRate = -1.0f;
        this.rendererOperatingRate = 1.0f;
        this.renderTimeLimitMs = -9223372036854775807L;
    }

    public final int supportsFormat(Format format) throws ExoPlaybackException {
        try {
            return supportsFormat(this.mediaCodecSelector, this.drmSessionManager, format);
        } catch (DecoderQueryException e) {
            throw ExoPlaybackException.createForRenderer(e, getIndex());
        }
    }

    /* Access modifiers changed, original: protected */
    public List<MediaCodecInfo> getDecoderInfos(MediaCodecSelector mediaCodecSelector, Format format, boolean z) throws DecoderQueryException {
        return mediaCodecSelector.getDecoderInfos(format.sampleMimeType, z);
    }

    /* Access modifiers changed, original: protected|final */
    public final void maybeInitCodec() throws ExoPlaybackException {
        if (this.codec == null && this.inputFormat != null) {
            setCodecDrmSession(this.sourceDrmSession);
            String str = this.inputFormat.sampleMimeType;
            DrmSession drmSession = this.codecDrmSession;
            if (drmSession != null) {
                if (this.mediaCrypto == null) {
                    FrameworkMediaCrypto frameworkMediaCrypto = (FrameworkMediaCrypto) drmSession.getMediaCrypto();
                    if (frameworkMediaCrypto != null) {
                        try {
                            this.mediaCrypto = new MediaCrypto(frameworkMediaCrypto.uuid, frameworkMediaCrypto.sessionId);
                            boolean z = !frameworkMediaCrypto.forceAllowInsecureDecoderComponents && this.mediaCrypto.requiresSecureDecoderComponent(str);
                            this.mediaCryptoRequiresSecureDecoder = z;
                        } catch (MediaCryptoException e) {
                            throw ExoPlaybackException.createForRenderer(e, getIndex());
                        }
                    } else if (this.codecDrmSession.getError() == null) {
                        return;
                    }
                }
                if (deviceNeedsDrmKeysToConfigureCodecWorkaround()) {
                    int state = this.codecDrmSession.getState();
                    if (state == 1) {
                        throw ExoPlaybackException.createForRenderer(this.codecDrmSession.getError(), getIndex());
                    } else if (state != 4) {
                        return;
                    }
                }
            }
            try {
                maybeInitCodecWithFallback(this.mediaCrypto, this.mediaCryptoRequiresSecureDecoder);
            } catch (DecoderInitializationException e2) {
                throw ExoPlaybackException.createForRenderer(e2, getIndex());
            }
        }
    }

    /* Access modifiers changed, original: protected|final */
    public final Format updateOutputFormatForTime(long j) {
        Format format = (Format) this.formatQueue.pollFloor(j);
        if (format != null) {
            this.outputFormat = format;
        }
        return format;
    }

    /* Access modifiers changed, original: protected|final */
    public final MediaCodec getCodec() {
        return this.codec;
    }

    /* Access modifiers changed, original: protected|final */
    public final MediaCodecInfo getCodecInfo() {
        return this.codecInfo;
    }

    /* Access modifiers changed, original: protected */
    public void onEnabled(boolean z) throws ExoPlaybackException {
        this.decoderCounters = new DecoderCounters();
    }

    /* Access modifiers changed, original: protected */
    public void onPositionReset(long j, boolean z) throws ExoPlaybackException {
        this.inputStreamEnded = false;
        this.outputStreamEnded = false;
        flushOrReinitCodec();
        this.formatQueue.clear();
    }

    public final void setOperatingRate(float f) throws ExoPlaybackException {
        this.rendererOperatingRate = f;
        if (this.codec != null && this.codecDrainAction != 2) {
            updateCodecOperatingRate();
        }
    }

    /* Access modifiers changed, original: protected */
    public void onDisabled() {
        this.inputFormat = null;
        if (this.sourceDrmSession == null && this.codecDrmSession == null) {
            flushOrReleaseCodec();
        } else {
            onReset();
        }
    }

    /* Access modifiers changed, original: protected */
    public void onReset() {
        try {
            releaseCodec();
        } finally {
            setSourceDrmSession(null);
        }
    }

    /* Access modifiers changed, original: protected */
    public void releaseCodec() {
        this.availableCodecInfos = null;
        this.codecInfo = null;
        this.codecFormat = null;
        resetInputBuffer();
        resetOutputBuffer();
        resetCodecBuffers();
        this.waitingForKeys = false;
        this.codecHotswapDeadlineMs = -9223372036854775807L;
        this.decodeOnlyPresentationTimestamps.clear();
        try {
            if (this.codec != null) {
                DecoderCounters decoderCounters = this.decoderCounters;
                decoderCounters.decoderReleaseCount++;
                this.codec.stop();
                this.codec.release();
            }
            this.codec = null;
            try {
                if (this.mediaCrypto != null) {
                    this.mediaCrypto.release();
                }
                this.mediaCrypto = null;
                this.mediaCryptoRequiresSecureDecoder = false;
                setCodecDrmSession(null);
            } catch (Throwable th) {
                this.mediaCrypto = null;
                this.mediaCryptoRequiresSecureDecoder = false;
                setCodecDrmSession(null);
            }
        } catch (Throwable th2) {
            this.codec = null;
            try {
                if (this.mediaCrypto != null) {
                    this.mediaCrypto.release();
                }
                this.mediaCrypto = null;
                this.mediaCryptoRequiresSecureDecoder = false;
                setCodecDrmSession(null);
            } catch (Throwable th3) {
                this.mediaCrypto = null;
                this.mediaCryptoRequiresSecureDecoder = false;
                setCodecDrmSession(null);
            }
        }
    }

    public void render(long j, long j2) throws ExoPlaybackException {
        if (this.outputStreamEnded) {
            renderToEndOfStream();
            return;
        }
        if (this.inputFormat == null) {
            this.flagsOnlyBuffer.clear();
            int readSource = readSource(this.formatHolder, this.flagsOnlyBuffer, true);
            if (readSource == -5) {
                onInputFormatChanged(this.formatHolder.format);
            } else {
                if (readSource == -4) {
                    Assertions.checkState(this.flagsOnlyBuffer.isEndOfStream());
                    this.inputStreamEnded = true;
                    processEndOfStream();
                }
                return;
            }
        }
        maybeInitCodec();
        if (this.codec != null) {
            long elapsedRealtime = SystemClock.elapsedRealtime();
            TraceUtil.beginSection("drainAndFeed");
            while (drainOutputBuffer(j, j2)) {
            }
            while (feedInputBuffer() && shouldContinueFeeding(elapsedRealtime)) {
            }
            TraceUtil.endSection();
        } else {
            DecoderCounters decoderCounters = this.decoderCounters;
            decoderCounters.skippedInputBufferCount += skipSource(j);
            this.flagsOnlyBuffer.clear();
            int readSource2 = readSource(this.formatHolder, this.flagsOnlyBuffer, false);
            if (readSource2 == -5) {
                onInputFormatChanged(this.formatHolder.format);
            } else if (readSource2 == -4) {
                Assertions.checkState(this.flagsOnlyBuffer.isEndOfStream());
                this.inputStreamEnded = true;
                processEndOfStream();
            }
        }
        this.decoderCounters.ensureUpdated();
    }

    /* Access modifiers changed, original: protected|final */
    public final void flushOrReinitCodec() throws ExoPlaybackException {
        if (flushOrReleaseCodec()) {
            maybeInitCodec();
        }
    }

    /* Access modifiers changed, original: protected */
    public boolean flushOrReleaseCodec() {
        if (this.codec == null) {
            return false;
        }
        if (this.codecDrainAction == 2 || this.codecNeedsFlushWorkaround || (this.codecNeedsEosFlushWorkaround && this.codecReceivedEos)) {
            releaseCodec();
            return true;
        }
        this.codec.flush();
        resetInputBuffer();
        resetOutputBuffer();
        this.codecHotswapDeadlineMs = -9223372036854775807L;
        this.codecReceivedEos = false;
        this.codecReceivedBuffers = false;
        this.waitingForFirstSyncSample = true;
        this.codecNeedsAdaptationWorkaroundBuffer = false;
        this.shouldSkipAdaptationWorkaroundOutputBuffer = false;
        this.shouldSkipOutputBuffer = false;
        this.waitingForKeys = false;
        this.decodeOnlyPresentationTimestamps.clear();
        this.codecDrainState = 0;
        this.codecDrainAction = 0;
        this.codecReconfigurationState = this.codecReconfigured;
        return false;
    }

    private List<MediaCodecInfo> getAvailableCodecInfos(boolean z) throws DecoderQueryException {
        List<MediaCodecInfo> decoderInfos = getDecoderInfos(this.mediaCodecSelector, this.inputFormat, z);
        if (decoderInfos.isEmpty() && z) {
            decoderInfos = getDecoderInfos(this.mediaCodecSelector, this.inputFormat, false);
            if (!decoderInfos.isEmpty()) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Drm session requires secure decoder for ");
                stringBuilder.append(this.inputFormat.sampleMimeType);
                stringBuilder.append(", but no secure decoder available. Trying to proceed with ");
                stringBuilder.append(decoderInfos);
                stringBuilder.append(".");
                Log.m18w("MediaCodecRenderer", stringBuilder.toString());
            }
        }
        return decoderInfos;
    }

    private void initCodec(MediaCodecInfo mediaCodecInfo, MediaCrypto mediaCrypto) throws Exception {
        float f;
        String str = mediaCodecInfo.name;
        if (Util.SDK_INT < 23) {
            f = -1.0f;
        } else {
            f = getCodecOperatingRateV23(this.rendererOperatingRate, this.inputFormat, getStreamFormats());
        }
        if (f <= this.assumedMinimumCodecOperatingRate) {
            f = -1.0f;
        }
        MediaCodec mediaCodec = null;
        try {
            long elapsedRealtime = SystemClock.elapsedRealtime();
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("createCodec:");
            stringBuilder.append(str);
            TraceUtil.beginSection(stringBuilder.toString());
            mediaCodec = MediaCodec.createByCodecName(str);
            TraceUtil.endSection();
            TraceUtil.beginSection("configureCodec");
            configureCodec(mediaCodecInfo, mediaCodec, this.inputFormat, mediaCrypto, f);
            TraceUtil.endSection();
            TraceUtil.beginSection("startCodec");
            mediaCodec.start();
            TraceUtil.endSection();
            long elapsedRealtime2 = SystemClock.elapsedRealtime();
            getCodecBuffers(mediaCodec);
            this.codec = mediaCodec;
            this.codecInfo = mediaCodecInfo;
            this.codecOperatingRate = f;
            this.codecFormat = this.inputFormat;
            this.codecAdaptationWorkaroundMode = codecAdaptationWorkaroundMode(str);
            this.codecNeedsReconfigureWorkaround = codecNeedsReconfigureWorkaround(str);
            this.codecNeedsDiscardToSpsWorkaround = codecNeedsDiscardToSpsWorkaround(str, this.codecFormat);
            this.codecNeedsFlushWorkaround = codecNeedsFlushWorkaround(str);
            this.codecNeedsEosFlushWorkaround = codecNeedsEosFlushWorkaround(str);
            this.codecNeedsEosOutputExceptionWorkaround = codecNeedsEosOutputExceptionWorkaround(str);
            this.codecNeedsMonoChannelCountWorkaround = codecNeedsMonoChannelCountWorkaround(str, this.codecFormat);
            boolean z = codecNeedsEosPropagationWorkaround(mediaCodecInfo) || getCodecNeedsEosPropagation();
            this.codecNeedsEosPropagation = z;
            resetInputBuffer();
            resetOutputBuffer();
            this.codecHotswapDeadlineMs = getState() == 2 ? SystemClock.elapsedRealtime() + 1000 : -9223372036854775807L;
            this.codecReconfigured = false;
            this.codecReconfigurationState = 0;
            this.codecReceivedEos = false;
            this.codecReceivedBuffers = false;
            this.codecDrainState = 0;
            this.codecDrainAction = 0;
            this.codecNeedsAdaptationWorkaroundBuffer = false;
            this.shouldSkipAdaptationWorkaroundOutputBuffer = false;
            this.shouldSkipOutputBuffer = false;
            this.waitingForFirstSyncSample = true;
            DecoderCounters decoderCounters = this.decoderCounters;
            decoderCounters.decoderInitCount++;
            onCodecInitialized(str, elapsedRealtime2, elapsedRealtime2 - elapsedRealtime);
        } catch (Exception e) {
            if (mediaCodec != null) {
                resetCodecBuffers();
                mediaCodec.release();
            }
            throw e;
        }
    }

    private boolean shouldContinueFeeding(long j) {
        return this.renderTimeLimitMs == -9223372036854775807L || SystemClock.elapsedRealtime() - j < this.renderTimeLimitMs;
    }

    private void getCodecBuffers(MediaCodec mediaCodec) {
        if (Util.SDK_INT < 21) {
            this.inputBuffers = mediaCodec.getInputBuffers();
            this.outputBuffers = mediaCodec.getOutputBuffers();
        }
    }

    private void resetCodecBuffers() {
        if (Util.SDK_INT < 21) {
            this.inputBuffers = null;
            this.outputBuffers = null;
        }
    }

    private ByteBuffer getInputBuffer(int i) {
        if (Util.SDK_INT >= 21) {
            return this.codec.getInputBuffer(i);
        }
        return this.inputBuffers[i];
    }

    private ByteBuffer getOutputBuffer(int i) {
        if (Util.SDK_INT >= 21) {
            return this.codec.getOutputBuffer(i);
        }
        return this.outputBuffers[i];
    }

    private boolean hasOutputBuffer() {
        return this.outputIndex >= 0;
    }

    private void resetInputBuffer() {
        this.inputIndex = -1;
        this.buffer.data = null;
    }

    private void resetOutputBuffer() {
        this.outputIndex = -1;
        this.outputBuffer = null;
    }

    private void setSourceDrmSession(DrmSession<FrameworkMediaCrypto> drmSession) {
        DrmSession drmSession2 = this.sourceDrmSession;
        this.sourceDrmSession = drmSession;
        releaseDrmSessionIfUnused(drmSession2);
    }

    private void setCodecDrmSession(DrmSession<FrameworkMediaCrypto> drmSession) {
        DrmSession drmSession2 = this.codecDrmSession;
        this.codecDrmSession = drmSession;
        releaseDrmSessionIfUnused(drmSession2);
    }

    private void releaseDrmSessionIfUnused(DrmSession<FrameworkMediaCrypto> drmSession) {
        if (drmSession != null && drmSession != this.sourceDrmSession && drmSession != this.codecDrmSession) {
            this.drmSessionManager.releaseSession(drmSession);
        }
    }

    private boolean feedInputBuffer() throws ExoPlaybackException {
        MediaCodec mediaCodec = this.codec;
        if (mediaCodec == null || this.codecDrainState == 2 || this.inputStreamEnded) {
            return false;
        }
        int i;
        if (this.inputIndex < 0) {
            this.inputIndex = mediaCodec.dequeueInputBuffer(0);
            i = this.inputIndex;
            if (i < 0) {
                return false;
            }
            this.buffer.data = getInputBuffer(i);
            this.buffer.clear();
        }
        if (this.codecDrainState == 1) {
            if (!this.codecNeedsEosPropagation) {
                this.codecReceivedEos = true;
                this.codec.queueInputBuffer(this.inputIndex, 0, 0, 0, 4);
                resetInputBuffer();
            }
            this.codecDrainState = 2;
            return false;
        } else if (this.codecNeedsAdaptationWorkaroundBuffer) {
            this.codecNeedsAdaptationWorkaroundBuffer = false;
            this.buffer.data.put(ADAPTATION_WORKAROUND_BUFFER);
            this.codec.queueInputBuffer(this.inputIndex, 0, ADAPTATION_WORKAROUND_BUFFER.length, 0, 0);
            resetInputBuffer();
            this.codecReceivedBuffers = true;
            return true;
        } else {
            int i2;
            if (this.waitingForKeys) {
                i = -4;
                i2 = 0;
            } else {
                if (this.codecReconfigurationState == 1) {
                    for (i = 0; i < this.codecFormat.initializationData.size(); i++) {
                        this.buffer.data.put((byte[]) this.codecFormat.initializationData.get(i));
                    }
                    this.codecReconfigurationState = 2;
                }
                i = this.buffer.data.position();
                i2 = i;
                i = readSource(this.formatHolder, this.buffer, false);
            }
            if (i == -3) {
                return false;
            }
            if (i == -5) {
                if (this.codecReconfigurationState == 2) {
                    this.buffer.clear();
                    this.codecReconfigurationState = 1;
                }
                onInputFormatChanged(this.formatHolder.format);
                return true;
            } else if (this.buffer.isEndOfStream()) {
                if (this.codecReconfigurationState == 2) {
                    this.buffer.clear();
                    this.codecReconfigurationState = 1;
                }
                this.inputStreamEnded = true;
                if (this.codecReceivedBuffers) {
                    try {
                        if (!this.codecNeedsEosPropagation) {
                            this.codecReceivedEos = true;
                            this.codec.queueInputBuffer(this.inputIndex, 0, 0, 0, 4);
                            resetInputBuffer();
                        }
                        return false;
                    } catch (CryptoException e) {
                        throw ExoPlaybackException.createForRenderer(e, getIndex());
                    }
                }
                processEndOfStream();
                return false;
            } else if (!this.waitingForFirstSyncSample || this.buffer.isKeyFrame()) {
                this.waitingForFirstSyncSample = false;
                boolean isEncrypted = this.buffer.isEncrypted();
                this.waitingForKeys = shouldWaitForKeys(isEncrypted);
                if (this.waitingForKeys) {
                    return false;
                }
                if (this.codecNeedsDiscardToSpsWorkaround && !isEncrypted) {
                    NalUnitUtil.discardToSps(this.buffer.data);
                    if (this.buffer.data.position() == 0) {
                        return true;
                    }
                    this.codecNeedsDiscardToSpsWorkaround = false;
                }
                try {
                    long j = this.buffer.timeUs;
                    if (this.buffer.isDecodeOnly()) {
                        this.decodeOnlyPresentationTimestamps.add(Long.valueOf(j));
                    }
                    if (this.waitingForFirstSampleInFormat) {
                        this.formatQueue.add(j, this.inputFormat);
                        this.waitingForFirstSampleInFormat = false;
                    }
                    this.buffer.flip();
                    onQueueInputBuffer(this.buffer);
                    if (isEncrypted) {
                        this.codec.queueSecureInputBuffer(this.inputIndex, 0, getFrameworkCryptoInfo(this.buffer, i2), j, 0);
                    } else {
                        this.codec.queueInputBuffer(this.inputIndex, 0, this.buffer.data.limit(), j, 0);
                    }
                    resetInputBuffer();
                    this.codecReceivedBuffers = true;
                    this.codecReconfigurationState = 0;
                    DecoderCounters decoderCounters = this.decoderCounters;
                    decoderCounters.inputBufferCount++;
                    return true;
                } catch (CryptoException e2) {
                    throw ExoPlaybackException.createForRenderer(e2, getIndex());
                }
            } else {
                this.buffer.clear();
                if (this.codecReconfigurationState == 2) {
                    this.codecReconfigurationState = 1;
                }
                return true;
            }
        }
    }

    private boolean shouldWaitForKeys(boolean z) throws ExoPlaybackException {
        if (this.codecDrmSession == null || (!z && this.playClearSamplesWithoutKeys)) {
            return false;
        }
        int state = this.codecDrmSession.getState();
        boolean z2 = true;
        if (state != 1) {
            if (state == 4) {
                z2 = false;
            }
            return z2;
        }
        throw ExoPlaybackException.createForRenderer(this.codecDrmSession.getError(), getIndex());
    }

    /* Access modifiers changed, original: protected */
    /* JADX WARNING: Missing block: B:45:0x009b, code skipped:
            if (r5.height == r2.height) goto L_0x009f;
     */
    public void onInputFormatChanged(com.google.android.exoplayer2.Format r5) throws com.google.android.exoplayer2.ExoPlaybackException {
        /*
        r4 = this;
        r0 = r4.inputFormat;
        r4.inputFormat = r5;
        r1 = 1;
        r4.waitingForFirstSampleInFormat = r1;
        r2 = r5.drmInitData;
        r3 = 0;
        if (r0 != 0) goto L_0x000e;
    L_0x000c:
        r0 = r3;
        goto L_0x0010;
    L_0x000e:
        r0 = r0.drmInitData;
    L_0x0010:
        r0 = com.google.android.exoplayer2.util.Util.areEqual(r2, r0);
        r0 = r0 ^ r1;
        if (r0 == 0) goto L_0x004d;
    L_0x0017:
        r0 = r5.drmInitData;
        if (r0 == 0) goto L_0x004a;
    L_0x001b:
        r0 = r4.drmSessionManager;
        if (r0 == 0) goto L_0x003a;
    L_0x001f:
        r2 = android.os.Looper.myLooper();
        r3 = r5.drmInitData;
        r0 = r0.acquireSession(r2, r3);
        r2 = r4.sourceDrmSession;
        if (r0 == r2) goto L_0x0031;
    L_0x002d:
        r2 = r4.codecDrmSession;
        if (r0 != r2) goto L_0x0036;
    L_0x0031:
        r2 = r4.drmSessionManager;
        r2.releaseSession(r0);
    L_0x0036:
        r4.setSourceDrmSession(r0);
        goto L_0x004d;
    L_0x003a:
        r5 = new java.lang.IllegalStateException;
        r0 = "Media requires a DrmSessionManager";
        r5.<init>(r0);
        r0 = r4.getIndex();
        r5 = com.google.android.exoplayer2.ExoPlaybackException.createForRenderer(r5, r0);
        throw r5;
    L_0x004a:
        r4.setSourceDrmSession(r3);
    L_0x004d:
        r0 = r4.codec;
        if (r0 != 0) goto L_0x0055;
    L_0x0051:
        r4.maybeInitCodec();
        return;
    L_0x0055:
        r2 = r4.sourceDrmSession;
        r3 = r4.codecDrmSession;
        if (r2 == r3) goto L_0x005f;
    L_0x005b:
        r4.drainAndReinitializeCodec();
        goto L_0x00b3;
    L_0x005f:
        r2 = r4.codecInfo;
        r3 = r4.codecFormat;
        r0 = r4.canKeepCodec(r0, r2, r3, r5);
        if (r0 == 0) goto L_0x00b0;
    L_0x0069:
        if (r0 == r1) goto L_0x00a7;
    L_0x006b:
        r2 = 2;
        if (r0 == r2) goto L_0x007d;
    L_0x006e:
        r1 = 3;
        if (r0 != r1) goto L_0x0077;
    L_0x0071:
        r4.codecFormat = r5;
        r4.updateCodecOperatingRate();
        goto L_0x00b3;
    L_0x0077:
        r5 = new java.lang.IllegalStateException;
        r5.<init>();
        throw r5;
    L_0x007d:
        r0 = r4.codecNeedsReconfigureWorkaround;
        if (r0 == 0) goto L_0x0085;
    L_0x0081:
        r4.drainAndReinitializeCodec();
        goto L_0x00b3;
    L_0x0085:
        r4.codecReconfigured = r1;
        r4.codecReconfigurationState = r1;
        r0 = r4.codecAdaptationWorkaroundMode;
        if (r0 == r2) goto L_0x009f;
    L_0x008d:
        if (r0 != r1) goto L_0x009e;
    L_0x008f:
        r0 = r5.width;
        r2 = r4.codecFormat;
        r3 = r2.width;
        if (r0 != r3) goto L_0x009e;
    L_0x0097:
        r0 = r5.height;
        r2 = r2.height;
        if (r0 != r2) goto L_0x009e;
    L_0x009d:
        goto L_0x009f;
    L_0x009e:
        r1 = 0;
    L_0x009f:
        r4.codecNeedsAdaptationWorkaroundBuffer = r1;
        r4.codecFormat = r5;
        r4.updateCodecOperatingRate();
        goto L_0x00b3;
    L_0x00a7:
        r4.drainAndFlushCodec();
        r4.codecFormat = r5;
        r4.updateCodecOperatingRate();
        goto L_0x00b3;
    L_0x00b0:
        r4.drainAndReinitializeCodec();
    L_0x00b3:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.exoplayer2.mediacodec.MediaCodecRenderer.onInputFormatChanged(com.google.android.exoplayer2.Format):void");
    }

    public boolean isEnded() {
        return this.outputStreamEnded;
    }

    public boolean isReady() {
        return (this.inputFormat == null || this.waitingForKeys || (!isSourceReady() && !hasOutputBuffer() && (this.codecHotswapDeadlineMs == -9223372036854775807L || SystemClock.elapsedRealtime() >= this.codecHotswapDeadlineMs))) ? false : true;
    }

    private void updateCodecOperatingRate() throws ExoPlaybackException {
        if (Util.SDK_INT >= 23) {
            float codecOperatingRateV23 = getCodecOperatingRateV23(this.rendererOperatingRate, this.codecFormat, getStreamFormats());
            float f = this.codecOperatingRate;
            if (f != codecOperatingRateV23) {
                if (codecOperatingRateV23 == -1.0f) {
                    drainAndReinitializeCodec();
                } else if (f != -1.0f || codecOperatingRateV23 > this.assumedMinimumCodecOperatingRate) {
                    Bundle bundle = new Bundle();
                    bundle.putFloat("operating-rate", codecOperatingRateV23);
                    this.codec.setParameters(bundle);
                    this.codecOperatingRate = codecOperatingRateV23;
                }
            }
        }
    }

    private void drainAndFlushCodec() {
        if (this.codecReceivedBuffers) {
            this.codecDrainState = 1;
            this.codecDrainAction = 1;
        }
    }

    private void drainAndReinitializeCodec() throws ExoPlaybackException {
        if (this.codecReceivedBuffers) {
            this.codecDrainState = 1;
            this.codecDrainAction = 2;
            return;
        }
        releaseCodec();
        maybeInitCodec();
    }

    private boolean drainOutputBuffer(long j, long j2) throws ExoPlaybackException {
        boolean processOutputBuffer;
        if (!hasOutputBuffer()) {
            int dequeueOutputBuffer;
            if (this.codecNeedsEosOutputExceptionWorkaround && this.codecReceivedEos) {
                try {
                    dequeueOutputBuffer = this.codec.dequeueOutputBuffer(this.outputBufferInfo, getDequeueOutputBufferTimeoutUs());
                } catch (IllegalStateException unused) {
                    processEndOfStream();
                    if (this.outputStreamEnded) {
                        releaseCodec();
                    }
                    return false;
                }
            }
            dequeueOutputBuffer = this.codec.dequeueOutputBuffer(this.outputBufferInfo, getDequeueOutputBufferTimeoutUs());
            if (dequeueOutputBuffer < 0) {
                if (dequeueOutputBuffer == -2) {
                    processOutputFormat();
                    return true;
                } else if (dequeueOutputBuffer == -3) {
                    processOutputBuffersChanged();
                    return true;
                } else {
                    if (this.codecNeedsEosPropagation && (this.inputStreamEnded || this.codecDrainState == 2)) {
                        processEndOfStream();
                    }
                    return false;
                }
            } else if (this.shouldSkipAdaptationWorkaroundOutputBuffer) {
                this.shouldSkipAdaptationWorkaroundOutputBuffer = false;
                this.codec.releaseOutputBuffer(dequeueOutputBuffer, false);
                return true;
            } else {
                BufferInfo bufferInfo = this.outputBufferInfo;
                if (bufferInfo.size != 0 || (bufferInfo.flags & 4) == 0) {
                    this.outputIndex = dequeueOutputBuffer;
                    this.outputBuffer = getOutputBuffer(dequeueOutputBuffer);
                    ByteBuffer byteBuffer = this.outputBuffer;
                    if (byteBuffer != null) {
                        byteBuffer.position(this.outputBufferInfo.offset);
                        byteBuffer = this.outputBuffer;
                        bufferInfo = this.outputBufferInfo;
                        byteBuffer.limit(bufferInfo.offset + bufferInfo.size);
                    }
                    this.shouldSkipOutputBuffer = shouldSkipOutputBuffer(this.outputBufferInfo.presentationTimeUs);
                    updateOutputFormatForTime(this.outputBufferInfo.presentationTimeUs);
                } else {
                    processEndOfStream();
                    return false;
                }
            }
        }
        if (this.codecNeedsEosOutputExceptionWorkaround && this.codecReceivedEos) {
            try {
                processOutputBuffer = processOutputBuffer(j, j2, this.codec, this.outputBuffer, this.outputIndex, this.outputBufferInfo.flags, this.outputBufferInfo.presentationTimeUs, this.shouldSkipOutputBuffer, this.outputFormat);
            } catch (IllegalStateException unused2) {
                processEndOfStream();
                if (this.outputStreamEnded) {
                    releaseCodec();
                }
                return false;
            }
        }
        MediaCodec mediaCodec = this.codec;
        ByteBuffer byteBuffer2 = this.outputBuffer;
        int i = this.outputIndex;
        BufferInfo bufferInfo2 = this.outputBufferInfo;
        processOutputBuffer = processOutputBuffer(j, j2, mediaCodec, byteBuffer2, i, bufferInfo2.flags, bufferInfo2.presentationTimeUs, this.shouldSkipOutputBuffer, this.outputFormat);
        if (processOutputBuffer) {
            onProcessedOutputBuffer(this.outputBufferInfo.presentationTimeUs);
            Object obj = (this.outputBufferInfo.flags & 4) != 0 ? 1 : null;
            resetOutputBuffer();
            if (obj == null) {
                return true;
            }
            processEndOfStream();
        }
        return false;
    }

    private void processOutputFormat() throws ExoPlaybackException {
        MediaFormat outputFormat = this.codec.getOutputFormat();
        if (this.codecAdaptationWorkaroundMode != 0 && outputFormat.getInteger("width") == 32 && outputFormat.getInteger("height") == 32) {
            this.shouldSkipAdaptationWorkaroundOutputBuffer = true;
            return;
        }
        if (this.codecNeedsMonoChannelCountWorkaround) {
            outputFormat.setInteger("channel-count", 1);
        }
        onOutputFormatChanged(this.codec, outputFormat);
    }

    private void processOutputBuffersChanged() {
        if (Util.SDK_INT < 21) {
            this.outputBuffers = this.codec.getOutputBuffers();
        }
    }

    private void processEndOfStream() throws ExoPlaybackException {
        int i = this.codecDrainAction;
        if (i == 1) {
            flushOrReinitCodec();
        } else if (i != 2) {
            this.outputStreamEnded = true;
            renderToEndOfStream();
        } else {
            releaseCodec();
            maybeInitCodec();
        }
    }

    private boolean shouldSkipOutputBuffer(long j) {
        int size = this.decodeOnlyPresentationTimestamps.size();
        for (int i = 0; i < size; i++) {
            if (((Long) this.decodeOnlyPresentationTimestamps.get(i)).longValue() == j) {
                this.decodeOnlyPresentationTimestamps.remove(i);
                return true;
            }
        }
        return false;
    }

    private static CryptoInfo getFrameworkCryptoInfo(DecoderInputBuffer decoderInputBuffer, int i) {
        CryptoInfo frameworkCryptoInfoV16 = decoderInputBuffer.cryptoInfo.getFrameworkCryptoInfoV16();
        if (i == 0) {
            return frameworkCryptoInfoV16;
        }
        if (frameworkCryptoInfoV16.numBytesOfClearData == null) {
            frameworkCryptoInfoV16.numBytesOfClearData = new int[1];
        }
        int[] iArr = frameworkCryptoInfoV16.numBytesOfClearData;
        iArr[0] = iArr[0] + i;
        return frameworkCryptoInfoV16;
    }

    /* JADX WARNING: Missing block: B:5:0x001c, code skipped:
            if ("AFTB".equals(com.google.android.exoplayer2.util.Util.MODEL) != false) goto L_0x001e;
     */
    private boolean deviceNeedsDrmKeysToConfigureCodecWorkaround() {
        /*
        r2 = this;
        r0 = com.google.android.exoplayer2.util.Util.MANUFACTURER;
        r1 = "Amazon";
        r0 = r1.equals(r0);
        if (r0 == 0) goto L_0x0020;
    L_0x000a:
        r0 = com.google.android.exoplayer2.util.Util.MODEL;
        r1 = "AFTM";
        r0 = r1.equals(r0);
        if (r0 != 0) goto L_0x001e;
    L_0x0014:
        r0 = com.google.android.exoplayer2.util.Util.MODEL;
        r1 = "AFTB";
        r0 = r1.equals(r0);
        if (r0 == 0) goto L_0x0020;
    L_0x001e:
        r0 = 1;
        goto L_0x0021;
    L_0x0020:
        r0 = 0;
    L_0x0021:
        return r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.exoplayer2.mediacodec.MediaCodecRenderer.deviceNeedsDrmKeysToConfigureCodecWorkaround():boolean");
    }

    private static boolean codecNeedsFlushWorkaround(String str) {
        int i = Util.SDK_INT;
        return i < 18 || ((i == 18 && ("OMX.SEC.avc.dec".equals(str) || "OMX.SEC.avc.dec.secure".equals(str))) || (Util.SDK_INT == 19 && Util.MODEL.startsWith("SM-G800") && ("OMX.Exynos.avc.dec".equals(str) || "OMX.Exynos.avc.dec.secure".equals(str))));
    }

    /* JADX WARNING: Missing block: B:27:0x0074, code skipped:
            if ("tilapia".equals(com.google.android.exoplayer2.util.Util.DEVICE) != false) goto L_0x0076;
     */
    private int codecAdaptationWorkaroundMode(java.lang.String r3) {
        /*
        r2 = this;
        r0 = com.google.android.exoplayer2.util.Util.SDK_INT;
        r1 = 25;
        if (r0 > r1) goto L_0x0038;
    L_0x0006:
        r0 = "OMX.Exynos.avc.dec.secure";
        r0 = r0.equals(r3);
        if (r0 == 0) goto L_0x0038;
    L_0x000e:
        r0 = com.google.android.exoplayer2.util.Util.MODEL;
        r1 = "SM-T585";
        r0 = r0.startsWith(r1);
        if (r0 != 0) goto L_0x0036;
    L_0x0018:
        r0 = com.google.android.exoplayer2.util.Util.MODEL;
        r1 = "SM-A510";
        r0 = r0.startsWith(r1);
        if (r0 != 0) goto L_0x0036;
    L_0x0022:
        r0 = com.google.android.exoplayer2.util.Util.MODEL;
        r1 = "SM-A520";
        r0 = r0.startsWith(r1);
        if (r0 != 0) goto L_0x0036;
    L_0x002c:
        r0 = com.google.android.exoplayer2.util.Util.MODEL;
        r1 = "SM-J700";
        r0 = r0.startsWith(r1);
        if (r0 == 0) goto L_0x0038;
    L_0x0036:
        r3 = 2;
        return r3;
    L_0x0038:
        r0 = com.google.android.exoplayer2.util.Util.SDK_INT;
        r1 = 24;
        if (r0 >= r1) goto L_0x0078;
    L_0x003e:
        r0 = "OMX.Nvidia.h264.decode";
        r0 = r0.equals(r3);
        if (r0 != 0) goto L_0x004e;
    L_0x0046:
        r0 = "OMX.Nvidia.h264.decode.secure";
        r3 = r0.equals(r3);
        if (r3 == 0) goto L_0x0078;
    L_0x004e:
        r3 = com.google.android.exoplayer2.util.Util.DEVICE;
        r0 = "flounder";
        r3 = r0.equals(r3);
        if (r3 != 0) goto L_0x0076;
    L_0x0058:
        r3 = com.google.android.exoplayer2.util.Util.DEVICE;
        r0 = "flounder_lte";
        r3 = r0.equals(r3);
        if (r3 != 0) goto L_0x0076;
    L_0x0062:
        r3 = com.google.android.exoplayer2.util.Util.DEVICE;
        r0 = "grouper";
        r3 = r0.equals(r3);
        if (r3 != 0) goto L_0x0076;
    L_0x006c:
        r3 = com.google.android.exoplayer2.util.Util.DEVICE;
        r0 = "tilapia";
        r3 = r0.equals(r3);
        if (r3 == 0) goto L_0x0078;
    L_0x0076:
        r3 = 1;
        return r3;
    L_0x0078:
        r3 = 0;
        return r3;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.exoplayer2.mediacodec.MediaCodecRenderer.codecAdaptationWorkaroundMode(java.lang.String):int");
    }

    private static boolean codecNeedsReconfigureWorkaround(String str) {
        return Util.MODEL.startsWith("SM-T230") && "OMX.MARVELL.VIDEO.HW.CODA7542DECODER".equals(str);
    }

    private static boolean codecNeedsDiscardToSpsWorkaround(String str, Format format) {
        return Util.SDK_INT < 21 && format.initializationData.isEmpty() && "OMX.MTK.VIDEO.DECODER.AVC".equals(str);
    }

    /* JADX WARNING: Missing block: B:11:0x002e, code skipped:
            if (r3.secure != false) goto L_0x0030;
     */
    private static boolean codecNeedsEosPropagationWorkaround(com.google.android.exoplayer2.mediacodec.MediaCodecInfo r3) {
        /*
        r0 = r3.name;
        r1 = com.google.android.exoplayer2.util.Util.SDK_INT;
        r2 = 17;
        if (r1 > r2) goto L_0x0018;
    L_0x0008:
        r1 = "OMX.rk.video_decoder.avc";
        r1 = r1.equals(r0);
        if (r1 != 0) goto L_0x0030;
    L_0x0010:
        r1 = "OMX.allwinner.video.decoder.avc";
        r0 = r1.equals(r0);
        if (r0 != 0) goto L_0x0030;
    L_0x0018:
        r0 = com.google.android.exoplayer2.util.Util.MANUFACTURER;
        r1 = "Amazon";
        r0 = r1.equals(r0);
        if (r0 == 0) goto L_0x0032;
    L_0x0022:
        r0 = com.google.android.exoplayer2.util.Util.MODEL;
        r1 = "AFTS";
        r0 = r1.equals(r0);
        if (r0 == 0) goto L_0x0032;
    L_0x002c:
        r3 = r3.secure;
        if (r3 == 0) goto L_0x0032;
    L_0x0030:
        r3 = 1;
        goto L_0x0033;
    L_0x0032:
        r3 = 0;
    L_0x0033:
        return r3;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.exoplayer2.mediacodec.MediaCodecRenderer.codecNeedsEosPropagationWorkaround(com.google.android.exoplayer2.mediacodec.MediaCodecInfo):boolean");
    }

    /* JADX WARNING: Missing block: B:9:0x0026, code skipped:
            if ("stvm8".equals(com.google.android.exoplayer2.util.Util.DEVICE) != false) goto L_0x0028;
     */
    /* JADX WARNING: Missing block: B:13:0x0036, code skipped:
            if ("OMX.amlogic.avc.decoder.awesome.secure".equals(r2) == false) goto L_0x003a;
     */
    private static boolean codecNeedsEosFlushWorkaround(java.lang.String r2) {
        /*
        r0 = com.google.android.exoplayer2.util.Util.SDK_INT;
        r1 = 23;
        if (r0 > r1) goto L_0x000e;
    L_0x0006:
        r0 = "OMX.google.vorbis.decoder";
        r0 = r0.equals(r2);
        if (r0 != 0) goto L_0x0038;
    L_0x000e:
        r0 = com.google.android.exoplayer2.util.Util.SDK_INT;
        r1 = 19;
        if (r0 > r1) goto L_0x003a;
    L_0x0014:
        r0 = com.google.android.exoplayer2.util.Util.DEVICE;
        r1 = "hb2000";
        r0 = r1.equals(r0);
        if (r0 != 0) goto L_0x0028;
    L_0x001e:
        r0 = com.google.android.exoplayer2.util.Util.DEVICE;
        r1 = "stvm8";
        r0 = r1.equals(r0);
        if (r0 == 0) goto L_0x003a;
    L_0x0028:
        r0 = "OMX.amlogic.avc.decoder.awesome";
        r0 = r0.equals(r2);
        if (r0 != 0) goto L_0x0038;
    L_0x0030:
        r0 = "OMX.amlogic.avc.decoder.awesome.secure";
        r2 = r0.equals(r2);
        if (r2 == 0) goto L_0x003a;
    L_0x0038:
        r2 = 1;
        goto L_0x003b;
    L_0x003a:
        r2 = 0;
    L_0x003b:
        return r2;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.exoplayer2.mediacodec.MediaCodecRenderer.codecNeedsEosFlushWorkaround(java.lang.String):boolean");
    }

    private static boolean codecNeedsEosOutputExceptionWorkaround(String str) {
        return Util.SDK_INT == 21 && "OMX.google.aac.decoder".equals(str);
    }

    private static boolean codecNeedsMonoChannelCountWorkaround(String str, Format format) {
        if (Util.SDK_INT <= 18 && format.channelCount == 1 && "OMX.MTK.AUDIO.DECODER.MP3".equals(str)) {
            return true;
        }
        return false;
    }
}
