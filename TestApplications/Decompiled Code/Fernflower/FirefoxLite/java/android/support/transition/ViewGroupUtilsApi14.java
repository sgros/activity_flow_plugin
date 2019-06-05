package android.support.transition;

import android.animation.Animator;
import android.animation.LayoutTransition;
import android.util.Log;
import android.view.ViewGroup;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

class ViewGroupUtilsApi14 {
   private static Method sCancelMethod;
   private static boolean sCancelMethodFetched;
   private static LayoutTransition sEmptyLayoutTransition;
   private static Field sLayoutSuppressedField;
   private static boolean sLayoutSuppressedFieldFetched;

   private static void cancelLayoutTransition(LayoutTransition var0) {
      if (!sCancelMethodFetched) {
         try {
            sCancelMethod = LayoutTransition.class.getDeclaredMethod("cancel");
            sCancelMethod.setAccessible(true);
         } catch (NoSuchMethodException var4) {
            Log.i("ViewGroupUtilsApi14", "Failed to access cancel method by reflection");
         }

         sCancelMethodFetched = true;
      }

      if (sCancelMethod != null) {
         try {
            sCancelMethod.invoke(var0);
         } catch (IllegalAccessException var2) {
            Log.i("ViewGroupUtilsApi14", "Failed to access cancel method by reflection");
         } catch (InvocationTargetException var3) {
            Log.i("ViewGroupUtilsApi14", "Failed to invoke cancel method by reflection");
         }
      }

   }

   static void suppressLayout(ViewGroup var0, boolean var1) {
      LayoutTransition var2 = sEmptyLayoutTransition;
      boolean var3 = false;
      boolean var4 = false;
      if (var2 == null) {
         sEmptyLayoutTransition = new LayoutTransition() {
            public boolean isChangingLayout() {
               return true;
            }
         };
         sEmptyLayoutTransition.setAnimator(2, (Animator)null);
         sEmptyLayoutTransition.setAnimator(0, (Animator)null);
         sEmptyLayoutTransition.setAnimator(1, (Animator)null);
         sEmptyLayoutTransition.setAnimator(3, (Animator)null);
         sEmptyLayoutTransition.setAnimator(4, (Animator)null);
      }

      if (var1) {
         var2 = var0.getLayoutTransition();
         if (var2 != null) {
            if (var2.isRunning()) {
               cancelLayoutTransition(var2);
            }

            if (var2 != sEmptyLayoutTransition) {
               var0.setTag(R.id.transition_layout_save, var2);
            }
         }

         var0.setLayoutTransition(sEmptyLayoutTransition);
      } else {
         var0.setLayoutTransition((LayoutTransition)null);
         if (!sLayoutSuppressedFieldFetched) {
            try {
               sLayoutSuppressedField = ViewGroup.class.getDeclaredField("mLayoutSuppressed");
               sLayoutSuppressedField.setAccessible(true);
            } catch (NoSuchFieldException var5) {
               Log.i("ViewGroupUtilsApi14", "Failed to access mLayoutSuppressed field by reflection");
            }

            sLayoutSuppressedFieldFetched = true;
         }

         var1 = var3;
         if (sLayoutSuppressedField != null) {
            label69: {
               label68: {
                  try {
                     var1 = sLayoutSuppressedField.getBoolean(var0);
                  } catch (IllegalAccessException var7) {
                     var1 = var4;
                     break label68;
                  }

                  if (!var1) {
                     break label69;
                  }

                  try {
                     sLayoutSuppressedField.setBoolean(var0, false);
                     break label69;
                  } catch (IllegalAccessException var6) {
                  }
               }

               Log.i("ViewGroupUtilsApi14", "Failed to get mLayoutSuppressed field by reflection");
            }
         }

         if (var1) {
            var0.requestLayout();
         }

         var2 = (LayoutTransition)var0.getTag(R.id.transition_layout_save);
         if (var2 != null) {
            var0.setTag(R.id.transition_layout_save, (Object)null);
            var0.setLayoutTransition(var2);
         }
      }

   }
}
