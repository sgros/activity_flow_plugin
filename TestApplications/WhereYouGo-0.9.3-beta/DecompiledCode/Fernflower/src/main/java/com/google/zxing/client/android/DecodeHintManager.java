package com.google.zxing.client.android;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import com.google.zxing.DecodeHintType;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public final class DecodeHintManager {
   private static final Pattern COMMA = Pattern.compile(",");
   private static final String TAG = DecodeHintManager.class.getSimpleName();

   private DecodeHintManager() {
   }

   public static Map parseDecodeHints(Intent var0) {
      Bundle var1 = var0.getExtras();
      EnumMap var7;
      if (var1 != null && !var1.isEmpty()) {
         var7 = new EnumMap(DecodeHintType.class);
         DecodeHintType[] var2 = DecodeHintType.values();
         int var3 = var2.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            DecodeHintType var5 = var2[var4];
            if (var5 != DecodeHintType.CHARACTER_SET && var5 != DecodeHintType.NEED_RESULT_POINT_CALLBACK && var5 != DecodeHintType.POSSIBLE_FORMATS) {
               String var6 = var5.name();
               if (var1.containsKey(var6)) {
                  if (var5.getValueType().equals(Void.class)) {
                     var7.put(var5, Boolean.TRUE);
                  } else {
                     Object var8 = var1.get(var6);
                     if (var5.getValueType().isInstance(var8)) {
                        var7.put(var5, var8);
                     } else {
                        Log.w(TAG, "Ignoring hint " + var5 + " because it is not assignable from " + var8);
                     }
                  }
               }
            }
         }

         Log.i(TAG, "Hints from the Intent: " + var7);
      } else {
         var7 = null;
      }

      return var7;
   }

   static Map parseDecodeHints(Uri var0) {
      String var11 = var0.getEncodedQuery();
      EnumMap var12;
      if (var11 != null && !var11.isEmpty()) {
         Map var1 = splitQuery(var11);
         EnumMap var2 = new EnumMap(DecodeHintType.class);
         DecodeHintType[] var3 = DecodeHintType.values();
         int var4 = var3.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            DecodeHintType var6 = var3[var5];
            if (var6 != DecodeHintType.CHARACTER_SET && var6 != DecodeHintType.NEED_RESULT_POINT_CALLBACK && var6 != DecodeHintType.POSSIBLE_FORMATS) {
               String var7 = (String)var1.get(var6.name());
               if (var7 != null) {
                  if (var6.getValueType().equals(Object.class)) {
                     var2.put(var6, var7);
                  } else if (var6.getValueType().equals(Void.class)) {
                     var2.put(var6, Boolean.TRUE);
                  } else if (var6.getValueType().equals(String.class)) {
                     var2.put(var6, var7);
                  } else if (var6.getValueType().equals(Boolean.class)) {
                     if (var7.isEmpty()) {
                        var2.put(var6, Boolean.TRUE);
                     } else if (!"0".equals(var7) && !"false".equalsIgnoreCase(var7) && !"no".equalsIgnoreCase(var7)) {
                        var2.put(var6, Boolean.TRUE);
                     } else {
                        var2.put(var6, Boolean.FALSE);
                     }
                  } else if (!var6.getValueType().equals(int[].class)) {
                     Log.w(TAG, "Unsupported hint type '" + var6 + "' of type " + var6.getValueType());
                  } else {
                     var11 = var7;
                     if (!var7.isEmpty()) {
                        var11 = var7;
                        if (var7.charAt(var7.length() - 1) == ',') {
                           var11 = var7.substring(0, var7.length() - 1);
                        }
                     }

                     String[] var8 = COMMA.split(var11);
                     int[] var14 = new int[var8.length];
                     int var9 = 0;

                     int[] var13;
                     while(true) {
                        var13 = var14;
                        if (var9 >= var8.length) {
                           break;
                        }

                        try {
                           var14[var9] = Integer.parseInt(var8[var9]);
                        } catch (NumberFormatException var10) {
                           Log.w(TAG, "Skipping array of integers hint " + var6 + " due to invalid numeric value: '" + var8[var9] + '\'');
                           var13 = null;
                           break;
                        }

                        ++var9;
                     }

                     if (var13 != null) {
                        var2.put(var6, var13);
                     }
                  }
               }
            }
         }

         Log.i(TAG, "Hints from the URI: " + var2);
         var12 = var2;
      } else {
         var12 = null;
      }

      return var12;
   }

   private static Map splitQuery(String var0) {
      HashMap var1 = new HashMap();
      int var2 = 0;

      while(var2 < var0.length()) {
         if (var0.charAt(var2) == '&') {
            ++var2;
         } else {
            int var3 = var0.indexOf(38, var2);
            int var4 = var0.indexOf(61, var2);
            String var5;
            String var6;
            if (var3 < 0) {
               if (var4 < 0) {
                  var0 = Uri.decode(var0.substring(var2).replace('+', ' '));
                  var5 = "";
               } else {
                  var6 = Uri.decode(var0.substring(var2, var4).replace('+', ' '));
                  var5 = Uri.decode(var0.substring(var4 + 1).replace('+', ' '));
                  var0 = var6;
               }

               if (!var1.containsKey(var0)) {
                  var1.put(var0, var5);
               }
               break;
            }

            if (var4 >= 0 && var4 <= var3) {
               var5 = Uri.decode(var0.substring(var2, var4).replace('+', ' '));
               var6 = Uri.decode(var0.substring(var4 + 1, var3).replace('+', ' '));
               if (!var1.containsKey(var5)) {
                  var1.put(var5, var6);
               }

               var2 = var3 + 1;
            } else {
               var5 = Uri.decode(var0.substring(var2, var3).replace('+', ' '));
               if (!var1.containsKey(var5)) {
                  var1.put(var5, "");
               }

               var2 = var3 + 1;
            }
         }
      }

      return var1;
   }
}
