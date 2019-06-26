// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.util;

import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.source.TrackGroupArray;
import android.view.Surface;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.PlaybackParameters;
import java.io.IOException;
import com.google.android.exoplayer2.source.MediaSourceEventListener;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.decoder.DecoderCounters;
import com.google.android.exoplayer2.analytics.AnalyticsListener$_CC;
import com.google.android.exoplayer2.audio.AudioAttributes;
import com.google.android.exoplayer2.metadata.Metadata;
import com.google.android.exoplayer2.source.TrackGroup;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import android.os.SystemClock;
import java.util.Locale;
import com.google.android.exoplayer2.trackselection.MappingTrackSelector;
import com.google.android.exoplayer2.Timeline;
import java.text.NumberFormat;
import com.google.android.exoplayer2.analytics.AnalyticsListener;

public class EventLogger implements AnalyticsListener
{
    private static final String DEFAULT_TAG = "EventLogger";
    private static final int MAX_TIMELINE_ITEM_LINES = 3;
    private static final NumberFormat TIME_FORMAT;
    private final Timeline.Period period;
    private final long startTimeMs;
    private final String tag;
    private final MappingTrackSelector trackSelector;
    private final Timeline.Window window;
    
    static {
        (TIME_FORMAT = NumberFormat.getInstance(Locale.US)).setMinimumFractionDigits(2);
        EventLogger.TIME_FORMAT.setMaximumFractionDigits(2);
        EventLogger.TIME_FORMAT.setGroupingUsed(false);
    }
    
    public EventLogger(final MappingTrackSelector mappingTrackSelector) {
        this(mappingTrackSelector, "EventLogger");
    }
    
    public EventLogger(final MappingTrackSelector trackSelector, final String tag) {
        this.trackSelector = trackSelector;
        this.tag = tag;
        this.window = new Timeline.Window();
        this.period = new Timeline.Period();
        this.startTimeMs = SystemClock.elapsedRealtime();
    }
    
    private static String getAdaptiveSupportString(final int n, final int n2) {
        if (n < 2) {
            return "N/A";
        }
        if (n2 == 0) {
            return "NO";
        }
        if (n2 == 8) {
            return "YES_NOT_SEAMLESS";
        }
        if (n2 != 16) {
            return "?";
        }
        return "YES";
    }
    
    private static String getDiscontinuityReasonString(final int n) {
        if (n == 0) {
            return "PERIOD_TRANSITION";
        }
        if (n == 1) {
            return "SEEK";
        }
        if (n == 2) {
            return "SEEK_ADJUSTMENT";
        }
        if (n == 3) {
            return "AD_INSERTION";
        }
        if (n != 4) {
            return "?";
        }
        return "INTERNAL";
    }
    
    private String getEventString(final EventTime eventTime, final String str) {
        final StringBuilder sb = new StringBuilder();
        sb.append(str);
        sb.append(" [");
        sb.append(this.getEventTimeString(eventTime));
        sb.append("]");
        return sb.toString();
    }
    
    private String getEventString(final EventTime eventTime, final String str, final String str2) {
        final StringBuilder sb = new StringBuilder();
        sb.append(str);
        sb.append(" [");
        sb.append(this.getEventTimeString(eventTime));
        sb.append(", ");
        sb.append(str2);
        sb.append("]");
        return sb.toString();
    }
    
