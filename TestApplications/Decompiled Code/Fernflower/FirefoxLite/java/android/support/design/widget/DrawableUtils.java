package android.support.design.widget;

import android.graphics.drawable.DrawableContainer;
import android.graphics.drawable.Drawable.ConstantState;
import android.graphics.drawable.DrawableContainer.DrawableContainerState;
import android.util.Log;
import java.lang.reflect.Method;

public class DrawableUtils {
   private static Method setConstantStateMethod;
   private static boolean setConstantStateMethodFetched;

   public static boolean setContainerConstantState(DrawableContainer var0, ConstantState var1) {
      return setContainerConstantStateV9(var0, var1);
   }

   private static boolean setContainerConstantStateV9(DrawableContainer var0, ConstantState var1) {
      if (!setConstantStateMethodFetched) {
         try {
            setConstantStateMethod = DrawableContainer.class.getDeclaredMethod("setConstantState", DrawableContainerState.class);
            setConstantStateMethod.setAccessible(true);
         } catch (NoSuchMethodException var3) {
            Log.e("DrawableUtils", "Could not fetch setConstantState(). Oh well.");
         }

         setConstantStateMethodFetched = true;
      }

      if (setConstantStateMethod != null) {
         try {
            setConstantStateMethod.invoke(var0, var1);
            return true;
         } catch (Exception var4) {
            Log.e("DrawableUtils", "Could not invoke setConstantState(). Oh well.");
         }
      }

      return false;
   }
}
