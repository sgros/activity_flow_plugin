package com.google.android.exoplayer2.extractor.mp4;

import com.google.android.exoplayer2.extractor.ExtractorInput;
import com.google.android.exoplayer2.util.ParsableByteArray;
import com.google.android.exoplayer2.util.Util;
import java.io.IOException;

final class Sniffer {
   private static final int[] COMPATIBLE_BRANDS = new int[]{Util.getIntegerCodeForString("isom"), Util.getIntegerCodeForString("iso2"), Util.getIntegerCodeForString("iso3"), Util.getIntegerCodeForString("iso4"), Util.getIntegerCodeForString("iso5"), Util.getIntegerCodeForString("iso6"), Util.getIntegerCodeForString("avc1"), Util.getIntegerCodeForString("hvc1"), Util.getIntegerCodeForString("hev1"), Util.getIntegerCodeForString("mp41"), Util.getIntegerCodeForString("mp42"), Util.getIntegerCodeForString("3g2a"), Util.getIntegerCodeForString("3g2b"), Util.getIntegerCodeForString("3gr6"), Util.getIntegerCodeForString("3gs6"), Util.getIntegerCodeForString("3ge6"), Util.getIntegerCodeForString("3gg6"), Util.getIntegerCodeForString("M4V "), Util.getIntegerCodeForString("M4A "), Util.getIntegerCodeForString("f4v "), Util.getIntegerCodeForString("kddi"), Util.getIntegerCodeForString("M4VP"), Util.getIntegerCodeForString("qt  "), Util.getIntegerCodeForString("MSNV")};

   private static boolean isCompatibleBrand(int var0) {
      if (var0 >>> 8 == Util.getIntegerCodeForString("3gp")) {
         return true;
      } else {
         int[] var1 = COMPATIBLE_BRANDS;
         int var2 = var1.length;

         for(int var3 = 0; var3 < var2; ++var3) {
            if (var1[var3] == var0) {
               return true;
            }
         }

         return false;
      }
   }

   public static boolean sniffFragmented(ExtractorInput var0) throws IOException, InterruptedException {
      return sniffInternal(var0, true);
   }

   private static boolean sniffInternal(ExtractorInput var0, boolean var1) throws IOException, InterruptedException {
      long var2 = var0.getLength();
      long var4 = 4096L;
      long var6 = var4;
      if (var2 != -1L) {
         if (var2 > 4096L) {
            var6 = var4;
         } else {
            var6 = var2;
         }
      }

      int var8 = (int)var6;
      ParsableByteArray var9 = new ParsableByteArray(64);
      int var10 = 0;
      boolean var11 = false;

      boolean var18;
      while(true) {
         if (var10 < var8) {
            var9.reset(8);
            var0.peekFully(var9.data, 0, 8);
            var4 = var9.readUnsignedInt();
            int var12 = var9.readInt();
            byte var13 = 16;
            if (var4 == 1L) {
               var0.peekFully(var9.data, 8, 8);
               var9.setLimit(16);
               var6 = var9.readLong();
            } else {
               var6 = var4;
               if (var4 == 0L) {
                  long var14 = var0.getLength();
                  var6 = var4;
                  if (var14 != -1L) {
                     var6 = var0.getPeekPosition();
                     var6 = (long)8 + (var14 - var6);
                  }
               }

               var13 = 8;
            }

            if (var2 != -1L && (long)var10 + var6 > var2) {
               return false;
            }

            var4 = (long)var13;
            if (var6 < var4) {
               return false;
            }

            var10 += var13;
            if (var12 == Atom.TYPE_moov) {
               int var22 = var8 + (int)var6;
               var8 = var22;
               if (var2 != -1L) {
                  var8 = var22;
                  if ((long)var22 > var2) {
                     var8 = (int)var2;
                  }
               }
               continue;
            }

            if (var12 == Atom.TYPE_moof || var12 == Atom.TYPE_mvex) {
               var18 = true;
               break;
            }

            if ((long)var10 + var6 - var4 < (long)var8) {
               int var16 = (int)(var6 - var4);
               int var17 = var10 + var16;
               boolean var21;
               if (var12 == Atom.TYPE_ftyp) {
                  if (var16 < 8) {
                     return false;
                  }

                  var9.reset(var16);
                  var0.peekFully(var9.data, 0, var16);
                  var12 = var16 / 4;

                  for(var10 = 0; var10 < var12; ++var10) {
                     var21 = true;
                     if (var10 == 1) {
                        var9.skipBytes(4);
                     } else if (isCompatibleBrand(var9.readInt())) {
                        var11 = var21;
                        break;
                     }
                  }

                  if (!var11) {
                     return false;
                  }

                  var21 = var11;
               } else {
                  var21 = var11;
                  if (var16 != 0) {
                     var0.advancePeekPosition(var16);
                     var21 = var11;
                  }
               }

               var10 = var17;
               var11 = var21;
               continue;
            }
         }

         var18 = false;
         break;
      }

      boolean var19 = false;
      boolean var20 = var19;
      if (var11) {
         var20 = var19;
         if (var1 == var18) {
            var20 = true;
         }
      }

      return var20;
   }

   public static boolean sniffUnfragmented(ExtractorInput var0) throws IOException, InterruptedException {
      return sniffInternal(var0, false);
   }
}
