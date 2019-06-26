package com.google.android.exoplayer2.extractor.ogg;

import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.drm.DrmInitData;
import com.google.android.exoplayer2.util.ParsableByteArray;
import com.google.android.exoplayer2.util.Util;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

final class OpusReader extends StreamReader {
   private static final int OPUS_CODE = Util.getIntegerCodeForString("Opus");
   private static final byte[] OPUS_SIGNATURE = new byte[]{79, 112, 117, 115, 72, 101, 97, 100};
   private boolean headerRead;

   private long getPacketDurationUs(byte[] var1) {
      int var2 = var1[0] & 255;
      int var3 = var2 & 3;
      byte var4 = 2;
      int var5;
      if (var3 != 0) {
         var5 = var4;
         if (var3 != 1) {
            var5 = var4;
            if (var3 != 2) {
               var5 = var1[1] & 63;
            }
         }
      } else {
         var5 = 1;
      }

      var2 >>= 3;
      int var6 = var2 & 3;
      if (var2 >= 16) {
         var6 = 2500 << var6;
      } else if (var2 >= 12) {
         var6 = 10000 << (var6 & 1);
      } else if (var6 == 3) {
         var6 = 60000;
      } else {
         var6 = 10000 << var6;
      }

      return (long)var5 * (long)var6;
   }

   private void putNativeOrderLong(List var1, int var2) {
      long var3 = (long)var2 * 1000000000L / 48000L;
      var1.add(ByteBuffer.allocate(8).order(ByteOrder.nativeOrder()).putLong(var3).array());
   }

   public static boolean verifyBitstreamType(ParsableByteArray var0) {
      int var1 = var0.bytesLeft();
      byte[] var2 = OPUS_SIGNATURE;
      if (var1 < var2.length) {
         return false;
      } else {
         byte[] var3 = new byte[var2.length];
         var0.readBytes(var3, 0, var2.length);
         return Arrays.equals(var3, OPUS_SIGNATURE);
      }
   }

   protected long preparePayload(ParsableByteArray var1) {
      return this.convertTimeToGranule(this.getPacketDurationUs(var1.data));
   }

   protected boolean readHeaders(ParsableByteArray var1, long var2, StreamReader.SetupData var4) {
      boolean var5 = this.headerRead;
      boolean var6 = true;
      if (!var5) {
         byte[] var7 = Arrays.copyOf(var1.data, var1.limit());
         byte var8 = var7[9];
         byte var9 = var7[11];
         byte var10 = var7[10];
         ArrayList var11 = new ArrayList(3);
         var11.add(var7);
         this.putNativeOrderLong(var11, (var9 & 255) << 8 | var10 & 255);
         this.putNativeOrderLong(var11, 3840);
         var4.format = Format.createAudioSampleFormat((String)null, "audio/opus", (String)null, -1, -1, var8 & 255, 48000, var11, (DrmInitData)null, 0, (String)null);
         this.headerRead = true;
         return true;
      } else {
         if (var1.readInt() != OPUS_CODE) {
            var6 = false;
         }

         var1.setPosition(0);
         return var6;
      }
   }

   protected void reset(boolean var1) {
      super.reset(var1);
      if (var1) {
         this.headerRead = false;
      }

   }
}
