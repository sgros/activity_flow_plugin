package androidx.core.graphics;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.CancellationSignal;
import android.os.ParcelFileDescriptor;
import android.system.ErrnoException;
import android.system.Os;
import android.system.OsConstants;
import android.util.Log;
import androidx.core.content.res.FontResourcesParserCompat;
import androidx.core.provider.FontsContractCompat;
import java.io.File;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

class TypefaceCompatApi21Impl extends TypefaceCompatBaseImpl {
   private static Method sAddFontWeightStyle;
   private static Method sCreateFromFamiliesWithDefault;
   private static Class sFontFamily;
   private static Constructor sFontFamilyCtor;
   private static boolean sHasInitBeenCalled;

   private static boolean addFontWeightStyle(Object var0, String var1, int var2, boolean var3) {
      init();

      try {
         var3 = (Boolean)sAddFontWeightStyle.invoke(var0, var1, var2, var3);
         return var3;
      } catch (IllegalAccessException var4) {
         var0 = var4;
      } catch (InvocationTargetException var5) {
         var0 = var5;
      }

      throw new RuntimeException((Throwable)var0);
   }

   private static Typeface createFromFamiliesWithDefault(Object var0) {
      init();

      try {
         Object var1 = Array.newInstance(sFontFamily, 1);
         Array.set(var1, 0, var0);
         Typeface var4 = (Typeface)sCreateFromFamiliesWithDefault.invoke((Object)null, var1);
         return var4;
      } catch (IllegalAccessException var2) {
         var0 = var2;
      } catch (InvocationTargetException var3) {
         var0 = var3;
      }

      throw new RuntimeException((Throwable)var0);
   }

   private File getFile(ParcelFileDescriptor var1) {
      try {
         StringBuilder var2 = new StringBuilder();
         var2.append("/proc/self/fd/");
         var2.append(var1.getFd());
         String var4 = Os.readlink(var2.toString());
         if (OsConstants.S_ISREG(Os.stat(var4).st_mode)) {
            File var5 = new File(var4);
            return var5;
         }
      } catch (ErrnoException var3) {
      }

      return null;
   }

   private static void init() {
      if (!sHasInitBeenCalled) {
         sHasInitBeenCalled = true;
         Object var0 = null;

         Object var1;
         Method var3;
         Method var4;
         Constructor var7;
         label18: {
            Object var2;
            try {
               var1 = Class.forName("android.graphics.FontFamily");
               var7 = ((Class)var1).getConstructor();
               var3 = ((Class)var1).getMethod("addFontWeightStyle", String.class, Integer.TYPE, Boolean.TYPE);
               var4 = Typeface.class.getMethod("createFromFamiliesWithDefault", Array.newInstance((Class)var1, 1).getClass());
               break label18;
            } catch (ClassNotFoundException var5) {
               var2 = var5;
            } catch (NoSuchMethodException var6) {
               var2 = var6;
            }

            Log.e("TypefaceCompatApi21Impl", var2.getClass().getName(), (Throwable)var2);
            var4 = null;
            var3 = var4;
            var1 = var4;
            var7 = (Constructor)var0;
         }

         sFontFamilyCtor = var7;
         sFontFamily = (Class)var1;
         sAddFontWeightStyle = var3;
         sCreateFromFamiliesWithDefault = var4;
      }
   }

   private static Object newFamily() {
      init();

      Object var0;
      try {
         var0 = sFontFamilyCtor.newInstance();
         return var0;
      } catch (IllegalAccessException var1) {
         var0 = var1;
      } catch (InstantiationException var2) {
         var0 = var2;
      } catch (InvocationTargetException var3) {
         var0 = var3;
      }

      throw new RuntimeException((Throwable)var0);
   }

   public Typeface createFromFontFamilyFilesResourceEntry(Context param1, FontResourcesParserCompat.FontFamilyFilesResourceEntry param2, Resources param3, int param4) {
      // $FF: Couldn't be decompiled
   }

   public Typeface createFromFontInfo(Context param1, CancellationSignal param2, FontsContractCompat.FontInfo[] param3, int param4) {
      // $FF: Couldn't be decompiled
   }
}
