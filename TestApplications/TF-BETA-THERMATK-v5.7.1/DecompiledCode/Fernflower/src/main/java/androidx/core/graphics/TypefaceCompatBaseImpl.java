package androidx.core.graphics;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.CancellationSignal;
import android.util.Log;
import androidx.core.content.res.FontResourcesParserCompat;
import androidx.core.provider.FontsContractCompat;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.concurrent.ConcurrentHashMap;

class TypefaceCompatBaseImpl {
   private ConcurrentHashMap mFontFamilies = new ConcurrentHashMap();

   private void addFontFamily(Typeface var1, FontResourcesParserCompat.FontFamilyFilesResourceEntry var2) {
      long var3 = getUniqueKey(var1);
      if (var3 != 0L) {
         this.mFontFamilies.put(var3, var2);
      }

   }

   private FontResourcesParserCompat.FontFileResourceEntry findBestEntry(FontResourcesParserCompat.FontFamilyFilesResourceEntry var1, int var2) {
      return (FontResourcesParserCompat.FontFileResourceEntry)findBestFont(var1.getEntries(), var2, new TypefaceCompatBaseImpl.StyleExtractor() {
         public int getWeight(FontResourcesParserCompat.FontFileResourceEntry var1) {
            return var1.getWeight();
         }

         public boolean isItalic(FontResourcesParserCompat.FontFileResourceEntry var1) {
            return var1.isItalic();
         }
      });
   }

   private static Object findBestFont(Object[] var0, int var1, TypefaceCompatBaseImpl.StyleExtractor var2) {
      short var3;
      if ((var1 & 1) == 0) {
         var3 = 400;
      } else {
         var3 = 700;
      }

      boolean var4;
      if ((var1 & 2) != 0) {
         var4 = true;
      } else {
         var4 = false;
      }

      int var5 = var0.length;
      Object var6 = null;
      int var7 = 0;

      int var11;
      for(var1 = Integer.MAX_VALUE; var7 < var5; var1 = var11) {
         Object var8 = var0[var7];
         int var9 = Math.abs(var2.getWeight(var8) - var3);
         byte var10;
         if (var2.isItalic(var8) == var4) {
            var10 = 0;
         } else {
            var10 = 1;
         }

         label30: {
            var9 = var9 * 2 + var10;
            if (var6 != null) {
               var11 = var1;
               if (var1 <= var9) {
                  break label30;
               }
            }

            var6 = var8;
            var11 = var9;
         }

         ++var7;
      }

      return var6;
   }

   private static long getUniqueKey(Typeface var0) {
      if (var0 == null) {
         return 0L;
      } else {
         try {
            Field var1 = Typeface.class.getDeclaredField("native_instance");
            var1.setAccessible(true);
            long var2 = ((Number)var1.get(var0)).longValue();
            return var2;
         } catch (NoSuchFieldException var4) {
            Log.e("TypefaceCompatBaseImpl", "Could not retrieve font from family.", var4);
            return 0L;
         } catch (IllegalAccessException var5) {
            Log.e("TypefaceCompatBaseImpl", "Could not retrieve font from family.", var5);
            return 0L;
         }
      }
   }

   public Typeface createFromFontFamilyFilesResourceEntry(Context var1, FontResourcesParserCompat.FontFamilyFilesResourceEntry var2, Resources var3, int var4) {
      FontResourcesParserCompat.FontFileResourceEntry var5 = this.findBestEntry(var2, var4);
      if (var5 == null) {
         return null;
      } else {
         Typeface var6 = TypefaceCompat.createFromResourcesFontFile(var1, var3, var5.getResourceId(), var5.getFileName(), var4);
         this.addFontFamily(var6, var2);
         return var6;
      }
   }

   public Typeface createFromFontInfo(Context var1, CancellationSignal var2, FontsContractCompat.FontInfo[] var3, int var4) {
      int var5 = var3.length;
      Object var6 = null;
      if (var5 < 1) {
         return null;
      } else {
         FontsContractCompat.FontInfo var19 = this.findBestInfo(var3, var4);

         label105: {
            label104: {
               InputStream var20;
               label103: {
                  try {
                     var20 = ((Context)var1).getContentResolver().openInputStream(var19.getUri());
                     break label103;
                  } catch (IOException var17) {
                  } finally {
                     ;
                  }

                  var2 = null;
                  break label104;
               }

               try {
                  var1 = this.createFromInputStream((Context)var1, var20);
                  break label105;
               } catch (IOException var15) {
                  var1 = var15;
               } finally {
                  TypefaceCompatUtil.closeQuietly(var20);
                  throw var1;
               }
            }

            TypefaceCompatUtil.closeQuietly(var2);
            return null;
         }

      }
   }

   protected Typeface createFromInputStream(Context param1, InputStream param2) {
      // $FF: Couldn't be decompiled
   }

   public Typeface createFromResourcesFontFile(Context param1, Resources param2, int param3, String param4, int param5) {
      // $FF: Couldn't be decompiled
   }

   protected FontsContractCompat.FontInfo findBestInfo(FontsContractCompat.FontInfo[] var1, int var2) {
      return (FontsContractCompat.FontInfo)findBestFont(var1, var2, new TypefaceCompatBaseImpl.StyleExtractor() {
         public int getWeight(FontsContractCompat.FontInfo var1) {
            return var1.getWeight();
         }

         public boolean isItalic(FontsContractCompat.FontInfo var1) {
            return var1.isItalic();
         }
      });
   }

   private interface StyleExtractor {
      int getWeight(Object var1);

      boolean isItalic(Object var1);
   }
}
