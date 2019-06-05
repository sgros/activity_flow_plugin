package android.support.transition;

import android.util.Log;
import android.view.ViewGroup;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

class ViewGroupUtilsApi18 {
   private static Method sSuppressLayoutMethod;
   private static boolean sSuppressLayoutMethodFetched;

   private static void fetchSuppressLayoutMethod() {
      if (!sSuppressLayoutMethodFetched) {
         try {
            sSuppressLayoutMethod = ViewGroup.class.getDeclaredMethod("suppressLayout", Boolean.TYPE);
            sSuppressLayoutMethod.setAccessible(true);
         } catch (NoSuchMethodException var1) {
            Log.i("ViewUtilsApi18", "Failed to retrieve suppressLayout method", var1);
         }

         sSuppressLayoutMethodFetched = true;
      }

   }

   static void suppressLayout(ViewGroup var0, boolean var1) {
      fetchSuppressLayoutMethod();
      if (sSuppressLayoutMethod != null) {
         try {
            sSuppressLayoutMethod.invoke(var0, var1);
         } catch (IllegalAccessException var2) {
            Log.i("ViewUtilsApi18", "Failed to invoke suppressLayout method", var2);
         } catch (InvocationTargetException var3) {
            Log.i("ViewUtilsApi18", "Error invoking suppressLayout method", var3);
         }
      }

   }
}
