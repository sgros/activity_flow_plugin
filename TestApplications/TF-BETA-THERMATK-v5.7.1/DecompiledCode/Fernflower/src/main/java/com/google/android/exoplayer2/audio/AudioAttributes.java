package com.google.android.exoplayer2.audio;

import android.annotation.TargetApi;

public final class AudioAttributes {
   public static final AudioAttributes DEFAULT = (new AudioAttributes.Builder()).build();
   private android.media.AudioAttributes audioAttributesV21;
   public final int contentType;
   public final int flags;
   public final int usage;

   private AudioAttributes(int var1, int var2, int var3) {
      this.contentType = var1;
      this.flags = var2;
      this.usage = var3;
   }

   // $FF: synthetic method
   AudioAttributes(int var1, int var2, int var3, Object var4) {
      this(var1, var2, var3);
   }

   public boolean equals(Object var1) {
      boolean var2 = true;
      if (this == var1) {
         return true;
      } else if (var1 != null && AudioAttributes.class == var1.getClass()) {
         AudioAttributes var3 = (AudioAttributes)var1;
         if (this.contentType != var3.contentType || this.flags != var3.flags || this.usage != var3.usage) {
            var2 = false;
         }

         return var2;
      } else {
         return false;
      }
   }

   @TargetApi(21)
   public android.media.AudioAttributes getAudioAttributesV21() {
      if (this.audioAttributesV21 == null) {
         this.audioAttributesV21 = (new android.media.AudioAttributes.Builder()).setContentType(this.contentType).setFlags(this.flags).setUsage(this.usage).build();
      }

      return this.audioAttributesV21;
   }

   public int hashCode() {
      return ((527 + this.contentType) * 31 + this.flags) * 31 + this.usage;
   }

   public static final class Builder {
      private int contentType = 0;
      private int flags = 0;
      private int usage = 1;

      public AudioAttributes build() {
         return new AudioAttributes(this.contentType, this.flags, this.usage);
      }

      public AudioAttributes.Builder setContentType(int var1) {
         this.contentType = var1;
         return this;
      }

      public AudioAttributes.Builder setUsage(int var1) {
         this.usage = var1;
         return this;
      }
   }
}
