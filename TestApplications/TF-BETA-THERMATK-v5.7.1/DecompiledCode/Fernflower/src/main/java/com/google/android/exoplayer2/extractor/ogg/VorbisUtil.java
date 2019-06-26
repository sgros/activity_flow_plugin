package com.google.android.exoplayer2.extractor.ogg;

import com.google.android.exoplayer2.ParserException;
import com.google.android.exoplayer2.util.Log;
import com.google.android.exoplayer2.util.ParsableByteArray;
import java.util.Arrays;

final class VorbisUtil {
   public static int iLog(int var0) {
      int var1;
      for(var1 = 0; var0 > 0; var0 >>>= 1) {
         ++var1;
      }

      return var1;
   }

   private static long mapType1QuantValues(long var0, long var2) {
      double var4 = (double)var0;
      double var6 = (double)var2;
      Double.isNaN(var6);
      return (long)Math.floor(Math.pow(var4, 1.0D / var6));
   }

   private static VorbisUtil.CodeBook readBook(VorbisBitArray var0) throws ParserException {
      if (var0.readBits(24) != 5653314) {
         StringBuilder var13 = new StringBuilder();
         var13.append("expected code book to start with [0x56, 0x43, 0x42] at ");
         var13.append(var0.getPosition());
         throw new ParserException(var13.toString());
      } else {
         int var1 = var0.readBits(16);
         int var2 = var0.readBits(24);
         long[] var3 = new long[var2];
         boolean var4 = var0.readBit();
         long var5 = 0L;
         int var7 = 0;
         int var9;
         if (!var4) {
            for(boolean var8 = var0.readBit(); var7 < var3.length; ++var7) {
               if (var8) {
                  if (var0.readBit()) {
                     var3[var7] = (long)(var0.readBits(5) + 1);
                  } else {
                     var3[var7] = 0L;
                  }
               } else {
                  var3[var7] = (long)(var0.readBits(5) + 1);
               }
            }
         } else {
            var9 = var0.readBits(5) + 1;

            for(var7 = 0; var7 < var3.length; ++var9) {
               int var10 = var0.readBits(iLog(var2 - var7));

               for(int var11 = 0; var11 < var10 && var7 < var3.length; ++var11) {
                  var3[var7] = (long)var9;
                  ++var7;
               }
            }
         }

         var7 = var0.readBits(4);
         if (var7 > 2) {
            StringBuilder var12 = new StringBuilder();
            var12.append("lookup type greater than 2 not decodable: ");
            var12.append(var7);
            throw new ParserException(var12.toString());
         } else {
            if (var7 == 1 || var7 == 2) {
               var0.skipBits(32);
               var0.skipBits(32);
               var9 = var0.readBits(4);
               var0.skipBits(1);
               if (var7 == 1) {
                  if (var1 != 0) {
                     var5 = mapType1QuantValues((long)var2, (long)var1);
                  }
               } else {
                  var5 = (long)var2 * (long)var1;
               }

               var0.skipBits((int)(var5 * (long)(var9 + 1)));
            }

            return new VorbisUtil.CodeBook(var1, var2, var3, var7, var4);
         }
      }
   }

   private static void readFloors(VorbisBitArray var0) throws ParserException {
      int var1 = var0.readBits(6);

      for(int var2 = 0; var2 < var1 + 1; ++var2) {
         int var3 = var0.readBits(16);
         int var6;
         if (var3 == 0) {
            var0.skipBits(8);
            var0.skipBits(16);
            var0.skipBits(16);
            var0.skipBits(6);
            var0.skipBits(8);
            var6 = var0.readBits(4);

            for(var3 = 0; var3 < var6 + 1; ++var3) {
               var0.skipBits(8);
            }
         } else {
            if (var3 != 1) {
               StringBuilder var10 = new StringBuilder();
               var10.append("floor type greater than 1 not decodable: ");
               var10.append(var3);
               throw new ParserException(var10.toString());
            }

            int var4 = var0.readBits(5);
            int[] var5 = new int[var4];
            var6 = 0;

            int var7;
            for(var7 = -1; var6 < var4; var7 = var3) {
               var5[var6] = var0.readBits(4);
               var3 = var7;
               if (var5[var6] > var7) {
                  var3 = var5[var6];
               }

               ++var6;
            }

            int[] var8 = new int[var7 + 1];

            for(var3 = 0; var3 < var8.length; ++var3) {
               var8[var3] = var0.readBits(3) + 1;
               var7 = var0.readBits(2);
               if (var7 > 0) {
                  var0.skipBits(8);
               }

               for(var6 = 0; var6 < 1 << var7; ++var6) {
                  var0.skipBits(8);
               }
            }

            var0.skipBits(2);
            int var9 = var0.readBits(4);
            var6 = 0;
            var7 = 0;

            for(var3 = 0; var6 < var4; ++var6) {
               for(var7 += var8[var5[var6]]; var3 < var7; ++var3) {
                  var0.skipBits(var9);
               }
            }
         }
      }

   }

