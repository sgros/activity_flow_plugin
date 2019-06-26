// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.audio;

import java.util.Arrays;
import com.google.android.exoplayer2.util.Log;
import java.nio.ByteOrder;
import android.os.SystemClock;
import android.annotation.TargetApi;
import android.media.AudioFormat;
import android.media.AudioFormat$Builder;
import android.media.AudioAttributes$Builder;
import com.google.android.exoplayer2.util.Util;
import java.util.Collection;
import java.util.Collections;
import java.util.ArrayList;
import com.google.android.exoplayer2.util.Assertions;
import android.os.ConditionVariable;
import java.util.ArrayDeque;
import java.nio.ByteBuffer;
import android.media.AudioTrack;
import com.google.android.exoplayer2.PlaybackParameters;

public final class DefaultAudioSink implements AudioSink
{
    public static boolean enablePreV21AudioSessionWorkaround = false;
    public static boolean failOnSpuriousAudioTimestamp = false;
    private AudioProcessor[] activeAudioProcessors;
    private PlaybackParameters afterDrainPlaybackParameters;
    private AudioAttributes audioAttributes;
    private final AudioCapabilities audioCapabilities;
    private final AudioProcessorChain audioProcessorChain;
    private int audioSessionId;
    private AudioTrack audioTrack;
    private final AudioTrackPositionTracker audioTrackPositionTracker;
    private AuxEffectInfo auxEffectInfo;
    private ByteBuffer avSyncHeader;
    private int bufferSize;
    private int bytesUntilNextAvSync;
    private boolean canApplyPlaybackParameters;
    private final ChannelMappingAudioProcessor channelMappingAudioProcessor;
    private int drainingAudioProcessorIndex;
    private final boolean enableConvertHighResIntPcmToFloat;
    private int framesPerEncodedSample;
    private boolean handledEndOfStream;
    private ByteBuffer inputBuffer;
    private int inputSampleRate;
    private boolean isInputPcm;
    private AudioTrack keepSessionIdAudioTrack;
    private long lastFeedElapsedRealtimeMs;
    private Listener listener;
    private ByteBuffer outputBuffer;
    private ByteBuffer[] outputBuffers;
    private int outputChannelConfig;
    private int outputEncoding;
    private int outputPcmFrameSize;
    private int outputSampleRate;
    private int pcmFrameSize;
    private PlaybackParameters playbackParameters;
    private final ArrayDeque<PlaybackParametersCheckpoint> playbackParametersCheckpoints;
    private long playbackParametersOffsetUs;
    private long playbackParametersPositionUs;
    private boolean playing;
    private byte[] preV21OutputBuffer;
    private int preV21OutputBufferOffset;
    private boolean processingEnabled;
    private final ConditionVariable releasingConditionVariable;
    private boolean shouldConvertHighResIntPcmToFloat;
    private int startMediaTimeState;
    private long startMediaTimeUs;
    private long submittedEncodedFrames;
    private long submittedPcmBytes;
    private final AudioProcessor[] toFloatPcmAvailableAudioProcessors;
    private final AudioProcessor[] toIntPcmAvailableAudioProcessors;
    private final TrimmingAudioProcessor trimmingAudioProcessor;
    private boolean tunneling;
    private float volume;
    private long writtenEncodedFrames;
    private long writtenPcmBytes;
    
    public DefaultAudioSink(final AudioCapabilities audioCapabilities, final AudioProcessorChain audioProcessorChain, final boolean enableConvertHighResIntPcmToFloat) {
        this.audioCapabilities = audioCapabilities;
        Assertions.checkNotNull(audioProcessorChain);
        this.audioProcessorChain = audioProcessorChain;
        this.enableConvertHighResIntPcmToFloat = enableConvertHighResIntPcmToFloat;
        this.releasingConditionVariable = new ConditionVariable(true);
        this.audioTrackPositionTracker = new AudioTrackPositionTracker((AudioTrackPositionTracker.Listener)new PositionTrackerListener());
        this.channelMappingAudioProcessor = new ChannelMappingAudioProcessor();
        this.trimmingAudioProcessor = new TrimmingAudioProcessor();
        final ArrayList<Object> list = new ArrayList<Object>();
        Collections.addAll(list, new ResamplingAudioProcessor(), this.channelMappingAudioProcessor, this.trimmingAudioProcessor);
        Collections.addAll(list, audioProcessorChain.getAudioProcessors());
        this.toIntPcmAvailableAudioProcessors = list.toArray(new AudioProcessor[0]);
        this.toFloatPcmAvailableAudioProcessors = new AudioProcessor[] { new FloatResamplingAudioProcessor() };
        this.volume = 1.0f;
        this.startMediaTimeState = 0;
        this.audioAttributes = AudioAttributes.DEFAULT;
        this.audioSessionId = 0;
        this.auxEffectInfo = new AuxEffectInfo(0, 0.0f);
        this.playbackParameters = PlaybackParameters.DEFAULT;
        this.drainingAudioProcessorIndex = -1;
        this.activeAudioProcessors = new AudioProcessor[0];
        this.outputBuffers = new ByteBuffer[0];
        this.playbackParametersCheckpoints = new ArrayDeque<PlaybackParametersCheckpoint>();
    }
    
    public DefaultAudioSink(final AudioCapabilities audioCapabilities, final AudioProcessor[] array) {
        this(audioCapabilities, array, false);
    }
    
    public DefaultAudioSink(final AudioCapabilities audioCapabilities, final AudioProcessor[] array, final boolean b) {
        this(audioCapabilities, (AudioProcessorChain)new DefaultAudioProcessorChain(array), b);
    }
    
