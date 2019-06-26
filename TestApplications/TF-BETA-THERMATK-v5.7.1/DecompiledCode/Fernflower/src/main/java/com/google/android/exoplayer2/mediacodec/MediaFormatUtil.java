package com.google.android.exoplayer2.mediacodec;

import android.annotation.TargetApi;
import android.media.MediaFormat;
import com.google.android.exoplayer2.video.ColorInfo;
import java.nio.ByteBuffer;
import java.util.List;

@TargetApi(16)
public final class MediaFormatUtil {
   public static void maybeSetByteBuffer(MediaFormat var0, String var1, byte[] var2) {
      if (var2 != null) {
         var0.setByteBuffer(var1, ByteBuffer.wrap(var2));
      }

   }

   public static void maybeSetColorInfo(MediaFormat var0, ColorInfo var1) {
      if (var1 != null) {
         maybeSetInteger(var0, "color-transfer", var1.colorTransfer);
         maybeSetInteger(var0, "color-standard", var1.colorSpace);
         maybeSetInteger(var0, "color-range", var1.colorRange);
         maybeSetByteBuffer(var0, "hdr-static-info", var1.hdrStaticInfo);
      }

   }

   public static void maybeSetFloat(MediaFormat var0, String var1, float var2) {
      if (var2 != -1.0F) {
         var0.setFloat(var1, var2);
      }

   }

   public static void maybeSetInteger(MediaFormat var0, String var1, int var2) {
      if (var2 != -1) {
         var0.setInteger(var1, var2);
      }

   }

   public static void setCsdBuffers(MediaFormat var0, List var1) {
      for(int var2 = 0; var2 < var1.size(); ++var2) {
         StringBuilder var3 = new StringBuilder();
         var3.append("csd-");
         var3.append(var2);
         var0.setByteBuffer(var3.toString(), ByteBuffer.wrap((byte[])var1.get(var2)));
      }

   }
}
