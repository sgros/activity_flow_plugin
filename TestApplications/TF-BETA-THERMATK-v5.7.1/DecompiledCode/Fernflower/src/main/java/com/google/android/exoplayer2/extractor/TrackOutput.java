package com.google.android.exoplayer2.extractor;

import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.util.ParsableByteArray;
import java.io.IOException;
import java.util.Arrays;

public interface TrackOutput {
   void format(Format var1);

   int sampleData(ExtractorInput var1, int var2, boolean var3) throws IOException, InterruptedException;

   void sampleData(ParsableByteArray var1, int var2);

   void sampleMetadata(long var1, int var3, int var4, int var5, TrackOutput.CryptoData var6);

   public static final class CryptoData {
      public final int clearBlocks;
      public final int cryptoMode;
      public final int encryptedBlocks;
      public final byte[] encryptionKey;

      public CryptoData(int var1, byte[] var2, int var3, int var4) {
         this.cryptoMode = var1;
         this.encryptionKey = var2;
         this.encryptedBlocks = var3;
         this.clearBlocks = var4;
      }

      public boolean equals(Object var1) {
         boolean var2 = true;
         if (this == var1) {
            return true;
         } else if (var1 != null && TrackOutput.CryptoData.class == var1.getClass()) {
            TrackOutput.CryptoData var3 = (TrackOutput.CryptoData)var1;
            if (this.cryptoMode != var3.cryptoMode || this.encryptedBlocks != var3.encryptedBlocks || this.clearBlocks != var3.clearBlocks || !Arrays.equals(this.encryptionKey, var3.encryptionKey)) {
               var2 = false;
            }

            return var2;
         } else {
            return false;
         }
      }

      public int hashCode() {
         return ((this.cryptoMode * 31 + Arrays.hashCode(this.encryptionKey)) * 31 + this.encryptedBlocks) * 31 + this.clearBlocks;
      }
   }
}
