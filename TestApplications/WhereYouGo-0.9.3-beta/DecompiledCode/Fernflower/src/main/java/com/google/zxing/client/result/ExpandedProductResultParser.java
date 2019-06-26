package com.google.zxing.client.result;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import java.util.HashMap;

public final class ExpandedProductResultParser extends ResultParser {
   private static String findAIvalue(int var0, String var1) {
      Object var2 = null;
      if (var1.charAt(var0) != '(') {
         var1 = (String)var2;
      } else {
         String var3 = var1.substring(var0 + 1);
         StringBuilder var4 = new StringBuilder();
         var0 = 0;

         while(true) {
            if (var0 >= var3.length()) {
               var1 = var4.toString();
               break;
            }

            char var5 = var3.charAt(var0);
            if (var5 == ')') {
               var1 = var4.toString();
               break;
            }

            var1 = (String)var2;
            if (var5 < '0') {
               break;
            }

            var1 = (String)var2;
            if (var5 > '9') {
               break;
            }

            var4.append(var5);
            ++var0;
         }
      }

      return var1;
   }

   private static String findValue(int var0, String var1) {
      StringBuilder var2 = new StringBuilder();
      var1 = var1.substring(var0);

      for(var0 = 0; var0 < var1.length(); ++var0) {
         char var3 = var1.charAt(var0);
         if (var3 == '(') {
            if (findAIvalue(var0, var1) != null) {
               break;
            }

            var2.append('(');
         } else {
            var2.append(var3);
         }
      }

      return var2.toString();
   }

