// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.audio;

import com.google.android.exoplayer2.drm.DrmInitData;
import com.google.android.exoplayer2.BaseRenderer;
import com.google.android.exoplayer2.decoder.DecoderCounters;
import java.nio.ByteBuffer;
import com.google.android.exoplayer2.util.Log;
import com.google.android.exoplayer2.decoder.DecoderInputBuffer;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.PlaybackParameters;
import android.annotation.SuppressLint;
import com.google.android.exoplayer2.mediacodec.MediaFormatUtil;
import com.google.android.exoplayer2.mediacodec.MediaCodecUtil;
import java.util.Collections;
import java.util.List;
import android.view.Surface;
import android.media.MediaCrypto;
import android.media.MediaCodec;
import com.google.android.exoplayer2.util.MimeTypes;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.mediacodec.MediaCodecInfo;
import com.google.android.exoplayer2.util.Util;
import android.os.Handler;
import com.google.android.exoplayer2.drm.FrameworkMediaCrypto;
import com.google.android.exoplayer2.drm.DrmSessionManager;
import com.google.android.exoplayer2.mediacodec.MediaCodecSelector;
import android.media.MediaFormat;
import android.content.Context;
import android.annotation.TargetApi;
import com.google.android.exoplayer2.util.MediaClock;
import com.google.android.exoplayer2.mediacodec.MediaCodecRenderer;

@TargetApi(16)
public class MediaCodecAudioRenderer extends MediaCodecRenderer implements MediaClock
{
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
    private final AudioRendererEventListener.EventDispatcher eventDispatcher;
    private long lastInputTimeUs;
    private boolean passthroughEnabled;
    private MediaFormat passthroughMediaFormat;
    private int pcmEncoding;
    private int pendingStreamChangeCount;
    private final long[] pendingStreamChangeTimesUs;
    
    public MediaCodecAudioRenderer(final Context context, final MediaCodecSelector mediaCodecSelector, final DrmSessionManager<FrameworkMediaCrypto> drmSessionManager, final boolean b, final Handler handler, final AudioRendererEventListener audioRendererEventListener, final AudioCapabilities audioCapabilities, final AudioProcessor... array) {
        this(context, mediaCodecSelector, drmSessionManager, b, handler, audioRendererEventListener, new DefaultAudioSink(audioCapabilities, array));
    }
    
    public MediaCodecAudioRenderer(final Context context, final MediaCodecSelector mediaCodecSelector, final DrmSessionManager<FrameworkMediaCrypto> drmSessionManager, final boolean b, final Handler handler, final AudioRendererEventListener audioRendererEventListener, final AudioSink audioSink) {
        super(1, mediaCodecSelector, drmSessionManager, b, 44100.0f);
        this.context = context.getApplicationContext();
        this.audioSink = audioSink;
        this.lastInputTimeUs = -9223372036854775807L;
        this.pendingStreamChangeTimesUs = new long[10];
        this.eventDispatcher = new AudioRendererEventListener.EventDispatcher(handler, audioRendererEventListener);
        audioSink.setListener((AudioSink.Listener)new AudioSinkListener());
    }
    
    private static boolean codecNeedsDiscardChannelsWorkaround(final String anObject) {
        return Util.SDK_INT < 24 && "OMX.SEC.aac.dec".equals(anObject) && "samsung".equals(Util.MANUFACTURER) && (Util.DEVICE.startsWith("zeroflte") || Util.DEVICE.startsWith("herolte") || Util.DEVICE.startsWith("heroqlte"));
    }
    
    private static boolean codecNeedsEosBufferTimestampWorkaround(final String anObject) {
        return Util.SDK_INT < 21 && "OMX.SEC.mp3.dec".equals(anObject) && "samsung".equals(Util.MANUFACTURER) && (Util.DEVICE.startsWith("baffin") || Util.DEVICE.startsWith("grand") || Util.DEVICE.startsWith("fortuna") || Util.DEVICE.startsWith("gprimelte") || Util.DEVICE.startsWith("j2y18lte") || Util.DEVICE.startsWith("ms01"));
    }
    
    private int getCodecMaxInputSize(final MediaCodecInfo mediaCodecInfo, final Format format) {
        if ("OMX.google.raw.decoder".equals(mediaCodecInfo.name)) {
            final int sdk_INT = Util.SDK_INT;
            if (sdk_INT < 24 && (sdk_INT != 23 || !Util.isTv(this.context))) {
                return -1;
            }
        }
        return format.maxInputSize;
    }
    
