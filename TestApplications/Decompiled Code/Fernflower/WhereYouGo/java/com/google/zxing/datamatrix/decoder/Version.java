package com.google.zxing.datamatrix.decoder;

import com.google.zxing.FormatException;

public final class Version {
   private static final Version[] VERSIONS = buildVersions();
   private final int dataRegionSizeColumns;
   private final int dataRegionSizeRows;
   private final Version.ECBlocks ecBlocks;
   private final int symbolSizeColumns;
   private final int symbolSizeRows;
   private final int totalCodewords;
   private final int versionNumber;

   private Version(int var1, int var2, int var3, int var4, int var5, Version.ECBlocks var6) {
      this.versionNumber = var1;
      this.symbolSizeRows = var2;
      this.symbolSizeColumns = var3;
      this.dataRegionSizeRows = var4;
      this.dataRegionSizeColumns = var5;
      this.ecBlocks = var6;
      var2 = 0;
      var4 = var6.getECCodewords();
      Version.ECB[] var8 = var6.getECBlocks();
      var3 = var8.length;

      for(var1 = 0; var1 < var3; ++var1) {
         Version.ECB var7 = var8[var1];
         var2 += var7.getCount() * (var7.getDataCodewords() + var4);
      }

      this.totalCodewords = var2;
   }

   private static Version[] buildVersions() {
      return new Version[]{new Version(1, 10, 10, 8, 8, new Version.ECBlocks(5, new Version.ECB(1, 3))), new Version(2, 12, 12, 10, 10, new Version.ECBlocks(7, new Version.ECB(1, 5))), new Version(3, 14, 14, 12, 12, new Version.ECBlocks(10, new Version.ECB(1, 8))), new Version(4, 16, 16, 14, 14, new Version.ECBlocks(12, new Version.ECB(1, 12))), new Version(5, 18, 18, 16, 16, new Version.ECBlocks(14, new Version.ECB(1, 18))), new Version(6, 20, 20, 18, 18, new Version.ECBlocks(18, new Version.ECB(1, 22))), new Version(7, 22, 22, 20, 20, new Version.ECBlocks(20, new Version.ECB(1, 30))), new Version(8, 24, 24, 22, 22, new Version.ECBlocks(24, new Version.ECB(1, 36))), new Version(9, 26, 26, 24, 24, new Version.ECBlocks(28, new Version.ECB(1, 44))), new Version(10, 32, 32, 14, 14, new Version.ECBlocks(36, new Version.ECB(1, 62))), new Version(11, 36, 36, 16, 16, new Version.ECBlocks(42, new Version.ECB(1, 86))), new Version(12, 40, 40, 18, 18, new Version.ECBlocks(48, new Version.ECB(1, 114))), new Version(13, 44, 44, 20, 20, new Version.ECBlocks(56, new Version.ECB(1, 144))), new Version(14, 48, 48, 22, 22, new Version.ECBlocks(68, new Version.ECB(1, 174))), new Version(15, 52, 52, 24, 24, new Version.ECBlocks(42, new Version.ECB(2, 102))), new Version(16, 64, 64, 14, 14, new Version.ECBlocks(56, new Version.ECB(2, 140))), new Version(17, 72, 72, 16, 16, new Version.ECBlocks(36, new Version.ECB(4, 92))), new Version(18, 80, 80, 18, 18, new Version.ECBlocks(48, new Version.ECB(4, 114))), new Version(19, 88, 88, 20, 20, new Version.ECBlocks(56, new Version.ECB(4, 144))), new Version(20, 96, 96, 22, 22, new Version.ECBlocks(68, new Version.ECB(4, 174))), new Version(21, 104, 104, 24, 24, new Version.ECBlocks(56, new Version.ECB(6, 136))), new Version(22, 120, 120, 18, 18, new Version.ECBlocks(68, new Version.ECB(6, 175))), new Version(23, 132, 132, 20, 20, new Version.ECBlocks(62, new Version.ECB(8, 163))), new Version(24, 144, 144, 22, 22, new Version.ECBlocks(62, new Version.ECB(8, 156), new Version.ECB(2, 155))), new Version(25, 8, 18, 6, 16, new Version.ECBlocks(7, new Version.ECB(1, 5))), new Version(26, 8, 32, 6, 14, new Version.ECBlocks(11, new Version.ECB(1, 10))), new Version(27, 12, 26, 10, 24, new Version.ECBlocks(14, new Version.ECB(1, 16))), new Version(28, 12, 36, 10, 16, new Version.ECBlocks(18, new Version.ECB(1, 22))), new Version(29, 16, 36, 14, 16, new Version.ECBlocks(24, new Version.ECB(1, 32))), new Version(30, 16, 48, 14, 22, new Version.ECBlocks(28, new Version.ECB(1, 49)))};
   }

   public static Version getVersionForDimensions(int var0, int var1) throws FormatException {
      if ((var0 & 1) == 0 && (var1 & 1) == 0) {
         Version[] var2 = VERSIONS;
         int var3 = var2.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            Version var5 = var2[var4];
            if (var5.symbolSizeRows == var0 && var5.symbolSizeColumns == var1) {
               return var5;
            }
         }

         throw FormatException.getFormatInstance();
      } else {
         throw FormatException.getFormatInstance();
      }
   }

   public int getDataRegionSizeColumns() {
      return this.dataRegionSizeColumns;
   }

   public int getDataRegionSizeRows() {
      return this.dataRegionSizeRows;
   }

   Version.ECBlocks getECBlocks() {
      return this.ecBlocks;
   }

   public int getSymbolSizeColumns() {
      return this.symbolSizeColumns;
   }

   public int getSymbolSizeRows() {
      return this.symbolSizeRows;
   }

   public int getTotalCodewords() {
      return this.totalCodewords;
   }

   public int getVersionNumber() {
      return this.versionNumber;
   }

   public String toString() {
      return String.valueOf(this.versionNumber);
   }

   static final class ECB {
      private final int count;
      private final int dataCodewords;

      private ECB(int var1, int var2) {
         this.count = var1;
         this.dataCodewords = var2;
      }

      // $FF: synthetic method
      ECB(int var1, int var2, Object var3) {
         this(var1, var2);
      }

      int getCount() {
         return this.count;
      }

      int getDataCodewords() {
         return this.dataCodewords;
      }
   }

   static final class ECBlocks {
      private final Version.ECB[] ecBlocks;
      private final int ecCodewords;

      private ECBlocks(int var1, Version.ECB var2) {
         this.ecCodewords = var1;
         this.ecBlocks = new Version.ECB[]{var2};
      }

      // $FF: synthetic method
      ECBlocks(int var1, Version.ECB var2, Object var3) {
         this(var1, var2);
      }

      private ECBlocks(int var1, Version.ECB var2, Version.ECB var3) {
         this.ecCodewords = var1;
         this.ecBlocks = new Version.ECB[]{var2, var3};
      }

      // $FF: synthetic method
      ECBlocks(int var1, Version.ECB var2, Version.ECB var3, Object var4) {
         this(var1, var2, var3);
      }

      Version.ECB[] getECBlocks() {
         return this.ecBlocks;
      }

      int getECCodewords() {
         return this.ecCodewords;
      }
   }
}
