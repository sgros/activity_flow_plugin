package com.bumptech.glide.load.resource.bitmap;

import android.util.Log;
import com.bumptech.glide.load.ImageHeaderParser;
import com.bumptech.glide.load.engine.bitmap_recycle.ArrayPool;
import com.bumptech.glide.util.Preconditions;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.Charset;

public final class DefaultImageHeaderParser implements ImageHeaderParser {
   private static final int[] BYTES_PER_FORMAT = new int[]{0, 1, 1, 2, 4, 8, 1, 1, 2, 4, 8, 4, 8};
   static final byte[] JPEG_EXIF_SEGMENT_PREAMBLE_BYTES = "Exif\u0000\u0000".getBytes(Charset.forName("UTF-8"));

   private static int calcTagOffset(int var0, int var1) {
      return var0 + 2 + var1 * 12;
   }

   private int getOrientation(DefaultImageHeaderParser.Reader var1, ArrayPool var2) throws IOException {
      int var3 = var1.getUInt16();
      if (!handles(var3)) {
         if (Log.isLoggable("DfltImageHeaderParser", 3)) {
            StringBuilder var7 = new StringBuilder();
            var7.append("Parser doesn't handle magic number: ");
            var7.append(var3);
            Log.d("DfltImageHeaderParser", var7.toString());
         }

         return -1;
      } else {
         var3 = this.moveToExifSegmentAndGetLength(var1);
         if (var3 == -1) {
            if (Log.isLoggable("DfltImageHeaderParser", 3)) {
               Log.d("DfltImageHeaderParser", "Failed to parse exif segment length, or exif segment not found");
            }

            return -1;
         } else {
            byte[] var4 = (byte[])var2.get(var3, byte[].class);

            try {
               var3 = this.parseExifSegment(var1, var4, var3);
            } finally {
               var2.put(var4, byte[].class);
            }

            return var3;
         }
      }
   }

   private ImageHeaderParser.ImageType getType(DefaultImageHeaderParser.Reader var1) throws IOException {
      int var2 = var1.getUInt16();
      if (var2 == 65496) {
         return ImageHeaderParser.ImageType.JPEG;
      } else {
         var2 = var2 << 16 & -65536 | var1.getUInt16() & '\uffff';
         ImageHeaderParser.ImageType var3;
         if (var2 == -1991225785) {
            var1.skip(21L);
            if (var1.getByte() >= 3) {
               var3 = ImageHeaderParser.ImageType.PNG_A;
            } else {
               var3 = ImageHeaderParser.ImageType.PNG;
            }

            return var3;
         } else if (var2 >> 8 == 4671814) {
            return ImageHeaderParser.ImageType.GIF;
         } else if (var2 != 1380533830) {
            return ImageHeaderParser.ImageType.UNKNOWN;
         } else {
            var1.skip(4L);
            if ((var1.getUInt16() << 16 & -65536 | var1.getUInt16() & '\uffff') != 1464156752) {
               return ImageHeaderParser.ImageType.UNKNOWN;
            } else {
               var2 = var1.getUInt16() << 16 & -65536 | var1.getUInt16() & '\uffff';
               if ((var2 & -256) != 1448097792) {
                  return ImageHeaderParser.ImageType.UNKNOWN;
               } else {
                  var2 &= 255;
                  if (var2 == 88) {
                     var1.skip(4L);
                     if ((var1.getByte() & 16) != 0) {
                        var3 = ImageHeaderParser.ImageType.WEBP_A;
                     } else {
                        var3 = ImageHeaderParser.ImageType.WEBP;
                     }

                     return var3;
                  } else if (var2 == 76) {
                     var1.skip(4L);
                     if ((var1.getByte() & 8) != 0) {
                        var3 = ImageHeaderParser.ImageType.WEBP_A;
                     } else {
                        var3 = ImageHeaderParser.ImageType.WEBP;
                     }

                     return var3;
                  } else {
                     return ImageHeaderParser.ImageType.WEBP;
                  }
               }
            }
         }
      }
   }

   private static boolean handles(int var0) {
      boolean var1;
      if ((var0 & '\uffd8') != 65496 && var0 != 19789 && var0 != 18761) {
         var1 = false;
      } else {
         var1 = true;
      }

      return var1;
   }

   private boolean hasJpegExifPreamble(byte[] var1, int var2) {
      boolean var3;
      if (var1 != null && var2 > JPEG_EXIF_SEGMENT_PREAMBLE_BYTES.length) {
         var3 = true;
      } else {
         var3 = false;
      }

      boolean var4 = var3;
      if (var3) {
         var2 = 0;

         while(true) {
            var4 = var3;
            if (var2 >= JPEG_EXIF_SEGMENT_PREAMBLE_BYTES.length) {
               break;
            }

            if (var1[var2] != JPEG_EXIF_SEGMENT_PREAMBLE_BYTES[var2]) {
               var4 = false;
               break;
            }

            ++var2;
         }
      }

      return var4;
   }

