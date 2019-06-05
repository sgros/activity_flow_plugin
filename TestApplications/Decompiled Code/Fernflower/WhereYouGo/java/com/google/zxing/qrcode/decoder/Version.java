package com.google.zxing.qrcode.decoder;

import com.google.zxing.FormatException;
import com.google.zxing.common.BitMatrix;

public final class Version {
   private static final Version[] VERSIONS = buildVersions();
   private static final int[] VERSION_DECODE_INFO = new int[]{31892, 34236, 39577, 42195, 48118, 51042, 55367, 58893, 63784, 68472, 70749, 76311, 79154, 84390, 87683, 92361, 96236, 102084, 102881, 110507, 110734, 117786, 119615, 126325, 127568, 133589, 136944, 141498, 145311, 150283, 152622, 158308, 161089, 167017};
   private final int[] alignmentPatternCenters;
   private final Version.ECBlocks[] ecBlocks;
   private final int totalCodewords;
   private final int versionNumber;

   private Version(int var1, int[] var2, Version.ECBlocks... var3) {
      byte var4 = 0;
      super();
      this.versionNumber = var1;
      this.alignmentPatternCenters = var2;
      this.ecBlocks = var3;
      int var5 = 0;
      int var6 = var3[0].getECCodewordsPerBlock();
      Version.ECB[] var8 = var3[0].getECBlocks();
      int var7 = var8.length;

      for(var1 = var4; var1 < var7; ++var1) {
         Version.ECB var9 = var8[var1];
         var5 += var9.getCount() * (var9.getDataCodewords() + var6);
      }

      this.totalCodewords = var5;
   }

