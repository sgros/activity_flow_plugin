package com.google.android.exoplayer2.audio;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.media.MediaCodec;
import android.media.MediaCrypto;
import android.media.MediaFormat;
import android.os.Handler;
import com.google.android.exoplayer2.BaseRenderer;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.audio.AudioRendererEventListener.EventDispatcher;
import com.google.android.exoplayer2.audio.AudioSink.Listener;
import com.google.android.exoplayer2.audio.AudioSink.WriteException;
import com.google.android.exoplayer2.decoder.DecoderInputBuffer;
import com.google.android.exoplayer2.drm.DrmInitData;
import com.google.android.exoplayer2.drm.DrmSessionManager;
import com.google.android.exoplayer2.drm.FrameworkMediaCrypto;
import com.google.android.exoplayer2.mediacodec.MediaCodecInfo;
import com.google.android.exoplayer2.mediacodec.MediaCodecRenderer;
import com.google.android.exoplayer2.mediacodec.MediaCodecSelector;
import com.google.android.exoplayer2.mediacodec.MediaCodecUtil.DecoderQueryException;
import com.google.android.exoplayer2.mediacodec.MediaFormatUtil;
import com.google.android.exoplayer2.util.Log;
import com.google.android.exoplayer2.util.MediaClock;
import com.google.android.exoplayer2.util.MimeTypes;
import com.google.android.exoplayer2.util.Util;
import java.util.Collections;
import java.util.List;

@TargetApi(16)
public class MediaCodecAudioRenderer extends MediaCodecRenderer implements MediaClock {
    private boolean allowFirstBufferPositionDiscontinuity;
    private boolean allowPositionDiscontinuity;
    private final AudioSink audioSink;
    private int channelCount;
    private int codecMaxInputSize;
    private boolean codecNeedsDiscardChannelsWorkaround;
    private boolean codecNeedsEosBufferTimestampWorkaround;
    private final Context context;
    private long currentPositionUs;
    private int encoderDelay;
    private int encoderPadding;
    private final EventDispatcher eventDispatcher;
    private long lastInputTimeUs;
    private boolean passthroughEnabled;
    private MediaFormat passthroughMediaFormat;
    private int pcmEncoding;
    private int pendingStreamChangeCount;
    private final long[] pendingStreamChangeTimesUs;

    private final class AudioSinkListener implements Listener {
        private AudioSinkListener() {
        }

        public void onAudioSessionId(int i) {
            MediaCodecAudioRenderer.this.eventDispatcher.audioSessionId(i);
            MediaCodecAudioRenderer.this.onAudioSessionId(i);
        }

        public void onPositionDiscontinuity() {
            MediaCodecAudioRenderer.this.onAudioTrackPositionDiscontinuity();
            MediaCodecAudioRenderer.this.allowPositionDiscontinuity = true;
        }

        public void onUnderrun(int i, long j, long j2) {
            MediaCodecAudioRenderer.this.eventDispatcher.audioTrackUnderrun(i, j, j2);
            MediaCodecAudioRenderer.this.onAudioTrackUnderrun(i, j, j2);
        }
    }

    public MediaClock getMediaClock() {
        return this;
    }

    /* Access modifiers changed, original: protected */
    public void onAudioSessionId(int i) {
    }

    /* Access modifiers changed, original: protected */
    public void onAudioTrackPositionDiscontinuity() {
    }

    /* Access modifiers changed, original: protected */
    public void onAudioTrackUnderrun(int i, long j, long j2) {
    }

