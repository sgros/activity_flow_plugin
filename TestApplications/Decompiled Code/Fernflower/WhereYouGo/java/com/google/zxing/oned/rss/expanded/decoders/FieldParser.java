package com.google.zxing.oned.rss.expanded.decoders;

import com.google.zxing.NotFoundException;

final class FieldParser {
   private static final Object[][] FOUR_DIGIT_DATA_LENGTH;
   private static final Object[][] THREE_DIGIT_DATA_LENGTH;
   private static final Object[][] THREE_DIGIT_PLUS_DIGIT_DATA_LENGTH;
   private static final Object[][] TWO_DIGIT_DATA_LENGTH;
   private static final Object VARIABLE_LENGTH = new Object();

   static {
      Object[] var0 = new Object[]{"00", 18};
      Object[] var1 = new Object[]{"01", 14};
      Object[] var2 = new Object[]{"02", 14};
      Object[] var3 = new Object[]{"10", VARIABLE_LENGTH, 20};
      Object[] var4 = new Object[]{"11", 6};
      Object[] var5 = new Object[]{"12", 6};
      Object[] var6 = new Object[]{"13", 6};
      Object[] var7 = new Object[]{"15", 6};
      Object[] var8 = new Object[]{"17", 6};
      Object[] var9 = new Object[]{"21", VARIABLE_LENGTH, 20};
      Object[] var10 = new Object[]{"22", VARIABLE_LENGTH, 29};
      Object[] var11 = new Object[]{"30", VARIABLE_LENGTH, 8};
      Object[] var12 = new Object[]{"37", VARIABLE_LENGTH, 8};
      Object[] var13 = new Object[]{"90", VARIABLE_LENGTH, 30};
      Object[] var14 = new Object[]{"91", VARIABLE_LENGTH, 30};
      Object[] var15 = new Object[]{"92", VARIABLE_LENGTH, 30};
      Object[] var16 = new Object[]{"93", VARIABLE_LENGTH, 30};
      Object[] var17 = new Object[]{"94", VARIABLE_LENGTH, 30};
      Object[] var18 = new Object[]{"95", VARIABLE_LENGTH, 30};
      Object[] var19 = new Object[]{"96", VARIABLE_LENGTH, 30};
      Object[] var20 = new Object[]{"97", VARIABLE_LENGTH, 30};
      Object[] var21 = new Object[]{"98", VARIABLE_LENGTH, 30};
      Object var22 = VARIABLE_LENGTH;
      TWO_DIGIT_DATA_LENGTH = new Object[][]{var0, var1, var2, var3, var4, var5, var6, var7, var8, {"20", 2}, var9, var10, var11, var12, var13, var14, var15, var16, var17, var18, var19, var20, var21, {"99", var22, 30}};
      var3 = new Object[]{"240", VARIABLE_LENGTH, 30};
      var4 = new Object[]{"241", VARIABLE_LENGTH, 30};
      var5 = new Object[]{"242", VARIABLE_LENGTH, 6};
      var6 = new Object[]{"250", VARIABLE_LENGTH, 30};
      Object var23 = VARIABLE_LENGTH;
      var7 = new Object[]{"253", VARIABLE_LENGTH, 17};
      Object var24 = VARIABLE_LENGTH;
      var8 = new Object[]{"400", VARIABLE_LENGTH, 30};
      Object var25 = VARIABLE_LENGTH;
      var9 = new Object[]{"402", 17};
      var10 = new Object[]{"403", VARIABLE_LENGTH, 30};
      var11 = new Object[]{"413", 13};
      var12 = new Object[]{"414", 13};
      var22 = VARIABLE_LENGTH;
      var13 = new Object[]{"421", VARIABLE_LENGTH, 15};
      var14 = new Object[]{"422", 3};
      var15 = new Object[]{"423", VARIABLE_LENGTH, 15};
      var16 = new Object[]{"424", 3};
      var17 = new Object[]{"426", 3};
      THREE_DIGIT_DATA_LENGTH = new Object[][]{var3, var4, var5, var6, {"251", var23, 30}, var7, {"254", var24, 20}, var8, {"401", var25, 30}, var9, var10, {"410", 13}, {"411", 13}, {"412", 13}, var11, var12, {"420", var22, 20}, var13, var14, var15, var16, {"425", 3}, var17};
      var4 = new Object[]{"341", 6};
      var5 = new Object[]{"347", 6};
      var6 = new Object[]{"362", 6};
      var23 = VARIABLE_LENGTH;
      var22 = VARIABLE_LENGTH;
      var24 = VARIABLE_LENGTH;
      var25 = VARIABLE_LENGTH;
      Object var26 = VARIABLE_LENGTH;
      THREE_DIGIT_PLUS_DIGIT_DATA_LENGTH = new Object[][]{{"310", 6}, {"311", 6}, {"312", 6}, {"313", 6}, {"314", 6}, {"315", 6}, {"316", 6}, {"320", 6}, {"321", 6}, {"322", 6}, {"323", 6}, {"324", 6}, {"325", 6}, {"326", 6}, {"327", 6}, {"328", 6}, {"329", 6}, {"330", 6}, {"331", 6}, {"332", 6}, {"333", 6}, {"334", 6}, {"335", 6}, {"336", 6}, {"340", 6}, var4, {"342", 6}, {"343", 6}, {"344", 6}, {"345", 6}, {"346", 6}, var5, {"348", 6}, {"349", 6}, {"350", 6}, {"351", 6}, {"352", 6}, {"353", 6}, {"354", 6}, {"355", 6}, {"356", 6}, {"357", 6}, {"360", 6}, {"361", 6}, var6, {"363", 6}, {"364", 6}, {"365", 6}, {"366", 6}, {"367", 6}, {"368", 6}, {"369", 6}, {"390", var23, 15}, {"391", var22, 18}, {"392", var24, 15}, {"393", var25, 18}, {"703", var26, 30}};
      FOUR_DIGIT_DATA_LENGTH = new Object[][]{{"7001", 13}, {"7002", VARIABLE_LENGTH, 30}, {"7003", 10}, {"8001", 14}, {"8002", VARIABLE_LENGTH, 20}, {"8003", VARIABLE_LENGTH, 30}, {"8004", VARIABLE_LENGTH, 30}, {"8005", 6}, {"8006", 18}, {"8007", VARIABLE_LENGTH, 30}, {"8008", VARIABLE_LENGTH, 12}, {"8018", 18}, {"8020", VARIABLE_LENGTH, 25}, {"8100", 6}, {"8101", 10}, {"8102", 2}, {"8110", VARIABLE_LENGTH, 70}, {"8200", VARIABLE_LENGTH, 70}};
   }

