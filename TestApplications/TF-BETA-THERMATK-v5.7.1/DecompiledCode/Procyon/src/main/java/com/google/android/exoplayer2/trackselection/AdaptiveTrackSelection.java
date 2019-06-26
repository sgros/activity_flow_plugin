// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.trackselection;

import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.source.chunk.MediaChunkIterator;
import com.google.android.exoplayer2.util.Util;
import com.google.android.exoplayer2.source.chunk.MediaChunk;
import java.util.List;
import com.google.android.exoplayer2.source.TrackGroup;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.util.Clock;

public class AdaptiveTrackSelection extends BaseTrackSelection
{
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
    
    private AdaptiveTrackSelection(final TrackGroup trackGroup, final int[] array, final BandwidthProvider bandwidthProvider, final long n, final long n2, final long n3, final float bufferedFractionToLiveEdgeForQualityIncrease, final long minTimeBetweenBufferReevaluationMs, final Clock clock) {
        super(trackGroup, array);
        this.bandwidthProvider = bandwidthProvider;
        this.minDurationForQualityIncreaseUs = n * 1000L;
        this.maxDurationForQualityDecreaseUs = n2 * 1000L;
        this.minDurationToRetainAfterDiscardUs = n3 * 1000L;
        this.bufferedFractionToLiveEdgeForQualityIncrease = bufferedFractionToLiveEdgeForQualityIncrease;
        this.minTimeBetweenBufferReevaluationMs = minTimeBetweenBufferReevaluationMs;
        this.clock = clock;
        this.playbackSpeed = 1.0f;
        int i = 0;
        this.reason = 0;
        this.lastBufferEvaluationMs = -9223372036854775807L;
        this.trackBitrateEstimator = TrackBitrateEstimator.DEFAULT;
        final int length = super.length;
        this.formats = new Format[length];
        this.formatBitrates = new int[length];
        this.trackBitrates = new int[length];
        while (i < super.length) {
            final Format format = this.getFormat(i);
            final Format[] formats = this.formats;
            formats[i] = format;
            this.formatBitrates[i] = formats[i].bitrate;
            ++i;
        }
    }
    
    private int determineIdealSelectedIndex(final long n, final int[] array) {
        final long allocatedBandwidth = this.bandwidthProvider.getAllocatedBandwidth();
        int i = 0;
        int n2 = 0;
        while (i < super.length) {
            if (n == Long.MIN_VALUE || !this.isBlacklisted(i, n)) {
                if (this.canSelectFormat(this.getFormat(i), array[i], this.playbackSpeed, allocatedBandwidth)) {
                    return i;
                }
                n2 = i;
            }
            ++i;
        }
        return n2;
    }
    
    private long minDurationForQualityIncreaseUs(long minDurationForQualityIncreaseUs) {
        if (minDurationForQualityIncreaseUs != -9223372036854775807L && minDurationForQualityIncreaseUs <= this.minDurationForQualityIncreaseUs) {
            minDurationForQualityIncreaseUs *= (long)this.bufferedFractionToLiveEdgeForQualityIncrease;
        }
        else {
            minDurationForQualityIncreaseUs = this.minDurationForQualityIncreaseUs;
        }
        return minDurationForQualityIncreaseUs;
    }
    
    protected boolean canSelectFormat(final Format format, final int n, final float n2, final long n3) {
        return Math.round(n * n2) <= n3;
    }
    
    @Override
    public void enable() {
        this.lastBufferEvaluationMs = -9223372036854775807L;
    }
    