    private void updateCurrentPosition() {
        long n = this.audioSink.getCurrentPositionUs(this.isEnded());
        if (n != Long.MIN_VALUE) {
            if (!this.allowPositionDiscontinuity) {
                n = Math.max(this.currentPositionUs, n);
            }
            this.currentPositionUs = n;
            this.allowPositionDiscontinuity = false;
        }
    }
    
    protected boolean allowPassthrough(final int n, final String s) {
        return this.audioSink.supportsOutput(n, MimeTypes.getEncoding(s));
    }
    
    protected boolean areCodecConfigurationCompatible(final Format format, final Format format2) {
        return Util.areEqual(format.sampleMimeType, format2.sampleMimeType) && format.channelCount == format2.channelCount && format.sampleRate == format2.sampleRate && format.initializationDataEquals(format2);
    }
    
    @Override
    protected int canKeepCodec(final MediaCodec mediaCodec, final MediaCodecInfo mediaCodecInfo, final Format format, final Format format2) {
        if (this.getCodecMaxInputSize(mediaCodecInfo, format2) <= this.codecMaxInputSize && format.encoderDelay == 0 && format.encoderPadding == 0 && format2.encoderDelay == 0) {
            if (format2.encoderPadding == 0) {
                if (mediaCodecInfo.isSeamlessAdaptationSupported(format, format2, true)) {
                    return 3;
                }
                if (this.areCodecConfigurationCompatible(format, format2)) {
                    return 1;
                }
            }
        }
        return 0;
    }
    
    @Override
    protected void configureCodec(final MediaCodecInfo mediaCodecInfo, final MediaCodec mediaCodec, final Format format, final MediaCrypto mediaCrypto, final float n) {
        this.codecMaxInputSize = this.getCodecMaxInputSize(mediaCodecInfo, format, this.getStreamFormats());
        this.codecNeedsDiscardChannelsWorkaround = codecNeedsDiscardChannelsWorkaround(mediaCodecInfo.name);
        this.codecNeedsEosBufferTimestampWorkaround = codecNeedsEosBufferTimestampWorkaround(mediaCodecInfo.name);
        this.passthroughEnabled = mediaCodecInfo.passthrough;
        String mimeType;
        if (this.passthroughEnabled) {
            mimeType = "audio/raw";
        }
        else {
            mimeType = mediaCodecInfo.mimeType;
        }
        final MediaFormat mediaFormat = this.getMediaFormat(format, mimeType, this.codecMaxInputSize, n);
        mediaCodec.configure(mediaFormat, (Surface)null, mediaCrypto, 0);
        if (this.passthroughEnabled) {
            (this.passthroughMediaFormat = mediaFormat).setString("mime", format.sampleMimeType);
        }
        else {
            this.passthroughMediaFormat = null;
        }
    }
    
    protected int getCodecMaxInputSize(final MediaCodecInfo mediaCodecInfo, final Format format, final Format[] array) {
        int codecMaxInputSize = this.getCodecMaxInputSize(mediaCodecInfo, format);
        if (array.length == 1) {
            return codecMaxInputSize;
        }
        int max;
        for (int length = array.length, i = 0; i < length; ++i, codecMaxInputSize = max) {
            final Format format2 = array[i];
            max = codecMaxInputSize;
            if (mediaCodecInfo.isSeamlessAdaptationSupported(format, format2, false)) {
                max = Math.max(codecMaxInputSize, this.getCodecMaxInputSize(mediaCodecInfo, format2));
            }
        }
        return codecMaxInputSize;
    }
    
    @Override
    protected float getCodecOperatingRateV23(float n, final Format format, final Format[] array) {
        final int length = array.length;
        int i = 0;
        int a = -1;
        while (i < length) {
            final int sampleRate = array[i].sampleRate;
            int max = a;
            if (sampleRate != -1) {
                max = Math.max(a, sampleRate);
            }
            ++i;
            a = max;
        }
        if (a == -1) {
            n = -1.0f;
        }
        else {
            n *= a;
        }
        return n;
    }
    
    @Override
    protected List<MediaCodecInfo> getDecoderInfos(final MediaCodecSelector mediaCodecSelector, final Format format, final boolean b) throws MediaCodecUtil.DecoderQueryException {
        if (this.allowPassthrough(format.channelCount, format.sampleMimeType)) {
            final MediaCodecInfo passthroughDecoderInfo = mediaCodecSelector.getPassthroughDecoderInfo();
            if (passthroughDecoderInfo != null) {
                return Collections.singletonList(passthroughDecoderInfo);
            }
        }
        return super.getDecoderInfos(mediaCodecSelector, format, b);
    }
    
    @Override
    public MediaClock getMediaClock() {
        return this;
    }
    