   private int moveToExifSegmentAndGetLength(DefaultImageHeaderParser.Reader var1) throws IOException {
      while(true) {
         short var2 = var1.getUInt8();
         StringBuilder var8;
         if (var2 != 255) {
            if (Log.isLoggable("DfltImageHeaderParser", 3)) {
               var8 = new StringBuilder();
               var8.append("Unknown segmentId=");
               var8.append(var2);
               Log.d("DfltImageHeaderParser", var8.toString());
            }

            return -1;
         }

         var2 = var1.getUInt8();
         if (var2 == 218) {
            return -1;
         }

         if (var2 == 217) {
            if (Log.isLoggable("DfltImageHeaderParser", 3)) {
               Log.d("DfltImageHeaderParser", "Found MARKER_EOI in exif segment");
            }

            return -1;
         }

         int var3 = var1.getUInt16() - 2;
         if (var2 != 225) {
            long var4 = (long)var3;
            long var6 = var1.skip(var4);
            if (var6 == var4) {
               continue;
            }

            if (Log.isLoggable("DfltImageHeaderParser", 3)) {
               var8 = new StringBuilder();
               var8.append("Unable to skip enough data, type: ");
               var8.append(var2);
               var8.append(", wanted to skip: ");
               var8.append(var3);
               var8.append(", but actually skipped: ");
               var8.append(var6);
               Log.d("DfltImageHeaderParser", var8.toString());
            }

            return -1;
         }

         return var3;
      }
   }

   private static int parseExifSegment(DefaultImageHeaderParser.RandomAccessReader var0) {
      int var1 = "Exif\u0000\u0000".length();
      short var2 = var0.getInt16(var1);
      ByteOrder var3;
      StringBuilder var11;
      if (var2 == 19789) {
         var3 = ByteOrder.BIG_ENDIAN;
      } else if (var2 == 18761) {
         var3 = ByteOrder.LITTLE_ENDIAN;
      } else {
         if (Log.isLoggable("DfltImageHeaderParser", 3)) {
            var11 = new StringBuilder();
            var11.append("Unknown endianness = ");
            var11.append(var2);
            Log.d("DfltImageHeaderParser", var11.toString());
         }

         var3 = ByteOrder.BIG_ENDIAN;
      }

      var0.order(var3);
      int var4 = var0.getInt32(var1 + 4) + var1;
      short var9 = var0.getInt16(var4);

      for(int var10 = 0; var10 < var9; ++var10) {
         int var5 = calcTagOffset(var4, var10);
         short var6 = var0.getInt16(var5);
         if (var6 == 274) {
            short var7 = var0.getInt16(var5 + 2);
            if (var7 >= 1 && var7 <= 12) {
               int var8 = var0.getInt32(var5 + 4);
               if (var8 < 0) {
                  if (Log.isLoggable("DfltImageHeaderParser", 3)) {
                     Log.d("DfltImageHeaderParser", "Negative tiff component count");
                  }
               } else {
                  if (Log.isLoggable("DfltImageHeaderParser", 3)) {
                     var11 = new StringBuilder();
                     var11.append("Got tagIndex=");
                     var11.append(var10);
                     var11.append(" tagType=");
                     var11.append(var6);
                     var11.append(" formatCode=");
                     var11.append(var7);
                     var11.append(" componentCount=");
                     var11.append(var8);
                     Log.d("DfltImageHeaderParser", var11.toString());
                  }

                  var8 += BYTES_PER_FORMAT[var7];
                  if (var8 > 4) {
                     if (Log.isLoggable("DfltImageHeaderParser", 3)) {
                        var11 = new StringBuilder();
                        var11.append("Got byte count > 4, not orientation, continuing, formatCode=");
                        var11.append(var7);
                        Log.d("DfltImageHeaderParser", var11.toString());
                     }
                  } else {
                     int var12 = var5 + 8;
                     if (var12 >= 0 && var12 <= var0.length()) {
                        if (var8 >= 0 && var8 + var12 <= var0.length()) {
                           return var0.getInt16(var12);
                        }

                        if (Log.isLoggable("DfltImageHeaderParser", 3)) {
                           var11 = new StringBuilder();
                           var11.append("Illegal number of bytes for TI tag data tagType=");
                           var11.append(var6);
                           Log.d("DfltImageHeaderParser", var11.toString());
                        }
                     } else if (Log.isLoggable("DfltImageHeaderParser", 3)) {
                        var11 = new StringBuilder();
                        var11.append("Illegal tagValueOffset=");
                        var11.append(var12);
                        var11.append(" tagType=");
                        var11.append(var6);
                        Log.d("DfltImageHeaderParser", var11.toString());
                     }
                  }
               }
            } else if (Log.isLoggable("DfltImageHeaderParser", 3)) {
               var11 = new StringBuilder();
               var11.append("Got invalid format code = ");
               var11.append(var7);
               Log.d("DfltImageHeaderParser", var11.toString());
            }
         }
      }

      return -1;
   }

