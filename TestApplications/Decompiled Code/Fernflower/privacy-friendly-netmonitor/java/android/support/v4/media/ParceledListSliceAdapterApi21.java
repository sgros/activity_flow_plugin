package android.support.v4.media;

import android.support.annotation.RequiresApi;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

@RequiresApi(21)
class ParceledListSliceAdapterApi21 {
   private static Constructor sConstructor;

   static {
      try {
         sConstructor = Class.forName("android.content.pm.ParceledListSlice").getConstructor(List.class);
      } catch (NoSuchMethodException | ClassNotFoundException var1) {
         var1.printStackTrace();
      }

   }

   static Object newInstance(List var0) {
      Object var2;
      try {
         var2 = sConstructor.newInstance(var0);
      } catch (IllegalAccessException | InvocationTargetException | InstantiationException var1) {
         var1.printStackTrace();
         var2 = null;
      }

      return var2;
   }
}