    @Override
    public int evaluateQueueSize(final long n, final List<? extends MediaChunk> list) {
        final long elapsedRealtime = this.clock.elapsedRealtime();
        if (!this.shouldEvaluateQueueSize(elapsedRealtime)) {
            return list.size();
        }
        this.lastBufferEvaluationMs = elapsedRealtime;
        final boolean empty = list.isEmpty();
        int i = 0;
        if (empty) {
            return 0;
        }
        final int size = list.size();
        final long playoutDurationForMediaDuration = Util.getPlayoutDurationForMediaDuration(((MediaChunk)list.get(size - 1)).startTimeUs - n, this.playbackSpeed);
        final long minDurationToRetainAfterDiscardUs = this.getMinDurationToRetainAfterDiscardUs();
        if (playoutDurationForMediaDuration < minDurationToRetainAfterDiscardUs) {
            return size;
        }
        final Format format = this.getFormat(this.determineIdealSelectedIndex(elapsedRealtime, this.formatBitrates));
        while (i < size) {
            final MediaChunk mediaChunk = (MediaChunk)list.get(i);
            final Format trackFormat = mediaChunk.trackFormat;
            if (Util.getPlayoutDurationForMediaDuration(mediaChunk.startTimeUs - n, this.playbackSpeed) >= minDurationToRetainAfterDiscardUs && trackFormat.bitrate < format.bitrate) {
                final int height = trackFormat.height;
                if (height != -1 && height < 720) {
                    final int width = trackFormat.width;
                    if (width != -1 && width < 1280 && height < format.height) {
                        return i;
                    }
                }
            }
            ++i;
        }
        return size;
    }
    
    public void experimental_setNonAllocatableBandwidth(final long n) {
        ((DefaultBandwidthProvider)this.bandwidthProvider).experimental_setNonAllocatableBandwidth(n);
    }
    
    public void experimental_setTrackBitrateEstimator(final TrackBitrateEstimator trackBitrateEstimator) {
        this.trackBitrateEstimator = trackBitrateEstimator;
    }
    
    protected long getMinDurationToRetainAfterDiscardUs() {
        return this.minDurationToRetainAfterDiscardUs;
    }
    
    @Override
    public int getSelectedIndex() {
        return this.selectedIndex;
    }
    
    @Override
    public Object getSelectionData() {
        return null;
    }
    
    @Override
    public int getSelectionReason() {
        return this.reason;
    }
    
    @Override
    public void onPlaybackSpeed(final float playbackSpeed) {
        this.playbackSpeed = playbackSpeed;
    }
    
    protected boolean shouldEvaluateQueueSize(final long n) {
        final long lastBufferEvaluationMs = this.lastBufferEvaluationMs;
        return lastBufferEvaluationMs == -9223372036854775807L || n - lastBufferEvaluationMs >= this.minTimeBetweenBufferReevaluationMs;
    }
    
    @Override
    public void updateSelectedTrack(long elapsedRealtime, final long n, final long n2, final List<? extends MediaChunk> list, final MediaChunkIterator[] array) {
        elapsedRealtime = this.clock.elapsedRealtime();
        this.trackBitrateEstimator.getBitrates(this.formats, list, array, this.trackBitrates);
        if (this.reason == 0) {
            this.reason = 1;
            this.selectedIndex = this.determineIdealSelectedIndex(elapsedRealtime, this.trackBitrates);
            return;
        }
        final int selectedIndex = this.selectedIndex;
        this.selectedIndex = this.determineIdealSelectedIndex(elapsedRealtime, this.trackBitrates);
        if (this.selectedIndex == selectedIndex) {
            return;
        }
        if (!this.isBlacklisted(selectedIndex, elapsedRealtime)) {
            final Format format = this.getFormat(selectedIndex);
            final Format format2 = this.getFormat(this.selectedIndex);
            if (format2.bitrate > format.bitrate && n < this.minDurationForQualityIncreaseUs(n2)) {
                this.selectedIndex = selectedIndex;
            }
            else if (format2.bitrate < format.bitrate && n >= this.maxDurationForQualityDecreaseUs) {
                this.selectedIndex = selectedIndex;
            }
        }
        if (this.selectedIndex != selectedIndex) {
            this.reason = 3;
        }
    }
    
    private interface BandwidthProvider
    {
        long getAllocatedBandwidth();
    }
    
    private static final class DefaultBandwidthProvider implements BandwidthProvider
    {
        private final float bandwidthFraction;
        private final BandwidthMeter bandwidthMeter;
        private long nonAllocatableBandwidth;
        
        DefaultBandwidthProvider(final BandwidthMeter bandwidthMeter, final float bandwidthFraction) {
            this.bandwidthMeter = bandwidthMeter;
            this.bandwidthFraction = bandwidthFraction;
        }
        
        void experimental_setNonAllocatableBandwidth(final long nonAllocatableBandwidth) {
            this.nonAllocatableBandwidth = nonAllocatableBandwidth;
        }
        
