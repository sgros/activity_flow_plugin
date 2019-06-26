// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2;

import com.google.android.exoplayer2.source.MediaPeriod;
import com.google.android.exoplayer2.upstream.Allocator;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.source.MediaSource;
import android.util.Pair;

final class MediaPeriodQueue
{
    private int length;
    private MediaPeriodHolder loading;
    private long nextWindowSequenceNumber;
    private Object oldFrontPeriodUid;
    private long oldFrontPeriodWindowSequenceNumber;
    private final Timeline.Period period;
    private MediaPeriodHolder playing;
    private MediaPeriodHolder reading;
    private int repeatMode;
    private boolean shuffleModeEnabled;
    private Timeline timeline;
    private final Timeline.Window window;
    
    public MediaPeriodQueue() {
        this.period = new Timeline.Period();
        this.window = new Timeline.Window();
        this.timeline = Timeline.EMPTY;
    }
    
    private boolean canKeepAfterMediaPeriodHolder(final MediaPeriodHolder mediaPeriodHolder, final long n) {
        return n == -9223372036854775807L || n == mediaPeriodHolder.info.durationUs;
    }
    
    private boolean canKeepMediaPeriodHolder(final MediaPeriodHolder mediaPeriodHolder, final MediaPeriodInfo mediaPeriodInfo) {
        final MediaPeriodInfo info = mediaPeriodHolder.info;
        return info.startPositionUs == mediaPeriodInfo.startPositionUs && info.id.equals(mediaPeriodInfo.id);
    }
    
    private MediaPeriodInfo getFirstMediaPeriodInfo(final PlaybackInfo playbackInfo) {
        return this.getMediaPeriodInfo(playbackInfo.periodId, playbackInfo.contentPositionUs, playbackInfo.startPositionUs);
    }
    
