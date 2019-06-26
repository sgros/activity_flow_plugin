package com.google.android.exoplayer2.source;

import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.FormatHolder;
import com.google.android.exoplayer2.SeekParameters;
import com.google.android.exoplayer2.decoder.DecoderInputBuffer;
import com.google.android.exoplayer2.source.MediaPeriod.Callback;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.util.MimeTypes;
import com.google.android.exoplayer2.util.TimestampAdjuster;
import com.google.android.exoplayer2.util.Util;
import java.io.IOException;

public final class ClippingMediaPeriod implements MediaPeriod, Callback {
    private Callback callback;
    long endUs;
    public final MediaPeriod mediaPeriod;
    private long pendingInitialDiscontinuityPositionUs;
    private ClippingSampleStream[] sampleStreams = new ClippingSampleStream[0];
    long startUs;

    private final class ClippingSampleStream implements SampleStream {
        public final SampleStream childStream;
        private boolean sentEos;

        public ClippingSampleStream(SampleStream sampleStream) {
            this.childStream = sampleStream;
        }

        public void clearSentEos() {
            this.sentEos = false;
        }

        public boolean isReady() {
            return !ClippingMediaPeriod.this.isPendingInitialDiscontinuity() && this.childStream.isReady();
        }

        public void maybeThrowError() throws IOException {
            this.childStream.maybeThrowError();
        }

        public int readData(FormatHolder formatHolder, DecoderInputBuffer decoderInputBuffer, boolean z) {
            if (ClippingMediaPeriod.this.isPendingInitialDiscontinuity()) {
                return -3;
            }
            if (this.sentEos) {
                decoderInputBuffer.setFlags(4);
                return -4;
            }
            int readData = this.childStream.readData(formatHolder, decoderInputBuffer, z);
            if (readData == -5) {
                Format format = formatHolder.format;
                if (!(format.encoderDelay == 0 && format.encoderPadding == 0)) {
                    readData = 0;
                    int i = ClippingMediaPeriod.this.startUs != 0 ? 0 : format.encoderDelay;
                    if (ClippingMediaPeriod.this.endUs == Long.MIN_VALUE) {
                        readData = format.encoderPadding;
                    }
                    formatHolder.format = format.copyWithGaplessInfo(i, readData);
                }
                return -5;
            }
            long j = ClippingMediaPeriod.this.endUs;
            if (j == Long.MIN_VALUE || ((readData != -4 || decoderInputBuffer.timeUs < j) && (readData != -3 || ClippingMediaPeriod.this.getBufferedPositionUs() != Long.MIN_VALUE))) {
                return readData;
            }
            decoderInputBuffer.clear();
            decoderInputBuffer.setFlags(4);
            this.sentEos = true;
            return -4;
        }

        public int skipData(long j) {
            if (ClippingMediaPeriod.this.isPendingInitialDiscontinuity()) {
                return -3;
            }
            return this.childStream.skipData(j);
        }
    }

    public ClippingMediaPeriod(MediaPeriod mediaPeriod, boolean z, long j, long j2) {
        this.mediaPeriod = mediaPeriod;
        this.pendingInitialDiscontinuityPositionUs = z ? j : -9223372036854775807L;
        this.startUs = j;
        this.endUs = j2;
    }

    public void prepare(Callback callback, long j) {
        this.callback = callback;
        this.mediaPeriod.prepare(this, j);
    }

    public void maybeThrowPrepareError() throws IOException {
        this.mediaPeriod.maybeThrowPrepareError();
    }

    public TrackGroupArray getTrackGroups() {
        return this.mediaPeriod.getTrackGroups();
    }