    @SuppressLint({ "InlinedApi" })
    protected MediaFormat getMediaFormat(final Format format, final String s, final int n, final float n2) {
        final MediaFormat mediaFormat = new MediaFormat();
        mediaFormat.setString("mime", s);
        mediaFormat.setInteger("channel-count", format.channelCount);
        mediaFormat.setInteger("sample-rate", format.sampleRate);
        MediaFormatUtil.setCsdBuffers(mediaFormat, format.initializationData);
        MediaFormatUtil.maybeSetInteger(mediaFormat, "max-input-size", n);
        if (Util.SDK_INT >= 23) {
            mediaFormat.setInteger("priority", 0);
            if (n2 != -1.0f) {
                mediaFormat.setFloat("operating-rate", n2);
            }
        }
        return mediaFormat;
    }
    
    @Override
    public PlaybackParameters getPlaybackParameters() {
        return this.audioSink.getPlaybackParameters();
    }
    
    @Override
    public long getPositionUs() {
        if (this.getState() == 2) {
            this.updateCurrentPosition();
        }
        return this.currentPositionUs;
    }
    
    @Override
    public void handleMessage(final int n, final Object o) throws ExoPlaybackException {
        if (n != 2) {
            if (n != 3) {
                if (n != 5) {
                    super.handleMessage(n, o);
                }
                else {
                    this.audioSink.setAuxEffectInfo((AuxEffectInfo)o);
                }
            }
            else {
                this.audioSink.setAudioAttributes((AudioAttributes)o);
            }
        }
        else {
            this.audioSink.setVolume((float)o);
        }
    }
    
    @Override
    public boolean isEnded() {
        return super.isEnded() && this.audioSink.isEnded();
    }
    
    @Override
    public boolean isReady() {
        return this.audioSink.hasPendingData() || super.isReady();
    }
    
    protected void onAudioSessionId(final int n) {
    }
    
    protected void onAudioTrackPositionDiscontinuity() {
    }
    
    protected void onAudioTrackUnderrun(final int n, final long n2, final long n3) {
    }
    
    @Override
    protected void onCodecInitialized(final String s, final long n, final long n2) {
        this.eventDispatcher.decoderInitialized(s, n, n2);
    }
    
    @Override
    protected void onDisabled() {
        try {
            this.lastInputTimeUs = -9223372036854775807L;
            this.pendingStreamChangeCount = 0;
            this.audioSink.flush();
            try {
                super.onDisabled();
            }
            finally {
                this.eventDispatcher.disabled(super.decoderCounters);
            }
        }
        finally {
            try {
                super.onDisabled();
                this.eventDispatcher.disabled(super.decoderCounters);
            }
            finally {
                this.eventDispatcher.disabled(super.decoderCounters);
            }
        }
    }
    
    @Override
    protected void onEnabled(final boolean b) throws ExoPlaybackException {
        super.onEnabled(b);
        this.eventDispatcher.enabled(super.decoderCounters);
        final int tunnelingAudioSessionId = this.getConfiguration().tunnelingAudioSessionId;
        if (tunnelingAudioSessionId != 0) {
            this.audioSink.enableTunnelingV21(tunnelingAudioSessionId);
        }
        else {
            this.audioSink.disableTunneling();
        }
    }
    
    @Override
    protected void onInputFormatChanged(final Format format) throws ExoPlaybackException {
        super.onInputFormatChanged(format);
        this.eventDispatcher.inputFormatChanged(format);
        int pcmEncoding;
        if ("audio/raw".equals(format.sampleMimeType)) {
            pcmEncoding = format.pcmEncoding;
        }
        else {
            pcmEncoding = 2;
        }
        this.pcmEncoding = pcmEncoding;
        this.channelCount = format.channelCount;
        this.encoderDelay = format.encoderDelay;
        this.encoderPadding = format.encoderPadding;
    }
    
