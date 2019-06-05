package android.support.v7.content.res;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.content.res.Resources.Theme;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.graphics.ColorUtils;
import android.support.v7.appcompat.R;
import android.util.AttributeSet;
import android.util.StateSet;
import android.util.Xml;
import java.io.IOException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

final class AppCompatColorStateListInflater {
   private static final int DEFAULT_COLOR = -65536;

   private AppCompatColorStateListInflater() {
   }

   @NonNull
   public static ColorStateList createFromXml(@NonNull Resources var0, @NonNull XmlPullParser var1, @Nullable Theme var2) throws XmlPullParserException, IOException {
      AttributeSet var3 = Xml.asAttributeSet(var1);

      int var4;
      do {
         var4 = var1.next();
      } while(var4 != 2 && var4 != 1);

      if (var4 != 2) {
         throw new XmlPullParserException("No start tag found");
      } else {
         return createFromXmlInner(var0, var1, var3, var2);
      }
   }

   @NonNull
   private static ColorStateList createFromXmlInner(@NonNull Resources var0, @NonNull XmlPullParser var1, @NonNull AttributeSet var2, @Nullable Theme var3) throws XmlPullParserException, IOException {
      String var4 = var1.getName();
      if (!var4.equals("selector")) {
         StringBuilder var5 = new StringBuilder();
         var5.append(var1.getPositionDescription());
         var5.append(": invalid color state list tag ");
         var5.append(var4);
         throw new XmlPullParserException(var5.toString());
      } else {
         return inflate(var0, var1, var2, var3);
      }
   }

   private static ColorStateList inflate(@NonNull Resources var0, @NonNull XmlPullParser var1, @NonNull AttributeSet var2, @Nullable Theme var3) throws XmlPullParserException, IOException {
      int var4 = var1.getDepth() + 1;
      int[][] var5 = new int[20][];
      int[] var6 = new int[var5.length];
      int var7 = 0;

      while(true) {
         int var8 = var1.next();
         if (var8 == 1) {
            break;
         }

         int var9 = var1.getDepth();
         if (var9 < var4 && var8 == 3) {
            break;
         }

         if (var8 == 2 && var9 <= var4 && var1.getName().equals("item")) {
            TypedArray var10 = obtainAttributes(var0, var3, var2, R.styleable.ColorStateListItem);
            int var11 = var10.getColor(R.styleable.ColorStateListItem_android_color, -65281);
            float var12 = 1.0F;
            if (var10.hasValue(R.styleable.ColorStateListItem_android_alpha)) {
               var12 = var10.getFloat(R.styleable.ColorStateListItem_android_alpha, 1.0F);
            } else if (var10.hasValue(R.styleable.ColorStateListItem_alpha)) {
               var12 = var10.getFloat(R.styleable.ColorStateListItem_alpha, 1.0F);
            }

            var10.recycle();
            int var13 = var2.getAttributeCount();
            int[] var18 = new int[var13];
            var9 = 0;

            int var15;
            for(var8 = var9; var9 < var13; var8 = var15) {
               int var14 = var2.getAttributeNameResource(var9);
               var15 = var8;
               if (var14 != 16843173) {
                  var15 = var8;
                  if (var14 != 16843551) {
                     var15 = var8;
                     if (var14 != R.attr.alpha) {
                        if (var2.getAttributeBooleanValue(var9, false)) {
                           var15 = var14;
                        } else {
                           var15 = -var14;
                        }

                        var18[var8] = var15;
                        var15 = var8 + 1;
                     }
                  }
               }

               ++var9;
            }

            var18 = StateSet.trimStateSet(var18, var8);
            var9 = modulateColorAlpha(var11, var12);
            if (var7 != 0) {
               var8 = var18.length;
            }

            var6 = GrowingArrayUtils.append(var6, var7, var9);
            var5 = (int[][])GrowingArrayUtils.append(var5, var7, var18);
            ++var7;
         }
      }

      int[] var17 = new int[var7];
      int[][] var16 = new int[var7][];
      System.arraycopy(var6, 0, var17, 0, var7);
      System.arraycopy(var5, 0, var16, 0, var7);
      return new ColorStateList(var16, var17);
   }

   private static int modulateColorAlpha(int var0, float var1) {
      return ColorUtils.setAlphaComponent(var0, Math.round((float)Color.alpha(var0) * var1));
   }

   private static TypedArray obtainAttributes(Resources var0, Theme var1, AttributeSet var2, int[] var3) {
      TypedArray var4;
      if (var1 == null) {
         var4 = var0.obtainAttributes(var2, var3);
      } else {
         var4 = var1.obtainStyledAttributes(var2, var3, 0, 0);
      }

      return var4;
   }
}