package com.google.android.exoplayer2;

import android.util.Pair;
import com.google.android.exoplayer2.Timeline.Period;
import com.google.android.exoplayer2.Timeline.Window;
import com.google.android.exoplayer2.source.MediaPeriod;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.MediaSource.MediaPeriodId;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.upstream.Allocator;
import com.google.android.exoplayer2.util.Assertions;

final class MediaPeriodQueue {
    private int length;
    private MediaPeriodHolder loading;
    private long nextWindowSequenceNumber;
    private Object oldFrontPeriodUid;
    private long oldFrontPeriodWindowSequenceNumber;
    private final Period period = new Period();
    private MediaPeriodHolder playing;
    private MediaPeriodHolder reading;
    private int repeatMode;
    private boolean shuffleModeEnabled;
    private Timeline timeline = Timeline.EMPTY;
    private final Window window = new Window();

    public void setTimeline(Timeline timeline) {
        this.timeline = timeline;
    }

    public boolean updateRepeatMode(int i) {
        this.repeatMode = i;
        return updateForPlaybackModeChange();
    }

    public boolean updateShuffleModeEnabled(boolean z) {
        this.shuffleModeEnabled = z;
        return updateForPlaybackModeChange();
    }

    public boolean isLoading(MediaPeriod mediaPeriod) {
        MediaPeriodHolder mediaPeriodHolder = this.loading;
        return mediaPeriodHolder != null && mediaPeriodHolder.mediaPeriod == mediaPeriod;
    }

    public void reevaluateBuffer(long j) {
        MediaPeriodHolder mediaPeriodHolder = this.loading;
        if (mediaPeriodHolder != null) {
            mediaPeriodHolder.reevaluateBuffer(j);
        }
    }

    public boolean shouldLoadNextMediaPeriod() {
        MediaPeriodHolder mediaPeriodHolder = this.loading;
        return mediaPeriodHolder == null || (!mediaPeriodHolder.info.isFinal && mediaPeriodHolder.isFullyBuffered() && this.loading.info.durationUs != -9223372036854775807L && this.length < 100);
    }

    public MediaPeriodInfo getNextMediaPeriodInfo(long j, PlaybackInfo playbackInfo) {
        MediaPeriodHolder mediaPeriodHolder = this.loading;
        if (mediaPeriodHolder == null) {
            return getFirstMediaPeriodInfo(playbackInfo);
        }
        return getFollowingMediaPeriodInfo(mediaPeriodHolder, j);
    }

    public MediaPeriod enqueueNextMediaPeriod(RendererCapabilities[] rendererCapabilitiesArr, TrackSelector trackSelector, Allocator allocator, MediaSource mediaSource, MediaPeriodInfo mediaPeriodInfo) {
        long j;
        MediaPeriodHolder mediaPeriodHolder = this.loading;
        if (mediaPeriodHolder == null) {
            j = mediaPeriodInfo.startPositionUs;
        } else {
            j = mediaPeriodHolder.getRendererOffset() + this.loading.info.durationUs;
        }
        MediaPeriodHolder mediaPeriodHolder2 = new MediaPeriodHolder(rendererCapabilitiesArr, j, trackSelector, allocator, mediaSource, mediaPeriodInfo);
        if (this.loading != null) {
            Assertions.checkState(hasPlayingPeriod());
            this.loading.setNext(mediaPeriodHolder2);
        }
        this.oldFrontPeriodUid = null;
        this.loading = mediaPeriodHolder2;
        this.length++;
        return mediaPeriodHolder2.mediaPeriod;
    }

    public MediaPeriodHolder getLoadingPeriod() {
        return this.loading;
    }

    public MediaPeriodHolder getPlayingPeriod() {
        return this.playing;
    }

    public MediaPeriodHolder getReadingPeriod() {
        return this.reading;
    }

    public MediaPeriodHolder getFrontPeriod() {
        return hasPlayingPeriod() ? this.playing : this.loading;
    }

    public boolean hasPlayingPeriod() {
        return this.playing != null;
    }

    public MediaPeriodHolder advanceReadingPeriod() {
        MediaPeriodHolder mediaPeriodHolder = this.reading;
        boolean z = (mediaPeriodHolder == null || mediaPeriodHolder.getNext() == null) ? false : true;
        Assertions.checkState(z);
        this.reading = this.reading.getNext();
        return this.reading;
    }

