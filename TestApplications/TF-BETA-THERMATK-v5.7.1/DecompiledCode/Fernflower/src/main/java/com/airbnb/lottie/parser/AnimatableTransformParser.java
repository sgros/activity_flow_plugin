package com.airbnb.lottie.parser;

import android.graphics.PointF;
import android.util.JsonReader;
import android.util.JsonToken;
import android.view.animation.Interpolator;
import com.airbnb.lottie.LottieComposition;
import com.airbnb.lottie.model.animatable.AnimatableFloatValue;
import com.airbnb.lottie.model.animatable.AnimatableIntegerValue;
import com.airbnb.lottie.model.animatable.AnimatablePathValue;
import com.airbnb.lottie.model.animatable.AnimatableScaleValue;
import com.airbnb.lottie.model.animatable.AnimatableSplitDimensionPathValue;
import com.airbnb.lottie.model.animatable.AnimatableTransform;
import com.airbnb.lottie.model.animatable.AnimatableValue;
import com.airbnb.lottie.value.Keyframe;
import com.airbnb.lottie.value.ScaleXY;
import java.io.IOException;

public class AnimatableTransformParser {
   private static boolean isAnchorPointIdentity(AnimatablePathValue var0) {
      boolean var1 = false;
      boolean var2;
      if (var0 != null) {
         var2 = var1;
         if (!var0.isStatic()) {
            return var2;
         }

         var2 = var1;
         if (!((PointF)((Keyframe)var0.getKeyframes().get(0)).startValue).equals(0.0F, 0.0F)) {
            return var2;
         }
      }

      var2 = true;
      return var2;
   }

   private static boolean isPositionIdentity(AnimatableValue var0) {
      boolean var1 = false;
      boolean var2;
      if (var0 != null) {
         var2 = var1;
         if (var0 instanceof AnimatableSplitDimensionPathValue) {
            return var2;
         }

         var2 = var1;
         if (!var0.isStatic()) {
            return var2;
         }

         var2 = var1;
         if (!((PointF)((Keyframe)var0.getKeyframes().get(0)).startValue).equals(0.0F, 0.0F)) {
            return var2;
         }
      }

      var2 = true;
      return var2;
   }

   private static boolean isRotationIdentity(AnimatableFloatValue var0) {
      boolean var1 = false;
      boolean var2;
      if (var0 != null) {
         var2 = var1;
         if (!var0.isStatic()) {
            return var2;
         }

         var2 = var1;
         if ((Float)((Keyframe)var0.getKeyframes().get(0)).startValue != 0.0F) {
            return var2;
         }
      }

      var2 = true;
      return var2;
   }

   private static boolean isScaleIdentity(AnimatableScaleValue var0) {
      boolean var1 = false;
      boolean var2;
      if (var0 != null) {
         var2 = var1;
         if (!var0.isStatic()) {
            return var2;
         }

         var2 = var1;
         if (!((ScaleXY)((Keyframe)var0.getKeyframes().get(0)).startValue).equals(1.0F, 1.0F)) {
            return var2;
         }
      }

      var2 = true;
      return var2;
   }

   private static boolean isSkewAngleIdentity(AnimatableFloatValue var0) {
      boolean var1 = false;
      boolean var2;
      if (var0 != null) {
         var2 = var1;
         if (!var0.isStatic()) {
            return var2;
         }

         var2 = var1;
         if ((Float)((Keyframe)var0.getKeyframes().get(0)).startValue != 0.0F) {
            return var2;
         }
      }

      var2 = true;
      return var2;
   }

   private static boolean isSkewIdentity(AnimatableFloatValue var0) {
      boolean var1 = false;
      boolean var2;
      if (var0 != null) {
         var2 = var1;
         if (!var0.isStatic()) {
            return var2;
         }

         var2 = var1;
         if ((Float)((Keyframe)var0.getKeyframes().get(0)).startValue != 0.0F) {
            return var2;
         }
      }

      var2 = true;
      return var2;
   }

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

      AnimatableFloatValue var3 = null;
      AnimatablePathValue var4 = null;
      AnimatableValue var5 = null;
      AnimatableScaleValue var6 = null;
      AnimatableFloatValue var7 = null;
      AnimatableFloatValue var8 = null;
      AnimatableIntegerValue var9 = null;
      AnimatableFloatValue var10 = null;
      AnimatableFloatValue var11 = null;

