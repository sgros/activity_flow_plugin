package android.support.transition;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.View;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

class ViewUtilsApi22 extends ViewUtilsApi21 {
   private static Method sSetLeftTopRightBottomMethod;
   private static boolean sSetLeftTopRightBottomMethodFetched;

   @SuppressLint({"PrivateApi"})
   private void fetchSetLeftTopRightBottomMethod() {
      if (!sSetLeftTopRightBottomMethodFetched) {
         try {
            sSetLeftTopRightBottomMethod = View.class.getDeclaredMethod("setLeftTopRightBottom", Integer.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE);
            sSetLeftTopRightBottomMethod.setAccessible(true);
         } catch (NoSuchMethodException var2) {
            Log.i("ViewUtilsApi22", "Failed to retrieve setLeftTopRightBottom method", var2);
         }

         sSetLeftTopRightBottomMethodFetched = true;
      }

   }

   public void setLeftTopRightBottom(View var1, int var2, int var3, int var4, int var5) {
      this.fetchSetLeftTopRightBottomMethod();
      if (sSetLeftTopRightBottomMethod != null) {
         try {
            sSetLeftTopRightBottomMethod.invoke(var1, var2, var3, var4, var5);
         } catch (IllegalAccessException var6) {
         } catch (InvocationTargetException var7) {
            throw new RuntimeException(var7.getCause());
         }
      }

   }
}