    public MediaPeriodHolder advancePlayingPeriod() {
        MediaPeriodHolder mediaPeriodHolder = this.playing;
        if (mediaPeriodHolder != null) {
            if (mediaPeriodHolder == this.reading) {
                this.reading = mediaPeriodHolder.getNext();
            }
            this.playing.release();
            this.length--;
            if (this.length == 0) {
                this.loading = null;
                mediaPeriodHolder = this.playing;
                this.oldFrontPeriodUid = mediaPeriodHolder.uid;
                this.oldFrontPeriodWindowSequenceNumber = mediaPeriodHolder.info.f15id.windowSequenceNumber;
            }
            this.playing = this.playing.getNext();
        } else {
            mediaPeriodHolder = this.loading;
            this.playing = mediaPeriodHolder;
            this.reading = mediaPeriodHolder;
        }
        return this.playing;
    }

    public boolean removeAfter(MediaPeriodHolder mediaPeriodHolder) {
        boolean z = false;
        Assertions.checkState(mediaPeriodHolder != null);
        this.loading = mediaPeriodHolder;
        while (mediaPeriodHolder.getNext() != null) {
            mediaPeriodHolder = mediaPeriodHolder.getNext();
            if (mediaPeriodHolder == this.reading) {
                this.reading = this.playing;
                z = true;
            }
            mediaPeriodHolder.release();
            this.length--;
        }
        this.loading.setNext(null);
        return z;
    }

    public void clear(boolean z) {
        MediaPeriodHolder frontPeriod = getFrontPeriod();
        if (frontPeriod != null) {
            this.oldFrontPeriodUid = z ? frontPeriod.uid : null;
            this.oldFrontPeriodWindowSequenceNumber = frontPeriod.info.f15id.windowSequenceNumber;
            frontPeriod.release();
            removeAfter(frontPeriod);
        } else if (!z) {
            this.oldFrontPeriodUid = null;
        }
        this.playing = null;
        this.loading = null;
        this.reading = null;
        this.length = 0;
    }

    public boolean updateQueuedPeriods(MediaPeriodId mediaPeriodId, long j) {
        int indexOfPeriod = this.timeline.getIndexOfPeriod(mediaPeriodId.periodUid);
        MediaPeriodHolder mediaPeriodHolder = null;
        MediaPeriodHolder frontPeriod = getFrontPeriod();
        while (frontPeriod != null) {
            if (mediaPeriodHolder == null) {
                MediaPeriodInfo mediaPeriodInfo = frontPeriod.info;
                long j2 = mediaPeriodInfo.durationUs;
                frontPeriod.info = getUpdatedMediaPeriodInfo(mediaPeriodInfo);
                if (!canKeepAfterMediaPeriodHolder(frontPeriod, j2)) {
                    return removeAfter(frontPeriod) ^ 1;
                }
            } else if (indexOfPeriod == -1 || !frontPeriod.uid.equals(this.timeline.getUidOfPeriod(indexOfPeriod))) {
                return removeAfter(mediaPeriodHolder) ^ 1;
            } else {
                MediaPeriodInfo followingMediaPeriodInfo = getFollowingMediaPeriodInfo(mediaPeriodHolder, j);
                if (followingMediaPeriodInfo == null) {
                    return removeAfter(mediaPeriodHolder) ^ 1;
                }
                frontPeriod.info = getUpdatedMediaPeriodInfo(frontPeriod.info);
                if (!canKeepMediaPeriodHolder(frontPeriod, followingMediaPeriodInfo)) {
                    return removeAfter(mediaPeriodHolder) ^ 1;
                }
                if (!canKeepAfterMediaPeriodHolder(frontPeriod, followingMediaPeriodInfo.durationUs)) {
                    return removeAfter(frontPeriod) ^ 1;
                }
            }
            if (frontPeriod.info.isLastInTimelinePeriod) {
                indexOfPeriod = this.timeline.getNextPeriodIndex(indexOfPeriod, this.period, this.window, this.repeatMode, this.shuffleModeEnabled);
            }
            MediaPeriodHolder mediaPeriodHolder2 = frontPeriod;
            frontPeriod = frontPeriod.getNext();
            mediaPeriodHolder = mediaPeriodHolder2;
        }
        return true;
    }

