package android.support.v4.content.res;

import android.content.res.Resources;
import android.content.res.TypedArray;
import android.content.res.Resources.Theme;
import android.util.AttributeSet;
import android.util.TypedValue;
import org.xmlpull.v1.XmlPullParser;

public class TypedArrayUtils {
   public static boolean getNamedBoolean(TypedArray var0, XmlPullParser var1, String var2, int var3, boolean var4) {
      return !hasAttribute(var1, var2) ? var4 : var0.getBoolean(var3, var4);
   }

   public static int getNamedColor(TypedArray var0, XmlPullParser var1, String var2, int var3, int var4) {
      return !hasAttribute(var1, var2) ? var4 : var0.getColor(var3, var4);
   }

   public static ComplexColorCompat getNamedComplexColor(TypedArray var0, XmlPullParser var1, Theme var2, String var3, int var4, int var5) {
      if (hasAttribute(var1, var3)) {
         TypedValue var7 = new TypedValue();
         var0.getValue(var4, var7);
         if (var7.type >= 28 && var7.type <= 31) {
            return ComplexColorCompat.from(var7.data);
         }

         ComplexColorCompat var6 = ComplexColorCompat.inflate(var0.getResources(), var0.getResourceId(var4, 0), var2);
         if (var6 != null) {
            return var6;
         }
      }

      return ComplexColorCompat.from(var5);
   }

   public static float getNamedFloat(TypedArray var0, XmlPullParser var1, String var2, int var3, float var4) {
      return !hasAttribute(var1, var2) ? var4 : var0.getFloat(var3, var4);
   }

   public static int getNamedInt(TypedArray var0, XmlPullParser var1, String var2, int var3, int var4) {
      return !hasAttribute(var1, var2) ? var4 : var0.getInt(var3, var4);
   }

   public static int getNamedResourceId(TypedArray var0, XmlPullParser var1, String var2, int var3, int var4) {
      return !hasAttribute(var1, var2) ? var4 : var0.getResourceId(var3, var4);
   }

   public static String getNamedString(TypedArray var0, XmlPullParser var1, String var2, int var3) {
      return !hasAttribute(var1, var2) ? null : var0.getString(var3);
   }

   public static boolean hasAttribute(XmlPullParser var0, String var1) {
      boolean var2;
      if (var0.getAttributeValue("http://schemas.android.com/apk/res/android", var1) != null) {
         var2 = true;
      } else {
         var2 = false;
      }

      return var2;
   }

   public static TypedArray obtainAttributes(Resources var0, Theme var1, AttributeSet var2, int[] var3) {
      return var1 == null ? var0.obtainAttributes(var2, var3) : var1.obtainStyledAttributes(var2, var3, 0, 0);
   }

   public static TypedValue peekNamedValue(TypedArray var0, XmlPullParser var1, String var2, int var3) {
      return !hasAttribute(var1, var2) ? null : var0.peekValue(var3);
   }
}
