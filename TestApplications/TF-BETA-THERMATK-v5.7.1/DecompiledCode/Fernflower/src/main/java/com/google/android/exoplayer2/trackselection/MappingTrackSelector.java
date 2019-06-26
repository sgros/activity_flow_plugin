package com.google.android.exoplayer2.trackselection;

import android.util.Pair;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.RendererCapabilities;
import com.google.android.exoplayer2.RendererConfiguration;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroup;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.util.Util;
import java.util.Arrays;

public abstract class MappingTrackSelector extends TrackSelector {
   private MappingTrackSelector.MappedTrackInfo currentMappedTrackInfo;

   private static int findRenderer(RendererCapabilities[] var0, TrackGroup var1) throws ExoPlaybackException {
      int var2 = var0.length;
      int var3 = 0;

      int var6;
      for(int var4 = 0; var3 < var0.length; var2 = var6) {
         RendererCapabilities var5 = var0[var3];
         var6 = var2;
         byte var7 = 0;
         var2 = var4;
         var4 = var6;

         int var9;
         for(var6 = var7; var6 < var1.length; var2 = var9) {
            int var8 = var5.supportsFormat(var1.getFormat(var6)) & 7;
            var9 = var2;
            if (var8 > var2) {
               if (var8 == 4) {
                  return var3;
               }

               var4 = var3;
               var9 = var8;
            }

            ++var6;
         }

         ++var3;
         var6 = var4;
         var4 = var2;
      }

      return var2;
   }

   private static int[] getFormatSupport(RendererCapabilities var0, TrackGroup var1) throws ExoPlaybackException {
      int[] var2 = new int[var1.length];

      for(int var3 = 0; var3 < var1.length; ++var3) {
         var2[var3] = var0.supportsFormat(var1.getFormat(var3));
      }

      return var2;
   }

   private static int[] getMixedMimeTypeAdaptationSupports(RendererCapabilities[] var0) throws ExoPlaybackException {
      int[] var1 = new int[var0.length];

      for(int var2 = 0; var2 < var1.length; ++var2) {
         var1[var2] = var0[var2].supportsMixedMimeTypeAdaptation();
      }

      return var1;
   }

   public final MappingTrackSelector.MappedTrackInfo getCurrentMappedTrackInfo() {
      return this.currentMappedTrackInfo;
   }

   public final void onSelectionActivated(Object var1) {
      this.currentMappedTrackInfo = (MappingTrackSelector.MappedTrackInfo)var1;
   }

   protected abstract Pair selectTracks(MappingTrackSelector.MappedTrackInfo var1, int[][][] var2, int[] var3) throws ExoPlaybackException;

   public final TrackSelectorResult selectTracks(RendererCapabilities[] var1, TrackGroupArray var2, MediaSource.MediaPeriodId var3, Timeline var4) throws ExoPlaybackException {
      int[] var5 = new int[var1.length + 1];
      TrackGroup[][] var6 = new TrackGroup[var1.length + 1][];
      int[][][] var17 = new int[var1.length + 1][][];
      byte var7 = 0;

      int var8;
      int var9;
      for(var8 = 0; var8 < var6.length; ++var8) {
         var9 = var2.length;
         var6[var8] = new TrackGroup[var9];
         var17[var8] = new int[var9][];
      }

      int[] var10 = getMixedMimeTypeAdaptationSupports(var1);

      int[] var16;
      for(var8 = 0; var8 < var2.length; ++var8) {
         TrackGroup var11 = var2.get(var8);
         int var12 = findRenderer(var1, var11);
         if (var12 == var1.length) {
            var16 = new int[var11.length];
         } else {
            var16 = getFormatSupport(var1[var12], var11);
         }

         var9 = var5[var12];
         var6[var12][var9] = var11;
         var17[var12][var9] = var16;
         int var10002 = var5[var12]++;
      }

      TrackGroupArray[] var14 = new TrackGroupArray[var1.length];
      var16 = new int[var1.length];

      for(var8 = var7; var8 < var1.length; ++var8) {
         int var18 = var5[var8];
         var14[var8] = new TrackGroupArray((TrackGroup[])Util.nullSafeArrayCopy(var6[var8], var18));
         var17[var8] = (int[][])Util.nullSafeArrayCopy(var17[var8], var18);
         var16[var8] = var1[var8].getTrackType();
      }

      var8 = var5[var1.length];
      MappingTrackSelector.MappedTrackInfo var13 = new MappingTrackSelector.MappedTrackInfo(var16, var14, var10, var17, new TrackGroupArray((TrackGroup[])Util.nullSafeArrayCopy(var6[var1.length], var8)));
      Pair var15 = this.selectTracks(var13, var17, var10);
      return new TrackSelectorResult((RendererConfiguration[])var15.first, (TrackSelection[])var15.second, var13);
   }

