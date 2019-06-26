package com.google.android.exoplayer2.metadata.id3;

import com.google.android.exoplayer2.metadata.Metadata;
import com.google.android.exoplayer2.metadata.MetadataDecoder;
import com.google.android.exoplayer2.metadata.MetadataInputBuffer;
import com.google.android.exoplayer2.util.Log;
import com.google.android.exoplayer2.util.ParsableBitArray;
import com.google.android.exoplayer2.util.ParsableByteArray;
import com.google.android.exoplayer2.util.Util;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

public final class Id3Decoder implements MetadataDecoder {
   public static final int ID3_TAG;
   public static final Id3Decoder.FramePredicate NO_FRAMES_PREDICATE;
   private final Id3Decoder.FramePredicate framePredicate;

   static {
      NO_FRAMES_PREDICATE = _$$Lambda$Id3Decoder$7M0gB_IGKaTbyTVX_WCb62bIHyc.INSTANCE;
      ID3_TAG = Util.getIntegerCodeForString("ID3");
   }

   public Id3Decoder() {
      this((Id3Decoder.FramePredicate)null);
   }

   public Id3Decoder(Id3Decoder.FramePredicate var1) {
      this.framePredicate = var1;
   }

   private static byte[] copyOfRangeIfValid(byte[] var0, int var1, int var2) {
      return var2 <= var1 ? Util.EMPTY_BYTE_ARRAY : Arrays.copyOfRange(var0, var1, var2);
   }

   private static ApicFrame decodeApicFrame(ParsableByteArray var0, int var1, int var2) throws UnsupportedEncodingException {
      int var3 = var0.readUnsignedByte();
      String var4 = getCharsetName(var3);
      --var1;
      byte[] var5 = new byte[var1];
      var0.readBytes(var5, 0, var1);
      String var9;
      if (var2 == 2) {
         StringBuilder var8 = new StringBuilder();
         var8.append("image/");
         var8.append(Util.toLowerInvariant(new String(var5, 0, 3, "ISO-8859-1")));
         String var6 = var8.toString();
         var9 = var6;
         if ("image/jpg".equals(var6)) {
            var9 = "image/jpeg";
         }

         var1 = 2;
      } else {
         var1 = indexOfZeroByte(var5, 0);
         var9 = Util.toLowerInvariant(new String(var5, 0, var1, "ISO-8859-1"));
         if (var9.indexOf(47) == -1) {
            StringBuilder var11 = new StringBuilder();
            var11.append("image/");
            var11.append(var9);
            var9 = var11.toString();
         }
      }

      byte var10 = var5[var1 + 1];
      var1 += 2;
      int var7 = indexOfEos(var5, var1, var3);
      return new ApicFrame(var9, new String(var5, var1, var7 - var1, var4), var10 & 255, copyOfRangeIfValid(var5, var7 + delimiterLength(var3), var5.length));
   }

   private static BinaryFrame decodeBinaryFrame(ParsableByteArray var0, int var1, String var2) {
      byte[] var3 = new byte[var1];
      var0.readBytes(var3, 0, var1);
      return new BinaryFrame(var2, var3);
   }

   private static ChapterFrame decodeChapterFrame(ParsableByteArray var0, int var1, int var2, boolean var3, int var4, Id3Decoder.FramePredicate var5) throws UnsupportedEncodingException {
      int var6 = var0.getPosition();
      int var7 = indexOfZeroByte(var0.data, var6);
      String var8 = new String(var0.data, var6, var7 - var6, "ISO-8859-1");
      var0.setPosition(var7 + 1);
      int var9 = var0.readInt();
      var7 = var0.readInt();
      long var10 = var0.readUnsignedInt();
      if (var10 == 4294967295L) {
         var10 = -1L;
      }

      long var12 = var0.readUnsignedInt();
      if (var12 == 4294967295L) {
         var12 = -1L;
      }

      ArrayList var14 = new ArrayList();

      while(var0.getPosition() < var6 + var1) {
         Id3Frame var15 = decodeFrame(var2, var0, var3, var4, var5);
         if (var15 != null) {
            var14.add(var15);
         }
      }

      Id3Frame[] var16 = new Id3Frame[var14.size()];
      var14.toArray(var16);
      return new ChapterFrame(var8, var9, var7, var10, var12, var16);
   }

