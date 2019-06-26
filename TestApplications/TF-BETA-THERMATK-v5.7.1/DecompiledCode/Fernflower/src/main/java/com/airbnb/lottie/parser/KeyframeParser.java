package com.airbnb.lottie.parser;

import android.graphics.PointF;
import android.util.JsonReader;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import androidx.collection.SparseArrayCompat;
import androidx.core.view.animation.PathInterpolatorCompat;
import com.airbnb.lottie.LottieComposition;
import com.airbnb.lottie.utils.MiscUtils;
import com.airbnb.lottie.utils.Utils;
import com.airbnb.lottie.value.Keyframe;
import java.io.IOException;
import java.lang.ref.WeakReference;

class KeyframeParser {
   private static final Interpolator LINEAR_INTERPOLATOR = new LinearInterpolator();
   private static SparseArrayCompat pathInterpolatorCache;

   private static WeakReference getInterpolator(int param0) {
      // $FF: Couldn't be decompiled
   }

   static Keyframe parse(JsonReader var0, LottieComposition var1, float var2, ValueParser var3, boolean var4) throws IOException {
      return var4 ? parseKeyframe(var1, var0, var2, var3) : parseStaticValue(var0, var2, var3);
   }

   private static Keyframe parseKeyframe(LottieComposition var0, JsonReader var1, float var2, ValueParser var3) throws IOException {
      var1.beginObject();
      boolean var4 = false;
      PointF var5 = null;
      PointF var6 = null;
      Object var7 = null;
      Object var8 = null;
      float var9 = 0.0F;
      PointF var10 = null;
      PointF var11 = null;

      int var13;
      while(var1.hasNext()) {
         boolean var14;
         byte var24;
         label98: {
            String var12 = var1.nextName();
            var13 = var12.hashCode();
            var14 = true;
            if (var13 != 101) {
               if (var13 != 111) {
                  if (var13 != 3701) {
                     if (var13 != 3707) {
                        if (var13 != 104) {
                           if (var13 != 105) {
                              if (var13 != 115) {
                                 if (var13 == 116 && var12.equals("t")) {
                                    var24 = 0;
                                    break label98;
                                 }
                              } else if (var12.equals("s")) {
                                 var24 = 1;
                                 break label98;
                              }
                           } else if (var12.equals("i")) {
                              var24 = 4;
                              break label98;
                           }
                        } else if (var12.equals("h")) {
                           var24 = 5;
                           break label98;
                        }
                     } else if (var12.equals("to")) {
                        var24 = 6;
                        break label98;
                     }
                  } else if (var12.equals("ti")) {
                     var24 = 7;
                     break label98;
                  }
               } else if (var12.equals("o")) {
                  var24 = 3;
                  break label98;
               }
            } else if (var12.equals("e")) {
               var24 = 2;
               break label98;
            }

            var24 = -1;
         }

         switch(var24) {
         case 0:
            var9 = (float)var1.nextDouble();
            break;
         case 1:
            var8 = var3.parse(var1, var2);
            break;
         case 2:
            var7 = var3.parse(var1, var2);
            break;
         case 3:
            var5 = JsonUtils.jsonToPoint(var1, var2);
            break;
         case 4:
            var6 = JsonUtils.jsonToPoint(var1, var2);
            break;
         case 5:
            boolean var25;
            if (var1.nextInt() == 1) {
               var25 = var14;
            } else {
               var25 = false;
            }

            var4 = var25;
            break;
         case 6:
            var10 = JsonUtils.jsonToPoint(var1, var2);
            break;
         case 7:
            var11 = JsonUtils.jsonToPoint(var1, var2);
            break;
         default:
            var1.skipValue();
         }
      }

      var1.endObject();
      Object var19;
      Interpolator var22;
      if (var4) {
         var22 = LINEAR_INTERPOLATOR;
         var19 = var8;
      } else if (var5 != null && var6 != null) {
         float var15 = var5.x;
         float var16 = -var2;
         var5.x = MiscUtils.clamp(var15, var16, var2);
         var5.y = MiscUtils.clamp(var5.y, -100.0F, 100.0F);
         var6.x = MiscUtils.clamp(var6.x, var16, var2);
         var6.y = MiscUtils.clamp(var6.y, -100.0F, 100.0F);
         var13 = Utils.hashFor(var5.x, var5.y, var6.x, var6.y);
         WeakReference var23 = getInterpolator(var13);
         Interpolator var20;
         if (var23 != null) {
            var20 = (Interpolator)var23.get();
         } else {
            var20 = null;
         }

         label105: {
            if (var23 != null) {
               var22 = var20;
               if (var20 != null) {
                  break label105;
               }
            }

            var22 = PathInterpolatorCompat.create(var5.x / var2, var5.y / var2, var6.x / var2, var6.y / var2);

            try {
               WeakReference var21 = new WeakReference(var22);
               putInterpolator(var13, var21);
            } catch (ArrayIndexOutOfBoundsException var17) {
            }
         }

         var19 = var7;
      } else {
         var22 = LINEAR_INTERPOLATOR;
         var19 = var7;
      }

      Keyframe var18 = new Keyframe(var0, var8, var19, var22, var9, (Float)null);
      var18.pathCp1 = var10;
      var18.pathCp2 = var11;
      return var18;
   }

   private static Keyframe parseStaticValue(JsonReader var0, float var1, ValueParser var2) throws IOException {
      return new Keyframe(var2.parse(var0, var1));
   }

   private static SparseArrayCompat pathInterpolatorCache() {
      if (pathInterpolatorCache == null) {
         pathInterpolatorCache = new SparseArrayCompat();
      }

      return pathInterpolatorCache;
   }

   private static void putInterpolator(int param0, WeakReference param1) {
      // $FF: Couldn't be decompiled
   }
}