    /*  JADX ERROR: JadxRuntimeException in pass: BlockProcessor
        jadx.core.utils.exceptions.JadxRuntimeException: Can't find immediate dominator for block B:22:0x004e in {2, 3, 13, 14, 18, 21} preds:[]
        	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.computeDominators(BlockProcessor.java:242)
        	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.processBlocksTree(BlockProcessor.java:52)
        	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.visit(BlockProcessor.java:42)
        	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:27)
        	at jadx.core.dex.visitors.DepthTraversal.lambda$visit$1(DepthTraversal.java:14)
        	at java.base/java.util.ArrayList.forEach(ArrayList.java:1378)
        	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
        	at jadx.core.ProcessClass.process(ProcessClass.java:32)
        	at jadx.core.ProcessClass.lambda$processDependencies$0(ProcessClass.java:51)
        	at java.base/java.lang.Iterable.forEach(Iterable.java:75)
        	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:51)
        	at jadx.core.ProcessClass.process(ProcessClass.java:37)
        	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:292)
        	at jadx.api.JavaClass.decompile(JavaClass.java:62)
        	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
        */
    protected void onOutputFormatChanged(android.media.MediaCodec r9, android.media.MediaFormat r10) throws com.google.android.exoplayer2.ExoPlaybackException {
        /*
        r8 = this;
        r9 = r8.passthroughMediaFormat;
        if (r9 == 0) goto L_0x0011;
        r10 = "mime";
        r9 = r9.getString(r10);
        r9 = com.google.android.exoplayer2.util.MimeTypes.getEncoding(r9);
        r10 = r8.passthroughMediaFormat;
        goto L_0x0013;
        r9 = r8.pcmEncoding;
        r1 = r9;
        r9 = "channel-count";
        r2 = r10.getInteger(r9);
        r9 = "sample-rate";
        r3 = r10.getInteger(r9);
        r9 = r8.codecNeedsDiscardChannelsWorkaround;
        if (r9 == 0) goto L_0x0037;
        r9 = 6;
        if (r2 != r9) goto L_0x0037;
        r10 = r8.channelCount;
        if (r10 >= r9) goto L_0x0037;
        r9 = new int[r10];
        r10 = 0;
        r0 = r8.channelCount;
        if (r10 >= r0) goto L_0x0038;
        r9[r10] = r10;
        r10 = r10 + 1;
        goto L_0x002e;
        r9 = 0;
        r5 = r9;
        r0 = r8.audioSink;	 Catch:{ ConfigurationException -> 0x0044 }
        r4 = 0;	 Catch:{ ConfigurationException -> 0x0044 }
        r6 = r8.encoderDelay;	 Catch:{ ConfigurationException -> 0x0044 }
        r7 = r8.encoderPadding;	 Catch:{ ConfigurationException -> 0x0044 }
        r0.configure(r1, r2, r3, r4, r5, r6, r7);	 Catch:{ ConfigurationException -> 0x0044 }
        return;
        r9 = move-exception;
        r10 = r8.getIndex();
        r9 = com.google.android.exoplayer2.ExoPlaybackException.createForRenderer(r9, r10);
        throw r9;
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.exoplayer2.audio.MediaCodecAudioRenderer.onOutputFormatChanged(android.media.MediaCodec, android.media.MediaFormat):void");
    }

    public MediaCodecAudioRenderer(Context context, MediaCodecSelector mediaCodecSelector, DrmSessionManager<FrameworkMediaCrypto> drmSessionManager, boolean z, Handler handler, AudioRendererEventListener audioRendererEventListener, AudioCapabilities audioCapabilities, AudioProcessor... audioProcessorArr) {
        AudioCapabilities audioCapabilities2 = audioCapabilities;
        this(context, mediaCodecSelector, drmSessionManager, z, handler, audioRendererEventListener, new DefaultAudioSink(audioCapabilities, audioProcessorArr));
    }

    public MediaCodecAudioRenderer(Context context, MediaCodecSelector mediaCodecSelector, DrmSessionManager<FrameworkMediaCrypto> drmSessionManager, boolean z, Handler handler, AudioRendererEventListener audioRendererEventListener, AudioSink audioSink) {
        super(1, mediaCodecSelector, drmSessionManager, z, 44100.0f);
        this.context = context.getApplicationContext();
        this.audioSink = audioSink;
        this.lastInputTimeUs = -9223372036854775807L;
        this.pendingStreamChangeTimesUs = new long[10];
        this.eventDispatcher = new EventDispatcher(handler, audioRendererEventListener);
        audioSink.setListener(new AudioSinkListener());
    }

    /* Access modifiers changed, original: protected */
    public int supportsFormat(MediaCodecSelector mediaCodecSelector, DrmSessionManager<FrameworkMediaCrypto> drmSessionManager, Format format) throws DecoderQueryException {
        String str = format.sampleMimeType;
        if (!MimeTypes.isAudio(str)) {
            return 0;
        }
        int i = Util.SDK_INT >= 21 ? 32 : 0;
        boolean supportsFormatDrm = BaseRenderer.supportsFormatDrm(drmSessionManager, format.drmInitData);
        int i2 = 4;
        int i3 = 8;
        if (supportsFormatDrm && allowPassthrough(format.channelCount, str) && mediaCodecSelector.getPassthroughDecoderInfo() != null) {
            return (i | 8) | 4;
        }
        int i4 = 1;
        if ((MimeTypes.AUDIO_RAW.equals(str) && !this.audioSink.supportsOutput(format.channelCount, format.pcmEncoding)) || !this.audioSink.supportsOutput(format.channelCount, 2)) {
            return 1;
        }
        boolean z;
        DrmInitData drmInitData = format.drmInitData;
        if (drmInitData != null) {
            z = false;
            for (int i5 = 0; i5 < drmInitData.schemeDataCount; i5++) {
                z |= drmInitData.get(i5).requiresSecureDecryption;
            }
        } else {
            z = false;
        }
        List decoderInfos = mediaCodecSelector.getDecoderInfos(format.sampleMimeType, z);
        if (decoderInfos.isEmpty()) {
            if (z && !mediaCodecSelector.getDecoderInfos(format.sampleMimeType, false).isEmpty()) {
                i4 = 2;
            }
            return i4;
        } else if (!supportsFormatDrm) {
            return 2;
        } else {
            MediaCodecInfo mediaCodecInfo = (MediaCodecInfo) decoderInfos.get(0);
            supportsFormatDrm = mediaCodecInfo.isFormatSupported(format);
            if (supportsFormatDrm && mediaCodecInfo.isSeamlessAdaptationSupported(format)) {
                i3 = 16;
            }
            if (!supportsFormatDrm) {
                i2 = 3;
            }
            return (i3 | i) | i2;
        }
    }

    /* Access modifiers changed, original: protected */
    public List<MediaCodecInfo> getDecoderInfos(MediaCodecSelector mediaCodecSelector, Format format, boolean z) throws DecoderQueryException {
        if (allowPassthrough(format.channelCount, format.sampleMimeType)) {
            MediaCodecInfo passthroughDecoderInfo = mediaCodecSelector.getPassthroughDecoderInfo();
            if (passthroughDecoderInfo != null) {
                return Collections.singletonList(passthroughDecoderInfo);
            }
        }
        return super.getDecoderInfos(mediaCodecSelector, format, z);
    }

    /* Access modifiers changed, original: protected */
    public boolean allowPassthrough(int i, String str) {
        return this.audioSink.supportsOutput(i, MimeTypes.getEncoding(str));
    }

    /* Access modifiers changed, original: protected */
    public void configureCodec(MediaCodecInfo mediaCodecInfo, MediaCodec mediaCodec, Format format, MediaCrypto mediaCrypto, float f) {
        this.codecMaxInputSize = getCodecMaxInputSize(mediaCodecInfo, format, getStreamFormats());
        this.codecNeedsDiscardChannelsWorkaround = codecNeedsDiscardChannelsWorkaround(mediaCodecInfo.name);
        this.codecNeedsEosBufferTimestampWorkaround = codecNeedsEosBufferTimestampWorkaround(mediaCodecInfo.name);
        this.passthroughEnabled = mediaCodecInfo.passthrough;
        MediaFormat mediaFormat = getMediaFormat(format, this.passthroughEnabled ? MimeTypes.AUDIO_RAW : mediaCodecInfo.mimeType, this.codecMaxInputSize, f);
        mediaCodec.configure(mediaFormat, null, mediaCrypto, 0);
        if (this.passthroughEnabled) {
            this.passthroughMediaFormat = mediaFormat;
            this.passthroughMediaFormat.setString("mime", format.sampleMimeType);
            return;
        }
        this.passthroughMediaFormat = null;
    }

    /* Access modifiers changed, original: protected */
    public int canKeepCodec(MediaCodec mediaCodec, MediaCodecInfo mediaCodecInfo, Format format, Format format2) {
        if (getCodecMaxInputSize(mediaCodecInfo, format2) <= this.codecMaxInputSize && format.encoderDelay == 0 && format.encoderPadding == 0 && format2.encoderDelay == 0 && format2.encoderPadding == 0) {
            if (mediaCodecInfo.isSeamlessAdaptationSupported(format, format2, true)) {
                return 3;
            }
            if (areCodecConfigurationCompatible(format, format2)) {
                return 1;
            }
        }
        return 0;
    }

    /* Access modifiers changed, original: protected */
    public float getCodecOperatingRateV23(float f, Format format, Format[] formatArr) {
        int i = -1;
        for (Format format2 : formatArr) {
            int i2 = format2.sampleRate;
            if (i2 != -1) {
                i = Math.max(i, i2);
            }
        }
        return i == -1 ? -1.0f : f * ((float) i);
    }

    /* Access modifiers changed, original: protected */
    public void onCodecInitialized(String str, long j, long j2) {
        this.eventDispatcher.decoderInitialized(str, j, j2);
    }

    /* Access modifiers changed, original: protected */
    public void onInputFormatChanged(Format format) throws ExoPlaybackException {
        super.onInputFormatChanged(format);
        this.eventDispatcher.inputFormatChanged(format);
        this.pcmEncoding = MimeTypes.AUDIO_RAW.equals(format.sampleMimeType) ? format.pcmEncoding : 2;
        this.channelCount = format.channelCount;
        this.encoderDelay = format.encoderDelay;
        this.encoderPadding = format.encoderPadding;
    }

    /* Access modifiers changed, original: protected */
    public void onEnabled(boolean z) throws ExoPlaybackException {
        super.onEnabled(z);
        this.eventDispatcher.enabled(this.decoderCounters);
        int i = getConfiguration().tunnelingAudioSessionId;
        if (i != 0) {
            this.audioSink.enableTunnelingV21(i);
        } else {
            this.audioSink.disableTunneling();
        }
    }

    /* Access modifiers changed, original: protected */
    public void onStreamChanged(Format[] formatArr, long j) throws ExoPlaybackException {
        super.onStreamChanged(formatArr, j);
        if (this.lastInputTimeUs != -9223372036854775807L) {
            int i = this.pendingStreamChangeCount;
            if (i == this.pendingStreamChangeTimesUs.length) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Too many stream changes, so dropping change at ");
                stringBuilder.append(this.pendingStreamChangeTimesUs[this.pendingStreamChangeCount - 1]);
                Log.m18w("MediaCodecAudioRenderer", stringBuilder.toString());
            } else {
                this.pendingStreamChangeCount = i + 1;
            }
            this.pendingStreamChangeTimesUs[this.pendingStreamChangeCount - 1] = this.lastInputTimeUs;
        }
    }