   private static void readMappings(int var0, VorbisBitArray var1) throws ParserException {
      int var2 = var1.readBits(6);

      for(int var3 = 0; var3 < var2 + 1; ++var3) {
         int var4 = var1.readBits(16);
         if (var4 != 0) {
            StringBuilder var5 = new StringBuilder();
            var5.append("mapping type other than 0 not supported: ");
            var5.append(var4);
            Log.e("VorbisUtil", var5.toString());
         } else {
            if (var1.readBit()) {
               var4 = var1.readBits(4) + 1;
            } else {
               var4 = 1;
            }

            int var7;
            if (var1.readBit()) {
               int var6 = var1.readBits(8);

               for(var7 = 0; var7 < var6 + 1; ++var7) {
                  int var8 = var0 - 1;
                  var1.skipBits(iLog(var8));
                  var1.skipBits(iLog(var8));
               }
            }

            if (var1.readBits(2) != 0) {
               throw new ParserException("to reserved bits must be zero after mapping coupling steps");
            }

            if (var4 > 1) {
               for(var7 = 0; var7 < var0; ++var7) {
                  var1.skipBits(4);
               }
            }

            for(var7 = 0; var7 < var4; ++var7) {
               var1.skipBits(8);
               var1.skipBits(8);
               var1.skipBits(8);
            }
         }
      }

   }

   private static VorbisUtil.Mode[] readModes(VorbisBitArray var0) {
      int var1 = var0.readBits(6) + 1;
      VorbisUtil.Mode[] var2 = new VorbisUtil.Mode[var1];

      for(int var3 = 0; var3 < var1; ++var3) {
         var2[var3] = new VorbisUtil.Mode(var0.readBit(), var0.readBits(16), var0.readBits(16), var0.readBits(8));
      }

      return var2;
   }

   private static void readResidues(VorbisBitArray var0) throws ParserException {
      int var1 = var0.readBits(6);

      for(int var2 = 0; var2 < var1 + 1; ++var2) {
         if (var0.readBits(16) > 2) {
            throw new ParserException("residueType greater than 2 is not decodable");
         }

         var0.skipBits(24);
         var0.skipBits(24);
         var0.skipBits(24);
         int var3 = var0.readBits(6) + 1;
         var0.skipBits(8);
         int[] var4 = new int[var3];

         int var5;
         int var7;
         for(var5 = 0; var5 < var3; ++var5) {
            int var6 = var0.readBits(3);
            if (var0.readBit()) {
               var7 = var0.readBits(5);
            } else {
               var7 = 0;
            }

            var4[var5] = var7 * 8 + var6;
         }

         for(var5 = 0; var5 < var3; ++var5) {
            for(var7 = 0; var7 < 8; ++var7) {
               if ((var4[var5] & 1 << var7) != 0) {
                  var0.skipBits(8);
               }
            }
         }
      }

   }

   public static VorbisUtil.CommentHeader readVorbisCommentHeader(ParsableByteArray var0) throws ParserException {
      int var1 = 0;
      verifyVorbisHeaderCapturePattern(3, var0, false);
      String var2 = var0.readString((int)var0.readLittleEndianUnsignedInt());
      int var3 = var2.length();
      long var4 = var0.readLittleEndianUnsignedInt();
      String[] var6 = new String[(int)var4];

      for(var3 = 11 + var3 + 4; (long)var1 < var4; ++var1) {
         var6[var1] = var0.readString((int)var0.readLittleEndianUnsignedInt());
         var3 = var3 + 4 + var6[var1].length();
      }

      if ((var0.readUnsignedByte() & 1) != 0) {
         return new VorbisUtil.CommentHeader(var2, var6, var3 + 1);
      } else {
         throw new ParserException("framing bit expected to be set");
      }
   }