    @Override
    protected void onOutputFormatChanged(final MediaCodec mediaCodec, MediaFormat passthroughMediaFormat) throws ExoPlaybackException {
        final MediaFormat passthroughMediaFormat2 = this.passthroughMediaFormat;
        int n;
        if (passthroughMediaFormat2 != null) {
            n = MimeTypes.getEncoding(passthroughMediaFormat2.getString("mime"));
            passthroughMediaFormat = this.passthroughMediaFormat;
        }
        else {
            n = this.pcmEncoding;
        }
        final int integer = passthroughMediaFormat.getInteger("channel-count");
        final int integer2 = passthroughMediaFormat.getInteger("sample-rate");
        Label_0111: {
            if (this.codecNeedsDiscardChannelsWorkaround && integer == 6) {
                final int channelCount = this.channelCount;
                if (channelCount < 6) {
                    final int[] array = new int[channelCount];
                    int n2 = 0;
                    while (true) {
                        final int[] array2 = array;
                        if (n2 >= this.channelCount) {
                            break Label_0111;
                        }
                        array[n2] = n2;
                        ++n2;
                    }
                }
            }
            final int[] array2 = null;
            try {
                this.audioSink.configure(n, integer, integer2, 0, array2, this.encoderDelay, this.encoderPadding);
            }
            catch (AudioSink.ConfigurationException ex) {
                throw ExoPlaybackException.createForRenderer(ex, this.getIndex());
            }
        }
    }
    
    @Override
    protected void onPositionReset(final long currentPositionUs, final boolean b) throws ExoPlaybackException {
        super.onPositionReset(currentPositionUs, b);
        this.audioSink.flush();
        this.currentPositionUs = currentPositionUs;
        this.allowFirstBufferPositionDiscontinuity = true;
        this.allowPositionDiscontinuity = true;
        this.lastInputTimeUs = -9223372036854775807L;
        this.pendingStreamChangeCount = 0;
    }
    
    @Override
    protected void onProcessedOutputBuffer(final long n) {
        while (this.pendingStreamChangeCount != 0 && n >= this.pendingStreamChangeTimesUs[0]) {
            this.audioSink.handleDiscontinuity();
            --this.pendingStreamChangeCount;
            final long[] pendingStreamChangeTimesUs = this.pendingStreamChangeTimesUs;
            System.arraycopy(pendingStreamChangeTimesUs, 1, pendingStreamChangeTimesUs, 0, this.pendingStreamChangeCount);
        }
    }
    
    @Override
    protected void onQueueInputBuffer(final DecoderInputBuffer decoderInputBuffer) {
        if (this.allowFirstBufferPositionDiscontinuity && !decoderInputBuffer.isDecodeOnly()) {
            if (Math.abs(decoderInputBuffer.timeUs - this.currentPositionUs) > 500000L) {
                this.currentPositionUs = decoderInputBuffer.timeUs;
            }
            this.allowFirstBufferPositionDiscontinuity = false;
        }
        this.lastInputTimeUs = Math.max(decoderInputBuffer.timeUs, this.lastInputTimeUs);
    }
    
    @Override
    protected void onReset() {
        try {
            super.onReset();
        }
        finally {
            this.audioSink.reset();
        }
    }
    
    @Override
    protected void onStarted() {
        super.onStarted();
        this.audioSink.play();
    }
    
    @Override
    protected void onStopped() {
        this.updateCurrentPosition();
        this.audioSink.pause();
        super.onStopped();
    }
    
