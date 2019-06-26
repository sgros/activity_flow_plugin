package androidx.core.graphics;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.graphics.Typeface.Builder;
import android.graphics.fonts.FontVariationAxis;
import android.os.CancellationSignal;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import androidx.core.content.res.FontResourcesParserCompat;
import androidx.core.provider.FontsContractCompat;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.util.Map;

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
      Method var4;
      Method var5;
      Method var6;
      Method var7;
      Class var12;
      Method var13;
      label16: {
         Object var2;
         try {
            var12 = this.obtainFontFamily();
            var3 = this.obtainFontFamilyCtor(var12);
            var4 = this.obtainAddFontFromAssetManagerMethod(var12);
            var5 = this.obtainAddFontFromBufferMethod(var12);
            var6 = this.obtainFreezeMethod(var12);
            var7 = this.obtainAbortCreationMethod(var12);
            var13 = this.obtainCreateFromFamiliesWithDefaultMethod(var12);
            break label16;
         } catch (ClassNotFoundException var10) {
            var2 = var10;
         } catch (NoSuchMethodException var11) {
            var2 = var11;
         }

         StringBuilder var8 = new StringBuilder();
         var8.append("Unable to collect necessary methods for class ");
         var8.append(var2.getClass().getName());
         Log.e("TypefaceCompatApi26Impl", var8.toString(), (Throwable)var2);
         Object var9 = null;
         var6 = (Method)var9;
         var7 = (Method)var9;
         var5 = (Method)var9;
         var4 = (Method)var9;
         var3 = (Constructor)var9;
         var13 = (Method)var9;
         var12 = (Class)var1;
      }

      this.mFontFamily = var12;
      this.mFontFamilyCtor = var3;
      this.mAddFontFromAssetManager = var4;
      this.mAddFontFromBuffer = var5;
      this.mFreeze = var6;
      this.mAbortCreation = var7;
      this.mCreateFromFamiliesWithDefault = var13;
   }

   private void abortCreation(Object var1) {
      try {
         this.mAbortCreation.invoke(var1);
      } catch (InvocationTargetException | IllegalAccessException var2) {
      }

   }

   private boolean addFontFromAssetManager(Context var1, Object var2, String var3, int var4, int var5, int var6, FontVariationAxis[] var7) {
      try {
         boolean var8 = (Boolean)this.mAddFontFromAssetManager.invoke(var2, var1.getAssets(), var3, 0, false, var4, var5, var6, var7);
         return var8;
      } catch (InvocationTargetException | IllegalAccessException var9) {
         return false;
      }
   }

   private boolean addFontFromBuffer(Object var1, ByteBuffer var2, int var3, int var4, int var5) {
      try {
         boolean var6 = (Boolean)this.mAddFontFromBuffer.invoke(var1, var2, var3, null, var4, var5);
         return var6;
      } catch (InvocationTargetException | IllegalAccessException var7) {
         return false;
      }
   }

   private boolean freeze(Object var1) {
      try {
         boolean var2 = (Boolean)this.mFreeze.invoke(var1);
         return var2;
      } catch (InvocationTargetException | IllegalAccessException var3) {
         return false;
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
         return null;
      }
   }

   protected Typeface createFromFamiliesWithDefault(Object var1) {
      try {
         Object var2 = Array.newInstance(this.mFontFamily, 1);
         Array.set(var2, 0, var1);
         Typeface var4 = (Typeface)this.mCreateFromFamiliesWithDefault.invoke((Object)null, var2, -1, -1);
         return var4;
      } catch (InvocationTargetException | IllegalAccessException var3) {
         return null;
      }
   }

   public Typeface createFromFontFamilyFilesResourceEntry(Context var1, FontResourcesParserCompat.FontFamilyFilesResourceEntry var2, Resources var3, int var4) {
      if (!this.isFontFamilyPrivateAPIAvailable()) {
         return super.createFromFontFamilyFilesResourceEntry(var1, var2, var3, var4);
      } else {
         Object var8 = this.newFamily();
         if (var8 == null) {
            return null;
         } else {
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
   }

   public Typeface createFromFontInfo(Context var1, CancellationSignal var2, FontsContractCompat.FontInfo[] var3, int var4) {
      if (var3.length < 1) {
         return null;
      } else {
         Typeface var38;
         if (!this.isFontFamilyPrivateAPIAvailable()) {
            FontsContractCompat.FontInfo var44 = this.findBestInfo(var3, var4);
            ContentResolver var39 = var1.getContentResolver();

            ParcelFileDescriptor var43;
            boolean var10001;
            try {
               var43 = var39.openFileDescriptor(var44.getUri(), "r", var2);
            } catch (IOException var36) {
               var10001 = false;
               return null;
            }

            if (var43 == null) {
               if (var43 != null) {
                  try {
                     var43.close();
                  } catch (IOException var32) {
                     var10001 = false;
                     return null;
                  }
               }

               return null;
            } else {
               label319: {
                  Throwable var42;
                  try {
                     Builder var41 = new Builder(var43.getFileDescriptor());
                     var38 = var41.setWeight(var44.getWeight()).setItalic(var44.isItalic()).build();
                     break label319;
                  } catch (Throwable var34) {
                     var42 = var34;
                  } finally {
                     ;
                  }

                  try {
                     throw var42;
                  } finally {
                     if (var43 != null) {
                        if (var42 != null) {
                           try {
                              var43.close();
                           } catch (Throwable var28) {
                           }
                        } else {
                           try {
                              var43.close();
                           } catch (IOException var30) {
                              var10001 = false;
                              return null;
                           }
                        }
                     }

                     try {
                        throw var39;
                     } catch (IOException var29) {
                        var10001 = false;
                        return null;
                     }
                  }
               }

               if (var43 != null) {
                  try {
                     var43.close();
                  } catch (IOException var33) {
                     var10001 = false;
                     return null;
                  }
               }

               return var38;
            }
         } else {
            Map var37 = FontsContractCompat.prepareFontData(var1, var3, var2);
            Object var5 = this.newFamily();
            if (var5 == null) {
               return null;
            } else {
               int var6 = var3.length;
               boolean var7 = false;

               for(int var8 = 0; var8 < var6; ++var8) {
                  FontsContractCompat.FontInfo var40 = var3[var8];
                  ByteBuffer var9 = (ByteBuffer)var37.get(var40.getUri());
                  if (var9 != null) {
                     if (!this.addFontFromBuffer(var5, var9, var40.getTtcIndex(), var40.getWeight(), var40.isItalic())) {
                        this.abortCreation(var5);
                        return null;
                     }

                     var7 = true;
                  }
               }

               if (!var7) {
                  this.abortCreation(var5);
                  return null;
               } else if (!this.freeze(var5)) {
                  return null;
               } else {
                  var38 = this.createFromFamiliesWithDefault(var5);
                  if (var38 == null) {
                     return null;
                  } else {
                     return Typeface.create(var38, var4);
                  }
               }
            }
         }
      }
   }

   public Typeface createFromResourcesFontFile(Context var1, Resources var2, int var3, String var4, int var5) {
      if (!this.isFontFamilyPrivateAPIAvailable()) {
         return super.createFromResourcesFontFile(var1, var2, var3, var4, var5);
      } else {
         Object var6 = this.newFamily();
         if (var6 == null) {
            return null;
         } else if (!this.addFontFromAssetManager(var1, var6, var4, 0, -1, -1, (FontVariationAxis[])null)) {
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
      Class var2 = Integer.TYPE;
      Class var3 = Boolean.TYPE;
      Class var4 = Integer.TYPE;
      return var1.getMethod("addFontFromAssetManager", AssetManager.class, String.class, var2, var3, var4, var4, var4, FontVariationAxis[].class);
   }

   protected Method obtainAddFontFromBufferMethod(Class var1) throws NoSuchMethodException {
      Class var2 = Integer.TYPE;
      return var1.getMethod("addFontFromBuffer", ByteBuffer.class, var2, FontVariationAxis[].class, var2, var2);
   }

   protected Method obtainCreateFromFamiliesWithDefaultMethod(Class var1) throws NoSuchMethodException {
      var1 = Array.newInstance(var1, 1).getClass();
      Class var2 = Integer.TYPE;
      Method var3 = Typeface.class.getDeclaredMethod("createFromFamiliesWithDefault", var1, var2, var2);
      var3.setAccessible(true);
      return var3;
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