   public static VorbisUtil.VorbisIdHeader readVorbisIdentificationHeader(ParsableByteArray var0) throws ParserException {
      verifyVorbisHeaderCapturePattern(1, var0, false);
      long var1 = var0.readLittleEndianUnsignedInt();
      int var3 = var0.readUnsignedByte();
      long var4 = var0.readLittleEndianUnsignedInt();
      int var6 = var0.readLittleEndianInt();
      int var7 = var0.readLittleEndianInt();
      int var8 = var0.readLittleEndianInt();
      int var9 = var0.readUnsignedByte();
      int var10 = (int)Math.pow(2.0D, (double)(var9 & 15));
      var9 = (int)Math.pow(2.0D, (double)((var9 & 240) >> 4));
      boolean var11;
      if ((var0.readUnsignedByte() & 1) > 0) {
         var11 = true;
      } else {
         var11 = false;
      }

      return new VorbisUtil.VorbisIdHeader(var1, var3, var4, var6, var7, var8, var10, var9, var11, Arrays.copyOf(var0.data, var0.limit()));
   }

   public static VorbisUtil.Mode[] readVorbisModes(ParsableByteArray var0, int var1) throws ParserException {
      byte var2 = 0;
      verifyVorbisHeaderCapturePattern(5, var0, false);
      int var3 = var0.readUnsignedByte();
      VorbisBitArray var4 = new VorbisBitArray(var0.data);
      var4.skipBits(var0.getPosition() * 8);

      int var5;
      for(var5 = 0; var5 < var3 + 1; ++var5) {
         readBook(var4);
      }

      var3 = var4.readBits(6);

      for(var5 = var2; var5 < var3 + 1; ++var5) {
         if (var4.readBits(16) != 0) {
            throw new ParserException("placeholder of time domain transforms not zeroed out");
         }
      }

      readFloors(var4);
      readResidues(var4);
      readMappings(var1, var4);
      VorbisUtil.Mode[] var6 = readModes(var4);
      if (var4.readBit()) {
         return var6;
      } else {
         throw new ParserException("framing bit after modes not set as expected");
      }
   }

   public static boolean verifyVorbisHeaderCapturePattern(int var0, ParsableByteArray var1, boolean var2) throws ParserException {
      if (var1.bytesLeft() < 7) {
         if (var2) {
            return false;
         } else {
            StringBuilder var3 = new StringBuilder();
            var3.append("too short header: ");
            var3.append(var1.bytesLeft());
            throw new ParserException(var3.toString());
         }
      } else if (var1.readUnsignedByte() != var0) {
         if (var2) {
            return false;
         } else {
            StringBuilder var4 = new StringBuilder();
            var4.append("expected header type ");
            var4.append(Integer.toHexString(var0));
            throw new ParserException(var4.toString());
         }
      } else if (var1.readUnsignedByte() == 118 && var1.readUnsignedByte() == 111 && var1.readUnsignedByte() == 114 && var1.readUnsignedByte() == 98 && var1.readUnsignedByte() == 105 && var1.readUnsignedByte() == 115) {
         return true;
      } else if (var2) {
         return false;
      } else {
         throw new ParserException("expected characters 'vorbis'");
      }
   }

   public static final class CodeBook {
      public final int dimensions;
      public final int entries;
      public final boolean isOrdered;
      public final long[] lengthMap;
      public final int lookupType;

      public CodeBook(int var1, int var2, long[] var3, int var4, boolean var5) {
         this.dimensions = var1;
         this.entries = var2;
         this.lengthMap = var3;
         this.lookupType = var4;
         this.isOrdered = var5;
      }
   }

   public static final class CommentHeader {
      public final String[] comments;
      public final int length;
      public final String vendor;

      public CommentHeader(String var1, String[] var2, int var3) {
         this.vendor = var1;
         this.comments = var2;
         this.length = var3;
      }
   }

   public static final class Mode {
      public final boolean blockFlag;
      public final int mapping;
      public final int transformType;
      public final int windowType;

      public Mode(boolean var1, int var2, int var3, int var4) {
         this.blockFlag = var1;
         this.windowType = var2;
         this.transformType = var3;
         this.mapping = var4;
      }
   }

   public static final class VorbisIdHeader {
      public final int bitrateMax;
      public final int bitrateMin;
      public final int bitrateNominal;
      public final int blockSize0;
      public final int blockSize1;
      public final int channels;
      public final byte[] data;
      public final boolean framingFlag;
      public final long sampleRate;
      public final long version;

      public VorbisIdHeader(long var1, int var3, long var4, int var6, int var7, int var8, int var9, int var10, boolean var11, byte[] var12) {
         this.version = var1;
         this.channels = var3;
         this.sampleRate = var4;
         this.bitrateMax = var6;
         this.bitrateNominal = var7;
         this.bitrateMin = var8;
         this.blockSize0 = var9;
         this.blockSize1 = var10;
         this.framingFlag = var11;
         this.data = var12;
      }
   }
}