    private MediaPeriodInfo getFollowingMediaPeriodInfo(MediaPeriodHolder next, long n) {
        final MediaPeriodInfo info = next.info;
        final long n2 = next.getRendererOffset() + info.durationUs - n;
        final boolean isLastInTimelinePeriod = info.isLastInTimelinePeriod;
        long longValue = 0L;
        final MediaPeriodInfo mediaPeriodInfo = null;
        final MediaPeriodInfo mediaPeriodInfo2 = null;
        if (isLastInTimelinePeriod) {
            final int nextPeriodIndex = this.timeline.getNextPeriodIndex(this.timeline.getIndexOfPeriod(info.id.periodUid), this.period, this.window, this.repeatMode, this.shuffleModeEnabled);
            if (nextPeriodIndex == -1) {
                return null;
            }
            final int windowIndex = this.timeline.getPeriod(nextPeriodIndex, this.period, true).windowIndex;
            final Object uid = this.period.uid;
            n = info.id.windowSequenceNumber;
            Object o;
            if (this.timeline.getWindow(windowIndex, this.window).firstPeriodIndex == nextPeriodIndex) {
                final Pair<Object, Long> periodPosition = this.timeline.getPeriodPosition(this.window, this.period, windowIndex, -9223372036854775807L, Math.max(0L, n2));
                if (periodPosition == null) {
                    return null;
                }
                final Object first = periodPosition.first;
                longValue = (long)periodPosition.second;
                next = next.getNext();
                if (next != null && next.uid.equals(first)) {
                    n = next.info.id.windowSequenceNumber;
                }
                else {
                    n = this.nextWindowSequenceNumber;
                    this.nextWindowSequenceNumber = 1L + n;
                }
                o = first;
            }
            else {
                o = uid;
            }
            return this.getMediaPeriodInfo(this.resolveMediaPeriodIdForAds(o, longValue, n), longValue, longValue);
        }
        else {
            final MediaSource.MediaPeriodId id = info.id;
            this.timeline.getPeriodByUid(id.periodUid, this.period);
            if (id.isAd()) {
                final int adGroupIndex = id.adGroupIndex;
                final int adCountInAdGroup = this.period.getAdCountInAdGroup(adGroupIndex);
                if (adCountInAdGroup == -1) {
                    return null;
                }
                final int nextAdIndexToPlay = this.period.getNextAdIndexToPlay(adGroupIndex, id.adIndexInAdGroup);
                if (nextAdIndexToPlay < adCountInAdGroup) {
                    MediaPeriodInfo mediaPeriodInfoForAd;
                    if (!this.period.isAdAvailable(adGroupIndex, nextAdIndexToPlay)) {
                        mediaPeriodInfoForAd = mediaPeriodInfo2;
                    }
                    else {
                        mediaPeriodInfoForAd = this.getMediaPeriodInfoForAd(id.periodUid, adGroupIndex, nextAdIndexToPlay, info.contentPositionUs, id.windowSequenceNumber);
                    }
                    return mediaPeriodInfoForAd;
                }
                n = info.contentPositionUs;
                if (this.period.getAdGroupCount() == 1 && this.period.getAdGroupTimeUs(0) == 0L) {
                    final Timeline timeline = this.timeline;
                    final Timeline.Window window = this.window;
                    final Timeline.Period period = this.period;
                    final Pair<Object, Long> periodPosition2 = timeline.getPeriodPosition(window, period, period.windowIndex, -9223372036854775807L, Math.max(0L, n2));
                    if (periodPosition2 == null) {
                        return null;
                    }
                    n = (long)periodPosition2.second;
                }
                return this.getMediaPeriodInfoForContent(id.periodUid, n, id.windowSequenceNumber);
            }
            else {
                final int adGroupIndexForPositionUs = this.period.getAdGroupIndexForPositionUs(info.id.endPositionUs);
                if (adGroupIndexForPositionUs == -1) {
                    return this.getMediaPeriodInfoForContent(id.periodUid, info.durationUs, id.windowSequenceNumber);
                }
                final int firstAdIndexToPlay = this.period.getFirstAdIndexToPlay(adGroupIndexForPositionUs);
                MediaPeriodInfo mediaPeriodInfoForAd2;
                if (!this.period.isAdAvailable(adGroupIndexForPositionUs, firstAdIndexToPlay)) {
                    mediaPeriodInfoForAd2 = mediaPeriodInfo;
                }
                else {
                    mediaPeriodInfoForAd2 = this.getMediaPeriodInfoForAd(id.periodUid, adGroupIndexForPositionUs, firstAdIndexToPlay, info.durationUs, id.windowSequenceNumber);
                }
                return mediaPeriodInfoForAd2;
            }
        }
    }
    
    private MediaPeriodInfo getMediaPeriodInfo(final MediaSource.MediaPeriodId mediaPeriodId, final long n, final long n2) {
        this.timeline.getPeriodByUid(mediaPeriodId.periodUid, this.period);
        if (!mediaPeriodId.isAd()) {
            return this.getMediaPeriodInfoForContent(mediaPeriodId.periodUid, n2, mediaPeriodId.windowSequenceNumber);
        }
        if (!this.period.isAdAvailable(mediaPeriodId.adGroupIndex, mediaPeriodId.adIndexInAdGroup)) {
            return null;
        }
        return this.getMediaPeriodInfoForAd(mediaPeriodId.periodUid, mediaPeriodId.adGroupIndex, mediaPeriodId.adIndexInAdGroup, n, mediaPeriodId.windowSequenceNumber);
    }
    
    private MediaPeriodInfo getMediaPeriodInfoForAd(final Object o, final int n, final int n2, final long n3, long adResumePositionUs) {
        final MediaSource.MediaPeriodId mediaPeriodId = new MediaSource.MediaPeriodId(o, n, n2, adResumePositionUs);
        final long adDurationUs = this.timeline.getPeriodByUid(mediaPeriodId.periodUid, this.period).getAdDurationUs(mediaPeriodId.adGroupIndex, mediaPeriodId.adIndexInAdGroup);
        if (n2 == this.period.getFirstAdIndexToPlay(n)) {
            adResumePositionUs = this.period.getAdResumePositionUs();
        }
        else {
            adResumePositionUs = 0L;
        }
        return new MediaPeriodInfo(mediaPeriodId, adResumePositionUs, n3, adDurationUs, false, false);
    }
    