    private long applySkipping(final long n) {
        return n + this.framesToDurationUs(this.audioProcessorChain.getSkippedOutputFrameCount());
    }
    
    private long applySpeedup(long n) {
        PlaybackParametersCheckpoint playbackParametersCheckpoint = null;
        while (!this.playbackParametersCheckpoints.isEmpty() && n >= this.playbackParametersCheckpoints.getFirst().positionUs) {
            playbackParametersCheckpoint = this.playbackParametersCheckpoints.remove();
        }
        if (playbackParametersCheckpoint != null) {
            this.playbackParameters = playbackParametersCheckpoint.playbackParameters;
            this.playbackParametersPositionUs = playbackParametersCheckpoint.positionUs;
            this.playbackParametersOffsetUs = playbackParametersCheckpoint.mediaTimeUs - this.startMediaTimeUs;
        }
        if (this.playbackParameters.speed == 1.0f) {
            return n + this.playbackParametersOffsetUs - this.playbackParametersPositionUs;
        }
        long n2;
        if (this.playbackParametersCheckpoints.isEmpty()) {
            final long playbackParametersOffsetUs = this.playbackParametersOffsetUs;
            n2 = this.audioProcessorChain.getMediaDuration(n - this.playbackParametersPositionUs);
            n = playbackParametersOffsetUs;
        }
        else {
            final long playbackParametersOffsetUs2 = this.playbackParametersOffsetUs;
            n2 = Util.getMediaDurationForPlayoutDuration(n - this.playbackParametersPositionUs, this.playbackParameters.speed);
            n = playbackParametersOffsetUs2;
        }
        return n + n2;
    }
    
    @TargetApi(21)
    private AudioTrack createAudioTrackV21() {
        android.media.AudioAttributes audioAttributes;
        if (this.tunneling) {
            audioAttributes = new AudioAttributes$Builder().setContentType(3).setFlags(16).setUsage(1).build();
        }
        else {
            audioAttributes = this.audioAttributes.getAudioAttributesV21();
        }
        final AudioFormat build = new AudioFormat$Builder().setChannelMask(this.outputChannelConfig).setEncoding(this.outputEncoding).setSampleRate(this.outputSampleRate).build();
        int audioSessionId = this.audioSessionId;
        if (audioSessionId == 0) {
            audioSessionId = 0;
        }
        return new AudioTrack(audioAttributes, build, this.bufferSize, 1, audioSessionId);
    }
    
    private boolean drainAudioProcessorsToEndOfStream() throws WriteException {
        while (true) {
            boolean b = false;
            Label_0038: {
                if (this.drainingAudioProcessorIndex != -1) {
                    b = false;
                    break Label_0038;
                }
                int length;
                if (this.processingEnabled) {
                    length = 0;
                }
                else {
                    length = this.activeAudioProcessors.length;
                }
                this.drainingAudioProcessorIndex = length;
                b = true;
            }
            final int drainingAudioProcessorIndex = this.drainingAudioProcessorIndex;
            final AudioProcessor[] activeAudioProcessors = this.activeAudioProcessors;
            if (drainingAudioProcessorIndex >= activeAudioProcessors.length) {
                final ByteBuffer outputBuffer = this.outputBuffer;
                if (outputBuffer != null) {
                    this.writeBuffer(outputBuffer, -9223372036854775807L);
                    if (this.outputBuffer != null) {
                        return false;
                    }
                }
                this.drainingAudioProcessorIndex = -1;
                return true;
            }
            final AudioProcessor audioProcessor = activeAudioProcessors[drainingAudioProcessorIndex];
            if (b) {
                audioProcessor.queueEndOfStream();
            }
            this.processBuffers(-9223372036854775807L);
            if (!audioProcessor.isEnded()) {
                return false;
            }
            ++this.drainingAudioProcessorIndex;
            continue;
        }
    }
    
    private long durationUsToFrames(final long n) {
        return n * this.outputSampleRate / 1000000L;
    }
    
    private void flushAudioProcessors() {
        int n = 0;
        while (true) {
            final AudioProcessor[] activeAudioProcessors = this.activeAudioProcessors;
            if (n >= activeAudioProcessors.length) {
                break;
            }
            final AudioProcessor audioProcessor = activeAudioProcessors[n];
            audioProcessor.flush();
            this.outputBuffers[n] = audioProcessor.getOutput();
            ++n;
        }
    }
    
    private long framesToDurationUs(final long n) {
        return n * 1000000L / this.outputSampleRate;
    }
    
    private AudioProcessor[] getAvailableAudioProcessors() {
        AudioProcessor[] array;
        if (this.shouldConvertHighResIntPcmToFloat) {
            array = this.toFloatPcmAvailableAudioProcessors;
        }
        else {
            array = this.toIntPcmAvailableAudioProcessors;
        }
        return array;
    }
    
    private static int getChannelConfig(int n, final boolean b) {
        int n2 = n;
        if (Util.SDK_INT <= 28) {
            n2 = n;
            if (!b) {
                if (n == 7) {
                    n2 = 8;
                }
                else if (n == 3 || n == 4 || (n2 = n) == 5) {
                    n2 = 6;
                }
            }
        }
        n = n2;
        if (Util.SDK_INT <= 26) {
            n = n2;
            if ("fugu".equals(Util.DEVICE)) {
                n = n2;
                if (!b && (n = n2) == 1) {
                    n = 2;
                }
            }
        }
        return Util.getAudioTrackChannelConfig(n);
    }
    
