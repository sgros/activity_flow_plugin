package com.google.android.exoplayer2.video;

import com.google.android.exoplayer2.ParserException;
import com.google.android.exoplayer2.util.NalUnitUtil;
import com.google.android.exoplayer2.util.ParsableByteArray;
import java.util.Collections;
import java.util.List;

public final class HevcConfig {
   public final List initializationData;
   public final int nalUnitLengthFieldLength;

   private HevcConfig(List var1, int var2) {
      this.initializationData = var1;
      this.nalUnitLengthFieldLength = var2;
   }

   public static HevcConfig parse(ParsableByteArray var0) throws ParserException {
      ArrayIndexOutOfBoundsException var10000;
      label102: {
         int var1;
         int var2;
         int var3;
         boolean var10001;
         try {
            var0.skipBytes(21);
            var1 = var0.readUnsignedByte();
            var2 = var0.readUnsignedByte();
            var3 = var0.getPosition();
         } catch (ArrayIndexOutOfBoundsException var19) {
            var10000 = var19;
            var10001 = false;
            break label102;
         }

         int var4 = 0;
         int var5 = 0;

         label96:
         while(true) {
            int var6;
            int var7;
            int var8;
            if (var4 >= var2) {
               byte[] var9;
               try {
                  var0.setPosition(var3);
                  var9 = new byte[var5];
               } catch (ArrayIndexOutOfBoundsException var15) {
                  var10000 = var15;
                  var10001 = false;
                  break;
               }

               var7 = 0;

               for(var4 = 0; var7 < var2; ++var7) {
                  try {
                     var0.skipBytes(1);
                     var6 = var0.readUnsignedShort();
                  } catch (ArrayIndexOutOfBoundsException var14) {
                     var10000 = var14;
                     var10001 = false;
                     break label96;
                  }

                  for(var3 = 0; var3 < var6; ++var3) {
                     try {
                        var8 = var0.readUnsignedShort();
                        System.arraycopy(NalUnitUtil.NAL_START_CODE, 0, var9, var4, NalUnitUtil.NAL_START_CODE.length);
                        var4 += NalUnitUtil.NAL_START_CODE.length;
                        System.arraycopy(var0.data, var0.getPosition(), var9, var4, var8);
                     } catch (ArrayIndexOutOfBoundsException var13) {
                        var10000 = var13;
                        var10001 = false;
                        break label96;
                     }

                     var4 += var8;

                     try {
                        var0.skipBytes(var8);
                     } catch (ArrayIndexOutOfBoundsException var12) {
                        var10000 = var12;
                        var10001 = false;
                        break label96;
                     }
                  }
               }

               List var20;
               if (var5 == 0) {
                  var20 = null;
               } else {
                  try {
                     var20 = Collections.singletonList(var9);
                  } catch (ArrayIndexOutOfBoundsException var11) {
                     var10000 = var11;
                     var10001 = false;
                     break;
                  }
               }

               try {
                  HevcConfig var22 = new HevcConfig(var20, (var1 & 3) + 1);
                  return var22;
               } catch (ArrayIndexOutOfBoundsException var10) {
                  var10000 = var10;
                  var10001 = false;
                  break;
               }
            }

            try {
               var0.skipBytes(1);
               var6 = var0.readUnsignedShort();
            } catch (ArrayIndexOutOfBoundsException var18) {
               var10000 = var18;
               var10001 = false;
               break;
            }

            for(var7 = 0; var7 < var6; ++var7) {
               try {
                  var8 = var0.readUnsignedShort();
               } catch (ArrayIndexOutOfBoundsException var17) {
                  var10000 = var17;
                  var10001 = false;
                  break label96;
               }

               var5 += var8 + 4;

               try {
                  var0.skipBytes(var8);
               } catch (ArrayIndexOutOfBoundsException var16) {
                  var10000 = var16;
                  var10001 = false;
                  break label96;
               }
            }

            ++var4;
         }
      }

      ArrayIndexOutOfBoundsException var21 = var10000;
      throw new ParserException("Error parsing HEVC config", var21);
   }
}
