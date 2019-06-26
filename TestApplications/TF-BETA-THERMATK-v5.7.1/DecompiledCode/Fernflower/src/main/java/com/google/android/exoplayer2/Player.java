package com.google.android.exoplayer2;

import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;

public interface Player {
   long getBufferedPosition();

   long getContentPosition();

   int getCurrentAdGroupIndex();

   int getCurrentAdIndexInAdGroup();

   long getCurrentPosition();

   Timeline getCurrentTimeline();

   int getCurrentWindowIndex();

   long getDuration();

   long getTotalBufferedDuration();

   void seekTo(int var1, long var2);

   public interface AudioComponent {
   }

   public interface EventListener {
      void onLoadingChanged(boolean var1);

      void onPlaybackParametersChanged(PlaybackParameters var1);

      void onPlayerError(ExoPlaybackException var1);

      void onPlayerStateChanged(boolean var1, int var2);

      void onPositionDiscontinuity(int var1);

      void onSeekProcessed();

      void onTimelineChanged(Timeline var1, Object var2, int var3);

      void onTracksChanged(TrackGroupArray var1, TrackSelectionArray var2);
   }

   public interface MetadataComponent {
   }

   public interface TextComponent {
   }

   public interface VideoComponent {
   }
}