   private static ChapterTocFrame decodeChapterTOCFrame(ParsableByteArray var0, int var1, int var2, boolean var3, int var4, Id3Decoder.FramePredicate var5) throws UnsupportedEncodingException {
      int var6 = var0.getPosition();
      int var7 = indexOfZeroByte(var0.data, var6);
      String var8 = new String(var0.data, var6, var7 - var6, "ISO-8859-1");
      var0.setPosition(var7 + 1);
      int var9 = var0.readUnsignedByte();
      var7 = 0;
      boolean var10;
      if ((var9 & 2) != 0) {
         var10 = true;
      } else {
         var10 = false;
      }

      boolean var11;
      if ((var9 & 1) != 0) {
         var11 = true;
      } else {
         var11 = false;
      }

      var9 = var0.readUnsignedByte();

      String[] var12;
      for(var12 = new String[var9]; var7 < var9; ++var7) {
         int var13 = var0.getPosition();
         int var14 = indexOfZeroByte(var0.data, var13);
         var12[var7] = new String(var0.data, var13, var14 - var13, "ISO-8859-1");
         var0.setPosition(var14 + 1);
      }

      ArrayList var15 = new ArrayList();

      while(var0.getPosition() < var6 + var1) {
         Id3Frame var16 = decodeFrame(var2, var0, var3, var4, var5);
         if (var16 != null) {
            var15.add(var16);
         }
      }

      Id3Frame[] var17 = new Id3Frame[var15.size()];
      var15.toArray(var17);
      return new ChapterTocFrame(var8, var10, var11, var12, var17);
   }

   private static CommentFrame decodeCommentFrame(ParsableByteArray var0, int var1) throws UnsupportedEncodingException {
      if (var1 < 4) {
         return null;
      } else {
         int var2 = var0.readUnsignedByte();
         String var3 = getCharsetName(var2);
         byte[] var4 = new byte[3];
         var0.readBytes(var4, 0, 3);
         String var7 = new String(var4, 0, 3);
         var1 -= 4;
         byte[] var5 = new byte[var1];
         var0.readBytes(var5, 0, var1);
         var1 = indexOfEos(var5, 0, var2);
         String var6 = new String(var5, 0, var1, var3);
         var1 += delimiterLength(var2);
         return new CommentFrame(var7, var6, decodeStringIfValid(var5, var1, indexOfEos(var5, var1, var2), var3));
      }
   }

   private static Id3Frame decodeFrame(int param0, ParsableByteArray param1, boolean param2, int param3, Id3Decoder.FramePredicate param4) {
      // $FF: Couldn't be decompiled
   }

   private static GeobFrame decodeGeobFrame(ParsableByteArray var0, int var1) throws UnsupportedEncodingException {
      int var2 = var0.readUnsignedByte();
      String var3 = getCharsetName(var2);
      --var1;
      byte[] var4 = new byte[var1];
      var0.readBytes(var4, 0, var1);
      var1 = indexOfZeroByte(var4, 0);
      String var7 = new String(var4, 0, var1, "ISO-8859-1");
      int var5 = var1 + 1;
      var1 = indexOfEos(var4, var5, var2);
      String var6 = decodeStringIfValid(var4, var5, var1, var3);
      var1 += delimiterLength(var2);
      var5 = indexOfEos(var4, var1, var2);
      return new GeobFrame(var7, var6, decodeStringIfValid(var4, var1, var5, var3), copyOfRangeIfValid(var4, var5 + delimiterLength(var2), var4.length));
   }