    @Override
    protected void onStreamChanged(final Format[] array, final long n) throws ExoPlaybackException {
        super.onStreamChanged(array, n);
        if (this.lastInputTimeUs != -9223372036854775807L) {
            final int pendingStreamChangeCount = this.pendingStreamChangeCount;
            if (pendingStreamChangeCount == this.pendingStreamChangeTimesUs.length) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Too many stream changes, so dropping change at ");
                sb.append(this.pendingStreamChangeTimesUs[this.pendingStreamChangeCount - 1]);
                Log.w("MediaCodecAudioRenderer", sb.toString());
            }
            else {
                this.pendingStreamChangeCount = pendingStreamChangeCount + 1;
            }
            this.pendingStreamChangeTimesUs[this.pendingStreamChangeCount - 1] = this.lastInputTimeUs;
        }
    }
    
    @Override
    protected boolean processOutputBuffer(long lastInputTimeUs, final long n, MediaCodec decoderCounters, final ByteBuffer byteBuffer, final int n2, final int n3, long n4, final boolean b, final Format format) throws ExoPlaybackException {
        if (this.codecNeedsEosBufferTimestampWorkaround && n4 == 0L && (n3 & 0x4) != 0x0) {
            lastInputTimeUs = this.lastInputTimeUs;
            if (lastInputTimeUs != -9223372036854775807L) {
                n4 = lastInputTimeUs;
            }
        }
        if (this.passthroughEnabled && (n3 & 0x2) != 0x0) {
            ((MediaCodec)decoderCounters).releaseOutputBuffer(n2, false);
            return true;
        }
        if (b) {
            ((MediaCodec)decoderCounters).releaseOutputBuffer(n2, false);
            final DecoderCounters decoderCounters2 = super.decoderCounters;
            ++decoderCounters2.skippedOutputBufferCount;
            this.audioSink.handleDiscontinuity();
            return true;
        }
        try {
            if (this.audioSink.handleBuffer(byteBuffer, n4)) {
                ((MediaCodec)decoderCounters).releaseOutputBuffer(n2, false);
                decoderCounters = (AudioSink.WriteException)super.decoderCounters;
                ++((DecoderCounters)decoderCounters).renderedOutputBufferCount;
                return true;
            }
            return false;
        }
        catch (AudioSink.WriteException decoderCounters) {}
        catch (AudioSink.InitializationException ex) {}
        throw ExoPlaybackException.createForRenderer(decoderCounters, this.getIndex());
    }
    
    @Override
    protected void renderToEndOfStream() throws ExoPlaybackException {
        try {
            this.audioSink.playToEndOfStream();
        }
        catch (AudioSink.WriteException ex) {
            throw ExoPlaybackException.createForRenderer(ex, this.getIndex());
        }
    }
    
    @Override
    public PlaybackParameters setPlaybackParameters(final PlaybackParameters playbackParameters) {
        return this.audioSink.setPlaybackParameters(playbackParameters);
    }
    
    @Override
    protected int supportsFormat(final MediaCodecSelector mediaCodecSelector, final DrmSessionManager<FrameworkMediaCrypto> drmSessionManager, final Format format) throws MediaCodecUtil.DecoderQueryException {
        final String sampleMimeType = format.sampleMimeType;
        if (!MimeTypes.isAudio(sampleMimeType)) {
            return 0;
        }
        int n;
        if (Util.SDK_INT >= 21) {
            n = 32;
        }
        else {
            n = 0;
        }
        final boolean supportsFormatDrm = BaseRenderer.supportsFormatDrm(drmSessionManager, format.drmInitData);
        int n2 = 4;
        final int n3 = 8;
        if (supportsFormatDrm && this.allowPassthrough(format.channelCount, sampleMimeType) && mediaCodecSelector.getPassthroughDecoderInfo() != null) {
            return n | 0x8 | 0x4;
        }
        final boolean equals = "audio/raw".equals(sampleMimeType);
        final boolean b = true;
        if ((equals && !this.audioSink.supportsOutput(format.channelCount, format.pcmEncoding)) || !this.audioSink.supportsOutput(format.channelCount, 2)) {
            return 1;
        }
        final DrmInitData drmInitData = format.drmInitData;
        int n6;
        if (drmInitData != null) {
            int n4 = 0;
            int n5 = 0;
            while (true) {
                n6 = n5;
                if (n4 >= drmInitData.schemeDataCount) {
                    break;
                }
                n5 |= (drmInitData.get(n4).requiresSecureDecryption ? 1 : 0);
                ++n4;
            }
        }
        else {
            n6 = 0;
        }
        final List<MediaCodecInfo> decoderInfos = mediaCodecSelector.getDecoderInfos(format.sampleMimeType, (boolean)(n6 != 0));
        if (decoderInfos.isEmpty()) {
            int n7 = b ? 1 : 0;
            if (n6 != 0) {
                n7 = (b ? 1 : 0);
                if (!mediaCodecSelector.getDecoderInfos(format.sampleMimeType, false).isEmpty()) {
                    n7 = 2;
                }
            }
            return n7;
        }
        if (!supportsFormatDrm) {
            return 2;
        }
        final MediaCodecInfo mediaCodecInfo = decoderInfos.get(0);
        final boolean formatSupported = mediaCodecInfo.isFormatSupported(format);
        int n8 = n3;
        if (formatSupported) {
            n8 = n3;
            if (mediaCodecInfo.isSeamlessAdaptationSupported(format)) {
                n8 = 16;
            }
        }
        if (!formatSupported) {
            n2 = 3;
        }
        return n8 | n | n2;
    }
    
    private final class AudioSinkListener implements Listener
    {
        @Override
        public void onAudioSessionId(final int n) {
            MediaCodecAudioRenderer.this.eventDispatcher.audioSessionId(n);
            MediaCodecAudioRenderer.this.onAudioSessionId(n);
        }
        
        @Override
        public void onPositionDiscontinuity() {
            MediaCodecAudioRenderer.this.onAudioTrackPositionDiscontinuity();
            MediaCodecAudioRenderer.this.allowPositionDiscontinuity = true;
        }
        
        @Override
        public void onUnderrun(final int n, final long n2, final long n3) {
            MediaCodecAudioRenderer.this.eventDispatcher.audioTrackUnderrun(n, n2, n3);
            MediaCodecAudioRenderer.this.onAudioTrackUnderrun(n, n2, n3);
        }
    }
}
