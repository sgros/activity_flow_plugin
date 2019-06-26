package com.google.android.exoplayer2.extractor.mp4;

import com.google.android.exoplayer2.extractor.TrackOutput;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.util.Log;

public final class TrackEncryptionBox {
   public final TrackOutput.CryptoData cryptoData;
   public final byte[] defaultInitializationVector;
   public final boolean isEncrypted;
   public final int perSampleIvSize;
   public final String schemeType;

   public TrackEncryptionBox(boolean var1, String var2, int var3, byte[] var4, int var5, int var6, byte[] var7) {
      boolean var8 = true;
      boolean var9;
      if (var3 == 0) {
         var9 = true;
      } else {
         var9 = false;
      }

      if (var7 != null) {
         var8 = false;
      }

      Assertions.checkArgument(var8 ^ var9);
      this.isEncrypted = var1;
      this.schemeType = var2;
      this.perSampleIvSize = var3;
      this.defaultInitializationVector = var7;
      this.cryptoData = new TrackOutput.CryptoData(schemeToCryptoMode(var2), var4, var5, var6);
   }

   private static int schemeToCryptoMode(String var0) {
      if (var0 == null) {
         return 1;
      } else {
         byte var1 = -1;
         switch(var0.hashCode()) {
         case 3046605:
            if (var0.equals("cbc1")) {
               var1 = 2;
            }
            break;
         case 3046671:
            if (var0.equals("cbcs")) {
               var1 = 3;
            }
            break;
         case 3049879:
            if (var0.equals("cenc")) {
               var1 = 0;
            }
            break;
         case 3049895:
            if (var0.equals("cens")) {
               var1 = 1;
            }
         }

         if (var1 != 0 && var1 != 1) {
            if (var1 != 2 && var1 != 3) {
               StringBuilder var2 = new StringBuilder();
               var2.append("Unsupported protection scheme type '");
               var2.append(var0);
               var2.append("'. Assuming AES-CTR crypto mode.");
               Log.w("TrackEncryptionBox", var2.toString());
               return 1;
            } else {
               return 2;
            }
         } else {
            return 1;
         }
      }
   }
}
