package android.support.v7.app;

import android.content.res.Resources;
import android.os.Build.VERSION;
import android.util.Log;
import android.util.LongSparseArray;
import java.lang.reflect.Field;
import java.util.Map;

class ResourcesFlusher {
   private static Field sDrawableCacheField;
   private static boolean sDrawableCacheFieldFetched;
   private static Field sResourcesImplField;
   private static boolean sResourcesImplFieldFetched;
   private static Class sThemedResourceCacheClazz;
   private static boolean sThemedResourceCacheClazzFetched;
   private static Field sThemedResourceCache_mUnthemedEntriesField;
   private static boolean sThemedResourceCache_mUnthemedEntriesFieldFetched;

   static void flush(Resources var0) {
      if (VERSION.SDK_INT < 28) {
         if (VERSION.SDK_INT >= 24) {
            flushNougats(var0);
         } else if (VERSION.SDK_INT >= 23) {
            flushMarshmallows(var0);
         } else if (VERSION.SDK_INT >= 21) {
            flushLollipops(var0);
         }

      }
   }

   private static void flushLollipops(Resources var0) {
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
         }
      }

   }

   private static void flushMarshmallows(Resources var0) {
      if (!sDrawableCacheFieldFetched) {
         try {
            sDrawableCacheField = Resources.class.getDeclaredField("mDrawableCache");
            sDrawableCacheField.setAccessible(true);
         } catch (NoSuchFieldException var2) {
            Log.e("ResourcesFlusher", "Could not retrieve Resources#mDrawableCache field", var2);
         }

         sDrawableCacheFieldFetched = true;
      }

      Object var4;
      label29: {
         if (sDrawableCacheField != null) {
            try {
               var4 = sDrawableCacheField.get(var0);
               break label29;
            } catch (IllegalAccessException var3) {
               Log.e("ResourcesFlusher", "Could not retrieve value from Resources#mDrawableCache", var3);
            }
         }

         var4 = null;
      }

      if (var4 != null) {
         flushThemedResourcesCache(var4);
      }
   }

   private static void flushNougats(Resources var0) {
      if (!sResourcesImplFieldFetched) {
         try {
            sResourcesImplField = Resources.class.getDeclaredField("mResourcesImpl");
            sResourcesImplField.setAccessible(true);
         } catch (NoSuchFieldException var4) {
            Log.e("ResourcesFlusher", "Could not retrieve Resources#mResourcesImpl field", var4);
         }

         sResourcesImplFieldFetched = true;
      }

      if (sResourcesImplField != null) {
         Object var6;
         try {
            var6 = sResourcesImplField.get(var0);
         } catch (IllegalAccessException var3) {
            Log.e("ResourcesFlusher", "Could not retrieve value from Resources#mResourcesImpl", var3);
            var6 = null;
         }

         if (var6 != null) {
            if (!sDrawableCacheFieldFetched) {
               try {
                  sDrawableCacheField = var6.getClass().getDeclaredField("mDrawableCache");
                  sDrawableCacheField.setAccessible(true);
               } catch (NoSuchFieldException var2) {
                  Log.e("ResourcesFlusher", "Could not retrieve ResourcesImpl#mDrawableCache field", var2);
               }

               sDrawableCacheFieldFetched = true;
            }

            label47: {
               if (sDrawableCacheField != null) {
                  try {
                     var6 = sDrawableCacheField.get(var6);
                     break label47;
                  } catch (IllegalAccessException var5) {
                     Log.e("ResourcesFlusher", "Could not retrieve value from ResourcesImpl#mDrawableCache", var5);
                  }
               }

               var6 = null;
            }

            if (var6 != null) {
               flushThemedResourcesCache(var6);
            }

         }
      }
   }

   private static void flushThemedResourcesCache(Object var0) {
      if (!sThemedResourceCacheClazzFetched) {
         try {
            sThemedResourceCacheClazz = Class.forName("android.content.res.ThemedResourceCache");
         } catch (ClassNotFoundException var4) {
            Log.e("ResourcesFlusher", "Could not find ThemedResourceCache class", var4);
         }

         sThemedResourceCacheClazzFetched = true;
      }

      if (sThemedResourceCacheClazz != null) {
         if (!sThemedResourceCache_mUnthemedEntriesFieldFetched) {
            try {
               sThemedResourceCache_mUnthemedEntriesField = sThemedResourceCacheClazz.getDeclaredField("mUnthemedEntries");
               sThemedResourceCache_mUnthemedEntriesField.setAccessible(true);
            } catch (NoSuchFieldException var3) {
               Log.e("ResourcesFlusher", "Could not retrieve ThemedResourceCache#mUnthemedEntries field", var3);
            }

            sThemedResourceCache_mUnthemedEntriesFieldFetched = true;
         }

         if (sThemedResourceCache_mUnthemedEntriesField != null) {
            LongSparseArray var5;
            try {
               var5 = (LongSparseArray)sThemedResourceCache_mUnthemedEntriesField.get(var0);
            } catch (IllegalAccessException var2) {
               Log.e("ResourcesFlusher", "Could not retrieve value from ThemedResourceCache#mUnthemedEntries", var2);
               var5 = null;
            }

            if (var5 != null) {
               var5.clear();
            }

         }
      }
   }
}
