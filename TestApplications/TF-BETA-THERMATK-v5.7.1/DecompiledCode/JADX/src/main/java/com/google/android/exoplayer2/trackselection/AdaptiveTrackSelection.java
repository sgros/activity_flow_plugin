package com.google.android.exoplayer2.trackselection;

import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.source.TrackGroup;
import com.google.android.exoplayer2.source.chunk.MediaChunk;
import com.google.android.exoplayer2.source.chunk.MediaChunkIterator;
import com.google.android.exoplayer2.trackselection.TrackSelection.Definition;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.util.Clock;
import com.google.android.exoplayer2.util.Util;
import java.util.List;

public class AdaptiveTrackSelection extends BaseTrackSelection {
    private final BandwidthProvider bandwidthProvider;
    private final float bufferedFractionToLiveEdgeForQualityIncrease;
    private final Clock clock;
    private final int[] formatBitrates;
    private final Format[] formats;
    private long lastBufferEvaluationMs;
    private final long maxDurationForQualityDecreaseUs;
    private final long minDurationForQualityIncreaseUs;
    private final long minDurationToRetainAfterDiscardUs;
    private final long minTimeBetweenBufferReevaluationMs;
    private float playbackSpeed;
    private int reason;
    private int selectedIndex;
    private TrackBitrateEstimator trackBitrateEstimator;
    private final int[] trackBitrates;

    private interface BandwidthProvider {
        long getAllocatedBandwidth();
    }

    private static final class DefaultBandwidthProvider implements BandwidthProvider {
        private final float bandwidthFraction;
        private final BandwidthMeter bandwidthMeter;
        private long nonAllocatableBandwidth;

        DefaultBandwidthProvider(BandwidthMeter bandwidthMeter, float f) {
            this.bandwidthMeter = bandwidthMeter;
            this.bandwidthFraction = f;
        }

        public long getAllocatedBandwidth() {
            return Math.max(0, ((long) (((float) this.bandwidthMeter.getBitrateEstimate()) * this.bandwidthFraction)) - this.nonAllocatableBandwidth);
        }

        /* Access modifiers changed, original: 0000 */
        public void experimental_setNonAllocatableBandwidth(long j) {
            this.nonAllocatableBandwidth = j;
        }
    }

    public static final class Factory implements com.google.android.exoplayer2.trackselection.TrackSelection.Factory {
        private final float bandwidthFraction;
        private final BandwidthMeter bandwidthMeter;
        private boolean blockFixedTrackSelectionBandwidth;
        private final float bufferedFractionToLiveEdgeForQualityIncrease;
        private final Clock clock;
        private final int maxDurationForQualityDecreaseMs;
        private final int minDurationForQualityIncreaseMs;
        private final int minDurationToRetainAfterDiscardMs;
        private final long minTimeBetweenBufferReevaluationMs;
        private TrackBitrateEstimator trackBitrateEstimator;

        @Deprecated
        public Factory(BandwidthMeter bandwidthMeter) {
            this(bandwidthMeter, 10000, 25000, 25000, 0.75f, 0.75f, 2000, Clock.DEFAULT);
        }

        @Deprecated
        public Factory(BandwidthMeter bandwidthMeter, int i, int i2, int i3, float f, float f2, long j, Clock clock) {
            this.bandwidthMeter = bandwidthMeter;
            this.minDurationForQualityIncreaseMs = i;
            this.maxDurationForQualityDecreaseMs = i2;
            this.minDurationToRetainAfterDiscardMs = i3;
            this.bandwidthFraction = f;
            this.bufferedFractionToLiveEdgeForQualityIncrease = f2;
            this.minTimeBetweenBufferReevaluationMs = j;
            this.clock = clock;
            this.trackBitrateEstimator = TrackBitrateEstimator.DEFAULT;
        }