    private MediaPeriodInfo getMediaPeriodInfoForContent(Object o, final long n, final long n2) {
        final int adGroupIndexAfterPositionUs = this.period.getAdGroupIndexAfterPositionUs(n);
        long n3;
        if (adGroupIndexAfterPositionUs != -1) {
            n3 = this.period.getAdGroupTimeUs(adGroupIndexAfterPositionUs);
        }
        else {
            n3 = -9223372036854775807L;
        }
        o = new MediaSource.MediaPeriodId(o, n2, n3);
        final boolean lastInPeriod = this.isLastInPeriod((MediaSource.MediaPeriodId)o);
        final boolean lastInTimeline = this.isLastInTimeline((MediaSource.MediaPeriodId)o, lastInPeriod);
        if (n3 == -9223372036854775807L || n3 == Long.MIN_VALUE) {
            n3 = this.period.durationUs;
        }
        return new MediaPeriodInfo((MediaSource.MediaPeriodId)o, n, -9223372036854775807L, n3, lastInPeriod, lastInTimeline);
    }
    
    private boolean isLastInPeriod(final MediaSource.MediaPeriodId mediaPeriodId) {
        return !mediaPeriodId.isAd() && mediaPeriodId.endPositionUs == -9223372036854775807L;
    }
    
    private boolean isLastInTimeline(final MediaSource.MediaPeriodId mediaPeriodId, final boolean b) {
        final int indexOfPeriod = this.timeline.getIndexOfPeriod(mediaPeriodId.periodUid);
        return !this.timeline.getWindow(this.timeline.getPeriod(indexOfPeriod, this.period).windowIndex, this.window).isDynamic && this.timeline.isLastPeriod(indexOfPeriod, this.period, this.window, this.repeatMode, this.shuffleModeEnabled) && b;
    }
    
    private MediaSource.MediaPeriodId resolveMediaPeriodIdForAds(final Object o, long adGroupTimeUs, final long n) {
        this.timeline.getPeriodByUid(o, this.period);
        final int adGroupIndexForPositionUs = this.period.getAdGroupIndexForPositionUs(adGroupTimeUs);
        if (adGroupIndexForPositionUs == -1) {
            final int adGroupIndexAfterPositionUs = this.period.getAdGroupIndexAfterPositionUs(adGroupTimeUs);
            if (adGroupIndexAfterPositionUs == -1) {
                adGroupTimeUs = -9223372036854775807L;
            }
            else {
                adGroupTimeUs = this.period.getAdGroupTimeUs(adGroupIndexAfterPositionUs);
            }
            return new MediaSource.MediaPeriodId(o, n, adGroupTimeUs);
        }
        return new MediaSource.MediaPeriodId(o, adGroupIndexForPositionUs, this.period.getFirstAdIndexToPlay(adGroupIndexForPositionUs), n);
    }
    
