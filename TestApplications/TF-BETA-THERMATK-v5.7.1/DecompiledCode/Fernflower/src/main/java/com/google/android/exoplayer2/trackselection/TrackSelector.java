package com.google.android.exoplayer2.trackselection;

import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.RendererCapabilities;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.util.Assertions;

public abstract class TrackSelector {
   private BandwidthMeter bandwidthMeter;
   private TrackSelector.InvalidationListener listener;

   protected final BandwidthMeter getBandwidthMeter() {
      BandwidthMeter var1 = this.bandwidthMeter;
      Assertions.checkNotNull(var1);
      return (BandwidthMeter)var1;
   }

   public final void init(TrackSelector.InvalidationListener var1, BandwidthMeter var2) {
      this.listener = var1;
      this.bandwidthMeter = var2;
   }

   public abstract void onSelectionActivated(Object var1);

   public abstract TrackSelectorResult selectTracks(RendererCapabilities[] var1, TrackGroupArray var2, MediaSource.MediaPeriodId var3, Timeline var4) throws ExoPlaybackException;

   public interface InvalidationListener {
   }
}
