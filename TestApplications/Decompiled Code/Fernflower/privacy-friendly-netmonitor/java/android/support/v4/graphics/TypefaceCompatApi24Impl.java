package android.support.v4.graphics;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.CancellationSignal;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.annotation.RestrictTo;
import android.support.v4.content.res.FontResourcesParserCompat;
import android.support.v4.provider.FontsContractCompat;
import android.support.v4.util.SimpleArrayMap;
import android.util.Log;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.util.List;

@RequiresApi(24)
@RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
class TypefaceCompatApi24Impl extends TypefaceCompatBaseImpl {
   private static final String ADD_FONT_WEIGHT_STYLE_METHOD = "addFontWeightStyle";
   private static final String CREATE_FROM_FAMILIES_WITH_DEFAULT_METHOD = "createFromFamiliesWithDefault";
   private static final String FONT_FAMILY_CLASS = "android.graphics.FontFamily";
   private static final String TAG = "TypefaceCompatApi24Impl";
   private static final Method sAddFontWeightStyle;
   private static final Method sCreateFromFamiliesWithDefault;
   private static final Class sFontFamily;
   private static final Constructor sFontFamilyCtor;

   static {
      Object var0 = null;

      Class var1;
      Constructor var2;
      Method var3;
      Method var4;
      try {
         var1 = Class.forName("android.graphics.FontFamily");
         var2 = var1.getConstructor();
         var3 = var1.getMethod("addFontWeightStyle", ByteBuffer.class, Integer.TYPE, List.class, Integer.TYPE, Boolean.TYPE);
         var4 = Typeface.class.getMethod("createFromFamiliesWithDefault", Array.newInstance(var1, 1).getClass());
      } catch (NoSuchMethodException | ClassNotFoundException var5) {
         Log.e("TypefaceCompatApi24Impl", var5.getClass().getName(), var5);
         var2 = null;
         var3 = var2;
         var4 = var2;
         var1 = var2;
         var2 = (Constructor)var0;
      }

      sFontFamilyCtor = var2;
      sFontFamily = var1;
      sAddFontWeightStyle = var3;
      sCreateFromFamiliesWithDefault = var4;
   }

   private static boolean addFontWeightStyle(Object var0, ByteBuffer var1, int var2, int var3, boolean var4) {
      try {
         var4 = (Boolean)sAddFontWeightStyle.invoke(var0, var1, var2, null, var3, var4);
         return var4;
      } catch (InvocationTargetException | IllegalAccessException var5) {
         throw new RuntimeException(var5);
      }
   }

   private static Typeface createFromFamiliesWithDefault(Object var0) {
      try {
         Object var1 = Array.newInstance(sFontFamily, 1);
         Array.set(var1, 0, var0);
         Typeface var3 = (Typeface)sCreateFromFamiliesWithDefault.invoke((Object)null, var1);
         return var3;
      } catch (InvocationTargetException | IllegalAccessException var2) {
         throw new RuntimeException(var2);
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
         throw new RuntimeException(var1);
      }
   }

   public Typeface createFromFontFamilyFilesResourceEntry(Context var1, FontResourcesParserCompat.FontFamilyFilesResourceEntry var2, Resources var3, int var4) {
      Object var5 = newFamily();
      FontResourcesParserCompat.FontFileResourceEntry[] var6 = var2.getEntries();
      int var7 = var6.length;

      for(var4 = 0; var4 < var7; ++var4) {
         FontResourcesParserCompat.FontFileResourceEntry var8 = var6[var4];
         if (!addFontWeightStyle(var5, TypefaceCompatUtil.copyToDirectBuffer(var1, var3, var8.getResourceId()), 0, var8.getWeight(), var8.isItalic())) {
            return null;
         }
      }

      return createFromFamiliesWithDefault(var5);
   }

   public Typeface createFromFontInfo(Context var1, @Nullable CancellationSignal var2, @NonNull FontsContractCompat.FontInfo[] var3, int var4) {
      Object var5 = newFamily();
      SimpleArrayMap var6 = new SimpleArrayMap();
      var4 = 0;

      for(int var7 = var3.length; var4 < var7; ++var4) {
         FontsContractCompat.FontInfo var8 = var3[var4];
         Uri var9 = var8.getUri();
         ByteBuffer var10 = (ByteBuffer)var6.get(var9);
         ByteBuffer var11 = var10;
         if (var10 == null) {
            var11 = TypefaceCompatUtil.mmap(var1, var2, var9);
            var6.put(var9, var11);
         }

         if (!addFontWeightStyle(var5, var11, var8.getTtcIndex(), var8.getWeight(), var8.isItalic())) {
            return null;
         }
      }

      return createFromFamiliesWithDefault(var5);
   }
}
