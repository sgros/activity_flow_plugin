package com.airbnb.lottie.parser;

import android.graphics.PointF;
import android.support.v4.util.SparseArrayCompat;
import android.support.v4.view.animation.PathInterpolatorCompat;
import android.util.JsonReader;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
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
      Object var4 = null;
      PointF var5 = null;
      PointF var6 = var5;
      Object var7 = var5;
      Object var8 = var5;
      PointF var9 = var5;
      PointF var10 = var5;
      boolean var11 = false;
      float var12 = 0.0F;

      int var14;
      while(var1.hasNext()) {
         boolean var15;
         byte var24;
         label85: {
            String var13 = var1.nextName();
            var14 = var13.hashCode();
            var15 = true;
            switch(var14) {
            case 101:
               if (var13.equals("e")) {
                  var24 = 2;
                  break label85;
               }
               break;
            case 104:
               if (var13.equals("h")) {
                  var24 = 5;
                  break label85;
               }
               break;
            case 105:
               if (var13.equals("i")) {
                  var24 = 4;
                  break label85;
               }
               break;
            case 111:
               if (var13.equals("o")) {
                  var24 = 3;
                  break label85;
               }
               break;
            case 115:
               if (var13.equals("s")) {
                  var24 = 1;
                  break label85;
               }
               break;
            case 116:
               if (var13.equals("t")) {
                  var24 = 0;
                  break label85;
               }
               break;
            case 3701:
               if (var13.equals("ti")) {
                  var24 = 7;
                  break label85;
               }
               break;
            case 3707:
               if (var13.equals("to")) {
                  var24 = 6;
                  break label85;
               }
            }

            var24 = -1;
         }

         switch(var24) {
         case 0:
            var12 = (float)var1.nextDouble();
            break;
         case 1:
            var7 = var3.parse(var1, var2);
            break;
         case 2:
            var8 = var3.parse(var1, var2);
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
               var25 = var15;
            } else {
               var25 = false;
            }

            var11 = var25;
            break;
         case 6:
            var9 = JsonUtils.jsonToPoint(var1, var2);
            break;
         case 7:
            var10 = JsonUtils.jsonToPoint(var1, var2);
            break;
         default:
            var1.skipValue();
         }
      }

      var1.endObject();
      Interpolator var20;
      if (var11) {
         var20 = LINEAR_INTERPOLATOR;
         var8 = var7;
      } else if (var5 != null && var6 != null) {
         float var16 = var5.x;
         float var17 = -var2;
         var5.x = MiscUtils.clamp(var16, var17, var2);
         var5.y = MiscUtils.clamp(var5.y, -100.0F, 100.0F);
         var6.x = MiscUtils.clamp(var6.x, var17, var2);
         var6.y = MiscUtils.clamp(var6.y, -100.0F, 100.0F);
         var14 = Utils.hashFor(var5.x, var5.y, var6.x, var6.y);
         WeakReference var22 = getInterpolator(var14);
         var20 = (Interpolator)var4;
         if (var22 != null) {
            var20 = (Interpolator)var22.get();
         }

         Interpolator var23;
         label92: {
            if (var22 != null) {
               var23 = var20;
               if (var20 != null) {
                  break label92;
               }
            }

            var23 = PathInterpolatorCompat.create(var5.x / var2, var5.y / var2, var6.x / var2, var6.y / var2);

            try {
               WeakReference var21 = new WeakReference(var23);
               putInterpolator(var14, var21);
            } catch (ArrayIndexOutOfBoundsException var18) {
            }
         }

         var20 = var23;
      } else {
         var20 = LINEAR_INTERPOLATOR;
      }

      Keyframe var19 = new Keyframe(var0, var7, var8, var20, var12, (Float)null);
      var19.pathCp1 = var9;
      var19.pathCp2 = var10;
      return var19;
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