    public MediaPeriodInfo getUpdatedMediaPeriodInfo(MediaPeriodInfo mediaPeriodInfo) {
        long adDurationUs;
        MediaPeriodId mediaPeriodId = mediaPeriodInfo.f15id;
        boolean isLastInPeriod = isLastInPeriod(mediaPeriodId);
        boolean isLastInTimeline = isLastInTimeline(mediaPeriodId, isLastInPeriod);
        this.timeline.getPeriodByUid(mediaPeriodInfo.f15id.periodUid, this.period);
        if (mediaPeriodId.isAd()) {
            adDurationUs = this.period.getAdDurationUs(mediaPeriodId.adGroupIndex, mediaPeriodId.adIndexInAdGroup);
        } else {
            adDurationUs = mediaPeriodId.endPositionUs;
            if (adDurationUs == -9223372036854775807L || adDurationUs == Long.MIN_VALUE) {
                adDurationUs = this.period.getDurationUs();
            }
        }
        return new MediaPeriodInfo(mediaPeriodId, mediaPeriodInfo.startPositionUs, mediaPeriodInfo.contentPositionUs, adDurationUs, isLastInPeriod, isLastInTimeline);
    }

    public MediaPeriodId resolveMediaPeriodIdForAds(Object obj, long j) {
        return resolveMediaPeriodIdForAds(obj, j, resolvePeriodIndexToWindowSequenceNumber(obj));
    }

    private MediaPeriodId resolveMediaPeriodIdForAds(Object obj, long j, long j2) {
        this.timeline.getPeriodByUid(obj, this.period);
        int adGroupIndexForPositionUs = this.period.getAdGroupIndexForPositionUs(j);
        if (adGroupIndexForPositionUs == -1) {
            int adGroupIndexAfterPositionUs = this.period.getAdGroupIndexAfterPositionUs(j);
            if (adGroupIndexAfterPositionUs == -1) {
                j = -9223372036854775807L;
            } else {
                j = this.period.getAdGroupTimeUs(adGroupIndexAfterPositionUs);
            }
            return new MediaPeriodId(obj, j2, j);
        }
        return new MediaPeriodId(obj, adGroupIndexForPositionUs, this.period.getFirstAdIndexToPlay(adGroupIndexForPositionUs), j2);
    }

    private long resolvePeriodIndexToWindowSequenceNumber(Object obj) {
        int indexOfPeriod;
        int i = this.timeline.getPeriodByUid(obj, this.period).windowIndex;
        Object obj2 = this.oldFrontPeriodUid;
        if (obj2 != null) {
            indexOfPeriod = this.timeline.getIndexOfPeriod(obj2);
            if (indexOfPeriod != -1 && this.timeline.getPeriod(indexOfPeriod, this.period).windowIndex == i) {
                return this.oldFrontPeriodWindowSequenceNumber;
            }
        }
        for (MediaPeriodHolder frontPeriod = getFrontPeriod(); frontPeriod != null; frontPeriod = frontPeriod.getNext()) {
            if (frontPeriod.uid.equals(obj)) {
                return frontPeriod.info.f15id.windowSequenceNumber;
            }
        }
        for (MediaPeriodHolder frontPeriod2 = getFrontPeriod(); frontPeriod2 != null; frontPeriod2 = frontPeriod2.getNext()) {
            indexOfPeriod = this.timeline.getIndexOfPeriod(frontPeriod2.uid);
            if (indexOfPeriod != -1 && this.timeline.getPeriod(indexOfPeriod, this.period).windowIndex == i) {
                return frontPeriod2.info.f15id.windowSequenceNumber;
            }
        }
        long j = this.nextWindowSequenceNumber;
        this.nextWindowSequenceNumber = 1 + j;
        return j;
    }

    private boolean canKeepMediaPeriodHolder(MediaPeriodHolder mediaPeriodHolder, MediaPeriodInfo mediaPeriodInfo) {
        MediaPeriodInfo mediaPeriodInfo2 = mediaPeriodHolder.info;
        return mediaPeriodInfo2.startPositionUs == mediaPeriodInfo.startPositionUs && mediaPeriodInfo2.f15id.equals(mediaPeriodInfo.f15id);
    }

    private boolean canKeepAfterMediaPeriodHolder(MediaPeriodHolder mediaPeriodHolder, long j) {
        return j == -9223372036854775807L || j == mediaPeriodHolder.info.durationUs;
    }

    private boolean updateForPlaybackModeChange() {
        MediaPeriodHolder frontPeriod = getFrontPeriod();
        boolean z = true;
        if (frontPeriod == null) {
            return true;
        }
        boolean removeAfter;
        int indexOfPeriod = this.timeline.getIndexOfPeriod(frontPeriod.uid);
        while (true) {
            indexOfPeriod = this.timeline.getNextPeriodIndex(indexOfPeriod, this.period, this.window, this.repeatMode, this.shuffleModeEnabled);
            while (frontPeriod.getNext() != null && !frontPeriod.info.isLastInTimelinePeriod) {
                frontPeriod = frontPeriod.getNext();
            }
            MediaPeriodHolder next = frontPeriod.getNext();
            if (indexOfPeriod == -1 || next == null || this.timeline.getIndexOfPeriod(next.uid) != indexOfPeriod) {
                removeAfter = removeAfter(frontPeriod);
                frontPeriod.info = getUpdatedMediaPeriodInfo(frontPeriod.info);
            } else {
                frontPeriod = next;
            }
        }
        removeAfter = removeAfter(frontPeriod);
        frontPeriod.info = getUpdatedMediaPeriodInfo(frontPeriod.info);
        if (removeAfter && hasPlayingPeriod()) {
            z = false;
        }
        return z;
    }

