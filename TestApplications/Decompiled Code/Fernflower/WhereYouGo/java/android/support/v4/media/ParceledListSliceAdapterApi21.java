package android.support.v4.media;

import android.annotation.TargetApi;
import android.support.annotation.RequiresApi;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

@TargetApi(21)
@RequiresApi(21)
class ParceledListSliceAdapterApi21 {
   private static Constructor sConstructor;

   static {
      label14: {
         Object var0;
         try {
            sConstructor = Class.forName("android.content.pm.ParceledListSlice").getConstructor(List.class);
            break label14;
         } catch (ClassNotFoundException var1) {
            var0 = var1;
         } catch (NoSuchMethodException var2) {
            var0 = var2;
         }

         ((ReflectiveOperationException)var0).printStackTrace();
      }

   }

   static Object newInstance(List var0) {
      Object var1 = null;

      Object var5;
      try {
         var5 = sConstructor.newInstance(var0);
         return var5;
      } catch (InstantiationException var2) {
         var5 = var2;
      } catch (IllegalAccessException var3) {
         var5 = var3;
      } catch (InvocationTargetException var4) {
         var5 = var4;
      }

      ((ReflectiveOperationException)var5).printStackTrace();
      var5 = var1;
      return var5;
   }
}
