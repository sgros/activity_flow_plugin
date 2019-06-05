package android.support.v4.view;

import android.os.Build.VERSION;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.LayoutInflater.Factory;
import android.view.LayoutInflater.Factory2;
import java.lang.reflect.Field;

public final class LayoutInflaterCompat {
   private static boolean sCheckedField;
   private static Field sLayoutInflaterFactory2Field;

   private static void forceSetFactory2(LayoutInflater var0, Factory2 var1) {
      if (!sCheckedField) {
         try {
            sLayoutInflaterFactory2Field = LayoutInflater.class.getDeclaredField("mFactory2");
            sLayoutInflaterFactory2Field.setAccessible(true);
         } catch (NoSuchFieldException var5) {
            StringBuilder var3 = new StringBuilder();
            var3.append("forceSetFactory2 Could not find field 'mFactory2' on class ");
            var3.append(LayoutInflater.class.getName());
            var3.append("; inflation may have unexpected results.");
            Log.e("LayoutInflaterCompatHC", var3.toString(), var5);
         }

         sCheckedField = true;
      }

      if (sLayoutInflaterFactory2Field != null) {
         try {
            sLayoutInflaterFactory2Field.set(var0, var1);
         } catch (IllegalAccessException var4) {
            StringBuilder var6 = new StringBuilder();
            var6.append("forceSetFactory2 could not set the Factory2 on LayoutInflater ");
            var6.append(var0);
            var6.append("; inflation may have unexpected results.");
            Log.e("LayoutInflaterCompatHC", var6.toString(), var4);
         }
      }

   }

   public static void setFactory2(LayoutInflater var0, Factory2 var1) {
      var0.setFactory2(var1);
      if (VERSION.SDK_INT < 21) {
         Factory var2 = var0.getFactory();
         if (var2 instanceof Factory2) {
            forceSetFactory2(var0, (Factory2)var2);
         } else {
            forceSetFactory2(var0, var1);
         }
      }

   }
}