   private static Id3Decoder.Id3Header decodeHeader(ParsableByteArray var0) {
      if (var0.bytesLeft() < 10) {
         Log.w("Id3Decoder", "Data too short to be an ID3 tag");
         return null;
      } else {
         int var1 = var0.readUnsignedInt24();
         StringBuilder var7;
         if (var1 != ID3_TAG) {
            var7 = new StringBuilder();
            var7.append("Unexpected first three bytes of ID3 tag header: ");
            var7.append(var1);
            Log.w("Id3Decoder", var7.toString());
            return null;
         } else {
            int var2 = var0.readUnsignedByte();
            boolean var3 = true;
            var0.skipBytes(1);
            int var4 = var0.readUnsignedByte();
            int var5 = var0.readSynchSafeInt();
            boolean var6;
            if (var2 == 2) {
               if ((var4 & 64) != 0) {
                  var6 = true;
               } else {
                  var6 = false;
               }

               var1 = var5;
               if (var6) {
                  Log.w("Id3Decoder", "Skipped ID3 tag with majorVersion=2 and undefined compression scheme");
                  return null;
               }
            } else if (var2 == 3) {
               if ((var4 & 64) != 0) {
                  var6 = true;
               } else {
                  var6 = false;
               }

               var1 = var5;
               if (var6) {
                  var1 = var0.readInt();
                  var0.skipBytes(var1);
                  var1 = var5 - (var1 + 4);
               }
            } else {
               if (var2 != 4) {
                  var7 = new StringBuilder();
                  var7.append("Skipped ID3 tag with unsupported majorVersion=");
                  var7.append(var2);
                  Log.w("Id3Decoder", var7.toString());
                  return null;
               }

               boolean var8;
               if ((var4 & 64) != 0) {
                  var8 = true;
               } else {
                  var8 = false;
               }

               int var10 = var5;
               if (var8) {
                  var1 = var0.readSynchSafeInt();
                  var0.skipBytes(var1 - 4);
                  var10 = var5 - var1;
               }

               boolean var9;
               if ((var4 & 16) != 0) {
                  var9 = true;
               } else {
                  var9 = false;
               }

               var1 = var10;
               if (var9) {
                  var1 = var10 - 10;
               }
            }

            if (var2 >= 4 || (var4 & 128) == 0) {
               var3 = false;
            }

            return new Id3Decoder.Id3Header(var2, var3, var1);
         }
      }
   }

   private static MlltFrame decodeMlltFrame(ParsableByteArray var0, int var1) {
      int var2 = var0.readUnsignedShort();
      int var3 = var0.readUnsignedInt24();
      int var4 = var0.readUnsignedInt24();
      int var5 = var0.readUnsignedByte();
      int var6 = var0.readUnsignedByte();
      ParsableBitArray var7 = new ParsableBitArray();
      var7.reset(var0);
      int var8 = (var1 - 10) * 8 / (var5 + var6);
      int[] var12 = new int[var8];
      int[] var9 = new int[var8];

      for(var1 = 0; var1 < var8; ++var1) {
         int var10 = var7.readBits(var5);
         int var11 = var7.readBits(var6);
         var12[var1] = var10;
         var9[var1] = var11;
      }

      return new MlltFrame(var2, var3, var4, var12, var9);
   }

   private static PrivFrame decodePrivFrame(ParsableByteArray var0, int var1) throws UnsupportedEncodingException {
      byte[] var2 = new byte[var1];
      var0.readBytes(var2, 0, var1);
      var1 = indexOfZeroByte(var2, 0);
      return new PrivFrame(new String(var2, 0, var1, "ISO-8859-1"), copyOfRangeIfValid(var2, var1 + 1, var2.length));
   }

   private static String decodeStringIfValid(byte[] var0, int var1, int var2, String var3) throws UnsupportedEncodingException {
      return var2 > var1 && var2 <= var0.length ? new String(var0, var1, var2 - var1, var3) : "";
   }

   private static TextInformationFrame decodeTextInformationFrame(ParsableByteArray var0, int var1, String var2) throws UnsupportedEncodingException {
      if (var1 < 1) {
         return null;
      } else {
         int var3 = var0.readUnsignedByte();
         String var4 = getCharsetName(var3);
         --var1;
         byte[] var5 = new byte[var1];
         var0.readBytes(var5, 0, var1);
         return new TextInformationFrame(var2, (String)null, new String(var5, 0, indexOfEos(var5, 0, var3), var4));
      }
   }

   private static TextInformationFrame decodeTxxxFrame(ParsableByteArray var0, int var1) throws UnsupportedEncodingException {
      if (var1 < 1) {
         return null;
      } else {
         int var2 = var0.readUnsignedByte();
         String var3 = getCharsetName(var2);
         --var1;
         byte[] var4 = new byte[var1];
         var0.readBytes(var4, 0, var1);
         var1 = indexOfEos(var4, 0, var2);
         String var5 = new String(var4, 0, var1, var3);
         var1 += delimiterLength(var2);
         return new TextInformationFrame("TXXX", var5, decodeStringIfValid(var4, var1, indexOfEos(var4, var1, var2), var3));
      }
   }

