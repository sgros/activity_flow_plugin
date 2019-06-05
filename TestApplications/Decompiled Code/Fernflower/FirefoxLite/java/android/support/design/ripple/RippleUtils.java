package android.support.design.ripple;

import android.annotation.TargetApi;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build.VERSION;
import android.support.v4.graphics.ColorUtils;
import android.util.StateSet;

public class RippleUtils {
   private static final int[] FOCUSED_STATE_SET;
   private static final int[] HOVERED_FOCUSED_STATE_SET;
   private static final int[] HOVERED_STATE_SET;
   private static final int[] PRESSED_STATE_SET;
   private static final int[] SELECTED_FOCUSED_STATE_SET;
   private static final int[] SELECTED_HOVERED_FOCUSED_STATE_SET;
   private static final int[] SELECTED_HOVERED_STATE_SET;
   private static final int[] SELECTED_PRESSED_STATE_SET;
   private static final int[] SELECTED_STATE_SET;
   public static final boolean USE_FRAMEWORK_RIPPLE;

   static {
      boolean var0;
      if (VERSION.SDK_INT >= 21) {
         var0 = true;
      } else {
         var0 = false;
      }

      USE_FRAMEWORK_RIPPLE = var0;
      PRESSED_STATE_SET = new int[]{16842919};
      HOVERED_FOCUSED_STATE_SET = new int[]{16843623, 16842908};
      FOCUSED_STATE_SET = new int[]{16842908};
      HOVERED_STATE_SET = new int[]{16843623};
      SELECTED_PRESSED_STATE_SET = new int[]{16842913, 16842919};
      SELECTED_HOVERED_FOCUSED_STATE_SET = new int[]{16842913, 16843623, 16842908};
      SELECTED_FOCUSED_STATE_SET = new int[]{16842913, 16842908};
      SELECTED_HOVERED_STATE_SET = new int[]{16842913, 16843623};
      SELECTED_STATE_SET = new int[]{16842913};
   }

   public static ColorStateList convertToRippleDrawableColor(ColorStateList var0) {
      int[] var1;
      int var2;
      int[] var3;
      int var4;
      if (USE_FRAMEWORK_RIPPLE) {
         var1 = SELECTED_STATE_SET;
         var2 = getColorForState(var0, SELECTED_PRESSED_STATE_SET);
         var3 = StateSet.NOTHING;
         var4 = getColorForState(var0, PRESSED_STATE_SET);
         return new ColorStateList(new int[][]{var1, var3}, new int[]{var2, var4});
      } else {
         var1 = SELECTED_PRESSED_STATE_SET;
         var4 = getColorForState(var0, SELECTED_PRESSED_STATE_SET);
         int[] var5 = SELECTED_HOVERED_FOCUSED_STATE_SET;
         int var6 = getColorForState(var0, SELECTED_HOVERED_FOCUSED_STATE_SET);
         int[] var7 = SELECTED_FOCUSED_STATE_SET;
         int var8 = getColorForState(var0, SELECTED_FOCUSED_STATE_SET);
         int[] var9 = SELECTED_HOVERED_STATE_SET;
         int var10 = getColorForState(var0, SELECTED_HOVERED_STATE_SET);
         int[] var11 = SELECTED_STATE_SET;
         var3 = PRESSED_STATE_SET;
         var2 = getColorForState(var0, PRESSED_STATE_SET);
         int[] var12 = HOVERED_FOCUSED_STATE_SET;
         int var13 = getColorForState(var0, HOVERED_FOCUSED_STATE_SET);
         int[] var14 = FOCUSED_STATE_SET;
         int var15 = getColorForState(var0, FOCUSED_STATE_SET);
         int[] var16 = HOVERED_STATE_SET;
         int var17 = getColorForState(var0, HOVERED_STATE_SET);
         return new ColorStateList(new int[][]{var1, var5, var7, var9, var11, var3, var12, var14, var16, StateSet.NOTHING}, new int[]{var4, var6, var8, var10, 0, var2, var13, var15, var17, 0});
      }
   }

   @TargetApi(21)
   private static int doubleAlpha(int var0) {
      return ColorUtils.setAlphaComponent(var0, Math.min(Color.alpha(var0) * 2, 255));
   }

   private static int getColorForState(ColorStateList var0, int[] var1) {
      int var2;
      if (var0 != null) {
         var2 = var0.getColorForState(var1, var0.getDefaultColor());
      } else {
         var2 = 0;
      }

      int var3 = var2;
      if (USE_FRAMEWORK_RIPPLE) {
         var3 = doubleAlpha(var2);
      }

      return var3;
   }
}
