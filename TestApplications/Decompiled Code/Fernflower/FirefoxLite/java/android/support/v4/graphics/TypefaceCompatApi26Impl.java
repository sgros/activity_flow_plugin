package android.support.v4.graphics;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.graphics.fonts.FontVariationAxis;
import android.os.CancellationSignal;
import android.support.v4.content.res.FontResourcesParserCompat;
import android.support.v4.provider.FontsContractCompat;
import android.util.Log;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;

public class TypefaceCompatApi26Impl extends TypefaceCompatApi21Impl {
   protected final Method mAbortCreation;
   protected final Method mAddFontFromAssetManager;
   protected final Method mAddFontFromBuffer;
   protected final Method mCreateFromFamiliesWithDefault;
   protected final Class mFontFamily;
   protected final Constructor mFontFamilyCtor;
   protected final Method mFreeze;

   public TypefaceCompatApi26Impl() {
      Object var1 = null;

      Constructor var3;
      Object var4;
      Object var5;
      Object var6;
      Object var7;
      Object var8;
      Class var10;
      try {
         var10 = this.obtainFontFamily();
         var3 = this.obtainFontFamilyCtor(var10);
         var4 = this.obtainAddFontFromAssetManagerMethod(var10);
         var5 = this.obtainAddFontFromBufferMethod(var10);
         var6 = this.obtainFreezeMethod(var10);
         var7 = this.obtainAbortCreationMethod(var10);
         var8 = this.obtainCreateFromFamiliesWithDefaultMethod(var10);
      } catch (NoSuchMethodException | ClassNotFoundException var9) {
         StringBuilder var2 = new StringBuilder();
         var2.append("Unable to collect necessary methods for class ");
         var2.append(var9.getClass().getName());
         Log.e("TypefaceCompatApi26Impl", var2.toString(), var9);
         var3 = null;
         var7 = var3;
         var8 = var3;
         var6 = var3;
         var5 = var3;
         var4 = var3;
         var10 = (Class)var1;
      }

      this.mFontFamily = var10;
      this.mFontFamilyCtor = var3;
      this.mAddFontFromAssetManager = (Method)var4;
      this.mAddFontFromBuffer = (Method)var5;
      this.mFreeze = (Method)var6;
      this.mAbortCreation = (Method)var7;
      this.mCreateFromFamiliesWithDefault = (Method)var8;
   }

   private void abortCreation(Object var1) {
      try {
         this.mAbortCreation.invoke(var1);
      } catch (InvocationTargetException | IllegalAccessException var2) {
         throw new RuntimeException(var2);
      }
   }

   private boolean addFontFromAssetManager(Context var1, Object var2, String var3, int var4, int var5, int var6, FontVariationAxis[] var7) {
      try {
         boolean var8 = (Boolean)this.mAddFontFromAssetManager.invoke(var2, var1.getAssets(), var3, 0, false, var4, var5, var6, var7);
         return var8;
      } catch (InvocationTargetException | IllegalAccessException var9) {
         throw new RuntimeException(var9);
      }
   }

   private boolean addFontFromBuffer(Object var1, ByteBuffer var2, int var3, int var4, int var5) {
      try {
         boolean var6 = (Boolean)this.mAddFontFromBuffer.invoke(var1, var2, var3, null, var4, var5);
         return var6;
      } catch (InvocationTargetException | IllegalAccessException var7) {
         throw new RuntimeException(var7);
      }
   }

   private boolean freeze(Object var1) {
      try {
         boolean var2 = (Boolean)this.mFreeze.invoke(var1);
         return var2;
      } catch (InvocationTargetException | IllegalAccessException var3) {
         throw new RuntimeException(var3);
      }
   }

