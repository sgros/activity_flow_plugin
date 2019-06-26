package com.google.android.exoplayer2.util;

import android.view.Surface;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.analytics.AnalyticsListener;
import com.google.android.exoplayer2.analytics.AnalyticsListener$_CC;
import com.google.android.exoplayer2.audio.AudioAttributes;
import com.google.android.exoplayer2.decoder.DecoderCounters;
import com.google.android.exoplayer2.metadata.Metadata;
import com.google.android.exoplayer2.source.MediaSourceEventListener;
import com.google.android.exoplayer2.source.TrackGroup;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.MappingTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.Locale;

public class EventLogger implements AnalyticsListener {
   private static final String DEFAULT_TAG = "EventLogger";
   private static final int MAX_TIMELINE_ITEM_LINES = 3;
   private static final NumberFormat TIME_FORMAT;
   private final Timeline.Period period;
   private final long startTimeMs;
   private final String tag;
   private final MappingTrackSelector trackSelector;
   private final Timeline.Window window;

   static {
      TIME_FORMAT = NumberFormat.getInstance(Locale.US);
      TIME_FORMAT.setMinimumFractionDigits(2);
      TIME_FORMAT.setMaximumFractionDigits(2);
      TIME_FORMAT.setGroupingUsed(false);
   }

   public EventLogger(MappingTrackSelector var1) {
      this(var1, "EventLogger");
   }

   public EventLogger(MappingTrackSelector var1, String var2) {
      this.trackSelector = var1;
      this.tag = var2;
      this.window = new Timeline.Window();
      this.period = new Timeline.Period();
      this.startTimeMs = android.os.SystemClock.elapsedRealtime();
   }

   private static String getAdaptiveSupportString(int var0, int var1) {
      if (var0 < 2) {
         return "N/A";
      } else if (var1 != 0) {
         if (var1 != 8) {
            return var1 != 16 ? "?" : "YES";
         } else {
            return "YES_NOT_SEAMLESS";
         }
      } else {
         return "NO";
      }
   }

   private static String getDiscontinuityReasonString(int var0) {
      if (var0 != 0) {
         if (var0 != 1) {
            if (var0 != 2) {
               if (var0 != 3) {
                  return var0 != 4 ? "?" : "INTERNAL";
               } else {
                  return "AD_INSERTION";
               }
            } else {
               return "SEEK_ADJUSTMENT";
            }
         } else {
            return "SEEK";
         }
      } else {
         return "PERIOD_TRANSITION";
      }
   }

   private String getEventString(AnalyticsListener.EventTime var1, String var2) {
      StringBuilder var3 = new StringBuilder();
      var3.append(var2);
      var3.append(" [");
      var3.append(this.getEventTimeString(var1));
      var3.append("]");
      return var3.toString();
   }

   private String getEventString(AnalyticsListener.EventTime var1, String var2, String var3) {
      StringBuilder var4 = new StringBuilder();
      var4.append(var2);
      var4.append(" [");
      var4.append(this.getEventTimeString(var1));
      var4.append(", ");
      var4.append(var3);
      var4.append("]");
      return var4.toString();
   }

   private String getEventTimeString(AnalyticsListener.EventTime var1) {
      StringBuilder var2 = new StringBuilder();
      var2.append("window=");
      var2.append(var1.windowIndex);
      String var3 = var2.toString();
      String var4 = var3;
      StringBuilder var5;
      if (var1.mediaPeriodId != null) {
         var2 = new StringBuilder();
         var2.append(var3);
         var2.append(", period=");
         var2.append(var1.timeline.getIndexOfPeriod(var1.mediaPeriodId.periodUid));
         var3 = var2.toString();
         var4 = var3;
         if (var1.mediaPeriodId.isAd()) {
            var2 = new StringBuilder();
            var2.append(var3);
            var2.append(", adGroup=");
            var2.append(var1.mediaPeriodId.adGroupIndex);
            var4 = var2.toString();
            var5 = new StringBuilder();
            var5.append(var4);
            var5.append(", ad=");
            var5.append(var1.mediaPeriodId.adIndexInAdGroup);
            var4 = var5.toString();
         }
      }

      var5 = new StringBuilder();
      var5.append(getTimeString(var1.realtimeMs - this.startTimeMs));
      var5.append(", ");
      var5.append(getTimeString(var1.currentPlaybackPositionMs));
      var5.append(", ");
      var5.append(var4);
      return var5.toString();
   }