   private static Version[] buildVersions() {
      Version.ECBlocks var0 = new Version.ECBlocks(7, new Version.ECB[]{new Version.ECB(1, 19)});
      Version.ECBlocks var1 = new Version.ECBlocks(10, new Version.ECB[]{new Version.ECB(1, 16)});
      Version.ECBlocks var2 = new Version.ECBlocks(13, new Version.ECB[]{new Version.ECB(1, 13)});
      Version.ECBlocks var3 = new Version.ECBlocks(17, new Version.ECB[]{new Version.ECB(1, 9)});
      Version var44 = new Version(1, new int[0], new Version.ECBlocks[]{var0, var1, var2, var3});
      var0 = new Version.ECBlocks(10, new Version.ECB[]{new Version.ECB(1, 34)});
      Version.ECBlocks var4 = new Version.ECBlocks(16, new Version.ECB[]{new Version.ECB(1, 28)});
      var2 = new Version.ECBlocks(22, new Version.ECB[]{new Version.ECB(1, 22)});
      var3 = new Version.ECBlocks(28, new Version.ECB[]{new Version.ECB(1, 16)});
      Version var46 = new Version(2, new int[]{6, 18}, new Version.ECBlocks[]{var0, var4, var2, var3});
      var0 = new Version.ECBlocks(15, new Version.ECB[]{new Version.ECB(1, 55)});
      var4 = new Version.ECBlocks(26, new Version.ECB[]{new Version.ECB(1, 44)});
      Version.ECBlocks var5 = new Version.ECBlocks(18, new Version.ECB[]{new Version.ECB(2, 17)});
      var2 = new Version.ECBlocks(22, new Version.ECB[]{new Version.ECB(2, 13)});
      Version var43 = new Version(3, new int[]{6, 22}, new Version.ECBlocks[]{var0, var4, var5, var2});
      var5 = new Version.ECBlocks(20, new Version.ECB[]{new Version.ECB(1, 80)});
      var4 = new Version.ECBlocks(18, new Version.ECB[]{new Version.ECB(2, 32)});
      Version.ECBlocks var6 = new Version.ECBlocks(26, new Version.ECB[]{new Version.ECB(2, 24)});
      var2 = new Version.ECBlocks(16, new Version.ECB[]{new Version.ECB(4, 9)});
      Version var45 = new Version(4, new int[]{6, 26}, new Version.ECBlocks[]{var5, var4, var6, var2});
      var4 = new Version.ECBlocks(26, new Version.ECB[]{new Version.ECB(1, 108)});
      var5 = new Version.ECBlocks(24, new Version.ECB[]{new Version.ECB(2, 43)});
      Version.ECBlocks var7 = new Version.ECBlocks(18, new Version.ECB[]{new Version.ECB(2, 15), new Version.ECB(2, 16)});
      var6 = new Version.ECBlocks(22, new Version.ECB[]{new Version.ECB(2, 11), new Version.ECB(2, 12)});
      Version var47 = new Version(5, new int[]{6, 30}, new Version.ECBlocks[]{var4, var5, var7, var6});
      var6 = new Version.ECBlocks(18, new Version.ECB[]{new Version.ECB(2, 68)});
      var5 = new Version.ECBlocks(16, new Version.ECB[]{new Version.ECB(4, 27)});
      Version.ECBlocks var8 = new Version.ECBlocks(24, new Version.ECB[]{new Version.ECB(4, 19)});
      var7 = new Version.ECBlocks(28, new Version.ECB[]{new Version.ECB(4, 15)});
      Version var48 = new Version(6, new int[]{6, 34}, new Version.ECBlocks[]{var6, var5, var8, var7});
      var8 = new Version.ECBlocks(20, new Version.ECB[]{new Version.ECB(2, 78)});
      var7 = new Version.ECBlocks(18, new Version.ECB[]{new Version.ECB(4, 31)});
      var6 = new Version.ECBlocks(18, new Version.ECB[]{new Version.ECB(2, 14), new Version.ECB(4, 15)});
      Version.ECBlocks var9 = new Version.ECBlocks(26, new Version.ECB[]{new Version.ECB(4, 13), new Version.ECB(1, 14)});
      Version var49 = new Version(7, new int[]{6, 22, 38}, new Version.ECBlocks[]{var8, var7, var6, var9});
      var8 = new Version.ECBlocks(24, new Version.ECB[]{new Version.ECB(2, 97)});
      var7 = new Version.ECBlocks(22, new Version.ECB[]{new Version.ECB(2, 38), new Version.ECB(2, 39)});
      var9 = new Version.ECBlocks(22, new Version.ECB[]{new Version.ECB(4, 18), new Version.ECB(2, 19)});
      Version.ECBlocks var10 = new Version.ECBlocks(26, new Version.ECB[]{new Version.ECB(4, 14), new Version.ECB(2, 15)});
      Version var50 = new Version(8, new int[]{6, 24, 42}, new Version.ECBlocks[]{var8, var7, var9, var10});
      var8 = new Version.ECBlocks(30, new Version.ECB[]{new Version.ECB(2, 116)});
      Version.ECBlocks var11 = new Version.ECBlocks(22, new Version.ECB[]{new Version.ECB(3, 36), new Version.ECB(2, 37)});
      var10 = new Version.ECBlocks(20, new Version.ECB[]{new Version.ECB(4, 16), new Version.ECB(4, 17)});
      var9 = new Version.ECBlocks(24, new Version.ECB[]{new Version.ECB(4, 12), new Version.ECB(4, 13)});
      Version var51 = new Version(9, new int[]{6, 26, 46}, new Version.ECBlocks[]{var8, var11, var10, var9});
      var11 = new Version.ECBlocks(18, new Version.ECB[]{new Version.ECB(2, 68), new Version.ECB(2, 69)});
      var9 = new Version.ECBlocks(26, new Version.ECB[]{new Version.ECB(4, 43), new Version.ECB(1, 44)});
      Version.ECBlocks var12 = new Version.ECBlocks(24, new Version.ECB[]{new Version.ECB(6, 19), new Version.ECB(2, 20)});
      var10 = new Version.ECBlocks(28, new Version.ECB[]{new Version.ECB(6, 15), new Version.ECB(2, 16)});
      Version var52 = new Version(10, new int[]{6, 28, 50}, new Version.ECBlocks[]{var11, var9, var12, var10});
      var12 = new Version.ECBlocks(20, new Version.ECB[]{new Version.ECB(4, 81)});
      var10 = new Version.ECBlocks(30, new Version.ECB[]{new Version.ECB(1, 50), new Version.ECB(4, 51)});
      Version.ECBlocks var13 = new Version.ECBlocks(28, new Version.ECB[]{new Version.ECB(4, 22), new Version.ECB(4, 23)});
      var11 = new Version.ECBlocks(24, new Version.ECB[]{new Version.ECB(3, 12), new Version.ECB(8, 13)});
      Version var53 = new Version(11, new int[]{6, 30, 54}, new Version.ECBlocks[]{var12, var10, var13, var11});
      var11 = new Version.ECBlocks(24, new Version.ECB[]{new Version.ECB(2, 92), new Version.ECB(2, 93)});
      Version.ECBlocks var14 = new Version.ECBlocks(22, new Version.ECB[]{new Version.ECB(6, 36), new Version.ECB(2, 37)});
      var12 = new Version.ECBlocks(26, new Version.ECB[]{new Version.ECB(4, 20), new Version.ECB(6, 21)});
      var13 = new Version.ECBlocks(28, new Version.ECB[]{new Version.ECB(7, 14), new Version.ECB(4, 15)});
      Version var54 = new Version(12, new int[]{6, 32, 58}, new Version.ECBlocks[]{var11, var14, var12, var13});
      var12 = new Version.ECBlocks(26, new Version.ECB[]{new Version.ECB(4, 107)});
      Version.ECBlocks var15 = new Version.ECBlocks(22, new Version.ECB[]{new Version.ECB(8, 37), new Version.ECB(1, 38)});
      var13 = new Version.ECBlocks(24, new Version.ECB[]{new Version.ECB(8, 20), new Version.ECB(4, 21)});
      var14 = new Version.ECBlocks(22, new Version.ECB[]{new Version.ECB(12, 11), new Version.ECB(4, 12)});
      Version var55 = new Version(13, new int[]{6, 34, 62}, new Version.ECBlocks[]{var12, var15, var13, var14});
      var13 = new Version.ECBlocks(30, new Version.ECB[]{new Version.ECB(3, 115), new Version.ECB(1, 116)});
      var14 = new Version.ECBlocks(24, new Version.ECB[]{new Version.ECB(4, 40), new Version.ECB(5, 41)});
      Version.ECBlocks var16 = new Version.ECBlocks(20, new Version.ECB[]{new Version.ECB(11, 16), new Version.ECB(5, 17)});
      var15 = new Version.ECBlocks(24, new Version.ECB[]{new Version.ECB(11, 12), new Version.ECB(5, 13)});
      Version var56 = new Version(14, new int[]{6, 26, 46, 66}, new Version.ECBlocks[]{var13, var14, var16, var15});
      Version.ECBlocks var17 = new Version.ECBlocks(22, new Version.ECB[]{new Version.ECB(5, 87), new Version.ECB(1, 88)});
      var15 = new Version.ECBlocks(24, new Version.ECB[]{new Version.ECB(5, 41), new Version.ECB(5, 42)});
      var16 = new Version.ECBlocks(30, new Version.ECB[]{new Version.ECB(5, 24), new Version.ECB(7, 25)});
      var14 = new Version.ECBlocks(24, new Version.ECB[]{new Version.ECB(11, 12), new Version.ECB(7, 13)});
      Version var57 = new Version(15, new int[]{6, 26, 48, 70}, new Version.ECBlocks[]{var17, var15, var16, var14});
      var16 = new Version.ECBlocks(24, new Version.ECB[]{new Version.ECB(5, 98), new Version.ECB(1, 99)});
      Version.ECBlocks var18 = new Version.ECBlocks(28, new Version.ECB[]{new Version.ECB(7, 45), new Version.ECB(3, 46)});
      var17 = new Version.ECBlocks(24, new Version.ECB[]{new Version.ECB(15, 19), new Version.ECB(2, 20)});
      var15 = new Version.ECBlocks(30, new Version.ECB[]{new Version.ECB(3, 15), new Version.ECB(13, 16)});
      Version var58 = new Version(16, new int[]{6, 26, 50, 74}, new Version.ECBlocks[]{var16, var18, var17, var15});
      Version.ECBlocks var19 = new Version.ECBlocks(28, new Version.ECB[]{new Version.ECB(1, 107), new Version.ECB(5, 108)});
      var16 = new Version.ECBlocks(28, new Version.ECB[]{new Version.ECB(10, 46), new Version.ECB(1, 47)});
      var17 = new Version.ECBlocks(28, new Version.ECB[]{new Version.ECB(1, 22), new Version.ECB(15, 23)});
      var18 = new Version.ECBlocks(28, new Version.ECB[]{new Version.ECB(2, 14), new Version.ECB(17, 15)});
      Version var59 = new Version(17, new int[]{6, 30, 54, 78}, new Version.ECBlocks[]{var19, var16, var17, var18});
      var17 = new Version.ECBlocks(30, new Version.ECB[]{new Version.ECB(5, 120), new Version.ECB(1, 121)});
      var18 = new Version.ECBlocks(26, new Version.ECB[]{new Version.ECB(9, 43), new Version.ECB(4, 44)});
      var19 = new Version.ECBlocks(28, new Version.ECB[]{new Version.ECB(17, 22), new Version.ECB(1, 23)});
      Version.ECBlocks var20 = new Version.ECBlocks(28, new Version.ECB[]{new Version.ECB(2, 14), new Version.ECB(19, 15)});
      Version var60 = new Version(18, new int[]{6, 30, 56, 82}, new Version.ECBlocks[]{var17, var18, var19, var20});
      var18 = new Version.ECBlocks(28, new Version.ECB[]{new Version.ECB(3, 113), new Version.ECB(4, 114)});
      Version.ECBlocks var21 = new Version.ECBlocks(26, new Version.ECB[]{new Version.ECB(3, 44), new Version.ECB(11, 45)});
      var19 = new Version.ECBlocks(26, new Version.ECB[]{new Version.ECB(17, 21), new Version.ECB(4, 22)});
      var20 = new Version.ECBlocks(26, new Version.ECB[]{new Version.ECB(9, 13), new Version.ECB(16, 14)});
      Version var61 = new Version(19, new int[]{6, 30, 58, 86}, new Version.ECBlocks[]{var18, var21, var19, var20});
      var21 = new Version.ECBlocks(28, new Version.ECB[]{new Version.ECB(3, 107), new Version.ECB(5, 108)});
      var20 = new Version.ECBlocks(26, new Version.ECB[]{new Version.ECB(3, 41), new Version.ECB(13, 42)});
      Version.ECBlocks var22 = new Version.ECBlocks(30, new Version.ECB[]{new Version.ECB(15, 24), new Version.ECB(5, 25)});
      var19 = new Version.ECBlocks(28, new Version.ECB[]{new Version.ECB(15, 15), new Version.ECB(10, 16)});
      Version var62 = new Version(20, new int[]{6, 34, 62, 90}, new Version.ECBlocks[]{var21, var20, var22, var19});
      Version.ECBlocks var23 = new Version.ECBlocks(28, new Version.ECB[]{new Version.ECB(4, 116), new Version.ECB(4, 117)});
      var21 = new Version.ECBlocks(26, new Version.ECB[]{new Version.ECB(17, 42)});
      var20 = new Version.ECBlocks(28, new Version.ECB[]{new Version.ECB(17, 22), new Version.ECB(6, 23)});
      var22 = new Version.ECBlocks(30, new Version.ECB[]{new Version.ECB(19, 16), new Version.ECB(6, 17)});
      Version var63 = new Version(21, new int[]{6, 28, 50, 72, 94}, new Version.ECBlocks[]{var23, var21, var20, var22});
      var22 = new Version.ECBlocks(28, new Version.ECB[]{new Version.ECB(2, 111), new Version.ECB(7, 112)});
      var21 = new Version.ECBlocks(28, new Version.ECB[]{new Version.ECB(17, 46)});
      var23 = new Version.ECBlocks(30, new Version.ECB[]{new Version.ECB(7, 24), new Version.ECB(16, 25)});
      Version.ECBlocks var24 = new Version.ECBlocks(24, new Version.ECB[]{new Version.ECB(34, 13)});
      Version var64 = new Version(22, new int[]{6, 26, 50, 74, 98}, new Version.ECBlocks[]{var22, var21, var23, var24});
      var22 = new Version.ECBlocks(30, new Version.ECB[]{new Version.ECB(4, 121), new Version.ECB(5, 122)});
      var23 = new Version.ECBlocks(28, new Version.ECB[]{new Version.ECB(4, 47), new Version.ECB(14, 48)});
      var24 = new Version.ECBlocks(30, new Version.ECB[]{new Version.ECB(11, 24), new Version.ECB(14, 25)});
      Version.ECBlocks var25 = new Version.ECBlocks(30, new Version.ECB[]{new Version.ECB(16, 15), new Version.ECB(14, 16)});
      Version var65 = new Version(23, new int[]{6, 30, 54, 78, 102}, new Version.ECBlocks[]{var22, var23, var24, var25});
      var25 = new Version.ECBlocks(30, new Version.ECB[]{new Version.ECB(6, 117), new Version.ECB(4, 118)});
      Version.ECBlocks var26 = new Version.ECBlocks(28, new Version.ECB[]{new Version.ECB(6, 45), new Version.ECB(14, 46)});
      var23 = new Version.ECBlocks(30, new Version.ECB[]{new Version.ECB(11, 24), new Version.ECB(16, 25)});
      var24 = new Version.ECBlocks(30, new Version.ECB[]{new Version.ECB(30, 16), new Version.ECB(2, 17)});
      Version var66 = new Version(24, new int[]{6, 28, 54, 80, 106}, new Version.ECBlocks[]{var25, var26, var23, var24});
      Version.ECBlocks var27 = new Version.ECBlocks(26, new Version.ECB[]{new Version.ECB(8, 106), new Version.ECB(4, 107)});
      var26 = new Version.ECBlocks(28, new Version.ECB[]{new Version.ECB(8, 47), new Version.ECB(13, 48)});
      var25 = new Version.ECBlocks(30, new Version.ECB[]{new Version.ECB(7, 24), new Version.ECB(22, 25)});
      var24 = new Version.ECBlocks(30, new Version.ECB[]{new Version.ECB(22, 15), new Version.ECB(13, 16)});
      Version var67 = new Version(25, new int[]{6, 32, 58, 84, 110}, new Version.ECBlocks[]{var27, var26, var25, var24});
      Version.ECBlocks var28 = new Version.ECBlocks(28, new Version.ECB[]{new Version.ECB(10, 114), new Version.ECB(2, 115)});
      var25 = new Version.ECBlocks(28, new Version.ECB[]{new Version.ECB(19, 46), new Version.ECB(4, 47)});
      var27 = new Version.ECBlocks(28, new Version.ECB[]{new Version.ECB(28, 22), new Version.ECB(6, 23)});
      var26 = new Version.ECBlocks(30, new Version.ECB[]{new Version.ECB(33, 16), new Version.ECB(4, 17)});
      Version var68 = new Version(26, new int[]{6, 30, 58, 86, 114}, new Version.ECBlocks[]{var28, var25, var27, var26});
      var27 = new Version.ECBlocks(30, new Version.ECB[]{new Version.ECB(8, 122), new Version.ECB(4, 123)});
      var26 = new Version.ECBlocks(28, new Version.ECB[]{new Version.ECB(22, 45), new Version.ECB(3, 46)});
      var28 = new Version.ECBlocks(30, new Version.ECB[]{new Version.ECB(8, 23), new Version.ECB(26, 24)});
      Version.ECBlocks var29 = new Version.ECBlocks(30, new Version.ECB[]{new Version.ECB(12, 15), new Version.ECB(28, 16)});
      Version var69 = new Version(27, new int[]{6, 34, 62, 90, 118}, new Version.ECBlocks[]{var27, var26, var28, var29});
      var29 = new Version.ECBlocks(30, new Version.ECB[]{new Version.ECB(3, 117), new Version.ECB(10, 118)});
      Version.ECBlocks var30 = new Version.ECBlocks(28, new Version.ECB[]{new Version.ECB(3, 45), new Version.ECB(23, 46)});
      var27 = new Version.ECBlocks(30, new Version.ECB[]{new Version.ECB(4, 24), new Version.ECB(31, 25)});
      var28 = new Version.ECBlocks(30, new Version.ECB[]{new Version.ECB(11, 15), new Version.ECB(31, 16)});
      Version var70 = new Version(28, new int[]{6, 26, 50, 74, 98, 122}, new Version.ECBlocks[]{var29, var30, var27, var28});
      var29 = new Version.ECBlocks(30, new Version.ECB[]{new Version.ECB(7, 116), new Version.ECB(7, 117)});
      var30 = new Version.ECBlocks(28, new Version.ECB[]{new Version.ECB(21, 45), new Version.ECB(7, 46)});
      var28 = new Version.ECBlocks(30, new Version.ECB[]{new Version.ECB(1, 23), new Version.ECB(37, 24)});
      Version.ECBlocks var31 = new Version.ECBlocks(30, new Version.ECB[]{new Version.ECB(19, 15), new Version.ECB(26, 16)});
      Version var71 = new Version(29, new int[]{6, 30, 54, 78, 102, 126}, new Version.ECBlocks[]{var29, var30, var28, var31});
      var29 = new Version.ECBlocks(30, new Version.ECB[]{new Version.ECB(5, 115), new Version.ECB(10, 116)});
      var30 = new Version.ECBlocks(28, new Version.ECB[]{new Version.ECB(19, 47), new Version.ECB(10, 48)});
      var31 = new Version.ECBlocks(30, new Version.ECB[]{new Version.ECB(15, 24), new Version.ECB(25, 25)});
      Version.ECBlocks var32 = new Version.ECBlocks(30, new Version.ECB[]{new Version.ECB(23, 15), new Version.ECB(25, 16)});
      Version var72 = new Version(30, new int[]{6, 26, 52, 78, 104, 130}, new Version.ECBlocks[]{var29, var30, var31, var32});
      var32 = new Version.ECBlocks(30, new Version.ECB[]{new Version.ECB(13, 115), new Version.ECB(3, 116)});
      var30 = new Version.ECBlocks(28, new Version.ECB[]{new Version.ECB(2, 46), new Version.ECB(29, 47)});
      Version.ECBlocks var33 = new Version.ECBlocks(30, new Version.ECB[]{new Version.ECB(42, 24), new Version.ECB(1, 25)});
      var31 = new Version.ECBlocks(30, new Version.ECB[]{new Version.ECB(23, 15), new Version.ECB(28, 16)});
      Version var73 = new Version(31, new int[]{6, 30, 56, 82, 108, 134}, new Version.ECBlocks[]{var32, var30, var33, var31});
      var31 = new Version.ECBlocks(30, new Version.ECB[]{new Version.ECB(17, 115)});
      Version.ECBlocks var34 = new Version.ECBlocks(28, new Version.ECB[]{new Version.ECB(10, 46), new Version.ECB(23, 47)});
      var33 = new Version.ECBlocks(30, new Version.ECB[]{new Version.ECB(10, 24), new Version.ECB(35, 25)});
      var32 = new Version.ECBlocks(30, new Version.ECB[]{new Version.ECB(19, 15), new Version.ECB(35, 16)});
      Version var74 = new Version(32, new int[]{6, 34, 60, 86, 112, 138}, new Version.ECBlocks[]{var31, var34, var33, var32});
      var34 = new Version.ECBlocks(30, new Version.ECB[]{new Version.ECB(17, 115), new Version.ECB(1, 116)});
      var32 = new Version.ECBlocks(28, new Version.ECB[]{new Version.ECB(14, 46), new Version.ECB(21, 47)});
      var33 = new Version.ECBlocks(30, new Version.ECB[]{new Version.ECB(29, 24), new Version.ECB(19, 25)});
      Version.ECBlocks var35 = new Version.ECBlocks(30, new Version.ECB[]{new Version.ECB(11, 15), new Version.ECB(46, 16)});
      Version var75 = new Version(33, new int[]{6, 30, 58, 86, 114, 142}, new Version.ECBlocks[]{var34, var32, var33, var35});
      var33 = new Version.ECBlocks(30, new Version.ECB[]{new Version.ECB(13, 115), new Version.ECB(6, 116)});
      var35 = new Version.ECBlocks(28, new Version.ECB[]{new Version.ECB(14, 46), new Version.ECB(23, 47)});
      Version.ECBlocks var36 = new Version.ECBlocks(30, new Version.ECB[]{new Version.ECB(44, 24), new Version.ECB(7, 25)});
      var34 = new Version.ECBlocks(30, new Version.ECB[]{new Version.ECB(59, 16), new Version.ECB(1, 17)});
      Version var76 = new Version(34, new int[]{6, 34, 62, 90, 118, 146}, new Version.ECBlocks[]{var33, var35, var36, var34});
      var36 = new Version.ECBlocks(30, new Version.ECB[]{new Version.ECB(12, 121), new Version.ECB(7, 122)});
      var35 = new Version.ECBlocks(28, new Version.ECB[]{new Version.ECB(12, 47), new Version.ECB(26, 48)});
      Version.ECBlocks var37 = new Version.ECBlocks(30, new Version.ECB[]{new Version.ECB(39, 24), new Version.ECB(14, 25)});
      var34 = new Version.ECBlocks(30, new Version.ECB[]{new Version.ECB(22, 15), new Version.ECB(41, 16)});
      Version var77 = new Version(35, new int[]{6, 30, 54, 78, 102, 126, 150}, new Version.ECBlocks[]{var36, var35, var37, var34});
      var36 = new Version.ECBlocks(30, new Version.ECB[]{new Version.ECB(6, 121), new Version.ECB(14, 122)});
      var35 = new Version.ECBlocks(28, new Version.ECB[]{new Version.ECB(6, 47), new Version.ECB(34, 48)});
      var37 = new Version.ECBlocks(30, new Version.ECB[]{new Version.ECB(46, 24), new Version.ECB(10, 25)});
      Version.ECBlocks var38 = new Version.ECBlocks(30, new Version.ECB[]{new Version.ECB(2, 15), new Version.ECB(64, 16)});
      Version var78 = new Version(36, new int[]{6, 24, 50, 76, 102, 128, 154}, new Version.ECBlocks[]{var36, var35, var37, var38});
      var37 = new Version.ECBlocks(30, new Version.ECB[]{new Version.ECB(17, 122), new Version.ECB(4, 123)});
      var38 = new Version.ECBlocks(28, new Version.ECB[]{new Version.ECB(29, 46), new Version.ECB(14, 47)});
      var36 = new Version.ECBlocks(30, new Version.ECB[]{new Version.ECB(49, 24), new Version.ECB(10, 25)});
      Version.ECBlocks var39 = new Version.ECBlocks(30, new Version.ECB[]{new Version.ECB(24, 15), new Version.ECB(46, 16)});
      Version var79 = new Version(37, new int[]{6, 28, 54, 80, 106, 132, 158}, new Version.ECBlocks[]{var37, var38, var36, var39});
      var38 = new Version.ECBlocks(30, new Version.ECB[]{new Version.ECB(4, 122), new Version.ECB(18, 123)});
      var37 = new Version.ECBlocks(28, new Version.ECB[]{new Version.ECB(13, 46), new Version.ECB(32, 47)});
      var39 = new Version.ECBlocks(30, new Version.ECB[]{new Version.ECB(48, 24), new Version.ECB(14, 25)});
      Version.ECBlocks var40 = new Version.ECBlocks(30, new Version.ECB[]{new Version.ECB(42, 15), new Version.ECB(32, 16)});
      Version var80 = new Version(38, new int[]{6, 32, 58, 84, 110, 136, 162}, new Version.ECBlocks[]{var38, var37, var39, var40});
      var40 = new Version.ECBlocks(30, new Version.ECB[]{new Version.ECB(20, 117), new Version.ECB(4, 118)});
      var38 = new Version.ECBlocks(28, new Version.ECB[]{new Version.ECB(40, 47), new Version.ECB(7, 48)});
      var39 = new Version.ECBlocks(30, new Version.ECB[]{new Version.ECB(43, 24), new Version.ECB(22, 25)});
      Version.ECBlocks var41 = new Version.ECBlocks(30, new Version.ECB[]{new Version.ECB(10, 15), new Version.ECB(67, 16)});
      Version var42 = new Version(39, new int[]{6, 26, 54, 82, 110, 138, 166}, new Version.ECBlocks[]{var40, var38, var39, var41});
      var40 = new Version.ECBlocks(30, new Version.ECB[]{new Version.ECB(19, 118), new Version.ECB(6, 119)});
      var41 = new Version.ECBlocks(28, new Version.ECB[]{new Version.ECB(18, 47), new Version.ECB(31, 48)});
      var38 = new Version.ECBlocks(30, new Version.ECB[]{new Version.ECB(34, 24), new Version.ECB(34, 25)});
      var39 = new Version.ECBlocks(30, new Version.ECB[]{new Version.ECB(20, 15), new Version.ECB(61, 16)});
      return new Version[]{var44, var46, var43, var45, var47, var48, var49, var50, var51, var52, var53, var54, var55, var56, var57, var58, var59, var60, var61, var62, var63, var64, var65, var66, var67, var68, var69, var70, var71, var72, var73, var74, var75, var76, var77, var78, var79, var80, var42, new Version(40, new int[]{6, 30, 58, 86, 114, 142, 170}, new Version.ECBlocks[]{var40, var41, var38, var39})};
   }

