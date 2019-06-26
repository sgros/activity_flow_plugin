package com.google.zxing.qrcode.decoder;

public enum Mode {
   ALPHANUMERIC(new int[]{9, 11, 13}, 2),
   BYTE(new int[]{8, 16, 16}, 4),
   ECI(new int[]{0, 0, 0}, 7),
   FNC1_FIRST_POSITION(new int[]{0, 0, 0}, 5),
   FNC1_SECOND_POSITION(new int[]{0, 0, 0}, 9),
   HANZI(new int[]{8, 10, 12}, 13),
   KANJI(new int[]{8, 10, 12}, 8),
   NUMERIC(new int[]{10, 12, 14}, 1),
   STRUCTURED_APPEND(new int[]{0, 0, 0}, 3),
   TERMINATOR(new int[]{0, 0, 0}, 0);

   private final int bits;
   private final int[] characterCountBitsForVersions;

   private Mode(int[] var3, int var4) {
      this.characterCountBitsForVersions = var3;
      this.bits = var4;
   }

   public static Mode forBits(int var0) {
      Mode var1;
      switch(var0) {
      case 0:
         var1 = TERMINATOR;
         break;
      case 1:
         var1 = NUMERIC;
         break;
      case 2:
         var1 = ALPHANUMERIC;
         break;
      case 3:
         var1 = STRUCTURED_APPEND;
         break;
      case 4:
         var1 = BYTE;
         break;
      case 5:
         var1 = FNC1_FIRST_POSITION;
         break;
      case 6:
      case 10:
      case 11:
      case 12:
      default:
         throw new IllegalArgumentException();
      case 7:
         var1 = ECI;
         break;
      case 8:
         var1 = KANJI;
         break;
      case 9:
         var1 = FNC1_SECOND_POSITION;
         break;
      case 13:
         var1 = HANZI;
      }

      return var1;
   }

   public int getBits() {
      return this.bits;
   }

   public int getCharacterCountBits(Version var1) {
      int var2 = var1.getVersionNumber();
      byte var3;
      if (var2 <= 9) {
         var3 = 0;
      } else if (var2 <= 26) {
         var3 = 1;
      } else {
         var3 = 2;
      }

      return this.characterCountBitsForVersions[var3];
   }
}