   private static String getFormatSupportString(int var0) {
      if (var0 != 0) {
         if (var0 != 1) {
            if (var0 != 2) {
               if (var0 != 3) {
                  return var0 != 4 ? "?" : "YES";
               } else {
                  return "NO_EXCEEDS_CAPABILITIES";
               }
            } else {
               return "NO_UNSUPPORTED_DRM";
            }
         } else {
            return "NO_UNSUPPORTED_TYPE";
         }
      } else {
         return "NO";
      }
   }

   private static String getRepeatModeString(int var0) {
      if (var0 != 0) {
         if (var0 != 1) {
            return var0 != 2 ? "?" : "ALL";
         } else {
            return "ONE";
         }
      } else {
         return "OFF";
      }
   }

   private static String getStateString(int var0) {
      if (var0 != 1) {
         if (var0 != 2) {
            if (var0 != 3) {
               return var0 != 4 ? "?" : "ENDED";
            } else {
               return "READY";
            }
         } else {
            return "BUFFERING";
         }
      } else {
         return "IDLE";
      }
   }

   private static String getTimeString(long var0) {
      String var2;
      if (var0 == -9223372036854775807L) {
         var2 = "?";
      } else {
         var2 = TIME_FORMAT.format((double)((float)var0 / 1000.0F));
      }

      return var2;
   }

   private static String getTimelineChangeReasonString(int var0) {
      if (var0 != 0) {
         if (var0 != 1) {
            return var0 != 2 ? "?" : "DYNAMIC";
         } else {
            return "RESET";
         }
      } else {
         return "PREPARED";
      }
   }

   private static String getTrackStatusString(TrackSelection var0, TrackGroup var1, int var2) {
      boolean var3;
      if (var0 != null && var0.getTrackGroup() == var1 && var0.indexOf(var2) != -1) {
         var3 = true;
      } else {
         var3 = false;
      }

      return getTrackStatusString(var3);
   }

   private static String getTrackStatusString(boolean var0) {
      String var1;
      if (var0) {
         var1 = "[X]";
      } else {
         var1 = "[ ]";
      }

      return var1;
   }

   private static String getTrackTypeString(int var0) {
      switch(var0) {
      case 0:
         return "default";
      case 1:
         return "audio";
      case 2:
         return "video";
      case 3:
         return "text";
      case 4:
         return "metadata";
      case 5:
         return "camera motion";
      case 6:
         return "none";
      default:
         String var2;
         if (var0 >= 10000) {
            StringBuilder var1 = new StringBuilder();
            var1.append("custom (");
            var1.append(var0);
            var1.append(")");
            var2 = var1.toString();
         } else {
            var2 = "?";
         }

         return var2;
      }
   }

   private void logd(AnalyticsListener.EventTime var1, String var2) {
      this.logd(this.getEventString(var1, var2));
   }

   private void logd(AnalyticsListener.EventTime var1, String var2, String var3) {
      this.logd(this.getEventString(var1, var2, var3));
   }

   private void loge(AnalyticsListener.EventTime var1, String var2, String var3, Throwable var4) {
      this.loge(this.getEventString(var1, var2, var3), var4);
   }

   private void loge(AnalyticsListener.EventTime var1, String var2, Throwable var3) {
      this.loge(this.getEventString(var1, var2), var3);
   }

   private void printInternalError(AnalyticsListener.EventTime var1, String var2, Exception var3) {
      this.loge(var1, "internalError", var2, var3);
   }

   private void printMetadata(Metadata var1, String var2) {
      for(int var3 = 0; var3 < var1.length(); ++var3) {
         StringBuilder var4 = new StringBuilder();
         var4.append(var2);
         var4.append(var1.get(var3));
         this.logd(var4.toString());
      }

   }

   protected void logd(String var1) {
      Log.d(this.tag, var1);
   }

   protected void loge(String var1, Throwable var2) {
      Log.e(this.tag, var1, var2);
   }

   // $FF: synthetic method
   public void onAudioAttributesChanged(AnalyticsListener.EventTime var1, AudioAttributes var2) {
      AnalyticsListener$_CC.$default$onAudioAttributesChanged(this, var1, var2);
   }

   public void onAudioSessionId(AnalyticsListener.EventTime var1, int var2) {
      this.logd(var1, "audioSessionId", Integer.toString(var2));
   }