        public TrackSelection[] createTrackSelections(Definition[] definitionArr, BandwidthMeter bandwidthMeter) {
            TrackSelection[] trackSelectionArr = new TrackSelection[definitionArr.length];
            AdaptiveTrackSelection adaptiveTrackSelection = null;
            int i = 0;
            for (int i2 = 0; i2 < definitionArr.length; i2++) {
                Definition definition = definitionArr[i2];
                if (definition != null) {
                    int[] iArr = definition.tracks;
                    if (iArr.length > 1) {
                        adaptiveTrackSelection = createAdaptiveTrackSelection(definition.group, bandwidthMeter, iArr);
                        trackSelectionArr[i2] = adaptiveTrackSelection;
                    } else {
                        trackSelectionArr[i2] = new FixedTrackSelection(definition.group, iArr[0]);
                        int i3 = definition.group.getFormat(definition.tracks[0]).bitrate;
                        if (i3 != -1) {
                            i += i3;
                        }
                    }
                }
            }
            if (this.blockFixedTrackSelectionBandwidth && adaptiveTrackSelection != null) {
                adaptiveTrackSelection.experimental_setNonAllocatableBandwidth((long) i);
            }
            return trackSelectionArr;
        }

        private AdaptiveTrackSelection createAdaptiveTrackSelection(TrackGroup trackGroup, BandwidthMeter bandwidthMeter, int[] iArr) {
            BandwidthMeter bandwidthMeter2 = this.bandwidthMeter;
            if (bandwidthMeter2 == null) {
                bandwidthMeter2 = bandwidthMeter;
            }
            AdaptiveTrackSelection adaptiveTrackSelection = r2;
            AdaptiveTrackSelection adaptiveTrackSelection2 = new AdaptiveTrackSelection(trackGroup, iArr, new DefaultBandwidthProvider(bandwidthMeter2, this.bandwidthFraction), (long) this.minDurationForQualityIncreaseMs, (long) this.maxDurationForQualityDecreaseMs, (long) this.minDurationToRetainAfterDiscardMs, this.bufferedFractionToLiveEdgeForQualityIncrease, this.minTimeBetweenBufferReevaluationMs, this.clock);
            adaptiveTrackSelection2 = adaptiveTrackSelection;
            adaptiveTrackSelection2.experimental_setTrackBitrateEstimator(this.trackBitrateEstimator);
            return adaptiveTrackSelection2;
        }
    }

    public Object getSelectionData() {
        return null;
    }

    private AdaptiveTrackSelection(TrackGroup trackGroup, int[] iArr, BandwidthProvider bandwidthProvider, long j, long j2, long j3, float f, long j4, Clock clock) {
        super(trackGroup, iArr);
        this.bandwidthProvider = bandwidthProvider;
        this.minDurationForQualityIncreaseUs = j * 1000;
        this.maxDurationForQualityDecreaseUs = j2 * 1000;
        this.minDurationToRetainAfterDiscardUs = j3 * 1000;
        this.bufferedFractionToLiveEdgeForQualityIncrease = f;
        this.minTimeBetweenBufferReevaluationMs = j4;
        this.clock = clock;
        this.playbackSpeed = 1.0f;
        int i = 0;
        this.reason = 0;
        this.lastBufferEvaluationMs = -9223372036854775807L;
        this.trackBitrateEstimator = TrackBitrateEstimator.DEFAULT;
        int i2 = this.length;
        this.formats = new Format[i2];
        this.formatBitrates = new int[i2];
        this.trackBitrates = new int[i2];
        while (i < this.length) {
            Format format = getFormat(i);
            Format[] formatArr = this.formats;
            formatArr[i] = format;
            this.formatBitrates[i] = formatArr[i].bitrate;
            i++;
        }
    }

    public void experimental_setTrackBitrateEstimator(TrackBitrateEstimator trackBitrateEstimator) {
        this.trackBitrateEstimator = trackBitrateEstimator;
    }

    public void experimental_setNonAllocatableBandwidth(long j) {
        ((DefaultBandwidthProvider) this.bandwidthProvider).experimental_setNonAllocatableBandwidth(j);
    }

    public void enable() {
        this.lastBufferEvaluationMs = -9223372036854775807L;
    }

    public void onPlaybackSpeed(float f) {
        this.playbackSpeed = f;
    }