   public static final class MappedTrackInfo {
      @Deprecated
      public final int length;
      private final int rendererCount;
      private final int[][][] rendererFormatSupports;
      private final int[] rendererMixedMimeTypeAdaptiveSupports;
      private final TrackGroupArray[] rendererTrackGroups;
      private final int[] rendererTrackTypes;
      private final TrackGroupArray unmappedTrackGroups;

      MappedTrackInfo(int[] var1, TrackGroupArray[] var2, int[] var3, int[][][] var4, TrackGroupArray var5) {
         this.rendererTrackTypes = var1;
         this.rendererTrackGroups = var2;
         this.rendererFormatSupports = var4;
         this.rendererMixedMimeTypeAdaptiveSupports = var3;
         this.unmappedTrackGroups = var5;
         this.rendererCount = var1.length;
         this.length = this.rendererCount;
      }

      public int getAdaptiveSupport(int var1, int var2, boolean var3) {
         int var4 = this.rendererTrackGroups[var1].get(var2).length;
         int[] var5 = new int[var4];
         int var6 = 0;

         int var7;
         int var9;
         for(var7 = 0; var6 < var4; var7 = var9) {
            label18: {
               int var8 = this.getTrackSupport(var1, var2, var6);
               if (var8 != 4) {
                  var9 = var7;
                  if (!var3) {
                     break label18;
                  }

                  var9 = var7;
                  if (var8 != 3) {
                     break label18;
                  }
               }

               var5[var7] = var6;
               var9 = var7 + 1;
            }

            ++var6;
         }

         return this.getAdaptiveSupport(var1, var2, Arrays.copyOf(var5, var7));
      }

      public int getAdaptiveSupport(int var1, int var2, int[] var3) {
         int var4 = 0;
         String var5 = null;
         boolean var6 = false;
         int var7 = 0;

         int var8;
         for(var8 = 16; var4 < var3.length; ++var7) {
            int var9 = var3[var4];
            String var10 = this.rendererTrackGroups[var1].get(var2).getFormat(var9).sampleMimeType;
            if (var7 == 0) {
               var5 = var10;
            } else {
               var6 |= Util.areEqual(var5, var10) ^ true;
            }

            var8 = Math.min(var8, this.rendererFormatSupports[var1][var2][var4] & 24);
            ++var4;
         }

         var2 = var8;
         if (var6) {
            var2 = Math.min(var8, this.rendererMixedMimeTypeAdaptiveSupports[var1]);
         }

         return var2;
      }

      public int getRendererCount() {
         return this.rendererCount;
      }

      public int getRendererType(int var1) {
         return this.rendererTrackTypes[var1];
      }

      public TrackGroupArray getTrackGroups(int var1) {
         return this.rendererTrackGroups[var1];
      }

      public int getTrackSupport(int var1, int var2, int var3) {
         return this.rendererFormatSupports[var1][var2][var3] & 7;
      }

      public TrackGroupArray getUnmappedTrackGroups() {
         return this.unmappedTrackGroups;
      }
   }
}
