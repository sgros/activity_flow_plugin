package android.support.v4.graphics;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.graphics.fonts.FontVariationAxis;
import android.os.CancellationSignal;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.annotation.RestrictTo;
import android.support.v4.content.res.FontResourcesParserCompat;
import android.support.v4.provider.FontsContractCompat;
import android.util.Log;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;

@RequiresApi(26)
@RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
public class TypefaceCompatApi26Impl extends TypefaceCompatApi21Impl {
   private static final String ABORT_CREATION_METHOD = "abortCreation";
   private static final String ADD_FONT_FROM_ASSET_MANAGER_METHOD = "addFontFromAssetManager";
   private static final String ADD_FONT_FROM_BUFFER_METHOD = "addFontFromBuffer";
   private static final String CREATE_FROM_FAMILIES_WITH_DEFAULT_METHOD = "createFromFamiliesWithDefault";
   private static final String FONT_FAMILY_CLASS = "android.graphics.FontFamily";
   private static final String FREEZE_METHOD = "freeze";
   private static final int RESOLVE_BY_FONT_TABLE = -1;
   private static final String TAG = "TypefaceCompatApi26Impl";
   private static final Method sAbortCreation;
   private static final Method sAddFontFromAssetManager;
   private static final Method sAddFontFromBuffer;
   private static final Method sCreateFromFamiliesWithDefault;
   private static final Class sFontFamily;
   private static final Constructor sFontFamilyCtor;
   private static final Method sFreeze;

   static {
      Object var0 = null;

      Class var1;
      Method var3;
      Method var4;
      Method var5;
      Method var6;
      Method var7;
      Constructor var10;
      try {
         var1 = Class.forName("android.graphics.FontFamily");
         var10 = var1.getConstructor();
         var3 = var1.getMethod("addFontFromAssetManager", AssetManager.class, String.class, Integer.TYPE, Boolean.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE, FontVariationAxis[].class);
         var4 = var1.getMethod("addFontFromBuffer", ByteBuffer.class, Integer.TYPE, FontVariationAxis[].class, Integer.TYPE, Integer.TYPE);
         var5 = var1.getMethod("freeze");
         var6 = var1.getMethod("abortCreation");
         var7 = Typeface.class.getDeclaredMethod("createFromFamiliesWithDefault", Array.newInstance(var1, 1).getClass(), Integer.TYPE, Integer.TYPE);
         var7.setAccessible(true);
      } catch (NoSuchMethodException | ClassNotFoundException var9) {
         StringBuilder var2 = new StringBuilder();
         var2.append("Unable to collect necessary methods for class ");
         var2.append(var9.getClass().getName());
         Log.e("TypefaceCompatApi26Impl", var2.toString(), var9);
         Object var8 = null;
         var6 = (Method)var8;
         var5 = (Method)var8;
         var4 = (Method)var8;
         var3 = (Method)var8;
         var7 = (Method)var8;
         var1 = (Class)var8;
         var10 = (Constructor)var0;
      }

      sFontFamilyCtor = var10;
      sFontFamily = var1;
      sAddFontFromAssetManager = var3;
      sAddFontFromBuffer = var4;
      sFreeze = var5;
      sAbortCreation = var6;
      sCreateFromFamiliesWithDefault = var7;
   }

   private static boolean abortCreation(Object var0) {
      try {
         boolean var1 = (Boolean)sAbortCreation.invoke(var0);
         return var1;
      } catch (InvocationTargetException | IllegalAccessException var2) {
         throw new RuntimeException(var2);
      }
   }

   private static boolean addFontFromAssetManager(Context var0, Object var1, String var2, int var3, int var4, int var5) {
      try {
         boolean var6 = (Boolean)sAddFontFromAssetManager.invoke(var1, var0.getAssets(), var2, 0, false, var3, var4, var5, null);
         return var6;
      } catch (InvocationTargetException | IllegalAccessException var7) {
         throw new RuntimeException(var7);
      }
   }

   private static boolean addFontFromBuffer(Object var0, ByteBuffer var1, int var2, int var3, int var4) {
      try {
         boolean var5 = (Boolean)sAddFontFromBuffer.invoke(var0, var1, var2, null, var3, var4);
         return var5;
      } catch (InvocationTargetException | IllegalAccessException var6) {
         throw new RuntimeException(var6);
      }
   }

   private static Typeface createFromFamiliesWithDefault(Object var0) {
      try {
         Object var1 = Array.newInstance(sFontFamily, 1);
         Array.set(var1, 0, var0);
         Typeface var3 = (Typeface)sCreateFromFamiliesWithDefault.invoke((Object)null, var1, -1, -1);
         return var3;
      } catch (InvocationTargetException | IllegalAccessException var2) {
         throw new RuntimeException(var2);
      }
   }

   private static boolean freeze(Object var0) {
      try {
         boolean var1 = (Boolean)sFreeze.invoke(var0);
         return var1;
      } catch (InvocationTargetException | IllegalAccessException var2) {
         throw new RuntimeException(var2);
      }
   }

   private static boolean isFontFamilyPrivateAPIAvailable() {
      if (sAddFontFromAssetManager == null) {
         Log.w("TypefaceCompatApi26Impl", "Unable to collect necessary private methods.Fallback to legacy implementation.");
      }

      boolean var0;
      if (sAddFontFromAssetManager != null) {
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
      if (!isFontFamilyPrivateAPIAvailable()) {
         return super.createFromFontFamilyFilesResourceEntry(var1, var2, var3, var4);
      } else {
         Object var8 = newFamily();
         FontResourcesParserCompat.FontFileResourceEntry[] var7 = var2.getEntries();
         int var5 = var7.length;

         for(var4 = 0; var4 < var5; ++var4) {
            FontResourcesParserCompat.FontFileResourceEntry var6 = var7[var4];
            if (!addFontFromAssetManager(var1, var8, var6.getFileName(), 0, var6.getWeight(), var6.isItalic())) {
               abortCreation(var8);
               return null;
            }
         }

         if (!freeze(var8)) {
            return null;
         } else {
            return createFromFamiliesWithDefault(var8);
         }
      }
   }

   public Typeface createFromFontInfo(Context param1, @Nullable CancellationSignal param2, @NonNull FontsContractCompat.FontInfo[] param3, int param4) {
      // $FF: Couldn't be decompiled
   }

   @Nullable
   public Typeface createFromResourcesFontFile(Context var1, Resources var2, int var3, String var4, int var5) {
      if (!isFontFamilyPrivateAPIAvailable()) {
         return super.createFromResourcesFontFile(var1, var2, var3, var4, var5);
      } else {
         Object var6 = newFamily();
         if (!addFontFromAssetManager(var1, var6, var4, 0, -1, -1)) {
            abortCreation(var6);
            return null;
         } else {
            return !freeze(var6) ? null : createFromFamiliesWithDefault(var6);
         }
      }
   }
}
