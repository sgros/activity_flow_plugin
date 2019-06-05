package com.bumptech.glide.load.model;

import android.support.v4.util.Pools;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ModelLoaderRegistry {
   private final ModelLoaderRegistry.ModelLoaderCache cache;
   private final MultiModelLoaderFactory multiModelLoaderFactory;

   public ModelLoaderRegistry(Pools.Pool var1) {
      this(new MultiModelLoaderFactory(var1));
   }

   ModelLoaderRegistry(MultiModelLoaderFactory var1) {
      this.cache = new ModelLoaderRegistry.ModelLoaderCache();
      this.multiModelLoaderFactory = var1;
   }

   private static Class getClass(Object var0) {
      return var0.getClass();
   }

   private List getModelLoadersForClass(Class var1) {
      List var2 = this.cache.get(var1);
      List var3 = var2;
      if (var2 == null) {
         var3 = Collections.unmodifiableList(this.multiModelLoaderFactory.build(var1));
         this.cache.put(var1, var3);
      }

      return var3;
   }

   public void append(Class var1, Class var2, ModelLoaderFactory var3) {
      synchronized(this){}

      try {
         this.multiModelLoaderFactory.append(var1, var2, var3);
         this.cache.clear();
      } finally {
         ;
      }

   }

   public List getDataClasses(Class var1) {
      synchronized(this){}

      List var4;
      try {
         var4 = this.multiModelLoaderFactory.getDataClasses(var1);
      } finally {
         ;
      }

      return var4;
   }

   public List getModelLoaders(Object var1) {
      synchronized(this){}

      Throwable var10000;
      label101: {
         boolean var10001;
         List var2;
         int var3;
         ArrayList var4;
         try {
            var2 = this.getModelLoadersForClass(getClass(var1));
            var3 = var2.size();
            var4 = new ArrayList(var3);
         } catch (Throwable var12) {
            var10000 = var12;
            var10001 = false;
            break label101;
         }

         int var5 = 0;

         while(true) {
            if (var5 >= var3) {
               return var4;
            }

            try {
               ModelLoader var6 = (ModelLoader)var2.get(var5);
               if (var6.handles(var1)) {
                  var4.add(var6);
               }
            } catch (Throwable var11) {
               var10000 = var11;
               var10001 = false;
               break;
            }

            ++var5;
         }
      }

      Throwable var13 = var10000;
      throw var13;
   }

   public void prepend(Class var1, Class var2, ModelLoaderFactory var3) {
      synchronized(this){}

      try {
         this.multiModelLoaderFactory.prepend(var1, var2, var3);
         this.cache.clear();
      } finally {
         ;
      }

   }

   private static class ModelLoaderCache {
      private final Map cachedModelLoaders = new HashMap();

      ModelLoaderCache() {
      }

      public void clear() {
         this.cachedModelLoaders.clear();
      }

      public List get(Class var1) {
         ModelLoaderRegistry.ModelLoaderCache.Entry var2 = (ModelLoaderRegistry.ModelLoaderCache.Entry)this.cachedModelLoaders.get(var1);
         List var3;
         if (var2 == null) {
            var3 = null;
         } else {
            var3 = var2.loaders;
         }

         return var3;
      }

      public void put(Class var1, List var2) {
         if ((ModelLoaderRegistry.ModelLoaderCache.Entry)this.cachedModelLoaders.put(var1, new ModelLoaderRegistry.ModelLoaderCache.Entry(var2)) != null) {
            StringBuilder var3 = new StringBuilder();
            var3.append("Already cached loaders for model: ");
            var3.append(var1);
            throw new IllegalStateException(var3.toString());
         }
      }

      private static class Entry {
         final List loaders;

         public Entry(List var1) {
            this.loaders = var1;
         }
      }
   }
}
