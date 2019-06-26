package android.support.v4.content.res;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.AnyRes;
import android.support.annotation.RestrictTo;
import android.support.annotation.StyleableRes;
import android.util.TypedValue;

@RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
public class TypedArrayUtils {
   public static int getAttr(Context var0, int var1, int var2) {
      TypedValue var3 = new TypedValue();
      var0.getTheme().resolveAttribute(var1, var3, true);
      if (var3.resourceId == 0) {
         var1 = var2;
      }

      return var1;
   }

   public static boolean getBoolean(TypedArray var0, @StyleableRes int var1, @StyleableRes int var2, boolean var3) {
      return var0.getBoolean(var1, var0.getBoolean(var2, var3));
   }

   public static Drawable getDrawable(TypedArray var0, @StyleableRes int var1, @StyleableRes int var2) {
      Drawable var3 = var0.getDrawable(var1);
      Drawable var4 = var3;
      if (var3 == null) {
         var4 = var0.getDrawable(var2);
      }

      return var4;
   }

   public static int getInt(TypedArray var0, @StyleableRes int var1, @StyleableRes int var2, int var3) {
      return var0.getInt(var1, var0.getInt(var2, var3));
   }

   @AnyRes
   public static int getResourceId(TypedArray var0, @StyleableRes int var1, @StyleableRes int var2, @AnyRes int var3) {
      return var0.getResourceId(var1, var0.getResourceId(var2, var3));
   }

   public static String getString(TypedArray var0, @StyleableRes int var1, @StyleableRes int var2) {
      String var3 = var0.getString(var1);
      String var4 = var3;
      if (var3 == null) {
         var4 = var0.getString(var2);
      }

      return var4;
   }

   public static CharSequence getText(TypedArray var0, @StyleableRes int var1, @StyleableRes int var2) {
      CharSequence var3 = var0.getText(var1);
      CharSequence var4 = var3;
      if (var3 == null) {
         var4 = var0.getText(var2);
      }

      return var4;
   }

   public static CharSequence[] getTextArray(TypedArray var0, @StyleableRes int var1, @StyleableRes int var2) {
      CharSequence[] var3 = var0.getTextArray(var1);
      CharSequence[] var4 = var3;
      if (var3 == null) {
         var4 = var0.getTextArray(var2);
      }

      return var4;
   }
}
