package android.support.v7.app;

import android.content.res.Resources;
import android.os.Build.VERSION;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.util.LongSparseArray;
import java.lang.reflect.Field;
import java.util.Map;

class ResourcesFlusher {
   private static final String TAG = "ResourcesFlusher";
   private static Field sDrawableCacheField;
   private static boolean sDrawableCacheFieldFetched;
   private static Field sResourcesImplField;
   private static boolean sResourcesImplFieldFetched;
   private static Class sThemedResourceCacheClazz;
   private static boolean sThemedResourceCacheClazzFetched;
   private static Field sThemedResourceCache_mUnthemedEntriesField;
   private static boolean sThemedResourceCache_mUnthemedEntriesFieldFetched;

   static boolean flush(@NonNull Resources var0) {
      if (VERSION.SDK_INT >= 24) {
         return flushNougats(var0);
      } else if (VERSION.SDK_INT >= 23) {
         return flushMarshmallows(var0);
      } else {
         return VERSION.SDK_INT >= 21 ? flushLollipops(var0) : false;
      }
   }

   @RequiresApi(21)
   private static boolean flushLollipops(@NonNull Resources var0) {
      if (!sDrawableCacheFieldFetched) {
         try {
            sDrawableCacheField = Resources.class.getDeclaredField("mDrawableCache");
            sDrawableCacheField.setAccessible(true);
         } catch (NoSuchFieldException var3) {
            Log.e("ResourcesFlusher", "Could not retrieve Resources#mDrawableCache field", var3);
         }

         sDrawableCacheFieldFetched = true;
      }

      if (sDrawableCacheField != null) {
         Map var4;
         try {
            var4 = (Map)sDrawableCacheField.get(var0);
         } catch (IllegalAccessException var2) {
            Log.e("ResourcesFlusher", "Could not retrieve value from Resources#mDrawableCache", var2);
            var4 = null;
         }

         if (var4 != null) {
            var4.clear();
            return true;
         }
      }

      return false;
   }

   @RequiresApi(23)
   private static boolean flushMarshmallows(@NonNull Resources var0) {
      if (!sDrawableCacheFieldFetched) {
         try {
            sDrawableCacheField = Resources.class.getDeclaredField("mDrawableCache");
            sDrawableCacheField.setAccessible(true);
         } catch (NoSuchFieldException var4) {
            Log.e("ResourcesFlusher", "Could not retrieve Resources#mDrawableCache field", var4);
         }

         sDrawableCacheFieldFetched = true;
      }

      Object var6;
      label35: {
         if (sDrawableCacheField != null) {
            try {
               var6 = sDrawableCacheField.get(var0);
               break label35;
            } catch (IllegalAccessException var5) {
               Log.e("ResourcesFlusher", "Could not retrieve value from Resources#mDrawableCache", var5);
            }
         }

         var6 = null;
      }

      boolean var2 = false;
      if (var6 == null) {
         return false;
      } else {
         boolean var3 = var2;
         if (var6 != null) {
            var3 = var2;
            if (flushThemedResourcesCache(var6)) {
               var3 = true;
            }
         }

         return var3;
      }
   }

   @RequiresApi(24)
   private static boolean flushNougats(@NonNull Resources var0) {
      boolean var1 = sResourcesImplFieldFetched;
      boolean var2 = true;
      if (!var1) {
         try {
            sResourcesImplField = Resources.class.getDeclaredField("mResourcesImpl");
            sResourcesImplField.setAccessible(true);
         } catch (NoSuchFieldException var6) {
            Log.e("ResourcesFlusher", "Could not retrieve Resources#mResourcesImpl field", var6);
         }

         sResourcesImplFieldFetched = true;
      }

      if (sResourcesImplField == null) {
         return false;
      } else {
         Object var8;
         try {
            var8 = sResourcesImplField.get(var0);
         } catch (IllegalAccessException var5) {
            Log.e("ResourcesFlusher", "Could not retrieve value from Resources#mResourcesImpl", var5);
            var8 = null;
         }

         if (var8 == null) {
            return false;
         } else {
            if (!sDrawableCacheFieldFetched) {
               try {
                  sDrawableCacheField = var8.getClass().getDeclaredField("mDrawableCache");
                  sDrawableCacheField.setAccessible(true);
               } catch (NoSuchFieldException var4) {
                  Log.e("ResourcesFlusher", "Could not retrieve ResourcesImpl#mDrawableCache field", var4);
               }

               sDrawableCacheFieldFetched = true;
            }

            label52: {
               if (sDrawableCacheField != null) {
                  try {
                     var8 = sDrawableCacheField.get(var8);
                     break label52;
                  } catch (IllegalAccessException var7) {
                     Log.e("ResourcesFlusher", "Could not retrieve value from ResourcesImpl#mDrawableCache", var7);
                  }
               }

               var8 = null;
            }

            if (var8 == null || !flushThemedResourcesCache(var8)) {
               var2 = false;
            }

            return var2;
         }
      }
   }

   @RequiresApi(16)
   private static boolean flushThemedResourcesCache(@NonNull Object var0) {
      if (!sThemedResourceCacheClazzFetched) {
         try {
            sThemedResourceCacheClazz = Class.forName("android.content.res.ThemedResourceCache");
         } catch (ClassNotFoundException var4) {
            Log.e("ResourcesFlusher", "Could not find ThemedResourceCache class", var4);
         }

         sThemedResourceCacheClazzFetched = true;
      }

      if (sThemedResourceCacheClazz == null) {
         return false;
      } else {
         if (!sThemedResourceCache_mUnthemedEntriesFieldFetched) {
            try {
               sThemedResourceCache_mUnthemedEntriesField = sThemedResourceCacheClazz.getDeclaredField("mUnthemedEntries");
               sThemedResourceCache_mUnthemedEntriesField.setAccessible(true);
            } catch (NoSuchFieldException var3) {
               Log.e("ResourcesFlusher", "Could not retrieve ThemedResourceCache#mUnthemedEntries field", var3);
            }

            sThemedResourceCache_mUnthemedEntriesFieldFetched = true;
         }

         if (sThemedResourceCache_mUnthemedEntriesField == null) {
            return false;
         } else {
            LongSparseArray var5;
            try {
               var5 = (LongSparseArray)sThemedResourceCache_mUnthemedEntriesField.get(var0);
            } catch (IllegalAccessException var2) {
               Log.e("ResourcesFlusher", "Could not retrieve value from ThemedResourceCache#mUnthemedEntries", var2);
               var5 = null;
            }

            if (var5 != null) {
               var5.clear();
               return true;
            } else {
               return false;
            }
         }
      }
   }
}