    private String getEventTimeString(final EventTime eventTime) {
        final StringBuilder sb = new StringBuilder();
        sb.append("window=");
        sb.append(eventTime.windowIndex);
        String s = sb.toString();
        if (eventTime.mediaPeriodId != null) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append(s);
            sb2.append(", period=");
            sb2.append(eventTime.timeline.getIndexOfPeriod(eventTime.mediaPeriodId.periodUid));
            final String str = s = sb2.toString();
            if (eventTime.mediaPeriodId.isAd()) {
                final StringBuilder sb3 = new StringBuilder();
                sb3.append(str);
                sb3.append(", adGroup=");
                sb3.append(eventTime.mediaPeriodId.adGroupIndex);
                final String string = sb3.toString();
                final StringBuilder sb4 = new StringBuilder();
                sb4.append(string);
                sb4.append(", ad=");
                sb4.append(eventTime.mediaPeriodId.adIndexInAdGroup);
                s = sb4.toString();
            }
        }
        final StringBuilder sb5 = new StringBuilder();
        sb5.append(getTimeString(eventTime.realtimeMs - this.startTimeMs));
        sb5.append(", ");
        sb5.append(getTimeString(eventTime.currentPlaybackPositionMs));
        sb5.append(", ");
        sb5.append(s);
        return sb5.toString();
    }
    
    private static String getFormatSupportString(final int n) {
        if (n == 0) {
            return "NO";
        }
        if (n == 1) {
            return "NO_UNSUPPORTED_TYPE";
        }
        if (n == 2) {
            return "NO_UNSUPPORTED_DRM";
        }
        if (n == 3) {
            return "NO_EXCEEDS_CAPABILITIES";
        }
        if (n != 4) {
            return "?";
        }
        return "YES";
    }
    
    private static String getRepeatModeString(final int n) {
        if (n == 0) {
            return "OFF";
        }
        if (n == 1) {
            return "ONE";
        }
        if (n != 2) {
            return "?";
        }
        return "ALL";
    }
    
    private static String getStateString(final int n) {
        if (n == 1) {
            return "IDLE";
        }
        if (n == 2) {
            return "BUFFERING";
        }
        if (n == 3) {
            return "READY";
        }
        if (n != 4) {
            return "?";
        }
        return "ENDED";
    }
    
    private static String getTimeString(final long n) {
        String format;
        if (n == -9223372036854775807L) {
            format = "?";
        }
        else {
            format = EventLogger.TIME_FORMAT.format(n / 1000.0f);
        }
        return format;
    }
    
    private static String getTimelineChangeReasonString(final int n) {
        if (n == 0) {
            return "PREPARED";
        }
        if (n == 1) {
            return "RESET";
        }
        if (n != 2) {
            return "?";
        }
        return "DYNAMIC";
    }
    
    private static String getTrackStatusString(final TrackSelection trackSelection, final TrackGroup trackGroup, final int n) {
        return getTrackStatusString(trackSelection != null && trackSelection.getTrackGroup() == trackGroup && trackSelection.indexOf(n) != -1);
    }
    
    private static String getTrackStatusString(final boolean b) {
        String s;
        if (b) {
            s = "[X]";
        }
        else {
            s = "[ ]";
        }
        return s;
    }
    
    private static String getTrackTypeString(final int i) {
        switch (i) {
            default: {
                String string;
                if (i >= 10000) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("custom (");
                    sb.append(i);
                    sb.append(")");
                    string = sb.toString();
                }
                else {
                    string = "?";
                }
                return string;
            }
            case 6: {
                return "none";
            }
            case 5: {
                return "camera motion";
            }
            case 4: {
                return "metadata";
            }
            case 3: {
                return "text";
            }
            case 2: {
                return "video";
            }
            case 1: {
                return "audio";
            }
            case 0: {
                return "default";
            }
        }
    }
    
    private void logd(final EventTime eventTime, final String s) {
        this.logd(this.getEventString(eventTime, s));
    }
    
    private void logd(final EventTime eventTime, final String s, final String s2) {
        this.logd(this.getEventString(eventTime, s, s2));
    }
    
    private void loge(final EventTime eventTime, final String s, final String s2, final Throwable t) {
        this.loge(this.getEventString(eventTime, s, s2), t);
    }
    
    private void loge(final EventTime eventTime, final String s, final Throwable t) {
        this.loge(this.getEventString(eventTime, s), t);
    }
    
    private void printInternalError(final EventTime eventTime, final String s, final Exception ex) {
        this.loge(eventTime, "internalError", s, ex);
    }
    
    private void printMetadata(final Metadata metadata, final String str) {
        for (int i = 0; i < metadata.length(); ++i) {
            final StringBuilder sb = new StringBuilder();
            sb.append(str);
            sb.append(metadata.get(i));
            this.logd(sb.toString());
        }
    }
    
    protected void logd(final String s) {
        Log.d(this.tag, s);
    }
    
    protected void loge(final String s, final Throwable t) {
        Log.e(this.tag, s, t);
    }
    
    @Override
    public void onAudioSessionId(final EventTime eventTime, final int i) {
        this.logd(eventTime, "audioSessionId", Integer.toString(i));
    }
    
    @Override
    public void onAudioUnderrun(final EventTime eventTime, final int i, final long lng, final long lng2) {
        final StringBuilder sb = new StringBuilder();
        sb.append(i);
        sb.append(", ");
        sb.append(lng);
        sb.append(", ");
        sb.append(lng2);
        sb.append("]");
        this.loge(eventTime, "audioTrackUnderrun", sb.toString(), null);
    }
    
    @Override
    public void onBandwidthEstimate(final EventTime eventTime, final int n, final long n2, final long n3) {
    }
    
    @Override
    public void onDecoderDisabled(final EventTime eventTime, final int n, final DecoderCounters decoderCounters) {
        this.logd(eventTime, "decoderDisabled", getTrackTypeString(n));
    }
    
    @Override
    public void onDecoderEnabled(final EventTime eventTime, final int n, final DecoderCounters decoderCounters) {
        this.logd(eventTime, "decoderEnabled", getTrackTypeString(n));
    }
    
    @Override
    public void onDecoderInitialized(final EventTime eventTime, final int n, final String str, final long n2) {
        final StringBuilder sb = new StringBuilder();
        sb.append(getTrackTypeString(n));
        sb.append(", ");
        sb.append(str);
        this.logd(eventTime, "decoderInitialized", sb.toString());
    }
    
    @Override
    public void onDecoderInputFormatChanged(final EventTime eventTime, final int n, final Format format) {
        final StringBuilder sb = new StringBuilder();
        sb.append(getTrackTypeString(n));
        sb.append(", ");
        sb.append(Format.toLogString(format));
        this.logd(eventTime, "decoderInputFormatChanged", sb.toString());
    }
    
    @Override
    public void onDownstreamFormatChanged(final EventTime eventTime, final MediaSourceEventListener.MediaLoadData mediaLoadData) {
        this.logd(eventTime, "downstreamFormatChanged", Format.toLogString(mediaLoadData.trackFormat));
    }
    
    @Override
    public void onDrmKeysLoaded(final EventTime eventTime) {
        this.logd(eventTime, "drmKeysLoaded");
    }
    
    public void onDrmKeysRemoved(final EventTime eventTime) {
        this.logd(eventTime, "drmKeysRemoved");
    }
    
    @Override
    public void onDrmKeysRestored(final EventTime eventTime) {
        this.logd(eventTime, "drmKeysRestored");
    }
    
    @Override
    public void onDrmSessionAcquired(final EventTime eventTime) {
        this.logd(eventTime, "drmSessionAcquired");
    }
    
    @Override
    public void onDrmSessionManagerError(final EventTime eventTime, final Exception ex) {
        this.printInternalError(eventTime, "drmSessionManagerError", ex);
    }
    
    @Override
    public void onDrmSessionReleased(final EventTime eventTime) {
        this.logd(eventTime, "drmSessionReleased");
    }
    
    @Override
    public void onDroppedVideoFrames(final EventTime eventTime, final int i, final long n) {
        this.logd(eventTime, "droppedFrames", Integer.toString(i));
    }
    
    @Override
    public void onLoadCanceled(final EventTime eventTime, final MediaSourceEventListener.LoadEventInfo loadEventInfo, final MediaSourceEventListener.MediaLoadData mediaLoadData) {
    }
    
    @Override
    public void onLoadCompleted(final EventTime eventTime, final MediaSourceEventListener.LoadEventInfo loadEventInfo, final MediaSourceEventListener.MediaLoadData mediaLoadData) {
    }
    
    @Override
    public void onLoadError(final EventTime eventTime, final MediaSourceEventListener.LoadEventInfo loadEventInfo, final MediaSourceEventListener.MediaLoadData mediaLoadData, final IOException ex, final boolean b) {
        this.printInternalError(eventTime, "loadError", ex);
    }
    
    @Override
    public void onLoadStarted(final EventTime eventTime, final MediaSourceEventListener.LoadEventInfo loadEventInfo, final MediaSourceEventListener.MediaLoadData mediaLoadData) {
    }
    
    @Override
    public void onLoadingChanged(final EventTime eventTime, final boolean b) {
        this.logd(eventTime, "loading", Boolean.toString(b));
    }
    
    @Override
    public void onMediaPeriodCreated(final EventTime eventTime) {
        this.logd(eventTime, "mediaPeriodCreated");
    }
    
    @Override
    public void onMediaPeriodReleased(final EventTime eventTime) {
        this.logd(eventTime, "mediaPeriodReleased");
    }
    
    @Override
    public void onMetadata(final EventTime eventTime, final Metadata metadata) {
        final StringBuilder sb = new StringBuilder();
        sb.append("metadata [");
        sb.append(this.getEventTimeString(eventTime));
        sb.append(", ");
        this.logd(sb.toString());
        this.printMetadata(metadata, "  ");
        this.logd("]");
    }
    
    @Override
    public void onPlaybackParametersChanged(final EventTime eventTime, final PlaybackParameters playbackParameters) {
        this.logd(eventTime, "playbackParameters", Util.formatInvariant("speed=%.2f, pitch=%.2f, skipSilence=%s", playbackParameters.speed, playbackParameters.pitch, playbackParameters.skipSilence));
    }
    
    @Override
    public void onPlayerError(final EventTime eventTime, final ExoPlaybackException ex) {
        this.loge(eventTime, "playerFailed", ex);
    }
    
    @Override
    public void onPlayerStateChanged(final EventTime eventTime, final boolean b, final int n) {
        final StringBuilder sb = new StringBuilder();
        sb.append(b);
        sb.append(", ");
        sb.append(getStateString(n));
        this.logd(eventTime, "state", sb.toString());
    }
    
    @Override
    public void onPositionDiscontinuity(final EventTime eventTime, final int n) {
        this.logd(eventTime, "positionDiscontinuity", getDiscontinuityReasonString(n));
    }
    
    @Override
    public void onReadingStarted(final EventTime eventTime) {
        this.logd(eventTime, "mediaPeriodReadingStarted");
    }
    
    @Override
    public void onRenderedFirstFrame(final EventTime eventTime, final Surface obj) {
        this.logd(eventTime, "renderedFirstFrame", String.valueOf(obj));
    }
    
    public void onRepeatModeChanged(final EventTime eventTime, final int n) {
        this.logd(eventTime, "repeatMode", getRepeatModeString(n));
    }
    
    @Override
    public void onSeekProcessed(final EventTime eventTime) {
        this.logd(eventTime, "seekProcessed");
    }
    
    @Override
    public void onSeekStarted(final EventTime eventTime) {
        this.logd(eventTime, "seekStarted");
    }
    
    public void onShuffleModeChanged(final EventTime eventTime, final boolean b) {
        this.logd(eventTime, "shuffleModeEnabled", Boolean.toString(b));
    }
    
    @Override
    public void onSurfaceSizeChanged(final EventTime eventTime, final int i, final int j) {
        final StringBuilder sb = new StringBuilder();
        sb.append(i);
        sb.append(", ");
        sb.append(j);
        this.logd(eventTime, "surfaceSizeChanged", sb.toString());
    }
    
    @Override
    public void onTimelineChanged(final EventTime eventTime, int i) {
        final int periodCount = eventTime.timeline.getPeriodCount();
        final int windowCount = eventTime.timeline.getWindowCount();
        final StringBuilder sb = new StringBuilder();
        sb.append("timelineChanged [");
        sb.append(this.getEventTimeString(eventTime));
        sb.append(", periodCount=");
        sb.append(periodCount);
        sb.append(", windowCount=");
        sb.append(windowCount);
        sb.append(", reason=");
        sb.append(getTimelineChangeReasonString(i));
        this.logd(sb.toString());
        final int n = 0;
        StringBuilder sb2;
        for (i = 0; i < Math.min(periodCount, 3); ++i) {
            eventTime.timeline.getPeriod(i, this.period);
            sb2 = new StringBuilder();
            sb2.append("  period [");
            sb2.append(getTimeString(this.period.getDurationMs()));
            sb2.append("]");
            this.logd(sb2.toString());
        }
        i = n;
        if (periodCount > 3) {
            this.logd("  ...");
            i = n;
        }
        while (i < Math.min(windowCount, 3)) {
            eventTime.timeline.getWindow(i, this.window);
            final StringBuilder sb3 = new StringBuilder();
            sb3.append("  window [");
            sb3.append(getTimeString(this.window.getDurationMs()));
            sb3.append(", ");
            sb3.append(this.window.isSeekable);
            sb3.append(", ");
            sb3.append(this.window.isDynamic);
            sb3.append("]");
            this.logd(sb3.toString());
            ++i;
        }
        if (windowCount > 3) {
            this.logd("  ...");
        }
        this.logd("]");
    }
    
    @Override
    public void onTracksChanged(final EventTime eventTime, TrackGroupArray unmappedTrackGroups, final TrackSelectionArray trackSelectionArray) {
        final MappingTrackSelector trackSelector = this.trackSelector;
        MappingTrackSelector.MappedTrackInfo currentMappedTrackInfo;
        if (trackSelector != null) {
            currentMappedTrackInfo = trackSelector.getCurrentMappedTrackInfo();
        }
        else {
            currentMappedTrackInfo = null;
        }
        if (currentMappedTrackInfo == null) {
            this.logd(eventTime, "tracksChanged", "[]");
            return;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("tracksChanged [");
        sb.append(this.getEventTimeString(eventTime));
        sb.append(", ");
        this.logd(sb.toString());
        final int rendererCount = currentMappedTrackInfo.getRendererCount();
        int i = 0;
        while (true) {
            final String s = "  ]";
            final String str = " [";
            if (i >= rendererCount) {
                break;
            }
            final TrackGroupArray trackGroups = currentMappedTrackInfo.getTrackGroups(i);
            final TrackSelection value = trackSelectionArray.get(i);
            if (trackGroups.length > 0) {
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("  Renderer:");
                sb2.append(i);
                sb2.append(" [");
                this.logd(sb2.toString());
                for (int j = 0; j < trackGroups.length; ++j) {
                    final TrackGroup value2 = trackGroups.get(j);
                    final String adaptiveSupportString = getAdaptiveSupportString(value2.length, currentMappedTrackInfo.getAdaptiveSupport(i, j, false));
                    final StringBuilder sb3 = new StringBuilder();
                    sb3.append("    Group:");
                    sb3.append(j);
                    sb3.append(", adaptive_supported=");
                    sb3.append(adaptiveSupportString);
                    sb3.append(str);
                    this.logd(sb3.toString());
                    for (int k = 0; k < value2.length; ++k) {
                        final String trackStatusString = getTrackStatusString(value, value2, k);
                        final String formatSupportString = getFormatSupportString(currentMappedTrackInfo.getTrackSupport(i, j, k));
                        final StringBuilder sb4 = new StringBuilder();
                        sb4.append("      ");
                        sb4.append(trackStatusString);
                        sb4.append(" Track:");
                        sb4.append(k);
                        sb4.append(", ");
                        sb4.append(Format.toLogString(value2.getFormat(k)));
                        sb4.append(", supported=");
                        sb4.append(formatSupportString);
                        this.logd(sb4.toString());
                    }
                    this.logd("    ]");
                }
                if (value != null) {
                    for (int l = 0; l < value.length(); ++l) {
                        final Metadata metadata = value.getFormat(l).metadata;
                        if (metadata != null) {
                            this.logd("    Metadata [");
                            this.printMetadata(metadata, "      ");
                            this.logd("    ]");
                            break;
                        }
                    }
                }
                this.logd(s);
            }
            ++i;
        }
        final String str2 = " [";
        unmappedTrackGroups = currentMappedTrackInfo.getUnmappedTrackGroups();
        if (unmappedTrackGroups.length > 0) {
            this.logd("  Renderer:None [");
            for (int m = 0; m < unmappedTrackGroups.length; ++m) {
                final StringBuilder sb5 = new StringBuilder();
                sb5.append("    Group:");
                sb5.append(m);
                sb5.append(str2);
                this.logd(sb5.toString());
                final TrackGroup value3 = unmappedTrackGroups.get(m);
                for (int i2 = 0; i2 < value3.length; ++i2) {
                    final String trackStatusString2 = getTrackStatusString(false);
                    final String formatSupportString2 = getFormatSupportString(0);
                    final StringBuilder sb6 = new StringBuilder();
                    sb6.append("      ");
                    sb6.append(trackStatusString2);
                    sb6.append(" Track:");
                    sb6.append(i2);
                    sb6.append(", ");
                    sb6.append(Format.toLogString(value3.getFormat(i2)));
                    sb6.append(", supported=");
                    sb6.append(formatSupportString2);
                    this.logd(sb6.toString());
                }
                this.logd("    ]");
            }
            this.logd("  ]");
        }
        this.logd("]");
    }
    
    @Override
    public void onUpstreamDiscarded(final EventTime eventTime, final MediaSourceEventListener.MediaLoadData mediaLoadData) {
        this.logd(eventTime, "upstreamDiscarded", Format.toLogString(mediaLoadData.trackFormat));
    }
    
    @Override
    public void onVideoSizeChanged(final EventTime eventTime, final int i, final int j, final int n, final float n2) {
        final StringBuilder sb = new StringBuilder();
        sb.append(i);
        sb.append(", ");
        sb.append(j);
        this.logd(eventTime, "videoSizeChanged", sb.toString());
    }
}