    /* Access modifiers changed, original: protected */
    public void onPositionReset(long j, boolean z) throws ExoPlaybackException {
        super.onPositionReset(j, z);
        this.audioSink.flush();
        this.currentPositionUs = j;
        this.allowFirstBufferPositionDiscontinuity = true;
        this.allowPositionDiscontinuity = true;
        this.lastInputTimeUs = -9223372036854775807L;
        this.pendingStreamChangeCount = 0;
    }

    /* Access modifiers changed, original: protected */
    public void onStarted() {
        super.onStarted();
        this.audioSink.play();
    }

    /* Access modifiers changed, original: protected */
    public void onStopped() {
        updateCurrentPosition();
        this.audioSink.pause();
        super.onStopped();
    }

    /* Access modifiers changed, original: protected */
    public void onDisabled() {
        try {
            this.lastInputTimeUs = -9223372036854775807L;
            this.pendingStreamChangeCount = 0;
            this.audioSink.flush();
            try {
                super.onDisabled();
            } finally {
                this.eventDispatcher.disabled(this.decoderCounters);
            }
        } catch (Throwable th) {
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
            this.audioSink.reset();
        }
    }

    public boolean isEnded() {
        return super.isEnded() && this.audioSink.isEnded();
    }

    public boolean isReady() {
        return this.audioSink.hasPendingData() || super.isReady();
    }