   private static UrlLinkFrame decodeUrlLinkFrame(ParsableByteArray var0, int var1, String var2) throws UnsupportedEncodingException {
      byte[] var3 = new byte[var1];
      var0.readBytes(var3, 0, var1);
      return new UrlLinkFrame(var2, (String)null, new String(var3, 0, indexOfZeroByte(var3, 0), "ISO-8859-1"));
   }

   private static UrlLinkFrame decodeWxxxFrame(ParsableByteArray var0, int var1) throws UnsupportedEncodingException {
      if (var1 < 1) {
         return null;
      } else {
         int var2 = var0.readUnsignedByte();
         String var3 = getCharsetName(var2);
         --var1;
         byte[] var4 = new byte[var1];
         var0.readBytes(var4, 0, var1);
         var1 = indexOfEos(var4, 0, var2);
         String var5 = new String(var4, 0, var1, var3);
         var1 += delimiterLength(var2);
         return new UrlLinkFrame("WXXX", var5, decodeStringIfValid(var4, var1, indexOfZeroByte(var4, var1), "ISO-8859-1"));
      }
   }

   private static int delimiterLength(int var0) {
      byte var1;
      if (var0 != 0 && var0 != 3) {
         var1 = 2;
      } else {
         var1 = 1;
      }

      return var1;
   }

   private static String getCharsetName(int var0) {
      if (var0 != 1) {
         if (var0 != 2) {
            return var0 != 3 ? "ISO-8859-1" : "UTF-8";
         } else {
            return "UTF-16BE";
         }
      } else {
         return "UTF-16";
      }
   }

   private static String getFrameId(int var0, int var1, int var2, int var3, int var4) {
      String var5;
      if (var0 == 2) {
         var5 = String.format(Locale.US, "%c%c%c", var1, var2, var3);
      } else {
         var5 = String.format(Locale.US, "%c%c%c%c", var1, var2, var3, var4);
      }

      return var5;
   }

   private static int indexOfEos(byte[] var0, int var1, int var2) {
      int var3 = indexOfZeroByte(var0, var1);
      if (var2 != 0) {
         var1 = var3;
         if (var2 != 3) {
            while(var1 < var0.length - 1) {
               if (var1 % 2 == 0 && var0[var1 + 1] == 0) {
                  return var1;
               }

               var1 = indexOfZeroByte(var0, var1 + 1);
            }

            return var0.length;
         }
      }

      return var3;
   }

   private static int indexOfZeroByte(byte[] var0, int var1) {
      while(var1 < var0.length) {
         if (var0[var1] == 0) {
            return var1;
         }

         ++var1;
      }

      return var0.length;
   }

   // $FF: synthetic method
   static boolean lambda$static$0(int var0, int var1, int var2, int var3, int var4) {
      return false;
   }

   private static int removeUnsynchronization(ParsableByteArray var0, int var1) {
      byte[] var2 = var0.data;
      int var3 = var0.getPosition();
      int var4 = var1;

      while(true) {
         int var5 = var3 + 1;
         if (var5 >= var4) {
            return var4;
         }

         var1 = var4;
         if ((var2[var3] & 255) == 255) {
            var1 = var4;
            if (var2[var5] == 0) {
               System.arraycopy(var2, var3 + 2, var2, var5, var4 - var3 - 2);
               var1 = var4 - 1;
            }
         }

         var3 = var5;
         var4 = var1;
      }
   }

