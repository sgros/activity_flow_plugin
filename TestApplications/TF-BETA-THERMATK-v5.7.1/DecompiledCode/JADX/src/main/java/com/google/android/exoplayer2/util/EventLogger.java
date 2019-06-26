package com.google.android.exoplayer2.util;

import android.os.SystemClock;
import android.view.Surface;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Timeline.Period;
import com.google.android.exoplayer2.Timeline.Window;
import com.google.android.exoplayer2.analytics.AnalyticsListener;
import com.google.android.exoplayer2.analytics.AnalyticsListener.C0138-CC;
import com.google.android.exoplayer2.analytics.AnalyticsListener.EventTime;
import com.google.android.exoplayer2.audio.AudioAttributes;
import com.google.android.exoplayer2.decoder.DecoderCounters;
import com.google.android.exoplayer2.metadata.Metadata;
import com.google.android.exoplayer2.source.MediaSourceEventListener.LoadEventInfo;
import com.google.android.exoplayer2.source.MediaSourceEventListener.MediaLoadData;
import com.google.android.exoplayer2.source.TrackGroup;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.MappingTrackSelector;
import com.google.android.exoplayer2.trackselection.MappingTrackSelector.MappedTrackInfo;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.Locale;

public class EventLogger implements AnalyticsListener {
    private static final String DEFAULT_TAG = "EventLogger";
    private static final int MAX_TIMELINE_ITEM_LINES = 3;
    private static final NumberFormat TIME_FORMAT = NumberFormat.getInstance(Locale.US);
    private final Period period;
    private final long startTimeMs;
    private final String tag;
    private final MappingTrackSelector trackSelector;
    private final Window window;

    private static String getAdaptiveSupportString(int i, int i2) {
        return i < 2 ? "N/A" : i2 != 0 ? i2 != 8 ? i2 != 16 ? "?" : "YES" : "YES_NOT_SEAMLESS" : "NO";
    }

    private static String getDiscontinuityReasonString(int i) {
        return i != 0 ? i != 1 ? i != 2 ? i != 3 ? i != 4 ? "?" : "INTERNAL" : "AD_INSERTION" : "SEEK_ADJUSTMENT" : "SEEK" : "PERIOD_TRANSITION";
    }

    private static String getFormatSupportString(int i) {
        return i != 0 ? i != 1 ? i != 2 ? i != 3 ? i != 4 ? "?" : "YES" : "NO_EXCEEDS_CAPABILITIES" : "NO_UNSUPPORTED_DRM" : "NO_UNSUPPORTED_TYPE" : "NO";
    }

    private static String getRepeatModeString(int i) {
        return i != 0 ? i != 1 ? i != 2 ? "?" : "ALL" : "ONE" : "OFF";
    }

    private static String getStateString(int i) {
        return i != 1 ? i != 2 ? i != 3 ? i != 4 ? "?" : "ENDED" : "READY" : "BUFFERING" : "IDLE";
    }

    private static String getTimelineChangeReasonString(int i) {
        return i != 0 ? i != 1 ? i != 2 ? "?" : "DYNAMIC" : "RESET" : "PREPARED";
    }

    private static String getTrackStatusString(boolean z) {
        return z ? "[X]" : "[ ]";
    }

    public /* synthetic */ void onAudioAttributesChanged(EventTime eventTime, AudioAttributes audioAttributes) {
        C0138-CC.$default$onAudioAttributesChanged(this, eventTime, audioAttributes);
    }

    public void onBandwidthEstimate(EventTime eventTime, int i, long j, long j2) {
    }

    public void onLoadCanceled(EventTime eventTime, LoadEventInfo loadEventInfo, MediaLoadData mediaLoadData) {
    }

    public void onLoadCompleted(EventTime eventTime, LoadEventInfo loadEventInfo, MediaLoadData mediaLoadData) {
    }

    public void onLoadStarted(EventTime eventTime, LoadEventInfo loadEventInfo, MediaLoadData mediaLoadData) {
    }

    public /* synthetic */ void onVolumeChanged(EventTime eventTime, float f) {
        C0138-CC.$default$onVolumeChanged(this, eventTime, f);
    }

    static {
        TIME_FORMAT.setMinimumFractionDigits(2);
        TIME_FORMAT.setMaximumFractionDigits(2);
        TIME_FORMAT.setGroupingUsed(false);
    }

    public EventLogger(MappingTrackSelector mappingTrackSelector) {
        this(mappingTrackSelector, DEFAULT_TAG);
    }

