package android.support.transition;

import android.animation.Animator;
import android.animation.LayoutTransition;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.ViewGroup;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@RequiresApi(14)
class ViewGroupUtilsApi14 implements ViewGroupUtilsImpl {
   private static final int LAYOUT_TRANSITION_CHANGING = 4;
   private static final String TAG = "ViewGroupUtilsApi14";
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

   public ViewGroupOverlayImpl getOverlay(@NonNull ViewGroup var1) {
      return ViewGroupOverlayApi14.createFrom(var1);
   }

   public void suppressLayout(@NonNull ViewGroup var1, boolean var2) {
      LayoutTransition var3 = sEmptyLayoutTransition;
      boolean var4 = false;
      boolean var5 = false;
      if (var3 == null) {
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

      if (var2) {
         var3 = var1.getLayoutTransition();
         if (var3 != null) {
            if (var3.isRunning()) {
               cancelLayoutTransition(var3);
            }

            if (var3 != sEmptyLayoutTransition) {
               var1.setTag(R.id.transition_layout_save, var3);
            }
         }

         var1.setLayoutTransition(sEmptyLayoutTransition);
      } else {
         var1.setLayoutTransition((LayoutTransition)null);
         if (!sLayoutSuppressedFieldFetched) {
            try {
               sLayoutSuppressedField = ViewGroup.class.getDeclaredField("mLayoutSuppressed");
               sLayoutSuppressedField.setAccessible(true);
            } catch (NoSuchFieldException var6) {
               Log.i("ViewGroupUtilsApi14", "Failed to access mLayoutSuppressed field by reflection");
            }

            sLayoutSuppressedFieldFetched = true;
         }

         var2 = var4;
         if (sLayoutSuppressedField != null) {
            label69: {
               label68: {
                  try {
                     var2 = sLayoutSuppressedField.getBoolean(var1);
                  } catch (IllegalAccessException var8) {
                     var2 = var5;
                     break label68;
                  }

                  if (!var2) {
                     break label69;
                  }

                  try {
                     sLayoutSuppressedField.setBoolean(var1, false);
                     break label69;
                  } catch (IllegalAccessException var7) {
                  }
               }

               Log.i("ViewGroupUtilsApi14", "Failed to get mLayoutSuppressed field by reflection");
            }
         }

         if (var2) {
            var1.requestLayout();
         }

         var3 = (LayoutTransition)var1.getTag(R.id.transition_layout_save);
         if (var3 != null) {
            var1.setTag(R.id.transition_layout_save, (Object)null);
            var1.setLayoutTransition(var3);
         }
      }

   }
}