   public void onAudioUnderrun(AnalyticsListener.EventTime var1, int var2, long var3, long var5) {
      StringBuilder var7 = new StringBuilder();
      var7.append(var2);
      var7.append(", ");
      var7.append(var3);
      var7.append(", ");
      var7.append(var5);
      var7.append("]");
      this.loge(var1, "audioTrackUnderrun", var7.toString(), (Throwable)null);
   }

   public void onBandwidthEstimate(AnalyticsListener.EventTime var1, int var2, long var3, long var5) {
   }

   public void onDecoderDisabled(AnalyticsListener.EventTime var1, int var2, DecoderCounters var3) {
      this.logd(var1, "decoderDisabled", getTrackTypeString(var2));
   }

   public void onDecoderEnabled(AnalyticsListener.EventTime var1, int var2, DecoderCounters var3) {
      this.logd(var1, "decoderEnabled", getTrackTypeString(var2));
   }

   public void onDecoderInitialized(AnalyticsListener.EventTime var1, int var2, String var3, long var4) {
      StringBuilder var6 = new StringBuilder();
      var6.append(getTrackTypeString(var2));
      var6.append(", ");
      var6.append(var3);
      this.logd(var1, "decoderInitialized", var6.toString());
   }

   public void onDecoderInputFormatChanged(AnalyticsListener.EventTime var1, int var2, Format var3) {
      StringBuilder var4 = new StringBuilder();
      var4.append(getTrackTypeString(var2));
      var4.append(", ");
      var4.append(Format.toLogString(var3));
      this.logd(var1, "decoderInputFormatChanged", var4.toString());
   }

   public void onDownstreamFormatChanged(AnalyticsListener.EventTime var1, MediaSourceEventListener.MediaLoadData var2) {
      this.logd(var1, "downstreamFormatChanged", Format.toLogString(var2.trackFormat));
   }

   public void onDrmKeysLoaded(AnalyticsListener.EventTime var1) {
      this.logd(var1, "drmKeysLoaded");
   }

   public void onDrmKeysRemoved(AnalyticsListener.EventTime var1) {
      this.logd(var1, "drmKeysRemoved");
   }

   public void onDrmKeysRestored(AnalyticsListener.EventTime var1) {
      this.logd(var1, "drmKeysRestored");
   }

   public void onDrmSessionAcquired(AnalyticsListener.EventTime var1) {
      this.logd(var1, "drmSessionAcquired");
   }

   public void onDrmSessionManagerError(AnalyticsListener.EventTime var1, Exception var2) {
      this.printInternalError(var1, "drmSessionManagerError", var2);
   }

   public void onDrmSessionReleased(AnalyticsListener.EventTime var1) {
      this.logd(var1, "drmSessionReleased");
   }

   public void onDroppedVideoFrames(AnalyticsListener.EventTime var1, int var2, long var3) {
      this.logd(var1, "droppedFrames", Integer.toString(var2));
   }

   public void onLoadCanceled(AnalyticsListener.EventTime var1, MediaSourceEventListener.LoadEventInfo var2, MediaSourceEventListener.MediaLoadData var3) {
   }

   public void onLoadCompleted(AnalyticsListener.EventTime var1, MediaSourceEventListener.LoadEventInfo var2, MediaSourceEventListener.MediaLoadData var3) {
   }

   public void onLoadError(AnalyticsListener.EventTime var1, MediaSourceEventListener.LoadEventInfo var2, MediaSourceEventListener.MediaLoadData var3, IOException var4, boolean var5) {
      this.printInternalError(var1, "loadError", var4);
   }

   public void onLoadStarted(AnalyticsListener.EventTime var1, MediaSourceEventListener.LoadEventInfo var2, MediaSourceEventListener.MediaLoadData var3) {
   }

   public void onLoadingChanged(AnalyticsListener.EventTime var1, boolean var2) {
      this.logd(var1, "loading", Boolean.toString(var2));
   }

   public void onMediaPeriodCreated(AnalyticsListener.EventTime var1) {
      this.logd(var1, "mediaPeriodCreated");
   }

   public void onMediaPeriodReleased(AnalyticsListener.EventTime var1) {
      this.logd(var1, "mediaPeriodReleased");
   }

