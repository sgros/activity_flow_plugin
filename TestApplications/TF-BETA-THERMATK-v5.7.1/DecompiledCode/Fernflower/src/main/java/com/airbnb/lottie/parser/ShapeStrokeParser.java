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
      boolean var11 = false;

      while(true) {
         label117:
         while(var0.hasNext()) {
            int var13;
            byte var17;
            label110: {
               String var12 = var0.nextName();
               var13 = var12.hashCode();
               if (var13 != 99) {
                  if (var13 != 100) {
                     if (var13 != 111) {
                        if (var13 != 119) {
                           if (var13 != 3324) {
                              if (var13 != 3447) {
                                 if (var13 != 3454) {
                                    if (var13 != 3487) {
                                       if (var13 == 3519 && var12.equals("nm")) {
                                          var17 = 0;
                                          break label110;
                                       }
                                    } else if (var12.equals("ml")) {
                                       var17 = 6;
                                       break label110;
                                    }
                                 } else if (var12.equals("lj")) {
                                    var17 = 5;
                                    break label110;
                                 }
                              } else if (var12.equals("lc")) {
                                 var17 = 4;
                                 break label110;
                              }
                           } else if (var12.equals("hd")) {
                              var17 = 7;
                              break label110;
                           }
                        } else if (var12.equals("w")) {
                           var17 = 2;
                           break label110;
                        }
                     } else if (var12.equals("o")) {
                        var17 = 3;
                        break label110;
                     }
                  } else if (var12.equals("d")) {
                     var17 = 8;
                     break label110;
                  }
               } else if (var12.equals("c")) {
                  var17 = 1;
                  break label110;
               }

               var17 = -1;
            }

            switch(var17) {
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
               var11 = var0.nextBoolean();
               break;
            case 8:
               var0.beginArray();

               while(true) {
                  AnimatableFloatValue var16;
                  label153:
                  do {
                     while(var0.hasNext()) {
                        var0.beginObject();
                        String var14 = null;
                        var16 = null;

                        while(var0.hasNext()) {
                           label129: {
                              String var15 = var0.nextName();
                              var13 = var15.hashCode();
                              if (var13 != 110) {
                                 if (var13 == 118 && var15.equals("v")) {
                                    var17 = 1;
                                    break label129;
                                 }
                              } else if (var15.equals("n")) {
                                 var17 = 0;
                                 break label129;
                              }

                              var17 = -1;
                           }

                           if (var17 != 0) {
                              if (var17 != 1) {
                                 var0.skipValue();
                              } else {
                                 var16 = AnimatableValueParser.parseFloat(var0, var1);
                              }
                           } else {
                              var14 = var0.nextString();
                           }
                        }

                        label141: {
                           var0.endObject();
                           var13 = var14.hashCode();
                           if (var13 != 100) {
                              if (var13 != 103) {
                                 if (var13 == 111 && var14.equals("o")) {
                                    var17 = 0;
                                    break label141;
                                 }
                              } else if (var14.equals("g")) {
                                 var17 = 2;
                                 break label141;
                              }
                           } else if (var14.equals("d")) {
                              var17 = 1;
                              break label141;
                           }

                           var17 = -1;
                        }

                        if (var17 != 0) {
                           continue label153;
                        }

                        var4 = var16;
                     }

                     var0.endArray();
                     if (var2.size() == 1) {
                        var2.add(var2.get(0));
                     }
                     continue label117;
                  } while(var17 != 1 && var17 != 2);

                  var1.setHasDashPattern(true);
                  var2.add(var16);
               }
            default:
               var0.skipValue();
            }
         }

         return new ShapeStroke(var3, var4, var2, var5, var6, var7, var8, var9, var10, var11);
      }
   }
}
