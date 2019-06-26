package com.google.android.exoplayer2.decoder;

import android.annotation.TargetApi;
import android.media.MediaCodec.CryptoInfo.Pattern;
import com.google.android.exoplayer2.util.Util;

public final class CryptoInfo {
   public int clearBlocks;
   public int encryptedBlocks;
   private final android.media.MediaCodec.CryptoInfo frameworkCryptoInfo;
   public byte[] iv;
   public byte[] key;
   public int mode;
   public int[] numBytesOfClearData;
   public int[] numBytesOfEncryptedData;
   public int numSubSamples;
   private final CryptoInfo.PatternHolderV24 patternHolder;

   public CryptoInfo() {
      android.media.MediaCodec.CryptoInfo var1;
      if (Util.SDK_INT >= 16) {
         var1 = this.newFrameworkCryptoInfoV16();
      } else {
         var1 = null;
      }

      this.frameworkCryptoInfo = var1;
      CryptoInfo.PatternHolderV24 var2;
      if (Util.SDK_INT >= 24) {
         var2 = new CryptoInfo.PatternHolderV24(this.frameworkCryptoInfo);
      } else {
         var2 = null;
      }

      this.patternHolder = var2;
   }

   @TargetApi(16)
   private android.media.MediaCodec.CryptoInfo newFrameworkCryptoInfoV16() {
      return new android.media.MediaCodec.CryptoInfo();
   }

   @TargetApi(16)
   private void updateFrameworkCryptoInfoV16() {
      android.media.MediaCodec.CryptoInfo var1 = this.frameworkCryptoInfo;
      var1.numSubSamples = this.numSubSamples;
      var1.numBytesOfClearData = this.numBytesOfClearData;
      var1.numBytesOfEncryptedData = this.numBytesOfEncryptedData;
      var1.key = this.key;
      var1.iv = this.iv;
      var1.mode = this.mode;
      if (Util.SDK_INT >= 24) {
         this.patternHolder.set(this.encryptedBlocks, this.clearBlocks);
      }

   }

   @TargetApi(16)
   public android.media.MediaCodec.CryptoInfo getFrameworkCryptoInfoV16() {
      return this.frameworkCryptoInfo;
   }

   public void set(int var1, int[] var2, int[] var3, byte[] var4, byte[] var5, int var6, int var7, int var8) {
      this.numSubSamples = var1;
      this.numBytesOfClearData = var2;
      this.numBytesOfEncryptedData = var3;
      this.key = var4;
      this.iv = var5;
      this.mode = var6;
      this.encryptedBlocks = var7;
      this.clearBlocks = var8;
      if (Util.SDK_INT >= 16) {
         this.updateFrameworkCryptoInfoV16();
      }

   }

   @TargetApi(24)
   private static final class PatternHolderV24 {
      private final android.media.MediaCodec.CryptoInfo frameworkCryptoInfo;
      private final Pattern pattern;

      private PatternHolderV24(android.media.MediaCodec.CryptoInfo var1) {
         this.frameworkCryptoInfo = var1;
         this.pattern = new Pattern(0, 0);
      }

      // $FF: synthetic method
      PatternHolderV24(android.media.MediaCodec.CryptoInfo var1, Object var2) {
         this(var1);
      }

      private void set(int var1, int var2) {
         this.pattern.set(var1, var2);
         this.frameworkCryptoInfo.setPattern(this.pattern);
      }
   }
}
