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

      boolean var14;
      ShapeStroke.LineCapType var24;
      for(var14 = false; var0.hasNext(); var10 = var24) {
         String var15;
         int var16;
         byte var25;
         label142: {
            var15 = var0.nextName();
            var16 = var15.hashCode();
            if (var16 != 100) {
               if (var16 != 101) {
                  if (var16 != 103) {
                     if (var16 != 111) {
                        if (var16 != 119) {
                           if (var16 != 3324) {
                              if (var16 != 3447) {
                                 if (var16 != 3454) {
                                    if (var16 != 3487) {
                                       if (var16 != 3519) {
                                          if (var16 != 115) {
                                             if (var16 == 116 && var15.equals("t")) {
                                                var25 = 3;
                                                break label142;
                                             }
                                          } else if (var15.equals("s")) {
                                             var25 = 4;
                                             break label142;
                                          }
                                       } else if (var15.equals("nm")) {
                                          var25 = 0;
                                          break label142;
                                       }
                                    } else if (var15.equals("ml")) {
                                       var25 = 9;
                                       break label142;
                                    }
                                 } else if (var15.equals("lj")) {
                                    var25 = 8;
                                    break label142;
                                 }
                              } else if (var15.equals("lc")) {
                                 var25 = 7;
                                 break label142;
                              }
                           } else if (var15.equals("hd")) {
                              var25 = 10;
                              break label142;
                           }
                        } else if (var15.equals("w")) {
                           var25 = 6;
                           break label142;
                        }
                     } else if (var15.equals("o")) {
                        var25 = 2;
                        break label142;
                     }
                  } else if (var15.equals("g")) {
                     var25 = 1;
                     break label142;
                  }
               } else if (var15.equals("e")) {
                  var25 = 5;
                  break label142;
               }
            } else if (var15.equals("d")) {
               var25 = 11;
               break label142;
            }

            var25 = -1;
         }

         AnimatableFloatValue var21;
         label186: {
            label185:
            switch(var25) {
            case 0:
               var3 = var0.nextString();
               break;
            case 1:
               var0.beginObject();
               int var19 = -1;

               while(var0.hasNext()) {
                  label179: {
                     var15 = var0.nextName();
                     var16 = var15.hashCode();
                     if (var16 != 107) {
                        if (var16 == 112 && var15.equals("p")) {
                           var25 = 0;
                           break label179;
                        }
                     } else if (var15.equals("k")) {
                        var25 = 1;
                        break label179;
                     }

                     var25 = -1;
                  }

                  if (var25 != 0) {
                     if (var25 != 1) {
                        var0.skipValue();
                     } else {
                        var5 = AnimatableValueParser.parseGradientColor(var0, var1, var19);
                     }
                  } else {
                     var19 = var0.nextInt();
                  }
               }

               var0.endObject();
               break;
            case 2:
               var6 = AnimatableValueParser.parseInteger(var0, var1);
               break;
            case 3:
               ShapeStroke.LineCapType var20 = var10;
               GradientType var22;
               if (var0.nextInt() == 1) {
                  var22 = GradientType.LINEAR;
               } else {
                  var22 = GradientType.RADIAL;
               }

               GradientType var26 = var22;
               var10 = var20;
               var4 = var26;
               break;
            case 4:
               var7 = AnimatableValueParser.parsePoint(var0, var1);
               break;
            case 5:
               var8 = AnimatableValueParser.parsePoint(var0, var1);
               break;
            case 6:
               var9 = AnimatableValueParser.parseFloat(var0, var1);
               var24 = var10;
               var21 = var9;
               break label186;
            case 7:
               var10 = ShapeStroke.LineCapType.values()[var0.nextInt() - 1];
               break;
            case 8:
               var11 = ShapeStroke.LineJoinType.values()[var0.nextInt() - 1];
               var24 = var10;
               var21 = var9;
               break label186;
            case 9:
               var12 = (float)var0.nextDouble();
               break;
            case 10:
               var14 = var0.nextBoolean();
               break;
            case 11:
               var0.beginArray();

               while(true) {
                  AnimatableFloatValue var27;
                  while(true) {
                     if (!var0.hasNext()) {
                        var0.endArray();
                        if (var2.size() == 1) {
                           var2.add(var2.get(0));
                        }
                        break label185;
                     }

                     var0.beginObject();
                     String var17 = null;
                     AnimatableFloatValue var23 = null;

                     while(var0.hasNext()) {
                        label155: {
                           String var18 = var0.nextName();
                           var16 = var18.hashCode();
                           if (var16 != 110) {
                              if (var16 == 118 && var18.equals("v")) {
                                 var25 = 1;
                                 break label155;
                              }
                           } else if (var18.equals("n")) {
                              var25 = 0;
                              break label155;
                           }

                           var25 = -1;
                        }

                        if (var25 != 0) {
                           if (var25 != 1) {
                              var0.skipValue();
                           } else {
                              var23 = AnimatableValueParser.parseFloat(var0, var1);
                           }
                        } else {
                           var17 = var0.nextString();
                        }
                     }

                     var0.endObject();
                     if (var17.equals("o")) {
                        var27 = var23;
                        break;
                     }

                     if (!var17.equals("d")) {
                        var27 = var13;
                        if (!var17.equals("g")) {
                           break;
                        }
                     }

                     var1.setHasDashPattern(true);
                     var2.add(var23);
                  }

                  var13 = var27;
               }
            default:
               var0.skipValue();
            }

            var24 = var10;
            var21 = var9;
         }

         var9 = var21;
      }

      return new GradientStroke(var3, var4, var5, var6, var7, var8, var9, var10, var11, var12, var2, var13, var14);
   }
}