    private int getDefaultBufferSize() {
        if (this.isInputPcm) {
            final int minBufferSize = AudioTrack.getMinBufferSize(this.outputSampleRate, this.outputChannelConfig, this.outputEncoding);
            Assertions.checkState(minBufferSize != -2);
            return Util.constrainValue(minBufferSize * 4, (int)this.durationUsToFrames(250000L) * this.outputPcmFrameSize, (int)Math.max(minBufferSize, this.durationUsToFrames(750000L) * this.outputPcmFrameSize));
        }
        int maximumEncodedRateBytesPerSecond = getMaximumEncodedRateBytesPerSecond(this.outputEncoding);
        if (this.outputEncoding == 5) {
            maximumEncodedRateBytesPerSecond *= 2;
        }
        return (int)(maximumEncodedRateBytesPerSecond * 250000L / 1000000L);
    }
    
    private static int getFramesPerEncodedSample(int trueHdSyncframeOffset, final ByteBuffer byteBuffer) {
        if (trueHdSyncframeOffset == 7 || trueHdSyncframeOffset == 8) {
            return DtsUtil.parseDtsAudioSampleCount(byteBuffer);
        }
        if (trueHdSyncframeOffset == 5) {
            return Ac3Util.getAc3SyncframeAudioSampleCount();
        }
        if (trueHdSyncframeOffset == 6) {
            return Ac3Util.parseEAc3SyncframeAudioSampleCount(byteBuffer);
        }
        if (trueHdSyncframeOffset == 14) {
            trueHdSyncframeOffset = Ac3Util.findTrueHdSyncframeOffset(byteBuffer);
            if (trueHdSyncframeOffset == -1) {
                trueHdSyncframeOffset = 0;
            }
            else {
                trueHdSyncframeOffset = Ac3Util.parseTrueHdSyncframeAudioSampleCount(byteBuffer, trueHdSyncframeOffset) * 16;
            }
            return trueHdSyncframeOffset;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Unexpected audio encoding: ");
        sb.append(trueHdSyncframeOffset);
        throw new IllegalStateException(sb.toString());
    }
    
    private static int getMaximumEncodedRateBytesPerSecond(final int n) {
        if (n == 5) {
            return 80000;
        }
        if (n == 6) {
            return 768000;
        }
        if (n == 7) {
            return 192000;
        }
        if (n == 8) {
            return 2250000;
        }
        if (n == 14) {
            return 3062500;
        }
        throw new IllegalArgumentException();
    }
    
    private long getSubmittedFrames() {
        long submittedEncodedFrames;
        if (this.isInputPcm) {
            submittedEncodedFrames = this.submittedPcmBytes / this.pcmFrameSize;
        }
        else {
            submittedEncodedFrames = this.submittedEncodedFrames;
        }
        return submittedEncodedFrames;
    }
    
    private long getWrittenFrames() {
        long writtenEncodedFrames;
        if (this.isInputPcm) {
            writtenEncodedFrames = this.writtenPcmBytes / this.outputPcmFrameSize;
        }
        else {
            writtenEncodedFrames = this.writtenEncodedFrames;
        }
        return writtenEncodedFrames;
    }
    
    private void initialize() throws InitializationException {
        this.releasingConditionVariable.block();
        this.audioTrack = this.initializeAudioTrack();
        final int audioSessionId = this.audioTrack.getAudioSessionId();
        if (DefaultAudioSink.enablePreV21AudioSessionWorkaround && Util.SDK_INT < 21) {
            final AudioTrack keepSessionIdAudioTrack = this.keepSessionIdAudioTrack;
            if (keepSessionIdAudioTrack != null && audioSessionId != keepSessionIdAudioTrack.getAudioSessionId()) {
                this.releaseKeepSessionIdAudioTrack();
            }
            if (this.keepSessionIdAudioTrack == null) {
                this.keepSessionIdAudioTrack = this.initializeKeepSessionIdAudioTrack(audioSessionId);
            }
        }
        if (this.audioSessionId != audioSessionId) {
            this.audioSessionId = audioSessionId;
            final Listener listener = this.listener;
            if (listener != null) {
                listener.onAudioSessionId(audioSessionId);
            }
        }
        PlaybackParameters playbackParameters;
        if (this.canApplyPlaybackParameters) {
            playbackParameters = this.audioProcessorChain.applyPlaybackParameters(this.playbackParameters);
        }
        else {
            playbackParameters = PlaybackParameters.DEFAULT;
        }
        this.playbackParameters = playbackParameters;
        this.setupAudioProcessors();
        this.audioTrackPositionTracker.setAudioTrack(this.audioTrack, this.outputEncoding, this.outputPcmFrameSize, this.bufferSize);
        this.setVolumeInternal();
        final int effectId = this.auxEffectInfo.effectId;
        if (effectId != 0) {
            this.audioTrack.attachAuxEffect(effectId);
            this.audioTrack.setAuxEffectSendLevel(this.auxEffectInfo.sendLevel);
        }
    }
    
    private AudioTrack initializeAudioTrack() throws InitializationException {
        AudioTrack audioTrackV21;
        if (Util.SDK_INT >= 21) {
            audioTrackV21 = this.createAudioTrackV21();
        }
        else {
            final int streamTypeForAudioUsage = Util.getStreamTypeForAudioUsage(this.audioAttributes.usage);
            final int audioSessionId = this.audioSessionId;
            if (audioSessionId == 0) {
                audioTrackV21 = new AudioTrack(streamTypeForAudioUsage, this.outputSampleRate, this.outputChannelConfig, this.outputEncoding, this.bufferSize, 1);
            }
            else {
                audioTrackV21 = new AudioTrack(streamTypeForAudioUsage, this.outputSampleRate, this.outputChannelConfig, this.outputEncoding, this.bufferSize, 1, audioSessionId);
            }
        }
        final int state = audioTrackV21.getState();
        if (state == 1) {
            return audioTrackV21;
        }
        try {
            audioTrackV21.release();
            throw new InitializationException(state, this.outputSampleRate, this.outputChannelConfig, this.bufferSize);
        }
        catch (Exception ex) {
            throw new InitializationException(state, this.outputSampleRate, this.outputChannelConfig, this.bufferSize);
        }
    }
    
    private AudioTrack initializeKeepSessionIdAudioTrack(final int n) {
        return new AudioTrack(3, 4000, 4, 2, 2, 0, n);
    }
    
    private long inputFramesToDurationUs(final long n) {
        return n * 1000000L / this.inputSampleRate;
    }
    
    private boolean isInitialized() {
        return this.audioTrack != null;
    }
    
    private void processBuffers(final long n) throws WriteException {
        int i;
        final int n2 = i = this.activeAudioProcessors.length;
        while (i >= 0) {
            ByteBuffer byteBuffer;
            if (i > 0) {
                byteBuffer = this.outputBuffers[i - 1];
            }
            else {
                byteBuffer = this.inputBuffer;
                if (byteBuffer == null) {
                    byteBuffer = AudioProcessor.EMPTY_BUFFER;
                }
            }
            if (i == n2) {
                this.writeBuffer(byteBuffer, n);
            }
            else {
                final AudioProcessor audioProcessor = this.activeAudioProcessors[i];
                audioProcessor.queueInput(byteBuffer);
                final ByteBuffer output = audioProcessor.getOutput();
                this.outputBuffers[i] = output;
                if (output.hasRemaining()) {
                    ++i;
                    continue;
                }
            }
            if (byteBuffer.hasRemaining()) {
                return;
            }
            --i;
        }
    }
    
    private void releaseKeepSessionIdAudioTrack() {
        final AudioTrack keepSessionIdAudioTrack = this.keepSessionIdAudioTrack;
        if (keepSessionIdAudioTrack == null) {
            return;
        }
        this.keepSessionIdAudioTrack = null;
        new Thread() {
            @Override
            public void run() {
                keepSessionIdAudioTrack.release();
            }
        }.start();
    }
    
    private void setVolumeInternal() {
        if (this.isInitialized()) {
            if (Util.SDK_INT >= 21) {
                setVolumeInternalV21(this.audioTrack, this.volume);
            }
            else {
                setVolumeInternalV3(this.audioTrack, this.volume);
            }
        }
    }
    
    @TargetApi(21)
    private static void setVolumeInternalV21(final AudioTrack audioTrack, final float volume) {
        audioTrack.setVolume(volume);
    }
    
    private static void setVolumeInternalV3(final AudioTrack audioTrack, final float n) {
        audioTrack.setStereoVolume(n, n);
    }
    
    private void setupAudioProcessors() {
        final ArrayList<AudioProcessor> list = new ArrayList<AudioProcessor>();
        for (final AudioProcessor e : this.getAvailableAudioProcessors()) {
            if (e.isActive()) {
                list.add(e);
            }
            else {
                e.flush();
            }
        }
        final int size = list.size();
        this.activeAudioProcessors = list.toArray(new AudioProcessor[size]);
        this.outputBuffers = new ByteBuffer[size];
        this.flushAudioProcessors();
    }
    
    private void writeBuffer(final ByteBuffer outputBuffer, final long n) throws WriteException {
        if (!outputBuffer.hasRemaining()) {
            return;
        }
        final ByteBuffer outputBuffer2 = this.outputBuffer;
        final boolean b = true;
        int n2 = 0;
        if (outputBuffer2 != null) {
            Assertions.checkArgument(outputBuffer2 == outputBuffer);
        }
        else {
            this.outputBuffer = outputBuffer;
            if (Util.SDK_INT < 21) {
                final int remaining = outputBuffer.remaining();
                final byte[] preV21OutputBuffer = this.preV21OutputBuffer;
                if (preV21OutputBuffer == null || preV21OutputBuffer.length < remaining) {
                    this.preV21OutputBuffer = new byte[remaining];
                }
                final int position = outputBuffer.position();
                outputBuffer.get(this.preV21OutputBuffer, 0, remaining);
                outputBuffer.position(position);
                this.preV21OutputBufferOffset = 0;
            }
        }
        final int remaining2 = outputBuffer.remaining();
        if (Util.SDK_INT < 21) {
            final int availableBufferSize = this.audioTrackPositionTracker.getAvailableBufferSize(this.writtenPcmBytes);
            if (availableBufferSize > 0) {
                final int write = this.audioTrack.write(this.preV21OutputBuffer, this.preV21OutputBufferOffset, Math.min(remaining2, availableBufferSize));
                if ((n2 = write) > 0) {
                    this.preV21OutputBufferOffset += write;
                    outputBuffer.position(outputBuffer.position() + write);
                    n2 = write;
                }
            }
        }
        else if (this.tunneling) {
            Assertions.checkState(n != -9223372036854775807L && b);
            n2 = this.writeNonBlockingWithAvSyncV21(this.audioTrack, outputBuffer, remaining2, n);
        }
        else {
            n2 = writeNonBlockingV21(this.audioTrack, outputBuffer, remaining2);
        }
        this.lastFeedElapsedRealtimeMs = SystemClock.elapsedRealtime();
        if (n2 >= 0) {
            if (this.isInputPcm) {
                this.writtenPcmBytes += n2;
            }
            if (n2 == remaining2) {
                if (!this.isInputPcm) {
                    this.writtenEncodedFrames += this.framesPerEncodedSample;
                }
                this.outputBuffer = null;
            }
            return;
        }
        throw new WriteException(n2);
    }
    
    @TargetApi(21)
    private static int writeNonBlockingV21(final AudioTrack audioTrack, final ByteBuffer byteBuffer, final int n) {
        return audioTrack.write(byteBuffer, n, 1);
    }
    
    @TargetApi(21)
    private int writeNonBlockingWithAvSyncV21(final AudioTrack audioTrack, final ByteBuffer byteBuffer, int writeNonBlockingV21, final long n) {
        if (this.avSyncHeader == null) {
            (this.avSyncHeader = ByteBuffer.allocate(16)).order(ByteOrder.BIG_ENDIAN);
            this.avSyncHeader.putInt(1431633921);
        }
        if (this.bytesUntilNextAvSync == 0) {
            this.avSyncHeader.putInt(4, writeNonBlockingV21);
            this.avSyncHeader.putLong(8, n * 1000L);
            this.avSyncHeader.position(0);
            this.bytesUntilNextAvSync = writeNonBlockingV21;
        }
        final int remaining = this.avSyncHeader.remaining();
        if (remaining > 0) {
            final int write = audioTrack.write(this.avSyncHeader, remaining, 1);
            if (write < 0) {
                this.bytesUntilNextAvSync = 0;
                return write;
            }
            if (write < remaining) {
                return 0;
            }
        }
        writeNonBlockingV21 = writeNonBlockingV21(audioTrack, byteBuffer, writeNonBlockingV21);
        if (writeNonBlockingV21 < 0) {
            this.bytesUntilNextAvSync = 0;
            return writeNonBlockingV21;
        }
        this.bytesUntilNextAvSync -= writeNonBlockingV21;
        return writeNonBlockingV21;
    }
    
    @Override
    public void configure(int n, int outputChannelCount, int outputEncoding, int defaultBufferSize, int[] array, int n2, int n3) throws ConfigurationException {
        this.inputSampleRate = outputEncoding;
        this.isInputPcm = Util.isEncodingLinearPcm(n);
        final boolean enableConvertHighResIntPcmToFloat = this.enableConvertHighResIntPcmToFloat;
        boolean canApplyPlaybackParameters = true;
        final int n4 = 0;
        this.shouldConvertHighResIntPcmToFloat = (enableConvertHighResIntPcmToFloat && this.supportsOutput(outputChannelCount, 1073741824) && Util.isEncodingHighResolutionIntegerPcm(n));
        if (this.isInputPcm) {
            this.pcmFrameSize = Util.getPcmFrameSize(n, outputChannelCount);
        }
        final boolean processingEnabled = this.isInputPcm && n != 4;
        if (!processingEnabled || this.shouldConvertHighResIntPcmToFloat) {
            canApplyPlaybackParameters = false;
        }
        this.canApplyPlaybackParameters = canApplyPlaybackParameters;
        int[] channelMap = array;
        if (Util.SDK_INT < 21) {
            channelMap = array;
            if (outputChannelCount == 8 && (channelMap = array) == null) {
                array = new int[6];
                int n5 = 0;
                while (true) {
                    channelMap = array;
                    if (n5 >= array.length) {
                        break;
                    }
                    array[n5] = n5;
                    ++n5;
                }
            }
        }
        int n6 = 0;
        int i = 0;
        int outputEncoding2 = 0;
        int outputSampleRate = 0;
        Label_0354: {
            if (processingEnabled) {
                this.trimmingAudioProcessor.setTrimFrameCount(n2, n3);
                this.channelMappingAudioProcessor.setChannelMap(channelMap);
                final AudioProcessor[] availableAudioProcessors = this.getAvailableAudioProcessors();
                final int length = availableAudioProcessors.length;
                n2 = outputEncoding;
                outputEncoding = n;
                n3 = 0;
                n = n2;
                n2 = n3;
                n3 = n4;
                while (true) {
                    n6 = n2;
                    i = outputChannelCount;
                    outputEncoding2 = outputEncoding;
                    outputSampleRate = n;
                    if (n3 < length) {
                        final AudioProcessor audioProcessor = availableAudioProcessors[n3];
                        try {
                            n2 |= (audioProcessor.configure(n, outputChannelCount, outputEncoding) ? 1 : 0);
                            if (audioProcessor.isActive()) {
                                outputChannelCount = audioProcessor.getOutputChannelCount();
                                n = audioProcessor.getOutputSampleRateHz();
                                outputEncoding = audioProcessor.getOutputEncoding();
                            }
                            ++n3;
                            continue;
                        }
                        catch (AudioProcessor.UnhandledFormatException ex) {
                            throw new ConfigurationException(ex);
                        }
                        break;
                    }
                    break Label_0354;
                }
            }
            n6 = 0;
            outputSampleRate = outputEncoding;
            outputEncoding2 = n;
            i = outputChannelCount;
        }
        n = getChannelConfig(i, this.isInputPcm);
        if (n == 0) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Unsupported channel count: ");
            sb.append(i);
            throw new ConfigurationException(sb.toString());
        }
        if (n6 == 0 && this.isInitialized() && this.outputEncoding == outputEncoding2 && this.outputSampleRate == outputSampleRate && this.outputChannelConfig == n) {
            return;
        }
        this.flush();
        this.processingEnabled = processingEnabled;
        this.outputSampleRate = outputSampleRate;
        this.outputChannelConfig = n;
        this.outputEncoding = outputEncoding2;
        if (this.isInputPcm) {
            n = Util.getPcmFrameSize(this.outputEncoding, i);
        }
        else {
            n = -1;
        }
        this.outputPcmFrameSize = n;
        if (defaultBufferSize == 0) {
            defaultBufferSize = this.getDefaultBufferSize();
        }
        this.bufferSize = defaultBufferSize;
    }
    
