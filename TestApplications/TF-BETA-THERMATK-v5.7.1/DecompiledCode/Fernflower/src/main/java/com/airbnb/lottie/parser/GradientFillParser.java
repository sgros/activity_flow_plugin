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
      FillType var2 = FillType.WINDING;
      String var3 = null;
      Object var5 = var3;
      Object var6 = var3;
      Object var7 = var3;
      Object var8 = var3;
      boolean var9 = false;
      Object var10 = var3;

      while(true) {
         while(var0.hasNext()) {
            String var4;
            int var11;
            byte var15;
            label86: {
               var4 = var0.nextName();
               var11 = var4.hashCode();
               if (var11 != 101) {
                  if (var11 != 103) {
                     if (var11 != 111) {
                        if (var11 != 3324) {
                           if (var11 != 3519) {
                              switch(var11) {
                              case 114:
                                 if (var4.equals("r")) {
                                    var15 = 6;
                                    break label86;
                                 }
                                 break;
                              case 115:
                                 if (var4.equals("s")) {
                                    var15 = 4;
                                    break label86;
                                 }
                                 break;
                              case 116:
                                 if (var4.equals("t")) {
                                    var15 = 3;
                                    break label86;
                                 }
                              }
                           } else if (var4.equals("nm")) {
                              var15 = 0;
                              break label86;
                           }
                        } else if (var4.equals("hd")) {
                           var15 = 7;
                           break label86;
                        }
                     } else if (var4.equals("o")) {
                        var15 = 2;
                        break label86;
                     }
                  } else if (var4.equals("g")) {
                     var15 = 1;
                     break label86;
                  }
               } else if (var4.equals("e")) {
                  var15 = 5;
                  break label86;
               }

               var15 = -1;
            }

            switch(var15) {
            case 0:
               var3 = var0.nextString();
               break;
            case 1:
               var0.beginObject();
               int var12 = -1;

               while(var0.hasNext()) {
                  label99: {
                     var4 = var0.nextName();
                     var11 = var4.hashCode();
                     if (var11 != 107) {
                        if (var11 == 112 && var4.equals("p")) {
                           var15 = 0;
                           break label99;
                        }
                     } else if (var4.equals("k")) {
                        var15 = 1;
                        break label99;
                     }

                     var15 = -1;
                  }

                  if (var15 != 0) {
                     if (var15 != 1) {
                        var0.skipValue();
                     } else {
                        var5 = AnimatableValueParser.parseGradientColor(var0, var1, var12);
                     }
                  } else {
                     var12 = var0.nextInt();
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
                  var14 = GradientType.LINEAR;
               } else {
                  var14 = GradientType.RADIAL;
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

               var2 = var13;
               break;
            case 7:
               var9 = var0.nextBoolean();
               break;
            default:
               var0.skipValue();
            }
         }

         return new GradientFill(var3, (GradientType)var10, var2, (AnimatableGradientColorValue)var5, (AnimatableIntegerValue)var6, (AnimatablePointValue)var7, (AnimatablePointValue)var8, (AnimatableFloatValue)null, (AnimatableFloatValue)null, var9);
      }
   }
}