   static Version decodeVersionInformation(int var0) {
      int var1 = Integer.MAX_VALUE;
      int var2 = 0;
      int var3 = 0;

      Version var5;
      while(true) {
         if (var3 >= VERSION_DECODE_INFO.length) {
            if (var1 <= 3) {
               var5 = getVersionForNumber(var2);
            } else {
               var5 = null;
            }
            break;
         }

         int var4 = VERSION_DECODE_INFO[var3];
         if (var4 == var0) {
            var5 = getVersionForNumber(var3 + 7);
            break;
         }

         int var6 = FormatInformation.numBitsDiffering(var0, var4);
         var4 = var1;
         if (var6 < var1) {
            var2 = var3 + 7;
            var4 = var6;
         }

         ++var3;
         var1 = var4;
      }

      return var5;
   }

   public static Version getProvisionalVersionForDimension(int var0) throws FormatException {
      if (var0 % 4 != 1) {
         throw FormatException.getFormatInstance();
      } else {
         try {
            Version var1 = getVersionForNumber((var0 - 17) / 4);
            return var1;
         } catch (IllegalArgumentException var2) {
            throw FormatException.getFormatInstance();
         }
      }
   }

   public static Version getVersionForNumber(int var0) {
      if (var0 > 0 && var0 <= 40) {
         return VERSIONS[var0 - 1];
      } else {
         throw new IllegalArgumentException();
      }
   }