    private long resolvePeriodIndexToWindowSequenceNumber(final Object obj) {
        final int windowIndex = this.timeline.getPeriodByUid(obj, this.period).windowIndex;
        final Object oldFrontPeriodUid = this.oldFrontPeriodUid;
        if (oldFrontPeriodUid != null) {
            final int indexOfPeriod = this.timeline.getIndexOfPeriod(oldFrontPeriodUid);
            if (indexOfPeriod != -1 && this.timeline.getPeriod(indexOfPeriod, this.period).windowIndex == windowIndex) {
                return this.oldFrontPeriodWindowSequenceNumber;
            }
        }
        for (MediaPeriodHolder mediaPeriodHolder = this.getFrontPeriod(); mediaPeriodHolder != null; mediaPeriodHolder = mediaPeriodHolder.getNext()) {
            if (mediaPeriodHolder.uid.equals(obj)) {
                return mediaPeriodHolder.info.id.windowSequenceNumber;
            }
        }
        for (MediaPeriodHolder mediaPeriodHolder2 = this.getFrontPeriod(); mediaPeriodHolder2 != null; mediaPeriodHolder2 = mediaPeriodHolder2.getNext()) {
            final int indexOfPeriod2 = this.timeline.getIndexOfPeriod(mediaPeriodHolder2.uid);
            if (indexOfPeriod2 != -1 && this.timeline.getPeriod(indexOfPeriod2, this.period).windowIndex == windowIndex) {
                return mediaPeriodHolder2.info.id.windowSequenceNumber;
            }
        }
        final long nextWindowSequenceNumber = this.nextWindowSequenceNumber;
        this.nextWindowSequenceNumber = 1L + nextWindowSequenceNumber;
        return nextWindowSequenceNumber;
    }
    
    private boolean updateForPlaybackModeChange() {
        MediaPeriodHolder mediaPeriodHolder = this.getFrontPeriod();
        final boolean b = true;
        if (mediaPeriodHolder == null) {
            return true;
        }
        int n = this.timeline.getIndexOfPeriod(mediaPeriodHolder.uid);
        while (true) {
            n = this.timeline.getNextPeriodIndex(n, this.period, this.window, this.repeatMode, this.shuffleModeEnabled);
            while (mediaPeriodHolder.getNext() != null && !mediaPeriodHolder.info.isLastInTimelinePeriod) {
                mediaPeriodHolder = mediaPeriodHolder.getNext();
            }
            final MediaPeriodHolder next = mediaPeriodHolder.getNext();
            if (n == -1) {
                break;
            }
            if (next == null) {
                break;
            }
            if (this.timeline.getIndexOfPeriod(next.uid) != n) {
                break;
            }
            mediaPeriodHolder = next;
        }
        final boolean removeAfter = this.removeAfter(mediaPeriodHolder);
        mediaPeriodHolder.info = this.getUpdatedMediaPeriodInfo(mediaPeriodHolder.info);
        boolean b2 = b;
        if (removeAfter) {
            b2 = (!this.hasPlayingPeriod() && b);
        }
        return b2;
    }
    
    public MediaPeriodHolder advancePlayingPeriod() {
        final MediaPeriodHolder playing = this.playing;
        if (playing != null) {
            if (playing == this.reading) {
                this.reading = playing.getNext();
            }
            this.playing.release();
            --this.length;
            if (this.length == 0) {
                this.loading = null;
                final MediaPeriodHolder playing2 = this.playing;
                this.oldFrontPeriodUid = playing2.uid;
                this.oldFrontPeriodWindowSequenceNumber = playing2.info.id.windowSequenceNumber;
            }
            this.playing = this.playing.getNext();
        }
        else {
            final MediaPeriodHolder loading = this.loading;
            this.playing = loading;
            this.reading = loading;
        }
        return this.playing;
    }
    
    public MediaPeriodHolder advanceReadingPeriod() {
        final MediaPeriodHolder reading = this.reading;
        Assertions.checkState(reading != null && reading.getNext() != null);
        return this.reading = this.reading.getNext();
    }
    
    public void clear(final boolean b) {
        final MediaPeriodHolder frontPeriod = this.getFrontPeriod();
        if (frontPeriod != null) {
            Object uid;
            if (b) {
                uid = frontPeriod.uid;
            }
            else {
                uid = null;
            }
            this.oldFrontPeriodUid = uid;
            this.oldFrontPeriodWindowSequenceNumber = frontPeriod.info.id.windowSequenceNumber;
            frontPeriod.release();
            this.removeAfter(frontPeriod);
        }
        else if (!b) {
            this.oldFrontPeriodUid = null;
        }
        this.playing = null;
        this.loading = null;
        this.reading = null;
        this.length = 0;
    }
    
