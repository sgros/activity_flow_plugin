package com.airbnb.lottie.parser;

import android.graphics.Path.FillType;
import android.util.JsonReader;
import com.airbnb.lottie.LottieComposition;
import com.airbnb.lottie.model.animatable.AnimatableColorValue;
import com.airbnb.lottie.model.animatable.AnimatableIntegerValue;
import com.airbnb.lottie.model.content.ShapeFill;
import java.io.IOException;

class ShapeFillParser {
   static ShapeFill parse(JsonReader var0, LottieComposition var1) throws IOException {
      String var2 = null;
      Object var3 = var2;
      Object var4 = var2;
      int var5 = 1;
      boolean var6 = false;
      boolean var7 = false;

      while(var0.hasNext()) {
         String var8 = var0.nextName();
         byte var9 = -1;
         int var10 = var8.hashCode();
         if (var10 != -396065730) {
            if (var10 != 99) {
               if (var10 != 111) {
                  if (var10 != 114) {
                     if (var10 != 3324) {
                        if (var10 == 3519 && var8.equals("nm")) {
                           var9 = 0;
                        }
                     } else if (var8.equals("hd")) {
                        var9 = 5;
                     }
                  } else if (var8.equals("r")) {
                     var9 = 4;
                  }
               } else if (var8.equals("o")) {
                  var9 = 2;
               }
            } else if (var8.equals("c")) {
               var9 = 1;
            }
         } else if (var8.equals("fillEnabled")) {
            var9 = 3;
         }

         if (var9 != 0) {
            if (var9 != 1) {
               if (var9 != 2) {
                  if (var9 != 3) {
                     if (var9 != 4) {
                        if (var9 != 5) {
                           var0.skipValue();
                        } else {
                           var7 = var0.nextBoolean();
                        }
                     } else {
                        var5 = var0.nextInt();
                     }
                  } else {
                     var6 = var0.nextBoolean();
                  }
               } else {
                  var4 = AnimatableValueParser.parseInteger(var0, var1);
               }
            } else {
               var3 = AnimatableValueParser.parseColor(var0, var1);
            }
         } else {
            var2 = var0.nextString();
         }
      }

      FillType var11;
      if (var5 == 1) {
         var11 = FillType.WINDING;
      } else {
         var11 = FillType.EVEN_ODD;
      }

      return new ShapeFill(var2, var6, var11, (AnimatableColorValue)var3, (AnimatableIntegerValue)var4, var7);
   }
}