   private FieldParser() {
   }

   static String parseFieldsInGeneralPurpose(String var0) throws NotFoundException {
      if (var0.isEmpty()) {
         var0 = null;
      } else {
         if (var0.length() < 2) {
            throw NotFoundException.getNotFoundInstance();
         }

         String var1 = var0.substring(0, 2);
         Object[][] var2 = TWO_DIGIT_DATA_LENGTH;
         int var3 = var2.length;
         int var4 = 0;

         while(true) {
            if (var4 >= var3) {
               if (var0.length() < 3) {
                  throw NotFoundException.getNotFoundInstance();
               }

               var1 = var0.substring(0, 3);
               Object[][] var8 = THREE_DIGIT_DATA_LENGTH;
               var3 = var8.length;

               Object[] var7;
               for(var4 = 0; var4 < var3; ++var4) {
                  var7 = var8[var4];
                  if (var7[0].equals(var1)) {
                     if (var7[1] == VARIABLE_LENGTH) {
                        var0 = processVariableAI(3, (Integer)var7[2], var0);
                     } else {
                        var0 = processFixedAI(3, (Integer)var7[1], var0);
                     }

                     return var0;
                  }
               }

               var8 = THREE_DIGIT_PLUS_DIGIT_DATA_LENGTH;
               var3 = var8.length;

               for(var4 = 0; var4 < var3; ++var4) {
                  var7 = var8[var4];
                  if (var7[0].equals(var1)) {
                     if (var7[1] == VARIABLE_LENGTH) {
                        var0 = processVariableAI(4, (Integer)var7[2], var0);
                     } else {
                        var0 = processFixedAI(4, (Integer)var7[1], var0);
                     }

                     return var0;
                  }
               }

               if (var0.length() < 4) {
                  throw NotFoundException.getNotFoundInstance();
               }

               String var9 = var0.substring(0, 4);
               var2 = FOUR_DIGIT_DATA_LENGTH;
               var3 = var2.length;

               for(var4 = 0; var4 < var3; ++var4) {
                  Object[] var6 = var2[var4];
                  if (var6[0].equals(var9)) {
                     if (var6[1] == VARIABLE_LENGTH) {
                        var0 = processVariableAI(4, (Integer)var6[2], var0);
                     } else {
                        var0 = processFixedAI(4, (Integer)var6[1], var0);
                     }

                     return var0;
                  }
               }

               throw NotFoundException.getNotFoundInstance();
            }

            Object[] var5 = var2[var4];
            if (var5[0].equals(var1)) {
               if (var5[1] == VARIABLE_LENGTH) {
                  var0 = processVariableAI(2, (Integer)var5[2], var0);
               } else {
                  var0 = processFixedAI(2, (Integer)var5[1], var0);
               }
               break;
            }

            ++var4;
         }
      }

      return var0;
   }

   private static String processFixedAI(int var0, int var1, String var2) throws NotFoundException {
      if (var2.length() < var0) {
         throw NotFoundException.getNotFoundInstance();
      } else {
         String var3 = var2.substring(0, var0);
         if (var2.length() < var0 + var1) {
            throw NotFoundException.getNotFoundInstance();
         } else {
            String var4 = var2.substring(var0, var0 + var1);
            String var5 = var2.substring(var0 + var1);
            var2 = "(" + var3 + ')' + var4;
            var4 = parseFieldsInGeneralPurpose(var5);
            if (var4 != null) {
               var2 = var2 + var4;
            }

            return var2;
         }
      }
   }

   private static String processVariableAI(int var0, int var1, String var2) throws NotFoundException {
      String var3 = var2.substring(0, var0);
      if (var2.length() < var0 + var1) {
         var1 = var2.length();
      } else {
         var1 += var0;
      }

      String var4 = var2.substring(var0, var1);
      String var5 = var2.substring(var1);
      var2 = "(" + var3 + ')' + var4;
      var5 = parseFieldsInGeneralPurpose(var5);
      if (var5 != null) {
         var2 = var2 + var5;
      }

      return var2;
   }
}