    public void updateSelectedTrack(long j, long j2, long j3, List<? extends MediaChunk> list, MediaChunkIterator[] mediaChunkIteratorArr) {
        j = this.clock.elapsedRealtime();
        this.trackBitrateEstimator.getBitrates(this.formats, list, mediaChunkIteratorArr, this.trackBitrates);
        if (this.reason == 0) {
            this.reason = 1;
            this.selectedIndex = determineIdealSelectedIndex(j, this.trackBitrates);
            return;
        }
        int i = this.selectedIndex;
        this.selectedIndex = determineIdealSelectedIndex(j, this.trackBitrates);
        if (this.selectedIndex != i) {
            if (!isBlacklisted(i, j)) {
                Format format = getFormat(i);
                Format format2 = getFormat(this.selectedIndex);
                if (format2.bitrate > format.bitrate && j2 < minDurationForQualityIncreaseUs(j3)) {
                    this.selectedIndex = i;
                } else if (format2.bitrate < format.bitrate && j2 >= this.maxDurationForQualityDecreaseUs) {
                    this.selectedIndex = i;
                }
            }
            if (this.selectedIndex != i) {
                this.reason = 3;
            }
        }
    }

    public int getSelectedIndex() {
        return this.selectedIndex;
    }

    public int getSelectionReason() {
        return this.reason;
    }

    public int evaluateQueueSize(long j, List<? extends MediaChunk> list) {
        long elapsedRealtime = this.clock.elapsedRealtime();
        if (!shouldEvaluateQueueSize(elapsedRealtime)) {
            return list.size();
        }
        this.lastBufferEvaluationMs = elapsedRealtime;
        int i = 0;
        if (list.isEmpty()) {
            return 0;
        }
        int size = list.size();
        long playoutDurationForMediaDuration = Util.getPlayoutDurationForMediaDuration(((MediaChunk) list.get(size - 1)).startTimeUs - j, this.playbackSpeed);
        long minDurationToRetainAfterDiscardUs = getMinDurationToRetainAfterDiscardUs();
        if (playoutDurationForMediaDuration < minDurationToRetainAfterDiscardUs) {
            return size;
        }
        Format format = getFormat(determineIdealSelectedIndex(elapsedRealtime, this.formatBitrates));
        while (i < size) {
            MediaChunk mediaChunk = (MediaChunk) list.get(i);
            Format format2 = mediaChunk.trackFormat;
            if (Util.getPlayoutDurationForMediaDuration(mediaChunk.startTimeUs - j, this.playbackSpeed) >= minDurationToRetainAfterDiscardUs && format2.bitrate < format.bitrate) {
                int i2 = format2.height;
                if (i2 != -1 && i2 < 720) {
                    int i3 = format2.width;
                    if (i3 != -1 && i3 < 1280 && i2 < format.height) {
                        return i;
                    }
                }
            }
            i++;
        }
        return size;
    }

    /* Access modifiers changed, original: protected */
    public boolean canSelectFormat(Format format, int i, float f, long j) {
        return ((long) Math.round(((float) i) * f)) <= j;
    }

    /* Access modifiers changed, original: protected */
    public boolean shouldEvaluateQueueSize(long j) {
        long j2 = this.lastBufferEvaluationMs;
        return j2 == -9223372036854775807L || j - j2 >= this.minTimeBetweenBufferReevaluationMs;
    }

    /* Access modifiers changed, original: protected */
    public long getMinDurationToRetainAfterDiscardUs() {
        return this.minDurationToRetainAfterDiscardUs;
    }

    private int determineIdealSelectedIndex(long j, int[] iArr) {
        long allocatedBandwidth = this.bandwidthProvider.getAllocatedBandwidth();
        int i = 0;
        int i2 = 0;
        while (i < this.length) {
            if (j == Long.MIN_VALUE || !isBlacklisted(i, j)) {
                if (canSelectFormat(getFormat(i), iArr[i], this.playbackSpeed, allocatedBandwidth)) {
                    return i;
                }
                i2 = i;
            }
            i++;
        }
        return i2;
    }

    private long minDurationForQualityIncreaseUs(long j) {
        Object obj = (j == -9223372036854775807L || j > this.minDurationForQualityIncreaseUs) ? null : 1;
        return obj != null ? (long) (((float) j) * this.bufferedFractionToLiveEdgeForQualityIncrease) : this.minDurationForQualityIncreaseUs;
    }
}