    public long getPositionUs() {
        if (getState() == 2) {
            updateCurrentPosition();
        }
        return this.currentPositionUs;
    }

    public PlaybackParameters setPlaybackParameters(PlaybackParameters playbackParameters) {
        return this.audioSink.setPlaybackParameters(playbackParameters);
    }

    public PlaybackParameters getPlaybackParameters() {
        return this.audioSink.getPlaybackParameters();
    }

    /* Access modifiers changed, original: protected */
    public void onQueueInputBuffer(DecoderInputBuffer decoderInputBuffer) {
        if (this.allowFirstBufferPositionDiscontinuity && !decoderInputBuffer.isDecodeOnly()) {
            if (Math.abs(decoderInputBuffer.timeUs - this.currentPositionUs) > 500000) {
                this.currentPositionUs = decoderInputBuffer.timeUs;
            }
            this.allowFirstBufferPositionDiscontinuity = false;
        }
        this.lastInputTimeUs = Math.max(decoderInputBuffer.timeUs, this.lastInputTimeUs);
    }

    /* Access modifiers changed, original: protected */
    public void onProcessedOutputBuffer(long j) {
        while (this.pendingStreamChangeCount != 0 && j >= this.pendingStreamChangeTimesUs[0]) {
            this.audioSink.handleDiscontinuity();
            this.pendingStreamChangeCount--;
            long[] jArr = this.pendingStreamChangeTimesUs;
            System.arraycopy(jArr, 1, jArr, 0, this.pendingStreamChangeCount);
        }
    }

