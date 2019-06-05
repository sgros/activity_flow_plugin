package com.airbnb.lottie.parser;

import android.util.JsonReader;
import com.airbnb.lottie.LottieComposition;
import com.airbnb.lottie.model.animatable.AnimatableFloatValue;
import com.airbnb.lottie.model.animatable.AnimatableGradientColorValue;
import com.airbnb.lottie.model.animatable.AnimatableIntegerValue;
import com.airbnb.lottie.model.animatable.AnimatablePointValue;
import com.airbnb.lottie.model.content.GradientStroke;
import com.airbnb.lottie.model.content.GradientType;
import com.airbnb.lottie.model.content.ShapeStroke;
import java.io.IOException;
import java.util.ArrayList;

class GradientStrokeParser {
   static GradientStroke parse(JsonReader var0, LottieComposition var1) throws IOException {
      ArrayList var2 = new ArrayList();
      String var3 = null;
      GradientType var4 = null;
      AnimatableGradientColorValue var5 = null;
      AnimatableIntegerValue var6 = null;
      AnimatablePointValue var7 = null;
      AnimatablePointValue var8 = null;
      AnimatableFloatValue var9 = null;
      ShapeStroke.LineCapType var10 = null;
      ShapeStroke.LineJoinType var11 = null;
      float var12 = 0.0F;
      AnimatableFloatValue var13 = null;

      while(true) {
         label128:
         while(var0.hasNext()) {
            String var14;
            byte var15;
            label110: {
               var14 = var0.nextName();
               switch(var14.hashCode()) {
               case 100:
                  if (var14.equals("d")) {
                     var15 = 10;
                     break label110;
                  }
                  break;
               case 101:
                  if (var14.equals("e")) {
                     var15 = 5;
                     break label110;
                  }
                  break;
               case 103:
                  if (var14.equals("g")) {
                     var15 = 1;
                     break label110;
                  }
                  break;
               case 111:
                  if (var14.equals("o")) {
                     var15 = 2;
                     break label110;
                  }
                  break;
               case 115:
                  if (var14.equals("s")) {
                     var15 = 4;
                     break label110;
                  }
                  break;
               case 116:
                  if (var14.equals("t")) {
                     var15 = 3;
                     break label110;
                  }
                  break;
               case 119:
                  if (var14.equals("w")) {
                     var15 = 6;
                     break label110;
                  }
                  break;
               case 3447:
                  if (var14.equals("lc")) {
                     var15 = 7;
                     break label110;
                  }
                  break;
               case 3454:
                  if (var14.equals("lj")) {
                     var15 = 8;
                     break label110;
                  }
                  break;
               case 3487:
                  if (var14.equals("ml")) {
                     var15 = 9;
                     break label110;
                  }
                  break;
               case 3519:
                  if (var14.equals("nm")) {
                     var15 = 0;
                     break label110;
                  }
               }

               var15 = -1;
            }

            int var21;
            switch(var15) {
            case 0:
               var3 = var0.nextString();
               break;
            case 1:
               var0.beginObject();
               int var18 = -1;

               while(var0.hasNext()) {
                  label123: {
                     var14 = var0.nextName();
                     var21 = var14.hashCode();
                     if (var21 != 107) {
                        if (var21 == 112 && var14.equals("p")) {
                           var15 = 0;
                           break label123;
                        }
                     } else if (var14.equals("k")) {
                        var15 = 1;
                        break label123;
                     }

                     var15 = -1;
                  }

                  switch(var15) {
                  case 0:
                     var18 = var0.nextInt();
                     break;
                  case 1:
                     var5 = AnimatableValueParser.parseGradientColor(var0, var1, var18);
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
               GradientType var20;
               if (var0.nextInt() == 1) {
                  var20 = GradientType.Linear;
               } else {
                  var20 = GradientType.Radial;
               }

               var4 = var20;
               break;
            case 4:
               var7 = AnimatableValueParser.parsePoint(var0, var1);
               break;
            case 5:
               var8 = AnimatableValueParser.parsePoint(var0, var1);
               break;
            case 6:
               var9 = AnimatableValueParser.parseFloat(var0, var1);
               break;
            case 7:
               var10 = ShapeStroke.LineCapType.values()[var0.nextInt() - 1];
               break;
            case 8:
               var11 = ShapeStroke.LineJoinType.values()[var0.nextInt() - 1];
               break;
            case 9:
               var12 = (float)var0.nextDouble();
               break;
            case 10:
               var0.beginArray();

               while(true) {
                  while(var0.hasNext()) {
                     var0.beginObject();
                     String var16 = null;
                     AnimatableFloatValue var19 = null;

                     while(var0.hasNext()) {
                        label140: {
                           String var17 = var0.nextName();
                           var21 = var17.hashCode();
                           if (var21 != 110) {
                              if (var21 == 118 && var17.equals("v")) {
                                 var15 = 1;
                                 break label140;
                              }
                           } else if (var17.equals("n")) {
                              var15 = 0;
                              break label140;
                           }

                           var15 = -1;
                        }

                        switch(var15) {
                        case 0:
                           var16 = var0.nextString();
                           break;
                        case 1:
                           var19 = AnimatableValueParser.parseFloat(var0, var1);
                           break;
                        default:
                           var0.skipValue();
                        }
                     }

                     var0.endObject();
                     if (var16.equals("o")) {
                        var13 = var19;
                     } else if (var16.equals("d") || var16.equals("g")) {
                        var2.add(var19);
                     }
                  }

                  var0.endArray();
                  if (var2.size() == 1) {
                     var2.add(var2.get(0));
                  }
                  continue label128;
               }
            default:
               var0.skipValue();
            }
         }

         return new GradientStroke(var3, var4, var5, var6, var7, var8, var9, var10, var11, var12, var2, var13);
      }
   }
}