   private int parseExifSegment(DefaultImageHeaderParser.Reader var1, byte[] var2, int var3) throws IOException {
      int var4 = var1.read(var2, var3);
      if (var4 != var3) {
         if (Log.isLoggable("DfltImageHeaderParser", 3)) {
            StringBuilder var5 = new StringBuilder();
            var5.append("Unable to read exif segment data, length: ");
            var5.append(var3);
            var5.append(", actually read: ");
            var5.append(var4);
            Log.d("DfltImageHeaderParser", var5.toString());
         }

         return -1;
      } else if (this.hasJpegExifPreamble(var2, var3)) {
         return parseExifSegment(new DefaultImageHeaderParser.RandomAccessReader(var2, var3));
      } else {
         if (Log.isLoggable("DfltImageHeaderParser", 3)) {
            Log.d("DfltImageHeaderParser", "Missing jpeg exif preamble");
         }

         return -1;
      }
   }

   public int getOrientation(InputStream var1, ArrayPool var2) throws IOException {
      return this.getOrientation((DefaultImageHeaderParser.Reader)(new DefaultImageHeaderParser.StreamReader((InputStream)Preconditions.checkNotNull(var1))), (ArrayPool)Preconditions.checkNotNull(var2));
   }

   public ImageHeaderParser.ImageType getType(InputStream var1) throws IOException {
      return this.getType((DefaultImageHeaderParser.Reader)(new DefaultImageHeaderParser.StreamReader((InputStream)Preconditions.checkNotNull(var1))));
   }

   private static final class RandomAccessReader {
      private final ByteBuffer data;

      RandomAccessReader(byte[] var1, int var2) {
         this.data = (ByteBuffer)ByteBuffer.wrap(var1).order(ByteOrder.BIG_ENDIAN).limit(var2);
      }

      private boolean isAvailable(int var1, int var2) {
         boolean var3;
         if (this.data.remaining() - var1 >= var2) {
            var3 = true;
         } else {
            var3 = false;
         }

         return var3;
      }

      short getInt16(int var1) {
         short var2;
         if (this.isAvailable(var1, 2)) {
            short var3 = this.data.getShort(var1);
            var2 = var3;
         } else {
            byte var4 = -1;
            var2 = var4;
         }

         return var2;
      }

      int getInt32(int var1) {
         if (this.isAvailable(var1, 4)) {
            var1 = this.data.getInt(var1);
         } else {
            var1 = -1;
         }

         return var1;
      }

      int length() {
         return this.data.remaining();
      }

      void order(ByteOrder var1) {
         this.data.order(var1);
      }
   }

   private interface Reader {
      int getByte() throws IOException;

      int getUInt16() throws IOException;

      short getUInt8() throws IOException;

      int read(byte[] var1, int var2) throws IOException;

      long skip(long var1) throws IOException;
   }

   private static final class StreamReader implements DefaultImageHeaderParser.Reader {
      private final InputStream is;

      StreamReader(InputStream var1) {
         this.is = var1;
      }

      public int getByte() throws IOException {
         return this.is.read();
      }

      public int getUInt16() throws IOException {
         return this.is.read() << 8 & '\uff00' | this.is.read() & 255;
      }

      public short getUInt8() throws IOException {
         return (short)(this.is.read() & 255);
      }

      public int read(byte[] var1, int var2) throws IOException {
         int var3;
         int var4;
         for(var3 = var2; var3 > 0; var3 -= var4) {
            var4 = this.is.read(var1, var2 - var3, var3);
            if (var4 == -1) {
               break;
            }
         }

         return var2 - var3;
      }

      public long skip(long var1) throws IOException {
         if (var1 < 0L) {
            return 0L;
         } else {
            long var3 = var1;

            while(var3 > 0L) {
               long var5 = this.is.skip(var3);
               if (var5 > 0L) {
                  var3 -= var5;
               } else {
                  if (this.is.read() == -1) {
                     break;
                  }

                  --var3;
               }
            }

            return var1 - var3;
         }
      }
   }
}
