// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.mediacodec;

import android.media.MediaCodec$CodecException;
import com.google.android.exoplayer2.drm.DrmInitData;
import android.os.Looper;
import android.media.MediaCryptoException;
import android.os.Bundle;
import android.media.MediaFormat;
import java.util.Collection;
import com.google.android.exoplayer2.util.TraceUtil;
import android.os.SystemClock;
import android.media.MediaCodec$CryptoInfo;
import com.google.android.exoplayer2.util.Log;
import java.util.List;
import com.google.android.exoplayer2.util.NalUnitUtil;
import android.media.MediaCodec$CryptoException;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.util.Util;
import android.media.MediaCodec$BufferInfo;
import android.media.MediaCrypto;
import java.nio.ByteBuffer;
import com.google.android.exoplayer2.util.TimedValueQueue;
import com.google.android.exoplayer2.FormatHolder;
import com.google.android.exoplayer2.drm.DrmSessionManager;
import com.google.android.exoplayer2.decoder.DecoderCounters;
import java.util.ArrayList;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.drm.FrameworkMediaCrypto;
import com.google.android.exoplayer2.drm.DrmSession;
import android.media.MediaCodec;
import com.google.android.exoplayer2.decoder.DecoderInputBuffer;
import java.util.ArrayDeque;
import android.annotation.TargetApi;
import com.google.android.exoplayer2.BaseRenderer;

@TargetApi(16)
public abstract class MediaCodecRenderer extends BaseRenderer
{
    private static final byte[] ADAPTATION_WORKAROUND_BUFFER;
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
    private final MediaCodec$BufferInfo outputBufferInfo;
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
    
    static {
        ADAPTATION_WORKAROUND_BUFFER = Util.getBytesFromHexString("0000016742C00BDA259000000168CE0F13200000016588840DCE7118A0002FBF1C31C3275D78");
    }
    
    public MediaCodecRenderer(final int n, final MediaCodecSelector mediaCodecSelector, final DrmSessionManager<FrameworkMediaCrypto> drmSessionManager, final boolean playClearSamplesWithoutKeys, final float assumedMinimumCodecOperatingRate) {
        super(n);
        Assertions.checkState(Util.SDK_INT >= 16);
        Assertions.checkNotNull(mediaCodecSelector);
        this.mediaCodecSelector = mediaCodecSelector;
        this.drmSessionManager = drmSessionManager;
        this.playClearSamplesWithoutKeys = playClearSamplesWithoutKeys;
        this.assumedMinimumCodecOperatingRate = assumedMinimumCodecOperatingRate;
        this.buffer = new DecoderInputBuffer(0);
        this.flagsOnlyBuffer = DecoderInputBuffer.newFlagsOnlyInstance();
        this.formatHolder = new FormatHolder();
        this.formatQueue = new TimedValueQueue<Format>();
        this.decodeOnlyPresentationTimestamps = new ArrayList<Long>();
        this.outputBufferInfo = new MediaCodec$BufferInfo();
        this.codecReconfigurationState = 0;
        this.codecDrainState = 0;
        this.codecDrainAction = 0;
        this.codecOperatingRate = -1.0f;
        this.rendererOperatingRate = 1.0f;
        this.renderTimeLimitMs = -9223372036854775807L;
    }
    
    private int codecAdaptationWorkaroundMode(final String anObject) {
        if (Util.SDK_INT <= 25 && "OMX.Exynos.avc.dec.secure".equals(anObject) && (Util.MODEL.startsWith("SM-T585") || Util.MODEL.startsWith("SM-A510") || Util.MODEL.startsWith("SM-A520") || Util.MODEL.startsWith("SM-J700"))) {
            return 2;
        }
        if (Util.SDK_INT < 24 && ("OMX.Nvidia.h264.decode".equals(anObject) || "OMX.Nvidia.h264.decode.secure".equals(anObject)) && ("flounder".equals(Util.DEVICE) || "flounder_lte".equals(Util.DEVICE) || "grouper".equals(Util.DEVICE) || "tilapia".equals(Util.DEVICE))) {
            return 1;
        }
        return 0;
    }
    
    private static boolean codecNeedsDiscardToSpsWorkaround(final String anObject, final Format format) {
        return Util.SDK_INT < 21 && format.initializationData.isEmpty() && "OMX.MTK.VIDEO.DECODER.AVC".equals(anObject);
    }
    
    private static boolean codecNeedsEosFlushWorkaround(final String anObject) {
        return (Util.SDK_INT <= 23 && "OMX.google.vorbis.decoder".equals(anObject)) || (Util.SDK_INT <= 19 && ("hb2000".equals(Util.DEVICE) || "stvm8".equals(Util.DEVICE)) && ("OMX.amlogic.avc.decoder.awesome".equals(anObject) || "OMX.amlogic.avc.decoder.awesome.secure".equals(anObject)));
    }
    
    private static boolean codecNeedsEosOutputExceptionWorkaround(final String anObject) {
        return Util.SDK_INT == 21 && "OMX.google.aac.decoder".equals(anObject);
    }
    
    private static boolean codecNeedsEosPropagationWorkaround(final MediaCodecInfo mediaCodecInfo) {
        final String name = mediaCodecInfo.name;
        return (Util.SDK_INT <= 17 && ("OMX.rk.video_decoder.avc".equals(name) || "OMX.allwinner.video.decoder.avc".equals(name))) || ("Amazon".equals(Util.MANUFACTURER) && "AFTS".equals(Util.MODEL) && mediaCodecInfo.secure);
    }
    
