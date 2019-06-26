package com.airbnb.lottie.parser;

import android.graphics.Color;
import android.util.JsonReader;
import android.util.JsonToken;
import java.io.IOException;

public class ColorParser implements ValueParser {
   public static final ColorParser INSTANCE = new ColorParser();

   private ColorParser() {
   }

   public Integer parse(JsonReader var1, float var2) throws IOException {
      boolean var3;
      if (var1.peek() == JsonToken.BEGIN_ARRAY) {
         var3 = true;
      } else {
         var3 = false;
      }

      if (var3) {
         var1.beginArray();
      }

      double var4 = var1.nextDouble();
      double var6 = var1.nextDouble();
      double var8 = var1.nextDouble();
      double var10 = var1.nextDouble();
      if (var3) {
         var1.endArray();
      }

      double var12 = var4;
      double var14 = var6;
      double var16 = var8;
      double var18 = var10;
      if (var4 <= 1.0D) {
         var12 = var4;
         var14 = var6;
         var16 = var8;
         var18 = var10;
         if (var6 <= 1.0D) {
            var12 = var4;
            var14 = var6;
            var16 = var8;
            var18 = var10;
            if (var8 <= 1.0D) {
               var12 = var4;
               var14 = var6;
               var16 = var8;
               var18 = var10;
               if (var10 <= 1.0D) {
                  var12 = var4 * 255.0D;
                  var14 = var6 * 255.0D;
                  var16 = var8 * 255.0D;
                  var18 = var10 * 255.0D;
               }
            }
         }
      }

      return Color.argb((int)var18, (int)var12, (int)var14, (int)var16);
   }
}
