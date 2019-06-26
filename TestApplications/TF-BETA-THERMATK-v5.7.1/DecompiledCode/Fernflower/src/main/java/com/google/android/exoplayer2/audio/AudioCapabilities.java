package com.google.android.exoplayer2.audio;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import java.util.Arrays;

@TargetApi(21)
public final class AudioCapabilities {
   public static final AudioCapabilities DEFAULT_AUDIO_CAPABILITIES = new AudioCapabilities(new int[]{2}, 8);
   private final int maxChannelCount;
   private final int[] supportedEncodings;

   public AudioCapabilities(int[] var1, int var2) {
      if (var1 != null) {
         this.supportedEncodings = Arrays.copyOf(var1, var1.length);
         Arrays.sort(this.supportedEncodings);
      } else {
         this.supportedEncodings = new int[0];
      }

      this.maxChannelCount = var2;
   }

   public static AudioCapabilities getCapabilities(Context var0) {
      return getCapabilities(var0.registerReceiver((BroadcastReceiver)null, new IntentFilter("android.media.action.HDMI_AUDIO_PLUG")));
   }

   @SuppressLint({"InlinedApi"})
   static AudioCapabilities getCapabilities(Intent var0) {
      return var0 != null && var0.getIntExtra("android.media.extra.AUDIO_PLUG_STATE", 0) != 0 ? new AudioCapabilities(var0.getIntArrayExtra("android.media.extra.ENCODINGS"), var0.getIntExtra("android.media.extra.MAX_CHANNEL_COUNT", 8)) : DEFAULT_AUDIO_CAPABILITIES;
   }

   public boolean equals(Object var1) {
      boolean var2 = true;
      if (this == var1) {
         return true;
      } else if (!(var1 instanceof AudioCapabilities)) {
         return false;
      } else {
         AudioCapabilities var3 = (AudioCapabilities)var1;
         if (!Arrays.equals(this.supportedEncodings, var3.supportedEncodings) || this.maxChannelCount != var3.maxChannelCount) {
            var2 = false;
         }

         return var2;
      }
   }

   public int getMaxChannelCount() {
      return this.maxChannelCount;
   }

   public int hashCode() {
      return this.maxChannelCount + Arrays.hashCode(this.supportedEncodings) * 31;
   }

   public boolean supportsEncoding(int var1) {
      boolean var2;
      if (Arrays.binarySearch(this.supportedEncodings, var1) >= 0) {
         var2 = true;
      } else {
         var2 = false;
      }

      return var2;
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append("AudioCapabilities[maxChannelCount=");
      var1.append(this.maxChannelCount);
      var1.append(", supportedEncodings=");
      var1.append(Arrays.toString(this.supportedEncodings));
      var1.append("]");
      return var1.toString();
   }
}
