package com.google.android.exoplayer2.audio;

import android.annotation.TargetApi;
import android.media.AudioAttributes;
import android.media.AudioAttributes.Builder;
import android.media.AudioFormat;
import android.media.AudioTrack;
import android.os.ConditionVariable;
import android.os.SystemClock;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.audio.AudioSink.InitializationException;
import com.google.android.exoplayer2.audio.AudioSink.Listener;
import com.google.android.exoplayer2.audio.AudioSink.WriteException;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.util.Log;
import com.google.android.exoplayer2.util.Util;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public final class DefaultAudioSink implements AudioSink {
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

    public interface AudioProcessorChain {
        PlaybackParameters applyPlaybackParameters(PlaybackParameters playbackParameters);

        AudioProcessor[] getAudioProcessors();

        long getMediaDuration(long j);

        long getSkippedOutputFrameCount();
    }

    public static final class InvalidAudioTrackTimestampException extends RuntimeException {
        /* synthetic */ InvalidAudioTrackTimestampException(String str, C01491 c01491) {
            this(str);
        }

        private InvalidAudioTrackTimestampException(String str) {
            super(str);
        }
    }

    private static final class PlaybackParametersCheckpoint {
        private final long mediaTimeUs;
        private final PlaybackParameters playbackParameters;
        private final long positionUs;

        /* synthetic */ PlaybackParametersCheckpoint(PlaybackParameters playbackParameters, long j, long j2, C01491 c01491) {
            this(playbackParameters, j, j2);
        }

        private PlaybackParametersCheckpoint(PlaybackParameters playbackParameters, long j, long j2) {
            this.playbackParameters = playbackParameters;
            this.mediaTimeUs = j;
            this.positionUs = j2;
        }
    }

    public static class DefaultAudioProcessorChain implements AudioProcessorChain {
        private final AudioProcessor[] audioProcessors;
        private final SilenceSkippingAudioProcessor silenceSkippingAudioProcessor = new SilenceSkippingAudioProcessor();
        private final SonicAudioProcessor sonicAudioProcessor = new SonicAudioProcessor();

        public DefaultAudioProcessorChain(AudioProcessor... audioProcessorArr) {
            this.audioProcessors = (AudioProcessor[]) Arrays.copyOf(audioProcessorArr, audioProcessorArr.length + 2);
            AudioProcessor[] audioProcessorArr2 = this.audioProcessors;
            audioProcessorArr2[audioProcessorArr.length] = this.silenceSkippingAudioProcessor;
            audioProcessorArr2[audioProcessorArr.length + 1] = this.sonicAudioProcessor;
        }

        public AudioProcessor[] getAudioProcessors() {
            return this.audioProcessors;
        }

        public PlaybackParameters applyPlaybackParameters(PlaybackParameters playbackParameters) {
            this.silenceSkippingAudioProcessor.setEnabled(playbackParameters.skipSilence);
            return new PlaybackParameters(this.sonicAudioProcessor.setSpeed(playbackParameters.speed), this.sonicAudioProcessor.setPitch(playbackParameters.pitch), playbackParameters.skipSilence);
        }

        public long getMediaDuration(long j) {
            return this.sonicAudioProcessor.scaleDurationForSpeedup(j);
        }

        public long getSkippedOutputFrameCount() {
            return this.silenceSkippingAudioProcessor.getSkippedFrames();
        }
    }

    private final class PositionTrackerListener implements AudioTrackPositionTracker.Listener {
        private PositionTrackerListener() {
        }

        /* synthetic */ PositionTrackerListener(DefaultAudioSink defaultAudioSink, C01491 c01491) {
            this();
        }

        public void onPositionFramesMismatch(long j, long j2, long j3, long j4) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Spurious audio timestamp (frame position mismatch): ");
            stringBuilder.append(j);
            String str = ", ";
            stringBuilder.append(str);
            stringBuilder.append(j2);
            stringBuilder.append(str);
            stringBuilder.append(j3);
            stringBuilder.append(str);
            stringBuilder.append(j4);
            stringBuilder.append(str);
            stringBuilder.append(DefaultAudioSink.this.getSubmittedFrames());
            stringBuilder.append(str);
            stringBuilder.append(DefaultAudioSink.this.getWrittenFrames());
            str = stringBuilder.toString();
            if (DefaultAudioSink.failOnSpuriousAudioTimestamp) {
                throw new InvalidAudioTrackTimestampException(str, null);
            }
            Log.m18w("AudioTrack", str);
        }

        public void onSystemTimeUsMismatch(long j, long j2, long j3, long j4) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Spurious audio timestamp (system clock mismatch): ");
            stringBuilder.append(j);
            String str = ", ";
            stringBuilder.append(str);
            stringBuilder.append(j2);
            stringBuilder.append(str);
            stringBuilder.append(j3);
            stringBuilder.append(str);
            stringBuilder.append(j4);
            stringBuilder.append(str);
            stringBuilder.append(DefaultAudioSink.this.getSubmittedFrames());
            stringBuilder.append(str);
            stringBuilder.append(DefaultAudioSink.this.getWrittenFrames());
            str = stringBuilder.toString();
            if (DefaultAudioSink.failOnSpuriousAudioTimestamp) {
                throw new InvalidAudioTrackTimestampException(str, null);
            }
            Log.m18w("AudioTrack", str);
        }

        public void onInvalidLatency(long j) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Ignoring impossibly large audio latency: ");
            stringBuilder.append(j);
            Log.m18w("AudioTrack", stringBuilder.toString());
        }

        public void onUnderrun(int i, long j) {
            if (DefaultAudioSink.this.listener != null) {
                DefaultAudioSink.this.listener.onUnderrun(i, j, SystemClock.elapsedRealtime() - DefaultAudioSink.this.lastFeedElapsedRealtimeMs);
            }
        }
    }

    /*  JADX ERROR: JadxRuntimeException in pass: BlockProcessor
        jadx.core.utils.exceptions.JadxRuntimeException: Can't find immediate dominator for block B:69:0x00ea in {6, 7, 10, 15, 16, 20, 21, 30, 39, 40, 43, 44, 56, 59, 60, 63, 64, 66, 68} preds:[]
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
    public void configure(int r5, int r6, int r7, int r8, int[] r9, int r10, int r11) throws com.google.android.exoplayer2.audio.AudioSink.ConfigurationException {
        /*
        r4 = this;
        r4.inputSampleRate = r7;
        r0 = com.google.android.exoplayer2.util.Util.isEncodingLinearPcm(r5);
        r4.isInputPcm = r0;
        r0 = r4.enableConvertHighResIntPcmToFloat;
        r1 = 1;
        r2 = 0;
        if (r0 == 0) goto L_0x001e;
        r0 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
        r0 = r4.supportsOutput(r6, r0);
        if (r0 == 0) goto L_0x001e;
        r0 = com.google.android.exoplayer2.util.Util.isEncodingHighResolutionIntegerPcm(r5);
        if (r0 == 0) goto L_0x001e;
        r0 = 1;
        goto L_0x001f;
        r0 = 0;
        r4.shouldConvertHighResIntPcmToFloat = r0;
        r0 = r4.isInputPcm;
        if (r0 == 0) goto L_0x002b;
        r0 = com.google.android.exoplayer2.util.Util.getPcmFrameSize(r5, r6);
        r4.pcmFrameSize = r0;
        r0 = r4.isInputPcm;
        if (r0 == 0) goto L_0x0034;
        r0 = 4;
        if (r5 == r0) goto L_0x0034;
        r0 = 1;
        goto L_0x0035;
        r0 = 0;
        if (r0 == 0) goto L_0x003c;
        r3 = r4.shouldConvertHighResIntPcmToFloat;
        if (r3 != 0) goto L_0x003c;
        goto L_0x003d;
        r1 = 0;
        r4.canApplyPlaybackParameters = r1;
        r1 = com.google.android.exoplayer2.util.Util.SDK_INT;
        r3 = 21;
        if (r1 >= r3) goto L_0x0057;
        r1 = 8;
        if (r6 != r1) goto L_0x0057;
        if (r9 != 0) goto L_0x0057;
        r9 = 6;
        r9 = new int[r9];
        r1 = 0;
        r3 = r9.length;
        if (r1 >= r3) goto L_0x0057;
        r9[r1] = r1;
        r1 = r1 + 1;
        goto L_0x004f;
        if (r0 == 0) goto L_0x0090;
        r1 = r4.trimmingAudioProcessor;
        r1.setTrimFrameCount(r10, r11);
        r10 = r4.channelMappingAudioProcessor;
        r10.setChannelMap(r9);
        r9 = r4.getAvailableAudioProcessors();
        r10 = r9.length;
        r11 = r7;
        r7 = r5;
        r5 = 0;
        if (r2 >= r10) goto L_0x0093;
        r1 = r9[r2];
        r3 = r1.configure(r11, r6, r7);	 Catch:{ UnhandledFormatException -> 0x0089 }
        r5 = r5 | r3;
        r3 = r1.isActive();
        if (r3 == 0) goto L_0x0086;
        r6 = r1.getOutputChannelCount();
        r11 = r1.getOutputSampleRateHz();
        r7 = r1.getOutputEncoding();
        r2 = r2 + 1;
        goto L_0x006b;
        r5 = move-exception;
        r6 = new com.google.android.exoplayer2.audio.AudioSink$ConfigurationException;
        r6.<init>(r5);
        throw r6;
        r11 = r7;
        r7 = r5;
        r5 = 0;
        r9 = r4.isInputPcm;
        r9 = getChannelConfig(r6, r9);
        if (r9 == 0) goto L_0x00d3;
        if (r5 != 0) goto L_0x00b0;
        r5 = r4.isInitialized();
        if (r5 == 0) goto L_0x00b0;
        r5 = r4.outputEncoding;
        if (r5 != r7) goto L_0x00b0;
        r5 = r4.outputSampleRate;
        if (r5 != r11) goto L_0x00b0;
        r5 = r4.outputChannelConfig;
        if (r5 != r9) goto L_0x00b0;
        return;
        r4.flush();
        r4.processingEnabled = r0;
        r4.outputSampleRate = r11;
        r4.outputChannelConfig = r9;
        r4.outputEncoding = r7;
        r5 = r4.isInputPcm;
        if (r5 == 0) goto L_0x00c6;
        r5 = r4.outputEncoding;
        r5 = com.google.android.exoplayer2.util.Util.getPcmFrameSize(r5, r6);
        goto L_0x00c7;
        r5 = -1;
        r4.outputPcmFrameSize = r5;
        if (r8 == 0) goto L_0x00cc;
        goto L_0x00d0;
        r8 = r4.getDefaultBufferSize();
        r4.bufferSize = r8;
        return;
        r5 = new com.google.android.exoplayer2.audio.AudioSink$ConfigurationException;
        r7 = new java.lang.StringBuilder;
        r7.<init>();
        r8 = "Unsupported channel count: ";
        r7.append(r8);
        r7.append(r6);
        r6 = r7.toString();
        r5.<init>(r6);
        throw r5;
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.exoplayer2.audio.DefaultAudioSink.configure(int, int, int, int, int[], int, int):void");
    }

    public DefaultAudioSink(AudioCapabilities audioCapabilities, AudioProcessor[] audioProcessorArr) {
        this(audioCapabilities, audioProcessorArr, false);
    }

    public DefaultAudioSink(AudioCapabilities audioCapabilities, AudioProcessor[] audioProcessorArr, boolean z) {
        this(audioCapabilities, new DefaultAudioProcessorChain(audioProcessorArr), z);
    }

    public DefaultAudioSink(AudioCapabilities audioCapabilities, AudioProcessorChain audioProcessorChain, boolean z) {
        this.audioCapabilities = audioCapabilities;
        Assertions.checkNotNull(audioProcessorChain);
        this.audioProcessorChain = audioProcessorChain;
        this.enableConvertHighResIntPcmToFloat = z;
        this.releasingConditionVariable = new ConditionVariable(true);
        this.audioTrackPositionTracker = new AudioTrackPositionTracker(new PositionTrackerListener(this, null));
        this.channelMappingAudioProcessor = new ChannelMappingAudioProcessor();
        this.trimmingAudioProcessor = new TrimmingAudioProcessor();
        ArrayList arrayList = new ArrayList();
        Collections.addAll(arrayList, new AudioProcessor[]{new ResamplingAudioProcessor(), this.channelMappingAudioProcessor, this.trimmingAudioProcessor});
        Collections.addAll(arrayList, audioProcessorChain.getAudioProcessors());
        this.toIntPcmAvailableAudioProcessors = (AudioProcessor[]) arrayList.toArray(new AudioProcessor[0]);
        this.toFloatPcmAvailableAudioProcessors = new AudioProcessor[]{new FloatResamplingAudioProcessor()};
        this.volume = 1.0f;
        this.startMediaTimeState = 0;
        this.audioAttributes = AudioAttributes.DEFAULT;
        this.audioSessionId = 0;
        this.auxEffectInfo = new AuxEffectInfo(0, 0.0f);
        this.playbackParameters = PlaybackParameters.DEFAULT;
        this.drainingAudioProcessorIndex = -1;
        this.activeAudioProcessors = new AudioProcessor[0];
        this.outputBuffers = new ByteBuffer[0];
        this.playbackParametersCheckpoints = new ArrayDeque();
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public boolean supportsOutput(int i, int i2) {
        boolean z = true;
        if (Util.isEncodingLinearPcm(i2)) {
            if (i2 == 4 && Util.SDK_INT < 21) {
                z = false;
            }
            return z;
        }
        AudioCapabilities audioCapabilities = this.audioCapabilities;
        if (audioCapabilities == null || !audioCapabilities.supportsEncoding(i2) || (i != -1 && i > this.audioCapabilities.getMaxChannelCount())) {
            z = false;
        }
        return z;
    }

    public long getCurrentPositionUs(boolean z) {
        if (!isInitialized() || this.startMediaTimeState == 0) {
            return Long.MIN_VALUE;
        }
        return this.startMediaTimeUs + applySkipping(applySpeedup(Math.min(this.audioTrackPositionTracker.getCurrentPositionUs(z), framesToDurationUs(getWrittenFrames()))));
    }

    private int getDefaultBufferSize() {
        int minBufferSize;
        if (this.isInputPcm) {
            minBufferSize = AudioTrack.getMinBufferSize(this.outputSampleRate, this.outputChannelConfig, this.outputEncoding);
            Assertions.checkState(minBufferSize != -2);
            return Util.constrainValue(minBufferSize * 4, ((int) durationUsToFrames(250000)) * this.outputPcmFrameSize, (int) Math.max((long) minBufferSize, durationUsToFrames(750000) * ((long) this.outputPcmFrameSize)));
        }
        minBufferSize = getMaximumEncodedRateBytesPerSecond(this.outputEncoding);
        if (this.outputEncoding == 5) {
            minBufferSize *= 2;
        }
        return (int) ((((long) minBufferSize) * 250000) / 1000000);
    }

    private void setupAudioProcessors() {
        ArrayList arrayList = new ArrayList();
        for (AudioProcessor audioProcessor : getAvailableAudioProcessors()) {
            if (audioProcessor.isActive()) {
                arrayList.add(audioProcessor);
            } else {
                audioProcessor.flush();
            }
        }
        int size = arrayList.size();
        this.activeAudioProcessors = (AudioProcessor[]) arrayList.toArray(new AudioProcessor[size]);
        this.outputBuffers = new ByteBuffer[size];
        flushAudioProcessors();
    }

    private void flushAudioProcessors() {
        int i = 0;
        while (true) {
            AudioProcessor[] audioProcessorArr = this.activeAudioProcessors;
            if (i < audioProcessorArr.length) {
                AudioProcessor audioProcessor = audioProcessorArr[i];
                audioProcessor.flush();
                this.outputBuffers[i] = audioProcessor.getOutput();
                i++;
            } else {
                return;
            }
        }
    }

    private void initialize() throws InitializationException {
        this.releasingConditionVariable.block();
        this.audioTrack = initializeAudioTrack();
        int audioSessionId = this.audioTrack.getAudioSessionId();
        if (enablePreV21AudioSessionWorkaround && Util.SDK_INT < 21) {
            AudioTrack audioTrack = this.keepSessionIdAudioTrack;
            if (!(audioTrack == null || audioSessionId == audioTrack.getAudioSessionId())) {
                releaseKeepSessionIdAudioTrack();
            }
            if (this.keepSessionIdAudioTrack == null) {
                this.keepSessionIdAudioTrack = initializeKeepSessionIdAudioTrack(audioSessionId);
            }
        }
        if (this.audioSessionId != audioSessionId) {
            this.audioSessionId = audioSessionId;
            Listener listener = this.listener;
            if (listener != null) {
                listener.onAudioSessionId(audioSessionId);
            }
        }
        this.playbackParameters = this.canApplyPlaybackParameters ? this.audioProcessorChain.applyPlaybackParameters(this.playbackParameters) : PlaybackParameters.DEFAULT;
        setupAudioProcessors();
        this.audioTrackPositionTracker.setAudioTrack(this.audioTrack, this.outputEncoding, this.outputPcmFrameSize, this.bufferSize);
        setVolumeInternal();
        audioSessionId = this.auxEffectInfo.effectId;
        if (audioSessionId != 0) {
            this.audioTrack.attachAuxEffect(audioSessionId);
            this.audioTrack.setAuxEffectSendLevel(this.auxEffectInfo.sendLevel);
        }
    }

    public void play() {
        this.playing = true;
        if (isInitialized()) {
            this.audioTrackPositionTracker.start();
            this.audioTrack.play();
        }
    }

    public void handleDiscontinuity() {
        if (this.startMediaTimeState == 1) {
            this.startMediaTimeState = 2;
        }
    }

    public boolean handleBuffer(ByteBuffer byteBuffer, long j) throws InitializationException, WriteException {
        ByteBuffer byteBuffer2 = byteBuffer;
        long j2 = j;
        ByteBuffer byteBuffer3 = this.inputBuffer;
        boolean z = byteBuffer3 == null || byteBuffer2 == byteBuffer3;
        Assertions.checkArgument(z);
        if (!isInitialized()) {
            initialize();
            if (this.playing) {
                play();
            }
        }
        if (!this.audioTrackPositionTracker.mayHandleBuffer(getWrittenFrames())) {
            return false;
        }
        String str = "AudioTrack";
        if (this.inputBuffer == null) {
            if (!byteBuffer.hasRemaining()) {
                return true;
            }
            if (!this.isInputPcm && this.framesPerEncodedSample == 0) {
                this.framesPerEncodedSample = getFramesPerEncodedSample(this.outputEncoding, byteBuffer2);
                if (this.framesPerEncodedSample == 0) {
                    return true;
                }
            }
            if (this.afterDrainPlaybackParameters != null) {
                if (!drainAudioProcessorsToEndOfStream()) {
                    return false;
                }
                PlaybackParameters playbackParameters = this.afterDrainPlaybackParameters;
                this.afterDrainPlaybackParameters = null;
                this.playbackParametersCheckpoints.add(new PlaybackParametersCheckpoint(this.audioProcessorChain.applyPlaybackParameters(playbackParameters), Math.max(0, j2), framesToDurationUs(getWrittenFrames()), null));
                setupAudioProcessors();
            }
            if (this.startMediaTimeState == 0) {
                this.startMediaTimeUs = Math.max(0, j2);
                this.startMediaTimeState = 1;
            } else {
                long inputFramesToDurationUs = this.startMediaTimeUs + inputFramesToDurationUs(getSubmittedFrames() - this.trimmingAudioProcessor.getTrimmedFrameCount());
                if (this.startMediaTimeState == 1 && Math.abs(inputFramesToDurationUs - j2) > 200000) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Discontinuity detected [expected ");
                    stringBuilder.append(inputFramesToDurationUs);
                    stringBuilder.append(", got ");
                    stringBuilder.append(j2);
                    stringBuilder.append("]");
                    Log.m14e(str, stringBuilder.toString());
                    this.startMediaTimeState = 2;
                }
                if (this.startMediaTimeState == 2) {
                    inputFramesToDurationUs = j2 - inputFramesToDurationUs;
                    this.startMediaTimeUs += inputFramesToDurationUs;
                    this.startMediaTimeState = 1;
                    Listener listener = this.listener;
                    if (!(listener == null || inputFramesToDurationUs == 0)) {
                        listener.onPositionDiscontinuity();
                    }
                }
            }
            if (this.isInputPcm) {
                this.submittedPcmBytes += (long) byteBuffer.remaining();
            } else {
                this.submittedEncodedFrames += (long) this.framesPerEncodedSample;
            }
            this.inputBuffer = byteBuffer2;
        }
        if (this.processingEnabled) {
            processBuffers(j2);
        } else {
            writeBuffer(this.inputBuffer, j2);
        }
        if (!this.inputBuffer.hasRemaining()) {
            this.inputBuffer = null;
            return true;
        } else if (!this.audioTrackPositionTracker.isStalled(getWrittenFrames())) {
            return false;
        } else {
            Log.m18w(str, "Resetting stalled audio track");
            flush();
            return true;
        }
    }

    private void processBuffers(long j) throws WriteException {
        int length = this.activeAudioProcessors.length;
        int i = length;
        while (i >= 0) {
            ByteBuffer byteBuffer;
            if (i > 0) {
                byteBuffer = this.outputBuffers[i - 1];
            } else {
                byteBuffer = this.inputBuffer;
                if (byteBuffer == null) {
                    byteBuffer = AudioProcessor.EMPTY_BUFFER;
                }
            }
            if (i == length) {
                writeBuffer(byteBuffer, j);
            } else {
                AudioProcessor audioProcessor = this.activeAudioProcessors[i];
                audioProcessor.queueInput(byteBuffer);
                ByteBuffer output = audioProcessor.getOutput();
                this.outputBuffers[i] = output;
                if (output.hasRemaining()) {
                    i++;
                }
            }
            if (!byteBuffer.hasRemaining()) {
                i--;
            } else {
                return;
            }
        }
    }

    private void writeBuffer(ByteBuffer byteBuffer, long j) throws WriteException {
        if (byteBuffer.hasRemaining()) {
            int remaining;
            ByteBuffer byteBuffer2 = this.outputBuffer;
            boolean z = true;
            int i = 0;
            if (byteBuffer2 != null) {
                Assertions.checkArgument(byteBuffer2 == byteBuffer);
            } else {
                this.outputBuffer = byteBuffer;
                if (Util.SDK_INT < 21) {
                    remaining = byteBuffer.remaining();
                    byte[] bArr = this.preV21OutputBuffer;
                    if (bArr == null || bArr.length < remaining) {
                        this.preV21OutputBuffer = new byte[remaining];
                    }
                    int position = byteBuffer.position();
                    byteBuffer.get(this.preV21OutputBuffer, 0, remaining);
                    byteBuffer.position(position);
                    this.preV21OutputBufferOffset = 0;
                }
            }
            remaining = byteBuffer.remaining();
            if (Util.SDK_INT < 21) {
                int availableBufferSize = this.audioTrackPositionTracker.getAvailableBufferSize(this.writtenPcmBytes);
                if (availableBufferSize > 0) {
                    i = this.audioTrack.write(this.preV21OutputBuffer, this.preV21OutputBufferOffset, Math.min(remaining, availableBufferSize));
                    if (i > 0) {
                        this.preV21OutputBufferOffset += i;
                        byteBuffer.position(byteBuffer.position() + i);
                    }
                }
            } else if (this.tunneling) {
                if (j == -9223372036854775807L) {
                    z = false;
                }
                Assertions.checkState(z);
                i = writeNonBlockingWithAvSyncV21(this.audioTrack, byteBuffer, remaining, j);
            } else {
                i = writeNonBlockingV21(this.audioTrack, byteBuffer, remaining);
            }
            this.lastFeedElapsedRealtimeMs = SystemClock.elapsedRealtime();
            if (i >= 0) {
                if (this.isInputPcm) {
                    this.writtenPcmBytes += (long) i;
                }
                if (i == remaining) {
                    if (!this.isInputPcm) {
                        this.writtenEncodedFrames += (long) this.framesPerEncodedSample;
                    }
                    this.outputBuffer = null;
                }
                return;
            }
            throw new WriteException(i);
        }
    }

    public void playToEndOfStream() throws WriteException {
        if (!this.handledEndOfStream && isInitialized() && drainAudioProcessorsToEndOfStream()) {
            this.audioTrackPositionTracker.handleEndOfStream(getWrittenFrames());
            this.audioTrack.stop();
            this.bytesUntilNextAvSync = 0;
            this.handledEndOfStream = true;
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:11:0x0021  */
    /* JADX WARNING: Removed duplicated region for block: B:20:0x003c  */
    private boolean drainAudioProcessorsToEndOfStream() throws com.google.android.exoplayer2.audio.AudioSink.WriteException {
        /*
        r9 = this;
        r0 = r9.drainingAudioProcessorIndex;
        r1 = -1;
        r2 = 1;
        r3 = 0;
        if (r0 != r1) goto L_0x0014;
    L_0x0007:
        r0 = r9.processingEnabled;
        if (r0 == 0) goto L_0x000d;
    L_0x000b:
        r0 = 0;
        goto L_0x0010;
    L_0x000d:
        r0 = r9.activeAudioProcessors;
        r0 = r0.length;
    L_0x0010:
        r9.drainingAudioProcessorIndex = r0;
    L_0x0012:
        r0 = 1;
        goto L_0x0015;
    L_0x0014:
        r0 = 0;
    L_0x0015:
        r4 = r9.drainingAudioProcessorIndex;
        r5 = r9.activeAudioProcessors;
        r6 = r5.length;
        r7 = -9223372036854775807; // 0x8000000000000001 float:1.4E-45 double:-4.9E-324;
        if (r4 >= r6) goto L_0x0038;
    L_0x0021:
        r4 = r5[r4];
        if (r0 == 0) goto L_0x0028;
    L_0x0025:
        r4.queueEndOfStream();
    L_0x0028:
        r9.processBuffers(r7);
        r0 = r4.isEnded();
        if (r0 != 0) goto L_0x0032;
    L_0x0031:
        return r3;
    L_0x0032:
        r0 = r9.drainingAudioProcessorIndex;
        r0 = r0 + r2;
        r9.drainingAudioProcessorIndex = r0;
        goto L_0x0012;
    L_0x0038:
        r0 = r9.outputBuffer;
        if (r0 == 0) goto L_0x0044;
    L_0x003c:
        r9.writeBuffer(r0, r7);
        r0 = r9.outputBuffer;
        if (r0 == 0) goto L_0x0044;
    L_0x0043:
        return r3;
    L_0x0044:
        r9.drainingAudioProcessorIndex = r1;
        return r2;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.exoplayer2.audio.DefaultAudioSink.drainAudioProcessorsToEndOfStream():boolean");
    }

    public boolean isEnded() {
        return !isInitialized() || (this.handledEndOfStream && !hasPendingData());
    }

    public boolean hasPendingData() {
        return isInitialized() && this.audioTrackPositionTracker.hasPendingData(getWrittenFrames());
    }

    public PlaybackParameters setPlaybackParameters(PlaybackParameters playbackParameters) {
        if (!isInitialized() || this.canApplyPlaybackParameters) {
            Object obj = this.afterDrainPlaybackParameters;
            if (obj == null) {
                obj = !this.playbackParametersCheckpoints.isEmpty() ? ((PlaybackParametersCheckpoint) this.playbackParametersCheckpoints.getLast()).playbackParameters : this.playbackParameters;
            }
            if (!playbackParameters.equals(obj)) {
                if (isInitialized()) {
                    this.afterDrainPlaybackParameters = playbackParameters;
                } else {
                    this.playbackParameters = this.audioProcessorChain.applyPlaybackParameters(playbackParameters);
                }
            }
            return this.playbackParameters;
        }
        this.playbackParameters = PlaybackParameters.DEFAULT;
        return this.playbackParameters;
    }

    public PlaybackParameters getPlaybackParameters() {
        return this.playbackParameters;
    }

    public void setAudioAttributes(AudioAttributes audioAttributes) {
        if (!this.audioAttributes.equals(audioAttributes)) {
            this.audioAttributes = audioAttributes;
            if (!this.tunneling) {
                flush();
                this.audioSessionId = 0;
            }
        }
    }

    public void setAuxEffectInfo(AuxEffectInfo auxEffectInfo) {
        if (!this.auxEffectInfo.equals(auxEffectInfo)) {
            int i = auxEffectInfo.effectId;
            float f = auxEffectInfo.sendLevel;
            AudioTrack audioTrack = this.audioTrack;
            if (audioTrack != null) {
                if (this.auxEffectInfo.effectId != i) {
                    audioTrack.attachAuxEffect(i);
                }
                if (i != 0) {
                    this.audioTrack.setAuxEffectSendLevel(f);
                }
            }
            this.auxEffectInfo = auxEffectInfo;
        }
    }

    public void enableTunnelingV21(int i) {
        Assertions.checkState(Util.SDK_INT >= 21);
        if (!this.tunneling || this.audioSessionId != i) {
            this.tunneling = true;
            this.audioSessionId = i;
            flush();
        }
    }

    public void disableTunneling() {
        if (this.tunneling) {
            this.tunneling = false;
            this.audioSessionId = 0;
            flush();
        }
    }

    public void setVolume(float f) {
        if (this.volume != f) {
            this.volume = f;
            setVolumeInternal();
        }
    }

    private void setVolumeInternal() {
        if (!isInitialized()) {
            return;
        }
        if (Util.SDK_INT >= 21) {
            setVolumeInternalV21(this.audioTrack, this.volume);
        } else {
            setVolumeInternalV3(this.audioTrack, this.volume);
        }
    }

    public void pause() {
        this.playing = false;
        if (isInitialized() && this.audioTrackPositionTracker.pause()) {
            this.audioTrack.pause();
        }
    }

    public void flush() {
        if (isInitialized()) {
            this.submittedPcmBytes = 0;
            this.submittedEncodedFrames = 0;
            this.writtenPcmBytes = 0;
            this.writtenEncodedFrames = 0;
            this.framesPerEncodedSample = 0;
            PlaybackParameters playbackParameters = this.afterDrainPlaybackParameters;
            if (playbackParameters != null) {
                this.playbackParameters = playbackParameters;
                this.afterDrainPlaybackParameters = null;
            } else if (!this.playbackParametersCheckpoints.isEmpty()) {
                this.playbackParameters = ((PlaybackParametersCheckpoint) this.playbackParametersCheckpoints.getLast()).playbackParameters;
            }
            this.playbackParametersCheckpoints.clear();
            this.playbackParametersOffsetUs = 0;
            this.playbackParametersPositionUs = 0;
            this.trimmingAudioProcessor.resetTrimmedFrameCount();
            this.inputBuffer = null;
            this.outputBuffer = null;
            flushAudioProcessors();
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
                public void run() {
                    try {
                        audioTrack.flush();
                        audioTrack.release();
                    } finally {
                        DefaultAudioSink.this.releasingConditionVariable.open();
                    }
                }
            }.start();
        }
    }

    public void reset() {
        flush();
        releaseKeepSessionIdAudioTrack();
        for (AudioProcessor reset : this.toIntPcmAvailableAudioProcessors) {
            reset.reset();
        }
        for (AudioProcessor reset2 : this.toFloatPcmAvailableAudioProcessors) {
            reset2.reset();
        }
        this.audioSessionId = 0;
        this.playing = false;
    }

    private void releaseKeepSessionIdAudioTrack() {
        final AudioTrack audioTrack = this.keepSessionIdAudioTrack;
        if (audioTrack != null) {
            this.keepSessionIdAudioTrack = null;
            new Thread() {
                public void run() {
                    audioTrack.release();
                }
            }.start();
        }
    }

    private long applySpeedup(long j) {
        PlaybackParametersCheckpoint playbackParametersCheckpoint = null;
        while (!this.playbackParametersCheckpoints.isEmpty() && j >= ((PlaybackParametersCheckpoint) this.playbackParametersCheckpoints.getFirst()).positionUs) {
            playbackParametersCheckpoint = (PlaybackParametersCheckpoint) this.playbackParametersCheckpoints.remove();
        }
        if (playbackParametersCheckpoint != null) {
            this.playbackParameters = playbackParametersCheckpoint.playbackParameters;
            this.playbackParametersPositionUs = playbackParametersCheckpoint.positionUs;
            this.playbackParametersOffsetUs = playbackParametersCheckpoint.mediaTimeUs - this.startMediaTimeUs;
        }
        if (this.playbackParameters.speed == 1.0f) {
            return (j + this.playbackParametersOffsetUs) - this.playbackParametersPositionUs;
        }
        long j2;
        if (this.playbackParametersCheckpoints.isEmpty()) {
            j2 = this.playbackParametersOffsetUs;
            j = this.audioProcessorChain.getMediaDuration(j - this.playbackParametersPositionUs);
        } else {
            j2 = this.playbackParametersOffsetUs;
            j = Util.getMediaDurationForPlayoutDuration(j - this.playbackParametersPositionUs, this.playbackParameters.speed);
        }
        return j2 + j;
    }

    private long applySkipping(long j) {
        return j + framesToDurationUs(this.audioProcessorChain.getSkippedOutputFrameCount());
    }

    private boolean isInitialized() {
        return this.audioTrack != null;
    }

    private long inputFramesToDurationUs(long j) {
        return (j * 1000000) / ((long) this.inputSampleRate);
    }

    private long framesToDurationUs(long j) {
        return (j * 1000000) / ((long) this.outputSampleRate);
    }

    private long durationUsToFrames(long j) {
        return (j * ((long) this.outputSampleRate)) / 1000000;
    }

    private long getSubmittedFrames() {
        return this.isInputPcm ? this.submittedPcmBytes / ((long) this.pcmFrameSize) : this.submittedEncodedFrames;
    }

    private long getWrittenFrames() {
        return this.isInputPcm ? this.writtenPcmBytes / ((long) this.outputPcmFrameSize) : this.writtenEncodedFrames;
    }

    private AudioTrack initializeAudioTrack() throws InitializationException {
        AudioTrack createAudioTrackV21;
        if (Util.SDK_INT >= 21) {
            createAudioTrackV21 = createAudioTrackV21();
        } else {
            int streamTypeForAudioUsage = Util.getStreamTypeForAudioUsage(this.audioAttributes.usage);
            int i = this.audioSessionId;
            AudioTrack audioTrack;
            if (i == 0) {
                audioTrack = new AudioTrack(streamTypeForAudioUsage, this.outputSampleRate, this.outputChannelConfig, this.outputEncoding, this.bufferSize, 1);
            } else {
                audioTrack = new AudioTrack(streamTypeForAudioUsage, this.outputSampleRate, this.outputChannelConfig, this.outputEncoding, this.bufferSize, 1, i);
            }
        }
        int state = createAudioTrackV21.getState();
        if (state == 1) {
            return createAudioTrackV21;
        }
        try {
            createAudioTrackV21.release();
        } catch (Exception unused) {
        }
        throw new InitializationException(state, this.outputSampleRate, this.outputChannelConfig, this.bufferSize);
    }

    @TargetApi(21)
    private AudioTrack createAudioTrackV21() {
        AudioAttributes build;
        if (this.tunneling) {
            build = new Builder().setContentType(3).setFlags(16).setUsage(1).build();
        } else {
            build = this.audioAttributes.getAudioAttributesV21();
        }
        AudioAttributes audioAttributes = build;
        AudioFormat build2 = new AudioFormat.Builder().setChannelMask(this.outputChannelConfig).setEncoding(this.outputEncoding).setSampleRate(this.outputSampleRate).build();
        int i = this.audioSessionId;
        return new AudioTrack(audioAttributes, build2, this.bufferSize, 1, i != 0 ? i : 0);
    }

    private AudioTrack initializeKeepSessionIdAudioTrack(int i) {
        return new AudioTrack(3, 4000, 4, 2, 2, 0, i);
    }

    private AudioProcessor[] getAvailableAudioProcessors() {
        return this.shouldConvertHighResIntPcmToFloat ? this.toFloatPcmAvailableAudioProcessors : this.toIntPcmAvailableAudioProcessors;
    }

    private static int getChannelConfig(int i, boolean z) {
        if (Util.SDK_INT <= 28 && !z) {
            if (i == 7) {
                i = 8;
            } else if (i == 3 || i == 4 || i == 5) {
                i = 6;
            }
        }
        if (Util.SDK_INT <= 26) {
            if ("fugu".equals(Util.DEVICE) && !z && r2 == 1) {
                i = 2;
            }
        }
        return Util.getAudioTrackChannelConfig(i);
    }

    private static int getMaximumEncodedRateBytesPerSecond(int i) {
        if (i == 5) {
            return 80000;
        }
        if (i == 6) {
            return 768000;
        }
        if (i == 7) {
            return 192000;
        }
        if (i == 8) {
            return 2250000;
        }
        if (i == 14) {
            return 3062500;
        }
        throw new IllegalArgumentException();
    }

    private static int getFramesPerEncodedSample(int i, ByteBuffer byteBuffer) {
        if (i == 7 || i == 8) {
            return DtsUtil.parseDtsAudioSampleCount(byteBuffer);
        }
        if (i == 5) {
            return Ac3Util.getAc3SyncframeAudioSampleCount();
        }
        if (i == 6) {
            return Ac3Util.parseEAc3SyncframeAudioSampleCount(byteBuffer);
        }
        if (i == 14) {
            i = Ac3Util.findTrueHdSyncframeOffset(byteBuffer);
            if (i == -1) {
                i = 0;
            } else {
                i = Ac3Util.parseTrueHdSyncframeAudioSampleCount(byteBuffer, i) * 16;
            }
            return i;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Unexpected audio encoding: ");
        stringBuilder.append(i);
        throw new IllegalStateException(stringBuilder.toString());
    }

    @TargetApi(21)
    private static int writeNonBlockingV21(AudioTrack audioTrack, ByteBuffer byteBuffer, int i) {
        return audioTrack.write(byteBuffer, i, 1);
    }

    @TargetApi(21)
    private int writeNonBlockingWithAvSyncV21(AudioTrack audioTrack, ByteBuffer byteBuffer, int i, long j) {
        if (this.avSyncHeader == null) {
            this.avSyncHeader = ByteBuffer.allocate(16);
            this.avSyncHeader.order(ByteOrder.BIG_ENDIAN);
            this.avSyncHeader.putInt(1431633921);
        }
        if (this.bytesUntilNextAvSync == 0) {
            this.avSyncHeader.putInt(4, i);
            this.avSyncHeader.putLong(8, j * 1000);
            this.avSyncHeader.position(0);
            this.bytesUntilNextAvSync = i;
        }
        int remaining = this.avSyncHeader.remaining();
        if (remaining > 0) {
            int write = audioTrack.write(this.avSyncHeader, remaining, 1);
            if (write < 0) {
                this.bytesUntilNextAvSync = 0;
                return write;
            } else if (write < remaining) {
                return 0;
            }
        }
        int writeNonBlockingV21 = writeNonBlockingV21(audioTrack, byteBuffer, i);
        if (writeNonBlockingV21 < 0) {
            this.bytesUntilNextAvSync = 0;
            return writeNonBlockingV21;
        }
        this.bytesUntilNextAvSync -= writeNonBlockingV21;
        return writeNonBlockingV21;
    }

    @TargetApi(21)
    private static void setVolumeInternalV21(AudioTrack audioTrack, float f) {
        audioTrack.setVolume(f);
    }

    private static void setVolumeInternalV3(AudioTrack audioTrack, float f) {
        audioTrack.setStereoVolume(f, f);
    }
}