    /* Access modifiers changed, original: protected */
    /* JADX WARNING: Missing block: B:7:0x0017, code skipped:
            if (r1 != -9223372036854775807L) goto L_0x001b;
     */
    public boolean processOutputBuffer(long r1, long r3, android.media.MediaCodec r5, java.nio.ByteBuffer r6, int r7, int r8, long r9, boolean r11, com.google.android.exoplayer2.Format r12) throws com.google.android.exoplayer2.ExoPlaybackException {
        /*
        r0 = this;
        r1 = r0.codecNeedsEosBufferTimestampWorkaround;
        if (r1 == 0) goto L_0x001a;
    L_0x0004:
        r1 = 0;
        r3 = (r9 > r1 ? 1 : (r9 == r1 ? 0 : -1));
        if (r3 != 0) goto L_0x001a;
    L_0x000a:
        r1 = r8 & 4;
        if (r1 == 0) goto L_0x001a;
    L_0x000e:
        r1 = r0.lastInputTimeUs;
        r3 = -9223372036854775807; // 0x8000000000000001 float:1.4E-45 double:-4.9E-324;
        r12 = (r1 > r3 ? 1 : (r1 == r3 ? 0 : -1));
        if (r12 == 0) goto L_0x001a;
    L_0x0019:
        goto L_0x001b;
    L_0x001a:
        r1 = r9;
    L_0x001b:
        r3 = r0.passthroughEnabled;
        r4 = 0;
        r9 = 1;
        if (r3 == 0) goto L_0x0029;
    L_0x0021:
        r3 = r8 & 2;
        if (r3 == 0) goto L_0x0029;
    L_0x0025:
        r5.releaseOutputBuffer(r7, r4);
        return r9;
    L_0x0029:
        if (r11 == 0) goto L_0x003b;
    L_0x002b:
        r5.releaseOutputBuffer(r7, r4);
        r1 = r0.decoderCounters;
        r2 = r1.skippedOutputBufferCount;
        r2 = r2 + r9;
        r1.skippedOutputBufferCount = r2;
        r1 = r0.audioSink;
        r1.handleDiscontinuity();
        return r9;
    L_0x003b:
        r3 = r0.audioSink;	 Catch:{ InitializationException -> 0x0051, InitializationException | WriteException -> 0x004f }
        r1 = r3.handleBuffer(r6, r1);	 Catch:{ InitializationException -> 0x0051, InitializationException | WriteException -> 0x004f }
        if (r1 == 0) goto L_0x004e;
    L_0x0043:
        r5.releaseOutputBuffer(r7, r4);	 Catch:{ InitializationException -> 0x0051, InitializationException | WriteException -> 0x004f }
        r1 = r0.decoderCounters;	 Catch:{ InitializationException -> 0x0051, InitializationException | WriteException -> 0x004f }
        r2 = r1.renderedOutputBufferCount;	 Catch:{ InitializationException -> 0x0051, InitializationException | WriteException -> 0x004f }
        r2 = r2 + r9;
        r1.renderedOutputBufferCount = r2;	 Catch:{ InitializationException -> 0x0051, InitializationException | WriteException -> 0x004f }
        return r9;
    L_0x004e:
        return r4;
    L_0x004f:
        r1 = move-exception;
        goto L_0x0052;
    L_0x0051:
        r1 = move-exception;
    L_0x0052:
        r2 = r0.getIndex();
        r1 = com.google.android.exoplayer2.ExoPlaybackException.createForRenderer(r1, r2);
        throw r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.exoplayer2.audio.MediaCodecAudioRenderer.processOutputBuffer(long, long, android.media.MediaCodec, java.nio.ByteBuffer, int, int, long, boolean, com.google.android.exoplayer2.Format):boolean");
    }

    /* Access modifiers changed, original: protected */
    public void renderToEndOfStream() throws ExoPlaybackException {
        try {
            this.audioSink.playToEndOfStream();
        } catch (WriteException e) {
            throw ExoPlaybackException.createForRenderer(e, getIndex());
        }
    }

    public void handleMessage(int i, Object obj) throws ExoPlaybackException {
        if (i == 2) {
            this.audioSink.setVolume(((Float) obj).floatValue());
        } else if (i == 3) {
            this.audioSink.setAudioAttributes((AudioAttributes) obj);
        } else if (i != 5) {
            super.handleMessage(i, obj);
        } else {
            this.audioSink.setAuxEffectInfo((AuxEffectInfo) obj);
        }
    }

    /* Access modifiers changed, original: protected */
    public int getCodecMaxInputSize(MediaCodecInfo mediaCodecInfo, Format format, Format[] formatArr) {
        int codecMaxInputSize = getCodecMaxInputSize(mediaCodecInfo, format);
        if (formatArr.length == 1) {
            return codecMaxInputSize;
        }
        int i = codecMaxInputSize;
        for (Format format2 : formatArr) {
            if (mediaCodecInfo.isSeamlessAdaptationSupported(format, format2, false)) {
                i = Math.max(i, getCodecMaxInputSize(mediaCodecInfo, format2));
            }
        }
        return i;
    }

    private int getCodecMaxInputSize(MediaCodecInfo mediaCodecInfo, Format format) {
        if ("OMX.google.raw.decoder".equals(mediaCodecInfo.name)) {
            int i = Util.SDK_INT;
            if (i < 24 && !(i == 23 && Util.isTv(this.context))) {
                return -1;
            }
        }
        return format.maxInputSize;
    }

    /* Access modifiers changed, original: protected */
    public boolean areCodecConfigurationCompatible(Format format, Format format2) {
        return Util.areEqual(format.sampleMimeType, format2.sampleMimeType) && format.channelCount == format2.channelCount && format.sampleRate == format2.sampleRate && format.initializationDataEquals(format2);
    }

    /* Access modifiers changed, original: protected */
    @SuppressLint({"InlinedApi"})
    public MediaFormat getMediaFormat(Format format, String str, int i, float f) {
        MediaFormat mediaFormat = new MediaFormat();
        mediaFormat.setString("mime", str);
        mediaFormat.setInteger("channel-count", format.channelCount);
        mediaFormat.setInteger("sample-rate", format.sampleRate);
        MediaFormatUtil.setCsdBuffers(mediaFormat, format.initializationData);
        MediaFormatUtil.maybeSetInteger(mediaFormat, "max-input-size", i);
        if (Util.SDK_INT >= 23) {
            mediaFormat.setInteger("priority", 0);
            if (f != -1.0f) {
                mediaFormat.setFloat("operating-rate", f);
            }
        }
        return mediaFormat;
    }

    private void updateCurrentPosition() {
        long currentPositionUs = this.audioSink.getCurrentPositionUs(isEnded());
        if (currentPositionUs != Long.MIN_VALUE) {
            if (!this.allowPositionDiscontinuity) {
                currentPositionUs = Math.max(this.currentPositionUs, currentPositionUs);
            }
            this.currentPositionUs = currentPositionUs;
            this.allowPositionDiscontinuity = false;
        }
    }

    private static boolean codecNeedsDiscardChannelsWorkaround(String str) {
        if (Util.SDK_INT < 24 && "OMX.SEC.aac.dec".equals(str)) {
            if ("samsung".equals(Util.MANUFACTURER) && (Util.DEVICE.startsWith("zeroflte") || Util.DEVICE.startsWith("herolte") || Util.DEVICE.startsWith("heroqlte"))) {
                return true;
            }
        }
        return false;
    }

    private static boolean codecNeedsEosBufferTimestampWorkaround(String str) {
        if (Util.SDK_INT < 21 && "OMX.SEC.mp3.dec".equals(str)) {
            if ("samsung".equals(Util.MANUFACTURER) && (Util.DEVICE.startsWith("baffin") || Util.DEVICE.startsWith("grand") || Util.DEVICE.startsWith("fortuna") || Util.DEVICE.startsWith("gprimelte") || Util.DEVICE.startsWith("j2y18lte") || Util.DEVICE.startsWith("ms01"))) {
                return true;
            }
        }
        return false;
    }
}
