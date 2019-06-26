package org.mapsforge.map.reader;

final class Deserializer {
   private Deserializer() {
      throw new IllegalStateException();
   }

   static long getFiveBytesLong(byte[] var0, int var1) {
      return ((long)var0[var1] & 255L) << 32 | ((long)var0[var1 + 1] & 255L) << 24 | ((long)var0[var1 + 2] & 255L) << 16 | ((long)var0[var1 + 3] & 255L) << 8 | (long)var0[var1 + 4] & 255L;
   }

   static int getInt(byte[] var0, int var1) {
      return var0[var1] << 24 | (var0[var1 + 1] & 255) << 16 | (var0[var1 + 2] & 255) << 8 | var0[var1 + 3] & 255;
   }

   static long getLong(byte[] var0, int var1) {
      return ((long)var0[var1] & 255L) << 56 | ((long)var0[var1 + 1] & 255L) << 48 | ((long)var0[var1 + 2] & 255L) << 40 | ((long)var0[var1 + 3] & 255L) << 32 | ((long)var0[var1 + 4] & 255L) << 24 | ((long)var0[var1 + 5] & 255L) << 16 | ((long)var0[var1 + 6] & 255L) << 8 | (long)var0[var1 + 7] & 255L;
   }

   static int getShort(byte[] var0, int var1) {
      return var0[var1] << 8 | var0[var1 + 1] & 255;
   }
}