        @Override
        public long getAllocatedBandwidth() {
            return Math.max(0L, (long)(this.bandwidthMeter.getBitrateEstimate() * this.bandwidthFraction) - this.nonAllocatableBandwidth);
        }
    }
    
    public static final class Factory implements TrackSelection.Factory
    {
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
        public Factory(final BandwidthMeter bandwidthMeter) {
            this(bandwidthMeter, 10000, 25000, 25000, 0.75f, 0.75f, 2000L, Clock.DEFAULT);
        }
        
        @Deprecated
        public Factory(final BandwidthMeter bandwidthMeter, final int minDurationForQualityIncreaseMs, final int maxDurationForQualityDecreaseMs, final int minDurationToRetainAfterDiscardMs, final float bandwidthFraction, final float bufferedFractionToLiveEdgeForQualityIncrease, final long minTimeBetweenBufferReevaluationMs, final Clock clock) {
            this.bandwidthMeter = bandwidthMeter;
            this.minDurationForQualityIncreaseMs = minDurationForQualityIncreaseMs;
            this.maxDurationForQualityDecreaseMs = maxDurationForQualityDecreaseMs;
            this.minDurationToRetainAfterDiscardMs = minDurationToRetainAfterDiscardMs;
            this.bandwidthFraction = bandwidthFraction;
            this.bufferedFractionToLiveEdgeForQualityIncrease = bufferedFractionToLiveEdgeForQualityIncrease;
            this.minTimeBetweenBufferReevaluationMs = minTimeBetweenBufferReevaluationMs;
            this.clock = clock;
            this.trackBitrateEstimator = TrackBitrateEstimator.DEFAULT;
        }
        
        private AdaptiveTrackSelection createAdaptiveTrackSelection(final TrackGroup trackGroup, BandwidthMeter bandwidthMeter, final int[] array) {
            final BandwidthMeter bandwidthMeter2 = this.bandwidthMeter;
            if (bandwidthMeter2 != null) {
                bandwidthMeter = bandwidthMeter2;
            }
            final AdaptiveTrackSelection adaptiveTrackSelection = new AdaptiveTrackSelection(trackGroup, array, (BandwidthProvider)new DefaultBandwidthProvider(bandwidthMeter, this.bandwidthFraction), this.minDurationForQualityIncreaseMs, this.maxDurationForQualityDecreaseMs, this.minDurationToRetainAfterDiscardMs, this.bufferedFractionToLiveEdgeForQualityIncrease, this.minTimeBetweenBufferReevaluationMs, this.clock, null);
            adaptiveTrackSelection.experimental_setTrackBitrateEstimator(this.trackBitrateEstimator);
            return adaptiveTrackSelection;
        }
        
        @Override
        public TrackSelection[] createTrackSelections(final Definition[] array, final BandwidthMeter bandwidthMeter) {
            final TrackSelection[] array2 = new TrackSelection[array.length];
            AdaptiveTrackSelection adaptiveTrackSelection = null;
            int i = 0;
            int n = 0;
            while (i < array.length) {
                final Definition definition = array[i];
                AdaptiveTrackSelection adaptiveTrackSelection2;
                int n2;
                if (definition == null) {
                    adaptiveTrackSelection2 = adaptiveTrackSelection;
                    n2 = n;
                }
                else {
                    final int[] tracks = definition.tracks;
                    if (tracks.length > 1) {
                        adaptiveTrackSelection2 = this.createAdaptiveTrackSelection(definition.group, bandwidthMeter, tracks);
                        array2[i] = adaptiveTrackSelection2;
                        n2 = n;
                    }
                    else {
                        array2[i] = new FixedTrackSelection(definition.group, tracks[0]);
                        final int bitrate = definition.group.getFormat(definition.tracks[0]).bitrate;
                        adaptiveTrackSelection2 = adaptiveTrackSelection;
                        n2 = n;
                        if (bitrate != -1) {
                            n2 = n + bitrate;
                            adaptiveTrackSelection2 = adaptiveTrackSelection;
                        }
                    }
                }
                ++i;
                adaptiveTrackSelection = adaptiveTrackSelection2;
                n = n2;
            }
            if (this.blockFixedTrackSelectionBandwidth && adaptiveTrackSelection != null) {
                adaptiveTrackSelection.experimental_setNonAllocatableBandwidth(n);
            }
            return array2;
        }
    }
}
