package com.airbnb.lottie.parser;

import android.util.JsonReader;
import com.airbnb.lottie.LottieComposition;
import com.airbnb.lottie.model.animatable.AnimatableColorValue;
import com.airbnb.lottie.model.animatable.AnimatableFloatValue;
import com.airbnb.lottie.model.animatable.AnimatableIntegerValue;
import com.airbnb.lottie.model.content.ShapeStroke;
import java.io.IOException;
import java.util.ArrayList;

class ShapeStrokeParser {
   static ShapeStroke parse(JsonReader var0, LottieComposition var1) throws IOException {
      ArrayList var2 = new ArrayList();
      String var3 = null;
      AnimatableFloatValue var4 = null;
      AnimatableColorValue var5 = null;
      AnimatableIntegerValue var6 = null;
      AnimatableFloatValue var7 = null;
      ShapeStroke.LineCapType var8 = null;
      ShapeStroke.LineJoinType var9 = null;
      float var10 = 0.0F;

      while(true) {
         while(var0.hasNext()) {
            int var12;
            byte var16;
            label98: {
               String var11 = var0.nextName();
               var12 = var11.hashCode();
               if (var12 != 111) {
                  if (var12 != 119) {
                     if (var12 != 3447) {
                        if (var12 != 3454) {
                           if (var12 != 3487) {
                              if (var12 != 3519) {
                                 switch(var12) {
                                 case 99:
                                    if (var11.equals("c")) {
                                       var16 = 1;
                                       break label98;
                                    }
                                    break;
                                 case 100:
                                    if (var11.equals("d")) {
                                       var16 = 7;
                                       break label98;
                                    }
                                 }
                              } else if (var11.equals("nm")) {
                                 var16 = 0;
                                 break label98;
                              }
                           } else if (var11.equals("ml")) {
                              var16 = 6;
                              break label98;
                           }
                        } else if (var11.equals("lj")) {
                           var16 = 5;
                           break label98;
                        }
                     } else if (var11.equals("lc")) {
                        var16 = 4;
                        break label98;
                     }
                  } else if (var11.equals("w")) {
                     var16 = 2;
                     break label98;
                  }
               } else if (var11.equals("o")) {
                  var16 = 3;
                  break label98;
               }

               var16 = -1;
            }

            switch(var16) {
            case 0:
               var3 = var0.nextString();
               break;
            case 1:
               var5 = AnimatableValueParser.parseColor(var0, var1);
               break;
            case 2:
               var7 = AnimatableValueParser.parseFloat(var0, var1);
               break;
            case 3:
               var6 = AnimatableValueParser.parseInteger(var0, var1);
               break;
            case 4:
               var8 = ShapeStroke.LineCapType.values()[var0.nextInt() - 1];
               break;
            case 5:
               var9 = ShapeStroke.LineJoinType.values()[var0.nextInt() - 1];
               break;
            case 6:
               var10 = (float)var0.nextDouble();
               break;
            case 7:
               var0.beginArray();
               AnimatableFloatValue var15 = var4;

               while(var0.hasNext()) {
                  var0.beginObject();
                  String var13 = null;
                  var4 = null;

                  while(var0.hasNext()) {
                     label117: {
                        String var14 = var0.nextName();
                        var12 = var14.hashCode();
                        if (var12 != 110) {
                           if (var12 == 118 && var14.equals("v")) {
                              var16 = 1;
                              break label117;
                           }
                        } else if (var14.equals("n")) {
                           var16 = 0;
                           break label117;
                        }

                        var16 = -1;
                     }

                     switch(var16) {
                     case 0:
                        var13 = var0.nextString();
                        break;
                     case 1:
                        var4 = AnimatableValueParser.parseFloat(var0, var1);
                        break;
                     default:
                        var0.skipValue();
                     }
                  }

                  label129: {
                     var0.endObject();
                     var12 = var13.hashCode();
                     if (var12 != 100) {
                        if (var12 != 103) {
                           if (var12 == 111 && var13.equals("o")) {
                              var16 = 0;
                              break label129;
                           }
                        } else if (var13.equals("g")) {
                           var16 = 2;
                           break label129;
                        }
                     } else if (var13.equals("d")) {
                        var16 = 1;
                        break label129;
                     }

                     var16 = -1;
                  }

                  switch(var16) {
                  case 0:
                     var15 = var4;
                     break;
                  case 1:
                  case 2:
                     var2.add(var4);
                  }
               }

               var0.endArray();
               var4 = var15;
               if (var2.size() == 1) {
                  var2.add(var2.get(0));
                  var4 = var15;
               }
               break;
            default:
               var0.skipValue();
            }
         }

         return new ShapeStroke(var3, var4, var2, var5, var6, var7, var8, var9, var10);
      }
   }
}