    private static boolean codecNeedsFlushWorkaround(final String s) {
        final int sdk_INT = Util.SDK_INT;
        if (sdk_INT >= 18 && (sdk_INT != 18 || (!"OMX.SEC.avc.dec".equals(s) && !"OMX.SEC.avc.dec.secure".equals(s)))) {
            if (Util.SDK_INT == 19 && Util.MODEL.startsWith("SM-G800")) {
                if ("OMX.Exynos.avc.dec".equals(s)) {
                    return true;
                }
                if ("OMX.Exynos.avc.dec.secure".equals(s)) {
                    return true;
                }
            }
            return false;
        }
        return true;
    }
    
    private static boolean codecNeedsMonoChannelCountWorkaround(final String anObject, final Format format) {
        final int sdk_INT = Util.SDK_INT;
        boolean b = true;
        if (sdk_INT > 18 || format.channelCount != 1 || !"OMX.MTK.AUDIO.DECODER.MP3".equals(anObject)) {
            b = false;
        }
        return b;
    }
    
    private static boolean codecNeedsReconfigureWorkaround(final String anObject) {
        return Util.MODEL.startsWith("SM-T230") && "OMX.MARVELL.VIDEO.HW.CODA7542DECODER".equals(anObject);
    }
    
    private boolean deviceNeedsDrmKeysToConfigureCodecWorkaround() {
        return "Amazon".equals(Util.MANUFACTURER) && ("AFTM".equals(Util.MODEL) || "AFTB".equals(Util.MODEL));
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
        }
        else {
            this.releaseCodec();
            this.maybeInitCodec();
        }
    }
    
    private boolean drainOutputBuffer(final long n, final long n2) throws ExoPlaybackException {
        if (!this.hasOutputBuffer()) {
            int outputIndex = 0;
            Label_0077: {
                if (this.codecNeedsEosOutputExceptionWorkaround && this.codecReceivedEos) {
                    try {
                        outputIndex = this.codec.dequeueOutputBuffer(this.outputBufferInfo, this.getDequeueOutputBufferTimeoutUs());
                        break Label_0077;
                    }
                    catch (IllegalStateException ex) {
                        this.processEndOfStream();
                        if (this.outputStreamEnded) {
                            this.releaseCodec();
                        }
                        return false;
                    }
                }
                outputIndex = this.codec.dequeueOutputBuffer(this.outputBufferInfo, this.getDequeueOutputBufferTimeoutUs());
            }
            if (outputIndex < 0) {
                if (outputIndex == -2) {
                    this.processOutputFormat();
                    return true;
                }
                if (outputIndex == -3) {
                    this.processOutputBuffersChanged();
                    return true;
                }
                if (this.codecNeedsEosPropagation && (this.inputStreamEnded || this.codecDrainState == 2)) {
                    this.processEndOfStream();
                }
                return false;
            }
            else {
                if (this.shouldSkipAdaptationWorkaroundOutputBuffer) {
                    this.shouldSkipAdaptationWorkaroundOutputBuffer = false;
                    this.codec.releaseOutputBuffer(outputIndex, false);
                    return true;
                }
                final MediaCodec$BufferInfo outputBufferInfo = this.outputBufferInfo;
                if (outputBufferInfo.size == 0 && (outputBufferInfo.flags & 0x4) != 0x0) {
                    this.processEndOfStream();
                    return false;
                }
                this.outputIndex = outputIndex;
                this.outputBuffer = this.getOutputBuffer(outputIndex);
                final ByteBuffer outputBuffer = this.outputBuffer;
                if (outputBuffer != null) {
                    outputBuffer.position(this.outputBufferInfo.offset);
                    final ByteBuffer outputBuffer2 = this.outputBuffer;
                    final MediaCodec$BufferInfo outputBufferInfo2 = this.outputBufferInfo;
                    outputBuffer2.limit(outputBufferInfo2.offset + outputBufferInfo2.size);
                }
                this.shouldSkipOutputBuffer = this.shouldSkipOutputBuffer(this.outputBufferInfo.presentationTimeUs);
                this.updateOutputFormatForTime(this.outputBufferInfo.presentationTimeUs);
            }
        }
        boolean b = false;
        Label_0420: {
            if (this.codecNeedsEosOutputExceptionWorkaround && this.codecReceivedEos) {
                try {
                    b = this.processOutputBuffer(n, n2, this.codec, this.outputBuffer, this.outputIndex, this.outputBufferInfo.flags, this.outputBufferInfo.presentationTimeUs, this.shouldSkipOutputBuffer, this.outputFormat);
                    break Label_0420;
                }
                catch (IllegalStateException ex2) {
                    this.processEndOfStream();
                    if (this.outputStreamEnded) {
                        this.releaseCodec();
                    }
                    return false;
                }
            }
            final MediaCodec codec = this.codec;
            final ByteBuffer outputBuffer3 = this.outputBuffer;
            final int outputIndex2 = this.outputIndex;
            final MediaCodec$BufferInfo outputBufferInfo3 = this.outputBufferInfo;
            b = this.processOutputBuffer(n, n2, codec, outputBuffer3, outputIndex2, outputBufferInfo3.flags, outputBufferInfo3.presentationTimeUs, this.shouldSkipOutputBuffer, this.outputFormat);
        }
        if (b) {
            this.onProcessedOutputBuffer(this.outputBufferInfo.presentationTimeUs);
            final boolean b2 = (this.outputBufferInfo.flags & 0x4) != 0x0;
            this.resetOutputBuffer();
            if (!b2) {
                return true;
            }
            this.processEndOfStream();
        }
        return false;
    }
    
    private boolean feedInputBuffer() throws ExoPlaybackException {
        final MediaCodec codec = this.codec;
        if (codec != null && this.codecDrainState != 2) {
            if (!this.inputStreamEnded) {
                if (this.inputIndex < 0) {
                    this.inputIndex = codec.dequeueInputBuffer(0L);
                    final int inputIndex = this.inputIndex;
                    if (inputIndex < 0) {
                        return false;
                    }
                    this.buffer.data = this.getInputBuffer(inputIndex);
                    this.buffer.clear();
                }
                if (this.codecDrainState == 1) {
                    if (!this.codecNeedsEosPropagation) {
                        this.codecReceivedEos = true;
                        this.codec.queueInputBuffer(this.inputIndex, 0, 0, 0L, 4);
                        this.resetInputBuffer();
                    }
                    this.codecDrainState = 2;
                    return false;
                }
                if (this.codecNeedsAdaptationWorkaroundBuffer) {
                    this.codecNeedsAdaptationWorkaroundBuffer = false;
                    this.buffer.data.put(MediaCodecRenderer.ADAPTATION_WORKAROUND_BUFFER);
                    this.codec.queueInputBuffer(this.inputIndex, 0, MediaCodecRenderer.ADAPTATION_WORKAROUND_BUFFER.length, 0L, 0);
                    this.resetInputBuffer();
                    return this.codecReceivedBuffers = true;
                }
                int source;
                int position;
                if (this.waitingForKeys) {
                    source = -4;
                    position = 0;
                }
                else {
                    if (this.codecReconfigurationState == 1) {
                        for (int i = 0; i < this.codecFormat.initializationData.size(); ++i) {
                            this.buffer.data.put(this.codecFormat.initializationData.get(i));
                        }
                        this.codecReconfigurationState = 2;
                    }
                    position = this.buffer.data.position();
                    source = this.readSource(this.formatHolder, this.buffer, false);
                }
                if (source == -3) {
                    return false;
                }
                if (source == -5) {
                    if (this.codecReconfigurationState == 2) {
                        this.buffer.clear();
                        this.codecReconfigurationState = 1;
                    }
                    this.onInputFormatChanged(this.formatHolder.format);
                    return true;
                }
                if (this.buffer.isEndOfStream()) {
                    if (this.codecReconfigurationState == 2) {
                        this.buffer.clear();
                        this.codecReconfigurationState = 1;
                    }
                    this.inputStreamEnded = true;
                    if (!this.codecReceivedBuffers) {
                        this.processEndOfStream();
                        return false;
                    }
                    try {
                        if (!this.codecNeedsEosPropagation) {
                            this.codecReceivedEos = true;
                            this.codec.queueInputBuffer(this.inputIndex, 0, 0, 0L, 4);
                            this.resetInputBuffer();
                        }
                        return false;
                    }
                    catch (MediaCodec$CryptoException ex) {
                        throw ExoPlaybackException.createForRenderer((Exception)ex, this.getIndex());
                    }
                }
                if (this.waitingForFirstSyncSample && !this.buffer.isKeyFrame()) {
                    this.buffer.clear();
                    if (this.codecReconfigurationState == 2) {
                        this.codecReconfigurationState = 1;
                    }
                    return true;
                }
                this.waitingForFirstSyncSample = false;
                final boolean encrypted = this.buffer.isEncrypted();
                this.waitingForKeys = this.shouldWaitForKeys(encrypted);
                if (this.waitingForKeys) {
                    return false;
                }
                if (this.codecNeedsDiscardToSpsWorkaround && !encrypted) {
                    NalUnitUtil.discardToSps(this.buffer.data);
                    if (this.buffer.data.position() == 0) {
                        return true;
                    }
                    this.codecNeedsDiscardToSpsWorkaround = false;
                }
                try {
                    final long timeUs = this.buffer.timeUs;
                    if (this.buffer.isDecodeOnly()) {
                        this.decodeOnlyPresentationTimestamps.add(timeUs);
                    }
                    if (this.waitingForFirstSampleInFormat) {
                        this.formatQueue.add(timeUs, this.inputFormat);
                        this.waitingForFirstSampleInFormat = false;
                    }
                    this.buffer.flip();
                    this.onQueueInputBuffer(this.buffer);
                    if (encrypted) {
                        this.codec.queueSecureInputBuffer(this.inputIndex, 0, getFrameworkCryptoInfo(this.buffer, position), timeUs, 0);
                    }
                    else {
                        this.codec.queueInputBuffer(this.inputIndex, 0, this.buffer.data.limit(), timeUs, 0);
                    }
                    this.resetInputBuffer();
                    this.codecReceivedBuffers = true;
                    this.codecReconfigurationState = 0;
                    final DecoderCounters decoderCounters = this.decoderCounters;
                    ++decoderCounters.inputBufferCount;
                    return true;
                }
                catch (MediaCodec$CryptoException ex2) {
                    throw ExoPlaybackException.createForRenderer((Exception)ex2, this.getIndex());
                }
            }
        }
        return false;
    }
    
    private List<MediaCodecInfo> getAvailableCodecInfos(final boolean b) throws MediaCodecUtil.DecoderQueryException {
        List<MediaCodecInfo> list2;
        final List<MediaCodecInfo> list = list2 = this.getDecoderInfos(this.mediaCodecSelector, this.inputFormat, b);
        if (list.isEmpty()) {
            list2 = list;
            if (b) {
                final List<MediaCodecInfo> obj = list2 = this.getDecoderInfos(this.mediaCodecSelector, this.inputFormat, (boolean)(0 != 0));
                if (!obj.isEmpty()) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("Drm session requires secure decoder for ");
                    sb.append(this.inputFormat.sampleMimeType);
                    sb.append(", but no secure decoder available. Trying to proceed with ");
                    sb.append(obj);
                    sb.append(".");
                    Log.w("MediaCodecRenderer", sb.toString());
                    list2 = obj;
                }
            }
        }
        return list2;
    }
    
    private void getCodecBuffers(final MediaCodec mediaCodec) {
        if (Util.SDK_INT < 21) {
            this.inputBuffers = mediaCodec.getInputBuffers();
            this.outputBuffers = mediaCodec.getOutputBuffers();
        }
    }
    
    private static MediaCodec$CryptoInfo getFrameworkCryptoInfo(final DecoderInputBuffer decoderInputBuffer, final int n) {
        final MediaCodec$CryptoInfo frameworkCryptoInfoV16 = decoderInputBuffer.cryptoInfo.getFrameworkCryptoInfoV16();
        if (n == 0) {
            return frameworkCryptoInfoV16;
        }
        if (frameworkCryptoInfoV16.numBytesOfClearData == null) {
            frameworkCryptoInfoV16.numBytesOfClearData = new int[1];
        }
        final int[] numBytesOfClearData = frameworkCryptoInfoV16.numBytesOfClearData;
        numBytesOfClearData[0] += n;
        return frameworkCryptoInfoV16;
    }
    
    private ByteBuffer getInputBuffer(final int n) {
        if (Util.SDK_INT >= 21) {
            return this.codec.getInputBuffer(n);
        }
        return this.inputBuffers[n];
    }
    
    private ByteBuffer getOutputBuffer(final int n) {
        if (Util.SDK_INT >= 21) {
            return this.codec.getOutputBuffer(n);
        }
        return this.outputBuffers[n];
    }
    
    private boolean hasOutputBuffer() {
        return this.outputIndex >= 0;
    }
    
    private void initCodec(final MediaCodecInfo codecInfo, final MediaCrypto mediaCrypto) throws Exception {
        final String name = codecInfo.name;
        float codecOperatingRateV23;
        if (Util.SDK_INT < 23) {
            codecOperatingRateV23 = -1.0f;
        }
        else {
            codecOperatingRateV23 = this.getCodecOperatingRateV23(this.rendererOperatingRate, this.inputFormat, this.getStreamFormats());
        }
        float codecOperatingRate = codecOperatingRateV23;
        if (codecOperatingRateV23 <= this.assumedMinimumCodecOperatingRate) {
            codecOperatingRate = -1.0f;
        }
        MediaCodec byCodecName;
        final MediaCodec mediaCodec = byCodecName = null;
        try {
            final long elapsedRealtime = SystemClock.elapsedRealtime();
            byCodecName = mediaCodec;
            byCodecName = mediaCodec;
            final StringBuilder sb = new StringBuilder();
            byCodecName = mediaCodec;
            sb.append("createCodec:");
            byCodecName = mediaCodec;
            sb.append(name);
            byCodecName = mediaCodec;
            TraceUtil.beginSection(sb.toString());
            byCodecName = mediaCodec;
            final MediaCodec codec = byCodecName = MediaCodec.createByCodecName(name);
            TraceUtil.endSection();
            byCodecName = codec;
            TraceUtil.beginSection("configureCodec");
            byCodecName = codec;
            this.configureCodec(codecInfo, codec, this.inputFormat, mediaCrypto, codecOperatingRate);
            byCodecName = codec;
            TraceUtil.endSection();
            byCodecName = codec;
            TraceUtil.beginSection("startCodec");
            byCodecName = codec;
            codec.start();
            byCodecName = codec;
            TraceUtil.endSection();
            byCodecName = codec;
            final long elapsedRealtime2 = SystemClock.elapsedRealtime();
            byCodecName = codec;
            this.getCodecBuffers(codec);
            this.codec = codec;
            this.codecInfo = codecInfo;
            this.codecOperatingRate = codecOperatingRate;
            this.codecFormat = this.inputFormat;
            this.codecAdaptationWorkaroundMode = this.codecAdaptationWorkaroundMode(name);
            this.codecNeedsReconfigureWorkaround = codecNeedsReconfigureWorkaround(name);
            this.codecNeedsDiscardToSpsWorkaround = codecNeedsDiscardToSpsWorkaround(name, this.codecFormat);
            this.codecNeedsFlushWorkaround = codecNeedsFlushWorkaround(name);
            this.codecNeedsEosFlushWorkaround = codecNeedsEosFlushWorkaround(name);
            this.codecNeedsEosOutputExceptionWorkaround = codecNeedsEosOutputExceptionWorkaround(name);
            this.codecNeedsMonoChannelCountWorkaround = codecNeedsMonoChannelCountWorkaround(name, this.codecFormat);
            this.codecNeedsEosPropagation = (codecNeedsEosPropagationWorkaround(codecInfo) || this.getCodecNeedsEosPropagation());
            this.resetInputBuffer();
            this.resetOutputBuffer();
            long codecHotswapDeadlineMs;
            if (this.getState() == 2) {
                codecHotswapDeadlineMs = SystemClock.elapsedRealtime() + 1000L;
            }
            else {
                codecHotswapDeadlineMs = -9223372036854775807L;
            }
            this.codecHotswapDeadlineMs = codecHotswapDeadlineMs;
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
            final DecoderCounters decoderCounters = this.decoderCounters;
            ++decoderCounters.decoderInitCount;
            this.onCodecInitialized(name, elapsedRealtime2, elapsedRealtime2 - elapsedRealtime);
        }
        catch (Exception ex) {
            if (byCodecName != null) {
                this.resetCodecBuffers();
                byCodecName.release();
            }
            throw ex;
        }
    }
    
    private void maybeInitCodecWithFallback(final MediaCrypto mediaCrypto, final boolean b) throws DecoderInitializationException {
        if (this.availableCodecInfos == null) {
            try {
                this.availableCodecInfos = new ArrayDeque<MediaCodecInfo>(this.getAvailableCodecInfos(b));
                this.preferredDecoderInitializationException = null;
            }
            catch (MediaCodecUtil.DecoderQueryException ex) {
                throw new DecoderInitializationException(this.inputFormat, ex, b, -49998);
            }
        }
        if (!this.availableCodecInfos.isEmpty()) {
            while (this.codec == null) {
                final MediaCodecInfo obj = this.availableCodecInfos.peekFirst();
                if (!this.shouldInitCodec(obj)) {
                    return;
                }
                try {
                    this.initCodec(obj, mediaCrypto);
                    continue;
                }
                catch (Exception ex2) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("Failed to initialize decoder: ");
                    sb.append(obj);
                    Log.w("MediaCodecRenderer", sb.toString(), ex2);
                    this.availableCodecInfos.removeFirst();
                    final DecoderInitializationException preferredDecoderInitializationException = new DecoderInitializationException(this.inputFormat, ex2, b, obj.name);
                    final DecoderInitializationException preferredDecoderInitializationException2 = this.preferredDecoderInitializationException;
                    if (preferredDecoderInitializationException2 == null) {
                        this.preferredDecoderInitializationException = preferredDecoderInitializationException;
                    }
                    else {
                        this.preferredDecoderInitializationException = preferredDecoderInitializationException2.copyWithFallbackException(preferredDecoderInitializationException);
                    }
                    if (!this.availableCodecInfos.isEmpty()) {
                        continue;
                    }
                    throw this.preferredDecoderInitializationException;
                }
                break;
            }
            this.availableCodecInfos = null;
            return;
        }
        throw new DecoderInitializationException(this.inputFormat, null, b, -49999);
    }
    
    private void processEndOfStream() throws ExoPlaybackException {
        final int codecDrainAction = this.codecDrainAction;
        if (codecDrainAction != 1) {
            if (codecDrainAction != 2) {
                this.outputStreamEnded = true;
                this.renderToEndOfStream();
            }
            else {
                this.releaseCodec();
                this.maybeInitCodec();
            }
        }
        else {
            this.flushOrReinitCodec();
        }
    }
    
    private void processOutputBuffersChanged() {
        if (Util.SDK_INT < 21) {
            this.outputBuffers = this.codec.getOutputBuffers();
        }
    }
    
    private void processOutputFormat() throws ExoPlaybackException {
        final MediaFormat outputFormat = this.codec.getOutputFormat();
        if (this.codecAdaptationWorkaroundMode != 0 && outputFormat.getInteger("width") == 32 && outputFormat.getInteger("height") == 32) {
            this.shouldSkipAdaptationWorkaroundOutputBuffer = true;
            return;
        }
        if (this.codecNeedsMonoChannelCountWorkaround) {
            outputFormat.setInteger("channel-count", 1);
        }
        this.onOutputFormatChanged(this.codec, outputFormat);
    }
    
    private void releaseDrmSessionIfUnused(final DrmSession<FrameworkMediaCrypto> drmSession) {
        if (drmSession != null && drmSession != this.sourceDrmSession && drmSession != this.codecDrmSession) {
            this.drmSessionManager.releaseSession(drmSession);
        }
    }
    
    private void resetCodecBuffers() {
        if (Util.SDK_INT < 21) {
            this.inputBuffers = null;
            this.outputBuffers = null;
        }
    }
    
    private void resetInputBuffer() {
        this.inputIndex = -1;
        this.buffer.data = null;
    }
    
    private void resetOutputBuffer() {
        this.outputIndex = -1;
        this.outputBuffer = null;
    }
    
    private void setCodecDrmSession(final DrmSession<FrameworkMediaCrypto> codecDrmSession) {
        final DrmSession<FrameworkMediaCrypto> codecDrmSession2 = this.codecDrmSession;
        this.codecDrmSession = codecDrmSession;
        this.releaseDrmSessionIfUnused(codecDrmSession2);
    }
    
    private void setSourceDrmSession(final DrmSession<FrameworkMediaCrypto> sourceDrmSession) {
        final DrmSession<FrameworkMediaCrypto> sourceDrmSession2 = this.sourceDrmSession;
        this.sourceDrmSession = sourceDrmSession;
        this.releaseDrmSessionIfUnused(sourceDrmSession2);
    }
    
    private boolean shouldContinueFeeding(final long n) {
        return this.renderTimeLimitMs == -9223372036854775807L || SystemClock.elapsedRealtime() - n < this.renderTimeLimitMs;
    }
    
    private boolean shouldSkipOutputBuffer(final long n) {
        for (int size = this.decodeOnlyPresentationTimestamps.size(), i = 0; i < size; ++i) {
            if (this.decodeOnlyPresentationTimestamps.get(i) == n) {
                this.decodeOnlyPresentationTimestamps.remove(i);
                return true;
            }
        }
        return false;
    }
    
    private boolean shouldWaitForKeys(final boolean b) throws ExoPlaybackException {
        if (this.codecDrmSession == null || (!b && this.playClearSamplesWithoutKeys)) {
            return false;
        }
        final int state = this.codecDrmSession.getState();
        boolean b2 = true;
        if (state != 1) {
            if (state == 4) {
                b2 = false;
            }
            return b2;
        }
        throw ExoPlaybackException.createForRenderer(this.codecDrmSession.getError(), this.getIndex());
    }
    
    private void updateCodecOperatingRate() throws ExoPlaybackException {
        if (Util.SDK_INT < 23) {
            return;
        }
        final float codecOperatingRateV23 = this.getCodecOperatingRateV23(this.rendererOperatingRate, this.codecFormat, this.getStreamFormats());
        final float codecOperatingRate = this.codecOperatingRate;
        if (codecOperatingRate != codecOperatingRateV23) {
            if (codecOperatingRateV23 == -1.0f) {
                this.drainAndReinitializeCodec();
            }
            else if (codecOperatingRate != -1.0f || codecOperatingRateV23 > this.assumedMinimumCodecOperatingRate) {
                final Bundle parameters = new Bundle();
                parameters.putFloat("operating-rate", codecOperatingRateV23);
                this.codec.setParameters(parameters);
                this.codecOperatingRate = codecOperatingRateV23;
            }
        }
    }
    
    protected abstract int canKeepCodec(final MediaCodec p0, final MediaCodecInfo p1, final Format p2, final Format p3);
    
    protected abstract void configureCodec(final MediaCodecInfo p0, final MediaCodec p1, final Format p2, final MediaCrypto p3, final float p4) throws MediaCodecUtil.DecoderQueryException;
    
    protected final void flushOrReinitCodec() throws ExoPlaybackException {
        if (this.flushOrReleaseCodec()) {
            this.maybeInitCodec();
        }
    }
    
    protected boolean flushOrReleaseCodec() {
        if (this.codec == null) {
            return false;
        }
        if (this.codecDrainAction != 2 && !this.codecNeedsFlushWorkaround && (!this.codecNeedsEosFlushWorkaround || !this.codecReceivedEos)) {
            this.codec.flush();
            this.resetInputBuffer();
            this.resetOutputBuffer();
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
            this.codecReconfigurationState = (this.codecReconfigured ? 1 : 0);
            return false;
        }
        this.releaseCodec();
        return true;
    }
    
    protected final MediaCodec getCodec() {
        return this.codec;
    }
    
    protected final MediaCodecInfo getCodecInfo() {
        return this.codecInfo;
    }
    
    protected boolean getCodecNeedsEosPropagation() {
        return false;
    }
    
    protected abstract float getCodecOperatingRateV23(final float p0, final Format p1, final Format[] p2);
    
    protected List<MediaCodecInfo> getDecoderInfos(final MediaCodecSelector mediaCodecSelector, final Format format, final boolean b) throws MediaCodecUtil.DecoderQueryException {
        return mediaCodecSelector.getDecoderInfos(format.sampleMimeType, b);
    }
    
    protected long getDequeueOutputBufferTimeoutUs() {
        return 0L;
    }
    
    @Override
    public boolean isEnded() {
        return this.outputStreamEnded;
    }
    
    @Override
    public boolean isReady() {
        return this.inputFormat != null && !this.waitingForKeys && (this.isSourceReady() || this.hasOutputBuffer() || (this.codecHotswapDeadlineMs != -9223372036854775807L && SystemClock.elapsedRealtime() < this.codecHotswapDeadlineMs));
    }
    
    protected final void maybeInitCodec() throws ExoPlaybackException {
        if (this.codec == null) {
            if (this.inputFormat != null) {
                this.setCodecDrmSession(this.sourceDrmSession);
                final String sampleMimeType = this.inputFormat.sampleMimeType;
                final DrmSession<FrameworkMediaCrypto> codecDrmSession = this.codecDrmSession;
                if (codecDrmSession != null) {
                    if (this.mediaCrypto == null) {
                        final FrameworkMediaCrypto frameworkMediaCrypto = codecDrmSession.getMediaCrypto();
                        if (frameworkMediaCrypto == null) {
                            if (this.codecDrmSession.getError() == null) {
                                return;
                            }
                        }
                        else {
                            try {
                                this.mediaCrypto = new MediaCrypto(frameworkMediaCrypto.uuid, frameworkMediaCrypto.sessionId);
                                this.mediaCryptoRequiresSecureDecoder = (!frameworkMediaCrypto.forceAllowInsecureDecoderComponents && this.mediaCrypto.requiresSecureDecoderComponent(sampleMimeType));
                            }
                            catch (MediaCryptoException ex) {
                                throw ExoPlaybackException.createForRenderer((Exception)ex, this.getIndex());
                            }
                        }
                    }
                    if (this.deviceNeedsDrmKeysToConfigureCodecWorkaround()) {
                        final int state = this.codecDrmSession.getState();
                        if (state == 1) {
                            throw ExoPlaybackException.createForRenderer(this.codecDrmSession.getError(), this.getIndex());
                        }
                        if (state != 4) {
                            return;
                        }
                    }
                }
                try {
                    this.maybeInitCodecWithFallback(this.mediaCrypto, this.mediaCryptoRequiresSecureDecoder);
                }
                catch (DecoderInitializationException ex2) {
                    throw ExoPlaybackException.createForRenderer(ex2, this.getIndex());
                }
            }
        }
    }
    
    protected abstract void onCodecInitialized(final String p0, final long p1, final long p2);
    
    @Override
    protected void onDisabled() {
        this.inputFormat = null;
        if (this.sourceDrmSession == null && this.codecDrmSession == null) {
            this.flushOrReleaseCodec();
        }
        else {
            this.onReset();
        }
    }
    
    @Override
    protected void onEnabled(final boolean b) throws ExoPlaybackException {
        this.decoderCounters = new DecoderCounters();
    }
    
    protected void onInputFormatChanged(final Format format) throws ExoPlaybackException {
        final Format inputFormat = this.inputFormat;
        this.inputFormat = format;
        final boolean b = true;
        this.waitingForFirstSampleInFormat = true;
        final DrmInitData drmInitData = format.drmInitData;
        Object drmInitData2;
        if (inputFormat == null) {
            drmInitData2 = null;
        }
        else {
            drmInitData2 = inputFormat.drmInitData;
        }
        if (Util.areEqual(drmInitData, drmInitData2) ^ true) {
            if (format.drmInitData != null) {
                final DrmSessionManager<FrameworkMediaCrypto> drmSessionManager = this.drmSessionManager;
                if (drmSessionManager == null) {
                    throw ExoPlaybackException.createForRenderer(new IllegalStateException("Media requires a DrmSessionManager"), this.getIndex());
                }
                final DrmSession<FrameworkMediaCrypto> acquireSession = drmSessionManager.acquireSession(Looper.myLooper(), format.drmInitData);
                if (acquireSession == this.sourceDrmSession || acquireSession == this.codecDrmSession) {
                    this.drmSessionManager.releaseSession(acquireSession);
                }
                this.setSourceDrmSession(acquireSession);
            }
            else {
                this.setSourceDrmSession(null);
            }
        }
        final MediaCodec codec = this.codec;
        if (codec == null) {
            this.maybeInitCodec();
            return;
        }
        if (this.sourceDrmSession != this.codecDrmSession) {
            this.drainAndReinitializeCodec();
        }
        else {
            final int canKeepCodec = this.canKeepCodec(codec, this.codecInfo, this.codecFormat, format);
            if (canKeepCodec != 0) {
                if (canKeepCodec != 1) {
                    if (canKeepCodec != 2) {
                        if (canKeepCodec != 3) {
                            throw new IllegalStateException();
                        }
                        this.codecFormat = format;
                        this.updateCodecOperatingRate();
                    }
                    else if (this.codecNeedsReconfigureWorkaround) {
                        this.drainAndReinitializeCodec();
                    }
                    else {
                        this.codecReconfigured = true;
                        this.codecReconfigurationState = 1;
                        final int codecAdaptationWorkaroundMode = this.codecAdaptationWorkaroundMode;
                        boolean codecNeedsAdaptationWorkaroundBuffer = b;
                        Label_0311: {
                            if (codecAdaptationWorkaroundMode != 2) {
                                if (codecAdaptationWorkaroundMode == 1) {
                                    final int width = format.width;
                                    final Format codecFormat = this.codecFormat;
                                    if (width == codecFormat.width && format.height == codecFormat.height) {
                                        codecNeedsAdaptationWorkaroundBuffer = b;
                                        break Label_0311;
                                    }
                                }
                                codecNeedsAdaptationWorkaroundBuffer = false;
                            }
                        }
                        this.codecNeedsAdaptationWorkaroundBuffer = codecNeedsAdaptationWorkaroundBuffer;
                        this.codecFormat = format;
                        this.updateCodecOperatingRate();
                    }
                }
                else {
                    this.drainAndFlushCodec();
                    this.codecFormat = format;
                    this.updateCodecOperatingRate();
                }
            }
            else {
                this.drainAndReinitializeCodec();
            }
        }
    }
    
    protected abstract void onOutputFormatChanged(final MediaCodec p0, final MediaFormat p1) throws ExoPlaybackException;
    
    @Override
    protected void onPositionReset(final long n, final boolean b) throws ExoPlaybackException {
        this.inputStreamEnded = false;
        this.outputStreamEnded = false;
        this.flushOrReinitCodec();
        this.formatQueue.clear();
    }
    
    protected abstract void onProcessedOutputBuffer(final long p0);
    
    protected abstract void onQueueInputBuffer(final DecoderInputBuffer p0);
    
    @Override
    protected void onReset() {
        try {
            this.releaseCodec();
        }
        finally {
            this.setSourceDrmSession(null);
        }
    }
    
    @Override
    protected void onStarted() {
    }
    
    @Override
    protected void onStopped() {
    }
    
    protected abstract boolean processOutputBuffer(final long p0, final long p1, final MediaCodec p2, final ByteBuffer p3, final int p4, final int p5, final long p6, final boolean p7, final Format p8) throws ExoPlaybackException;
    
    protected void releaseCodec() {
        this.availableCodecInfos = null;
        this.codecInfo = null;
        this.codecFormat = null;
        this.resetInputBuffer();
        this.resetOutputBuffer();
        this.resetCodecBuffers();
        this.waitingForKeys = false;
        this.codecHotswapDeadlineMs = -9223372036854775807L;
        this.decodeOnlyPresentationTimestamps.clear();
        try {
            if (this.codec != null) {
                final DecoderCounters decoderCounters = this.decoderCounters;
                ++decoderCounters.decoderReleaseCount;
                try {
                    this.codec.stop();
                }
                finally {
                    this.codec.release();
                }
            }
            this.codec = null;
            try {
                if (this.mediaCrypto != null) {
                    this.mediaCrypto.release();
                }
            }
            finally {
                this.mediaCrypto = null;
                this.mediaCryptoRequiresSecureDecoder = false;
                this.setCodecDrmSession(null);
            }
        }
        finally {
            this.codec = null;
            try {
                if (this.mediaCrypto != null) {
                    this.mediaCrypto.release();
                }
                this.mediaCrypto = null;
                this.mediaCryptoRequiresSecureDecoder = false;
                this.setCodecDrmSession(null);
            }
            finally {
                this.mediaCrypto = null;
                this.mediaCryptoRequiresSecureDecoder = false;
                this.setCodecDrmSession(null);
            }
        }
    }
    
    @Override
    public void render(final long n, final long n2) throws ExoPlaybackException {
        if (this.outputStreamEnded) {
            this.renderToEndOfStream();
            return;
        }
        if (this.inputFormat == null) {
            this.flagsOnlyBuffer.clear();
            final int source = this.readSource(this.formatHolder, this.flagsOnlyBuffer, true);
            if (source != -5) {
                if (source == -4) {
                    Assertions.checkState(this.flagsOnlyBuffer.isEndOfStream());
                    this.inputStreamEnded = true;
                    this.processEndOfStream();
                }
                return;
            }
            this.onInputFormatChanged(this.formatHolder.format);
        }
        this.maybeInitCodec();
        if (this.codec != null) {
            final long elapsedRealtime = SystemClock.elapsedRealtime();
            TraceUtil.beginSection("drainAndFeed");
            while (this.drainOutputBuffer(n, n2)) {}
            while (this.feedInputBuffer() && this.shouldContinueFeeding(elapsedRealtime)) {}
            TraceUtil.endSection();
        }
        else {
            final DecoderCounters decoderCounters = this.decoderCounters;
            decoderCounters.skippedInputBufferCount += this.skipSource(n);
            this.flagsOnlyBuffer.clear();
            final int source2 = this.readSource(this.formatHolder, this.flagsOnlyBuffer, false);
            if (source2 == -5) {
                this.onInputFormatChanged(this.formatHolder.format);
            }
            else if (source2 == -4) {
                Assertions.checkState(this.flagsOnlyBuffer.isEndOfStream());
                this.inputStreamEnded = true;
                this.processEndOfStream();
            }
        }
        this.decoderCounters.ensureUpdated();
    }
    
    protected void renderToEndOfStream() throws ExoPlaybackException {
    }
    
    @Override
    public final void setOperatingRate(final float rendererOperatingRate) throws ExoPlaybackException {
        this.rendererOperatingRate = rendererOperatingRate;
        if (this.codec != null && this.codecDrainAction != 2) {
            this.updateCodecOperatingRate();
        }
    }
    
    protected boolean shouldInitCodec(final MediaCodecInfo mediaCodecInfo) {
        return true;
    }
    
    @Override
    public final int supportsFormat(final Format format) throws ExoPlaybackException {
        try {
            return this.supportsFormat(this.mediaCodecSelector, this.drmSessionManager, format);
        }
        catch (MediaCodecUtil.DecoderQueryException ex) {
            throw ExoPlaybackException.createForRenderer(ex, this.getIndex());
        }
    }
    
    protected abstract int supportsFormat(final MediaCodecSelector p0, final DrmSessionManager<FrameworkMediaCrypto> p1, final Format p2) throws MediaCodecUtil.DecoderQueryException;
    
    @Override
    public final int supportsMixedMimeTypeAdaptation() {
        return 8;
    }
    
    protected final Format updateOutputFormatForTime(final long n) {
        final Format outputFormat = this.formatQueue.pollFloor(n);
        if (outputFormat != null) {
            this.outputFormat = outputFormat;
        }
        return outputFormat;
    }
    
    public static class DecoderInitializationException extends Exception
    {
        public final String decoderName;
        public final String diagnosticInfo;
        public final DecoderInitializationException fallbackDecoderInitializationException;
        public final String mimeType;
        public final boolean secureDecoderRequired;
        
        public DecoderInitializationException(final Format obj, final Throwable t, final boolean b, final int i) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Decoder init failed: [");
            sb.append(i);
            sb.append("], ");
            sb.append(obj);
            this(sb.toString(), t, obj.sampleMimeType, b, null, buildCustomDiagnosticInfo(i), null);
        }
        
        public DecoderInitializationException(final Format obj, final Throwable t, final boolean b, final String str) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Decoder init failed: ");
            sb.append(str);
            sb.append(", ");
            sb.append(obj);
            final String string = sb.toString();
            final String sampleMimeType = obj.sampleMimeType;
            String diagnosticInfoV21;
            if (Util.SDK_INT >= 21) {
                diagnosticInfoV21 = getDiagnosticInfoV21(t);
            }
            else {
                diagnosticInfoV21 = null;
            }
            this(string, t, sampleMimeType, b, str, diagnosticInfoV21, null);
        }
        
        private DecoderInitializationException(final String message, final Throwable cause, final String mimeType, final boolean secureDecoderRequired, final String decoderName, final String diagnosticInfo, final DecoderInitializationException fallbackDecoderInitializationException) {
            super(message, cause);
            this.mimeType = mimeType;
            this.secureDecoderRequired = secureDecoderRequired;
            this.decoderName = decoderName;
            this.diagnosticInfo = diagnosticInfo;
            this.fallbackDecoderInitializationException = fallbackDecoderInitializationException;
        }
        
        private static String buildCustomDiagnosticInfo(final int a) {
            String str;
            if (a < 0) {
                str = "neg_";
            }
            else {
                str = "";
            }
            final StringBuilder sb = new StringBuilder();
            sb.append("com.google.android.exoplayer.MediaCodecTrackRenderer_");
            sb.append(str);
            sb.append(Math.abs(a));
            return sb.toString();
        }
        
        private DecoderInitializationException copyWithFallbackException(final DecoderInitializationException ex) {
            return new DecoderInitializationException(this.getMessage(), this.getCause(), this.mimeType, this.secureDecoderRequired, this.decoderName, this.diagnosticInfo, ex);
        }
        
        @TargetApi(21)
        private static String getDiagnosticInfoV21(final Throwable t) {
            if (t instanceof MediaCodec$CodecException) {
                return ((MediaCodec$CodecException)t).getDiagnosticInfo();
            }
            return null;
        }
    }
}
