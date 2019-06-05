package com.airbnb.lottie.parser;

import android.graphics.Path.FillType;
import android.util.JsonReader;
import com.airbnb.lottie.LottieComposition;
import com.airbnb.lottie.model.animatable.AnimatableFloatValue;
import com.airbnb.lottie.model.animatable.AnimatableGradientColorValue;
import com.airbnb.lottie.model.animatable.AnimatableIntegerValue;
import com.airbnb.lottie.model.animatable.AnimatablePointValue;
import com.airbnb.lottie.model.content.GradientFill;
import com.airbnb.lottie.model.content.GradientType;
import java.io.IOException;

class GradientFillParser {
   static GradientFill parse(JsonReader var0, LottieComposition var1) throws IOException {
      Object var2 = null;
      AnimatableGradientColorValue var5 = (AnimatableGradientColorValue)var2;
      AnimatableIntegerValue var6 = (AnimatableIntegerValue)var2;
      AnimatablePointValue var7 = (AnimatablePointValue)var2;
      AnimatablePointValue var8 = (AnimatablePointValue)var2;
      FillType var9 = (FillType)var2;
      GradientType var10 = (GradientType)var2;
      String var4 = (String)var2;

      while(true) {
         while(var0.hasNext()) {
            String var3;
            int var11;
            byte var15;
            label78: {
               var3 = var0.nextName();
               var11 = var3.hashCode();
               if (var11 != 101) {
                  if (var11 != 103) {
                     if (var11 != 111) {
                        if (var11 != 3519) {
                           switch(var11) {
                           case 114:
                              if (var3.equals("r")) {
                                 var15 = 6;
                                 break label78;
                              }
                              break;
                           case 115:
                              if (var3.equals("s")) {
                                 var15 = 4;
                                 break label78;
                              }
                              break;
                           case 116:
                              if (var3.equals("t")) {
                                 var15 = 3;
                                 break label78;
                              }
                           }
                        } else if (var3.equals("nm")) {
                           var15 = 0;
                           break label78;
                        }
                     } else if (var3.equals("o")) {
                        var15 = 2;
                        break label78;
                     }
                  } else if (var3.equals("g")) {
                     var15 = 1;
                     break label78;
                  }
               } else if (var3.equals("e")) {
                  var15 = 5;
                  break label78;
               }

               var15 = -1;
            }

            switch(var15) {
            case 0:
               var4 = var0.nextString();
               break;
            case 1:
               var0.beginObject();
               int var12 = -1;

               while(var0.hasNext()) {
                  label91: {
                     var3 = var0.nextName();
                     var11 = var3.hashCode();
                     if (var11 != 107) {
                        if (var11 == 112 && var3.equals("p")) {
                           var15 = 0;
                           break label91;
                        }
                     } else if (var3.equals("k")) {
                        var15 = 1;
                        break label91;
                     }

                     var15 = -1;
                  }

                  switch(var15) {
                  case 0:
                     var12 = var0.nextInt();
                     break;
                  case 1:
                     var5 = AnimatableValueParser.parseGradientColor(var0, var1, var12);
                     break;
                  default:
                     var0.skipValue();
                  }
               }

               var0.endObject();
               break;
            case 2:
               var6 = AnimatableValueParser.parseInteger(var0, var1);
               break;
            case 3:
               GradientType var14;
               if (var0.nextInt() == 1) {
                  var14 = GradientType.Linear;
               } else {
                  var14 = GradientType.Radial;
               }

               var10 = var14;
               break;
            case 4:
               var7 = AnimatableValueParser.parsePoint(var0, var1);
               break;
            case 5:
               var8 = AnimatableValueParser.parsePoint(var0, var1);
               break;
            case 6:
               FillType var13;
               if (var0.nextInt() == 1) {
                  var13 = FillType.WINDING;
               } else {
                  var13 = FillType.EVEN_ODD;
               }

               var9 = var13;
               break;
            default:
               var0.skipValue();
            }
         }

         return new GradientFill(var4, var10, var9, var5, var6, var7, var8, (AnimatableFloatValue)null, (AnimatableFloatValue)null);
      }
   }
}