   public void onMetadata(AnalyticsListener.EventTime var1, Metadata var2) {
      StringBuilder var3 = new StringBuilder();
      var3.append("metadata [");
      var3.append(this.getEventTimeString(var1));
      var3.append(", ");
      this.logd(var3.toString());
      this.printMetadata(var2, "  ");
      this.logd("]");
   }

   public void onPlaybackParametersChanged(AnalyticsListener.EventTime var1, PlaybackParameters var2) {
      this.logd(var1, "playbackParameters", Util.formatInvariant("speed=%.2f, pitch=%.2f, skipSilence=%s", var2.speed, var2.pitch, var2.skipSilence));
   }

   public void onPlayerError(AnalyticsListener.EventTime var1, ExoPlaybackException var2) {
      this.loge(var1, "playerFailed", var2);
   }

   public void onPlayerStateChanged(AnalyticsListener.EventTime var1, boolean var2, int var3) {
      StringBuilder var4 = new StringBuilder();
      var4.append(var2);
      var4.append(", ");
      var4.append(getStateString(var3));
      this.logd(var1, "state", var4.toString());
   }

   public void onPositionDiscontinuity(AnalyticsListener.EventTime var1, int var2) {
      this.logd(var1, "positionDiscontinuity", getDiscontinuityReasonString(var2));
   }

   public void onReadingStarted(AnalyticsListener.EventTime var1) {
      this.logd(var1, "mediaPeriodReadingStarted");
   }

   public void onRenderedFirstFrame(AnalyticsListener.EventTime var1, Surface var2) {
      this.logd(var1, "renderedFirstFrame", String.valueOf(var2));
   }

   public void onRepeatModeChanged(AnalyticsListener.EventTime var1, int var2) {
      this.logd(var1, "repeatMode", getRepeatModeString(var2));
   }

   public void onSeekProcessed(AnalyticsListener.EventTime var1) {
      this.logd(var1, "seekProcessed");
   }

   public void onSeekStarted(AnalyticsListener.EventTime var1) {
      this.logd(var1, "seekStarted");
   }

   public void onShuffleModeChanged(AnalyticsListener.EventTime var1, boolean var2) {
      this.logd(var1, "shuffleModeEnabled", Boolean.toString(var2));
   }

   public void onSurfaceSizeChanged(AnalyticsListener.EventTime var1, int var2, int var3) {
      StringBuilder var4 = new StringBuilder();
      var4.append(var2);
      var4.append(", ");
      var4.append(var3);
      this.logd(var1, "surfaceSizeChanged", var4.toString());
   }

   public void onTimelineChanged(AnalyticsListener.EventTime var1, int var2) {
      int var3 = var1.timeline.getPeriodCount();
      int var4 = var1.timeline.getWindowCount();
      StringBuilder var5 = new StringBuilder();
      var5.append("timelineChanged [");
      var5.append(this.getEventTimeString(var1));
      var5.append(", periodCount=");
      var5.append(var3);
      var5.append(", windowCount=");
      var5.append(var4);
      var5.append(", reason=");
      var5.append(getTimelineChangeReasonString(var2));
      this.logd(var5.toString());
      byte var6 = 0;

      for(var2 = 0; var2 < Math.min(var3, 3); ++var2) {
         var1.timeline.getPeriod(var2, this.period);
         var5 = new StringBuilder();
         var5.append("  period [");
         var5.append(getTimeString(this.period.getDurationMs()));
         var5.append("]");
         this.logd(var5.toString());
      }

      var2 = var6;
      if (var3 > 3) {
         this.logd("  ...");
         var2 = var6;
      }

      while(var2 < Math.min(var4, 3)) {
         var1.timeline.getWindow(var2, this.window);
         var5 = new StringBuilder();
         var5.append("  window [");
         var5.append(getTimeString(this.window.getDurationMs()));
         var5.append(", ");
         var5.append(this.window.isSeekable);
         var5.append(", ");
         var5.append(this.window.isDynamic);
         var5.append("]");
         this.logd(var5.toString());
         ++var2;
      }

      if (var4 > 3) {
         this.logd("  ...");
      }

      this.logd("]");
   }