   private static boolean validateFrames(ParsableByteArray var0, int var1, int var2, boolean var3) {
      int var4 = var0.getPosition();

      Throwable var10000;
      while(true) {
         int var5;
         boolean var10001;
         try {
            var5 = var0.bytesLeft();
         } catch (Throwable var43) {
            var10000 = var43;
            var10001 = false;
            break;
         }

         byte var6 = 1;
         if (var5 < var2) {
            var0.setPosition(var4);
            return true;
         }

         long var7;
         int var9;
         if (var1 >= 3) {
            try {
               var5 = var0.readInt();
               var7 = var0.readUnsignedInt();
               var9 = var0.readUnsignedShort();
            } catch (Throwable var42) {
               var10000 = var42;
               var10001 = false;
               break;
            }
         } else {
            int var10;
            try {
               var5 = var0.readUnsignedInt24();
               var10 = var0.readUnsignedInt24();
            } catch (Throwable var41) {
               var10000 = var41;
               var10001 = false;
               break;
            }

            var7 = (long)var10;
            var9 = 0;
         }

         if (var5 == 0 && var7 == 0L && var9 == 0) {
            var0.setPosition(var4);
            return true;
         }

         long var11 = var7;
         if (var1 == 4) {
            var11 = var7;
            if (!var3) {
               if ((8421504L & var7) != 0L) {
                  var0.setPosition(var4);
                  return false;
               }

               var11 = (var7 >> 24 & 255L) << 21 | var7 & 255L | (var7 >> 8 & 255L) << 7 | (var7 >> 16 & 255L) << 14;
            }
         }

         boolean var44;
         boolean var46;
         label623: {
            label622: {
               if (var1 == 4) {
                  if ((var9 & 64) != 0) {
                     var46 = true;
                  } else {
                     var46 = false;
                  }

                  var44 = var46;
                  if ((var9 & 1) != 0) {
                     break label622;
                  }
               } else if (var1 == 3) {
                  if ((var9 & 32) != 0) {
                     var46 = true;
                  } else {
                     var46 = false;
                  }

                  var44 = var46;
                  if ((var9 & 128) != 0) {
                     break label622;
                  }
               } else {
                  var44 = false;
               }

               var46 = false;
               break label623;
            }

            boolean var47 = true;
            var44 = var46;
            var46 = var47;
         }

         byte var45;
         if (var44) {
            var45 = var6;
         } else {
            var45 = 0;
         }

         var9 = var45;
         if (var46) {
            var9 = var45 + 4;
         }

         if (var11 < (long)var9) {
            var0.setPosition(var4);
            return false;
         }

         try {
            var5 = var0.bytesLeft();
         } catch (Throwable var40) {
            var10000 = var40;
            var10001 = false;
            break;
         }

         if ((long)var5 < var11) {
            var0.setPosition(var4);
            return false;
         }

         var5 = (int)var11;

         try {
            var0.skipBytes(var5);
         } catch (Throwable var39) {
            var10000 = var39;
            var10001 = false;
            break;
         }
      }

      Throwable var13 = var10000;
      var0.setPosition(var4);
      throw var13;
   }

   public Metadata decode(MetadataInputBuffer var1) {
      ByteBuffer var2 = var1.data;
      return this.decode(var2.array(), var2.limit());
   }

   public Metadata decode(byte[] var1, int var2) {
      ArrayList var3 = new ArrayList();
      ParsableByteArray var4 = new ParsableByteArray(var1, var2);
      Id3Decoder.Id3Header var9 = decodeHeader(var4);
      if (var9 == null) {
         return null;
      } else {
         int var5 = var4.getPosition();
         byte var10;
         if (var9.majorVersion == 2) {
            var10 = 6;
         } else {
            var10 = 10;
         }

         int var6 = var9.framesSize;
         if (var9.isUnsynchronized) {
            var6 = removeUnsynchronization(var4, var9.framesSize);
         }

         var4.setLimit(var5 + var6);
         var6 = var9.majorVersion;
         boolean var7 = false;
         if (!validateFrames(var4, var6, var10, false)) {
            if (var9.majorVersion != 4 || !validateFrames(var4, 4, var10, true)) {
               StringBuilder var11 = new StringBuilder();
               var11.append("Failed to validate ID3 tag with majorVersion=");
               var11.append(var9.majorVersion);
               Log.w("Id3Decoder", var11.toString());
               return null;
            }

            var7 = true;
         }

         while(var4.bytesLeft() >= var10) {
            Id3Frame var8 = decodeFrame(var9.majorVersion, var4, var7, var10, this.framePredicate);
            if (var8 != null) {
               var3.add(var8);
            }
         }

         return new Metadata(var3);
      }
   }

   public interface FramePredicate {
      boolean evaluate(int var1, int var2, int var3, int var4, int var5);
   }

   private static final class Id3Header {
      private final int framesSize;
      private final boolean isUnsynchronized;
      private final int majorVersion;

      public Id3Header(int var1, boolean var2, int var3) {
         this.majorVersion = var1;
         this.isUnsynchronized = var2;
         this.framesSize = var3;
      }
   }
}
