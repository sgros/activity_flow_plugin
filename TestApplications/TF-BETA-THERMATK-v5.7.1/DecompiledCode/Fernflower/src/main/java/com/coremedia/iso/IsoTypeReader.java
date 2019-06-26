package com.coremedia.iso;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

public final class IsoTypeReader {
   public static int byte2int(byte var0) {
      int var1 = var0;
      if (var0 < 0) {
         var1 = var0 + 256;
      }

      return var1;
   }

   public static String read4cc(ByteBuffer var0) {
      byte[] var1 = new byte[4];
      var0.get(var1);

      try {
         String var3 = new String(var1, "ISO-8859-1");
         return var3;
      } catch (UnsupportedEncodingException var2) {
         throw new RuntimeException(var2);
      }
   }

   public static double readFixedPoint0230(ByteBuffer var0) {
      byte[] var1 = new byte[4];
      var0.get(var1);
      double var2 = (double)(0 | var1[0] << 24 & -16777216 | var1[1] << 16 & 16711680 | var1[2] << 8 & '\uff00' | var1[3] & 255);
      Double.isNaN(var2);
      return var2 / 1.073741824E9D;
   }

   public static double readFixedPoint1616(ByteBuffer var0) {
      byte[] var1 = new byte[4];
      var0.get(var1);
      double var2 = (double)(0 | var1[0] << 24 & -16777216 | var1[1] << 16 & 16711680 | var1[2] << 8 & '\uff00' | var1[3] & 255);
      Double.isNaN(var2);
      return var2 / 65536.0D;
   }

   public static float readFixedPoint88(ByteBuffer var0) {
      byte[] var1 = new byte[2];
      var0.get(var1);
      return (float)((short)((short)(0 | var1[0] << 8 & '\uff00') | var1[1] & 255)) / 256.0F;
   }

   public static String readIso639(ByteBuffer var0) {
      int var1 = readUInt16(var0);
      StringBuilder var3 = new StringBuilder();

      for(int var2 = 0; var2 < 3; ++var2) {
         var3.append((char)((var1 >> (2 - var2) * 5 & 31) + 96));
      }

      return var3.toString();
   }

   public static String readString(ByteBuffer var0, int var1) {
      byte[] var2 = new byte[var1];
      var0.get(var2);
      return Utf8.convert(var2);
   }

   public static int readUInt16(ByteBuffer var0) {
      return (byte2int(var0.get()) << 8) + 0 + byte2int(var0.get());
   }

   public static int readUInt24(ByteBuffer var0) {
      return (readUInt16(var0) << 8) + 0 + byte2int(var0.get());
   }

   public static long readUInt32(ByteBuffer var0) {
      long var1 = (long)var0.getInt();
      long var3 = var1;
      if (var1 < 0L) {
         var3 = var1 + 4294967296L;
      }

      return var3;
   }

   public static long readUInt64(ByteBuffer var0) {
      long var1 = (readUInt32(var0) << 32) + 0L;
      if (var1 >= 0L) {
         return var1 + readUInt32(var0);
      } else {
         throw new RuntimeException("I don't know how to deal with UInt64! long is not sufficient and I don't want to use BigInt");
      }
   }

   public static int readUInt8(ByteBuffer var0) {
      return byte2int(var0.get());
   }
}
