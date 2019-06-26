package android.support.v7.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.v4.graphics.ColorUtils;
import android.util.AttributeSet;
import android.util.TypedValue;

class ThemeUtils {
   static final int[] ACTIVATED_STATE_SET = new int[]{16843518};
   static final int[] CHECKED_STATE_SET = new int[]{16842912};
   static final int[] DISABLED_STATE_SET = new int[]{-16842910};
   static final int[] EMPTY_STATE_SET = new int[0];
   static final int[] FOCUSED_STATE_SET = new int[]{16842908};
   static final int[] NOT_PRESSED_OR_FOCUSED_STATE_SET = new int[]{-16842919, -16842908};
   static final int[] PRESSED_STATE_SET = new int[]{16842919};
   static final int[] SELECTED_STATE_SET = new int[]{16842913};
   private static final int[] TEMP_ARRAY = new int[1];
   private static final ThreadLocal TL_TYPED_VALUE = new ThreadLocal();

   public static ColorStateList createDisabledStateList(int var0, int var1) {
      return new ColorStateList(new int[][]{DISABLED_STATE_SET, EMPTY_STATE_SET}, new int[]{var1, var0});
   }

   public static int getDisabledThemeAttrColor(Context var0, int var1) {
      ColorStateList var2 = getThemeAttrColorStateList(var0, var1);
      if (var2 != null && var2.isStateful()) {
         return var2.getColorForState(DISABLED_STATE_SET, var2.getDefaultColor());
      } else {
         TypedValue var3 = getTypedValue();
         var0.getTheme().resolveAttribute(16842803, var3, true);
         return getThemeAttrColor(var0, var1, var3.getFloat());
      }
   }

   public static int getThemeAttrColor(Context var0, int var1) {
      TEMP_ARRAY[0] = var1;
      TintTypedArray var2 = TintTypedArray.obtainStyledAttributes(var0, (AttributeSet)null, TEMP_ARRAY);

      try {
         var1 = var2.getColor(0, 0);
      } finally {
         var2.recycle();
      }

      return var1;
   }

   static int getThemeAttrColor(Context var0, int var1, float var2) {
      var1 = getThemeAttrColor(var0, var1);
      return ColorUtils.setAlphaComponent(var1, Math.round((float)Color.alpha(var1) * var2));
   }

   public static ColorStateList getThemeAttrColorStateList(Context var0, int var1) {
      TEMP_ARRAY[0] = var1;
      TintTypedArray var5 = TintTypedArray.obtainStyledAttributes(var0, (AttributeSet)null, TEMP_ARRAY);

      ColorStateList var2;
      try {
         var2 = var5.getColorStateList(0);
      } finally {
         var5.recycle();
      }

      return var2;
   }

   private static TypedValue getTypedValue() {
      TypedValue var0 = (TypedValue)TL_TYPED_VALUE.get();
      TypedValue var1 = var0;
      if (var0 == null) {
         var1 = new TypedValue();
         TL_TYPED_VALUE.set(var1);
      }

      return var1;
   }
}
