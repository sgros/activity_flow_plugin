package android.support.v7.widget;

import android.graphics.Rect;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.DrawableContainer;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.InsetDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ScaleDrawable;
import android.graphics.drawable.Drawable.ConstantState;
import android.graphics.drawable.DrawableContainer.DrawableContainerState;
import android.os.Build.VERSION;
import android.support.v4.graphics.drawable.WrappedDrawable;
import android.support.v7.graphics.drawable.DrawableWrapper;

public class DrawableUtils {
   public static final Rect INSETS_NONE = new Rect();
   private static Class sInsetsClazz;

   static {
      if (VERSION.SDK_INT >= 18) {
         try {
            sInsetsClazz = Class.forName("android.graphics.Insets");
         } catch (ClassNotFoundException var1) {
         }
      }

   }

   public static boolean canSafelyMutateDrawable(Drawable var0) {
      if (VERSION.SDK_INT < 15 && var0 instanceof InsetDrawable) {
         return false;
      } else if (VERSION.SDK_INT < 15 && var0 instanceof GradientDrawable) {
         return false;
      } else if (VERSION.SDK_INT < 17 && var0 instanceof LayerDrawable) {
         return false;
      } else {
         if (var0 instanceof DrawableContainer) {
            ConstantState var3 = var0.getConstantState();
            if (var3 instanceof DrawableContainerState) {
               Drawable[] var4 = ((DrawableContainerState)var3).getChildren();
               int var1 = var4.length;

               for(int var2 = 0; var2 < var1; ++var2) {
                  if (!canSafelyMutateDrawable(var4[var2])) {
                     return false;
                  }
               }
            }
         } else {
            if (var0 instanceof WrappedDrawable) {
               return canSafelyMutateDrawable(((WrappedDrawable)var0).getWrappedDrawable());
            }

            if (var0 instanceof DrawableWrapper) {
               return canSafelyMutateDrawable(((DrawableWrapper)var0).getWrappedDrawable());
            }

            if (var0 instanceof ScaleDrawable) {
               return canSafelyMutateDrawable(((ScaleDrawable)var0).getDrawable());
            }
         }

         return true;
      }
   }

   static void fixDrawable(Drawable var0) {
      if (VERSION.SDK_INT == 21 && "android.graphics.drawable.VectorDrawable".equals(var0.getClass().getName())) {
         fixVectorDrawableTinting(var0);
      }

   }

   private static void fixVectorDrawableTinting(Drawable var0) {
      int[] var1 = var0.getState();
      if (var1 != null && var1.length != 0) {
         var0.setState(ThemeUtils.EMPTY_STATE_SET);
      } else {
         var0.setState(ThemeUtils.CHECKED_STATE_SET);
      }

      var0.setState(var1);
   }

   public static Mode parseTintMode(int var0, Mode var1) {
      if (var0 != 3) {
         if (var0 != 5) {
            if (var0 != 9) {
               switch(var0) {
               case 14:
                  return Mode.MULTIPLY;
               case 15:
                  return Mode.SCREEN;
               case 16:
                  return Mode.ADD;
               default:
                  return var1;
               }
            } else {
               return Mode.SRC_ATOP;
            }
         } else {
            return Mode.SRC_IN;
         }
      } else {
         return Mode.SRC_OVER;
      }
   }
}
