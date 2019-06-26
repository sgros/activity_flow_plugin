package com.google.android.exoplayer2.video;

import com.google.android.exoplayer2.ParserException;
import com.google.android.exoplayer2.util.CodecSpecificDataUtil;
import com.google.android.exoplayer2.util.NalUnitUtil;
import com.google.android.exoplayer2.util.ParsableByteArray;
import java.util.ArrayList;
import java.util.List;

public final class AvcConfig {
   public final int height;
   public final List initializationData;
   public final int nalUnitLengthFieldLength;
   public final float pixelWidthAspectRatio;
   public final int width;

   private AvcConfig(List var1, int var2, int var3, int var4, float var5) {
      this.initializationData = var1;
      this.nalUnitLengthFieldLength = var2;
      this.width = var3;
      this.height = var4;
      this.pixelWidthAspectRatio = var5;
   }

   private static byte[] buildNalUnitForChild(ParsableByteArray var0) {
      int var1 = var0.readUnsignedShort();
      int var2 = var0.getPosition();
      var0.skipBytes(var1);
      return CodecSpecificDataUtil.buildNalUnit(var0.data, var2, var1);
   }

   public static AvcConfig parse(ParsableByteArray var0) throws ParserException {
      ArrayIndexOutOfBoundsException var10000;
      label76: {
         int var1;
         boolean var10001;
         try {
            var0.skipBytes(4);
            var1 = (var0.readUnsignedByte() & 3) + 1;
         } catch (ArrayIndexOutOfBoundsException var14) {
            var10000 = var14;
            var10001 = false;
            break label76;
         }

         if (var1 != 3) {
            label78: {
               ArrayList var2;
               int var3;
               try {
                  var2 = new ArrayList();
                  var3 = var0.readUnsignedByte() & 31;
               } catch (ArrayIndexOutOfBoundsException var12) {
                  var10000 = var12;
                  var10001 = false;
                  break label78;
               }

               int var4;
               for(var4 = 0; var4 < var3; ++var4) {
                  try {
                     var2.add(buildNalUnitForChild(var0));
                  } catch (ArrayIndexOutOfBoundsException var11) {
                     var10000 = var11;
                     var10001 = false;
                     break label78;
                  }
               }

               int var5;
               try {
                  var5 = var0.readUnsignedByte();
               } catch (ArrayIndexOutOfBoundsException var10) {
                  var10000 = var10;
                  var10001 = false;
                  break label78;
               }

               for(var4 = 0; var4 < var5; ++var4) {
                  try {
                     var2.add(buildNalUnitForChild(var0));
                  } catch (ArrayIndexOutOfBoundsException var9) {
                     var10000 = var9;
                     var10001 = false;
                     break label78;
                  }
               }

               float var6;
               if (var3 > 0) {
                  try {
                     byte[] var15 = (byte[])var2.get(0);
                     NalUnitUtil.SpsData var16 = NalUnitUtil.parseSpsNalUnit((byte[])var2.get(0), var1, var15.length);
                     var3 = var16.width;
                     var4 = var16.height;
                     var6 = var16.pixelWidthAspectRatio;
                  } catch (ArrayIndexOutOfBoundsException var8) {
                     var10000 = var8;
                     var10001 = false;
                     break label78;
                  }
               } else {
                  var3 = -1;
                  var4 = -1;
                  var6 = 1.0F;
               }

               try {
                  return new AvcConfig(var2, var1, var3, var4, var6);
               } catch (ArrayIndexOutOfBoundsException var7) {
                  var10000 = var7;
                  var10001 = false;
               }
            }
         } else {
            try {
               IllegalStateException var18 = new IllegalStateException();
               throw var18;
            } catch (ArrayIndexOutOfBoundsException var13) {
               var10000 = var13;
               var10001 = false;
            }
         }
      }

      ArrayIndexOutOfBoundsException var17 = var10000;
      throw new ParserException("Error parsing AVC config", var17);
   }
}