    private MediaPeriodInfo getFirstMediaPeriodInfo(PlaybackInfo playbackInfo) {
        return getMediaPeriodInfo(playbackInfo.periodId, playbackInfo.contentPositionUs, playbackInfo.startPositionUs);
    }

    private MediaPeriodInfo getFollowingMediaPeriodInfo(MediaPeriodHolder mediaPeriodHolder, long j) {
        MediaPeriodInfo mediaPeriodInfo = mediaPeriodHolder.info;
        long rendererOffset = (mediaPeriodHolder.getRendererOffset() + mediaPeriodInfo.durationUs) - j;
        long j2 = 0;
        MediaPeriodInfo mediaPeriodInfo2 = null;
        int nextPeriodIndex;
        long j3;
        Pair periodPosition;
        if (mediaPeriodInfo.isLastInTimelinePeriod) {
            int indexOfPeriod = this.timeline.getIndexOfPeriod(mediaPeriodInfo.f15id.periodUid);
            nextPeriodIndex = this.timeline.getNextPeriodIndex(indexOfPeriod, this.period, this.window, this.repeatMode, this.shuffleModeEnabled);
            if (nextPeriodIndex == -1) {
                return null;
            }
            long j4;
            Object obj;
            int i = this.timeline.getPeriod(nextPeriodIndex, this.period, true).windowIndex;
            Object obj2 = this.period.uid;
            j3 = mediaPeriodInfo.f15id.windowSequenceNumber;
            if (this.timeline.getWindow(i, this.window).firstPeriodIndex == nextPeriodIndex) {
                periodPosition = this.timeline.getPeriodPosition(this.window, this.period, i, -9223372036854775807L, Math.max(0, rendererOffset));
                if (periodPosition == null) {
                    return null;
                }
                long j5;
                Object obj3 = periodPosition.first;
                long longValue = ((Long) periodPosition.second).longValue();
                MediaPeriodHolder next = mediaPeriodHolder.getNext();
                if (next == null || !next.uid.equals(obj3)) {
                    j5 = this.nextWindowSequenceNumber;
                    this.nextWindowSequenceNumber = 1 + j5;
                } else {
                    j5 = next.info.f15id.windowSequenceNumber;
                }
                j2 = longValue;
                j4 = j5;
                obj = obj3;
            } else {
                obj = obj2;
                j4 = j3;
            }
            rendererOffset = j2;
            return getMediaPeriodInfo(resolveMediaPeriodIdForAds(obj, rendererOffset, j4), rendererOffset, j2);
        }
        MediaPeriodId mediaPeriodId = mediaPeriodInfo.f15id;
        this.timeline.getPeriodByUid(mediaPeriodId.periodUid, this.period);
        if (mediaPeriodId.isAd()) {
            nextPeriodIndex = mediaPeriodId.adGroupIndex;
            int adCountInAdGroup = this.period.getAdCountInAdGroup(nextPeriodIndex);
            if (adCountInAdGroup == -1) {
                return null;
            }
            int nextAdIndexToPlay = this.period.getNextAdIndexToPlay(nextPeriodIndex, mediaPeriodId.adIndexInAdGroup);
            if (nextAdIndexToPlay < adCountInAdGroup) {
                if (this.period.isAdAvailable(nextPeriodIndex, nextAdIndexToPlay)) {
                    mediaPeriodInfo2 = getMediaPeriodInfoForAd(mediaPeriodId.periodUid, nextPeriodIndex, nextAdIndexToPlay, mediaPeriodInfo.contentPositionUs, mediaPeriodId.windowSequenceNumber);
                }
                return mediaPeriodInfo2;
            }
            j3 = mediaPeriodInfo.contentPositionUs;
            if (this.period.getAdGroupCount() == 1 && this.period.getAdGroupTimeUs(0) == 0) {
                Timeline timeline = this.timeline;
                Window window = this.window;
                Period period = this.period;
                periodPosition = timeline.getPeriodPosition(window, period, period.windowIndex, -9223372036854775807L, Math.max(0, rendererOffset));
                if (periodPosition == null) {
                    return null;
                }
                rendererOffset = ((Long) periodPosition.second).longValue();
            } else {
                rendererOffset = j3;
            }
            return getMediaPeriodInfoForContent(mediaPeriodId.periodUid, rendererOffset, mediaPeriodId.windowSequenceNumber);
        }
        int adGroupIndexForPositionUs = this.period.getAdGroupIndexForPositionUs(mediaPeriodInfo.f15id.endPositionUs);
        if (adGroupIndexForPositionUs == -1) {
            return getMediaPeriodInfoForContent(mediaPeriodId.periodUid, mediaPeriodInfo.durationUs, mediaPeriodId.windowSequenceNumber);
        }
        int firstAdIndexToPlay = this.period.getFirstAdIndexToPlay(adGroupIndexForPositionUs);
        if (this.period.isAdAvailable(adGroupIndexForPositionUs, firstAdIndexToPlay)) {
            mediaPeriodInfo2 = getMediaPeriodInfoForAd(mediaPeriodId.periodUid, adGroupIndexForPositionUs, firstAdIndexToPlay, mediaPeriodInfo.durationUs, mediaPeriodId.windowSequenceNumber);
        }
        return mediaPeriodInfo2;
    }

