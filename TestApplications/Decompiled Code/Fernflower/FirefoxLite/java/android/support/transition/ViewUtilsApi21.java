package android.support.transition;

import android.graphics.Matrix;
import android.util.Log;
import android.view.View;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

class ViewUtilsApi21 extends ViewUtilsApi19 {
   private static Method sTransformMatrixToGlobalMethod;
   private static boolean sTransformMatrixToGlobalMethodFetched;
   private static Method sTransformMatrixToLocalMethod;
   private static boolean sTransformMatrixToLocalMethodFetched;

   private void fetchTransformMatrixToGlobalMethod() {
      if (!sTransformMatrixToGlobalMethodFetched) {
         try {
            sTransformMatrixToGlobalMethod = View.class.getDeclaredMethod("transformMatrixToGlobal", Matrix.class);
            sTransformMatrixToGlobalMethod.setAccessible(true);
         } catch (NoSuchMethodException var2) {
            Log.i("ViewUtilsApi21", "Failed to retrieve transformMatrixToGlobal method", var2);
         }

         sTransformMatrixToGlobalMethodFetched = true;
      }

   }

   private void fetchTransformMatrixToLocalMethod() {
      if (!sTransformMatrixToLocalMethodFetched) {
         try {
            sTransformMatrixToLocalMethod = View.class.getDeclaredMethod("transformMatrixToLocal", Matrix.class);
            sTransformMatrixToLocalMethod.setAccessible(true);
         } catch (NoSuchMethodException var2) {
            Log.i("ViewUtilsApi21", "Failed to retrieve transformMatrixToLocal method", var2);
         }

         sTransformMatrixToLocalMethodFetched = true;
      }

   }

   public void transformMatrixToGlobal(View var1, Matrix var2) {
      this.fetchTransformMatrixToGlobalMethod();
      if (sTransformMatrixToGlobalMethod != null) {
         try {
            sTransformMatrixToGlobalMethod.invoke(var1, var2);
         } catch (IllegalAccessException var3) {
         } catch (InvocationTargetException var4) {
            throw new RuntimeException(var4.getCause());
         }
      }

   }

   public void transformMatrixToLocal(View var1, Matrix var2) {
      this.fetchTransformMatrixToLocalMethod();
      if (sTransformMatrixToLocalMethod != null) {
         try {
            sTransformMatrixToLocalMethod.invoke(var1, var2);
         } catch (IllegalAccessException var3) {
         } catch (InvocationTargetException var4) {
            throw new RuntimeException(var4.getCause());
         }
      }

   }
}
