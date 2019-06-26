package androidx.core.graphics;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.CancellationSignal;
import android.util.Log;
import androidx.collection.SimpleArrayMap;
import androidx.core.content.res.FontResourcesParserCompat;
import androidx.core.provider.FontsContractCompat;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.util.List;

class TypefaceCompatApi24Impl extends TypefaceCompatBaseImpl {
   private static final Method sAddFontWeightStyle;
   private static final Method sCreateFromFamiliesWithDefault;
   private static final Class sFontFamily;
   private static final Constructor sFontFamilyCtor;

   static {
      Constructor var0 = null;

      Class var1;
      Object var3;
      Object var4;
      label18: {
         Constructor var2;
         label17: {
            try {
               var1 = Class.forName("android.graphics.FontFamily");
               var2 = var1.getConstructor();
               var3 = var1.getMethod("addFontWeightStyle", ByteBuffer.class, Integer.TYPE, List.class, Integer.TYPE, Boolean.TYPE);
               var4 = Typeface.class.getMethod("createFromFamiliesWithDefault", Array.newInstance(var1, 1).getClass());
               break label17;
            } catch (ClassNotFoundException var5) {
               var4 = var5;
            } catch (NoSuchMethodException var6) {
               var4 = var6;
            }

            Log.e("TypefaceCompatApi24Impl", var4.getClass().getName(), (Throwable)var4);
            var1 = null;
            var4 = var1;
            var3 = var1;
            break label18;
         }

         var0 = var2;
      }

      sFontFamilyCtor = var0;
      sFontFamily = var1;
      sAddFontWeightStyle = (Method)var3;
      sCreateFromFamiliesWithDefault = (Method)var4;
   }

   private static boolean addFontWeightStyle(Object var0, ByteBuffer var1, int var2, int var3, boolean var4) {
      try {
         var4 = (Boolean)sAddFontWeightStyle.invoke(var0, var1, var2, null, var3, var4);
         return var4;
      } catch (InvocationTargetException | IllegalAccessException var5) {
         return false;
      }
   }

   private static Typeface createFromFamiliesWithDefault(Object var0) {
      try {
         Object var1 = Array.newInstance(sFontFamily, 1);
         Array.set(var1, 0, var0);
         Typeface var3 = (Typeface)sCreateFromFamiliesWithDefault.invoke((Object)null, var1);
         return var3;
      } catch (InvocationTargetException | IllegalAccessException var2) {
         return null;
      }
   }

   public static boolean isUsable() {
      if (sAddFontWeightStyle == null) {
         Log.w("TypefaceCompatApi24Impl", "Unable to collect necessary private methods.Fallback to legacy implementation.");
      }

      boolean var0;
      if (sAddFontWeightStyle != null) {
         var0 = true;
      } else {
         var0 = false;
      }

      return var0;
   }

   private static Object newFamily() {
      try {
         Object var0 = sFontFamilyCtor.newInstance();
         return var0;
      } catch (InstantiationException | InvocationTargetException | IllegalAccessException var1) {
         return null;
      }
   }

   public Typeface createFromFontFamilyFilesResourceEntry(Context var1, FontResourcesParserCompat.FontFamilyFilesResourceEntry var2, Resources var3, int var4) {
      Object var5 = newFamily();
      if (var5 == null) {
         return null;
      } else {
         FontResourcesParserCompat.FontFileResourceEntry[] var6 = var2.getEntries();
         int var7 = var6.length;

         for(var4 = 0; var4 < var7; ++var4) {
            FontResourcesParserCompat.FontFileResourceEntry var8 = var6[var4];
            ByteBuffer var9 = TypefaceCompatUtil.copyToDirectBuffer(var1, var3, var8.getResourceId());
            if (var9 == null) {
               return null;
            }

            if (!addFontWeightStyle(var5, var9, var8.getTtcIndex(), var8.getWeight(), var8.isItalic())) {
               return null;
            }
         }

         return createFromFamiliesWithDefault(var5);
      }
   }

   public Typeface createFromFontInfo(Context var1, CancellationSignal var2, FontsContractCompat.FontInfo[] var3, int var4) {
      Object var5 = newFamily();
      if (var5 == null) {
         return null;
      } else {
         SimpleArrayMap var6 = new SimpleArrayMap();
         int var7 = var3.length;

         for(int var8 = 0; var8 < var7; ++var8) {
            FontsContractCompat.FontInfo var9 = var3[var8];
            Uri var10 = var9.getUri();
            ByteBuffer var11 = (ByteBuffer)var6.get(var10);
            ByteBuffer var12 = var11;
            if (var11 == null) {
               var12 = TypefaceCompatUtil.mmap(var1, var2, var10);
               var6.put(var10, var12);
            }

            if (var12 == null) {
               return null;
            }

            if (!addFontWeightStyle(var5, var12, var9.getTtcIndex(), var9.getWeight(), var9.isItalic())) {
               return null;
            }
         }

         Typeface var13 = createFromFamiliesWithDefault(var5);
         if (var13 == null) {
            return null;
         } else {
            return Typeface.create(var13, var4);
         }
      }
   }
}