    public EventLogger(MappingTrackSelector mappingTrackSelector, String str) {
        this.trackSelector = mappingTrackSelector;
        this.tag = str;
        this.window = new Window();
        this.period = new Period();
        this.startTimeMs = SystemClock.elapsedRealtime();
    }

    public void onLoadingChanged(EventTime eventTime, boolean z) {
        logd(eventTime, "loading", Boolean.toString(z));
    }

    public void onPlayerStateChanged(EventTime eventTime, boolean z, int i) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(z);
        stringBuilder.append(", ");
        stringBuilder.append(getStateString(i));
        logd(eventTime, "state", stringBuilder.toString());
    }

    public void onRepeatModeChanged(EventTime eventTime, int i) {
        logd(eventTime, "repeatMode", getRepeatModeString(i));
    }

    public void onShuffleModeChanged(EventTime eventTime, boolean z) {
        logd(eventTime, "shuffleModeEnabled", Boolean.toString(z));
    }

    public void onPositionDiscontinuity(EventTime eventTime, int i) {
        logd(eventTime, "positionDiscontinuity", getDiscontinuityReasonString(i));
    }

    public void onSeekStarted(EventTime eventTime) {
        logd(eventTime, "seekStarted");
    }

    public void onPlaybackParametersChanged(EventTime eventTime, PlaybackParameters playbackParameters) {
        logd(eventTime, "playbackParameters", Util.formatInvariant("speed=%.2f, pitch=%.2f, skipSilence=%s", Float.valueOf(playbackParameters.speed), Float.valueOf(playbackParameters.pitch), Boolean.valueOf(playbackParameters.skipSilence)));
    }

    public void onTimelineChanged(EventTime eventTime, int i) {
        String str;
        int periodCount = eventTime.timeline.getPeriodCount();
        int windowCount = eventTime.timeline.getWindowCount();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("timelineChanged [");
        stringBuilder.append(getEventTimeString(eventTime));
        stringBuilder.append(", periodCount=");
        stringBuilder.append(periodCount);
        stringBuilder.append(", windowCount=");
        stringBuilder.append(windowCount);
        stringBuilder.append(", reason=");
        stringBuilder.append(getTimelineChangeReasonString(i));
        logd(stringBuilder.toString());
        i = 0;
        int i2 = 0;
        while (true) {
            str = "]";
            if (i2 >= Math.min(periodCount, 3)) {
                break;
            }
            eventTime.timeline.getPeriod(i2, this.period);
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append("  period [");
            stringBuilder2.append(getTimeString(this.period.getDurationMs()));
            stringBuilder2.append(str);
            logd(stringBuilder2.toString());
            i2++;
        }
        String str2 = "  ...";
        if (periodCount > 3) {
            logd(str2);
        }
        while (i < Math.min(windowCount, 3)) {
            eventTime.timeline.getWindow(i, this.window);
            StringBuilder stringBuilder3 = new StringBuilder();
            stringBuilder3.append("  window [");
            stringBuilder3.append(getTimeString(this.window.getDurationMs()));
            String str3 = ", ";
            stringBuilder3.append(str3);
            stringBuilder3.append(this.window.isSeekable);
            stringBuilder3.append(str3);
            stringBuilder3.append(this.window.isDynamic);
            stringBuilder3.append(str);
            logd(stringBuilder3.toString());
            i++;
        }
        if (windowCount > 3) {
            logd(str2);
        }
        logd(str);
    }

    public void onPlayerError(EventTime eventTime, ExoPlaybackException exoPlaybackException) {
        loge(eventTime, "playerFailed", exoPlaybackException);
    }

    public void onTracksChanged(EventTime eventTime, TrackGroupArray trackGroupArray, TrackSelectionArray trackSelectionArray) {
        MappingTrackSelector mappingTrackSelector = this.trackSelector;
        MappedTrackInfo currentMappedTrackInfo = mappingTrackSelector != null ? mappingTrackSelector.getCurrentMappedTrackInfo() : null;
        if (currentMappedTrackInfo == null) {
            logd(eventTime, "tracksChanged", "[]");
            return;
        }
        String str;
        String str2;
        String str3;
        String str4;
        String str5;
        String str6;
        String str7;
        StringBuilder stringBuilder;
        String formatSupportString;
        String str8;
        EventTime eventTime2 = eventTime;
        StringBuilder stringBuilder2 = new StringBuilder();
        stringBuilder2.append("tracksChanged [");
        stringBuilder2.append(getEventTimeString(eventTime));
        String str9 = ", ";
        stringBuilder2.append(str9);
        logd(stringBuilder2.toString());
        int rendererCount = currentMappedTrackInfo.getRendererCount();
        int i = 0;
        while (true) {
            str = ", supported=";
            str2 = " Track:";
            str3 = "    Group:";
            str4 = "  ]";
            str5 = "      ";
            str6 = "    ]";
            str7 = " [";
            if (i >= rendererCount) {
                break;
            }
            int i2;
            TrackGroupArray trackGroups = currentMappedTrackInfo.getTrackGroups(i);
            TrackSelection trackSelection = trackSelectionArray.get(i);
            if (trackGroups.length > 0) {
                String str10;
                StringBuilder stringBuilder3 = new StringBuilder();
                i2 = rendererCount;
                stringBuilder3.append("  Renderer:");
                stringBuilder3.append(i);
                stringBuilder3.append(str7);
                logd(stringBuilder3.toString());
                rendererCount = 0;
                while (rendererCount < trackGroups.length) {
                    TrackGroup trackGroup = trackGroups.get(rendererCount);
                    TrackGroupArray trackGroupArray2 = trackGroups;
                    str10 = str4;
                    str4 = getAdaptiveSupportString(trackGroup.length, currentMappedTrackInfo.getAdaptiveSupport(i, rendererCount, false));
                    stringBuilder = new StringBuilder();
                    stringBuilder.append(str3);
                    stringBuilder.append(rendererCount);
                    stringBuilder.append(", adaptive_supported=");
                    stringBuilder.append(str4);
                    stringBuilder.append(str7);
                    logd(stringBuilder.toString());
                    int i3 = 0;
                    while (i3 < trackGroup.length) {
                        String trackStatusString = getTrackStatusString(trackSelection, trackGroup, i3);
                        formatSupportString = getFormatSupportString(currentMappedTrackInfo.getTrackSupport(i, rendererCount, i3));
                        str8 = str7;
                        StringBuilder stringBuilder4 = new StringBuilder();
                        stringBuilder4.append(str5);
                        stringBuilder4.append(trackStatusString);
                        stringBuilder4.append(str2);
                        stringBuilder4.append(i3);
                        stringBuilder4.append(str9);
                        stringBuilder4.append(Format.toLogString(trackGroup.getFormat(i3)));
                        stringBuilder4.append(str);
                        stringBuilder4.append(formatSupportString);
                        logd(stringBuilder4.toString());
                        i3++;
                        str7 = str8;
                    }
                    str8 = str7;
                    logd(str6);
                    rendererCount++;
                    TrackSelectionArray trackSelectionArray2 = trackSelectionArray;
                    trackGroups = trackGroupArray2;
                    str4 = str10;
                }
                str10 = str4;
                if (trackSelection != null) {
                    for (rendererCount = 0; rendererCount < trackSelection.length(); rendererCount++) {
                        Metadata metadata = trackSelection.getFormat(rendererCount).metadata;
                        if (metadata != null) {
                            logd("    Metadata [");
                            printMetadata(metadata, str5);
                            logd(str6);
                            break;
                        }
                    }
                }
                logd(str10);
            } else {
                i2 = rendererCount;
            }
            i++;
            rendererCount = i2;
        }
        String str11 = str4;
        str8 = str7;
        TrackGroupArray unmappedTrackGroups = currentMappedTrackInfo.getUnmappedTrackGroups();
        if (unmappedTrackGroups.length > 0) {
            logd("  Renderer:None [");
            int i4 = 0;
            while (i4 < unmappedTrackGroups.length) {
                StringBuilder stringBuilder5 = new StringBuilder();
                stringBuilder5.append(str3);
                stringBuilder5.append(i4);
                str4 = str8;
                stringBuilder5.append(str4);
                logd(stringBuilder5.toString());
                TrackGroup trackGroup2 = unmappedTrackGroups.get(i4);
                for (int i5 = 0; i5 < trackGroup2.length; i5++) {
                    formatSupportString = getTrackStatusString(false);
                    String formatSupportString2 = getFormatSupportString(0);
                    stringBuilder = new StringBuilder();
                    stringBuilder.append(str5);
                    stringBuilder.append(formatSupportString);
                    stringBuilder.append(str2);
                    stringBuilder.append(i5);
                    stringBuilder.append(str9);
                    stringBuilder.append(Format.toLogString(trackGroup2.getFormat(i5)));
                    stringBuilder.append(str);
                    stringBuilder.append(formatSupportString2);
                    logd(stringBuilder.toString());
                }
                logd(str6);
                i4++;
                str8 = str4;
            }
            logd(str11);
        }
        logd("]");
    }

    public void onSeekProcessed(EventTime eventTime) {
        logd(eventTime, "seekProcessed");
    }

    public void onMetadata(EventTime eventTime, Metadata metadata) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("metadata [");
        stringBuilder.append(getEventTimeString(eventTime));
        stringBuilder.append(", ");
        logd(stringBuilder.toString());
        printMetadata(metadata, "  ");
        logd("]");
    }

    public void onDecoderEnabled(EventTime eventTime, int i, DecoderCounters decoderCounters) {
        logd(eventTime, "decoderEnabled", getTrackTypeString(i));
    }

    public void onAudioSessionId(EventTime eventTime, int i) {
        logd(eventTime, "audioSessionId", Integer.toString(i));
    }

    public void onDecoderInitialized(EventTime eventTime, int i, String str, long j) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(getTrackTypeString(i));
        stringBuilder.append(", ");
        stringBuilder.append(str);
        logd(eventTime, "decoderInitialized", stringBuilder.toString());
    }

    public void onDecoderInputFormatChanged(EventTime eventTime, int i, Format format) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(getTrackTypeString(i));
        stringBuilder.append(", ");
        stringBuilder.append(Format.toLogString(format));
        logd(eventTime, "decoderInputFormatChanged", stringBuilder.toString());
    }

    public void onDecoderDisabled(EventTime eventTime, int i, DecoderCounters decoderCounters) {
        logd(eventTime, "decoderDisabled", getTrackTypeString(i));
    }

    public void onAudioUnderrun(EventTime eventTime, int i, long j, long j2) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(i);
        String str = ", ";
        stringBuilder.append(str);
        stringBuilder.append(j);
        stringBuilder.append(str);
        stringBuilder.append(j2);
        stringBuilder.append("]");
        loge(eventTime, "audioTrackUnderrun", stringBuilder.toString(), null);
    }

    public void onDroppedVideoFrames(EventTime eventTime, int i, long j) {
        logd(eventTime, "droppedFrames", Integer.toString(i));
    }

    public void onVideoSizeChanged(EventTime eventTime, int i, int i2, int i3, float f) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(i);
        stringBuilder.append(", ");
        stringBuilder.append(i2);
        logd(eventTime, "videoSizeChanged", stringBuilder.toString());
    }

    public void onRenderedFirstFrame(EventTime eventTime, Surface surface) {
        logd(eventTime, "renderedFirstFrame", String.valueOf(surface));
    }

    public void onMediaPeriodCreated(EventTime eventTime) {
        logd(eventTime, "mediaPeriodCreated");
    }

    public void onMediaPeriodReleased(EventTime eventTime) {
        logd(eventTime, "mediaPeriodReleased");
    }

    public void onLoadError(EventTime eventTime, LoadEventInfo loadEventInfo, MediaLoadData mediaLoadData, IOException iOException, boolean z) {
        printInternalError(eventTime, "loadError", iOException);
    }

    public void onReadingStarted(EventTime eventTime) {
        logd(eventTime, "mediaPeriodReadingStarted");
    }

    public void onSurfaceSizeChanged(EventTime eventTime, int i, int i2) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(i);
        stringBuilder.append(", ");
        stringBuilder.append(i2);
        logd(eventTime, "surfaceSizeChanged", stringBuilder.toString());
    }

    public void onUpstreamDiscarded(EventTime eventTime, MediaLoadData mediaLoadData) {
        logd(eventTime, "upstreamDiscarded", Format.toLogString(mediaLoadData.trackFormat));
    }

    public void onDownstreamFormatChanged(EventTime eventTime, MediaLoadData mediaLoadData) {
        logd(eventTime, "downstreamFormatChanged", Format.toLogString(mediaLoadData.trackFormat));
    }

    public void onDrmSessionAcquired(EventTime eventTime) {
        logd(eventTime, "drmSessionAcquired");
    }

    public void onDrmSessionManagerError(EventTime eventTime, Exception exception) {
        printInternalError(eventTime, "drmSessionManagerError", exception);
    }

    public void onDrmKeysRestored(EventTime eventTime) {
        logd(eventTime, "drmKeysRestored");
    }

    public void onDrmKeysRemoved(EventTime eventTime) {
        logd(eventTime, "drmKeysRemoved");
    }

    public void onDrmKeysLoaded(EventTime eventTime) {
        logd(eventTime, "drmKeysLoaded");
    }

    public void onDrmSessionReleased(EventTime eventTime) {
        logd(eventTime, "drmSessionReleased");
    }

    /* Access modifiers changed, original: protected */
    public void logd(String str) {
        Log.m12d(this.tag, str);
    }

    /* Access modifiers changed, original: protected */
    public void loge(String str, Throwable th) {
        Log.m15e(this.tag, str, th);
    }

    private void logd(EventTime eventTime, String str) {
        logd(getEventString(eventTime, str));
    }

    private void logd(EventTime eventTime, String str, String str2) {
        logd(getEventString(eventTime, str, str2));
    }

    private void loge(EventTime eventTime, String str, Throwable th) {
        loge(getEventString(eventTime, str), th);
    }

    private void loge(EventTime eventTime, String str, String str2, Throwable th) {
        loge(getEventString(eventTime, str, str2), th);
    }

    private void printInternalError(EventTime eventTime, String str, Exception exception) {
        loge(eventTime, "internalError", str, exception);
    }

    private void printMetadata(Metadata metadata, String str) {
        for (int i = 0; i < metadata.length(); i++) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(str);
            stringBuilder.append(metadata.get(i));
            logd(stringBuilder.toString());
        }
    }

    private String getEventString(EventTime eventTime, String str) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(str);
        stringBuilder.append(" [");
        stringBuilder.append(getEventTimeString(eventTime));
        stringBuilder.append("]");
        return stringBuilder.toString();
    }

    private String getEventString(EventTime eventTime, String str, String str2) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(str);
        stringBuilder.append(" [");
        stringBuilder.append(getEventTimeString(eventTime));
        stringBuilder.append(", ");
        stringBuilder.append(str2);
        stringBuilder.append("]");
        return stringBuilder.toString();
    }

    private String getEventTimeString(EventTime eventTime) {
        StringBuilder stringBuilder;
        StringBuilder stringBuilder2 = new StringBuilder();
        stringBuilder2.append("window=");
        stringBuilder2.append(eventTime.windowIndex);
        String stringBuilder3 = stringBuilder2.toString();
        if (eventTime.mediaPeriodId != null) {
            stringBuilder = new StringBuilder();
            stringBuilder.append(stringBuilder3);
            stringBuilder.append(", period=");
            stringBuilder.append(eventTime.timeline.getIndexOfPeriod(eventTime.mediaPeriodId.periodUid));
            stringBuilder3 = stringBuilder.toString();
            if (eventTime.mediaPeriodId.isAd()) {
                stringBuilder = new StringBuilder();
                stringBuilder.append(stringBuilder3);
                stringBuilder.append(", adGroup=");
                stringBuilder.append(eventTime.mediaPeriodId.adGroupIndex);
                stringBuilder3 = stringBuilder.toString();
                stringBuilder = new StringBuilder();
                stringBuilder.append(stringBuilder3);
                stringBuilder.append(", ad=");
                stringBuilder.append(eventTime.mediaPeriodId.adIndexInAdGroup);
                stringBuilder3 = stringBuilder.toString();
            }
        }
        stringBuilder = new StringBuilder();
        stringBuilder.append(getTimeString(eventTime.realtimeMs - this.startTimeMs));
        String str = ", ";
        stringBuilder.append(str);
        stringBuilder.append(getTimeString(eventTime.currentPlaybackPositionMs));
        stringBuilder.append(str);
        stringBuilder.append(stringBuilder3);
        return stringBuilder.toString();
    }

    private static String getTimeString(long j) {
        return j == -9223372036854775807L ? "?" : TIME_FORMAT.format((double) (((float) j) / 1000.0f));
    }

    private static String getTrackStatusString(TrackSelection trackSelection, TrackGroup trackGroup, int i) {
        boolean z = (trackSelection == null || trackSelection.getTrackGroup() != trackGroup || trackSelection.indexOf(i) == -1) ? false : true;
        return getTrackStatusString(z);
    }

    private static String getTrackTypeString(int i) {
        switch (i) {
            case 0:
                return "default";
            case 1:
                return MimeTypes.BASE_TYPE_AUDIO;
            case 2:
                return MimeTypes.BASE_TYPE_VIDEO;
            case 3:
                return MimeTypes.BASE_TYPE_TEXT;
            case 4:
                return "metadata";
            case 5:
                return "camera motion";
            case 6:
                return "none";
            default:
                String stringBuilder;
                if (i >= 10000) {
                    StringBuilder stringBuilder2 = new StringBuilder();
                    stringBuilder2.append("custom (");
                    stringBuilder2.append(i);
                    stringBuilder2.append(")");
                    stringBuilder = stringBuilder2.toString();
                } else {
                    stringBuilder = "?";
                }
                return stringBuilder;
        }
    }
}