   BitMatrix buildFunctionPattern() {
      int var1 = this.getDimensionForVersion();
      BitMatrix var2 = new BitMatrix(var1);
      var2.setRegion(0, 0, 9, 9);
      var2.setRegion(var1 - 8, 0, 8, 9);
      var2.setRegion(0, var1 - 8, 9, 8);
      int var3 = this.alignmentPatternCenters.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         int var5 = this.alignmentPatternCenters[var4];

         for(int var6 = 0; var6 < var3; ++var6) {
            if ((var4 != 0 || var6 != 0 && var6 != var3 - 1) && (var4 != var3 - 1 || var6 != 0)) {
               var2.setRegion(this.alignmentPatternCenters[var6] - 2, var5 - 2, 5, 5);
            }
         }
      }

      var2.setRegion(6, 9, 1, var1 - 17);
      var2.setRegion(9, 6, var1 - 17, 1);
      if (this.versionNumber > 6) {
         var2.setRegion(var1 - 11, 0, 3, 6);
         var2.setRegion(0, var1 - 11, 6, 3);
      }

      return var2;
   }

   public int[] getAlignmentPatternCenters() {
      return this.alignmentPatternCenters;
   }

   public int getDimensionForVersion() {
      return this.versionNumber * 4 + 17;
   }

   public Version.ECBlocks getECBlocksForLevel(ErrorCorrectionLevel var1) {
      return this.ecBlocks[var1.ordinal()];
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

   public static final class ECB {
      private final int count;
      private final int dataCodewords;

      ECB(int var1, int var2) {
         this.count = var1;
         this.dataCodewords = var2;
      }

      public int getCount() {
         return this.count;
      }

      public int getDataCodewords() {
         return this.dataCodewords;
      }
   }

   public static final class ECBlocks {
      private final Version.ECB[] ecBlocks;
      private final int ecCodewordsPerBlock;

      ECBlocks(int var1, Version.ECB... var2) {
         this.ecCodewordsPerBlock = var1;
         this.ecBlocks = var2;
      }

      public Version.ECB[] getECBlocks() {
         return this.ecBlocks;
      }

      public int getECCodewordsPerBlock() {
         return this.ecCodewordsPerBlock;
      }

      public int getNumBlocks() {
         int var1 = 0;
         Version.ECB[] var2 = this.ecBlocks;
         int var3 = var2.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            var1 += var2[var4].getCount();
         }

         return var1;
      }

      public int getTotalECCodewords() {
         return this.ecCodewordsPerBlock * this.getNumBlocks();
      }
   }
}
