package com.google.zxing.oned.rss.expanded.decoders;

import com.google.zxing.NotFoundException;

final class FieldParser {
   private static final Object[][] FOUR_DIGIT_DATA_LENGTH;
   private static final Object[][] THREE_DIGIT_DATA_LENGTH;
   private static final Object[][] THREE_DIGIT_PLUS_DIGIT_DATA_LENGTH;
   private static final Object[][] TWO_DIGIT_DATA_LENGTH;
   private static final Object VARIABLE_LENGTH = new Object();

   static {
      Object var0 = VARIABLE_LENGTH;
      Object var1 = VARIABLE_LENGTH;
      Object var2 = VARIABLE_LENGTH;
      Object var3 = VARIABLE_LENGTH;
      Object[] var4 = new Object[]{"37", VARIABLE_LENGTH, 8};
      Object var5 = VARIABLE_LENGTH;
      Object var6 = VARIABLE_LENGTH;
      Object var7 = VARIABLE_LENGTH;
      Object var8 = VARIABLE_LENGTH;
      Object var9 = VARIABLE_LENGTH;
      Object var10 = VARIABLE_LENGTH;
      Object var11 = VARIABLE_LENGTH;
      Object var12 = VARIABLE_LENGTH;
      Object var13 = VARIABLE_LENGTH;
      Object var14 = VARIABLE_LENGTH;
      TWO_DIGIT_DATA_LENGTH = new Object[][]{{"00", 18}, {"01", 14}, {"02", 14}, {"10", var0, 20}, {"11", 6}, {"12", 6}, {"13", 6}, {"15", 6}, {"17", 6}, {"20", 2}, {"21", var1, 20}, {"22", var2, 29}, {"30", var3, 8}, var4, {"90", var5, 30}, {"91", var6, 30}, {"92", var7, 30}, {"93", var8, 30}, {"94", var9, 30}, {"95", var10, 30}, {"96", var11, 30}, {"97", var12, 30}, {"98", var13, 30}, {"99", var14, 30}};
      Object[] var23 = new Object[]{"240", VARIABLE_LENGTH, 30};
      var13 = VARIABLE_LENGTH;
      Object[] var30 = new Object[]{"242", VARIABLE_LENGTH, 6};
      Object[] var31 = new Object[]{"250", VARIABLE_LENGTH, 30};
      Object[] var26 = new Object[]{"251", VARIABLE_LENGTH, 30};
      var5 = VARIABLE_LENGTH;
      Object[] var29 = new Object[]{"254", VARIABLE_LENGTH, 20};
      Object[] var24 = new Object[]{"400", VARIABLE_LENGTH, 30};
      Object[] var22 = new Object[]{"401", VARIABLE_LENGTH, 30};
      Object[] var27 = new Object[]{"402", 17};
      Object[] var21 = new Object[]{"403", VARIABLE_LENGTH, 30};
      Object[] var28 = new Object[]{"410", 13};
      Object[] var25 = new Object[]{"412", 13};
      var4 = new Object[]{"413", 13};
      Object[] var15 = new Object[]{"414", 13};
      Object[] var16 = new Object[]{"420", VARIABLE_LENGTH, 20};
      var11 = VARIABLE_LENGTH;
      Object[] var17 = new Object[]{"422", 3};
      Object[] var18 = new Object[]{"423", VARIABLE_LENGTH, 15};
      Object[] var19 = new Object[]{"424", 3};
      Object[] var20 = new Object[]{"426", 3};
      THREE_DIGIT_DATA_LENGTH = new Object[][]{var23, {"241", var13, 30}, var30, var31, var26, {"253", var5, 17}, var29, var24, var22, var27, var21, var28, {"411", 13}, var25, var4, var15, var16, {"421", var11, 15}, var17, var18, var19, {"425", 3}, var20};
      var31 = new Object[]{"312", 6};
      var26 = new Object[]{"348", 6};
      var29 = new Object[]{"361", 6};
      var13 = VARIABLE_LENGTH;
      var12 = VARIABLE_LENGTH;
      var11 = VARIABLE_LENGTH;
      var2 = VARIABLE_LENGTH;
      var5 = VARIABLE_LENGTH;
      THREE_DIGIT_PLUS_DIGIT_DATA_LENGTH = new Object[][]{{"310", 6}, {"311", 6}, var31, {"313", 6}, {"314", 6}, {"315", 6}, {"316", 6}, {"320", 6}, {"321", 6}, {"322", 6}, {"323", 6}, {"324", 6}, {"325", 6}, {"326", 6}, {"327", 6}, {"328", 6}, {"329", 6}, {"330", 6}, {"331", 6}, {"332", 6}, {"333", 6}, {"334", 6}, {"335", 6}, {"336", 6}, {"340", 6}, {"341", 6}, {"342", 6}, {"343", 6}, {"344", 6}, {"345", 6}, {"346", 6}, {"347", 6}, var26, {"349", 6}, {"350", 6}, {"351", 6}, {"352", 6}, {"353", 6}, {"354", 6}, {"355", 6}, {"356", 6}, {"357", 6}, {"360", 6}, var29, {"362", 6}, {"363", 6}, {"364", 6}, {"365", 6}, {"366", 6}, {"367", 6}, {"368", 6}, {"369", 6}, {"390", var13, 15}, {"391", var12, 18}, {"392", var11, 15}, {"393", var2, 18}, {"703", var5, 30}};
      var7 = VARIABLE_LENGTH;
      var5 = VARIABLE_LENGTH;
      var29 = new Object[]{"8003", VARIABLE_LENGTH, 30};
      var13 = VARIABLE_LENGTH;
      var14 = VARIABLE_LENGTH;
      var2 = VARIABLE_LENGTH;
      var24 = new Object[]{"8018", 18};
      var22 = new Object[]{"8020", VARIABLE_LENGTH, 25};
      var11 = VARIABLE_LENGTH;
      var12 = VARIABLE_LENGTH;
      FOUR_DIGIT_DATA_LENGTH = new Object[][]{{"7001", 13}, {"7002", var7, 30}, {"7003", 10}, {"8001", 14}, {"8002", var5, 20}, var29, {"8004", var13, 30}, {"8005", 6}, {"8006", 18}, {"8007", var14, 30}, {"8008", var2, 12}, var24, var22, {"8100", 6}, {"8101", 10}, {"8102", 2}, {"8110", var11, 70}, {"8200", var12, 70}};
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
            Object[] var5;
            if (var4 >= var3) {
               if (var0.length() < 3) {
                  throw NotFoundException.getNotFoundInstance();
               }

               String var7 = var0.substring(0, 3);
               Object[][] var6 = THREE_DIGIT_DATA_LENGTH;
               var3 = var6.length;

               for(var4 = 0; var4 < var3; ++var4) {
                  var5 = var6[var4];
                  if (var5[0].equals(var7)) {
                     if (var5[1] == VARIABLE_LENGTH) {
                        var0 = processVariableAI(3, (Integer)var5[2], var0);
                     } else {
                        var0 = processFixedAI(3, (Integer)var5[1], var0);
                     }

                     return var0;
                  }
               }

               var6 = THREE_DIGIT_PLUS_DIGIT_DATA_LENGTH;
               var3 = var6.length;

               for(var4 = 0; var4 < var3; ++var4) {
                  var5 = var6[var4];
                  if (var5[0].equals(var7)) {
                     if (var5[1] == VARIABLE_LENGTH) {
                        var0 = processVariableAI(4, (Integer)var5[2], var0);
                     } else {
                        var0 = processFixedAI(4, (Integer)var5[1], var0);
                     }

                     return var0;
                  }
               }

               if (var0.length() < 4) {
                  throw NotFoundException.getNotFoundInstance();
               }

               var1 = var0.substring(0, 4);
               Object[][] var9 = FOUR_DIGIT_DATA_LENGTH;
               var3 = var9.length;

               for(var4 = 0; var4 < var3; ++var4) {
                  Object[] var8 = var9[var4];
                  if (var8[0].equals(var1)) {
                     if (var8[1] == VARIABLE_LENGTH) {
                        var0 = processVariableAI(4, (Integer)var8[2], var0);
                     } else {
                        var0 = processFixedAI(4, (Integer)var8[1], var0);
                     }

                     return var0;
                  }
               }

               throw NotFoundException.getNotFoundInstance();
            }

            var5 = var2[var4];
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
            var3 = parseFieldsInGeneralPurpose(var5);
            if (var3 != null) {
               var2 = var2 + var3;
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
      var4 = parseFieldsInGeneralPurpose(var5);
      if (var4 != null) {
         var2 = var2 + var4;
      }

      return var2;
   }
}
