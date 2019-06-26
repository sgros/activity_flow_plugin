package com.google.android.exoplayer2.source.ads;

import android.net.Uri;
import com.google.android.exoplayer2.util.Assertions;
import java.util.Arrays;

public final class AdPlaybackState {
   public static final AdPlaybackState NONE = new AdPlaybackState(new long[0]);
   public final int adGroupCount;
   public final long[] adGroupTimesUs;
   public final AdPlaybackState.AdGroup[] adGroups;
   public final long adResumePositionUs;
   public final long contentDurationUs;

   public AdPlaybackState(long... var1) {
      int var2 = var1.length;
      this.adGroupCount = var2;
      this.adGroupTimesUs = Arrays.copyOf(var1, var2);
      this.adGroups = new AdPlaybackState.AdGroup[var2];

      for(int var3 = 0; var3 < var2; ++var3) {
         this.adGroups[var3] = new AdPlaybackState.AdGroup();
      }

      this.adResumePositionUs = 0L;
      this.contentDurationUs = -9223372036854775807L;
   }

   private boolean isPositionBeforeAdGroup(long var1, int var3) {
      if (var1 == Long.MIN_VALUE) {
         return false;
      } else {
         long var4 = this.adGroupTimesUs[var3];
         boolean var6 = true;
         boolean var7 = true;
         if (var4 == Long.MIN_VALUE) {
            var4 = this.contentDurationUs;
            var6 = var7;
            if (var4 != -9223372036854775807L) {
               if (var1 < var4) {
                  var6 = var7;
               } else {
                  var6 = false;
               }
            }

            return var6;
         } else {
            if (var1 >= var4) {
               var6 = false;
            }

            return var6;
         }
      }
   }

   public boolean equals(Object var1) {
      boolean var2 = true;
      if (this == var1) {
         return true;
      } else if (var1 != null && AdPlaybackState.class == var1.getClass()) {
         AdPlaybackState var3 = (AdPlaybackState)var1;
         if (this.adGroupCount != var3.adGroupCount || this.adResumePositionUs != var3.adResumePositionUs || this.contentDurationUs != var3.contentDurationUs || !Arrays.equals(this.adGroupTimesUs, var3.adGroupTimesUs) || !Arrays.equals(this.adGroups, var3.adGroups)) {
            var2 = false;
         }

         return var2;
      } else {
         return false;
      }
   }

   public int getAdGroupIndexAfterPositionUs(long var1, long var3) {
      if (var1 != Long.MIN_VALUE && (var3 == -9223372036854775807L || var1 < var3)) {
         int var5 = 0;

         while(true) {
            long[] var6 = this.adGroupTimesUs;
            if (var5 >= var6.length || var6[var5] == Long.MIN_VALUE || var1 < var6[var5] && this.adGroups[var5].hasUnplayedAds()) {
               if (var5 >= this.adGroupTimesUs.length) {
                  var5 = -1;
               }

               return var5;
            }

            ++var5;
         }
      } else {
         return -1;
      }
   }

   public int getAdGroupIndexForPositionUs(long var1) {
      int var3;
      for(var3 = this.adGroupTimesUs.length - 1; var3 >= 0 && this.isPositionBeforeAdGroup(var1, var3); --var3) {
      }

      if (var3 < 0 || !this.adGroups[var3].hasUnplayedAds()) {
         var3 = -1;
      }

      return var3;
   }

   public int hashCode() {
      return (((this.adGroupCount * 31 + (int)this.adResumePositionUs) * 31 + (int)this.contentDurationUs) * 31 + Arrays.hashCode(this.adGroupTimesUs)) * 31 + Arrays.hashCode(this.adGroups);
   }

   public static final class AdGroup {
      public final int count;
      public final long[] durationsUs;
      public final int[] states;
      public final Uri[] uris;

      public AdGroup() {
         this(-1, new int[0], new Uri[0], new long[0]);
      }

      private AdGroup(int var1, int[] var2, Uri[] var3, long[] var4) {
         boolean var5;
         if (var2.length == var3.length) {
            var5 = true;
         } else {
            var5 = false;
         }

         Assertions.checkArgument(var5);
         this.count = var1;
         this.states = var2;
         this.uris = var3;
         this.durationsUs = var4;
      }

      public boolean equals(Object var1) {
         boolean var2 = true;
         if (this == var1) {
            return true;
         } else if (var1 != null && AdPlaybackState.AdGroup.class == var1.getClass()) {
            AdPlaybackState.AdGroup var3 = (AdPlaybackState.AdGroup)var1;
            if (this.count != var3.count || !Arrays.equals(this.uris, var3.uris) || !Arrays.equals(this.states, var3.states) || !Arrays.equals(this.durationsUs, var3.durationsUs)) {
               var2 = false;
            }

            return var2;
         } else {
            return false;
         }
      }

      public int getFirstAdIndexToPlay() {
         return this.getNextAdIndexToPlay(-1);
      }

      public int getNextAdIndexToPlay(int var1) {
         ++var1;

         while(true) {
            int[] var2 = this.states;
            if (var1 >= var2.length || var2[var1] == 0 || var2[var1] == 1) {
               return var1;
            }

            ++var1;
         }
      }

      public boolean hasUnplayedAds() {
         boolean var1;
         if (this.count != -1 && this.getFirstAdIndexToPlay() >= this.count) {
            var1 = false;
         } else {
            var1 = true;
         }

         return var1;
      }

      public int hashCode() {
         return ((this.count * 31 + Arrays.hashCode(this.uris)) * 31 + Arrays.hashCode(this.states)) * 31 + Arrays.hashCode(this.durationsUs);
      }
   }
}
