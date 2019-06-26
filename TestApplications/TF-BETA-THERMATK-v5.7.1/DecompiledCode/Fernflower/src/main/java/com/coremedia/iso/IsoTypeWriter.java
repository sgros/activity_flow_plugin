package com.coremedia.iso;

import java.nio.ByteBuffer;

public final class IsoTypeWriter {
   public static void writeFixedPoint0230(ByteBuffer var0, double var1) {
      int var3 = (int)(var1 * 1.073741824E9D);
      var0.put((byte)((-16777216 & var3) >> 24));
      var0.put((byte)((16711680 & var3) >> 16));
      var0.put((byte)(('\uff00' & var3) >> 8));
      var0.put((byte)(var3 & 255));
   }

   public static void writeFixedPoint1616(ByteBuffer var0, double var1) {
      int var3 = (int)(var1 * 65536.0D);
      var0.put((byte)((-16777216 & var3) >> 24));
      var0.put((byte)((16711680 & var3) >> 16));
      var0.put((byte)(('\uff00' & var3) >> 8));
      var0.put((byte)(var3 & 255));
   }

   public static void writeFixedPoint88(ByteBuffer var0, double var1) {
      short var3 = (short)((int)(var1 * 256.0D));
      var0.put((byte)(('\uff00' & var3) >> 8));
      var0.put((byte)(var3 & 255));
   }

   public static void writeIso639(ByteBuffer var0, String var1) {
      if (var1.getBytes().length != 3) {
         StringBuilder var4 = new StringBuilder("\"");
         var4.append(var1);
         var4.append("\" language string isn't exactly 3 characters long!");
         throw new IllegalArgumentException(var4.toString());
      } else {
         int var2 = 0;

         int var3;
         for(var3 = 0; var2 < 3; ++var2) {
            var3 += var1.getBytes()[var2] - 96 << (2 - var2) * 5;
         }

         writeUInt16(var0, var3);
      }
   }

   public static void writeUInt16(ByteBuffer var0, int var1) {
      var1 &= 65535;
      writeUInt8(var0, var1 >> 8);
      writeUInt8(var0, var1 & 255);
   }

   public static void writeUInt24(ByteBuffer var0, int var1) {
      var1 &= 16777215;
      writeUInt16(var0, var1 >> 8);
      writeUInt8(var0, var1);
   }

   public static void writeUInt32(ByteBuffer var0, long var1) {
      var0.putInt((int)var1);
   }

   public static void writeUInt64(ByteBuffer var0, long var1) {
      var0.putLong(var1);
   }

   public static void writeUInt8(ByteBuffer var0, int var1) {
      var0.put((byte)(var1 & 255));
   }

   public static void writeUtf8String(ByteBuffer var0, String var1) {
      var0.put(Utf8.convert(var1));
      writeUInt8(var0, 0);
   }
}
