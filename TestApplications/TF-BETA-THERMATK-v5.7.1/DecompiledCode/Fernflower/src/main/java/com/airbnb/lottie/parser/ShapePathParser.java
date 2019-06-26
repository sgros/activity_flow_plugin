package com.airbnb.lottie.parser;

import android.util.JsonReader;
import com.airbnb.lottie.LottieComposition;
import com.airbnb.lottie.model.animatable.AnimatableShapeValue;
import com.airbnb.lottie.model.content.ShapePath;
import java.io.IOException;

class ShapePathParser {
   static ShapePath parse(JsonReader var0, LottieComposition var1) throws IOException {
      String var2 = null;
      AnimatableShapeValue var3 = null;
      int var4 = 0;
      boolean var5 = false;

      while(var0.hasNext()) {
         String var6 = var0.nextName();
         byte var7 = -1;
         int var8 = var6.hashCode();
         if (var8 != 3324) {
            if (var8 != 3432) {
               if (var8 != 3519) {
                  if (var8 == 104415 && var6.equals("ind")) {
                     var7 = 1;
                  }
               } else if (var6.equals("nm")) {
                  var7 = 0;
               }
            } else if (var6.equals("ks")) {
               var7 = 2;
            }
         } else if (var6.equals("hd")) {
            var7 = 3;
         }

         if (var7 != 0) {
            if (var7 != 1) {
               if (var7 != 2) {
                  if (var7 != 3) {
                     var0.skipValue();
                  } else {
                     var5 = var0.nextBoolean();
                  }
               } else {
                  var3 = AnimatableValueParser.parseShapeData(var0, var1);
               }
            } else {
               var4 = var0.nextInt();
            }
         } else {
            var2 = var0.nextString();
         }
      }

      return new ShapePath(var2, var4, var3, var5);
   }
}