      while(true) {
         while(var0.hasNext()) {
            byte var17;
            label124: {
               String var12 = var0.nextName();
               int var13 = var12.hashCode();
               if (var13 != 97) {
                  if (var13 != 3242) {
                     if (var13 != 3656) {
                        if (var13 != 3662) {
                           if (var13 != 3672) {
                              if (var13 != 3676) {
                                 if (var13 != 111) {
                                    if (var13 != 112) {
                                       if (var13 != 114) {
                                          if (var13 == 115 && var12.equals("s")) {
                                             var17 = 2;
                                             break label124;
                                          }
                                       } else if (var12.equals("r")) {
                                          var17 = 4;
                                          break label124;
                                       }
                                    } else if (var12.equals("p")) {
                                       var17 = 1;
                                       break label124;
                                    }
                                 } else if (var12.equals("o")) {
                                    var17 = 5;
                                    break label124;
                                 }
                              } else if (var12.equals("so")) {
                                 var17 = 6;
                                 break label124;
                              }
                           } else if (var12.equals("sk")) {
                              var17 = 8;
                              break label124;
                           }
                        } else if (var12.equals("sa")) {
                           var17 = 9;
                           break label124;
                        }
                     } else if (var12.equals("rz")) {
                        var17 = 3;
                        break label124;
                     }
                  } else if (var12.equals("eo")) {
                     var17 = 7;
                     break label124;
                  }
               } else if (var12.equals("a")) {
                  var17 = 0;
                  break label124;
               }

               var17 = -1;
            }

            switch(var17) {
            case 0:
               var0.beginObject();

               while(var0.hasNext()) {
                  if (var0.nextName().equals("k")) {
                     var4 = AnimatablePathValueParser.parse(var0, var1);
                  } else {
                     var0.skipValue();
                  }
               }

               var0.endObject();
               break;
            case 1:
               var5 = AnimatablePathValueParser.parseSplitPath(var0, var1);
               break;
            case 2:
               var6 = AnimatableValueParser.parseScale(var0, var1);
               break;
            case 3:
               var1.addWarning("Lottie doesn't support 3D layers.");
            case 4:
               var3 = AnimatableValueParser.parseFloat(var0, var1, false);
               if (var3.getKeyframes().isEmpty()) {
                  var3.getKeyframes().add(new Keyframe(var1, 0.0F, 0.0F, (Interpolator)null, 0.0F, var1.getEndFrame()));
               } else if (((Keyframe)var3.getKeyframes().get(0)).startValue == null) {
                  var3.getKeyframes().set(0, new Keyframe(var1, 0.0F, 0.0F, (Interpolator)null, 0.0F, var1.getEndFrame()));
               }
               break;
            case 5:
               var9 = AnimatableValueParser.parseInteger(var0, var1);
               break;
            case 6:
               var10 = AnimatableValueParser.parseFloat(var0, var1, false);
               break;
            case 7:
               var11 = AnimatableValueParser.parseFloat(var0, var1, false);
               break;
            case 8:
               var7 = AnimatableValueParser.parseFloat(var0, var1, false);
               break;
            case 9:
               var8 = AnimatableValueParser.parseFloat(var0, var1, false);
               break;
            default:
               var0.skipValue();
            }
         }

         if (var2) {
            var0.endObject();
         }

         AnimatablePathValue var14 = var4;
         if (isAnchorPointIdentity(var4)) {
            var14 = null;
         }

         AnimatableValue var15;
         if (isPositionIdentity(var5)) {
            var15 = null;
         } else {
            var15 = var5;
         }

         AnimatableFloatValue var16;
         if (isRotationIdentity(var3)) {
            var16 = null;
         } else {
            var16 = var3;
         }

         if (isScaleIdentity(var6)) {
            var6 = null;
         }

         if (isSkewIdentity(var7)) {
            var7 = null;
         }

         if (isSkewAngleIdentity(var8)) {
            var8 = null;
         }

         return new AnimatableTransform(var14, var15, var6, var16, var9, var10, var11, var7, var8);
      }
   }
}
