package com.airbnb.lottie.parser;

import android.util.JsonReader;
import com.airbnb.lottie.LottieComposition;
import com.airbnb.lottie.model.animatable.AnimatableColorValue;
import com.airbnb.lottie.model.animatable.AnimatableFloatValue;
import com.airbnb.lottie.model.animatable.AnimatableTextProperties;
import java.io.IOException;

public class AnimatableTextPropertiesParser {
   public static AnimatableTextProperties parse(JsonReader var0, LottieComposition var1) throws IOException {
      var0.beginObject();
      AnimatableTextProperties var2 = null;

      while(var0.hasNext()) {
         String var3 = var0.nextName();
         byte var4 = -1;
         if (var3.hashCode() == 97 && var3.equals("a")) {
            var4 = 0;
         }

         if (var4 != 0) {
            var0.skipValue();
         } else {
            var2 = parseAnimatableTextProperties(var0, var1);
         }
      }

      var0.endObject();
      if (var2 == null) {
         return new AnimatableTextProperties((AnimatableColorValue)null, (AnimatableColorValue)null, (AnimatableFloatValue)null, (AnimatableFloatValue)null);
      } else {
         return var2;
      }
   }

   private static AnimatableTextProperties parseAnimatableTextProperties(JsonReader var0, LottieComposition var1) throws IOException {
      var0.beginObject();
      AnimatableColorValue var2 = null;
      AnimatableColorValue var3 = null;
      Object var4 = var3;
      Object var5 = var3;

      while(var0.hasNext()) {
         String var6 = var0.nextName();
         byte var7 = -1;
         int var8 = var6.hashCode();
         if (var8 != 116) {
            if (var8 != 3261) {
               if (var8 != 3664) {
                  if (var8 == 3684 && var6.equals("sw")) {
                     var7 = 2;
                  }
               } else if (var6.equals("sc")) {
                  var7 = 1;
               }
            } else if (var6.equals("fc")) {
               var7 = 0;
            }
         } else if (var6.equals("t")) {
            var7 = 3;
         }

         if (var7 != 0) {
            if (var7 != 1) {
               if (var7 != 2) {
                  if (var7 != 3) {
                     var0.skipValue();
                  } else {
                     var5 = AnimatableValueParser.parseFloat(var0, var1);
                  }
               } else {
                  var4 = AnimatableValueParser.parseFloat(var0, var1);
               }
            } else {
               var3 = AnimatableValueParser.parseColor(var0, var1);
            }
         } else {
            var2 = AnimatableValueParser.parseColor(var0, var1);
         }
      }

      var0.endObject();
      return new AnimatableTextProperties(var2, var3, (AnimatableFloatValue)var4, (AnimatableFloatValue)var5);
   }
}