    @Override
    public void disableTunneling() {
        if (this.tunneling) {
            this.tunneling = false;
            this.audioSessionId = 0;
            this.flush();
        }
    }
    
    @Override
    public void enableTunnelingV21(final int audioSessionId) {
        Assertions.checkState(Util.SDK_INT >= 21);
        if (!this.tunneling || this.audioSessionId != audioSessionId) {
            this.tunneling = true;
            this.audioSessionId = audioSessionId;
            this.flush();
        }
    }
    
    @Override
    public void flush() {
        if (this.isInitialized()) {
            this.submittedPcmBytes = 0L;
            this.submittedEncodedFrames = 0L;
            this.writtenPcmBytes = 0L;
            this.writtenEncodedFrames = 0L;
            this.framesPerEncodedSample = 0;
            final PlaybackParameters afterDrainPlaybackParameters = this.afterDrainPlaybackParameters;
            if (afterDrainPlaybackParameters != null) {
                this.playbackParameters = afterDrainPlaybackParameters;
                this.afterDrainPlaybackParameters = null;
            }
            else if (!this.playbackParametersCheckpoints.isEmpty()) {
                this.playbackParameters = this.playbackParametersCheckpoints.getLast().playbackParameters;
            }
            this.playbackParametersCheckpoints.clear();
            this.playbackParametersOffsetUs = 0L;
            this.playbackParametersPositionUs = 0L;
            this.trimmingAudioProcessor.resetTrimmedFrameCount();
            this.inputBuffer = null;
            this.outputBuffer = null;
            this.flushAudioProcessors();
            this.handledEndOfStream = false;
            this.drainingAudioProcessorIndex = -1;
            this.avSyncHeader = null;
            this.bytesUntilNextAvSync = 0;
            this.startMediaTimeState = 0;
            if (this.audioTrackPositionTracker.isPlaying()) {
                this.audioTrack.pause();
            }
            final AudioTrack audioTrack = this.audioTrack;
            this.audioTrack = null;
            this.audioTrackPositionTracker.reset();
            this.releasingConditionVariable.close();
            new Thread() {
                @Override
                public void run() {
                    try {
                        audioTrack.flush();
                        audioTrack.release();
                    }
                    finally {
                        DefaultAudioSink.this.releasingConditionVariable.open();
                    }
                }
            }.start();
        }
    }
    
