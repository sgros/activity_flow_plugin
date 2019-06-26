package com.google.android.exoplayer2;

import android.content.Context;
import android.os.Looper;
import com.google.android.exoplayer2.analytics.AnalyticsCollector;
import com.google.android.exoplayer2.drm.DrmSessionManager;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.util.Util;

public final class ExoPlayerFactory {
   private static BandwidthMeter singletonBandwidthMeter;

   private static BandwidthMeter getDefaultBandwidthMeter(Context var0) {
      synchronized(ExoPlayerFactory.class){}

      BandwidthMeter var4;
      try {
         if (singletonBandwidthMeter == null) {
            DefaultBandwidthMeter.Builder var1 = new DefaultBandwidthMeter.Builder(var0);
            singletonBandwidthMeter = var1.build();
         }

         var4 = singletonBandwidthMeter;
      } finally {
         ;
      }

      return var4;
   }

   public static SimpleExoPlayer newSimpleInstance(Context var0, RenderersFactory var1, TrackSelector var2, LoadControl var3, DrmSessionManager var4) {
      return newSimpleInstance(var0, var1, var2, var3, var4, Util.getLooper());
   }

   public static SimpleExoPlayer newSimpleInstance(Context var0, RenderersFactory var1, TrackSelector var2, LoadControl var3, DrmSessionManager var4, Looper var5) {
      return newSimpleInstance(var0, var1, var2, var3, var4, new AnalyticsCollector.Factory(), var5);
   }

   public static SimpleExoPlayer newSimpleInstance(Context var0, RenderersFactory var1, TrackSelector var2, LoadControl var3, DrmSessionManager var4, AnalyticsCollector.Factory var5, Looper var6) {
      return newSimpleInstance(var0, var1, var2, var3, var4, getDefaultBandwidthMeter(var0), var5, var6);
   }

   public static SimpleExoPlayer newSimpleInstance(Context var0, RenderersFactory var1, TrackSelector var2, LoadControl var3, DrmSessionManager var4, BandwidthMeter var5, AnalyticsCollector.Factory var6, Looper var7) {
      return new SimpleExoPlayer(var0, var1, var2, var3, var4, var5, var6, var7);
   }

   @Deprecated
   public static SimpleExoPlayer newSimpleInstance(Context var0, TrackSelector var1, LoadControl var2, DrmSessionManager var3, int var4) {
      return newSimpleInstance(var0, new DefaultRenderersFactory(var0, var4), var1, var2, var3);
   }
}