   private boolean isFontFamilyPrivateAPIAvailable() {
      if (this.mAddFontFromAssetManager == null) {
         Log.w("TypefaceCompatApi26Impl", "Unable to collect necessary private methods. Fallback to legacy implementation.");
      }

      boolean var1;
      if (this.mAddFontFromAssetManager != null) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   private Object newFamily() {
      try {
         Object var1 = this.mFontFamilyCtor.newInstance();
         return var1;
      } catch (InstantiationException | InvocationTargetException | IllegalAccessException var2) {
         throw new RuntimeException(var2);
      }
   }

   protected Typeface createFromFamiliesWithDefault(Object var1) {
      try {
         Object var2 = Array.newInstance(this.mFontFamily, 1);
         Array.set(var2, 0, var1);
         Typeface var4 = (Typeface)this.mCreateFromFamiliesWithDefault.invoke((Object)null, var2, -1, -1);
         return var4;
      } catch (InvocationTargetException | IllegalAccessException var3) {
         throw new RuntimeException(var3);
      }
   }

   public Typeface createFromFontFamilyFilesResourceEntry(Context var1, FontResourcesParserCompat.FontFamilyFilesResourceEntry var2, Resources var3, int var4) {
      if (!this.isFontFamilyPrivateAPIAvailable()) {
         return super.createFromFontFamilyFilesResourceEntry(var1, var2, var3, var4);
      } else {
         Object var8 = this.newFamily();
         FontResourcesParserCompat.FontFileResourceEntry[] var7 = var2.getEntries();
         int var5 = var7.length;

         for(var4 = 0; var4 < var5; ++var4) {
            FontResourcesParserCompat.FontFileResourceEntry var6 = var7[var4];
            if (!this.addFontFromAssetManager(var1, var8, var6.getFileName(), var6.getTtcIndex(), var6.getWeight(), var6.isItalic(), FontVariationAxis.fromFontVariationSettings(var6.getVariationSettings()))) {
               this.abortCreation(var8);
               return null;
            }
         }

         if (!this.freeze(var8)) {
            return null;
         } else {
            return this.createFromFamiliesWithDefault(var8);
         }
      }
   }

   public Typeface createFromFontInfo(Context param1, CancellationSignal param2, FontsContractCompat.FontInfo[] param3, int param4) {
      // $FF: Couldn't be decompiled
   }

   public Typeface createFromResourcesFontFile(Context var1, Resources var2, int var3, String var4, int var5) {
      if (!this.isFontFamilyPrivateAPIAvailable()) {
         return super.createFromResourcesFontFile(var1, var2, var3, var4, var5);
      } else {
         Object var6 = this.newFamily();
         if (!this.addFontFromAssetManager(var1, var6, var4, 0, -1, -1, (FontVariationAxis[])null)) {
            this.abortCreation(var6);
            return null;
         } else {
            return !this.freeze(var6) ? null : this.createFromFamiliesWithDefault(var6);
         }
      }
   }

   protected Method obtainAbortCreationMethod(Class var1) throws NoSuchMethodException {
      return var1.getMethod("abortCreation");
   }

   protected Method obtainAddFontFromAssetManagerMethod(Class var1) throws NoSuchMethodException {
      return var1.getMethod("addFontFromAssetManager", AssetManager.class, String.class, Integer.TYPE, Boolean.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE, FontVariationAxis[].class);
   }

   protected Method obtainAddFontFromBufferMethod(Class var1) throws NoSuchMethodException {
      return var1.getMethod("addFontFromBuffer", ByteBuffer.class, Integer.TYPE, FontVariationAxis[].class, Integer.TYPE, Integer.TYPE);
   }

   protected Method obtainCreateFromFamiliesWithDefaultMethod(Class var1) throws NoSuchMethodException {
      Method var2 = Typeface.class.getDeclaredMethod("createFromFamiliesWithDefault", Array.newInstance(var1, 1).getClass(), Integer.TYPE, Integer.TYPE);
      var2.setAccessible(true);
      return var2;
   }

   protected Class obtainFontFamily() throws ClassNotFoundException {
      return Class.forName("android.graphics.FontFamily");
   }

   protected Constructor obtainFontFamilyCtor(Class var1) throws NoSuchMethodException {
      return var1.getConstructor();
   }

   protected Method obtainFreezeMethod(Class var1) throws NoSuchMethodException {
      return var1.getMethod("freeze");
   }
}