    @Override
    public long getCurrentPositionUs(final boolean b) {
        if (this.isInitialized() && this.startMediaTimeState != 0) {
            return this.startMediaTimeUs + this.applySkipping(this.applySpeedup(Math.min(this.audioTrackPositionTracker.getCurrentPositionUs(b), this.framesToDurationUs(this.getWrittenFrames()))));
        }
        return Long.MIN_VALUE;
    }
    
    @Override
    public PlaybackParameters getPlaybackParameters() {
        return this.playbackParameters;
    }
    
    @Override
    public boolean handleBuffer(final ByteBuffer inputBuffer, final long lng) throws InitializationException, WriteException {
        final ByteBuffer inputBuffer2 = this.inputBuffer;
        Assertions.checkArgument(inputBuffer2 == null || inputBuffer == inputBuffer2);
        if (!this.isInitialized()) {
            this.initialize();
            if (this.playing) {
                this.play();
            }
        }
        if (!this.audioTrackPositionTracker.mayHandleBuffer(this.getWrittenFrames())) {
            return false;
        }
        if (this.inputBuffer == null) {
            if (!inputBuffer.hasRemaining()) {
                return true;
            }
            if (!this.isInputPcm && this.framesPerEncodedSample == 0) {
                this.framesPerEncodedSample = getFramesPerEncodedSample(this.outputEncoding, inputBuffer);
                if (this.framesPerEncodedSample == 0) {
                    return true;
                }
            }
            if (this.afterDrainPlaybackParameters != null) {
                if (!this.drainAudioProcessorsToEndOfStream()) {
                    return false;
                }
                final PlaybackParameters afterDrainPlaybackParameters = this.afterDrainPlaybackParameters;
                this.afterDrainPlaybackParameters = null;
                this.playbackParametersCheckpoints.add(new PlaybackParametersCheckpoint(this.audioProcessorChain.applyPlaybackParameters(afterDrainPlaybackParameters), Math.max(0L, lng), this.framesToDurationUs(this.getWrittenFrames())));
                this.setupAudioProcessors();
            }
            if (this.startMediaTimeState == 0) {
                this.startMediaTimeUs = Math.max(0L, lng);
                this.startMediaTimeState = 1;
            }
            else {
                final long lng2 = this.startMediaTimeUs + this.inputFramesToDurationUs(this.getSubmittedFrames() - this.trimmingAudioProcessor.getTrimmedFrameCount());
                if (this.startMediaTimeState == 1 && Math.abs(lng2 - lng) > 200000L) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("Discontinuity detected [expected ");
                    sb.append(lng2);
                    sb.append(", got ");
                    sb.append(lng);
                    sb.append("]");
                    Log.e("AudioTrack", sb.toString());
                    this.startMediaTimeState = 2;
                }
                if (this.startMediaTimeState == 2) {
                    final long n = lng - lng2;
                    this.startMediaTimeUs += n;
                    this.startMediaTimeState = 1;
                    final Listener listener = this.listener;
                    if (listener != null && n != 0L) {
                        listener.onPositionDiscontinuity();
                    }
                }
            }
            if (this.isInputPcm) {
                this.submittedPcmBytes += inputBuffer.remaining();
            }
            else {
                this.submittedEncodedFrames += this.framesPerEncodedSample;
            }
            this.inputBuffer = inputBuffer;
        }
        if (this.processingEnabled) {
            this.processBuffers(lng);
        }
        else {
            this.writeBuffer(this.inputBuffer, lng);
        }
        if (!this.inputBuffer.hasRemaining()) {
            this.inputBuffer = null;
            return true;
        }
        if (this.audioTrackPositionTracker.isStalled(this.getWrittenFrames())) {
            Log.w("AudioTrack", "Resetting stalled audio track");
            this.flush();
            return true;
        }
        return false;
    }
    
    @Override
    public void handleDiscontinuity() {
        if (this.startMediaTimeState == 1) {
            this.startMediaTimeState = 2;
        }
    }
    
    @Override
    public boolean hasPendingData() {
        return this.isInitialized() && this.audioTrackPositionTracker.hasPendingData(this.getWrittenFrames());
    }
    
    @Override
    public boolean isEnded() {
        return !this.isInitialized() || (this.handledEndOfStream && !this.hasPendingData());
    }
    
    @Override
    public void pause() {
        this.playing = false;
        if (this.isInitialized() && this.audioTrackPositionTracker.pause()) {
            this.audioTrack.pause();
        }
    }
    
    @Override
    public void play() {
        this.playing = true;
        if (this.isInitialized()) {
            this.audioTrackPositionTracker.start();
            this.audioTrack.play();
        }
    }
    
    @Override
    public void playToEndOfStream() throws WriteException {
        if (!this.handledEndOfStream) {
            if (this.isInitialized()) {
                if (this.drainAudioProcessorsToEndOfStream()) {
                    this.audioTrackPositionTracker.handleEndOfStream(this.getWrittenFrames());
                    this.audioTrack.stop();
                    this.bytesUntilNextAvSync = 0;
                    this.handledEndOfStream = true;
                }
            }
        }
    }
    
    @Override
    public void reset() {
        this.flush();
        this.releaseKeepSessionIdAudioTrack();
        final AudioProcessor[] toIntPcmAvailableAudioProcessors = this.toIntPcmAvailableAudioProcessors;
        for (int length = toIntPcmAvailableAudioProcessors.length, i = 0; i < length; ++i) {
            toIntPcmAvailableAudioProcessors[i].reset();
        }
        final AudioProcessor[] toFloatPcmAvailableAudioProcessors = this.toFloatPcmAvailableAudioProcessors;
        for (int length2 = toFloatPcmAvailableAudioProcessors.length, j = 0; j < length2; ++j) {
            toFloatPcmAvailableAudioProcessors[j].reset();
        }
        this.audioSessionId = 0;
        this.playing = false;
    }
    
    @Override
    public void setAudioAttributes(final AudioAttributes audioAttributes) {
        if (this.audioAttributes.equals(audioAttributes)) {
            return;
        }
        this.audioAttributes = audioAttributes;
        if (this.tunneling) {
            return;
        }
        this.flush();
        this.audioSessionId = 0;
    }
    
    @Override
    public void setAuxEffectInfo(final AuxEffectInfo auxEffectInfo) {
        if (this.auxEffectInfo.equals(auxEffectInfo)) {
            return;
        }
        final int effectId = auxEffectInfo.effectId;
        final float sendLevel = auxEffectInfo.sendLevel;
        final AudioTrack audioTrack = this.audioTrack;
        if (audioTrack != null) {
            if (this.auxEffectInfo.effectId != effectId) {
                audioTrack.attachAuxEffect(effectId);
            }
            if (effectId != 0) {
                this.audioTrack.setAuxEffectSendLevel(sendLevel);
            }
        }
        this.auxEffectInfo = auxEffectInfo;
    }
    
    @Override
    public void setListener(final Listener listener) {
        this.listener = listener;
    }
    
    @Override
    public PlaybackParameters setPlaybackParameters(final PlaybackParameters afterDrainPlaybackParameters) {
        if (this.isInitialized() && !this.canApplyPlaybackParameters) {
            return this.playbackParameters = PlaybackParameters.DEFAULT;
        }
        PlaybackParameters playbackParameters = this.afterDrainPlaybackParameters;
        if (playbackParameters == null) {
            if (!this.playbackParametersCheckpoints.isEmpty()) {
                playbackParameters = this.playbackParametersCheckpoints.getLast().playbackParameters;
            }
            else {
                playbackParameters = this.playbackParameters;
            }
        }
        if (!afterDrainPlaybackParameters.equals(playbackParameters)) {
            if (this.isInitialized()) {
                this.afterDrainPlaybackParameters = afterDrainPlaybackParameters;
            }
            else {
                this.playbackParameters = this.audioProcessorChain.applyPlaybackParameters(afterDrainPlaybackParameters);
            }
        }
        return this.playbackParameters;
    }
    
    @Override
    public void setVolume(final float volume) {
        if (this.volume != volume) {
            this.volume = volume;
            this.setVolumeInternal();
        }
    }
    
    @Override
    public boolean supportsOutput(final int n, final int n2) {
        final boolean encodingLinearPcm = Util.isEncodingLinearPcm(n2);
        final boolean b = true;
        final boolean b2 = true;
        if (encodingLinearPcm) {
            boolean b3 = b2;
            if (n2 == 4) {
                b3 = (Util.SDK_INT >= 21 && b2);
            }
            return b3;
        }
        final AudioCapabilities audioCapabilities = this.audioCapabilities;
        if (audioCapabilities != null && audioCapabilities.supportsEncoding(n2)) {
            boolean b4 = b;
            if (n == -1) {
                return b4;
            }
            if (n <= this.audioCapabilities.getMaxChannelCount()) {
                b4 = b;
                return b4;
            }
        }
        return false;
    }
    
    public interface AudioProcessorChain
    {
        PlaybackParameters applyPlaybackParameters(final PlaybackParameters p0);
        
        AudioProcessor[] getAudioProcessors();
        
        long getMediaDuration(final long p0);
        
        long getSkippedOutputFrameCount();
    }
    
    public static class DefaultAudioProcessorChain implements AudioProcessorChain
    {
        private final AudioProcessor[] audioProcessors;
        private final SilenceSkippingAudioProcessor silenceSkippingAudioProcessor;
        private final SonicAudioProcessor sonicAudioProcessor;
        
        public DefaultAudioProcessorChain(final AudioProcessor... original) {
            this.audioProcessors = Arrays.copyOf(original, original.length + 2);
            this.silenceSkippingAudioProcessor = new SilenceSkippingAudioProcessor();
            this.sonicAudioProcessor = new SonicAudioProcessor();
            final AudioProcessor[] audioProcessors = this.audioProcessors;
            audioProcessors[original.length] = this.silenceSkippingAudioProcessor;
            audioProcessors[original.length + 1] = this.sonicAudioProcessor;
        }
        
        @Override
        public PlaybackParameters applyPlaybackParameters(final PlaybackParameters playbackParameters) {
            this.silenceSkippingAudioProcessor.setEnabled(playbackParameters.skipSilence);
            return new PlaybackParameters(this.sonicAudioProcessor.setSpeed(playbackParameters.speed), this.sonicAudioProcessor.setPitch(playbackParameters.pitch), playbackParameters.skipSilence);
        }
        
        @Override
        public AudioProcessor[] getAudioProcessors() {
            return this.audioProcessors;
        }
        
        @Override
        public long getMediaDuration(final long n) {
            return this.sonicAudioProcessor.scaleDurationForSpeedup(n);
        }
        
        @Override
        public long getSkippedOutputFrameCount() {
            return this.silenceSkippingAudioProcessor.getSkippedFrames();
        }
    }
    
    public static final class InvalidAudioTrackTimestampException extends RuntimeException
    {
        private InvalidAudioTrackTimestampException(final String message) {
            super(message);
        }
    }
    
    private static final class PlaybackParametersCheckpoint
    {
        private final long mediaTimeUs;
        private final PlaybackParameters playbackParameters;
        private final long positionUs;
        
        private PlaybackParametersCheckpoint(final PlaybackParameters playbackParameters, final long mediaTimeUs, final long positionUs) {
            this.playbackParameters = playbackParameters;
            this.mediaTimeUs = mediaTimeUs;
            this.positionUs = positionUs;
        }
    }
    
    private final class PositionTrackerListener implements AudioTrackPositionTracker.Listener
    {
        @Override
        public void onInvalidLatency(final long lng) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Ignoring impossibly large audio latency: ");
            sb.append(lng);
            Log.w("AudioTrack", sb.toString());
        }
        
        @Override
        public void onPositionFramesMismatch(final long lng, final long lng2, final long lng3, final long lng4) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Spurious audio timestamp (frame position mismatch): ");
            sb.append(lng);
            sb.append(", ");
            sb.append(lng2);
            sb.append(", ");
            sb.append(lng3);
            sb.append(", ");
            sb.append(lng4);
            sb.append(", ");
            sb.append(DefaultAudioSink.this.getSubmittedFrames());
            sb.append(", ");
            sb.append(DefaultAudioSink.this.getWrittenFrames());
            final String string = sb.toString();
            if (!DefaultAudioSink.failOnSpuriousAudioTimestamp) {
                Log.w("AudioTrack", string);
                return;
            }
            throw new InvalidAudioTrackTimestampException(string);
        }
        
        @Override
        public void onSystemTimeUsMismatch(final long lng, final long lng2, final long lng3, final long lng4) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Spurious audio timestamp (system clock mismatch): ");
            sb.append(lng);
            sb.append(", ");
            sb.append(lng2);
            sb.append(", ");
            sb.append(lng3);
            sb.append(", ");
            sb.append(lng4);
            sb.append(", ");
            sb.append(DefaultAudioSink.this.getSubmittedFrames());
            sb.append(", ");
            sb.append(DefaultAudioSink.this.getWrittenFrames());
            final String string = sb.toString();
            if (!DefaultAudioSink.failOnSpuriousAudioTimestamp) {
                Log.w("AudioTrack", string);
                return;
            }
            throw new InvalidAudioTrackTimestampException(string);
        }
        
        @Override
        public void onUnderrun(final int n, final long n2) {
            if (DefaultAudioSink.this.listener != null) {
                DefaultAudioSink.this.listener.onUnderrun(n, n2, SystemClock.elapsedRealtime() - DefaultAudioSink.this.lastFeedElapsedRealtimeMs);
            }
        }
    }
}
