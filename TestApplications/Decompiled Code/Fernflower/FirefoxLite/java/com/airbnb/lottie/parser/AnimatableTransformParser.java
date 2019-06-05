package com.airbnb.lottie.parser;

import android.util.JsonReader;
import android.util.JsonToken;
import android.util.Log;
import com.airbnb.lottie.LottieComposition;
import com.airbnb.lottie.model.animatable.AnimatableFloatValue;
import com.airbnb.lottie.model.animatable.AnimatableIntegerValue;
import com.airbnb.lottie.model.animatable.AnimatablePathValue;
import com.airbnb.lottie.model.animatable.AnimatableScaleValue;
import com.airbnb.lottie.model.animatable.AnimatableTransform;
import com.airbnb.lottie.model.animatable.AnimatableValue;
import com.airbnb.lottie.value.ScaleXY;
import java.io.IOException;

public class AnimatableTransformParser {
   public static AnimatableTransform parse(JsonReader var0, LottieComposition var1) throws IOException {
      boolean var2;
      if (var0.peek() == JsonToken.BEGIN_OBJECT) {
         var2 = true;
      } else {
         var2 = false;
      }

      if (var2) {
         var0.beginObject();
      }

      Object var3 = null;
      String var4 = null;
      AnimatableIntegerValue var5 = var4;
      AnimatableFloatValue var8 = var4;
      AnimatableFloatValue var9 = var4;
      AnimatableFloatValue var10 = var4;
      AnimatableValue var11 = var4;
      AnimatableScaleValue var6 = var4;
      AnimatablePathValue var7 = (AnimatablePathValue)var3;

      while(true) {
         while(var0.hasNext()) {
            byte var12;
            label80: {
               var4 = var0.nextName();
               switch(var4.hashCode()) {
               case 97:
                  if (var4.equals("a")) {
                     var12 = 0;
                     break label80;
                  }
                  break;
               case 111:
                  if (var4.equals("o")) {
                     var12 = 5;
                     break label80;
                  }
                  break;
               case 112:
                  if (var4.equals("p")) {
                     var12 = 1;
                     break label80;
                  }
                  break;
               case 114:
                  if (var4.equals("r")) {
                     var12 = 4;
                     break label80;
                  }
                  break;
               case 115:
                  if (var4.equals("s")) {
                     var12 = 2;
                     break label80;
                  }
                  break;
               case 3242:
                  if (var4.equals("eo")) {
                     var12 = 7;
                     break label80;
                  }
                  break;
               case 3656:
                  if (var4.equals("rz")) {
                     var12 = 3;
                     break label80;
                  }
                  break;
               case 3676:
                  if (var4.equals("so")) {
                     var12 = 6;
                     break label80;
                  }
               }

               var12 = -1;
            }

            switch(var12) {
            case 0:
               var0.beginObject();

               while(var0.hasNext()) {
                  if (var0.nextName().equals("k")) {
                     var7 = AnimatablePathValueParser.parse(var0, var1);
                  } else {
                     var0.skipValue();
                  }
               }

               var0.endObject();
               break;
            case 1:
               var11 = AnimatablePathValueParser.parseSplitPath(var0, var1);
               break;
            case 2:
               var6 = AnimatableValueParser.parseScale(var0, var1);
               break;
            case 3:
               var1.addWarning("Lottie doesn't support 3D layers.");
            case 4:
               var10 = AnimatableValueParser.parseFloat(var0, var1, false);
               break;
            case 5:
               var5 = AnimatableValueParser.parseInteger(var0, var1);
               break;
            case 6:
               var8 = AnimatableValueParser.parseFloat(var0, var1, false);
               break;
            case 7:
               var9 = AnimatableValueParser.parseFloat(var0, var1, false);
               break;
            default:
               var0.skipValue();
            }
         }

         if (var2) {
            var0.endObject();
         }

         AnimatablePathValue var13 = var7;
         if (var7 == null) {
            Log.w("LOTTIE", "Layer has no transform property. You may be using an unsupported layer type such as a camera.");
            var13 = new AnimatablePathValue();
         }

         AnimatableScaleValue var14 = var6;
         if (var6 == null) {
            var14 = new AnimatableScaleValue(new ScaleXY(1.0F, 1.0F));
         }

         AnimatableIntegerValue var15 = var5;
         if (var5 == null) {
            var15 = new AnimatableIntegerValue();
         }

         return new AnimatableTransform(var13, var11, var14, var10, var15, var8, var9);
      }
   }
}
