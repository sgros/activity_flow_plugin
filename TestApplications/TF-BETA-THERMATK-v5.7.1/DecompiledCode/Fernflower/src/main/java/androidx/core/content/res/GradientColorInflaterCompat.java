package androidx.core.content.res;

import android.content.res.Resources;
import android.content.res.TypedArray;
import android.content.res.Resources.Theme;
import android.graphics.LinearGradient;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.graphics.Shader.TileMode;
import android.util.AttributeSet;
import androidx.core.R$styleable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

final class GradientColorInflaterCompat {
   private static GradientColorInflaterCompat.ColorStops checkColors(GradientColorInflaterCompat.ColorStops var0, int var1, int var2, boolean var3, int var4) {
      if (var0 != null) {
         return var0;
      } else {
         return var3 ? new GradientColorInflaterCompat.ColorStops(var1, var4, var2) : new GradientColorInflaterCompat.ColorStops(var1, var2);
      }
   }

   static Shader createFromXmlInner(Resources var0, XmlPullParser var1, AttributeSet var2, Theme var3) throws IOException, XmlPullParserException {
      String var4 = var1.getName();
      if (var4.equals("gradient")) {
         TypedArray var20 = TypedArrayUtils.obtainAttributes(var0, var3, var2, R$styleable.GradientColor);
         float var5 = TypedArrayUtils.getNamedFloat(var20, var1, "startX", R$styleable.GradientColor_android_startX, 0.0F);
         float var6 = TypedArrayUtils.getNamedFloat(var20, var1, "startY", R$styleable.GradientColor_android_startY, 0.0F);
         float var7 = TypedArrayUtils.getNamedFloat(var20, var1, "endX", R$styleable.GradientColor_android_endX, 0.0F);
         float var8 = TypedArrayUtils.getNamedFloat(var20, var1, "endY", R$styleable.GradientColor_android_endY, 0.0F);
         float var9 = TypedArrayUtils.getNamedFloat(var20, var1, "centerX", R$styleable.GradientColor_android_centerX, 0.0F);
         float var10 = TypedArrayUtils.getNamedFloat(var20, var1, "centerY", R$styleable.GradientColor_android_centerY, 0.0F);
         int var11 = TypedArrayUtils.getNamedInt(var20, var1, "type", R$styleable.GradientColor_android_type, 0);
         int var12 = TypedArrayUtils.getNamedColor(var20, var1, "startColor", R$styleable.GradientColor_android_startColor, 0);
         boolean var13 = TypedArrayUtils.hasAttribute(var1, "centerColor");
         int var14 = TypedArrayUtils.getNamedColor(var20, var1, "centerColor", R$styleable.GradientColor_android_centerColor, 0);
         int var15 = TypedArrayUtils.getNamedColor(var20, var1, "endColor", R$styleable.GradientColor_android_endColor, 0);
         int var16 = TypedArrayUtils.getNamedInt(var20, var1, "tileMode", R$styleable.GradientColor_android_tileMode, 0);
         float var17 = TypedArrayUtils.getNamedFloat(var20, var1, "gradientRadius", R$styleable.GradientColor_android_gradientRadius, 0.0F);
         var20.recycle();
         GradientColorInflaterCompat.ColorStops var19 = checkColors(inflateChildElements(var0, var1, var2, var3), var12, var15, var13, var14);
         if (var11 != 1) {
            return (Shader)(var11 != 2 ? new LinearGradient(var5, var6, var7, var8, var19.mColors, var19.mOffsets, parseTileMode(var16)) : new SweepGradient(var9, var10, var19.mColors, var19.mOffsets));
         } else if (var17 > 0.0F) {
            return new RadialGradient(var9, var10, var17, var19.mColors, var19.mOffsets, parseTileMode(var16));
         } else {
            throw new XmlPullParserException("<gradient> tag requires 'gradientRadius' attribute with radial type");
         }
      } else {
         StringBuilder var18 = new StringBuilder();
         var18.append(var1.getPositionDescription());
         var18.append(": invalid gradient color tag ");
         var18.append(var4);
         throw new XmlPullParserException(var18.toString());
      }
   }

   private static GradientColorInflaterCompat.ColorStops inflateChildElements(Resources var0, XmlPullParser var1, AttributeSet var2, Theme var3) throws XmlPullParserException, IOException {
      int var4 = var1.getDepth() + 1;
      ArrayList var5 = new ArrayList(20);
      ArrayList var6 = new ArrayList(20);

      while(true) {
         int var7 = var1.next();
         if (var7 == 1) {
            break;
         }

         int var8 = var1.getDepth();
         if (var8 < var4 && var7 == 3) {
            break;
         }

         if (var7 == 2 && var8 <= var4 && var1.getName().equals("item")) {
            TypedArray var9 = TypedArrayUtils.obtainAttributes(var0, var3, var2, R$styleable.GradientColorItem);
            boolean var10 = var9.hasValue(R$styleable.GradientColorItem_android_color);
            boolean var11 = var9.hasValue(R$styleable.GradientColorItem_android_offset);
            if (!var10 || !var11) {
               StringBuilder var13 = new StringBuilder();
               var13.append(var1.getPositionDescription());
               var13.append(": <item> tag requires a 'color' attribute and a 'offset' attribute!");
               throw new XmlPullParserException(var13.toString());
            }

            var8 = var9.getColor(R$styleable.GradientColorItem_android_color, 0);
            float var12 = var9.getFloat(R$styleable.GradientColorItem_android_offset, 0.0F);
            var9.recycle();
            var6.add(var8);
            var5.add(var12);
         }
      }

      return var6.size() > 0 ? new GradientColorInflaterCompat.ColorStops(var6, var5) : null;
   }

   private static TileMode parseTileMode(int var0) {
      if (var0 != 1) {
         return var0 != 2 ? TileMode.CLAMP : TileMode.MIRROR;
      } else {
         return TileMode.REPEAT;
      }
   }

   static final class ColorStops {
      final int[] mColors;
      final float[] mOffsets;

      ColorStops(int var1, int var2) {
         this.mColors = new int[]{var1, var2};
         this.mOffsets = new float[]{0.0F, 1.0F};
      }

      ColorStops(int var1, int var2, int var3) {
         this.mColors = new int[]{var1, var2, var3};
         this.mOffsets = new float[]{0.0F, 0.5F, 1.0F};
      }

      ColorStops(List var1, List var2) {
         int var3 = var1.size();
         this.mColors = new int[var3];
         this.mOffsets = new float[var3];

         for(int var4 = 0; var4 < var3; ++var4) {
            this.mColors[var4] = (Integer)var1.get(var4);
            this.mOffsets[var4] = (Float)var2.get(var4);
         }

      }
   }
}