   public ExpandedProductParsedResult parse(Result var1) {
      ExpandedProductParsedResult var20;
      if (var1.getBarcodeFormat() != BarcodeFormat.RSS_EXPANDED) {
         var20 = null;
      } else {
         String var2 = getMassagedText(var1);
         String var3 = null;
         String var4 = null;
         String var5 = null;
         String var6 = null;
         String var7 = null;
         String var8 = null;
         String var9 = null;
         String var10 = null;
         String var11 = null;
         String var12 = null;
         String var13 = null;
         String var14 = null;
         String var15 = null;
         HashMap var16 = new HashMap();
         int var17 = 0;

         while(true) {
            if (var17 >= var2.length()) {
               var20 = new ExpandedProductParsedResult(var2, var3, var4, var5, var6, var7, var8, var9, var10, var11, var12, var13, var14, var15, var16);
               break;
            }

            String var18 = findAIvalue(var17, var2);
            if (var18 == null) {
               var20 = null;
               break;
            }

            var17 += var18.length() + 2;
            String var21 = findValue(var17, var2);
            int var19 = var17 + var21.length();
            byte var22 = -1;
            switch(var18.hashCode()) {
            case 1536:
               if (var18.equals("00")) {
                  var22 = 0;
               }
               break;
            case 1537:
               if (var18.equals("01")) {
                  var22 = 1;
               }
               break;
            case 1567:
               if (var18.equals("10")) {
                  var22 = 2;
               }
               break;
            case 1568:
               if (var18.equals("11")) {
                  var22 = 3;
               }
               break;
            case 1570:
               if (var18.equals("13")) {
                  var22 = 4;
               }
               break;
            case 1572:
               if (var18.equals("15")) {
                  var22 = 5;
               }
               break;
            case 1574:
               if (var18.equals("17")) {
                  var22 = 6;
               }
               break;
            case 1567966:
               if (var18.equals("3100")) {
                  var22 = 7;
               }
               break;
            case 1567967:
               if (var18.equals("3101")) {
                  var22 = 8;
               }
               break;
            case 1567968:
               if (var18.equals("3102")) {
                  var22 = 9;
               }
               break;
            case 1567969:
               if (var18.equals("3103")) {
                  var22 = 10;
               }
               break;
            case 1567970:
               if (var18.equals("3104")) {
                  var22 = 11;
               }
               break;
            case 1567971:
               if (var18.equals("3105")) {
                  var22 = 12;
               }
               break;
            case 1567972:
               if (var18.equals("3106")) {
                  var22 = 13;
               }
               break;
            case 1567973:
               if (var18.equals("3107")) {
                  var22 = 14;
               }
               break;
            case 1567974:
               if (var18.equals("3108")) {
                  var22 = 15;
               }
               break;
            case 1567975:
               if (var18.equals("3109")) {
                  var22 = 16;
               }
               break;
            case 1568927:
               if (var18.equals("3200")) {
                  var22 = 17;
               }
               break;
            case 1568928:
               if (var18.equals("3201")) {
                  var22 = 18;
               }
               break;
            case 1568929:
               if (var18.equals("3202")) {
                  var22 = 19;
               }
               break;
            case 1568930:
               if (var18.equals("3203")) {
                  var22 = 20;
               }
               break;
            case 1568931:
               if (var18.equals("3204")) {
                  var22 = 21;
               }
               break;
            case 1568932:
               if (var18.equals("3205")) {
                  var22 = 22;
               }
               break;
            case 1568933:
               if (var18.equals("3206")) {
                  var22 = 23;
               }
               break;
            case 1568934:
               if (var18.equals("3207")) {
                  var22 = 24;
               }
               break;
            case 1568935:
               if (var18.equals("3208")) {
                  var22 = 25;
               }
               break;
            case 1568936:
               if (var18.equals("3209")) {
                  var22 = 26;
               }
               break;
            case 1575716:
               if (var18.equals("3920")) {
                  var22 = 27;
               }
               break;
            case 1575717:
               if (var18.equals("3921")) {
                  var22 = 28;
               }
               break;
            case 1575718:
               if (var18.equals("3922")) {
                  var22 = 29;
               }
               break;
            case 1575719:
               if (var18.equals("3923")) {
                  var22 = 30;
               }
               break;
            case 1575747:
               if (var18.equals("3930")) {
                  var22 = 31;
               }
               break;
            case 1575748:
               if (var18.equals("3931")) {
                  var22 = 32;
               }
               break;
            case 1575749:
               if (var18.equals("3932")) {
                  var22 = 33;
               }
               break;
            case 1575750:
               if (var18.equals("3933")) {
                  var22 = 34;
               }
            }

            switch(var22) {
            case 0:
               var4 = var21;
               var17 = var19;
               break;
            case 1:
               var3 = var21;
               var17 = var19;
               break;
            case 2:
               var5 = var21;
               var17 = var19;
               break;
            case 3:
               var6 = var21;
               var17 = var19;
               break;
            case 4:
               var7 = var21;
               var17 = var19;
               break;
            case 5:
               var8 = var21;
               var17 = var19;
               break;
            case 6:
               var9 = var21;
               var17 = var19;
               break;
            case 7:
            case 8:
            case 9:
            case 10:
            case 11:
            case 12:
            case 13:
            case 14:
            case 15:
            case 16:
               var11 = "KG";
               var12 = var18.substring(3);
               var10 = var21;
               var17 = var19;
               break;
            case 17:
            case 18:
            case 19:
            case 20:
            case 21:
            case 22:
            case 23:
            case 24:
            case 25:
            case 26:
               var11 = "LB";
               var12 = var18.substring(3);
               var10 = var21;
               var17 = var19;
               break;
            case 27:
            case 28:
            case 29:
            case 30:
               var14 = var18.substring(3);
               var13 = var21;
               var17 = var19;
               break;
            case 31:
            case 32:
            case 33:
            case 34:
               if (var21.length() < 4) {
                  var20 = null;
                  return var20;
               }

               var13 = var21.substring(3);
               var15 = var21.substring(0, 3);
               var14 = var18.substring(3);
               var17 = var19;
               break;
            default:
               var16.put(var18, var21);
               var17 = var19;
            }
         }
      }

      return var20;
   }
}