    private MediaPeriodInfo getMediaPeriodInfo(MediaPeriodId mediaPeriodId, long j, long j2) {
        this.timeline.getPeriodByUid(mediaPeriodId.periodUid, this.period);
        if (!mediaPeriodId.isAd()) {
            return getMediaPeriodInfoForContent(mediaPeriodId.periodUid, j2, mediaPeriodId.windowSequenceNumber);
        } else if (!this.period.isAdAvailable(mediaPeriodId.adGroupIndex, mediaPeriodId.adIndexInAdGroup)) {
            return null;
        } else {
            return getMediaPeriodInfoForAd(mediaPeriodId.periodUid, mediaPeriodId.adGroupIndex, mediaPeriodId.adIndexInAdGroup, j, mediaPeriodId.windowSequenceNumber);
        }
    }

    private MediaPeriodInfo getMediaPeriodInfoForAd(Object obj, int i, int i2, long j, long j2) {
        MediaPeriodId mediaPeriodId = new MediaPeriodId(obj, i, i2, j2);
        int i3 = i;
        return new MediaPeriodInfo(mediaPeriodId, i2 == this.period.getFirstAdIndexToPlay(i) ? this.period.getAdResumePositionUs() : 0, j, this.timeline.getPeriodByUid(mediaPeriodId.periodUid, this.period).getAdDurationUs(mediaPeriodId.adGroupIndex, mediaPeriodId.adIndexInAdGroup), false, false);
    }

    private MediaPeriodInfo getMediaPeriodInfoForContent(Object obj, long j, long j2) {
        long j3;
        int adGroupIndexAfterPositionUs = this.period.getAdGroupIndexAfterPositionUs(j);
        long adGroupTimeUs = adGroupIndexAfterPositionUs != -1 ? this.period.getAdGroupTimeUs(adGroupIndexAfterPositionUs) : -9223372036854775807L;
        MediaPeriodId mediaPeriodId = new MediaPeriodId(obj, j2, adGroupTimeUs);
        boolean isLastInPeriod = isLastInPeriod(mediaPeriodId);
        boolean isLastInTimeline = isLastInTimeline(mediaPeriodId, isLastInPeriod);
        if (adGroupTimeUs == -9223372036854775807L || adGroupTimeUs == Long.MIN_VALUE) {
            j3 = this.period.durationUs;
        } else {
            j3 = adGroupTimeUs;
        }
        return new MediaPeriodInfo(mediaPeriodId, j, -9223372036854775807L, j3, isLastInPeriod, isLastInTimeline);
    }

    private boolean isLastInPeriod(MediaPeriodId mediaPeriodId) {
        return !mediaPeriodId.isAd() && mediaPeriodId.endPositionUs == -9223372036854775807L;
    }

    private boolean isLastInTimeline(MediaPeriodId mediaPeriodId, boolean z) {
        int indexOfPeriod = this.timeline.getIndexOfPeriod(mediaPeriodId.periodUid);
        return !this.timeline.getWindow(this.timeline.getPeriod(indexOfPeriod, this.period).windowIndex, this.window).isDynamic && this.timeline.isLastPeriod(indexOfPeriod, this.period, this.window, this.repeatMode, this.shuffleModeEnabled) && z;
    }
}