    public MediaPeriod enqueueNextMediaPeriod(final RendererCapabilities[] array, final TrackSelector trackSelector, final Allocator allocator, final MediaSource mediaSource, final MediaPeriodInfo mediaPeriodInfo) {
        final MediaPeriodHolder loading = this.loading;
        long startPositionUs;
        if (loading == null) {
            startPositionUs = mediaPeriodInfo.startPositionUs;
        }
        else {
            startPositionUs = loading.getRendererOffset() + this.loading.info.durationUs;
        }
        final MediaPeriodHolder mediaPeriodHolder = new MediaPeriodHolder(array, startPositionUs, trackSelector, allocator, mediaSource, mediaPeriodInfo);
        if (this.loading != null) {
            Assertions.checkState(this.hasPlayingPeriod());
            this.loading.setNext(mediaPeriodHolder);
        }
        this.oldFrontPeriodUid = null;
        this.loading = mediaPeriodHolder;
        ++this.length;
        return mediaPeriodHolder.mediaPeriod;
    }
    
    public MediaPeriodHolder getFrontPeriod() {
        MediaPeriodHolder mediaPeriodHolder;
        if (this.hasPlayingPeriod()) {
            mediaPeriodHolder = this.playing;
        }
        else {
            mediaPeriodHolder = this.loading;
        }
        return mediaPeriodHolder;
    }
    
    public MediaPeriodHolder getLoadingPeriod() {
        return this.loading;
    }
    
    public MediaPeriodInfo getNextMediaPeriodInfo(final long n, final PlaybackInfo playbackInfo) {
        final MediaPeriodHolder loading = this.loading;
        MediaPeriodInfo mediaPeriodInfo;
        if (loading == null) {
            mediaPeriodInfo = this.getFirstMediaPeriodInfo(playbackInfo);
        }
        else {
            mediaPeriodInfo = this.getFollowingMediaPeriodInfo(loading, n);
        }
        return mediaPeriodInfo;
    }
    
    public MediaPeriodHolder getPlayingPeriod() {
        return this.playing;
    }
    
    public MediaPeriodHolder getReadingPeriod() {
        return this.reading;
    }
    
    public MediaPeriodInfo getUpdatedMediaPeriodInfo(final MediaPeriodInfo mediaPeriodInfo) {
        final MediaSource.MediaPeriodId id = mediaPeriodInfo.id;
        final boolean lastInPeriod = this.isLastInPeriod(id);
        final boolean lastInTimeline = this.isLastInTimeline(id, lastInPeriod);
        this.timeline.getPeriodByUid(mediaPeriodInfo.id.periodUid, this.period);
        long n;
        if (id.isAd()) {
            n = this.period.getAdDurationUs(id.adGroupIndex, id.adIndexInAdGroup);
        }
        else {
            final long endPositionUs = id.endPositionUs;
            if (endPositionUs != -9223372036854775807L) {
                n = endPositionUs;
                if (endPositionUs != Long.MIN_VALUE) {
                    return new MediaPeriodInfo(id, mediaPeriodInfo.startPositionUs, mediaPeriodInfo.contentPositionUs, n, lastInPeriod, lastInTimeline);
                }
            }
            n = this.period.getDurationUs();
        }
        return new MediaPeriodInfo(id, mediaPeriodInfo.startPositionUs, mediaPeriodInfo.contentPositionUs, n, lastInPeriod, lastInTimeline);
    }
    
    public boolean hasPlayingPeriod() {
        return this.playing != null;
    }
    
    public boolean isLoading(final MediaPeriod mediaPeriod) {
        final MediaPeriodHolder loading = this.loading;
        return loading != null && loading.mediaPeriod == mediaPeriod;
    }
    