   public void onTracksChanged(AnalyticsListener.EventTime var1, TrackGroupArray var2, TrackSelectionArray var3) {
      MappingTrackSelector var18 = this.trackSelector;
      MappingTrackSelector.MappedTrackInfo var19;
      if (var18 != null) {
         var19 = var18.getCurrentMappedTrackInfo();
      } else {
         var19 = null;
      }

      if (var19 == null) {
         this.logd(var1, "tracksChanged", "[]");
      } else {
         StringBuilder var4 = new StringBuilder();
         var4.append("tracksChanged [");
         var4.append(this.getEventTimeString(var1));
         var4.append(", ");
         this.logd(var4.toString());
         int var5 = var19.getRendererCount();
         int var6 = 0;

         while(true) {
            String var22 = "  ]";
            String var7 = " [";
            if (var6 >= var5) {
               String var17 = " [";
               var2 = var19.getUnmappedTrackGroups();
               if (var2.length > 0) {
                  this.logd("  Renderer:None [");

                  for(var5 = 0; var5 < var2.length; ++var5) {
                     StringBuilder var20 = new StringBuilder();
                     var20.append("    Group:");
                     var20.append(var5);
                     var20.append(var17);
                     this.logd(var20.toString());
                     TrackGroup var24 = var2.get(var5);

                     for(var6 = 0; var6 < var24.length; ++var6) {
                        var22 = getTrackStatusString(false);
                        String var21 = getFormatSupportString(0);
                        StringBuilder var23 = new StringBuilder();
                        var23.append("      ");
                        var23.append(var22);
                        var23.append(" Track:");
                        var23.append(var6);
                        var23.append(", ");
                        var23.append(Format.toLogString(var24.getFormat(var6)));
                        var23.append(", supported=");
                        var23.append(var21);
                        this.logd(var23.toString());
                     }

                     this.logd("    ]");
                  }

                  this.logd("  ]");
               }

               this.logd("]");
               return;
            }

            TrackGroupArray var15 = var19.getTrackGroups(var6);
            TrackSelection var8 = var3.get(var6);
            if (var15.length > 0) {
               StringBuilder var9 = new StringBuilder();
               var9.append("  Renderer:");
               var9.append(var6);
               var9.append(" [");
               this.logd(var9.toString());
               int var10 = 0;

               while(true) {
                  if (var10 >= var15.length) {
                     if (var8 != null) {
                        for(var10 = 0; var10 < var8.length(); ++var10) {
                           Metadata var16 = var8.getFormat(var10).metadata;
                           if (var16 != null) {
                              this.logd("    Metadata [");
                              this.printMetadata(var16, "      ");
                              this.logd("    ]");
                              break;
                           }
                        }
                     }

                     this.logd(var22);
                     break;
                  }

                  TrackGroup var25 = var15.get(var10);
                  String var11 = getAdaptiveSupportString(var25.length, var19.getAdaptiveSupport(var6, var10, false));
                  StringBuilder var12 = new StringBuilder();
                  var12.append("    Group:");
                  var12.append(var10);
                  var12.append(", adaptive_supported=");
                  var12.append(var11);
                  var12.append(var7);
                  this.logd(var12.toString());

                  for(int var13 = 0; var13 < var25.length; ++var13) {
                     var11 = getTrackStatusString(var8, var25, var13);
                     String var26 = getFormatSupportString(var19.getTrackSupport(var6, var10, var13));
                     StringBuilder var14 = new StringBuilder();
                     var14.append("      ");
                     var14.append(var11);
                     var14.append(" Track:");
                     var14.append(var13);
                     var14.append(", ");
                     var14.append(Format.toLogString(var25.getFormat(var13)));
                     var14.append(", supported=");
                     var14.append(var26);
                     this.logd(var14.toString());
                  }

                  this.logd("    ]");
                  ++var10;
               }
            }

            ++var6;
         }
      }
   }

   public void onUpstreamDiscarded(AnalyticsListener.EventTime var1, MediaSourceEventListener.MediaLoadData var2) {
      this.logd(var1, "upstreamDiscarded", Format.toLogString(var2.trackFormat));
   }

   public void onVideoSizeChanged(AnalyticsListener.EventTime var1, int var2, int var3, int var4, float var5) {
      StringBuilder var6 = new StringBuilder();
      var6.append(var2);
      var6.append(", ");
      var6.append(var3);
      this.logd(var1, "videoSizeChanged", var6.toString());
   }

   // $FF: synthetic method
   public void onVolumeChanged(AnalyticsListener.EventTime var1, float var2) {
      AnalyticsListener$_CC.$default$onVolumeChanged(this, var1, var2);
   }
}