    /* JADX WARNING: Removed duplicated region for block: B:28:0x006e  */
    /* JADX WARNING: Removed duplicated region for block: B:17:0x0052  */
    /* JADX WARNING: Removed duplicated region for block: B:28:0x006e  */
    /* JADX WARNING: Missing block: B:22:0x0062, code skipped:
            if (r2 > r4) goto L_0x0065;
     */
    public long selectTracks(com.google.android.exoplayer2.trackselection.TrackSelection[] r13, boolean[] r14, com.google.android.exoplayer2.source.SampleStream[] r15, boolean[] r16, long r17) {
        /*
        r12 = this;
        r0 = r12;
        r1 = r15;
        r2 = r1.length;
        r2 = new com.google.android.exoplayer2.source.ClippingMediaPeriod.ClippingSampleStream[r2];
        r0.sampleStreams = r2;
        r2 = r1.length;
        r9 = new com.google.android.exoplayer2.source.SampleStream[r2];
        r10 = 0;
        r2 = 0;
    L_0x000c:
        r3 = r1.length;
        r11 = 0;
        if (r2 >= r3) goto L_0x0025;
    L_0x0010:
        r3 = r0.sampleStreams;
        r4 = r1[r2];
        r4 = (com.google.android.exoplayer2.source.ClippingMediaPeriod.ClippingSampleStream) r4;
        r3[r2] = r4;
        r4 = r3[r2];
        if (r4 == 0) goto L_0x0020;
    L_0x001c:
        r3 = r3[r2];
        r11 = r3.childStream;
    L_0x0020:
        r9[r2] = r11;
        r2 = r2 + 1;
        goto L_0x000c;
    L_0x0025:
        r2 = r0.mediaPeriod;
        r3 = r13;
        r4 = r14;
        r5 = r9;
        r6 = r16;
        r7 = r17;
        r2 = r2.selectTracks(r3, r4, r5, r6, r7);
        r4 = r12.isPendingInitialDiscontinuity();
        if (r4 == 0) goto L_0x0047;
    L_0x0038:
        r4 = r0.startUs;
        r6 = (r17 > r4 ? 1 : (r17 == r4 ? 0 : -1));
        if (r6 != 0) goto L_0x0047;
    L_0x003e:
        r6 = r13;
        r4 = shouldKeepInitialDiscontinuity(r4, r13);
        if (r4 == 0) goto L_0x0047;
    L_0x0045:
        r4 = r2;
        goto L_0x004c;
    L_0x0047:
        r4 = -9223372036854775807; // 0x8000000000000001 float:1.4E-45 double:-4.9E-324;
    L_0x004c:
        r0.pendingInitialDiscontinuityPositionUs = r4;
        r4 = (r2 > r17 ? 1 : (r2 == r17 ? 0 : -1));
        if (r4 == 0) goto L_0x0067;
    L_0x0052:
        r4 = r0.startUs;
        r6 = (r2 > r4 ? 1 : (r2 == r4 ? 0 : -1));
        if (r6 < 0) goto L_0x0065;
    L_0x0058:
        r4 = r0.endUs;
        r6 = -9223372036854775808;
        r8 = (r4 > r6 ? 1 : (r4 == r6 ? 0 : -1));
        if (r8 == 0) goto L_0x0067;
    L_0x0060:
        r6 = (r2 > r4 ? 1 : (r2 == r4 ? 0 : -1));
        if (r6 > 0) goto L_0x0065;
    L_0x0064:
        goto L_0x0067;
    L_0x0065:
        r4 = 0;
        goto L_0x0068;
    L_0x0067:
        r4 = 1;
    L_0x0068:
        com.google.android.exoplayer2.util.Assertions.checkState(r4);
    L_0x006b:
        r4 = r1.length;
        if (r10 >= r4) goto L_0x0099;
    L_0x006e:
        r4 = r9[r10];
        if (r4 != 0) goto L_0x0077;
    L_0x0072:
        r4 = r0.sampleStreams;
        r4[r10] = r11;
        goto L_0x0090;
    L_0x0077:
        r4 = r1[r10];
        if (r4 == 0) goto L_0x0085;
    L_0x007b:
        r4 = r0.sampleStreams;
        r4 = r4[r10];
        r4 = r4.childStream;
        r5 = r9[r10];
        if (r4 == r5) goto L_0x0090;
    L_0x0085:
        r4 = r0.sampleStreams;
        r5 = new com.google.android.exoplayer2.source.ClippingMediaPeriod$ClippingSampleStream;
        r6 = r9[r10];
        r5.<init>(r6);
        r4[r10] = r5;
    L_0x0090:
        r4 = r0.sampleStreams;
        r4 = r4[r10];
        r1[r10] = r4;
        r10 = r10 + 1;
        goto L_0x006b;
    L_0x0099:
        return r2;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.exoplayer2.source.ClippingMediaPeriod.selectTracks(com.google.android.exoplayer2.trackselection.TrackSelection[], boolean[], com.google.android.exoplayer2.source.SampleStream[], boolean[], long):long");
    }

    public void discardBuffer(long j, boolean z) {
        this.mediaPeriod.discardBuffer(j, z);
    }

    public void reevaluateBuffer(long j) {
        this.mediaPeriod.reevaluateBuffer(j);
    }

    public long readDiscontinuity() {
        long j;
        if (isPendingInitialDiscontinuity()) {
            j = this.pendingInitialDiscontinuityPositionUs;
            this.pendingInitialDiscontinuityPositionUs = -9223372036854775807L;
            long readDiscontinuity = readDiscontinuity();
            if (readDiscontinuity != -9223372036854775807L) {
                j = readDiscontinuity;
            }
            return j;
        }
        j = this.mediaPeriod.readDiscontinuity();
        if (j == -9223372036854775807L) {
            return -9223372036854775807L;
        }
        boolean z = true;
        Assertions.checkState(j >= this.startUs);
        long j2 = this.endUs;
        if (j2 != Long.MIN_VALUE && j > j2) {
            z = false;
        }
        Assertions.checkState(z);
        return j;
    }

    public long getBufferedPositionUs() {
        long bufferedPositionUs = this.mediaPeriod.getBufferedPositionUs();
        if (bufferedPositionUs != Long.MIN_VALUE) {
            long j = this.endUs;
            if (j == Long.MIN_VALUE || bufferedPositionUs < j) {
                return bufferedPositionUs;
            }
        }
        return Long.MIN_VALUE;
    }

    /* JADX WARNING: Missing block: B:13:0x0032, code skipped:
            if (r0 > r7) goto L_0x0035;
     */
    public long seekToUs(long r7) {
        /*
        r6 = this;
        r0 = -9223372036854775807; // 0x8000000000000001 float:1.4E-45 double:-4.9E-324;
        r6.pendingInitialDiscontinuityPositionUs = r0;
        r0 = r6.sampleStreams;
        r1 = r0.length;
        r2 = 0;
        r3 = 0;
    L_0x000c:
        if (r3 >= r1) goto L_0x0018;
    L_0x000e:
        r4 = r0[r3];
        if (r4 == 0) goto L_0x0015;
    L_0x0012:
        r4.clearSentEos();
    L_0x0015:
        r3 = r3 + 1;
        goto L_0x000c;
    L_0x0018:
        r0 = r6.mediaPeriod;
        r0 = r0.seekToUs(r7);
        r3 = (r0 > r7 ? 1 : (r0 == r7 ? 0 : -1));
        if (r3 == 0) goto L_0x0034;
    L_0x0022:
        r7 = r6.startUs;
        r3 = (r0 > r7 ? 1 : (r0 == r7 ? 0 : -1));
        if (r3 < 0) goto L_0x0035;
    L_0x0028:
        r7 = r6.endUs;
        r3 = -9223372036854775808;
        r5 = (r7 > r3 ? 1 : (r7 == r3 ? 0 : -1));
        if (r5 == 0) goto L_0x0034;
    L_0x0030:
        r3 = (r0 > r7 ? 1 : (r0 == r7 ? 0 : -1));
        if (r3 > 0) goto L_0x0035;
    L_0x0034:
        r2 = 1;
    L_0x0035:
        com.google.android.exoplayer2.util.Assertions.checkState(r2);
        return r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.exoplayer2.source.ClippingMediaPeriod.seekToUs(long):long");
    }

    public long getAdjustedSeekPositionUs(long j, SeekParameters seekParameters) {
        long j2 = this.startUs;
        if (j == j2) {
            return j2;
        }
        return this.mediaPeriod.getAdjustedSeekPositionUs(j, clipSeekParameters(j, seekParameters));
    }

    public long getNextLoadPositionUs() {
        long nextLoadPositionUs = this.mediaPeriod.getNextLoadPositionUs();
        if (nextLoadPositionUs != Long.MIN_VALUE) {
            long j = this.endUs;
            if (j == Long.MIN_VALUE || nextLoadPositionUs < j) {
                return nextLoadPositionUs;
            }
        }
        return Long.MIN_VALUE;
    }

    public boolean continueLoading(long j) {
        return this.mediaPeriod.continueLoading(j);
    }

    public void onPrepared(MediaPeriod mediaPeriod) {
        this.callback.onPrepared(this);
    }

    public void onContinueLoadingRequested(MediaPeriod mediaPeriod) {
        this.callback.onContinueLoadingRequested(this);
    }

    /* Access modifiers changed, original: 0000 */
    public boolean isPendingInitialDiscontinuity() {
        return this.pendingInitialDiscontinuityPositionUs != -9223372036854775807L;
    }

    private SeekParameters clipSeekParameters(long j, SeekParameters seekParameters) {
        long constrainValue = Util.constrainValue(seekParameters.toleranceBeforeUs, 0, j - this.startUs);
        long j2 = seekParameters.toleranceAfterUs;
        long j3 = this.endUs;
        j = Util.constrainValue(j2, 0, j3 == Long.MIN_VALUE ? TimestampAdjuster.DO_NOT_OFFSET : j3 - j);
        if (constrainValue == seekParameters.toleranceBeforeUs && j == seekParameters.toleranceAfterUs) {
            return seekParameters;
        }
        return new SeekParameters(constrainValue, j);
    }

    private static boolean shouldKeepInitialDiscontinuity(long j, TrackSelection[] trackSelectionArr) {
        if (j != 0) {
            for (TrackSelection trackSelection : trackSelectionArr) {
                if (trackSelection != null && !MimeTypes.isAudio(trackSelection.getSelectedFormat().sampleMimeType)) {
                    return true;
                }
            }
        }
        return false;
    }
}