    public void reevaluateBuffer(final long n) {
        final MediaPeriodHolder loading = this.loading;
        if (loading != null) {
            loading.reevaluateBuffer(n);
        }
    }
    
    public boolean removeAfter(MediaPeriodHolder next) {
        final boolean b = false;
        Assertions.checkState(next != null);
        this.loading = next;
        boolean b2 = b;
        while (next.getNext() != null) {
            next = next.getNext();
            if (next == this.reading) {
                this.reading = this.playing;
                b2 = true;
            }
            next.release();
            --this.length;
        }
        this.loading.setNext(null);
        return b2;
    }
    
    public MediaSource.MediaPeriodId resolveMediaPeriodIdForAds(final Object o, final long n) {
        return this.resolveMediaPeriodIdForAds(o, n, this.resolvePeriodIndexToWindowSequenceNumber(o));
    }
    
    public void setTimeline(final Timeline timeline) {
        this.timeline = timeline;
    }
    
    public boolean shouldLoadNextMediaPeriod() {
        final MediaPeriodHolder loading = this.loading;
        return loading == null || (!loading.info.isFinal && loading.isFullyBuffered() && this.loading.info.durationUs != -9223372036854775807L && this.length < 100);
    }
    
    public boolean updateQueuedPeriods(final MediaSource.MediaPeriodId mediaPeriodId, final long n) {
        int indexOfPeriod = this.timeline.getIndexOfPeriod(mediaPeriodId.periodUid);
        MediaPeriodHolder frontPeriod = this.getFrontPeriod();
        MediaPeriodHolder mediaPeriodHolder = null;
        while (frontPeriod != null) {
            if (mediaPeriodHolder == null) {
                final MediaPeriodInfo info = frontPeriod.info;
                final long durationUs = info.durationUs;
                frontPeriod.info = this.getUpdatedMediaPeriodInfo(info);
                if (!this.canKeepAfterMediaPeriodHolder(frontPeriod, durationUs)) {
                    return this.removeAfter(frontPeriod) ^ true;
                }
            }
            else {
                if (indexOfPeriod == -1 || !frontPeriod.uid.equals(this.timeline.getUidOfPeriod(indexOfPeriod))) {
                    return this.removeAfter(mediaPeriodHolder) ^ true;
                }
                final MediaPeriodInfo followingMediaPeriodInfo = this.getFollowingMediaPeriodInfo(mediaPeriodHolder, n);
                if (followingMediaPeriodInfo == null) {
                    return this.removeAfter(mediaPeriodHolder) ^ true;
                }
                frontPeriod.info = this.getUpdatedMediaPeriodInfo(frontPeriod.info);
                if (!this.canKeepMediaPeriodHolder(frontPeriod, followingMediaPeriodInfo)) {
                    return this.removeAfter(mediaPeriodHolder) ^ true;
                }
                if (!this.canKeepAfterMediaPeriodHolder(frontPeriod, followingMediaPeriodInfo.durationUs)) {
                    return this.removeAfter(frontPeriod) ^ true;
                }
            }
            int nextPeriodIndex = indexOfPeriod;
            if (frontPeriod.info.isLastInTimelinePeriod) {
                nextPeriodIndex = this.timeline.getNextPeriodIndex(indexOfPeriod, this.period, this.window, this.repeatMode, this.shuffleModeEnabled);
            }
            final MediaPeriodHolder next = frontPeriod.getNext();
            mediaPeriodHolder = frontPeriod;
            frontPeriod = next;
            indexOfPeriod = nextPeriodIndex;
        }
        return true;
    }
    
    public boolean updateRepeatMode(final int repeatMode) {
        this.repeatMode = repeatMode;
        return this.updateForPlaybackModeChange();
    }
    
    public boolean updateShuffleModeEnabled(final boolean shuffleModeEnabled) {
        this.shuffleModeEnabled = shuffleModeEnabled;
        return this.updateForPlaybackModeChange();
    }
}
